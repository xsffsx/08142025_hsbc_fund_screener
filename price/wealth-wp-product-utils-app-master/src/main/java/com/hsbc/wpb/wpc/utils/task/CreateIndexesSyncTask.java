package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.mongodb.MongoServerException;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CreateIndexesSyncTask implements SyncTask {

    @Getter
    @Value("dummy_lock_token")
    private String lockToken;

    @Autowired
    private MongoDatabase mongoDatabase;

    @Autowired
    MongoTemplate mongoTemplate;

    protected static final String CONFIG_COLLECTION_NAME = CollectionName.configuration;

    private static final String SPARSE = "sparse";

    private static final String UNIQUE = "unique";

    private static final String PARTIAL = "partialFilterExpression";

    @Override
    public String getTaskName() {
        return "CreateIndexesSyncTask";
    }

    @Override
    public void setLockToken(String lockToken) {
        this.lockToken = lockToken;
    }

    @Override
    public void run() {
        createCollectionIndexes();
    }


    @SuppressWarnings(value = "unchecked")
    public synchronized void createCollectionIndexes() {
        MongoCollection<Document> configColl = mongoDatabase.getCollection(CONFIG_COLLECTION_NAME);
        Document configDoc = configColl.find(Filters.eq("_id", "ALL/mongodb-collection-config")).first();
        List<Document> idxConfigList = (List<Document>) Objects.requireNonNull(configDoc).get("config", new Document());
        List<Document> indexConfigs = Optional.ofNullable(idxConfigList).orElseThrow(NullPointerException::new);

        for (Document doc : indexConfigs) {
            String collection = doc.getString("collection");

            List<Document> indexes = doc.get("indexes", List.class);
            Map<String, Document> configIndexMap = indexes.stream().collect(Collectors.toMap(item -> item.getString("name"), Function.identity(), (v1, v2) -> v1));
            log.info("========================= {} ==========================", collection);
            ListIndexesIterable<Document> mongoIndexIterable = mongoTemplate.getCollection(collection).listIndexes();
            List<Document> mongoIndexes = new ArrayList<>();
            for (Document listIndexDocument : mongoIndexIterable) {
                mongoIndexes.add(listIndexDocument);
            }
            Map<String, Document> mongoIndexMap = mongoIndexes.stream().collect(Collectors.toMap(item -> item.getString("name"), Function.identity(), (v1, v2) -> v1));

            processConfigIndexMap(configIndexMap, mongoIndexMap, collection);
        }

    }

    @SuppressWarnings(value = "unchecked")
    private void createMongoDBIndex(Document index, String collection) {
        String indexName = index.getString("name");

        List<String> keys = (List<String>) index.get("key");
        Integer order = index.getInteger("order");
        Bson bson = order > 0 ? Indexes.ascending(keys) : Indexes.descending(keys);

        log.info("Creating_Index: {}, collection: {}, columns: {}", indexName, collection, StringUtils.join(keys, ","));
        long start = System.currentTimeMillis();

        IndexOptions indexOptions = new IndexOptions().name(indexName)
                .sparse(index.getBoolean(SPARSE, false))
                .unique(index.getBoolean(UNIQUE, false))
                .partialFilterExpression(index.get(PARTIAL, Document.class));

        try {
            mongoTemplate.getCollection(collection).createIndex(bson, indexOptions);
        } catch (MongoServerException mongoServerException){
            log.error("Create index {} fail, reason: {}", indexName, mongoServerException.getMessage());
        }
        log.info("Create {} index costs: {} millisecond", indexName, System.currentTimeMillis() - start);
    }

    private void dropMongoDBIndex(String collection, String indexName) {
        log.info("Drop_Mongo_Index: {}", indexName);
        long start = System.currentTimeMillis();
        mongoTemplate.getCollection(collection).dropIndex(indexName);
        log.info("Drop {} index costs: {} millisecond", indexName, System.currentTimeMillis() - start);
    }

    @SuppressWarnings(value = "unchecked")
    private void processConfigIndexMap(Map<String, Document> configIndexMap, Map<String, Document> mongoIndexMap, String collection) {
        mongoIndexMap.forEach((mongoKeyName, mongoDocument) -> {
            if (!configIndexMap.containsKey(mongoKeyName) && !mongoKeyName.equals("_id_")) {
                log.info("Need_To_Drop Index: {}", mongoKeyName);
                dropMongoDBIndex(collection, mongoKeyName);
            }
        });

        configIndexMap.forEach((configKeyName, configDocument) -> {
            if (!mongoIndexMap.containsKey(configKeyName)) {
                log.info("Need_To_Create Index: {}", configKeyName);
                createMongoDBIndex(configDocument, collection);
            } else {
                List<String> configKeys = (List<String>) configDocument.get("key");
                Integer configOrders = (Integer) configDocument.get("order");
                Document mongoDocument = mongoIndexMap.get(configKeyName);
                Document mongoKeys = (Document) mongoDocument.get("key");
                Set<String> mongoKeySet = mongoKeys.keySet();
                Collection<Object> mongoOrderValues = mongoKeys.values();

                boolean isKeyConsistent = ListUtils.isEqualList(configKeys, mongoKeySet);
                boolean isOrderConsistent = mongoOrderValues.stream().allMatch(configOrders::equals);
                boolean isSparseConsistent = Objects.equals(configDocument.get(SPARSE, false), mongoDocument.get(SPARSE, false));
                boolean isUniqueConsistent = Objects.equals(configDocument.get(UNIQUE, false), mongoDocument.get(UNIQUE, false));
                boolean isPartialConsistent = Objects.equals(configDocument.get(PARTIAL, Document.class), mongoDocument.get(PARTIAL));

                if (!isSparseConsistent || !isKeyConsistent || !isOrderConsistent || !isUniqueConsistent || !isPartialConsistent) {
                    log.info("Drop_Index and Create_New_Index: {}", configKeyName);
                    dropMongoDBIndex(collection, configKeyName);
                    createMongoDBIndex(configDocument, collection);
                }
            }
        });
    }
}

