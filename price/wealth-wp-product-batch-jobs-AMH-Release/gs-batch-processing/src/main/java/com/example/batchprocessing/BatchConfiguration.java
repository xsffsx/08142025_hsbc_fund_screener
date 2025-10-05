package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    // tag::readerwriterprocessor[]
    @Bean
    @StepScope
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<Person> writer() {
        return new ItemWriter<Person>() {

            /**
             * Process the supplied data element. Will not be called with any null items
             * in normal operation.
             *
             * @param items items to be written
             * @throws Exception if there are errors. The framework will catch the
             *                   exception and convert or rethrow it as appropriate.
             */
            @Override
            public void write(List<? extends Person> items) throws Exception {
                items.forEach(System.out::println);
            }
        };
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(Step step1, JobRepository jobRepository) {
        return new JobBuilder("Import User Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(ItemWriter<Person> writer, JobRepository jobRepository,PlatformTransactionManager transactionManager,FlatFileItemReader<Person> reader) {
        return new StepBuilder("step1")
                .<Person, Person>chunk(10)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    // end::jobstep[]
}
