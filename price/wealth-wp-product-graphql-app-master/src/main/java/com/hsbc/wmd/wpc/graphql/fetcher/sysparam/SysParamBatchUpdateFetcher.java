package com.dummy.wmd.wpc.graphql.fetcher.sysparam;

import com.dummy.wmd.wpc.graphql.model.SysParamBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.service.SysParamService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

@Component
public class SysParamBatchUpdateFetcher implements DataFetcher<SysParamBatchUpdateResult> {

    private SysParamService sysParamService;

    public SysParamBatchUpdateFetcher(SysParamService sysParamService) {
        this.sysParamService = sysParamService;
    }

    @Override
    public SysParamBatchUpdateResult get(DataFetchingEnvironment environment) throws Exception {
        return sysParamService.batchUpdate(environment.getArgument("sysParams"));
    }
}
