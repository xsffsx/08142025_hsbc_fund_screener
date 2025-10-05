package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.productLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.dummy.wpb.wpc.utils.CommonUtils.getHostname;

@Slf4j
@Service
public class LockService {
    private final MongoTemplate mongoTemplate;
    private final String hostname;
    private static final FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true).upsert(true);

    @Value("#{${product.sync.lock-timeout:30}}")
    private int lockTimeout;

    public LockService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.hostname = getHostname();
    }

    /**
     * Retrieve a lock with given name, a free lock=0, any value greater than 0 indicate it's busy, any time a call to retrieveLock will increase the value by 1.
     *
     * @param lockName
     * @return  a token which can be used for lock releasing
     */
    public String retrieveLock(String lockName) {
        Date now = new Date();
        Query q = Query.query(Criteria.where(Field._id).is(lockName));
        Update update = new Update().inc(Field.lock, 1);
        productLock lock = mongoTemplate.findAndModify(q, update, findAndModifyOptions, productLock.class);
        if(Objects.nonNull(lock) && !Objects.equals(hostname, lock.getLockFrom()) && lock.getLock() > 1) {
            Duration timeElapsed = Duration.between(lock.getLastHeartbeatTime().toInstant(), Instant.now());
            if(timeElapsed.toMillis() > lock.getTimeout()) {
                log.info("{}.{} timeout", lock.getId(), lock.getToken());
                if(releaseLock(lock.getId())) { // release lock may fail in case 2 instance try to do it in the same time
                    return retrieveLock(lock.getId());
                } else {
                    return null;
                }
            } else {
                log.info("{} is occupied", lock.getId());
                return null;
            }
        }
        if(Objects.nonNull(lock)){
            // a new created lock, need to supplement some information
            String token = UUID.randomUUID().toString();
            lock.setToken(token);
            lock.setLockFrom(hostname);
            lock.setTimeout(lockTimeout * 60000L);
            lock.setStartTime(now);
            lock.setLastHeartbeatTime(now);
            mongoTemplate.save(lock);
            log.info("{}.{} created", lock.getId(), lock.getToken());
            return lock.getToken();
        }else {
            return null;
        }


    }

    /**
     * Release a lock with given name and token
     *
     * @param lockName
     */
    public boolean releaseLock(String lockName){
        Query q = Query.query(Criteria.where(Field._id).is(lockName).and(Field.lock).gt(0));
        Update update = new Update()
                .set(Field.lock, 0)
                .set(Field.heartbeatTimes, 0)
                .set(Field.endTime, new Date());
        try {
            productLock lock = mongoTemplate.findAndModify(q, update, findAndModifyOptions, productLock.class);
            if(Objects.nonNull(lock)) {
                log.info("{}.{} released", lock.getId(), lock.getToken());
            }
            return true;
        } catch (DuplicateKeyException e) {
            log.warn("{} release failed", lockName);
            return false;
        }
    }

    /**
     * Call to perform a heartbeat, it will refresh the lastHeartbeatTime
     *
     * @param lockName
     */
    public void heartbeat(String lockName) {
        Query q = Query.query(Criteria.where(Field._id).is(lockName));
        Update update = new Update()
                .set(Field.lastHeartbeatTime, new Date())
                .inc(Field.heartbeatTimes, 1);
        productLock lock = mongoTemplate.findAndModify(q, update, findAndModifyOptions, productLock.class);
        if(Objects.nonNull(lock)) {
            log.debug("{} heartbeat: {}", lock.getId(), lock.getHeartbeatTimes());
        }
    }
}
