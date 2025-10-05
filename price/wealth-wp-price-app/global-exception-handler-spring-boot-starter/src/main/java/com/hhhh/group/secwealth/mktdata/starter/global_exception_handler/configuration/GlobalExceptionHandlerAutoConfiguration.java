/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.handler.GlobalExceptionHandler;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.properties.GlobalExceptionHandlerProperties;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;

@Configuration
@EnableConfigurationProperties(GlobalExceptionHandlerProperties.class)
@ConditionalOnProperty(prefix = "global.exception.handler", value = "enabled", matchIfMissing = true)
public class GlobalExceptionHandlerAutoConfiguration {

    @Autowired
    private GlobalExceptionHandlerProperties properties;

    @Bean
    @ConditionalOnMissingBean(ExResponseComponent.class)
    public ExResponseComponent exResponseComponent() {
        final ExResponseComponent exResponseComponent = new ExResponseComponent();
        exResponseComponent.setExResponsePath(this.properties.getPath());
        exResponseComponent.setExResponseMapping(this.properties.getMapping());
        exResponseComponent.setExResponseConfig(this.properties.getConfig());
        exResponseComponent.setDefaultExCode(this.properties.getDefaultExCode());
        return exResponseComponent;
    }

    @Bean
    @ConditionalOnBean(ExResponseComponent.class)
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        globalExceptionHandler.setExResponseComponent(exResponseComponent());
        return globalExceptionHandler;
    }

}
