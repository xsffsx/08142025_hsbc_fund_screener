package com.dummy.wpb.product.component;

import com.dummy.wpb.product.error.ErrorLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dummy.wpb.product.constant.EsgConstants.JOB_NAME;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_ZBJ110;

@Slf4j
@Component
public class EsgListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("==========================================");
        log.info("{} begins", JOB_NAME);
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        jobContext.put("failUpdate", new AtomicInteger(0));
        jobContext.put("validateFail", new AtomicInteger(0));
        jobContext.put("prodNotFound", new AtomicInteger(0));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        AtomicInteger prodNotFoundNum = (AtomicInteger)jobContext.get("prodNotFound");
        AtomicInteger validateFailNum = (AtomicInteger)jobContext.get("validateFail");

        if(validateFailNum != null && validateFailNum.intValue() > 0){
            log.info("Totally {} Records are rejected in {}", validateFailNum, JOB_NAME);
            ErrorLogger.logErrorMsg(
                    OTPSERR_ZBJ110,
                    JOB_NAME,
                    "Totally " + validateFailNum +" Records are rejected in "+ JOB_NAME);
        }

        Optional<StepExecution> stepExecutionOpt = jobExecution.getStepExecutions().stream().findFirst();
        if (stepExecutionOpt.isPresent()) {
            StepExecution stepExecution = stepExecutionOpt.get();
            log.info("{} ended", JOB_NAME);
            log.info("Total number of records:" + stepExecution.getReadCount());
            log.info("              processed:" + stepExecution.getWriteCount());
            log.info("        validate failed:" + validateFailNum);
            log.info("      product not found:" + prodNotFoundNum);
            log.info("{} completed", JOB_NAME);
            log.info("==========================================");
        }

    }
}
