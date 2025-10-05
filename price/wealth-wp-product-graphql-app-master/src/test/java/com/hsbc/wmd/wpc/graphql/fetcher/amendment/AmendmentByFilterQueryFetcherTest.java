package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetchingEnvironment;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AmendmentByFilterQueryFetcherTest {

    @InjectMocks
    private AmendmentByFilterQueryFetcher amendmentByFilterQueryFetcher;
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
        amendmentByFilterQueryFetcher = new AmendmentByFilterQueryFetcher(mongoDatabase);
        ReflectionTestUtils.setField(amendmentByFilterQueryFetcher, "collection", collection);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDocumentList() {
        MockedStatic<AmendmentUtils> amendmentUtilsMockedStatic = Mockito.mockStatic(AmendmentUtils.class);
        MockedConstruction<FilterUtils> mockedConstruction = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("A", "B");
            Mockito.when(environment.getArgument(anyString())).thenReturn(map).thenReturn(map).thenReturn(1).thenReturn(null);
            Mockito.when(collection.find(any(Bson.class))).thenReturn(findIterable);
            Mockito.when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
            Mockito.when(findIterable.skip(any(Integer.class))).thenReturn(findIterable);
            Mockito.when(findIterable.limit(any(Integer.class))).thenReturn(findIterable);
            mockedConstruction = Mockito.mockConstruction(FilterUtils.class, (filterUtils, context) ->{
                Mockito.when(filterUtils.toBsonDocument(anyMap())).thenReturn(new BsonDocument());
            });
            List<Document> DocumentList = new ArrayList<>();
            Document document = new Document();
            DocumentList.add(document);
            Mockito.when(findIterable.into(any(List.class))).thenReturn(DocumentList);
            amendmentUtilsMockedStatic.clearInvocations();
            List list = amendmentByFilterQueryFetcher.get(environment);
            Assert.assertNotNull(list);
        } catch (JsonProcessingException e) {
            Assert.fail();
        } finally {
            mockedConstruction.close();
            amendmentUtilsMockedStatic.close();
        }
    }
}
