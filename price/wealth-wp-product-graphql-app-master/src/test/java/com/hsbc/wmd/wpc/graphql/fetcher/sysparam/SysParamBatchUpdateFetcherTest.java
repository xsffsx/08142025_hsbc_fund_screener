package com.dummy.wmd.wpc.graphql.fetcher.sysparam;

import com.dummy.wmd.wpc.graphql.model.SysParamBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.service.SysParamService;
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
public class SysParamBatchUpdateFetcherTest {

    @Mock
    private SysParamService sysParamService;

    private SysParamBatchUpdateFetcher sysParamBatchUpdateFetcher;

    @Before
    public void setUp() {
        sysParamBatchUpdateFetcher = new SysParamBatchUpdateFetcher(sysParamService);
    }

    @Test
    public void testGet_givenEnvironment_returnsSysParamBatchUpdateResult() throws Exception {
        Document doc = CommonUtils.readResourceAsDocument("/files/sys_params.json");
        List<Map<String, Object>> sysParams = (List<Map<String, Object>>) doc.get("sysParams");

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("sysParams", sysParams);

        DataFetchingEnvironment environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();

        SysParamBatchUpdateResult mockResult = new SysParamBatchUpdateResult();

        mockResult.setUpdatedSysParams(sysParams.stream().map(sysParam -> new Document(sysParam)).collect(Collectors.toList()));
        mockResult.setNotExistingSysParams(Collections.emptyList());
        Mockito.when(sysParamService.batchUpdate(any(List.class))).thenReturn(mockResult);

        SysParamBatchUpdateResult result = sysParamBatchUpdateFetcher.get(environment);
        assertNotNull(result);
        assertNotNull(result.getUpdatedSysParams());
        assertNotNull(result.getNotExistingSysParams());
    }


}
