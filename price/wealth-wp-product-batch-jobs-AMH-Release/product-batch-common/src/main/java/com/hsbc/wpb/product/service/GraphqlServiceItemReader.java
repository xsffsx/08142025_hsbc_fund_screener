package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;

import java.util.*;

@Slf4j
public class GraphqlServiceItemReader extends AbstractPaginatedDataItemReader<Document> {
    private GraphQLRequest graphQLRequest;
    private GraphQLService graphQLService;

    private   TypeReference<List<Document>> typeReference = new TypeReference<List<Document>>() {};

    public void setGraphQLRequest(GraphQLRequest graphQLRequest) {
        this.graphQLRequest = graphQLRequest;
    }

    public void setGraphQLService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @Override
    protected Iterator<Document> doPageRead() {
        Map<String, Object> variables =graphQLRequest.getVariables();
        if (page == 0){
            variables = new HashMap<>(graphQLRequest.getVariables());
            graphQLRequest.setVariables(variables);
            variables.put("limit",pageSize);
            variables.computeIfAbsent("sort", k -> Collections.singletonMap(Field.prodId, 1));
        }

        variables.put("skip", pageSize * page);

        List<Document> documents = graphQLService.executeRequest(graphQLRequest, typeReference);

        return documents.iterator();
    }
}
