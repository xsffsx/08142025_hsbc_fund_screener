/*
 */
package com.hhhh.group.secwealth.mktdata.starter.datasource.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatasourceInterceptorConfigurer extends WebMvcConfigurerAdapter {

    private String urlPatterns;

    private DatasourceInterceptor datasourceInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(this.datasourceInterceptor).addPathPatterns(this.urlPatterns);
    }

}
