package com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties;

import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.LabciPortalTokenService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.configuration.HttpClientManagerAutoConfiguration;
import org.junit.Assert;
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

        LabciProtalProperties.class

}, initializers=
        ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties
@SpringBootTest
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class LabciPortalPropertiesTest {

    @Autowired
    private LabciProtalProperties labciProtalProperties;

    @Test
    public void testLoad() {
        System.out.println(labciProtalProperties);
        Assert.assertNotNull(labciProtalProperties);
    }
}
