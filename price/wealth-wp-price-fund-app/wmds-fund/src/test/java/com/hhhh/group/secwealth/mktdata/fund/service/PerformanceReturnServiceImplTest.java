package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.service.ServiceExecutor;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.AdvanceChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.AdvanceChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.PerformanceReturnResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart.Result;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.performancereturn.PerformanceReturn;
import com.hhhh.group.secwealth.mktdata.fund.dao.PerformanceReturnDao;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.hhhh.group.secwealth.mktdata.fund.service.FundSearchResultServiceImplTest.findMethodWithAccess;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerformanceReturnServiceImplTest {


    private static final String INSTID = "INSTID";
    private static final String INSTUID = "INSTUID";
    private static final String EMAIL = "EMAIL";
    private static final String GROUP = "GROUP";
    private static final String SESSION_U_R_L = "SESSION_U_R_L";
    private static final int RETRY_TIMES = 1;
    @Mock
    private ServiceExecutor performanceReturnServiceExecutor;
    @Mock
    private PerformanceReturnDao performanceReturnDao;
    @Mock
    private InternalProductKeyUtil internalProductKeyUtil;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private HttpConnectionManager connManager;
    @Mock
    private MstarAccessCodeHelper accessCodeHelper;
    @InjectMocks
    private PerformanceReturnServiceImpl underTest;


    @Nested
    class WhenExecuting {
        @Mock
        private PerformanceReturnRequest request;

        @BeforeEach
        void setup() throws Exception {
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            PerformanceReturnResponse response = new PerformanceReturnResponse();
            List<PerformanceReturn> products = new ArrayList<>();
            PerformanceReturn performanceReturn = new PerformanceReturn();
            performanceReturn.setCurrency("HKD");
            performanceReturn.setLastUpdatedDate("2021-05-31");
            products.add(performanceReturn);
            response.setProducts(products);

            Mockito.when(performanceReturnServiceExecutor.execute(any(PerformanceReturnRequest.class))).thenReturn(response);

        }
        @Test
         void test_WhenExecuting() throws Exception {
            PerformanceReturnResponse performanceReturnResponse =  (PerformanceReturnResponse)underTest.execute(request);
            Assert.assertNotNull(performanceReturnResponse.getProducts());
        }
    }


    @Nested
    class WhenExecuting1 {
        @Test
         void mergeResponseFromeDB() throws Exception {
            PerformanceReturnResponse response = new PerformanceReturnResponse();
            response.setProducts(new ArrayList<PerformanceReturn>(){{add(new PerformanceReturn());}});
            Method method = findMethodWithAccess(underTest, "mergeResponseFromeDB", PerformanceReturnResponse.class, List.class,List.class,int.class);
            Object [] objects =new Object[]{};
            Assert.assertThrows(Exception.class,()->method.invoke(underTest,response,new ArrayList<ProductKey>(){{add(new ProductKey());}},new ArrayList<Object[]>(){{add(objects);}},1));
        }
    }
}