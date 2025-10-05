package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ReferenceDataLoader implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.reference_data;
    protected MongoCollection<Document> colReferenceData;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SequenceService sequenceService;

    public ReferenceDataLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.colReferenceData = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.sequenceService = new SequenceService(mongodb);
    }

    private Map<Map<String, Object>, Document> importFullReferenceData() {
        String sql = "select ROWID, t.* from CDE_DESC_VALUE t";
        Map<Map<String, Object>, Document> map = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            Map<String, Object> key = new LinkedHashMap<>();
            key.put(Field.CTRY_REC_CDE, rs.getString(Field.CTRY_REC_CDE));
            key.put(Field.GRP_MEMBR_REC_CDE, rs.getString(Field.GRP_MEMBR_REC_CDE));
            key.put(Field.CDV_TYPE_CDE, rs.getString(Field.CDV_TYPE_CDE));
            key.put(Field.CDV_CDE, rs.getString(Field.CDV_CDE));
            row = CodeUtils.toCamelCase(row);
            row.put(Field._id, sequenceService.nextId(Sequence.referenceDataId));
            row.put(Field.revision, 1L);
            row.put(Field.createdBy, "load");
            map.put(key, new Document(row));
        });
        return map;
    }

    private Map<Map<String, Object>, List<String>> retrieveReferenceDataChannelCode() {
        String sql = "select * from CDE_DESC_VALUE_CHANL_REL";
        Map<Map<String, Object>, List<String>> map = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql, rs -> {
            Map<String, Object> key = new LinkedHashMap<>();
            key.put(Field.CTRY_REC_CDE, rs.getString(Field.CTRY_REC_CDE));
            key.put(Field.GRP_MEMBR_REC_CDE, rs.getString(Field.GRP_MEMBR_REC_CDE));
            key.put(Field.CDV_TYPE_CDE, rs.getString(Field.CDV_TYPE_CDE));
            key.put(Field.CDV_CDE, rs.getString(Field.CDV_CDE));

            List<String> list = map.getOrDefault(key, new LinkedList<>());
            list.add(rs.getString(Field.CHANL_COMN_CDE));
            map.putIfAbsent(key, list);
        });

        return map;
    }

    @Override
    public void load() {
        if(colReferenceData.countDocuments() > 0){
            colReferenceData.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading Reference Data ...");
        long start = System.currentTimeMillis();

        Map<Map<String, Object>, Document> refDataMap = importFullReferenceData();
        Map<Map<String, Object>, List<String>> chanlCodeMap = retrieveReferenceDataChannelCode();
        chanlCodeMap.forEach((key, chanlCodeList) -> {      // attach chanlComnCde attribute to the reference data document
            Document doc = refDataMap.get(key);
            if(null != doc){
                doc.put(Field.chanlComnCde, chanlCodeList);
            }
        });

        List<List<Document>> batchList = separateBatch(new LinkedList<>(refDataMap.values()));
        batchList.forEach(batch -> colReferenceData.insertMany(batch));

        long cost = (System.currentTimeMillis() - start) / 1000;
        log.info("{} seconds consumed", cost);
    }



    private List<List<Document>> separateBatch(List<Document> docs) {
        List<List<Document>> batchList = new LinkedList<>();
        int batchSize = 1000;
        for(int i=0; i<docs.size(); i+=batchSize){
            int toIndex = i + batchSize;
            if(toIndex > docs.size()) toIndex = docs.size();
            batchList.add(docs.subList(i, toIndex));
        }
        return batchList;
    }
}
