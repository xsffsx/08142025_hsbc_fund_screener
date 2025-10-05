package com.dummy.wpb.product;

import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
public class ImportCustEligXmlConfiguration {

    @Value("${batch.size}")
    private Integer chunkSize;

    @Bean
    public Job importCustEligXmlFileJob(Step importCustEligXmlStep, JobRepository jobRepository, SystemCdeCheckListener systemCdeCheckListener) {
        return new JobBuilder("Import Customer Eligibility XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .flow(importCustEligXmlStep)
                .end()
                .build();
    }

    @Bean
    public Step importCustEligXmlStep(PlatformTransactionManager transactionManager,
                                      JobRepository jobRepository,
                                      TaskExecutor executor,
                                      CompositeItemProcessor<ProductStreamItem, ProductStreamItem> custEligXmlFileProcessor,
                                      ProductImportItemWriter productImportItemWriter,
                                      ItemStreamReader<ProductStreamItem> custEligXmlFileReader) {
        return new StepBuilder("importCustEligXmlStep")
                .<ProductStreamItem, ProductStreamItem>chunk(chunkSize)
                .reader(custEligXmlFileReader)
                .processor(custEligXmlFileProcessor)
                .writer(productImportItemWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ValidatingItemProcessor<ProductStreamItem> custEligValidatorProcessor() {
        ValidatingItemProcessor<ProductStreamItem> prodPriceValidatorProcessor = new ValidatingItemProcessor<>();
        prodPriceValidatorProcessor.setValidator(new CustEligValidator());
        prodPriceValidatorProcessor.setFilter(true);
        return prodPriceValidatorProcessor;
    }

    @Bean
    @ConditionalOnMissingBean(ProductImportItemWriter.class)
    public ProductImportItemWriter custEligImportItemWriter(ProductService productService) {
        return new ProductImportItemWriter(productService);
    }

    @Bean
    public CompositeItemProcessor<ProductStreamItem, ProductStreamItem> custEligXmlFileProcessor(List<ItemProcessor<ProductStreamItem, ProductStreamItem>> processorList) {
        CompositeItemProcessor<ProductStreamItem, ProductStreamItem> productPriceProcessor = new CompositeItemProcessor<>();
        productPriceProcessor.setDelegates(processorList);
        return productPriceProcessor;
    }
}
