package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductType;
import com.dummy.wpb.product.service.ProductItemReader;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
public class ProductPeriodUpdateJobConfiguration {
    private static final Integer CHUNK_SIZE = 10000;

    private final MongoDatabase mongoDatabase;

    public ProductPeriodUpdateJobConfiguration(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Bean
    public Job productPeriodUpdateJob(JobRepository jobRepository,
                                      Step productPeriodUpdateStep) {
        return new JobBuilder("Internal Calculate Product Period Update Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(productPeriodUpdateStep)
                .end()
                .build();
    }

    @Bean
    public Step productPeriodUpdateStep(PlatformTransactionManager transactionManager,
                                        JobRepository jobRepository,
                                        ProductItemReader productPeriodUpdateItemReader,
                                        TaskExecutor executor) {
        return new StepBuilder("productPeriodUpdateStep")
                .<Document, Document>chunk(CHUNK_SIZE)
                .reader(productPeriodUpdateItemReader)
                .processor(new ProductPeriodUpdateProcessor())
                .writer(productPeriodUpdateItemWriter())
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public ProductPeriodUpdateItemWriter productPeriodUpdateItemWriter() {
        return new ProductPeriodUpdateItemWriter(mongoDatabase);
    }

    @Bean
    @StepScope
    public ProductItemReader productPeriodUpdateItemReader(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                           @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                           @Value("#{jobParameters['prodTypeCde']}") String prodTypeCde) {
        String[] prodTypesArray = prodTypeCde.split(",");
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        if (!Arrays.asList(prodTypesArray).contains(ProductType.ALL.name())) {
            query.addCriteria(Criteria.where(Field.prodTypeCde).in(prodTypesArray));
        }
        query.limit(CHUNK_SIZE);
        query.fields().include(
                Field._id,
                Field.ctryRecCde,
                Field.grpMembrRecCde,
                Field.prodTypeCde,
                Field.prodAltPrimNum,
                Field.prodStatCde,
                Field.prodId,
                Field.termRemainDayCnt,
                Field.prdProdCde,
                Field.prdProdNum,
                Field.prdInvstTnorNum);
        return new ProductItemReader(query);
    }

}
