package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.result.InsertOneResult;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;
    @Mock
    private MongoCollection<Document> colFile;
    @Mock
    private MongoCollection<Document> colFileChunk;
    @Mock
    private MongoDatabase mongodb;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private MongoCursor cursor;
    @Mock
    private InsertOneResult insertOneResult;

    @Before
    public void setUp() {
        fileService = new FileService(mongodb);
        ReflectionTestUtils.setField(fileService, "colFile", colFile);
        ReflectionTestUtils.setField(fileService, "colFileChunk", colFileChunk);
    }

    @Test
    public void testSaveFile_givenMultipartFile_DoesNotThrow(){
        try {
            MockMultipartFile mockMultipartFile = new MockMultipartFile("mockFile", new FileInputStream(CommonUtils.class.getResource("/files/file-chunk.json").toURI().getPath()));
            Mockito.when(colFile.countDocuments(any(Bson.class))).thenReturn(2L);
            Mockito.when(colFile.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class))).thenReturn(new Document());
            fileService.saveFile(mockMultipartFile);

            Mockito.when(colFile.countDocuments(any(Bson.class))).thenReturn(0L);
            Mockito.when(colFileChunk.insertOne(any(Document.class))).thenReturn(insertOneResult);
            fileService.saveFile(mockMultipartFile);
            Assert.assertTrue(mockMultipartFile.getSize()>0);
        } catch ( Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = productErrorException.class)
    public void testSaveFile_givenMultipartFile_throwException() throws Exception {
        MockMultipartFile mockMultipartFile = Mockito.mock(MockMultipartFile.class);
        Mockito.when(mockMultipartFile.getBytes()).thenThrow(new IOException());
        fileService.saveFile(mockMultipartFile);
    }

    @Test
    public void testRetrieveFile_givenMd5StringAndFileOutputStream_DoesNotThrow() {
        try {
            String md5 = "skngaiweugig13";
            FileOutputStream fileOutputStream = new FileOutputStream(CommonUtils.class.getResource("/files/file-chunk.txt").toURI().getPath());
            Mockito.when(colFileChunk.find(any(Bson.class))).thenReturn(findIterable);
            Mockito.when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
            Mockito.when(findIterable.iterator()).thenReturn(cursor);
            Mockito.when(cursor.hasNext()).thenReturn(true).thenReturn(false);
            Document doc = CommonUtils.readResourceAsDocument("/files/file-chunk.json");
            Mockito.when(cursor.next()).thenReturn(doc);
            fileService.retrieveFile(md5, fileOutputStream);
            Assert.assertNotNull(fileOutputStream);
        } catch ( Exception e) {
            Assert.fail();
        }
    }
}
