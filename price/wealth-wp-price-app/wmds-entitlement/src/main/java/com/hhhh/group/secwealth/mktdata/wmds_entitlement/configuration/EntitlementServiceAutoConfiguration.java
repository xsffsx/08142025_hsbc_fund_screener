/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_entitlement.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor.EntitlementCacheKeyGenerator;
import com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor.EntitlementHandlerInterceptor;
import com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor.EntitlementInterceptorConfig;
import com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor.EntitlementService;
import com.hhhh.group.secwealth.mktdata.wmds_entitlement.properties.EntitlementProperties;

@Configuration
@EnableConfigurationProperties(EntitlementProperties.class)
@ConditionalOnProperty(prefix = "entitlement", value = "enable")
public class EntitlementServiceAutoConfiguration {

    @Autowired
    private EntitlementProperties entitlementProperties;

    @Bean
    @ConditionalOnMissingBean(EntitlementService.class)
    public EntitlementService entitlementService() {
        return new EntitlementService();
    }

    @Bean
    @ConditionalOnMissingBean(EntitlementHandlerInterceptor.class)
    public EntitlementHandlerInterceptor entitlementHandlerInterceptor() {
        EntitlementHandlerInterceptor interceptor = new EntitlementHandlerInterceptor();
        interceptor.setService(entitlementService());
        interceptor.setProp(this.entitlementProperties);
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean(EntitlementInterceptorConfig.class)
    public EntitlementInterceptorConfig entitlementInterceptorConfig() {
        EntitlementInterceptorConfig config = new EntitlementInterceptorConfig();
        config.setInterceptor(entitlementHandlerInterceptor());
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(EntitlementCacheKeyGenerator.class)
    public EntitlementCacheKeyGenerator entitlementCacheKeyGenerator() {

        EntitlementCacheKeyGenerator cacheKeyGenerator = new EntitlementCacheKeyGenerator();
        return cacheKeyGenerator;
    }
}
