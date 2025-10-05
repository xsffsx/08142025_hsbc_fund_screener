package com.dummy.wpb.wpc.utils.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.ProductMapping;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.load.*;
import com.dummy.wpb.wpc.utils.model.*;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.ProductService;
import com.dummy.wpb.wpc.utils.service.SyncService;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wpb.wpc.utils.CommonUtils.getHostname;

/**
 * Delta sync WPC data from OracleDB to MongoDB
 */
@Component
@Slf4j
public class DeltaSyncTask implements SyncTask {
    @Value("#{${product.sync.multiple-thread-load}}")
    private boolean multipleThreadLoad;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MongoDatabase mongodb;

    @Autowired
    private ProductService productService;

    @Autowired
    private SyncService syncService;

    @Autowired
    private LockService lockService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Set<Map<String, Object>>> finishedKeySetMap;
    @Value("0")
    private Long processStartMils;
    @Value("")
    private String lockToken;
    @Value("#{${product.sync.clean-auto-created-products}}")
    private boolean cleanAutoCreatedProducts;

    public DeltaSyncTask(){
        finishedKeySetMap = new LinkedHashMap<>();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String getTaskName(){
        return "DeltaSyncTask";
    }

    @Override
    public void run() {
        processStartMils = System.currentTimeMillis();

        boolean allDone;
        finishedKeySetMap.clear();
        syncProductData();
        do {
            lockService.heartbeat(productLock.DATA_UTILS_TASK_LOCK);
            allDone = syncNonProductData();
        } while (!allDone);

        // for UK on the fly auto creation
        if(cleanAutoCreatedProducts) {
            productService.cleanAutoCreatedProducts();
        }

        long seconds = (System.currentTimeMillis() - processStartMils) / 1000;
        if (seconds > 0) {
            log.info("{} seconds consumed", seconds);
        }
        log.info("MaxMemory = {}MB,  TotalMemory = {}MB,  FreeMemory = {}MB",
                (Runtime.getRuntime().maxMemory() / 1024 / 1024),
                (Runtime.getRuntime().totalMemory() / 1024 / 1024),
                (Runtime.getRuntime().freeMemory()) / 1024 / 1024);
    }

    /**
     * sync a batch, 1000 records
     *
     * @return true if all done, else false
     */
    private boolean syncNonProductData() {
        Date processStartTime = DbUtils.toUTCDate(new Date(processStartMils));
        List<DataChangeEvent> changeList = syncService.retrieveNonProductChangeEvents(processStartTime);
        if(CollectionUtils.isEmpty(changeList)){
            // No update
            return true;
        }

        Date startTime = changeList.get(0).getTs();
        Date endTime = changeList.get(changeList.size() - 1).getTs();
        log.info("Events {}, {} --> {} ...", changeList.size(), startTime, endTime);
        GeneralSyncLog syncLog = new GeneralSyncLog(changeList, startTime, endTime, new Date());

        // group records by data category, eg. PRODUCT, REFERENCE_DATA ... <table name, DataChangeEvent set>
        Map<String, Set<DataChangeEvent>> eventSetMap = groupEventsByTable(changeList);

        // retrieve master table key list <table name, key map set>, Be aware that for some tables like TB_PROD related tables, master keys will be grouped into on table like TB_PROD
        Map<String, Set<Map<String, Object>>> keySetMap = collectMasterTableKeySet(eventSetMap);

        // for each data category, fetch all related data and feed into MongoDB
        Map<String, Set<String>> deletedSetMap = new LinkedHashMap<>();
        keySetMap.forEach((table, keySet) -> {
            DataSynchronizer synchronizer = getDataSynchronizer(table);

            // handle INSERT, UPDATE items
            Set<Map<String, Object>> finishedSet = finishedKeySetMap.getOrDefault(table, new LinkedHashSet<>());
            finishedKeySetMap.putIfAbsent(table, finishedSet);
            keySet.removeAll(finishedSet);  // avoid duplicate effort

            if(CollectionUtils.isNotEmpty(keySet)) {
                synchronizer.sync(keySet);
                // registered to avoid duplicate handling
                finishedSet.addAll(keySet);
            }

            // handle DELETE items
            Set<DataChangeEvent> eventSet = eventSetMap.get(table);
            if(null != eventSet) {  // could be null, for example only PROD_PRC_CMPAR is updated, it will be mapped to TB_PROD
                List<String> deleteList = eventSet.stream().filter(e -> "DELETE".equalsIgnoreCase(e.getOp())).map(DataChangeEvent::getRowId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(deleteList)) {
                    Set<String> rowIdSet = new LinkedHashSet<>(deleteList);
                    synchronizer.delete(rowIdSet);
                    deletedSetMap.put(table, rowIdSet);
                }
            }
        });

        // update data_sync_log
        syncLog.setLockToken(lockToken);
        syncLog.setSyncEndTime(new Date());
        syncLog.setHostname(getHostname());
        syncLog.setMasterKeys(keySetMap);
        syncLog.setDeletedSetMap(deletedSetMap);
        syncService.writeDeltaSyncLog(syncLog);

        // clean data change event which has been handled
        syncService.deleteDataChangeEvents(changeList);

        return changeList.size() < 1000;    // < 1000 indicate there is no more events left at the moment
    }

    /**
     * handle all product related events in one go
     *
     */
    private void syncProductData() {
        Date processStartTime = DbUtils.toUTCDate(new Date(processStartMils));
        long start = System.currentTimeMillis();
        ProductEventGroup group = syncService.retrieveProductChangeEvents(processStartTime);
        if(Objects.isNull(group) || group.getEventCount() == 0){
            // No update
            return;
        }

        log.info("Event: {}, product: {}, ({}, {}) ...", group.getEventCount(), group.getProdIdList().size(), group.getEventStartTime(), group.getEventEndTime());
        ProductSyncLog syncLog = new ProductSyncLog(group);

        ProductDataSynchronizer synchronizer = (ProductDataSynchronizer)getDataSynchronizer(Table.TB_PROD);
        synchronizer.syncProds(group.getProdIdList());
        log.debug("syncProds cost {}", System.currentTimeMillis() - start);

        // update data_sync_log
        syncLog.setSyncEndTime(new Date());
        syncLog.setHostname(getHostname());
        syncLog.setLockToken(lockToken);
        syncService.writeDeltaSyncLog(syncLog);
        // clean data change event which has been handled
        syncService.deleteProductChangeEvents(group);
    }

    private DataSynchronizer getDataSynchronizer(String table) {
        switch (table) {
            case Table.TB_PROD:
                return new ProductDataSynchronizer(namedParameterJdbcTemplate, mongodb, threadPoolTaskExecutor, multipleThreadLoad, lockService);
            case Table.CDE_DESC_VALUE:
                return new ReferenceDataSynchronizer(namedParameterJdbcTemplate, mongodb, threadPoolTaskExecutor);
            case Table.ASET_VOLTL_CLASS_CHAR:
                return new AsetVoltlClassCharSynchronizer(namedParameterJdbcTemplate, mongodb);
            case Table.ASET_VOLTL_CLASS_CORL:
                return new AsetVoltlClassCorlSynchronizer(namedParameterJdbcTemplate, mongodb);
            case Table.PROD_TYPE_FIN_DOC:
            case Table.PROD_SUBTP_FIN_DOC:
                return new ProdTypeFinDocSynchronizer(table, namedParameterJdbcTemplate, mongodb);
            case Table.PROD_TYPE_STAF_LIC_CHECK:
            case Table.PROD_SUBTP_STAF_LIC_CHECK:
                return new StaffLicenceCheckSynchronizer(table, namedParameterJdbcTemplate, mongodb);
            case Table.PROD_ATRIB_MAP:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.prod_atrib_map, threadPoolTaskExecutor);
            case Table.PROD_PRC_HIST:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.prod_prc_hist, threadPoolTaskExecutor);
            case Table.CHANL_COMN_CDE:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.chanl_comn_cde, threadPoolTaskExecutor);
            case Table.PEND_APROVE_TRAN:
                return new PendAppoveTranSynchronizer(namedParameterJdbcTemplate, mongodb);
            case Table.FIN_DOC:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.fin_doc, threadPoolTaskExecutor);
            case Table.FIN_DOC_HIST:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.fin_doc_hist, threadPoolTaskExecutor);
            case Table.FIN_DOC_UPLD:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.fin_doc_upld, threadPoolTaskExecutor);
            case Table.PROD_TYPE_FIN_DOC_TYPE_REL:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.prod_type_fin_doc_type_rel, threadPoolTaskExecutor);
            case Table.TB_PROD_TYPE_CHANL_ATTR:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.prod_type_chanl_attr, threadPoolTaskExecutor);
            case Table.LOG_EQTY_LINK_INVST:
                return new SingleTableSynchronizer(namedParameterJdbcTemplate, table, mongodb, CollectionName.log_eqty_link_invst, threadPoolTaskExecutor);
            case Table.SYS_PARM:
            case Table.FIN_DOC_PARM:
                return new SysParamSynchronizer(table, namedParameterJdbcTemplate, mongodb);
            case Table.PROD_NAME_SEQS:
                return new ProdNameSeqsSynchronizer( namedParameterJdbcTemplate, mongodb);
            default:
                throw new IllegalArgumentException("Unsupported table: "+table);
        }
    }

    public void setLockToken(String lockToken) {
        this.lockToken = lockToken;
    }

    /**
     * Categorize to master table primary key set
     *
     * @param eventSetMap
     * @return
     */
    public Map<String, Set<Map<String, Object>>> collectMasterTableKeySet(Map<String, Set<DataChangeEvent>> eventSetMap) {
        Map<String, Set<Map<String, Object>>> rowidKeySetMap = new LinkedHashMap<>();
        eventSetMap.forEach((table, events) -> {
            Set<String> rowIdSet = new LinkedHashSet<>();
            events.forEach(event -> rowIdSet.add(event.getRowId()));
            if(Table.CDE_DESC_VALUE.equals(table) || Table.CDE_DESC_VALUE_CHANL_REL.equals(table)) {
                String sql = String.format("select DISTINCT CTRY_REC_CDE, GRP_MEMBR_REC_CDE, CDV_TYPE_CDE, CDV_CDE from %s where rowid in (:rowIdSet)", table);
                List<Map<String, Object>> keyList = retrieveKeyList(sql, rowIdSet);
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(Table.CDE_DESC_VALUE, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(Table.CDE_DESC_VALUE, keySet);
            } else if(Table.ASET_VOLTL_CLASS_CHAR.equals(table)){
                String sql = String.format("select DISTINCT CTRY_REC_CDE, GRP_MEMBR_REC_CDE from %s where rowid in (:rowIdSet)", table);
                List<Map<String, Object>> keyList = retrieveKeyList(sql, rowIdSet);
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(Table.ASET_VOLTL_CLASS_CHAR, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(Table.ASET_VOLTL_CLASS_CHAR, keySet);
            } else if(Table.ASET_VOLTL_CLASS_CORL.equals(table)){
                String sql = String.format("select DISTINCT CTRY_REC_CDE, GRP_MEMBR_REC_CDE from %s where rowid in (:rowIdSet)", table);
                List<Map<String, Object>> keyList = retrieveKeyList(sql, rowIdSet);
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(Table.ASET_VOLTL_CLASS_CORL, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(Table.ASET_VOLTL_CLASS_CORL, keySet);
            } else if(Table.PROD_TYPE_FIN_DOC.equals(table)
                    || Table.PROD_SUBTP_FIN_DOC.equals(table)
//                    || Table.PROD_TYPE_STAF_LIC_CHECK.equals(table)
//                    || Table.PROD_SUBTP_STAF_LIC_CHECK.equals(table)
                    || Table.PROD_ATRIB_MAP.equals(table)
                    || Table.CHANL_COMN_CDE.equals(table)
                    || Table.PROD_PRC_HIST.equals(table)
                    || Table.FIN_DOC.equals(table)
                    || Table.FIN_DOC_HIST.equals(table)
                    || Table.FIN_DOC_UPLD.equals(table)
                    || Table.PROD_TYPE_FIN_DOC_TYPE_REL.equals(table)
                    || Table.TB_PROD_TYPE_CHANL_ATTR.equals(table)
                    || Table.SYS_PARM.equals(table)
                    || Table.FIN_DOC_PARM.equals(table)
                    || Table.LOG_EQTY_LINK_INVST.equals(table)
                    || Table.PROD_NAME_SEQS.equals(table)
            ){
                // single table to a collection, they are the same, use ROWID instead of their own key set to simplify the design
                List<Map<String, Object>> keyList = new ArrayList<>();
                rowIdSet.forEach(rowid -> keyList.add(Collections.singletonMap(Field.ROWID, rowid)));
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(table, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(table, keySet);
            }else if(Table.PEND_APROVE_TRAN.equals(table)){

                String sql = String.format("select DISTINCT CTRY_REC_CDE, GRP_MEMBR_REC_CDE,REC_PEND_APROVE_NUM from %s where rowid in (:rowIdSet)", table);
                List<Map<String, Object>> keyList = retrieveKeyList(sql, rowIdSet);
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(Table.PEND_APROVE_TRAN, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(Table.PEND_APROVE_TRAN, keySet);

            } else if (Table.PROD_TYPE_STAF_LIC_CHECK.equals(table)) {
                String sql = String.format("select PROD_TYPE_CDE, EMPLOY_POSN_CDE, CTRY_REC_CDE, GRP_MEMBR_REC_CDE from %s where rowid in (:rowIdSet)", table);
                List<Map<String, Object>> keyList = retrieveKeyList(sql, rowIdSet);
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(Table.PROD_TYPE_STAF_LIC_CHECK, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(Table.PROD_TYPE_STAF_LIC_CHECK, keySet);
            } else if (Table.PROD_SUBTP_STAF_LIC_CHECK.equals(table)) {
                    String sql = String.format("select EMPLOY_POSN_CDE, PROD_SUBTP_CDE, CTRY_REC_CDE, GRP_MEMBR_REC_CDE from %s where rowid in (:rowIdSet)", table);
                List<Map<String, Object>> keyList = retrieveKeyList(sql, rowIdSet);
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(Table.PROD_SUBTP_STAF_LIC_CHECK, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(Table.PROD_SUBTP_STAF_LIC_CHECK, keySet);
            } else {
                String prodIdField = ProductMapping.getProductIdField(table);
                String sql = String.format("select DISTINCT %s as PROD_ID from %s where rowid in (:rowIdSet)", prodIdField, table);
                List<Map<String, Object>> keyList = retrieveKeyList(sql, rowIdSet);
                Set<Map<String, Object>> keySet = rowidKeySetMap.getOrDefault(Table.TB_PROD, new LinkedHashSet<>());
                keySet.addAll(keyList);
                rowidKeySetMap.put(Table.TB_PROD, keySet);
            }
        });
        return rowidKeySetMap;
    }

    private List<Map<String, Object>> retrieveKeyList(String sql, Set<String> rowIdSet) {
        SqlParameterSource parameters = new MapSqlParameterSource("rowIdSet", rowIdSet);
        List<Map<String, Object>> list = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, parameters, (RowCallbackHandler) rs ->
            list.add(DbUtils.getStringObjectMap(rs))
        );
        return list;
    }

    /**
     * Group change records by table, so that we can handle changes table by table
     *
     * @param list
     * @return
     */
    public Map<String, Set<DataChangeEvent>> groupEventsByTable(List<DataChangeEvent> list) {
        Map<String, Set<DataChangeEvent>> map = new LinkedHashMap<>();
        list.forEach(item -> {
            Set<DataChangeEvent> eventSet = map.getOrDefault(item.getTableName(), new LinkedHashSet<>());
            eventSet.add(item);
            map.put(item.getTableName(), eventSet);
        });
        return map;
    }

}
