package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Configuration
public class BatchConfiguration {
    @Bean
    public Job gbaGenerationJob(JobRepository jobRepository, Step gbaGenerationStep) {
        return new JobBuilder("Generate GBA TXT File Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(gbaGenerationStep)
                .end()
                .build();
    }

    @Bean
    public Step gbaGenerationStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  MongoItemReader<Document> gbaIndItemReader,
                                  FlatFileItemWriter<Document> gbaIndItemWriter) {
        return new StepBuilder("gbaGenerationStep")
                .<Document, Document>chunk(1000)
                .reader(gbaIndItemReader)
                .writer(gbaIndItemWriter)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public MongoItemReader<Document> gbaIndItemReader(MongoOperations mongoTemplate,
                                                      @Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                      @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                      @Value("#{jobParameters['prodTypeCde']}") String prodTypeCde) {
        String queryJson = CommonUtils.readResource("/product-criteria.json");

        Document fields = new Document()
                .append(Field.prodAltPrimNum, 1)
                .append(Field.gbaAcctTrdb, 1)
                .append(Field.gnrAcctTrdb, 1);

        return new MongoItemReaderBuilder<Document>()
                .name("gbaIndItemReader")
                .targetType(Document.class)
                .jsonQuery(queryJson)
                .parameterValues(ctryRecCde, grpMembrRecCde, prodTypeCde)
                .collection(CollectionName.product.name())
                .sorts(Collections.singletonMap(Field.prodAltPrimNum, ASC))
                .fields(fields.toJson())
                .pageSize(1000)
                .template(mongoTemplate)
                .build();
    }

    @Bean
    @StepScope
    public GBAIndItemWriter gbaIndItemWriter() {
        return new GBAIndItemWriter();
    }
}
