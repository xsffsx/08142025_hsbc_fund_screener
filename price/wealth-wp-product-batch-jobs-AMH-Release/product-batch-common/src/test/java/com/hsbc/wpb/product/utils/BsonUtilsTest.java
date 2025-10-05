package com.dummy.wpb.product.utils;

import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BsonUtilsTest {
    @Test
    void testToJson_ReturnsJsonString_WhenValidBson() {
        Bson bson = BsonDocument.parse("{\"key\": \"value\"}");
        String json = BsonUtils.toJson(bson);
        assertEquals("{\"key\": \"value\"}", json);
    }

    @Test
    void testToJson_ReturnsEmptyJsonString_WhenEmptyBson() {
        Bson bson = new BsonDocument();
        String json = BsonUtils.toJson(bson);
        assertEquals("{}", json);
    }

    @Test
    void testToMap_ReturnsMap_WhenValidBson() {
        Bson bson = BsonDocument.parse("{\"key\": \"value\"}");
        Map<String, Object> map = BsonUtils.toMap(bson);
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));
    }

    @Test
    void testToMap_ReturnsEmptyMap_WhenEmptyBson() {
        Bson bson = new BsonDocument();
        Map<String, Object> map = BsonUtils.toMap(bson);
        assertTrue(map.isEmpty());
    }

}