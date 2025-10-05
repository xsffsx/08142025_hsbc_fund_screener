package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.email.EmailFormation;
import com.dummy.wpb.product.script.ChmodScript;
import com.dummy.wpb.product.service.FinDocCollectionsService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.FinDocUtils;
import com.dummy.wpb.product.utils.Namefilter;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@StepScope
public class ReadFileNameTasklet implements Tasklet, StepExecutionListener {

    @Value("${finDoc.uplFilePattern}")
    private String filePattern;

    @Value("${finDoc.ftp}")
    private String finDocPath;

    @Value("${finDoc.doc.src}")
    private String finDocSrcPath;

    @Value("${finDoc.pdf.rej}")
    private String finDocPdfRejPath;

    @Value("${finDoc.reject.ENS}")
    private String rejLogPath;

    @Value("${CHMOD.Script}")
    private String chmodScriptPath;

    @Autowired
    private FinDocCollectionsService finDocCollectionsService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private PDFFileService pdfFileService;

    private List<EmailContent> rejRecLst;
    private List<String> filePathLst;

    public ReadFileNameTasklet() {
        this.rejRecLst = new ArrayList<>();
        this.filePathLst = new ArrayList<>();
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        List<File> finDocUplFiles = getMappingFiles();
        if (CollectionUtils.isEmpty(finDocUplFiles)) {
            log.error("No financial document list file can be processed.");
            return RepeatStatus.FINISHED;
        }

        for (int i = 0; i < finDocUplFiles.size(); i++) {
            //.ack file will be removed after checkAck() method executed. that means you need to prepare the .ack file everytime while testing.
            if (FinDocUtils.checkAck(finDocUplFiles.get(i))) {
                checkInputFile(finDocUplFiles.get(i));
            }else {
                log.error("Rejected For: " + finDocUplFiles.get(i).getName() + " could not process cause by no relate .ack file.");
            }
        }
        //set file to Job executionContext
        ExecutionContext jobcontext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        jobcontext.put("fileLst", filePathLst);
        //store reject list file
        if (CollectionUtils.isNotEmpty(rejRecLst)) {
            EmailFormation email = new EmailFormation();
            email.emailFormation(FinDocConstants.FDDFLTSNDR,
                     rejRecLst.toArray(new EmailContent[0]), chmodScriptPath, rejLogPath);
        }

        return RepeatStatus.FINISHED;
    }

    private void checkInputFile(File file) throws Exception {
        List<Document> finDocUplDocs = finDocCollectionsService.finDocUpldByFilters(Filters.eq(Field.fileRqstName, file.getName()));
        // chkAndCreate() will check the .xls file and create a new .xls with the same name.
        File curFile;
        curFile = FinDocUtils.chkAndCreate(file);
        if (CollectionUtils.isEmpty(finDocUplDocs)) {
            // move .xls file to path xxx.../req/incoming. ---> FinDoc.doc.src
            // if file existed in FinDoc.doc.src cannot move file
            curFile = movefile(curFile);
            ChmodScript.chmodScript(curFile.getPath(), "755", chmodScriptPath);
            filePathLst.add(curFile.getPath());

        }else {
            uploadService.moveForm(curFile,finDocPath);

            File reqFile;
            for(int j = 0; j < finDocUplDocs.size(); j++) {
                String pdfName = finDocUplDocs.get(j).getString(Field.docRecvName);
                FilenameFilter fnfPdf = new Namefilter(pdfName.trim(), false);
                File[] match;
                try {
                    match = pdfFileService.lsFileInFolder(finDocPath, fnfPdf);
                    if (match.length > 0) {
                        reqFile = match[0];
                        FinDocUtils.checkAck(reqFile);
                        String recCreatDtTm = LocalDateTime.ofInstant(((Date)finDocUplDocs.get(j).get(Field.recCreatDtTm)).toInstant(), ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss"));
                        pdfFileService.movePDF2Rej(recCreatDtTm, reqFile, new File(finDocPdfRejPath));
                    }
                } catch (Exception e) {
                    log.error("Cannot move pdf file " + pdfName + " to FinDoc.pdf.rej folder. Error message: " + e.getMessage());
                }
            }
            log.error("Rejected Form: " + file.getName() + " already exist in the fin_doc_upld collection");
            rejRecLst.add(sendMail(curFile));
        }
    }

    private List<File> getMappingFiles() {
        List<File> files = CommonUtils.scanFileInPathWithPattern(finDocPath, filePattern);
        return files.stream().filter(file -> !file.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_ACK) && (file.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_CSV)
                || file.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_XLS))).collect(Collectors.toList());
    }

    public  File movefile(File file) {
        File dir = new File(finDocPath, file.getName());
        boolean success = dir.renameTo(new File(finDocSrcPath, dir.getName()));
        if (!success) {
            log.error("Finanical Input Document File can not be moved " + dir);
            // return a virtual file
            return new File("");
        }
        // return the new file
        return new File(finDocSrcPath, dir.getName());
    }

    private EmailContent sendMail(File file) {
        String content = "Duplicate input form was submitted at "
                + FinDocUtils.getcurTimeDDMMMYY() + ". Please upload again.";
        EmailContent emailCnt = new EmailContent();
        emailCnt.setSubject(file.getName() + " Rejected");
        emailCnt.setRecptAdr(null);
        emailCnt.setContent(content);
        return emailCnt;
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
