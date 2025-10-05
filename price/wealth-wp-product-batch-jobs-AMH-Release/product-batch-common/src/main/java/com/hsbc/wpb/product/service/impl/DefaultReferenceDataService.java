package com.dummy.wpb.product.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultReferenceDataService implements ReferenceDataService {
    private final GraphQLService graphQLService;

    public DefaultReferenceDataService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    public List<ReferenceData> referenceDataByFilter(Map<String,Object> filterMap) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/referenceData-query.gql"));
        graphQLRequest.setDataPath("data.referenceDataByFilter");
        graphQLRequest.setVariables(Collections.singletonMap("filter",filterMap));
        return graphQLService.executeRequest(graphQLRequest, new TypeReference<List<ReferenceData>>() {
        });

    }

    public ReferenceDataBatchCreateResult createRefData(List<ReferenceData> referenceDataList) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/referenceData-batch-create.gql"));
        graphQLRequest.setDataPath("data.referenceDataBatchCreate");

        Map<String, Object> variablesObj = new LinkedHashMap<>();
        variablesObj.put("refData", referenceDataList);
        variablesObj.put("operations","referenceDataBatchCreate");
        variablesObj.put("allowPartial", true);

        graphQLRequest.setVariables(variablesObj);

        ReferenceDataBatchCreateResult createResult = graphQLService.executeRequest(graphQLRequest, ReferenceDataBatchCreateResult.class);
        createResult.logCreateResult(log);
        return createResult;
    }

}
