package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Component
@Slf4j
public class ProductTenorDayUpdateJobWriter implements ItemWriter<Document> {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String JOB_NAME = "Product Tenor Day Update Job";

    private static final UpdateOptions options = new UpdateOptions().upsert(false);

    @Override
    public void write(List<? extends Document> productList) {
        if (CollectionUtils.isEmpty(productList)) return;

        List<UpdateOneModel<Document>> modelList = new CopyOnWriteArrayList<>();

        productList.forEach(product -> {
            Bson filter = and(eq(Field.prodId, product.getLong(Field.prodId)));
            // GraphQL always returns termRemainDayCnt as integer type
            // need to convert as long
            Long termRemainDayCnt = product.getLong(Field.termRemainDayCnt);
            Bson updates = Updates.combine(
                    Updates.set(Field.termRemainDayCnt, termRemainDayCnt),
                    Updates.set(Field.recUpdtDtTm, new Date()),
                    Updates.set(Field.lastUpdatedBy, JOB_NAME)
            );
            modelList.add(new UpdateOneModel<>(filter, updates, options));
        });


        BulkWriteResult bulkWriteResult = mongoTemplate.getCollection(CollectionName.product.name()).bulkWrite(modelList);
        if (bulkWriteResult.getModifiedCount() == productList.size()) {
            productList.forEach(product -> log.info("Product has been updated (ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {}, termRemainDayCnt: {})",
                    product.getString(Field.ctryRecCde),
                    product.getString(Field.grpMembrRecCde),
                    product.getString(Field.prodTypeCde),
                    product.getString(Field.prodAltPrimNum),
                    product.getLong(Field.termRemainDayCnt)));
        } else {
            productList.forEach(product -> log.warn("Product may not have been successfully updated (ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {}, termRemainDayCnt: {})",
                    product.getString(Field.ctryRecCde),
                    product.getString(Field.grpMembrRecCde),
                    product.getString(Field.prodTypeCde),
                    product.getString(Field.prodAltPrimNum),
                    product.getLong(Field.termRemainDayCnt)));
        }
    }
}
