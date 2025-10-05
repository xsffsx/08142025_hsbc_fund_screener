package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.annotation.RetryableTransactional;
import com.dummy.wmd.wpc.graphql.calc.CalculationManager;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.Sequence;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.fetcher.amendment.AmendmentUtils;
import com.dummy.wmd.wpc.graphql.listener.NotificationManager;
import com.dummy.wmd.wpc.graphql.model.*;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.utils.JsonPathUtils;
import com.dummy.wmd.wpc.graphql.utils.ProductInputUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ProductValidator;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.Updates;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

@Service
public class ProductService {
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private DocumentRevisionService documentRevisionService;
    @Autowired
    private CalculationManager calculationManager;
    @Autowired
    private NotificationManager notificationManager;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String CONST_COUNT = "count";

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Document> getProductsByFilter(Bson filterBson) {
        return getProductCollection()
                .find(filterBson)
                .into(new LinkedList<>());
    }

    public Long countProductByFilter(Bson filterBson) {
        return getProductCollection()
                .countDocuments(filterBson);
    }

    public Document createProduct(Map<String, Object> prod){
        long prodId = sequenceService.nextId(Sequence.prodId);
        prod.put(Field._id, prodId);
        prod.put(Field.prodId, prodId);
        prod.put(Field.prodCde, Long.toString(prodId));
        prod.put(Field.revision, 1L);

        // set recCreatDtTm, recUpdtDtTm, rowId in each level, according to the ProductInput schema
        Map<String, Object> result = ProductInputUtils.supplementMissingFields(prod);

        Date now = new Date();
        result.put(Field.prodStatUpdtDtTm, now);
        if (null != DocumentUtils.getBigDecimal(result, Field.prodBidPrcAmt)
                || null != DocumentUtils.getBigDecimal(result, Field.prodOffrPrcAmt)
                || null != DocumentUtils.getBigDecimal(result, Field.prodMktPrcAmt)
                || null != DocumentUtils.getBigDecimal(result, Field.prodNavPrcAmt)) {
            result.put(Field.prodPrcUpdtDtTm, now);
        }

        Document doc = new Document(result);
        // handle calculated fields
        calculationManager.updateCalculatedFields(doc);

        notificationManager.beforeInsert(doc);  // notify listeners
        getProductCollection().insertOne(doc);
        notificationManager.afterInsert(doc);   // notify listeners
        return doc;
    }

    /**
     * Update a product document, the old product document will be copy to document_revision collection.
     *
     * @param doc
     */
    public Document updateProduct(Document doc, List<OperationInput> operations) {
        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document oldDoc = getProductCollection().find(filter).first();
        if(null == oldDoc) {
            throw new productErrorException(productErrors.RuntimeException, "Product document not found, id=" + id);
        }

        documentRevisionService.saveDocumentRevision(DocType.product, oldDoc);
        // recUpdtDtTm will be take cared by the caller, but missing rowId in each level can be automatically set, according to the ProductInput schema
        doc = new Document(ProductInputUtils.supplementMissingFields(doc));
        // update the recUpdtDtTm field anyway
        doc.put(Field.recUpdtDtTm, new Date());
        RevisionUtils.setRevisionNumber(oldDoc, doc);

        // handle calculated fields
        calculationManager.updateCalculatedFields(oldDoc, doc, operations);

        notificationManager.beforeUpdate(doc, operations);
        getProductCollection().replaceOne(filter, doc);
        notificationManager.afterAllUpdate(Collections.singletonMap(doc, operations));   // notify listeners
        return doc;
    }

    @RetryableTransactional
    public ProductBatchUpdateResult batchUpdate(BsonDocument filter, List<OperationInput> operations, boolean allowPartial) {
        // limit 100 records at a time to avoid OOM error
        if (countProductByFilter(filter) > 100) {
            throw new productErrorException(productErrors.TooManyDocuments, "Not support to update more than 100 records");
        }

        // Necessary for transaction!!!
        notifyStartTransaction(filter);

        // Step 1: Retrieve the latest document A
        List<Document> prodList = ((ProductService) AopContext.currentProxy()).getProductsByFilter(filter);
        // deep copy the original product list
        List<Document> matchProducts = DocumentUtils.clone(prodList);
        // Step 3: Apply update to document A with Json Patch, the result would be document C.
        List<Document> mergedProdList = applyUpdateOperations(prodList, operations);

        List<Document> validProdList = new ArrayList<>();
        List<ProductValidationResult> invalidProdList = new ArrayList<>();

        mergedProdList.forEach(prod -> {
            Document oldProd = matchProducts.stream().filter(p -> prod.get(Field.prodId).equals(p.get(Field.prodId))).findFirst().orElse(null);
            notificationManager.beforeValidation(oldProd, prod, operations);
            List<Error> errors = productValidator.validate(oldProd, prod, operations);
            if(!errors.isEmpty()) {
                invalidProdList.add(new ProductValidationResult(prod, errors));
            } else {
                validProdList.add(prod);
            }
        });

        // do save products
        List<Document> updatedProducts = new ArrayList<>();
        if(!validProdList.isEmpty() && (mergedProdList.size() == validProdList.size() || allowPartial)) {
            validProdList.forEach(prod -> updatedProducts.add(updateProduct(new Document(prod), operations)));
        }

        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        result.setMatchCount(matchProducts.size());
        result.setMatchProducts(matchProducts);
        result.setUpdatedProducts(updatedProducts);
        result.setInvalidProducts(invalidProdList);
        return result;
    }

    @RetryableTransactional
    public ProductBatchUpdateResult batchUpdateById(Map<Long, List<OperationInput>> prodIdOperationMap, boolean allowPartial) {
        // limit 100 records at a time to avoid OOM error
        if (prodIdOperationMap.size() > 100) {
            throw new productErrorException(productErrors.TooManyDocuments, "Not support to update more than 100 records");
        }

        Bson filter = Filters.in(Field.prodId, prodIdOperationMap.keySet().toArray(new Long[]{}));

        // Necessary for transaction!!!
        notifyStartTransaction(filter);

        Map<Long, Document> productMap = ((ProductService) AopContext.currentProxy())
                .getProductsByFilter(filter)
                .stream()
                .collect(Collectors.toMap(
                        prod -> (Long) prod.get(Field.prodId),
                        prod -> prod));
        Map<Document, List<OperationInput>> productOperationMap = new LinkedHashMap<>();

        Map<Long, Document> oldProductMap = DocumentUtils.clone(productMap);
        List<ProductValidationResult> invalidProdList = new ArrayList<>();

        prodIdOperationMap.forEach((prodId, operations) -> {

            Document product = productMap.get(prodId);
            if (null == product) {
                throw new productErrorException(productErrors.RuntimeException, "Product document not found, id=" + prodId);
            }

            // Step 3: Apply update to document A with Json Patch, the result would be document C.
            Map<String, Object> mergedProd = applyUpdateOperations(Collections.singletonList(product), operations).get(0);

            Document oldProd = oldProductMap.get(prodId);
            notificationManager.beforeValidation(oldProd, mergedProd, operations);
            List<Error> errors = productValidator.validate(oldProd, mergedProd, operations);
            if (!errors.isEmpty()) {
                invalidProdList.add(new ProductValidationResult(mergedProd, errors));
            } else if (allowPartial) {
                productOperationMap.put(new Document(mergedProd), operations);
            }
        });

        List<Document> oldProducts = new ArrayList<>(oldProductMap.values());
        // do save products
        List<Document> updateResult = updateProduct(productOperationMap, oldProducts);
        ProductBatchUpdateResult result = new ProductBatchUpdateResult();
        result.setMatchCount(oldProducts.size());
        result.setMatchProducts(oldProducts);
        result.setUpdatedProducts(updateResult);
        result.setInvalidProducts(invalidProdList);
        return result;
    }

    public List<Document> updateProduct(Map<Document, List<OperationInput>> productOperationMap, List<Document> oldProducts) {
        List<ReplaceOneModel<Document>> modelList = new ArrayList<>();
        List<Map<String, Object>> oldDocList = new ArrayList<>();

        productOperationMap.forEach((prod, operations) -> {
            Object id = prod.get(Field._id);
            Document oldProd = oldProducts.stream()
                    .filter(p -> id.equals(p.get(Field.prodId)))
                    .findFirst()
                    .map(Document::new)
                    .orElse(null);

            oldDocList.add(oldProd);
            prod = new Document(ProductInputUtils.supplementMissingFields(prod));
            // update the recUpdtDtTm field anyway
            prod.put(Field.recUpdtDtTm, new Date());
            RevisionUtils.setRevisionNumber(oldProd, prod);

            // handle calculated fields
            calculationManager.updateCalculatedFields(oldProd, prod, operations);
            notificationManager.beforeUpdate(prod, operations);
            modelList.add(new ReplaceOneModel<>(eq(Field._id, id), prod));
        });

        if (!modelList.isEmpty()) {
            getProductCollection().bulkWrite(modelList);
        }

        notificationManager.afterAllUpdate(productOperationMap);
        return new ArrayList<>(productOperationMap.keySet());
    }

    /**
     * apply update operations to the list of products, but not update DB yet
     *
     * @param prodList
     * @param operations
     */
    private List<Document> applyUpdateOperations(List<Document> prodList, List<OperationInput> operations) {
        GraphQLSchema schema = GraphQLProvider.getGraphQLSchema();
        List<Document> resultList = new ArrayList<>();
        prodList.forEach(prod -> {
            JsonPathUtils.applyChanges(prod, operations);

            // Coerce the product document against the graphql schema
            Document amdm = new Document(Field.docType, "product");
            amdm.put(Field.doc, prod);
            AmendmentUtils.coerceChangeDocument(amdm, schema);
            resultList.add(amdm.get(Field.doc, Document.class));
        });
        return resultList;
    }

    @Autowired
    private LockService lockService;

    @RetryableTransactional
    public ProductBatchCreateResult batchCreate(List<Map<String, Object>> prodList, boolean allowPartial) {
        List<ProductValidationResult> invalidProdList = new ArrayList<>();
        List<Map<String, Object>> createdProdList = new ArrayList<>();

        Set<String> locks = new HashSet<>();
        try {
            for (Map<String, Object> prod : prodList) {
                // if prod has _id field, some verification will fail
                prod.remove(Field._id);
                prod.remove(Field.prodId);
                prod.remove(Field.prodCde);

                String key = "PRODUCT_CREATE_" +
                        prod.get(Field.ctryRecCde) +
                        prod.get(Field.grpMembrRecCde) +
                        prod.get(Field.prodTypeCde) +
                        prod.get(Field.prodAltPrimNum);
                lockService.lock(key);
                locks.add(key);

                notificationManager.beforeValidation(null, prod, Collections.emptyList());
                List<Error> errors = productValidator.validate(prod);
                if (!errors.isEmpty()) {
                    invalidProdList.add(new ProductValidationResult(prod, errors));
                } else {
                    createdProdList.add(createProduct(prod));
                }
            }
        } finally {
            locks.forEach(lockService::unLock);
        }

        if (!invalidProdList.isEmpty() && !allowPartial) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        ProductBatchCreateResult result = new ProductBatchCreateResult();
        result.setCreatedProducts(createdProdList);
        result.setInvalidProducts(invalidProdList);
        return result;
    }

    /**
     *  Have to update recUpdtDtTm to notify mongodb to lock the product
     * */
    private void notifyStartTransaction(Bson filter) {
        getProductCollection().updateMany(filter, Updates.set(Field.recUpdtDtTm, new Date()));
    }

    /**
     * Group products by product type code, Delisted product are excluded
     *
     * @return
     */
    public List<GroupItem> groupByProductType(Map<String, Object> filterMap){
        // db.getCollection('product').aggregate([{$group : {_id : "$prodTypeCde", count : {$sum : 1}}}])
        List<Bson> pipeline = new ArrayList<>();
        pipeline.add(match(ne(Field.prodStatCde, "D")));
        filterMap.entrySet().forEach(filter -> pipeline.add(match(eq(filter.getKey(),filter.getValue()))));
        pipeline.add(group("$prodTypeCde", Accumulators.sum(CONST_COUNT, 1)));
        return getProductCollection().aggregate(pipeline)
                .into(new ArrayList<>()).stream()
                .map(doc -> new GroupItem(doc.getString(Field._id), doc.getInteger(CONST_COUNT)))
                .collect(Collectors.toList());
    }

    /**
     * Count status, optionally filter by product type
     *
     * @param prodTypeCode
     * @return
     */
    public List<GroupItem> groupByProductStatus(String prodTypeCode,Map<String, Object> filterMap){
        // db.getCollection('product').aggregate([{$group : {_id : "$prodStatCde", count : {$sum : 1}}}])
        List<Bson> pipeline = new ArrayList<>();
        // in case prodTypeCode is specified, filter by product type first
        if(StringUtils.isNotBlank(prodTypeCode)) {
            pipeline.add(match(eq(Field.prodTypeCde, prodTypeCode)));
        }
        filterMap.forEach((key, value) -> pipeline.add(match(eq(key, value))));
        pipeline.add(group("$prodStatCde", Accumulators.sum(CONST_COUNT, 1)));

        return getProductCollection().aggregate(pipeline)
                .into(new ArrayList<>()).stream()
                .map(doc -> new GroupItem(doc.getString(Field._id), doc.getInteger(CONST_COUNT)))
                .collect(Collectors.toList());
    }

    public Document findFirst(Bson filter) {
        return getProductCollection().find(filter).first();
    }
    
    private MongoCollection<Document> getProductCollection() {
        return mongoTemplate.getCollection(CollectionName.product.name());
    }
}
