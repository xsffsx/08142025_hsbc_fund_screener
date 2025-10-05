/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.configuration;

import java.util.concurrent.Executor;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.interceptor.CacheDistributeConfigurerAdapter;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.interceptor.CacheDistributeHandlerInterceptor;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.properties.CacheDistributeProperties;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.service.CacheDistributeService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.bean.Connection;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientFactory;

/**
 * <p>
 * <b> Automatic configuration class. </b>
 * </p>
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(CacheDistributeProperties.class)
@ConditionalOnProperty(prefix = "cache.distribute", value = "enabled", matchIfMissing = false)
public class CacheDistributeConfiguration {

    @Autowired
    private CacheDistributeProperties props;

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

    @Bean("cacheDistributeExecutor")
    public Executor cacheDistributeExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("cache-distribute-");
        executor.setCorePoolSize(this.props.getExecutor().getCorePoolSize());
        executor.setMaxPoolSize(this.props.getExecutor().getMaxPoolSize());
        executor.setQueueCapacity(this.props.getExecutor().getQueueCapacity());
        executor.initialize();
        return executor;
    }

    @Bean
    public CacheDistributeConfigurerAdapter cacheDistributeConfigurerAdapter() {
        CacheDistributeConfigurerAdapter adapter = new CacheDistributeConfigurerAdapter();
        adapter.setInterceptor(cacheDistributeHandlerInterceptor());
        adapter.setPatterns(this.props.getPatterns());
        return adapter;
    }

    @Bean
    public CacheDistributeHandlerInterceptor cacheDistributeHandlerInterceptor() {
        CacheDistributeHandlerInterceptor interceptor = new CacheDistributeHandlerInterceptor();
        interceptor.setService(cacheDistributeService());
        return interceptor;
    }

    @Bean
    public CacheDistributeService cacheDistributeService() {
        CacheDistributeService service = new CacheDistributeService();
        service.setRestTemplate(cacheDistributeRestTemplate());
//        service.setUri(this.props.getUri());
        service.setAwsUri(this.props.getAwsUri());
        return service;
    }

}
