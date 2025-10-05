package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

/**
 * Migrate data from Oracle DB to MongoDB
 */
@Slf4j
public class ProductDataReLoader extends ProductDataHandler implements DataLoader {
    private List<Long> prodIdList;
    private SequenceService prodSequenceService;
    public ProductDataReLoader(List<Long> prodIdList, NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        super(namedParameterJdbcTemplate, mongodb);
        prodSequenceService = new SequenceService(mongodb);
        this.prodIdList = prodIdList;
    }

    protected boolean showTableLoading(){
        return false;
    }

    @Override
    protected void saveProducts(Map<Long, Map<String, Object>> prodMap) {
        prodMap.forEach((prodId, prod) -> {
            Document doc = new Document(prod);
            doc.put(Field.revision, 1L);   // init revision number is 1
            doc.put(Field.createdBy, "load");
            UpdateResult updateResult = colProduct.replaceOne(eq(Field.prodId, prodId), doc, new ReplaceOptions().upsert(true));
            log.info("Reload product {}, result {}", prodId, updateResult);
        });
    }

    @Override
    public void load() {
        log.info("Loading Product Data " + prodIdList);
        long start = System.currentTimeMillis();

        importTables(prodIdList);
        initSequenceCollection();

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

        prodSequenceService.initSequenceId(Sequence.prodId, maxProdId);
    }
}