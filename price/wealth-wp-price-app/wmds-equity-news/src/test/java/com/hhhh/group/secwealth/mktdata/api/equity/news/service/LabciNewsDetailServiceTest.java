package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsDetailRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.NewsDetailResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.Envelop;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import lombok.Getter;
import lombok.Setter;
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest","test"})
public class LabciNewsDetailServiceTest {

    @InjectMocks
    private LabciNewsDetailService labciNewsDetailService;

    private CommonRequestHeader commonRequestHeader;

    private NewsDetailRequest newsDetailRequest;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private LabciAmhNewsCommonService labciAmhNewsCommonService;

    @Mock
    private LabciProtalTokenService labciProtalAmhTokenService;

    @Mock
    private LabciProtalProperties labciAmhUSProperties;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        newsDetailRequest = new NewsDetailRequest();
        newsDetailRequest.setId("326416");
        newsDetailRequest.setMarket("HK");
        newsDetailRequest.setSource("ETNet");
        newsDetailRequest.setTranslate(true);
        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg>Authentication Failed</errormsg></header><body><newsid>326416</newsid><newscontent>Bloomberg News reported that with a market cap of US$279 billion</newscontent><newslang>222</newslang><newstopic>TENCENT Becomes One of Top 10 Listed Companies Worldwide</newstopic><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></body></envelop>";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
    }

    @Test
    public void testConvertRequest() throws Exception{
        List<NameValuePair> nameValuePairs = (List<NameValuePair>)labciNewsDetailService.convertRequest(newsDetailRequest,commonRequestHeader);
        for (NameValuePair nameValuePair : nameValuePairs){
            assertNull(nameValuePair.getValue());
        }
    }

    @Test
    public void testExecute() throws Exception{
        List<NameValuePair> nameValuePairs = (List<NameValuePair>)labciNewsDetailService.convertRequest(newsDetailRequest,commonRequestHeader);
        assertNotNull(labciNewsDetailService.execute(nameValuePairs));
    }

    @Test
    public void testErrorValidateServiceResponse() throws Exception{
        try {
            String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>001</responsecode><errormsg>Authentication Failed</errormsg></header><body><newsid>326416</newsid><newscontent>Bloomberg News reported that with a market cap of US$279 billion</newscontent><newslang>222</newslang><newstopic>TENCENT Becomes One of Top 10 Listed Companies Worldwide</newstopic><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></body></envelop>";
            when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
            List<NameValuePair> nameValuePairs = (List<NameValuePair>)labciNewsDetailService.convertRequest(newsDetailRequest,commonRequestHeader);
            assertNotNull(labciNewsDetailService.validateServiceResponse(labciNewsDetailService.execute(nameValuePairs)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
