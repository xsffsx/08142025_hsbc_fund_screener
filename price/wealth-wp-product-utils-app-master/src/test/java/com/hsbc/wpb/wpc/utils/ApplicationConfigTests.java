package com.dummy.wpb.wpc.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.BlockingQueue;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConfigTests {

    @InjectMocks
    private ApplicationConfig applicationConfig;
    @Mock
    private ThreadPoolTaskExecutorConfiguration threadPoolTaskExecutorConfiguration;
    @Mock
    private MongoClient mongoClient;

    @Test
    public void testGetBlockingQueue_omitsArgs_returnsBlockingQueue() {
        BlockingQueue<String> blockingQueue = applicationConfig.getBlockingQueue();
        Assert.assertNotNull(blockingQueue);
    }

    @Test
    public void testThreadPoolTaskScheduler_omitsArgs_returnsThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = applicationConfig.threadPoolTaskScheduler();
        Assert.assertNotNull(threadPoolTaskScheduler);

    }

    @Test
    public void testThreadPoolTaskExecutor_givenThreadPoolTaskExecutorConfiguration_returnsThreadPoolTaskExecutor() {
        Mockito.when(threadPoolTaskExecutorConfiguration.getCorePoolSize()).thenReturn(2);
        Mockito.when(threadPoolTaskExecutorConfiguration.getMaxPoolSize()).thenReturn(5);
        ThreadPoolTaskExecutor threadPoolTaskExecutor = applicationConfig.threadPoolTaskExecutor(
                threadPoolTaskExecutorConfiguration);
        Assert.assertNotNull(threadPoolTaskExecutor);
    }

    @Test
    public void testMongoClient_connectsToLocalHost_doesNotThrow() {
        try (MockedStatic<MongoClients> mongoClientsMockedStatic = Mockito.mockStatic(MongoClients.class)) {
            ReflectionTestUtils.setField(applicationConfig, "mongoHost", "localhost");
            mongoClientsMockedStatic.when(MongoClients::create).thenReturn(mongoClient);
            MongoClient mockedClient = applicationConfig.mongoClient();
            mockedClient.close();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testMongoClient_connectsToRemoteHost_doesNotThrow() {
        try (MockedStatic<MongoClients> mongoClientsMockedStatic = Mockito.mockStatic(MongoClients.class)) {
            ReflectionTestUtils.setField(applicationConfig, "mongoHost", "test-host.com");
            ReflectionTestUtils.setField(applicationConfig, "mongoPort", 27017);
            ReflectionTestUtils.setField(applicationConfig, "mongoDatabase", "TEST_DB");
            ReflectionTestUtils.setField(applicationConfig, "mongoUsername", "TEST_USER");
            ReflectionTestUtils.setField(applicationConfig, "mongoPassword", "test_password");
            mongoClientsMockedStatic.when(() -> MongoClients.create(Mockito.any(MongoClientSettings.class)))
                    .thenReturn(mongoClient);
            MongoClient mockedClient = applicationConfig.mongoClient();
            mockedClient.close();
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
