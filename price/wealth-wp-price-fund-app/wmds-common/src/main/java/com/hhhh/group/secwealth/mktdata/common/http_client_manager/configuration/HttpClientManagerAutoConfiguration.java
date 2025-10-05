/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.http_client_manager.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhhh.group.secwealth.mktdata.common.http_client_manager.bean.Connection;
import com.hhhh.group.secwealth.mktdata.common.http_client_manager.component.HttpClientFactory;
import com.hhhh.group.secwealth.mktdata.common.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.common.http_client_manager.properties.ConnectionProperties;
import com.hhhh.group.secwealth.mktdata.common.http_client_manager.properties.HttpClientProperties;


@Configuration
//@EnableConfigurationProperties({ConnectionProperties.class, HttpClientProperties.class})
//@ConditionalOnProperty(prefix = "httpclient", value = "enabled", matchIfMissing = true)
public class HttpClientManagerAutoConfiguration {

    @Autowired
    private ConnectionProperties connectionProperties;

    @Autowired
    private HttpClientProperties httpClientProperties;

    @Bean
    public Map<String, CloseableHttpClient> httpClientMapper() throws Exception {
        final Map<String, CloseableHttpClient> httpClientMapper = new HashMap<>();
        final Map<String, Connection> connectionMapper = this.connectionProperties.getConnectionMap();
        if (connectionMapper != null && !connectionMapper.isEmpty()) {
            for (final Map.Entry<String, Connection> mapper : connectionMapper.entrySet()) {
                final HttpClientFactory httpClientFactory = new HttpClientFactory();
                httpClientFactory.setInitDefaultExCode(this.httpClientProperties.getInitDefaultExCode());
                httpClientFactory.setConnection(mapper.getValue());
                httpClientMapper.put(mapper.getKey(), httpClientFactory.create());
            }
        }
        return httpClientMapper;
    }

    @Bean
    public HttpClientHelper httpClientHelper() throws Exception {
        final HttpClientHelper httpClientHelper = new HttpClientHelper();
        httpClientHelper.setProcessDefaultExCode(this.httpClientProperties.getProcessDefaultExCode());
        httpClientHelper.setDefaultHttpClientName(this.httpClientProperties.getDefaultHttpClientName());
        httpClientHelper.setHttpClientMapper(httpClientMapper());
        return httpClientHelper;
    }

}
