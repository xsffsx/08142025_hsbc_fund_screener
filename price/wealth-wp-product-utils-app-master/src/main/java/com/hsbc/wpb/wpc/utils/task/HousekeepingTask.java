package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class HousekeepingTask implements Runnable {
    @Autowired
    private MongoDatabase mongodb;

    @Value("#{${product.sync.keep-data-sync-log-days}}")
    private int keepLogDays;

    @Override
    public void run() {
        Date time = new Date(System.currentTimeMillis() - 86400L * keepLogDays * 1000);
        DeleteResult result = mongodb.getCollection(CollectionName.data_sync_log).deleteMany(Filters.lt(Field.syncEndTime, time));
        log.info("housekeeping, {} records out of {} days has been deleted from data_sync_log", result.getDeletedCount(), keepLogDays);
    }
}
