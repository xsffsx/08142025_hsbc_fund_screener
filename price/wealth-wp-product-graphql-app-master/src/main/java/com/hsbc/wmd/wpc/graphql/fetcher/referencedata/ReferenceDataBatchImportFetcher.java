package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.model.ReferenceDataBatchImportResult;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReferenceDataBatchImportFetcher implements DataFetcher<ReferenceDataBatchImportResult> {
    private ReferenceDataService referDataService;

    public ReferenceDataBatchImportFetcher(ReferenceDataService referDataService) {
        this.referDataService = referDataService;
    }

    @Override
    public ReferenceDataBatchImportResult get(DataFetchingEnvironment environment) throws Exception {
        List<Map<String, Object>> referDataList = environment.getArgument("referenceData");
        boolean allowPartial = environment.getArgument("allowPartial");
        return referDataService.batchImport(referDataList, allowPartial);
    }
}
