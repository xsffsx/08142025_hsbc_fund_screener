package com.dummy.wpb.product.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import static com.dummy.wpb.product.constant.JobExecutionContextKey.*;
import static com.dummy.wpb.product.constant.JobExecutionContextKey.UPDATED_COUNT;

@Component
@Slf4j
public class WrtsUndlStockStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put(READ_COUNT, 0);
        stepContext.put(FAILED_COUNT, 0);
        stepContext.put(SKIPPED_COUNT, 0);
        stepContext.put(UPDATED_COUNT, 0);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        int readCount = stepContext.getInt(READ_COUNT);
        int updatedCount = stepContext.getInt(UPDATED_COUNT);
        int failedCount = stepContext.getInt(FAILED_COUNT);
        int skippedCount = stepContext.getInt(SKIPPED_COUNT);
        stepExecution.setReadCount(readCount);
        stepExecution.setWriteCount(updatedCount);
        log.info("Total number of records: {}", readCount);
        log.info("                Updated: {}", updatedCount);
        log.info("                 Failed: {}", failedCount);
        log.info("                Skipped: {}", skippedCount);
        return stepExecution.getExitStatus();
    }
}
