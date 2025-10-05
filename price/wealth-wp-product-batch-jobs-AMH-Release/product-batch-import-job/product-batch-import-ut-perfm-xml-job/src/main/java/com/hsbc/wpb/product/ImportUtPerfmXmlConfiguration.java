package com.dummy.wpb.product;

import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportUtPerfmXmlConfiguration {

    @Value("${batch.size}")
    private int batchSize;

    @Bean
    public Job importUtPerfmXmlFileJob(Step importUtXmlStep, JobRepository jobRepository, SystemCdeCheckListener systemCdeCheckListener) {
        return new JobBuilder("Import UT Performance XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .flow(importUtXmlStep)
                .end()
                .build();
    }

    @Bean
    public Step importUtXmlStep(PlatformTransactionManager transactionManager, JobRepository jobRepository, UtPerfmXmlProcessor utPerfmXmlProcessor,
                                ProductImportItemWriter productImportItemWriter,  ItemStreamReader<ProductStreamItem> utPerfmXmlFileReader) {
        return new StepBuilder("importUtPerfmXmlFileStep")
                .<ProductStreamItem, ProductStreamItem>chunk(batchSize)
                .reader(utPerfmXmlFileReader)
                .processor(utPerfmXmlProcessor)
                .writer(productImportItemWriter)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public ProductImportItemWriter graphqlImportWriter(ProductService productService) {
        return new ProductImportItemWriter(productService);
    }
}
