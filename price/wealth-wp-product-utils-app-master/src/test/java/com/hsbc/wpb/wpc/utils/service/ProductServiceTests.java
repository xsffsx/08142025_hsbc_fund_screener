package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTests {

    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> colProduct;
    @Mock
    private FindIterable findIterable;

    @Mock
    private DeleteResult deleteResult;

    @InjectMocks
    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.product)).thenReturn(colProduct);
        productService = new ProductService(mockMongodb);
    }

    @Test
    public void testCleanAutoCreatedProducts() {
        //set up
        Document document = Document.parse(CommonUtils.readResource("/data/product-data.json"));
        List<Document> documents = Arrays.asList(document);


        when(colProduct.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.into(new ArrayList<>())).thenReturn(documents);
        when(colProduct.countDocuments(any(Bson.class))).thenReturn(1L);
        when(colProduct.deleteOne(any(Bson.class))).thenReturn(deleteResult);
        when(deleteResult.getDeletedCount()).thenReturn(1L);
        //run the test
        try{
            productService.cleanAutoCreatedProducts();
        }catch (Exception e){
            Assert.fail();
        }




    }
}
