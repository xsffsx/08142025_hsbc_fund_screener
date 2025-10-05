package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

@Slf4j
@Service
public class ProductService {
    protected static final String TARGET_COLLECTION_NAME = CollectionName.product;
    private final MongoCollection<Document> colProduct;

    public ProductService(MongoDatabase mongodb){
        this.colProduct = mongodb.getCollection(TARGET_COLLECTION_NAME);
    }

    /**
     * Auto created products are marked as createdBy=autoCreation, since the auto creation will happen in the legacy WPC side,
     * the product auto created in product should be removed once the WPC one has been synced, so that we can avoid duplicated
     * the same product documents with different product id.
     *
     * Given "prodStatCde" : "A", the combination below should be unique:
     *      ctryRecCde + grpMembrRecCde + prodTypeCde + prodAltPrimNum
     *
     * For each auto created product, if the corresponding one has been synced from WPC, remove the auto created one.
     */
    public void cleanAutoCreatedProducts() {
        log.debug("cleanAutoCreatedProducts ...");
        long start = System.currentTimeMillis();
        Bson filterCreated = eq(Field.createdBy, "autoCreation");
        List<Document> createdList = colProduct.find(filterCreated).into(new ArrayList<>());
        createdList.forEach(prod -> {
            Bson filter = and(
                    ne(Field.createdBy, "autoCreation"),
                    eq(Field.prodStatCde, "A"),
                    eq(Field.ctryRecCde, prod.getString(Field.ctryRecCde)),
                    eq(Field.grpMembrRecCde, prod.getString(Field.grpMembrRecCde)),
                    eq(Field.prodTypeCde, prod.getString(Field.prodTypeCde)),
                    eq(Field.prodAltPrimNum, prod.getString(Field.prodAltPrimNum))
            );
            long count = colProduct.countDocuments(filter);
            if(count > 0) {
                DeleteResult result = colProduct.deleteOne(eq(Field._id, prod.get(Field._id)));
                if(result.getDeletedCount() > 0) {
                    log.info("Clean auto created product {}", prod.get(Field._id));
                }
            }
        });
        log.debug("cleanAutoCreatedProducts cost {}", System.currentTimeMillis() - start);
    }
}
