package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.DiffType;
import com.dummy.wmd.wpc.graphql.service.DocumentRevisionService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class DocumentRevisionDiffFetcherTest {

    @InjectMocks
    private DocumentRevisionDiffFetcher documentRevisionDiffFetcher;
    @Mock
    private DocumentRevisionService revisionService;
    @Mock
    private DataFetchingEnvironment environment;

    @Before
    public void setUp() {
        documentRevisionDiffFetcher = new DocumentRevisionDiffFetcher(revisionService);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDiffTypeList1() throws Exception {
        Mockito.when(environment.getArgument(anyString())).thenReturn("docType").thenReturn(1L).thenReturn(1L).thenReturn(1L);
        Mockito.when(revisionService.getDocument(anyString(), anyLong(), anyLong())).thenReturn(new Document());
        List<DiffType> diffTypes = documentRevisionDiffFetcher.get(environment);
        Assert.assertNotNull(diffTypes);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDiffTypeList2() throws Exception {
        Mockito.when(environment.getArgument(anyString())).thenReturn("docType").thenReturn(1L).thenReturn(1L).thenReturn(1L);
        Mockito.when(revisionService.getDocument(anyString(), anyLong(), anyLong())).thenReturn(null);
        List<DiffType> diffTypes = documentRevisionDiffFetcher.get(environment);
        Assert.assertNotNull(diffTypes);
    }
}
