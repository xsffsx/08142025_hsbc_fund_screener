package com.dummy.wpb.product.jobs.registration;

import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import com.dummy.wpb.product.service.SystemParameterService;

import static com.dummy.wpb.product.jobs.registration.ProductRegistrationApplication.JOB_NAME;

@Configuration
public class BatchConfiguration {

    @Bean
    public Job productRegistrationJob(JobRepository jobRepository, Step productRegistrationStep,
                                      ProductRegistrationListener listener) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .listener(listener)
                .flow(productRegistrationStep)
                .end()
                .build();
    }

    @Bean
    public ValidatingItemProcessor<Document> itemProcessor(){
        ValidatingItemProcessor<Document> validatingItemProcessor = new ValidatingItemProcessor<>();
        validatingItemProcessor.setValidator(new ProductDocumentValidator());
        validatingItemProcessor.setFilter(true);
        return validatingItemProcessor;
    }

    @Bean
    @JobScope
    public Step productRegistrationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                        ProductRegistrationReader reader, ValidatingItemProcessor<Document> itemProcessor,
                                        ProductRegistrationWriter writer, TaskExecutor executor, @Value("#{jobParameters['groupSize']}") String groupSize) {

        return new StepBuilder("productRegistrationStep")
                .<Document, Document>chunk(Integer.parseInt(groupSize))
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .taskExecutor(executor)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public ProductRegistrationListener productRegistrationServiceListener(SystemParameterService systemParameterService) {
        return new ProductRegistrationListener(systemParameterService);
    }

    @Bean
    public SystemParameterService systemParameterService(MongoOperations mongoOperations) {
        return new SystemParameterService(mongoOperations);
    }
}
