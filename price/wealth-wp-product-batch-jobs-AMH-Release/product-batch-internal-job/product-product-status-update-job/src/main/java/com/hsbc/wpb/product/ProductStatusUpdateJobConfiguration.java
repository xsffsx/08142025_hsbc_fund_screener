package com.dummy.wpb.product;

import com.dummy.wpb.product.builder.GraphqlServiceItemReaderBuilder;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductStatus;
import com.dummy.wpb.product.constant.ProductType;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.GraphqlServiceItemReader;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableBatchProcessing
@Slf4j
public class ProductStatusUpdateJobConfiguration {

    @Value("${batch.chunk-size:5000}")
    private Integer chunkSize;

    // product status in A, C, S
    private static final List<String> MATCHED_PRODUCT_STATUS_LIST = Arrays.asList(ProductStatus.A.name(), ProductStatus.C.name(), ProductStatus.S.name());

    @Bean
    public Job productStatusUpdateJob(
            JobRepository jobRepository,
            Step productStatusUpdateStep) {
        return new JobBuilder("Internal Calculate Product Status Code Update Job")
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(productStatusUpdateStep)
                .end()
                .build();
    }

    @Bean
    public Step productStatusUpdateStep(
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository,
            TaskExecutor executor,
            GraphqlServiceItemReader reader,
            ProductStatusUpdateJobWriter writer) {
        return new StepBuilder("productStatusUpdateStep")
                .<Document, Document>chunk(chunkSize)
                .reader(reader)
                .processor(new ProductStatusUpdateProcessor())
                .writer(writer)
                .taskExecutor(executor)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

    @Bean
    @StepScope
    public GraphqlServiceItemReader productItemReader(GraphQLService graphQLService,
                                                      @Value("#{jobParameters['ctryRecCde']}") String ctryRecCde,
                                                      @Value("#{jobParameters['grpMembrRecCde']}") String grpMembrRecCde,
                                                      @Value("#{jobParameters['prodTypeCde']}") String prodTypeCde) {
        String[] prodTypesArray = prodTypeCde.split(",");
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.prodStatCde).in(MATCHED_PRODUCT_STATUS_LIST);
        if (!Arrays.asList(prodTypesArray).contains(ProductType.ALL.name())) {
            criteria = criteria.and(Field.prodTypeCde).in(prodTypesArray);
        }
        // build graphql query
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-query.gql"));
        graphQLRequest.setVariables(Collections.singletonMap("filter", new Query(criteria).getQueryObject()));
        graphQLRequest.setDataPath("data.productByFilter");
        return new GraphqlServiceItemReaderBuilder()
                .name("productItemReader")
                .pageSize(chunkSize)
                .graphQLRequest(graphQLRequest)
                .graphQLService(graphQLService)
                .build();
    }
}
