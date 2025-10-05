package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.index.request.IndexQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.IndexQuotesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Indice;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour.ErrorCode;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour.RetrieveTradeHourInfoResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest","test"})
public class IndexLabciProtalServiceTest {

    @InjectMocks
//    @Qualifier("labciProtalIndicesService")
    private IndexLabciProtalService labciProtalService;

    private IndexQuotesRequest quotesRequest;

    private CommonRequestHeader commonRequestHeader;

    private CacheDistributeResponse response;

    @Mock
    private LabciProtalTokenService labciProtalTokenService;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private IndexQuotesCounterUSService quotesCounterUSService;

    @Mock
    private IndexQuotesLogUSService indexQuotesLogUSService;

    @Mock
    private LabciProtalProperties labciProtalProperties;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        quotesRequest = new IndexQuotesRequest();
        List<String> symbols = new ArrayList<>();
        symbols.add(".IXIC");
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
        when(labciProtalTokenService.encryptLabciToken(any(String.class),any(LabciToken.class))).thenReturn("be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c");
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg>Authentication Failed</errormsg></header><body><indiceslist><indexsymbol>.DJI</indexsymbol><indexname>Hang Seng Index</indexname><last>29357.69</last><change>61.64</change><changepct>0.21</changepct><open>29215.54</open><previousclose>29296.05</previousclose><dayhigh>29436.84</dayhigh><daylow>29089.39</daylow><w52high>31592.56</w52high><w52low>31686.67</w52low><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></indiceslist></body></envelop>";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        when(quotesCounterUSService.getUserType()).thenReturn("CUST");
        doNothing().when(indexQuotesLogUSService).updateQuoteMeterAndLog(any(HashMap.class));
        when(labciProtalProperties.getTradingParamPrefix()).thenReturn("");
        when(labciProtalProperties.getTradingParam()).thenReturn("");
        when(labciProtalProperties.getTradingHourUrl()).thenReturn("");
        when(labciProtalProperties.getTradingDayUrl()).thenReturn("");
        String httpResult2 = "{\"errorInfo\":[{\"code\":\"000\",\"traceCode\":\"111\"}]}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);
    }

    @Test
    public void testConvertRequest() throws Exception{
        List<NameValuePair> finalParams = (List<NameValuePair>) labciProtalService.convertRequest(quotesRequest,commonRequestHeader);
        assertEquals("be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c",finalParams.get(1).getValue());
    }

    @Test
    public void testResultStateConvertRequest(){
        try {
            response.setCacheExistingFlag("N");
            List<NameValuePair> finalParams = (List<NameValuePair>)labciProtalService.convertRequest(quotesRequest,commonRequestHeader);
            assertNotNull(finalParams);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testExecute() throws Exception{
        List<NameValuePair> finalParams = (List<NameValuePair>)labciProtalService.convertRequest(quotesRequest,commonRequestHeader);
        String xmlResp = (String) labciProtalService.execute(finalParams);
        assertNotNull(xmlResp);
    }

    @Test
    public void testValidateServiceResponse() throws Exception{
        List<NameValuePair> finalParams = (List<NameValuePair>)labciProtalService.convertRequest(quotesRequest,commonRequestHeader);
        String xmlResp = (String) labciProtalService.execute(finalParams);
        String xmlObj = (String) labciProtalService.validateServiceResponse(xmlResp);
        assertNotNull(xmlObj);
    }
    @Test
    public void testConvertResponse() throws Exception{
        List<NameValuePair> finalParams = (List<NameValuePair>)labciProtalService.convertRequest(quotesRequest,commonRequestHeader);
        String xmlResp = (String) labciProtalService.execute(finalParams);
        String response = (String)labciProtalService.validateServiceResponse(xmlResp);
        IndexQuotesResponse indexQuotesResponse = labciProtalService.convertResponse(response);
        List<Indice> indiceList = indexQuotesResponse.getIndices();
        for (Indice indice : indiceList){
            assertEquals("Hang Seng Index",indice.getName());
        }
    }

    @Test
    public void testSymbol1ConvertResponse() throws Exception{
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg>Authentication Failed</errormsg></header><body><indiceslist><indexsymbol>.IXIC</indexsymbol><indexname>Hang Seng Index</indexname><last>29357.69</last><change>61.64</change><changepct>0.21</changepct><open>29215.54</open><previousclose>29296.05</previousclose><dayhigh>29436.84</dayhigh><daylow>29089.39</daylow><w52high>31592.56</w52high><w52low>31686.67</w52low><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></indiceslist></body></envelop>";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        List<NameValuePair> finalParams = (List<NameValuePair>)labciProtalService.convertRequest(quotesRequest,commonRequestHeader);
        String xmlResp = (String) labciProtalService.execute(finalParams);
        String response = (String)labciProtalService.validateServiceResponse(xmlResp);
        IndexQuotesResponse indexQuotesResponse = labciProtalService.convertResponse(response);
        List<Indice> indiceList = indexQuotesResponse.getIndices();
        for (Indice indice : indiceList){
            assertEquals(".IXIC",indice.getSymbol());
        }
    }

    @Test
    public void testSymbol2ConvertResponse() throws Exception{
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg>Authentication Failed</errormsg></header><body><indiceslist><indexsymbol>.GSPC</indexsymbol><indexname>Hang Seng Index</indexname><last>29357.69</last><change>61.64</change><changepct>0.21</changepct><open>29215.54</open><previousclose>29296.05</previousclose><dayhigh>29436.84</dayhigh><daylow>29089.39</daylow><w52high>31592.56</w52high><w52low>31686.67</w52low><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></indiceslist></body></envelop>";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        List<NameValuePair> finalParams = (List<NameValuePair>)labciProtalService.convertRequest(quotesRequest,commonRequestHeader);
        String xmlResp = (String) labciProtalService.execute(finalParams);
        String response = (String)labciProtalService.validateServiceResponse(xmlResp);
        IndexQuotesResponse indexQuotesResponse = labciProtalService.convertResponse(response);
        List<Indice> indiceList = indexQuotesResponse.getIndices();
        for (Indice indice : indiceList){
            assertEquals(".GSPC",indice.getSymbol());
        }
    }

    @Test
    public void testCheckTradingHour() throws Exception{
        RetrieveTradeHourInfoResponse response = labciProtalService.checkTradingHour(commonRequestHeader);
        List<ErrorCode> errorInfo = response.getErrorInfo();
        for (ErrorCode errorCode : errorInfo){
            assertEquals("111", errorCode.getTraceCode());
        }
    }

    @Test
    public void testCheckTradingDate() throws Exception{
        String httpResult = "{\"errorInfo\":[{\"code\":\"000\",\"traceCode\":\"111\"}],\"tradeDateList\":[{\"orderTradeDate\":\"2021-08-23\"}]}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult);
        boolean flag = labciProtalService.checkTradingDate(commonRequestHeader);
        assertEquals(false, flag);
    }


}
