package com.dummy.wmd.wpc.graphql.task;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.error.ErrorCode;
import com.dummy.wmd.wpc.graphql.utils.RequestLogUtils;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonMaximumSizeExceededException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@SuppressWarnings("java:S4507")
@Component
@Slf4j
public class RequestLogTask implements Runnable {
    @Autowired
    private BlockingQueue<Document> requestLogQueue;
    @Autowired
    private MongoDatabase mongodb;

    @Value("#{${product.graphql.enable-db-data-log:True}}")
    private boolean enableDbDataLog;
    private static final String REQUEST_LOG = CollectionName.request_log.toString();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Document doc = requestLogQueue.take();
                if(enableDbDataLog) {   // has Data, write one by one, since could be failed
                    insertRequestLog(doc);
                } else {    // without data, can write log in bulk
                    List<Document> docList = new ArrayList<>();
                    docList.add(doc);
                    requestLogQueue.drainTo(docList, 1000);
                    mongodb.getCollection(REQUEST_LOG).insertMany(docList);
                    if(!requestLogQueue.isEmpty()) {
                        log.debug("wrote {} request log, left in queue {}", docList.size(), requestLogQueue.size());
                    }
                }
            } catch (InterruptedException e) {
                log.warn("The RequestLogTask thread is interrupted");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("{}: {}", ErrorCode.OTPSERR_ZGQ000, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void insertRequestLog(Document doc) {
        try {
            mongodb.getCollection(REQUEST_LOG).insertOne(doc);
        } catch(BsonMaximumSizeExceededException e) {
            // too large to fit in a document, remove the data field
            RequestLogUtils.removeData(doc);
            mongodb.getCollection(REQUEST_LOG).insertOne(doc);
        }
    }
}
