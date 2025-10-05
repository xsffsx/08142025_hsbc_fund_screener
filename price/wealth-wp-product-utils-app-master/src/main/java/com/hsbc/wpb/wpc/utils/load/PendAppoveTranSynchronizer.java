package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Slf4j
public class PendAppoveTranSynchronizer implements DataSynchronizer {
    private static final String TARGET_COLLECTION_NAME = CollectionName.pend_appove_tran;
    protected MongoCollection<Document> pendAppoveTran;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected String tableName;

    public PendAppoveTranSynchronizer(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.pendAppoveTran = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.tableName = Table.PEND_APROVE_TRAN;
    }

    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        log.info("Updating {} records in {} ...", keySet.size(), tableName);
        keySet.forEach(keys -> {
            Bson filter = and(
                    eq(Field.ctryRecCde, keys.get("CTRY_REC_CDE")),
                    eq(Field.grpMembrRecCde, keys.get("GRP_MEMBR_REC_CDE")),
                    eq(Field.recPendAproveNum, keys.get("REC_PEND_APROVE_NUM"))
            );
            pendAppoveTran.deleteOne(filter);
            String sql = "select CTRY_REC_CDE, GRP_MEMBR_REC_CDE, REC_PEND_APROVE_NUM, count(*) as parts from PEND_APROVE_TRAN where CTRY_REC_CDE= :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and REC_PEND_APROVE_NUM= :REC_PEND_APROVE_NUM group by CTRY_REC_CDE, GRP_MEMBR_REC_CDE, REC_PEND_APROVE_NUM ";
            List<Map<String, Object>> oracleRecord = namedParameterJdbcTemplate.queryForList(sql, keys);
            loadRecord(oracleRecord.get(0));
        });
    }

    @Override
    public void delete(Set<String> rowidSet) {
        // no op need
    }

    private void loadRecord(Map<String, Object> map) {
        String sql = "select * from PEND_APROVE_TRAN where CTRY_REC_CDE= :CTRY_REC_CDE and GRP_MEMBR_REC_CDE = :GRP_MEMBR_REC_CDE and REC_PEND_APROVE_NUM= :REC_PEND_APROVE_NUM order by REC_SEQ_NUM asc";
        List<Document> docList = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        namedParameterJdbcTemplate.query(sql, map, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            row = CodeUtils.toCamelCase(row);
            row.put(Field._id, map.toString());
            sb.append(row.get(Field.msgRecText));
            docList.add(new Document(row));
        });
        Document doc = docList.get(0);
        if (doc.get(Field.msgRecName) != null && !doc.get(Field.msgRecName).equals("EXCELUPLOAD")) {
            String encodedString = sb.toString();
            encodedString = encodedString.replace("\r", "");
            encodedString = encodedString.replace("\n", "");
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
                String decodedString = new String(decodedBytes);
                doc.put(Field.msgRecText, decodedString);
            } catch (Exception e) {
                log.error(
                        "The application encountered an error when loading records. The error message is \n{}",
                        e.getMessage()
                );
            }
        }
        doc.put(Field.recSeqNum, docList.size());
        pendAppoveTran.insertOne(doc);
    }
}
