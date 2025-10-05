package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.TestCaseCommonUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AdvanceChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.AdvanceChartDao;
import com.hhhh.group.secwealth.mktdata.fund.util.EhcacheUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.MstarAccessCodeHelper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import org.springframework.util.ReflectionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.hhhh.group.secwealth.mktdata.fund.service.AdvanceChartServiceImplTest.*;
import static com.hhhh.group.secwealth.mktdata.fund.service.FundSearchResultServiceImplTest.findMethodWithAccess;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdvanceChartJobCacheServiceImplTest {

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
    private JAXBContext jaxbContext;
    @Mock
    private JAXBContext growthDataClassJC;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private AdvanceChartJobCacheServiceImpl underTest;

    @Nested
    class WhenExecuting1 {

        @BeforeEach
        void setup() throws Exception{
            when(advanceChartDao.getUtProdInstmList(any(),anyMap())).thenReturn(buildUtProdInstmList());
        }


        @Test
         void calculateNavStartDate() throws Exception {
            AdvanceChartRequest request = new AdvanceChartRequest();
            request.setTimeZone("Asia/Hong_Kong");
            request.setFrequency("daily");
            Method method = findMethodWithAccess(underTest, "calculateNavStartDate", AdvanceChartRequest.class,String.class);
            Method method1 = findMethodWithAccess(underTest, "calculateTradeTime", AdvanceChartRequest.class);
            request.setPeriod("YTD");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            String.valueOf(method1.invoke(underTest, request));
            request.setPeriod("MAX");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            String.valueOf(method1.invoke(underTest, request));
            assertNotNull(method);
        }
    }

    @Nested
    class WhenExecuting {
        @Mock
        private AdvanceChartRequest request;

        @BeforeEach
        void setup() throws Exception {
            Field navUrl = underTest.getClass().getDeclaredField("navUrl");
            navUrl.setAccessible(true);
            navUrl.set(underTest, "https://eultrcqa.morningstar.com/api/rest.svc/timeseries_price");
            Field mstarToken = underTest.getClass().getDeclaredField("mstarToken");
            mstarToken.setAccessible(true);
            mstarToken.set(underTest, "x7gotzru67");
            Field growthUrl = underTest.getClass().getDeclaredField("growthUrl");
            growthUrl.setAccessible(true);
            growthUrl.set(underTest, "https://eultrcqa.morningstar.com/api/rest.svc/timeseries_cumulativereturn");
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
            Mockito.when( localeMappingUtil.getNameByIndex(any())).thenReturn(1);
            Mockito.when(advanceChartDao.getEnableCacheProdInstmList()).thenReturn(buildUtProdInstmList());
            when(advanceChartDao.getUtProdInstmList(any(),anyMap())).thenReturn(buildUtProdInstmList());
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(any(), any(), any(), any(), any(), any(), any())).thenReturn(buildSearchProduct());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.AdvanceChartGrowthData")).createUnmarshaller();
            when(growthDataClassJC.createUnmarshaller()).thenReturn(un);
//            Mockito.when(navDataClassJC.createUnmarshaller().unmarshal(anyString())
            String res = buildRes();
            when(connManager.sendRequest(any(String.class), any(String.class),any())).thenReturn(res);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            Mockito.when(request.getStartDate()).thenReturn("2004-01-22");
            Mockito.when(request.getEndDate()).thenReturn("2016-02-22");
            Assert.assertThrows(Exception.class,()->underTest.cacheData());
            Mockito.when(request.getFrequency()).thenReturn("monthly");
            Assert.assertThrows(Exception.class,()->underTest.cacheData());
            Mockito.when(request.getPeriod()).thenReturn("MAX");
            Assert.assertThrows(Exception.class,()->underTest.cacheData());

        }

    @Test
    void getNavPriceData() throws InvocationTargetException, IllegalAccessException, JAXBException, NoSuchFieldException {

        Method method = TestCaseCommonUtil.findMethodWithAccessibility(underTest, "getNavPriceData",
                AdvanceChartRequest.class,String.class,String.class, List.class,String.class);
        assertNotNull(method);
        List<NameValuePair> params  = new ArrayList<>();
         params.add(new BasicNameValuePair("startDate", "2004-01-22"));
        params.add(new BasicNameValuePair("startDate1", "2004-01-23"));
        params.add(new BasicNameValuePair("startDate2", "2004-01-25"));
        params.add(new BasicNameValuePair("21313", "2131"));
        params.add(new BasicNameValuePair("2131243", "213we1"));
        assertThrows(InvocationTargetException.class, () -> method.invoke(underTest, request, "2004-01-22", "2004-01-22", params, "200"));
    }


    }


}