package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.listener.NotificationManager;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.utils.DocumentPatch;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ProductValidator;
import com.mongodb.client.model.Filters;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductChangeService implements ChangeService {
    private ProductService productService;
    private ProductValidator productValidator;
    private NotificationManager notificationManager;

    @Autowired
    private PriceCompareService priceCompareService;

    public ProductChangeService(ProductService productService, ProductValidator productValidator, NotificationManager notificationManager) {
        this.productService = productService;
        this.productValidator = productValidator;
        this.notificationManager = notificationManager;
    }

    @Override
    public List<Error> validate(Document amendment) {
        ActionCde actionCde = ActionCde.valueOf(amendment.getString(Field.actionCde));
        if (ActionCde.delete == actionCde) {
            return Collections.emptyList();
        }

        Document newProd = (Document) amendment.get(Field.doc);
        // Validate input data and update price history
        Map<String, Object> oldProd = findFirst(Filters.eq(Field.prodId, newProd.get(Field.prodId)));
        notificationManager.beforeValidation(oldProd, newProd, Collections.emptyList());
        return productValidator.validate(newProd);
    }

    @Override
    public void add(Document doc) {
        //set recOnlnUpdtDtTm when create prod through ui
        doc.put(Field.recOnlnUpdtDtTm, new Date());
        productService.createProduct(doc);
    }

    @Override
    public void update(Document newProd) {
        Document oldProd = findFirst(Filters.eq(Field.prodId, newProd.get(Field.prodId)));
        // get graphql operations for cutoff time update
        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> operations = documentPatch.patch(oldProd, newProd);
        // update price compare results
        priceCompareService.updateStlPrcCmparFromWps(newProd);
        Date now = new Date();
        // the prod changed through ui should update recOnlnUpdtDtTm
        newProd.put(Field.recOnlnUpdtDtTm, now);
        // refresh recCreatDtTm and recUpdtDtTm again to ensure they are equal to now
        refreshRecDtTm(oldProd, newProd, now);
        productService.updateProduct(newProd, operations);
    }

    @Override
    public void delete(Document doc) {
        // it's an update with prodStatCde=D only
        Date now = new Date();
        doc.put(Field.prodStatCde, "D");
        doc.put(Field.recUpdtDtTm, now);
        doc.put(Field.prodStatUpdtDtTm, now);
        // set graphql operations for cutoff time update
        List<OperationInput> operations = new ArrayList<>();
        OperationInput op1 = new OperationInput(Operation.put, Field.prodStatCde, doc.get(Field.prodStatCde));
        OperationInput op2 = new OperationInput(Operation.put, Field.recUpdtDtTm, now);
        OperationInput op3 = new OperationInput(Operation.put, Field.prodStatUpdtDtTm, now);
        operations.add(op1);
        operations.add(op2);
        operations.add(op3);

        productService.updateProduct(doc, operations);
    }

    @Override
    public Document findFirst(Bson filter) {
        return productService.findFirst(filter);
    }

    private void refreshRecDtTm(Document oldDoc, Document newDoc, Date now) {
        AtomicBoolean areEqual = new AtomicBoolean(true);
        newDoc.forEach((key, newValue) -> {
            if (StringUtils.equalsAny(key, Field.rowid, Field.recCreatDtTm, Field.recUpdtDtTm)) {
                return;
            }
            if (newValue instanceof Document) {
                Document newEmbeddedDoc = (Document) newValue;
                Document oldEmbeddedDoc = oldDoc.get(key, new Document());
                refreshRecDtTm(oldEmbeddedDoc, newEmbeddedDoc, now);
            } else if (newValue instanceof List) {
                if (((List<?>) newValue).isEmpty() || !(((List<?>) newValue).get(0) instanceof Document)) {
                    return;
                }
                Map<String, Document> newDocMap = ((List<Document>) newValue).stream().collect(Collectors.toMap(doc -> doc.getString(Field.rowid), Function.identity()));
                Map<String, Document> oldDocMap = oldDoc.getList(key, Document.class, Collections.emptyList()).stream().collect(Collectors.toMap(doc -> doc.getString(Field.rowid), Function.identity()));
                newDocMap.forEach((rowid, newEmbeddedDoc) -> {
                    Document oldEmbeddedDoc = oldDocMap.getOrDefault(rowid, new Document());
                    refreshRecDtTm(oldEmbeddedDoc, newEmbeddedDoc, now);
                });
            }

            areEqual.set(areEqual.get() && Objects.equals(newValue, oldDoc.get(key)));
        });

        if (MapUtils.isEmpty(oldDoc)){
            newDoc.computeIfPresent(Field.recCreatDtTm, (k, v) -> now);
            newDoc.computeIfPresent(Field.recUpdtDtTm, (k, v) -> now);
        }else if (!areEqual.get()) {
            newDoc.computeIfPresent(Field.recUpdtDtTm, (k, v) -> now);
        }
    }
}
