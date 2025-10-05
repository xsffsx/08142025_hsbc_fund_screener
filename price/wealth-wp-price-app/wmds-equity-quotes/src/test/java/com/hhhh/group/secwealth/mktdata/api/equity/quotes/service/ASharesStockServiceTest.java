package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncherTest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.ASharesStock;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import org.apache.http.NameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.ASharesStockService.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class ASharesStockServiceTest {

    @InjectMocks
    ASharesStockService aSharesStockService;

    @Mock
    private EtnetProperties etnetProperties;

    @Mock
    private HttpClientHelper httpClientHelper;

    private final String MOCK_ETNET_RESPONSE = "{\"aSharesStock\":[{\"exchange\":\"SZSE\",\"aSymbol\":\"300837\",\"aNameeng\":\"ZHE KUANG HEAVY\",\"aNametc\":\"浙礦股份\",\"aNamesc\":\"浙矿股份\",\"stockConnectStatus\":\"N\",\"eligibility\":\"N\",\"hSymbol\":\"\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"},{\"exchange\":\"SZSE\",\"aSymbol\":\"300838\",\"aNameeng\":\"ZHEJIANG LINUO\",\"aNametc\":\"浙江力諾\",\"aNamesc\":\"浙江力诺\",\"stockConnectStatus\":\"N\",\"eligibility\":\"N\",\"hSymbol\":\"\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"},{\"exchange\":\"SZSE\",\"aSymbol\":\"300839\",\"aNameeng\":\"BOHUI CHEMICAL\",\"aNametc\":\"博匯股份\",\"aNamesc\":\"博汇股份\",\"stockConnectStatus\":\"N\",\"eligibility\":\"N\",\"hSymbol\":\"\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"},{\"exchange\":\"SZSE\",\"aSymbol\":\"000488\",\"aNameeng\":\"CHENMING PAPER\",\"aNametc\":\"晨鳴紙業\",\"aNamesc\":\"晨鸣纸业\",\"stockConnectStatus\":\"Y\",\"eligibility\":\"Y\",\"hSymbol\":\"01812\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"}]}";

    private final String MOCK_ETNET_URL = "https://xxxxxx/HSAPI/GetData/ASharesStock";

    private final String MOCK_ETNET_TOKEN = "XXXXXXXXXXXXXXXXXX";

    private final String MOCK_PROXY_NAME = "XXXX";

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(etnetProperties.getAsharesStockUrl()).thenReturn(MOCK_ETNET_URL);
        when(etnetProperties.getEtnetToken()).thenReturn(MOCK_ETNET_TOKEN);
        when(etnetProperties.getProxyName()).thenReturn(MOCK_PROXY_NAME);
        aSharesStockMap = new HashMap<>();
    }

    @Test
    public void testInitASharesStock() throws Exception {
        when(httpClientHelper.doGet(eq(MOCK_PROXY_NAME), eq(MOCK_ETNET_URL), anyListOf(NameValuePair.class), any())).thenReturn(MOCK_ETNET_RESPONSE);

        try{
            aSharesStockService.initASharesStock();
            Assert.assertEquals(4, aSharesStockMap.size());
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("test initASharesStock when db is empty with fail");
        }
    }

    @Test
    public void testInitASharesStockWhenETNetIsUnavailable() throws Exception {
        when(httpClientHelper.doGet(eq(MOCK_PROXY_NAME), eq(MOCK_ETNET_URL), anyListOf(NameValuePair.class), any())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        try{
            aSharesStockService.initASharesStock();
            Assert.assertEquals(0, aSharesStockMap.size());
            verify(httpClientHelper, times(REQUEST_ETNET_MAX_TIMES)).doGet(eq(MOCK_PROXY_NAME), eq(MOCK_ETNET_URL), anyListOf(NameValuePair.class), any());
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("test initASharesStock when etnet is unavailable with fail");
        }
    }


    @Test
    public void testReloadASharesStockFromETNetWhenStocksChange() throws Exception {
        List<ASharesStock> mockASharesStocks = new ArrayList<>();
        mockASharesStocks.add(new ASharesStock("", "A", "", "", "", "", "", "", new Timestamp(System.currentTimeMillis())));
        mockASharesStocks.add(new ASharesStock("", "B", "", "", "", "", "", "", new Timestamp(System.currentTimeMillis())));
        for (ASharesStock stock : mockASharesStocks) {
            aSharesStockMap.put(stock.getASymbol(), stock);
        }
        when(httpClientHelper.doGet(eq(MOCK_PROXY_NAME), eq(MOCK_ETNET_URL), anyListOf(NameValuePair.class), any())).thenReturn(MOCK_ETNET_RESPONSE);

        Assert.assertEquals(2, aSharesStockMap.size());
        aSharesStockService.reloadASharesStockFromETNet();
        Assert.assertEquals(4, aSharesStockMap.size());
    }

    @Test
    public void testSetASharesInfoWhenCacheIsEmpty() throws Exception {
        when(httpClientHelper.doGet(eq(MOCK_PROXY_NAME), eq(MOCK_ETNET_URL), anyListOf(NameValuePair.class), any())).thenReturn(MOCK_ETNET_RESPONSE);
        aSharesStockService.setASharesInfo(new QuotesLabciQuote());
        Assert.assertEquals(4, aSharesStockMap.size());
    }

    @Test
    public void testSetASharesInfoWhenCacheIsEmptyAndETNetIsUnavailable() throws Exception {
        when(httpClientHelper.doGet(eq(MOCK_PROXY_NAME), eq(MOCK_ETNET_URL), anyListOf(NameValuePair.class), any())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        aSharesStockService.setASharesInfo(new QuotesLabciQuote());
        Assert.assertEquals(0, aSharesStockMap.size());
    }

    @Test
    public void testSetASharesInfoWhenSymbolIsInAShareStockMapping() {
        aSharesStockMap.put("AAA", new ASharesStock("", "AAA", "", "", "", "", "Y", "HHH", new Timestamp(System.currentTimeMillis())));
        QuotesLabciQuote quote = new QuotesLabciQuote();
        quote.setSymbol("AAA");
        aSharesStockService.setASharesInfo(quote);
        Assert.assertEquals("Y", quote.getEligibility());
        Assert.assertEquals("HHH", quote.getHpgCode());
    }

    @Test
    public void testSetASharesInfoWhenSymbolIsNotInAShareStockMapping() {
        aSharesStockMap.put("AAA", new ASharesStock("", "AAA", "", "", "", "", "Y", "HHH", new Timestamp(System.currentTimeMillis())));
        QuotesLabciQuote quote = new QuotesLabciQuote();
        quote.setSymbol("BBB");
        aSharesStockService.setASharesInfo(quote);
        Assert.assertNull(quote.getEligibility());
        Assert.assertNull(quote.getHpgCode());
    }


}
