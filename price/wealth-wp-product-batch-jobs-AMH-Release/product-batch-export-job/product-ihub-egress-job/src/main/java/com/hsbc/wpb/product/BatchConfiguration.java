package com.dummy.wpb.product;

import com.dummy.wpb.product.config.loader.ExportConfigLoader;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.service.ProductItemReader;
import com.dummy.wpb.product.writer.SingleTableFileItemWriter;
import com.dummy.wpb.product.writer.TableFileItemWriter;
import com.dummy.wpb.product.writer.TableFileItemWriterFactory;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

@Configuration
public class BatchConfiguration {

    @Value("${batch.chunk-size}")
    private Integer chunkSize;

    // product data export configuration start
    @Bean
    public Job iHubEgressJob(JobRepository jobRepository,
                             TaskExecutor executor,
                             Flow productExportFlow,
                             Flow referenceDataExportFlow,
                             Flow prodPrcHistExportFlow) {

        return new JobBuilder("Generate IHub CSV File Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .start(productExportFlow)
                .split(executor)
                .add(referenceDataExportFlow, prodPrcHistExportFlow)
                .end()
                .build();
    }

    @Bean
    public Flow productExportFlow(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  ProductItemReader productItemReader,
                                  CompositeItemWriter<Document> productItemWriter,
                                  TaskExecutor executor) {
        TaskletStep productExportStep = new StepBuilder("productExportStep")
                .<Document, Document>chunk(chunkSize)
                .reader(productItemReader)
                .writer(productItemWriter)
                .repository(jobRepository)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("productExportFlow").start(productExportStep).build();
    }

    @Bean
    public ProductItemReader productItemReader() {
        Query query = new Query();
        query.limit(chunkSize);
        return new ProductItemReader(query);
    }

    @Bean
    @StepScope
    public CompositeItemWriter<Document> productItemWriter(@Value("#{jobParameters['outputPath']}") String outputPath) {
        TableFileItemWriter[] writers = ExportConfigLoader.getProductTables()
                .stream()
                .map(table -> TableFileItemWriterFactory.createProductTableWriter(table, outputPath))
                .filter(Objects::nonNull)
                .toArray(TableFileItemWriter[]::new);
        return new CompositeItemWriterBuilder<Document>()
                .delegates(writers)
                .build();
    }

    // reference data export configuration start
    @Bean
    public Flow referenceDataExportFlow(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        @Qualifier("referenceDataItemReader") ItemReader<Document> referenceDataItemReader,
                                        @Qualifier("referenceDataItemWriter") SingleTableFileItemWriter referenceDataItemWriter,
                                        TaskExecutor executor) {
        TaskletStep referenceDataExportStep = new StepBuilder("referenceDataExportStep")
                .<Document, Document>chunk(chunkSize)
                .reader(referenceDataItemReader)
                .writer(referenceDataItemWriter)
                .repository(jobRepository)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("referenceDataExportFlow").start(referenceDataExportStep).build();
    }

    @Bean
    public ItemReader<Document> referenceDataItemReader(MongoTemplate mongoTemplate) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc(Field._id)));
        return new MongoItemReaderBuilder<Document>()
                .name("referenceDataItemReader")
                .template(mongoTemplate)
                .targetType(Document.class)
                .pageSize(chunkSize)
                .sorts(Collections.emptyMap())
                .collection(CollectionName.reference_data.name())
                .query(query)
                .build();
    }

    @Bean
    @StepScope
    public SingleTableFileItemWriter referenceDataItemWriter(@Value("#{jobParameters['outputPath']}") String outputPath) {
        return new SingleTableFileItemWriter("CDE_DESC_VALUE", outputPath);
    }


    // product price history data export configuration start
    @Bean
    public Flow prodPrcHistExportFlow(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager,
                                      @Qualifier("prodPrcHistItemReader") ItemReader<Document> prodPrcHistItemReader,
                                      @Qualifier("prodPrcHistItemWriter") SingleTableFileItemWriter prodPrcHistItemWriter,
                                      TaskExecutor executor) {
        TaskletStep prodPrcHistExportStep = new StepBuilder("prodPrcHistExportStep")
                .<Document, Document>chunk(chunkSize)
                .reader(prodPrcHistItemReader)
                .writer(prodPrcHistItemWriter)
                .repository(jobRepository)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("prodPrcHistExportFlow").start(prodPrcHistExportStep).build();
    }

    @Bean
    public ItemReader<Document> prodPrcHistItemReader(MongoTemplate mongoTemplate) {
        LocalDate previousDate = LocalDate.now().minusDays(1);
        Query query = new Query().addCriteria(Criteria.where(Field.recUpdtDtTm).gt(previousDate)).with(Sort.by(Sort.Order.asc(Field.recUpdtDtTm)));
        return new MongoItemReaderBuilder<Document>()
                .name("prodPrcHistItemReader")
                .template(mongoTemplate)
                .targetType(Document.class)
                .pageSize(chunkSize * 10)
                .sorts(Collections.emptyMap())
                .collection(CollectionName.prod_prc_hist.name())
                .query(query)
                .build();
    }

    @Bean
    @StepScope
    public SingleTableFileItemWriter prodPrcHistItemWriter(@Value("#{jobParameters['outputPath']}") String outputPath) {
        return new SingleTableFileItemWriter("PROD_PRC_HIST", outputPath);
    }
}
