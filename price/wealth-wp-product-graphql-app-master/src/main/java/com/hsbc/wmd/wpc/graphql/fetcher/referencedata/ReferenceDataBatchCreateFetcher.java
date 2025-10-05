package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.model.ReferenceDataBatchCreateResult;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReferenceDataBatchCreateFetcher implements DataFetcher<ReferenceDataBatchCreateResult> {
    private ReferenceDataService referDataService;

    public ReferenceDataBatchCreateFetcher(ReferenceDataService referDataService) {
        this.referDataService = referDataService;
    }

    @Override
    public ReferenceDataBatchCreateResult get(DataFetchingEnvironment environment) throws Exception {
        List<Map<String, Object>> referenceDataList = environment.getArgument("referenceData");
        boolean allowPartial = environment.getArgument("allowPartial");
        return referDataService.batchCreate(referenceDataList, allowPartial);
    }
}
