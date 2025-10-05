/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.Setter;

@Configuration
public class EntitlementInterceptorConfig extends WebMvcConfigurerAdapter {

    @Value("${entitlement.enable}")
    private boolean entitlementEnable;

    @Value("${entitlement.interceptor-url}")
    private String interceptorUrl;

    @Setter
    private EntitlementHandlerInterceptor interceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {

        if (this.entitlementEnable) {
            registry.addInterceptor(this.interceptor).addPathPatterns(this.interceptorUrl);
            super.addInterceptors(registry);
        }
    }
}
