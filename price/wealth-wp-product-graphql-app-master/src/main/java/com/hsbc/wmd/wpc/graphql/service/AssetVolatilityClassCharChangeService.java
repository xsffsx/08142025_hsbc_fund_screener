package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.jayway.jsonpath.JsonPath;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

@SuppressWarnings("java:S3740")
@Service
public class AssetVolatilityClassCharChangeService extends AssetVolatilityClassBaseChangeService {
    private MongoCollection<Document> colAsetVoltlClass;
    @Autowired private DocumentRevisionService documentRevisionService;
    @Autowired private SequenceService sequenceService;
    @Autowired private ReferenceDataService referenceDataService;

    public AssetVolatilityClassCharChangeService(MongoDatabase mongoDatabase) {
        this.colAsetVoltlClass = mongoDatabase.getCollection(CollectionName.aset_voltl_class_char.toString());
    }

    @Override
    public List<Error> validate(Document amendment) {
        String cde = amendment.getString(Field.actionCde);
        ActionCde actionCde = ActionCde.valueOf(cde);
        List<Error> errors = new ArrayList<>();
        if (actionCde == ActionCde.update) {
            errors.addAll(validateDocument((Document)amendment.get(Field.doc)));
        } else {
            String message = "Action code is not support with asset volatility class char: " + actionCde;
            throw new productErrorException(productErrors.NotSupportAction, message);
        }
        return errors;
    }

    /**
     * 1. list[*].asetVoltlClassCde expected to be unique in the list
     *
     * @param doc
     * @return
     */
    public List<Error> validateDocument(Document doc) {
        List<Error> errors = new ArrayList<>();
        // check asetVoltlClassCde uniqueness
        String jsonPath = "$.list[*].asetVoltlClassCde";
        List codeList = JsonPath.read(doc, jsonPath);
        Set codeSet = new HashSet(codeList);
        if(codeList.size() != codeSet.size()) {
            errors.add(new Error("list", "list@unique", "The value [asetVoltlClassCde] in the list has to be unique"));
        }

        // check reference data
        Bson filter = and(
                eq("ctryRecCde", doc.get(Field.ctryRecCde)),
                eq("grpMembrRecCde", doc.get(Field.grpMembrRecCde)),
                eq("cdvTypeCde", "ASETVOLCLSCDE")
        );
        List<Document> referDatas = referenceDataService.getReferDataByFilter(filter);
        List<String> asetVoltlClassCdes = new ArrayList<>();
        referDatas.forEach(document -> asetVoltlClassCdes.add((String) document.get(Field.cdvCde)));
        codeList.forEach(asetVoltlClassCde -> {
            if (!asetVoltlClassCdes.contains(asetVoltlClassCde)) {
                errors.add(new Error("list", "list[*].asetVoltlClassCde@referenceData", String.format("Reference data doesn't exist: {cdvTypeCde=ASETVOLCLSCDE, cdvCde=%s}", asetVoltlClassCde)));
            }
        });
        return errors;
    }

    /**
     * add a new asset volatility class document and insert into the db
     *
     * @param doc
     */
    @Override
    public void add(Document doc) {
        String message = "Action code is not support with asset volatility class char: add";
        throw new productErrorException(productErrors.NotSupportAction, message);
    }

    /**
     * Update a asset volatility class document, the old asset volatility class document will be copy to document_revision collection.
     *
     * @param doc
     */
    @Override
    public void update(Document doc) {
        updateDocument(doc);
    }

    public Document updateDocument(Document doc) {
        fillListRowid(doc.getList("list", Map.class));

        Object id = doc.get(Field._id);
        Bson filter = eq(Field._id, id);
        Document oldDoc = colAsetVoltlClass.find(filter).first();
        if (null == oldDoc) {
            throw new productErrorException(productErrors.RuntimeException, "asset volatility class char document not found, id=" + id);
        }

        documentRevisionService.saveDocumentRevision(DocType.aset_voltl_class_char, oldDoc);

        RevisionUtils.setRevisionNumber(oldDoc, doc);

        doc.put(Field.recCreatDtTm, oldDoc.getDate(Field.recCreatDtTm));
        doc.put(Field.recUpdtDtTm, new Date());
        colAsetVoltlClass.replaceOne(filter, doc);
        return doc;
    }

    public Document addDocument(Document doc) {
        fillListRowid(doc.getList("list", Map.class));

        // check if record exists by country code + group member
        String ctryRecCde = doc.getString(Field.ctryRecCde);
        String grpMembrRecCde = doc.getString(Field.grpMembrRecCde);
        long count = colAsetVoltlClass.countDocuments(and(eq(Field.ctryRecCde, ctryRecCde), eq(Field.grpMembrRecCde, grpMembrRecCde)));
        if(count > 0) {
            String message = String.format("AssetVolatilityClassChar document for entity %s%s already exist", ctryRecCde, grpMembrRecCde);
            throw new productErrorException(productErrors.DocumentDuplicated, message);
        }
        Date now = new Date();
        doc.put(Field._id, sequenceService.nextId(Sequence.aset_voltl_class_char_id));
        doc.put(Field.recCreatDtTm, now);
        doc.put(Field.recUpdtDtTm, now);
        doc.put(Field.revision, 1L);
        colAsetVoltlClass.insertOne(doc);
        return doc;
    }

    @Override
    public void delete(Document doc) {
        String message = "Action code is not support with asset volatility class char: delete";
        throw new productErrorException(productErrors.NotSupportAction, message);
    }

    @Override
    public Document findFirst(Bson filter) {
        return colAsetVoltlClass.find(filter).first();
    }
}
