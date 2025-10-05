package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ReferenceDataDocumentValidator;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

@Service
public class ReferenceDataChangeService implements ChangeService {
    private MongoCollection<Document> colReferenceData;
    @Autowired private DocumentRevisionService documentRevisionService;
    @Autowired private SequenceService sequenceService;
    private ReferenceDataDocumentValidator validator;

    public ReferenceDataChangeService(MongoDatabase mongoDatabase) {
        this.colReferenceData = mongoDatabase.getCollection(CollectionName.reference_data.toString());
        validator = new ReferenceDataDocumentValidator();
    }

    @Override
    public List<Error> validate(Document amendment) {
        String cde = amendment.getString(Field.actionCde);
        ActionCde actionCde = ActionCde.valueOf(cde);
        List<Error> errors = new ArrayList<>();
        Document doc = (Document) amendment.get(Field.doc);
        if (actionCde == ActionCde.add) {
            errors.addAll(validator.validateCreate(doc));
        } else if (actionCde == ActionCde.update) {
            errors.addAll(validator.validateUpdate(doc));
        } else if (actionCde == ActionCde.delete) {
            errors.addAll(validateDelete(amendment));
        }
        return errors;
    }

    /**
     * Just make sure the _id and revision fields exists
     *
     * @param amendment
     * @return
     */
    private List<Error> validateDelete(Document amendment) {
        Document doc = (Document) amendment.get(Field.doc);
        List<Error> errors = new ArrayList<>();
        if (null == doc.get(Field._id)) {
            errors.add(new Error(Field._id, "_id@required", "required"));
        }
        if (null == doc.get(Field.revision)) {
            errors.add(new Error(Field.revision, "revision@required", "required"));
        }

        //if docBase and docLatest are not the same, give another error
        Document docLatest = colReferenceData.find(eq(Field._id,doc.get(Field._id))).first();
        if (!Objects.equals(DocumentUtils.getLong(doc, Field.revision), DocumentUtils.getLong(docLatest, Field.revision))){
            errors.add(new Error(Field.doc, "doc@notSame", "docBase and docLatest are not the same"));
        }
        return errors;
    }

    /**
     * add a new referenceData document and insert into the db
     *
     * @param doc
     */
    @Override
    public void add(Document doc) {
        Date now = new Date();
        doc.put(Field._id, sequenceService.nextId(Sequence.referenceDataId));
        doc.put(Field.revision, 1L);
        doc.put(Field.recCreatDtTm, now);
        doc.put(Field.recUpdtDtTm, now);
        doc.put(Field.createdBy, "wps");
        colReferenceData.insertOne(doc);
    }

    /**
     * Update a referenceData document, the old referenceData document will be copy to document_revision collection.
     *
     * @param doc
     */
    @Override
    public void update(Document doc) {
        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document oldDoc = colReferenceData.find(filter).first();
        if (null == oldDoc) {
            throw new productErrorException(productErrors.RuntimeException, "ReferenceData document not found, id=" + id);
        }

        documentRevisionService.saveDocumentRevision(DocType.reference_data, oldDoc);

        RevisionUtils.setRevisionNumber(oldDoc, doc);

        doc.put(Field.recCreatDtTm, oldDoc.getDate(Field.recCreatDtTm));
        doc.put(Field.recUpdtDtTm, new Date());
        doc.put(Field.lastUpdatedBy, "wps");
        colReferenceData.replaceOne(filter, doc);
    }

    @Override
    public void delete(Document doc) {
       colReferenceData.deleteOne(eq(Field._id, doc.get(Field._id)));
    }

    @Override
    public Document findFirst(Bson filter) {
        return colReferenceData.find(filter).first();
    }
}
