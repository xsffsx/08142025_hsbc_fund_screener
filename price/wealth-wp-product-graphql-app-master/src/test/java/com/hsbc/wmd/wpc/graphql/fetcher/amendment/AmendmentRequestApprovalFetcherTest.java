package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.service.AmendmentService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AmendmentRequestApprovalFetcherTest {

    @InjectMocks
    private AmendmentRequestApprovalFetcher amendmentRequestApprovalFetcher;
    @Mock
    private AmendmentService service;
    @Mock
    private DataFetchingEnvironment environment;

    @Before
    public void setUp() {
        amendmentRequestApprovalFetcher = new AmendmentRequestApprovalFetcher(service);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDocument() {
        Mockito.when(environment.getArgument(anyString())).thenReturn(1L).thenReturn("comments");
        Mockito.when(service.requestApproval(anyLong(), anyString())).thenReturn(new Document());
        Document document = amendmentRequestApprovalFetcher.get(environment);
        Assert.assertNotNull(document);
    }
}
