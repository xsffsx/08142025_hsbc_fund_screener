package com.hhhh.group.secwealth.mktdata.api.equity.news.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.news.request.NewsHeadlinesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci.Envelop;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
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
public class LabciNewsHeadlinesServiceTest {

    @InjectMocks
    private LabciNewsHeadlinesService labciNewsHeadlinesService;

    private NewsHeadlinesRequest newsHeadlinesRequest;

    private CommonRequestHeader commonRequestHeader;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private PredSrchService predSrchService;

    @Mock
    private PredSrchProperties predSrchProps;

    @Mock
    private PredSrchRequest predSrchRequest;

    @Mock
    private LabciAmhNewsCommonService labciAmhNewsCommonService;

    @Mock
    private LabciProtalTokenService labciProtalTokenService;

    @Mock
    private LabciProtalProperties labciProtalProperties;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        newsHeadlinesRequest = new NewsHeadlinesRequest();
        newsHeadlinesRequest.setMarket("HK");
        newsHeadlinesRequest.setCategory("relatednews");
        newsHeadlinesRequest.setProductCodeIndicator("M");
        List<String> symbols = new ArrayList<>();
        symbols.add(0,"00005");
        symbols.add(1,"00006");
        symbols.add(2,"00007");
        newsHeadlinesRequest.setSymbol(symbols);
        newsHeadlinesRequest.setHeadline("hhhh Keeps Prime Rate Unchanged at 5%");
        newsHeadlinesRequest.setLocation("HongKong");
        newsHeadlinesRequest.setTranslate(true);
        //newsHeadlinesRequest.setPageId(2);

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("zh_CN");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg>Authentication Failed</errormsg></header><body><newsid>326416</newsid><newscontent>Bloomberg News reported that with a market cap of US$279 billion</newscontent><newslang>222</newslang><newstopic>TENCENT Becomes One of Top 10 Listed Companies Worldwide</newstopic><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></body></envelop>";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);

        when(httpClientHelper.doGet(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);

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
        ArgsHolder.putArgs("ifSkipCallLabciFlag",true);
        when(predSrchService.precSrch(any(PredSrchRequest.class), any(CommonRequestHeader.class))).thenReturn(predSrchResponses);
        when(predSrchProps.getRequestParams()).thenReturn(predSrchRequest);

        //
    }

    @Test
    public void testConvertRequest() throws Exception{
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
        when(predSrchService.precSrchForNews(any(PredSrchRequest.class), any(CommonRequestHeader.class))).thenReturn(predSrchResponses);
        assertNotNull(labciNewsHeadlinesService.convertRequest(newsHeadlinesRequest,commonRequestHeader));
    }

    @Test
    public void testExecute() throws Exception{
        List<NameValuePair> params = (List<NameValuePair>)labciNewsHeadlinesService.convertRequest(newsHeadlinesRequest,commonRequestHeader);
        assertNotNull(labciNewsHeadlinesService.execute(params));
    }

    @Test
    public void testValidateServiceResponse() throws Exception{
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>001</responsecode><errormsg>Authentication Failed</errormsg></header><body><newsid>326416</newsid><newscontent>Bloomberg News reported that with a market cap of US$279 billion</newscontent><newslang>222</newslang><newstopic>TENCENT Becomes One of Top 10 Listed Companies Worldwide</newstopic><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></body></envelop>";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        List<NameValuePair> params = (List<NameValuePair>)labciNewsHeadlinesService.convertRequest(newsHeadlinesRequest,commonRequestHeader);
        Envelop responseEnvelop = (Envelop)labciNewsHeadlinesService.validateServiceResponse(labciNewsHeadlinesService.execute(params));
        assertNull(responseEnvelop);
    }

    @Test
    public void testErrorValidateServiceResponse() throws Exception{
        try {
            String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>001</responsecode><errormsg>Authentication Failed</errormsg></header><body><newsid>326416</newsid><newscontent>Bloomberg News reported that with a market cap of US$279 billion</newscontent><newslang>222</newslang><newstopic>TENCENT Becomes One of Top 10 Listed Companies Worldwide</newstopic><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></body></envelop>";
            when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
            List<NameValuePair> params = (List<NameValuePair>)labciNewsHeadlinesService.convertRequest(newsHeadlinesRequest,commonRequestHeader);
            Envelop responseEnvelop = (Envelop)labciNewsHeadlinesService.validateServiceResponse(labciNewsHeadlinesService.execute(params));
            assertNull(responseEnvelop);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testConvertResponse() throws Exception{
        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envelop><header><msgid>error</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg>Authentication Failed</errormsg></header><body><newslist><newsid>326416</newsid><newscontent>Bloomberg News reported that with a market cap of US$279 billion</newscontent><newslang>222</newslang><newstopic>TENCENT Becomes One of Top 10 Listed Companies Worldwide</newstopic><lastupdateddate>2021-01-01</lastupdateddate><lastupdatedtime>00:00:00</lastupdatedtime><timezone>2021-08-23</timezone></newslist></body></envelop>";
        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
        List<NameValuePair> params = (List<NameValuePair>)labciNewsHeadlinesService.convertRequest(newsHeadlinesRequest,commonRequestHeader);
        Envelop responseEnvelop = (Envelop)labciNewsHeadlinesService.validateServiceResponse(labciNewsHeadlinesService.execute(params));
        assertNotNull(labciNewsHeadlinesService.convertResponse(responseEnvelop));
    }

//    @Test
//    public void testConvertResponse1() throws Exception{
//        String httpResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<envelop><header><msgid>newsheader</msgid><marketid>US</marketid><responsecode>000</responsecode><errormsg></errormsg></header><body><newslist><newsid>urn:newsml:reuters.com:20230803063139:rL1N39J091</newsid><newstopic>Nintendo sold 3.9 mln Switch units in Q1 </newstopic><newsdate>2023-08-03</newsdate><newstime>06:31:39</newstime><timezone>GMT</timezone><newslang>EN</newslang><translated>N</translated></newslist><newslist><newsid>urn:newsml:reuters.com:20230803063131:rFWN39K03I</newsid><newstopic>BRIEF-Sabre Insurance Says Half Year Results In Line With Expectations </newstopic><newsdate>2023-08-03</newsdate><newstime>06:31:31</newstime><timezone>GMT</timezone><newslang>EN</newslang><translated>N</translated></newslist><newslist><newsid>urn:newsml:reuters.com:20230803063103:rXB01E8O14</newsid><newstopic>TABLE-Gmo Reserch &lt;3695.T&gt;- 6-MTH group results</newstopic><newsdate>2023-08-03</newsdate><newstime>06:31:03</newstime><timezone>GMT</timezone><newslang>EN</newslang><translated>N</translated></newslist><newslist><newsid>urn:newsml:reuters.com:20230803063103:rXB1L1FY2A</newsid><newstopic>TABLE-GMO AD Partners &lt;4784.T&gt;- 6-MTH group results</newstopic><newsdate>2023-08-03</newsdate><newstime>06:31:03</newstime><timezone>GMT</timezone><newslang>EN</newslang><translated>N</translated></newslist><newslist><newsid>urn:newsml:reuters.com:20230803063103:rXB07K4JJA</newsid><newstopic>TABLE-Gmo Tech &lt;6026.T&gt;- 6-MTH group results</newstopic><newsdate>2023-08-03</newsdate><newstime>06:31:03</newstime><timezone>GMT</timezone><newslang>EN</newslang><translated>N</translated></newslist></body></envelop>\n";
//        when(httpClientHelper.doPost(any(String.class),any(String.class),any(ArrayList.class),any(HashMap.class))).thenReturn(httpResult);
//        List<NameValuePair> params = (List<NameValuePair>)labciNewsHeadlinesService.convertRequest(newsHeadlinesRequest,commonRequestHeader);
//        ArgsHolder.putArgs("ifSkipCallLabciFlag",false);
//        Envelop responseEnvelop = (Envelop)labciNewsHeadlinesService.validateServiceResponse(labciNewsHeadlinesService.execute(params));
//        assertNotNull(labciNewsHeadlinesService.convertResponse(responseEnvelop));
//    }

}
