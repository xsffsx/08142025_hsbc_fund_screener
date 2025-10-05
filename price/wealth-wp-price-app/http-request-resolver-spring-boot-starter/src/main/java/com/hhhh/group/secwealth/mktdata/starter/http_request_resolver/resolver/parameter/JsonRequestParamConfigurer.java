/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter;

import java.util.List;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Setter;

public class JsonRequestParamConfigurer extends WebMvcConfigurerAdapter {

    @Setter
    private JsonRequestParamHandlerMethodArgumentResolver jsonRequestParamHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.jsonRequestParamHandlerMethodArgumentResolver);
    }

}
