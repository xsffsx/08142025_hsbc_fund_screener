package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncherTest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteCounterRepository;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.configuration.HttpClientManagerAutoConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {
        HttpClientManagerAutoConfiguration.class,
        PredSrchService.class,
        PredSrchProperties.class,
        LabciProtalProperties.class,
        //QuoteCounterRepository.class,
        NameIdHandler.class,
        QuotesUSCommonService.class


}, initializers=
        ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties
@SpringBootTest(classes = ServerLauncherTest.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class QuotesUSCommonServiceTest {

    @Mock
    private PredSrchService predSrchService;

    @Mock
    private QuotesUSCommonService quotesUSCommonService;

    @Test
    public void testCallPredictiveSearch() {
        PredSrchResponse predSrchResponse = this.predSrchService.localPredSrch("BIDU");
        System.out.println(predSrchResponse);
        Assert.assertNotNull(predSrchService);
    }


    @Test
    public void testInitQuotesUSCommonService() {
        System.out.println(quotesUSCommonService);
        Assert.assertNotNull(quotesUSCommonService);
    }
}
