package com.dummy.wpb.wpc.utils.service;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckServiceTests {

    @InjectMocks
    HealthCheckService healthCheckService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Map<String, RestTemplate> restTemplateMap;

    @Before
    public void setUp() throws Exception {
        Mockito.when(restTemplateMap.get(any())).thenReturn(restTemplate);
        ReflectionTestUtils.setField(healthCheckService, "restTemplateMap", restTemplateMap);
        ReflectionTestUtils.setField(healthCheckService, "folder", "test");
    }

    @Test
    public void testHealthCheckScope() {
        Document output = healthCheckService.healthCheckScopes();
        Assert.assertNotNull(output);
    }

    @Test
    public void testHealthCheck() {
        Mockito.when(restTemplate.getForEntity((String) any(), (Class) any(), (Map) any()))
                .thenReturn(ResponseEntity.ok("ok"));
        Document output = healthCheckService.healthCheck("servicea", null, true);
        Assert.assertNotNull(output);
        Assert.assertEquals("UP", output.get("status"));
    }

    @Test
    public void testHealthCheck_invalidServiceId() {
        Document output;
        try {
            output = healthCheckService.healthCheck("errorServiceId", new HashMap<>(), true);
        } catch (Exception e) {
            output = null;
        }
        Assert.assertNull(output);
    }

    @Test
    public void testHealthCheckAll() {
        Mockito.when(restTemplate.getForEntity((String) any(), (Class) any(), (Map) any()))
                .thenReturn(ResponseEntity.ok("ok"));
        Mockito.when(restTemplate.postForEntity((String) any(), any(), (Class) any()))
                .thenReturn(ResponseEntity.ok("ok"));
        List<Document> checkResults = healthCheckService.healthCheckAll();
        Assert.assertFalse(checkResults.isEmpty());
        checkResults.forEach(output ->  Assert.assertEquals("UP", output.get("status")));
    }

    @Test
    public void testHealthCheckAll_500() {
        Mockito.when(restTemplate.getForEntity((String) any(), (Class) any(), (Map) any()))
                .thenReturn(ResponseEntity.status(500).body("error"));
        Mockito.when(restTemplate.postForEntity((String) any(), any(), (Class) any()))
                .thenReturn(ResponseEntity.status(500).body("error"));
        List<Document> checkResults = healthCheckService.healthCheckAll();
        Assert.assertFalse(checkResults.isEmpty());
        checkResults.forEach(output ->  Assert.assertEquals("DOWN", output.get("status")));
    }

    @Test
    public void testHealthCheckAll_error() {
        Mockito.when(restTemplate.getForEntity((String) any(), (Class) any(), (Map) any()))
                .thenThrow(new RuntimeException("test exception"))
                .thenThrow(HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, "internal error", new HttpHeaders(), "".getBytes(), null));
        Mockito.when(restTemplate.postForEntity((String) any(), any(), (Class) any()))
                .thenThrow(new RuntimeException("test exception"));
        List<Document> checkResults = healthCheckService.healthCheckAll();
        Assert.assertFalse(checkResults.isEmpty());
        checkResults.forEach(output ->  Assert.assertEquals("DOWN", output.get("status")));
    }

}
