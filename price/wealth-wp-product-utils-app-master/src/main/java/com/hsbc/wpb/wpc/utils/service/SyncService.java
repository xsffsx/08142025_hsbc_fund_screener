package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.ProductMapping;
import com.dummy.wpb.wpc.utils.model.*;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncService {
    private MongoTemplate mongoTemplate;

    public SyncService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static String startTime = "processStartTime";
    private static String productTables = "productTables";

    /**
     * Retrieve all product change events before given processStartTime, but only return the prodId set for handling.
     * In this manner, we can handle product which has many events update in one go.
     *
     * @param processStartTime
     * @return
     */
    public ProductEventGroup retrieveProductChangeEvents(Date processStartTime) {
        MapSqlParameterSource parameters = new MapSqlParameterSource(startTime, processStartTime);
        parameters.addValue(productTables, ProductMapping.getTableList());
        parameters.addValue("countStartTime", new Date(processStartTime.getTime() - 60000));

        // Count product events in the last 60s, skip handling in case events are generating actively
        String sql = "select count(1) as eventCount from DATA_CHANGE_EVENT t where TS between :countStartTime AND :processStartTime AND OP_Table in (:productTables)";
        int eventCount = namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
        if (eventCount > 6000) {
            sql = "select min(ts) as eventStartTime from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)";
            Date eventStartTime = namedParameterJdbcTemplate.queryForObject(sql, parameters, Date.class);
            // deffer time up to 5 minutes
            long defferTime = 0L;
            if(null != eventStartTime) defferTime = processStartTime.getTime() - eventStartTime.getTime();
            if (defferTime < 5 * 60000) {
                log.warn("deffer product sync to next round, last 60s product event count {}, the deffer time {}", eventCount, defferTime);
                return null;
            }
        }

        RowMapper<ProductEventGroup> rowMapper = new BeanPropertyRowMapper<>(ProductEventGroup.class);

        sql = "select min(ts) as eventStartTime, max(ts) as eventEndTime, count(1) as eventCount from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)";
        ProductEventGroup group = namedParameterJdbcTemplate.queryForObject(sql, parameters, rowMapper);

        sql = "select distinct PROD_ID from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)";
        List<Long> prodIdList = namedParameterJdbcTemplate.queryForList(sql, parameters, Long.class);
         if(null != group){
            group.setProdIdList(prodIdList);
            group.setProcessStartTime(processStartTime);
        }
        return group;
    }

    /**
     * Delete product related events before given processStartTime
     *
     * @param group
     */
    public void deleteProductChangeEvents(ProductEventGroup group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource(startTime, group.getProcessStartTime());
        parameters.addValue(productTables, ProductMapping.getTableList());

        String sql = "delete from DATA_CHANGE_EVENT t where TS < :processStartTime AND OP_Table in (:productTables)";
        int rows = namedParameterJdbcTemplate.update(sql, parameters);
        if (rows != group.getEventCount()) {
            log.warn("{} product events deleted, while expected {}", rows, group.getEventCount());
        } else {
            log.info("{} product events deleted", rows);
        }
    }

    public List<DataChangeEvent> retrieveNonProductChangeEvents(Date processStartTime) {
        // ORA-01795: maximum number of expressions in a list is 1000, add the TS limitation to avoid long last processing missing new events
        MapSqlParameterSource parameters = new MapSqlParameterSource(startTime, processStartTime);
        parameters.addValue(productTables, ProductMapping.getTableList());
        String sql = "select ROWID as ID, t.OP_TABLE as TABLE_NAME, t.OP, t.ROW_ID, t.TS, t.PROD_ID from DATA_CHANGE_EVENT t where TS < :processStartTime AND ROWNUM <= 1000 AND OP_TABLE not in (:productTables) ORDER BY TS";
        return namedParameterJdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(DataChangeEvent.class));
    }

    /**
     * Clear DATA_CHANGE_EVENT, since there is chance exceed 1000, and Oracle DB has limit for the 'in' operation of 1000, make the clear 1000 per batch.
     *
     * @param events
     */
    public void deleteDataChangeEvents(List<DataChangeEvent> events) {
        List<String> rowidList = events.stream().map(DataChangeEvent::getId).collect(Collectors.toList());

        for (int i = 0; i < rowidList.size(); i += 1000) {
            int toIndex = i + 1000;
            if (toIndex > rowidList.size()) {
                toIndex = rowidList.size();
            }
            List<String> batchIds = rowidList.subList(i, toIndex);
            SqlParameterSource paramSource = new MapSqlParameterSource("rowids", batchIds);
            namedParameterJdbcTemplate.update("delete from DATA_CHANGE_EVENT where ROWID in (:rowids)", paramSource);
        }
    }

    public void writeDeltaSyncLog(SyncLog log) {
        mongoTemplate.insert(log);
    }

    public void removeEventsByTableNames(List<String> tableNames) {
        int affected = namedParameterJdbcTemplate.update("delete from DATA_CHANGE_EVENT where OP_TABLE in (:tableNames)", Collections.singletonMap("tableNames", tableNames));
        log.info("{} records is deleted from DATA_CHANGE_EVENT", affected);
    }
}
