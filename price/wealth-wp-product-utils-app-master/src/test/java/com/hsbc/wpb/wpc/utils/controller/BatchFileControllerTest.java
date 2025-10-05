package com.dummy.wpb.wpc.utils.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class BatchFileControllerTest {

    private BatchFileController batchFileController = new BatchFileController();

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(batchFileController, "rootPath",
                new ClassPathResource("/").getFile().getAbsolutePath());
    }

    @Test
    public void testListDirectoryTree_ValidPath() throws IOException {
         String validPath = new ClassPathResource("data").getFile().getAbsolutePath();
        String rootPath = new ClassPathResource("/").getFile().getAbsolutePath();
         String filePath = rootPath + "/data";

        ResponseEntity<Object> response = batchFileController.listFilesInDirectory(filePath);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testListDirectoryTree_ValidPath_notDirectory() throws IOException {
        String rootPath = new ClassPathResource("/").getFile().getAbsolutePath();
         String filePath = rootPath + "/config_index.yml";

        ResponseEntity<Object> response = batchFileController.listFilesInDirectory(filePath);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testListDirectoryTree_InvalidPath()  throws IOException {
        String rootPath = new ClassPathResource("/").getFile().getAbsolutePath();
         String filePath = rootPath + "/invalidPath";
         ResponseEntity<Object> response = batchFileController.listFilesInDirectory(filePath);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Directory not found"));
    }

    @Test
    public void testListDirectoryTree_nullPath() throws IOException {
        ResponseEntity<Object> response = batchFileController.listFilesInDirectory(null);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<Object> isEmptyresponse = batchFileController.listFilesInDirectory("");
        assertEquals(HttpStatus.OK, isEmptyresponse.getStatusCode());
    }

    @Test
    public void testListDirectoryTree_throwsIOException() throws IOException {
        ReflectionTestUtils.setField(batchFileController, "rootPath", "/invalid/path");

        ResponseEntity<Object> response = batchFileController.listFilesInDirectory("invalidPath");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Entry is outside of the target directory"));
    }


    @Test
    public void testDownloadBatchFile_FileNotFound() throws IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        String rootPath = new ClassPathResource("/").getFile().getAbsolutePath();
        String filePath = rootPath + "/nonExistentFile.txt";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            batchFileController.downloadBatchFile(filePath, response);
        });
        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    public void testDownloadBatchFile_InvalidPath() throws IOException {
        // Arrange
        MockHttpServletResponse response = new MockHttpServletResponse();
        String filePath =  "/nonExistentFile.txt";
        batchFileController.downloadBatchFile(filePath, response);
        assertEquals(500, response.getStatus());
    }

    @Test
    public void testDownloadBatchFile_FileExists() throws IOException {

        String rootPath = new ClassPathResource("/").getFile().getAbsolutePath();
        String filePath = rootPath + "/data/configuration.json";

        MockHttpServletResponse response = new MockHttpServletResponse();
        batchFileController.downloadBatchFile(filePath, response);
        assertEquals(200, response.getStatus());
        assertEquals("attachment;filename=configuration.json", response.getHeader("Content-Disposition"));
    }

    @Test
    public void testListDirectoryTree_ValidPath_notFile() throws IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        String rootPath = new ClassPathResource("/").getFile().getAbsolutePath();
        String filePath = rootPath + "/data";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            batchFileController.downloadBatchFile(filePath, response);
        });
        assertTrue(exception.getMessage().contains("File not found"));
    }

}