package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.ProductHelper;
import com.dummy.wpb.wpc.utils.ProductMapping;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.productLock;
import com.dummy.wpb.wpc.utils.model.ProductTableInfo;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sync data from Oracle DB to MongoDB, which TB_PROD.REC_UPDT_DT_TM > a given timestamp
 */
@Slf4j
@SuppressWarnings("java:S115")
public abstract class ProductDataHandler {
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected static final String TARGET_COLLECTION_NAME = CollectionName.product;
    protected static final String Config_COLLECTION_NAME = CollectionName.configuration;
    protected static final String Metadata_COLLECTION_NAME = CollectionName.metadata;
    protected MongoDatabase mongodb;
    protected MongoCollection<Document> colProduct;
    protected MongoCollection<Document> configColl;
    protected MongoCollection<Document> metadataColl;
    // <table, listFields>
    protected List<String> listExtFields;

    protected boolean multipleThreadLoad;

    protected ThreadPoolTaskExecutor threadPoolTaskExecutor;
    protected SequenceService sequenceService;
    protected LockService lockService;

    private static String asMasterTable = "importMasterTable";
    private static String asObject = "importAsObject";
    private static String asObjectList = "importAsObjectList";
    private static String asStringList = "importAsStringList";
    private static String asExt = "importExtensionFields";
    private static String asUdf = "importUserDefinedFields";
    private static String asPriceHistoryTable = "importPriceHistoryTable";

    private static String prodIdListParamName = "prodIdList";

    protected ProductDataHandler(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.mongodb = mongodb;
        this.colProduct = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.configColl = mongodb.getCollection(Config_COLLECTION_NAME);
        this.metadataColl = mongodb.getCollection(Metadata_COLLECTION_NAME);
        initExtListFieldMap();
    }

    /**
     * Fetch from existing data, to know the fields that should be treated as List
     *
     * @return
     */
    protected void initExtListFieldMap() {
        Document configDoc = configColl.find(Filters.eq("_id", "ALL/user-defined-field-mapping")).first();
        if (MapUtils.isEmpty(configDoc)) {
            return;
        }

        Document fieldConfigDoc = configDoc.get("config", new Document());
        List<Document> allExtFields = new ArrayList<>();
        Stream.of("ext", "extOp", "extEg", "udf").forEach(extType ->
            allExtFields.addAll(fieldConfigDoc.get(extType, new ArrayList<>())));

        List<Bson> attrNameFilters = new ArrayList<>();
        for (Document extField : allExtFields) {
            String fieldCde = extField.getString("fieldCde").replace("[*]", "");
            attrNameFilters.add(Filters.regex("attrName", String.format("^%s(\\[\\*\\])?$", fieldCde)));
        }

        Bson filters = Filters.and(Filters.or(attrNameFilters), Filters.regex("graphQLType", "\\[*\\]"));
        listExtFields = metadataColl
                .find(filters)
                .into(new ArrayList<>())
                .stream()
                .map(metadata -> metadata.getString("attrName").replace("[*]", ""))
                .collect(Collectors.toList());
    }

    protected String getSql(String name, ProductTableInfo info) {
        String sql = "select rowid, t.* from %s t where %s in (:prodIdList) order by %s";
        if (asMasterTable.equals(name)) {
            return String.format("select rowid, t.* from %s t where PROD_ID in (:prodIdList)", info.getTable());
        }
        if (asPriceHistoryTable.equals(name)) {
            return String.format("select rowid, t.* from %s t", info.getTable());
        }
        if (asObject.equals(name)) {
            return String.format(sql, info.getTable(), info.getProdIdField(), info.getProdIdField());
        }
        if (asObjectList.equals(name)) {
            if ("TB_EQTY_LINK_INVST_UNDL_STOCK".equals(info.getTable()) || "TB_STRUC_INVST_DEPST_INTRM_RTN".equals(info.getTable())) {
                return String.format("select rowid, t.* from %s t where %s in (:prodIdList) order by %s ,REC_CREAT_DT_TM", info.getTable(), info.getProdIdField(), info.getProdIdField());
            }
            if ("PROD_TRADE_CCY".equals(info.getTable())) {
                return String.format("select rowid, t.* from %s t where %s in (:prodIdList) order by %s ,CCY_PROD_TRADE_CDE", info.getTable(), info.getProdIdField(), info.getProdIdField());
            }
            return String.format(sql, info.getTable(), info.getProdIdField(), info.getProdIdField());
        }
        if (asExt.equals(name)) {
            return String.format("select rowid, t.* from %s t where %s in (:prodIdList) order by PROD_ID, FIELD_CDE, FIELD_SEQ_NUM", info.getTable(), info.getProdIdField());
        }
        if (asUdf.equals(name)) {
            return String.format("select rowid, t.* from %s t where PROD_ID in (:prodIdList) order by PROD_ID", info.getTable());
        }
        if (asStringList.equals(name)) {
            return String.format(sql, info.getTable(), info.getProdIdField(), info.getProdIdField());
        }
        throw new IllegalArgumentException("Not support import method: " + name);
    }

    protected abstract boolean showTableLoading();

    protected void importTables(List<Long> prodIdList) {
        Map<Long, Map<String, Object>> prodMap = getProdData(prodIdList);
        // save all products
        saveProducts(prodMap);
    }

    protected void importTablesByBatch(List<List<Long>> prodIdBatchList) {
        if (multipleThreadLoad) {
            log.debug("Loading prod with multiple thread ...");
            importTablesMultiThread(prodIdBatchList);
        } else {
            log.debug("Loading prod with single thread ...");
            importTablesSingleThread(prodIdBatchList);
        }
    }

    protected void importTablesMultiThread(List<List<Long>> prodIdBatchList) {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < prodIdBatchList.size(); i++) {
            int batchIndex = i;

            tasks.add(
                    CompletableFuture.runAsync(() -> {
                        lockService.heartbeat(productLock.DATA_UTILS_TASK_LOCK);
                        importTables(prodIdBatchList.get(batchIndex));
                        log.info("{}th batch of {} batches loaded ...", (batchIndex + 1), prodIdBatchList.size());
                    }, threadPoolTaskExecutor)
            );
        }
        CompletableFuture<Void> results = CompletableFuture
                .allOf(tasks.toArray(new CompletableFuture[tasks.size()]));
        results.join();
    }

    protected void importTablesSingleThread(List<List<Long>> prodIdBatchList) {
        for (int i = 0; i < prodIdBatchList.size(); i++) {
            importTables(prodIdBatchList.get(i));
            log.info("{}/{} batch loaded ...", (i + 1), prodIdBatchList.size());
        }
    }

    protected List<List<Long>> breakdownByBatch(List<Long> list) {
        List<List<Long>> batchList = new LinkedList<>();
        int batchSize = 1000;
        for (int i = 0; i < list.size(); i += batchSize) {
            int toIndex = i + batchSize;
            if (toIndex > list.size()) toIndex = list.size();
            List<Long> batch = list.subList(i, toIndex);
            batchList.add(batch);
        }
        return batchList;
    }

    protected Map<Long, Map<String, Object>> getProdData(List<Long> prodIdList) {
        List<ProductTableInfo> tableList = ProductMapping.getTableInfoList();
        Map<Long, Map<String, Object>> prodMap = null;
        for (ProductTableInfo tableInfo : tableList) {
            if (showTableLoading()) {
                log.info("{} is loading ...", tableInfo.getTable());
            }
            tableInfo.setToAttribute(CodeUtils.toCamelCase(tableInfo.getToAttribute()));
            String importMethod = tableInfo.getImportMethod();
            if (asMasterTable.equals(importMethod)) {
                prodMap = importMasterTable(tableInfo, prodIdList);
            } else {
                Map<Long, Object> objMap = getObjMap(importMethod, tableInfo, prodIdList);
                if (objMap.size() > 0) {
                    attachProductField(prodMap, tableInfo.getToAttribute(), objMap);
                }
            }
        }
        remodelProductsViaConfig(prodMap);
        return prodMap;
    }

    private Map<Long, Object> getObjMap(String importMethod, ProductTableInfo tableInfo, List<Long> prodIdList) {
        if (asObject.equals(importMethod)) {
            return importAsObject(tableInfo, prodIdList);
        } else if (asObjectList.equals(importMethod)) {
            return importAsObjectList(tableInfo, prodIdList);
        } else if (asStringList.equals(importMethod)) {
            return importAsStringList(tableInfo, prodIdList);
        } else if (asExt.equals(importMethod)) {
            return importExtensionFields(tableInfo, prodIdList);
        } else if (asUdf.equals(importMethod)) {
            return importUserDefinedFields(tableInfo, prodIdList);
        }
        return new HashMap<>();
    }

    /**
     * Remodel UDF and EXT fields
     *
     * @param prodMap
     */
    private void remodelProductsViaConfig(Map<Long, Map<String, Object>> prodMap) {

        Document config = new Document();
        Document document = configColl.find(Filters.eq("_id", "ALL/user-defined-field-mapping")).first();
        if (document == null) return;
        if (document.get("name").equals("user-defined-field-mapping")) {
            config = (Document) document.get("config");
        }

        Document finalConfig = config;
        if (prodMap != null && !prodMap.isEmpty()) {
            prodMap.forEach((prodId, prod) -> {
                try {
                    ProductHelper.remodelViaConfig(prod, finalConfig);
                    removeEmptyExtAttr(prod);
                } catch (Exception e) {
                    log.error("this product remodel error :" + prodId.toString());
                }
            });
        }

    }

    private void removeEmptyExtAttr(Map<String, Object> prod) {
        List<String> removeKeys = Arrays.asList("ext", "extOp", "extEg", "udf");
        removeKeys.forEach(key -> {
            if (prod.containsKey(key)) {
                Map<String, Object> values = (Map<String, Object>) prod.get(key);
                if (values.size() == 0) prod.remove(key);
            }
        });
    }

    protected abstract void saveProducts(Map<Long, Map<String, Object>> prodMap);

    private void attachProductField(Map<Long, Map<String, Object>> prodMap, String toAttribute, Map<Long, Object> feedingMap) {
        String attr = CodeUtils.toCamelCase(toAttribute);

        String[] attrs = attr.split("\\.");
        feedingMap.forEach((prodId, value) -> {
            Map<String, Object> prod = prodMap.get(prodId);
            if (null != prod) {
                if (attrs.length == 1) {
                    if ("perfm".equals(attrs[0])) {
                        // special handling for performance data, separate the value into performance or benchmark
                        attachPerfmField(prod, (List<Map<String, Object>>) value);
                    } else {
                        prod.put(attrs[0], value);
                    }
                } else if (attrs.length == 2) {       // handle a.b form
                    Map<String, Object> obj = (Map<String, Object>) prod.getOrDefault(attrs[0], new LinkedHashMap<>());
                    obj.put(attrs[1], value);
                    prod.putIfAbsent(attrs[0], obj);
                } else {
                    // no a.b.c level currently
                    throw new IllegalArgumentException("Does not support attribute for: " + attr);
                }
            }
        });
    }

    /**
     * To better design the UI config, separate data in the PROD_PERFM into 2 fields
     * <p>
     * PERFM_TYPE_CDE=P --> performance
     * PERFM_TYPE_CDE=B --> benchmark
     *
     * @param prod
     * @param list
     */
    protected void attachPerfmField(Map<String, Object> prod, List<Map<String, Object>> list) {
        if (null == list || list.isEmpty()) return;
        list.forEach(map -> {
            String perfmTypeCde = (String) map.get(Field.perfmTypeCde);
            map.remove(Field.rowid);
            if ("P".equals(perfmTypeCde)) {
                prod.put(Field.performance, map);
            } else if ("B".equals(perfmTypeCde)) {
                prod.put(Field.benchmark, map);
            }
        });
    }

    /**
     * Migrate data in TB_PROD table
     *
     * @return
     */
    private Map<Long, Map<String, Object>> importMasterTable(ProductTableInfo info, List<Long> prodIdList) {
        Map<Long, Map<String, Object>> map = new LinkedHashMap<>();
        String sql = getSql("importMasterTable", info);
        SqlParameterSource parameters = new MapSqlParameterSource(prodIdListParamName, prodIdList);
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            Map<String, Object> prod = CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs));
            Long prodId = (Long) prod.get(Field.prodId);
            prod.put(Field._id, prodId);

            // remove and prodParntId
            prod.remove(Field.prodParntId);

            map.put(prodId, prod);
            combineCtryProdTradeCde(prod);
        });
        return map;
    }

    /**
     * Combine ctryProdTrade1Cde ... ctryProdTrade5Cde into a [String] field ctryProdTradeCde
     *
     * @param prod
     */
    private void combineCtryProdTradeCde(Map<String, Object> prod) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String key = String.format("ctryProdTrade%dCde", i);
            String value = (String) prod.get(key);
            if (null != value) list.add(value);
            prod.remove(key);
        }
        prod.put(Field.ctryProdTradeCde, list);
    }

    private Map<Long, Object> importAsObject(ProductTableInfo info, List<Long> prodIdList) {
        Map<Long, Object> map = new LinkedHashMap<>();
        String sql = getSql("importAsObject", info);
        SqlParameterSource parameters = new MapSqlParameterSource(prodIdListParamName, prodIdList);
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            Long prodId = (Long) row.get(info.getProdIdField());
            DbUtils.removeFields(row, info.getProdIdField());
            row = CodeUtils.toCamelCase(row);
            row.remove(Field.rowid);    // no rowid for objects
            map.put(prodId, row);
        });
        return map;
    }

    private Map<Long, Object> importAsObjectList(ProductTableInfo info, List<Long> prodIdList) {
        String sql = getSql("importAsObjectList", info);
        SqlParameterSource parameters = new MapSqlParameterSource(prodIdListParamName, prodIdList);
        Map<Object, List<Map<String, Object>>> listMap = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            Object id = row.get(info.getProdIdField());
            DbUtils.removeFields(row, info.getProdIdField());
            row = CodeUtils.toCamelCase(row);

            List<Map<String, Object>> list = listMap.getOrDefault(id, new LinkedList<>());
            list.add(row);
            listMap.putIfAbsent(id, list);
        });

        Map<Long, Object> map = new LinkedHashMap<>();
        listMap.forEach((prodId, list) -> map.put((Long) prodId, list));
        return map;
    }

    private Map<Long, Object> importExtensionFields(ProductTableInfo info, List<Long> prodIdList) {
        String sql = getSql(asExt, info);
        SqlParameterSource parameters = new MapSqlParameterSource(prodIdListParamName, prodIdList);

        Map<Long, List<Map<String, Object>>> rowListMap = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            Long prodId = (Long) row.get(info.getProdIdField());
            List<Map<String, Object>> rowList = rowListMap.getOrDefault(prodId, new LinkedList<>());
            rowListMap.putIfAbsent(prodId, rowList);
            rowList.add(row);
        });

        Map<Long, Object> map = new LinkedHashMap<>();
        rowListMap.forEach((prodId, rowList) -> map.put(prodId, DbUtils.extFieldToMap(rowList, listExtFields)));
        return map;
    }

    private Map<Long, Object> importUserDefinedFields(ProductTableInfo info, List<Long> prodIdList) {
        String sql = getSql(asUdf, info);
        SqlParameterSource parameters = new MapSqlParameterSource(prodIdListParamName, prodIdList);
        Map<Long, Map<String, Object>> prodUdfMap = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            Long prodId = (Long) row.get(info.getProdIdField());
            String fieldName = CodeUtils.toCamelCase((String) row.get(Field.FIELD_CDE));
            String fieldValue = (String) row.get(Field.FIELD_VALUE_TEXT);
            Map<String, Object> udfMap = prodUdfMap.getOrDefault(prodId, new LinkedHashMap<>());
            prodUdfMap.putIfAbsent(prodId, udfMap);
            udfMap.put(fieldName, fieldValue);
        });

        Map<Long, Object> map = new LinkedHashMap<>();
        prodUdfMap.forEach((prodId, udfMap) -> map.putAll(prodUdfMap));
        return map;
    }

    private Map<Long, Object> importAsStringList(ProductTableInfo info, List<Long> prodIdList) {
        String sql = getSql(asStringList, info);
        SqlParameterSource parameters = new MapSqlParameterSource(prodIdListParamName, prodIdList);
        Map<Object, List<Object>> listMap = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql, parameters, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            Object id = row.get(info.getProdIdField());
            DbUtils.removeFields(row, info.getProdIdField());

            List<Object> list = listMap.getOrDefault(id, new LinkedList<>());
            listMap.putIfAbsent(id, list);
            list.add(row.get(info.getValueField()));
        });

        Map<Long, Object> map = new LinkedHashMap<>();
        listMap.forEach((prodId, list) -> map.put((Long) prodId, list));
        return map;
    }
}