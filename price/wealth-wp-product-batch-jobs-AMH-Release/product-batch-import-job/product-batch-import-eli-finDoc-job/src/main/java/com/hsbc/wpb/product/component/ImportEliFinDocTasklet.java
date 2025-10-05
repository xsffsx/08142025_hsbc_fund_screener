package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.utils.EliFinDocPathListener;
import com.dummy.wpb.product.utils.EliTsFinDocUtil;
import com.dummy.wpb.product.utils.Namefilter;
import com.dummy.wpb.product.utils.PathMonitorManager;
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

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@StepScope
public class ImportEliFinDocTasklet implements Tasklet, StepExecutionListener {

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{jobParameters['prodTypeCde']}")
    private String prodTypeCde;

    @Value("#{jobParameters['fileName']}")
    private String finDocPath;

    @Value("#{jobParameters['actionCde'] ?: ''}")
    private String postProduct;

    @Autowired
    private EliTsFinDocUtil eliTsFinDocUtil;

    @Autowired
    private ImportEliFinDocService importEliFinDocService;

    @Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("WPC ELI Financial Document Job start");
        Path path = Paths.get(finDocPath);
        if (postProduct.equals("Y")) {
            log.info("postProduct is {}, Enter Post process product", postProduct);
            postProcess(path);
        } else {
            log.info("postProduct is {}, Enter Monitor", postProduct);
            monitorFinDocPath(path);
        }
		log.info("WPC ELI Financial Document Job end");

		return RepeatStatus.FINISHED;
	}

    private void postProcess(Path path) throws Exception {
        File dir = path.toRealPath().toFile();
        if (dir.isDirectory()) {
            Namefilter filter = new Namefilter(FinDocConstants.SUFFIX_ACK, false, false);
            File[] files = dir.listFiles(filter);
            for (File file : files) {
                eliTsFinDocUtil.handleFinDocFile(file, ctryRecCde, grpMembrRecCde, prodTypeCde, importEliFinDocService, true);
            }
        }
    }

    private void monitorFinDocPath(Path path) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        EliFinDocPathListener listener = new EliFinDocPathListener(ctryRecCde, grpMembrRecCde, prodTypeCde, importEliFinDocService, eliTsFinDocUtil);
        listener.setExecutorService(executorService);
        FileFilter fileFilter = file -> file.getName().endsWith(FinDocConstants.SUFFIX_ACK);
        PathMonitorManager monitor = new PathMonitorManager(path, listener, fileFilter);
        try {
            monitor.start();
            TimeUnit.HOURS.sleep(12);
        } finally {
            executorService.shutdown();
            monitor.stop();
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
