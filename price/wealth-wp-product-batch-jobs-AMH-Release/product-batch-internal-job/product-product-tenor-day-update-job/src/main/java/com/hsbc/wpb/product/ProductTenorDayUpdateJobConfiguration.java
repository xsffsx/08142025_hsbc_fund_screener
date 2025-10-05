package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductStatus;
import com.dummy.wpb.product.constant.ProductType;
import com.dummy.wpb.product.service.ProductItemReader;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
@EnableBatchProcessing
@Slf4j
public class ProductTenorDayUpdateJobConfiguration {

    @Value("${batch.chunk-size:10000}")
    private Integer chunkSize;

    @Bean
    public Job productTenorDayUpdateJob(
            JobRepository jobRepository,
            Step productTenorDayUpdateStep) {
        return new JobBuilder("Internal Calculate Tenor In Day Batch (ALL) Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(productTenorDayUpdateStep)
                .end()
                .build();
    }

    @Bean
    public Step productTenorDayUpdateStep(
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository,
            TaskExecutor executor,
            ProductItemReader reader,
            ProductTenorDayUpdateJobWriter writer) {
        return new StepBuilder("productTenorDayUpdateStep")
                .<Document, Document>chunk(chunkSize)
                .reader(reader)
                .writer(writer)
                .processor(new ProductTenorDayUpdateProcessor())
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @StepScope
    public ProductItemReader productItemReader(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                               @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                               @Value("#{jobParameters['prodTypeCde']}") String prodTypeCde) {
        String[] prodTypesArray = prodTypeCde.split(",");
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        query.addCriteria(Criteria.where(Field.prodStatCde).ne(ProductStatus.D.name()));
        if (!Arrays.asList(prodTypesArray).contains(ProductType.ALL.name())) {
            query.addCriteria(Criteria.where(Field.prodTypeCde).in(prodTypesArray));
        }
        query.limit(chunkSize);
        query.fields().include(
                Field.prodId,
                Field.prodMturDt,
                Field.prodLnchDt,
                Field.termRemainDayCnt,
                Field.ctryRecCde,
                Field.grpMembrRecCde,
                Field.prodTypeCde,
                Field.prodAltPrimNum);
        return new ProductItemReader(query);
    }

}
