package com.dummy.wpb.product;

import com.dummy.wpb.product.component.GacItemProcessor;
import com.dummy.wpb.product.component.GacItemWriter;
import com.dummy.wpb.product.component.GacJsonObjectReader;
import com.dummy.wpb.product.component.GacListener;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Map;

import static com.dummy.wpb.product.ImportGacJsonJobApplication.JOB_NAME;

@Configuration
@Slf4j
public class ImportGacJsonConfiguration {

    @Bean
    @StepScope
    public ImportFileItemReader<Map<String, Object>> securityReader(
            @Value("${batch.file.pattern}") String pattern, @Value("#{jobParameters['incomingPath']}") String incomingPath) {
        JsonItemReader<Map<String, Object>> mapJsonItemReader = new JsonItemReader<>();
        mapJsonItemReader.setJsonObjectReader(new GacJsonObjectReader());
        return new ImportFileItemReader<>(incomingPath, pattern, mapJsonItemReader);
    }

    @Bean
    public Job importGacJsonFileJob(Step theStep, JobRepository jobRepository, GacListener gacListener) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(gacListener)
                .repository(jobRepository)
                .flow(theStep)
                .end()
                .build();
    }

    @Bean
    public Step importGacJsonFileStep(PlatformTransactionManager transactionManager, JobRepository jobRepository, TaskExecutor executor,
                                      ImportFileItemReader<Map<String, Object>> securityReader, GacItemProcessor gacProcessor, GacItemWriter gacWriter) {
        return new StepBuilder("importGacJsonFileStep")
                .<Map<String,Object>, Map<String,Object>>chunk(500)
                .reader(securityReader)
                .processor(gacProcessor)
                .writer(gacWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }
}
