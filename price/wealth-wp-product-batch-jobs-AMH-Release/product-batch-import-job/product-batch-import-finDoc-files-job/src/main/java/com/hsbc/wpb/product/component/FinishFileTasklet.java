package com.dummy.wpb.product.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
@StepScope
public class FinishFileTasklet implements Tasklet {

    @Value("#{jobExecutionContext['curFilePath']}")
    private String curFilePath;

    @Value("${finDoc.doc.src}")
    private String finDocSrcPath;

    @Autowired
    private UploadService uploadService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

        //move .xls file with timestep to xxx.../data/FinDoc/req/chk
        uploadService.moveForm(new File(curFilePath), finDocSrcPath);

        return RepeatStatus.FINISHED;
    }

}
