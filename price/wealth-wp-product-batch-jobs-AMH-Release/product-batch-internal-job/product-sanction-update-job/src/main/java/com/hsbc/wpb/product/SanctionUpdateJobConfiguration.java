package com.dummy.wpb.product;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class SanctionUpdateJobConfiguration {

    public static final String JOB_NAME = "Internal Sanction Buy And Sell List Update Job";

    public static final String STEP_NAME = "Internal Sanction Buy And Sell List Update Step";

    @Bean
    public Job sanctionUpdateJob(JobRepository jobRepository, Step sanctionsUpdateStep) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(sanctionsUpdateStep)
                .end()
                .build();
    }

    @Bean
    public Step sanctionsUpdateStep(
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository,
            SanctionUpdateTasklet tasklet) {
        return new StepBuilder(STEP_NAME)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }
}
