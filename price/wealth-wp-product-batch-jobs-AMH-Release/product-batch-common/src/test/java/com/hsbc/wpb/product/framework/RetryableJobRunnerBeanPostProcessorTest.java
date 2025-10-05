package com.dummy.wpb.product.framework;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;

public class RetryableJobRunnerBeanPostProcessorTest {

    RetryableJobRunnerBeanPostProcessor retryableJobRunnerBeanPostProcessor = new RetryableJobRunnerBeanPostProcessor();

    @Test
    public void testPostProcessAfterInitialization() {
        Object object = new Object();
        Assert.assertEquals(object, retryableJobRunnerBeanPostProcessor.postProcessAfterInitialization(object, "object"));

        JobLauncherApplicationRunner jobRunner = new JobLauncherApplicationRunner(Mockito.mock(JobLauncher.class), Mockito.mock(JobExplorer.class), Mockito.mock(JobRepository.class));

        Object postJobRunner = retryableJobRunnerBeanPostProcessor.postProcessAfterInitialization(jobRunner, "jobRunner");
        Assert.assertTrue(AopUtils.isJdkDynamicProxy(postJobRunner));
        Assert.assertNotEquals(jobRunner, postJobRunner);
        Assert.assertEquals(jobRunner, AopProxyUtils.getSingletonTarget(postJobRunner));
    }
}
