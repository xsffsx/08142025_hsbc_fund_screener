package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.constant.EmailContent;
import com.dummy.wmd.wpc.graphql.email.EmailFormation;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.*;

@Service
public class FinDocHistChangeService implements ChangeService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LockService lockService;

    @Autowired
    private FinDocRelService finDocRelService;

    @Autowired
    private FinDocService finDocService;

    @Value("${product.finDoc.reject.ENS}")
    private String rejLogPath;

    @Value("${product.chmod.script}")
    private String chmodScriptPath;

    private MongoCollection<Document> getFinDocHistCollection() {
       return mongoTemplate.getCollection(CollectionName.fin_doc_hist.name());
    }

    @Override
    public List<Error> validate(Document amendment) {
        List<Error> errors = new ArrayList<>();

        // when updating an existed finDocHist, check duplicated records with different rsrcItemIdFinDoc
        Document newFinDoc = amendment.get(Field.doc, Document.class);
        if (newFinDoc.get(Field.rsrcItemIdFinDoc) != null) {
            Bson filter = and(
                    eq(Field.ctryRecCde, newFinDoc.get(Field.ctryRecCde)),
                    eq(Field.grpMembrRecCde, newFinDoc.get(Field.grpMembrRecCde)));
            filter = and(filter, eq(Field.rsrcItemIdFinDoc, newFinDoc.getLong(Field.rsrcItemIdFinDoc)));
            Long rsrcItemIdFinDoc = newFinDoc.getLong(Field.rsrcItemIdFinDoc);

            if (getFinDocHistCollection().countDocuments(filter) > 0) {
                errors.add(new Error("$", "@duplicate", String.format("Financial document data with rsrcItemIdFinDoc = %s is repeated", rsrcItemIdFinDoc)));
            }
        }

        return errors;
    }

    //@insert record in fin_doc_hist; update sys_parm; update fin_doc_upld;@
    @Override
    public void add(Document doc) {
        String lockName = doc.getString(Field.ctryRecCde) + "." + doc.getString(Field.grpMembrRecCde) + "." + FinDocService.SYS_PARAM_FIN_DOC_SEQ_NUM + ".LOCK";
        lockService.lock(lockName);
        Document rsrcItemIdFinDoc;
        long finDocHistItemId;
        try {
            rsrcItemIdFinDoc = finDocService.getDocSerNumDoc(doc.getString(Field.ctryRecCde), doc.getString(Field.grpMembrRecCde));
            finDocHistItemId = Long.parseLong(rsrcItemIdFinDoc.getString(Field.parmValueText).trim());
            finDocService.updateRsrcItemIdFinDoc(rsrcItemIdFinDoc, finDocHistItemId + 1);
        } finally {
            lockService.unLock(lockName);
        }
        Date dateTm = new Date();
        doc.put(Field._id, UUID.randomUUID().toString());
        doc.put(Field.rsrcItemIdFinDoc, finDocHistItemId + 1);
        doc.put(Field.docStatCde, "V");
        doc.put(Field.docArchStatCde, "P");
        doc.put(Field.docServrStatCde, "P");
        doc.put(Field.reqAproveInd, "Y");
        doc.put(Field.recCreatDtTm, dateTm);
        doc.put(Field.recUpdtDtTm, dateTm);
        doc.put(Field.docAproveDtTm, dateTm);
        doc.put(Field.docArchSerNum, "WPC" + rsrcItemIdFinDoc.getString(Field.parmValueText).trim());//Temp Archive Num
        getFinDocHistCollection().insertOne(doc);

        finDocRelService.updateFinDocUpload(doc, ApprovalAction.approved, null);

        if (!StringUtils.isEmpty(doc.getString(Field.prodTypeCde))
                && !StringUtils.isEmpty(doc.getString(Field.prodSubtpCde))
                && !StringUtils.isEmpty(doc.getString(Field.prodCde))) {
            finDocRelService.updateOrInsertPDRL(doc);
        }
    }

    @Override
    public void update(Document doc) {
        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document oldDoc = getFinDocHistCollection().find(eq(Field._id, doc.get(Field._id))).first();
        if (null == oldDoc) {
            throw new productErrorException(productErrors.RuntimeException, "Financial document not found, id=" + id);
        }

        doc.put(Field.recCreatDtTm, oldDoc.getDate(Field.recCreatDtTm));
        doc.put(Field.recUpdtDtTm, new Date());
        doc.put(Field.lastUpdatedBy, "wps");
        getFinDocHistCollection().replaceOne(filter, doc);
    }

    @Override
    public void delete(Document doc) {
        getFinDocHistCollection().deleteOne(eq(Field._id, doc.get(Field._id)));
    }

    @Override
    public Document findFirst(Bson filter) {
        return getFinDocHistCollection().find(filter).first();
    }

    public void rejectOpation(Document amdmDoc) {
        Document docChanged = DocumentUtils.compactDocument((Document) amdmDoc.get(Field.doc));
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss 'on' dd MMM yy");
        String dateTime = docChanged.getDate(Field.recCreatDtTm) != null ? sf.format(docChanged.getDate(Field.recCreatDtTm)) : null;
        String rejMsg = "dummy authorizer rejected document submitted at " + dateTime + ". Please upload again.";
        finDocRelService.updateFinDocUpload(docChanged, ApprovalAction.rejected, rejMsg);
        sendEmail(docChanged, rejMsg);
    }

    private void sendEmail(Document docChanged, String content) {
        String sender = "#WPC_SUPPORT";

        EmailFormation eForm = new EmailFormation();
        EmailContent emailCnt = new EmailContent();

        String defAdd = "#WPC_SUPPORT";
        emailCnt.setRecptAdr(defAdd);
        String sub = "Document " + docChanged.getString(Field.docRecvName) + " Rejected";
        emailCnt.setSubject(sub);
        emailCnt.setContent(content);
        EmailContent[] emCnts = new EmailContent[] { emailCnt };
        eForm.emailFormation(sender, emCnts, chmodScriptPath, rejLogPath);
    }
}
