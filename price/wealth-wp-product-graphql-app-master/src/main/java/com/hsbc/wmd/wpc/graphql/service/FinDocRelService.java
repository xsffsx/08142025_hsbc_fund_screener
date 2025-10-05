package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ApprovalAction;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mongodb.client.model.Filters.*;


//for financial document relate, fin_doc_upld \ fin_doc_hist \ prod_type_fin_doc
@Service
public class FinDocRelService {

    @Autowired
    private ProductService productService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoCollection<Document> getFinDocUpldCollection() {
       return mongoTemplate.getCollection(CollectionName.fin_doc_upld.name());
    }

    private MongoCollection<Document> getProductCollection() {
       return mongoTemplate.getCollection(CollectionName.product.name());
    }

    private MongoCollection<Document> getProdTypeFinDocCollection() {
       return mongoTemplate.getCollection(CollectionName.prod_type_fin_doc.name());
    }

    public void updateFinDocUpload(Document doc, ApprovalAction approvalAction, String rejMsg) {
        Document newFinDocUpl = getFinDocUpRec(doc.getString(Field.ctryRecCde), doc.getString(Field.grpMembrRecCde), doc.getString(Field.docFinTypeCde),
                doc.getString(Field.docFinCatCde), doc.getString(Field.docFinCde), doc.getString(Field.langFinDocCde), "P");
        Date date = new Date();
        if (approvalAction.equals(ApprovalAction.approved)) {
            newFinDocUpl.put(Field.docStatCde, "V");
            newFinDocUpl.put(Field.docServrStatCde, "P");
            newFinDocUpl.put(Field.docArchStatCde, "P");
            newFinDocUpl.put(Field.docSerNum, doc.getLong(Field.rsrcItemIdFinDoc));
        }else if (approvalAction.equals(ApprovalAction.rejected)) {
            newFinDocUpl.put(Field.docStatCde, "J");
            newFinDocUpl.put(Field.reasonRejText, rejMsg);
        }

        newFinDocUpl.put(Field.urlLclSysText, doc.getString(Field.urlLclSysText));
        newFinDocUpl.put(Field.recUpdtDtTm, date);
        newFinDocUpl.put(Field.docAproveDtTm, date);
        getFinDocUpldCollection().replaceOne(eq(Field._id, newFinDocUpl.get(Field._id)), newFinDocUpl);

    }

    private Document getFinDocUpRec(String ctryRecCde, String grpMembrRecCde, String docFinTypeCde, String docFinCatCde, String docFinCde,
                                          String langFinDocCde, String docStatCde) {
        Bson filter = and(
                eq(Field.ctryRecCde, ctryRecCde),
                eq(Field.grpMembrRecCde, grpMembrRecCde),
                eq(Field.docFinTypeCde, docFinTypeCde),
                eq(Field.docFinCatCde, docFinCatCde),
                eq(Field.docFinCde, docFinCde),
                eq(Field.langFinDocCde, langFinDocCde),
                eq(Field.docStatCde, docStatCde));

        return getFinDocUpldCollection().find(filter).sort(Sorts.descending(Field.recCreatDtTm)).first();
    }

    public void updateOrInsertPDRL(Document doc) {
        String ctryRecCde = doc.getString(Field.ctryRecCde);
        String grpMembrRecCde = doc.getString(Field.grpMembrRecCde);

        Bson filter = and(
                eq(Field.ctryRecCde, ctryRecCde),
                eq(Field.grpMembrRecCde, grpMembrRecCde),
                eq(Field.prodTypeCde, doc.getString(Field.prodTypeCde)),
                eq(Field.prodAltPrimNum, doc.getString(Field.prodCde)),
                ne(Field.prodStatCde, "D"));
        Document prodDoc = getProductCollection().find(filter).first();

        //update or insert 2 collections: prod_fin_doc, prod_type_fin_doc(PROD_TYPE_FIN_DOC / PROD_SUBTP_FIN_DOC)
        prodRLUpdateOrInsert(doc, prodDoc);

        if (doc.getString(Field.langFinDocCde).trim().equalsIgnoreCase("BL")) {
            doc.put(Field.langFinDocCde, "EN");
            prodRLInsert(doc, prodDoc);
            if(("JP").equals(ctryRecCde) && ("dummy").equals(grpMembrRecCde)){
                doc.put(Field.langFinDocCde, "JP");
            }else if(("ID").equals(ctryRecCde) && ("dummy").equals(grpMembrRecCde)){
                doc.put(Field.langFinDocCde, "IN");
            }else{
                doc.put(Field.langFinDocCde, "TW");
            }
            prodRLInsert(doc, prodDoc);
        }

    }

    public void prodRLUpdateOrInsert(Document doc, Document prodDoc) {
        if (StringUtils.isNotBlank(doc.getString(Field.prodCde)) && prodDoc != null){
            updateProdFinDoc(doc, prodDoc);
        } else {
            ArrayList<Document> prodTypeFinDocs = getProdTypeFinDocCollection().find(buildTypeFilter(doc)).into(new ArrayList<>());
            updOrInsProdTypeFinDoc(prodTypeFinDocs, doc, false, true);
        }
    }

    public Document newFinDoc(Document doc) {
        Document finDoc = new Document();
        Date date = new Date();
        finDoc.put(Field.rowid, UUID.randomUUID().toString());
        finDoc.put(Field.docFinTypeCde, doc.getString(Field.docFinTypeCde));
        finDoc.put(Field.langFinDocCde, doc.getString(Field.langFinDocCde));
        finDoc.put(Field.rsrcItemIdFinDoc, doc.getLong(Field.rsrcItemIdFinDoc));
        finDoc.put(Field.urlDocText, null);
        finDoc.put(Field.finDocConHashCde, null);
        finDoc.put(Field.finDocCustClassCde, "ALL");
        finDoc.put(Field.recCreatDtTm, date);
        finDoc.put(Field.recUpdtDtTm, date);
        return finDoc;
    }

    public Bson buildTypeFilter(Document doc) {
        return Filters.and(Filters.eq(Field.ctryRecCde, doc.getString(Field.ctryRecCde)),
                Filters.eq(Field.grpMembrRecCde, doc.getString(Field.grpMembrRecCde)),
                Filters.eq(Field.prodTypeCde, doc.getString(Field.prodTypeCde)),
                Filters.eq(Field.docFinTypeCde, doc.getString(Field.docFinTypeCde)),
                Filters.eq(Field.langFinDocCde, doc.getString(Field.langFinDocCde)));
    }

    public void updOrInsProdTypeFinDoc(List<Document> prodTypeFinDocs, Document doc, Boolean isProdSubtypeCde, Boolean isProdTypeCde) {
        //update
        if (CollectionUtils.isNotEmpty(prodTypeFinDocs)) {
            Document prodTypeFinDoc = prodTypeFinDocs.get(0);
            prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, doc.getLong(Field.rsrcItemIdFinDoc));
            prodTypeFinDoc.put(Field.urlDocText, null);
            prodTypeFinDoc.put(Field.recUpdtDtTm, new Date());
            getProdTypeFinDocCollection().updateOne(Filters.eq("_id", prodTypeFinDoc.get(Field._id)), prodTypeFinDoc);
        }else {
            //insert
            insertProdTypeFinDoc(prodTypeFinDocs, doc, isProdSubtypeCde, isProdTypeCde);
        }
    }

    public void insertProdTypeFinDoc(List<Document> prodTypeFinDocs, Document doc, boolean isProdSubtypeCde, boolean isProdTypeCde) {
        if (CollectionUtils.isEmpty(prodTypeFinDocs)) {
            Document prodTypeFinDoc = new Document();
            prodTypeFinDoc.put(Field.rowid, UUID.randomUUID().toString());
            prodTypeFinDoc.put(Field.ctryRecCde, doc.getString(Field.ctryRecCde));
            prodTypeFinDoc.put(Field.grpMembrRecCde, doc.getString(Field.grpMembrRecCde));
            if (isProdTypeCde) {
                prodTypeFinDoc.put(Field.prodTypeCde, doc.getString(Field.prodTypeCde));
            }
            if (isProdSubtypeCde) {
                prodTypeFinDoc.put(Field.prodSubtpCde, doc.getString(Field.prodSubtpCde));
            }
            prodTypeFinDoc.put(Field.docFinTypeCde, doc.getString(Field.docFinTypeCde));
            prodTypeFinDoc.put(Field.langFinDocCde, doc.getString(Field.langFinDocCde));
            prodTypeFinDoc.put(Field.rsrcItemIdFinDoc, doc.getString(Field.rsrcItemIdFinDoc));
            prodTypeFinDoc.put(Field.urlDocText, null);
            Date date = new Date();
            prodTypeFinDoc.put(Field.recUpdtDtTm, date);
            prodTypeFinDoc.put(Field.recCreatDtTm, date);
            getProdTypeFinDocCollection().insertOne(prodTypeFinDoc);
        }
    }

    public void prodRLInsert(Document doc, Document prodDoc) {

        if (StringUtils.isNotBlank(doc.getString(Field.prodCde)) && prodDoc != null){
            updateProdFinDoc(doc, prodDoc);

        }else {
            ArrayList<Document> prodTypeFinDocs = getProdTypeFinDocCollection().find(buildTypeFilter(doc)).into(new ArrayList<>());
            insertProdTypeFinDoc(prodTypeFinDocs, doc, false, true);
        }
    }

    public void updateProdFinDoc(Document doc, Document prodDoc) {
        List<Document> prodFinDocs = prodDoc.getList(Field.finDoc, Document.class);
        List<Document> finDocs = CollectionUtils.isEmpty(prodFinDocs) ? new ArrayList<>() : prodFinDocs;
        Optional<Document> finDoc = finDocs.stream()
                .filter(fd -> StringUtils.equals((String) fd.get(Field.docFinTypeCde), doc.getString(Field.docFinTypeCde)) &&
                        StringUtils.equals((String) fd.get(Field.langFinDocCde), doc.getString(Field.langFinDocCde))).findFirst();
        if (CollectionUtils.isEmpty(finDocs) || !finDoc.isPresent()) {
            finDocs.add(newFinDoc(doc));
            prodDoc.put(Field.finDoc, finDocs);
            productService.updateProduct(prodDoc, null);
        }
    }
}
