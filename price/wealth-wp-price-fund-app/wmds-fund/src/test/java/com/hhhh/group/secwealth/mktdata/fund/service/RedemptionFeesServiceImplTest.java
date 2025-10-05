package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.RedemptionFeesRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.RedemptionFeesResponse;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedemptionFeesServiceImplTest {

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
    private RedemptionFeesServiceImpl underTest;

    @Nested
    class WhenIniting {
        @BeforeEach
        void setup() {
        }
    }

    @Nested
    class WhenExecuting {
        @Mock
        private RedemptionFeesRequest request;

        @BeforeEach
        void setup() throws Exception{
            Field url = underTest.getClass().getDeclaredField("url");
            url.setAccessible(true);
            url.set(underTest,"https://api.morningstar.com/v2/service/mf/zzmqvfz1j567bs96");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getLocale()).thenReturn("en");
            Mockito.when(request.getMarket()).thenReturn("HK");
            Mockito.when(request.getProdCdeAltClassCde()).thenReturn("M");
            Mockito.when(request.getProdAltNum()).thenReturn("XRE");
            Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(DataBuilder.buildSearchProduct());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees.RedemptionFeesData")).createUnmarshaller();
            Mockito.when(dataClassJC.createUnmarshaller()).thenReturn(un);
            String res = DataBuilder.buildRespStr();
            Mockito.when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);
        }
        @Test
         void test_WhenExecuting() throws Exception {
            RedemptionFeesResponse response = (RedemptionFeesResponse) underTest.execute(request);
            Assert.assertNotNull(response);
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

        public static String buildRespStr(){
            String res = "<response>\n" +
                    "<status>\n" +
                    "<code>0</code>\n" +
                    "<message>OK</message>\n" +
                    "</status>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0001DK0T\">\n" +
                    "<api _id=\"zzmqvfz1j567bs96\">\n" +
                    "<LS-AdministrativeFee>\n" +
                    "<FeeSchedule>\n" +
                    "<LowBreakpoint>0</LowBreakpoint>\n" +
                    "<BreakpointUnit>Monetary</BreakpointUnit>\n" +
                    "<Unit>Percentage</Unit>\n" +
                    "<Value>0.40000</Value>\n" +
                    "</FeeSchedule>\n" +
                    "</LS-AdministrativeFee>\n" +
                    "<LS-CreationUnitDate>2018-09-04</LS-CreationUnitDate>\n" +
                    "<LS-FrontLoads>\n" +
                    "<FrontLoad>\n" +
                    "<LowBreakpoint>0</LowBreakpoint>\n" +
                    "<BreakpointUnit>Monetary</BreakpointUnit>\n" +
                    "<Unit>Percentage</Unit>\n" +
                    "<Value>5.00000</Value>\n" +
                    "</FrontLoad>\n" +
                    "</LS-FrontLoads>\n" +
                    "<LS-ManagementFees>\n" +
                    "<ManagementFee>\n" +
                    "<LowBreakpoint>0</LowBreakpoint>\n" +
                    "<BreakpointUnit>Monetary</BreakpointUnit>\n" +
                    "<Unit>Percentage</Unit>\n" +
                    "<Value>1.50000</Value>\n" +
                    "</ManagementFee>\n" +
                    "</LS-ManagementFees>\n" +
                    "<LS-MaximumFrontLoad>5.00000</LS-MaximumFrontLoad>\n" +
                    "<LS-MaximumManagementFee>1.50000</LS-MaximumManagementFee>\n" +
                    "<LS-SettlementDay>3</LS-SettlementDay>\n" +
                    "<LS-SettlementDayType>Excluding Bank Holiday</LS-SettlementDayType>\n" +
                    "<LS-TotalSalesCharge/>\n" +
                    "<LS-ProspectusCustodianFee>\n" +
                    "</LS-ProspectusCustodianFee>\n" +
                    "<LS-CreationUnitDate>\n" +
                    "</LS-CreationUnitDate>\n" +
                    "<LS-DistributionFees>\n" +
                    "</LS-DistributionFees>\n" +
                    "<LS-ManagementFees>\n" +
                    "</LS-ManagementFees>\n" +
                    "<LS-RedemptionFees>\n" +
                    "</LS-RedemptionFees>\n" +
                    "</api>\n" +
                    "</data>\n" +
                    "</response>";
            return res;
        }
    }
}