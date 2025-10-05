package com.dummy.wpb.product;

import com.dummy.wpb.product.configuration.GraphQLConfiguration;
import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.listener.ProductFormatProcessorListener;
import com.dummy.wpb.product.listener.SystemCdeCheckListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@AutoConfigureAfter(GraphQLConfiguration.class)
public class BatchConfiguration {
    @ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotEmpty('${system-update-config[0].systemCde:}')")
    @Bean
    @ConfigurationProperties
    public SystemUpdateConfigHolder systemUpdateConfigHolder() {
        return new SystemUpdateConfigHolder();
    }

    @ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotEmpty('${system-default-values[0].systemCde:}')")
    @Bean
    @ConfigurationProperties
    public SystemDefaultValuesHolder systemDefaultValuesHolder() {
        return new SystemDefaultValuesHolder();
    }

    @Bean
    public SystemCdeCheckListener systemCdeCheckListener() {
        return new SystemCdeCheckListener();
    }

    @Bean
    @StepScope
    @ConditionalOnMissingBean(ProductFormatProcessorListener.class)
    public ProductFormatProcessorListener productFormatProcessor() {
        return new ProductFormatProcessorListener();
    }
}
