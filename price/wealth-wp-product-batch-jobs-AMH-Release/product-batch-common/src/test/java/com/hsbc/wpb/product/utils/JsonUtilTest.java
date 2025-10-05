package com.dummy.wpb.product.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.exception.productBatchException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest {

    @Test
    void testConvertObject2Json() throws JsonProcessingException {
        Map<String, String> object = new HashMap<>();
        object.put("key", "value");
        String json = JsonUtil.convertObject2Json(object);
        assertEquals("{\"key\":\"value\"}", json);
    }


    @Test
    void testConvertJson2Object_ThrowsproductBatch() {
        String json = "invalid json";
        TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
        productBatchException exception = assertThrows(productBatchException.class, () -> {
            JsonUtil.convertJson2Object(json, typeRef);
        });
        assertTrue(exception.getMessage().contains("An error occurred when parsing JSON"));
    }

    @Test
    void testConvertObject2Json_ThrowsJsonProcessingException() {
        Object invalidObject = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Invalid object");
            }
        };
        assertThrows(JsonProcessingException.class, () -> {
            JsonUtil.convertObject2Json(invalidObject);
        });
    }

    @Test
    void testConvertJson2Object_ReturnsObject_WhenValidJson() {
        String json = "{\"key\":\"value\"}";
        Map<String, String> result = JsonUtil.convertJson2Object(json, new TypeReference<Map<String, String>>() {});
        assertEquals("value", result.get("key"));
    }


    @Test
    void testDeepCopy() {
        Map<String, String> original = new HashMap<>();
        original.put("key", "value");
        Map<String, String> copy = JsonUtil.deepCopy(original);
        assertNotSame(original, copy);
        assertEquals(original, copy);
    }

    @Test
    void testDeepCopyJsonProcessingException() {
        Object invalidObject = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Invalid object");
            }
        };
        TypeReference<Object> typeRef = new TypeReference<Object>() {};
        productBatchException exception = assertThrows(productBatchException.class, () -> {
            JsonUtil.deepCopy(invalidObject, typeRef);
        });
        assertNotNull(exception);
    }


    @Test
    void testDeepCopyIOException() {
        Object invalidObject = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Invalid object");
            }
        };
        productBatchException exception = assertThrows(productBatchException.class, () -> {
            JsonUtil.deepCopy(invalidObject);
        });
        assertNotNull(exception);
    }

    @Test
    void testDeepCopy_ReturnsNull_WhenNullObject() {
        Map<String, String> copy = JsonUtil.deepCopy(null);
        assertNull(copy);
    }

    @Test
    void testDeepCopyWithTypeReference_ReturnsDeepCopy_WhenValidObject() {
        Map<String, String> original = new HashMap<>();
        original.put("key", "value");
        Map<String, String> copy = JsonUtil.deepCopy(original, new TypeReference<Map<String, String>>() {});
        assertNotSame(original, copy);
        assertEquals(original, copy);
    }

    @Test
    void testConvertType_ReturnsConvertedObject_WhenValidObject() throws IOException {
        Map<String, String> original = new HashMap<>();
        original.put("key", "value");
        Map<String, String> result = JsonUtil.convertType(original, new TypeReference<Map<String, String>>() {});
        assertEquals("value", result.get("key"));
    }

    @Test
    void testConvertType_ThrowsIOException_WhenInvalidObject() {
        Object invalidObject = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Invalid object");
            }
        };
        assertThrows(IOException.class, () -> {
            JsonUtil.convertType(invalidObject, new TypeReference<Map<String, String>>() {});
        });
    }
}