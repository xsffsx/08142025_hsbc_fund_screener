package com.dummy.wpb.product.component;

import com.dummy.wpb.product.error.ErrorLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.dummy.wpb.product.FinDocReleaseJobApplication.JOB_NAME;
import static com.dummy.wpb.product.constant.BatchConstants.EXCEPTION_TITLE;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ109;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ112;

@Component
@Slf4j
@StepScope
public class FinDocReleaseTasklet implements Tasklet, StepExecutionListener {

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Autowired
    private FinDocReleaseService finDocReleaseService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("WPC Financial Document Release Job start");
        dispatchProcessor();
        log.info("WPC Financial Document Release Job end");

        return RepeatStatus.FINISHED;
    }

    private void dispatchProcessor() {

        long start = System.currentTimeMillis();

        try {

            boolean procRecHandleStatus = false;
            boolean procAprvHandleStatus = false;
            boolean releaseFilesAndUpdateDBStatus = false;

            //update the pending reject record
            procRecHandleStatus = finDocReleaseService.aprvRecHandle(ctryRecCde, grpMembrRecCde);

            //update the pending approval record
            procAprvHandleStatus = finDocReleaseService.procAprvHandle(ctryRecCde, grpMembrRecCde);

            //release files to S3 and update DB
            releaseFilesAndUpdateDBStatus = finDocReleaseService.releaseFilesAndUpdateDB(ctryRecCde, grpMembrRecCde);

            boolean overallStatus = procRecHandleStatus && procAprvHandleStatus && releaseFilesAndUpdateDBStatus;
            logResult(overallStatus);
        } catch (Exception e) {
            log.info(EXCEPTION_TITLE + e.getClass() + ":" + e.getMessage());
            log.info("Release process failed -- Return code 9999");
            ErrorLogger.logErrorMsg(OTPSERR_EBJ112, JOB_NAME, e.getStackTrace().getClass() + ":" + e.getMessage());
        }
        long end = System.currentTimeMillis();
        log.info("Processing Time: " + (end - start) + "ms");

    }


    private void logResult(boolean rtrnCde) {
        if (rtrnCde) {
            log.info("Release process completed.");
        } else {
            log.info(OTPSERR_EBJ109 + " Release process completed with errors.");
            ErrorLogger.logErrorMsg(OTPSERR_EBJ109, JOB_NAME);
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("===============================================");
        log.info("The {} is started",stepExecution.getJobExecution().getJobInstance().getJobName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
