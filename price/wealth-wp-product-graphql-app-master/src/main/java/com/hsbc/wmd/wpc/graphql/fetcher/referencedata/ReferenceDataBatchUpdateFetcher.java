package com.dummy.wmd.wpc.graphql.fetcher.referencedata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.model.ReferenceDataBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.dummy.wmd.wpc.graphql.utils.FilterUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ReferenceDataBatchUpdateFetcher implements DataFetcher<ReferenceDataBatchUpdateResult> {
    private ReferenceDataService referDataService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ReferenceDataBatchUpdateFetcher(ReferenceDataService referDataService){
        this.referDataService = referDataService;
    }

    @Override
    public ReferenceDataBatchUpdateResult get(DataFetchingEnvironment environment) throws Exception {
        Map<String, Object> filterMap = environment.getArgument("filter");
        BsonDocument revisedFilterBson = new FilterUtils(CollectionName.reference_data).toBsonDocument(filterMap);
        List<OperationInput> operations  = objectMapper.convertValue(environment.getArgument("operations"), new TypeReference<List<OperationInput>>(){});
        boolean allowPartial = environment.getArgument("allowPartial");

        return referDataService.batchUpdate(revisedFilterBson, operations, allowPartial);
    }
}
