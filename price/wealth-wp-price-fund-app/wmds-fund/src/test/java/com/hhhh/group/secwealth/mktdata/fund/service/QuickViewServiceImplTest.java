package com.hhhh.group.secwealth.mktdata.fund.service;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.exception.ValidatorException;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuickViewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.QuickViewResponse;
import com.hhhh.group.secwealth.mktdata.fund.constants.QuickViewTableName;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.criteria.QuickViewCriteria;
import com.hhhh.group.secwealth.mktdata.fund.dao.QuickViewDao;
import com.hhhh.group.secwealth.mktdata.fund.util.WpcApiCfg;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductInfo;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.ProductKeyListWithEligibilityCheckService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuickViewServiceImplTest {


    private static final String DEFAULT_TIMESCALE = "DEFAULT_TIMESCALE";
    @Mock
    private Map<String, String> newFundBestSellerMap;
    @Mock
    private Map<String, String> newFundBestSellerCaseMap;
    @Mock
    private ProductKeyListWithEligibilityCheckService productKeyListWithEligibilityCheckService;
    @Mock
    private QuickViewDao quickViewDao;
    @Mock
    private WpcApiCfg wpcApiCfg;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private SiteFeature siteFeature;
    @InjectMocks
    private QuickViewServiceImpl underTest;


    @Nested
    class WhenExecuting {
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);



            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("UT");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(true);
            Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_NEW_FUND");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria3  = new SearchCriteria();
            searchCriteria3.setOperator("eq");
            searchCriteria3.setCriteriaKey("prodStatCde");
            searchCriteria3.setCriteriaValue("A");
            criterias.add(searchCriteria3);
            SearchCriteria searchCriteria4  = new SearchCriteria();
            searchCriteria4.setOperator("eq");
            searchCriteria4.setCriteriaKey("currency");
            searchCriteria4.setCriteriaValue("HKD");
            criterias.add(searchCriteria4);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = "UT";
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = "UT";
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
            Mockito.when(quickViewDao.searchFundFromdb(any(TimeScale.class), any(String.class),any(QuickViewRequest.class),any(String.class),any(String.class))).thenReturn(li);
        }


//        @Test
//         void test_WhenExecuting() throws Exception {
//             QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
//             Assert.assertNotNull(respnse.getQuickView());
//        }

    }

    @Nested
    class WhenExecuting2 {
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getRestrOnlScribInd()).thenReturn("N");

            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
      //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
     //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("BP");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            SearchCriteria searchCriteria3  = new SearchCriteria();
            searchCriteria3.setOperator("eq");
            searchCriteria3.setCriteriaKey("prodStatCde");
            searchCriteria3.setCriteriaValue("A");
            criterias.add(searchCriteria3);
            SearchCriteria searchCriteria4  = new SearchCriteria();
            searchCriteria4.setOperator("eq");
            searchCriteria4.setCriteriaKey("currency");
            searchCriteria4.setCriteriaValue("HKD");
            criterias.add(searchCriteria4);

            SearchCriteria searchCriteria5  = new SearchCriteria();
            searchCriteria5.setOperator("eq");
            searchCriteria5.setCriteriaKey("limitResult");
            searchCriteria5.setCriteriaValue("10");
            criterias.add(searchCriteria5);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
            Mockito.when(quickViewDao.getBottomPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class),any(String.class),any(String.class),any(String.class), any(Map.class))).thenReturn(li);

            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(String.class),any(String.class),any(String.class), any(Map.class))).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


          //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
          Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting3 {
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("TP");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
      //      Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }
    @Nested
    class WhenExecuting4 {
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");

            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("WEBSERVICE");

            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_BEST_SELLER");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
     //       Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
             Assert.assertNotNull(respnse.getQuickView());
        }

    }
    @Nested
    class WhenExecuting5 {
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_ALL_FUNDS");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
     //       Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting6 {
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_FUND_OF_QUARTER");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
       //     Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting7{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_CPF_SRS");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
      //      Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting8{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_MONTH_INV_PLAN");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
      //      Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting9{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_RESTRICTED_FUND");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
      //      Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting10{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_WORLDWIDE_FUND");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
      //      Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }
    @Nested
    class WhenExecuting11{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_NO_SUBSCRIPTION_FEE");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
      //      Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting12{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_CIE");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
//            Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting13{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("hhhh_NO_SUBSCRIPTION_FEE");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
//            Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting14{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("TOP5_PERFORMERS");
            criterias.add(searchCriteria);

            SearchCriteria searchCriteria1  = new SearchCriteria();
            searchCriteria1.setOperator("eq");
            searchCriteria1.setCriteriaKey("category");
            searchCriteria1.setCriteriaValue("sbu");
            criterias.add(searchCriteria1);


            SearchCriteria searchCriteria2  = new SearchCriteria();
            searchCriteria2.setOperator("eq");
            searchCriteria2.setCriteriaKey("productSubType");
            searchCriteria2.setCriteriaValue("ewe");
            criterias.add(searchCriteria2);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
//            Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }


    @Nested
    class WhenExecuting15{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("ESG_FUND");
            criterias.add(searchCriteria);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
//            Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting17{
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("TOP_DIVIDEND_YIELD");
            criterias.add(searchCriteria);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
//            Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting16{
        @Mock
        private QuickViewRequest request;
        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("GBA_FUND");
            criterias.add(searchCriteria);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
//            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("hhhh_NEW_FUND");
//            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
//            Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

    @Nested
    class WhenExecuting18 {
        @Mock
        private QuickViewRequest request;
        @Mock
        private SearchCriteria searchCriteria;
        @Mock
        private QuickViewCriteria quickViewCriteria;
        @Mock
        private ProductInfo productInfo;

        @BeforeEach
        void init() {
            Mockito.when(request.getCriterias()).thenReturn(Collections.singletonList(searchCriteria));
            Mockito.when(searchCriteria.getOperator()).thenReturn("!=");
            Mockito.when(searchCriteria.getCriteriaValue()).thenReturn("test001");
            Assertions.assertThrows(ValidatorException.class, () -> underTest.execute(request));
            ReflectionTestUtils.setField(underTest, "defaultTimescale", "timescale");
        }

        @Test
        void test_execute_1() throws Exception {
            when(productKeyListWithEligibilityCheckService.getProductInfoList(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(Collections.singletonList(productInfo));
            try (MockedStatic<QuickViewCriteria> quickViewCriteriaMockedStatic = mockStatic(QuickViewCriteria.class)) {
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(null);
                Mockito.when(searchCriteria.getOperator()).thenReturn("EQ");
                quickViewCriteriaMockedStatic.when(() -> QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class), anyString()))
                                             .thenReturn(quickViewCriteria);
                // BP
                Mockito.when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.BP);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                Assertions.assertNotNull(underTest.execute(request));
                // TP
                Mockito.when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.TP);
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.getTopPerformanceFunds(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(null);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_ALL_FUNDS
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_ALL_FUNDS);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.getFundsByFundHouse(any(), any(), any(), any(), any())).thenReturn(null);
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_BEST_SELLER
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_BEST_SELLER);
                when(request.getSiteKey()).thenReturn("hkhbap");
                when(newFundBestSellerMap.get("HKHBAP_BEST_SELLER")).thenReturn(null);
                when(newFundBestSellerMap.get("DEFAULT_BEST_SELLER")).thenReturn("DB");
                when(quickViewDao.searchFundFromdb(any(), any(), any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_FUND_OF_QUARTER
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_FUND_OF_QUARTER);
                when(quickViewDao.getFundOfQuarter(any(), any(), any(), any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_CPF_SRS
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_CPF_SRS);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_MONTH_INV_PLAN
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_MONTH_INV_PLAN);
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_NEW_FUND
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_NEW_FUND);
                when(newFundBestSellerMap.get("HKHBAP_NEW_FUND")).thenReturn("");
                when(newFundBestSellerMap.get("DEFAULT_NEW_FUND")).thenReturn("test-001");
                Assertions.assertNotNull(underTest.execute(request));
                when(newFundBestSellerMap.get("HKHBAP_NEW_FUND")).thenReturn("DB");
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_RESTRICTED_FUND
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_RESTRICTED_FUND);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
            }
        }

        @Test
        void test_execute_2() throws Exception {
            when(productKeyListWithEligibilityCheckService.getProductInfoList(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(Collections.singletonList(productInfo));
            try (MockedStatic<QuickViewCriteria> quickViewCriteriaMockedStatic = mockStatic(QuickViewCriteria.class)) {
                Mockito.when(searchCriteria.getOperator()).thenReturn("EQ");
                quickViewCriteriaMockedStatic.when(() -> QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class), anyString()))
                                             .thenReturn(quickViewCriteria);

                // hhhh_WORLDWIDE_FUND
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_WORLDWIDE_FUND);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_CIE
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_CIE);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_NO_SUBSCRIPTION_FEE
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_NO_SUBSCRIPTION_FEE);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.searchFundByProductKeys(any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // TOP5_PERFORMERS
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.TOP5_PERFORMERS);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.getTop5PerformersFunds(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.getTop5PerformersFunds(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // ESG_FUND
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.ESG_FUND);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.getEsgFund(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.getEsgFund(any(), any(), any(), any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // GBA_FUND
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.GBA_FUND);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.getGBAFund(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.getGBAFund(any(), any(), any(), any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // TOP_DIVIDEND_YIELD
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.TRUE);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.TOP_DIVIDEND_YIELD);
                Assertions.assertNotNull(underTest.execute(request));
                when(request.getReturnOnlyNumberOfMatches()).thenReturn(Boolean.FALSE);
                when(quickViewDao.getTopDividend(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
                Assertions.assertNotNull(underTest.execute(request));
                when(quickViewDao.getTopDividend(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(quickViewRespItems());
                Assertions.assertNotNull(underTest.execute(request));
                // Default
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.BE);
                Assertions.assertNotNull(underTest.execute(request));
            }
        }

        @Test
        void test_execute_sort_response() throws Exception {
            try (MockedStatic<QuickViewCriteria> quickViewCriteriaMockedStatic = mockStatic(QuickViewCriteria.class)) {
                Mockito.when(searchCriteria.getOperator()).thenReturn("EQ");
                quickViewCriteriaMockedStatic.when(() -> QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class), anyString()))
                                             .thenReturn(quickViewCriteria);
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.TP);
                when(quickViewDao.getTopPerformanceFunds(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(quickViewSortItems());
                Assertions.assertNotNull(underTest.execute(request));
                // GBA_FUND
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.GBA_FUND);
                when(quickViewDao.getGBAFund(any(), any(), any(), any(), any(), any())).thenReturn(quickViewSortItems());
                Assertions.assertNotNull(underTest.execute(request));
                // hhhh_BEST_SELLER
                when(quickViewDao.searchFundFromdb(any(), any(), any(), any(), any())).thenReturn(quickViewSortItems());
                when(quickViewCriteria.getTableName()).thenReturn(QuickViewTableName.hhhh_BEST_SELLER);
                when(newFundBestSellerCaseMap.get("BEST_SELLER")).thenReturn("test-001");
                when(newFundBestSellerMap.get("HKHBAP_BEST_SELLER")).thenReturn("DB");
                when(request.getSiteKey()).thenReturn("HKHBAP");
                Assertions.assertNotNull(underTest.execute(request));
                ReflectionTestUtils.setField(underTest, "quickviewResultLimit", 1);
                Assertions.assertNotNull(underTest.execute(request));
            }
        }

        @Test
        void test_other_methods() throws Exception {
            Assertions.assertNotNull(underTest);
            ReflectionTestUtils.invokeMethod(underTest, "converterToResponse", 2,quickViewRespItems(), 1, false);
            ReflectionTestUtils.invokeMethod(underTest, "converterToResponse", 2,quickViewRespItems(), 2, false);
            ReflectionTestUtils.invokeMethod(underTest, "converterToResponse", 2,quickViewRespItems(), 3, false);

            when(productKeyListWithEligibilityCheckService.getProductInfoList(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(Stream.of(productInfo, null).collect(Collectors.toList()));
            ReflectionTestUtils.invokeMethod(underTest, "getResultList", request, "CD", TimeScale.MTD, "test", "WEBSERVICE"
                    , "HKD", "A");

        }

        private List<Object[]> quickViewRespItems() {
            List<Object[]> resultList = new ArrayList<>();
            Object[] data = new Object[10];
            data[0] = "test-001";
            data[1] = "UT";
            data[3] = "test-001";
            data[4] = "test-001";
            data[5] = "HK";
            data[7] = "199.99";
            data[8] = "1";
            data[9] = "test-001";
            resultList.add(data);
            return resultList;
        }

        private List<Object[]> quickViewSortItems() {
            List<Object[]> resultList = new ArrayList<>();
            Object[] data = new Object[10];
            data[0] = "test-001";
            data[1] = "UT";
            data[3] = "test-001";
            data[4] = "test-001";
            data[5] = "HK";
            data[7] = "199.99";
            data[8] = "1";
            data[9] = "test-001";
            Object[] data2 = new Object[10];
            data2[0] = "test-002";
            data2[1] = "UT";
            data2[3] = "test-002";
            data2[4] = "test-002";
            data2[5] = "HK";
            data2[7] = "199.99";
            data2[8] = "1";
            data2[9] = "test-001";
            resultList.add(data);
            resultList.add(data2);
            return resultList;
        }
    }
    
    @Nested
    class WhenExecuting19 {
        @Mock
        private QuickViewRequest request;



        @BeforeEach
        void setup() throws Exception {

            Field f = underTest.getClass().getDeclaredField("defaultTimescale");
            f.setAccessible(true);
            f.set(underTest, "1Y");
            Field ff = underTest.getClass().getDeclaredField("quickviewResultLimit");
            ff.setAccessible(true);
            ff.set(underTest, 50);
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getSiteKey()).thenReturn("HK_HBAP");
            Mockito.when(request.getCountryCode()).thenReturn("HK");
            Mockito.when(request.getGroupMember()).thenReturn("HBAP");
            Mockito.when(request.getProductType()).thenReturn("SEC");
            Mockito.when(request.getReturnOnlyNumberOfMatches()).thenReturn(false);
            //      Mockito.when(request.getRestrOnlScribInd()).thenReturn("Y");
            //       Mockito.when(request.getChannelRestrictCode()).thenReturn("Y");
            List<SearchCriteria> criterias =  new ArrayList<>();
            SearchCriteria searchCriteria  = new SearchCriteria();
            searchCriteria.setOperator("eq");
            searchCriteria.setCriteriaKey("tableName");
            searchCriteria.setCriteriaValue("HBSC_RETIREMENT_FUND");
            criterias.add(searchCriteria);

            Mockito.when(request.getCriterias()).thenReturn(criterias);
            Mockito.when(newFundBestSellerCaseMap.get(any(String.class))).thenReturn("HBSC_RETIREMENT_FUND");
            Mockito.when(newFundBestSellerMap.get(any(String.class))).thenReturn("DB");
            List<Object[]> li = new ArrayList<>();

            Object[] obj1 = new Object[20];
            obj1[0] = "HKD";
            obj1[1] = "UT";
            obj1[2] = new Date();
            obj1[3] = "UT";
            obj1[4] = "UT";
            obj1[7] = 2121;
            obj1[8] = "UT";
            obj1[9] = "UT";
            li.add(obj1);
     //       Mockito.when(quickViewDao.getTopPerformanceFunds(any(String.class),any(TimeScale.class),any(String.class),any(String.class),any(Integer.class))).thenReturn(li);

//            Mockito.when(quickViewDao.getBottomPerformanceTotalCount(any(),any(),any(),any())).thenReturn(10);
            QuickViewCriteria quick = new QuickViewCriteria();
            quick.setCategory("JK");
            quick.setProductSubType("sb");


            //  Mockito.when(QuickViewCriteria.getQuickViewCriteria(any(QuickViewRequest.class),any(String.class))).thenReturn(quick);

        }

        @Test
         void test_WhenExecuting() throws Exception {
            QuickViewResponse respnse =(QuickViewResponse) underTest.execute(request);
            Assert.assertNotNull(respnse.getQuickView());
        }

    }

}