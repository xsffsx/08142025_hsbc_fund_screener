package com.dummy.wpb.product.utils;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonPathUtilsTest {

    Document document = new Document();

    @Before
    public void setUp() {
        Document inner = new Document();
        inner.put("value", "abc");
        document.put("inner", inner);
    }

    @Test
    public void testReadValue() {
        Assert.assertEquals("abc", JsonPathUtils.readValue(document, "inner.value"));
        Assert.assertEquals("abc", JsonPathUtils.readValue(document, "inner.value", "edf"));
        Assert.assertEquals("edf", JsonPathUtils.readValue(document, "inner.value1", "edf"));
        Assert.assertNull(JsonPathUtils.readValue(document, "inner.value1"));
    }

    @Test
    public void testDeleteValue() {
        JsonPathUtils.deleteValue(document, "inner.value");
        Document inner = document.get("inner", Document.class);
        Assert.assertFalse(inner.containsKey("value"));
    }

    @Test
    public void testSetValue() {
        JsonPathUtils.setValue(document, "inner.value", "def");
        Document inner = document.get("inner", Document.class);
        Assert.assertEquals("def", inner.get("value"));

        JsonPathUtils.setValueIfAbsent(document, "inner.value", "qqq");
        inner = document.get("inner", Document.class);
        Assert.assertEquals("def", inner.get("value"));

        JsonPathUtils.setValueIfAbsent(document, "inner.value1", "qqq");
        inner = document.get("inner", Document.class);
        Assert.assertEquals("qqq", inner.get("value1"));

        JsonPathUtils.setValue(document, "inner1.value", "ghi");
        Document inner1 = document.get("inner1", Document.class);
        Assert.assertEquals("ghi", inner1.get("value"));

        JsonPathUtils.setValue(document, "listMap[1].value", "ghi");
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) document.get("listMap");
        Assert.assertEquals(2, listMap.size());
        Assert.assertEquals("ghi", listMap.get(1).get("value"));


        document.put("list", "a");
        JsonPathUtils.setValue(document, "list[0]", "jkl");
        List<String> list = (List<String>) document.get("list");

        document.put("list", new ArrayList<>());
        JsonPathUtils.setValue(document, "list[0]", "jkl");
        Assert.assertEquals("jkl", list.get(0));
    }

    @Test
    public void testCopyValue() {
        Document newDoc = new Document();
        JsonPathUtils.copyValue(document, newDoc, "inner.value");
        Document inner1 = document.get("inner", Document.class);
        Document inner2 = newDoc.get("inner", Document.class);
        Assert.assertEquals(inner1.get("value"), inner2.get("value"));

        inner1.put("value1", null);
        JsonPathUtils.copyValue(document, newDoc, "inner.value1",false);
        Assert.assertFalse(inner2.containsKey("value1"));

        JsonPathUtils.copyValue(document, newDoc, "inner.value1");
        Assert.assertNull(inner2.get("value1"));
    }
}
