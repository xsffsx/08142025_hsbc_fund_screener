package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.ProductKeyListWithEligibilityCheckService;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckImplDelegate;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WpcWebServiceDashboardTest {


    @Mock
    private ProductKeyListWithEligibilityCheckService productKeyListWithEligibilityCheckService;
    @Mock
    private ProductKeyListWithEligibilityCheckImplDelegate productKeyListWithEligibilityCheckImplDelegate;
    @InjectMocks
    private WpcWebServiceDashboard underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private Object object;

        @BeforeEach
        void setup() throws Exception {
            String[] li = {"HK_HBAP"};
            Field f = underTest.getClass().getDeclaredField("supportSites");
            f.setAccessible(true);
            f.set(underTest,new String[]{"Default","HK_HBAP"});
         //   String[] tags = (String[]) f.get(underTest);


            ProductKeyListWithEligibilityCheckResponse response = new ProductKeyListWithEligibilityCheckResponse();
            Mockito.when(productKeyListWithEligibilityCheckService.getProductKeyListWithEligibilityCheck(any(String.class), any(String.class), any(String.class), any(String.class), ArgumentMatchers.<String>anyObject(), any(String.class),ArgumentMatchers.<String[]>anyObject())).thenReturn(response);

           //
          //  Mockito.when(supportSites).thenReturn(si);

        }
        @Test
         void test_WhenExecuting() throws Exception {

            Map<String, String> headerMap = new HashMap<>();
            ProductKeyListWithEligibilityCheckResponse respnse =(ProductKeyListWithEligibilityCheckResponse) underTest.execute(headerMap);
            Assert.assertNotNull(respnse.getReasonCode());
        }

    }

}