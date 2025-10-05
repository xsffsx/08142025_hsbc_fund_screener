package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo.StockInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.LabciPortalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.PastPerformance;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.rbpTradingHour.RetrieveTradeHourInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.stockinfo.QuotesUSLabciPortalService;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import com.google.gson.reflect.TypeToken;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest"})
public class QuotesUSCachingServerServiceTest {

    @InjectMocks
    private QuotesUSCachingServerService quotesUSCachingServerService;

    private SECQuotesRequest secQuotesRequest;

    private CommonRequestHeader commonRequestHeader;

    private CacheDistributeResponse response;

    private PredSrchResponse predSrchResponse;

    @Mock
    private PredSrchService predSrchService;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private QuotesUSCommonService quotesUSCommonService;

    @Mock
    private QuotesUSLabciPortalService quotesUSLabciPortalService;

    @Mock
    private QuotesUSLogService quotesUSLogService;

    @Mock
    private LabciPortalTokenService labciPortalTokenService;

    @Mock
    private LabciPortalProperties labciPortalProperties;

    @Mock
    private ExResponseComponent exRespComponent;

    @Mock
    private LabciProperties labciProperties;

    @Mock
    private ApplicationProperties appProps;


    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_SITE, "HK_HASE");
        secQuotesRequest = new SECQuotesRequest();
        secQuotesRequest.setMarket("US");
        secQuotesRequest.setDelay(true);
        secQuotesRequest.setRequestType("20");
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProdAltNum("IBM");
        productKey.setProdCdeAltClassCde("M");
        productKey.setProductType("SEC");
        productKey.setExchange("SHS");
        productKeys.add(0,productKey);
        secQuotesRequest.setProductKeys(productKeys);

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setFunctionId("01");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
        response = new CacheDistributeResponse();
        response.setCacheExistingFlag("Y");
        response.setKey("eID");
        response.setValue("{\"eID\":\"001\",\"permID\":\"123\",\"stsCode\":\"0\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"stsCode\":\"0\",\"displayName\":\"hhhh HOLDINGS\",\"child\":[{ \"_item\":\"001\", \"MNEMONIC\":\"CSI300\", \"DSPLY_NAME\":\"大喜屋集團\",\"TRDPRC_1\":8.95,\"NETCHNG_1\":11.88,\"PCTCHNG\":-0.83,\"CURRENCY\":\"HKD\",\"TRADE_DATE\":\"2020-03-23\",\"TRDTIM_1\":\"02:15:00.000Z\"}],\"timeZone\":{ \"gmtOffset\":1, \"shortName\":\"11111\", \"summerEnd\":\"2222\",\"summerOffset\":3,\"summerStart\":\"3333\" },\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}");
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.complete(response);
        CacheDistributeHolder.putCacheDistribute(future);

        predSrchResponse = new PredSrchResponse();
        predSrchResponse.setProductType("SEC");
        predSrchResponse.setCountryTradableCode("CN");
        predSrchResponse.setProductName("hhhh HOLDINGS PLC");
        predSrchResponse.setProductShortName("CSI 300 Index");
        predSrchResponse.setSymbol("IBM");
        predSrchResponse.setProductCode("2");
        predSrchResponse.setMarket("HK");
        predSrchResponse.setExchange("HKG");
        List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
        ProdAltNumSeg prodAltNumSeg1 = new ProdAltNumSeg();
        prodAltNumSeg1.setProdAltNum("CSI300");
        prodAltNumSeg1.setProdCdeAltClassCde("M");
        ProdAltNumSeg prodAltNumSeg2 = new ProdAltNumSeg();
        prodAltNumSeg2.setProdAltNum(".CSI300");
        prodAltNumSeg2.setProdCdeAltClassCde("T");
        ProdAltNumSeg prodAltNumSeg3 = new ProdAltNumSeg();
        prodAltNumSeg3.setProdAltNum("2");
        prodAltNumSeg3.setProdCdeAltClassCde("W");
        prodAltNumSegs.add(prodAltNumSeg1);
        prodAltNumSegs.add(prodAltNumSeg2);
        prodAltNumSegs.add(prodAltNumSeg3);

        predSrchResponse.setProdAltNumSegs(prodAltNumSegs);
        ArrayList<PredSrchResponse> predSrchResponses = new ArrayList<>();
        predSrchResponses.add(predSrchResponse);
        when(predSrchService.precSrch(any(PredSrchRequest.class), any(CommonRequestHeader.class))).thenReturn(predSrchResponses);
        when(predSrchService.localPredSrch(any(String.class),any(String.class),any(String.class))).thenReturn(predSrchResponse);
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=.CSI300\">.CSI300</fid><fid id=\"SERVICE\">dIDN_RDF</fid><fid id=\"TRDPRC_1\">4869.45870</fid><fid id=\"ADJUST_CLS\">4805.60990</fid><fid id=\"OFF_CLOSE\">4869.45870</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">63.84880</fid><fid id=\"PCTCHNG\">1.33</fid><fid id=\"TRADE_DATE\">01 SEP 2021</fid><fid id=\"TRDTIM_1\">07:54</fid><fid id=\"OPEN_PRC\">4804.68630</fid><fid id=\"HST_CLOSE\">4805.60990</fid><fid id=\"LOW_1\">4763.29920</fid><fid id=\"HIGH_1\">4906.40240</fid><fid id=\"YRLOW\">4663.90090</fid><fid id=\"LIFE_LOW\">807.78400</fid><fid id=\"52WK_LOW\">4554.71990</fid><fid id=\"YRHIGH\">5930.91220</fid><fid id=\"LIFE_HIGH\">5930.91220</fid><fid id=\"52WK_HIGH\">5930.91220</fid><fid id=\"ACVOL_1\">326721</fid><fid id=\"PERATIO\">0</fid><fid id=\"TURNOVER\">568819.748</fid><fid id=\"RDN_EXCHID\"/><fid id=\"RDN_EXCHD2\">CSI</fid><fid id=\"DSPLY_NAME\">DELAYED-30CSI300</fid><fid id=\"DSPLY_NMLL\">滬深300指數|沪深300指数</fid><fid id=\"TRDPRC_2\">4869.41360</fid><fid id=\"TRDPRC_3\">4866.92740</fid><fid id=\"TRDPRC_4\">4866.87950</fid><fid id=\"TRDPRC_5\">4866.87950</fid><fid id=\"SALTIM_MS\">28486000</fid></ric></watchlist>\n";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult);
        doNothing().when(quotesUSCommonService).validate(any(SECQuotesRequest.class),any(CommonRequestHeader.class));
        when(quotesUSCommonService.checkTradingDate(any(CommonRequestHeader.class))).thenReturn(true);
        String rbpTradingDate = "{\"tradingHoursList\":[{\"tradingSessionCode\":\"AFTMTH\",\"tradingSessionEndTime\":\"235959\",\"tradingSessionEndTimeTimezone\":\"-0400\",\"tradingSubSessionCode\":\"SESS01\"},{\"tradingSessionCode\":\"CLOSIN\",\"tradingSubSessionCode\":\"ORDINP\"},{\"tradingSessionCode\":\"CLOSIN\",\"tradingSubSessionCode\":\"ORDMTH\"},{\"tradingSessionCode\":\"CLOSIN\",\"tradingSubSessionCode\":\"PREMTH\"},{\"tradingSessionCode\":\"NORMAL\",\"tradingSessionEndTime\":\"235959\",\"tradingSessionEndTimeTimezone\":\"-0400\",\"tradingSessionStartTime\":\"000000\",\"tradingSessionStartTimeTimezone\":\"-0400\",\"tradingSubSessionCode\":\"SESS01\"},{\"tradingSessionCode\":\"NORMAL\",\"tradingSubSessionCode\":\"SESS02\"},{\"tradingSessionCode\":\"NORMAL\",\"tradingSubSessionCode\":\"SESS03\"},{\"tradingSessionCode\":\"PREOPN\",\"tradingSubSessionCode\":\"ORDINP\"},{\"tradingSessionCode\":\"PREOPN\",\"tradingSubSessionCode\":\"ORDMTH\"},{\"tradingSessionCode\":\"PREOPN\",\"tradingSubSessionCode\":\"PREMTH\"}]}";
        RetrieveTradeHourInfoResponse response = JsonUtil.fromJson(rbpTradingDate, new TypeToken<RetrieveTradeHourInfoResponse>() {}.getType());
        when(quotesUSCommonService.checkTradingHour(any(CommonRequestHeader.class))).thenReturn(response);
        doNothing().when(quotesUSCommonService).validateCacheDistributeResult(any(CacheDistributeResult.class));
        when(quotesUSCommonService.callLabci(any(String.class),any(String.class))).thenReturn(httpResult);
        StockInfo stockInfo = new StockInfo();
        stockInfo.setNbRic("000");
        stockInfo.setSymbol("0005");
        stockInfo.setName("hhhh");
        stockInfo.setIsETF(true);
        stockInfo.setIsADR(true);
        stockInfo.setPERatio(new BigDecimal(12.02));
        stockInfo.setExpenseRatio(new BigDecimal(15.0));
        stockInfo.setMarketCap(new BigDecimal(1317376));
        stockInfo.setDivYield(new BigDecimal(125));
        stockInfo.setExpectedDividendYield(new BigDecimal(2136));
        stockInfo.setCcy("HKD");
        stockInfo.setIndustry("hengshen");
        stockInfo.setExDivDate("2021-09-01");
        when(quotesUSLabciPortalService.getStockInfo(any(String.class),any(String.class),any(String.class),any(String.class))).thenReturn(stockInfo);
        PastPerformance pastPerformance = new PastPerformance();
        pastPerformance.setNbRic("000");
        pastPerformance.setMPC1(new BigDecimal(10));
        pastPerformance.setMPC3(new BigDecimal(20));
        pastPerformance.setWPC1(new BigDecimal(11));
        pastPerformance.setMPC3(new BigDecimal(112));
        pastPerformance.setMPC3(new BigDecimal(111));
//        when(pastPerformanceService.getPastPerformance(any(String.class),any(String.class),any(String.class))).thenReturn(pastPerformance);

        when(labciPortalTokenService.encryptLabciPortalToken(any(String.class),any(String.class))).thenReturn("tokenxxxxxxx");
        when(labciPortalProperties.getProxyName()).thenReturn("xxx");
        when(labciPortalProperties.getPastPerformanceUrl()).thenReturn("http://xxxxxxxx");
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(String.class), anyObject())).thenReturn("{\"stsCode\":\"462\",\"stsTxt\":\"INVALID_REQUEST: Invalid nb_ric \\u003d SQQQ.NB\",\"result\":[]}");

        doNothing().when(quotesUSLogService).updateQuoteMeterAndLog(any(HashMap.class));
    }


    @Test
    public void testConvertRequest() throws Exception {
        Object obj = quotesUSCachingServerService.convertRequest(secQuotesRequest, commonRequestHeader);
        assertNotNull(obj);
    }

    @Test
    public void testReStatusConvertRequest() throws Exception {
        try {
            response.setCacheExistingFlag("N");
            Object obj = quotesUSCachingServerService.convertRequest(secQuotesRequest, commonRequestHeader);
            assertNotNull(obj);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testResTypeConvertRequest() throws Exception {
        try {
            secQuotesRequest.setRequestType("10");
            Object obj = quotesUSCachingServerService.convertRequest(secQuotesRequest, commonRequestHeader);
            assertNotNull(obj);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void testExecute() throws Exception {
        Object quoteServiceRequest = this.quotesUSCachingServerService.convertRequest(secQuotesRequest, commonRequestHeader);
        Object serviceResponseMapper = this.quotesUSCachingServerService.execute(quoteServiceRequest);
        assertNotNull(serviceResponseMapper);
    }


    @Test
    public void testValidateServiceResponse() throws Exception {
        Object quoteServiceRequest = this.quotesUSCachingServerService.convertRequest(secQuotesRequest, commonRequestHeader);
        Object serviceResponseMapper = this.quotesUSCachingServerService.execute(quoteServiceRequest);
        assertNotNull(quotesUSCachingServerService.validateServiceResponse(serviceResponseMapper));
    }


}
