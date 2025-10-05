package com.dummy.wpb.product;

import com.dummy.wpb.product.listener.ProductFormatProcessorListener;
import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Collections;

@Configuration
@Slf4j
public class ImportSnXmlConfiguration {

    @Value("${batch.size}")
    private int batchSize;

    @Bean
    public Job importSnXmlFileJob(Step importSnXmlStep,
                                  JobRepository jobRepository,
                                  ReferenceDataService referenceDataService,
                                  SystemCdeCheckListener systemCdeCheckListener) {
        return new JobBuilder("Import SN XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .flow(importSnXmlStep)
                .end()
                .build();
    }

    @Bean
    public Step importSnXmlStep(PlatformTransactionManager transactionManager,
                                JobRepository jobRepository,
                                TaskExecutor executor,
                                ProductImportItemWriter productImportItemWriter,
                                ItemStreamReader<ProductStreamItem> snXmlFileReader,
                                ProductFormatProcessorListener productFormatProcessorListener) {
        return new StepBuilder("importSnXmlFileStep")
                .<ProductStreamItem, ProductStreamItem>chunk(batchSize)
                .listener(productFormatProcessorListener)
                .reader(snXmlFileReader)
                .processor(snProductXmlFileProcessor())
                .writer(productImportItemWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }


    @Bean
    public ValidatingItemProcessor<ProductStreamItem> snProductValidatorProcessor() {
        ValidatingItemProcessor<ProductStreamItem> gnrcProdValidatorProcessor = new ValidatingItemProcessor<>();
        gnrcProdValidatorProcessor.setValidator(new SnProductValidator());
        gnrcProdValidatorProcessor.setFilter(true);
        return gnrcProdValidatorProcessor;
    }

    @Bean
    public CompositeItemProcessor<ProductStreamItem, ProductStreamItem> snProductXmlFileProcessor() {
        CompositeItemProcessor<ProductStreamItem, ProductStreamItem> productPriceProcessor = new CompositeItemProcessor<>();
        productPriceProcessor.setDelegates(Collections.singletonList(snProductValidatorProcessor()));
        return productPriceProcessor;
    }

    @Bean
    public ProductImportItemWriter graphqlImportWriter(ProductService productService) {
        return new ProductImportItemWriter(productService);
    }
}
