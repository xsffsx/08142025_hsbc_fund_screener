package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import graphql.schema.*;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FilterUtilsTest {

    @Mock
    private GraphQLObjectType mockGraphQLType;
    @Mock
    private GraphQLSchema graphQLSchema;

    @Mock
    private GraphQLFieldDefinition field;
    @Mock
    private GraphQLNonNull graphQLNonNull;
    @Mock
    private GraphQLScalarType graphQLScalarType;
    @Mock
    private GraphQLList graphQLList;
    @Mock
    private BsonValue bsonValue;
    @Mock
    private BsonString bsonString;

    private FilterUtils filterUtils;


    @Before
    public void setUp() throws Exception {
        filterUtils = new FilterUtils(mockGraphQLType);
    }

    @Test
    public void testFilterUtils_givenCollectionName_returnsTypeName() {
        try (MockedStatic<GraphQLProvider> provider = Mockito.mockStatic(GraphQLProvider.class)) {
            provider.when(GraphQLProvider::getGraphQLSchema).thenReturn(graphQLSchema);
            Mockito.when(graphQLSchema.getType("AmendmentType")).thenReturn(mockGraphQLType);
            new FilterUtils(CollectionName.amendment);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetTypeName_givenCollectionName_returnsTypeName() {
        String typeName = FilterUtils.getTypeName(CollectionName.amendment);
        assertEquals("AmendmentType", typeName);
    }

    @Test(expected = productErrorException.class)
    public void testGetTypeName_givenCollectionName_throwsException() {
        FilterUtils.getTypeName(CollectionName.test_user);
    }


    @Test
    public void testToBsonDocument_givenFilter_returnsBsonDocument() {
        // Setup
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("ctryRecCde", "HK");
        filterMap.put("grpMembrRecCde", "HASE");
        filterMap.put("testDate", new Date());
        filterMap.put("float", 0.11F);
        filterMap.put("long", 1L);

        // Run the test
        BsonDocument result = filterUtils.toBsonDocument(filterMap);
        filterMap.put("bson", result);
        BsonArray list = new BsonArray();
        list.add(result);
        filterMap.put("bsonList", list);
        result = filterUtils.toBsonDocument(filterMap);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testGetTypeBySchema_giveFullNameAndGraphQLNonNull_returnsNull() throws Exception {
        Method method = getTypeBySchema();
        Mockito.when(mockGraphQLType.getFieldDefinition(any(String.class))).thenReturn(field);
        Mockito.when(field.getType()).thenReturn(graphQLNonNull);
        assertNull(method.invoke(filterUtils, "AmendmentType", mockGraphQLType));
    }

    @Test
    public void testGetTypeBySchema_giveFullNameAndGraphQLScalarType_returnsString() throws Exception {
        Method method = getTypeBySchema();
        Mockito.when(mockGraphQLType.getFieldDefinition(any(String.class))).thenReturn(field);
        Mockito.when(field.getType()).thenReturn(graphQLNonNull);
        Mockito.when(graphQLNonNull.getWrappedType()).thenReturn(graphQLScalarType);
        Mockito.when(graphQLScalarType.getName()).thenReturn("graphQLScalarType");
        String reslut = (String) method.invoke(filterUtils, "AmendmentType", mockGraphQLType);
        assertEquals("graphQLScalarType", reslut);
    }

    @Test
    public void testGetTypeBySchema_giveFullNameAndGraphQLList_returnsNull() throws Exception {
        Method method = getTypeBySchema();
        Mockito.when(mockGraphQLType.getFieldDefinition(any(String.class))).thenReturn(field);
        Mockito.when(field.getType()).thenReturn(graphQLList);
        assertNull(method.invoke(filterUtils, "AmendmentType.second", mockGraphQLType));
    }

    @Test
    public void testGetTypeBySchema_giveFullNameGraphQLListAndGraphQLType_returnsNull() throws Exception {
        Method method = getTypeBySchema();
        Mockito.when(mockGraphQLType.getFieldDefinition(any(String.class))).thenReturn(field);
        Mockito.when(field.getType()).thenReturn(graphQLList);
        Mockito.when(graphQLList.getWrappedType()).thenReturn(mockGraphQLType);
        assertNull(method.invoke(filterUtils, "AmendmentType.second", mockGraphQLType));
    }

    private Method getTypeBySchema() throws Exception {
        Method method = filterUtils.getClass()
                                   .getDeclaredMethod("getTypeBySchema", String.class, GraphQLObjectType.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    public void testReviseBsonValue_givenValueAndType_returnsBsonValue() throws Exception {
        Method method = filterUtils.getClass().getDeclaredMethod("reviseBsonValue", BsonValue.class, String.class);
        method.setAccessible(true);
        Mockito.when(bsonValue.isString()).thenReturn(true);
        Mockito.when(bsonValue.asString()).thenReturn(bsonString);

        Mockito.when(bsonString.getValue()).thenReturn("2023-09-01");
        assertNotNull(method.invoke(filterUtils, bsonValue, "Date"));
        assertNotNull(method.invoke(filterUtils, bsonValue, "DateTime"));
        Mockito.when(bsonString.getValue()).thenReturn("1");
        assertNotNull(method.invoke(filterUtils, bsonValue, "Long"));
        Mockito.when(bsonString.getValue()).thenReturn("1.00");
        assertNotNull(method.invoke(filterUtils, bsonValue, "Float"));
    }


}
