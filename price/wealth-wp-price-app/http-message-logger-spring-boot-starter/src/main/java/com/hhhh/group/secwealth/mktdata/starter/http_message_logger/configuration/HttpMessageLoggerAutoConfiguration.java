/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_message_logger.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.interceptor.HttpRequestMessageInterceptor;
import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.interceptor.HttpRequestMessageInterceptorConfigurer;
import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.interceptor.HttpResponseMessageAdvice;
import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.properties.HttpMessageLoggerProperties;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(HttpMessageLoggerProperties.class)
@ConditionalOnProperty(prefix = "http.message.logger", value = "enabled", matchIfMissing = true)
public class HttpMessageLoggerAutoConfiguration {

    @Autowired
    private HttpMessageLoggerProperties properties;

    @Bean
    @ConditionalOnMissingBean(HttpRequestMessageInterceptor.class)
    public HttpRequestMessageInterceptor httpRequestMessageInterceptor() {
        return new HttpRequestMessageInterceptor();
    }

    @Bean
    @ConditionalOnBean(HttpRequestMessageInterceptor.class)
    @ConditionalOnMissingBean(HttpRequestMessageInterceptorConfigurer.class)
    public HttpRequestMessageInterceptorConfigurer httpRequestMessageInterceptorConfigurer() {
        final HttpRequestMessageInterceptorConfigurer httpRequestMessageInterceptorConfigurer =
            new HttpRequestMessageInterceptorConfigurer();
        httpRequestMessageInterceptorConfigurer.setUrlPatterns(this.properties.getUrlPatterns());
        httpRequestMessageInterceptorConfigurer.setInterceptor(httpRequestMessageInterceptor());
        return httpRequestMessageInterceptorConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(HttpResponseMessageAdvice.class)
    public HttpResponseMessageAdvice httpResponseMessageAdvice() {
        final HttpResponseMessageAdvice httpResponseMessageAdvice = new HttpResponseMessageAdvice();
        httpResponseMessageAdvice.setObjectMapper(new ObjectMapper());
        return httpResponseMessageAdvice;
    }

}
