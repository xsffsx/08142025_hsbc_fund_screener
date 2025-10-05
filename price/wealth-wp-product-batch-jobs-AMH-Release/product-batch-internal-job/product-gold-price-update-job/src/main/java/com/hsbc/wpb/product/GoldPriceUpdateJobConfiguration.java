package com.dummy.wpb.product;

import com.dummy.wpb.product.builder.GraphqlServiceItemReaderBuilder;
import com.dummy.wpb.product.config.TokenizedGoldProductConfig;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductStatus;
import com.dummy.wpb.product.entity.TokenizedGoldProduct;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.GraphqlServiceItemReader;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.writer.ProductImportItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dummy.wpb.product.GoldPriceUpdateJobApplication.JOB_NAME;

@Configuration
@Slf4j
public class GoldPriceUpdateJobConfiguration {

    @Value("${batch.chunk-size:5000}")
    private Integer chunkSize;

    @Bean
    public Job goldPriceUpdateJob(
            JobRepository jobRepository,
            Step goldPriceUpdateStep) {
        return new JobBuilder(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .repository(jobRepository)
                .flow(goldPriceUpdateStep)
                .end()
                .build();
    }

    @Bean
    public Step goldPriceUpdateStep(
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository,
            TaskExecutor executor,
            GraphqlServiceItemReader reader,
            ProductImportItemWriter writer,
            GoldPriceUpdateJobProcessor processor) {
        return new StepBuilder("goldPriceUpdateStep")
                .<Document, ProductStreamItem>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
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
                                                      TokenizedGoldProductConfig tokenizedGoldProductConfig) {
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.prodStatCde).ne(ProductStatus.D.name());
        // query tokenized gold products by id
        List<TokenizedGoldProduct> products = tokenizedGoldProductConfig.getProducts();
        List<Long> prodIds = new ArrayList<>();
        for (TokenizedGoldProduct product : products) {
            prodIds.add(product.getProdId());
        }
        criteria.and(Field.prodId).in(prodIds);

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

    @Bean
    public ProductImportItemWriter productImportItemWriter(ProductService productService) {
        return new ProductImportItemWriter(productService);
    }
}
