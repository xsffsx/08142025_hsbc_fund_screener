package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncherTest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.LabciPortalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.PastPerformance;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest"})
public class PastPerformanceServiceTest {

    @InjectMocks
    PastPerformanceService pastPerformanceService;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private LabciPortalTokenService labciPortalTokenService;

    @Mock
    private LabciPortalProperties labciPortalProperties;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(labciPortalTokenService.encryptLabciPortalToken(any(String.class),any(String.class))).thenReturn("tokenxxxxxxx");
        when(labciPortalProperties.getProxyName()).thenReturn("xxx");
        when(labciPortalProperties.getPastPerformanceUrl()).thenReturn("http://xxxxxxxx");
    }

    @Test
    public void testWhenLabciReturnWithIncorrectJson() throws Exception {
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(String.class), anyObject())).thenReturn("{\"stsCode\":\"462\",\"stsTxt\":\"INVALID_REQUEST: Invalid nb_ric \\u003d SQQQ.NB\",\"result\":[]}");
        PastPerformance pastPerformance = pastPerformanceService.getPastPerformance("OHI", "US", "= SQQQ.NB");
        assertNull(pastPerformance);
    }

}
