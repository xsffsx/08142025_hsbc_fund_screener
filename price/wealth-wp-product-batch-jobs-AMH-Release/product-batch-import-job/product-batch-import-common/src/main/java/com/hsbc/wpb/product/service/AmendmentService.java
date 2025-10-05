package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.Amendment;
import com.dummy.wpb.product.utils.BsonUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AmendmentService {
    private final GraphQLService graphQLService;

    public AmendmentService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    public List<Document> amendmentByFilters(Bson filter) {
        Map<String, Object> mapFilter = BsonUtils.toMap(filter);
        return amendmentByFilters(mapFilter);
    }
    public List<Document> amendmentByFilters(Map<String, Object> filter) {
        GraphQLRequest graphQLRequest = new GraphQLRequest();
        String query = CommonUtils.readResource("/gql/amendment-query.gql");
        graphQLRequest.setQuery(query);
        graphQLRequest.setVariables(Collections.singletonMap("filter", filter));
        graphQLRequest.setDataPath("data.amendmentByFilter");

        return graphQLService.executeRequest(graphQLRequest, new TypeReference<List<Document>>() {});
    }

    public Amendment amendmentCreate(String actionCde,String docType,Map<String,Object> docChanged) {

        GraphQLRequest graphQLRequest = new GraphQLRequest();
        graphQLRequest.setQuery(CommonUtils.readResource("/gql/amendment-create.gql"));
        graphQLRequest.setDataPath("data.amendmentCreate");

        Map<String, Object> variablesObj = new LinkedHashMap<>();
        variablesObj.put(Field.actionCde, actionCde);
        variablesObj.put(Field.docType,docType);
        variablesObj.put("docChanged",docChanged);
        graphQLRequest.setVariables(variablesObj);

        Amendment newAmendment = graphQLService.executeRequest(graphQLRequest, Amendment.class);

        if (newAmendment == null) {
            log.error("Amendment create failed.");
        } else {
            log.info("New Amendment has been create.");
        }
        return newAmendment;
    }

}
