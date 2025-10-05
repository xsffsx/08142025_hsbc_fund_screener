/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.handler.GlobalArgumentsInterceptor;
import com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.handler.GlobalArgumentsInterceptorConfigurer;
import com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.properties.GlobalArgumentsHandlerProperties;

@Configuration
@EnableConfigurationProperties(GlobalArgumentsHandlerProperties.class)
@ConditionalOnProperty(prefix = "global.arguments.handler", value = "enabled", matchIfMissing = true)
public class GlobalArgumentsHandlerAutoConfiguration {

    @Autowired
    private GlobalArgumentsHandlerProperties properties;

    @Bean
    @ConditionalOnMissingBean(GlobalArgumentsInterceptor.class)
    public GlobalArgumentsInterceptor globalArgumentsInterceptor() {
        final GlobalArgumentsInterceptor globalArgumentsInterceptor = new GlobalArgumentsInterceptor();
        globalArgumentsInterceptor.setArgumentsPatterns(this.properties.getPatterns());
        globalArgumentsInterceptor.setDefaultExCode(this.properties.getDefaultExCode());
        return globalArgumentsInterceptor;
    }

    @Bean
    @ConditionalOnBean(GlobalArgumentsInterceptor.class)
    @ConditionalOnMissingBean(GlobalArgumentsInterceptorConfigurer.class)
    public GlobalArgumentsInterceptorConfigurer globalArgumentsInterceptorConfigurer() {
        final GlobalArgumentsInterceptorConfigurer globalArgumentsInterceptorConfigurer =
            new GlobalArgumentsInterceptorConfigurer();
        globalArgumentsInterceptorConfigurer.setArgumentsUrlPatterns(this.properties.getUrlPatterns());
        globalArgumentsInterceptorConfigurer.setGlobalArgumentsInterceptor(globalArgumentsInterceptor());
        return globalArgumentsInterceptorConfigurer;
    }

}
