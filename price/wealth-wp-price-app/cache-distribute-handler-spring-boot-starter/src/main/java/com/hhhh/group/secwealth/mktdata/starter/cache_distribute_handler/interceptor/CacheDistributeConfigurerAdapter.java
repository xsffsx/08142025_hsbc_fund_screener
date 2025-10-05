/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Setter;

/**
 * <p>
 * <b> Register {@link CacheDistributeHandlerInterceptor}. </b>
 * </p>
 */
public class CacheDistributeConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Setter
    private HandlerInterceptor interceptor;

    @Setter
    private String[] patterns;

    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(this.interceptor).addPathPatterns(this.patterns);
    }

}
