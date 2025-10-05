package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncherTest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.IPORequest;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest"})
public class CurrentIPOServiceTest {

    @InjectMocks
    private CurrentIPOService currentIPOService;

    private IPORequest request;

    private CommonRequestHeader commonRequestHeader;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private EtnetProperties etnetProperties;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        request = new IPORequest();
        request.setMarket("HK");

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setFunctionId("01");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
        String httpResult = "{\"err_code\":\"2\",\"listedIPO\":[{\"symbol\":\"06669\",\"name\":\"ACOTEC-B\",\"listDate\":\"2021-08-24\",\"listPrice\":23.8,\"lastPrice\":17.92,\"accumulatePerChange\":-24.706,\"changeAmount\":0.16,\"changePercent\":0.901,\"currency\":\"HKD\",\"overSubscriptionRate\":\"654.51x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-26.05,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06609\",\"name\":\"HEARTCARE-B\",\"listDate\":\"2021-08-20\",\"listPrice\":171.0,\"lastPrice\":141.0,\"accumulatePerChange\":-17.544,\"changeAmount\":5.5,\"changePercent\":4.059,\"currency\":\"HKD\",\"overSubscriptionRate\":\"333.14x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-24.561,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02015\",\"name\":\"LI AUTO-W\",\"listDate\":\"2021-08-12\",\"listPrice\":118.0,\"lastPrice\":118.2,\"accumulatePerChange\":0.169,\"changeAmount\":-0.6,\"changePercent\":-0.505,\"currency\":\"HKD\",\"overSubscriptionRate\":\"5.5x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-0.847,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06611\",\"name\":\"SANXUN GROUP\",\"listDate\":\"2021-07-19\",\"listPrice\":4.75,\"lastPrice\":4.69,\"accumulatePerChange\":-1.263,\"changeAmount\":-0.01,\"changePercent\":-0.213,\"currency\":\"HKD\",\"overSubscriptionRate\":\"3.22x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02205\",\"name\":\"KANGQIAO SER\",\"listDate\":\"2021-07-16\",\"listPrice\":3.68,\"lastPrice\":3.62,\"accumulatePerChange\":-1.63,\"changeAmount\":0.0,\"changePercent\":0.0,\"currency\":\"HKD\",\"overSubscriptionRate\":\"23.72x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-1.087,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02207\",\"name\":\"RONSHINE SERV\",\"listDate\":\"2021-07-16\",\"listPrice\":4.88,\"lastPrice\":5.01,\"accumulatePerChange\":2.664,\"changeAmount\":0.03,\"changePercent\":0.602,\"currency\":\"HKD\",\"overSubscriptionRate\":\"8.12x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.41,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02175\",\"name\":\"CH GENERAL EDU\",\"listDate\":\"2021-07-16\",\"listPrice\":3.69,\"lastPrice\":4.52,\"accumulatePerChange\":22.493,\"changeAmount\":-0.09,\"changePercent\":-1.952,\"currency\":\"HKD\",\"overSubscriptionRate\":\"4.12x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"09960\",\"name\":\"KINDSTAR GLOBAL\",\"listDate\":\"2021-07-16\",\"listPrice\":9.78,\"lastPrice\":7.04,\"accumulatePerChange\":-28.016,\"changeAmount\":-0.02,\"changePercent\":-0.283,\"currency\":\"HKD\",\"overSubscriptionRate\":\"543.03x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-7.566,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06616\",\"name\":\"GLOBAL NEW MAT\",\"listDate\":\"2021-07-16\",\"listPrice\":3.25,\"lastPrice\":6.14,\"accumulatePerChange\":88.923,\"changeAmount\":0.0,\"changePercent\":0.0,\"currency\":\"HKD\",\"overSubscriptionRate\":\"10.79x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":13.846,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06909\",\"name\":\"BETTERLIFE HLDG\",\"listDate\":\"2021-07-15\",\"listPrice\":4.4,\"lastPrice\":7.66,\"accumulatePerChange\":74.091,\"changeAmount\":-0.05,\"changePercent\":-0.648,\"currency\":\"HKD\",\"overSubscriptionRate\":\"43.92x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":25.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"}]}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        when(etnetProperties.getEtnetTokenWithoutVerify()).thenReturn("be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c");
    }

    @Test
    public void testConvertRequest() throws Exception{
        List<NameValuePair> params = (List<NameValuePair>)currentIPOService.convertRequest(request,commonRequestHeader);
        assertNotNull(params);
    }

    @Test
    public void testLocalConvertRequest() throws Exception{
        commonRequestHeader.setLocale("zh_HK");
        assertNotNull(currentIPOService.convertRequest(request,commonRequestHeader));
    }

    @Test
    public void testLocal2ConvertRequest() throws Exception{
        commonRequestHeader.setLocale("zh_CN");
        assertNotNull(currentIPOService.convertRequest(request,commonRequestHeader));
    }

    @Test
    public void testExecute() throws Exception{
        List<NameValuePair> params = (List<NameValuePair>)currentIPOService.convertRequest(request,commonRequestHeader);
        assertNotNull(currentIPOService.execute(params));
    }

    @Test
    public void testValidateServiceResponse() throws Exception{
        String httpResult = "{\"listedIPO\":[{\"symbol\":\"06669\",\"name\":\"ACOTEC-B\",\"listDate\":\"2021-08-24\",\"listPrice\":23.8,\"lastPrice\":17.92,\"accumulatePerChange\":-24.706,\"changeAmount\":0.16,\"changePercent\":0.901,\"currency\":\"HKD\",\"overSubscriptionRate\":\"654.51x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-26.05,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06609\",\"name\":\"HEARTCARE-B\",\"listDate\":\"2021-08-20\",\"listPrice\":171.0,\"lastPrice\":141.0,\"accumulatePerChange\":-17.544,\"changeAmount\":5.5,\"changePercent\":4.059,\"currency\":\"HKD\",\"overSubscriptionRate\":\"333.14x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-24.561,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02015\",\"name\":\"LI AUTO-W\",\"listDate\":\"2021-08-12\",\"listPrice\":118.0,\"lastPrice\":118.2,\"accumulatePerChange\":0.169,\"changeAmount\":-0.6,\"changePercent\":-0.505,\"currency\":\"HKD\",\"overSubscriptionRate\":\"5.5x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-0.847,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06611\",\"name\":\"SANXUN GROUP\",\"listDate\":\"2021-07-19\",\"listPrice\":4.75,\"lastPrice\":4.69,\"accumulatePerChange\":-1.263,\"changeAmount\":-0.01,\"changePercent\":-0.213,\"currency\":\"HKD\",\"overSubscriptionRate\":\"3.22x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02205\",\"name\":\"KANGQIAO SER\",\"listDate\":\"2021-07-16\",\"listPrice\":3.68,\"lastPrice\":3.62,\"accumulatePerChange\":-1.63,\"changeAmount\":0.0,\"changePercent\":0.0,\"currency\":\"HKD\",\"overSubscriptionRate\":\"23.72x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-1.087,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02207\",\"name\":\"RONSHINE SERV\",\"listDate\":\"2021-07-16\",\"listPrice\":4.88,\"lastPrice\":5.01,\"accumulatePerChange\":2.664,\"changeAmount\":0.03,\"changePercent\":0.602,\"currency\":\"HKD\",\"overSubscriptionRate\":\"8.12x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.41,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02175\",\"name\":\"CH GENERAL EDU\",\"listDate\":\"2021-07-16\",\"listPrice\":3.69,\"lastPrice\":4.52,\"accumulatePerChange\":22.493,\"changeAmount\":-0.09,\"changePercent\":-1.952,\"currency\":\"HKD\",\"overSubscriptionRate\":\"4.12x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"09960\",\"name\":\"KINDSTAR GLOBAL\",\"listDate\":\"2021-07-16\",\"listPrice\":9.78,\"lastPrice\":7.04,\"accumulatePerChange\":-28.016,\"changeAmount\":-0.02,\"changePercent\":-0.283,\"currency\":\"HKD\",\"overSubscriptionRate\":\"543.03x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-7.566,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06616\",\"name\":\"GLOBAL NEW MAT\",\"listDate\":\"2021-07-16\",\"listPrice\":3.25,\"lastPrice\":6.14,\"accumulatePerChange\":88.923,\"changeAmount\":0.0,\"changePercent\":0.0,\"currency\":\"HKD\",\"overSubscriptionRate\":\"10.79x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":13.846,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06909\",\"name\":\"BETTERLIFE HLDG\",\"listDate\":\"2021-07-15\",\"listPrice\":4.4,\"lastPrice\":7.66,\"accumulatePerChange\":74.091,\"changeAmount\":-0.05,\"changePercent\":-0.648,\"currency\":\"HKD\",\"overSubscriptionRate\":\"43.92x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":25.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"}]}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        List<NameValuePair> params = (List<NameValuePair>)currentIPOService.convertRequest(request,commonRequestHeader);
        String response = (String) currentIPOService.execute(params);
        assertNotNull(currentIPOService.validateServiceResponse(response));
    }

    @Test
    public void testErrorValidateServiceResponse() throws Exception{
        try {
            List<NameValuePair> params = (List<NameValuePair>)currentIPOService.convertRequest(request,commonRequestHeader);
            String response = (String) currentIPOService.execute(params);
            assertNotNull(currentIPOService.validateServiceResponse(response));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testConvertResponse() throws Exception{
        String httpResult = "{\"listedIPO\":[{\"symbol\":\"06669\",\"name\":\"ACOTEC-B\",\"listDate\":\"2021-08-24\",\"listPrice\":23.8,\"lastPrice\":17.92,\"accumulatePerChange\":-24.706,\"changeAmount\":0.16,\"changePercent\":0.901,\"currency\":\"HKD\",\"overSubscriptionRate\":\"654.51x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-26.05,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06609\",\"name\":\"HEARTCARE-B\",\"listDate\":\"2021-08-20\",\"listPrice\":171.0,\"lastPrice\":141.0,\"accumulatePerChange\":-17.544,\"changeAmount\":5.5,\"changePercent\":4.059,\"currency\":\"HKD\",\"overSubscriptionRate\":\"333.14x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-24.561,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02015\",\"name\":\"LI AUTO-W\",\"listDate\":\"2021-08-12\",\"listPrice\":118.0,\"lastPrice\":118.2,\"accumulatePerChange\":0.169,\"changeAmount\":-0.6,\"changePercent\":-0.505,\"currency\":\"HKD\",\"overSubscriptionRate\":\"5.5x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-0.847,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06611\",\"name\":\"SANXUN GROUP\",\"listDate\":\"2021-07-19\",\"listPrice\":4.75,\"lastPrice\":4.69,\"accumulatePerChange\":-1.263,\"changeAmount\":-0.01,\"changePercent\":-0.213,\"currency\":\"HKD\",\"overSubscriptionRate\":\"3.22x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02205\",\"name\":\"KANGQIAO SER\",\"listDate\":\"2021-07-16\",\"listPrice\":3.68,\"lastPrice\":3.62,\"accumulatePerChange\":-1.63,\"changeAmount\":0.0,\"changePercent\":0.0,\"currency\":\"HKD\",\"overSubscriptionRate\":\"23.72x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-1.087,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02207\",\"name\":\"RONSHINE SERV\",\"listDate\":\"2021-07-16\",\"listPrice\":4.88,\"lastPrice\":5.01,\"accumulatePerChange\":2.664,\"changeAmount\":0.03,\"changePercent\":0.602,\"currency\":\"HKD\",\"overSubscriptionRate\":\"8.12x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.41,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"02175\",\"name\":\"CH GENERAL EDU\",\"listDate\":\"2021-07-16\",\"listPrice\":3.69,\"lastPrice\":4.52,\"accumulatePerChange\":22.493,\"changeAmount\":-0.09,\"changePercent\":-1.952,\"currency\":\"HKD\",\"overSubscriptionRate\":\"4.12x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":0.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"09960\",\"name\":\"KINDSTAR GLOBAL\",\"listDate\":\"2021-07-16\",\"listPrice\":9.78,\"lastPrice\":7.04,\"accumulatePerChange\":-28.016,\"changeAmount\":-0.02,\"changePercent\":-0.283,\"currency\":\"HKD\",\"overSubscriptionRate\":\"543.03x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":-7.566,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06616\",\"name\":\"GLOBAL NEW MAT\",\"listDate\":\"2021-07-16\",\"listPrice\":3.25,\"lastPrice\":6.14,\"accumulatePerChange\":88.923,\"changeAmount\":0.0,\"changePercent\":0.0,\"currency\":\"HKD\",\"overSubscriptionRate\":\"10.79x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":13.846,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"},{\"symbol\":\"06909\",\"name\":\"BETTERLIFE HLDG\",\"listDate\":\"2021-07-15\",\"listPrice\":4.4,\"lastPrice\":7.66,\"accumulatePerChange\":74.091,\"changeAmount\":-0.05,\"changePercent\":-0.648,\"currency\":\"HKD\",\"overSubscriptionRate\":\"43.92x\",\"ipoSponsor\":null,\"peRatio\":null,\"firstDayPerformance\":25.0,\"ipoStatus\":0,\"asOfDateTime\":\"2021-09-03T14:13:00.000+0800\"}]}";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        List<NameValuePair> params = (List<NameValuePair>)currentIPOService.convertRequest(request,commonRequestHeader);
        String response = (String) currentIPOService.execute(params);
        assertNotNull(currentIPOService.convertResponse(currentIPOService.validateServiceResponse(response)));
    }

}
