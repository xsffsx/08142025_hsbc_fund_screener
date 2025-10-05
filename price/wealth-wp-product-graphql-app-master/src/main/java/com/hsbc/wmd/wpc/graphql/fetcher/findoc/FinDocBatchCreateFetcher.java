package com.dummy.wmd.wpc.graphql.fetcher.findoc;

import com.dummy.wmd.wpc.graphql.model.FinDocBatchCreateResult;
import com.dummy.wmd.wpc.graphql.service.FinDocService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FinDocBatchCreateFetcher implements DataFetcher<FinDocBatchCreateResult> {

    private FinDocService finDocService;

    public FinDocBatchCreateFetcher(FinDocService finDocService) {
        this.finDocService = finDocService;
    }

    @Override
    public FinDocBatchCreateResult get(DataFetchingEnvironment environment) throws Exception {
        log.debug("FinDocBatchCreateFetcher.get");
        List<Map<String, Object>> finDocList = environment.getArgument("finDoc");
        boolean allowPartial = environment.getArgument("allowPartial");

        return finDocService.batchCreateFinDoc(finDocList, allowPartial);
    }
}
