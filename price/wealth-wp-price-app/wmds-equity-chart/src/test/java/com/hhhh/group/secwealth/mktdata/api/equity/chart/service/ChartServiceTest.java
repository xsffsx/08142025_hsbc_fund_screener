package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service.TrisChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.helper.SymbolConverter;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.utils.ChartDateUtils;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest", "test"})
public class ChartServiceTest {

    @InjectMocks
    private ChartService chartService;

    @Mock
    private TrisProperties trisProps;

    @Mock
    private TrisTokenService trisTokenService;

    private ChartRequest chartRequest;

    private CommonRequestHeader commonRequestHeader;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private PredSrchService predSrchService;
    @Mock
    private ApplicationProperties appProps;



    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        String httpResult = "{\"result\":{\"name\":\"CKH HOLDINGS\",\"fields\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\",\"SMA10\",\"SMA20\",\"SMA50\"],\"data\":[[\"2021-07-20T16:08:00.000+0800\",57.25,57.3,57.05,57.15,977000,57.25,57.19,57.251],[\"2021-07-20T15:55:00.000+0800\",57.3,57.35,57.2,57.3,115000,57.25,57.1825,57.261],[\"2021-07-20T15:50:00.000+0800\",57.2,57.35,57.2,57.3,27500,57.24,57.1675,57.268],[\"2021-07-20T15:45:00.000+0800\",57.25,57.25,57.2,57.25,46500,57.235,57.1575,57.274],[\"2021-07-20T15:40:00.000+0800\",57.25,57.25,57.2,57.2,45500,57.225,57.155,57.283],[\"2021-07-20T15:35:00.000+0800\",57.35,57.35,57.2,57.2,153500,57.215,57.1475,57.293],[\"2021-07-20T15:30:00.000+0800\",57.3,57.3,57.25,57.3,49000,57.205,57.14,57.3],[\"2021-07-20T15:25:00.000+0800\",57.3,57.3,57.25,57.25,3000,57.185,57.1325,57.303],[\"2021-07-20T15:20:00.000+0800\",57.3,57.35,57.25,57.3,67500,57.17,57.1275,57.31],[\"2021-07-20T15:15:00.000+0800\",57.15,57.3,57.15,57.25,66500,57.155,57.1225,57.313],[\"2021-07-20T15:10:00.000+0800\",57.2,57.2,57.15,57.15,3000,57.13,57.12,57.32],[\"2021-07-20T15:05:00.000+0800\",57.25,57.35,57.2,57.2,80500,57.115,57.125,57.329],[\"2021-07-20T15:00:00.000+0800\",57.15,57.25,57.15,57.25,43000,57.095,57.1275,57.335],[\"2021-07-20T14:55:00.000+0800\",57.1,57.15,57.05,57.15,58500,57.08,57.1275,57.34],[\"2021-07-20T14:50:00.000+0800\",57.15,57.15,57.1,57.1,23000,57.085,57.135,57.347],[\"2021-07-20T14:45:00.000+0800\",57.1,57.1,57.1,57.1,31000,57.08,57.145,57.352],[\"2021-07-20T14:40:00.000+0800\",57.1,57.15,57.1,57.1,25500,57.075,57.1525,57.357],[\"2021-07-20T14:35:00.000+0800\",57.1,57.1,57.1,57.1,11500,57.08,57.1625,57.373],[\"2021-07-20T14:30:00.000+0800\",57.0,57.15,57.0,57.15,58000,57.085,57.1725,57.391],[\"2021-07-20T14:25:00.000+0800\",57.0,57.0,57.0,57.0,88500,57.09,57.1825,57.408],[\"2021-07-20T14:20:00.000+0800\",57.0,57.0,56.95,57.0,14000,57.11,57.1975,57.428],[\"2021-07-20T14:15:00.000+0800\",57.1,57.1,56.95,57.0,494500,57.135,57.2125,57.448],[\"2021-07-20T14:10:00.000+0800\",57.15,57.15,57.05,57.1,58500,57.16,57.2325,57.47],[\"2021-07-20T14:05:00.000+0800\",57.1,57.2,57.1,57.2,63000,57.175,57.2475,57.488],[\"2021-07-20T14:00:00.000+0800\",57.05,57.1,57.05,57.05,58500,57.185,57.26,57.505],[\"2021-07-20T13:55:00.000+0800\",57.1,57.15,57.05,57.05,84500,57.21,57.285,57.525],[\"2021-07-20T13:50:00.000+0800\",57.15,57.15,57.15,57.15,14000,57.23,57.305,57.543],[\"2021-07-20T13:45:00.000+0800\",57.15,57.15,57.15,57.15,24500,57.245,57.3175,57.559],[\"2021-07-20T13:40:00.000+0800\",57.2,57.2,57.15,57.2,6000,57.26,57.34,57.576],[\"2021-07-20T13:35:00.000+0800\",57.25,57.25,57.15,57.2,132000,57.275,57.3575,57.593],[\"2021-07-20T13:30:00.000+0800\",57.25,57.25,57.2,57.25,14000,57.285,57.3825,57.611],[\"2021-07-20T13:25:00.000+0800\",57.35,57.35,57.2,57.25,50500,57.29,57.4025,57.627],[\"2021-07-20T13:20:00.000+0800\",57.3,57.3,57.25,57.25,18000,57.305,57.4225,57.641],[\"2021-07-20T13:15:00.000+0800\",57.25,57.35,57.25,57.3,78500,57.32,57.44,57.656],[\"2021-07-20T13:10:00.000+0800\",57.25,57.3,57.25,57.3,11000,57.335,57.46,57.668],[\"2021-07-20T13:05:00.000+0800\",57.3,57.3,57.25,57.25,63000,57.36,57.48,57.681],[\"2021-07-20T12:00:00.000+0800\",57.25,57.3,57.25,57.3,13500,57.38,57.495,57.694],[\"2021-07-20T11:55:00.000+0800\",57.35,57.35,57.25,57.3,42000,57.39,57.5025,57.706],[\"2021-07-20T11:50:00.000+0800\",57.3,57.35,57.3,57.35,24500,57.42,57.5175,57.719],[\"2021-07-20T11:45:00.000+0800\",57.3,57.35,57.25,57.3,63000,57.44,57.5225,57.73],[\"2021-07-20T11:40:00.000+0800\",57.35,57.35,57.3,57.3,34500,57.48,57.5375,57.743],[\"2021-07-20T11:35:00.000+0800\",57.4,57.45,57.35,57.4,38500,57.515,57.5525,57.756],[\"2021-07-20T11:30:00.000+0800\",57.45,57.45,57.4,57.4,11500,57.54,57.5575,57.767],[\"2021-07-20T11:25:00.000+0800\",57.55,57.55,57.45,57.45,11500,57.56,57.5625,57.775],[\"2021-07-20T11:20:00.000+0800\",57.45,57.55,57.45,57.55,16000,57.585,57.565,57.783],[\"2021-07-20T11:15:00.000+0800\",57.4,57.5,57.4,57.45,16000,57.6,57.555,57.79],[\"2021-07-20T11:10:00.000+0800\",57.6,57.6,57.4,57.4,49000,57.61,57.55,57.8],[\"2021-07-20T11:05:00.000+0800\",57.6,57.6,57.6,57.6,10000,57.615,57.575,57.811],[\"2021-07-20T11:00:00.000+0800\",57.65,57.65,57.5,57.55,69500,57.615,57.595,57.818],[\"2021-07-20T10:55:00.000+0800\",57.65,57.7,57.65,57.7,7000,57.605,57.6175,57.826],[\"2021-07-20T10:50:00.000+0800\",57.6,57.65,57.6,57.65,62500,57.595,57.6325,57.831],[\"2021-07-20T10:45:00.000+0800\",57.6,57.65,57.5,57.65,50000,57.59,57.65,57.837],[\"2021-07-20T10:40:00.000+0800\",57.8,57.85,57.6,57.6,142500,57.575,57.6725,57.843],[\"2021-07-20T10:35:00.000+0800\",57.65,57.75,57.65,57.7,12500,57.565,57.6925,57.85],[\"2021-07-20T10:30:00.000+0800\",57.55,57.7,57.55,57.7,41000,57.545,57.71,57.855],[\"2021-07-20T10:25:00.000+0800\",57.45,57.55,57.45,57.55,15000,57.51,57.7275,57.861],[\"2021-07-20T10:20:00.000+0800\",57.6,57.6,57.45,57.45,32000,57.49,57.7475,57.87],[\"2021-07-20T10:15:00.000+0800\",57.5,57.65,57.5,57.6,46500,57.535,57.7725,57.88],[\"2021-07-20T10:10:00.000+0800\",57.6,57.6,57.45,57.45,25000,57.575,57.7925,57.886],[\"2021-07-20T10:05:00.000+0800\",57.6,57.65,57.6,57.6,49000,57.63,57.8225,57.896],[\"2021-07-20T10:00:00.000+0800\",57.5,57.6,57.5,57.6,59000,57.67,57.8475,57.902],[\"2021-07-20T09:55:00.000+0800\",57.5,57.7,57.5,57.5,64000,57.71,57.87,57.908],[\"2021-07-20T09:50:00.000+0800\",57.5,57.6,57.45,57.5,106000,57.77,57.8925,57.916],[\"2021-07-20T09:45:00.000+0800\",57.35,57.5,57.35,57.5,30500,57.82,57.9175,57.924],[\"2021-07-20T09:40:00.000+0800\",57.35,57.4,57.3,57.35,58000,57.875,57.9375,57.93],[\"2021-07-20T09:35:00.000+0800\",57.2,57.6,57.2,57.35,288500,57.945,57.9675,57.941]]}}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        chartRequest = new ChartRequest();
        chartRequest.setSymbol(new String[]{"00001"});
        chartRequest.setMarket("HK - Hong Kong");
        chartRequest.setFilters(new String[]{"DATE","OPEN","HIGH","LOW","CLOSE","VOLUME","SMA=25,50,75"});
        chartRequest.setProductType("SEC");
        chartRequest.setDelay(true);
        chartRequest.setPeriod(0);
        chartRequest.setIntCnt(5);
        chartRequest.setIntType("MINUTE");
        chartRequest.setStartTime("2017-01-01T00:00:00");
        chartRequest.setEndTime("2017-01-01T08:00:00");
        ProductKey productKey = new ProductKey();
        productKey.setProdCdeAltClassCde("M");
        productKey.setProductType("SEC");
        productKey.setProdAltNum("0005");
        List<ProductKey> productKeys = new ArrayList<>();
        productKeys.add(productKey);
        chartRequest.setProductKeys(productKeys);

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");

        PredSrchResponse predSrchResponse = new PredSrchResponse();
        predSrchResponse.setProductType("INDEX");
        predSrchResponse.setCountryTradableCode("CN");
        predSrchResponse.setProductName("沪深300指数");
        predSrchResponse.setProductShortName("CSI 300 Index");
        predSrchResponse.setSymbol("CSI300");
        predSrchResponse.setProductCode("2");
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
        when(predSrchService.precSrch(any(ArrayList.class), any(CommonRequestHeader.class))).thenReturn(predSrchResponses);
        when(predSrchService.localPredSrch(any(String.class))).thenReturn(predSrchResponse);
        when(appProps.getSiteTimezone(any(String.class))).thenReturn(Constant.DEFAULT_OPTION);
//        "timeZone":{ "gmtOffset":1, "shortName":"11111", "summerEnd":"2222","summerOffset":3,"summerStart":"3333" }
        String httpResult2 = "{\"stsCode\":\"000\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"displayName\":\"hhhh HOLDINGS\",\"timeZone\":{ \"gmtOffset\":1, \"shortName\":\"11111\", \"summerEnd\":\"2222\",\"summerOffset\":3,\"summerStart\":\"3333\" },\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);


    }

    @Test
    public void testService(){
        Assert.assertNotNull(ChartDateUtils.format(new Date(), Constant.DATE_FORMAT_FOT_LABCI));
    }

    @Test
    public void testConvertDelayRequest(){
        try {
            chartRequest.setDelay(false);
            Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
            assertNotNull(convertRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertRequest() throws Exception {
        Map<SymbolConverter, TrisChartRequest> convertRequest = (Map<SymbolConverter, TrisChartRequest>)chartService.convertRequest(chartRequest, commonRequestHeader);
        assertNotNull(convertRequest);
    }

    @Test
    public void testConvertProCodeYTDRequest() throws Exception {
        chartRequest.setPeriod(10);
        chartRequest.setMarket("HK");
        Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
        assertNotNull(convertRequest);
    }
    @Test
    public void testConvertProCodeMTDRequest() throws Exception {
        chartRequest.setPeriod(4);
        chartRequest.setIntType("MINUTE");
        Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
        assertNotNull(convertRequest);
    }

    @Test
    public void testConvertProCodeMRequest() throws Exception {
        chartRequest.setPeriod(5);
        chartRequest.setProductType("SEC");
        Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
        assertNotNull(convertRequest);
    }
    @Test
    public void testConvertProCodeYRequest() throws Exception {
        chartRequest.setPeriod(11);
        chartRequest.setDelay(true);
        Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
        assertNotNull(convertRequest);
    }
    @Test
    public void testConvertProCodeQTDRequest() throws Exception {
        chartRequest.setPeriod(9);
        ProductKey productKey = new ProductKey();
        productKey.setProdCdeAltClassCde("R");
        List<ProductKey> productKeys = new ArrayList<>();
        productKeys.add(productKey);
        chartRequest.setProductKeys(productKeys);
        Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
        assertNotNull(convertRequest);
    }

    @Test
    public void testExecute() throws Exception {
        Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
        Map<String, String> executeResult = (Map<String, String>)chartService.execute(convertRequest);
        assertNotNull(executeResult);
    }


    @Test
    public void testValidateServiceResponse(){
        try{
            Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
            Object executeResult = chartService.execute(convertRequest);
            Object serviceResponse = chartService.validateServiceResponse(executeResult);
           assertNotNull(serviceResponse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testValidateNullServiceResponse(){
        try{
            Object serviceResponse = chartService.validateServiceResponse(null);
            assertNotNull(serviceResponse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertResponse() throws Exception {
        Object convertRequest = chartService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = chartService.execute(convertRequest);
        ChartResponse chartResponse = chartService.convertResponse(executeResult);
        for (Result result : chartResponse.getResult()){
            assertEquals("hhhh HOLDINGS",result.getDisplayName());
        }
    }
}
