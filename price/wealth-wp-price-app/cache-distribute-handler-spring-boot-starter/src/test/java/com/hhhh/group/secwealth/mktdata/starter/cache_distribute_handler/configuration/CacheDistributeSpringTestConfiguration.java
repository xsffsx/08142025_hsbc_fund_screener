/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.configuration;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.properties.CacheDistributeSpringTestProperties;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.service.CacheDistributeService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.bean.Connection;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientFactory;

/**
 * <p>
 * <b> It's only used to test. </b>
 * </p>
 */
@Configuration
@EnableConfigurationProperties(CacheDistributeSpringTestProperties.class)
public class CacheDistributeSpringTestConfiguration {

    @Autowired
    private CacheDistributeSpringTestProperties props;

    @Bean
    public RestTemplate cacheDistributeRestTemplate() {
        return new RestTemplate(cacheDistributeClientHttpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory cacheDistributeClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(cacheDistributeCloseableHttpClient());
        return clientHttpRequestFactory;
    }

    /**
     * <p>
     * <b> Create a {@link CloseableHttpClient} using {@link HttpClientFactory}
     * . It's comes from
     * {@link http-client-manager-spring-boot-starter.jar} </b>
     * </p>
     *
     * @return {@link CloseableHttpClient}
     */
    @Bean
    public CloseableHttpClient cacheDistributeCloseableHttpClient() {
        HttpClientFactory factory = new HttpClientFactory();
        Connection connection = new Connection();
        BeanUtils.copyProperties(this.props.getConnection(), connection);
        factory.setConnection(connection);
        CloseableHttpClient httpClient;
        try {
            httpClient = factory.create();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return httpClient;
    }

    @Bean
    public CacheDistributeService cacheDistributeService() {
        CacheDistributeService service = new CacheDistributeService();
        service.setRestTemplate(cacheDistributeRestTemplate());
        service.setAwsUri(this.props.getUri());
        return service;
    }

}
