package com.dummy.wpb.product;

import com.dummy.wpb.product.component.ActiveFileListener;
import com.dummy.wpb.product.component.ActiveFileProcesser;
import com.dummy.wpb.product.component.ActiveFileWriter;
import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import com.dummy.wpb.product.mapper.ActiveFileRowMapper;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

import static com.dummy.wpb.product.ImportEquityRecActiveJobApplication.JOB_NAME;


@Configuration
@Slf4j
public class ImportEquityRecActiveConfiguration {

    @Value("${batch.active.pattern}")
    private String activePattern;

    //read request file job
    @Bean
    public Job equityRecActiveJob(Step importActiveFileStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new ActiveFileListener())
                .repository(jobRepository)
                .flow(importActiveFileStep)
                .end()
                .build();
    }


    @JobScope
    @Bean
    public Step importActiveFileStep(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                     ActiveFileProcesser activeFileProcesser, ActiveFileWriter activeFileWriter,
                                     ImportFileItemReader<EquityRecommendationsPo> activeFileReader) {
        return new StepBuilder("importActiveFileStep")
                .<EquityRecommendationsPo, Map<EquityRecommendationsPo, List<Document>>>chunk(10)
                .reader(activeFileReader)
                .processor(activeFileProcesser)
                .writer(activeFileWriter)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public ImportFileItemReader<EquityRecommendationsPo> activeFileReader(@Value("#{jobParameters['incomingPath']}") String incomingPath) {
        PoiItemReader<EquityRecommendationsPo> activeFileReader = new PoiItemReader<>();
        activeFileReader.setLinesToSkip(1);
        activeFileReader.setRowMapper(new ActiveFileRowMapper());
        return new ImportFileItemReader<>(incomingPath, activePattern, activeFileReader);
    }

}

