/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.component.SpringBeansHolder;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.controller.HealthcheckController;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.properties.HealthcheckProperties;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.security.AuthenticationSuccessHandlerImpl;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.security.HealthcheckSecurityConfigurer;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.HealthcheckService;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.impl.ApplicationHealthcheckService;

@Configuration
@EnableConfigurationProperties(HealthcheckProperties.class)
@ConditionalOnProperty(prefix = "healthcheck", value = "enabled", matchIfMissing = true)
public class HealthcheckAutoConfiguration {

    @Autowired
    private HealthcheckProperties properties;

    @Bean
    @ConditionalOnMissingBean(HealthcheckSecurityConfigurer.class)
    public HealthcheckSecurityConfigurer healthcheckSecurityConfigurer() {
        final HealthcheckSecurityConfigurer healthcheckSecurityConfigurer = new HealthcheckSecurityConfigurer();
        healthcheckSecurityConfigurer.setUsername(this.properties.getUsername());
        healthcheckSecurityConfigurer.setPassword(this.properties.getPassword());
        healthcheckSecurityConfigurer.setRoles(this.properties.getRoles());
        healthcheckSecurityConfigurer.setSuccessHandler(authenticationSuccessHandler());
        return healthcheckSecurityConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(SpringBeansHolder.class)
    public SpringBeansHolder springBeansHolder() {
        return new SpringBeansHolder();
    }

    @Bean
    @ConditionalOnBean(SpringBeansHolder.class)
    @ConditionalOnMissingBean(HealthcheckService.class)
    public HealthcheckService healthcheckService() {
        final HealthcheckService healthcheckService = new HealthcheckService();
        healthcheckService.setHolder(springBeansHolder());
        return healthcheckService;
    }

    @Bean
    @ConditionalOnBean(HealthcheckService.class)
    @ConditionalOnMissingBean(HealthcheckController.class)
    public HealthcheckController healthcheckController() {
        final HealthcheckController healthcheckController = new HealthcheckController();
        healthcheckController.setHealthcheckService(healthcheckService());
        return healthcheckController;
    }

    @Bean
    @ConditionalOnMissingBean(ApplicationHealthcheckService.class)
    public ApplicationHealthcheckService applicationHealthcheckService() {
        return new ApplicationHealthcheckService();
    }

    @Bean
    public AuthenticationSuccessHandlerImpl authenticationSuccessHandler() {
        return new AuthenticationSuccessHandlerImpl();
    }

}
