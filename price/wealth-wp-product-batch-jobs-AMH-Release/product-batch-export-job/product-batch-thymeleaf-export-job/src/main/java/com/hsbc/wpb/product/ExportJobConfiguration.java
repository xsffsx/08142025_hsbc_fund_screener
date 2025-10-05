package com.dummy.wpb.product;

import com.dummy.wpb.product.batch.StepFactory;
import com.dummy.wpb.product.exception.FileResourceException;
import com.dummy.wpb.product.model.ExportRequest;
import com.dummy.wpb.product.service.SystemParameterService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@Slf4j
public class ExportJobConfiguration {
    @Bean
    public Job exportJob(JobRepository jobRepository,
                         StepFactory stepFactory,
                         TaskExecutor taskExecutor,
                         List<JobExecutionListener> listeners) {
        SimpleFlow[] exportflows = stepFactory.createStep().stream().map(step -> new FlowBuilder<SimpleFlow>(step.getName()).start(step).build()).toArray(SimpleFlow[]::new);
        SimpleFlow splitFlow = new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor)

                .add(exportflows)
                .build();

        JobBuilder jobBuilder = new JobBuilder("Export Thymeleaf Job").repository(jobRepository).preventRestart().incrementer(new RunIdIncrementer());
        listeners.forEach(jobBuilder::listener);
        return jobBuilder
                .start(splitFlow)
                .build()
                .build();
    }

    @Bean
    public SystemParameterService systemParameterService(MongoTemplate mongoTemplate){
        return new SystemParameterService(mongoTemplate);
    }

    @Bean
    public ExportRequest exportRequest(@Value("${nonOptionArgs}") String filePath) {
        String content;
        try {
            // Try reading from the absolute path
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            // If reading from the absolute path fails, try the relative path
            content = CommonUtils.readResource(filePath);
        }

        try {
            return JsonUtil.convertJson2Object(content, ExportRequest.class);
        } catch (Exception e) {
            throw new FileResourceException("Failed to read file: " + filePath, e);
        }
    }
}
