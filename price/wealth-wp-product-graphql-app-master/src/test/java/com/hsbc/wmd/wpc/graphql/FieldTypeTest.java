package com.dummy.wmd.wpc.graphql;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FieldTypeTest {

    private FieldType fieldType;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "map");
        Map<String, Object> typeMap = new HashMap<>();
        typeMap.put("kind", "SCALAR");
        typeMap.put("name", "typeMap");
        Map<String, String> ofType = new HashMap<>();
        ofType.put("name", "ofType");
        ofType.put("kind", "LIST");
        typeMap.put("ofType", ofType);
        map.put("type", typeMap);
        fieldType = new FieldType(map);
    }

    @Test
    public void testFieldType() {
        assertNotNull(fieldType.toString());
    }

    @Test
    public void testIsScalar_givenNull_returnsBoolean() {
        assertTrue(fieldType.isScalar());
    }

    @Test
    public void testIsList_givenNull_returnsBoolean() {
        assertFalse(fieldType.isList());
    }

    @Test
    public void testIsObject_givenNull_returnsBoolean() {
        assertFalse(fieldType.isObject());
    }

}
