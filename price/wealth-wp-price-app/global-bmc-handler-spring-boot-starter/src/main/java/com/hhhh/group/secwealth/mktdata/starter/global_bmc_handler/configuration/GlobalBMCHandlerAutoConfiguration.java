/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCHelper;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.GlobalBMCHandler;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.properties.GlobalBMCHandlerProperties;

@Configuration
@EnableConfigurationProperties(GlobalBMCHandlerProperties.class)
@ConditionalOnProperty(prefix = "global.bmc.handler", value = "enabled", matchIfMissing = true)
public class GlobalBMCHandlerAutoConfiguration {

    @Autowired
    private GlobalBMCHandlerProperties properties;

    @Bean
    @ConditionalOnMissingBean(BMCHelper.class)
    public BMCHelper bmcHelper() {
        final BMCHelper bmcHelper = new BMCHelper();
        bmcHelper.setDefaultExCode(this.properties.getDefaultExKey());
        return bmcHelper;
    }

    @Bean
    @ConditionalOnBean(BMCHelper.class)
    @ConditionalOnMissingBean(BMCComponent.class)
    public BMCComponent bmcComponent() {
        final BMCComponent component = new BMCComponent();
        component.setPath(this.properties.getPath());
        component.setMapping(this.properties.getMapping());
        component.setConfig(this.properties.getConfig());
        component.setEntityKey(this.properties.getEntityKey());
        component.setDefaultEntityName(this.properties.getDefaultEntityName());
        component.setPrefixMessage(this.properties.getPrefixMessage());
        component.setDefaultExKey(this.properties.getDefaultExKey());
        component.setHelper(bmcHelper());
        return component;
    }

    @Bean
    @ConditionalOnBean(BMCComponent.class)
    @ConditionalOnMissingBean(GlobalBMCHandler.class)
    public GlobalBMCHandler globalBMCHandler() {
        final GlobalBMCHandler handler = new GlobalBMCHandler();
        handler.setInterceptResponse(this.properties.getInterceptResponse());
        handler.setInterceptKey(this.properties.getInterceptKey());
        handler.setComponent(bmcComponent());
        return handler;
    }

}
