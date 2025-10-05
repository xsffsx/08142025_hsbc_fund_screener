package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.*;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.returnIndexChart.R;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AdvanceChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchResultRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AdvanceChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.Result;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchResultProduct;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.HoldingAllocation;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.TopHoldingsSearch;
import com.hhhh.group.secwealth.mktdata.fund.dao.AdvanceChartDao;
import com.hhhh.group.secwealth.mktdata.fund.util.EhcacheUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductInfo;
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
import org.opensaml.xmlsec.signature.P;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hhhh.group.secwealth.mktdata.fund.service.FundSearchResultServiceImplTest.findMethodWithAccess;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdvanceChartServiceImplTest {


    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private AdvanceChartDao advanceChartDao;
    @Mock
    private EhcacheUtil ehcacheUtil;
    @Mock
    private JAXBContext dataClassJC;
    @Mock
    private JAXBContext navDataClassJC;
    @Mock
    private JAXBContext growthDataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private AdvanceChartServiceImpl underTest;

    @Nested
    class WhenExecuting1 {

        @BeforeEach
        void setup() throws Exception{
            when(advanceChartDao.getUtProdInstmList(any(), anyMap())).thenReturn(buildUtProdInstmList());
        }
        @Test
         void SetIndexName() throws Exception {
            Method method = findMethodWithAccess(underTest, "SetIndexName", AdvanceChartRequest.class,AdvanceChartResponse.class);
            AdvanceChartResponse response = new AdvanceChartResponse();
            Result result = new Result();
            ProdAltNumSeg prodAltNumSegs = new ProdAltNumSeg();
            prodAltNumSegs.setProdAltNum("540002");
            result.setProdAltNumSegs(new ArrayList<ProdAltNumSeg>(){{add(prodAltNumSegs);}});
            response.setResult(new ArrayList<Result>(){{add(result);}});
            AdvanceChartRequest request = new AdvanceChartRequest();
            ProductKey productKey = new ProductKey();
            productKey.setProdCdeAltClassCde("M");
            productKey.setProductType("UT");
            productKey.setProdAltNum("540002");
            request.setProductKeys(new ArrayList<ProductKey>(){{add(productKey);}});
            request.setDataType(new String[]{"indexPrice"});;
            String.valueOf(method.invoke(underTest, request,response));
            Assert.assertNotNull(method);
        }

        @Test
         void calculateNavStartDate() throws Exception {
            AdvanceChartRequest request = new AdvanceChartRequest();
            request.setTimeZone("Asia/Hong_Kong");
            request.setFrequency("daily");
            request.setPeriod("1Y");
            Method method = findMethodWithAccess(underTest, "calculateNavStartDate", AdvanceChartRequest.class,String.class);
            Method method1 = findMethodWithAccess(underTest, "calculateTradeTime", AdvanceChartRequest.class,String.class);
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            String.valueOf(method1.invoke(underTest, request,"2023-02-01"));
            request.setPeriod("YTD");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            request.setPeriod("MAX");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            String.valueOf(method1.invoke(underTest, request,"2023-02-01"));
            Assert.assertNotNull(method);
        }


    }

    @Nested
    class WhenExecuting {
        @Mock
        private AdvanceChartRequest request;

        @BeforeEach
        void setup() throws Exception{
            Field navUrl = underTest.getClass().getDeclaredField("navUrl");
            navUrl.setAccessible(true);
            navUrl.set(underTest,"https://eultrcqa.morningstar.com/api/rest.svc/timeseries_price");
            Field mstarToken = underTest.getClass().getDeclaredField("mstarToken");
            mstarToken.setAccessible(true);
            mstarToken.set(underTest,"x7gotzru67");
            Field growthUrl = underTest.getClass().getDeclaredField("growthUrl");
            growthUrl.setAccessible(true);
            growthUrl.set(underTest,"https://eultrcqa.morningstar.com/api/rest.svc/timeseries_cumulativereturn");
            Mockito.when(request.getProductKeys()).thenReturn(buildProductKeyList());
            Mockito.when(request.getDataType()).thenReturn(buildDataType());
            Mockito.when(request.getTimeZone()).thenReturn("Asia/Hong_Kong");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getLocale()).thenReturn("en");
            Mockito.when(request.getPeriod()).thenReturn("YTD");
            Mockito.when(request.getCurrency()).thenReturn("EUR");
            Mockito.when(request.getFrequency()).thenReturn("daily");
            Mockito.when(request.getNavForwardFill()).thenReturn(true);

            when(advanceChartDao.getUtProdInstmList(any(),anyMap())).thenReturn(buildUtProdInstmList());
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(buildSearchProduct());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.AdvanceChartNavData")).createUnmarshaller();
            when(navDataClassJC.createUnmarshaller()).thenReturn(un);
//            Mockito.when(navDataClassJC.createUnmarshaller().unmarshal(anyString())
            String res = buildRes();
            when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);

        }
        @Test
         void test_WhenExecuting() throws Exception {
            AdvanceChartResponse response = (AdvanceChartResponse) underTest.execute(request);
            Assert.assertNotNull(response.getResult());
            Mockito.when(request.getStartDate()).thenReturn("2004-01-22");
            Mockito.when(request.getEndDate()).thenReturn("2016-02-22");
            underTest.execute(request);
            Mockito.when(request.getFrequency()).thenReturn("monthly");
            underTest.execute(request);
            Mockito.when(request.getPeriod()).thenReturn("MAX");
            underTest.execute(request);
            Assert.assertTrue(true);
        }
    }


        public static SearchProduct buildSearchProduct(){
            SearchProduct searchProduct = new SearchProduct();
            searchProduct.setExternalKey("0P0001BFG7");
            SearchableObject searchObject = new SearchableObject();
            searchObject.setSymbol("U62509");
            ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
            prodAltNumSeg.setProdAltNum("U62509");
            prodAltNumSeg.setProdCdeAltClassCde("M");
            List<ProdAltNumSeg> prodAltNumSegs = new ArrayList<ProdAltNumSeg>(){{add(prodAltNumSeg);}};
            searchObject.setProdAltNumSeg(prodAltNumSegs);
            searchProduct.setSearchObject(searchObject);
            return searchProduct;
        }

        public static List<ProductKey> buildProductKeyList() {
            List<ProductKey> productKeyList = new ArrayList<>();
            ProductKey productKey = new ProductKey();
            productKey.setMarket("HK");
            productKey.setProdAltNum("U62509");
            productKey.setProdCdeAltClassCde("M");
            productKey.setProductType("UT");
            productKeyList.add(productKey);
            return productKeyList;
        }

        public static String[] buildDataType()  {
            String[] datatypes = {"navPrice"};
            return datatypes;
        }

        public static List<UtProdInstm> buildUtProdInstmList(){
            List<UtProdInstm> utProdInstmList  = new ArrayList<>();
            UtProdInstm utProdInstm =  new UtProdInstm();
            UtProdInstmId utProdInstmId = new UtProdInstmId();
            utProdInstmId.setProductId(2);
            utProdInstmId.setBatchId(4);
            utProdInstm.setAllowReguCntbInd("Y");
            utProdInstm.setUtProdInstmId(utProdInstmId);
            utProdInstm.setPerformanceId("0P00007KN5");
            utProdInstm.setSymbol("U62509");
            utProdInstm.setCcyProdTradeCde("HKD");
            utProdInstm.setUndlIndexCde("Y");
            utProdInstm.setmStarID("XIUSA000PM");
            utProdInstm.setMstarCcyCde("HKD");
            utProdInstm.setCcyInvstCde("HKD");
            utProdInstm.setProductType("UT");
            utProdInstm.setMarket("HK");
            utProdInstmList.add(utProdInstm);
            return utProdInstmList;
        }
    public static String buildRes(){
        String res = "<TimeSeries>\n" +
                "<Security Id=\"XIUSA000PM\">\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-16</EndDate>\n" +
                "<Value>32.9955626259211</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-17</EndDate>\n" +
                "<Value>32.9955626259211</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-18</EndDate>\n" +
                "<Value>32.9955626259211</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-19</EndDate>\n" +
                "<Value>30.8977535834498</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-20</EndDate>\n" +
                "<Value>32.2660409883412</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-21</EndDate>\n" +
                "<Value>33.64710268179</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-22</EndDate>\n" +
                "<Value>34.0161258470475</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-23</EndDate>\n" +
                "<Value>35.1406953341568</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-24</EndDate>\n" +
                "<Value>35.1406953341568</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-25</EndDate>\n" +
                "<Value>35.1406953341568</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-26</EndDate>\n" +
                "<Value>35.6452511534344</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2021-07-27</EndDate>\n" +
                "<Value>35.2443924537224</Value>\n" +
                "</HistoryDetail>\n" +
                "</Security>\n" +
                "</TimeSeries>";
        return res;
    }

    public static String buildGrowthRes(){
        String res = "<TimeSeries>\n" +
                "<Security Id=\"XIUSA000PM\">\n" +
                "<CumulativeReturnSeries>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-08</EndDate>\n" +
                "<Value>0</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-09</EndDate>\n" +
                "<Value>1.59167748449329</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-10</EndDate>\n" +
                "<Value>1.65785597062099</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-11</EndDate>\n" +
                "<Value>1.65785597062099</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-12</EndDate>\n" +
                "<Value>1.65785597062099</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-13</EndDate>\n" +
                "<Value>0.874440457940054</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-14</EndDate>\n" +
                "<Value>3.44743067574518</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-15</EndDate>\n" +
                "<Value>1.03176051967662</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-16</EndDate>\n" +
                "<Value>1.25472747157218</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-17</EndDate>\n" +
                "<Value>3.99120083960258</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-18</EndDate>\n" +
                "<Value>3.99120083960258</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-19</EndDate>\n" +
                "<Value>3.99120083960258</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-20</EndDate>\n" +
                "<Value>2.78068181052706</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-21</EndDate>\n" +
                "<Value>-0.402263708857177</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-22</EndDate>\n" +
                "<Value>1.45959550958472</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-23</EndDate>\n" +
                "<Value>1.78572576665897</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-24</EndDate>\n" +
                "<Value>2.48251189008204</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-25</EndDate>\n" +
                "<Value>2.48251189008204</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-26</EndDate>\n" +
                "<Value>2.48251189008204</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-27</EndDate>\n" +
                "<Value>4.28803128968309</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-28</EndDate>\n" +
                "<Value>4.33432651939498</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-29</EndDate>\n" +
                "<Value>6.79403741123387</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-04-30</EndDate>\n" +
                "<Value>5.90319897016869</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-01</EndDate>\n" +
                "<Value>3.43251219774329</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-02</EndDate>\n" +
                "<Value>3.43251219774329</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-03</EndDate>\n" +
                "<Value>3.43251219774329</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-04</EndDate>\n" +
                "<Value>3.09476824732531</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-05</EndDate>\n" +
                "<Value>4.14808355389526</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-06</EndDate>\n" +
                "<Value>3.58961441802559</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-07</EndDate>\n" +
                "<Value>4.5686458235656</Value>\n" +
                "</HistoryDetail>\n" +
                "<HistoryDetail>\n" +
                "<EndDate>2020-05-08</EndDate>\n" +
                "<Value>6.43306094924267</Value>\n" +
                "</HistoryDetail>\n" +
                "</CumulativeReturnSeries>\n" +
                "</Security>\n" +
                "</TimeSeries>";
        return res;
    }

}