package com.dummy.wpb.product;

import com.dummy.wpb.product.listener.ProductFormatProcessorListener;
import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportEliXmlConfiguration {
    @Value("${batch.size}")
    private int batchSize;

    @Bean
    public Job importSecXmlFileJob(Step importEliXmlFileStep,
                                   JobRepository jobRepository,
                                   SystemCdeCheckListener systemCdeCheckListener) {
        return new JobBuilder("Import Eli XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .flow(importEliXmlFileStep)
                .end()
                .build();
    }

    @Bean
    public Step importEliXmlFileStep(PlatformTransactionManager transactionManager,
                                     JobRepository jobRepository,
                                     TaskExecutor executor,
                                     ProductImportItemWriter productImportWriter,
                                     ItemStreamReader<ProductStreamItem> eliXmlFileReader,
                                     ProductFormatProcessorListener productFormatProcessorListener) {
        return new StepBuilder("importEliXmlFileStep")
                .<ProductStreamItem, ProductStreamItem>chunk(batchSize)
                .listener(productFormatProcessorListener)
                .reader(eliXmlFileReader)
                .processor(eliXmlFileProcessor())
                .writer(productImportWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public ProductImportItemWriter productImportWriter(ProductService productService) {
        return new ProductImportItemWriter(productService);
    }

    @Bean
    @StepScope
    public EliXmlFileProcessor eliXmlFileProcessor() {
        return new EliXmlFileProcessor();
    }

}
