package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.CodeUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Service
public class StaffLicenseCheckChangeService implements ChangeService {
    private MongoCollection<Document> colStaffLicenseCheckData;

    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private ReferenceDataService referenceDataService;

    public StaffLicenseCheckChangeService(MongoDatabase mongoDatabase) {
        this.colStaffLicenseCheckData = mongoDatabase.getCollection(CollectionName.staff_license_check.toString());
    }

    @Override
    public List<Error> validate(Document amendment) {
        List<Error> errors = new ArrayList<>();
        String cde = amendment.getString(Field.actionCde);
        ActionCde actionCde = ActionCde.valueOf(cde);

        Document doc = (Document) amendment.get(Field.doc);
        String prodTypeCde = doc.getString(Field.prodTypeCde);
        String prodSubtpCde = doc.getString(Field.prodSubtpCde);
        String ctryRecCde = doc.getString(Field.ctryRecCde);
        String grpMembrRecCde = doc.getString(Field.grpMembrRecCde);
        String employPosnCde = doc.getString(Field.employPosnCde);
        if (StringUtils.isAllBlank(prodTypeCde, prodSubtpCde)) {
            errors.add(new Error("$", "@null", "Please input prodTypeCde/prodSubtpCde"));
            return errors;
        }

        if (!StringUtils.isAnyBlank(prodTypeCde, prodSubtpCde)) {
            errors.add(new Error("$", "@prodTypeCde", "Please input either prodTypeCde or prodSubtpCde"));
            return errors;
        }

        if (!isValidCode(ctryRecCde, grpMembrRecCde, "PRODTYP", prodTypeCde)) {
            errors.add(new Error("$prodTypeCde", "prodTypeCde@invalid", "Product Type Code is invalid."));
            return errors;
        }

        if (!isValidCode(ctryRecCde, grpMembrRecCde, "PRODSUBTP", prodSubtpCde)) {
            errors.add(new Error("$prodSubtpCde", "prodSubtpCde@invalid", "Product Subtype Code is invalid."));
            return errors;
        }

        if (StringUtils.isBlank(employPosnCde) || !isValidCode(ctryRecCde, grpMembrRecCde, "STFCATGY", employPosnCde)) {
            errors.add(new Error("$employPosnCde", "employPosnCde@invalid", "Employment Position Code is invalid."));
            return errors;
        }

        if (!CodeUtils.validateSyntax(doc.getString(Field.frmlaEmplyEligText))) {
            errors.add(new Error("$frmlaEmplyEligText", "frmlaEmplyEligText@invalid",
                    "Formula Employee Eligibility Text is invalid."));
            return errors;
        }
        if (!CodeUtils.validateSyntax(doc.getString(Field.frmlaEmpEligBfrCtoffText))) {
            errors.add(new Error("$frmlaEmpEligBfrCtoffText", "frmlaEmpEligBfrCtoffText@invalid",
                    "Formula Employee Eligibility Before Cutoff Text is invalid."));
            return errors;
        }

        verifyAddAction(actionCde, amendment, employPosnCde, prodTypeCde, prodSubtpCde, errors);

        return errors;
    }

    private boolean isValidCode(String ctryRecCde, String grpMembrRecCde, String cdvTypeCde, String cdvCde) {
        if (StringUtils.isBlank(cdvCde)) return true;
        Bson filter = and(
                eq(Field.ctryRecCde, ctryRecCde),
                eq(Field.grpMembrRecCde, grpMembrRecCde),
                eq(Field.cdvTypeCde, cdvTypeCde),
                eq(Field.cdvCde, cdvCde)
        );
        List<Document> documents = referenceDataService.getReferDataByFilter(filter);
        return !documents.isEmpty();
    }

    private void verifyAddAction(ActionCde actionCde, Document amendment, String employPosnCde, String prodTypeCde,
                                 String prodSubtpCde, List<Error> errors) {
        if (ActionCde.update == actionCde) return;
        List<Bson> filterList = Lists.newArrayList(
                    eq(Field.employPosnCde, employPosnCde),
                    eq(Field.ctryRecCde, amendment.getString(Field.ctryRecCde)),
                    eq(Field.grpMembrRecCde, amendment.getString(Field.grpMembrRecCde))
            );
            filterList.add(StringUtils.isNotBlank(prodSubtpCde) ? eq(Field.prodSubtpCde, prodSubtpCde) :
                    eq(Field.prodTypeCde, prodTypeCde));

            if (colStaffLicenseCheckData.countDocuments(and(filterList)) > 0) {
                String message = StringUtils.isNotBlank(prodSubtpCde) ?
                        String.format("StaffLicenseCheck data prodSubtpCde=%s, employPosnCde=%s already exists", prodSubtpCde, employPosnCde) :
                        String.format("StaffLicenseCheck data prodTypeCde=%s, employPosnCde=%s already exists", prodTypeCde, employPosnCde);
                errors.add(new Error("$", "@duplicate", message));
            }
    }

    @Override
    public void add(Document doc) {
        Date date = new Date();
        doc.put(Field._id, sequenceService.nextId(Sequence.staffLicenseCheckId));
        doc.put(Field.revision, 1L);
        doc.put(Field.recCreatDtTm, date);
        doc.put(Field.recUpdtDtTm, date);
        doc.putIfAbsent(Field.createdBy, "wps");
        colStaffLicenseCheckData.insertOne(doc);
    }

    @Override
    public void update(Document doc) {
        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document oldDoc = colStaffLicenseCheckData.find(filter).first();
        if (null == oldDoc) {
            throw new productErrorException(productErrors.RuntimeException, "ReferenceData document not found, id=" + id);
        }

        RevisionUtils.setRevisionNumber(oldDoc, doc);

        doc.put(Field.recCreatDtTm, oldDoc.getDate(Field.recCreatDtTm));
        doc.put(Field.recUpdtDtTm, new Date());
        doc.putIfAbsent(Field.lastUpdatedBy, "wps");
        colStaffLicenseCheckData.replaceOne(filter, doc);
    }

    @Override
    public void delete(Document doc) {
        colStaffLicenseCheckData.deleteOne(eq(Field._id, doc.get(Field._id)));
    }

    @Override
    public Document findFirst(Bson filter) {
        return colStaffLicenseCheckData.find(filter).first();
    }
}
