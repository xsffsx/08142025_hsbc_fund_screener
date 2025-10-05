package com.dummy.wpb.product;

import com.dummy.wpb.product.component.*;
import com.dummy.wpb.product.mapper.FinDocMappingExlRowMapper;
import com.dummy.wpb.product.model.FinDocMapInput;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.List;

import static com.dummy.wpb.product.ImportFinDocMappingJobApplication.JOB_NAME;


@Configuration
public class ImportFinDocMappingConfiguration {
    
    //read request file job
    @Bean
	public Job finDocMappingUploadJob(JobRepository jobRepository,
			Step importFinDocMappingWithExcelStep,
			FinDocMappingUploadJoblistener finDocMappingUploadJoblistener) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .start(importFinDocMappingWithExcelStep)
                .listener(finDocMappingUploadJoblistener)
                .build();
    }

    @Bean
    @JobScope
    public Step importFinDocMappingWithExcelStep(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                      FinDocFilesProcessor finDocFilesProcessor, FinDocFilesWriter finDocFilesWriter,
                                      MultiResourceItemReader<FinDocMapInput> multiResourceItemReader) {
        return new StepBuilder("importFinDocMappingWithExcelStep")
                .<FinDocMapInput,FinDocMapInput>chunk(100)
                .reader(multiResourceItemReader)
                .processor(finDocFilesProcessor)
                .writer(finDocFilesWriter)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<FinDocMapInput> finDocMappingReqExcelReader() {
        PoiItemReader<FinDocMapInput> finDocMappingExlReader = new PoiItemReader<>();
        finDocMappingExlReader.setLinesToSkip(3);
        finDocMappingExlReader.setRowMapper(new FinDocMappingExlRowMapper());

        return finDocMappingExlReader;
    }
    
    @Bean
    @StepScope
    public MultiResourceItemReader<FinDocMapInput> multiResourceItemReader(@Value("#{jobExecutionContext['mappingFilePaths']}") List<String> mappingFilePaths) {
        MultiResourceItemReader<FinDocMapInput> resourceItemReader = new MultiResourceItemReader<>();
        Resource[] resources = mappingFilePaths.stream().map(FileSystemResource::new).toArray(Resource[]::new);
        resourceItemReader.setResources(resources);
        resourceItemReader.setDelegate(finDocMappingReqExcelReader());

        return resourceItemReader;
    }

}

