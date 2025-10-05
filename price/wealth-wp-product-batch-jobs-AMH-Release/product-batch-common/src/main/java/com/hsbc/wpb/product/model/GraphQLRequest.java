package com.dummy.wpb.product.model;

import lombok.Data;

import java.util.Map;

/**
* Encapsulate the parameters of the graphql query call
*/
@Data
public class GraphQLRequest {
    private String query;

    private Map<String, Object> variables;

    private String operationName;

    private String dataPath;
}
