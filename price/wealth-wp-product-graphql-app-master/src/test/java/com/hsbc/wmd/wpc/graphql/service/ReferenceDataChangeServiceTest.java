package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ReferenceDataDocumentValidator;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataChangeServiceTest {

    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoCollection<Document> colReferenceData;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private ReferenceDataDocumentValidator validator;
    @Mock
    private FindIterable<Document> iterable;
    @Mock
    private DocumentRevisionService documentRevisionService;

    @InjectMocks
    private ReferenceDataChangeService referenceDataChangeService;


    @Before
    public void setUp() throws Exception {
        referenceDataChangeService = new ReferenceDataChangeService(mockMongoDatabase);
        ReflectionTestUtils.setField(referenceDataChangeService, "colReferenceData", colReferenceData);
        ReflectionTestUtils.setField(referenceDataChangeService, "validator", validator);
        ReflectionTestUtils.setField(referenceDataChangeService, "sequenceService", sequenceService);
        ReflectionTestUtils.setField(referenceDataChangeService, "documentRevisionService", documentRevisionService);
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeAdd_returnsErrorList() {
        // Setup
        Document document = new Document(Field.doc, new Document("key", "value"));
        document.put(Field.actionCde, "add");
        // Run the test
        List<Error> result = referenceDataChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeUpdate_returnsErrorList() {
        // Setup
        Document document = new Document(Field.doc, new Document("key", "value"));
        document.put(Field.actionCde, "update");
        // Run the test
        List<Error> result = referenceDataChangeService.validate(document);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeDelete_returnsErrorList() {
        // Setup
        Document doc = new Document();
        doc.put(Field._id, 1L);
        doc.put(Field.revision, 1L);
        Document document = new Document(Field.doc, doc);
        document.put(Field.actionCde, "delete");
        mockCollFindFirst(doc);
        // Run the test
        List<Error> result = referenceDataChangeService.validate(document);
        // Verify the results
        assertEquals(0L, result.size());
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeDeleteWithDifferentRevision_returnsErrorList() {
        // Setup
        Document doc = new Document();
        doc.put(Field._id, 1L);
        doc.put(Field.revision, 1L);
        Document document = new Document(Field.doc, doc);
        document.put(Field.actionCde, "delete");

        Document differentDoc = new Document();
        doc.put(Field._id, 1L);
        doc.put(Field.revision, 2L);
        mockCollFindFirst(differentDoc);
        // Run the test
        List<Error> result = referenceDataChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeDeleteWithOutId_returnsErrorList() {
        // Setup
        Document doc = new Document();
        doc.put(Field.revision, 1L);
        Document document = new Document(Field.doc, doc);
        document.put(Field.actionCde, "delete");
        mockCollFindFirst(doc);
        // Run the test
        List<Error> result = referenceDataChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testValidate_givenDocumentAndActionCdeDeleteWithOutRevision_returnsErrorList() {
        // Setup
        Document doc = new Document();
        doc.put(Field._id, 1L);
        Document document = new Document(Field.doc, doc);
        document.put(Field.actionCde, "delete");
        mockCollFindFirst(doc);
        // Run the test
        List<Error> result = referenceDataChangeService.validate(document);
        // Verify the results
        assertEquals(1L, result.size());
    }

    @Test
    public void testAdd_givenDocument_returnsNull() {
        // Setup
        Document doc = new Document("key", "value");
        // Run the test
        try {
            referenceDataChangeService.add(doc);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testUpdate_givenDocument_returnsNull() {
        // Setup
        Document document = new Document(Field._id, 1L);
        mockCollFindFirst(document);
        // Run the test
        try {
            referenceDataChangeService.update(document);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = productErrorException.class)
    public void testUpdate_givenDocument_throwsException() {
        // Setup
        Document document = new Document(Field._id, 1L);
        mockCollFindFirst(null);
        // Run the test
        referenceDataChangeService.update(document);
    }

    @Test
    public void testDelete_givenDocument_returnsNull() {
        // Setup
        Document doc = new Document(Field._id, "value");
        // Run the test
        try {
            referenceDataChangeService.delete(doc);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testFindFirst_givenFilter_returnsDocument() {
        // Setup
        Bson filter = new BsonDocument();
        Document document = new Document("key", "value");
        mockCollFindFirst(document);
        // Run the test
        Document result = referenceDataChangeService.findFirst(filter);
        // Verify the results
        assertEquals(document, result);
    }

    private void mockCollFindFirst(Document document) {
        Mockito.when(colReferenceData.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(document);
    }
}
