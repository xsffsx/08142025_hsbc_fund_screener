package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ApprovalAction;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.FindIterable;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoCollection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FinDocHistChangeServiceTest {

    @InjectMocks
    private FinDocHistChangeService finDocHistChangeService;
    @Mock
    private MongoCollection<Document> colFinDocHist;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private UpdateResult updateResult;

    @Mock
    private FinDocService finDocService;

    @Mock
    private FinDocRelService finDocRelService;

    private Document amendmentDoc;

    private Document finDocHistDoc;

    @Mock
    private LockService lockService;

    @Before
    public void setUp() {
        finDocHistChangeService = new FinDocHistChangeService();
        ReflectionTestUtils.setField(finDocHistChangeService, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(finDocHistChangeService, "lockService", lockService);
        ReflectionTestUtils.setField(finDocHistChangeService, "finDocService", finDocService);
        ReflectionTestUtils.setField(finDocHistChangeService, "finDocRelService", finDocRelService);
        ReflectionTestUtils.setField(finDocHistChangeService, "rejLogPath", "src/test/resources/ENS/");
        ReflectionTestUtils.setField(finDocHistChangeService, "chmodScriptPath", "src/main/java/com/dummy/wmd/wpc/graphql/script/ChmodScript.java");
        amendmentDoc = CommonUtils.readResourceAsDocument("/files/amendment-fin_doc_hist.json");
        finDocHistDoc = CommonUtils.readResourceAsDocument("/files/fin_doc_hist-doc.json");

        Mockito.when(mongoTemplate.getCollection(CollectionName.fin_doc_hist.name())).thenReturn(colFinDocHist);
        Mockito.when(colFinDocHist.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.doNothing().when(finDocRelService).updateFinDocUpload(any(Document.class), any(ApprovalAction.class), any());
    }

    @Test
    public void testValidate_givenDocument_returnsErrorList() {
        Document doc = amendmentDoc.get("doc", Document.class);
        doc.put("rsrcItemIdFinDoc",0L);

        Mockito.when(mongoTemplate.getCollection(CollectionName.fin_doc_hist.name()).countDocuments(any(Bson.class))).thenReturn(1L);
        List<Error> errors =  finDocHistChangeService.validate(amendmentDoc);
        Assert.assertNotNull(errors);
    }

    @Test
    public void testAdd_givenDocument_DoesNotReturn() {
        Document doc = CommonUtils.readResourceAsDocument("/files/sys_parm-doc.json");
        /*Mockito.when(sysParmCollection.find(any(Bson.class))).thenReturn(findIterable);*/
        Mockito.when(finDocService.getDocSerNumDoc(any(), any())).thenReturn(doc);
        Mockito.doNothing().when(finDocRelService).updateOrInsertPDRL(any(Document.class));
        try {
            finDocHistChangeService.add(finDocHistDoc);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testUpdate_givenDocument_DoesNotThrow() {
        Mockito.when(findIterable.first()).thenReturn(finDocHistDoc);
        Mockito.when(colFinDocHist.replaceOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        finDocHistChangeService.update(finDocHistDoc);
        Assert.assertNotNull(finDocHistDoc);
    }

    @Test(expected = productErrorException.class)
    public void testUpdate_givenDocument_throwException() {
        Mockito.when(findIterable.first()).thenReturn(null);
        finDocHistChangeService.update(finDocHistDoc);
    }

    @Test
    public void testDelete_givenDocument_DoesNotThrow() {
        DeleteResult deleteResult = Mockito.mock(DeleteResult.class);
        Mockito.when(mongoTemplate.getCollection(CollectionName.fin_doc_hist.name()).deleteOne(any(Bson.class))).thenReturn(deleteResult);
        finDocHistChangeService.delete(finDocHistDoc);
        Assert.assertNotNull(finDocHistDoc);
    }

    @Test
    public void testFindFirst_givenBsonFilter_returnsDocument() {
        Mockito.when(findIterable.first()).thenReturn(new Document());
        Document doc = finDocHistChangeService.findFirst(new BsonDocument());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testRejectOpation_givenReject_genErrorEmailFile_doesNotThrowAndDeleteFile() {
        try {
            finDocHistChangeService.rejectOpation(amendmentDoc);
            delectEmailFiles("src/test/resources/ENS/");
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    private void delectEmailFiles(String path) throws IOException {
        File folder = new File(path);
        if (folder.exists()) {
            String[] fileNames = folder.list();
            for (String fileName : fileNames) {
                File file = new File(path, fileName);
                Files.delete(file.toPath());
            }
        }
    }
}
