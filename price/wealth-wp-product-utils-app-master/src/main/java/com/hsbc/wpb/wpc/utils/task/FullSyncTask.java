package com.dummy.wpb.wpc.utils.task;

import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.load.*;
import com.dummy.wpb.wpc.utils.model.productLock;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.SyncService;
import com.mongodb.client.MongoDatabase;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Full sync WPC data from OracleDB to MongoDB
 */
@Slf4j
@Component
public class FullSyncTask implements SyncTask {
    @Value("#{${product.sync.multiple-thread-load}}")
    private boolean multipleThreadLoad;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private MongoDatabase mongodb;
    @Setter
    @Value("")
    private String lockToken;
    @Autowired
    private SyncService syncService;
    @Autowired
    private LockService lockService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private CreateIndexesSyncTask createIndexesSyncTask;

    public String getTaskName() {
        return "FullSyncTask";
    }

    @Override
    public void run() {
        log.info("lockToken:", lockToken);
        List<DataLoader> loaderList = Arrays.asList(
                new ProdTypeFinDocLoader(namedParameterJdbcTemplate, mongodb),
                new StaffLicenseCheckLoader(namedParameterJdbcTemplate, mongodb),
                new ReferenceDataLoader(namedParameterJdbcTemplate, mongodb),
                new AsetVoltlClassCharLoader(namedParameterJdbcTemplate, mongodb),
                new AsetVoltlClassCorlLoader(namedParameterJdbcTemplate, mongodb),
                new SysParamLoader(namedParameterJdbcTemplate, mongodb),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.PROD_ATRIB_MAP, mongodb, CollectionName.prod_atrib_map, threadPoolTaskExecutor, lockService),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.CHANL_COMN_CDE, mongodb, CollectionName.chanl_comn_cde, threadPoolTaskExecutor, lockService),
                new ProductDataLoader(namedParameterJdbcTemplate, mongodb, threadPoolTaskExecutor, multipleThreadLoad, lockService),
                // PROD_PRC_HIST is too big, we are not going to reload it if not empty, drop the collection before data loading in case need to reload
                new ProdPrcHistLoader(namedParameterJdbcTemplate, Table.PROD_PRC_HIST, mongodb, CollectionName.prod_prc_hist, threadPoolTaskExecutor, lockService, false),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.FIN_DOC, mongodb, CollectionName.fin_doc, threadPoolTaskExecutor, lockService),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.TB_PROD_TYPE_CHANL_ATTR, mongodb, CollectionName.prod_type_chanl_attr, threadPoolTaskExecutor, lockService),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.PROD_TYPE_FIN_DOC_TYPE_REL, mongodb, CollectionName.prod_type_fin_doc_type_rel, threadPoolTaskExecutor, lockService),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.FIN_DOC_HIST, mongodb, CollectionName.fin_doc_hist, threadPoolTaskExecutor, lockService),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.FIN_DOC_UPLD, mongodb, CollectionName.fin_doc_upld, threadPoolTaskExecutor, lockService),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.SYS_ACTV_LOG, mongodb, CollectionName.sys_actv_log, threadPoolTaskExecutor, lockService),
                new SingleTableLoader(namedParameterJdbcTemplate, Table.LOG_EQTY_LINK_INVST, mongodb, CollectionName.log_eqty_link_invst, threadPoolTaskExecutor, lockService),
                new ProdNameSeqsLoader(namedParameterJdbcTemplate, mongodb),
                new PendAppoveTranLoader(namedParameterJdbcTemplate, mongodb)
        );
        long start = System.currentTimeMillis();
        // to ignore all the changes before data loading
        int affected = namedParameterJdbcTemplate.update("delete from DATA_CHANGE_EVENT", Collections.emptyMap());

        log.info("{} records is deleted from DATA_CHANGE_EVENT", affected);

        loaderList.forEach(loader -> {
            lockService.heartbeat(productLock.DATA_UTILS_TASK_LOCK);
            loader.load();
        });
        log.info("create indexes");
        createIndexesSyncTask.run();
        log.info("indexes created success");

        long cost = (System.currentTimeMillis() - start) / 60000;
        log.info("{} minutes consumed in total", cost);
    }
}
