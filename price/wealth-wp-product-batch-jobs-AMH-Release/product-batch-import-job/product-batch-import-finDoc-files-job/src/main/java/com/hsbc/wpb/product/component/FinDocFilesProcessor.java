package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.model.FinDocInput;
import com.dummy.wpb.product.validation.UploadValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;


@StepScope
@Component
@Slf4j
public class FinDocFilesProcessor implements ItemProcessor<FinDocInput,FinDocInput>, StepExecutionListener {

    @Autowired
    public UploadValidation uploadValidation;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Value("#{jobExecutionContext['curFilePath']}")
    private String curFilePath;

    @Override
    public FinDocInput process(FinDocInput item) throws Exception {
        ArrayList<EmailContent> rejRecLst = new ArrayList<>();
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        File curFile = new File(curFilePath);
        item.setFileReqName(curFile.getName());

        //validate input fields.
        uploadValidation.setVersion(FinDocConstants.VER_1_1);
        uploadValidation.setFd(item);
        uploadValidation.setRejectRec(rejRecLst);
        if (uploadValidation.validation()) {
            //check if needs approve from sys_parm, Check the archieve parm
            uploadValidation.checkAprv(item);
            uploadValidation.checkArch(item);
            return item;
        }else {
            //validation false reject record
            item.setStatCde(FinDocConstants.REJECT_ACTION);
            jobContext.put("rejRecLst", rejRecLst);
            return item;
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Product Interface (FINDOCLIST) Update Batch Begins [{}]", curFilePath);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
