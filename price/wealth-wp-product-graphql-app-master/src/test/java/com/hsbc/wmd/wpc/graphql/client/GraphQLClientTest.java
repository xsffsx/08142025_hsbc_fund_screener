package com.dummy.wmd.wpc.graphql.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class GraphQLClientTest {

    @InjectMocks
    private GraphQLClient graphQLClient;
    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        graphQLClient = new GraphQLClient("endpoint");
        ReflectionTestUtils.setField(graphQLClient, "restTemplate", restTemplate);
    }

    @Test
    public void testPost_givenQueryString_returnsMap() {
        Mockito.when(restTemplate.postForObject(anyString(), any(), any(Map.class.getClass()))).thenReturn(new HashMap<>());
        Map<String, Object> map = graphQLClient.post("query");
        Assert.assertNotNull(map);
    }

    @Test
    public void testHeader_givenNameAndValue_DoesNotThrow() {
        try {
            graphQLClient.header("name", "value");
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
