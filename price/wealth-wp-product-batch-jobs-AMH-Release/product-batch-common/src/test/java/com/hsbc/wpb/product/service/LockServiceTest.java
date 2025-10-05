package com.dummy.wpb.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.model.LockPo;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class LockServiceTest {

    MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);

    LockService lockService = new LockService(mongoOperations);

    private static final String HOST_NAME = StringUtils.defaultIfEmpty(System.getenv("HOSTNAME"),
            StringUtils.defaultIfEmpty(System.getenv("COMPUTERNAME"), ""));


    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void testLock_lockSuccessDirectly() {

        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

        StringBuilder expectToken = new StringBuilder();
        Mockito.when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(LockPo.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Update update = invocation.getArgument(1, Update.class);
                LockPo lockPo = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), LockPo.class);
                lockPo.setId("lock_test");
                expectToken.append(lockPo.getToken());
                return lockPo;
            }
        });

        String actualToken = lockService.lock("lock_test");
        Assert.assertEquals(expectToken.toString(), actualToken);
    }

    @Test
    public void testLock_givenOtherTokenAndTimeoutLockRecord() {
        LockPo oldLock = new LockPo();
        oldLock.setId("lock_test");
        oldLock.setLockFrom("myHost");
        oldLock.setToken(UUID.randomUUID().toString());
        oldLock.setTimeout(30000L);
        oldLock.setLockTime(LocalDateTime.now().minusMinutes(1));

        StringBuilder newToken = new StringBuilder();

        AtomicReference<Boolean> deleted = new AtomicReference<>(Boolean.FALSE);

        Mockito.when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(LockPo.class))).thenAnswer(invocation -> {
            if (deleted.get()) {
                Update update = invocation.getArgument(1, Update.class);
                LockPo newLock = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), LockPo.class);
                newLock.setId("lock_test");
                newToken.append(newLock.getToken());
                return newLock;
            } else {
                return oldLock;
            }
        });

        Mockito.when(mongoOperations.remove(any(Query.class), eq(LockPo.class))).thenAnswer(invocation -> {
            deleted.set(Boolean.TRUE);
            return new DeleteResult() {
                @Override
                public boolean wasAcknowledged() {
                    return false;
                }

                @Override
                public long getDeletedCount() {
                    return 1;
                }
            };
        });

        String token = lockService.lock("lock_test");

        Assert.assertEquals(newToken.toString(), token);
    }

    @Test
    public void testLock_givenSameTokenLockRecord() {
        StringBuilder expectToken = new StringBuilder();

        Mockito.when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(LockPo.class))).thenAnswer(invocation -> {
            Update update = invocation.getArgument(1, Update.class);
            LockPo lock = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), LockPo.class);
            lock.setId("lock_test");
            expectToken.append(lock.getToken());
            return lock;
        });

        String actualToken = lockService.lock("lock_test");
        Assert.assertEquals(expectToken.toString(), actualToken);
    }

    @Test
    public void testLock_givenOtherTokenLockRecord() {
        LocalDateTime lockTime = LocalDateTime.now();
        LockPo oldLock = new LockPo();
        oldLock.setId("lock_test");
        oldLock.setLockFrom("myHost");
        oldLock.setToken(UUID.randomUUID().toString());
        oldLock.setTimeout(3000L);
        oldLock.setLockTime(lockTime);

        AtomicReference<Boolean> deleted = new AtomicReference<>(Boolean.FALSE);

        Mockito.when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(LockPo.class))).thenAnswer(invocation -> {
            if (deleted.get()) {
                Update update = invocation.getArgument(1, Update.class);
                LockPo newLock = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), LockPo.class);
                newLock.setId("lock_test");
                return newLock;
            } else {
                return oldLock;
            }
        });

        Mockito.when(mongoOperations.remove(any(Query.class), eq(LockPo.class))).thenAnswer(invocation -> {
            deleted.set(Boolean.TRUE);
            return new DeleteResult() {
                @Override
                public boolean wasAcknowledged() {
                    return false;
                }

                @Override
                public long getDeletedCount() {
                    return 1;
                }
            };
        });

        lockService.lock("lock_test", 3000L);
        LocalDateTime now = LocalDateTime.now();
        Assert.assertTrue(Duration.between(lockTime, now).toMillis() >= 3000L);
    }

    @Test
    public void testUnlock() {
        try {
            lockService.unLock("lock_test", "");
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

}