package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.CodeUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

@SuppressWarnings("java:S3740")
@Service
public class StaffEligibilityChangeService implements ChangeService {
    private MongoCollection<Document> colProductData;

    private static final List<String> fieldList = Arrays.asList("ctryRecCde", "grpMembrRecCde", "prodName", "prodSubtpCde", "prodTypeCde", "prodAltPrimNum", "revision",
            Field.stafLicCheck, "recCreatDtTm", "recUpdtDtTm");

    public StaffEligibilityChangeService(MongoDatabase mongoDatabase) {
        this.colProductData = mongoDatabase.getCollection(CollectionName.product.toString());
    }

    @Override
    public List<Error> validate(Document amendment) {
        List<Error> errors = new ArrayList<>();
        Document doc = (Document) amendment.get(Field.doc);

        String cde = amendment.getString(Field.actionCde);
        ActionCde actionCde = ActionCde.valueOf(cde);

        Map stafLicCheck = (Map) doc.get(Field.stafLicCheck);

        String employPosnCde = (String) stafLicCheck.get(Field.employPosnCde);
        if (StringUtils.isBlank(employPosnCde)) {
            errors.add(new Error("$", "@null", "employPosnCde in stafLicCheck is null or blank"));
            return errors;
        }

        if (!CodeUtils.validateSyntax((String) stafLicCheck.get(Field.frmlaEmpEligBfrCtoffText))) {
            errors.add(new Error("$frmlaEmpEligBfrCtoffText", "frmlaEmpEligBfrCtoffText@invalid",
                    "Formula Employee Eligibility Before Cutoff Text is invalid."));
            return errors;
        }
        if (!CodeUtils.validateSyntax((String) stafLicCheck.get(Field.frmlaEmplyEligText))) {
            errors.add(new Error("$frmlaEmplyEligText", "frmlaEmplyEligText@invalid",
                    "Formula Employee Eligibility Text is invalid."));
            return errors;
        }


        Document docBase = actionCde.equals(ActionCde.add) ? this.findFirst(eq(Field._id, doc.get(Field._id))) : (Document) amendment.getOrDefault(Field.docBase, Collections.emptyMap());

        if (actionCde.equals(ActionCde.add)) {
            List<Map<String, Object>> stafLicCheckList = (List) docBase.getOrDefault(Field.stafLicCheck, Collections.emptyList());
            if (stafLicCheckList.stream().anyMatch(item -> item.get(Field.employPosnCde).equals(employPosnCde))) {
                errors.add(new Error("$", "@duplicate", String.format("StafLicCheck data with employPosnCde = %s is repeated", employPosnCde)));
            }

        } else if (actionCde.equals(ActionCde.update)) {
            Map stafLicCheckDB = (Map) docBase.getOrDefault(Field.stafLicCheck, Collections.emptyMap());
            if (stafLicCheckDB.isEmpty()) {
                errors.add(new Error("$", "@notFound", String.format("StafLicCheck data with employPosnCde = %s is not found", employPosnCde)));
            }
        }

        return errors;
    }

    @Override
    public void add(Document doc) {
        updateProductStaff(doc, true);
    }

    @Override
    public void update(Document doc) {
        updateProductStaff(doc, false);
    }

    private void updateProductStaff(Document doc, boolean isAdd) {
        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document dbDoc = colProductData.find(filter).first();

        if (null == dbDoc) {
            throw new productErrorException(productErrors.RuntimeException, "Product record not found, id=" + id);
        }

        RevisionUtils.increaseRevisionNumber(dbDoc);
        dbDoc.put(Field.recUpdtDtTm, new Date());

        List<Map> stafLicCheckList = (List) dbDoc.getOrDefault(Field.stafLicCheck, new ArrayList<>());
        Map stafLicCheck = (Map) doc.get(Field.stafLicCheck);
        stafLicCheck.putIfAbsent(Field.recCreatDtTm, new Date());
        stafLicCheck.put(Field.recUpdtDtTm, new Date());

        if (isAdd) {
            stafLicCheckList.add(stafLicCheck);
        } else {
            stafLicCheckList.replaceAll(item -> item.get(Field.employPosnCde).equals(stafLicCheck.get(Field.employPosnCde)) ? stafLicCheck : item);
        }

        dbDoc.put(Field.stafLicCheck, stafLicCheckList);
        colProductData.updateOne(filter, new Document("$set", dbDoc));
    }

    @Override
    public void delete(Document doc) {
        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document dbDoc = colProductData.find(filter).first();

        RevisionUtils.increaseRevisionNumber(dbDoc);
        dbDoc.put(Field.recUpdtDtTm, new Date());

        Map stafLicCheck = (Map) doc.get(Field.stafLicCheck);
        List<Map> stafLicCheckList = (List) dbDoc.get(Field.stafLicCheck);
        stafLicCheckList.removeIf(item -> item.get(Field.employPosnCde).equals(stafLicCheck.get(Field.employPosnCde)));

        colProductData.updateOne(filter, new Document("$set", dbDoc));
    }

    @Override
    public Document findFirst(Bson filter) {
        return this.findFirst(filter, new Document());
    }

    @Override
    public Document findFirst(Bson filter, Document docChange) {
        Bson includes = include(fieldList);
        Document document = colProductData.find(filter).projection(includes).first();

        if (null != docChange && docChange.containsKey(Field.stafLicCheck)) {
            List<Map> stafLicCheckList = (List) document.getOrDefault(Field.stafLicCheck, Collections.emptyList());
            Map<String, Object> stafLicCheck = (Map) docChange.get(Field.stafLicCheck);
            Map records = stafLicCheckList.stream().filter(item -> item.get(Field.employPosnCde).equals(stafLicCheck.get(Field.employPosnCde))).findFirst().orElse(Collections.emptyMap());

            document.put(Field.stafLicCheck, records);
        }

        return document;
    }
}
