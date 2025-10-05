package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncherTest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MarketHourProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest"})
public class QuotesHaseMidfsServiceTest {

    @InjectMocks
    private QuotesHaseMidfsService quotesHaseMidfsService;

    private SECQuotesRequest secQuotesRequest;

    private CommonRequestHeader commonRequestHeader;

    private CommonRequestHeader commonRequestHeader1;

    private CacheDistributeResponse response;

    @Mock
    private PredSrchService predSrchService;

    private PredSrchResponse predSrchResponse;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private TradingSessionService tradingSessionService;

    @Mock
    private EtnetProperties etnetProperties;

    @Mock
    private QuoteAccessLogService quoteAccessLogService;

    @Mock
    private MarketHourProperties marketHourProperties;

    @Mock
    private ExResponseComponent exRespComponent;

    @Mock
    private LabciProperties labciProps;

    @Mock
    private QuoteUserService quoteUserService;




    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_SITE, "HK_HASE");
        secQuotesRequest = new SECQuotesRequest();
        secQuotesRequest.setMarket("HK");
        secQuotesRequest.setRequestType("20");
        secQuotesRequest.setDelay(false);
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProdAltNum("IBM");
        productKey.setProdCdeAltClassCde("M");
        productKey.setProductType("SEC");
        productKey.setExchange("SHS");
        productKeys.add(0,productKey);
//        ProductKey productKey2 = new ProductKey();
//        productKey2.setProdAltNum("AAPL");
//        productKey2.setProdCdeAltClassCde("M");
//        productKey2.setProductType("ALL");
//        productKey.setExchange("HKG");
//        productKeys.add(1,productKey2);
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
        response.setValue("{\"eID\":\"001\",\"stsCode\":\"0\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"stsCode\":\"0\",\"displayName\":\"hhhh HOLDINGS\",\"child\":[{ \"_item\":\"001\", \"MNEMONIC\":\"CSI300\", \"DSPLY_NAME\":\"大喜屋集團\",\"TRDPRC_1\":8.95,\"NETCHNG_1\":11.88,\"PCTCHNG\":-0.83,\"CURRENCY\":\"HKD\",\"TRADE_DATE\":\"2020-03-23\",\"TRDTIM_1\":\"02:15:00.000Z\"}],\"timeZone\":{ \"gmtOffset\":1, \"shortName\":\"11111\", \"summerEnd\":\"2222\",\"summerOffset\":3,\"summerStart\":\"3333\" },\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}");
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
        String httpResult = "{\"moverType\":\"VOL\",\"err_code\":\"2\",\"boardType\":\"MAIN\",\"stockList\":[{\"symbol\":\"1398\",\"name\":\"ICBC\"},{\"symbol\":\"939\",\"name\":\"CCB\"},{\"symbol\":\"3988\",\"name\":\"BANK OF CHINA\"},{\"symbol\":\"2800\",\"name\":\"TRACKER FUND\"},{\"symbol\":\"857\",\"name\":\"PETROCHINA\"},{\"symbol\":\"1091\",\"name\":\"SOUTH MANGANESE\"},{\"symbol\":\"728\",\"name\":\"CHINA TELECOM\"},{\"symbol\":\"8133\",\"name\":\"JETE POWER\"},{\"symbol\":\"1288\",\"name\":\"ABC\"},{\"symbol\":\"762\",\"name\":\"CHINA UNICOM\"}],\"asOfDateTime\":\"2021-08-17T16:42:00.000+0800\"}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult);
        String httpResult2 = "{\"priceQuotes\": [{\"prodAltNumSegs\": [{\"prodCdeAltClassCde\": \"I\",\"prodAltNum\": \"GB0005405286\"},{\"prodCdeAltClassCde\": \"M\",\"prodAltNum\": \"0005\"},{\"prodCdeAltClassCde\": \"P\",\"prodAltNum\": \"HSBA.HK\"},{ \"prodCdeAltClassCde\": \"T\",\"prodAltNum\": \"0005.HK\"},{\"prodCdeAltClassCde\": \"W\",\"prodAltNum\": \"20001968\"}],\"bidAskQueues\":[{ \"bidPrice\": null,\"bidSize\": 30000,\"bidOrder\": null,\"askPrice\": null,\"askSize\": 30800,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null}],\"symbol\": \"0005\",\"currency\": \"HKD\",\"market\": \"HK\",\"exchangeCode\": \"HKG\",\"productType\": \"SEC\",\"productSubType\": \"SZEQ\",\"companyName\": \"hhhh HOLDINGS PLC\",\"isSuspended\": false,\"nominalPrice\": 65.1,\"nominalPriceType\": \"N\",\"dayLowPrice\": 65,\"dayHighPrice\": 65.55,\"peRatio\": 12.02,\"previousClosePrice\": 65.2,\"yearLowPrice\": 60.35,\"bidPrice\": 65.1,\"askPrice\": 65.15,\"asOfDateTime\": \"2019-05-21T07:51:22.000Z\",\"turnover\": 761308548,\"sharesOutstanding\": null,\"dividendYield\": 6.09,\"volume\": 11665082,\"changeAmount\": -0.1,\"changePercent\": -0.15,\"delay\": true,\"marketStatus\": \"normal\",\"maturityDate\": \"2016-07-06\",\"bidSize\": 30000,\"openPrice\": 65.1,\"askSize\": 30800,\"bidSpread\": 0.05,\"askSpread\": 0.05,\"spreadCode\": \"\",\"yearHighPrice\": 78.25,\"boardLotSize\": 400,\"marketCap\": 1317376,\"brokerAskQueue\": null,\"brokerBidQueue\": null,\"vcmEligible\": null,\"casEligible\": null,\"vcmStatus\": null,\"orderImbalanceDirection\": \"N\",\"orderImbalanceQuantity\": 1000,\"casLowerPrice\": null,\"casUpperPrice\": null,\"casReferencePrice\": \"\",\"vcmStartTime\": null,\"vcmEndTime\": null,\"vcmLowerLimitPrice\": null,\"vcmUpperLimitPrice\": null,\"vcmReferencePrice\": \"\",\"upperLimitPrice\": null,\"lowerLimitPrice\": null,\"casLowerLimitPrice\": null,\"casUpperLimitPrice\": null,\"underlyingStock\": \"\",\"auctionIndicator\": null,\"eps\": 5.416,\"iep\": null,\"iev\": null,\"dividend\": 3.9649,\"prevTradePrice\": null,\"riskAlert\": false,\"riskLvlCde\": \"5\",\"settlementPrice\": null,\"primaryLastActivity\": null,\"accumulatedVolume\": null,\"unscaledTurnover\": null,\"totalSharesOutstanding\": null,\"totalIssuedShares\": null,\"derivativeFlag\": \"1\",\"callPrice\": 68.88,\"strikePrice\": 91.88,\"strikeLower\": 90.88,\"strikeUpper\": 91.88,\"warrantType\": \"Plain Vanilla\",\"marketStatus\": \"[3] Continuous Trading\",\"daySecLowLimPrice\": null,\"daySecUpperLimPrice\": null,\"dayLowLimPrice\": null,\"dayUpperLimPrice\": null,\"limitReferencePrice\": null,\"priceCode\": null}],\"remainingQuota\": -1,\"totalQuota\": -1,\"messages\": null,\"error_code\": \"2\"}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);
        when(etnetProperties.getEtnetTokenWithoutVerify()).thenReturn("be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c");
        String httpResult3 = "{\"priceQuotes\": [{\"prodAltNumSegs\": [{\"prodCdeAltClassCde\": \"I\",\"prodAltNum\": \"GB0005405286\"},{\"prodCdeAltClassCde\": \"M\",\"prodAltNum\": \"0005\"},{\"prodCdeAltClassCde\": \"P\",\"prodAltNum\": \"HSBA.HK\"},{ \"prodCdeAltClassCde\": \"T\",\"prodAltNum\": \"0005.HK\"},{\"prodCdeAltClassCde\": \"W\",\"prodAltNum\": \"20001968\"}],\"bidAskQueues\":[{ \"bidPrice\": null,\"bidSize\": 30000,\"bidOrder\": null,\"askPrice\": null,\"askSize\": 30800,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null}],\"symbol\": \"0005\",\"currency\": \"HKD\",\"market\": \"HK\",\"exchangeCode\": \"HKG\",\"productType\": \"SEC\",\"productSubType\": \"SZEQ\",\"companyName\": \"hhhh HOLDINGS PLC\",\"isSuspended\": false,\"nominalPrice\": 65.1,\"nominalPriceType\": \"N\",\"dayLowPrice\": 65,\"dayHighPrice\": 65.55,\"peRatio\": 12.02,\"previousClosePrice\": 65.2,\"yearLowPrice\": 60.35,\"bidPrice\": 65.1,\"askPrice\": 65.15,\"asOfDateTime\": \"2019-05-21T07:51:22.000Z\",\"turnover\": 761308548,\"sharesOutstanding\": null,\"dividendYield\": 6.09,\"volume\": 11665082,\"changeAmount\": -0.1,\"changePercent\": -0.15,\"delay\": true,\"marketStatus\": \"normal\",\"maturityDate\": \"2016-07-06\",\"bidSize\": 30000,\"openPrice\": 65.1,\"askSize\": 30800,\"bidSpread\": 0.05,\"askSpread\": 0.05,\"spreadCode\": \"\",\"yearHighPrice\": 78.25,\"boardLotSize\": 400,\"marketCap\": 1317376,\"brokerAskQueue\": null,\"brokerBidQueue\": null,\"vcmEligible\": null,\"casEligible\": null,\"vcmStatus\": null,\"orderImbalanceDirection\": \"N\",\"orderImbalanceQuantity\": 1000,\"casLowerPrice\": null,\"casUpperPrice\": null,\"casReferencePrice\": \"\",\"vcmStartTime\": null,\"vcmEndTime\": null,\"vcmLowerLimitPrice\": null,\"vcmUpperLimitPrice\": null,\"vcmReferencePrice\": \"\",\"upperLimitPrice\": null,\"lowerLimitPrice\": null,\"casLowerLimitPrice\": null,\"casUpperLimitPrice\": null,\"underlyingStock\": \"\",\"auctionIndicator\": null,\"eps\": 5.416,\"iep\": null,\"iev\": null,\"dividend\": 3.9649,\"prevTradePrice\": null,\"riskAlert\": false,\"riskLvlCde\": \"5\",\"settlementPrice\": null,\"primaryLastActivity\": null,\"accumulatedVolume\": null,\"unscaledTurnover\": null,\"totalSharesOutstanding\": null,\"totalIssuedShares\": null,\"derivativeFlag\": \"1\",\"callPrice\": 68.88,\"strikePrice\": 91.88,\"strikeLower\": 90.88,\"strikeUpper\": 91.88,\"warrantType\": \"Plain Vanilla\",\"marketStatus\": \"[3] Continuous Trading\",\"daySecLowLimPrice\": null,\"daySecUpperLimPrice\": null,\"dayLowLimPrice\": null,\"dayUpperLimPrice\": null,\"limitReferencePrice\": null,\"priceCode\": null}],\"remainingQuota\": -1,\"totalQuota\": -1,\"messages\": null,\"error_code\": \"2\"}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult3);
        doNothing().when(quoteAccessLogService).saveQuoteAccessLog(any(ArrayList.class));
        when(tradingSessionService.isTradingDay()).thenReturn(true);

        commonRequestHeader1 = new CommonRequestHeader();
        commonRequestHeader1.setCountryCode("HK");
        commonRequestHeader1.setGroupMember("HASE");
        commonRequestHeader1.setChannelId("OHB");
        commonRequestHeader1.setLocale("en");
        commonRequestHeader1.setAppCode("STB");
        commonRequestHeader1.setFunctionId("01");
        commonRequestHeader1.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");

    }

    @Test
    public void testResultStateConvertRequest(){
        try {
            response.setCacheExistingFlag("N");
            assertNotNull(quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void testExecute() throws Exception{
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        assertNotNull(quotesHaseMidfsService.execute(serviceRequestMapper));
    }

    @Test
    public void testStatusExecute() throws Exception{
        secQuotesRequest.setRequestType("10");
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        assertNotNull(quotesHaseMidfsService.execute(serviceRequestMapper));
    }

    @Test
    public void testStatus2Execute() throws Exception{
        secQuotesRequest.setRequestType("0");
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        assertNotNull(quotesHaseMidfsService.execute(serviceRequestMapper));
    }

    @Test
    public void testValidateServiceResponse() throws Exception{
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
        assertNotNull(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper));
    }

    @Test
    public void testConvertResponse() throws Exception{
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
        assertNotNull(quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper)));
    }
    @Test
    public void testLocalConvertResponse() throws Exception{
        commonRequestHeader.setLocale("zh_CN");
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
        assertNotNull(quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper)));
    }
    @Test
    public void testLocal2ConvertResponse() throws Exception{
        commonRequestHeader.setLocale("zh_HK");
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
        assertNotNull(quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper)));
    }
    @Test
    public void testStatusConvertResponse() throws Exception{
        secQuotesRequest.setRequestType("10");
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
        assertNotNull(quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper)));
    }

    @Test
    public void testStatus2ConvertResponse() throws Exception{
        secQuotesRequest.setRequestType("0");
        Map<String, String> serviceRequestMapper =
                (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
        Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
        assertNotNull(quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper)));
    }

    @Test
    public void testResponseConvertResponse() throws Exception{
        try {
            String httpResult2 = "{\"priceQuotes\": [{\"prodAltNumSegs\": [{\"prodCdeAltClassCde\": \"I\",\"prodAltNum\": \"GB0005405286\"},{\"prodCdeAltClassCde\": \"M\",\"prodAltNum\": \"0005\"},{\"prodCdeAltClassCde\": \"P\",\"prodAltNum\": \"HSBA.HK\"},{ \"prodCdeAltClassCde\": \"T\",\"prodAltNum\": \"0005.HK\"},{\"prodCdeAltClassCde\": \"W\",\"prodAltNum\": \"20001968\"}],\"bidAskQueues\":[{ \"bidPrice\": null,\"bidSize\": 30000,\"bidOrder\": null,\"askPrice\": null,\"askSize\": 30800,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null}],\"symbol\": \"0005\",\"currency\": \"HKD\",\"market\": \"HK\",\"exchangeCode\": \"HKG\",\"productType\": \"SEC\",\"productSubType\": \"SZEQ\",\"companyName\": \"hhhh HOLDINGS PLC\",\"isSuspended\": false,\"nominalPrice\": 65.1,\"nominalPriceType\": \"N\",\"dayLowPrice\": 65,\"dayHighPrice\": 65.55,\"peRatio\": 12.02,\"previousClosePrice\": 65.2,\"yearLowPrice\": 60.35,\"bidPrice\": 65.1,\"askPrice\": 65.15,\"asOfDateTime\": \"2019-05-21T07:51:22.000Z\",\"turnover\": 761308548,\"sharesOutstanding\": null,\"dividendYield\": 6.09,\"volume\": 11665082,\"changeAmount\": -0.1,\"changePercent\": -0.15,\"delay\": true,\"marketStatus\": \"normal\",\"maturityDate\": \"2016-07-06\",\"bidSize\": 30000,\"openPrice\": 65.1,\"askSize\": 30800,\"bidSpread\": 0.05,\"askSpread\": 0.05,\"spreadCode\": \"\",\"yearHighPrice\": 78.25,\"boardLotSize\": 400,\"marketCap\": 1317376,\"brokerAskQueue\": null,\"brokerBidQueue\": null,\"vcmEligible\": null,\"casEligible\": null,\"vcmStatus\": null,\"orderImbalanceDirection\": \"N\",\"orderImbalanceQuantity\": 1000,\"casLowerPrice\": null,\"casUpperPrice\": null,\"casReferencePrice\": \"\",\"vcmStartTime\": null,\"vcmEndTime\": null,\"vcmLowerLimitPrice\": null,\"vcmUpperLimitPrice\": null,\"vcmReferencePrice\": \"\",\"upperLimitPrice\": null,\"lowerLimitPrice\": null,\"casLowerLimitPrice\": null,\"casUpperLimitPrice\": null,\"underlyingStock\": \"\",\"auctionIndicator\": null,\"eps\": 5.416,\"iep\": null,\"iev\": null,\"dividend\": 3.9649,\"prevTradePrice\": null,\"riskAlert\": false,\"riskLvlCde\": \"5\",\"settlementPrice\": null,\"primaryLastActivity\": null,\"accumulatedVolume\": null,\"unscaledTurnover\": null,\"totalSharesOutstanding\": null,\"totalIssuedShares\": null,\"derivativeFlag\": \"1\",\"callPrice\": 68.88,\"strikePrice\": 91.88,\"strikeLower\": 90.88,\"strikeUpper\": 91.88,\"warrantType\": \"Plain Vanilla\",\"marketStatus\": \"[3] Continuous Trading\",\"daySecLowLimPrice\": null,\"daySecUpperLimPrice\": null,\"dayLowLimPrice\": null,\"dayUpperLimPrice\": null,\"limitReferencePrice\": null,\"priceCode\": null}],\"remainingQuota\": -1,\"totalQuota\": -1,\"messages\": null,\"error_code\": \"2\",\"responseCode\": \"01\"}";
            when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);
            Map<String, String> serviceRequestMapper =
                    (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
            Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
            quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testResponse2ConvertResponse() {
        try {
            String httpResult2 = "{\"priceQuotes\": [{\"prodAltNumSegs\": [{\"prodCdeAltClassCde\": \"I\",\"prodAltNum\": \"GB0005405286\"},{\"prodCdeAltClassCde\": \"M\",\"prodAltNum\": \"0005\"},{\"prodCdeAltClassCde\": \"P\",\"prodAltNum\": \"HSBA.HK\"},{ \"prodCdeAltClassCde\": \"T\",\"prodAltNum\": \"0005.HK\"},{\"prodCdeAltClassCde\": \"W\",\"prodAltNum\": \"20001968\"}],\"bidAskQueues\":[{ \"bidPrice\": null,\"bidSize\": 30000,\"bidOrder\": null,\"askPrice\": null,\"askSize\": 30800,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null}],\"symbol\": \"0005\",\"currency\": \"HKD\",\"market\": \"HK\",\"exchangeCode\": \"HKG\",\"productType\": \"SEC\",\"productSubType\": \"SZEQ\",\"companyName\": \"hhhh HOLDINGS PLC\",\"isSuspended\": false,\"nominalPrice\": 65.1,\"nominalPriceType\": \"N\",\"dayLowPrice\": 65,\"dayHighPrice\": 65.55,\"peRatio\": 12.02,\"previousClosePrice\": 65.2,\"yearLowPrice\": 60.35,\"bidPrice\": 65.1,\"askPrice\": 65.15,\"asOfDateTime\": \"2019-05-21T07:51:22.000Z\",\"turnover\": 761308548,\"sharesOutstanding\": null,\"dividendYield\": 6.09,\"volume\": 11665082,\"changeAmount\": -0.1,\"changePercent\": -0.15,\"delay\": true,\"marketStatus\": \"normal\",\"maturityDate\": \"2016-07-06\",\"bidSize\": 30000,\"openPrice\": 65.1,\"askSize\": 30800,\"bidSpread\": 0.05,\"askSpread\": 0.05,\"spreadCode\": \"\",\"yearHighPrice\": 78.25,\"boardLotSize\": 400,\"marketCap\": 1317376,\"brokerAskQueue\": null,\"brokerBidQueue\": null,\"vcmEligible\": null,\"casEligible\": null,\"vcmStatus\": null,\"orderImbalanceDirection\": \"N\",\"orderImbalanceQuantity\": 1000,\"casLowerPrice\": null,\"casUpperPrice\": null,\"casReferencePrice\": \"\",\"vcmStartTime\": null,\"vcmEndTime\": null,\"vcmLowerLimitPrice\": null,\"vcmUpperLimitPrice\": null,\"vcmReferencePrice\": \"\",\"upperLimitPrice\": null,\"lowerLimitPrice\": null,\"casLowerLimitPrice\": null,\"casUpperLimitPrice\": null,\"underlyingStock\": \"\",\"auctionIndicator\": null,\"eps\": 5.416,\"iep\": null,\"iev\": null,\"dividend\": 3.9649,\"prevTradePrice\": null,\"riskAlert\": false,\"riskLvlCde\": \"5\",\"settlementPrice\": null,\"primaryLastActivity\": null,\"accumulatedVolume\": null,\"unscaledTurnover\": null,\"totalSharesOutstanding\": null,\"totalIssuedShares\": null,\"derivativeFlag\": \"1\",\"callPrice\": 68.88,\"strikePrice\": 91.88,\"strikeLower\": 90.88,\"strikeUpper\": 91.88,\"warrantType\": \"Plain Vanilla\",\"marketStatus\": \"[3] Continuous Trading\",\"daySecLowLimPrice\": null,\"daySecUpperLimPrice\": null,\"dayLowLimPrice\": null,\"dayUpperLimPrice\": null,\"limitReferencePrice\": null,\"priceCode\": null}],\"remainingQuota\": -1,\"totalQuota\": -1,\"messages\": null,\"error_code\": \"2\",\"responseCode\": \"02\"}";
            when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);
            Map<String, String> serviceRequestMapper =
                    (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
            Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
            quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testResponse3ConvertResponse() throws Exception{
        try {
            String httpResult2 = "{\"priceQuotes\": [{\"prodAltNumSegs\": [{\"prodCdeAltClassCde\": \"I\",\"prodAltNum\": \"GB0005405286\"},{\"prodCdeAltClassCde\": \"M\",\"prodAltNum\": \"0005\"},{\"prodCdeAltClassCde\": \"P\",\"prodAltNum\": \"HSBA.HK\"},{ \"prodCdeAltClassCde\": \"T\",\"prodAltNum\": \"0005.HK\"},{\"prodCdeAltClassCde\": \"W\",\"prodAltNum\": \"20001968\"}],\"bidAskQueues\":[{ \"bidPrice\": null,\"bidSize\": 30000,\"bidOrder\": null,\"askPrice\": null,\"askSize\": 30800,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null},{ \"bidPrice\": null,\"bidSize\": null,\"bidOrder\": null,\"askPrice\": null,\"askSize\":null,\"askOrder\": null}],\"symbol\": \"0005\",\"currency\": \"HKD\",\"market\": \"HK\",\"exchangeCode\": \"HKG\",\"productType\": \"SEC\",\"productSubType\": \"SZEQ\",\"companyName\": \"hhhh HOLDINGS PLC\",\"isSuspended\": false,\"nominalPrice\": 65.1,\"nominalPriceType\": \"N\",\"dayLowPrice\": 65,\"dayHighPrice\": 65.55,\"peRatio\": 12.02,\"previousClosePrice\": 65.2,\"yearLowPrice\": 60.35,\"bidPrice\": 65.1,\"askPrice\": 65.15,\"asOfDateTime\": \"2019-05-21T07:51:22.000Z\",\"turnover\": 761308548,\"sharesOutstanding\": null,\"dividendYield\": 6.09,\"volume\": 11665082,\"changeAmount\": -0.1,\"changePercent\": -0.15,\"delay\": true,\"marketStatus\": \"normal\",\"maturityDate\": \"2016-07-06\",\"bidSize\": 30000,\"openPrice\": 65.1,\"askSize\": 30800,\"bidSpread\": 0.05,\"askSpread\": 0.05,\"spreadCode\": \"\",\"yearHighPrice\": 78.25,\"boardLotSize\": 400,\"marketCap\": 1317376,\"brokerAskQueue\": null,\"brokerBidQueue\": null,\"vcmEligible\": null,\"casEligible\": null,\"vcmStatus\": null,\"orderImbalanceDirection\": \"N\",\"orderImbalanceQuantity\": 1000,\"casLowerPrice\": null,\"casUpperPrice\": null,\"casReferencePrice\": \"\",\"vcmStartTime\": null,\"vcmEndTime\": null,\"vcmLowerLimitPrice\": null,\"vcmUpperLimitPrice\": null,\"vcmReferencePrice\": \"\",\"upperLimitPrice\": null,\"lowerLimitPrice\": null,\"casLowerLimitPrice\": null,\"casUpperLimitPrice\": null,\"underlyingStock\": \"\",\"auctionIndicator\": null,\"eps\": 5.416,\"iep\": null,\"iev\": null,\"dividend\": 3.9649,\"prevTradePrice\": null,\"riskAlert\": false,\"riskLvlCde\": \"5\",\"settlementPrice\": null,\"primaryLastActivity\": null,\"accumulatedVolume\": null,\"unscaledTurnover\": null,\"totalSharesOutstanding\": null,\"totalIssuedShares\": null,\"derivativeFlag\": \"1\",\"callPrice\": 68.88,\"strikePrice\": 91.88,\"strikeLower\": 90.88,\"strikeUpper\": 91.88,\"warrantType\": \"Plain Vanilla\",\"marketStatus\": \"[3] Continuous Trading\",\"daySecLowLimPrice\": null,\"daySecUpperLimPrice\": null,\"dayLowLimPrice\": null,\"dayUpperLimPrice\": null,\"limitReferencePrice\": null,\"priceCode\": null}],\"remainingQuota\": -1,\"totalQuota\": -1,\"messages\": null,\"error_code\": \"2\",\"responseCode\": \"03\"}";
            when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);
            Map<String, String> serviceRequestMapper =
                    (Map<String, String>)quotesHaseMidfsService.convertRequest(secQuotesRequest,commonRequestHeader);
            Map<String, String> serviceResponseMapper = (Map<String, String>)quotesHaseMidfsService.execute(serviceRequestMapper);
            quotesHaseMidfsService.convertResponse(quotesHaseMidfsService.validateServiceResponse(serviceResponseMapper));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testisWeekend() throws Exception{
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2024-08-02");
            assertFalse(quotesHaseMidfsService.isWeekend("Asia/Shanghai",date));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testisWeekend1() throws Exception{
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2024-08-03");
            assertTrue(quotesHaseMidfsService.isWeekend("Asia/Shanghai",date));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
