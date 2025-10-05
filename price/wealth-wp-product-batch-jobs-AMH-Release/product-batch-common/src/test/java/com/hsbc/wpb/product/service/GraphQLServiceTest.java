package com.dummy.wpb.product.service;

import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.GraphQLRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GraphQLServiceTest {

    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    GraphQLService graphQLService;

    @Test
    void testExecuteRequest() throws Exception {

        graphQLService.graphQLUrl = "http://example.com/graphql";

        GraphQLRequest request = new GraphQLRequest();
        request.setDataPath("name");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("name", "expectedData");

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(responseMap);
        when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        Object result = graphQLService.executeRequest(request, Object.class);

        assertEquals("expectedData", result);
    }

    @Test
    void testExecuteRequestErrors(){
        graphQLService.graphQLUrl = "http://example.com/graphql";

        GraphQLRequest request = new GraphQLRequest();
        request.setQuery("query");
        request.setVariables(new HashMap<>());
        request.setOperationName("operation");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("errors", "someError");

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(responseMap);
        when(restTemplate.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        assertThrows(productBatchException.class, () -> {
            graphQLService.executeRequest(request, Object.class);
        });
    }
}