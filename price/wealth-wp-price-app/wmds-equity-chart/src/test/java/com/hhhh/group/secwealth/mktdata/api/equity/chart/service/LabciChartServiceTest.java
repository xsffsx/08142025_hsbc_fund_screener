package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import com.hhhh.group.secwealth.mktdata.api.equity.chart.authentication.LabciTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service.LabciChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class LabciChartServiceTest {

    @InjectMocks
    private LabciChartService labciChartService = new LabciChartService();

    @Mock
    private LabciProperties labciProperties;

    @Mock
    private PredSrchProperties predSrchProps;

    @Mock
    private PredSrchRequest predSrchRequest;

    @Mock
    private PredSrchService predSrchService;

    @Mock
    private LabciTokenService labciTokenService;

    @Mock
    private HttpClientHelper httpClientHelper;

    private ChartRequest chartRequest;

    private CommonRequestHeader commonRequestHeader;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(labciProperties.getLabciTokenTimezone(any(String.class))).thenReturn("GMT");

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
        when(predSrchService.precSrch(any(PredSrchRequest.class), any(CommonRequestHeader.class))).thenReturn(predSrchResponses);

        when(labciTokenService.encryptLabciToken(any(String.class),any(LabciToken.class))).thenReturn("SUHzmXgqCKojA2iH4k0W+Mt9eBw0a+TxYvTCneFePMzuRQDjTIvTLjia5hhRktYW");

        String httpResult = "[{\"prodAltNumSegs\":[{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"CSI300\"},{\"prodCdeAltClassCde\":\"T\",\"prodAltNum\":\".CSI300\"},{\"prodCdeAltClassCde\":\"W\",\"prodAltNum\":\"2\"}],\"productType\":\"INDEX\",\"productSubType\":null,\"countryTradableCode\":\"CN\",\"allowBuy\":null,\"allowSell\":null,\"productName\":\"沪深300指数\",\"productShortName\":\"CSI 300 Index\",\"productCcy\":null,\"market\":null,\"exchange\":null,\"fundHouseCde\":null,\"bondIssuer\":null,\"allowSellMipProdInd\":null,\"allowTradeProdInd\":null,\"prodTaxFreeWrapActStaCde\":null,\"allowSwInProdInd\":null,\"allowSwOutProdInd\":null,\"fundUnSwitchCode\":null,\"swithableGroups\":null,\"assetCountries\":null,\"assetSectors\":null,\"parentAssetClasses\":null,\"channelRestrictList\":null,\"chanlCdeList\":null,\"symbol\":\"CSI300\",\"productCode\":\"2\",\"riskLvlCde\":null,\"prodStatCde\":null,\"restrOnlScribInd\":null,\"piFundInd\":null,\"deAuthFundInd\":null}]";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);

        String httpResult2 = "{\"stsCode\":\"000\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);

        //predSrchProps = new PredSrchProperties();
        //predSrchProps.setUrl("https://aag.wealth-platform-hase.sit.aws.cloud.hhhh/wealth/api/v1/market-data/product/multi-predictive-search");
        //predSrchProps.setBodyPrefix("param=");
        //predSrchRequest.setAssetClasses(new String[]{"ALL"});
        //predSrchRequest.setSearchFields(new String[]{"symbol"});
        //predSrchRequest.setSortingFields(new String[]{"symbol"});
        //predSrchRequest.setPreciseSrch(false);

        when(predSrchProps.getUrl()).thenReturn("https://aag.wealth-platform-hase.sit.aws.cloud.hhhh/wealth/api/v1/market-data/product/multi-predictive-search");
        when(predSrchProps.getBodyPrefix()).thenReturn("param=");
        when(predSrchRequest.getAssetClasses()).thenReturn(new String[]{"ALL"});
        when(predSrchRequest.getSearchFields()).thenReturn(new String[]{"symbol"});
        when(predSrchRequest.isPreciseSrch()).thenReturn(false);
        when(predSrchProps.getRequestParams()).thenReturn(predSrchRequest);

        chartRequest = new ChartRequest();
        chartRequest.setSymbol(new String[]{"00001"});
        chartRequest.setMarket("HK");
        chartRequest.setFilters(new String[]{"DATE","OPEN","HIGH","LOW","CLOSE","VOLUME","SMA=25,50,75"});
        chartRequest.setProductType("SEC");
        chartRequest.setDelay(true);
        chartRequest.setPeriod(0);
        chartRequest.setIntCnt(5);
        chartRequest.setIntType("MINUTE");
        chartRequest.setStartTime("2017-01-01T00:00:00");
        chartRequest.setEndTime("2017-01-01T08:00:00");

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setFunctionId("00");
        commonRequestHeader.setJwtString("eyJ0eXAiOiJKV1MiLCJraWQiOiJFMkVfVFJVU1RfU0FNTF9DTUJfVUFUIiwiYWxnIjoiUlMyNTYiLCJzY2giOiJ1cm46YWltOnRva2VuOmludGVybmFsIiwic2N2IjoiMS4wIiwidGt2IjoiMS4wIn0");
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");

        //applicationContext.
    }

    @Test
    public void testConvertRequest() throws Exception {
        Map<String, LabciChartRequest> convertRequest = (Map<String, LabciChartRequest>)labciChartService.convertRequest(chartRequest, commonRequestHeader);
        assertNotNull(convertRequest);
//        assertEquals("2017-01-01T00:00:00",convertRequest.get("沪深300指数").getStartTm());
    }

    @Test
    public void testExecute() throws Exception {
        Object convertRequest = labciChartService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = labciChartService.execute(convertRequest);
        assertNotNull(executeResult);
    }

    @Test
    public void testValidateServiceResponse() throws Exception {
        Object convertRequest = labciChartService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = labciChartService.execute(convertRequest);
        Object serviceResponse = labciChartService.validateServiceResponse(executeResult);
        assertNotNull(serviceResponse);
    }

    @Test
    public void testConvertResponse() throws Exception {
        Object convertRequest = labciChartService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = labciChartService.execute(convertRequest);
        ChartResponse chartResponse = labciChartService.convertResponse(executeResult);
        for (Result result : chartResponse.getResult()){
            assertEquals("SEC",result.getProductType());
        }
    }
}
