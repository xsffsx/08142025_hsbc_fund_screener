package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.Sequence;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AssetVolatilityClassCorlChangeServiceTest {

    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoCollection<Document> colAsetVoltlClass;
    @Mock
    private FindIterable<Document> iterable;
    @Mock
    private UpdateResult updateResult;
    @Mock
    private DocumentRevisionService documentRevisionService;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private ReferenceDataService referenceDataService;
    @InjectMocks
    private AssetVolatilityClassCorlChangeService assetVolatilityClassCorlChangeService;

    @Before
    public void setUp() {
        assetVolatilityClassCorlChangeService = new AssetVolatilityClassCorlChangeService(mockMongoDatabase);
        ReflectionTestUtils.setField(assetVolatilityClassCorlChangeService, "referenceDataService", referenceDataService);
        ReflectionTestUtils.setField(assetVolatilityClassCorlChangeService, "colAsetVoltlClass", colAsetVoltlClass);
        ReflectionTestUtils.setField(assetVolatilityClassCorlChangeService, "documentRevisionService", documentRevisionService);
        ReflectionTestUtils.setField(assetVolatilityClassCorlChangeService, "sequenceService", sequenceService);
    }

    @Test
    public void testValidate_givenAmendment_returnsErrorList() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl.json");
        Document amendment = new Document(Field.actionCde, "update");
        amendment.put("doc", doc);
        List<Document> referDatas = new ArrayList<>();
        Document ref = new Document(Field.cdvCde, "BRA_EQTY");
        referDatas.add(ref);
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(referDatas);
        // Run the test
        List<Error> result = assetVolatilityClassCorlChangeService.validate(amendment);
        assertNotNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testValidate_givenAmendment_throwsException() {
        // Setup
        Document amendment = new Document(Field.actionCde, "add");
        // Run the test
        assetVolatilityClassCorlChangeService.validate(amendment);
    }

    @Test
    public void testValidateDocument_givenDoc_returnErrors() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl.json");
        List<Document> list = (List<Document>) doc.get("list");
        list.add(list.get(0));
        // Run the test
        List<Error> result = assetVolatilityClassCorlChangeService.validateDocument(doc);
        assertNotNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testAdd_ThrowsproductErrorException() {
        // Setup
        Document doc = new Document("key", "value");
        // Run the test
        assetVolatilityClassCorlChangeService.add(doc);
    }

    @Test(expected = productErrorException.class)
    public void testUpdate_ThrowsproductErrorException() {
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl.json");
        Document amendment = new Document(Field.actionCde, "update");
        amendment.put("doc", doc);
        Mockito.when(colAsetVoltlClass.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(null);
        // Run the test
        assetVolatilityClassCorlChangeService.update(doc);
    }

    @Test
    public void testUpdate_givenDocument_returnsDocument() {
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl.json");
        Document amendment = new Document(Field.actionCde, "update");
        amendment.put("doc", doc);
        Mockito.when(colAsetVoltlClass.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(amendment);
        Mockito.doNothing().when(documentRevisionService).saveDocumentRevision(DocType.aset_voltl_class_corl, amendment);
        Mockito.when(colAsetVoltlClass.replaceOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        // Run the test
        assetVolatilityClassCorlChangeService.update(doc);
        assertNotNull(doc);
    }

    @Test
    public void testAddDocument_givenDocument_returnsDocument() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl.json");
        Mockito.when(sequenceService.nextId(Sequence.aset_voltl_class_corl_id)).thenReturn(1L);
        // Run the test
        Document result = assetVolatilityClassCorlChangeService.addDocument(doc);
        // Verify the results
        assertEquals(doc, result);
    }

    @Test(expected = productErrorException.class)
    public void testAddDocument_ThrowsproductErrorException() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-corl.json");
        Mockito.when(colAsetVoltlClass.countDocuments(any(Bson.class))).thenReturn(1L);
        // Run the test
        assetVolatilityClassCorlChangeService.addDocument(doc);

    }

    @Test(expected = productErrorException.class)
    public void testDelete_ThrowsproductErrorException() {
        // Setup
        Document doc = new Document("key", "value");
        // Run the test
        assetVolatilityClassCorlChangeService.delete(doc);
    }

    @Test
    public void testFindFirst_givenFilter_returnsDocument() {
        // Setup
        Bson filter = new BsonDocument();
        final Document document = new Document("key", "value");
        Mockito.when(colAsetVoltlClass.find(filter)).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(document);
        // Run the test
        Document result = assetVolatilityClassCorlChangeService.findFirst(filter);
        // Verify the results
        assertNotNull(result);
    }
}
