package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Slf4j
public class ReferenceDataSynchronizer implements DataSynchronizer {
    protected MongoCollection<Document> colReferenceData;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SequenceService sequenceService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    public ReferenceDataSynchronizer(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb,ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.colReferenceData = mongodb.getCollection(CollectionName.reference_data);
        this.sequenceService = new SequenceService(mongodb);
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    private List<String> getChannelCodeList(Map<String, Object> keys) {
        String sql = "select CHANL_COMN_CDE from CDE_DESC_VALUE_CHANL_REL t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and CDV_TYPE_CDE = :CDV_TYPE_CDE and CDV_CDE = :CDV_CDE";
        SqlParameterSource parameters = new MapSqlParameterSource(keys);
        return namedParameterJdbcTemplate.queryForList(sql, parameters, String.class);
    }

    protected Map<String, Object> getMasterRecord(Map<String, Object> keys) {
        String sql = "select ROWID, t.* from CDE_DESC_VALUE t where CTRY_REC_CDE = :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and CDV_TYPE_CDE = :CDV_TYPE_CDE and CDV_CDE = :CDV_CDE";
        SqlParameterSource parameters = new MapSqlParameterSource(keys);
        List<Map<String, Object>> list = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, parameters, (RowCallbackHandler) rs ->
            list.add(CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs)))
        );
        return !list.isEmpty() ? list.get(0) : null;
    }

    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        log.info("Updating {} ReferenceData(s) ...", keySet.size());
        Date lastSyncTime = new Date();
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        keySet.forEach(keys ->
            tasks.add(
                    CompletableFuture.runAsync(() -> syncReferenceData(keys,lastSyncTime), threadPoolTaskExecutor)
            )
        );
        CompletableFuture<Void> results = CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[tasks.size()]));
        results.join();
    }
    private void syncReferenceData(Map<String, Object> keys, Date lastSyncTime){
        Map<String, Object> master = getMasterRecord(keys);
        if(null != master) {
            List<String> chanlComnCde = getChannelCodeList(keys);
            master.put(Field.chanlComnCde, chanlComnCde);

            Bson filter = and(
                    eq(Field.ctryRecCde, master.get(Field.ctryRecCde)),
                    eq(Field.grpMembrRecCde, master.get(Field.grpMembrRecCde)),
                    eq(Field.cdvTypeCde, master.get(Field.cdvTypeCde)),
                    eq(Field.cdvCde, master.get(Field.cdvCde))
            );
            Document doc = colReferenceData.find(filter).first();
            Long id;
            if(null == doc) {   // insert
                id = sequenceService.nextId(Sequence.referenceDataId);
                master.put(Field._id, id);
                master.put(Field.revision, 1L);
                master.put(Field.createdBy, "sync");
            } else {            // update
                id = doc.getLong(Field._id);
                master.put(Field._id, id);
                master.put(Field.revision, doc.get(Field.revision));
                master.put(Field.lastUpdatedBy, "sync");
            }
            master.put(Field.lastSyncTime, lastSyncTime);
            colReferenceData.replaceOne(eq(Field._id, id), new Document(master), new ReplaceOptions().upsert(true));
        }
    }

    @Override
    public void delete(Set<String> rowidSet) {
        DeleteResult result = colReferenceData.deleteMany(Filters.in("rowid", rowidSet));
        log.info("{} Reference Data item has been deleted", result.getDeletedCount());
    }
}
