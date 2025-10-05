package com.dummy.wpb.product;

import com.dummy.wpb.product.model.TableObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
public class PW0JsonFileConfiguration {

    @Bean
    public Job pw0JsonFileJob(
            JobRepository jobRepository,
            Step exportPW0JsonStep) {
        return new JobBuilder("Generate PW0 JSON File Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(exportPW0JsonStep)
                .end()
                .build();
    }

    @Bean
    public Step pw0JsonFileeStep(PlatformTransactionManager transactionManager,
                                  JobRepository jobRepository,
                                  TaskExecutor executor,
                                  PW0JsonFileReader reader,
                                  PW0JsonFileWriter writer
                                  ) {
        return new StepBuilder("pw0JsonFileStep")
                .<List<TableObject>, List<TableObject>>chunk(1)
                .reader(reader)
                .writer(writer)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @StepScope
    public PW0JsonFileWriter pw0JsonFileWriter(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                               @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                               @Value("#{jobParameters['prodTypCde']}") String prodTypCde,
                                               @Value("#{jobParameters['targetName']}") String targetName,
                                               @Value("#{jobParameters['outputPath']}") String outputPath) {
        return new PW0JsonFileWriter(ctryRecCde, grpMembrRecCde, prodTypCde, targetName, outputPath);
    }

    @Bean
    @StepScope
    public PW0JsonFileReader pw0JsonFileReader(@Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                               @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                               @Value("#{jobParameters['prodTypCde']}") String prodTypCde,
                                               @Value("#{jobParameters['targetName']}") String targetName) {
        return new PW0JsonFileReader(ctryRecCde, grpMembrRecCde, prodTypCde, targetName);
    }

}