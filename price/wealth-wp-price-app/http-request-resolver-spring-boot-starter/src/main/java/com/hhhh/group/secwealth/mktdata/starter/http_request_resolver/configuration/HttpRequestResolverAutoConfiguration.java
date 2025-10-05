/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.properties.HttpRequestResolverProperties;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeaderConfigurer;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeaderHandlerMethodArgumentResolver;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParamConfigurer;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParamHandlerMethodArgumentResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(HttpRequestResolverProperties.class)
@ConditionalOnProperty(prefix = "http.request.resolver", value = "enabled", matchIfMissing = true)
public class HttpRequestResolverAutoConfiguration {

    @Autowired
    private HttpRequestResolverProperties properties;

    @Bean
    @ConditionalOnMissingBean(JsonRequestParamHandlerMethodArgumentResolver.class)
    public JsonRequestParamHandlerMethodArgumentResolver jsonRequestParamHandlerMethodArgumentResolver() {
        final JsonRequestParamHandlerMethodArgumentResolver jsonRequestParamHandlerMethodArgumentResolver =
            new JsonRequestParamHandlerMethodArgumentResolver();
        jsonRequestParamHandlerMethodArgumentResolver.setObjectMapper(new ObjectMapper());
        jsonRequestParamHandlerMethodArgumentResolver.setDefaultExCode(this.properties.getDefaultExCode());
        return jsonRequestParamHandlerMethodArgumentResolver;
    }

    @Bean
    @ConditionalOnBean(JsonRequestParamHandlerMethodArgumentResolver.class)
    @ConditionalOnMissingBean(JsonRequestParamConfigurer.class)
    public JsonRequestParamConfigurer jsonRequestParamConfigurer() {
        final JsonRequestParamConfigurer jsonRequestParamConfigurer = new JsonRequestParamConfigurer();
        jsonRequestParamConfigurer
            .setJsonRequestParamHandlerMethodArgumentResolver(jsonRequestParamHandlerMethodArgumentResolver());
        return jsonRequestParamConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(RequestHeaderHandlerMethodArgumentResolver.class)
    public RequestHeaderHandlerMethodArgumentResolver requestHeaderHandlerMethodArgumentResolver() {
        return new RequestHeaderHandlerMethodArgumentResolver();
    }

    @Bean
    @ConditionalOnBean(RequestHeaderHandlerMethodArgumentResolver.class)
    @ConditionalOnMissingBean(RequestHeaderConfigurer.class)
    public RequestHeaderConfigurer requestHeaderConfigurer() {
        final RequestHeaderConfigurer requestHeaderConfigurer = new RequestHeaderConfigurer();
        requestHeaderConfigurer.setRequestHeaderHandlerMethodArgumentResolver(requestHeaderHandlerMethodArgumentResolver());
        return requestHeaderConfigurer;
    }

}
