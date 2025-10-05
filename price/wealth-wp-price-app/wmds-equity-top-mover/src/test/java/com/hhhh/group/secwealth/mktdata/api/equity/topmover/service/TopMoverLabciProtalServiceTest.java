package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;


import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.TopMoverRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciTable;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest","test"})
public class TopMoverLabciProtalServiceTest {

    @InjectMocks
    private TopMoverLabciProtalService topMoverLabciProtalService;

    private TopMoverRequest topMoverRequest;

    private CommonRequestHeader commonRequestHeader;

    @Mock
    private LabciProtalTokenService labciProtalTokenService;

    @Mock
    private LabciProtalProperties labciProtalProperties;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        topMoverRequest = new TopMoverRequest();
        topMoverRequest.setMarket("HK - Hong Kong");
        topMoverRequest.setExchangeCode("HKG");
        topMoverRequest.setDelay(true);
        topMoverRequest.setProductType("SEC");
        topMoverRequest.setTopNum(3);
        topMoverRequest.setCountryCode("HK");
        topMoverRequest.setGroupMember("0005");
        topMoverRequest.setAppCode("STMA");
        topMoverRequest.setRequestType("0");
        topMoverRequest.setBoardType("MAIN");
        topMoverRequest.setMoverType("VOL");

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
        when(labciProtalTokenService.encryptLabciToken(any(String.class),any(LabciToken.class))).thenReturn("SUHzmXgqCKojA2iH4k0W+Mt9eBw0a+TxfbMX6nwKLWf0fDoVwc91BH78IcqRbj2D");
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>topmovers</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg>Authentication Failed</errormsg></header><body><boardtype>MAIN</boardtype><movertype>VOL</movertype><locale>en</locale><returnnumber>10</returnnumber><useragenttype></useragenttype><exchange>HKG</exchange><lastupdateddate>2021-08-12 09:00:00</lastupdateddate><lastupdatedtime>2021-08-12 12:02:06</lastupdatedtime>\n" +
                "<stocklist><stocksymbol>00005</stocksymbol><riccode>MDSEQTY40001</riccode><stockname>hase</stockname><last>1</last><change>HKG</change><changepct>SEC</changepct><open>2</open><previousclose>220</previousclose><turnover>on</turnover><volume>11</volume><score>10</score></stocklist><timezone>2021-08-12</timezone></body></envelop>\n";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult);
    }

    @Test
    public void testConvertRequest() throws Exception{
        Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciProtalService.convertRequest(topMoverRequest,commonRequestHeader);
        assertNotNull(serviceRequestMapper.get("VOL|MAIN"));
    }

    @Test
    public void testMTNullConvertRequest(){
        try {
            topMoverRequest.setMoverType(null);
            Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciProtalService.convertRequest(topMoverRequest,commonRequestHeader);
            assertNotNull(serviceRequestMapper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testEecute() throws Exception{
        Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciProtalService.convertRequest(topMoverRequest,commonRequestHeader);
        Map<String,Object> serviceResponseMapper = (Map<String,Object>)topMoverLabciProtalService.execute(serviceRequestMapper);
        assertNotNull(serviceResponseMapper);
    }

    @Test
    public void testValidateServiceResponse() throws Exception{
        try {
            Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciProtalService.convertRequest(topMoverRequest,commonRequestHeader);
            Map<String,Object> serviceResponseMapper = (Map<String,Object>)topMoverLabciProtalService.execute(serviceRequestMapper);
            Object validateServiceResponse = topMoverLabciProtalService.validateServiceResponse(serviceResponseMapper);
            assertNotNull(validateServiceResponse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testConvertQuotesList() throws Exception{
        Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciProtalService.convertRequest(topMoverRequest,commonRequestHeader);
        Map<String,Object> serviceResponseMapper = (Map<String,Object>)topMoverLabciProtalService.execute(serviceRequestMapper);
        Object validateServiceResponse = topMoverLabciProtalService.validateServiceResponse(serviceResponseMapper);
        TopMoverLabciResponse topMoverLabciResponse = topMoverLabciProtalService.convertResponse(validateServiceResponse);
        for (TopMoverLabciTable topMoverLabciTable : topMoverLabciResponse.getTopMovers()){
            assertEquals("VOL",topMoverLabciTable.getTableKey());
        }
    }
    @Test
    public void tesMoverTypetConvertQuotesList() throws Exception{
        topMoverRequest.setMoverType("LOSEPCT");
        Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciProtalService.convertRequest(topMoverRequest,commonRequestHeader);
        Map<String,Object> serviceResponseMapper = (Map<String,Object>)topMoverLabciProtalService.execute(serviceRequestMapper);
        Object validateServiceResponse = topMoverLabciProtalService.validateServiceResponse(serviceResponseMapper);
        TopMoverLabciResponse topMoverLabciResponse = topMoverLabciProtalService.convertResponse(validateServiceResponse);
        for (TopMoverLabciTable topMoverLabciTable : topMoverLabciResponse.getTopMovers()){
            assertEquals("LOSEPCT",topMoverLabciTable.getTableKey());
        }
    }
    @Test
    public void tesMoverType2tConvertQuotesList() throws Exception{
        topMoverRequest.setMoverType("GAINPCT");
        Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciProtalService.convertRequest(topMoverRequest,commonRequestHeader);
        Map<String,Object> serviceResponseMapper = (Map<String,Object>)topMoverLabciProtalService.execute(serviceRequestMapper);
        Object validateServiceResponse = topMoverLabciProtalService.validateServiceResponse(serviceResponseMapper);
        TopMoverLabciResponse topMoverLabciResponse = topMoverLabciProtalService.convertResponse(validateServiceResponse);
        for (TopMoverLabciTable topMoverLabciTable : topMoverLabciResponse.getTopMovers()){
            assertEquals("GAINPCT",topMoverLabciTable.getTableKey());
        }
    }
}
