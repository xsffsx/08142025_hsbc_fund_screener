package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.model.ReferenceDataBatchDeleteResult;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataBatchDeleteFetcherTest {

    @Mock
    private ReferenceDataService referDataService;

    private ReferenceDataBatchDeleteFetcher referenceDataBatchDeleteFetcher;

    @Before
    public void setUp() {
        referenceDataBatchDeleteFetcher = new ReferenceDataBatchDeleteFetcher(referDataService);
    }

    @Test
    public void testGet_givenEnvironment_returnsReferenceDataBatchImportResult() throws Exception {
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("referenceData", doc.get("referenceData"));

        DataFetchingEnvironment environment = new DataFetchingEnvironmentImpl.Builder()
                .arguments(arguments)
                .build();

        ReferenceDataBatchDeleteResult mockResult = new ReferenceDataBatchDeleteResult();
        mockResult.setDeletedCount(1);
        Mockito.when(referDataService.batchDelete(any())).thenReturn(mockResult);

        ReferenceDataBatchDeleteResult result = referenceDataBatchDeleteFetcher.get(environment);
        assertEquals(1,result.getDeletedCount());
    }


}
