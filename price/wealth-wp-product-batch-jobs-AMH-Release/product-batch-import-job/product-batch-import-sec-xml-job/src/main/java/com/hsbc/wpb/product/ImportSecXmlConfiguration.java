package com.dummy.wpb.product;

import com.dummy.wpb.product.listener.ProductFormatProcessorListener;
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
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
public class ImportSecXmlConfiguration {
    @Value("${batch.size}")
    private int batchSize;

    @Bean
    public Job importSecXmlFileJob(Step importSecXmlFileStep,
                                   JobRepository jobRepository,
                                   SystemCdeCheckListener systemCdeCheckListener) {
        return new JobBuilder("Import SEC XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .flow(importSecXmlFileStep)
                .end()
                .build();
    }

    @Bean
    public Step importSecXmlFileStep(PlatformTransactionManager transactionManager,
                                     JobRepository jobRepository,
                                     TaskExecutor executor,
                                     ProductImportItemWriter productImportWriter,
                                     ItemStreamReader<ProductStreamItem> secXmlFileReader,
                                     CompositeItemProcessor<ProductStreamItem, ProductStreamItem> secCompositeProcessor,
                                     ProductFormatProcessorListener productFormatProcessorListener) {
        return new StepBuilder("importSecXmlFileStep")
                .<ProductStreamItem, ProductStreamItem>chunk(batchSize)
                .reader(secXmlFileReader)
                .listener(productFormatProcessorListener)
                .processor(secCompositeProcessor)
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
    public ValidatingItemProcessor<ProductStreamItem> secXmlFileValidatorProcessor(SecXmlFileValidator secXmlFileValidator) {
        ValidatingItemProcessor<ProductStreamItem> secXmlFileValidatorProcessor = new ValidatingItemProcessor<>();
        secXmlFileValidatorProcessor.setValidator(secXmlFileValidator);
        secXmlFileValidatorProcessor.setFilter(true);
        return secXmlFileValidatorProcessor;
    }

    @Bean
    public CompositeItemProcessor<ProductStreamItem, ProductStreamItem> secCompositeProcessor(
            ValidatingItemProcessor<ProductStreamItem> secXmlFileValidatorProcessor,
            SecXmlFileProcessor secXmlFileProcessor) {
        CompositeItemProcessor<ProductStreamItem, ProductStreamItem> productPriceProcessor = new CompositeItemProcessor<>();
        productPriceProcessor.setDelegates(Arrays.asList(secXmlFileValidatorProcessor, secXmlFileProcessor));
        return productPriceProcessor;
    }
}
