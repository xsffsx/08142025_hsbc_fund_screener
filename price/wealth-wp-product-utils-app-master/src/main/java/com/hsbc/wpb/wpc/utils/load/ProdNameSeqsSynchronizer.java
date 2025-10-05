package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class ProdNameSeqsSynchronizer implements DataSynchronizer {
    protected MongoCollection<Document> collection;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProdNameSeqsSynchronizer(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collection = mongodb.getCollection(CollectionName.sys_parm);
    }

    protected Map<String, Object> getMasterRecord(Map<String, Object> keys) {
        String sql = "select ROWID, t.* from PROD_NAME_SEQS t where ROWID = :ROWID";
        SqlParameterSource parameters = new MapSqlParameterSource(keys);
        List<Map<String, Object>> list = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, parameters, (RowCallbackHandler) rs ->
            list.add(CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs)))
        );
        return !list.isEmpty() ? list.get(0) : null;
    }

    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        log.info("Updating {} Product Name Sequence(s) ...", keySet.size());
        for (Map<String, Object> keys : keySet) {
            Map<String, Object> sequence = getMasterRecord(keys);
            if (null == sequence) {
                continue;
            }

            LocalDateTime prodMatDt = (LocalDateTime) sequence.get("prodMatDt");
            String prodMturDt = prodMatDt.toLocalDate().toString();
            int seqNum = MapUtils.getIntValue(sequence, "seqNum");

            collection.updateOne(
                    Filters.eq("parmCde", "prodMturDt_seq"),
                    Updates.combine(
                            Updates.set("parmValueTexts." + prodMturDt, seqNum),
                            Updates.set(Field.recUpdtDtTm, new Date()),
                            Updates.setOnInsert(Field.recCreatDtTm, new Date()),
                            Updates.setOnInsert(Field._id, UUID.randomUUID().toString())
                    ),
                    new UpdateOptions().upsert(true));
        }
    }

    @Override
    public void delete(Set<String> rowidSet) {
        log.warn("Deleting PROD_NAME_SEQS records is not supported.");
    }
}
