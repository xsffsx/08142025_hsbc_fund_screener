package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.MongoDbErrorCodes;
import com.dummy.wmd.wpc.graphql.model.*;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ReferenceDataDocumentValidator;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.ServerAddress;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteInsert;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.InsertManyOptions;
import org.assertj.core.util.Lists;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataServiceTest {

    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoCollection<Document> colReferenceData;
    @Mock
    private DocumentRevisionService documentRevisionService;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private FindIterable<Document> iterable;

    @InjectMocks
    private ReferenceDataService referenceDataService;

    @Before
    public void setUp() {
        referenceDataService = new ReferenceDataService(mockMongoDatabase);
        ReflectionTestUtils.setField(referenceDataService, "colReferenceData", colReferenceData);
        ReflectionTestUtils.setField(referenceDataService, "documentRevisionService", documentRevisionService);
        ReflectionTestUtils.setField(referenceDataService, "sequenceService", sequenceService);
    }

    @Test
    public void testBatchCreate_giveReferDataList_returnsReferenceDataBatchCreateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");

        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    Mockito.when(validator.validateCreate(any(Document.class))).thenReturn(new ArrayList<>());
                });
        // Run the test
        ReferenceDataBatchCreateResult result = referenceDataService.batchCreate(referDataList, false);
        mockedConstruction.close();
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testBatchCreate_giveReferDataListWithError_returnsReferenceDataBatchCreateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");
        Map<String, Object> wrongReferData = referDataList.get(0);
        List<Error> errors = new LinkedList<>();
        errors.add(new Error("$.cdvTypeCde", "cdvTypeCde@invalid", "Invalid cdvTypeCde"));
        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    Mockito.when(validator.validateCreate(new Document(wrongReferData))).thenReturn(errors);
                });
        // Run the test
        ReferenceDataBatchCreateResult result = referenceDataService.batchCreate(referDataList, true);
        mockedConstruction.close();

        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testBatchCreate_giveReferDataListWithMongodbDuplicateError_returnsReferenceDataBatchCreateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");
        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    Mockito.when(validator.validateCreate(any(Document.class))).thenReturn(new ArrayList<>());
                });

        BulkWriteError bulkWriteError = new BulkWriteError(MongoDbErrorCodes.DUPLICATE_KEY, "E11000 duplicate key error collection: reference_data index: idx_cdvCde", new BsonDocument(), 0);

        BulkWriteResult bulkWriteResult = BulkWriteResult.acknowledged(
                1,
                1,
                0,
                0,
                Collections.emptyList(),
                Lists.list(new BulkWriteInsert(1,new BsonInt64(123))));
        MongoBulkWriteException mongoBulkWriteException = new MongoBulkWriteException(bulkWriteResult, Collections.singletonList(bulkWriteError), null, new ServerAddress());
        Mockito.when(colReferenceData.insertMany(anyList(),any(InsertManyOptions.class))).thenThrow(mongoBulkWriteException);
        // Run the test
        ReferenceDataBatchCreateResult result = referenceDataService.batchCreate(referDataList, true);
        Assert.assertEquals(referDataList.get(1), result.getCreatedReferenceData().get(0));
        Assert.assertEquals("@duplicate", result.getInvalidReferenceData().get(0).getErrors().get(0).getCode());
        Assert.assertEquals(referDataList.get(0), result.getInvalidReferenceData().get(0).getReferData());
        Assert.assertEquals(referDataList.get(2), result.getInvalidReferenceData().get(1).getReferData());

        mockedConstruction.close();
    }


    @Test
    public void testBatchUpdate_giveReferDataListWithError_returnsReferenceDataBatchUpdateResult() {
        // Setup
        BsonDocument filterBson = new BsonDocument();
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        LinkedList<Document> linkedList = new LinkedList<>();
        linkedList.addAll((Collection<? extends Document>) doc.get("referenceData"));
        Map<String, Object> wrongReferData = linkedList.get(0);
        Mockito.when(colReferenceData.find(filterBson)).thenReturn(iterable);
        Mockito.when(iterable.into(new LinkedList<>())).thenReturn(linkedList);

        List<Error> errors = new LinkedList<>();
        errors.add(new Error("$.cdvTypeCde", "cdvTypeCde@invalid", "Invalid cdvTypeCde"));

        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    Mockito.when(validator.validateUpdate(new Document(wrongReferData))).thenReturn(errors);
                });
        List<OperationInput> operations = Lists.list(new OperationInput(Operation.put, "a", 123));
        // Run the test
        ReferenceDataBatchUpdateResult result = referenceDataService.batchUpdate(filterBson, operations,
                true);
        mockedConstruction.close();
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testBatchUpdate_giveReferDataList_returnsReferenceDataBatchUpdateResult() {
        // Setup
        BsonDocument filterBson = new BsonDocument();
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        LinkedList<Document> linkedList = new LinkedList<>();
        linkedList.addAll((Collection<? extends Document>) doc.get("referenceData"));
        Mockito.when(colReferenceData.find(filterBson)).thenReturn(iterable);
        Mockito.when(iterable.into(new LinkedList<>())).thenReturn(linkedList);
        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    Mockito.when(validator.validateUpdate(any(Document.class))).thenReturn(new ArrayList<>());
                });
        List<OperationInput> operations = Lists.list(new OperationInput(Operation.put, "a", 123));
        // Run the test
        ReferenceDataBatchUpdateResult result = referenceDataService.batchUpdate(filterBson, operations,
                false);
        mockedConstruction.close();
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testBatchImport_giveReferDataList4Create_returnsReferenceDataBatchUpdateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");
        // return null, then create document
        Mockito.when(colReferenceData.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class))).thenReturn(null);
        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    Mockito.when(validator.validateImport(any(Document.class))).thenReturn(new ArrayList<>());
                });
        // Run the test
        ReferenceDataBatchImportResult result = referenceDataService.batchImport(referDataList, false);
        mockedConstruction.close();
        // Verify the results
        assertNotNull(result);
        List<Document> importedReferenceData = result.getImportedReferenceData();
        assertNotNull(importedReferenceData);
        assertFalse(importedReferenceData.isEmpty());
        assertEquals(referDataList.size(), importedReferenceData.size());
        importedReferenceData.forEach((referenceData) -> {
            // check revision is 1
            assertEquals(1L, Long.parseLong(String.valueOf(referenceData.get(Field.revision))));
        });
    }

    @Test
    public void testBatchDelete_giveReferDataList() {
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");

        Document criteria = new Document(referDataList.get(0));
        Mockito.when(colReferenceData.findOneAndDelete(any())).thenReturn(criteria);
        ReferenceDataBatchDeleteResult result = referenceDataService.batchDelete(referDataList);
        assertNotNull(result);
        assertEquals(3, result.getDeletedCount());
        Mockito.when(colReferenceData.findOneAndDelete(any())).thenReturn(null);
        result = referenceDataService.batchDelete(referDataList);
        assertEquals(0, result.getDeletedCount());
        assertNotNull(result);
    }

    @Test
    public void testBatchImport_giveReferDataList4Update_returnsReferenceDataBatchUpdateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");
        // return non null document, then update document
        Document docInDB = new Document(referDataList.get(0));
        Mockito.when(colReferenceData.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class))).thenReturn(docInDB);
        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    Mockito.when(validator.validateImport(any(Document.class))).thenReturn(new ArrayList<>());
                });
        // Run the test
        ReferenceDataBatchImportResult result = referenceDataService.batchImport(referDataList, false);
        mockedConstruction.close();
        // Verify the results
        assertNotNull(result);
        List<Document> importedReferenceData = result.getImportedReferenceData();
        assertNotNull(importedReferenceData);
        assertFalse(importedReferenceData.isEmpty());
        importedReferenceData.forEach((referenceData) -> {
            // check revision is greater than 1
            assertTrue(Long.parseLong(String.valueOf(referenceData.get(Field.revision))) > 1L);
        });
    }

    @Test
    public void testBatchImport_giveReferDataListWithError_returnsReferenceDataBatchUpdateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");
        Map<String, Object> invalidReferenceData = referDataList.get(0);
        List<Error> errors = new LinkedList<>();
        errors.add(new Error("$.cdvTypeCde", "cdvTypeCde@invalid", "Invalid cdvTypeCde"));
        Mockito.when(colReferenceData.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class))).thenReturn(null);
        MockedConstruction<ReferenceDataDocumentValidator> mockedConstruction =
                Mockito.mockConstruction(ReferenceDataDocumentValidator.class, (validator, context) -> {
                    // return errors first time, after that, always return empty list
                    Mockito.when(validator.validateImport(new Document(invalidReferenceData))).thenReturn(errors, new ArrayList<>());
                });
        // Run the test
        ReferenceDataBatchImportResult result = referenceDataService.batchImport(referDataList, true);
        mockedConstruction.close();
        // Verify the results
        assertNotNull(result);
        assertNotNull(result.getInvalidReferenceData());
        List<Document> importedReferenceData = result.getImportedReferenceData();
        assertNotNull(importedReferenceData);
        assertFalse(importedReferenceData.isEmpty());
        assertEquals(referDataList.size() - 1, importedReferenceData.size());
    }

    @Test
    public void testGetReferDataByFilter_givenFilter_returnsListDocument() {
        // Setup
        Bson filterBson = new BsonDocument();
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        LinkedList<Document> linkedList = new LinkedList<>();
        linkedList.addAll((Collection<? extends Document>) doc.get("referenceData"));
        Mockito.when(colReferenceData.find(filterBson)).thenReturn(iterable);
        Mockito.when(iterable.into(new LinkedList<>())).thenReturn(linkedList);
        // Run the test
        List<Document> result = referenceDataService.getReferDataByFilter(filterBson);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testCountReferDataByFilter_givenFilter_returnsCount() {
        // Setup
        Bson filterBson = new BsonDocument();
        Mockito.when(colReferenceData.countDocuments(filterBson)).thenReturn(0L);
        // Run the test
        long result = referenceDataService.countReferDataByFilter(filterBson);
        // Verify the results
        assertEquals(0L, result);
    }
}
