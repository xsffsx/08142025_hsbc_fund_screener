package com.dummy.wpb.product.error;

import com.dummy.wpb.product.constant.Field;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchConstants.PROCESSED_FILE_NAMES;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ110;

public class InvalidRecordAlertJobListener extends JobExecutionListenerSupport {
    @Override
    public void afterJob(JobExecution jobExecution) {
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            int skipCount = stepExecution.getSkipCount();
            if (skipCount > 0) {
                ExecutionContext executionContext = stepExecution.getExecutionContext();

                String message;
                Object object = executionContext.get(PROCESSED_FILE_NAMES);
                if (object instanceof String[]) {
                    message = String.format("Job: %s(%s), Step: %s, File: %s has %d invalid records.",
                            jobExecution.getJobInstance().getJobName(),
                            getParamDesc(jobExecution.getJobParameters()),
                            stepExecution.getStepName(),
                            ArrayUtils.toString(object),
                            skipCount);
                } else {
                    message = String.format("Job: %s(%s), Step: %s has %d invalid records.",
                            jobExecution.getJobInstance().getJobName(),
                            getParamDesc(jobExecution.getJobParameters()),
                            stepExecution.getStepName(),
                            skipCount);
                }
                ErrorLogger.logErrorMsg(OTPSERR_EBJ110 + " " + message);
            }
        }
    }

    private String getParamDesc(JobParameters jobParameters) {
        return Stream.of(Field.ctryRecCde, Field.grpMembrRecCde, "systemCde")
                .filter(jobParameters.getParameters()::containsKey)
                .map(jobParameters::getString)
                .collect(Collectors.joining(", "));
    }
}
