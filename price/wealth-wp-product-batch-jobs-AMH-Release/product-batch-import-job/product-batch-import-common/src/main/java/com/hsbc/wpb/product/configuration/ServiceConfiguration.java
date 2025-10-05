package com.dummy.wpb.product.configuration;

import com.dummy.wpb.product.service.AmendmentService;
import com.dummy.wpb.product.service.FinDocCollectionsService;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.service.impl.DefaultProductFormatService;
import com.dummy.wpb.product.util.LegacyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@ConditionalOnBean(GraphQLService.class)
@AutoConfigureAfter(GraphQLConfiguration.class)
@Configuration
public class ServiceConfiguration {

    @Autowired
    GraphQLService graphQLService;

    @Bean
    @Lazy
    public AmendmentService amendmentService(){
        return new AmendmentService(graphQLService);
    }

    @Bean
    @Lazy
    public FinDocCollectionsService finDocCollectionsService(){
        return new FinDocCollectionsService(graphQLService);
    }

    @Bean
    public LegacyConfig legacyConfig(){
        return new LegacyConfig(graphQLService);
    }

    @Bean
    @Lazy
    public ProductFormatService productFormatService(){
        return new DefaultProductFormatService(graphQLService);
    }
}
