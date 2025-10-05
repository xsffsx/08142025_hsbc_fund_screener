package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class GraphQLService {
    public static final String X_dummy_REQUEST_CORRELATION_ID = "X-dummy-Request-Correlation-Id";

    @Value("${batch.graphQLUrl}")
    String graphQLUrl;

    @Autowired
    private RestTemplate restTemplate;

    @SneakyThrows
    public <T> T executeRequest(GraphQLRequest graphQLRequest, Class<T> clazz) {
        return  JsonUtil.convertType(doExecute(graphQLRequest),clazz);
    }

    @SneakyThrows
    public <T> T executeRequest(GraphQLRequest graphQLRequest, TypeReference<T> typeReference) {
        return  JsonUtil.convertType(doExecute(graphQLRequest),typeReference);
    }

    @SneakyThrows
    private Object doExecute(GraphQLRequest graphQLRequest) {
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(buildRequestEntity(graphQLRequest),
                new ParameterizedTypeReference<Map<String, Object>>() {
        });
        Map<String, Object> response = responseEntity.getBody();
        if (Objects.nonNull(response) && response.containsKey("errors")) {
            log.error(graphQLRequest.getVariables().toString());
            throw new productBatchException(JsonUtil.convertObject2Json(response));
        }
        return JsonPathUtils.readValue(response, graphQLRequest.getDataPath());
    }

    @SneakyThrows
    private RequestEntity<Map<String, Object>> buildRequestEntity(GraphQLRequest graphQLRequest) {
        String correlationId = "BATCH-" + UUID.randomUUID();

        Map<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("query", graphQLRequest.getQuery());
        requestMap.put("variables", graphQLRequest.getVariables());
        requestMap.put("operationName", graphQLRequest.getOperationName());

        log.debug("GraphQL Request: {}", requestMap);
        return RequestEntity.post(new URI(graphQLUrl))
                .contentType(MediaType.APPLICATION_JSON)
                .header(X_dummy_REQUEST_CORRELATION_ID, correlationId)
                .body(requestMap);
    }
}
