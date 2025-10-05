package com.dummy.wpb.product.component;

import com.dummy.wpb.product.error.ErrorLogger;
import com.dummy.wpb.product.utils.FinDocUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ111;

@Component
@Slf4j
public class FinDocFilesUploadFailListener implements JobExecutionListener {

    @Value("${finDoc.ftp}")
    private String finDocPath;

    @Value("${finDoc.doc.src}")
    private String finDocSrcPath;

    @Autowired
    private UploadService uploadService;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Financial document excel/csv and pdf files upload job start.");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStepExecutions().stream().anyMatch(stepExecution -> BatchStatus.FAILED.equals(stepExecution.getStatus()))) {
            ExecutionContext executionContext = jobExecution.getExecutionContext();
            String curFilePath = executionContext.getString("curFilePath");
            if (StringUtils.isNotBlank(curFilePath)) {
                try {
                    File processFile = new File(curFilePath);
                    FinDocUtils.checkAck(processFile);
                    File exFile = new File(finDocPath, processFile.getName());
                    if (exFile.exists()) {
                        processFile = FinDocUtils.chkAndCreate(processFile);
                        uploadService.moveForm(processFile,finDocPath);
                    } else {
                        uploadService.moveForm(processFile,finDocSrcPath);

                    }
                } catch (IOException e) {
                    log.error("IOException: " + e.getMessage());
                }
                log.error("Financial Document list file Upload failed.");
                ErrorLogger.logErrorMsg(OTPSERR_EBJ111, "finDocFilesUploadJob", jobExecution.getStatus().toString());
            }
        }
    }
}
