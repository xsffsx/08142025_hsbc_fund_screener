package com.dummy.wmd.wpc.graphql.fetcher.product;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
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
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductByFilterQueryFetcherTest {


    private ProductByFilterQueryFetcher productByFilterQueryFetcher;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private FindIterable findIterable;
    @Mock
    private DataFetchingFieldSelectionSet selectionSet;
    @Mock
    private SelectedField selectedField;
    private MockedConstruction<FilterUtils> filterUtilsMockedConstruction;

    @Before
    public void setUp() {
        Mockito.when(mongoDatabase.getCollection(anyString())).thenReturn(collection);
        Mockito.when(collection.find(any(Bson.class))).thenReturn(findIterable);

        productByFilterQueryFetcher = new ProductByFilterQueryFetcher(mongoDatabase);
        filterUtilsMockedConstruction = Mockito.mockConstruction(FilterUtils.class, (filterUtils, context) ->
                Mockito.when(filterUtils.toBsonDocument(anyMap())).thenReturn(new BsonDocument())
        );

        Mockito.when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.skip(any(Integer.class))).thenReturn(findIterable);
        Mockito.when(findIterable.limit(any(Integer.class))).thenReturn(findIterable);
        Mockito.when(findIterable.batchSize(any(Integer.class))).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any())).thenReturn(findIterable);

    }

    @After
    public void tearDown() {
        filterUtilsMockedConstruction.close();
    }

    @Test
    public void testGet_givenEnvironment_returnsSmallDocumentList() throws Exception {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("filter", Collections.emptyMap());
        arguments.put("sort", Collections.emptyMap());
        arguments.put("skip", 0);
        arguments.put("projection", true);

        Mockito.when(selectedField.getQualifiedName())
                .thenReturn("eqtyLinkInvst.undlStock.prodUndlInstm")
                .thenReturn("prodReln.prodRel")
                .thenReturn("prodId")
                .thenReturn("prodAltPrimNum");

        Mockito.when(selectionSet.getFields()).thenReturn(Arrays.asList(selectedField, selectedField, selectedField, selectedField));

        DataFetchingEnvironment environment = new DataFetchingEnvironmentImpl.Builder()
                .arguments(arguments)
                .selectionSet(selectionSet)
                .build();

        List<Document> productList = Collections.singletonList(Document.parse(CommonUtils.readResource("/files/prod1.json")));
        Mockito.when(findIterable.into(any(List.class))).thenReturn(productList);

        List<Document> result = productByFilterQueryFetcher.get(environment);
        Assert.assertEquals(1, result.size());
    }
}
