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
import com.dummy.wpb.product.constant.FinDocComConstants;
import com.dummy.wpb.product.model.FinDocInput;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.FinDocCollectionsService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.BsonUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.ImportFinDocFilesJobApplication.JOB_NAME;


@Service
@Slf4j
public class FinDocProdRLService {

    @Autowired
    public FinDocCollectionsService finDocCollectionsService;

    @Autowired
    public MongoDatabase mongoDatabase;

    private MongoCollection<Document> colProdTypeFinDoc;

    public FinDocProdRLService(MongoDatabase mongoDatabase){
        this.colProdTypeFinDoc = mongoDatabase.getCollection(CollectionName.prod_type_fin_doc.toString());
    }

    @Autowired
    public ProductService productService;

    public void prodToFinDocRLProcess(FinDocInput fdi, Long docSerNum) {

        if (StringUtils.isNotBlank(fdi.getProdTypeCde())) {

            if (fdi.getProdId() == 0) {

                Long prodId = 0L;
                Bson filters = Filters.and(Filters.eq(Field.ctryRecCde, fdi.getCtryCde()),
                        Filters.eq(Field.grpMembrRecCde, fdi.getOrgnCde()), Filters.ne(Field.prodStatCde, "D"),
                        Filters.eq(Field.prodTypeCde, fdi.getProdTypeCde()), Filters.eq(Field.prodAltPrimNum, fdi.getProdCde()));
                List<Document> prods = productService.productByFilters(BsonUtils.toMap(filters));
                if (!CollectionUtils.isEmpty(prods)) {
                    prodId = prods.get(0).getInteger(Field.prodId).longValue();
                }
                fdi.setProdId(prodId);
            }

            //update or insert 2 collections: prod_fin_doc, prod_type_fin_doc(PROD_TYPE_FIN_DOC / PROD_SUBTP_FIN_DOC)
            prodRLUpdateOrInsert(fdi, docSerNum);

            if (fdi.getLangCatCde().equalsIgnoreCase(FinDocComConstants.BL)) {
                fdi.setLangCatCde(FinDocComConstants.EN);
                prodRLInsert(fdi, docSerNum);
            	if(("JP").equals(fdi.getCtryCde()) && ("dummy").equals(fdi.getOrgnCde())){
                     fdi.setLangCatCde(FinDocComConstants.JP);
            	}else if(("ID").equals(fdi.getCtryCde()) && ("dummy").equals(fdi.getOrgnCde())){
                    fdi.setLangCatCde(FinDocComConstants.IN);
            	}else{
                    fdi.setLangCatCde(FinDocComConstants.TW);
            	}
                prodRLInsert(fdi, docSerNum);
            }
        }
    }

            //DocSerNum
    public void prodRLUpdateOrInsert (FinDocInput fdi, Long docSerNum) {
        if (StringUtils.isNotBlank(fdi.getProdCde()) && fdi.getProdId() != 0){
            //update / insert product.finDoc
            Document prod = productService.productByFilters(BsonUtils.toMap(Filters.eq(Field.prodId, fdi.getProdId()))).get(0);
            List<Document> prodFinDocs = prod.getList(Field.finDoc, Document.class);
            List<Document> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs.stream()
                    .filter(fd -> !(StringUtils.equals((String)fd.get(Field.docFinTypeCde), fdi.getDocTypeCde()) &&
                            StringUtils.equals((String)fd.get(Field.langFinDocCde), fdi.getLangCatCde()))).collect(Collectors.toList());
            finDocs.add(newFinDoc(fdi, docSerNum));
            updateProdByFilters(prod.getInteger(Field.prodId), finDocs);

        } else if (StringUtils.isNotBlank(fdi.getProdSubtypeCde())){
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildSubtpFilter(fdi));
            updOrInsProdTypeFinDoc(prodTypeFinDocs, fdi, docSerNum, true, false);

        } else {
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildTypeFilter(fdi));
            updOrInsProdTypeFinDoc(prodTypeFinDocs, fdi, docSerNum, false, true);
        }

    }

    private void updOrInsProdTypeFinDoc(List<Document> prodTypeFinDocs, FinDocInput fdi, Long docSerNum, Boolean isProdSubtypeCde, Boolean isProdTypeCde) {
        //update
        if (CollectionUtils.isNotEmpty(prodTypeFinDocs)) {
            Document prodTypeFinDoc = prodTypeFinDocs.get(0);
            prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, docSerNum);
            prodTypeFinDoc.put(Field.urlDocText, null);
            prodTypeFinDoc.put(Field.recUpdtDtTm, new LocalDateTime());
            colProdTypeFinDoc.updateOne(Filters.eq("_id", prodTypeFinDoc.get(Field._id)), prodTypeFinDoc);
        }else {
            //insert
            insertProdTypeFinDoc(prodTypeFinDocs, fdi, docSerNum, isProdSubtypeCde, isProdTypeCde);
        }
    }

    private Document newFinDoc(FinDocInput fdi, Long docSerNum) {
        Document finDoc = new Document();
        finDoc.put(Field.rowid, UUID.randomUUID().toString());
        finDoc.put(Field.docFinTypeCde, fdi.getDocTypeCde());
        finDoc.put(Field.langFinDocCde, fdi.getLangCatCde());
        finDoc.put(Field.rsrcItemIdFinDoc, docSerNum);
        finDoc.put(Field.urlDocText, null);
        finDoc.put(Field.finDocConHashCde, null);
        finDoc.put(Field.finDocCustClassCde, "ALL");
        finDoc.put(Field.recCreatDtTm, new LocalDateTime());
        finDoc.put(Field.recUpdtDtTm, new LocalDateTime());
        return finDoc;
    }


    public void updateProdByFilters(Integer prodId, List<Document> finDocs) {
        Map<String, Object> filterMap = BsonUtils.toMap(Filters.eq(Field.prodId, prodId));
        List<Operation> opList = buildOps("put", Field.finDoc, finDocs);
        List<Operation> opList2 = buildOps("put", Field.lastUpdatedBy, JOB_NAME);
        opList.addAll(opList2);
        ProductBatchUpdateResult updateResult = productService.batchUpdateProduct(new ProductBatchUpdateInput(filterMap, opList));
        updateResult.logUpdateResult(log);
    }

    public List<Operation> buildOps(String opAction, String fieldPath, Object o) {
        List<Operation> ops = new LinkedList<>();
        Operation op = new Operation();
        op.setOp(opAction);
        op.setPath(fieldPath);
        op.setValue(o);
        ops.add(op);
        return ops;
    }

    public void prodRLInsert(FinDocInput fdi, Long docSerNum) {

        if (StringUtils.isNotBlank(fdi.getProdCde()) && fdi.getProdId() != 0){
            Document prod = productService.productByFilters(BsonUtils.toMap(Filters.eq(Field.prodId, fdi.getProdId()))).get(0);
            List<Document> prodFinDocs = prod.getList(Field.finDoc, Document.class);
            List<Document> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
            Optional<Document> finDoc = finDocs.stream()
                    .filter(fd -> StringUtils.equals((String)fd.get(Field.docFinTypeCde), fdi.getDocTypeCde()) &&
                            StringUtils.equals((String)fd.get(Field.langFinDocCde), fdi.getLangCatCde())).findFirst();
            if (CollectionUtils.isEmpty(finDocs) || !finDoc.isPresent()) {
                finDocs.add(newFinDoc(fdi, docSerNum));
                updateProdByFilters(prod.getInteger(Field.prodId), finDocs);
            }

        } else if (StringUtils.isNotBlank(fdi.getProdSubtypeCde())) {
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildSubtpFilter(fdi));
            insertProdTypeFinDoc(prodTypeFinDocs, fdi, docSerNum, true, false);
        } else {
            List<Document> prodTypeFinDocs = finDocCollectionsService.prodTypeFinDocByFilters(buildTypeFilter(fdi));
            insertProdTypeFinDoc(prodTypeFinDocs, fdi, docSerNum, false, true);
        }
    }

    private Bson buildTypeFilter(FinDocInput fdi) {
        return Filters.and(Filters.eq(Field.ctryRecCde, fdi.getCtryCde()),
                Filters.eq(Field.grpMembrRecCde, fdi.getOrgnCde()),
                Filters.eq(Field.prodTypeCde, fdi.getProdTypeCde()),
                Filters.eq(Field.docFinTypeCde, fdi.getDocTypeCde()),
                Filters.eq(Field.langFinDocCde, fdi.getLangCatCde()));
    }

    private Bson buildSubtpFilter(FinDocInput fdi) {
        return Filters.and(Filters.eq(Field.ctryRecCde, fdi.getCtryCde()),
                Filters.eq(Field.grpMembrRecCde, fdi.getOrgnCde()),
                Filters.eq(Field.prodSubtpCde, fdi.getProdSubtypeCde()),
                Filters.eq(Field.docFinTypeCde, fdi.getDocTypeCde()),
                Filters.eq(Field.langFinDocCde, fdi.getLangCatCde()));
    }

    private void insertProdTypeFinDoc(List<Document> prodTypeFinDocs, FinDocInput fdi, Long docSerNum, boolean isProdSubtypeCde, boolean isProdTypeCde) {
        if (CollectionUtils.isEmpty(prodTypeFinDocs)) {
            Document prodTypeFinDoc = new Document();
            prodTypeFinDoc.put(Field.rowid, UUID.randomUUID().toString());
            prodTypeFinDoc.put(Field.ctryRecCde, fdi.getCtryCde());
            prodTypeFinDoc.put(Field.grpMembrRecCde, fdi.getOrgnCde());
            if (isProdTypeCde) {
                prodTypeFinDoc.put(Field.prodTypeCde, fdi.getProdTypeCde());
            }
            if (isProdSubtypeCde) {
                prodTypeFinDoc.put(Field.prodSubtpCde, fdi.getProdSubtypeCde());
            }
            prodTypeFinDoc.put(Field.docFinTypeCde, fdi.getDocTypeCde());
            prodTypeFinDoc.put(Field.langFinDocCde, fdi.getLangCatCde());
            prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, docSerNum);
            prodTypeFinDoc.put(Field.urlDocText, null);
            prodTypeFinDoc.put(Field.recUpdtDtTm, new LocalDateTime());
            prodTypeFinDoc.put(Field.recCreatDtTm, new LocalDateTime());
            colProdTypeFinDoc.insertOne(prodTypeFinDoc);
        }
    }
}