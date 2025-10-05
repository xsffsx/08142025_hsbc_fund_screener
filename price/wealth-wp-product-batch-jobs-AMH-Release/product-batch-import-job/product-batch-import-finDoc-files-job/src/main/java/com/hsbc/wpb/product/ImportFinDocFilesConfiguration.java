package com.dummy.wpb.product;

import com.dummy.wpb.product.component.*;
import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.mapper.FinDocCsvLineMapper;
import com.dummy.wpb.product.mapper.FinDocExlRowMapper;
import com.dummy.wpb.product.model.FinDocInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import static com.dummy.wpb.product.ImportFinDocFilesJobApplication.JOB_NAME;


@Configuration
@Slf4j
public class ImportFinDocFilesConfiguration {

    private static final String COMPLETED = "COMPLETED";

    //read request file job
    @Bean
    public Job finDocFilesUploadJob(Step importFinDocFilesStep, JobRepository jobRepository, FileFormatDecider fileFormatDecider,
                                    Step importFinDocFilesWithExcelStep, Step importFinDocFilesWithCsvStep, Step finishFinDocFilesStep,
                                    FinDocFilesUploadFailListener finDocFilesUploadFailListener) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(finDocFilesUploadFailListener)
                .start(importFinDocFilesStep)
                .next(fileFormatDecider)
                .from(fileFormatDecider).on("Excel").to(importFinDocFilesWithExcelStep)
                .from(fileFormatDecider).on("CSV").to(importFinDocFilesWithCsvStep)
                .from(fileFormatDecider).on(COMPLETED).end()
                .from(importFinDocFilesWithExcelStep).on(COMPLETED).to(finishFinDocFilesStep)
                .from(importFinDocFilesWithCsvStep).on(COMPLETED).to(finishFinDocFilesStep)
                .from(finishFinDocFilesStep).on("*").to(fileFormatDecider)
                .end()
                .build();
    }

    @JobScope
    @Bean
    public Step importFinDocFilesStep(PlatformTransactionManager transactionManager, JobRepository jobRepository, ReadFileNameTasklet readFileNameTasklet) {
        return new StepBuilder("importFinDocFilesStep")
                .tasklet(readFileNameTasklet)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }


    @JobScope
    @Bean
    public Step importFinDocFilesWithExcelStep(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                      FinDocFilesProcessor finDocFilesProcessor, FinDocFilesWriter finDocFilesWriter,
                                               PoiItemReader<FinDocInput> finDocReqExcelReader) {
        SkipPolicy skipPolicy = (t, skipCount) -> t.getCause() instanceof InvalidRecordException;
        return new StepBuilder("importFinDocFilesWithExcelStep")
                .<FinDocInput,FinDocInput>chunk(1)
                .reader(finDocReqExcelReader)
                .processor(finDocFilesProcessor)
                .writer(finDocFilesWriter)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .skipLimit(Integer.MAX_VALUE)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @JobScope
    @Bean
    public Step importFinDocFilesWithCsvStep(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                               FinDocFilesProcessor finDocFilesProcessor, FinDocFilesWriter finDocFilesWriter,
                                             FlatFileItemReader<FinDocInput> finDocReqCsvReader) {
        return new StepBuilder("importFinDocFilesWithCsvStep")
                .<FinDocInput,FinDocInput>chunk(10)
                .reader(finDocReqCsvReader)
                .processor(finDocFilesProcessor)
                .writer(finDocFilesWriter)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @JobScope
    @Bean
    public Step finishFinDocFilesStep(PlatformTransactionManager transactionManager, JobRepository jobRepository, FinishFileTasklet finishFileTasklet) {
        return new StepBuilder("finishFinDocFilesStep")
                .tasklet(finishFileTasklet)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<FinDocInput> finDocReqExcelReader(@Value("#{jobExecutionContext['curFilePath']}") String curFilePath) {
        PoiItemReader<FinDocInput> finDocExlReader = new PoiItemReader<>();
        finDocExlReader.setLinesToSkip(3);
        finDocExlReader.setResource(new PathResource(curFilePath));
        finDocExlReader.setRowMapper(new FinDocExlRowMapper());

        return finDocExlReader;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<FinDocInput> finDocReqCsvReader(@Value("#{jobExecutionContext['curFilePath']}") String curFilePath) {
        FlatFileItemReader<FinDocInput> csvReader = new FlatFileItemReader<>();
        csvReader.setSaveState(false);
        csvReader.setResource(new PathResource(curFilePath));
        csvReader.setLinesToSkip(2);
        csvReader.setLineMapper(new FinDocCsvLineMapper());

        return csvReader;
    }

}

