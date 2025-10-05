package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.ExportFile;
import com.dummy.wpb.product.writer.support.AbstractTemplateWriter;
import com.dummy.wpb.product.writer.support.CompositeStepAwareItemStreamWriter;
import com.dummy.wpb.product.writer.support.StepAwareItemStreamWriter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriterFactoryBean implements FactoryBean<ItemStreamWriter<Document>>, BeanNameAware {

    @Setter
    List<ExportFile> files;

    @Value("#{jobParameters}")
    Map<String, Object> jobParameters;

    ItemStreamWriter<Document> writer;

    String name;

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("#{exportRequest.params}")
    Document params;

    @PostConstruct
    private void buildWriter() {
        List<ItemWriter<? super Document>> writers = new ArrayList<>();

        String outputPath = (String) jobParameters.get("outputPath");

        for (ExportFile file : files) {

            //todo: better way to handle this
            AbstractTemplateWriter templateWriter;
            if (StringUtils.isNotBlank(file.getClassName())) {
                try {
                    templateWriter = (AbstractTemplateWriter) Class.forName(file.getClassName()).getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new productBatchException("Failed to create reader instance: " + file.getClassName(), e);
                }
            } else {
                templateWriter = file.getFileName().endsWith("xml") ? new XmlTemplateWriter() : new CsvTemplateWriter();
            }

            templateWriter.setTemplateName(file.getTemplateName());
            templateWriter.setResource(new FileSystemResource(outputPath + File.separator + file.getFileName()));
            templateWriter.setName(name);
            templateWriter.setParams(params);
            templateWriter.setMongoTemplate(mongoTemplate);
            writers.add(templateWriter);
        }

        this.writer = new CompositeStepAwareItemStreamWriter<>(writers);
    }

    @Override
    public ItemStreamWriter<Document> getObject() {
        return writer;
    }

    @Override
    public Class<?> getObjectType() {
        return StepAwareItemStreamWriter.class;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }
}
