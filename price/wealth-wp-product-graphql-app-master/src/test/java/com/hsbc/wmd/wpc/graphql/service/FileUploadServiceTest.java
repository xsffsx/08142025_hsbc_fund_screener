package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.ApplicationContextConfig;
import com.dummy.wmd.wpc.graphql.constant.Sequence;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
import org.springframework.context.ApplicationContext;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadServiceTest {

    @InjectMocks
    private FileUploadService fileUploadService;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private MongoCollection<Document> colFileUpload;
    @Mock
    private MongoCollection<Document> colFileChunk;
    @Mock
    private FileService fileService;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private MongoCursor cursor;
    @Mock
    private UpdateResult updateResult;

    @Before
    public void setUp() {
        fileUploadService = new FileUploadService(mongodb);
        ReflectionTestUtils.setField(fileUploadService, "colFileUpload", colFileUpload);
        ReflectionTestUtils.setField(fileUploadService, "colFileChunk", colFileChunk);
        ReflectionTestUtils.setField(fileUploadService, "fileService", fileService);
        ReflectionTestUtils.setField(fileUploadService, "sequenceService", sequenceService);
    }

    @Test
    public void testGetUploadFile_givenMd5String_returnsDocument() {
        prepareFileUploadMockdata();
        Document doc_fileUpload = fileUploadService.getUploadFile("124ef345cbcc24df");
        Assert.assertNotNull(doc_fileUpload);
    }

    private void prepareFileUploadMockdata() {
        Document doc = CommonUtils.readResourceAsDocument("/files/file_upload-doc.json");
        Mockito.when(colFileUpload.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(doc);
    }

    @Test
    public void testApprove_omitsArgs_returnsDocument() throws Exception{
        prepareFileUploadMockdata();
        ApplicationContextConfig applicationContextConfig = new ApplicationContextConfig();
        ReflectionTestUtils.setField(applicationContextConfig, "applicationContext", applicationContext);
        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.setProperty("batch.ingress.path", CommonUtils.class.getResource("/files/file.txt").toURI().getPath());
        Mockito.when(applicationContext.getEnvironment()).thenReturn(mockEnvironment);
        Mockito.when(colFileChunk.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.iterator()).thenReturn(cursor);
        Mockito.when(cursor.hasNext()).thenReturn(true).thenReturn(false);
        Document doc_file_chunk = CommonUtils.readResourceAsDocument("/files/file_chunk-doc.json");
        Mockito.when(cursor.next()).thenReturn(doc_file_chunk);
        Mockito.when(colFileUpload.replaceOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        Document document = fileUploadService.approve("approved",12345L, "approved", "comment", "MSUT");
        Assert.assertNotNull(document);
    }

    @Test(expected = productErrorException.class)
    public void testApprove_omitsArgs_throwException1() {
        Mockito.when(colFileUpload.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(null);
        fileUploadService.approve("approved",12345L, "approved", "comment", "MSUT");
    }

    @Test(expected = productErrorException.class)
    public void testApprove_omitsArgs_throwException2() {
        prepareFileUploadMockdata();
        fileUploadService.approve("123",12345L, "approved", "comment", "MSUT");
    }

    @Test(expected = productErrorException.class)
    public void testApprove_omitsArgs_throwException3() {
        Document doc = CommonUtils.readResourceAsDocument("/files/file_upload-doc.json");
        Mockito.when(colFileUpload.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(doc);
        doc.replace("recStatCde", "approved");
        fileUploadService.approve("approvedBy",12345L, "approved", "comment", "MSUT");
    }

    @Test
    public void testNewFilename_givenFileNameAndId_returnsStringName() {
        String newName = FileUploadService.newFilename("hello.ext", 123456);
        Assert.assertEquals("hello123456.ext", newName);
    }

    @Test
    public void testUploadRequestApproval_omitsArgs_returnsDocument() throws Exception {
        MultipartFile file = new MockMultipartFile("file/file.txt",new FileInputStream(CommonUtils.class.getResource("/files/file.txt").toURI().getPath()));
        Mockito.when(fileService.saveFile(any(MultipartFile.class))).thenReturn("13e4e2e3d3553ce553");
        Mockito.when(sequenceService.nextId(Sequence.fileAmendmentId)).thenReturn(1234L);
        Document doc = fileUploadService.uploadRequestApproval("ctryRecCde", "grpMembrRecCde",
                "emplyNum", "uploadType", file, "comments");
        Assert.assertNotNull(doc);
    }

    @Test(expected = productErrorException.class)
    public void testTransferFileToBatch_givenFilePathAndField_throwException() {
        fileUploadService.transferFileToBatch("/file/hello.txt", "field");
    }

    @Test
    public void testGetFilename_givenMultipartFile_returnsName() throws Exception {
        MultipartFile file = new MockMultipartFile("file/f.txt",new FileInputStream(CommonUtils.class.getResource("/files/file.txt").toURI().getPath()));
        ReflectionTestUtils.setField(file, "originalFilename", "file/f.txt");
        String name1 = fileUploadService.getFilename(file);
        Assert.assertEquals("f.txt", name1);
        ReflectionTestUtils.setField(file, "originalFilename", "file\\f.txt");
        String name2 = fileUploadService.getFilename(file);
        Assert.assertEquals("f.txt", name2);
    }
}
