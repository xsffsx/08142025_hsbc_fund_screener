package com.dummy.wpb.product;

import com.dummy.wpb.product.component.CoverageFileListener;
import com.dummy.wpb.product.component.CoverageFileWriter;
import com.dummy.wpb.product.component.EquityRecommendationsPoValidator;
import com.dummy.wpb.product.mapper.CoverageFileRowMapper;
import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import com.dummy.wpb.product.reader.ImportFileItemReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@Slf4j
public class ImportEquityRecCovConfiguration {

    @Value("${batch.coverage.pattern}")
    private String coveragePattern;

    //read request file job
    @Bean
    public Job equityRecCoverageJob(Step importCoverageFileStep, JobRepository jobRepository, CoverageFileListener coverageFileListener) {

        return new JobBuilder("Import Equity Recom Coverage EXCEL Job")
                .incrementer(new RunIdIncrementer())
                .listener(coverageFileListener)
                .repository(jobRepository)
                .flow(importCoverageFileStep)
                .end()
                .build();
    }


    @JobScope
    @Bean
    public Step importCoverageFileStep(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                       CoverageFileWriter coverageFileWriter,
                                       ImportFileItemReader<EquityRecommendationsPo> coverageFileReader) {
        return new StepBuilder("importCoverageFileStep")
                .<EquityRecommendationsPo, EquityRecommendationsPo>chunk(100)
                .reader(coverageFileReader)
                .processor(validateProcessor())
                .writer(coverageFileWriter)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public ImportFileItemReader<EquityRecommendationsPo> coverageFileReader(@Value("#{jobParameters['incomingPath']}") String incomingPath) {
        PoiItemReader<EquityRecommendationsPo> coverageFileReader = new PoiItemReader<>();
        coverageFileReader.setLinesToSkip(3);
        coverageFileReader.setRowMapper(new CoverageFileRowMapper());
        return new ImportFileItemReader<>(incomingPath, coveragePattern, coverageFileReader);
    }

    @Bean
    @StepScope
    public ValidatingItemProcessor<EquityRecommendationsPo> validateProcessor() {
        ValidatingItemProcessor<EquityRecommendationsPo> equityRecPoValidatingItemProcessor = new ValidatingItemProcessor<>();
        equityRecPoValidatingItemProcessor.setValidator(new EquityRecommendationsPoValidator());
        equityRecPoValidatingItemProcessor.setFilter(true);
        return equityRecPoValidatingItemProcessor;
    }

}

