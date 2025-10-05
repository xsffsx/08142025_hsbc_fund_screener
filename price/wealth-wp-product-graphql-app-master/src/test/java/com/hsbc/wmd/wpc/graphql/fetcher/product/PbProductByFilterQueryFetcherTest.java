package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PbProductByFilterQueryFetcherTest {

    @InjectMocks
    private PbProductByFilterQueryFetcher pbProductByFilterQueryFetcher;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private FindIterable findIterable;
    @Mock
    private DataFetchingFieldSelectionSet dataFetchingFieldSelectionSet;
    @Mock
    private SelectedField selectedField;
    private MockedConstruction<FilterUtils> filterUtilsMockedConstruction;

    @Before
    public void setUp() {
        pbProductByFilterQueryFetcher = new PbProductByFilterQueryFetcher(mongoDatabase);
        ReflectionTestUtils.setField(pbProductByFilterQueryFetcher, "collection", collection);
        filterUtilsMockedConstruction = Mockito.mockConstruction(FilterUtils.class, (filterUtils, context) ->
                Mockito.when(filterUtils.toBsonDocument(anyMap())).thenReturn(new BsonDocument())
        );
        Mockito.when(environment.getArgument(anyString())).
                thenReturn(new LinkedHashMap<>()).thenReturn(new LinkedHashMap<>()).thenReturn(true).
                thenReturn(1).thenReturn(null);
        Mockito.when(environment.getSelectionSet()).thenReturn(dataFetchingFieldSelectionSet);
        List<SelectedField> selectedFields = new LinkedList<>();
        selectedFields.add(selectedField);
        Mockito.when(dataFetchingFieldSelectionSet.getFields()).thenReturn(selectedFields);
        Mockito.when(selectedField.getQualifiedName()).thenReturn("/A");
        Mockito.when(collection.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.skip(any(Integer.class))).thenReturn(findIterable);
        Mockito.when(findIterable.limit(any(Integer.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(findIterable);
        List<Document> DocumentList = new LinkedList<>();
        Document document = new Document();
        DocumentList.add(document);
        Mockito.when(findIterable.into(any(List.class))).thenReturn(DocumentList);
    }

    @After
    public void tearDown() {
        filterUtilsMockedConstruction.close();
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDocumentList() throws Exception {
        List<Document> list = pbProductByFilterQueryFetcher.get(environment);
        Assert.assertNotNull(list);
    }
}
