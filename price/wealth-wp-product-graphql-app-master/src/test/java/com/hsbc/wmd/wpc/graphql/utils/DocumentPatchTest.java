package com.dummy.wmd.wpc.graphql.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class DocumentPatchTest {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Object> dataMap;

    @Before
    public void setup() throws IOException {
        URL url = DocumentPatchTest.class.getResource("/files/prod1.json");
        dataMap = objectMapper.readValue(url, Map.class);
        log.info("product data loaded");
    }

    @Test
    public void testNoChange_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(0, opList.size());
    }

    @Test
    public void testSimpleChange_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        String newProdName = "dummy HOLDINGS PLC ORDINARY USD0.50 + test";
        docChanged.put(Field.prodName, newProdName);
        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(1, opList.size());
        assertEquals(new OperationInput(Operation.set, "$.prodName", newProdName), opList.get(0));
    }
    @Test
    public void testLeftOnlyKeysChange_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        docChanged.remove(Field.prodName);
        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(1, opList.size());

    }
    @Test
    public void testRightOnlyKeysChange_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(dataMap);
        docBase.remove(Field.prodName);
        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(1, opList.size());

    }

    @Test
    public void testDeleteListItem_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<Map> list = docChanged.getList("altId", Map.class);
        String rowid = "AAAY2PAAAAABXUkAA7";
        Map<String, Object> deleteItem = list.stream().filter(item -> item.get("rowid").equals(rowid)).findFirst().orElse(null);
        List copyList = new ArrayList(list);
        copyList.remove(deleteItem);
        docChanged.put("altId", copyList);

        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(1, opList.size());
        assertEquals(new OperationInput(Operation.delete, "$.altId[?(@.rowid=='" + rowid + "')]", deleteItem), opList.get(0));
    }

    @Test
    public void testAddListItem_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<Map> list = docChanged.getList("altId", Map.class);
        String rowid = "AAAY2PAAAAABXUkAA7";
        Map<String, Object> deleteItem = list.stream().filter(item -> item.get("rowid").equals(rowid)).findFirst().orElse(null);
        List copyList = new ArrayList(list);
        list.remove(deleteItem);
        docChanged.put("altId", copyList);
        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(1, opList.size());
        assertEquals(new OperationInput(Operation.add, "$.altId", deleteItem), opList.get(0));
    }
    @Test
    public void testUpdateListItem_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<Map> list = docBase.getList("altId", Map.class);
        List<Map<String, Object>> copyList = objectMapper.convertValue(list, new TypeReference<List<Map<String, Object>>>(){});
        docChanged.put("altId", copyList);
        Map<String, Object> item = copyList.get(0);
        String prodAltNum = "GB0005405286 - test";
        item.put("prodAltNum", prodAltNum);
        copyList.set(0, new LinkedHashMap(item));

        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(1, opList.size());
        assertEquals(new OperationInput(Operation.set, "$.altId[?(@.rowid=='AAAY2PAAAAABXUkAA5')].prodAltNum", prodAltNum), opList.get(0));
    }

    @Test
    public void testCtryProdTradeCde_givenDocumentAndDocChange_returnsOpList() {
        Document docBase = new Document(dataMap);
        Document docChanged = new Document(docBase);
        List<String> list = docBase.getList("ctryProdTradeCde", String.class);
        List<String> copyList = objectMapper.convertValue(list, new TypeReference<List<String>>(){});
        copyList.add("CNY");
        docChanged.put("ctryProdTradeCde", copyList);

        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList = documentPatch.patch(docBase, docChanged);
        assertEquals(1, opList.size());
        assertEquals(new OperationInput(Operation.set, "$.ctryProdTradeCde", copyList), opList.get(0));
    }
    @Test(expected = InvocationTargetException.class)
    public void testBuildRowidMap_givenList_throwsException() throws Exception {
        DocumentPatch documentPatch = new DocumentPatch();
        Method method = documentPatch.getClass().getDeclaredMethod("buildRowidMap", List.class);
        method.setAccessible(true);
        Map<String,Object> map = new HashMap<>();
        map.put("key","value");
        method.invoke(documentPatch, Arrays.asList(map));
    }
}
