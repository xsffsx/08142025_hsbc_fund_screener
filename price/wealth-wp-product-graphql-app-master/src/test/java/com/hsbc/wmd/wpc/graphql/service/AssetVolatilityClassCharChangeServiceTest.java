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
public class AssetVolatilityClassCharChangeServiceTest {

    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private ReferenceDataService referenceDataService;
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
    @InjectMocks
    private AssetVolatilityClassCharChangeService assetVolatilityClassCharChangeService;

    @Before
    public void setUp() {
        assetVolatilityClassCharChangeService = new AssetVolatilityClassCharChangeService(mockMongoDatabase);
        ReflectionTestUtils.setField(assetVolatilityClassCharChangeService, "referenceDataService", referenceDataService);
        ReflectionTestUtils.setField(assetVolatilityClassCharChangeService, "colAsetVoltlClass", colAsetVoltlClass);
        ReflectionTestUtils.setField(assetVolatilityClassCharChangeService, "documentRevisionService", documentRevisionService);
        ReflectionTestUtils.setField(assetVolatilityClassCharChangeService, "sequenceService", sequenceService);
    }

    @Test
    public void testValidate_givenAmendment_returnErrorList() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-char.json");
        Document amendment = new Document(Field.actionCde, "update");
        amendment.put("doc", doc);
        List<Document> referDatas = new ArrayList<>();
        Document ref = new Document(Field.cdvCde, "BRA_EQTY");
        referDatas.add(ref);
        Mockito.when(referenceDataService.getReferDataByFilter(any(Bson.class))).thenReturn(referDatas);
        // Run the test
        List<Error> result = assetVolatilityClassCharChangeService.validate(amendment);
        assertNotNull(result);
    }

    @Test(expected = productErrorException.class)
    public void testValidate_givenAmendment_throwsException() {
        // Setup
        Document amendment = new Document(Field.actionCde, "add");
        // Run the test
        assetVolatilityClassCharChangeService.validate(amendment);
    }

    @Test
    public void testValidateDocument_givenDoc_returnErrors() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-char.json");
        List<Document> list = (List<Document>) doc.get("list");
        list.add(list.get(0));
        // Run the test
        List<Error> result = assetVolatilityClassCharChangeService.validateDocument(doc);
        assertNotNull(result);
    }


    @Test(expected = productErrorException.class)
    public void testAdd_ThrowsproductErrorException() {
        Document doc = new Document("key", "value");
        assetVolatilityClassCharChangeService.add(doc);
    }

    @Test(expected = productErrorException.class)
    public void testUpdate_ThrowsproductErrorException() {
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-char.json");
        Document amendment = new Document(Field.actionCde, "update");
        amendment.put("doc", doc);
        Mockito.when(colAsetVoltlClass.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(null);
        // Run the test
        assetVolatilityClassCharChangeService.update(doc);
    }

    @Test
    public void testUpdate_givenDocument_returnsUpdateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-char.json");
        Document amendment = new Document(Field.actionCde, "update");
        amendment.put("doc", doc);
        Mockito.when(colAsetVoltlClass.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(amendment);
        Mockito.doNothing().when(documentRevisionService).saveDocumentRevision(DocType.aset_voltl_class_char, amendment);
        Mockito.when(colAsetVoltlClass.replaceOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        // Run the test
        assetVolatilityClassCharChangeService.update(doc);
        assertNotNull(doc);

    }

    @Test
    public void testAddDocument_givenDocument_returnsDocument() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-char.json");
        Mockito.when(sequenceService.nextId(Sequence.aset_voltl_class_char_id)).thenReturn(1L);
        // Run the test
        Document result = assetVolatilityClassCharChangeService.addDocument(doc);
        // Verify the results
        assertEquals(doc, result);
    }

    @Test(expected = productErrorException.class)
    public void testAddDocument_ThrowsproductErrorException() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/aset-voltl-class-char.json");
        Mockito.when(colAsetVoltlClass.countDocuments(any(Bson.class))).thenReturn(1L);
        // Run the test
        Document result = assetVolatilityClassCharChangeService.addDocument(doc);

    }

    @Test(expected = productErrorException.class)
    public void testDelete_ThrowsproductErrorException() {
        // Setup
        Document doc = new Document("key", "value");
        // Run the test
        assetVolatilityClassCharChangeService.delete(doc);
    }

    @Test
    public void testFindFirst_givenFilter_returnDocument() {
        // Setup
        Bson filter = new BsonDocument();
        final Document document = new Document("key", "value");
        Mockito.when(colAsetVoltlClass.find(filter)).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(document);
        // Run the test
        Document result = assetVolatilityClassCharChangeService.findFirst(filter);
        // Verify the results
        assertNotNull(result);
    }
}
