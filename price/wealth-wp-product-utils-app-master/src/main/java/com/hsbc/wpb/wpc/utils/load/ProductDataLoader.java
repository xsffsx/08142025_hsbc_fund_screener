package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.service.LockService;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Migrate data from Oracle DB to MongoDB
 */
@Slf4j
public class ProductDataLoader extends ProductDataHandler implements DataLoader {
    public ProductDataLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb,
                             ThreadPoolTaskExecutor threadPoolTaskExecutor, boolean multipleThreadLoad, LockService lockService) {
        super(namedParameterJdbcTemplate, mongodb);
        this.lockService = lockService;
        sequenceService = new SequenceService(mongodb);
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.multipleThreadLoad = multipleThreadLoad;
    }

    protected boolean showTableLoading() {
        return false;
    }

    @Override
    protected void saveProducts(Map<Long, Map<String, Object>> prodMap) {
        List<Document> list = new LinkedList<>();
        prodMap.forEach((prodId, prod) -> {
            prod.put(Field.revision, 1L);   // init revision number is 1
            prod.put(Field.createdBy, "load");
            list.add(new Document(prod));
        });
        colProduct.insertMany(list);
    }

    @Override
    public void load() {
        if (colProduct.countDocuments() > 0) {
            colProduct.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading Product Data ...");
        long start = System.currentTimeMillis();

        List<List<Long>> prodIdBatchList = getProductIdBatchList();
        importTablesByBatch(prodIdBatchList);

        initSequenceCollection();
        initProdFinDocSequence();

        long cost = (System.currentTimeMillis() - start) / 60000;
        log.info("{} minutes consumed", cost);
    }

    /**
     * Put the max prodId to sequence
     */
    private void initSequenceCollection() {
        Document query = Document.parse("{$group : {_id : \"max\", prodId : {$max : \"$prodId\"}}}");
        Document result = colProduct.aggregate(Collections.singletonList(query)).first();
        long maxProdId = result.getLong(Field.prodId);
        log.info("max prodId to sequence: {}", maxProdId);
        sequenceService.initSequenceId(Sequence.prodId, maxProdId);
    }

    private void initProdFinDocSequence() {
        String sql = "select MAX(RSRC_ITEM_ID_FIN_DOC) from PROD_FIN_DOC ";
        Long maxRsrcItemId= namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.TYPE);
        log.info("max rsrcItemId to sequence: {}", maxRsrcItemId);
        if(maxRsrcItemId!=null) sequenceService.initSequenceId(Sequence.rsrcItemIdFinDoc, maxRsrcItemId);
    }

    private List<List<Long>> getProductIdBatchList() {
        String sql = "select PROD_ID from TB_PROD";
        log.info("Retrieve prodId list ...");
        List<Long> list = namedParameterJdbcTemplate.queryForList(sql, new MapSqlParameterSource(), Long.TYPE);
        return breakdownByBatch(list);
    }
}