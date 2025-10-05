package com.dummy.wmd.wpc.graphql.utils;

import com.google.gson.Gson;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

@Slf4j
public class DocumentUtilsTest {
    private Document doc;
    private Gson gson = new Gson();

    @Before
    public void setup() {
        doc = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        log.info("product data loaded");
    }

    @Test
    public void testCloneNull_givenNull_retuensNull() {
        Document obj = DocumentUtils.clone(null);
        assertEquals(null, obj);
    }

    @Test
    public void testCloneDocument_givenDocument_returnsDocument() {
        Document obj = DocumentUtils.clone(doc);
        JSONAssert.assertEquals(gson.toJson(doc), gson.toJson(obj), JSONCompareMode.STRICT);
    }

    @Test
    public void testCloneDocumentList_givenDocumentList_returnsDocumentList() {
        List<Document> listIn = Arrays.asList(doc);
        List<Document> listOut = DocumentUtils.clone(listIn);
        JSONAssert.assertEquals(gson.toJson(listIn), gson.toJson(listOut), JSONCompareMode.STRICT);
    }
    @Test
    public void testGetLong_givenMap_returnsNull(){
        Map<String,Object> map = new HashMap<>();
        map.put("key",1L);
        assertNotNull(DocumentUtils.getLong(map,"key"));
    }
    @Test
    public void testGetLong_givenNull_returnsNull(){
        assertNull(DocumentUtils.getLong(null,"key"));
    }
    @Test
    public void testGetLong_givenInteger_returnsLong(){
        Document document = new Document("key",1);
        Long value = DocumentUtils.getLong(document,"key");
        assertNotNull(value);
    }
    @Test
    public void testGetLong_givenLong_returnsLong(){
        Document document = new Document("key",1L);
        Long value = DocumentUtils.getLong(document,"key");
        assertNotNull(value);
    }

    @Test
    public void testCompactDocument_givenDocument_returnsCompactedDocument() {
        Document doc1 = CommonUtils.readResourceAsDocument("/files/UT-40028782.json");
        Document doc2 = DocumentUtils.compactDocument(doc1);
        JsonWriterSettings settings = JsonWriterSettings
                .builder()
                .outputMode(JsonMode.RELAXED)
                .build();
        assertNull(doc2.get("performance"));
    }

    @Test
    public void testCompactDocument_givenDocument_returnsDocument() {
        Document doc1 = CommonUtils.readResourceAsDocument("/files/UT-40004996.json");
        Document doc2 = DocumentUtils.compactDocument(doc1);
        assertNotNull(doc2.get("performance"));
    }
    @Test
    public void testGetLocalDate_givenNull_returnsNull(){
        assertNull( DocumentUtils.getLocalDate(null,"key"));
    }
    @Test
    public void testGetLocalDate_givenDoc_returnsNull(){
        Map<String, Object> doc = new HashMap<>();
        doc.put("key",null);
        LocalDate result = DocumentUtils.getLocalDate(doc,"key");
        assertNull(result);
    }
    @Test
    public void testGetLocalDate_givenDocWithLocalDate_returnsNull(){
        LocalDate localDate = LocalDate.now();
        Map<String, Object> doc = new HashMap<>();
        doc.put("key",localDate);
        LocalDate result = DocumentUtils.getLocalDate(doc,"key");
        assertNotNull(result);
    }
    @Test
    public void testGetLocalDate_givenDocWithDate_returnsNull(){
        Date date = new Date();
        Map<String, Object> doc = new HashMap<>();
        doc.put("key",date);
        LocalDate result = DocumentUtils.getLocalDate(doc,"key");
        assertNotNull(result);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetLocalDate_givenDocWithString_throwsException(){
        Map<String, Object> doc = new HashMap<>();
        doc.put("key","exception");
        DocumentUtils.getLocalDate(doc,"key");

    }
    @Test
    public void testGetString_givenNull_returnsNull(){
        assertNull(DocumentUtils.getString(null,"key"));
    }
    @Test
    public void testGetString_givenDocument_returnsString(){
        Map<String, Object> doc = new HashMap<>();
        doc.put("key","value");
        String result = DocumentUtils.getString(doc,"key");
        assertNotNull(result);
    }
    @Test
    public void testBigDecimal_givenNull_returnsNull(){
        assertNull(DocumentUtils.getBigDecimal(null,"key"));
    }
    @Test
    public void testBigDecimal_givenDoc_returnsNull(){
        Map<String, Object> doc = new HashMap<>();
        doc.put("key",null);
        assertNull(DocumentUtils.getBigDecimal(doc,"key"));
    }
    @Test
    public void testBigDecimal_givenDocWithDouble_returnsNull(){
        Map<String, Object> doc = new HashMap<>();
        doc.put("key",new Double(0.11));
        assertNotNull(DocumentUtils.getBigDecimal(doc,"key"));
    }
    @Test
    public void testBigDecimal_givenDocWithLong_returnsNull(){
        Map<String, Object> doc = new HashMap<>();
        doc.put("key",1L);
        assertNotNull(DocumentUtils.getBigDecimal(doc,"key"));
    }

}
