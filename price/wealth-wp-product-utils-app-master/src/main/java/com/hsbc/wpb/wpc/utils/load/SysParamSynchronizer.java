package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SysParamSynchronizer implements DataSynchronizer {
    protected MongoCollection collection;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected String table;

    public SysParamSynchronizer(String table, NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.table = table;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collection = mongodb.getCollection(CollectionName.sys_parm);
    }

    protected Map<String, Object> getMasterRecord(Map<String, Object> keys) {
        String sql = String.format("select ROWID, t.* from %s t where ROWID = :ROWID", table);
        SqlParameterSource parameters = new MapSqlParameterSource(keys);
        List<Map<String, Object>> list = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            list.add(CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs)));
        });
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        log.info("Updating {} SysParam(s) ...", keySet.size());
        keySet.forEach(keys -> {
            Map<String, Object> master = getMasterRecord(keys);
            if(null != master) {
                String rowid = (String)master.get(Field.rowid);
                master.put(Field._id, rowid);
                master.remove(Field.rowid);
                collection.replaceOne(Filters.eq(Field._id, master.get(Field._id)), new Document(master), new ReplaceOptions().upsert(true));
            }
        });
    }

    @Override
    public void delete(Set<String> rowidSet) {
        DeleteResult result = collection.deleteMany(Filters.in("_id", rowidSet));
        log.info("{} {} item has been deleted", result.getDeletedCount(), CollectionName.sys_parm);
    }
}