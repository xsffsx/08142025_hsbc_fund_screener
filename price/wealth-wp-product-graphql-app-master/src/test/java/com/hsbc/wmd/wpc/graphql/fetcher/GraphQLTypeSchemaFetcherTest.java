package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.Schema;
import graphql.schema.*;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class GraphQLTypeSchemaFetcherTest {

    @InjectMocks
    private GraphQLTypeSchemaFetcher graphQLTypeSchemaFetcher;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private GraphQLSchema graphQLSchema;
    @Mock
    private GraphQLScalarType graphQLScalarType;
    @Mock
    private GraphQLInputObjectType graphQLInputObjectType;
    @Mock
    private GraphQLObjectType graphQLObjectType;
    @Mock
    private GraphQLList graphQLList;
    @Mock
    private GraphQLFieldDefinition graphQLFieldDefinition;
    @Mock
    private GraphQLInputObjectField graphQLInputObjectField;
    private MockedConstruction<Schema> schemaMockedConstruction;

    @Before
    public void setUp() throws Exception {
        schemaMockedConstruction = Mockito.mockConstruction(Schema.class);
        Mockito.when(environment.getArgument(anyString())).thenReturn("graphQLType");
        Mockito.when(environment.getGraphQLSchema()).thenReturn(graphQLSchema);
    }

    @After
    public void tearDown() {
        schemaMockedConstruction.close();
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsSchemaList1() throws Exception {
        Mockito.when(graphQLSchema.getType(anyString())).thenReturn(graphQLScalarType);
        List<Schema> schemas = graphQLTypeSchemaFetcher.get(environment);
        Assert.assertNotNull(schemas);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsSchemaList2() throws Exception {
        Mockito.when(graphQLSchema.getType(anyString())).thenReturn(graphQLObjectType);
        List<GraphQLFieldDefinition> list = new ArrayList<>();
        list.add(graphQLFieldDefinition);
        list.add(graphQLFieldDefinition);
        Mockito.when(graphQLObjectType.getFieldDefinitions()).thenReturn(list);
        Mockito.when(graphQLFieldDefinition.getType()).thenReturn(graphQLObjectType).thenReturn(graphQLScalarType);
        Mockito.when(graphQLObjectType.getName()).thenReturn("ProductType");
        List<Schema> schemas = graphQLTypeSchemaFetcher.get(environment);
        Assert.assertNotNull(schemas);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsSchemaList3() throws Exception {
        Mockito.when(graphQLSchema.getType(anyString())).thenReturn(graphQLInputObjectType);
        List<GraphQLInputObjectField> list = new ArrayList<>();
        list.add(graphQLInputObjectField);
        list.add(graphQLInputObjectField);
        Mockito.when(graphQLInputObjectType.getFieldDefinitions()).thenReturn(list);
        Mockito.when(graphQLInputObjectField.getType()).thenReturn(graphQLList).thenReturn(graphQLScalarType);
        Mockito.when(graphQLList.getOriginalWrappedType()).thenReturn(graphQLScalarType);
        List<Schema> schemas = graphQLTypeSchemaFetcher.get(environment);
        Assert.assertNotNull(schemas);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsSchemaList4() throws Exception {
        Mockito.when(graphQLSchema.getType(anyString())).thenReturn(graphQLList);
        Mockito.when(graphQLList.getOriginalWrappedType()).thenReturn(graphQLScalarType);
        List<Schema> schemas = graphQLTypeSchemaFetcher.get(environment);
        Assert.assertNotNull(schemas);
    }
}