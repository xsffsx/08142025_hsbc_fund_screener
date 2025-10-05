package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.ReturnIndexChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sun.rmi.runtime.Log;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReturnIndexChartServiceImplTest {

    private static final String URL = "URL";
    private static final String DATA_CLASS = "DATA_CLASS";
    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 57;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private ReturnIndexChartServiceImpl underTest;

    @Nested
    class WhenIniting {
        @BeforeEach
        void setup() {
        }
    }

    @Nested
    class WhenExecuting {
        @Mock
        private ReturnIndexChartRequest request;

        @BeforeEach
        void setup() throws Exception{
            Field url = underTest.getClass().getDeclaredField("url");
            url.setAccessible(true);
            url.set(underTest,"https://api.morningstar.com/service/mf/MonthReturn");
            Mockito.when(request.getPeriod()).thenReturn("3M");
            Mockito.when(request.getTimeZone()).thenReturn("Asia/Hong_Kong");
            Mockito.when(request.getProdCdeAltClassCde()).thenReturn("M");
            Mockito.when(request.getCountryCode()).thenReturn("");
            Mockito.when(request.getGroupMember()).thenReturn("");
            Mockito.when(request.getProdAltNum()).thenReturn("U61041");
            Mockito.when(request.getMarket()).thenReturn("HK");
            Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(request.getLocale()).thenReturn("en");
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(DataBuilder.buildSearchProduct());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.returnIndexChart.ReturnIndexChartData")).createUnmarshaller();
            String res = DataBuilder.buildRes();
            when(dataClassJC.createUnmarshaller()).thenReturn(un);
            when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);

        }
        @Test
         void test_WhenExecuting() throws Exception {
            underTest.execute(request);
            Assertions.assertEquals("HK", request.getMarket());

        }
    }
    private static class DataBuilder {
        public static SearchProduct buildSearchProduct(){
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setExternalKey("0P0001BFG7");
            SearchableObject searchObject = new SearchableObject();
            searchObject.setSymbol("540002");
            searchProduct.setSearchObject(searchObject);
            return searchProduct;
        }

        public static String buildRes(){
            String res = "<response>\n" +
                    "<data idtype=\"mstarid\" id=\"0P00000B0R\">\n" +
                    "<CalendarReturn>\n" +
                    "<r y=\"2016\" m=\"6\" v=\"3.89060\"/>\n" +
                    "<r y=\"2016\" m=\"7\" v=\"5.33927\"/>\n" +
                    "<r y=\"2016\" m=\"8\" v=\"3.41429\"/>\n" +
                    "<r y=\"2016\" m=\"9\" v=\"0.64670\"/>\n" +
                    "<r y=\"2016\" m=\"10\" v=\"0.67636\"/>\n" +
                    "<r y=\"2016\" m=\"11\" v=\"-0.36950\"/>\n" +
                    "<r y=\"2016\" m=\"12\" v=\"2.89953\"/>\n" +
                    "<r y=\"2017\" m=\"1\" v=\"6.06160\"/>\n" +
                    "<r y=\"2017\" m=\"2\" v=\"2.96571\"/>\n" +
                    "<r y=\"2017\" m=\"3\" v=\"1.77018\"/>\n" +
                    "<r y=\"2017\" m=\"4\" v=\"1.06132\"/>\n" +
                    "<r y=\"2017\" m=\"5\" v=\"3.15053\"/>\n" +
                    "<r y=\"2017\" m=\"6\" v=\"0.00000\"/>\n" +
                    "<r y=\"2017\" m=\"7\" v=\"5.54299\"/>\n" +
                    "<r y=\"2017\" m=\"8\" v=\"0.58950\"/>\n" +
                    "<r y=\"2017\" m=\"9\" v=\"2.07778\"/>\n" +
                    "<r y=\"2017\" m=\"10\" v=\"1.48747\"/>\n" +
                    "<r y=\"2017\" m=\"11\" v=\"-1.31139\"/>\n" +
                    "<r y=\"2017\" m=\"12\" v=\"3.02241\"/>\n" +
                    "<r y=\"2018\" m=\"1\" v=\"8.24482\"/>\n" +
                    "<r y=\"2018\" m=\"2\" v=\"-5.72430\"/>\n" +
                    "<r y=\"2018\" m=\"3\" v=\"-3.14746\"/>\n" +
                    "<r y=\"2018\" m=\"4\" v=\"-1.45855\"/>\n" +
                    "<r y=\"2018\" m=\"5\" v=\"-0.88289\"/>\n" +
                    "<r y=\"2018\" m=\"6\" v=\"-5.31831\"/>\n" +
                    "<r y=\"2018\" m=\"7\" v=\"-0.33204\"/>\n" +
                    "<r y=\"2018\" m=\"8\" v=\"-0.27762\"/>\n" +
                    "<r y=\"2018\" m=\"9\" v=\"-0.58463\"/>\n" +
                    "<r y=\"2018\" m=\"10\" v=\"-11.45337\"/>\n" +
                    "<r y=\"2018\" m=\"11\" v=\"4.11132\"/>\n" +
                    "<r y=\"2018\" m=\"12\" v=\"-4.28311\"/>\n" +
                    "<r y=\"2019\" m=\"1\" v=\"9.90162\"/>\n" +
                    "<r y=\"2019\" m=\"2\" v=\"0.89518\"/>\n" +
                    "<r y=\"2019\" m=\"3\" v=\"2.66171\"/>\n" +
                    "<r y=\"2019\" m=\"4\" v=\"2.00725\"/>\n" +
                    "<r y=\"2019\" m=\"5\" v=\"-8.52692\"/>\n" +
                    "<r y=\"2019\" m=\"6\" v=\"6.75232\"/>\n" +
                    "<r y=\"2019\" m=\"7\" v=\"-0.39183\"/>\n" +
                    "<r y=\"2019\" m=\"8\" v=\"-4.29896\"/>\n" +
                    "<r y=\"2019\" m=\"9\" v=\"2.14328\"/>\n" +
                    "<r y=\"2019\" m=\"10\" v=\"2.06956\"/>\n" +
                    "<r y=\"2019\" m=\"11\" v=\"1.52070\"/>\n" +
                    "<r y=\"2019\" m=\"12\" v=\"6.57420\"/>\n" +
                    "<r y=\"2020\" m=\"1\" v=\"-4.24258\"/>\n" +
                    "<r y=\"2020\" m=\"2\" v=\"-2.09296\"/>\n" +
                    "<r y=\"2020\" m=\"3\" v=\"-15.32482\"/>\n" +
                    "<r y=\"2020\" m=\"4\" v=\"7.70492\"/>\n" +
                    "<r y=\"2020\" m=\"5\" v=\"-0.15221\"/>\n" +
                    "<r y=\"2020\" m=\"6\" v=\"8.53659\"/>\n" +
                    "<r y=\"2020\" m=\"7\" v=\"10.16854\"/>\n" +
                    "<r y=\"2020\" m=\"8\" v=\"4.13055\"/>\n" +
                    "<r y=\"2020\" m=\"9\" v=\"-0.90597\"/>\n" +
                    "<r y=\"2020\" m=\"10\" v=\"3.48406\"/>\n" +
                    "<r y=\"2020\" m=\"11\" v=\"6.94842\"/>\n" +
                    "<r y=\"2020\" m=\"12\" v=\"6.22907\"/>\n" +
                    "<r y=\"2021\" m=\"1\" v=\"5.44346\"/>\n" +
                    "<r y=\"2021\" m=\"2\" v=\"1.79390\"/>\n" +
                    "<r y=\"2021\" m=\"3\" v=\"-3.34835\"/>\n" +
                    "<r y=\"2021\" m=\"4\" v=\"0.56726\"/>\n" +
                    "<r y=\"2021\" m=\"5\" v=\"0.30218\"/>\n" +
                    "<r y=\"2021\" m=\"6\" v=\"-2.20928\"/>\n" +
                    "</CalendarReturn>\n" +
                    "</data>\n" +
                    "</response>";
            return res;
        }
    }

}