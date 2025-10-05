package com.dummy.wpb.wpc.utils.task;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.ProductMapping;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.load.*;
import com.dummy.wpb.wpc.utils.model.ProductTableInfo;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.SyncService;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Sync selected collections only
 */
@Slf4j
@Component
public class CollectionSyncTask implements SyncTask {
    @Value("#{${product.sync.multiple-thread-load}}")
    private boolean multipleThreadLoad;
    private final SyncService syncService;
    @Value("")
    private String lockToken;
    @Value("")
    private String collectionName;
    private final Map<String, DataLoader> dataLoaderMap;

    @Autowired
    CreateIndexesSyncTask createIndexesSyncTask;

    public CollectionSyncTask(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb,
                              SyncService syncService, ThreadPoolTaskExecutor threadPoolTaskExecutor, LockService lockService) {

        this.syncService = syncService;

        dataLoaderMap = new ImmutableMap.Builder<String, DataLoader>()
                .put(CollectionName.product, new ProductDataLoader(namedParameterJdbcTemplate, mongodb, threadPoolTaskExecutor, multipleThreadLoad, lockService))
                .put(CollectionName.prod_type_fin_doc, new ProdTypeFinDocLoader(namedParameterJdbcTemplate, mongodb))
                .put(CollectionName.staff_license_check, new StaffLicenseCheckLoader(namedParameterJdbcTemplate, mongodb))
                .put(CollectionName.reference_data, new ReferenceDataLoader(namedParameterJdbcTemplate, mongodb))
                .put(CollectionName.aset_voltl_class_char, new AsetVoltlClassCharLoader(namedParameterJdbcTemplate, mongodb))
                .put(CollectionName.aset_voltl_class_corl, new AsetVoltlClassCorlLoader(namedParameterJdbcTemplate, mongodb))
                .put(CollectionName.pend_appove_tran, new PendAppoveTranLoader(namedParameterJdbcTemplate, mongodb))
                .put(CollectionName.prod_atrib_map, new SingleTableLoader(namedParameterJdbcTemplate, Table.PROD_ATRIB_MAP, mongodb, CollectionName.prod_atrib_map, threadPoolTaskExecutor, lockService))
                .put(CollectionName.chanl_comn_cde, new SingleTableLoader(namedParameterJdbcTemplate, Table.CHANL_COMN_CDE, mongodb, CollectionName.chanl_comn_cde, threadPoolTaskExecutor, lockService))
                .put(CollectionName.prod_prc_hist, new ProdPrcHistLoader(namedParameterJdbcTemplate, Table.PROD_PRC_HIST, mongodb, CollectionName.prod_prc_hist, threadPoolTaskExecutor, lockService))
                .put(CollectionName.fin_doc, new SingleTableLoader(namedParameterJdbcTemplate, Table.FIN_DOC, mongodb, CollectionName.fin_doc, threadPoolTaskExecutor, lockService))
                .put(CollectionName.prod_type_chanl_attr, new SingleTableLoader(namedParameterJdbcTemplate, Table.TB_PROD_TYPE_CHANL_ATTR, mongodb, CollectionName.prod_type_chanl_attr, threadPoolTaskExecutor, lockService))
                .put(CollectionName.prod_type_fin_doc_type_rel, new SingleTableLoader(namedParameterJdbcTemplate, Table.PROD_TYPE_FIN_DOC_TYPE_REL, mongodb, CollectionName.prod_type_fin_doc_type_rel, threadPoolTaskExecutor, lockService))
                .put(CollectionName.fin_doc_hist, new SingleTableLoader(namedParameterJdbcTemplate, Table.FIN_DOC_HIST, mongodb, CollectionName.fin_doc_hist, threadPoolTaskExecutor, lockService))
                .put(CollectionName.fin_doc_upld, new SingleTableLoader(namedParameterJdbcTemplate, Table.FIN_DOC_UPLD, mongodb, CollectionName.fin_doc_upld, threadPoolTaskExecutor, lockService))
                .put(CollectionName.sys_parm, new SingleTableLoader(namedParameterJdbcTemplate, Table.SYS_PARM, mongodb, CollectionName.sys_parm, threadPoolTaskExecutor, lockService))
                .put(CollectionName.prod_name_seqs, new ProdNameSeqsLoader(namedParameterJdbcTemplate, mongodb))
                .build();
    }

    @Override
    public String getTaskName() {
        return "CollectionSyncTask";
    }

    @Override
    public void setLockToken(String lockToken) {
        this.lockToken = lockToken;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public void run() {
        log.info("collection [{}] is loading ", collectionName);

        long start = System.currentTimeMillis();
        DataLoader loader = dataLoaderMap.get(collectionName);

        if (Objects.isNull(loader)) {
            log.info("wrong collection name {} ", collectionName);
            return;
        }
        List<String> tableNames = getTableNames(loader);

        //remove events by table name
        syncService.removeEventsByTableNames(tableNames);
        //load data
        loader.load();

        //createIndex
        log.info("create indexes");
        createIndexesSyncTask.run();
        log.info("indexes created success");

        long cost = (System.currentTimeMillis() - start) / 60000;

        log.info("{} minutes consumed in total", cost);

    }

    private List<String> getTableNames(DataLoader loader) {
        List<String> tableNames = new ArrayList<>();

        if (loader instanceof ProductDataLoader) {
            List<ProductTableInfo> tableList = ProductMapping.getTableInfoList();
            List<String> tables = tableList.stream().map(ProductTableInfo::getTable).collect(Collectors.toList());
            tableNames.addAll(tables);
        } else if (loader instanceof SingleTableLoader) {
            tableNames.add(((SingleTableLoader) loader).getTableName());
        } else if (loader instanceof ProdTypeFinDocLoader) {
            tableNames.addAll(Arrays.asList(Table.PROD_TYPE_FIN_DOC, Table.PROD_SUBTP_FIN_DOC));
        } else if (loader instanceof StaffLicenseCheckLoader) {
            tableNames.addAll(Arrays.asList(Table.PROD_TYPE_STAF_LIC_CHECK, Table.PROD_SUBTP_STAF_LIC_CHECK));
        } else if (loader instanceof ReferenceDataLoader) {
            tableNames.addAll(Arrays.asList(Table.CDE_DESC_VALUE, Table.CDE_DESC_VALUE_CHANL_REL));
        } else if (loader instanceof AsetVoltlClassCharLoader) {
            tableNames.add(Table.ASET_VOLTL_CLASS_CHAR);
        } else if (loader instanceof AsetVoltlClassCorlLoader) {
            tableNames.add(Table.ASET_VOLTL_CLASS_CORL);
        } else if (loader instanceof PendAppoveTranLoader) {
            tableNames.add(Table.PEND_APROVE_TRAN);
        } else if (loader instanceof ProdPrcHistLoader) {
            tableNames.add(Table.PROD_PRC_HIST);
        } else if (loader instanceof ProdNameSeqsLoader) {
            tableNames.add(Table.PROD_NAME_SEQS);
        }  else {
            throw new IllegalArgumentException("Not support DataLoader " + loader);
        }
        return tableNames;
    }
}
