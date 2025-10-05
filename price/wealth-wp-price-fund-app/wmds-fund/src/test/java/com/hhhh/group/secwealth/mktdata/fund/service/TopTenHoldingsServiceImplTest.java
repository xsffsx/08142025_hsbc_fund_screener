package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.TopTenHoldingsData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TopTenHoldingsRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.TopTenHoldingsResponse;
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

import javax.xml.bind.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopTenHoldingsServiceImplTest {


    private static final String S_I_T_E__F_E_A_T_U_R_E__M_S_T_A_R__M_S_T_A_R_N_E_W_A_P_I = "S_I_T_E__F_E_A_T_U_R_E__M_S_T_A_R__M_S_T_A_R_N_E_W_A_P_I";
    private static final String S_I_T_E__F_E_A_T_U_R_E__M_S_T_A_R__M_S_T_A_R_N_E_W_A_P_I__S_I_T_E_T_Y_P_E = "S_I_T_E__F_E_A_T_U_R_E__M_S_T_A_R__M_S_T_A_R_N_E_W_A_P_I__S_I_T_E_T_Y_P_E";
    private static final String URL = "URL";
    private static final String URL_API = "URL_API";
    private static final String URL_API_GLCM = "URL_API_GLCM";
    private static final String DATA_CLASS = "DATA_CLASS";
    private static final String DATA_SET_CLASS = "DATA_SET_CLASS";
    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 54;
    @Mock
    private Map<String, String> topTenHoldingDataPointNewapiMap;
    @Mock
    private ConcurrentHashMap<String, String> countryCodeMap;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private SiteFeature siteFeature;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private JAXBContext dataSetClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private TopTenHoldingsServiceImpl underTest;

    @Nested
    class WhenIniting {
        @BeforeEach
        void setup() throws JAXBException, ClassNotFoundException {
            try {
                dataClassJC = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.TopTenHoldingsData"));
                dataSetClassJC = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.quotesummary.newapi.DataSet"));
            } catch (Exception e) {
                throw e;
            }

        }

    }

    @Nested
    class WhenExecuting {
        @Mock
        private TopTenHoldingsRequest request;

        @Mock
        private PredSrchProperties predSrchProps;

        @Mock
        private SearchProduct searchProduct;
        @Mock
        TopTenHoldingsData topTenHoldingsData;



        @Mock
        ResponseData response;

        @BeforeEach
        void setup() throws Exception {
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings.TopTenHoldingsData")).createUnmarshaller();
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


            String res = "<response><status><code>0</code><message>OK</message></status><data _idtype=\"mstarid\" _id=\"0P0001EPQ8\"><api _id=\"zxvpktjnovy1zj77\"><T10H-Holdings><HoldingDetail><HoldingType>BT</HoldingType><Name>The Government of Hong Kong Special Administrative Region 0%</Name><CountryId>HKG</CountryId><Country>Hong Kong</Country><CurrencyId>HKD</CurrencyId><Currency>Hong Kong Dollar</Currency><CUSIP>Y3R2B8JL9</CUSIP><ISIN>HK0000685468</ISIN><Weighting>6.46742</Weighting><NumberOfShare>368000000</NumberOfShare><MarketValue>367999444</MarketValue><ShareChange>368000000</ShareChange><MaturityDate>2021-05-12</MaturityDate><Coupon>0</Coupon></HoldingDetail><HoldingDetail><HoldingType>BD</HoldingType><Name>Hong Kong Monetary Authority 0%</Name><CountryId>HKG</CountryId><Country>Hong Kong</Country><CurrencyId>HKD</CurrencyId><Currency>Hong Kong Dollar</Currency><ISIN>HK0000706652</ISIN><Weighting>4.39362</Weighting><NumberOfShare>250000000</NumberOfShare><MarketValue>249999142</MarketValue><ShareChange>250000000</ShareChange><MaturityDate>2021-05-26</MaturityDate><Coupon>0</Coupon></HoldingDetail><HoldingDetail><HoldingType>BD</HoldingType><Name>The Hong Kong Mortgage Corporation Limited 0%</Name><CountryId>HKG</CountryId><Country>Hong Kong</Country><CurrencyId>HKD</CurrencyId><Currency>Hong Kong Dollar</Currency><CUSIP>Y3R21PKJ5</CUSIP><ISIN>XS2338000073</ISIN><Weighting>1.93291</Weighting><NumberOfShare>110000000</NumberOfShare><MarketValue>109983728</MarketValue><ShareChange>110000000</ShareChange><MaturityDate>2021-07-30</MaturityDate><Coupon>0</Coupon></HoldingDetail></T10H-Holdings><PSRP-PortfolioDate>2021-04-30</PSRP-PortfolioDate><PS-PortfolioCurrencyId>HKD</PS-PortfolioCurrencyId></api></data></response>";

            response = new TopTenHoldingsData();
            Mockito.when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);



        }
        @Test
         void test_WhenExecuting() throws Exception {
            TopTenHoldingsResponse response = (TopTenHoldingsResponse) underTest.execute(request);
            Assert.assertNotNull(response.getTop10Holdings());
        }
    }

    @Nested
    class WhenSendingRequest {
        @Mock
        private TopTenHoldingsRequest request;

        @BeforeEach
        void setup() {
        //    Mockito.when(underTest.sendRequest(any(Object.class),any(Object.class),any(Object.class))).thenReturn(new Object());

        }
    }

    @Nested
    class WhenSendingRequestToNewApi {
        @Mock
        private TopTenHoldingsRequest request;

        @BeforeEach
        void setup() {
        }
    }

    @Nested
    class WhenGettingSearchProduct {
        @Mock
        private TopTenHoldingsRequest request;

        @BeforeEach
        void setup() {
        }
    }
}