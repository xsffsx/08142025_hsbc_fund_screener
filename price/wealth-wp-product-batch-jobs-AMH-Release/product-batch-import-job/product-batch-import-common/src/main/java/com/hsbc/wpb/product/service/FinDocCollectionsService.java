package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.utils.BsonUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class FinDocCollectionsService {
    private final GraphQLService graphQLService;

    public FinDocCollectionsService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    //fin_doc_upld
    public List<Document> finDocUpldByFilters(Bson filter) {
        Map<String, Object> mapFilter = BsonUtils.toMap(filter);
        return finDocUpldByFilters(mapFilter);
    }
    public List<Document> finDocUpldByFilters(Map<String, Object> filter) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        String query = CommonUtils.readResource("/gql/fin_doc_upld-query.gql");
        graphQLRequest.setQuery(query);
        graphQLRequest.setVariables(Collections.singletonMap("filter", filter));
        graphQLRequest.setDataPath("data.finDocUpldByFilter");

        return graphQLService.executeRequest(graphQLRequest, new TypeReference<List<Document>>() {});
    }

    //prod_type_fin_doc
    public List<Document> prodTypeFinDocByFilters(Bson filter) {
        Map<String, Object> mapFilter = BsonUtils.toMap(filter);
        return prodTypeFinDocByFilters(mapFilter);
    }
    public List<Document> prodTypeFinDocByFilters(Map<String, Object> filter) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        String query = CommonUtils.readResource("/gql/prod_type_fin_doc-query.gql");
        graphQLRequest.setQuery(query);
        graphQLRequest.setVariables(Collections.singletonMap("filter", filter));
        graphQLRequest.setDataPath("data.prodTypeFinDocByFilter");

        return graphQLService.executeRequest(graphQLRequest, new TypeReference<List<Document>>() {});
    }

}
