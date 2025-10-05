package com.dummy.wpb.product.batch;

import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.processor.WisdomBondProcessor;
import com.dummy.wpb.product.reader.WisdomBondItemReader;
import com.dummy.wpb.product.writer.WisdomBondItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.dummy.wpb.product.ImportBondWisdomJobApplication.JOB_NAME;

@Configuration
@Slf4j
public class ImportWisdomBondXmlConfiguration {

    @Value("${batch.size}")
    private int batchSize;

    @Bean
    public CleanTopSellRankListener cleanTopSellRank() {
        return new CleanTopSellRankListener();
    }

    @Bean
    public Job importWisdomBondXmlFileJob(CleanTopSellRankListener cleanTopSellRankListener,
                                          Step importWisdomBondXmlFileStep,
                                          JobRepository jobRepository,
                                          JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory
                .get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(cleanTopSellRankListener)
                .flow(importWisdomBondXmlFileStep)
                .end()
                .build();
    }

    @Bean
    public Step importWisdomBondXmlFileStep(PlatformTransactionManager transactionManager,
                                            JobRepository jobRepository,
                                            WisdomBondItemReader wisdomBondXmlFileReader,
                                            WisdomBondProcessor wisdomBondProcessor,
                                            WisdomBondItemWriter wisdomBondItemWriter) {
        return new StepBuilder("importWisdomBondXmlFileStep")
                .<ProductStreamItem, ProductStreamItem>chunk(batchSize)
                .reader(wisdomBondXmlFileReader)
                .processor(wisdomBondProcessor)
                .writer(wisdomBondItemWriter)
                .faultTolerant()
                .skip(InvalidRecordException.class)
                .skipLimit(Integer.MAX_VALUE)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @StepScope
    public WisdomBondItemWriter wisdomBondItemWriter(){
        return new WisdomBondItemWriter();
    }
}
