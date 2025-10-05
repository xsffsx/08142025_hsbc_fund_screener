package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

@Slf4j
public class PendAppoveTranLoader implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.pend_appove_tran;
    protected MongoCollection<Document> collection;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PendAppoveTranLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb){
        this.collection = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void load() {
        if(collection.countDocuments() > 0){
            collection.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading pend_appove_tran ...");
        long start = System.currentTimeMillis();

        String sql = "select CTRY_REC_CDE, GRP_MEMBR_REC_CDE, REC_PEND_APROVE_NUM, count(*) as parts from PEND_APROVE_TRAN group by CTRY_REC_CDE, GRP_MEMBR_REC_CDE, REC_PEND_APROVE_NUM";
        List<Map<String, Object>> keyList = namedParameterJdbcTemplate.queryForList(sql, Collections.emptyMap());

        for (int i=0; i< keyList.size(); i++) {
            Map<String, Object> map =  keyList.get(i);
            log.info("Loading {}/{} - {}", i+1, keyList.size(), map);
            loadRecord(map);
        }

        long cost = (System.currentTimeMillis() - start) / 1000;
        log.info("{} seconds consumed", cost);
    }


    /**
     * load a pending approve record
     *
     * @param map
     */
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
        if(doc.get(Field.msgRecName)!=null && !doc.get(Field.msgRecName).equals("EXCELUPLOAD")){
            String encodedString = sb.toString();
            encodedString = encodedString.replace("\\r", "");
            encodedString = encodedString.replace("\\n", "");
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
                String decodedString = new String(decodedBytes);
                doc.put(Field.msgRecText, decodedString);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        doc.put(Field.recSeqNum, docList.size());
        collection.insertOne(doc);
    }
}
