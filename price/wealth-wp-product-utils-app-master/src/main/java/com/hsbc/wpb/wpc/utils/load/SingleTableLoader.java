package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.model.productLock;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class SingleTableLoader implements DataLoader {
    private String targetCollectionName;
    private String tableName;
    protected MongoCollection<Document> collection;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private  ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private LockService lockService;
    private boolean reload;

    public SingleTableLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, MongoDatabase mongodb, String collectionName,
                             ThreadPoolTaskExecutor threadPoolTaskExecutor, LockService lockService){
        this(namedParameterJdbcTemplate, tableName, mongodb, collectionName, threadPoolTaskExecutor, lockService,true);
    }

    public SingleTableLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, MongoDatabase mongodb, String collectionName,
                             ThreadPoolTaskExecutor threadPoolTaskExecutor, LockService lockService, boolean reload){
        this.lockService = lockService;
        this.tableName = tableName;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.targetCollectionName = collectionName;
        this.collection = mongodb.getCollection(collectionName);
        this.reload = reload;
        this.threadPoolTaskExecutor=threadPoolTaskExecutor;
    }

    public String getTableName(){
        return this.tableName;
    }

    @Override
    public void load() {
        if(collection.countDocuments() > 0){
            if(reload) {
                collection.drop();
                log.warn("Target collection [{}] is not empty, dropped", targetCollectionName);
            } else {
                log.warn("Target collection [{}] is not empty, skipped", targetCollectionName);
                return;
            }
        }

        long start = System.currentTimeMillis();
        int total = getRecordCount(tableName);
        log.info("Loading {} total count {}...", tableName,total);
        // load by batch, 1000 record each batch
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < total; i += 1000) {
            int rowStart = i;
            tasks.add(
                    CompletableFuture.runAsync(() -> {
                        lockService.heartbeat(productLock.DATA_UTILS_TASK_LOCK);
                        List<Document> docs = load(tableName, rowStart);
                        if (!docs.isEmpty()) {
                            collection.insertMany(docs);
                        }
                    }, threadPoolTaskExecutor)
            );
        }
        CompletableFuture<Void> results = CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[tasks.size()]));
        results.join();

        long cost = (System.currentTimeMillis() - start) / 1000;
        long count = collection.countDocuments();
        log.info("{} seconds consumed load table {},success count{}", cost,tableName,count);

    }

    private int getRecordCount(String table) {
        String sql = String.format("select count(*) from %s", table);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, Collections.emptyMap(), Integer.class);
        return count == null ? 0 : count;
    }

    public List<Document> load(String table, int rowStart) {
        String sql = String.format(
                "select * from (select ROWNUM as RNUM, ROWID as RID, t.* from %s t where ROWNUM <= (:rowStart + 1000)) where RNUM > :rowStart",
                table
        );
        List<Document> docList = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, Collections.singletonMap("rowStart", rowStart), rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            row = CodeUtils.toCamelCase(row);
            row.remove(Field.rnum);
            if(!Table.PROD_PRC_HIST.equals(table)){
                row.put(Field._id, row.get(Field.rid));
                row.remove(Field.rid);
            }
            docList.add(new Document(row));
        });
        return docList;
    }
}
