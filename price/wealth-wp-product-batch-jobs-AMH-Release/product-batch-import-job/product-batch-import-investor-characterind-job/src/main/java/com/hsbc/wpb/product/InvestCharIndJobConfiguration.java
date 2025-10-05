package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.model.InvestorCharacter;
import com.dummy.wpb.product.processor.InvestCharIndCommonItemProcessor;
import com.dummy.wpb.product.processor.InvestCharIndItemProcessor;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import com.dummy.wpb.product.processor.InvestCharIndDailyUpdateItemProcessor;
import com.dummy.wpb.product.reader.InvestCharFieldSetMapper;
import com.dummy.wpb.product.writer.InvestCharIndCommonItemWriter;
import com.dummy.wpb.product.writer.InvestCharIndDailyUpdateItemWriter;
import com.dummy.wpb.product.writer.InvestCharIndGraphqlItemWriter;
import com.dummy.wpb.product.processor.InvestCharMonthlyReconItemProcessor;
import com.dummy.wpb.product.writer.InvestCharIndRetryItemWriter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.dummy.wpb.product.ImportInvestorCharacterIndApplication.JOB_NAME;
import static com.dummy.wpb.product.constant.BatchConstants.DELISTED;

@Configuration
public class InvestCharIndJobConfiguration {

    private static final int BATCH_SIZE = 100;

    @Bean
    public Job investorCharacterIndJob(JobRepository jobRepository,
                                       @Qualifier("icInitStep") Step icInitStep,
                                       @Qualifier("icCommonStep") Step icCommonStep,
                                       @Qualifier("icRetryStep") Step icRetryStep,
                                       @Qualifier("icMonthlyReconStep") Step icMonthlyReconStep,
                                       @Qualifier("icDailyUpdateStep") Step icDailyUpdateStep) {
        InvestCharIndJobDecider jobDecider = new InvestCharIndJobDecider();
        SimpleFlow flow = new FlowBuilder<SimpleFlow>("investorCharacterIndFlow")
                .start(jobDecider).on("INIT").to(icInitStep)
                .from(jobDecider).on("COMMON").to(icCommonStep)
                .from(jobDecider).on("EX_RETRY").to(icRetryStep)
                .from(jobDecider).on("MONTHLY_RECONCILIATION").to(icMonthlyReconStep)
                .from(jobDecider).on("DAILY_UPDATE").to(icDailyUpdateStep)
                .from(jobDecider).on("*").end().build();
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .start(flow)
                .end()
                .build();
    }

    /**
     * init step configuration begin
     **/
    @Bean
    public Step icInitStep(@Qualifier("icSMIPFileItemReader") ImportFileItemReader<Object> icSMIPFileItemReader,
                           InvestCharIndItemProcessor icInitItemProcessor,
                           InvestCharIndGraphqlItemWriter icInitItemWriter,
                           PlatformTransactionManager transactionManager,
                           JobRepository jobRepository,
                           TaskExecutor executor) {
        return new StepBuilder("icInitStep")
                .<Object, InvestorCharacter>chunk(BATCH_SIZE)
                .reader(icSMIPFileItemReader)
                .processor(icInitItemProcessor)
                .writer(icInitItemWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository).build();
    }

    @Bean
    public InvestCharIndItemProcessor icInitItemProcessor() {
        return new InvestCharIndItemProcessor();
    }

    /**
     * common step configuration begin
     **/
    @Bean
    public Step icCommonStep(ItemStreamReader<Object> icSDIPFileItemReader,
                             @Qualifier("icCommonItemProcessor") InvestCharIndCommonItemProcessor icCommonItemProcessor,
                             @Qualifier("investCharIndCommonItemWriter") InvestCharIndCommonItemWriter icCommonItemWriter,
                             PlatformTransactionManager transactionManager,
                             JobRepository jobRepository,
                             TaskExecutor executor) {
        return new StepBuilder("icCommonStep")
                .<Object, InvestorCharacter>chunk(BATCH_SIZE)
                .reader(icSDIPFileItemReader)
                .processor(icCommonItemProcessor)
                .writer(icCommonItemWriter)
                .faultTolerant()
                .skip(InvalidRecordException.class)
                .skipLimit(Integer.MAX_VALUE)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository).build();
    }

    @Bean
    public InvestCharIndCommonItemProcessor icCommonItemProcessor(MongoTemplate mongoTemplate) {
        return new InvestCharIndCommonItemProcessor(mongoTemplate, false);
    }

    /**
     * retry step configuration begin
     **/
    @Bean
    public Step icRetryStep(MongoItemReader<InvestorCharacter> icRetryItemReader,
                            @Qualifier("icRetryItemProcessor") InvestCharIndCommonItemProcessor icRetryItemProcessor,
                            InvestCharIndRetryItemWriter icRetryItemWriter,
                            PlatformTransactionManager transactionManager,
                            JobRepository jobRepository,
                            TaskExecutor executor) {
        return new StepBuilder("icRetryStep")
                .<Object, InvestorCharacter>chunk(BATCH_SIZE)
                .reader(icRetryItemReader)
                .processor(icRetryItemProcessor)
                .writer(icRetryItemWriter)
                .faultTolerant()
                .skip(InvalidRecordException.class)
                .skipLimit(Integer.MAX_VALUE)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository).build();
    }

    @Bean
    public InvestCharIndCommonItemProcessor icRetryItemProcessor(MongoTemplate mongoTemplate) {
        return new InvestCharIndCommonItemProcessor(mongoTemplate, true);
    }

    @Bean
    public MongoItemReader<InvestorCharacter> icRetryItemReader(MongoTemplate mongoTemplate) {
        return new MongoItemReaderBuilder<InvestorCharacter>()
                .name("icRetryItemReader")
                .template(mongoTemplate)
                .query(new Query())
                .sorts(Collections.emptyMap())
                .collection(CollectionName.prod_dervt_handl_reqmt.name())
                .targetType(InvestorCharacter.class)
                .build();
    }

    /**
     * monthly reconciliation step configuration begin
     */
    @Bean
    public Step icMonthlyReconStep(@Qualifier("icSMIPFileItemReader") ImportFileItemReader<Object> icSMIPFileItemReader,
                                   InvestCharMonthlyReconItemProcessor icMonthlyReconItemProcessor,
                                   InvestCharIndGraphqlItemWriter icMonthlyReconItemWriter,
                                   PlatformTransactionManager transactionManager,
                                   JobRepository jobRepository,
                                   TaskExecutor executor) {
        return new StepBuilder("icMonthlyReconStep")
                .<Object, InvestorCharacter>chunk(BATCH_SIZE)
                .reader(icSMIPFileItemReader)
                .processor(icMonthlyReconItemProcessor)
                .writer(icMonthlyReconItemWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository).build();
    }


    @Bean
    public InvestCharMonthlyReconItemProcessor icMonthlyReconItemProcessor() {
        return new InvestCharMonthlyReconItemProcessor();
    }


    /**
     * daily update step configuration begin
     */
    @Bean
    public Step icDailyUpdateStep(@Qualifier("icDailyUpdateItemReader") MongoItemReader<InvestorCharacter> icDailyUpdateItemReader,
                                  InvestCharIndDailyUpdateItemProcessor icDailyUpdateItemProcessor,
                                  InvestCharIndDailyUpdateItemWriter icDailyUpdateItemWriter,
                                  PlatformTransactionManager transactionManager,
                                  JobRepository jobRepository,
                                  TaskExecutor executor) {
        return new StepBuilder("icDailyUpdateStep")
                .<InvestorCharacter, InvestorCharacter>chunk(BATCH_SIZE)
                .reader(icDailyUpdateItemReader)
                .processor(icDailyUpdateItemProcessor)
                .writer(icDailyUpdateItemWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository).build();
    }

    @Bean
    @StepScope
    public MongoItemReader<InvestorCharacter> icDailyUpdateItemReader(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                                      @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                                      MongoTemplate mongoTemplate) throws ParseException {
        Query query = new Query()
                .addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde))
                .addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde))
                .addCriteria(Criteria.where(Field.prodStatCde).ne(DELISTED))
                .addCriteria(Criteria.where(Field.prdDervRvsEffDt).is(DateUtils.parseDate(LocalDate.now().toString(), "yyyy-MM-dd")));
        query.fields().include(Field.ctryRecCde, Field.grpMembrRecCde, Field.prodAltPrimNum, Field.prodTypeCde, Field.prodDervtCde, Field.prodDervtRvsCde);
        return new MongoItemReaderBuilder<InvestorCharacter>()
                .name("icDailyUpdateItemReader")
                .template(mongoTemplate)
                .query(query)
                .sorts(Collections.emptyMap())
                .collection(CollectionName.product.name())
                .targetType(InvestorCharacter.class)
                .build();
    }

    @Bean
    public InvestCharIndDailyUpdateItemProcessor icDailyUpdateItemProcessor() {
        return new InvestCharIndDailyUpdateItemProcessor();
    }

    @Bean
    public InvestCharIndDailyUpdateItemWriter icDailyUpdateItemWriter() {
        return new InvestCharIndDailyUpdateItemWriter();
    }

    /**
     * daily update step configuration end
     */

    @Bean
    public InvestCharIndGraphqlItemWriter icGraphqlItemWriter() {
        return new InvestCharIndGraphqlItemWriter();
    }

    @Bean
    @StepScope
    public ImportFileItemReader<Object> icSMIPFileItemReader(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                             @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                             @Value("#{jobParameters['incomingPath']}") String incomingPath) {
        return createICAggregateItemReader(ctryRecCde, grpMembrRecCde, incomingPath, "^RS@SMIP.IC[0-9]{6}$");
    }

    @Bean
    @StepScope
    public ImportFileItemReader<Object> icSDIPFileItemReader(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                             @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                             @Value("#{jobParameters['incomingPath']}") String incomingPath) {
        return createICAggregateItemReader(ctryRecCde, grpMembrRecCde, incomingPath, "^RS@SDIP.IC[0-9]{6}$");
    }

    private ImportFileItemReader<Object> createICAggregateItemReader(String ctryRecCde, String grpMembrRecCde, String incomingPath, String filePattern) {
        FixedLengthTokenizer headerTokenizer = new FixedLengthTokenizer();
        headerTokenizer.setNames("headerIdentifier", "hederContent");
        headerTokenizer.setColumns(new Range(1, 7), new Range(8));

        FixedLengthTokenizer tailerTokenizer = new FixedLengthTokenizer();
        tailerTokenizer.setNames("trailerIdentifier", "trailerContent");
        tailerTokenizer.setColumns(new Range(1, 7), new Range(8));

        FixedLengthTokenizer icCheckTokenizer = new FixedLengthTokenizer();
        icCheckTokenizer.setNames("maintCode", "prodCde", "prodTypeCde", "prodMktCde", "currentICCheckActCde", "icCheckActCde",
                "icCheckEffDate", "hfiProdType", "hfiProdSubType", "recUpdtDtTm");
        icCheckTokenizer.setColumns(new Range(1, 1), new Range(2, 13), new Range(14, 17), new Range(18, 29), new Range(30, 30),
                new Range(31, 31), new Range(32, 39), new Range(40, 54), new Range(55, 69), new Range(70));

        Map<String, LineTokenizer> tokenizers = new LinkedHashMap<>();
        tokenizers.put("HEADER*", headerTokenizer);
        tokenizers.put("TRAILER*", tailerTokenizer);
        tokenizers.put("*", icCheckTokenizer);

        PatternMatchingCompositeLineMapper<Object> lineMapper = new PatternMatchingCompositeLineMapper<>();
        lineMapper.setTokenizers(tokenizers);
        lineMapper.setFieldSetMappers(Collections.singletonMap("*", new InvestCharFieldSetMapper(ctryRecCde, grpMembrRecCde)));

        FlatFileItemReader<Object> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setLineMapper(lineMapper);
        return new ImportFileItemReader<>(incomingPath, filePattern, flatFileItemReader);
    }
}
