package com.dummy.wmd.wpc.graphql.fetcher.upload;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.FileIngressStatus;
import com.dummy.wmd.wpc.graphql.model.PageResult;
import com.dummy.wmd.wpc.graphql.service.DataProcessingService;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class UploadResultQueryFetcherTest {

    @InjectMocks
    private UploadResultQueryFetcher uploadResultQueryFetcher;
    @Mock
    private DataProcessingService service;
    @Mock
    private DataFetchingEnvironment environment;

    @Before
    public void setUp() {
        uploadResultQueryFetcher = new UploadResultQueryFetcher(service);
        ReflectionTestUtils.setField(uploadResultQueryFetcher, "service", service);
        Document document = new Document();
        document.put(Field._id, 123);
        document.put(Field.fileName, "hello.txt");
        document.put(Field.fileMd5, "fileMd5");
        Mockito.when(environment.getSource()).thenReturn(document);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsFileIngressStatus() throws Exception {
        getFileIngressStatusPageResult(true);
        FileIngressStatus fileIngressStatus = uploadResultQueryFetcher.get(environment);
        Assert.assertNotNull(fileIngressStatus);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsNull() throws Exception {
        getFileIngressStatusPageResult(false);
        FileIngressStatus fileIngressStatus = uploadResultQueryFetcher.get(environment);
        Assert.assertNull(fileIngressStatus);
    }

    private void getFileIngressStatusPageResult(Boolean hasData) {
        PageResult<FileIngressStatus> result = new PageResult();
        List<FileIngressStatus> list = new ArrayList<>();
        if(hasData) {
            list.add(new FileIngressStatus());
        }
        ReflectionTestUtils.setField(result, "list", list);
        Mockito.when(service.queryFileIngressStatus(any(), any(), any(), any(),
                any(), any(), any(), any(), any())).thenReturn(result);
    }
}
