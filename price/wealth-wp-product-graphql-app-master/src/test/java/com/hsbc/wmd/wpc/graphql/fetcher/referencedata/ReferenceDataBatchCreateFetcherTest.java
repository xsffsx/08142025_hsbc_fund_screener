package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.model.ReferenceDataBatchCreateResult;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataBatchCreateFetcherTest {

    @InjectMocks
    private ReferenceDataBatchCreateFetcher referenceDataBatchCreateFetcher;
    @Mock
    private ReferenceDataService referDataService;
    @Mock
    private DataFetchingEnvironment environment;

    @Before
    public void setUp() {
        referenceDataBatchCreateFetcher = new ReferenceDataBatchCreateFetcher(referDataService);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsReferenceDataBatchCreateResult() throws Exception {
        Mockito.when(environment.getArgument(anyString())).thenReturn(new ArrayList<>()).thenReturn(true);
        Mockito.when(referDataService.batchCreate(anyList(), anyBoolean())).thenReturn(new ReferenceDataBatchCreateResult());
        ReferenceDataBatchCreateResult referenceDataBatchCreateResult = referenceDataBatchCreateFetcher.get(environment);
        Assert.assertNotNull(referenceDataBatchCreateResult);
    }
}
