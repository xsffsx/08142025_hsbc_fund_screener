package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.bulk.BulkWriteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.EsgConstants.ESG_NODE;
import static com.dummy.wpb.product.constant.EsgConstants.JOB_NAME;

@Component
@Slf4j
@StepScope
public class EsgWriter extends MongoItemWriter<List<Document>> {

    public EsgWriter(MongoOperations mongoTemplate) {
        this.setCollection(CollectionName.product.name());
        this.setTemplate(mongoTemplate);
    }

    @Override
    public void write(List<? extends List<Document>> items) {
        List<Document> list = items.stream().flatMap(List::stream).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<String> expectedUpdateCount = new ArrayList<>();
        List<Pair<Query, Update>> updateProducts = prepareProducts(list, expectedUpdateCount);

        BulkWriteResult bulkWriteResult = getTemplate().bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                CollectionName.product.name()
        ).updateOne(updateProducts).execute();

        logUpdateResult(bulkWriteResult, expectedUpdateCount);

    }

    private List<Pair<Query, Update>> prepareProducts(List<Document> list, List<String> expectedUpdateCount) {
        List<Pair<Query, Update>> updates = new ArrayList<>();
        for (Document doc : list) {
            Query query = new Query(Criteria.where(Field.prodId).is(doc.get(Field.prodId, 0L)));
            Map<String, Object> esgMap = (Map) doc.get(ESG_NODE);
            esgMap.put(Field.recUpdtDtTm, LocalDateTime.now());
            Update update = new Update();
            update.set(ESG_NODE, esgMap);
            update.set(Field.lastUpdatedBy, JOB_NAME);
            update.set(Field.recUpdtDtTm, LocalDateTime.now());
            update.inc(Field.revision, 1);
            updates.add(Pair.of(query, update));
            expectedUpdateCount.add(doc.getString(Field.prodAltPrimNum));
        }
        return updates;
    }

    private void logUpdateResult(BulkWriteResult result, List<String> expectedRecords) {
        int matchedCount = result.getMatchedCount();
        int modifiedCount = result.getModifiedCount();
        int expectedCount = expectedRecords.size();
        log.info("expected {} records {} to be updated, matched {} records, update {} records", expectedCount, expectedRecords, matchedCount, modifiedCount);
    }

}
