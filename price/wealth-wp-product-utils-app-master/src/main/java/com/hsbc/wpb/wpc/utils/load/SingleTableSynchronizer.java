package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.productLock;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class SingleTableSynchronizer implements DataSynchronizer {
    protected MongoCollection<Document> collection;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected String tableName;
    private String collectionName;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public SingleTableSynchronizer(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, MongoDatabase mongodb, String collectionName,ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.tableName = tableName;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collection = mongodb.getCollection(collectionName);
        this.collectionName = collectionName;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    protected Map<String, Object> getMasterRecord(Map<String, Object> keys) {
        String sql = String.format("select ROWID, t.* from %s t where ROWID = :ROWID", tableName);
        SqlParameterSource parameters = new MapSqlParameterSource(keys);
        List<Map<String, Object>> list = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, parameters, (RowCallbackHandler) rs ->
            list.add(CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs)))
        );
        return !list.isEmpty() ? list.get(0) : null;
    }

    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        log.info("Updating {} records in {} ...", keySet.size(), tableName);
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        keySet.forEach(keys ->
            tasks.add(
                    CompletableFuture.runAsync(() -> {
                        Map<String, Object> master = getMasterRecord(keys);
                        if(null != master) {    // update
                            String rowid = (String)master.get(Field.rowid);
                            master.put(Field._id, rowid);
                            master.remove(Field.rowid);
                            collection.replaceOne(Filters.eq(Field._id, master.get(Field._id)), new Document(master), new ReplaceOptions().upsert(true));
                        }
                    }, threadPoolTaskExecutor)
            )
        );
        CompletableFuture<Void> results = CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[tasks.size()]));
        results.join();
    }

    @Override
    public void delete(Set<String> rowidSet) {
        DeleteResult result = collection.deleteMany(Filters.in("_id", rowidSet));
        log.info("{} {} item has been deleted", result.getDeletedCount(), collectionName);
    }
}
