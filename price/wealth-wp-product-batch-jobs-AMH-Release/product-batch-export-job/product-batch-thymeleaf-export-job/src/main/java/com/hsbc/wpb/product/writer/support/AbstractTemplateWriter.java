package com.dummy.wpb.product.writer.support;

import com.dummy.wpb.product.constant.Field;
import lombok.Setter;
import org.bson.Document;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
public abstract class AbstractTemplateWriter extends AbstractFileItemWriter<Document> implements StepAwareItemStreamWriter<Document> {

    protected String ctryRecCde;
    protected String grpMembrRecCde;
    protected String templateName;
    protected Date startTime;
    protected MongoTemplate mongoTemplate;
    protected Resource resource;
    protected Document params;
    protected Context thymeleafContext = new Context();

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        JobParameters jobParameters = stepExecution.getJobParameters();
        this.ctryRecCde = jobParameters.getString(Field.ctryRecCde);
        this.grpMembrRecCde = jobParameters.getString(Field.grpMembrRecCde);
        this.startTime = (Date) stepExecution.getJobExecution().getExecutionContext().get("startTime");

        List<String> files = (List<String>) executionContext.get("files");
        if (null == files) {
            files = new ArrayList<>();
        }
        files.add(resource.getFilename());
        executionContext.put("files", files);

        thymeleafContext.setVariable("startTime", startTime);
        params.forEach(thymeleafContext::setVariable);
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        thymeleafContext.setVariable("count", executionContext.get("count"));
        super.open(executionContext);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        super.setResource(resource);
    }
}
