package com.dummy.wmd.wpc.graphql.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class GraphQLClient {
    private final String endpoint;
    private HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = new RestTemplate();

    public GraphQLClient(String endpoint){
        this.endpoint = endpoint;
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public Map<String, Object> post(String query){
        return post(query, Collections.emptyMap());
    }

    public void header(String name, String value){
        headers.add(name, value);
    }

    public Map<String, Object> post(String query, Map<String, Object> variables){
        Map<String, Object> gqlRequest = new LinkedHashMap<>();
        gqlRequest.put("query", query);
        gqlRequest.put("variables", variables);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(gqlRequest, headers);
        return restTemplate.postForObject(endpoint, requestEntity, Map.class);
    }
}
