package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.PerformanceReturnResponse;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PerformanceReturnServiceExecutorTest {

    private static final String URL = "URL";
    private static final String DATA_CLASS = "DATA_CLASS";
    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 61;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private PerformanceReturnServiceExecutor underTest;

    @Nested
    class WhenIniting {
        @BeforeEach
        void setup() throws ClassNotFoundException, JAXBException {
            dataClassJC = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn.PerformanceReturnData"));
        }
    }

    @Nested
    class WhenExecuting {
        @Mock
        private PerformanceReturnRequest request;
        @Mock
        private SearchProduct searchProduct;

        @BeforeEach
        void setup() throws Exception{
            Field url = underTest.getClass().getDeclaredField("url");
            url.setAccessible(true);
            url.set(underTest,"https://apiuat.morningstar.com/v2/service/mf/gqthkiq8dnspf7cs");
            Mockito.when(request.getCurrency()).thenReturn("USD");
            Mockito.when(request.getIndicate()).thenReturn("index");
            Mockito.when(request.getProductKeys()).thenReturn(DataBuilder.buildProductKeyList());
            searchProduct = DataBuilder.buildSearchProduct();
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn.PerformanceReturnData")).createUnmarshaller();
            when(dataClassJC.createUnmarshaller()).thenReturn(un);
            String res = DataBuilder.buildRespStr();
            when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);
        }
        @Test
         void test_WhenExecuting() throws Exception {
            underTest.execute(request);
            Assert.assertTrue(true);
        }
    }

    @Nested
    class WhenSendingRequest {
        @Mock
        private PerformanceReturnRequest request;
        @Mock
        private SearchProduct searchProduct;

        @BeforeEach
        void setup() throws Exception{
            Field url = underTest.getClass().getDeclaredField("url");
            url.setAccessible(true);
            url.set(underTest,"https://apiuat.morningstar.com/v2/service/mf/gqthkiq8dnspf7cs");
            Mockito.when(request.getCurrency()).thenReturn("USD");
            Mockito.when(request.getIndicate()).thenReturn("symbol");
            Mockito.when(request.getProductKeys()).thenReturn(DataBuilder.buildProductKeyList());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn.PerformanceReturnData")).createUnmarshaller();
            when(dataClassJC.createUnmarshaller()).thenReturn(un);
            String res = DataBuilder.buildRespStr();
            searchProduct = DataBuilder.buildSearchProduct();
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(),any(),any(),any(),any(),any(),any())).thenReturn(DataBuilder.buildSearchProduct());
            when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);
        }

        @Test
         void test_WhenWhenSendingFundList() throws Exception{
            Object[] response = (Object[])underTest.sendRequest(request);
            Assert.assertNotNull(response);
        }

    }

    private static class DataBuilder {
        public static List<ProductKey> buildProductKeyList(){
            List<ProductKey> productKeyList = new ArrayList<>();
            ProductKey productKey = new ProductKey();
            productKey.setMarket("HK");
            productKey.setProdAltNum("U62509");
            productKey.setProdCdeAltClassCde("M");
            productKey.setProductType("UT");
            productKeyList.add(productKey);
            return productKeyList;
        }

        public static SearchProduct buildSearchProduct(){
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setExternalKey("0P0001BFG7");
            SearchableObject searchObject = new SearchableObject();
            searchObject.setSymbol("540002");
            searchProduct.setSearchObject(searchObject);
            return searchProduct;
        }

        public static String buildRespStr(){
            String res = "<response>\n" +
                    "<status>\n" +
                    "<code>0</code>\n" +
                    "<message>OK</message>\n" +
                    "</status>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0001BFG7\">\n" +
                    "<api _id=\"gqthkiq8dnspf7cs\">\n" +
                    "<DP-DayEndDate>2021-07-28</DP-DayEndDate>\n" +
                    "<DP-ReturnYTD>-0.69265</DP-ReturnYTD>\n" +
                    "<CYR-Year1>6.20334</CYR-Year1>\n" +
                    "<CYR-Year2>10.24543</CYR-Year2>\n" +
                    "<CYR-Year3>-1.29551</CYR-Year3>\n" +
                    "<CYR-Year4>6.03348</CYR-Year4>\n" +
                    "<CYR-Year5>3.68592</CYR-Year5>\n" +
                    "<CYR-Year6>1.59989</CYR-Year6>\n" +
                    "<CYR-Year7>7.21047</CYR-Year7>\n" +
                    "<CYR-Year8>-1.74373</CYR-Year8>\n" +
                    "<CYR-Year9>11.32900</CYR-Year9>\n" +
                    "</api>\n" +
                    "</data>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0001DK0T\">\n" +
                    "<api _id=\"gqthkiq8dnspf7cs\">\n" +
                    "<DP-DayEndDate>2021-07-28</DP-DayEndDate>\n" +
                    "<DP-ReturnYTD>15.66809</DP-ReturnYTD>\n" +
                    "<CYR-Year1>25.73598</CYR-Year1>\n" +
                    "<CYR-Year2>19.50245</CYR-Year2>\n" +
                    "<CYR-Year3>-25.69405</CYR-Year3>\n" +
                    "<CYR-Year4>38.39869</CYR-Year4>\n" +
                    "<CYR-Year5>6.21312</CYR-Year5>\n" +
                    "<CYR-Year6>8.36065</CYR-Year6>\n" +
                    "<CYR-Year7>-9.32963</CYR-Year7>\n" +
                    "<CYR-Year8>50.24155</CYR-Year8>\n" +
                    "<CYR-Year9>38.09427</CYR-Year9>\n" +
                    "<CYR-Year10>-25.73770</CYR-Year10>\n" +
                    "</api>\n" +
                    "</data>\n" +
                    "</response>";
            return res;
        }

    }
}