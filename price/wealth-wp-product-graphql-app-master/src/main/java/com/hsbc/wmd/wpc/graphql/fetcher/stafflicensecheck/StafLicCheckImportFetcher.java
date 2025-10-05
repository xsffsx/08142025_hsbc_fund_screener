package com.dummy.wmd.wpc.graphql.fetcher.stafflicensecheck;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.StaffLicenseCheckBatchImportResult;
import com.dummy.wmd.wpc.graphql.service.StafLicCheckService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class StafLicCheckImportFetcher implements DataFetcher<StaffLicenseCheckBatchImportResult> {
    private final StafLicCheckService service;

    public StafLicCheckImportFetcher(StafLicCheckService stafLicCheckService){
        service = stafLicCheckService;
    }

    @Override
    public StaffLicenseCheckBatchImportResult get(DataFetchingEnvironment environment) throws Exception {
        List<Map<String, Object>> stafLicCheckList = environment.getArgument(Field.stafLicCheck);
        return service.saveStaffLicenseCheck(stafLicCheckList);
    }
}
