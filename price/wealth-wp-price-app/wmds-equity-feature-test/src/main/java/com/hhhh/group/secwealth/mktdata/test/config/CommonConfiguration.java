package com.hhhh.group.secwealth.mktdata.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.test.utils.JSONUtil;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Clock;

@Configuration
@ComponentScan(
    basePackages = {"com.hhhh.group"}
)
public class CommonConfiguration {

    @Value("${mds.proxy.host:}")
    String proxyHost;

    @Value("${mds.proxy.port:0}")
    int proxyPort;

    public CommonConfiguration() {
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JSONUtil.setObjectMapper(mapper);
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate;
        if("".equals(proxyHost)){
            restTemplate = new RestTemplate();
        }else{
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            requestFactory.setProxy(proxy);
            restTemplate = new RestTemplate(requestFactory);
        }
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        return restTemplate;
    }

    @Bean
    Clock provideClock() {
        return Clock.systemUTC();
    }
}
