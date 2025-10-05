package com.dummy.wmd.wpc.graphql;

import org.ehcache.event.CacheEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CacheEventLoggerTest {
    @Mock
    private CacheEvent<? extends Object, ? extends Object> cacheEvent;
    private CacheEventLogger cacheEventLogger;

    @Before
    public void setUp() throws Exception {
        cacheEventLogger = new CacheEventLogger();
    }

    @Test
    public void testOnEvent_givenCacheEvent_returnsNull() {
        Mockito.when(cacheEvent.getType()).thenReturn(null);
        Mockito.when(cacheEvent.getKey()).thenReturn(null);
        Mockito.when(cacheEvent.getOldValue()).thenReturn(null);
        Mockito.when(cacheEvent.getNewValue()).thenReturn(null);
        try {
            cacheEventLogger.onEvent(cacheEvent);
        }catch (Exception e){
            Assert.fail();
        }
    }
}
