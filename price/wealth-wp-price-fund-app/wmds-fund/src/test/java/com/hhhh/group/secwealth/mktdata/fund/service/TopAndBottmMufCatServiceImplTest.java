package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopBottomCategory;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TopAndBottmMufCatRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.TopAndBottmMufCatResponse;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.dao.TopAndBottmMufCatServiceDao;
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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopAndBottmMufCatServiceImplTest {

    @Mock
    private TopAndBottmMufCatServiceDao topAndBottmMufCatServiceDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @InjectMocks
    private TopAndBottmMufCatServiceImpl underTest;

    @Nested
    class WhenExecuting {
        @Mock
        private TopAndBottmMufCatRequest request;

        @BeforeEach
        void setup() throws Exception {
            Field f = underTest.getClass().getDeclaredField("topBottomCategoryReturnCnt");
            f.setAccessible(true);
            f.set(underTest, 10);

            Mockito.when(request.getCountryCode()).thenReturn("HK");
//            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
//            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getProductSubType()).thenReturn("uk");
            Mockito.when(request.getMarket()).thenReturn("hk");
            Mockito.when(request.getAssetAllocation()).thenReturn("stock");
            Mockito.when(request.getTimeScale()).thenReturn("1Y");
            Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            TopAndBottomCategoryResponse response =  new TopAndBottomCategoryResponse();
            List<TopBottomCategory> topCategories = new ArrayList<>();
            TopBottomCategory topBottomCategory = new TopBottomCategory();
            topBottomCategory.setCategoryCode("hk");
            topBottomCategory.setCategoryName("family");
            topBottomCategory.setAverageRisk(new BigDecimal(1));
            topBottomCategory.setNumberOfFunds(1);
            topBottomCategory.setNumberOfhhhhFunds(2);
            topCategories.add(topBottomCategory);
            List<TopBottomCategory> bottomCategories = new ArrayList<>();
            TopBottomCategory topBottomCategory1 = new TopBottomCategory();
            topBottomCategory1.setCategoryCode("hk");
            topBottomCategory1.setCategoryName("family");
            topBottomCategory1.setAverageRisk(new BigDecimal(1));
            topBottomCategory1.setNumberOfFunds(1);
            topBottomCategory1.setNumberOfhhhhFunds(2);
            bottomCategories.add(topBottomCategory1);
            response.setBottomCategories(topCategories);
            response.setTopCategories(bottomCategories);

            Mockito.when(topAndBottmMufCatServiceDao.searchTopAndBottomList(any(Integer.class), ArgumentMatchers.<String>anyList(),ArgumentMatchers.<String>anyList(),ArgumentMatchers.<String>anyList(),any(String.class),any(TimeScale.class),any(Integer.class),any(String.class),any(String.class))).thenReturn(response);
            Mockito.when(topAndBottmMufCatServiceDao.searchPerformanceTableLastUpdateDate(any(String.class),any(String.class))).thenReturn(new Date());


        }

        @Test
         void test_WhenExecuting() throws Exception {
            TopAndBottmMufCatResponse response =  (TopAndBottmMufCatResponse)underTest.execute(request);
            Assert.assertNotNull(response.getFundCategories());
        }
    }
}