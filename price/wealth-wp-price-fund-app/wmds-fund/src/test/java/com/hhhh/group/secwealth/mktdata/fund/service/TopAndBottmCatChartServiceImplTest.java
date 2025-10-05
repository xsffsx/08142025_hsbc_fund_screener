package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.CategoryLine;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.TopAndBottmCatChartRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuickViewResponse;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.TopAndBottmCatChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.dao.TopAndBottmCatChartServiceDao;
import com.hhhh.group.secwealth.mktdata.fund.util.FeConvertorHelper;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopAndBottmCatChartServiceImplTest {


    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private TopAndBottmCatChartServiceDao topAndBottmCatChartServiceDao;
    @InjectMocks
    private TopAndBottmCatChartServiceImpl underTest;


    @Nested
    class WhenExecuting {
        @Mock
        private TopAndBottmCatChartRequest request;

        @BeforeEach
        void setup() throws Exception {
            Field f = underTest.getClass().getDeclaredField("topBottomCategoryReturnCnt");
            f.setAccessible(true);
            f.set(underTest, 10);

            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getMarket()).thenReturn("HK");

            Mockito.when(request.getCategoryCode()).thenReturn("se");
            Mockito.when(request.getProductSubType()).thenReturn("geo");
            Mockito.when(request.getLocale()).thenReturn("en_US");
            Mockito.when(request.getMarketPeriod()).thenReturn("1Y");
            Mockito.when(request.getProductType()).thenReturn("UT");

            TopAndBottomCategoryChartResponse response = new TopAndBottomCategoryChartResponse();
            List<CategoryLine> categoryLines = new ArrayList<CategoryLine>();
            CategoryLine ategoryLine= new CategoryLine();
            ategoryLine.setCategoryName("rw");
            ategoryLine.setCategoryCode("dode");
            categoryLines.add(ategoryLine);

            response.setCategoryLines(categoryLines);

            Mockito.when(topAndBottmCatChartServiceDao.searchUtProdCatList(any(Integer.class),
                    ArgumentMatchers.<String>anyList(), ArgumentMatchers.<String>anyList(), ArgumentMatchers.<String>anyList(),ArgumentMatchers.<String>anyList(), any(TimeScale.class), any(Integer.class))).thenReturn(response);





        }
         @Test
         void test_WhenExecuting() throws Exception {
            TopAndBottmCatChartResponse response =(TopAndBottmCatChartResponse) underTest.execute(request);
            Assert.assertNotNull(response.getCategories());
        }

    }
}