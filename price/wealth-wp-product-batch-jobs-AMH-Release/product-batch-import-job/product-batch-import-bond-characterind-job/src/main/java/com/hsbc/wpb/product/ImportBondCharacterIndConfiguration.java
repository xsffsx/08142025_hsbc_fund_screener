package com.dummy.wpb.product;

import com.dummy.wpb.product.model.BondCharacter;
import com.dummy.wpb.product.writer.BondCharacterIndUpdateItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import static com.dummy.wpb.product.ImportBondCharacterIndJonApplication.JOB_NAME;

@Configuration
@Slf4j
public class ImportBondCharacterIndConfiguration {

    private static final String HEADER = "File Header";

    private static final String TRAILER = "File Trailer";

    @Bean
    public Job importBondCharacterIndJob(Step importBondCharacterIndStep, JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(importBondCharacterIndStep)
                .end()
                .build();
    }

    @Bean
    public Step importBondCharacterIndStep(PlatformTransactionManager transactionManager,
                                     JobRepository jobRepository,
                                     TaskExecutor executor,
                                     ItemStreamReader<Object> bondCharacterIndReader,
                                     BondCharacterIndUpdateItemWriter bcIndUpdateItemWriter,
                                     ItemProcessor<Object, BondCharacter> bcIndProcessor) {
        return new StepBuilder("importBondCharacterIndStep")
                .<Object, BondCharacter>chunk(100)
                .reader(bondCharacterIndReader)
                .processor(bcIndProcessor)
                .writer(bcIndUpdateItemWriter)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public ItemProcessor<Object, BondCharacter> bcIndProcessor() {
        return item -> {
            if (item instanceof BondCharacter) {
                return (BondCharacter) item;
            } else if (StringUtils.startsWithAny(item.toString(), HEADER, TRAILER)) {
                // filter header row and trailer row
                return null;
            }

            log.info("Read record to BondCharacter type fail. {}", item);
            return null;
        };
    }
}
