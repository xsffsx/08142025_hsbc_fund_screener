package com.dummy.wmd.wpc.graphql.fetcher.stafLicCheck;

import com.dummy.wmd.wpc.graphql.fetcher.stafflicensecheck.StafLicCheckImportFetcher;
import com.dummy.wmd.wpc.graphql.model.StaffLicenseCheckBatchImportResult;
import com.dummy.wmd.wpc.graphql.service.StafLicCheckService;
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
public class StafLicCheckImportFetcherTest {

    @Mock
    private StafLicCheckService stafLicCheckService;

    private StafLicCheckImportFetcher stafLicCheckImportFetcher;

    @Before
    public void setUp() {
        stafLicCheckImportFetcher = new StafLicCheckImportFetcher(stafLicCheckService);
    }

    @Test
    public void testGet_givenEnvironment_returnsstafLicCheckBatchImportResult() throws Exception {
        Document doc = CommonUtils.readResourceAsDocument("/files/prod-type-staff-license-check.json");
        List<Map<String, Object>> stafLicCheckList = (List<Map<String, Object>>) doc.get("stafLicCheck");

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("stafLicCheck", stafLicCheckList);

        DataFetchingEnvironment environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(arguments)
            .build();

        StaffLicenseCheckBatchImportResult mockResult = new StaffLicenseCheckBatchImportResult();
        mockResult.setCreatedStaffLicenseCheck(stafLicCheckList.stream().map(referData -> new Document(referData)).collect(Collectors.toList()));
        mockResult.setInvalidStaffLicenseCheck(Collections.emptyList());
        Mockito.when(stafLicCheckService.saveStaffLicenseCheck(any(List.class))).thenReturn(mockResult);

        StaffLicenseCheckBatchImportResult result = stafLicCheckImportFetcher.get(environment);
        assertNotNull(result);
        assertNotNull(result.getCreatedStaffLicenseCheck());
        assertNotNull(result.getInvalidStaffLicenseCheck());
    }


}
