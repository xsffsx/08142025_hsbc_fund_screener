package com.dummy.wmd.wpc.graphql.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.model.Lock;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class LockServiceTest {

    MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

    LockService lockService = new LockService();

    private static final String HOST_NAME = StringUtils.defaultIfEmpty(System.getenv("HOSTNAME"),
            StringUtils.defaultIfEmpty(System.getenv("COMPUTERNAME"), ""));

    @Before
    public void before() {
        ReflectionTestUtils.setField(lockService, "mongoTemplate", mongoTemplate);
    }


    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void testLock_lockSuccessDirectly() throws JsonProcessingException {
        Mockito.when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Lock.class))).thenAnswer(invocation -> {
            Query query = invocation.getArgument(0, Query.class);
            Update update = invocation.getArgument(1, Update.class);
            Lock lock = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), Lock.class);
            lock.setId(query.getQueryObject().getString("_id"));
            return lock;
        });

        lockService.lock("lock_test");
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        Mockito.verify(mongoTemplate, Mockito.times(1)).findAndModify(queryCaptor.capture(), any(Update.class), any(FindAndModifyOptions.class), eq(Lock.class));
        Assert.assertEquals("lock_test", queryCaptor.getValue().getQueryObject().get("_id"));
    }

    @Test
    public void testLock_givenOtherTokenAndTimeoutLockRecord() {
        Lock oldLock = new Lock();
        oldLock.setId("lock_test");
        oldLock.setLockFrom("myHost-Thread1");
        oldLock.setTimeout(30000L);
        oldLock.setLockTime(LocalDateTime.now().minusMinutes(1));

        AtomicReference<Boolean> deleted = new AtomicReference<>(Boolean.FALSE);

        Mockito.when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Lock.class)))
                .thenThrow(new DuplicateKeyException("Duplicate key exception"))
                .thenAnswer(invocation -> {
                    if (deleted.get()) {
                        Query query = invocation.getArgument(0, Query.class);
                        Update update = invocation.getArgument(1, Update.class);
                        Lock newLock = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), Lock.class);
                        newLock.setId(query.getQueryObject().getString("_id"));
                        return newLock;
                    } else {
                        return oldLock;
                    }
                });

        Mockito.when(mongoTemplate.remove(any(Query.class), eq(Lock.class))).thenAnswer(invocation -> {
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

        lockService.lock("lock_test");

        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
        Mockito.verify(mongoTemplate, Mockito.times(3)).findAndModify(any(Query.class), updateCaptor.capture(), any(FindAndModifyOptions.class), eq(Lock.class));
        Assert.assertNotEquals(oldLock.getLockFrom(), updateCaptor.getValue().getUpdateObject().get("$setOnInsert", Document.class).get("lockForm"));
    }

    @Test
    public void testLock_givenSameTokenLockRecord() {

        Mockito.when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Lock.class))).thenAnswer(invocation -> {
            Query query = invocation.getArgument(0, Query.class);
            Update update = invocation.getArgument(1, Update.class);
            Lock lock = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), Lock.class);
            lock.setId(query.getQueryObject().getString("_id"));
            return lock;
        });

        lockService.lock("lock_test");
        Mockito.verify(mongoTemplate, Mockito.times(1)).findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Lock.class));
    }

    @Test
    public void testLock_givenOtherTokenLockRecord() {
        LocalDateTime lockTime = LocalDateTime.now();
        Lock oldLock = new Lock();
        oldLock.setId("lock_test");
        oldLock.setLockFrom("myHost");
        oldLock.setToken(UUID.randomUUID().toString());
        oldLock.setTimeout(3000L);
        oldLock.setLockTime(lockTime);

        AtomicReference<Boolean> deleted = new AtomicReference<>(Boolean.FALSE);

        Mockito.when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Lock.class))).thenAnswer(invocation -> {
            if (deleted.get()) {
                Update update = invocation.getArgument(1, Update.class);
                Lock newLock = objectMapper.readValue(objectMapper.writeValueAsString(update.getUpdateObject().get("$setOnInsert")), Lock.class);
                newLock.setId("lock_test");
                return newLock;
            } else {
                return oldLock;
            }
        });

        Mockito.when(mongoTemplate.remove(any(Query.class), eq(Lock.class))).thenAnswer(invocation -> {
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
        lockService.unLock("lock_test");
        Mockito.verify(mongoTemplate, Mockito.times(1)).remove(any(Query.class), eq(Lock.class));
    }

}