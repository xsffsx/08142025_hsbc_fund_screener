package com.dummy.wpb.product.framework;

import com.dummy.wpb.product.error.FailedStatusJobListener;
import com.dummy.wpb.product.error.InvalidRecordAlertJobListener;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class JobListenerRegisterPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractJob) {
            AbstractJob job = (AbstractJob) bean;
            job.registerJobExecutionListener(new FailedStatusJobListener());
            job.registerJobExecutionListener(new InvalidRecordAlertJobListener());
        }
        return bean;
    }
}
