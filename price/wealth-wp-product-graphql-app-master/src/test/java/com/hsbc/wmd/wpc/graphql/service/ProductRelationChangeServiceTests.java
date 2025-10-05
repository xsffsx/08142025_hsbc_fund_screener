package com.dummy.wmd.wpc.graphql.service;

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

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ProductRelationChangeServiceTests {

    @InjectMocks
    private ProductRelationChangeService productRelationChangeService;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private MongoCollection<Document> colProductData;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private UpdateResult updateResult;

    @Before
    public void setUp() {
        productRelationChangeService = new ProductRelationChangeService(mongoDatabase);
        ReflectionTestUtils.setField(productRelationChangeService, "colProductData", colProductData);
        Mockito.when(colProductData.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(findIterable);
    }

    @Test
    public void testValidate_givenDocument_returnsErrorList() {
        Document doc = CommonUtils.readResourceAsDocument("/files/amendment.json");
        Mockito.when(findIterable.first()).thenReturn(null).thenReturn(new Document());
        List<Error> errorList =  productRelationChangeService.validate(doc);
        Assert.assertNotNull(errorList);
    }

    @Test
    public void testAdd_givenDocument_DoesNotReturn() {
        try {
            Document doc = CommonUtils.readResourceAsDocument("/files/INS-1000048227.json");
            Mockito.when(findIterable.first()).thenReturn(doc);
            Mockito.when(colProductData.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResult);
            productRelationChangeService.add(doc);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = productErrorException.class)
    public void testDelete_givenDocument_throwException() {
        productRelationChangeService.delete(new Document());
    }

    @Test
    public void testFindFirst_givenBson_returnsDocument() {
        Mockito.when(findIterable.first()).thenReturn(new Document());
        Document document = productRelationChangeService.findFirst(new BsonDocument());
        Assert.assertNotNull(document);
    }
}
