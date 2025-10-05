package com.dummy.wmd.wpc.graphql.utils;

import graphql.schema.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class GraphQLSchemaUtilsTests {

    @Mock
    private GraphQLSchema graphQLSchema;
    @Mock
    private GraphQLFieldDefinition fieldDefinition;
    @Mock
    private GraphQLObjectType graphQLObjectType;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private DataFetchingFieldSelectionSet selectionSet;
    @Mock
    private GraphQLFieldsContainer graphQLFieldsContainer;
    @Mock
    private GraphQLFieldDefinition graphQLFieldDefinition;
    @Mock
    private GraphQLList graphQLList;
    @InjectMocks
    private GraphQLSchemaUtils graphQLSchemaUtils;

    @Before
    public void setUp() throws Exception {
        graphQLSchemaUtils = new GraphQLSchemaUtils(graphQLSchema);
        graphQLSchemaUtils = new GraphQLSchemaUtils(graphQLSchema, Arrays.asList("value"));
    }

    @Test
    public void testGetFieldSelectionOfQuery_givenQueryName_returnsNull() {
        // Setup
        Mockito.when(graphQLSchema.getQueryType()).thenReturn(graphQLObjectType);
        Mockito.when(graphQLObjectType.getFieldDefinition(any(String.class))).thenReturn(null);
        // Run the test
        String fieldSelection= graphQLSchemaUtils.getFieldSelectionOfQuery("queryName");
        assertNull(fieldSelection);
    }
    @Test
    public void testGetFieldSelectionOfQuery_givenQueryName_returnsString() {
        // Setup
        Mockito.when(graphQLSchema.getQueryType()).thenReturn(graphQLObjectType);
        Mockito.when(graphQLObjectType.getFieldDefinition(any(String.class))).thenReturn(fieldDefinition);
        // Run the test
        String fieldSelection= graphQLSchemaUtils.getFieldSelectionOfQuery("queryName");
        assertNotNull(fieldSelection);
    }


    @Test
    public void testGetSelectedFields_givenEnvironment_returnsSet() {
        // Setup
        Mockito.when(environment.getSelectionSet()).thenReturn(selectionSet);
        Mockito.when(selectionSet.getFields()).thenReturn(new ArrayList<>());
        // Run the test
        Set<String> result = GraphQLSchemaUtils.getSelectedFields(environment);
        assertNotNull(result);
    }

    @Test
    public void testGetFieldSelection_givenGraphQLFieldsContainer_returnsString() throws Exception {
        Method method = graphQLSchemaUtils.getClass().getDeclaredMethod("getFieldSelection",GraphQLOutputType.class);
        method.setAccessible(true);
        Mockito.when(graphQLFieldsContainer.getName()).thenReturn("graphQLFieldsContainer");
        List<GraphQLFieldDefinition> fieldDefinitions= new ArrayList<>();
        fieldDefinitions.add(graphQLFieldDefinition);
        Mockito.when(graphQLFieldsContainer.getFieldDefinitions()).thenReturn(fieldDefinitions);
        assertNotNull(method.invoke(graphQLSchemaUtils,  graphQLFieldsContainer));
    }
    @Test
    public void testGetFieldSelection_givenGraphQLList_returnsString() throws Exception {
        Method method = graphQLSchemaUtils.getClass().getDeclaredMethod("getFieldSelection",GraphQLOutputType.class);
        method.setAccessible(true);
        Mockito.when(graphQLFieldsContainer.getName()).thenReturn("graphQLList");
        Mockito.when(graphQLList.getWrappedType()).thenReturn(graphQLFieldsContainer);
        List<GraphQLFieldDefinition> fieldDefinitions= new ArrayList<>();
        fieldDefinitions.add(graphQLFieldDefinition);
        Mockito.when(graphQLFieldsContainer.getFieldDefinitions()).thenReturn(fieldDefinitions);
        assertNotNull(method.invoke(graphQLSchemaUtils,  graphQLList));
    }
    @Test
    public void testGetFieldSelection_givenGraphQLFieldsContainer_returnsNull() throws Exception {
        Method method = graphQLSchemaUtils.getClass().getDeclaredMethod("getFieldSelection",GraphQLOutputType.class);
        method.setAccessible(true);
        Mockito.when(graphQLFieldsContainer.getName()).thenReturn("fieldsContainer");
        Stack<String> typeNameStack = new Stack<>();
        typeNameStack.push("fieldsContainer");
        ReflectionTestUtils.setField(graphQLSchemaUtils, "typeNameStack", typeNameStack);
        assertNull(method.invoke(graphQLSchemaUtils,  graphQLFieldsContainer));
    }

}
