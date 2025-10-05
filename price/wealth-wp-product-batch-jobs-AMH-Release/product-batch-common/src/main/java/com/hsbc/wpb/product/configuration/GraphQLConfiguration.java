package com.dummy.wpb.product.configuration;


import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductPriceHistoryService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.service.impl.DefaultProductPriceHistoryService;
import com.dummy.wpb.product.service.impl.DefaultProductService;
import com.dummy.wpb.product.service.impl.DefaultReferenceDataService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@ConditionalOnProperty(name = "batch.graphQLUrl")
@EnableRetry
public class GraphQLConfiguration {
    @Bean
    public GraphQLService graphQLService() {
        return new GraphQLService();
    }

    @Bean
    @ConditionalOnMissingBean(ProductService.class)
    @Lazy
    public ProductService productService() {
        return new DefaultProductService(graphQLService());
    }

    @Bean
    @ConditionalOnMissingBean(ReferenceDataService.class)
    @Lazy
    public ReferenceDataService referenceDataService() {
        return new DefaultReferenceDataService(graphQLService());
    }

    @Bean
    @ConditionalOnMissingBean(ProductPriceHistoryService.class)
    @Lazy
    public ProductPriceHistoryService productPriceHistoryService() {
        return new DefaultProductPriceHistoryService(graphQLService());
    }
}
