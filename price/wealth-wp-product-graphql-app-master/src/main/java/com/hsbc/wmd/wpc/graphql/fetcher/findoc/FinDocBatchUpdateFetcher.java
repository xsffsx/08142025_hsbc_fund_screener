package com.dummy.wmd.wpc.graphql.fetcher.findoc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.model.FinDocBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.service.FinDocService;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FinDocBatchUpdateFetcher implements DataFetcher<FinDocBatchUpdateResult> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private FinDocService finDocService;

    public FinDocBatchUpdateFetcher(FinDocService finDocService) {
        this.finDocService = finDocService;
    }

    @Override
    public FinDocBatchUpdateResult get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> filterMap = environment.getArgument("filter");
        Bson filter = new FilterUtils(CollectionName.fin_doc).toBsonDocument(filterMap);
        List<OperationInput> operations  = objectMapper.convertValue(environment.getArgument("operations"), new TypeReference<List<OperationInput>>(){});
        boolean allowPartial = environment.getArgument("allowPartial");

        return finDocService.batchUpdateFinDoc(filter, operations, allowPartial);
    }
}
