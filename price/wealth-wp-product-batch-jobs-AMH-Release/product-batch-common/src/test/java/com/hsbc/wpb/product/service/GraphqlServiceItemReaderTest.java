package com.dummy.wpb.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GraphqlServiceItemReaderTest {

    @Mock
    private GraphQLService graphQLService;
    @Mock
    private GraphQLRequest graphQLRequest;

    @InjectMocks
    private GraphqlServiceItemReader graphqlServiceItemReader;

    @Test
    void testDoPageRead() {
        when(graphQLRequest.getVariables()).thenReturn(new HashMap<>());
        List<Document> expectedDocuments = Arrays.asList(new Document("key", "value"));
        when(graphQLService.executeRequest(any(GraphQLRequest.class), any(TypeReference.class)))
                .thenReturn(expectedDocuments);

        Iterator<Document> result = graphqlServiceItemReader.doPageRead();

        assertTrue(result.hasNext());
        assertEquals("value", result.next().get("key"));
    }

}