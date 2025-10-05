package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MarketHourProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles({"test", "hase_stma_api_support-test", "uat_hase_stma_eqty-test"})
public class QuotesLabciServiceTest {

    @InjectMocks
    QuotesLabciService quotesLabciService;

    @Mock
    private HttpClientHelper httpClientHelper;

    @Mock
    private PredSrchService predSrchService;

    @Mock
    private ExResponseComponent exRespComponent;

    @Mock
    private MarketHourProperties marketHourProperties;

    @Mock
    private QuoteUserService quoteUserService;

    @Mock
    private QuoteAccessLogService quoteAccessLogService;

    @Mock
    private ASharesStockService aSharesStockService;

    @Mock
    private LabciProperties labciProps;

    @Mock
    private ApplicationProperties appProps;

    private final String MOCK_ETNET_RESPONSE = "{\"aSharesStock\":[{\"exchange\":\"SZSE\",\"aSymbol\":\"300837\",\"aNameeng\":\"ZHE KUANG HEAVY\",\"aNametc\":\"浙礦股份\",\"aNamesc\":\"浙矿股份\",\"stockConnectStatus\":\"N\",\"eligibility\":\"N\",\"hSymbol\":\"\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"},{\"exchange\":\"SZSE\",\"aSymbol\":\"300838\",\"aNameeng\":\"ZHEJIANG LINUO\",\"aNametc\":\"浙江力諾\",\"aNamesc\":\"浙江力诺\",\"stockConnectStatus\":\"N\",\"eligibility\":\"N\",\"hSymbol\":\"\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"},{\"exchange\":\"SZSE\",\"aSymbol\":\"300839\",\"aNameeng\":\"BOHUI CHEMICAL\",\"aNametc\":\"博匯股份\",\"aNamesc\":\"博汇股份\",\"stockConnectStatus\":\"N\",\"eligibility\":\"N\",\"hSymbol\":\"\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"},{\"exchange\":\"SZSE\",\"aSymbol\":\"000488\",\"aNameeng\":\"CHENMING PAPER\",\"aNametc\":\"晨鳴紙業\",\"aNamesc\":\"晨鸣纸业\",\"stockConnectStatus\":\"Y\",\"eligibility\":\"Y\",\"hSymbol\":\"01812\",\"asOfDateTime\":\"2021-10-07T15:43:00.000+0800\"}]}";

    private final String MOCK_ETNET_URL = "https://xxxxxx/HSAPI/GetData/ASharesStock";

    private final String MOCK_ETNET_TOKEN = "XXXXXXXXXXXXXXXXXX";

    private final String MOCK_PROXY_NAME = "XXXX";

    CommonRequestHeader commonRequestHeader;

    CacheDistributeResponse cacheDistributeResponse;

    PredSrchResponse predSrchResponse;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        commonRequestHeader = new CommonRequestHeader();
        commonRequestHeader.setCountryCode("HK");
        commonRequestHeader.setGroupMember("HASE");
        commonRequestHeader.setChannelId("OHI");
        commonRequestHeader.setLocale("en");
        commonRequestHeader.setAppCode("STMA");
        commonRequestHeader.setFunctionId("01");
        commonRequestHeader.setSaml3String("<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_f05dfba8-f54f-4bb2-8b09-8841eed507a9' IssueInstant='2018-09-13T06:34:32.938Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_f05dfba8-f54f-4bb2-8b09-8841eed507a9'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>MTorXOJZywI0rGxRejeh25NnHd597uZ6vjl1+zb4AY8=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>BUCrrR+kHl4WEXRQga7kMlNjFF4/u/qsTXtwetexIvE4TtMnnjSF6XlgmLvbwpqI2xz0bUmanNqVWJnIcaU2gzbHCgI/hypX4XtQTuEKYEn87ZvNwAaeZmpLb361GPeghZC7/TIQBHvRpbbe2MKR3Y3gk2R1KIYCEEdmf3MaxsB/35E8Yf2bqlHo3GKpClPebnNqa1eQmrmnzZKpOFgR4nv+bHRc/cB7GNwcaOdEUQwq3IyimVOfG0v+lWBQzaJ9sQmuhQKZoI/Dp2yGovYz9DFBQZt9Q1bCIKn9kPTtrQnqF3yd3mRTPjxK/ZawyzhVomQmbDuDwExGMf/fdFgd6g==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>80f19b7022b611e8bb8b006136</saml:NameID></saml:Subject><saml:Conditions NotBefore='2018-09-13T06:34:31.938Z' NotOnOrAfter='2018-09-13T06:35:32.938Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>80f19b70-22b6-11e8-bb8b-000006010306</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");

        cacheDistributeResponse = new CacheDistributeResponse();
        cacheDistributeResponse.setCacheExistingFlag("Y");
        cacheDistributeResponse.setKey("eID");
        cacheDistributeResponse.setValue("{\"eID\":\"001\",\"stsCode\":\"0\",\"stsTxt\":\"OK\",\"result\":[{\"item\":\".CSI300\",\"stsCode\":\"0\",\"displayName\":\"hhhh HOLDINGS\",\"child\":[{ \"_item\":\"001\", \"MNEMONIC\":\"CSI300\", \"DSPLY_NAME\":\"大喜屋集團\",\"TRDPRC_1\":8.95,\"NETCHNG_1\":11.88,\"PCTCHNG\":-0.83,\"CURRENCY\":\"HKD\",\"TRADE_DATE\":\"2020-03-23\",\"TRDTIM_1\":\"02:15:00.000Z\"}],\"timeZone\":{ \"gmtOffset\":1, \"shortName\":\"11111\", \"summerEnd\":\"2222\",\"summerOffset\":3,\"summerStart\":\"3333\" },\"field\":[\"DATE\",\"OPEN\",\"HIGH\",\"LOW\",\"CLOSE\",\"VOLUME\"],\"data\":[[\"2021-07-23T01:30:00.000Z\",5145.379,5145.379,5120.148,5124.342,16698],[\"2021-07-23T01:35:00.000Z\",5124.530,5128.960,5109.777,5110.258,12318],[\"2021-07-23T01:40:00.000Z\",5110.111,5115.073,5103.866,5107.004,9717],[\"2021-07-23T01:45:00.000Z\",5106.603,5108.750,5097.142,5108.750,8726],[\"2021-07-23T01:50:00.000Z\",5108.566,5114.193,5108.566,5111.264,7603],[\"2021-07-23T01:55:00.000Z\",5111.370,5112.570,5102.432,5108.410,6672],[\"2021-07-23T02:00:00.000Z\",5108.942,5109.706,5103.054,5109.706,6786],[\"2021-07-23T02:05:00.000Z\",5110.464,5116.648,5109.316,5109.316,5282],[\"2021-07-23T02:10:00.000Z\",5108.982,5108.982,5105.335,5106.516,5189],[\"2021-07-23T02:15:00.000Z\",5106.144,5106.531,5098.227,5098.301,4369],[\"2021-07-23T02:20:00.000Z\",5097.991,5099.235,5094.829,5098.616,4348],[\"2021-07-23T02:25:00.000Z\",5098.387,5098.404,5088.941,5089.400,4339],[\"2021-07-23T02:30:00.000Z\",5089.205,5100.817,5087.743,5099.676,5151],[\"2021-07-23T02:35:00.000Z\",5099.623,5101.889,5095.609,5095.609,4792],[\"2021-07-23T02:40:00.000Z\",5095.365,5096.498,5093.116,5096.498,4023],[\"2021-07-23T02:45:00.000Z\",5096.379,5097.732,5092.887,5097.732,3919],[\"2021-07-23T02:50:00.000Z\",5098.295,5109.343,5098.295,5104.478,2980],[\"2021-07-23T02:55:00.000Z\",5104.289,5107.907,5103.106,5105.517,3291],[\"2021-07-23T03:00:00.000Z\",5105.026,5120.397,5102.700,5120.397,4486],[\"2021-07-23T03:05:00.000Z\",5121.900,5122.886,5115.930,5116.283,5423]]}]}");
        CompletableFuture<CacheDistributeResponse> future = new CompletableFuture<>();
        future.complete(cacheDistributeResponse);
        CacheDistributeHolder.putCacheDistribute(future);

        predSrchResponse = new PredSrchResponse();
        predSrchResponse.setProductType("SEC");
        predSrchResponse.setProductSubType("SZEQ");
        predSrchResponse.setCountryTradableCode("CN");
        predSrchResponse.setProductName("万科A");
        predSrchResponse.setProductShortName("万科A");
        predSrchResponse.setSymbol("000002");
        predSrchResponse.setProductCode("280007927");
        predSrchResponse.setMarket("CN");
        predSrchResponse.setExchange("SZAS");
        predSrchResponse.setAllowSell("Y");
        predSrchResponse.setAllowBuy("Y");
        predSrchResponse.setProdStatCde("A");
        List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<>();
        ProdAltNumSeg prodAltNumSeg1 = new ProdAltNumSeg();
        prodAltNumSeg1.setProdAltNum("000002.ZK");
        prodAltNumSeg1.setProdCdeAltClassCde("C");
        ProdAltNumSeg prodAltNumSeg2 = new ProdAltNumSeg();
        prodAltNumSeg2.setProdAltNum("CNE0000000T2");
        prodAltNumSeg2.setProdCdeAltClassCde("I");
        ProdAltNumSeg prodAltNumSeg3 = new ProdAltNumSeg();
        prodAltNumSeg3.setProdAltNum("000002");
        prodAltNumSeg3.setProdCdeAltClassCde("M");
        ProdAltNumSeg prodAltNumSeg4 = new ProdAltNumSeg();
        prodAltNumSeg4.setProdAltNum("000002");
        prodAltNumSeg4.setProdCdeAltClassCde("P");
        ProdAltNumSeg prodAltNumSeg5 = new ProdAltNumSeg();
        prodAltNumSeg5.setProdAltNum("000002.SZ");
        prodAltNumSeg5.setProdCdeAltClassCde("T");
        ProdAltNumSeg prodAltNumSeg6 = new ProdAltNumSeg();
        prodAltNumSeg6.setProdAltNum("280007927");
        prodAltNumSeg6.setProdCdeAltClassCde("W");
        prodAltNumSegs.add(prodAltNumSeg1);
        prodAltNumSegs.add(prodAltNumSeg2);
        prodAltNumSegs.add(prodAltNumSeg3);
        prodAltNumSegs.add(prodAltNumSeg4);
        prodAltNumSegs.add(prodAltNumSeg5);
        prodAltNumSegs.add(prodAltNumSeg6);

        predSrchResponse.setProdAltNumSegs(prodAltNumSegs);
        ArrayList<PredSrchResponse> predSrchResponses = new ArrayList<>();
        predSrchResponses.add(predSrchResponse);
        when(predSrchService.precSrch(any(PredSrchRequest.class), any(CommonRequestHeader.class))).thenReturn(predSrchResponses);
        when(predSrchService.localPredSrch("000002.SZ", "SEC", "T")).thenReturn(predSrchResponse);

        doNothing().when(quoteAccessLogService).saveQuoteAccessLog(any(ArrayList.class));
        doNothing().when(aSharesStockService).setASharesInfo(any(QuotesLabciQuote.class));

        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_SITE, "HK_HASE");
        ArgsHolder.putArgs("QUOTE_LABCI_CUSTOMER_ID", "DKQ089QQS089FI29F");
        ArgsHolder.putArgs(QuotesLabciService.THREAD_INVISIBLE_DELAY, true);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_PRODUCT_KEY, Arrays.asList("000002.SZ"));
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, commonRequestHeader);
        ArgsHolder.putArgs("QUOTE_LABCI_ACCES_CMND_CDE", "QUOTE_LIST");

        String[] fieldKeys = {"HK_HASE", "SEC", "SZAS"};
        this.labciProps.getLabciFields(this.appProps.getQuotesResponseLabciFields(fieldKeys), fieldKeys);
    }

    @Test
    public void testConvertRequestWithSymbol() {
        SECQuotesRequest quotesRequest = new SECQuotesRequest();
        List<ProductKey> productKeyList = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProdAltNum("000002");
        productKey.setProductType("SEC");
        productKey.setProdCdeAltClassCde("M");
        productKeyList.add(productKey);
        quotesRequest.setDelay(true);
        quotesRequest.setMarket("CN");
        quotesRequest.setRequestType("0");
        quotesRequest.setProductKeys(productKeyList);
        //return
        String returnKey = "HK_HASE|SEC|null";
        List<NameValuePair> labciParams = new ArrayList<NameValuePair>();
        labciParams.add(new BasicNameValuePair("SymbolList", "000002.SZ;000002.SZd"));
        labciParams.add(new BasicNameValuePair("FieldList", "TRDPRC_1;ADJUST_CLS;OFF_CLOSE;CURRENCY;NETCHNG_1;OFF_CLOSE;ADJUST_CLS;PCTCHNG;OFF_CLOSE;ADJUST_CLS;TRADE_DATE;TRDTIM_1;BID;BIDSIZE;ASK;ASKSIZE;UPPER_SPRD;LOWER_SPRD;OPEN_PRC;ADJUST_CLS;HST_CLOSE;LOW_1;HIGH_1;YRLOW;LIFE_LOW;52WK_LOW;YRHIGH;LIFE_HIGH;52WK_HIGH;ACVOL_1;LOT_SIZE;LOT_SIZE_A;DSS_LOT_SIZE;GEN_VAL4;MKT_CAP;DSS_MKT_CAP;PERATIO;EARNINGS;TURNOVER;DIVIDEND;YIELD;RDN_EXCHID;RDN_EXCHD2;BEST_BID1;BEST_BSIZ1;NO_BIDMMKR;BEST_ASK1;BEST_ASIZ1;NO_ASKMMKR;BEST_BID2;BEST_BSIZ2;NO_BIDMKR2;BEST_ASK2;BEST_ASIZ2;NO_ASKMKR2;BEST_BID3;BEST_BSIZ3;NO_BIDMKR3;BEST_ASK3;BEST_ASIZ3;NO_ASKMKR3;BEST_BID4;BEST_BSIZ4;NO_BIDMKR4;BEST_ASK4;BEST_ASIZ4;NO_ASKMKR4;BEST_BID5;BEST_BSIZ5;NO_BIDMKR5;BEST_ASK5;BEST_ASIZ5;NO_ASKMKR5;DSPLY_NAME;OFF_CLOSE;PRC_QL_CD;DSPLY_NMLL;PRC_QL3;EXCH_ANN;DSS_OUT_SHARES;IEP_VOLUME;IEP_PRICE;UPLIMIT;LOLIMIT;FIN_STATUS;TRD_STATUS;TRDPRC_1;TRDPRC_2;TRDPRC_3;TRDPRC_4;TRDPRC_5;TRDVOL_1;TRDVOL_2;TRDVOL_3;TRDVOL_4;TRDVOL_5;SALTIM_MS;INST_PHASE"));
        labciParams.add(new BasicNameValuePair("DataRepresentation", "XML"));
        try {
            Map<String, String> serviceRequestMapper = (Map<String, String>) quotesLabciService.convertRequest(quotesRequest, commonRequestHeader);
            Assert.assertEquals(1, serviceRequestMapper.size());
//            Assert.assertEquals(returnKey, serviceRequestMapper.keySet().toArray()[0]);
//            Assert.assertEquals(URLEncodedUtils.format(labciParams, "UTF-8"), serviceRequestMapper.values().toArray()[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("test convertRequest with fail");
        }
    }

    @Test
    public void testExecuteWithSymbol() {
        Map<String, String> serviceRequestMapper = new HashMap<>();
        try {
            String returnKey = "HK_HASE|SEC|SZAS";
            String mockData = "SymbolList=000002.SZ%3B000002.SZd&FieldList=TRDPRC_1%3BADJUST_CLS%3BOFF_CLOSE%3BCURRENCY%3BNETCHNG_1%3BOFF_CLOSE%3BADJUST_CLS%3BPCTCHNG%3BOFF_CLOSE%3BADJUST_CLS%3BTRADE_DATE%3BTRDTIM_1%3BBID%3BBIDSIZE%3BASK%3BASKSIZE%3BUPPER_SPRD%3BLOWER_SPRD%3BOPEN_PRC%3BADJUST_CLS%3BHST_CLOSE%3BLOW_1%3BHIGH_1%3BYRLOW%3BLIFE_LOW%3B52WK_LOW%3BYRHIGH%3BLIFE_HIGH%3B52WK_HIGH%3BACVOL_1%3BLOT_SIZE%3BLOT_SIZE_A%3BDSS_LOT_SIZE%3BGEN_VAL4%3BMKT_CAP%3BDSS_MKT_CAP%3BPERATIO%3BEARNINGS%3BTURNOVER%3BDIVIDEND%3BYIELD%3BRDN_EXCHID%3BRDN_EXCHD2%3BBEST_BID1%3BBEST_BSIZ1%3BNO_BIDMMKR%3BBEST_ASK1%3BBEST_ASIZ1%3BNO_ASKMMKR%3BBEST_BID2%3BBEST_BSIZ2%3BNO_BIDMKR2%3BBEST_ASK2%3BBEST_ASIZ2%3BNO_ASKMKR2%3BBEST_BID3%3BBEST_BSIZ3%3BNO_BIDMKR3%3BBEST_ASK3%3BBEST_ASIZ3%3BNO_ASKMKR3%3BBEST_BID4%3BBEST_BSIZ4%3BNO_BIDMKR4%3BBEST_ASK4%3BBEST_ASIZ4%3BNO_ASKMKR4%3BBEST_BID5%3BBEST_BSIZ5%3BNO_BIDMKR5%3BBEST_ASK5%3BBEST_ASIZ5%3BNO_ASKMKR5%3BDSPLY_NAME%3BOFF_CLOSE%3BPRC_QL_CD%3BDSPLY_NMLL%3BPRC_QL3%3BEXCH_ANN%3BDSS_OUT_SHARES%3BIEP_VOLUME%3BIEP_PRICE%3BUPLIMIT%3BLOLIMIT%3BFIN_STATUS%3BTRD_STATUS%3BTRDPRC_1%3BTRDPRC_2%3BTRDPRC_3%3BTRDPRC_4%3BTRDPRC_5%3BTRDVOL_1%3BTRDVOL_2%3BTRDVOL_3%3BTRDVOL_4%3BTRDVOL_5%3BSALTIM_MS%3BINST_PHASE&DataRepresentation=XML";
            when(httpClientHelper.doGet(this.labciProps.getLabciUrl(), mockData, null)).thenReturn(mockData);

            serviceRequestMapper.put(returnKey, "SymbolList=000002.SZ%3B000002.SZd&FieldList=TRDPRC_1%3BADJUST_CLS%3BOFF_CLOSE%3BCURRENCY%3BNETCHNG_1%3BOFF_CLOSE%3BADJUST_CLS%3BPCTCHNG%3BOFF_CLOSE%3BADJUST_CLS%3BTRADE_DATE%3BTRDTIM_1%3BBID%3BBIDSIZE%3BASK%3BASKSIZE%3BUPPER_SPRD%3BLOWER_SPRD%3BOPEN_PRC%3BADJUST_CLS%3BHST_CLOSE%3BLOW_1%3BHIGH_1%3BYRLOW%3BLIFE_LOW%3B52WK_LOW%3BYRHIGH%3BLIFE_HIGH%3B52WK_HIGH%3BACVOL_1%3BLOT_SIZE%3BLOT_SIZE_A%3BDSS_LOT_SIZE%3BGEN_VAL4%3BMKT_CAP%3BDSS_MKT_CAP%3BPERATIO%3BEARNINGS%3BTURNOVER%3BDIVIDEND%3BYIELD%3BRDN_EXCHID%3BRDN_EXCHD2%3BBEST_BID1%3BBEST_BSIZ1%3BNO_BIDMMKR%3BBEST_ASK1%3BBEST_ASIZ1%3BNO_ASKMMKR%3BBEST_BID2%3BBEST_BSIZ2%3BNO_BIDMKR2%3BBEST_ASK2%3BBEST_ASIZ2%3BNO_ASKMKR2%3BBEST_BID3%3BBEST_BSIZ3%3BNO_BIDMKR3%3BBEST_ASK3%3BBEST_ASIZ3%3BNO_ASKMKR3%3BBEST_BID4%3BBEST_BSIZ4%3BNO_BIDMKR4%3BBEST_ASK4%3BBEST_ASIZ4%3BNO_ASKMKR4%3BBEST_BID5%3BBEST_BSIZ5%3BNO_BIDMKR5%3BBEST_ASK5%3BBEST_ASIZ5%3BNO_ASKMKR5%3BDSPLY_NAME%3BOFF_CLOSE%3BPRC_QL_CD%3BDSPLY_NMLL%3BPRC_QL3%3BEXCH_ANN%3BDSS_OUT_SHARES%3BIEP_VOLUME%3BIEP_PRICE%3BUPLIMIT%3BLOLIMIT%3BFIN_STATUS%3BTRD_STATUS%3BTRDPRC_1%3BTRDPRC_2%3BTRDPRC_3%3BTRDPRC_4%3BTRDPRC_5%3BTRDVOL_1%3BTRDVOL_2%3BTRDVOL_3%3BTRDVOL_4%3BTRDVOL_5%3BSALTIM_MS%3BINST_PHASE&DataRepresentation=XML");
            Map<String, String> serviceResponseMapper = (Map<String, String>) quotesLabciService.execute(serviceRequestMapper);
            Assert.assertEquals(1, serviceResponseMapper.size());
            Assert.assertEquals(returnKey, serviceResponseMapper.keySet().toArray()[0]);
            Assert.assertEquals(mockData, serviceResponseMapper.values().toArray()[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("test execute with fail");
        }
    }

}
