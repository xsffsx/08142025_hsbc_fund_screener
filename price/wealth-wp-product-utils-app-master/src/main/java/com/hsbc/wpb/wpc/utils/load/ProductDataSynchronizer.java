package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DataSynchronizer;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.ProdReservedAttrUtils;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

/**
 * Sync data from Oracle DB to MongoDB, which TB_PROD.REC_UPDT_DT_TM > a given timestamp
 */
@Slf4j
public class ProductDataSynchronizer extends ProductDataHandler implements DataSynchronizer {
    private static final ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);

    public ProductDataSynchronizer(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb, ThreadPoolTaskExecutor threadPoolTaskExecutor, boolean multipleThreadLoad, LockService lockService) {
        super(namedParameterJdbcTemplate, mongodb);
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.lockService = lockService;
        this.multipleThreadLoad = multipleThreadLoad;
    }

    protected boolean showTableLoading() {
        return false;
    }

    @Override
    protected void saveProducts(Map<Long, Map<String, Object>> prodMap) {
        Bson filter = in(Field._id, prodMap.keySet());

        Map<Long, Document> oldProdMap = new LinkedHashMap<>();
        colProduct.find(filter).forEach((Consumer<Document>) doc -> oldProdMap.put(doc.getLong(Field._id), doc));

        prodMap.forEach((prodId, prod) -> {
            Document oldProd = oldProdMap.get(prodId);
            saveProduct(oldProd, new Document(prod));
        });
    }

    private void saveProduct(Document oldProd, Document prod) {
        Long prodId = prod.getLong(Field._id);
        Long revision = MapUtils.getLong(oldProd, Field.revision, 0L);
        String createdBy = MapUtils.getString(oldProd, Field.createdBy, "sync");
        prod.put(Field.revision, ++revision);
        prod.put(Field.createdBy, createdBy);
        prod.put(Field.lastSyncTime, new Date());
        prod.put(Field.lastUpdatedBy, "sync");

        if (MapUtils.isNotEmpty(oldProd)) {
            ProdReservedAttrUtils.copyReservedAttrs(oldProd, prod);
        }

        colProduct.replaceOne(eq(Field._id, prodId), prod, replaceOptions);
    }

    @Override
    public void sync(Set<Map<String, Object>> keySet) {
        Set<Long> prodIdSet = new LinkedHashSet<>();
        keySet.forEach(item -> prodIdSet.add(Long.parseLong(item.get(Field.PROD_ID).toString())));
        log.info("Updating {} Product(s) ...", prodIdSet.size());
        syncProds(new ArrayList<>(prodIdSet));
    }

    public void syncProds(List<Long> prodIdList) {
        List<List<Long>> prodIdBatchList = breakdownByBatch(prodIdList);
        importTablesByBatch(prodIdBatchList);
    }

    @Override
    public void delete(Set<String> rowidSet) {
        DeleteResult result = colProduct.deleteMany(Filters.in("rowid", rowidSet));
        log.info("{} Product item has been deleted", result.getDeletedCount());
    }
}