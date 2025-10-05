package com.hhhh.group.secwealth.mktdata.api.equity.chart.service;

import com.hhhh.group.secwealth.mktdata.api.equity.chart.request.ChartRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.ChartResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.chart.response.pre.Result;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
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


public class EtnetChartServiceTest {

    @InjectMocks
    private EtnetChartService etnetChartService = new EtnetChartService();

    @Mock
    private HttpClientHelper httpClientHelper;

    //be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c
    @Mock
    private EtnetProperties etnetProperties;

    private ChartRequest chartRequest;

    private CommonRequestHeader commonRequestHeader;



    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        String httpResult = "{\"err_code\":\"2\",\"result\":{\"name\":\"CKH HOLDINGS\",\"fields\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\",\"SMA10\",\"SMA20\",\"SMA50\"],\"data\":[[\"2021-07-20T16:08:00.000+0800\",57.25,57.3,57.05,57.15,977000,57.25,57.19,57.251],[\"2021-07-20T15:55:00.000+0800\",57.3,57.35,57.2,57.3,115000,57.25,57.1825,57.261],[\"2021-07-20T15:50:00.000+0800\",57.2,57.35,57.2,57.3,27500,57.24,57.1675,57.268],[\"2021-07-20T15:45:00.000+0800\",57.25,57.25,57.2,57.25,46500,57.235,57.1575,57.274],[\"2021-07-20T15:40:00.000+0800\",57.25,57.25,57.2,57.2,45500,57.225,57.155,57.283],[\"2021-07-20T15:35:00.000+0800\",57.35,57.35,57.2,57.2,153500,57.215,57.1475,57.293],[\"2021-07-20T15:30:00.000+0800\",57.3,57.3,57.25,57.3,49000,57.205,57.14,57.3],[\"2021-07-20T15:25:00.000+0800\",57.3,57.3,57.25,57.25,3000,57.185,57.1325,57.303],[\"2021-07-20T15:20:00.000+0800\",57.3,57.35,57.25,57.3,67500,57.17,57.1275,57.31],[\"2021-07-20T15:15:00.000+0800\",57.15,57.3,57.15,57.25,66500,57.155,57.1225,57.313],[\"2021-07-20T15:10:00.000+0800\",57.2,57.2,57.15,57.15,3000,57.13,57.12,57.32],[\"2021-07-20T15:05:00.000+0800\",57.25,57.35,57.2,57.2,80500,57.115,57.125,57.329],[\"2021-07-20T15:00:00.000+0800\",57.15,57.25,57.15,57.25,43000,57.095,57.1275,57.335],[\"2021-07-20T14:55:00.000+0800\",57.1,57.15,57.05,57.15,58500,57.08,57.1275,57.34],[\"2021-07-20T14:50:00.000+0800\",57.15,57.15,57.1,57.1,23000,57.085,57.135,57.347],[\"2021-07-20T14:45:00.000+0800\",57.1,57.1,57.1,57.1,31000,57.08,57.145,57.352],[\"2021-07-20T14:40:00.000+0800\",57.1,57.15,57.1,57.1,25500,57.075,57.1525,57.357],[\"2021-07-20T14:35:00.000+0800\",57.1,57.1,57.1,57.1,11500,57.08,57.1625,57.373],[\"2021-07-20T14:30:00.000+0800\",57.0,57.15,57.0,57.15,58000,57.085,57.1725,57.391],[\"2021-07-20T14:25:00.000+0800\",57.0,57.0,57.0,57.0,88500,57.09,57.1825,57.408],[\"2021-07-20T14:20:00.000+0800\",57.0,57.0,56.95,57.0,14000,57.11,57.1975,57.428],[\"2021-07-20T14:15:00.000+0800\",57.1,57.1,56.95,57.0,494500,57.135,57.2125,57.448],[\"2021-07-20T14:10:00.000+0800\",57.15,57.15,57.05,57.1,58500,57.16,57.2325,57.47],[\"2021-07-20T14:05:00.000+0800\",57.1,57.2,57.1,57.2,63000,57.175,57.2475,57.488],[\"2021-07-20T14:00:00.000+0800\",57.05,57.1,57.05,57.05,58500,57.185,57.26,57.505],[\"2021-07-20T13:55:00.000+0800\",57.1,57.15,57.05,57.05,84500,57.21,57.285,57.525],[\"2021-07-20T13:50:00.000+0800\",57.15,57.15,57.15,57.15,14000,57.23,57.305,57.543],[\"2021-07-20T13:45:00.000+0800\",57.15,57.15,57.15,57.15,24500,57.245,57.3175,57.559],[\"2021-07-20T13:40:00.000+0800\",57.2,57.2,57.15,57.2,6000,57.26,57.34,57.576],[\"2021-07-20T13:35:00.000+0800\",57.25,57.25,57.15,57.2,132000,57.275,57.3575,57.593],[\"2021-07-20T13:30:00.000+0800\",57.25,57.25,57.2,57.25,14000,57.285,57.3825,57.611],[\"2021-07-20T13:25:00.000+0800\",57.35,57.35,57.2,57.25,50500,57.29,57.4025,57.627],[\"2021-07-20T13:20:00.000+0800\",57.3,57.3,57.25,57.25,18000,57.305,57.4225,57.641],[\"2021-07-20T13:15:00.000+0800\",57.25,57.35,57.25,57.3,78500,57.32,57.44,57.656],[\"2021-07-20T13:10:00.000+0800\",57.25,57.3,57.25,57.3,11000,57.335,57.46,57.668],[\"2021-07-20T13:05:00.000+0800\",57.3,57.3,57.25,57.25,63000,57.36,57.48,57.681],[\"2021-07-20T12:00:00.000+0800\",57.25,57.3,57.25,57.3,13500,57.38,57.495,57.694],[\"2021-07-20T11:55:00.000+0800\",57.35,57.35,57.25,57.3,42000,57.39,57.5025,57.706],[\"2021-07-20T11:50:00.000+0800\",57.3,57.35,57.3,57.35,24500,57.42,57.5175,57.719],[\"2021-07-20T11:45:00.000+0800\",57.3,57.35,57.25,57.3,63000,57.44,57.5225,57.73],[\"2021-07-20T11:40:00.000+0800\",57.35,57.35,57.3,57.3,34500,57.48,57.5375,57.743],[\"2021-07-20T11:35:00.000+0800\",57.4,57.45,57.35,57.4,38500,57.515,57.5525,57.756],[\"2021-07-20T11:30:00.000+0800\",57.45,57.45,57.4,57.4,11500,57.54,57.5575,57.767],[\"2021-07-20T11:25:00.000+0800\",57.55,57.55,57.45,57.45,11500,57.56,57.5625,57.775],[\"2021-07-20T11:20:00.000+0800\",57.45,57.55,57.45,57.55,16000,57.585,57.565,57.783],[\"2021-07-20T11:15:00.000+0800\",57.4,57.5,57.4,57.45,16000,57.6,57.555,57.79],[\"2021-07-20T11:10:00.000+0800\",57.6,57.6,57.4,57.4,49000,57.61,57.55,57.8],[\"2021-07-20T11:05:00.000+0800\",57.6,57.6,57.6,57.6,10000,57.615,57.575,57.811],[\"2021-07-20T11:00:00.000+0800\",57.65,57.65,57.5,57.55,69500,57.615,57.595,57.818],[\"2021-07-20T10:55:00.000+0800\",57.65,57.7,57.65,57.7,7000,57.605,57.6175,57.826],[\"2021-07-20T10:50:00.000+0800\",57.6,57.65,57.6,57.65,62500,57.595,57.6325,57.831],[\"2021-07-20T10:45:00.000+0800\",57.6,57.65,57.5,57.65,50000,57.59,57.65,57.837],[\"2021-07-20T10:40:00.000+0800\",57.8,57.85,57.6,57.6,142500,57.575,57.6725,57.843],[\"2021-07-20T10:35:00.000+0800\",57.65,57.75,57.65,57.7,12500,57.565,57.6925,57.85],[\"2021-07-20T10:30:00.000+0800\",57.55,57.7,57.55,57.7,41000,57.545,57.71,57.855],[\"2021-07-20T10:25:00.000+0800\",57.45,57.55,57.45,57.55,15000,57.51,57.7275,57.861],[\"2021-07-20T10:20:00.000+0800\",57.6,57.6,57.45,57.45,32000,57.49,57.7475,57.87],[\"2021-07-20T10:15:00.000+0800\",57.5,57.65,57.5,57.6,46500,57.535,57.7725,57.88],[\"2021-07-20T10:10:00.000+0800\",57.6,57.6,57.45,57.45,25000,57.575,57.7925,57.886],[\"2021-07-20T10:05:00.000+0800\",57.6,57.65,57.6,57.6,49000,57.63,57.8225,57.896],[\"2021-07-20T10:00:00.000+0800\",57.5,57.6,57.5,57.6,59000,57.67,57.8475,57.902],[\"2021-07-20T09:55:00.000+0800\",57.5,57.7,57.5,57.5,64000,57.71,57.87,57.908],[\"2021-07-20T09:50:00.000+0800\",57.5,57.6,57.45,57.5,106000,57.77,57.8925,57.916],[\"2021-07-20T09:45:00.000+0800\",57.35,57.5,57.35,57.5,30500,57.82,57.9175,57.924],[\"2021-07-20T09:40:00.000+0800\",57.35,57.4,57.3,57.35,58000,57.875,57.9375,57.93],[\"2021-07-20T09:35:00.000+0800\",57.2,57.6,57.2,57.35,288500,57.945,57.9675,57.941]]}}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        when(etnetProperties.getEtnetTokenWithoutVerify()).thenReturn("be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c");
        chartRequest = new ChartRequest();
        chartRequest.setSymbol(new String[]{"00001"});
        chartRequest.setMarket("HK");
        chartRequest.setFilters(new String[]{"DATE","OPEN","HIGH","LOW","CLOSE","VOLUME","SMA=25,50,75"});
        chartRequest.setProductType("SEC");
        chartRequest.setDelay(true);
        chartRequest.setPeriod(0);
        chartRequest.setIntCnt(5);
        chartRequest.setIntType("MINUTE");

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
    }

    @Test
    public void testConvertSymbolsNullRequest() throws Exception {
        try {
            chartRequest.setSymbol(null);
            Object convertRequest = etnetChartService.convertRequest(chartRequest, commonRequestHeader);
            assertNotNull(convertRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertRequest() throws Exception {
        Map<String, List<NameValuePair>> convertRequest = (Map<String, List<NameValuePair>>) etnetChartService.convertRequest(chartRequest, commonRequestHeader);
        for (NameValuePair nameValuePair : convertRequest.get("00001")) {
            if (nameValuePair.getName().equals("indicators")) {
                assertEquals("SMA10,SMA20,SMA50", nameValuePair.getValue());
            }
        }
    }

    @Test
    public void testPIntNConvertRequest() throws Exception {
        try {
            chartRequest.setIntType("");
            Map<String, List<NameValuePair>> convertRequest = (Map<String, List<NameValuePair>>) etnetChartService.convertRequest(chartRequest, commonRequestHeader);
            assertNotNull(convertRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testExecute() throws Exception {
        Object convertRequest = etnetChartService.convertRequest(chartRequest, commonRequestHeader);
        Map<String,String> executeResult = (Map<String, String>) etnetChartService.execute(convertRequest);
        assertNotNull(executeResult);
    }

    @Test
    public void testIntTypeExecute() throws Exception {
        chartRequest.setIntType("DAILY");
        commonRequestHeader.setLocale("zh_CN");
        Object convertRequest = (Map<String, List<NameValuePair>>)etnetChartService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = etnetChartService.execute(convertRequest);
        assertNotNull(executeResult);
    }

    @Test
    public void testValidateServiceResponse() throws Exception {
        Object convertRequest = etnetChartService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = etnetChartService.execute(convertRequest);
        Object serviceResponse = etnetChartService.validateServiceResponse(executeResult);
        assertNotNull(serviceResponse);
    }

    @Test
    public void testValidateNullServiceResponse() {
        try {
            Object serviceResponse = etnetChartService.validateServiceResponse(null);
            assertNotNull(serviceResponse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertResponse() throws Exception {
        Object convertRequest = etnetChartService.convertRequest(chartRequest, commonRequestHeader);
        Object executeResult = etnetChartService.execute(convertRequest);
        ChartResponse chartResponse = etnetChartService.convertResponse(executeResult);
        for (Result result : chartResponse.getResult()){
            assertEquals("CKH HOLDINGS",result.getDisplayName());
        }
    }

    @Test
    public void testConvertNullResponse(){
        try{
            ChartResponse chartResponse = etnetChartService.convertResponse(null);
            assertNotNull(chartResponse);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
