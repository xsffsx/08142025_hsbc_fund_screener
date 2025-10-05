package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.model.productLock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LockServiceTests {

    @Mock
    private MongoTemplate mockMongoTemplate;

    private LockService lockService;

    @Before
    public void setUp() throws Exception {
        lockService = new LockService(mockMongoTemplate);
        ReflectionTestUtils.setField(lockService, "lockTimeout", 0);
    }

    @Test
    public void testLock_mockLock_returnsNull() {
        productLock productLock = new productLock();
        productLock.setId("id");
        productLock.setStartTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        productLock.setLastHeartbeatTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        productLock.setHeartbeatTimes(0);
        productLock.setTimeout(0L);
        productLock.setToken("token");
        productLock.setLock(0);
        productLock.setLockFrom("hostname");
        when(mockMongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class),
                eq(productLock.class))).thenReturn(productLock);
        // Run the test
        try {
            lockService.retrieveLock("lockName");
            lockService.releaseLock("lockName");
            lockService.heartbeat("lockName");
        }catch (Exception e){
            Assert.fail();
        }

    }
    @Test
    public void testLock_mockNull_returnsNull() {
        when(mockMongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class),
                eq(productLock.class))).thenReturn(null);
        try {
            lockService.retrieveLock("lockName");
            lockService.releaseLock("lockName");
            lockService.heartbeat("lockName");
        }catch (Exception e){
            Assert.fail();
        }

    }

}