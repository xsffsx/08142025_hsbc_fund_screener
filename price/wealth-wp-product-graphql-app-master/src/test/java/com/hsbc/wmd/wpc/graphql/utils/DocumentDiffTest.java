package com.dummy.wmd.wpc.graphql.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.DiffType;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.Assert.*;

@Slf4j
public class DocumentDiffTest {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Object> dataMap;

    @Before
    public void setup() throws IOException {
        URL url = DocumentDiffTest.class.getResource("/files/prod1.json");
        dataMap = objectMapper.readValue(url, Map.class);
    }

    @Test
    public void testNoChange_givenDocuments_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);

        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(0, diffList.size());
    }

    @Test
    public void testSimpleChange_givenDocuments_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        String oldProdName = docChanged.getString(Field.prodName);
        String newProdName = "dummy HOLDINGS PLC ORDINARY USD0.50 + test";
        docChanged.put(Field.prodName, newProdName);

        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(1, diffList.size());
        assertEquals(new DiffType("$.prodName", oldProdName, newProdName), diffList.get(0));
    }

    @Test
    public void testSimpleChange_leftOnly_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        docChanged.remove(Field.allowTradProdInd);
        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(1, diffList.size());
    }

    @Test
    public void testSimpleChange_rightOnly_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        docBase.remove(Field.allowTradProdInd);
        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(1, diffList.size());
    }

    @Test
    public void testDeleteListItem_givenDocuments_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<Map> list = docChanged.getList("altId", Map.class);
        String rowid = "AAAY2PAAAAABXUkAA7";
        Map<String, Object> theItem = list.stream().filter(item -> item.get("rowid").equals(rowid)).findFirst().orElse(null);
        List copyList = new ArrayList(list);
        copyList.remove(theItem);
        docChanged.put("altId", copyList);

        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(1, diffList.size());
        assertEquals(new DiffType("$.altId[?(@.rowid=='" + rowid + "')]", theItem, null), diffList.get(0));
    }

    @Test
    public void testAddListItem_givenDocuments_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<Map> list = docChanged.getList("altId", Map.class);
        String rowid = "AAAY2PAAAAABXUkAA7";
        Map<String, Object> theItem = list.stream().filter(item -> item.get("rowid").equals(rowid)).findFirst().orElse(null);
        List copyList = new ArrayList(list);
        list.remove(theItem);
        docChanged.put("altId", copyList);

        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(1, diffList.size());
        assertEquals(new DiffType("$.altId[?(@.rowid=='" + rowid + "')]", null, theItem), diffList.get(0));
    }

    @Test
    public void testUpdateListItem_givenDocuments_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<Map> list = docBase.getList("altId", Map.class);
        List<Map<String, Object>> copyList = objectMapper.convertValue(list, new TypeReference<List<Map<String, Object>>>() {
        });
        docChanged.put("altId", copyList);
        Map<String, Object> item = copyList.get(0);
        Object oldProdAltNum = item.get("prodAltNum");
        String newProdAltNum = "GB0005405286 - test";
        item.put("prodAltNum", newProdAltNum);
        copyList.set(0, new LinkedHashMap(item));

        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(1, diffList.size());
        assertEquals(new DiffType("$.altId[?(@.rowid=='AAAY2PAAAAABXUkAA5')].prodAltNum", oldProdAltNum, newProdAltNum), diffList.get(0));
    }

    @Test
    public void testCtryProdTradeCde_givenDocuments_returnsDiffList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<String> leftList = docBase.getList("ctryProdTradeCde", String.class);
        List<String> righList = objectMapper.convertValue(leftList, new TypeReference<List<String>>() {
        });
        righList.add("CNY");
        docChanged.put("ctryProdTradeCde", righList);

        List<DiffType> diffList = DocumentDiff.diff(docBase, docChanged);
        assertEquals(1, diffList.size());
        assertEquals(new DiffType("$.ctryProdTradeCde", leftList, righList), diffList.get(0));
    }
    @Test
    public void testRectifyDateTimeType_givenObject_returnsObject() throws Exception {
        Method method = DocumentDiff.class.getDeclaredMethod("rectifyDateTimeType", Object.class);
        method.setAccessible(true);
        assertNotNull(method.invoke(null, "value"));
        assertNotNull(method.invoke(null, new Date()));
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        assertNotNull(method.invoke(null, offsetDateTime));
        LocalDateTime localDateTime = LocalDateTime.now();
        assertNotNull(method.invoke(null, localDateTime));
        LocalDate localDate = LocalDate.now();
        assertNotNull(method.invoke(null, localDate));
    }
    @Test
    public void testIsStringList_givenString_returnFalse() throws Exception{
        Method method = DocumentDiff.class.getDeclaredMethod("isStringList", Object.class);
        method.setAccessible(true);
        assertFalse((Boolean) method.invoke(null, "value"));
    }
    @Test
    public void testDiffStringList_givenStringAndNullLists_returnList() throws Exception{
        Method method = DocumentDiff.class.getDeclaredMethod("diffStringList", String.class,List.class,List.class);
        method.setAccessible(true);
        List<DiffType> result = (List<DiffType>) method.invoke(null, "path",null,null);
        assertEquals(0L,result.size());
    }
    @Test
    public void testDiffStringList_givenStringAndLists_returnList() throws Exception{
        Method method = DocumentDiff.class.getDeclaredMethod("diffStringList", String.class,List.class,List.class);
        method.setAccessible(true);
        List<String> obj = Arrays.asList("value");
        List<DiffType> result = (List<DiffType>) method.invoke(null, "path",obj,null);
        assertEquals(1L,result.size());
    }
    @Test(expected = InvocationTargetException.class)
    public void testBuildRowidMap() throws Exception{
        Method method = DocumentDiff.class.getDeclaredMethod("buildRowidMap",List.class);
        method.setAccessible(true);
        Map<String, Object> map = new HashMap<>();
        map.put("key","value");
        method.invoke(null, Arrays.asList(map));
    }

}
