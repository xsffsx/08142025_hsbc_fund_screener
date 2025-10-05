package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Slf4j
public class ProductPeriodUpdateItemWriter implements ItemWriter<Document> {

    private static final String JOB_NAME = "Calculate tenor period Job";

    private final MongoCollection<Document> productCollection;
    private static final UpdateOptions options = new UpdateOptions().upsert(false);

    public ProductPeriodUpdateItemWriter(MongoDatabase mongodb) {
        this.productCollection = mongodb.getCollection(CollectionName.product.toString());
    }

    @Override
    public void write(List<? extends Document> productList) {
        if (CollectionUtils.isEmpty(productList)){
            return;
        }

        List<UpdateOneModel<Document>> modelList = new ArrayList<>();

        productList.forEach(product -> {
            Bson filter = and(eq(Field.prodId, product.getLong(Field.prodId)));
            Long prdInvstTnorNum = product.getLong(Field.prdInvstTnorNum);
            Bson updates = Updates.combine(
                    Updates.set(Field.prdInvstTnorNum, prdInvstTnorNum),
                    Updates.set(Field.recUpdtDtTm, new Date()),
                    Updates.set(Field.lastUpdatedBy, JOB_NAME)
            );
            modelList.add(new UpdateOneModel<>(filter, updates, options));
        });

        if (CollectionUtils.isEmpty(modelList)) return;

        BulkWriteResult bulkWriteResult = productCollection.bulkWrite(modelList);
        if (bulkWriteResult.getModifiedCount() == productList.size()) {
            productList.forEach(product -> log.info("Product has been updated " +
                            "(ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodStatCde: {}, prodAltPrimNum: {}, prdInvstTnorNum: {})",
                    product.getString(Field.ctryRecCde),
                    product.getString(Field.grpMembrRecCde),
                    product.getString(Field.prodTypeCde),
                    product.getString(Field.prodStatCde),
                    product.getString(Field.prodAltPrimNum),
                    product.getLong(Field.prdInvstTnorNum)));
        } else {
            productList.forEach(product -> log.warn("Product may not have been successfully updated " +
                            "(ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodStatCde: {}, prodAltPrimNum: {}, prdInvstTnorNum: {})",
                    product.getString(Field.ctryRecCde),
                    product.getString(Field.grpMembrRecCde),
                    product.getString(Field.prodTypeCde),
                    product.getString(Field.prodStatCde),
                    product.getString(Field.prodAltPrimNum),
                    product.getLong(Field.prdInvstTnorNum)));
        }
    }
}
