package com.hhhh.group.secwealth.mktdata.common.svc.httpclient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RestConfigTest {
    @Test
    public void restTemplate_Should_CreateRestTemplate() {
        // When
        RestTemplate restTemplate = new RestConfig().restTemplate();

        // Then
        assertNotNull(restTemplate);
    }
}