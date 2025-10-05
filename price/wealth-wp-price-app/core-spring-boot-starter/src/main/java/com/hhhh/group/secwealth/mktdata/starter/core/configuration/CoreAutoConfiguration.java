/*
 */
package com.hhhh.group.secwealth.mktdata.starter.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.core.properties.CoreProperties;

@Configuration
@EnableConfigurationProperties(CoreProperties.class)
@ConditionalOnProperty(prefix = "core", value = "enabled", matchIfMissing = true)
public class CoreAutoConfiguration {

    @Autowired
    private CoreProperties properties;

    @Bean("defaultInvalidResponseExCode")
    public String defaultInvalidResponseExCode() {
        return this.properties.getDefaultInvalidResponseExCode();
    }

}
