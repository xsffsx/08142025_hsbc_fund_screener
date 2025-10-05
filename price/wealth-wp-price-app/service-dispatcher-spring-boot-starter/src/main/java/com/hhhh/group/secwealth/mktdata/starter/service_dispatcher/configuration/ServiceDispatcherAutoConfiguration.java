/*
 */
package com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.bean.InputParametersInvoker;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.controller.DispatcherController;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.properties.DispatcherProperties;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service.DispatcherCacheKeyGenerator;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service.DispatcherService;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.service.ServiceAdapter;

@Configuration
@EnableConfigurationProperties(DispatcherProperties.class)
@ConditionalOnProperty(prefix = "dispatcher", value = "enabled", matchIfMissing = true)
public class ServiceDispatcherAutoConfiguration {

    @Autowired
    private DispatcherProperties dispatcherProperties;

    @Bean
    @ConditionalOnMissingBean(ServiceAdapter.class)
    public ServiceAdapter serviceAdapter() {
        return new ServiceAdapter();
    }

    @Bean
    @ConditionalOnMissingBean(InputParametersInvoker.class)
    public InputParametersInvoker inputParametersInvoker() {
        return new InputParametersInvoker();
    }

    @Bean
    @ConditionalOnBean(InputParametersInvoker.class)
    @ConditionalOnMissingBean(DispatcherCacheKeyGenerator.class)
    public DispatcherCacheKeyGenerator dispatcherCacheKeyGenerator() {
        DispatcherCacheKeyGenerator keyGenerator = new DispatcherCacheKeyGenerator();
        keyGenerator.setInvoker(inputParametersInvoker());
        keyGenerator.setProp(this.dispatcherProperties);
        return keyGenerator;
    }

    @Bean
    @ConditionalOnBean(InputParametersInvoker.class)
    @ConditionalOnMissingBean(DispatcherService.class)
    public DispatcherService dispatcherService() {
        DispatcherService service = new DispatcherService();
        service.setInvoker(inputParametersInvoker());
        service.setProp(this.dispatcherProperties);
        return service;
    }

    @Bean
    @ConditionalOnBean({DispatcherService.class, ServiceAdapter.class})
    @ConditionalOnMissingBean(DispatcherController.class)
    public DispatcherController dispatcherController() {
        DispatcherController controller = new DispatcherController();
        controller.setDispatcherService(dispatcherService());
        controller.setServiceAdapter(serviceAdapter());
        return controller;
    }
}
