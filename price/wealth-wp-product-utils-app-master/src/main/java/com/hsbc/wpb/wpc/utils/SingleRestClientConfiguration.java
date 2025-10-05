package com.dummy.wpb.wpc.utils;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

import static com.dummy.wpb.wpc.utils.constant.Service.*;
import static com.dummy.wpb.wpc.utils.constant.Service.GRAPHQL_SERVICE_ID;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.config.discovery.enabled", havingValue = "false")
public class SingleRestClientConfiguration {

    @Bean(REST_ADAPTER_SERVICE_ID + "-restTemplate")
    public RestTemplate restAdapterRestTemplate(final Environment environment) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildRestTemplate(REST_ADAPTER_SERVICE_ID, environment);
    }

    @Bean(SOAP_ADAPTER_SERVICE_ID + "-restTemplate")
    public RestTemplate soapAdapterRestTemplate(final Environment environment) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildRestTemplate(SOAP_ADAPTER_SERVICE_ID, environment);
    }

    @Bean(EGRESS_SERVICE_ID + "-restTemplate")
    public RestTemplate egressRestTemplate(final Environment environment) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildRestTemplate(EGRESS_SERVICE_ID, environment);
    }

    @Bean(INGRESS_SERVICE_ID + "-restTemplate")
    public RestTemplate ingressRestTemplate(final Environment environment) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildRestTemplate(INGRESS_SERVICE_ID, environment);
    }

    @Bean(GRAPHQL_SERVICE_ID + "-restTemplate")
    public RestTemplate graphqlRestTemplate(final Environment environment) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildRestTemplate(GRAPHQL_SERVICE_ID, environment);
    }

    private RestTemplate buildRestTemplate(String serviceId, final Environment environment) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String url = environment.getProperty("product." + serviceId + ".url");
        int connectTimeout = Integer.parseInt(environment.getProperty("product.http-connect-timeout"));
        int readTimeout = Integer.parseInt(environment.getProperty("product.http-read-timeout"));
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        factory.setHttpClient(httpClient);
        return new RestTemplateBuilder().rootUri(url)
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .requestFactory(() -> factory)
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .build();
    }
}
