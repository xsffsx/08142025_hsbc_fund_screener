package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LockService {

    private static final String HOST_NAME = StringUtils.defaultIfEmpty(System.getenv("HOSTNAME"),
            StringUtils.defaultIfEmpty(System.getenv("COMPUTERNAME"), ""));

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true).upsert(true);

    private static final Long DEFAULT_TIMEOUT = 30 * 1000L;

    public void lock(String lockName) {
        this.lock(lockName, DEFAULT_TIMEOUT);
    }

    /**
     * To successfully acquired the lock, one of the following conditions must be met: <br>
     * 1. The lock record does not exist.<br>
     * 2. The lock is expired.<br>
     * 3. The lock is not expired, but it is held by the same machine.<br>
     */
    public void lock(String lockName, Long lockTimeout) {
        Lock lock = doLock(lockName, lockTimeout);
        String processId = getCurrentProcessId();

        // case 1 or case 3
        while (null == lock || !processId.equals(lock.getLockFrom())) {
            if (null != lock) {
                // case 2
                boolean expired = ObjectUtils.compare(LocalDateTime.now().minus(lock.getTimeout(), ChronoUnit.MILLIS), lock.getLockTime()) >= 0;
                if (expired) {
                    this.unLock(lockName);
                }
            }

            // Try to get the lock again after two second
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            lock = doLock(lockName, lockTimeout);
        }
    }

    private Lock doLock(String lockName, Long lockTimeout) {
        Query query = Query.query(Criteria.where(Field._id).is(lockName));
        Update update = new Update()
                .setOnInsert("lockFrom", getCurrentProcessId())
                .setOnInsert("lockTime", LocalDateTime.now())
                .setOnInsert("timeout", lockTimeout);
        try {
            return mongoTemplate.findAndModify(query, update, findAndModifyOptions, Lock.class);
        } catch (DuplicateKeyException e) {
            return null;
        }
    }

    private String getCurrentProcessId() {
        return HOST_NAME + "-" + Thread.currentThread().getName();
    }

    public void unLock(String lockName) {
        mongoTemplate.remove(Query.query(Criteria.where(Field._id).is(lockName)).addCriteria(Criteria.where("lockFrom").is(getCurrentProcessId())), Lock.class);
    }
}
