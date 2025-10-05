package com.dummy.wpb.product.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.ProductPriceHistory;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductPriceHistoryService;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultProductPriceHistoryService implements ProductPriceHistoryService {
    private final GraphQLService graphQLService;

    public DefaultProductPriceHistoryService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    public List<ProductPriceHistory> productPriceHistoryByFilter(Map<String, Object> filter) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        String query = CommonUtils.readResource("/gql/product-price-history-query.gql");
        graphQLRequest.setQuery(query);
        graphQLRequest.setVariables(Collections.singletonMap("filter", filter));
        graphQLRequest.setDataPath("data.productPriceHistoryByFilter");
        return graphQLService.executeRequest(graphQLRequest, new TypeReference<List<ProductPriceHistory>>() {
        });
    }
}
