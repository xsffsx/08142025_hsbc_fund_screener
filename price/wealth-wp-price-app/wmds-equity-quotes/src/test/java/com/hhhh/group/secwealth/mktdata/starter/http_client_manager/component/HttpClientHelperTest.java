package com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component;

import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.configuration.HttpClientManagerAutoConfiguration;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.properties.HttpClientProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        HttpClientManagerAutoConfiguration.class
}, initializers=
        ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties
@SpringBootTest
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class HttpClientHelperTest {

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Test
    public void testInitHttpClientManagerAutoConfiguration() {
        System.out.println(httpClientHelper);
        Assert.assertNotNull(httpClientHelper);
    }

}
