package com.hhhh.group.secwealth.mktdata.api.equity.quotes.configuration;

import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class JunitTestConfiguration {

    @Bean
    @Primary
    public HttpClientHelper httpClientHelper(){
        return Mockito.mock(HttpClientHelper.class);
    }
}

