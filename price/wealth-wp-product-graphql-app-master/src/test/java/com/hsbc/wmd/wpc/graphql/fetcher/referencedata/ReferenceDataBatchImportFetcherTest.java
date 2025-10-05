package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.model.ReferenceDataBatchImportResult;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataBatchImportFetcherTest {

    @Mock
    private ReferenceDataService referDataService;

    private ReferenceDataBatchImportFetcher referenceDataBatchImportFetcher;

    @Before
    public void setUp() {
        referenceDataBatchImportFetcher = new ReferenceDataBatchImportFetcher(referDataService);
    }

    @Test
    public void testGet_givenEnvironment_returnsReferenceDataBatchImportResult() throws Exception {
        Document doc = CommonUtils.readResourceAsDocument("/files/reference-data-list.json");
        List<Map<String, Object>> referDataList = (List<Map<String, Object>>) doc.get("referenceData");

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("referenceData", referDataList);
        arguments.put("allowPartial", true);

        DataFetchingEnvironment environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();

        ReferenceDataBatchImportResult mockResult = new ReferenceDataBatchImportResult();
        mockResult.setImportedReferenceData(referDataList.stream().map(referData -> new Document(referData)).collect(Collectors.toList()));
        mockResult.setInvalidReferenceData(Collections.emptyList());
        Mockito.when(referDataService.batchImport(any(List.class), any(Boolean.class))).thenReturn(mockResult);

        ReferenceDataBatchImportResult result = referenceDataBatchImportFetcher.get(environment);
        assertNotNull(result);
        assertNotNull(result.getImportedReferenceData());
        assertNotNull(result.getInvalidReferenceData());
    }


}
