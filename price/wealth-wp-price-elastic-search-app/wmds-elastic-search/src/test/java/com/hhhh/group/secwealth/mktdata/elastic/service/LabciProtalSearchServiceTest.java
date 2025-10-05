package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.Header;
import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.StocksList;
import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.SymbolSearchBoby;
import com.hhhh.group.secwealth.mktdata.elastic.bean.labci.SymbolSearchEnvelop;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LabciProtalSearchServiceTest {
    @Mock
    private HttpClientHelper httpClientHelper;
    @Mock
    private LabciProtalProperties labciProtalProperties;
    @Mock
    private LabciProtalService labciProtalService;
    @InjectMocks
    private LabciProtalSearchService underTest;

    public CommonRequestHeader setCommonRequestHeader(){
        CommonRequestHeader header = new CommonRequestHeader();
        header.setCountryCode("HK");
        header.setGroupMember("hhhh");
        header.setChannelId("OHI");
        header.setLocale("en");
        header.setAppCode("STB");
        header.setSaml3String("<saml:Assertion xmlns:saml=\"http://www.hhhh.com/saas/assertion\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ID=\"id_3b02c13c-6d0f-4306-b65c-60cee8f66d58\" IssueInstant=\"2020-01-14T09:29:42.886Z\" Version=\"3.0\"><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/><ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><ds:Reference URI=\"#id_3b02c13c-6d0f-4306-b65c-60cee8f66d58\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"><ds:InclusiveNamespaces xmlns:ds=\"http://www.w3.org/2001/10/xml-exc-c14n#\" PrefixList=\"#default saml ds xs xsi\"/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><ds:DigestValue>miH8Oii5TAV4HFy/CotCWSXEzDUoavZryV9rzKirdbk=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>TD29zb6exGWe+K2VVJDYPEn6Izb3/I9a1Q3ov4XtcGmRz0ZyBh/g8mSec3dIoiYA+dUOFWEX0etQ 9il8lFbp0EfbJrpVW+CAAe1KjQRfhIbzI2QRkdZBv49EyM5xl3EJ+5kzkmy65RvYivtPQfkDC1+W 1VTIKohZTasBQuCE80mmKhTAPYF/wnUwvJKuaoh/YGxkGDysYzjy/RJGohSE54z1yEoz3ZMeetEd 4Ou6FXbdkKbhaatknGDJIcMZue3CFQLOZcu1OZRgNJUbX8wDB2vktqE9zVApCvuREJ5bntr6Ec7E QfoKpqQ2ZEd2p2UEiN/LplTLZL1xHfLKrLwaaA==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>HK00456417588801</saml:NameID></saml:Subject><saml:Conditions NotBefore=\"2020-01-14T09:29:41.886Z\" NotOnOrAfter=\"2020-01-14T09:30:12.886Z\"/><saml:AttributeStatement><saml:Attribute Name=\"GUID\"><saml:AttributeValue>4061cb10-cdad-11dd-bfe4-000309040604</saml:AttributeValue></saml:Attribute><saml:Attribute Name=\"CAM\"><saml:AttributeValue>40</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");
        header.setCustomerId("HK06838990788801");
        return header;
    }

    public SymbolSearchEnvelop setEnvelop(){
        SymbolSearchEnvelop searchEnvelop = new SymbolSearchEnvelop();
        Header header = new Header();
        header.setMsgid("symbolsearch");
        header.setMarketid("US");
        header.setResponsecode("000");
        header.setErrormsg("");
        searchEnvelop.setHeader(header);
        SymbolSearchBoby symbolSearchBoby = new SymbolSearchBoby();
        symbolSearchBoby.setTotalrecordno("1");
        ArrayList<StocksList> list = new ArrayList<>();
        StocksList stocksList = new StocksList();
        stocksList.setExchange("NASDAQ");
        stocksList.setRiccode("AAPL.O");
        stocksList.setStockname("APPLE ORD");
        stocksList.setStocksymbol("AAPL");
        list.add(stocksList);
        symbolSearchBoby.setStockslist(list);
        searchEnvelop.setBody(symbolSearchBoby);
        return searchEnvelop;
    }

    @Nested
    class WhenPredsrching {
        @Mock
        private PredSrchRequest request;
        @Mock
        private CommonRequestHeader commonHeader;

        @Test
        void test_predsrch() throws Exception {
            PredSrchRequest predSrchRequest = new PredSrchRequest();
            predSrchRequest.setTopNum("1");
            predSrchRequest.setMarket("HK");
            predSrchRequest.setAssetClasses(new String[]{"SEC"});
            predSrchRequest.setKeyword("00005");
            request = predSrchRequest;
            commonHeader = setCommonRequestHeader();
            assertThrows(NullPointerException.class,() -> underTest.predsrch(request, commonHeader));
        }
    }

    @Nested
    class WhenMultiingPredsrch {
        @Mock
        private MultiPredSrchRequest request;
        @Mock
        private CommonRequestHeader header;

        @Test
        void test_multiingPredsrch(){
            MultiPredSrchRequest predSrchRequest = new MultiPredSrchRequest();
            predSrchRequest.setTopNum("1");
            predSrchRequest.setMarket("HK");
            predSrchRequest.setAssetClasses(new String[]{"SEC"});
            predSrchRequest.setKeyword(new String[]{"00005"});
            request = predSrchRequest;
            header = setCommonRequestHeader();
            assertThrows(NullPointerException.class,() -> underTest.multiPredsrch(request, header));
        }
    }
}
