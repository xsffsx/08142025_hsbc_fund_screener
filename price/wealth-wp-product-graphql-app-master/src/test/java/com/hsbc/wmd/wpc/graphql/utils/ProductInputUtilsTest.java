package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.constant.Field;
import graphql.schema.*;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static graphql.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ProductInputUtilsTest {
    @Mock
    private static GraphQLSchema graphQLSchema;
    @Mock
    private static GraphQLInputObjectType inputObjectType;

    @Test
    public void testSupplementMissingFields_givenDocument_returnsMap() {
        MockedStatic<GraphQLProvider> providerMockedStatic = Mockito.mockStatic(GraphQLProvider.class);
        providerMockedStatic.when(() -> GraphQLProvider.getGraphQLSchema()).thenReturn(graphQLSchema);
        Mockito.when(graphQLSchema.getType("ProductInput")).thenReturn(inputObjectType);
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40004997.json");
        Map<String, Object> map = ProductInputUtils.supplementMissingFields(doc);
        providerMockedStatic.close();
        assertNotNull(map);
    }

    @Test
    public void testSupplementFieldsForList() throws Exception {
        Constructor<ProductInputUtils> productInputUtilsConstructor = ProductInputUtils.class.getDeclaredConstructor();
        productInputUtilsConstructor.setAccessible(true);
        ProductInputUtils productInputUtils = productInputUtilsConstructor.newInstance();
        Method method = productInputUtils.getClass().getDeclaredMethod("supplementFieldsForList", List.class, GraphQLInputObjectType.class);
        method.setAccessible(true);
        List list = new ArrayList<>();
        list.add("string");
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        list.add(map);
        List result = (List) method.invoke(productInputUtils, list, inputObjectType);
        assertNotNull(result);
    }

    @Test
    public void supplementFieldsForMap_givenMapAndGraphQLInputObjectType_returnMap() throws Exception {
        Constructor<ProductInputUtils> productInputUtilsConstructor = ProductInputUtils.class.getDeclaredConstructor();
        productInputUtilsConstructor.setAccessible(true);
        ProductInputUtils productInputUtils = productInputUtilsConstructor.newInstance();
        Method method = productInputUtils.getClass().getDeclaredMethod("supplementFieldsForMap", Map.class, GraphQLInputObjectType.class);
        method.setAccessible(true);
        Document doc = CommonUtils.readResourceAsDocument("/files/UT-40004997.json");
        doc.replace("recCreatDtTm", null);
        doc.replace("recUpdtDtTm", null);
        doc.replace("rowid", null);
        List<GraphQLInputObjectField> fields = new ArrayList<>();
        List<GraphQLInputObjectField> fields2 = new ArrayList<>();
        GraphQLInputObjectField objectType = Mockito.mock(GraphQLInputObjectField.class);
        fields.add(objectType);
        fields.add(objectType);
        fields.add(objectType);
        fields.add(objectType);
        fields.add(objectType);
        fields2.add(objectType);
        fields2.add(objectType);
        Mockito.when(inputObjectType.getFields()).thenReturn(fields).thenReturn(fields2);
        GraphQLScalarType graphQLScalarType = Mockito.mock(GraphQLScalarType.class);
        GraphQLList graphQLList = Mockito.mock(GraphQLList.class);
        Mockito.when(objectType.getType()).thenReturn(graphQLScalarType).
                thenReturn(graphQLScalarType).thenReturn(graphQLScalarType).
                thenReturn(graphQLList).thenReturn(inputObjectType).
                thenReturn(graphQLScalarType).thenReturn(inputObjectType);
        Mockito.when(objectType.getName()).thenReturn(Field.recCreatDtTm).
                thenReturn(Field.recUpdtDtTm).thenReturn(Field.rowid).
                thenReturn(Field.finDoc).thenReturn(Field.createdBy).thenReturn(Field.recCreatDtTm).thenReturn("performance");
        Mockito.when(graphQLList.getOriginalWrappedType()).thenReturn(inputObjectType);
        Map result = (Map) method.invoke(productInputUtils, doc, inputObjectType);

        Assert.assertNotNull(result);
    }

}
