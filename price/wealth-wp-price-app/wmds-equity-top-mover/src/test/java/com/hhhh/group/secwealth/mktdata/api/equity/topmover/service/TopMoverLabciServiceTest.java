package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import com.hhhh.group.secwealth.mktdata.api.equity.ServerLauncher;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.bean.ChainMapping;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.TopMoverRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"sit_hase_stma_eqty_unittest","test"})
public class TopMoverLabciServiceTest {

    @InjectMocks
    private TopMoverLabciService topMoverLabciService;

    private TopMoverRequest topMoverRequest;

    private CommonRequestHeader commonRequestHeader;

    private CacheDistributeResponse response;

    @Mock
    private PredSrchService predSrchService;

    @Mock
    private QuoteAccessLogService quoteAccessLogService;

    @Mock
    private EtnetProperties etnetProperties;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private ChainMapping chainMapping;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(etnetProperties.getEtnetTokenWithoutVerify()).thenReturn("be984b45f46ba4f3cb43f009672a583b1605a621b33f4358967a1bf06539030c");
        topMoverRequest = new TopMoverRequest();
        topMoverRequest.setMarket("HK");
        topMoverRequest.setExchangeCode("SHAS");
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
        commonRequestHeader.setFunctionId("01");

        response = new CacheDistributeResponse();
        response.setCacheExistingFlag("Y");
        response.setKey("eID");
        response.setValue("{\"eID\":\"001\",\"stsCode\":\"0\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"stsCode\":\"0\",\"displayName\":\"hhhh HOLDINGS\",\"child\":[{ \"_item\":\"001\", \"MNEMONIC\":\"CSI300\", \"DSPLY_NAME\":\"大喜屋集團\",\"TRDPRC_1\":8.95,\"NETCHNG_1\":11.88,\"PCTCHNG\":-0.83,\"CURRENCY\":\"HKD\",\"TRADE_DATE\":\"2020-03-23\",\"TRDTIM_1\":\"02:15:00.000Z\"}],\"timeZone\":{ \"gmtOffset\":1, \"shortName\":\"11111\", \"summerEnd\":\"2222\",\"summerOffset\":3,\"summerStart\":\"3333\" },\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}");
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.complete(response);
        CacheDistributeHolder.putCacheDistribute(future);

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
        String httpResult3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><watchlist xml:space=\"preserve\"><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"BEST_ASIZ19\">0</fid><fid id=\"BEST_BSIZ15\">0</fid><fid id=\"BEST_ASIZ10\">0</fid><fid id=\"NO_BIDMKR4\">0</fid><fid id=\"DDS_DSO_ID\">4195</fid><fid id=\"PREF_LINK\"/><fid id=\"NO_BIDMKR9\">0</fid><fid id=\"BEST_BSIZ12\">0</fid><fid id=\"BEST_ASIZ5\">0</fid><fid id=\"BEST_BSIZ2\">0</fid><fid id=\"BEST_BID1\">0</fid><fid id=\"NO_BIDMKR3\">0</fid><fid id=\"BEST_BID8\">0</fid><fid id=\"ACTIV_DATE\">20 AUG 2021</fid><fid id=\"BEST_BID3\">0</fid><fid id=\"BEST_BID18\">0</fid><fid id=\"BEST_ASK17\">0</fid><fid id=\"LONGLINK7\" url=\"Watchlist?SymbolList=601919.SS\">601919.SS</fid><fid id=\"BEST_ASK3\">0</fid><fid id=\"BEST_BSIZ5\">0</fid><fid id=\"BEST_ASK9\">0</fid><fid id=\"BEST_BSIZ6\">0</fid><fid id=\"NO_ASKMKR2\">0</fid><fid id=\"BEST_ASK11\">0</fid><fid id=\"BEST_ASK16\">0</fid><fid id=\"NO_ASKMKR15\">0</fid><fid id=\"BEST_BSIZ16\">0</fid><fid id=\"LONGLINK8\" url=\"Watchlist?SymbolList=600050.SS\">600050.SS</fid><fid id=\"BEST_ASIZ16\">0</fid><fid id=\"LONGNEXTLR\"/><fid id=\"NO_BIDMKR15\">0</fid><fid id=\"BEST_ASK2\">0</fid><fid id=\"LONGPREVLR\"/><fid id=\"NO_BIDMKR12\">0</fid><fid id=\"NO_BIDMKR2\">0</fid><fid id=\"LONGLINK2\" url=\"Watchlist?SymbolList=600010.SS\">600010.SS</fid><fid id=\"NO_ASKMKR5\">0</fid><fid id=\"BEST_BID10\">0</fid><fid id=\"BEST_ASIZ20\">0</fid><fid id=\"BEST_ASIZ9\">0</fid><fid id=\"BEST_BSIZ20\">0</fid><fid id=\"NO_ASKMKR14\">0</fid><fid id=\"BEST_ASK19\">0</fid><fid id=\"BEST_BSIZ14\">0</fid><fid id=\"DSPLY_NAME\">TOP 10 VOLUME-A</fid><fid id=\"BEST_BSIZ4\">0</fid><fid id=\"BEST_ASK1\">0</fid><fid id=\"NO_BIDMKR20\">0</fid><fid id=\"BEST_ASIZ17\">0</fid><fid id=\"BEST_ASIZ15\">0</fid><fid id=\"NO_BIDMKR13\">0</fid><fid id=\"BEST_BSIZ17\">0</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"BEST_BID16\">0</fid><fid id=\"BEST_BSIZ1\">0</fid><fid id=\"BEST_ASK13\">0</fid><fid id=\"BEST_ASK8\">0</fid><fid id=\"LONGLINK1\" url=\"Watchlist?SymbolList=601728.SS\">601728.SS</fid><fid id=\"BEST_BSIZ18\">0</fid><fid id=\"BEST_BSIZ13\">0</fid><fid id=\"NM_IND\">EvalCt</fid><fid id=\"NUM_MOVES\">122</fid><fid id=\"SPS_SP_RIC\">.[SPSSSEVAE4</fid><fid id=\"BEST_BID17\">0</fid><fid id=\"BEST_ASIZ2\">0</fid><fid id=\"BEST_ASK18\">0</fid><fid id=\"NO_ASKMMKR\">0</fid><fid id=\"RECORDTYPE\">117</fid><fid id=\"NO_ASKMKR20\">0</fid><fid id=\"BEST_ASIZ7\">0</fid><fid id=\"BEST_BID15\">0</fid><fid id=\"BEST_ASIZ1\">0</fid><fid id=\"BEST_ASIZ14\">0</fid><fid id=\"NO_ASKMKR13\">0</fid><fid id=\"CONTEXT_ID\">3773</fid><fid id=\"NO_BIDMKR17\">0</fid><fid id=\"NO_ASKMKR9\">0</fid><fid id=\"TIMACT1\">03:30:36</fid><fid id=\"NO_BIDMKR14\">0</fid><fid id=\"BEST_ASK10\">0</fid><fid id=\"BEST_ASK7\">0</fid><fid id=\"NO_BIDMKR11\">0</fid><fid id=\"NO_BIDMKR5\">0</fid><fid id=\"NO_ASKMKR3\">0</fid><fid id=\"NO_BIDMKR6\">0</fid><fid id=\"PREF_DISP\">5337</fid><fid id=\"NO_ASKMKR12\">0</fid><fid id=\"NO_ASKMKR8\">0</fid><fid id=\"BEST_ASIZ8\">0</fid><fid id=\"BEST_ASK6\">0</fid><fid id=\"LONGLINK4\" url=\"Watchlist?SymbolList=601899.SS\">601899.SS</fid><fid id=\"NO_BIDMKR10\">0</fid><fid id=\"NO_BIDMKR16\">0</fid><fid id=\"BEST_BID14\">0</fid><fid id=\"BEST_BID20\">0</fid><fid id=\"NO_ASKMKR17\">0</fid><fid id=\"BEST_BID7\">0</fid><fid id=\"BEST_BSIZ19\">0</fid><fid id=\"BEST_ASIZ4\">0</fid><fid id=\"LONGLINK9\" url=\"Watchlist?SymbolList=600905.SS\">600905.SS</fid><fid id=\"BEST_BID19\">0</fid><fid id=\"NO_ASKMKR6\">0</fid><fid id=\"BEST_ASIZ13\">0</fid><fid id=\"BEST_ASK5\">0</fid><fid id=\"BEST_BID4\">0</fid><fid id=\"RDN_EXCHID\">SHH</fid><fid id=\"NO_BIDMKR7\">0</fid><fid id=\"BEST_BID13\">0</fid><fid id=\"BEST_ASK15\">0</fid><fid id=\"LONGLINK3\" url=\"Watchlist?SymbolList=600958.SS\">600958.SS</fid><fid id=\"BEST_ASK20\">0</fid><fid id=\"NO_ASKMKR16\">0</fid><fid id=\"PROD_PERM\">9153</fid><fid id=\"BEST_ASIZ12\">0</fid><fid id=\"BEST_ASK12\">0</fid><fid id=\"BEST_BSIZ11\">0</fid><fid id=\"BEST_ASK4\">0</fid><fid id=\"NO_BIDMKR19\">0</fid><fid id=\"BEST_BSIZ9\">0</fid><fid id=\"LONGLINK10\" url=\"Watchlist?SymbolList=603993.SS\">603993.SS</fid><fid id=\"NO_ASKMKR10\">0</fid><fid id=\"BEST_BSIZ10\">0</fid><fid id=\"NO_ASKMKR7\">0</fid><fid id=\"REF_COUNT\">10</fid><fid id=\"BEST_ASK14\">0</fid><fid id=\"NO_BIDMKR8\">0</fid><fid id=\"NO_BIDMMKR\">0</fid><fid id=\"BEST_BID6\">0</fid><fid id=\"NO_ASKMKR19\">0</fid><fid id=\"BEST_ASIZ3\">0</fid><fid id=\"PREV_DISP\">0</fid><fid id=\"RDNDISPLAY\">174</fid><fid id=\"LONGLINK5\" url=\"Watchlist?SymbolList=601618.SS\">601618.SS</fid><fid id=\"TIMACT\">03:30:36</fid><fid id=\"BEST_BID5\">0</fid><fid id=\"NO_BIDMKR18\">0</fid><fid id=\"BEST_BID12\">0</fid><fid id=\"NO_ASKMKR4\">0</fid><fid id=\"BEST_ASIZ6\">0</fid><fid id=\"BEST_BSIZ3\">0</fid><fid id=\"BEST_ASIZ18\">0</fid><fid id=\"NO_ASKMKR11\">0</fid><fid id=\"BEST_ASIZ11\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"BEST_BSIZ8\">0</fid><fid id=\"BEST_BSIZ7\">0</fid><fid id=\"LONGLINK6\" url=\"Watchlist?SymbolList=601377.SS\">601377.SS</fid><fid id=\"DSPLY_NMLL\">十大成交量-A股|十大成交量-A股</fid><fid id=\"BEST_BID9\">0</fid><fid id=\"BEST_BID11\">0</fid><fid id=\"BEST_BID2\">0</fid><fid id=\"NO_ASKMKR18\">0</fid><fid id=\"TRDTIM_1\"/><fid id=\"TURNOVER\"/><fid id=\"ACVOL_1\"/></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=601728.SS\">601728.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">CHINA TELECOM</fid><fid id=\"TRDPRC_1\">5.390</fid><fid id=\"ADJUST_CLS\">4.530</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">0.860</fid><fid id=\"PCTCHNG\">18.985</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">4.790</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">601728</fid><fid id=\"DSPLY_NMLL\">中國電信|中国电信</fid><fid id=\"MNEMONIC\">601728</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">0</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">122219287</fid><fid id=\"ACVOL_1\">2601135136</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=600010.SS\">600010.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">BAOTOU STEEL</fid><fid id=\"TRDPRC_1\">2.560</fid><fid id=\"ADJUST_CLS\">2.560</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">0.000</fid><fid id=\"PCTCHNG\">0.000</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">2.530</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">600010</fid><fid id=\"DSPLY_NMLL\">包鋼股份|包钢股份</fid><fid id=\"MNEMONIC\">600010</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">2.560</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">14548244</fid><fid id=\"ACVOL_1\">572046153</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=600958.SS\">600958.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">ORIENT SECURITY</fid><fid id=\"TRDPRC_1\">16.420</fid><fid id=\"ADJUST_CLS\">16.220</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">0.200</fid><fid id=\"PCTCHNG\">1.233</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">16.130</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">600958</fid><fid id=\"DSPLY_NMLL\">東方證券|东方证券</fid><fid id=\"MNEMONIC\">600958</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">16.220</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">45702145</fid><fid id=\"ACVOL_1\">280188735</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=601899.SS\">601899.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">ZIJIN MINING</fid><fid id=\"TRDPRC_1\">9.940</fid><fid id=\"ADJUST_CLS\">10.340</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">-0.400</fid><fid id=\"PCTCHNG\">-3.868</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">10.130</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">601899</fid><fid id=\"DSPLY_NMLL\">紫金礦業|紫金矿业</fid><fid id=\"MNEMONIC\">601899</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">10.340</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">27621910</fid><fid id=\"ACVOL_1\">277062977</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=601618.SS\">601618.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">METALLURGICAL CN</fid><fid id=\"TRDPRC_1\">3.660</fid><fid id=\"ADJUST_CLS\">3.860</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">-0.200</fid><fid id=\"PCTCHNG\">-5.181</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">3.790</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">601618</fid><fid id=\"DSPLY_NMLL\">中國中冶|中国中冶</fid><fid id=\"MNEMONIC\">601618</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">3.860</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">10226395</fid><fid id=\"ACVOL_1\">276712877</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=601377.SS\">601377.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">INDUSTRIAL SEC</fid><fid id=\"TRDPRC_1\">11.020</fid><fid id=\"ADJUST_CLS\">10.990</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">0.030</fid><fid id=\"PCTCHNG\">0.273</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">11.000</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">601377</fid><fid id=\"DSPLY_NMLL\">興業證券|兴业证券</fid><fid id=\"MNEMONIC\">601377</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">10.990</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">26986252</fid><fid id=\"ACVOL_1\">242206871</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=601919.SS\">601919.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">COSCO SHIPPING</fid><fid id=\"TRDPRC_1\">19.370</fid><fid id=\"ADJUST_CLS\">20.790</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">-1.420</fid><fid id=\"PCTCHNG\">-6.830</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">20.400</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">601919</fid><fid id=\"DSPLY_NMLL\">中遠海控|中远海控</fid><fid id=\"MNEMONIC\">601919</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">20.790</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">40913910</fid><fid id=\"ACVOL_1\">205559475</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=600050.SS\">600050.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">UNITED NETWORK</fid><fid id=\"TRDPRC_1\">4.200</fid><fid id=\"ADJUST_CLS\">4.330</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">-0.130</fid><fid id=\"PCTCHNG\">-3.002</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">4.290</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">600050</fid><fid id=\"DSPLY_NMLL\">中國聯通|中国联通</fid><fid id=\"MNEMONIC\">600050</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">4.330</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">8622038</fid><fid id=\"ACVOL_1\">205467522</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=600905.SS\">600905.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">THREE GORGES</fid><fid id=\"TRDPRC_1\">5.720</fid><fid id=\"ADJUST_CLS\">5.830</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">-0.110</fid><fid id=\"PCTCHNG\">-1.887</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">5.800</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">600905</fid><fid id=\"DSPLY_NMLL\">三峽能源|三峡能源</fid><fid id=\"MNEMONIC\">600905</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">5.830</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">11494918</fid><fid id=\"ACVOL_1\">200090549</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric><ric><fid id=\"REC_STATUS\">0</fid><fid id=\"SYMBOL\" url=\"Watchlist?SymbolList=603993.SS\">603993.SS</fid><fid id=\"SERVICE\">IDN_RDF</fid><fid id=\"PARENT\" url=\"Watchlist?SymbolList=.AVa.SS\">.AVa.SS</fid><fid id=\"DSPLY_NAME\">CHINA MOLYBDENUM</fid><fid id=\"TRDPRC_1\">6.640</fid><fid id=\"ADJUST_CLS\">6.740</fid><fid id=\"OFF_CLOSE\">0</fid><fid id=\"CURRENCY\">CNY</fid><fid id=\"NETCHNG_1\">-0.100</fid><fid id=\"PCTCHNG\">-1.484</fid><fid id=\"TRADE_DATE\">20 AUG 2021</fid><fid id=\"OPEN_PRC\">6.650</fid><fid id=\"RECORDTYPE\">113</fid><fid id=\"OFFCL_CODE\">603993</fid><fid id=\"DSPLY_NMLL\">洛陽鉬業|洛阳钼业</fid><fid id=\"MNEMONIC\">603993</fid><fid id=\"RDN_EXCHD2\">SHH</fid><fid id=\"HST_CLOSE\">6.740</fid><fid id=\"TRDTIM_1\">03:29</fid><fid id=\"TURNOVER\">13199763</fid><fid id=\"ACVOL_1\">198621541</fid><fid id=\"RDN_EXCHID\">SHH</fid></ric></watchlist>";
        when(httpClientHelper.doGet(any(String.class),any(String.class),any(HashMap.class))).thenReturn(httpResult3);
        doNothing().when(quoteAccessLogService).saveQuoteAccessLog(any(ArrayList.class));
    }

    @Test
    public void testFunIdConvertRequest(){
        try {
            commonRequestHeader.setFunctionId("1");
            Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciService.convertRequest(topMoverRequest,commonRequestHeader);
            assertNotNull(serviceRequestMapper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCusIdConvertRequest(){
        try {
            response.setValue("{\"eID\":\"\",\"stsCode\":\"0\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"stsCode\":\"0\",\"displayName\":\"hhhh HOLDINGS\",\"child\":[{ \"_item\":\"001\", \"MNEMONIC\":\"CSI300\", \"DSPLY_NAME\":\"大喜屋集團\",\"TRDPRC_1\":8.95,\"NETCHNG_1\":11.88,\"PCTCHNG\":-0.83,\"CURRENCY\":\"HKD\",\"TRADE_DATE\":\"2020-03-23\",\"TRDTIM_1\":\"02:15:00.000Z\"}],\"timeZone\":{ \"gmtOffset\":1, \"shortName\":\"11111\", \"summerEnd\":\"2222\",\"summerOffset\":3,\"summerStart\":\"3333\" },\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}");
            CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
            future.complete(response);
            CacheDistributeHolder.putCacheDistribute(future);
            Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciService.convertRequest(topMoverRequest,commonRequestHeader);
            assertNotNull(serviceRequestMapper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void testResultStateConvertRequest(){
        try {
            response.setCacheExistingFlag("N");
            Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciService.convertRequest(topMoverRequest,commonRequestHeader);
            assertNotNull(serviceRequestMapper);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testvValidateServiceResponse() throws Exception{
        try {
            Map<String,String> serviceRequestMapper =(Map<String,String>)topMoverLabciService.convertRequest(topMoverRequest,commonRequestHeader);
            Map<String, String> serviceResponseMapper =(Map<String, String>) topMoverLabciService.execute(serviceRequestMapper);
            Object response =  topMoverLabciService.validateServiceResponse(serviceResponseMapper);
            assertNotNull(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
