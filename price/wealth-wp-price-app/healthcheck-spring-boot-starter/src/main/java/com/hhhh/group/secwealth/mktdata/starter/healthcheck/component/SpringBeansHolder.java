/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.component;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeansHolder implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> Map<String, T> getBeansOfType(final Class<T> clazz) {
        return this.applicationContext.getBeansOfType(clazz);
    }

}
