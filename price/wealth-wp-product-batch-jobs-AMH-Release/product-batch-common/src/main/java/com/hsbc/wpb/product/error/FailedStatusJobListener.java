package com.dummy.wpb.product.error;

import com.dummy.wpb.product.constant.Field;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchConstants.PROCESSED_FILE_NAMES;
import static com.dummy.wpb.product.error.ErrorCodeExitStatus.FAILED;

public class FailedStatusJobListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExitStatus exitStatus = jobExecution.getExitStatus();

        if (!FAILED.equals(exitStatus.getExitCode())) {
            return;
        }

        String jobName = jobExecution.getJobInstance().getJobName();
        String params = getParamDesc(jobExecution.getJobParameters());
        String jobDesc = String.format(" Job: %s(%s).", jobName, params);

        StepExecution failedStep = jobExecution.getStepExecutions()
                .stream()
                .filter(se -> FAILED.equals(se.getExitStatus().getExitCode()))
                .findFirst()
                .orElse(null);

        ErrorCode defaultErrorCode = ErrorCode.OTPSERR_EBJ000;

        if (null != failedStep) {
            ExecutionContext executionContext = failedStep.getExecutionContext();

            Object object = executionContext.get(PROCESSED_FILE_NAMES);// mean the job has import file step
            if (object instanceof String[]) {
                String[] processedFileNames = (String[]) object;
                jobDesc = String.format(" Job: %s(%s), File: %s.", jobName, params, ArrayUtils.toString(processedFileNames));
                defaultErrorCode = ErrorCode.OTPSERR_EBJ101;
            }

            exitStatus = failedStep.getExitStatus();
        }

        if (exitStatus.getExitDescription().contains("OTPSERR")) {
            ErrorLogger.logErrorMsg(exitStatus.getExitDescription() + jobDesc);
            return;
        }

        ErrorLogger.logErrorMsg(defaultErrorCode + jobDesc);
    }

    private String getParamDesc(JobParameters jobParameters) {
        return Stream.of(Field.ctryRecCde, Field.grpMembrRecCde, "systemCde")
                .filter(jobParameters.getParameters()::containsKey)
                .map(jobParameters::getString)
                .collect(Collectors.joining(", "));
    }
}
