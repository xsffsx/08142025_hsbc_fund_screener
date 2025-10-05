package com.dummy.wpb.product.service;

import com.dummy.wpb.product.model.LockPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public class LockService {

    private static final String HOST_NAME = StringUtils.defaultIfEmpty(System.getenv("HOSTNAME"),
            StringUtils.defaultIfEmpty(System.getenv("COMPUTERNAME"), ""));

    private MongoOperations mongoOperations;

    private static final FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true).upsert(true);

    private static final String CONST_ID = "_id";
    private static final String CONST_TOKEN = "token";

    public LockService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public String lock(String lockName) {
        return this.lock(lockName, 30 * 1000L);
    }

    public String lock(String lockName, Long lockTimeout) {
        Query query = Query.query(Criteria.where(CONST_ID).is(lockName));
        String token = UUID.randomUUID().toString();
        Update update = new Update()
                .setOnInsert("lockFrom", HOST_NAME)
                .setOnInsert("lockTime", LocalDateTime.now())
                .setOnInsert(CONST_TOKEN, token)
                .setOnInsert("timeout", lockTimeout);

        LockPo lock = mongoOperations.findAndModify(query, update, findAndModifyOptions, LockPo.class);

        while (null != lock && !token.equals(lock.getToken())) {
            if (null == lock.getLockTime() || Duration.between(lock.getLockTime(), LocalDateTime.now()).toMillis() >= lock.getTimeout()) {
                this.unLock(lockName, lock.getToken());
            }

            // Try to get the lock again after two second
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            update.setOnInsert("lockTime", LocalDateTime.now());
            lock = mongoOperations.findAndModify(query, update, findAndModifyOptions, LockPo.class);
        }

        return token;
    }

    public void unLock(String lockName, String token) {
        mongoOperations.remove(Query.query(Criteria.where(CONST_ID).is(lockName)).addCriteria(Criteria.where(CONST_TOKEN).is(token)), LockPo.class);
    }
}
