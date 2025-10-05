package com.dummy.wpb.wpc.utils.service;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.RowSnapshot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class ExtFieldService {
    private final MongoDatabase mongoDatabase;
    private Map<String, String> dataType2fieldName;
    private Map<String, String> attrFromToMap;
    // ext field that has list value
    private static List<String> extFieldAsList = Arrays.asList("jurisCde", "wrapCde", "paymentSchedule", "autocallDtList", "relMgrIds", "switchFundGrpList");
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExtFieldService(MongoDatabase mongoDatabase, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.mongoDatabase = mongoDatabase;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        dataType2fieldName = new ImmutableMap.Builder<String, String>()
                .put("Char", "fieldCharValueText")
                .put("String", "fieldStrngValueText")
                .put("Integer", "fieldIntgValueNum")
                .put("Decimal", "fieldDcmlValueNum")
                .put("Date", "fieldDtValueDt")
                .put("Timestamp", "fieldTsValueDtTm")
                .build();
        attrFromToMap = getAttrFromToMap();
    }

    private Map<String, String> getAttrFromToMap() {
        Map<String, String> fromTo = new HashMap<>();
        MongoCollection<Document> configuration = mongoDatabase.getCollection(CollectionName.configuration);
        Document remodel = configuration.find(Filters.eq("name","user-defined-field-mapping")).first();
        if(remodel==null) return fromTo;
        Document config = (Document) remodel.get("config");
        config.keySet().stream().forEach(configKey -> {
            List<Map<String, String>> udfMappings = (List<Map<String, String>>) config.get(configKey);
            udfMappings.stream().forEach(mapping -> {
                String fieldCde = mapping.get("fieldCde");
                //only udf field will to camel case
                if("udf".equalsIgnoreCase(configKey)){
                    fieldCde = CodeUtils.toCamelCase(fieldCde);
                }
                fromTo.put(configKey + "." + fieldCde, mapping.get("jsonPath"));
            });
        });
        return fromTo;
    }

    /**
     * Map an attribute to another
     *
     * @param attr
     * @return
     */
    public String remodel(String attr) {
        return attrFromToMap.getOrDefault(attr, attr);
    }

    /**
     * Read ext field value, in case the value type is a list type, need to read values from other records as well.
     *
     * @param rowData
     * @return
     */
    public Object readValue(Map<String, Object> rowData, Map<String, List<Object>> listValueMap) {
        Objects.requireNonNull(listValueMap, "listValueMap can not be null");
        String key = String.format("%s@%s", rowData.get("fieldCde"), rowData.get("prodId"));
        List<Object> listValue = listValueMap.get(key);

        if(null != listValue) {    // in case it's a list value
            return listValue;
        } else {    // otherwise, get the value according to dataType
            String dataType = (String) rowData.get(Field.fieldDataTypeText);
            String fieldName = dataType2fieldName.get(dataType);
            Objects.requireNonNull(fieldName, "fieldName not found for data type: " + dataType);
            return rowData.get(fieldName);
        }
    }

    /**
     * retrieve prodIds which needs to update ext list fields
     *
     * @param rowDataMap
     * @return
     */
    public Set<Long> getExtListFieldProds(Map<String, Map<String, Object>> rowDataMap, Map<String, RowSnapshot> snapshotMap){
        Set<Long> prodIds = new HashSet<>();
        //prodIds that need to update ext list fields
        for (Map.Entry<String,Map<String, Object>> entry : rowDataMap.entrySet()) {
            Map<String, Object> row = entry.getValue();
            if(extFieldAsList.contains(row.get(Field.fieldCde))) {
                prodIds.add((Long) row.get(Field.prodId));
            }
        }

        // for DELETE case, rowData will be missing, get the prodId from snapshot
        for (Map.Entry<String, RowSnapshot> entry : snapshotMap.entrySet()) {
            if (null != entry.getValue() && null != entry.getValue().getData()) {
                Map<String, Object> snapshot = entry.getValue().getData();
                if (extFieldAsList.contains(snapshot.get(Field.fieldCde))) {
                    prodIds.add((Long) snapshot.get(Field.prodId));
                }
            }
        }
        return prodIds;
    }

    /**
     * Read row data from a table with given prodIds list,
     *
     * @param tableName
     * @param prodIds
     * @return [wrapCde@1000047573, row data] map
     */
    public Map<String, List<Object>> readExtListValueMap(String tableName, Set<Long> prodIds){
        Map<String, List<Object>> rowMap = new LinkedHashMap<>();
        if(CollectionUtils.isEmpty(prodIds)) {
            return rowMap;
        }

        // just refer to one of these FIELD_CDE but here search them all now
        String sql = String.format("select rowid, t.* from %s t where PROD_ID in (:prodIds) and FIELD_CDE in (:fields) ", tableName);
        SqlParameterSource parameters = new MapSqlParameterSource("prodIds", prodIds).addValue("fields", extFieldAsList);

        namedParameterJdbcTemplate.query(sql, parameters, rs -> {

            Map<String, Object> row = CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs));

            String key =  String.format("%s@%s",row.get(Field.fieldCde),row.get(Field.prodId));

            List<Object> values = rowMap.get(key);
            if(CollectionUtils.isEmpty(values)) values=new ArrayList<>();
            Object fieldValue = row.get(dataType2fieldName.get(row.get(Field.fieldDataTypeText)));
            values.add(fieldValue);
            rowMap.put(key, values);
        });
        return rowMap;
    }


}
