package com.dummy.wpb.wpc.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ThreadPoolTaskExecutorConfigurationTests {

    ThreadPoolTaskExecutorConfiguration threadPoolTaskExecutorConfiguration;

    @Before
    public void setUp() {
        threadPoolTaskExecutorConfiguration = new ThreadPoolTaskExecutorConfiguration();
        threadPoolTaskExecutorConfiguration.setCorePoolSize(0);
        threadPoolTaskExecutorConfiguration.setMaxPoolSize(0);
        threadPoolTaskExecutorConfiguration.setKeepAliveSeconds(0);
    }

    @Test
    public void testGetCorePoolSize_noArgs_returnsInt() {
        int corePoolSize = threadPoolTaskExecutorConfiguration.getCorePoolSize();
        Assert.assertEquals(0, corePoolSize);
    }

    @Test
    public void testGetMaxPoolSize_noArgs_returnsInt() {
        int maxPoolSize = threadPoolTaskExecutorConfiguration.getMaxPoolSize();
        Assert.assertEquals(0, maxPoolSize);
    }

    @Test
    public void testGetKeepAliveSeconds_noArgs_returnsInt() {
        int keepAliveSeconds = threadPoolTaskExecutorConfiguration.getKeepAliveSeconds();
        Assert.assertEquals(0, keepAliveSeconds);
    }

    @Test
    public void testSetCorePoolSize_givenInt_doesNotThrow() {
        try {
            threadPoolTaskExecutorConfiguration.setCorePoolSize(1);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testSetMaxPoolSize_givenInt_doesNotThrow() {
        try {
            threadPoolTaskExecutorConfiguration.setMaxPoolSize(2);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testSetKeepAliveSeconds_givenInt_doesNotThrow() {
        try {
            threadPoolTaskExecutorConfiguration.setKeepAliveSeconds(3);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
