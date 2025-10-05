package com.dummy.wpb.wpc.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class LoadBalancedRestClientConfigurationTests {

    @InjectMocks
    LoadBalancedRestClientConfiguration cfg;

    @Mock
    Environment environment;
    
    @Test
    public void test() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        Mockito.when(environment.getProperty("product.http-connect-timeout")).thenReturn("120000");
        Mockito.when(environment.getProperty("product.http-read-timeout")).thenReturn("120000");
        try {
            cfg.restAdapterRestTemplate(environment);
            cfg.soapAdapterRestTemplate(environment);
            cfg.egressRestTemplate(environment);
            cfg.ingressRestTemplate(environment);
            cfg.graphqlRestTemplate(environment);
        } catch (Exception e) {
            fail();
        }
    }

}
