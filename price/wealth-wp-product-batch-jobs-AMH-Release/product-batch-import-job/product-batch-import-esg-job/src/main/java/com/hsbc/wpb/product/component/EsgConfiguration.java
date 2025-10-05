package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.EsgConstants;
import com.dummy.wpb.product.model.EsgDataItem;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import static com.dummy.wpb.product.constant.EsgConstants.JOB_NAME;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class EsgConfiguration {
    @Bean
    public Job esgImportJob(JobRepository jobRepository, Step esgImportStep) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new EsgListener())
                .repository(jobRepository)
                .flow(esgImportStep)
                .end()
                .build();
    }
    @Bean
    public Step esgImportStep(
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository,
            ImportFileItemReader<EsgDataItem> reader,
            TaskExecutor executor,
            EsgWriter writer,
            EsgProcessor processor) {
        return new StepBuilder("esgImportStep")
                .<EsgDataItem, List<Document>>chunk(500)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @StepScope
    public ImportFileItemReader<EsgDataItem> esgReader(@Value("#{jobParameters['incomingPath']}") String incomingPath,
                                                                  @Value("#{jobParameters['filePattern']}") String filePattern) {

        FlatFileItemReader<EsgDataItem> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(EsgConstants.nameList);
        BeanWrapperFieldSetMapper<EsgDataItem> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(EsgDataItem.class);
        DefaultLineMapper<EsgDataItem> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(tokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(defaultLineMapper);
        return new ImportFileItemReader<>(incomingPath, filePattern, reader);
    }

}
