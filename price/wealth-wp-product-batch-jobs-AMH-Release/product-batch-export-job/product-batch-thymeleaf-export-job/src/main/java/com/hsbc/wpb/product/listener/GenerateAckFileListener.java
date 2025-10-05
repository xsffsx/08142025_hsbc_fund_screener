package com.dummy.wpb.product.listener;

import com.dummy.wpb.product.exception.FileResourceException;
import com.dummy.wpb.product.model.ExportAck;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.batch.core.ExitStatus.COMPLETED;

@Component
public class GenerateAckFileListener extends JobExecutionListenerSupport {

    @Value("#{exportRequest.ack}")
    ExportAck ack;

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExitStatus exitStatus = jobExecution.getExitStatus();

        if (!COMPLETED.equals(exitStatus) || !ack.isGenerate()) {
            return;
        }

        boolean hasInvalidRecord = jobExecution.getStepExecutions()
                .stream()
                .anyMatch(se -> se.getSkipCount() > 0);

        if (hasInvalidRecord) {
            return;
        }

        try {
            JobParameters jobParameters = jobExecution.getJobParameters();
            String outputPath = jobParameters.getString("outputPath");
            Files.createFile(Paths.get(outputPath, ack.getFileName()));
        } catch (Exception e) {
            throw new FileResourceException("Failed to generate ack file.", e);
        }
    }
}
