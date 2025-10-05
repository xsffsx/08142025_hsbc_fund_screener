package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentRevisionServiceTest {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> colDocRevision;
    @Mock
    private MongoCollection<Document> coll;
    @Mock
    private FindIterable<Document> iterable;
    @Mock
    private FindIterable<Document> collIterable;
    @InjectMocks
    private DocumentRevisionService documentRevisionService;


    @Before
    public void setUp() {
        documentRevisionService = new DocumentRevisionService(mockMongodb);
        ReflectionTestUtils.setField(documentRevisionService, "colDocRevision", colDocRevision);
    }

    @Test
    public void testSaveDocumentRevision_givenDocTypeAndDoucment_returnNull() {
        // Setup
        Map<String, Object> document = new HashMap<>();
        // Run the test
        try {
            documentRevisionService.saveDocumentRevision(DocType.product, document);
        }catch (Exception e){
            Assert.fail();
        }

    }

    @Test
    public void testGetDocument_givenDocTypeDocIdAndRevision_returnsDocument() {
        // Setup
        Document expectedResult = new Document("key", "value");
        Document document = new Document("doc",expectedResult);
        Mockito.when(colDocRevision.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(document);
        // Run the test
        Document result = documentRevisionService.getDocument("docType", 1L, 1L);
        // Verify the results
        assertEquals(expectedResult, result);
    }
    @Test
    public void testGetDocument_givenDocTypeDocIdAndRevision_returnsDocument2() {
        // Setup
        Document expectedResult = new Document("key", "value");
        Mockito.when(colDocRevision.find(any(Bson.class))).thenReturn(iterable);
        Mockito.when(iterable.first()).thenReturn(null);
        // Run the test
        when(mockMongodb.getCollection("docType")).thenReturn(coll);
        Mockito.when(coll.find(any(Bson.class))).thenReturn(collIterable);
        Mockito.when(collIterable.first()).thenReturn(expectedResult);
        Document result = documentRevisionService.getDocument("docType", 1L, 1L);
        // Verify the results
        assertEquals(expectedResult, result);
    }


    @Test
    public void testSaveDocumentRevision_given_DocTypeAndDocumentList() {
        List<Map<String,Object>> documentList = new ArrayList<>();
        Map map = new HashMap<>();
        map.put(Field.ctryRecCde,"HK");
        map.put(Field.grpMembrRecCde,"HBAP");
        map.put(Field._id,1L);
        map.put(Field.revision,"1");
        map.put(Field.ctryRecCde,"HK");
        map.put(Field.ctryRecCde,"HK");
        map.put(Field.ctryRecCde,"HK");
        map.put(Field.ctryRecCde,"HK");
        documentList.add(map);
        Assertions.assertDoesNotThrow(()->documentRevisionService.saveDocumentRevision(DocType.product, documentList));
    }


}
