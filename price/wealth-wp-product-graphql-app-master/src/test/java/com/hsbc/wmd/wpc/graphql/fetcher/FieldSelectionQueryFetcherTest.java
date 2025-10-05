package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.utils.GraphQLSchemaUtils;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class FieldSelectionQueryFetcherTest {

    @InjectMocks
    private FieldSelectionQueryFetcher fieldSelectionQueryFetcher;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private GraphQLSchema graphQLSchema;
    private MockedConstruction<GraphQLSchemaUtils> graphQLSchemaUtilsMockedStatic;

    @After
    public void tearDown() {
        graphQLSchemaUtilsMockedStatic.close();
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsString() {
        Mockito.when(environment.getArgument(anyString())).thenReturn("queryName").thenReturn(new ArrayList<>());
        Mockito.when(environment.getGraphQLSchema()).thenReturn(graphQLSchema);
        graphQLSchemaUtilsMockedStatic = Mockito.mockConstruction(GraphQLSchemaUtils.class,
                (graphQLSchemaUtils, context) ->
                        Mockito.when(graphQLSchemaUtils.getFieldSelectionOfQuery("queryName")).thenReturn("selection")
        );
        String s = fieldSelectionQueryFetcher.get(environment);
        Assert.assertEquals("selection", s);
    }
}
