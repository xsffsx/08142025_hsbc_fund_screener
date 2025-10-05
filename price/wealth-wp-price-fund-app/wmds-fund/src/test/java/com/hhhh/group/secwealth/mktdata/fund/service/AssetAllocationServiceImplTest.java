package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.TopTenHoldingsData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AssetAllocationRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AssetAllocationResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.CategoryOverviewResponse;
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
class AssetAllocationServiceImplTest {


    private static final String URL = "URL";
    private static final String DATA_CLASS = "DATA_CLASS";
    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 16;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private AssetAllocationServiceImpl underTest;



    @Nested
    class WhenIniting {
        @BeforeEach
        void setup() throws ClassNotFoundException, JAXBException {

            dataClassJC = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.assetallocation.AssetAllocationData"));

        }


    }

    @Nested
    class WhenExecuting {
        @Mock
        private AssetAllocationRequest request;

        @BeforeEach
        void setup() {
        }


    }

    @Nested
    class WhenSendingRequest {
        @Mock
        private AssetAllocationRequest request;
        @Mock
        private SearchProduct searchProduct;
        @BeforeEach
        void setup() throws Exception {
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            Mockito.when(request.getProdCdeAltClassCde()).thenReturn("M");
            Mockito.when(request.getProdAltNum()).thenReturn("U50002");
            Mockito.when(request.getMarket()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("UT");

            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.assetallocation.AssetAllocationData")).createUnmarshaller();
            Mockito.when(dataClassJC.createUnmarshaller()).thenReturn(un);

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
            String res = "<response>\n" +
                    "<status>\n" +
                    "<code>0</code>\n" +
                    "<message>OK</message>\n" +
                    "</status>\n" +
                    "<data _idtype=\"mstarid\" _id=\"0P0000PVV1\">\n" +
                    "<api _name=\"AssetAllocationBreakdownRecentPort\">\n" +
                    "<PortfolioDate>2021-05-31</PortfolioDate>\n" +
                    "<AssetAllocCashLong>250.55872</AssetAllocCashLong>\n" +
                    "<AssetAllocEquityLong>20.09007</AssetAllocEquityLong>\n" +
                    "<AssetAllocBondLong>68.73943</AssetAllocBondLong>\n" +
                    "<AssetAllocCashShort>233.91283</AssetAllocCashShort>\n" +
                    "<AssetAllocEquityShort>1.02697</AssetAllocEquityShort>\n" +
                    "<AssetAllocBondShort>9.55617</AssetAllocBondShort>\n" +
                    "<AssetAllocBondNet>59.18326</AssetAllocBondNet>\n" +
                    "<PreferredStockNet>0.00534</PreferredStockNet>\n" +
                    "<AssetAllocCashNet>16.64590</AssetAllocCashNet>\n" +
                    "<OtherNet>5.10773</OtherNet>\n" +
                    "<PreferredStockShort>0.00000</PreferredStockShort>\n" +
                    "<ConvertibleShort>0.00000</ConvertibleShort>\n" +
                    "<OtherShort>0.20085</OtherShort>\n" +
                    "<PreferredStockLong>0.00534</PreferredStockLong>\n" +
                    "<OtherLong>5.30857</OtherLong>\n" +
                    "<AssetAllocEquityNet>19.06311</AssetAllocEquityNet>\n" +
                    "<ConvertibleLong>2.27056</ConvertibleLong>\n" +
                    "<ConvertibleNet>2.27056</ConvertibleNet>\n" +
                    "<AssetAllocConvBondLong>2.27056</AssetAllocConvBondLong>\n" +
                    "</api>\n" +
                    "</data>\n" +
                    "</response>";

            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class))).thenReturn(searchProduct);
            Mockito.when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);
        }

        @Test
         void test_WhenExecuting() throws Exception {
            AssetAllocationResponse response = (AssetAllocationResponse)underTest.execute(request);
            Assert.assertNotNull(response.getPortfolioDate());
        }
    }
}