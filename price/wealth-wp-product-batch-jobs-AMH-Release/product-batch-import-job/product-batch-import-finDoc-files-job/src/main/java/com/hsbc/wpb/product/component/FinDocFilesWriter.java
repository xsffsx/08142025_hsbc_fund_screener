package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.email.EmailFormation;
import com.dummy.wpb.product.model.FinDocInput;
import com.dummy.wpb.product.validation.UploadValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@StepScope
@Component
@Slf4j
public class FinDocFilesWriter implements ItemWriter<FinDocInput>, StepExecutionListener {

    @Value("#{jobExecutionContext['curFilePath']}")
    private String curFilePath;

    @Value("${finDoc.doc.src}")
    private String finDocSrcPath;

    @Value("${finDoc.reject.ENS}")
    private String rejLogPath;

    @Value("${CHMOD.Script}")
    private String chmodScriptPath;

    @Autowired
    public UploadValidation uploadValidation;

    @Autowired
    public UploadService uploadService;

    @Override
    public void write(List<? extends FinDocInput> items) throws IOException {
        if (CollectionUtils.isEmpty(items)) {
            log.error("Nun records can be processed.");
        }

        for (FinDocInput fd : items) {
            if (FinDocConstants.REJECT_ACTION.equals(fd.getStatCde())) {
                //remove ack and move pdf file to reject folder and add dateTime suffix to fileName.store reject record to fin_doc_upld
                File rejFile = uploadService.pdfMove2Rej(fd);
                uploadService.insertUploadReq(fd, uploadValidation.getErrmsg(), rejFile, null, false);
            }else {
                uploadService.uploadRec(fd);
            }
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        //no need to use this method
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext jobcontext = stepExecution.getJobExecution().getExecutionContext();
        ArrayList<EmailContent> rejRecLst = (ArrayList)jobcontext.get("rejRecLst");

        //store reject list file
        if (!CollectionUtils.isEmpty(rejRecLst)) {
            EmailFormation email = new EmailFormation();
            email.emailFormation(FinDocConstants.FDDFLTSNDR, rejRecLst.toArray(new EmailContent[0]), chmodScriptPath, rejLogPath);
        }

        jobcontext.remove("rejRecLst");
        return ExitStatus.COMPLETED;
    }
}
