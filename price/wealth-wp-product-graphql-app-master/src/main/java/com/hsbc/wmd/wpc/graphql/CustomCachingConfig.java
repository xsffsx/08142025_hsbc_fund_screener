package com.dummy.wmd.wpc.graphql;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomCachingConfig extends CachingConfigurerSupport {

    @Override
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}
