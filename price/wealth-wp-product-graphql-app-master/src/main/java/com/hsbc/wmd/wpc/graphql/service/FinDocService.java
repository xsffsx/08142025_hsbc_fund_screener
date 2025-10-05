package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.FinDocBatchCreateResult;
import com.dummy.wmd.wpc.graphql.model.FinDocBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.model.FinDocValidationResult;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.utils.JsonPathUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

@Service
public class FinDocService {

    private MongoCollection<Document> finDocCollection;

    private MongoCollection<Document> sysParmCollection;

    public static final String SYS_PARAM_FIN_DOC_SEQ_NUM = "FDSN";

    @Autowired
    private LockService lockService;

    public FinDocService(MongoDatabase mongoDatabase) {
        this.finDocCollection = mongoDatabase.getCollection(CollectionName.fin_doc.toString());
        this.sysParmCollection = mongoDatabase.getCollection(CollectionName.sys_parm.toString());
    }

    public List<Error> validate(Document finDoc) {
        List<Error> errors = new ArrayList<>();
        //---TO_DO check input data type
        // check duplicate fin doc
        Bson filter = and(
                eq(Field.ctryRecCde, finDoc.get(Field.ctryRecCde)),
                eq(Field.grpMembrRecCde, finDoc.get(Field.grpMembrRecCde)),
                eq(Field.docFinTypeCde, finDoc.get(Field.docFinTypeCde)),
                eq(Field.docFinCatCde, finDoc.get(Field.docFinCatCde)),
                eq(Field.docFinCde, finDoc.get(Field.docFinCde)),
                eq(Field.langFinDocCde, finDoc.get(Field.langFinDocCde)));
        // when updating an existed finDoc, check duplicated records with different rsrcItemIdFinDoc 
        if (finDoc.get(Field.rsrcItemIdFinDoc) != null) {
            filter = and(filter, ne(Field.rsrcItemIdFinDoc, finDoc.getLong(Field.rsrcItemIdFinDoc)));
        }
        if (finDocCollection.countDocuments(filter) > 0) {
            errors.add(new Error("$", "@duplicate",
                    String.format("Financial document data with fileDocName = %s is duplicated", finDoc.getString(Field.fileDocName))));
        }

        return errors;
    }

    public FinDocBatchCreateResult batchCreateFinDoc(List<Map<String, Object>> finDocList, boolean allowPartial) {
        // validate fin doc
        List<Document> validFinDocs = new ArrayList<>();
        List<FinDocValidationResult> invalidFinDocs = new ArrayList<>();
        finDocList.forEach(map -> {
            Document finDoc = new Document(map);
            List<Error> errors = validate(finDoc);
            if (!errors.isEmpty()) {
                invalidFinDocs.add(new FinDocValidationResult(finDoc, errors));
            } else {
                validFinDocs.add(finDoc);
            }
        });
        // save fin doc in DB
        // if allowPartial is true, keep doing
        // if allowPartial is false & invalid list > 0, skip
        List<Document> createdFinDocs = new ArrayList<>();
        if (allowPartial || invalidFinDocs.isEmpty()) {
            validFinDocs.forEach(finDoc -> createdFinDocs.add(batchCreate(finDoc)));
        }

        FinDocBatchCreateResult result = new FinDocBatchCreateResult();
        result.setCreatedFinDocs(createdFinDocs);
        result.setInvalidFinDocs(invalidFinDocs);
        return result;
    }

    private Document batchCreate(Document doc) throws productErrorException {
        String ctryRecCde = doc.getString(Field.ctryRecCde);
        String grpMembrRecCde = doc.getString(Field.grpMembrRecCde);
        String lockName = ctryRecCde + "." + grpMembrRecCde + "." + SYS_PARAM_FIN_DOC_SEQ_NUM + ".LOCK";
        lockService.lock(lockName);
        long rsrcItemIdFinDoc;
        try {
            Document rsrcItemIdFinDocSysParam = getDocSerNumDoc(ctryRecCde, grpMembrRecCde);
            rsrcItemIdFinDoc = Long.parseLong(rsrcItemIdFinDocSysParam.getString(Field.parmValueText));
            // update id sequence
            updateRsrcItemIdFinDoc(rsrcItemIdFinDocSysParam, rsrcItemIdFinDoc + 1);
        } finally {
            lockService.unLock(lockName);
        }
        doc.computeIfAbsent(Field._id, k -> doc.put(k, UUID.randomUUID().toString()));
        doc.put(Field.rsrcItemIdFinDoc, rsrcItemIdFinDoc + 1);
        doc.put(Field.docEffDtTm, new Date());
        doc.put(Field.recCreatDtTm, new Date());
        doc.put(Field.recUpdtDtTm, new Date());
        doc.put(Field.lastUpdatedBy, "product-batch");

        // insert fin doc
        finDocCollection.insertOne(doc);
        return doc;
    }

    public FinDocBatchUpdateResult batchUpdateFinDoc(Bson filter, List<OperationInput> operations, boolean allowPartial) {
        // find fin doc by filter
        List<Document> matchFinDocs = finDocCollection.find(filter).into(new ArrayList<>());
        // apply update operations
        List<Document> mergedFinDocs = applyUpdateOperations(matchFinDocs, operations);
        // valid fin doc list
        List<Document> validFinDocs = new ArrayList<>();
        // invalid fin doc list
        List<FinDocValidationResult> invalidFinDocs = new ArrayList<>();
        // validate fin doc
        mergedFinDocs.forEach(finDoc -> {
            List<Error> errors = validate(finDoc);
            if (!errors.isEmpty()) {
                invalidFinDocs.add(new FinDocValidationResult(finDoc, errors));
            } else {
                validFinDocs.add(finDoc);
            }
        });
        // save fin doc in DB
        // if allowPartial is true, keep doing
        // if allowPartial is false & invalid list > 0, skip
        List<Document> updatedFinDocs = new ArrayList<>();
        if (allowPartial || invalidFinDocs.isEmpty()) {
            validFinDocs.forEach(finDoc -> updatedFinDocs.add(batchUpdate(finDoc)));
        }

        FinDocBatchUpdateResult result = new FinDocBatchUpdateResult();
        result.setMatchCount(matchFinDocs.size());
        result.setMatchFinDocs(matchFinDocs);
        result.setInvalidFinDocs(invalidFinDocs);
        result.setUpdatedFinDocs(updatedFinDocs);
        return result;
    }

    /**
     * apply update operations to the fin doc list, but not update DB yet
     * @param finDocList
     * @param operations
     */
    private List<Document> applyUpdateOperations(List<Document> finDocList, List<OperationInput> operations) {
        List<Document> newFinDocList = DocumentUtils.clone(finDocList);
        List<Document> resultList = new ArrayList<>();
        newFinDocList.forEach(finDoc -> {
            JsonPathUtils.applyChanges(finDoc, operations);
            resultList.add(finDoc);
        });
        return resultList;
    }

    private Document batchUpdate(Document doc) throws productErrorException {
        Long rsrcItemIdFinDoc = doc.getLong(Field.rsrcItemIdFinDoc);
        Document oldDoc = finDocCollection.find(eq(Field.rsrcItemIdFinDoc, rsrcItemIdFinDoc)).first();
        if (null == oldDoc) {
            throw new productErrorException(productErrors.RuntimeException, "Financial document not found, rsrcItemIdFinDoc = " + rsrcItemIdFinDoc);
        }

        doc.put(Field.docEffDtTm, new Date());
        doc.put(Field.recCreatDtTm, oldDoc.getDate(Field.recCreatDtTm));
        doc.put(Field.recUpdtDtTm, new Date());
        doc.put(Field.lastUpdatedBy, "product-batch");
        finDocCollection.replaceOne(eq(Field._id, oldDoc.getString(Field._id)), doc);
        return doc;
    }

    public void updateRsrcItemIdFinDoc(Document sysParam, Long newRsrcItemIdFinDoc) {
        sysParam.put(Field.parmValueText, newRsrcItemIdFinDoc.toString());
        sysParam.put(Field.recUpdtDtTm, new Date());
        // replace by id
        sysParmCollection.replaceOne(eq(Field._id, sysParam.get(Field._id)), sysParam);
    }

    public Document getDocSerNumDoc(String ctryRecCde, String grpMembrRecCde) {
        Bson query = and(
                eq(Field.ctryRecCde, ctryRecCde),
                eq(Field.grpMembrRecCde, grpMembrRecCde),
                eq(Field.parmCde, SYS_PARAM_FIN_DOC_SEQ_NUM)
        );
        Document sysParam = sysParmCollection.find(query).first();
        if (null == sysParam) {
            throw new productErrorException(productErrors.RuntimeException, "Financial document resource item ID is not defined");
        }
        return sysParam;
    }
}
