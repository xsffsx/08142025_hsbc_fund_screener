package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.CategoryOverviewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.CategoryOverviewResponse;
import com.hhhh.group.secwealth.mktdata.fund.dao.CategoryOverviewDao;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "job.start.day.time = 10"
})
class CategoryOverviewServiceImplTest {

    @Mock
    private CategoryOverviewDao categoryOverviewDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @InjectMocks
    private CategoryOverviewServiceImpl underTest;



    @Nested
    class WhenExecuting {
        @Mock
        private CategoryOverviewRequest request;

        @BeforeEach
        void setup() throws Exception {


            Field f = underTest.getClass().getDeclaredField("startDayTime");
            f.setAccessible(true);
            f.set(underTest, 10);

            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getLocale()).thenReturn("en_US");

            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = "UT";
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = "UT";
            obj1[8] = 12.3;
            obj1[9] = 12.3;
            obj1[10] = 12.3;
            obj1[11] = 12.3;
            obj1[12] = 12.3;
            li.add(obj1);
            Mockito.when(categoryOverviewDao.getCategoryOverviewList(any(String.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class))).thenReturn(li);
        }

        @Test
         void test_WhenExecuting() throws Exception {
            CategoryOverviewResponse respnse =(CategoryOverviewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getLastUpdatedDate());
        }

        @Test
         void test_WhenExecuting2() throws Exception {
            Mockito.when(request.getLocale()).thenReturn("zh_HK");
            CategoryOverviewResponse respnse =(CategoryOverviewResponse) underTest.execute(request);
            Mockito.when(request.getLocale()).thenReturn("zh_CN");
            underTest.execute(request);
            Assert.assertNotNull(respnse.getLastUpdatedDate());
        }
    }
}