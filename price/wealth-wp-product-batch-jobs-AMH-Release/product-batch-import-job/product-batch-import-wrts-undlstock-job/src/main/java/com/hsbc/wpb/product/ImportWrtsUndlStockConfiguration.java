package com.dummy.wpb.product;

import com.dummy.wpb.product.listener.WrtsUndlStockStepExecutionListener;
import com.dummy.wpb.product.model.WrtsUndlStockRecord;
import com.dummy.wpb.product.utils.ImportWrtsUndlStockUtils;
import com.dummy.wpb.product.validator.WrtsUndlStockValidator;
import com.dummy.wpb.product.writer.WrtsUndlStockItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class ImportWrtsUndlStockConfiguration {

    @Value("${batch.chunk-size}")
    private int chunkSize;

    public static final String JOB_NAME = "Import MIDFS WRTS Underlying Stocks Job";

    public static final String STEP_NAME = "Import MIDFS WRTS Underlying Stocks Step";

    @Bean
    public Job importWrtsUndlStockJob(
            JobRepository jobRepository,
            Step importWrtsUndlStockStep) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(importWrtsUndlStockStep)
                .end()
                .build();
    }

    /**
     * IMPORTANT! Please DO NOT enable {@link TaskExecutor} because the count incrementing
     * methods of {@link ImportWrtsUndlStockUtils} are not thread-safe. Also, the complexity and scale of this job don't
     * require parallel processing.
     */
    @Bean
    public Step importWrtsUndlStockStep(
            ItemStreamReader<WrtsUndlStockRecord> wrtsUndlStockReader,
            ValidatingItemProcessor<WrtsUndlStockRecord> processor,
            WrtsUndlStockItemWriter writer,
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository,
            WrtsUndlStockStepExecutionListener listener) {
        return new StepBuilder(STEP_NAME)
                .<WrtsUndlStockRecord, WrtsUndlStockRecord>chunk(chunkSize)
                .reader(wrtsUndlStockReader)
                .processor(processor)
                .writer(writer)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .listener(listener)
                .build();
    }

    @Bean
    public ValidatingItemProcessor<WrtsUndlStockRecord> wrtsUndlStockValidatingItemProcessor(
            WrtsUndlStockValidator validator) {
        ValidatingItemProcessor<WrtsUndlStockRecord> processor = new ValidatingItemProcessor<>();
        processor.setValidator(validator);
        processor.setFilter(true);
        return processor;
    }
}
