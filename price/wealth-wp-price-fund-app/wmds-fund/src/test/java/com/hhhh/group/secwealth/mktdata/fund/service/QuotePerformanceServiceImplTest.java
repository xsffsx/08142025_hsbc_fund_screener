package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.convertor.ConvertorUtil;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuotePerformanceRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuotePerformanceResponse;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuotePerformanceServiceImplTest {


    private static final String CURRENCY_ALIGN_KEY = "CURRENCY_ALIGN_KEY";
    private static final String URL = "URL";
    private static final String DATA_CLASS = "DATA_CLASS";
    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 3;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private QuotePerformanceServiceImpl underTest;

    @Mock
    private SearchProduct searchProduct;

    @Nested
    class WhenGettingCurrencyAlignKey {
        @BeforeEach
        void setup() {
        }
    }

    @Nested
    class WhenIniting {
        @BeforeEach
        void setup() {
        }
    }

    @Nested
    class WhenExecuting {
        @Mock
        private QuotePerformanceRequest request;

        @BeforeEach
        void setup() throws Exception {
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quoteperformance.QuotePerformanceData")).createUnmarshaller();
            Mockito.when(dataClassJC.createUnmarshaller()).thenReturn(un);
            // Mockito.when(predSrchProps.getPredSrchBodyPrefix()).thenReturn("body=");
            //   Mockito.when(predSrchProps.getUrl()).thenReturn("https://mds-elastic-search-debug.wealth-platform-amh.dev.aws.cloud.hhhh/wealth/api/v1/market-data/product/predictive-search");


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

            //    Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(String.class),any(String.class),any(String.class),
            //   any(String.class),any(String.class),any(String.class),any(String.class))).thenReturn(searchProduct);

            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(request.getMarket()).thenReturn("HK");
            Mockito.when(request.getProdAltNum()).thenReturn("U50001");
            Mockito.when(request.getProdCdeAltClassCde()).thenReturn("M");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class))).thenReturn(searchProduct);


            String res = "<response>\n" +
                    "<status>\n" +
                    "<code>0</code>\n" +
                    "<message>OK</message>\n" +
                    "</status>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0001IUYN\">\n" +
                    "<api _id=\"cgwrv4xrtx5bcr67\">\n" +
                    "<TTR-Return1Mth>1.56637</TTR-Return1Mth>\n" +
                    "<TTR-Return3Mth>5.23922</TTR-Return3Mth>\n" +
                    "<TTR-Return6Mth>10.04689</TTR-Return6Mth>\n" +
                    "<TTR-Return1Yr>23.65378</TTR-Return1Yr>\n" +
                    "<TTRR-MonthEndDate>2021-06-30</TTRR-MonthEndDate>\n" +
                    "<RM-StdDev1Yr>14.433</RM-StdDev1Yr>\n" +
                    "<RM-SharpeRatio1Yr>2.217</RM-SharpeRatio1Yr>\n" +
                    "<RM-EndDate>2021-06-30</RM-EndDate>\n" +
                    "<TTRR-Rank1MthQuartileBreakpoint1>9.42249</TTRR-Rank1MthQuartileBreakpoint1>\n" +
                    "<TTRR-Rank1MthQuartileBreakpoint25>-0.94979</TTRR-Rank1MthQuartileBreakpoint25>\n" +
                    "<TTRR-Rank1MthQuartileBreakpoint50>-2.96727</TTRR-Rank1MthQuartileBreakpoint50>\n" +
                    "<TTRR-Rank1MthQuartileBreakpoint75>-5.48769</TTRR-Rank1MthQuartileBreakpoint75>\n" +
                    "<TTRR-Rank1MthQuartileBreakpoint99>-8.28082</TTRR-Rank1MthQuartileBreakpoint99>\n" +
                    "<TTRR-Rank3MthQuartileBreakpoint1>9.70009</TTRR-Rank3MthQuartileBreakpoint1>\n" +
                    "<TTRR-Rank3MthQuartileBreakpoint25>-2.55624</TTRR-Rank3MthQuartileBreakpoint25>\n" +
                    "<TTRR-Rank3MthQuartileBreakpoint50>-7.48164</TTRR-Rank3MthQuartileBreakpoint50>\n" +
                    "<TTRR-Rank3MthQuartileBreakpoint75>-10.97254</TTRR-Rank3MthQuartileBreakpoint75>\n" +
                    "<TTRR-Rank3MthQuartileBreakpoint99>-15.22840</TTRR-Rank3MthQuartileBreakpoint99>\n" +
                    "<TTRR-Rank6MthQuartileBreakpoint1>6.75141</TTRR-Rank6MthQuartileBreakpoint1>\n" +
                    "<TTRR-Rank6MthQuartileBreakpoint25>-6.20758</TTRR-Rank6MthQuartileBreakpoint25>\n" +
                    "<TTRR-Rank6MthQuartileBreakpoint50>-12.99603</TTRR-Rank6MthQuartileBreakpoint50>\n" +
                    "<TTRR-Rank6MthQuartileBreakpoint75>-16.45526</TTRR-Rank6MthQuartileBreakpoint75>\n" +
                    "<TTRR-Rank6MthQuartileBreakpoint99>-23.25118</TTRR-Rank6MthQuartileBreakpoint99>\n" +
                    "<TTRR-Rank1YrQuartileBreakpoint1>19.11626</TTRR-Rank1YrQuartileBreakpoint1>\n" +
                    "<TTRR-Rank1YrQuartileBreakpoint25>1.74627</TTRR-Rank1YrQuartileBreakpoint25>\n" +
                    "<TTRR-Rank1YrQuartileBreakpoint50>-3.68990</TTRR-Rank1YrQuartileBreakpoint50>\n" +
                    "<TTRR-Rank1YrQuartileBreakpoint75>-11.67083</TTRR-Rank1YrQuartileBreakpoint75>\n" +
                    "<TTRR-Rank1YrQuartileBreakpoint99>-17.91070</TTRR-Rank1YrQuartileBreakpoint99>\n" +
                    "<TTRR-Rank3YrQuartileBreakpoint1>43.73019</TTRR-Rank3YrQuartileBreakpoint1>\n" +
                    "<TTRR-Rank3YrQuartileBreakpoint25>13.70683</TTRR-Rank3YrQuartileBreakpoint25>\n" +
                    "<TTRR-Rank3YrQuartileBreakpoint50>3.13575</TTRR-Rank3YrQuartileBreakpoint50>\n" +
                    "<TTRR-Rank3YrQuartileBreakpoint75>-6.96812</TTRR-Rank3YrQuartileBreakpoint75>\n" +
                    "<TTRR-Rank3YrQuartileBreakpoint99>-35.26586</TTRR-Rank3YrQuartileBreakpoint99>\n" +
                    "<TTRR-Rank5YrQuartileBreakpoint1>160.23835</TTRR-Rank5YrQuartileBreakpoint1>\n" +
                    "<TTRR-Rank5YrQuartileBreakpoint25>38.08432</TTRR-Rank5YrQuartileBreakpoint25>\n" +
                    "<TTRR-Rank5YrQuartileBreakpoint50>14.80373</TTRR-Rank5YrQuartileBreakpoint50>\n" +
                    "<TTRR-Rank5YrQuartileBreakpoint75>-7.05875</TTRR-Rank5YrQuartileBreakpoint75>\n" +
                    "</api>\n" +
                    "</data>\n" +
                    "</response>";


            Mockito.when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            try (MockedStatic<ConvertorUtil> utilities1 = mockStatic(ConvertorUtil.class)) {
                utilities1.when(() -> ConvertorUtil.doConvert("qw","ew")).thenReturn("ewe");

                QuotePerformanceResponse respnse =(QuotePerformanceResponse) underTest.execute(request);
                Assert.assertNotNull(respnse.getPerformance());
            }




        }
    }

    @Nested
    class WhenSendingRequest {
        @Mock
        private QuotePerformanceRequest request;

        @BeforeEach
        void setup() {
        }
    }
}