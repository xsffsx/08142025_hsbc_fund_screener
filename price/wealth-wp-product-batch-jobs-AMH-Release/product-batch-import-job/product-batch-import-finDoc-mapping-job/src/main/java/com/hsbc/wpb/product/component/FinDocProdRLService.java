/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		FinDocProdRL
 *
 * Creation Date	Mar 8, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 8, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.component;


import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.model.FinDocMapInput;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateByIdInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.FinDocCollectionsService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.BsonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.validation.MappingValidation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.ImportFinDocMappingJobApplication.JOB_NAME;
import static com.dummy.wpb.product.constant.FinDocConstants.*;


@Service
@Slf4j
public class FinDocProdRLService {

    @Autowired
    public MongoDatabase mongoDatabase;

    private MongoCollection<Document> colProdTypeFinDoc;

    public FinDocProdRLService(MongoDatabase mongoDatabase){
        this.colProdTypeFinDoc = mongoDatabase.getCollection(CollectionName.prod_type_fin_doc.toString());
    }

    @Autowired
    public ProductService productService;

    @Autowired
    public FinDocCollectionsService finDocCollectionsService;

    public void processProdRLBatch(List<FinDocMapInput> items, MappingValidation fdv, String action) throws Exception {
        Map<String, List<FinDocMapInput>> categorizedItems = categorizeItems(items);
        List<FinDocMapInput> prodItems = categorizedItems.get("prodItems");
        List<FinDocMapInput> subtpItems = categorizedItems.get("subtpItems");
        List<FinDocMapInput> typeItems = categorizedItems.get("typeItems");

        if (!prodItems.isEmpty()) {
            processProdItems(prodItems, fdv, action);
        }

        if (!subtpItems.isEmpty()) {
            processItems(subtpItems, fdv, action, true);
        }

        if (!typeItems.isEmpty()) {
            processItems(typeItems, fdv, action, false);
        }
    }

    private Map<String, List<FinDocMapInput>> categorizeItems(List<FinDocMapInput> items) {
        Map<String, List<FinDocMapInput>> categorizedItems = new HashMap<>();
        List<FinDocMapInput> prodItems = new ArrayList<>();
        List<FinDocMapInput> subtpItems = new ArrayList<>();
        List<FinDocMapInput> typeItems = new ArrayList<>();

        for (FinDocMapInput fdi : items) {
            if (!(fdi.getProdId().equals("*")) && fdi.getProdRealID() != 0) {
                prodItems.add(fdi);
            } else if (fdi.getProdSubtpCde() != null && !(fdi.getProdSubtpCde().equals("*"))) {
                subtpItems.add(fdi);
            } else {
                typeItems.add(fdi);
            }
        }

        categorizedItems.put("prodItems", prodItems);
        categorizedItems.put("subtpItems", subtpItems);
        categorizedItems.put("typeItems", typeItems);

        return categorizedItems;
    }

    private void processProdItems(List<FinDocMapInput> prodItems, MappingValidation fdv, String action) throws Exception {
        Set<Long> prodRealIDs = prodItems.stream().map(FinDocMapInput::getProdRealID).collect(Collectors.toSet());
        List<Document> products = productService.productByFilters(BsonUtils.toMap(Filters.in(Field.prodId, prodRealIDs)));

        Map<Long, List<Map<String, Object>>> productFinDocsMap = new HashMap<>();
        Map<Long, List<Map<String, Object>>> productFieldGroupCtoffsMap = new HashMap<>();
        for (Document prod : products) {
            Long prodId = prod.getInteger(Field.prodId).longValue();
            List<Map<String, Object>> prodFinDocs = JsonPathUtils.readValue(prod, Field.finDoc);
            prodFinDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
            productFinDocsMap.put(prodId, prodFinDocs);
            productFieldGroupCtoffsMap.put(prodId, updateFieldGroupCtoffs(prod));
        }

        Map<Long, List<Map<String, Object>>> changeProductFinDocsMap = new HashMap<>();
        for (FinDocMapInput fdi : prodItems) {
            List<Map<String, Object>> finDocs = productFinDocsMap.get(fdi.getProdRealID());
            if (finDocs == null) {
                continue;
            }
            Optional<Map<String, Object>> finDoc = findFinDoc(finDocs, fdi);
            handleAction(finDocs, finDoc, fdi, fdv, action, changeProductFinDocsMap);
        }

        updateProdByFiltersBatch(changeProductFinDocsMap, productFieldGroupCtoffsMap);
    }

    private Optional<Map<String, Object>> findFinDoc(List<Map<String, Object>> finDocs, FinDocMapInput fdi) {
        return finDocs.stream()
                .filter(fd -> StringUtils.equals((String) fd.get(Field.docFinTypeCde), fdi.getDocTypeCdeP()) &&
                        StringUtils.equals((String) fd.get(Field.langFinDocCde), fdi.getLangCatCdeP()) &&
                        StringUtils.equals((String) fd.get(Field.finDocCustClassCde), fdi.getCustClassCde()))
                .findFirst();
    }

    private void handleAction(List<Map<String, Object>> finDocs, Optional<Map<String, Object>> finDoc, FinDocMapInput fdi, MappingValidation fdv, String action, Map<Long, List<Map<String, Object>>> changeProductFinDocsMap) {
        switch (action) {
            case ADD_ACTION:
                handleInsertAction(finDocs, finDoc, fdi, fdv, changeProductFinDocsMap);
                break;
            case DELETE_ACTION:
                handleDeleteAction(finDocs, finDoc, fdi, fdv, changeProductFinDocsMap);
                break;
            case CHANGE_ACTION:
                handleUpdateAction(finDocs, finDoc, fdi, fdv, changeProductFinDocsMap);
                break;
            default:
                throw new IllegalArgumentException("Invalid handle action: " + action);
        }
    }

    private boolean isFinDocPresent(List<Map<String, Object>> finDocs, Optional<Map<String, Object>> finDoc) {
        return !CollectionUtils.isEmpty(finDocs) && finDoc.isPresent();
    }

    private void handleInsertAction(List<Map<String, Object>> finDocs, Optional<Map<String, Object>> finDoc, FinDocMapInput fdi, MappingValidation fdv, Map<Long, List<Map<String, Object>>> changeProductFinDocsMap) {
        if (!isFinDocPresent(finDocs, finDoc)) {
            finDocs.add(newFinDoc(fdi));
            changeProductFinDocsMap.put(fdi.getProdRealID(), finDocs);
        } else {
            fdv.getRejectRec().add(fdv.rejectMail(fdi, FinDocConstants.DUPLICATE_REC_INSERT, log));
        }
    }

    @SuppressWarnings("java:S3655")
    private void handleDeleteAction(List<Map<String, Object>> finDocs, Optional<Map<String, Object>> finDoc, FinDocMapInput fdi, MappingValidation fdv, Map<Long, List<Map<String, Object>>> changeProductFinDocsMap) {
        if (isFinDocPresent(finDocs, finDoc)) {
            finDocs.remove(finDoc.get());
            if (CollectionUtils.isEmpty(finDocs)) {
                finDocs = null;
            }
            changeProductFinDocsMap.put(fdi.getProdRealID(), finDocs);
        } else {
            fdv.getRejectRec().add(fdv.rejectMail(fdi, FinDocConstants.NO_REC_DELETE, log));
        }
    }

    @SuppressWarnings("java:S3655")
    private void handleUpdateAction(List<Map<String, Object>> finDocs, Optional<Map<String, Object>> finDoc, FinDocMapInput fdi, MappingValidation fdv, Map<Long, List<Map<String, Object>>> changeProductFinDocsMap) {
        if (isFinDocPresent(finDocs, finDoc)) {
            Map<String, Object> finDocMap = finDoc.get();
            finDocMap.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
            finDocMap.put(Field.urlDocText, fdi.getUrl());
            finDocMap.put(Field.recUpdtDtTm, new DateTime(DateTimeZone.UTC).toString());
            changeProductFinDocsMap.put(fdi.getProdRealID(), finDocs);
        } else {
            fdv.getRejectRec().add(fdv.rejectMail(fdi, FinDocConstants.NO_REC_CHANGE, log));
        }
    }

    private void processItems(List<FinDocMapInput> items, MappingValidation fdv, String action, boolean isProdSubtypeCde) throws Exception {
        List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildFilter(items, isProdSubtypeCde));
        List<FinDocMapInput> changeItems = filterChangeItems(items, prodTypeFinDocs, isProdSubtypeCde, fdv, action);
        processProdTypeFinDocBatch(changeItems, isProdSubtypeCde, action);
    }

    private List<FinDocMapInput> filterChangeItems(List<FinDocMapInput> items, List<Document> prodTypeFinDocs, boolean isProdSubtypeCde, MappingValidation fdv, String action) {
        List<FinDocMapInput> changeItems = new ArrayList<>();
        for (FinDocMapInput fdi : items) {
            boolean match = isMatchingDocument(prodTypeFinDocs, fdi, isProdSubtypeCde);
            boolean alreadyInChangeItems = isAlreadyInChangeItems(changeItems, fdi, isProdSubtypeCde);
            processAction(action, match, alreadyInChangeItems, fdi, fdv, changeItems);
        }
        return changeItems;
    }

    private boolean isMatchingDocument(List<Document> prodTypeFinDocs, FinDocMapInput fdi, boolean isProdSubtypeCde) {
        return prodTypeFinDocs != null && prodTypeFinDocs.stream().anyMatch(doc ->
                StringUtils.equals(doc.getString(Field.ctryRecCde), fdi.getCtryCde()) &&
                        StringUtils.equals(doc.getString(Field.grpMembrRecCde), fdi.getOrgnCde()) &&
                        (isProdSubtypeCde ? StringUtils.equals(doc.getString(Field.prodSubtpCde), fdi.getProdSubtpCde()) : StringUtils.equals(doc.getString(Field.prodTypeCde), fdi.getProdTypeCde())) &&
                        StringUtils.equals(doc.getString(Field.docFinTypeCde), fdi.getDocTypeCdeP()) &&
                        StringUtils.equals(doc.getString(Field.langFinDocCde), fdi.getLangCatCdeP())
        );
    }

    private boolean isAlreadyInChangeItems(List<FinDocMapInput> changeItems, FinDocMapInput fdi, boolean isProdSubtypeCde) {
        return changeItems.stream().anyMatch(fd ->
                StringUtils.equals(fd.getCtryCde(), fdi.getCtryCde()) &&
                StringUtils.equals(fd.getOrgnCde(), fdi.getOrgnCde()) &&
                (isProdSubtypeCde ? StringUtils.equals(fd.getProdSubtpCde(), fdi.getProdSubtpCde()) : StringUtils.equals(fd.getProdTypeCde(), fdi.getProdTypeCde())) &&
                StringUtils.equals(fd.getDocTypeCdeP(), fdi.getDocTypeCdeP()) &&
                StringUtils.equals(fd.getLangCatCdeP(), fdi.getLangCatCdeP())
        );
    }

    private void processAction(String action, boolean match, boolean alreadyInChangeItems, FinDocMapInput fdi, MappingValidation fdv, List<FinDocMapInput> changeItems) {
        switch (action) {
            case ADD_ACTION:
                processInsertAction(match, alreadyInChangeItems, fdi, fdv, changeItems);
                break;
            case DELETE_ACTION:
                processDeleteAction(match, alreadyInChangeItems, fdi, fdv, changeItems);
                break;
            case CHANGE_ACTION:
                processUpdateAction(match, alreadyInChangeItems, fdi, fdv, changeItems);
                break;
            default:
                throw new IllegalArgumentException("Invalid process action: " + action);
        }
    }

    private void processInsertAction(boolean match, boolean alreadyInChangeItems, FinDocMapInput fdi, MappingValidation fdv, List<FinDocMapInput> changeItems) {
        if (!match && !alreadyInChangeItems) {
            changeItems.add(fdi);
        } else {
            fdv.getRejectRec().add(fdv.rejectMail(fdi, FinDocConstants.DUPLICATE_REC_INSERT, log));
        }
    }

    private void processDeleteAction(boolean match, boolean alreadyInChangeItems, FinDocMapInput fdi, MappingValidation fdv, List<FinDocMapInput> changeItems) {
        if (match && !alreadyInChangeItems) {
            changeItems.add(fdi);
        } else {
            fdv.getRejectRec().add(fdv.rejectMail(fdi, FinDocConstants.NO_REC_DELETE, log));
        }
    }

    private void processUpdateAction(boolean match, boolean alreadyInChangeItems, FinDocMapInput fdi, MappingValidation fdv, List<FinDocMapInput> changeItems) {
        if (match && !alreadyInChangeItems) {
            changeItems.add(fdi);
        } else {
            fdv.getRejectRec().add(fdv.rejectMail(fdi, FinDocConstants.NO_REC_CHANGE, log));
        }
    }

    private void processProdTypeFinDocBatch(List<FinDocMapInput> items, boolean isProdSubtypeCde, String action) {
        switch (action) {
            case ADD_ACTION:
                insertProdTypeFinDocBatch(items, isProdSubtypeCde);
                break;
            case DELETE_ACTION:
                deleteProdTypeFinDocBatch(items, isProdSubtypeCde);
                break;
            case CHANGE_ACTION:
                updateProdTypeFinDocBatch(items, isProdSubtypeCde);
                break;
            default:
                throw new IllegalArgumentException("Invalid process ProdTypeFinDoc action: " + action);
        }
    }

    private void deleteProdTypeFinDocBatch(List<FinDocMapInput> items, boolean isProdSubtypeCde) {
        for (FinDocMapInput fdi : items) {
            colProdTypeFinDoc.deleteOne(buildFilter(fdi, isProdSubtypeCde));
        }
    }

    private void updateProdTypeFinDocBatch(List<FinDocMapInput> items, boolean isProdSubtypeCde) {
        for (FinDocMapInput fdi : items) {
            Document prodTypeFinDoc = new Document();
            prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
            prodTypeFinDoc.put(Field.urlDocText, fdi.getUrl());
            prodTypeFinDoc.put(Field.recUpdtDtTm, new Date());
            colProdTypeFinDoc.updateOne(buildFilter(fdi, isProdSubtypeCde), new Document().append("$set", prodTypeFinDoc));
        }
    }

    public boolean isProdRLNull(FinDocMapInput fdi) throws Exception {
    	if (!(fdi.getProdId().equals("*"))&& fdi.getProdRealID() != 0){
            Document prod = productService.productByFilters(BsonUtils.toMap(Filters.eq(Field.prodId, fdi.getProdRealID()))).get(0);
            List<Map<String, Object>> prodFinDocs = JsonPathUtils.readValue(prod, Field.finDoc);
            List<Map<String, Object>> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
            Optional<Map<String, Object>> finDoc = finDocs.stream()
                    .filter(fd -> StringUtils.equals((String)fd.get(Field.docFinTypeCde), fdi.getDocTypeCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.langFinDocCde), fdi.getLangCatCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.finDocCustClassCde), fdi.getCustClassCde())).findFirst();
            return CollectionUtils.isEmpty(finDocs) || !finDoc.isPresent();

        } else if (fdi.getProdSubtpCde() != null && !(fdi.getProdSubtpCde().equals("*"))){
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildSubtpFilter(fdi));
            return CollectionUtils.isEmpty(prodTypeFinDocs);
        } else {
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildTypeFilter(fdi));
            return CollectionUtils.isEmpty(prodTypeFinDocs);
        }
    }
    
    public void insertProdRL(FinDocMapInput fdi) {
        if (!(fdi.getProdId().equals("*"))&& fdi.getProdRealID() != 0){
            Document prod = productService.productByFilters(BsonUtils.toMap(Filters.eq(Field.prodId, fdi.getProdRealID()))).get(0);
            List<Map<String, Object>> prodFinDocs = JsonPathUtils.readValue(prod, Field.finDoc);
            List<Map<String, Object>> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
            Optional<Map<String, Object>> finDoc = finDocs.stream()
                    .filter(fd -> StringUtils.equals((String)fd.get(Field.docFinTypeCde), fdi.getDocTypeCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.langFinDocCde), fdi.getLangCatCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.finDocCustClassCde), fdi.getCustClassCde())).findFirst();
            if (CollectionUtils.isEmpty(finDocs) || !finDoc.isPresent()) {
	            finDocs.add(newFinDoc(fdi));   
	            List<Map<String, Object>> fieldGroupCtoffs = updateFieldGroupCtoffs(prod);
	            updateProdByFilters(prod.getInteger(Field.prodId).longValue(), finDocs, fieldGroupCtoffs);
            }

        } else if (fdi.getProdSubtpCde() != null && !(fdi.getProdSubtpCde().equals("*"))){
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildSubtpFilter(fdi));
            if (CollectionUtils.isEmpty(prodTypeFinDocs)) {
            	insertProdTypeFinDoc(fdi, true, false);
            }
        } else {
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildTypeFilter(fdi));
            if (CollectionUtils.isEmpty(prodTypeFinDocs)) {
            	insertProdTypeFinDoc(fdi, false, true);
            }
        }
    }
    
    public void deleteProdRL(FinDocMapInput fdi) {
        if (!(fdi.getProdId().equals("*"))&& fdi.getProdRealID() != 0){
            Document prod = productService.productByFilters(BsonUtils.toMap(Filters.eq(Field.prodId, fdi.getProdRealID()))).get(0);
            List<Map<String, Object>> prodFinDocs = JsonPathUtils.readValue(prod, Field.finDoc);
            List<Map<String, Object>> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
            Optional<Map<String, Object>> finDoc = finDocs.stream()
                    .filter(fd -> StringUtils.equals((String)fd.get(Field.docFinTypeCde), fdi.getDocTypeCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.langFinDocCde), fdi.getLangCatCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.finDocCustClassCde), fdi.getCustClassCde())).findFirst();
            processProd(prod, finDocs, finDoc);

        } else if (fdi.getProdSubtpCde() != null && !(fdi.getProdSubtpCde().equals("*"))){
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildSubtpFilter(fdi));
            if (!CollectionUtils.isEmpty(prodTypeFinDocs)) {
                colProdTypeFinDoc.deleteOne(buildSubtpFilter(fdi));
            }
        } else {
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildTypeFilter(fdi));
            if (!CollectionUtils.isEmpty(prodTypeFinDocs)) {
                colProdTypeFinDoc.deleteOne(buildTypeFilter(fdi));
            }
        }
    }

    private void processProd(Document prod, List<Map<String, Object>> finDocs, Optional<Map<String, Object>> finDoc) {
        if (!CollectionUtils.isEmpty(finDocs) && finDoc.isPresent()) {
            finDocs.remove(finDoc.get());
            if (CollectionUtils.isEmpty(finDocs)) {
                finDocs = null;
            }
            List<Map<String, Object>> fieldGroupCtoffs = updateFieldGroupCtoffs(prod);
            updateProdByFilters(prod.getInteger(Field.prodId).longValue(), finDocs, fieldGroupCtoffs);
        }
    }

    public void updateProdRL(FinDocMapInput fdi) {
        if (!(fdi.getProdId().equals("*"))&& fdi.getProdRealID() != 0){
            Document prod = productService.productByFilters(BsonUtils.toMap(Filters.eq(Field.prodId, fdi.getProdRealID()))).get(0);
            List<Map<String, Object>> prodFinDocs = JsonPathUtils.readValue(prod, Field.finDoc);
            List<Map<String, Object>> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
            Optional<Map<String, Object>> finDoc = finDocs.stream()
                    .filter(fd -> StringUtils.equals((String)fd.get(Field.docFinTypeCde), fdi.getDocTypeCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.langFinDocCde), fdi.getLangCatCdeP()) &&
                            StringUtils.equals((String)fd.get(Field.finDocCustClassCde), fdi.getCustClassCde())).findFirst();
            if (!CollectionUtils.isEmpty(finDocs) && finDoc.isPresent()) {
            	Map<String, Object> finDocMap = finDoc.get();
            	finDocMap.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
            	finDocMap.put(Field.urlDocText, fdi.getUrl());
            	finDocMap.put(Field.recUpdtDtTm, new DateTime(DateTimeZone.UTC).toString());
            	List<Map<String, Object>> fieldGroupCtoffs = updateFieldGroupCtoffs(prod);
	            updateProdByFilters(prod.getInteger(Field.prodId).longValue(), finDocs, fieldGroupCtoffs);
            }

        } else if (fdi.getProdSubtpCde() != null && !(fdi.getProdSubtpCde().equals("*"))){
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildSubtpFilter(fdi));
            if (!CollectionUtils.isEmpty(prodTypeFinDocs)) {
            	Document prodTypeFinDoc = new Document();
                prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
                prodTypeFinDoc.put(Field.urlDocText, fdi.getUrl());
                prodTypeFinDoc.put(Field.recUpdtDtTm, new Date());
                colProdTypeFinDoc.updateOne(buildSubtpFilter(fdi), new Document().append("$set", prodTypeFinDoc));
            }
        } else {
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildTypeFilter(fdi));
            if (!CollectionUtils.isEmpty(prodTypeFinDocs)) {
            	Document prodTypeFinDoc = new Document();
                prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
                prodTypeFinDoc.put(Field.urlDocText, fdi.getUrl());
                prodTypeFinDoc.put(Field.recUpdtDtTm, new Date());
                colProdTypeFinDoc.updateOne(buildTypeFilter(fdi), new Document().append("$set", prodTypeFinDoc));
            }
        }
    }
    
	private List<Map<String, Object>> updateFieldGroupCtoffs(Document prod) {
		List<Map<String, Object>> prodFieldGroupCtoffs = JsonPathUtils.readValue(prod, Field.fieldGroupCtoff);
		List<Map<String, Object>> fieldGroupCtoffs = CollectionUtils.isEmpty(prodFieldGroupCtoffs) ? new ArrayList<>()
				: prodFieldGroupCtoffs;
		Optional<Map<String, Object>> fieldGroupCtoff = fieldGroupCtoffs.stream()
				.filter(fd -> StringUtils.equals((String) fd.get(Field.fieldGroupCde), Field.product)).findFirst();
		if (fieldGroupCtoff.isPresent()) {
			fieldGroupCtoffs.remove(fieldGroupCtoff.get());
			fieldGroupCtoffs.add(newFieldGroupCtoff());
		} else {
			fieldGroupCtoffs.add(newFieldGroupCtoff());
		}
		return fieldGroupCtoffs;
	}
    
    private Document newFinDoc(FinDocMapInput fdi) {
        Document finDoc = new Document();
        finDoc.put(Field.docFinTypeCde, fdi.getDocTypeCdeP());
        finDoc.put(Field.langFinDocCde, fdi.getLangCatCdeP());
        finDoc.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
        finDoc.put(Field.urlDocText, fdi.getUrl());
        finDoc.put(Field.finDocConHashCde, null);
        finDoc.put(Field.finDocCustClassCde, fdi.getCustClassCde());
        return finDoc;
    }
    
    private Document newFieldGroupCtoff() {
        Document fieldGroupCtoff = new Document();
        fieldGroupCtoff.put(Field.fieldGroupCde, Field.product);
        fieldGroupCtoff.put(Field.fieldGroupCtoffDtTm, new DateTime(DateTimeZone.UTC).toString());
        return fieldGroupCtoff;
    }
    
    private Bson buildSubtpFilter(FinDocMapInput fdi) {
        return Filters.and(Filters.eq(Field.ctryRecCde, fdi.getCtryCde()),
                Filters.eq(Field.grpMembrRecCde, fdi.getOrgnCde()),
                Filters.eq(Field.prodSubtpCde, fdi.getProdSubtpCde()),
                Filters.eq(Field.docFinTypeCde, fdi.getDocTypeCdeP()),
                Filters.eq(Field.langFinDocCde, fdi.getLangCatCdeP()));
    }

    private Bson buildTypeFilter(FinDocMapInput fdi) {
        return Filters.and(Filters.eq(Field.ctryRecCde, fdi.getCtryCde()),
                Filters.eq(Field.grpMembrRecCde, fdi.getOrgnCde()),
                Filters.eq(Field.prodTypeCde, fdi.getProdTypeCde()),
                Filters.eq(Field.docFinTypeCde, fdi.getDocTypeCdeP()),
                Filters.eq(Field.langFinDocCde, fdi.getLangCatCdeP()));
    }

    private Bson buildFilter(FinDocMapInput fdi, boolean isProdSubtypeCde) {
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq(Field.ctryRecCde, fdi.getCtryCde()));
        filters.add(Filters.eq(Field.grpMembrRecCde, fdi.getOrgnCde()));
        if (isProdSubtypeCde) {
            filters.add(Filters.eq(Field.prodSubtpCde, fdi.getProdSubtpCde()));
        } else {
            filters.add(Filters.eq(Field.prodTypeCde, fdi.getProdTypeCde()));
        }
        filters.add(Filters.eq(Field.docFinTypeCde, fdi.getDocTypeCdeP()));
        filters.add(Filters.eq(Field.langFinDocCde, fdi.getLangCatCdeP()));
        return Filters.and(filters);
    }

    private Bson buildFilter(List<FinDocMapInput> items, boolean isProdSubtypeCde) {
        Set<String> ctryRecCdes = new LinkedHashSet<>();
        Set<String> grpMembrRecCdes = new LinkedHashSet<>();
        Set<String> prodSubtpCdes = new LinkedHashSet<>();
        Set<String> prodTypeCdes = new LinkedHashSet<>();
        Set<String> docFinTypeCdes = new LinkedHashSet<>();
        Set<String> langFinDocCdes = new LinkedHashSet<>();

        for (FinDocMapInput fdi : items) {
            ctryRecCdes.add(fdi.getCtryCde());
            grpMembrRecCdes.add(fdi.getOrgnCde());
            docFinTypeCdes.add(fdi.getDocTypeCdeP());
            langFinDocCdes.add(fdi.getLangCatCdeP());
            if (isProdSubtypeCde) {
                prodSubtpCdes.add(fdi.getProdSubtpCde());
            } else {
                prodTypeCdes.add(fdi.getProdTypeCde());
            }
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.in(Field.ctryRecCde, ctryRecCdes));
        filters.add(Filters.in(Field.grpMembrRecCde, grpMembrRecCdes));
        filters.add(Filters.in(Field.docFinTypeCde, docFinTypeCdes));
        filters.add(Filters.in(Field.langFinDocCde, langFinDocCdes));
        if (isProdSubtypeCde) {
            filters.add(Filters.in(Field.prodSubtpCde, prodSubtpCdes));
        } else {
            filters.add(Filters.in(Field.prodTypeCde, prodTypeCdes));
        }

        return Filters.and(filters);
    }

    private void insertProdTypeFinDoc(FinDocMapInput fdi, boolean isProdSubtypeCde, boolean isProdTypeCde) {
       
            Document prodTypeFinDoc = new Document();
            prodTypeFinDoc.put(Field.ctryRecCde, fdi.getCtryCde());
            prodTypeFinDoc.put(Field.grpMembrRecCde, fdi.getOrgnCde());
            if (isProdTypeCde) {
                prodTypeFinDoc.put(Field.prodTypeCde, fdi.getProdTypeCde());
            }
            if (isProdSubtypeCde) {
                prodTypeFinDoc.put(Field.prodSubtpCde, fdi.getProdSubtpCde());
            }
            prodTypeFinDoc.put(Field.docFinTypeCde, fdi.getDocTypeCdeP());
            prodTypeFinDoc.put(Field.langFinDocCde, fdi.getLangCatCdeP());
            prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
            prodTypeFinDoc.put(Field.urlDocText, fdi.getUrl());
            Date currentDate = new Date();
            prodTypeFinDoc.put(Field.recCreatDtTm, currentDate);
            prodTypeFinDoc.put(Field.recUpdtDtTm, currentDate);
            
            colProdTypeFinDoc.insertOne(prodTypeFinDoc);

    }

    public void insertProdTypeFinDocBatch(List<FinDocMapInput> items, boolean isProdSubtypeCde) {
        List<Document> documents = new ArrayList<>();

        for (FinDocMapInput fdi : items) {
            Document prodTypeFinDoc = new Document();
            prodTypeFinDoc.put(Field.ctryRecCde, fdi.getCtryCde());
            prodTypeFinDoc.put(Field.grpMembrRecCde, fdi.getOrgnCde());
            if (isProdSubtypeCde) {
                prodTypeFinDoc.put(Field.prodSubtpCde, fdi.getProdSubtpCde());
            } else {
                prodTypeFinDoc.put(Field.prodTypeCde, fdi.getProdTypeCde());
            }
            prodTypeFinDoc.put(Field.docFinTypeCde, fdi.getDocTypeCdeP());
            prodTypeFinDoc.put(Field.langFinDocCde, fdi.getLangCatCdeP());
            prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, fdi.getDocSerNum());
            prodTypeFinDoc.put(Field.urlDocText, fdi.getUrl());
            Date currentDate = new Date();
            prodTypeFinDoc.put(Field.recCreatDtTm, currentDate);
            prodTypeFinDoc.put(Field.recUpdtDtTm, currentDate);

            documents.add(prodTypeFinDoc);
        }

        if (!documents.isEmpty()) {
            colProdTypeFinDoc.insertMany(documents);
        }
    }
    
	public void updateProdByFilters(Long prodId, List<Map<String, Object>> finDocs,
			List<Map<String, Object>> fieldGroupCtoffs) {
        ProductBatchUpdateByIdInput updateInput = new ProductBatchUpdateByIdInput();
        updateInput.setProdId(prodId);

		List<Operation> opList = buildOps("put", Field.finDoc, finDocs);
		List<Operation> opList2 = buildOps("put", Field.fieldGroupCtoff, fieldGroupCtoffs);
        List<Operation> opList3 = buildOps("put", Field.lastUpdatedBy, JOB_NAME);
        opList.addAll(opList2);
        opList.addAll(opList3);
        updateInput.setOperations(opList);

        ProductBatchUpdateResult updateResult = productService.batchUpdateProductById(Collections.singletonList(updateInput));
        updateResult.logUpdateResult(log);
    }

    public void updateProdByFiltersBatch(Map<Long, List<Map<String, Object>>> productFinDocsMap,
			Map<Long, List<Map<String, Object>>> fieldGroupCtoffsMap) throws Exception {
        List<ProductBatchUpdateByIdInput> updateInputs = new ArrayList<>();

        for (Map.Entry<Long, List<Map<String, Object>>> entry : productFinDocsMap.entrySet()) {
            Long prodId = entry.getKey();
            List<Map<String, Object>> finDocs = entry.getValue();
            List<Map<String, Object>> fieldGroupCtoffs = fieldGroupCtoffsMap.get(prodId);

            ProductBatchUpdateByIdInput updateInput = new ProductBatchUpdateByIdInput();
            updateInput.setProdId(prodId);

            List<Operation> opList = buildOps("put", Field.finDoc, finDocs);
            List<Operation> opList2 = buildOps("put", Field.fieldGroupCtoff, fieldGroupCtoffs);
            List<Operation> opList3 = buildOps("put", Field.lastUpdatedBy, JOB_NAME);
            opList.addAll(opList2);
            opList.addAll(opList3);
            updateInput.setOperations(opList);

            updateInputs.add(updateInput);
        }

        if (!updateInputs.isEmpty()) {
            ProductBatchUpdateResult updateResult = productService.batchUpdateProductById(updateInputs);
            updateResult.logUpdateResult(log);
        }
    }

    public List<Operation> buildOps(String opAction, String fieldPath, Object value) {
        List<Operation> ops = new LinkedList<>();
        Operation op = new Operation();
        op.setOp(opAction);
        op.setPath(fieldPath);
        op.setValue(value);
        ops.add(op);
        return ops;
    }

}