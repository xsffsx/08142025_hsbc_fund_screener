package com.dummy.wpb.product.configuration;

import com.dummy.wpb.product.component.WimFlatFileItemWriter;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.utils.CommonUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class BatchConfiguration {

    @Value("${batch.chunk-size}")
    private Integer chunkSize;

    @Autowired
    TaskExecutor executor;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MongoOperations mongoTemplate;

    @Bean
    public Job wimEgressJob(Flow productFlow,
                            Flow prodAltIdFlow,
                            Flow referenceFlow) {
        return new JobBuilder("Generate WIM CSV File Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .start(productFlow)
                .split(executor)
                .add(prodAltIdFlow,referenceFlow)
                .end()
                .build();
    }

    @Bean
    public Flow productFlow(TaskExecutor executor,
                            ItemReader<Document> productItemReader,
                            CompositeItemWriter<Document> productCompositeItemWriter) {
        TaskletStep productStep = new StepBuilder("productStep")
                .<Document, Document>chunk(chunkSize)
                .reader(productItemReader)
                .writer(productCompositeItemWriter)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("productFlow").start(productStep).build();
    }


    @Bean
    public ItemReader<Document> productItemReader() {
        return new MongoItemReaderBuilder<Document>()
                .name("productItemReader")
                .template(mongoTemplate)
                .targetType(Document.class)
                .pageSize(chunkSize)
                .collection(CollectionName.product.name())
                .jsonQuery(CommonUtils.readResource("/product-criteria.json"))
                .sorts(Collections.singletonMap(Field.prodId, Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<Document> productCompositeItemWriter(@Value("#{jobParameters['outputPath']}") String outputPath) {
        WimFlatFileItemWriter utTrstInstmItemWriter = new WimFlatFileItemWriter("TB_UT_TRST_INSTM", outputPath);
        WimFlatFileItemWriter debtInstmItemWriter = new WimFlatFileItemWriter("TB_DEBT_INSTM", outputPath);
        WimFlatFileItemWriter productItemWriter = new WimFlatFileItemWriter("TB_PROD", outputPath);
        CompositeItemWriter<Document> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(utTrstInstmItemWriter, debtInstmItemWriter, productItemWriter));
        return compositeItemWriter;
    }

    @Bean
    public Flow prodAltIdFlow(ItemReader<Document> prodAltIdItemReader,
                              WimFlatFileItemWriter prodAltIdItemWriter) {
        TaskletStep referenceStep = new StepBuilder("prodAltIdStep")
                .<Document, Document>chunk(chunkSize)
                .reader(prodAltIdItemReader)
                .writer(prodAltIdItemWriter)
                .taskExecutor(executor)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("prodAltIdFlow").start(referenceStep).build();
    }

    @Bean
    public ItemReader<Document> prodAltIdItemReader() {
        Query query = new BasicQuery(CommonUtils.readResource("/product-altId-criteria.json"));
        query.fields().include(Field.prodId, Field.ctryRecCde, Field.grpMembrRecCde, Field.altId);
        query.with(Sort.by(Sort.Order.asc(Field._id)));
        query.cursorBatchSize(chunkSize);
        return new MongoItemReaderBuilder<Document>()
                .name("productItemReader")
                .template(mongoTemplate)
                .targetType(Document.class)
                .pageSize(chunkSize * 10)
                .collection(CollectionName.product.name())
                .sorts(Collections.emptyMap())
                .query(query)
                .build();
    }

    @Bean
    @StepScope
    public WimFlatFileItemWriter prodAltIdItemWriter(@Value("#{jobParameters['outputPath']}") String outputPath) {
        return new WimFlatFileItemWriter("TB_PROD_ALT_ID", outputPath);
    }

    @Bean
    public Flow referenceFlow(ItemReader<Document> referenceDataItemReader,
                              WimFlatFileItemWriter referenceDataItemWriter) {
        TaskletStep referenceStep = new StepBuilder("referenceStep")
                .<Document, Document>chunk(chunkSize)
                .reader(referenceDataItemReader)
                .writer(referenceDataItemWriter)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("referenceFlow").start(referenceStep).build();
    }

    @Bean
    public ItemReader<Document> referenceDataItemReader() {
        return new MongoItemReaderBuilder<Document>()
                .name("referenceDataItemReader")
                .template(mongoTemplate)
                .targetType(Document.class)
                .pageSize(chunkSize)
                .collection(CollectionName.reference_data.name())
                .jsonQuery(CommonUtils.readResource("/referenceData-criteria.json"))
                .sorts(Collections.singletonMap(Field._id, Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public WimFlatFileItemWriter referenceDataItemWriter(@Value("#{jobParameters['outputPath']}") String outputPath) {
        return new WimFlatFileItemWriter("CDE_DESC_VALUE", outputPath);
    }

}
