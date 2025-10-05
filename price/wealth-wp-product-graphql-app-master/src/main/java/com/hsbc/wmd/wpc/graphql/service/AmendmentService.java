package com.dummy.wmd.wpc.graphql.service;

import com.google.common.collect.ImmutableMap;
import com.dummy.wmd.wpc.graphql.annotation.RetryableTransactional;
import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.AmendmentResolver;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.DeleteResult;
import graphql.com.google.common.collect.Lists;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

@SuppressWarnings({"java:S1172", "java:S1168", "java:S1068", "java:S1144"})
@Service
public class AmendmentService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private ProductChangeService productChangeService;
    @Autowired
    private ReferenceDataChangeService referenceDataChangeService;
    @Autowired
    private AssetVolatilityClassCharChangeService assetVolatilityClassCharChangeService;
    @Autowired
    private AssetVolatilityClassCorlChangeService assetVolatilityClassCorlChangeService;
    @Autowired
    private CustomerEligibilityChangeService customerEligibilityChangeService;
    @Autowired
    private StaffEligibilityChangeService staffEligibilityChangeService;
    @Autowired
    private StaffLicenseCheckChangeService staffLicenseCheckChangeService;
    @Autowired
    private ProductRelationChangeService productRelationChangeService;
    @Autowired
    private FinDocHistChangeService finDocHistChangeService;
    @Autowired
    private ChanlRelatedFieldsChangeService chanlRelatedFieldsChangeService;

    /**
     * Get the latest document with given docType and docId
     *
     * @param docType
     * @param docId
     * @return
     */
    public Document getLatestDocument(DocType docType, Object docId) {
        ChangeService changeService = getChangeService(docType);
        return changeService.findFirst(eq(Field._id, docId));
    }

    public Document getLatestDocument(DocType docType, Object docId, Document docChange) {
        ChangeService changeService = getChangeService(docType);
        return changeService.findFirst(eq(Field._id, docId), docChange);
    }

    private String getCollectionName(DocType docType) {
        return docType.toString();
    }

    /**
     * Calculate conflict between the amendment and the latest document
     *
     * @param docType
     * @param docId
     * @param docRevision
     * @return
     */
    public boolean hasConflict(String docType, long docId, long docRevision) {
        //implement me, or return the conflict list directly?
        return false;
    }

    /**
     * Update amendment doc with given id, only amendment in draft status will be updated
     * <p>
     * draft --> pending
     * pending --> reject
     * pending --> approved
     *
     * @param amendment
     * @return
     */
    public Document updateAmendment(Document amendment) {
        getAmendmentCollection().replaceOne(eq(Field._id, amendment.get(Field._id)), amendment);
        return amendment;
    }

    /**
     * Create a new amendment document and insert into the db
     *
     * @param amendment
     * @return
     */
    public Document createAmendment(Document amendment) {
        Date now = new Date();
        amendment.put(Field._id, sequenceService.nextId(Sequence.amendmentId));
        amendment.put(Field.recCreatDtTm, now);
        amendment.put(Field.recUpdtDtTm, now);
        getAmendmentCollection().insertOne(amendment);
        return amendment;
    }

    private static FindOneAndReplaceOptions findOneAndReplaceOptions = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

    public Document amendmentById(long amendmentId) {
        return findFirst(eq(Field._id, amendmentId));
    }

    /**
     * Approve or Reject an amendment
     *
     * @param id
     * @param action  ApprovalAction
     * @param comment
     * @return
     */
    @RetryableTransactional
    public Document approveAmendment(String approvedBy, long id, ApprovalAction action, String comment) {
        Document amdmDoc = findFirst(eq(Field._id, id));
        getAmendmentCollection().updateOne(eq(Field._id, id), Updates.set(Field.recUpdtDtTm, new Date()));
        if (null == amdmDoc) return null;

        if (StringUtils.hasText(approvedBy) && approvedBy.equals(amdmDoc.getString(Field.emplyNum)) && action != ApprovalAction.returned) {
            throw new productErrorException(productErrors.ApproveByCreator, "Amendment is not allow to be approved by creator");
        }

        /*
         * pending --> reject
         * pending --> approved
         */
        RecStatCde recStatCde = RecStatCde.valueOf(amdmDoc.getString(Field.recStatCde));
        if (RecStatCde.pending != recStatCde) {
            throw new productErrorException(productErrors.DocumentStatusError, "Amendment must be in 'pending' status before approve / reject, recStatCde=" + recStatCde);
        }

        // apply the change to target collection
        if (action.equals(ApprovalAction.approved)) {
            validateDocument(amdmDoc);
            applyChange(amdmDoc);
        } else if (action.equals(ApprovalAction.rejected)) {
            rejectOp(amdmDoc);
        }

        amdmDoc.put(Field.approvedBy, approvedBy);
        amdmDoc.put(Field.recStatCde, action.toString());
        amdmDoc.put(Field.approvalComment, comment);
        amdmDoc.put(Field.recUpdtDtTm, new Date());
        amdmDoc = getAmendmentCollection().findOneAndReplace(eq(Field._id, amdmDoc.get(Field._id)), amdmDoc, findOneAndReplaceOptions);

        return amdmDoc;
    }

    private void rejectOp(Document amdmDoc) {
        DocType docType = DocType.valueOf(amdmDoc.getString(Field.docType));
        if (DocType.fin_doc_hist == docType) {
            finDocHistChangeService.rejectOpation(amdmDoc);
        }
    }

    private void applyChange(Document amdmDoc) {
        DocType docType = DocType.valueOf(amdmDoc.getString(Field.docType));
        Long docId = DocumentUtils.getLong(amdmDoc, Field.docId);
        ActionCde actionCde = ActionCde.valueOf(amdmDoc.getString(Field.actionCde));
        Document docChanged = DocumentUtils.compactDocument((Document) amdmDoc.get(Field.doc));
        ChangeService changeService = getChangeService(docType);

        if (actionCde == ActionCde.add) {
            // add, create the product
            changeService.add(docChanged);
        } else if (actionCde == ActionCde.update) {
            // update, update the product
            Document docBase = (Document) amdmDoc.get(Field.docBase);
            Document docLatest = getLatestDocument(docType, docId, docChanged);
            if (null == docLatest) {
                String message = String.format("Latest document not found, docType=%s, id=%s", docType, docId);
                throw new productErrorException(productErrors.DocumentNotFound, message);
            }
            AmendmentResolver resolver = new AmendmentResolver(docBase, docChanged, docLatest);
            Document docResolved = resolver.resolve();
            changeService.update(docResolved);
        } else if (actionCde == ActionCde.delete) {
            // delete, update the product with new status
            changeService.delete(docChanged);
        }
    }

    /**
     * Validate the amendment, through exception if the docChanged is not valid
     *
     * @param amdm
     */
    public void validateDocument(Document amdm) {
        DocType docType = DocType.valueOf(amdm.getString(Field.docType));
        ChangeService changeService = getChangeService(docType);
        List<Error> errors = changeService.validate(amdm);
        if (null != errors && !errors.isEmpty()) {
            Map<String, Object> extensions = new ImmutableMap.Builder<String, Object>()
                    .put(productErrorException.product_ERROR_CODE, productErrors.ValidationError)
                    .put("validationErrors", errors)
                    .build();
            throw new productErrorException(extensions, "Document validation error");
        }
    }

    /**
     * For new add document check if the document exists, through exception in case the new document
     * 1. lack of any key fields
     * 2. in the documents
     * 3. in the amendments
     *
     * @param docType
     * @param doc
     */
    public void validateCreateDocument(DocType docType, Document doc) {
        String ctryRecCde = doc.getString(Field.ctryRecCde);
        String grpMembrRecCde = doc.getString(Field.grpMembrRecCde);
        if(DocType.product.equals(docType)){
            String prodTypeCde = doc.getString(Field.prodTypeCde);
            String prodAltPrimNum = doc.getString(Field.prodAltPrimNum);
            validateProductExistence(ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
        }else if(DocType.reference_data.equals(docType)){
            String cdvTypeCde = doc.getString(Field.cdvTypeCde);
            String cdvCde = doc.getString(Field.cdvCde);
            validateRefDataExistence(ctryRecCde, grpMembrRecCde, cdvTypeCde, cdvCde);
        }
    }

    private void validateRefDataExistence(String ctryRecCde, String grpMembrRecCde, String cdvTypeCde, String cdvCde) {
        if (!StringUtils.hasText(ctryRecCde)
                || !StringUtils.hasText(grpMembrRecCde)
                || !StringUtils.hasText(cdvTypeCde)
                || !StringUtils.hasText(cdvCde)) {
            throw new productErrorException(productErrors.ValidationError, "None of following fields should be empty: ctryRecCde, grpMembrRecCde, cdvTypeCde, cdvCde");
        }
        ChangeService changeService = getChangeService(DocType.reference_data);
        Bson filter = and(
                eq("doc.ctryRecCde", ctryRecCde),
                eq("doc.grpMembrRecCde", grpMembrRecCde),
                eq("doc.cdvTypeCde", cdvTypeCde),
                eq("doc.cdvCde", cdvCde),
                nin(Field.recStatCde, RecStatCde.rejected.toString(), RecStatCde.approved.toString()));
        Document doc = findFirst(filter);
        if (null != doc) {
            String message = String.format("Amendment of reference data with the same code already exist: ID=%s, %s/%s", doc.get(Field._id), cdvTypeCde, cdvCde);
            throw new productErrorException(productErrors.DocumentDuplicated, message);
        }

        /*
         * Given "prodStatCde" : "A", the combination below should be unique:
         * ctryRecCde + grpMembrRecCde + prodTypeCde + prodAltPrimNum
         */
        filter = and(
                eq(Field.ctryRecCde, ctryRecCde),
                eq(Field.grpMembrRecCde, grpMembrRecCde),
                eq(Field.cdvTypeCde, cdvTypeCde),
                eq(Field.cdvCde, cdvCde));
        doc = changeService.findFirst(filter);
        if (null != doc) {
            String message = String.format("Reference data with the same code already exist: ID=%s, %s/%s", doc.get(Field._id), cdvTypeCde, cdvCde);
            throw new productErrorException(productErrors.DocumentDuplicated, message);
        }
    }

    private void validateProductExistence(String ctryRecCde, String grpMembrRecCde, String prodTypeCde, String prodAltPrimNum) {
        if (!StringUtils.hasText(ctryRecCde)
                || !StringUtils.hasText(grpMembrRecCde)
                || !StringUtils.hasText(prodTypeCde)
                || !StringUtils.hasText(prodAltPrimNum)) {
            throw new productErrorException(productErrors.ValidationError, "None of following fields should be empty: ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum");
        }
        ChangeService changeService = getChangeService(DocType.product);
        Bson filter = and(
                eq("doc.ctryRecCde", ctryRecCde),
                eq("doc.grpMembrRecCde", grpMembrRecCde),
                eq("doc.prodTypeCde", prodTypeCde),
                eq("doc.prodAltPrimNum", prodAltPrimNum),
                nin(Field.recStatCde, RecStatCde.rejected.toString(), RecStatCde.approved.toString(), RecStatCde.deleted.toString()));
        Document doc = findFirst(filter);
        if (null != doc) {
            String message = String.format("Amendment of product with the same code already exist: ID=%s, %s/%s", doc.get(Field._id), prodTypeCde, prodAltPrimNum);
            throw new productErrorException(productErrors.DocumentDuplicated, message);
        }

        /*
         * Given "prodStatCde" : "A", the combination below should be unique:
         * ctryRecCde + grpMembrRecCde + prodTypeCde + prodAltPrimNum
         */
        filter = and(
                eq(Field.ctryRecCde, ctryRecCde),
                eq(Field.grpMembrRecCde, grpMembrRecCde),
                eq(Field.prodTypeCde, prodTypeCde),
                eq(Field.prodAltPrimNum, prodAltPrimNum),
                eq(Field.prodStatCde, "A"));
        doc = changeService.findFirst(filter);
        if (null != doc) {
            String message = String.format("Product with the same code already exist: ID=%s, %s/%s", doc.get(Field._id), prodTypeCde, prodAltPrimNum);
            throw new productErrorException(productErrors.DocumentDuplicated, message);
        }
    }

    public Document requestApproval(long id, String comment) {
        Document amdm = findFirst(eq(Field._id, id));
        if (null == amdm) {
            throw new productErrorException(productErrors.DocumentNotFound, "Amendment not found: id=" + id);
        }

        RecStatCde recStatCde = RecStatCde.valueOf(amdm.getString(Field.recStatCde));
        if (RecStatCde.draft != recStatCde) {
            throw new productErrorException(productErrors.DocumentStatusError, "Amendment must be in 'draft' status before request approval, recStatCde=" + recStatCde);
        }

        validateDocument(amdm);

        amdm.put(Field.recStatCde, "pending");
        amdm.put(Field.requestComment, comment);
        amdm.put(Field.recUpdtDtTm, new Date());
        amdm = getAmendmentCollection().findOneAndReplace(eq(Field._id, amdm.get(Field._id)), amdm, findOneAndReplaceOptions);

        return amdm;
    }

    public Document getAmendmentById(Long id) {
        return findFirst(eq(Field._id, id));
    }

    /**
     * Document with recStatCde in [draft, pending, returned] is ongoing amendment
     * Excludes DELETE amendment (actionCde == 'delete') with status in [returned]
     *
     * @param docType
     * @param docId
     * @return
     */
    public boolean hasOngoingAmendment(Document docChanged, DocType docType, Long docId) {

        List<Bson> filterValid = Lists.newArrayList(
                eq(Field.docType, docType.toString()),
                eq(Field.docId, docId),
                in(Field.recStatCde,
                        RecStatCde.draft.toString(),
                        RecStatCde.pending.toString()));

        List<Bson> filtReturned = Lists.newArrayList(
                eq(Field.docType, docType.toString()),
                eq(Field.docId, docId),
                eq(Field.recStatCde, RecStatCde.returned.toString()),
                ne(Field.actionCde, ActionCde.delete.toString()));

        if (docType.equals(DocType.product_staff_eligibility)) {
            try {
                String employPosnCde = JsonPath.read(docChanged, "stafLicCheck.employPosnCde");
                filterValid.add(eq("doc.stafLicCheck.employPosnCde", employPosnCde));
                filtReturned.add(eq("doc.stafLicCheck.employPosnCde", employPosnCde));
            } catch (JsonPathException e) {
                // just ignore
            }
        }

        Bson filter = or(and(filterValid), and(filtReturned));
        long count = getAmendmentCollection().countDocuments(filter);
        return count > 0;
    }

    public long deleteAmendmentById(Long amendmentId) {
        DeleteResult result = getAmendmentCollection().deleteOne(eq(Field._id, amendmentId));
        return result.getDeletedCount();
    }

    public Document findFirst(Bson filter) {
        return getAmendmentCollection().find(filter).first();
    }

    private MongoCollection<Document> getAmendmentCollection() {
        return mongoTemplate.getCollection(CollectionName.amendment.name());
    }

    private ChangeService getChangeService(DocType docType) {
        if (DocType.product == docType) {
            return productChangeService;
        } else if (DocType.reference_data == docType) {
            return referenceDataChangeService;
        } else if (DocType.aset_voltl_class_char == docType) {
            return assetVolatilityClassCharChangeService;
        } else if (DocType.aset_voltl_class_corl == docType) {
            return assetVolatilityClassCorlChangeService;
        } else if (DocType.product_customer_eligibility == docType) {
            return customerEligibilityChangeService;
        } else if (DocType.product_staff_eligibility == docType) {
            return staffEligibilityChangeService;
        } else if (DocType.staff_license_check == docType) {
            return staffLicenseCheckChangeService;
        } else if (DocType.product_prod_relation == docType) {
            return productRelationChangeService;
        } else if (DocType.fin_doc_hist == docType) {
            return finDocHistChangeService;
        } else if (DocType.chanl_related_fileds == docType) {
            return chanlRelatedFieldsChangeService;
        } else {
            throw new productErrorException(productErrors.NotSupportDocType, "Not support docType: " + docType);
        }
    }
}
