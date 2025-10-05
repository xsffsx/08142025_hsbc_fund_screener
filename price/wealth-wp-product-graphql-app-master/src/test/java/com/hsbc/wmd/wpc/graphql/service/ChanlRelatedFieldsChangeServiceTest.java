package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChanlRelatedFieldsChangeServiceTest {

    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private Document updatedDoc;
    @Mock
    private Document existedDoc;
    @Mock
    private Document document;
    @Mock
    private Document srcDoc;
    @Mock
    private Bson filter;

    @Mock
    private MongoCollection<Document> colProductData;
    @Mock
    private FindIterable<Document> findIterable;

    private ChanlRelatedFieldsChangeService chanlRelatedFieldsChangeService;

    @Before
    public void init() {
        when(mongoDatabase.getCollection(CollectionName.product.toString())).thenReturn(colProductData);
        when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(existedDoc);
        chanlRelatedFieldsChangeService = new ChanlRelatedFieldsChangeService(mongoDatabase);
        // build data
        when(updatedDoc.get(Field._id)).thenReturn(100L);
        List<Map<String, Object>> existedList = new ArrayList<>();
        Map<String, Object> data0 = new HashMap<>();
        data0.put(Field.chanlCde, "CMB_I");
        data0.put(Field.fieldCde, "ALLOW_SELL_MIP_PROD_IND");
        data0.put(Field.fieldCharValueText, "Y");
        Map<String, Object> data1 = new HashMap<>();
        data1.put(Field.chanlCde, "CMB_I");
        data1.put(Field.fieldCde, "ALLOW_BUY_PROD_IND");
        data1.put(Field.fieldCharValueText, "Y");
        existedList.add(data0);
        existedList.add(data1);

        List<Map<String, Object>> updatedList = new ArrayList<>();
        Map<String, Object> data2 = new HashMap<>();
        data2.put(Field.chanlCde, "CMB_I");
        data2.put(Field.fieldCde, "ALLOW_BUY_PROD_IND");
        data2.put(Field.fieldCharValueText, "Y");
        Map<String, Object> data3 = new HashMap<>();
        data3.put(Field.chanlCde, "CMB_I");
        data3.put(Field.fieldCde, "ALLOW_SELL_PROD_IND");
        data3.put(Field.fieldCharValueText, "Y");
        updatedList.add(data2);
        updatedList.add(data3);
        when(existedDoc.get(Field.chanlAttr)).thenReturn(existedList);
        when(updatedDoc.get(Field.chanlAttr)).thenReturn(updatedList);
    }

    @Test
    public void test_validate() {
        when(document.get(Field.doc)).thenReturn(srcDoc);
        when(document.get(Field.actionCde)).thenReturn(ActionCde.add);
        Map<String, Object> dataMap = new HashMap<>();
        when(srcDoc.get(Field.chanlAttr)).thenReturn(Collections.singletonList(dataMap));
        assertEquals(1, chanlRelatedFieldsChangeService.validate(document).size());
        dataMap.put(Field.chanlCde, "CMB_I");
        assertEquals(1, chanlRelatedFieldsChangeService.validate(document).size());
        dataMap.put(Field.fieldCde, "ALLOW_BUY_PROD_IND");
        assertEquals(1, chanlRelatedFieldsChangeService.validate(document).size());
        when(document.get(Field.actionCde)).thenReturn(ActionCde.delete);
        assertEquals(0, chanlRelatedFieldsChangeService.validate(document).size());
        dataMap.put(Field.fieldCharValueText, "N");
        assertTrue(chanlRelatedFieldsChangeService.validate(document).isEmpty());
    }

    @Test
    public void test_add() {
        assertNotNull(updatedDoc);
        chanlRelatedFieldsChangeService.add(updatedDoc);
        when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);
        try {
            chanlRelatedFieldsChangeService.add(updatedDoc);
        } catch (productErrorException e) {
            // ignore the error
        }
    }

    @Test
    public void test_update() {
        assertNotNull(updatedDoc);
        chanlRelatedFieldsChangeService.update(updatedDoc);
    }

    @Test
    public void test_delete() {
        assertNotNull(updatedDoc);
        chanlRelatedFieldsChangeService.delete(updatedDoc);
        when(findIterable.first()).thenReturn(document);
        when(document.get(Field.chanlAttr)).thenReturn(Collections.emptyList());
        chanlRelatedFieldsChangeService.delete(updatedDoc);
        when(findIterable.first()).thenReturn(null);
        try {
            chanlRelatedFieldsChangeService.delete(updatedDoc);
        } catch (productErrorException e) {
            // ignore the error
        }
    }

    @Test
    public void test_findFirst() {
        when(findIterable.projection(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);
        try {
            chanlRelatedFieldsChangeService.findFirst(filter);
        } catch (productErrorException e) {
            // ignore the error
        }
        when(findIterable.first()).thenReturn(existedDoc);
        assertEquals(existedDoc, chanlRelatedFieldsChangeService.findFirst(filter));
    }
}