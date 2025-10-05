package com.dummy.wpb.wpc.utils;

import static com.dummy.wpb.wpc.utils.constant.Service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.config.discovery.enabled", havingValue = "true")
public class LoadBalancedRestClientConfiguration {

    @Bean(REST_ADAPTER_SERVICE_ID + "-restTemplate")
    @LoadBalanced
    public RestTemplate restAdapterRestTemplate(final Environment environment) {
        return buildRestTemplate(REST_ADAPTER_SERVICE_ID, environment);
    }

    @Bean(SOAP_ADAPTER_SERVICE_ID + "-restTemplate")
    @LoadBalanced
    public RestTemplate soapAdapterRestTemplate(final Environment environment) {
        return buildRestTemplate(SOAP_ADAPTER_SERVICE_ID, environment);
    }

    @Bean(EGRESS_SERVICE_ID + "-restTemplate")
    @LoadBalanced
    public RestTemplate egressRestTemplate(final Environment environment) {
        return buildRestTemplate(EGRESS_SERVICE_ID, environment);
    }

    @Bean(INGRESS_SERVICE_ID + "-restTemplate")
    @LoadBalanced
    public RestTemplate ingressRestTemplate(final Environment environment) {
        return buildRestTemplate(INGRESS_SERVICE_ID, environment);
    }

    @Bean(GRAPHQL_SERVICE_ID + "-restTemplate")
    @LoadBalanced
    public RestTemplate graphqlRestTemplate(final Environment environment) {
        return buildRestTemplate(GRAPHQL_SERVICE_ID, environment);
    }

    private RestTemplate buildRestTemplate(String serviceId,final Environment environment) {
        String url = environment.getProperty("product." + serviceId + ".url");
        int connectTimeout = Integer.parseInt(environment.getProperty("product.http-connect-timeout"));
        int readTimeout = Integer.parseInt(environment.getProperty("product.http-read-timeout"));
        return new RestTemplateBuilder().rootUri(url)
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .build();
    }
}
