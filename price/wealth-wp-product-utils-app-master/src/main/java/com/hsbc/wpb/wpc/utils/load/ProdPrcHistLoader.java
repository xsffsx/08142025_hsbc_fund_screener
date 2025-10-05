package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.productLock;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ProdPrcHistLoader implements DataLoader {
    private String prodPrcHistCollectionName;
    private String tableName;
    protected MongoCollection<Document> collectionProdPrcHist;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private  ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private LockService lockService;
    private boolean reload;

    public ProdPrcHistLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, MongoDatabase mongodb, String collectionName,
                             ThreadPoolTaskExecutor threadPoolTaskExecutor, LockService lockService){
        this(namedParameterJdbcTemplate, tableName, mongodb, collectionName, threadPoolTaskExecutor, lockService,true);
    }

    public ProdPrcHistLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String tableName, MongoDatabase mongodb, String collectionName,
                             ThreadPoolTaskExecutor threadPoolTaskExecutor, LockService lockService, boolean reload){

        this.tableName = tableName;
        this.reload = reload;
        this.lockService = lockService;
        this.prodPrcHistCollectionName = collectionName;
        this.collectionProdPrcHist = mongodb.getCollection(collectionName);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.threadPoolTaskExecutor=threadPoolTaskExecutor;
    }

    public String getTableName(){
        return this.tableName;
    }

    @Override
    public void load() {
        if(collectionProdPrcHist.countDocuments() > 0){
            if(reload) {
                collectionProdPrcHist.drop();
                log.warn("Target collection [{}] is not empty, dropped", prodPrcHistCollectionName);
            } else {
                log.warn("Target collection [{}] is not empty, skipped", prodPrcHistCollectionName);
                return;
            }
        }

        long start = System.currentTimeMillis();
        int total = getProdPrcHistRecordCount(tableName);
        Date processStartTime = DbUtils.toUTCDate(new Date(System.currentTimeMillis()));
        log.info("Loading {} total count {}...", tableName,total);
        // load by batch, 1000 record each batch
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (int i = 0; i < total; i += 10000) {
            int rowStart = i;
            completableFutures.add(
                    CompletableFuture.runAsync(() -> {
                        lockService.heartbeat(productLock.DATA_UTILS_TASK_LOCK);
                        List<Document> docs = loadProdPrcHist(tableName, rowStart,processStartTime);
                        if (!docs.isEmpty()) {
                            collectionProdPrcHist.insertMany(docs);
                        }
                    }, threadPoolTaskExecutor)
            );
        }
        CompletableFuture<Void> results = CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
        results.join();

        long cost = (System.currentTimeMillis() - start) / 1000;

        long count = collectionProdPrcHist.countDocuments();
        log.info("{} seconds consumed load table {},success count{}", cost,tableName,count);


    }

    private int getProdPrcHistRecordCount(String table) {
        String sql = String.format("select count(*) from %s", table);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, Collections.emptyMap(), Integer.class);
        return count == null ? 0 : count;
    }

    private List<Document> loadProdPrcHist(String table, int rowStart, Date processStartTime) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("processStartTime", processStartTime);
        parameters.addValue("rowStart", rowStart);
        String sql = String.format(
                "select * from (select ROWNUM as RNUM, ROWID as RID, t.* from %s t where REC_CREAT_DT_TM < :processStartTime and ROWNUM <= (:rowStart + 10000)) where RNUM > :rowStart",
                table
        );
        List<Document> docList = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            row = CodeUtils.toCamelCase(row);
            row.remove(Field.rnum);
            row.put(Field._id, row.get(Field.rid));
            row.remove(Field.rid);
            docList.add(new Document(row));
        });
        return docList;
    }
}
