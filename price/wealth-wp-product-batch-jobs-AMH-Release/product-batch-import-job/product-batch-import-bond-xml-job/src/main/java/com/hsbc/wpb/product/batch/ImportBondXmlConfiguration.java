package com.dummy.wpb.product.batch;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.listener.ProductFormatProcessorListener;
import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.processor.DebtInstmFormatProcessor;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.writer.BondProductImportWriter;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;


@Configuration
@Slf4j
public class ImportBondXmlConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final JobRepository jobRepository;

    protected ImportBondXmlConfiguration(JobBuilderFactory jobBuilderFactory, JobRepository jobRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.jobRepository = jobRepository;
    }

    @Value("${batch.size}")
    private int batchSize;

    @Bean
    public Job importBondXmlFileJob(Step importBondXmlFileStep,
                                    ReferenceDataService referenceDataService,
                                    SystemCdeCheckListener systemCdeCheckListener) {
        return jobBuilderFactory.get("Import Bond XML Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(systemCdeCheckListener)
                .listener(new ProductSubtpCdeHolder(referenceDataService))
                .flow(importBondXmlFileStep)
                .end()
                .build();
    }

    @Bean
    public Step importBondXmlFileStep(ItemReader<ProductStreamItem> bondXmlFileReader,
                                      ProductImportItemWriter cmbProductImportWriter,
                                      PlatformTransactionManager transactionManager,
                                      TaskExecutor executor,
                                      ProductFormatProcessorListener productFormatProcessorListener) {
        return new StepBuilder("importBondXmlFileStep")
                .<ProductStreamItem, ProductStreamItem>chunk(batchSize)
                .reader(bondXmlFileReader)
                .processor(debtInstmFormatProcessor())
                .listener(productFormatProcessorListener)
                .writer(cmbProductImportWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository).build();
    }


    @Bean
    @StepScope
    public DebtInstmFormatProcessor debtInstmFormatProcessor() {
        return new DebtInstmFormatProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(BondProductImportWriter.class)
    @StepScope
    public BondProductImportWriter bondProductImportWriter(ProductService productService, ReferenceDataService referenceDataService) {
        return new BondProductImportWriter(productService, referenceDataService);
    }

    @Bean
    @StepScope
    public ProductFormatProcessorListener productFormatProcessorListener() {
        ProductFormatProcessorListener processorListener = new ProductFormatProcessorListener();
        processorListener.putDefaultValueCondition("debtInstm.bondIndusGrp", product -> Objects.equals("CD", product.get(Field.prodSubtpCde)));
        processorListener.putDefaultValueCondition("debtInstm.cmbPIBondInd", product -> Objects.equals("CD", product.get(Field.prodSubtpCde)));
        return processorListener;
    }
}
