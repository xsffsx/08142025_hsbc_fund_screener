package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironment;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class ProductMetadataCreateFetcherTest {

    @InjectMocks
    private ProductMetadataCreateFetcher productMetadataCreateFetcher;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private FindIterable findIterable;
    @Mock
    private InsertOneResult insertOneResult;

    @Before
    public void setUp() {
        productMetadataCreateFetcher = new ProductMetadataCreateFetcher(mongoDatabase);
        ReflectionTestUtils.setField(productMetadataCreateFetcher, "collection", collection);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("jsonPath", 1L);
        Mockito.when(environment.getArgument(anyString())).thenReturn(metadata);
        Mockito.when(collection.insertOne(any(Document.class))).thenReturn(insertOneResult);
        Mockito.when(collection.find(any(Bson.class))).thenReturn(findIterable);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDataFetcherResult() throws Exception {
        Mockito.when(findIterable.first()).thenReturn(new Document());
        DataFetcherResult dataFetcherResult = productMetadataCreateFetcher.get(environment);
        Assert.assertNotNull(dataFetcherResult);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDataFetcherResult2() throws Exception {
        Mockito.when(findIterable.first()).thenThrow(new MongoException("error"));
        DataFetcherResult dataFetcherResult = productMetadataCreateFetcher.get(environment);
        Assert.assertNotNull(dataFetcherResult);
    }
}
