package com.dummy.wpb.product;

import com.dummy.wpb.product.component.SystemFormatProcessor;
import com.dummy.wpb.product.component.UtTrstInstmFormatProcessor;
import com.dummy.wpb.product.configuration.FundHouseCdeHolder;
import com.dummy.wpb.product.listener.ProductFormatProcessorListener;
import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
@Slf4j
public class ImportUtXmlConfiguration {

    @Bean
    public Job importUtXmlFileJob(Step importUtXmlStep,
                                  JobRepository jobRepository,
                                  ReferenceDataService referenceDataService,
                                  SystemCdeCheckListener systemCdeCheckListener) {
        return new JobBuilder("Import UT XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .listener(new FundHouseCdeHolder(referenceDataService))
                .flow(importUtXmlStep)
                .end()
                .build();
    }

    @Bean
    public Step importUtXmlStep(PlatformTransactionManager transactionManager,
                                JobRepository jobRepository,
                                TaskExecutor executor,
                                ProductImportItemWriter productImportItemWriter,
                                ItemStreamReader<ProductStreamItem> utXmlFileReader,
                                ProductFormatProcessorListener productFormatProcessorListener) {
        return new StepBuilder("importUtXmlFileStep")
                .<ProductStreamItem, ProductStreamItem>chunk(50)
                .reader(utXmlFileReader)
                .processor(utXmlFileProcessor())
                .writer(productImportItemWriter)
                .listener(productFormatProcessorListener)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @StepScope
    public UtTrstInstmFormatProcessor utTrstInstmFormatProcessor() {
        return new UtTrstInstmFormatProcessor();
    }

    @Bean
    @StepScope
    public SystemFormatProcessor systemFormatProcessor() {
        return new SystemFormatProcessor();
    }

    @Bean
    public ItemProcessor<ProductStreamItem, ProductStreamItem> utXmlFileProcessor() {
        CompositeItemProcessor<ProductStreamItem, ProductStreamItem> utXmlFileProcessor = new CompositeItemProcessor<>();
        utXmlFileProcessor.setDelegates(Arrays.asList(systemFormatProcessor(), utTrstInstmFormatProcessor()));
        return utXmlFileProcessor;
    }

    @Bean
    public ProductImportItemWriter graphqlImportWriter(ProductService productService) {
        return new ProductImportItemWriter(productService);
    }
}
