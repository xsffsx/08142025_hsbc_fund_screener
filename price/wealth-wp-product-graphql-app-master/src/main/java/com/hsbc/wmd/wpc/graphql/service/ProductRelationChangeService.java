package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

@SuppressWarnings("java:S135")
@Service
public class ProductRelationChangeService implements ChangeService {

    private MongoCollection<Document> colProductData;

    private static final String PROD_PROD_RELN_CDE = "prodProdRelnCde";
    private static final String PROD_RELN = "prodReln";
    private static final String PROD_ID_REL = "prodIdRel";
    public ProductRelationChangeService(MongoDatabase mongoDatabase) {
        this.colProductData = mongoDatabase.getCollection(CollectionName.product.toString());
    }

    private static final List<String> fieldList = Arrays.asList("ctryRecCde", "grpMembrRecCde", "prodName", "prodSubtpCde", "prodTypeCde", "prodAltPrimNum", "revision",
            PROD_RELN, "recCreatDtTm", "recUpdtDtTm");

    @Override
    public List<Error> validate(Document amendment) {
        List<Error> errors = new ArrayList<>();

        Document doc = amendment.get(Field.doc, Document.class);

        List<Map<String, Object>> prodRelnList = doc.get(PROD_RELN, new ArrayList<>());
        Set<Long> prodRelnKeySet = new HashSet<>();

        for (int i = 0; i < prodRelnList.size(); i++) {
            Map<String, Object> prodReln = prodRelnList.get(i);
            Long prodIdRel = (Long) prodReln.get(PROD_ID_REL);


            if (prodRelnKeySet.contains(prodIdRel)) {
                errors.add(new Error("$", "@duplicate", String.format("ProdReln with prodIdRel = %s is repeated", prodIdRel)));
                continue;
            }
            prodRelnKeySet.add(prodIdRel);

            Bson bson = and(eq(Field._id, prodIdRel));
            Document product = colProductData.find(bson).projection(include(Field.prodId)).first();
            if (null == product) {
                errors.add(new Error("$", i + "@notFound", String.format("Con not found product with prodId = %s", prodIdRel)));
                continue;
            }

            prodReln.putIfAbsent(Field.rowid,UUID.randomUUID().toString());
        }
        return errors;
    }

    @Override
    public void add(Document doc) {
        update(doc);
    }

    @Override
    public void update(Document doc) {
        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document product = colProductData.find(filter).first();

        List<Document> prodRelnListUpdate = doc.get(PROD_RELN, List.class);

        List<Document> prodRelnListDb = product.get(PROD_RELN, new ArrayList<>());

        List<Document> prodRelnListSave = new ArrayList<>();
        for (Document prodRelnUpdate : prodRelnListUpdate) {
            String prodProdRelnCde = prodRelnUpdate.getString(PROD_PROD_RELN_CDE);
            Long prodIdRel = prodRelnUpdate.get(PROD_ID_REL, Long.class);

            Document prodRelSave = prodRelnListDb.stream().filter(item ->
                            prodProdRelnCde.equals(item.getString(PROD_PROD_RELN_CDE)) && prodIdRel.equals(item.get(PROD_ID_REL)))
                    .findFirst().orElse(new Document());

            prodRelSave.putIfAbsent(Field.rowid,UUID.randomUUID().toString());
            prodRelSave.put(PROD_ID_REL, prodIdRel);
            prodRelSave.put(PROD_PROD_RELN_CDE, prodProdRelnCde);
            prodRelSave.putIfAbsent(Field.recCreatDtTm, new Date());
            prodRelSave.put(Field.recUpdtDtTm, new Date());

            prodRelnListSave.add(prodRelSave);
        }

        product.put(PROD_RELN, prodRelnListSave);
        RevisionUtils.increaseRevisionNumber(product);
        colProductData.updateOne(filter, new Document("$set", product));
    }

    @Override
    public void delete(Document doc) {
        throw new productErrorException(productErrors.NotSupportDocType, "The docType: " + DocType.product_prod_relation + " can not be deleted");
    }

    @Override
    public Document findFirst(Bson filter) {
        return colProductData.find(filter).projection(include(fieldList)).first();
    }

}
