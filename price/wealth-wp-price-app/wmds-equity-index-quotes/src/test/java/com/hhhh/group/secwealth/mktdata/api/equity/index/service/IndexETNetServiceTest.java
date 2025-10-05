package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.index.request.IndexQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.IndexQuotesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Indice;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest","test"})
public class IndexETNetServiceTest {

    @InjectMocks
    private IndexETNetService indexETNetService;

    private IndexQuotesRequest quotesRequest;

    private CommonRequestHeader commonRequestHeader;

    private CacheDistributeResponse response;

    @Mock
    private EtnetProperties etnetProperties;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private QuoteAccessLogService quoteAccessLogService;

    @Mock
    private LabciProperties labciProps;

    @Mock
    private QuoteUserService quoteUserService;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(etnetProperties.getEtnetTokenWithoutVerify()).thenReturn("be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c");
        quotesRequest = new IndexQuotesRequest();
        List<String> symbols = new ArrayList<>();
        symbols.add("HSC");
        quotesRequest.setSymbol(symbols);
        quotesRequest.setMarket("HK");

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");

        response = new CacheDistributeResponse();
        response.setCacheExistingFlag("Y");
        response.setKey("eID");
        response.setValue("{\"eID\":\"001\",\"stsCode\":\"0\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"stsCode\":\"0\",\"displayName\":\"hhhh HOLDINGS\",\"child\":[{ \"_item\":\"001\", \"MNEMONIC\":\"CSI300\", \"DSPLY_NAME\":\"大喜屋集團\",\"TRDPRC_1\":8.95,\"NETCHNG_1\":11.88,\"PCTCHNG\":-0.83,\"CURRENCY\":\"HKD\",\"TRADE_DATE\":\"2020-03-23\",\"TRDTIM_1\":\"02:15:00.000Z\"}],\"timeZone\":{ \"gmtOffset\":1, \"shortName\":\"11111\", \"summerEnd\":\"2222\",\"summerOffset\":3,\"summerStart\":\"3333\" },\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}");
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.complete(response);
        CacheDistributeHolder.putCacheDistribute(future);
        String httpResult = "{\"moverType\":\"VOL\",\"err_code\":\"2\",\"boardType\":\"MAIN\",\"stockList\":[{\"symbol\":\"1398\",\"name\":\"ICBC\"},{\"symbol\":\"939\",\"name\":\"CCB\"},{\"symbol\":\"3988\",\"name\":\"BANK OF CHINA\"},{\"symbol\":\"2800\",\"name\":\"TRACKER FUND\"},{\"symbol\":\"857\",\"name\":\"PETROCHINA\"},{\"symbol\":\"1091\",\"name\":\"SOUTH MANGANESE\"},{\"symbol\":\"728\",\"name\":\"CHINA TELECOM\"},{\"symbol\":\"8133\",\"name\":\"JETE POWER\"},{\"symbol\":\"1288\",\"name\":\"ABC\"},{\"symbol\":\"762\",\"name\":\"CHINA UNICOM\"}],\"asOfDateTime\":\"2021-08-17T16:42:00.000+0800\"}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult);

//        String httpResult2 = "{\"moverType\":\"VOL\",\"boardType\":\"MAIN\",\"indices\":[{\"symbol\":\"1398\",\"name\":\"ICBC\"},{\"symbol\":\"939\",\"name\":\"CCB\"},{\"symbol\":\"3988\",\"name\":\"BANK OF CHINA\"},{\"symbol\":\"2800\",\"name\":\"TRACKER FUND\"},{\"symbol\":\"857\",\"name\":\"PETROCHINA\"},{\"symbol\":\"1091\",\"name\":\"SOUTH MANGANESE\"},{\"symbol\":\"728\",\"name\":\"CHINA TELECOM\"},{\"symbol\":\"8133\",\"name\":\"JETE POWER\"},{\"symbol\":\"1288\",\"name\":\"ABC\"},{\"symbol\":\"762\",\"name\":\"CHINA UNICOM\"}],\"asOfDateTime\":\"2021-08-17T16:42:00.000+0800\"}";
        String httpResult2 = "{\n" +
                "    \"indices\": [\n" +
                "        {\n" +
                "            \"symbol\": \"HSI\",\n" +
                "            \"name\": \"恒生指数\",\n" +
                "            \"lastPrice\": 18226.58,\n" +
                "            \"changeAmount\": -31.99,\n" +
                "            \"changePercent\": -0.1752,\n" +
                "            \"openPrice\": 18237.41,\n" +
                "            \"previousClosePrice\": 18258.57,\n" +
                "            \"dayRangeHigh\": 18426.92,\n" +
                "            \"dayRangeLow\": 18212.58,\n" +
                "            \"changePercent1M\": 3.5,\n" +
                "            \"changePercent2M\": 5.3,\n" +
                "            \"changePercent3M\": 1.1,\n" +
                "            \"changePercent1Y\": 0.9,\n" +
                "            \"oneMonthLowPrice\": 16964.28,\n" +
                "            \"twoMonthLowPrice\": 16441.44,\n" +
                "            \"threeMonthLowPrice\": 16441.44,\n" +
                "            \"oneMonthHighPrice\": 18355.15,\n" +
                "            \"twoMonthHighPrice\": 18426.92,\n" +
                "            \"threeMonthHighPrice\": 18426.92,\n" +
                "            \"yearHighPrice\": 19706.12,\n" +
                "            \"yearLowPrice\": 14794.16,\n" +
                "            \"asOfDateTime\": \"2024-09-23T16:00:00.000+0800\",\n" +
                "            \"asOfDate\": null,\n" +
                "            \"asOfTime\": null\n" +
                "        }\n" +
                "    ],\n" +
                "    \"messages\": null\n" +
                "}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult2);
        doNothing().when(quoteAccessLogService).saveQuoteAccessLog(any(ArrayList.class));
    }

    @Test
    public void testConvertRequest() throws Exception{
        Map<String, String> serviceRequestMapper =(Map<String, String>)indexETNetService.convertRequest(quotesRequest,commonRequestHeader);
        assertNotNull(serviceRequestMapper);
    }

    @Test
    public void testResultStateConvertRequest(){
        try {
            response.setCacheExistingFlag("N");
            Map<String, String> serviceRequestMapper =(Map<String, String>)indexETNetService.convertRequest(quotesRequest,commonRequestHeader);
            assertNotNull(serviceRequestMapper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testExecute() throws Exception{
        Map<String, String> serviceRequestMapper =(Map<String, String>)indexETNetService.convertRequest(quotesRequest,commonRequestHeader);
        Map<String, String> response = (Map<String, String>)indexETNetService.execute(serviceRequestMapper);
        assertNotNull(response);
    }

    @Test
    public void testValidateServiceResponse() throws Exception{
        Map<String, String> serviceRequestMapper =(Map<String, String>)indexETNetService.convertRequest(quotesRequest,commonRequestHeader);
        Map<String, String> response = (Map<String, String>)indexETNetService.execute(serviceRequestMapper);
        Map<String, String>validateServiceResponse =(Map<String, String>)indexETNetService.validateServiceResponse(response);
        assertNotNull(validateServiceResponse.get("HK"));
    }

    @Test
    public void testConvertResponse() throws Exception{
        Map<String, String> serviceRequestMapper =(Map<String, String>)indexETNetService.convertRequest(quotesRequest,commonRequestHeader);
        Map<String, String> response = (Map<String, String>)indexETNetService.execute(serviceRequestMapper);
        Map<String, String> serviceResponse = (Map<String, String>)indexETNetService.validateServiceResponse(response);
        IndexQuotesResponse convertResponse = indexETNetService.convertResponse(serviceResponse);
        List<Indice> indiceList = convertResponse.getIndices();
        assertEquals("恒生指数",indiceList.get(0).getName());

    }
    @Test
    public void testLocalConvertResponse() throws Exception{
        commonRequestHeader.setLocale("zh_CN");
        Map<String, String> serviceRequestMapper =(Map<String, String>)indexETNetService.convertRequest(quotesRequest,commonRequestHeader);
        Map<String, String> response = (Map<String, String>)indexETNetService.execute(serviceRequestMapper);
        Map<String, String> serviceResponse = (Map<String, String>)indexETNetService.validateServiceResponse(response);
        IndexQuotesResponse convertResponse = indexETNetService.convertResponse(serviceResponse);
        List<Indice> indiceList = convertResponse.getIndices();
        assertEquals("HSI",indiceList.get(0).getSymbol());
    }
    @Test
    public void testLocal2ConvertResponse() throws Exception{
        commonRequestHeader.setLocale("zh_HK");
        Map<String, String> serviceRequestMapper =(Map<String, String>)indexETNetService.convertRequest(quotesRequest,commonRequestHeader);
        Map<String, String> response = (Map<String, String>)indexETNetService.execute(serviceRequestMapper);
        Map<String, String> serviceResponse = (Map<String, String>)indexETNetService.validateServiceResponse(response);
        IndexQuotesResponse convertResponse = indexETNetService.convertResponse(serviceResponse);
        List<Indice> indiceList = convertResponse.getIndices();
        assertEquals("恒生指数",indiceList.get(0).getName());
    }
}
