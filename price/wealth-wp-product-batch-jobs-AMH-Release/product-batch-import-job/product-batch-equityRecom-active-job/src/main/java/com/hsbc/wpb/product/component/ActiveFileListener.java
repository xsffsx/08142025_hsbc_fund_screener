package com.dummy.wpb.product.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@JobScope
public class ActiveFileListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("================================");
        log.info("Equity Recommendation active excel file job begin");
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        jobContext.put("failUpdate", 0);
        jobContext.put("prodNotFound", 0);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        int prodNotFoundNum = jobContext.getInt("prodNotFound");
        int failUpdateNum = jobContext.getInt("failUpdate");
        Optional<StepExecution> stepExecutionOpt = jobExecution.getStepExecutions().stream().findFirst();
        if (stepExecutionOpt.isPresent()) {
            StepExecution stepExecution = stepExecutionOpt.get();
            log.info("Equity Recommendation active excel file job ended");
            log.info("Total number of records:" + stepExecution.getReadCount());
            log.info("              processed:" + (stepExecution.getWriteCount() - failUpdateNum));
            log.info("                 failed:" + failUpdateNum);
            log.info("     coverage not found:" + (stepExecution.getFilterCount() - prodNotFoundNum));
            log.info("      product not found:" + prodNotFoundNum);
        }
    }
}
