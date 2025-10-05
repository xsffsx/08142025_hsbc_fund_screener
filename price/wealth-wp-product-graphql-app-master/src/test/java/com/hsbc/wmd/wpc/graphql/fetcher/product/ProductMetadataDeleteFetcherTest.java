package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class ProductMetadataDeleteFetcherTest {

    @InjectMocks
    private ProductMetadataDeleteFetcher productMetadataDeleteFetcher;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private FindIterable findIterable;

    @Before
    public void setUp() {
        productMetadataDeleteFetcher = new ProductMetadataDeleteFetcher(mongoDatabase);
        ReflectionTestUtils.setField(productMetadataDeleteFetcher, "collection", collection);
        Mockito.when(environment.getArgument(anyString())).thenReturn("id");
        Mockito.when(collection.find(any(Bson.class))).thenReturn(findIterable);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDataFetcherResult() throws Exception {
        Mockito.when(findIterable.first()).thenReturn(new Document());
        DataFetcherResult dataFetcherResult = productMetadataDeleteFetcher.get(environment);
        Assert.assertNotNull(dataFetcherResult);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDataFetcherResult2() throws Exception {
        Mockito.when(findIterable.first()).thenThrow(new MongoException("error"));
        DataFetcherResult dataFetcherResult = productMetadataDeleteFetcher.get(environment);
        Assert.assertNotNull(dataFetcherResult);
    }
}
