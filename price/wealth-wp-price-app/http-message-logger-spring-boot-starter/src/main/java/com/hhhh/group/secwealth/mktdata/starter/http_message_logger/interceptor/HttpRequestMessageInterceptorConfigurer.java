/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_message_logger.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpRequestMessageInterceptorConfigurer extends WebMvcConfigurerAdapter {

    private String urlPatterns;

    private HttpRequestMessageInterceptor interceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(this.interceptor).addPathPatterns(this.urlPatterns);
    }

}
