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

import static com.dummy.wpb.product.FinDocReleaseJobApplication.JOB_NAME;


@Configuration
public class FinDocReleaseConfiguration {

    //read request file job
    @Bean
    public Job finDocReleaseJob(Step finDocReleaseStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .start(finDocReleaseStep)
                .build();
    }

    @JobScope
    @Bean
    public Step finDocReleaseStep(PlatformTransactionManager transactionManager, JobRepository jobRepository, FinDocReleaseTasklet finDocReleaseTasklet) {
        return new StepBuilder("finDocReleaseStep")
                .tasklet(finDocReleaseTasklet)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

}

