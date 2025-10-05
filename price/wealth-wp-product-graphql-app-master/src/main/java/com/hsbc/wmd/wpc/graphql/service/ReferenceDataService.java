package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.*;
import com.dummy.wmd.wpc.graphql.model.*;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.dummy.wmd.wpc.graphql.utils.JsonPathUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ReferenceDataDocumentValidator;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Service
public class ReferenceDataService {

    private static final FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE).upsert(true);
    private static final String BATCH = "batch";
    private MongoCollection<Document> colReferenceData;
    @Autowired
    private DocumentRevisionService documentRevisionService;
    @Autowired
    private SequenceService sequenceService;

    public ReferenceDataService(MongoDatabase mongoDatabase) {
        this.colReferenceData = mongoDatabase.getCollection(CollectionName.reference_data.toString());
    }

    @Autowired
    MongoTemplate mongoTemplate;

    public ReferenceDataBatchCreateResult batchCreate(List<Map<String, Object>> referenceDataList, boolean allowPartial) {
        List<Map<String, Object>> validReferDataList = new ArrayList<>();
        List<ReferDataValidationResult> invalidReferenceDataList = new ArrayList<>();
        ReferenceDataDocumentValidator validator = new ReferenceDataDocumentValidator();

        for (Map<String, Object> referData : referenceDataList) {
            List<Error> errors = validator.validateCreate(new Document(referData));
            if (!errors.isEmpty()) {
                invalidReferenceDataList.add(new ReferDataValidationResult(referData, errors));
            } else {
                validReferDataList.add(referData);
            }
        }

        ReferenceDataBatchCreateResult result = new ReferenceDataBatchCreateResult();

        List<Map<String, Object>> createdReferenceDataList = new ArrayList<>();
        if (!validReferDataList.isEmpty() && (referenceDataList.size() == validReferDataList.size() || allowPartial)) {
            ReferenceDataBatchCreateResult createResult = createReferenceData(validReferDataList);
            createdReferenceDataList.addAll(createResult.getCreatedReferenceData());
            invalidReferenceDataList.addAll(createResult.getInvalidReferenceData());
        }

        result.setCreatedReferenceData(createdReferenceDataList);
        result.setInvalidReferenceData(invalidReferenceDataList);
        return result;
    }

    public ReferenceDataBatchCreateResult createReferenceData(List<Map<String, Object>> referenceDataList) {
        List<Document> docList = new LinkedList<>();
        referenceDataList.forEach(referenceData -> {
            long referDataId = sequenceService.nextId(Sequence.referenceDataId);
            referenceData.put(Field._id, referDataId);
            referenceData.put(Field.recCreatDtTm, new Date());
            referenceData.put(Field.recUpdtDtTm, new Date());
            referenceData.put(Field.chanlComnCde, Collections.emptyList());
            referenceData.put(Field.revision, 1L);
            referenceData.put(Field.createdBy, BATCH);
            Document doc = new Document(referenceData);
            docList.add(doc);
        });

        ReferenceDataBatchCreateResult createResult = new ReferenceDataBatchCreateResult();
        try {
            // There is no need to stop when encountering a failed record
            colReferenceData.insertMany(docList, new InsertManyOptions().ordered(false));
            createResult.setCreatedReferenceData(referenceDataList);
        } catch (MongoBulkWriteException e) {
            List<Map<String, Object>> copyReferenceDataList = new LinkedList<>(referenceDataList);

            List<Map<String, Object>> createdReferenceDataList = e.getWriteResult()
                    .getInserts()
                    .stream()
                    .map(insert -> copyReferenceDataList.get(insert.getIndex()))
                    .collect(Collectors.toList());
            createResult.setCreatedReferenceData(createdReferenceDataList);

            copyReferenceDataList.removeAll(createdReferenceDataList);

            BulkWriteError writeError = e.getWriteErrors().get(0);

            String code;
            String message;
            if (writeError.getCode() == MongoDbErrorCodes.DUPLICATE_KEY) {
                code = "@duplicate";
                message = "Reference data cdvTypeCde / cdvCde already exists";
            } else {
                code = "@mongodbException";
                message = e.getMessage();
            }
            List<ReferDataValidationResult> invalidReferenceDataList = copyReferenceDataList.stream()
                    .map(referenceData -> new ReferDataValidationResult(referenceData, Collections.singletonList(new Error("$", code, message))))
                    .collect(Collectors.toList());
            createResult.setInvalidReferenceData(invalidReferenceDataList);
        }

        return createResult;
    }

    public ReferenceDataBatchDeleteResult batchDelete(List<Map<String, Object>> filters) {
        ReferenceDataBatchDeleteResult result = new ReferenceDataBatchDeleteResult();
        result.setDeletedReferenceData(new ArrayList<>());
        result.setInvalidReferenceData(new ArrayList<>());

        for (Map<String, Object> filter : filters) {
            Document criteria = new Document(filter);
            Document ref = colReferenceData.findOneAndDelete(criteria);
            if (null != ref) {
                result.setDeletedCount(result.getDeletedCount() + 1);
                result.getDeletedReferenceData().add(ref);
            } else {
                result.getInvalidReferenceData().add(filter);
            }

        }
        return result;
    }

    public ReferenceDataBatchUpdateResult batchUpdate(BsonDocument filter, List<OperationInput> operations, boolean allowPartial) {
        List<Document> referDataList = getReferDataByFilter(filter);
        List<Document> matchReferenceData = DocumentUtils.clone(referDataList);
        referDataList.forEach(referData -> JsonPathUtils.applyChanges(referData, operations));

        List<Document> validReferDataList = new ArrayList<>();
        List<ReferDataValidationResult> invalidReferDataList = new ArrayList<>();

        referDataList.forEach(referData -> {
            ReferenceDataDocumentValidator validator = new ReferenceDataDocumentValidator();
            List<Error> errorList = validator.validateUpdate(referData);
            if (!errorList.isEmpty()) {
                invalidReferDataList.add(new ReferDataValidationResult(referData, errorList));
            } else {
                validReferDataList.add(referData);
            }
        });

        List<Document> updatedReferenceData = new ArrayList<>();
        if (!validReferDataList.isEmpty() && (referDataList.size() == validReferDataList.size() || allowPartial)) {
            updatedReferenceData = updateReferenceData(validReferDataList);
            List<Long> referIds = new ArrayList<>();
            updatedReferenceData.forEach(referData -> referIds.add(DocumentUtils.getLong(referData, Field._id)));
            matchReferenceData.stream()
                    .filter(referData -> referIds.contains(referData.get(Field._id)))
                    .forEach(referData -> documentRevisionService.saveDocumentRevision(DocType.reference_data, referData));
        }

        ReferenceDataBatchUpdateResult result = new ReferenceDataBatchUpdateResult();
        result.setMatchCount(matchReferenceData.size());
        result.setMatchReferenceData(matchReferenceData);
        result.setUpdatedReferenceData(updatedReferenceData);
        result.setInvalidReferenceData(invalidReferDataList);
        return result;
    }

    public List<Document> updateReferenceData(List<Document> referDataList) {
        referDataList.forEach(referData -> {
            RevisionUtils.increaseRevisionNumber(referData);
            referData.put(Field.recUpdtDtTm, new Date());
            colReferenceData.replaceOne(eq(Field._id, referData.get(Field._id)), new Document(referData));
        });
        return referDataList;
    }

    public ReferenceDataBatchImportResult batchImport(List<Map<String, Object>> referDataList, boolean allowPartial) {
        List<Map<String, Object>> validReferDataList = new ArrayList<>();
        List<ReferDataValidationResult> invalidReferDataList = new ArrayList<>();
        referDataList.forEach(referData -> {
            ReferenceDataDocumentValidator validator = new ReferenceDataDocumentValidator();
            List<Error> errorList = validator.validateImport(new Document(referData));
            if (!errorList.isEmpty()) {
                invalidReferDataList.add(new ReferDataValidationResult(referData, errorList));
            } else {
                validReferDataList.add(referData);
            }
        });

        List<Document> importedReferenceData = new ArrayList<>();
        ReferenceDataBatchImportResult result = new ReferenceDataBatchImportResult();
        result.setImportedReferenceData(importedReferenceData);
        result.setInvalidReferenceData(invalidReferDataList);
        if (validReferDataList.isEmpty() ||
                (referDataList.size() > validReferDataList.size() && !allowPartial)) {
            return result;
        }

        validReferDataList.forEach(validReferData -> {
            Bson query = and(
                    eq(Field.ctryRecCde, validReferData.get(Field.ctryRecCde)),
                    eq(Field.grpMembrRecCde, validReferData.get(Field.grpMembrRecCde)),
                    eq(Field.cdvTypeCde, validReferData.get(Field.cdvTypeCde)),
                    eq(Field.cdvCde, validReferData.get(Field.cdvCde)));

            if (!validReferData.containsKey(Field.chanlComnCde)
                    || validReferData.get(Field.chanlComnCde) == null) {
                validReferData.put(Field.chanlComnCde, Collections.emptyList());
            }
            validReferData.remove(Field._id);
            validReferData.remove(Field.recUpdtDtTm);
            validReferData.remove(Field.recCreatDtTm);
            validReferData.remove(Field.createdBy);
            validReferData.remove(Field.revision);

            Update update = new Update();
            long referDataId = sequenceService.nextId(Sequence.referenceDataId);
            Date date = new Date();
            update.setOnInsert(Field._id, referDataId);
            update.setOnInsert(Field.recCreatDtTm, date);
            update.setOnInsert(Field.createdBy, BATCH);
            // no matter create or update, set recUpdtDtTm.
            update.set(Field.recUpdtDtTm, date);

            validReferData.forEach(update::set);
            // default value will be 1 when creating
            update.inc(Field.revision);

            // query condition, by the 4 specific fields
            Document updatedDoc = colReferenceData.findOneAndUpdate(query, update.getUpdateObject(), findOneAndUpdateOptions);
            if (updatedDoc == null) {
                updatedDoc = new Document();
                updatedDoc.putAll(validReferData);
                updatedDoc.put(Field._id, referDataId);
                updatedDoc.put(Field.recUpdtDtTm, date);
                updatedDoc.put(Field.recCreatDtTm, date);
                updatedDoc.put(Field.createdBy, BATCH);
                updatedDoc.put(Field.revision, 1L);
            } else {
                // save revision
                documentRevisionService.saveDocumentRevision(DocType.reference_data, updatedDoc);
                // recover updated fields
                updatedDoc.putAll(validReferData);
                updatedDoc.put(Field.recUpdtDtTm, date);
                // $inc is an atomic operation, so use Mongo's return value then increased by 1, should be correct.
                Long oldVersion = Long.parseLong(String.valueOf(updatedDoc.get(Field.revision)));
                updatedDoc.put(Field.revision, oldVersion + 1L);
            }
            importedReferenceData.add(updatedDoc);
        });
        return result;
    }

    @Cacheable("productCache")
    public List<Document> getReferDataByFilter(Bson filterBson) {
        return colReferenceData.find(filterBson).into(new LinkedList<>());
    }

    @Cacheable("productCache")
    public long countReferDataByFilter(Bson filterBson) {
        return colReferenceData.countDocuments(filterBson);
    }
}
