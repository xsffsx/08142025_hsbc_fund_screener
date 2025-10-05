package com.dummy.wpb.product.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
public class GacListener implements JobExecutionListener {

    @Value("${batch.ackPath}")
    private String ackPath;

    @Value("${batch.ackFileName}")
    private String ackFileName;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("==========================================");
        log.info("Global Asset Classification file job begin");
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        jobContext.put("noProdId", 0);
        jobContext.put("illegalProdId", 0);
        jobContext.put("prodNotFound", 0);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        int noProdIdNum = jobContext.getInt("noProdId");
        int illegalProdIdNum = jobContext.getInt("illegalProdId");
        int prodNotFoundNum = jobContext.getInt("prodNotFound");
        Optional<StepExecution> stepExecutionOpt = jobExecution.getStepExecutions().stream().findFirst();
        if (stepExecutionOpt.isPresent()) {
            StepExecution stepExecution = stepExecutionOpt.get();
            log.info("Global Asset Classification file job ended");
            log.info("Total number of records:" + stepExecution.getReadCount());
            log.info("              processed:" + (stepExecution.getWriteCount() - prodNotFoundNum));
            log.info("           No CLIENT_ID:" + noProdIdNum);
            log.info("      Illegal CLIENT_ID:" + illegalProdIdNum);
            log.info("      Product not found:" + prodNotFoundNum);
            log.info("       Other error case:" + (stepExecution.getFilterCount() - noProdIdNum - illegalProdIdNum));
            genFinisheFile(stepExecution);
        }
    }

    private void genFinisheFile(StepExecution stepExecution) {
        if (stepExecution.getStatus().equals(BatchStatus.COMPLETED) && stepExecution.getReadCount() > 0) {
            FileOutputStream fileOutputStream;
            try {
                String curDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH));
                fileOutputStream = new FileOutputStream(ackPath + ackFileName + curDateStr + ".ack");
                fileOutputStream.close();
            } catch (Exception e) {
                log.error("Create finish GAC job file failed. Message: " + e.getMessage());
            }
        }
    }
}
