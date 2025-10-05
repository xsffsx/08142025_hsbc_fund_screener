package com.dummy.wpb.product;

import com.dummy.wpb.product.configuration.TermsheetConfiguration;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.dummy.wpb.product.ImportTermsheetJobApplication.JOB_NAME;

@Configuration
@Slf4j
public class ImportTermsheetJobConfiguration {

    @Bean
    public Job importTermsheetJob(JobRepository jobRepository, Step importTermsheetStep) {
        log.debug(JOB_NAME);
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(importTermsheetStep)
                .end()
                .build();
    }

    @Bean
    public Step importTermsheetStep(TermsheetConfiguration termsheetConfiguration,
                                    ImportTermsheetService importTermsheetService,
                                    PlatformTransactionManager transactionManager,
                                    JobRepository jobRepository) {
        return new StepBuilder("importTermsheetStep")
                .tasklet(new ImportTermsheetProcessor(termsheetConfiguration, importTermsheetService))
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

}
