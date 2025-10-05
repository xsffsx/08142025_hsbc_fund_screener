package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.dummy.wmd.wpc.graphql.model.ReferenceDataBatchDeleteResult;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ReferenceDataBatchDeleteFetcher implements DataFetcher<ReferenceDataBatchDeleteResult> {
    private final ReferenceDataService referDataService;

    public ReferenceDataBatchDeleteFetcher(ReferenceDataService referDataService) {
        this.referDataService = referDataService;
    }

    @Override
    public ReferenceDataBatchDeleteResult get(DataFetchingEnvironment environment) throws Exception {
        List<Map<String, Object>> filters = environment.getArgument("referenceData");
        return referDataService.batchDelete(filters);
    }

}
