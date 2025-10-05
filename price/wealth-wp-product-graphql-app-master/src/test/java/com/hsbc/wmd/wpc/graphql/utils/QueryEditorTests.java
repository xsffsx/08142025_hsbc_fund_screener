package com.dummy.wmd.wpc.graphql.utils;

import graphql.schema.GraphQLSchema;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class QueryEditorTests {

    @Mock
    private GraphQLSchema mockSchema;
    @Mock
    private GraphQLSchemaUtils schemaUtils;

    private QueryEditor queryEditorUnderTest;

    @Before
    public void setUp() throws Exception {
        queryEditorUnderTest = new QueryEditor(Arrays.asList("key", "value"), mockSchema);
    }

    @Test
    public void testLateInsert_givenArgs_returnValue() {
        LateInsert lateInsert = new LateInsert(1, "value");
        assertEquals(1,lateInsert.getPos());
        assertEquals("value",lateInsert.getFieldSelection());
        assertNotNull(lateInsert.toString());
    }

    @Test
    public void testExpandFieldSelection_givenString_returnsString() {
        ReflectionTestUtils.setField(queryEditorUnderTest, "schemaUtils", schemaUtils);
        Mockito.when(schemaUtils.getFieldSelectionOfQuery(anyString())).thenReturn("fieldSelection");
        String query = "query{key{value(test)}}}";
        String result = queryEditorUnderTest.expandFieldSelection(query);
        assertNotNull(result);
    }

    @Test
    public void testTrimQueryName_givenString_returnsString() throws Exception {
        String text = "queryName";
        List<String> retrieveAllFieldsFor = new ArrayList<>();
        QueryEditor queryEditor = new QueryEditor(retrieveAllFieldsFor, mockSchema);
        Method method = queryEditor.getClass().getDeclaredMethod("trimQueryName", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(queryEditor, text);
        assertNotNull(result);
    }

    @Test(expected = InvocationTargetException.class)
    public void testTrimQueryName_givenString_throwsException() throws Exception {
        String text = "";
        List<String> retrieveAllFieldsFor = new ArrayList<>();
        QueryEditor queryEditor = new QueryEditor(retrieveAllFieldsFor, mockSchema);
        Method method = queryEditor.getClass().getDeclaredMethod("trimQueryName", String.class);
        method.setAccessible(true);
        method.invoke(queryEditor, text);
    }
}
