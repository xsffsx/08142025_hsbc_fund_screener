package com.dummy.wpb.product;

import com.dummy.wpb.product.component.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.dummy.wpb.product.ImportEliFinDocJobApplication.JOB_NAME;


@Configuration
public class ImportEliFinDocConfiguration {

    //read request file job
    @Bean
    public Job importEliFinDocJob(Step importEliFinDocStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .start(importEliFinDocStep)
                .build();
    }

    @JobScope
    @Bean
    public Step importEliFinDocStep(PlatformTransactionManager transactionManager, JobRepository jobRepository, ImportEliFinDocTasklet importEliFinDocTasklet) {
        return new StepBuilder("importEliFinDocStep")
                .tasklet(importEliFinDocTasklet)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

}

