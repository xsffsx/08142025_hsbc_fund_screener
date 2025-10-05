package com.dummy.wpb.product;

import com.dummy.wpb.product.configuration.TermsheetConfiguration;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.PathMonitor;
import com.dummy.wpb.product.utils.SidFinDocPathListener;
import com.dummy.wpb.product.utils.SidTsFinDocUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ImportTermsheetProcessor implements Tasklet, StepExecutionListener {

    private final ImportTermsheetService importTermsheetService;

    private final TermsheetConfiguration termsheetConfiguration;

    public ImportTermsheetProcessor(TermsheetConfiguration termsheetConfiguration, ImportTermsheetService importTermsheetService) {
        this.termsheetConfiguration = termsheetConfiguration;
        this.importTermsheetService = importTermsheetService;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("=========================================================");
        log.info("Process SPOMS term sheet begins");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Process SPOMS term sheet ends");
        return ExitStatus.COMPLETED;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        StepExecution stepExecution = stepContribution.getStepExecution();
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
        String isPostProduct = jobParameters.getString("isPostProduct");
        String ctryRecCde = jobParameters.getString(Field.ctryRecCde);
        String grpMembrRecCde = jobParameters.getString(Field.grpMembrRecCde);
        String prodTypeCde = jobParameters.getString(Field.prodTypeCde);
        String finDocPath = jobParameters.getString("finDocPath");
        Assert.notNull(finDocPath, "Job Parameter finDocPath can not be null");
        Assert.notNull(isPostProduct, "Job Parameter isPostProduct can not be null");
        // list incoming findoc files
        File directory = Paths.get(finDocPath).toFile();
        if (isPostProduct.equals("Y")) {
            log.info("postProduct is {}, path is {}, Enter Post process product", isPostProduct, directory.getPath());
            SidTsFinDocUtil.postProcess(directory, ctryRecCde, grpMembrRecCde, prodTypeCde, importTermsheetService, termsheetConfiguration);
        } else {
            log.info("postProduct is {}, path is {}, Enter Monitor", isPostProduct, directory.getPath());
            monitorFinDocPath(directory, ctryRecCde, grpMembrRecCde, prodTypeCde);
        }

        return RepeatStatus.FINISHED;
    }


    private void monitorFinDocPath(File directory, String ctryRecCde, String grpMembrRecCde, String prodTypeCde) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        SidFinDocPathListener listener = new SidFinDocPathListener(directory, ctryRecCde, grpMembrRecCde, prodTypeCde, importTermsheetService, termsheetConfiguration);
        listener.setExecutorService(executorService);
        FileFilter fileFilter = file -> file.getName().endsWith(FinDocConstants.SUFFIX_ACK);
        PathMonitor monitor = new PathMonitor(directory.toPath(), listener, fileFilter);
        try {
            monitor.start();
            TimeUnit.HOURS.sleep(12);
        } finally {
            executorService.shutdown();
            monitor.stop();
        }
    }


}