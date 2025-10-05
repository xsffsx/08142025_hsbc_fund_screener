package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.model.FinDocBatchCreateResult;
import com.dummy.wmd.wpc.graphql.model.FinDocBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FinDocServiceTest {

    @InjectMocks
    private FinDocService finDocService;
    @Mock
    private MongoCollection<Document> finDocCollection;
    @Mock
    private MongoCollection<Document> sysParmCollection;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private UpdateResult updateResult;

    @Mock
    private LockService lockService;

    @Before
    public void setUp() {
        finDocService = new FinDocService(mongoDatabase);
        ReflectionTestUtils.setField(finDocService, "finDocCollection", finDocCollection);
        ReflectionTestUtils.setField(finDocService, "sysParmCollection", sysParmCollection);
        ReflectionTestUtils.setField(finDocService, "lockService", lockService);
    }

    @Test
    public void testValidate_givenDocument_returnsErrorList() {
        Document doc = CommonUtils.readResourceAsDocument("/files/fin_doc-doc.json");
        Mockito.when(finDocCollection.countDocuments(any(Bson.class))).thenReturn(1L);
        List<Error> errors = finDocService.validate(doc);
        Assert.assertNotNull(errors);
    }

    @Test
    public void testBatchCreateFinDoc_givenListAndBoolean_returnsFinDocBatchCreateResult() {
        Document doc = CommonUtils.readResourceAsDocument("/files/fin_doc-doc.json");
        doc.replace("_id", null);
        List list = new LinkedList();
        list.add(doc);
        Mockito.when(finDocCollection.countDocuments(any(Bson.class))).thenReturn(0L);
        Mockito.when(sysParmCollection.find(any(Bson.class))).thenReturn(findIterable);
        Document sysParam = CommonUtils.readResourceAsDocument("/files/sys_parm-doc.json");
        Mockito.when(findIterable.first()).thenReturn(sysParam);
        Mockito.when(sysParmCollection.replaceOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        FinDocBatchCreateResult fdb = finDocService.batchCreateFinDoc(list, true);
        Assert.assertNotNull(fdb);
    }

    @Test
    public void testBatchUpdateFinDoc_givenDocumentAndOperationAndBoolean_returnsFinDocBatchUpdateResult() {
        Document doc = CommonUtils.readResourceAsDocument("/files/fin_doc-doc.json");
        List<OperationInput> operations = new ArrayList<>();
        OperationInput operationInput = new OperationInput();
        operationInput.setOp(Operation.valueOf("put"));
        operationInput.setPath("$.A");
        operations.add(operationInput);
        ArrayList<Document> documentArrayList = new ArrayList<>();
        documentArrayList.add(doc);
        Mockito.when(finDocCollection.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.into(any(ArrayList.class))).thenReturn(documentArrayList);
        Mockito.when(findIterable.first()).thenReturn(doc);
        FinDocBatchUpdateResult updateFinDoc = finDocService.batchUpdateFinDoc(new Document(), operations, true);
        Assert.assertNotNull(updateFinDoc);
        Mockito.when(finDocCollection.countDocuments(any(Bson.class))).thenReturn(1L);
        FinDocBatchUpdateResult updateFinDoc_2 = finDocService.batchUpdateFinDoc(new Document(), operations, true);
        Assert.assertNotNull(updateFinDoc_2);
    }

    @Test(expected = InvocationTargetException.class)
    public void testBatchUpdate_givenDocument_throwException() throws Exception {
        Document doc = CommonUtils.readResourceAsDocument("/files/fin_doc-doc.json");
        Mockito.when(finDocCollection.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(null);
        Method batchUpdate = finDocService.getClass().getDeclaredMethod("batchUpdate", Document.class);
        batchUpdate.setAccessible(true);
        batchUpdate.invoke(finDocService, doc);
    }

    @Test
    public void testUpdateRsrcItemIdFinDoc() {
        try {
            Method updateRsrcItemIdFinDoc = finDocService.getClass().getDeclaredMethod("updateRsrcItemIdFinDoc", Document.class, Long.class);
            updateRsrcItemIdFinDoc.setAccessible(true);
            updateRsrcItemIdFinDoc.invoke(finDocService, new Document(), 1L);
        } catch (Exception e) {
            Assert.fail("An unexpected exception arise.");
        }
    }

}
