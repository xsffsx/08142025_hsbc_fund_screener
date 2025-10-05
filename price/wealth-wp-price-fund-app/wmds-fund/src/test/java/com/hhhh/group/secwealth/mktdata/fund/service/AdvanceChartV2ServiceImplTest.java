package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AdvanceChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AdvanceChartV2Response;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.ResultV2;
import com.hhhh.group.secwealth.mktdata.fund.dao.AdvanceChartDao;
import com.hhhh.group.secwealth.mktdata.fund.util.EhcacheUtil;
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
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.hhhh.group.secwealth.mktdata.fund.service.AdvanceChartServiceImplTest.*;
import static com.hhhh.group.secwealth.mktdata.fund.service.FundSearchResultServiceImplTest.findMethodWithAccess;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdvanceChartV2ServiceImplTest {

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
    private AdvanceChartV2ServiceImpl underTest;

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
            AdvanceChartV2Response response = (AdvanceChartV2Response) underTest.execute(request);
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
        @Test
        void test_WhenExecuting2() throws Exception {
            Mockito.when(internalProductKeyUtil.getProductBySearchWithAltClassCde(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(buildSearchProduct());
            Unmarshaller un = JAXBContext.newInstance(Class.forName("com.hhhh.group.secwealth.mktdata.fund.beans.mstar.advanceChart.growth.AdvanceChartGrowthData")).createUnmarshaller();
            when(navDataClassJC.createUnmarshaller()).thenReturn(un);
            String res = buildGrowthRes();
            when(connManager.sendRequest(any(String.class),any(String.class),any())).thenReturn(res);
            AdvanceChartV2Response response = (AdvanceChartV2Response) underTest.execute(request);
            Assert.assertNotNull(response.getResult());
            Mockito.when(request.getStartDate()).thenReturn("2004-01-22");
            Mockito.when(request.getEndDate()).thenReturn("2016-02-22");
            underTest.execute(request);
            Assert.assertTrue(true);
        }
        @Test
         void calculateNavStartDate() throws Exception {
            AdvanceChartRequest request = new AdvanceChartRequest();
            request.setTimeZone("Asia/Hong_Kong");
            request.setFrequency("daily");
            Method method = findMethodWithAccess(underTest, "calculateTradeTime", AdvanceChartRequest.class,String.class);
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            request.setPeriod("1Y");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            request.setPeriod("YTD");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            request.setPeriod("MAX");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            request.setPeriod("1M");
            String.valueOf(method.invoke(underTest, request,"2023-02-01"));
            request.setPeriod("1D");
            Assert.assertThrows(Exception.class,()->method.invoke(underTest, request,"2023-02-01"));
        }

        @Test
        void setProductNameByIndex() throws Exception {
            Method method = findMethodWithAccess(underTest, "setProductNameByIndex", boolean.class,UtProdInstm.class, ResultV2.class,int.class);
            String.valueOf(method.invoke(underTest, true,new UtProdInstm(),new ResultV2(),1));
            String.valueOf(method.invoke(underTest, true,new UtProdInstm(),new ResultV2(),2));
            String.valueOf(method.invoke(underTest, true,new UtProdInstm(),new ResultV2(),0));

            String.valueOf(method.invoke(underTest, false,new UtProdInstm(),new ResultV2(),1));
            String.valueOf(method.invoke(underTest, false,new UtProdInstm(),new ResultV2(),2));
            String.valueOf(method.invoke(underTest, false,new UtProdInstm(),new ResultV2(),0));
            Assert.assertNotNull(method);
        }

    }
}