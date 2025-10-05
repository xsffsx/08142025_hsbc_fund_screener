package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteholdings.QuoteHoldingsData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.TopTenHoldingsData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuoteHoldingsRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuickViewResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuoteHoldingsResponse;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteHoldingsServiceImplTest {


    private static final String URL = "URL";
    private static final String DATA_CLASS = "DATA_CLASS";
    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 50;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private QuoteHoldingsServiceImpl underTest;
    @Mock
    private SearchProduct searchProduct;

    @Mock
    ResponseData response;


    @Nested
    class WhenIniting {
        @Mock
        private QuoteHoldingsRequest request;
        @BeforeEach
        void setup() throws ClassNotFoundException, JAXBException {
            dataClassJC = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteholdings.QuoteHoldingsData"));

        }


    }

    @Nested
    class WhenExecuting {
        @Mock
        private QuoteHoldingsRequest request;

        @BeforeEach
        void setup() throws Exception {

            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(request.getMarket()).thenReturn("HK");
            Mockito.when(request.getProdAltNum()).thenReturn("U50001");
            Mockito.when(request.getProdCdeAltClassCde()).thenReturn("M");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            searchProduct= new SearchProduct();
            searchProduct.setExternalKey("0P0001EPQ8");
            SearchableObject searchableObject  = new SearchableObject();
            searchableObject.setProductSubType("UT");
            searchableObject.setMarket("HK");
            searchableObject.setProductType("SD");
            searchableObject.setProductSubType("DC");
            SearchableObject searchObject = new SearchableObject() ;
            searchObject.setProductSubType("DC");
            searchProduct.setSearchObject(searchObject);

            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class))).thenReturn(searchProduct);

            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteholdings.QuoteHoldingsData")).createUnmarshaller();
            Mockito.when(dataClassJC.createUnmarshaller()).thenReturn(un);
            String res = " <response>\n" +
                    "<status>\n" +
                    "<code>0</code>\n" +
                    "<message>OK</message>\n" +
                    "</status>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0001IUYN\">\n" +
                    "<api _id=\"j5a71w9eq8byhxr4\">\n" +
                    "<FNA-SurveyedFundNetAssets>413972266.38</FNA-SurveyedFundNetAssets>\n" +
                    "<FNA-NormalizedFundNetAssets>413972265</FNA-NormalizedFundNetAssets>\n" +
                    "<FNA-AsOfOriginalReportedCurrencyId>USD</FNA-AsOfOriginalReportedCurrencyId>\n" +
                    "<FNA-FundNetAssets>520363138</FNA-FundNetAssets>\n" +
                    "<PS-PriceToEarnings>0.03741</PS-PriceToEarnings>\n" +
                    "<PS-CategoryPriceToEarnings>0.04483</PS-CategoryPriceToEarnings>\n" +
                    "<PS-PriceToBook>0.26047</PS-PriceToBook>\n" +
                    "<PS-CategoryPriceToBook>0.42401</PS-CategoryPriceToBook>\n" +
                    "<PS-ROA>9.1192</PS-ROA>\n" +
                    "<PS-CategoryROA>6.68925</PS-CategoryROA>\n" +
                    "<PS-ROE>26.45633</PS-ROE>\n" +
                    "<PS-CategoryROE>18.05678</PS-CategoryROE>\n" +
                    "<YLD-Yield1Yr>3.92703</YLD-Yield1Yr>\n" +
                    "<PS-CategoryAverageCreditQuality>12</PS-CategoryAverageCreditQuality>\n" +
                    "<PS-CategoryYieldToMaturity>0.00004</PS-CategoryYieldToMaturity>\n" +
                    "<PSRP-PortfolioDate>2021-05-31</PSRP-PortfolioDate>\n" +
                    "</api>\n" +
                    "</data>\n" +
                    "</response>";

            response = new QuoteHoldingsData();
            Mockito.when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);


        }
        @Test
         void test_WhenExecuting() throws Exception {
            QuoteHoldingsResponse response =(QuoteHoldingsResponse) underTest.execute(request);
            Assert.assertNotNull(response.getHoldings());
        }
    }




}