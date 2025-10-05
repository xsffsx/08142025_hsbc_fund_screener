/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.handler;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalArgumentsInterceptorConfigurer extends WebMvcConfigurerAdapter {

    private String argumentsUrlPatterns;

    private GlobalArgumentsInterceptor globalArgumentsInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(this.globalArgumentsInterceptor).addPathPatterns(this.argumentsUrlPatterns);
    }

}
