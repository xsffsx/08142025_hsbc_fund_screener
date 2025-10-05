package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.jpo.LogEqtyLinkInvstPo;
import com.dummy.wpb.product.jpo.ReferenceDataPo;
import com.dummy.wpb.product.service.ProductItemReader;
import com.dummy.wpb.product.writer.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Configuration
public class BatchConfiguration {

    @Value("${batch.chunk-size}")
    private Integer chunkSize;

    @Autowired
    MongoTemplate mongoTemplate;

    private static final String COGNOSCDE = "COGNOS.PROD.LASTESTRECUPDTTM";

    @Bean
    public Job migrateDataJob(JobRepository jobRepository,
                             TaskExecutor executor,
                             Flow productMigrateFlow,
                             Flow logEqtyLinkInvstMigrateFlow,
                             Flow referenceDataMigrateFlow, DeleteSimpleTableListener deleteSimpleTableListener) {

        return new JobBuilder("Internal Migrate Cognos Tables Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(deleteSimpleTableListener)
                .start(productMigrateFlow)
                .split(executor)
                .add(referenceDataMigrateFlow, logEqtyLinkInvstMigrateFlow)
                .end()
                .build();
    }


    // reference data configuration start
    @Bean
    public Flow referenceDataMigrateFlow(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        @Qualifier("referenceDataItemReader") ItemReader<ReferenceDataPo> referenceDataItemReader,
                                        @Qualifier("refeDataRepositoryWriter") RefeDataRepositoryWriter refeDataRepositoryWriter,
                                        TaskExecutor executor) {
        TaskletStep referenceDataMigrateStep = new StepBuilder("referenceDataMigrateStep")
                .<ReferenceDataPo, ReferenceDataPo>chunk(chunkSize)
                .reader(referenceDataItemReader)
                .writer(refeDataRepositoryWriter)
                .repository(jobRepository)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("referenceDataMigrateFlow").start(referenceDataMigrateStep).build();
    }

    @Bean
    public ItemReader<ReferenceDataPo> referenceDataItemReader(MongoTemplate mongoTemplate) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc(Field._id)));
        return new MongoItemReaderBuilder<ReferenceDataPo>()
                .name("referenceDataItemReader")
                .template(mongoTemplate)
                .targetType(ReferenceDataPo.class)
                .pageSize(chunkSize)
                .sorts(Collections.emptyMap())
                .collection(CollectionName.reference_data.name())
                .query(query)
                .build();
    }

    @Bean
    public Flow productMigrateFlow(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  ProductItemReader productItemReader,
                                  CompositeItemWriter<Document> productItemWriter,
                                  TaskExecutor executor) {
        TaskletStep productMigrateStep = new StepBuilder("productMigrateStep")
                .<Document, Document>chunk(chunkSize)
                .reader(productItemReader)
                .writer(productItemWriter)
                .repository(jobRepository)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("productMigrateFlow").start(productMigrateStep).build();
    }

    @Bean
    @StepScope
    public ProductItemReader productItemReader( @Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                @Value("#{jobParameters['isFullSycn']}") String isFullSycn) {
        Query query = new Query();
        LocalDateTime latestRecUpdtDtTm = queryLatestUpdtDtTm(ctryRecCde, grpMembrRecCde);
        if (latestRecUpdtDtTm != null && !Boolean.parseBoolean(isFullSycn)) {
            query.addCriteria(Criteria.where(Field.recUpdtDtTm).gt(latestRecUpdtDtTm));
        }
        query.limit(chunkSize);
        return new ProductItemReader(query);
    }

    @Bean
    @StepScope
    public CompositeItemWriter<Document> productItemWriter(List<ProductMigrateWriter> writerList) {
        return new CompositeItemWriterBuilder<Document>()
                .delegates(writerList.toArray(new ProductMigrateWriter[]{}))
                .build();
    }

    // LOG_EQTY_LINK_INVST configuration start
    @Bean
    public Flow logEqtyLinkInvstMigrateFlow(JobRepository jobRepository,
                                         PlatformTransactionManager transactionManager,
                                         @Qualifier("logEqtyLinkInvstItemReader") ItemReader<LogEqtyLinkInvstPo> logEqtyLinkInvstItemReader,
                                         @Qualifier("logEqtyLinkInvstWriter") LogEqtyLinkInvstWriter logEqtyLinkInvstWriter,
                                         TaskExecutor executor) {
        TaskletStep logEqtyLinkInvstMigrateStep = new StepBuilder("logEqtyLinkInvstMigrateStep")
                .<LogEqtyLinkInvstPo, LogEqtyLinkInvstPo>chunk(chunkSize)
                .reader(logEqtyLinkInvstItemReader)
                .writer(logEqtyLinkInvstWriter)
                .repository(jobRepository)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("logEqtyLinkInvstMigrateFlow").start(logEqtyLinkInvstMigrateStep).build();
    }

    @Bean
    public ItemReader<LogEqtyLinkInvstPo> logEqtyLinkInvstItemReader(MongoTemplate mongoTemplate) {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc(Field._id)));
        return new MongoItemReaderBuilder<LogEqtyLinkInvstPo>()
                .name("logEqtyLinkInvstItemReader")
                .template(mongoTemplate)
                .targetType(LogEqtyLinkInvstPo.class)
                .pageSize(chunkSize)
                .sorts(Collections.emptyMap())
                .collection("log_eqty_link_invst")
                .query(query)
                .build();
    }

    private LocalDateTime queryLatestUpdtDtTm(String ctryRecCde, String grpMembrRecCde) {
        Query query = new Query().addCriteria(Criteria.where(Field.parmCde).is(COGNOSCDE))
                .addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde))
                .addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        List<Document> docs = mongoTemplate.find(query, Document.class, CollectionName.sys_parm.name());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        if (docs.isEmpty()) {
            Document cognosConfig = new Document();
            cognosConfig.put(Field._id, UUID.randomUUID().toString());
            cognosConfig.put(Field.ctryRecCde, ctryRecCde);
            cognosConfig.put(Field.grpMembrRecCde, grpMembrRecCde);
            cognosConfig.put(Field.parmCde, COGNOSCDE);
            cognosConfig.put(Field.parmValueText, now.format(dateTimeFormatter));
            cognosConfig.put(Field.recCreatDtTm, now);
            cognosConfig.put(Field.recUpdtDtTm, now);
            mongoTemplate.insert(cognosConfig, CollectionName.sys_parm.name());
            return null;
        }

        Update update = new Update();
        update.set(Field.recUpdtDtTm, now);
        update.set(Field.parmValueText, now.format(dateTimeFormatter));
        mongoTemplate.updateFirst(query, update, CollectionName.sys_parm.name());

        return LocalDateTime.parse(docs.get(0).getString(Field.parmValueText), dateTimeFormatter);
    }
}
