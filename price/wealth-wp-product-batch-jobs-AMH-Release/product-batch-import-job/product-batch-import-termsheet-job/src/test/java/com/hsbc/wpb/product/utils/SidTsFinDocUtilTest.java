package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.ImportTermsheetService;
import com.dummy.wpb.product.configuration.TermsheetConfiguration;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.productBatchException;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;

public class SidTsFinDocUtilTest {

    private final String CTRY_REC_CDE = "CTRY_REC_CDE";
    private final String GRP_MEMBR_REC_CDE = "GRP_MEMBR_REC_CDE";
    private final String PROD_TYPE_CDE = "PROD_TYPE_CDE";

    private File directory = new File("src/test/resources/test/SID_termsheet_pdf");

    private TermsheetConfiguration termsheetConfiguration = Mockito.mock(TermsheetConfiguration.class);

    private ImportTermsheetService importTermsheetService = Mockito.mock(ImportTermsheetService.class);

    @Test
    public void testPostProcess_givenInvalidFiles() throws Exception {
        Document product = new Document();
        product.put(Field.parmValueText, "123456");
        Mockito.when(importTermsheetService.isValidIncomingFile(any(), any())).thenReturn(false);
        Mockito.when(importTermsheetService.queryProductByCode(eq(CTRY_REC_CDE), eq(GRP_MEMBR_REC_CDE), eq(PROD_TYPE_CDE), anyString())).thenReturn(product);
        SidTsFinDocUtil.postProcess(directory, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_TYPE_CDE, importTermsheetService, termsheetConfiguration);
        Assert.assertNotNull(directory);
    }

    @Test
    public void testPostProcess_givenValidFiles() throws Exception {
        Document product = new Document();
        product.put(Field.parmValueText, "123456");
        List<File> list = new ArrayList<>();
        File file1 = ResourceUtils.getFile("classpath:test/SID_termsheet_pdf/sid_ap_CNY002_bl.ack");
        File file2 = ResourceUtils.getFile("classpath:test/SID_termsheet_pdf/sid_ap_notfound_en.ack");
        list.add(file1);
        list.add(file2);
        MockedStatic<CommonUtils> commonUtilsMockedStatic = Mockito.mockStatic(CommonUtils.class);
        commonUtilsMockedStatic.when(() -> CommonUtils.scanFileInPath(any(), any(), any())).thenReturn(list);
        Mockito.when(importTermsheetService.isValidIncomingFile(any(), any())).thenReturn(true);
        Mockito.when(importTermsheetService.queryProductByCode(eq(CTRY_REC_CDE), eq(GRP_MEMBR_REC_CDE), eq(PROD_TYPE_CDE), anyString())).thenReturn(product);

        Mockito.when(termsheetConfiguration.getLocalPath()).thenReturn("local/path");
        Mockito.when(termsheetConfiguration.getS3bucket()).thenReturn("s3bucket");
        Mockito.when(termsheetConfiguration.getS3path()).thenReturn("s3path");
        Mockito.when(termsheetConfiguration.getS3region()).thenReturn("us-west-2");
        Mockito.when(termsheetConfiguration.getS3url()).thenReturn("https://s3.amazonaws.com");

        Runtime mockedRuntime = Mockito.mock(Runtime.class);
        try (MockedStatic<Runtime> runtimeMockedStatic = Mockito.mockStatic(Runtime.class)) {
            runtimeMockedStatic.when(Runtime::getRuntime).thenReturn(mockedRuntime);
            Process mockedProcess = Mockito.mock(Process.class);
            Mockito.when(mockedRuntime.exec(anyString())).thenReturn(mockedProcess);
            Mockito.when(mockedProcess.waitFor(60, TimeUnit.SECONDS)).thenReturn(true);
            Mockito.when(mockedProcess.exitValue()).thenReturn(0);
            Mockito.when(mockedProcess.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

            SidTsFinDocUtil.postProcess(directory, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_TYPE_CDE, importTermsheetService, termsheetConfiguration);
            Assert.assertNotNull(directory);
        }
    }

    @Test
    public void testTransferToS3_timeout() throws IOException, InterruptedException {
        Mockito.when(termsheetConfiguration.getLocalPath()).thenReturn("local/path");
        Mockito.when(termsheetConfiguration.getS3bucket()).thenReturn("s3bucket");
        Mockito.when(termsheetConfiguration.getS3path()).thenReturn("s3path");
        Mockito.when(termsheetConfiguration.getS3region()).thenReturn("us-west-2");
        Mockito.when(termsheetConfiguration.getS3url()).thenReturn("https://s3.amazonaws.com");

        Runtime mockedRuntime = Mockito.mock(Runtime.class);
        try (MockedStatic<Runtime> runtimeMockedStatic = Mockito.mockStatic(Runtime.class)) {
            runtimeMockedStatic.when(Runtime::getRuntime).thenReturn(mockedRuntime);
            Process mockedProcess = Mockito.mock(Process.class);
            Mockito.when(mockedRuntime.exec(anyString())).thenReturn(mockedProcess);
            Mockito.when(mockedProcess.waitFor(60, TimeUnit.SECONDS)).thenReturn(false);
            Mockito.when(mockedProcess.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

            int exitValue = SidTsFinDocUtil.transferToS3(termsheetConfiguration, "CTRY", "GRP", "PROD", "filename.pdf");

            Assert.assertEquals(Integer.MAX_VALUE, exitValue);
            Mockito.verify(mockedProcess).waitFor(60, TimeUnit.SECONDS);
            Mockito.verify(mockedProcess).destroy();
        }
    }

    @Test
    public void testTransferToS3_ioException() throws IOException {
        Mockito.when(termsheetConfiguration.getLocalPath()).thenReturn("local/path");
        Mockito.when(termsheetConfiguration.getS3bucket()).thenReturn("s3bucket");
        Mockito.when(termsheetConfiguration.getS3path()).thenReturn("s3path");
        Mockito.when(termsheetConfiguration.getS3region()).thenReturn("us-west-2");
        Mockito.when(termsheetConfiguration.getS3url()).thenReturn("https://s3.amazonaws.com");

        Runtime mockedRuntime = Mockito.mock(Runtime.class);
        try (MockedStatic<Runtime> runtimeMockedStatic = Mockito.mockStatic(Runtime.class)) {
            runtimeMockedStatic.when(Runtime::getRuntime).thenReturn(mockedRuntime);
            Mockito.when(mockedRuntime.exec(anyString())).thenThrow(new IOException("Mocked IO Exception"));

            Assert.assertThrows(productBatchException.class, () -> {
                SidTsFinDocUtil.transferToS3(termsheetConfiguration, "CTRY", "GRP", "PROD", "filename.pdf");
            });
        }
    }

    @Test
    public void testTransferToS3_interruptedException() throws IOException, InterruptedException {
        Mockito.when(termsheetConfiguration.getLocalPath()).thenReturn("local/path");
        Mockito.when(termsheetConfiguration.getS3bucket()).thenReturn("s3bucket");
        Mockito.when(termsheetConfiguration.getS3path()).thenReturn("s3path");
        Mockito.when(termsheetConfiguration.getS3region()).thenReturn("us-west-2");
        Mockito.when(termsheetConfiguration.getS3url()).thenReturn("https://s3.amazonaws.com");

        Runtime mockedRuntime = Mockito.mock(Runtime.class);
        try (MockedStatic<Runtime> runtimeMockedStatic = Mockito.mockStatic(Runtime.class)) {
            runtimeMockedStatic.when(Runtime::getRuntime).thenReturn(mockedRuntime);
            Process mockedProcess = Mockito.mock(Process.class);
            Mockito.when(mockedRuntime.exec(anyString())).thenReturn(mockedProcess);
            Mockito.when(mockedProcess.waitFor(60, TimeUnit.SECONDS)).thenThrow(new InterruptedException("Mocked Interrupted Exception"));
            Mockito.when(mockedProcess.getErrorStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

            Assert.assertThrows(productBatchException.class, () -> {
                SidTsFinDocUtil.transferToS3(termsheetConfiguration, "CTRY", "GRP", "PROD", "filename.pdf");
            });
        }
    }

    @Test
    public void testProcessAckFile_lockedFile() throws Exception {
        File ackFile = ResourceUtils.getFile("classpath:test/SID_termsheet_pdf/sid_ap_CNY002_bl.ack");

        Mockito.when(importTermsheetService.isValidIncomingFile(any(), eq(ackFile))).thenReturn(true);

        try (MockedStatic<FileChannel> fileChannelMockedStatic = Mockito.mockStatic(FileChannel.class)) {
            FileChannel mockChannel = Mockito.mock(FileChannel.class);
            fileChannelMockedStatic.when(() -> FileChannel.open(any(), any(StandardOpenOption.class), any(StandardOpenOption.class))).thenReturn(mockChannel);
            Mockito.when(mockChannel.tryLock()).thenThrow(new OverlappingFileLockException());

            SidTsFinDocUtil.processAckFile(ackFile, directory, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_TYPE_CDE, importTermsheetService, termsheetConfiguration);
        }

        Mockito.verify(importTermsheetService, Mockito.never()).processTermsheetData(any(), any(), eq(ackFile));
    }

    @Test
    public void testProcessAckFile_missingFile() throws Exception {
        File ackFile = new File("nonexistent_file.ack");

        Mockito.when(importTermsheetService.isValidIncomingFile(any(), eq(ackFile))).thenReturn(true);

        try (MockedStatic<CommonUtils> commonUtilsMockedStatic = Mockito.mockStatic(CommonUtils.class);
             MockedStatic<FileChannel> fileChannelMockedStatic = Mockito.mockStatic(FileChannel.class)) {
            FileChannel mockChannel = Mockito.mock(FileChannel.class);
            fileChannelMockedStatic.when(() -> FileChannel.open(any(), any(StandardOpenOption.class), any(StandardOpenOption.class))).thenReturn(mockChannel);
            Mockito.when(mockChannel.tryLock()).thenThrow(new NoSuchFileException("nonexistent_file.ack"));

            SidTsFinDocUtil.processAckFile(ackFile, directory, CTRY_REC_CDE, GRP_MEMBR_REC_CDE, PROD_TYPE_CDE, importTermsheetService, termsheetConfiguration);
        }

        Mockito.verify(importTermsheetService, Mockito.never()).processTermsheetData(any(), any(), eq(ackFile));
    }
}
