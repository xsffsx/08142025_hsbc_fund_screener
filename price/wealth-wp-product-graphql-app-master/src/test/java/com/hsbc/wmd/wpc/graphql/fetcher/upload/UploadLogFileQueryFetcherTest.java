package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.LogFileService;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class UploadLogFileQueryFetcherTest {

    @InjectMocks
    private UploadLogFileQueryFetcher uploadLogFileQueryFetcher;
    @Mock
    private DataFetchingEnvironment environment;
    private MockedStatic<LogFileService> logFileServiceMockedStatic;

    @Before
    public void setUp() {
        logFileServiceMockedStatic = Mockito.mockStatic(LogFileService.class);
        logFileServiceMockedStatic.when(() -> LogFileService.mapLogFileByFileName(anyString())).thenReturn("string");
    }

    @After
    public void tearDown() {
        logFileServiceMockedStatic.close();
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsString() {
        Document document = new Document();
        document.put(Field.recStatCde, "approved");
        document.put(Field.fileName, "fileName");
        document.put(Field.recUpdtDtTm, new Date());
        Mockito.when(environment.getSource()).thenReturn(document);
        String s = uploadLogFileQueryFetcher.get(environment);
        Assert.assertNotNull(s);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsNull() {
        Document document = new Document();
        document.put(Field.recStatCde, "approve");
        Mockito.when(environment.getSource()).thenReturn(document);
        String s = uploadLogFileQueryFetcher.get(environment);
        Assert.assertNull(s);
    }
}
