package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.CategoryOverviewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareIndexRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.CategoryOverviewResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.FundCompareIndexResponse;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundCompareIndexDao;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundCompareIndexServiceImplTest {


    @Mock
    private FundCompareIndexDao fundCompareIndexDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @InjectMocks
    private FundCompareIndexServiceImpl underTest;

    @Nested
    class WhenExecuting {

        @Mock
        private FundCompareIndexRequest request;

        @BeforeEach
        void setup() throws Exception {
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = "UT";
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[5] = "UT";

            li.add(obj1);

            Mockito.when(fundCompareIndexDao.getCompareIndexList(any(FundCompareIndexRequest.class))).thenReturn(li);
        }
        @Test
         void test_WhenExecuting() throws Exception {
            FundCompareIndexResponse response=  (FundCompareIndexResponse) underTest.execute(request);
            Assert.assertNotNull(response.getCompareIndex());
        }
    }
}