/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header;

import java.util.List;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Setter;

public class RequestHeaderConfigurer extends WebMvcConfigurerAdapter {

    @Setter
    private RequestHeaderHandlerMethodArgumentResolver requestHeaderHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.requestHeaderHandlerMethodArgumentResolver);
    }

}
