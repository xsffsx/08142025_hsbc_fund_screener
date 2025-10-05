package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProdReservedAttrUtilsTests {
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private FindIterable findIterable;

    private ProdReservedAttrUtils prodReservedAttrUtils;

    @Before
    public void setUp() {
        Document document = Document.parse(CommonUtils.readResource("/data/product-reserved-attrs.json"));
        when(mongodb.getCollection(CollectionName.configuration)).thenReturn(collection);
        when(collection.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(document);
        prodReservedAttrUtils = new ProdReservedAttrUtils(mongodb);
    }

    @Test
    public void testCopyReservedAttrs_givenDocuments_returnsNull() {
        // Setup
        Document document = new Document("key", "value");
        Document newProd = Document.parse(CommonUtils.readResource("/data/product-data.json"));
        // Run the test
        try {
            prodReservedAttrUtils.copyReservedAttrs(newProd, document);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
