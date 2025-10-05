package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service.LabciChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
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


public class LabciChartHaseUSServiceTest {

    @InjectMocks
    private LabciChartHaseUSService labciChartHaseUSService = new LabciChartHaseUSService();

    @Mock
    private LabciProtalProperties labciProtalProperties;

    @Mock
    private PredSrchProperties predSrchProps;

    @Mock
    private LabciProtalTokenService labciProtalTokenService;

    @Mock
    private PredSrchService predSrchService;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private PredSrchRequest predSrchRequest;

    private ChartRequest chartRequest;

    private CommonRequestHeader commonRequestHeader;


    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(labciProtalProperties.getLabciTokenTimezone(any(String.class))).thenReturn("HK_HASE");

        PredSrchResponse predSrchResponse = new PredSrchResponse();
        predSrchResponse.setProductType("SEC");
        predSrchResponse.setCountryTradableCode("US");
        predSrchResponse.setProductName("ARK INNOVATION ETF");
        predSrchResponse.setProductShortName("ARK INNOVATION ETF");
        predSrchResponse.setSymbol("ARKK");
        //predSrchResponse.setProductCode("2");
        predSrchResponse.setProductCcy("USD");
        predSrchResponse.setMarket("US");
        predSrchResponse.setExchange("NYSE Arca");
        List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
        ProdAltNumSeg prodAltNumSeg1 = new ProdAltNumSeg();
        prodAltNumSeg1.setProdAltNum("ARKK");
        prodAltNumSeg1.setProdCdeAltClassCde("M");
        ProdAltNumSeg prodAltNumSeg2 = new ProdAltNumSeg();
        prodAltNumSeg2.setProdAltNum("ARKK.NB");
        prodAltNumSeg2.setProdCdeAltClassCde("T");
        prodAltNumSegs.add(prodAltNumSeg1);
        prodAltNumSegs.add(prodAltNumSeg2);

        predSrchResponse.setProdAltNumSegs(prodAltNumSegs);

        ArrayList<PredSrchResponse> predSrchResponses = new ArrayList<>();
        predSrchResponses.add(predSrchResponse);
        when(predSrchService.precSrch(any(PredSrchRequest.class), any(CommonRequestHeader.class))).thenReturn(predSrchResponses);

        when(labciProtalTokenService.encryptLabciToken(any(String.class),any(LabciToken.class))).thenReturn("SUHzmXgqCKojA2iH4k0W+Mt9eBw0a+TxfbMX6nwKLWf0fDoVwc91BH78IcqRbj2D");
        //labciProtalTokenService.encryptLabciToken(site, token)

        String httpResult = "[{\"prodAltNumSegs\":[{\"prodCdeAltClassCde\":\"M\",\"prodAltNum\":\"ARKK\"},{\"prodCdeAltClassCde\":\"T\",\"prodAltNum\":\"ARKK.NB\"}],\"productType\":\"SEC\",\"productSubType\":null,\"countryTradableCode\":\"US\",\"allowBuy\":null,\"allowSell\":null,\"productName\":\"ARK INNOVATION ETF\",\"productShortName\":\"ARK INNOVATION ETF\",\"productCcy\":\"USD\",\"market\":\"US\",\"exchange\":\"NYSE Arca\",\"fundHouseCde\":null,\"bondIssuer\":null,\"allowSellMipProdInd\":null,\"allowTradeProdInd\":null,\"prodTaxFreeWrapActStaCde\":null,\"allowSwInProdInd\":null,\"allowSwOutProdInd\":null,\"fundUnSwitchCode\":null,\"swithableGroups\":null,\"assetCountries\":null,\"assetSectors\":null,\"parentAssetClasses\":null,\"channelRestrictList\":null,\"chanlCdeList\":null,\"symbol\":\"ARKK\",\"productCode\":null,\"riskLvlCde\":null,\"prodStatCde\":null,\"restrOnlScribInd\":null,\"piFundInd\":null,\"deAuthFundInd\":null}]";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);

        String httpResult2 = "{\"stsCode\":\"000\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\"ARKK.NB\",\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\",\"SMA(10,20,50).10\",\"SMA(10,20,50).20\",\"SMA(10,20,50).50\"],\"data\":[[\"2021-06-22T00:00:00.000Z\",119.0400,121.0500,118.8500,121.0200,6356747,116.9820,114.0160,114.3846],[\"2021-06-23T00:00:00.000Z\",121.5000,123.8800,121.2200,123.5300,7841349,118.0500,114.7235,114.3038],[\"2021-06-24T00:00:00.000Z\",124.9100,127.2200,124.5200,125.2800,9897568,119.0810,115.4160,114.3164],[\"2021-06-25T00:00:00.000Z\",125.9400,126.3300,124.0300,125.4900,7330180,120.0180,116.0765,114.2948],[\"2021-06-28T00:00:00.000Z\",128.7000,130.8300,128.5340,130.2300,10325594,121.2080,116.9830,114.4108],[\"2021-06-29T00:00:00.000Z\",130.0700,131.4990,129.0100,130.8800,7071339,122.7870,117.9645,114.6204],[\"2021-06-30T00:00:00.000Z\",130.5500,132.5000,129.0600,130.7800,9309809,124.4120,118.9385,114.8566],[\"2021-07-01T00:00:00.000Z\",130.7500,131.5500,127.2800,129.1600,7373177,125.4780,120.0250,115.0038],[\"2021-07-02T00:00:00.000Z\",129.8800,131.4300,127.8900,128.1500,5891693,126.4030,120.9570,115.1384],[\"2021-07-06T00:00:00.000Z\",128.1000,128.9760,126.4100,127.2400,7372568,127.1760,121.6845,115.2108],[\"2021-07-07T00:00:00.000Z\",128.0100,128.2800,123.1200,124.2600,9314231,127.5000,122.2410,115.1420],[\"2021-07-08T00:00:00.000Z\",120.3800,124.3400,119.3900,123.3100,9659898,127.4780,122.7640,115.0848],[\"2021-07-09T00:00:00.000Z\",123.6100,125.9000,122.0300,125.6200,6307448,127.5120,123.2965,115.0900],[\"2021-07-12T00:00:00.000Z\",126.2400,127.4000,124.0100,124.7400,4063559,127.4370,123.7275,115.1474],[\"2021-07-13T00:00:00.000Z\",124.4900,124.9500,122.2000,122.2700,6477401,126.6410,123.9245,115.1780],[\"2021-07-14T00:00:00.000Z\",123.3100,123.3100,118.0700,118.1700,10496994,125.3700,124.0785,115.2018],[\"2021-07-15T00:00:00.000Z\",118.0500,119.4100,114.4580,116.4000,12755645,123.9320,124.1720,115.2638],[\"2021-07-16T00:00:00.000Z\",117.8100,117.9400,115.4500,116.5300,7648180,122.6690,124.0735,115.3642],[\"2021-07-19T00:00:00.000Z\",113.9200,117.5800,113.2700,117.2800,11861771,121.5820,123.9925,115.5472],[\"2021-07-20T00:00:00.000Z\",117.5100,121.1750,116.4500,120.7600,6707699,120.9340,124.0550,115.7690],[\"2021-07-21T00:00:00.000Z\",120.8750,122.9100,119.9800,122.6600,5738169,120.7740,124.1370,116.1430],[\"2021-07-22T00:00:00.000Z\",122.5000,123.4200,121.2500,121.7200,4781538,120.6150,124.0465,116.4550]]}]}";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult2);

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
        commonRequestHeader.setJwtString("eyJ0eXAiOiJKV1MiLCJraWQiOiJFMkVfVFJVU1RfU0FNTF9DTUJfVUFUIiwiYWxnIjoiUlMyNTYiLCJzY2giOiJ1cm46YWltOnRva2VuOmludGVybmFsIiwic2N2IjoiMS4wIiwidGt2IjoiMS4wIn0.eyJzdWIiOiI0MzY0MDI3MyIsImdycCI6WyJDTj1JbmZvZGlyLVNwbHVua19XUEJfUHJvZF93bWhwX0JyZWFrZ2xhc3MsT1U9U1BMVU5LLE9VPUFwcGxpY2F0aW9ucyxPVT1Hcm91cHMsREM9SW5mb0RpcixEQz1Qcm9kLERDPUhTQkMiLCJDTj1JbmZvZGlyLVNwbHVua19XUEJfUHJvZF93bWhwX2RldmVsb3BlcixPVT1TUExVTkssT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItU3BsdW5rX1dQQl9Qcm9kX3dtaHBfc3RhbmRhcmQsT1U9U1BMVU5LLE9VPUFwcGxpY2F0aW9ucyxPVT1Hcm91cHMsREM9SW5mb0RpcixEQz1Qcm9kLERDPUhTQkMiLCJDTj1JbmZvZGlyLVNwbHVua19XUEJfRGV2X3dtaHBfZGV2ZWxvcGVyLE9VPVNQTFVOSyxPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1TcGx1bmtfV1BCX0Rldl93bWhwX3N0YW5kYXJkLE9VPVNQTFVOSyxPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb0Rpci1ab29tLVBvbGxpbmctR3JvdXAsT1U9QUFELE9VPUNsb3VkIFNlcnZpY2VzLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItc01BQy1QVCBQb3N0bWFuLE9VPXNNYWMgSkFNRixPVT1BQUQsT1U9Q2xvdWQgU2VydmljZXMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1qZW5raW5zLWdsd20tdXNlcnMsT1U9SmVua2lucyxPVT1EaWdpdGFsQ2lDZEVuZyxPVT1BQUQsT1U9Q2xvdWQgU2VydmljZXMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1EUy1TTEFDSy1Vc2VyLE9VPVNMQUNLLE9VPUFBRCxPVT1DbG91ZCBTZXJ2aWNlcyxPVT1Hcm91cHMsREM9SW5mb0RpcixEQz1Qcm9kLERDPUhTQkMiLCJDTj1JbmZvZGlyLURTLURJR0lUQUxDT05GTFVFTkNFLVVzZXIsT1U9SEJFVSxPVT1ESUdJVEFMQ09ORkxVRU5DRSxPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1BcHBEX1dlYWx0aF9leHBlcnQsT1U9QXBwRHluYW1pY3MsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItRFMtTkVYVVMzLVVzZXIsT1U9SEJFVSxPVT1OZXh1czMsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItQXBwRF9XZWFsdGhfdXNlcnMsT1U9QXBwRHluYW1pY3MsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItRFMtSVRTQUpJUkEtVXNlcixPVT1IQkVVLE9VPUlUU0FKSVJBLE9VPUFwcGxpY2F0aW9ucyxPVT1Hcm91cHMsREM9SW5mb0RpcixEQz1Qcm9kLERDPUhTQkMiLCJDTj1JbmZvRGlyLVhNQVRURVJTLVVzZXIsT1U9SEJFVSxPVT14TWF0dGVycyxPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1BTE1TTlRMQy1Vc2VyLE9VPUhCQVAsT1U9QUxNU05UTEMsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItRFMtRElHSVRBTEpJUkEtVXNlcixPVT1IQkVVLE9VPURJR0lUQUxKSVJBLE9VPUFwcGxpY2F0aW9ucyxPVT1Hcm91cHMsREM9SW5mb0RpcixEQz1Qcm9kLERDPUhTQkMiLCJDTj1JbmZvZGlyLURTLUFMTUNPTkZMVUVOQ0UtVXNlcixPVT1IQkVVLE9VPUFMTUNPTkZMVUVOQ0UsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItRFMtQUxNSklSQS1Vc2VyLE9VPUhCRVUsT1U9QUxNSklSQSxPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1EUy1BTE1HSVRIVUItVXNlcixPVT1IQkVVLE9VPUFMTUdJVEhVQixPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1BTE1KZW5raW5zLVVzZXIsT1U9QUxNSkVOS0lOUyxPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIiwiQ049SW5mb2Rpci1EUy1TT05BUi1Vc2VyLE9VPUhCRVUsT1U9U09OQVIsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItRzNSVEMtVXNlcnMsT1U9SEJFVSxPVT1SVEMsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItRFMtTkVYVVMtVXNlcixPVT1IQkVVLE9VPURTTmV4dXMsT1U9QXBwbGljYXRpb25zLE9VPUdyb3VwcyxEQz1JbmZvRGlyLERDPVByb2QsREM9SFNCQyIsIkNOPUluZm9kaXItUlRDLUphenpVc2VycyxPVT1IQkVVLE9VPVJUQyxPVT1BcHBsaWNhdGlvbnMsT1U9R3JvdXBzLERDPUluZm9EaXIsREM9UHJvZCxEQz1IU0JDIl0sImlzcyI6ImNtYmRzcC51ay5oc2JjLmNvbSIsImV4cCI6MTYyNzAyMjI4MSwiaWF0IjoxNjI3MDIyMjUxLCJqdGkiOiIzYTY5MmJhMi1jMzFhLTQwOWMtOTJiZS0zMTZhZjk2YTg3NjMiLCJzaXQiOiJhZDpzdmM6cHJpbmNpcGFsIn0.VKk8MyjqWHXfO9hEVJ1Lhyvsw5Q6V40lCBvmUwzcQ2ShI6LWd_SavBRWexETHiesYjx8Hx_mujiDIgQ3RbkZOYm8ta2BIleJaFDXEvefTg-5pXnKvEcxDLmrGutbTZGOknv3rxaMKIn2UeW2gZrFM1utsh4ViIp-Fhpvdf6JjQXYGy-c7qzN2NNWqoBHqHRSSx7_GzYxYMlUh3EHhuVPXjp2aPiHeoqmYUcdHQOJFbrVSeaxhhvrrgNZnfvSsN7KomXEFMja-EFemFjUccQllvDJKWtb0UNF0NzZuOtOudOSAk3OYDrAvKcLlSpZe1BstMObbLDuc4x0sbcZ2RY3ng");
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
    }

    @Test
    public void testConvertRequest() throws Exception {
        Map<String, LabciChartRequest> convertRequest = (Map<String, LabciChartRequest>)labciChartHaseUSService.convertRequest(chartRequest, commonRequestHeader);
        assertEquals("SUHzmXgqCKojA2iH4k0W+Mt9eBw0a+TxfbMX6nwKLWf0fDoVwc91BH78IcqRbj2D",convertRequest.get("ARK INNOVATION ETF").getToken());
    }

    @Test
    public void testConvertIndexRequest() throws Exception {
        chartRequest.setProductType("INDEX");
        Map<String, LabciChartRequest>  convertRequest = (Map<String, LabciChartRequest>)labciChartHaseUSService.convertRequest(chartRequest, commonRequestHeader);
        assertEquals("SUHzmXgqCKojA2iH4k0W+Mt9eBw0a+TxfbMX6nwKLWf0fDoVwc91BH78IcqRbj2D",convertRequest.get("00001").getToken());
    }

    @Test
    public void testExecute() throws Exception {
        Object convertRequest = labciChartHaseUSService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = (Map<String, String>)labciChartHaseUSService.execute(convertRequest);
        assertNotNull(executeResult);
    }

    @Test
    public void testValidateServiceResponse() throws Exception {
        Object convertRequest = labciChartHaseUSService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = labciChartHaseUSService.execute(convertRequest);
        Object serviceResponse = labciChartHaseUSService.validateServiceResponse(executeResult);
        assertNotNull(serviceResponse);
    }

    @Test
    public void testConvertResponse() throws Exception {
        Object convertRequest = labciChartHaseUSService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = labciChartHaseUSService.execute(convertRequest);
        ChartResponse chartResponse = labciChartHaseUSService.convertResponse(executeResult);
        for (Result result : chartResponse.getResult()){
            assertEquals("ARK INNOVATION ETF",result.getDisplayName());
        }
    }
}
