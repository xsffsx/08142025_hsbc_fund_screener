package com.dummy.wpb.product.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * Because some jobs do not pass parameters in the standard way of spring batch(like GFIX-JOB),
 * it may cause an {@link org.springframework.dao.CannotSerializeTransactionException} when getting the md5 of
 * {@link org.springframework.batch.core.JobInstance} according JobParameters.
 * */
@Component
@Slf4j
public class RetryableJobRunnerBeanPostProcessor implements BeanPostProcessor {

    private static final int MAX_ATTEMPTS = 3;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof JobLauncherApplicationRunner)) {
            return bean;
        }

        JobLauncherApplicationRunner runner = (JobLauncherApplicationRunner) bean;
        RetryTemplate retryTemplate = RetryTemplate.builder()
                .maxAttempts(MAX_ATTEMPTS)
                .retryOn(TransientDataAccessException.class)
                .withListener(new LogRetryInfoListener())
                .build();

        RetryOperationsInterceptor retryInterceptor = RetryInterceptorBuilder
                .stateless()
                .retryOperations(retryTemplate)
                .build();

        ProxyFactory proxyFactory = new ProxyFactory();
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(retryInterceptor);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.addMethodName("run");
        advisor.setPointcut(pointcut);
        proxyFactory.addAdvisor(advisor);
        proxyFactory.addInterface(ApplicationRunner.class);
        proxyFactory.setTarget(runner);
        return proxyFactory.getProxy(getClass().getClassLoader());
    }

    private static final class LogRetryInfoListener extends RetryListenerSupport {
        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
            log.error("{} Occurred, Retry count: {}", throwable.getClass().getSimpleName(), context.getRetryCount());
        }
    }
}
