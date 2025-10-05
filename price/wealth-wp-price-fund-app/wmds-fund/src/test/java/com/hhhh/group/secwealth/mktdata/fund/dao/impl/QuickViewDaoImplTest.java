package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuickViewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;

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

import javax.persistence.*;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuickViewDaoImplTest {


    private final String hhhh_BEST_SELLER = "quickview.bestsellers";
    private final String hhhh_NEW_FUND = "quickview.newfunds";
    @InjectMocks
    private QuickViewDaoImpl underTest;

    @Mock
    private BaseDao baseDao;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;


    @Nested
    class WhenExecuting {

        @BeforeEach
        void setup() throws Exception {
            Field period = underTest.getClass().getDeclaredField("period");
            period.setAccessible(true);
            period.set(underTest, "1Y");

            when(baseDao.getEntityManager()).thenReturn(entityManager);
            when(entityManager.createNativeQuery(any())).thenReturn(query);
            when(query.getResultList()).thenReturn(new ArrayList());


        }

        @Test
         void getTopPerformanceFunds() throws Exception {
            String productType = "UT";
            TimeScale timeScale = TimeScale.fromString("1Y");
            String category = "cat";
            String productSubType = "prod";
            Integer quickviewResultLimit = 10;
            String restrOnlScribInd = "Y";
            String currency = "HKD";
            String prodStatCde = "C";
            Assert.assertNotNull(underTest.getTopPerformanceFunds(productType, timeScale, category, productSubType, quickviewResultLimit, restrOnlScribInd, currency, prodStatCde, new HashMap<>()));
        }

        @Test
        void getTopPerformanceFunds2() throws Exception {
            String productType = "";
            String category = "";
            String productSubType = "";
            String restrOnlScribInd = "";
            String currency = "";
            String prodStatCde = "";
            Assert.assertNotNull(underTest.getTopPerformanceFunds(productType, null, category, productSubType, null, restrOnlScribInd, currency, prodStatCde, new HashMap<>()));
        }
    }

    @Test
    void getBottomPerformanceFunds() throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        String productType = "UT";
        TimeScale timeScale = TimeScale.fromString("1Y");
        String category = "";
        String productSubType = "";
        Integer quickviewResultLimit = 10;
        String restrOnlScribInd = "Y";
        String currency = "HKD";
        String prodStatCde = "C";
        Assert.assertNotNull(underTest.getBottomPerformanceFunds(productType, timeScale, category, productSubType, quickviewResultLimit, restrOnlScribInd, currency, prodStatCde, new HashMap<>()));
    }

    @Test
    void getFundOfQuarter() throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        String productType = "UT";
        TimeScale timeScale = TimeScale.fromString("1Y");
        String category = "ca";
        String productSubType = "prod";
        Integer quickviewResultLimit = 10;
        Assert.assertNotNull(underTest.getFundOfQuarter(productType, timeScale, category, productSubType, quickviewResultLimit, new HashMap<>()));
    }

    @Test
    void getFundOfQuarter2() throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        String productType = "";
        String category = "";
        String productSubType = "";
        Assert.assertNotNull(underTest.getFundOfQuarter(productType, null, category, productSubType, null, new HashMap<>()));
    }

    @Test
    void getTopPerformanceFundsTotalCount() throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        String productType = "UT";
        TimeScale timeScale = TimeScale.fromString("1Y");
        String category = "cat";
        String productSubType = "prod";
        String restrOnlScribInd = "Y";
        String currency = "HKD";
        String prodStatCde = "C";
        Assert.assertNotNull(underTest.getTopPerformanceFundsTotalCount(productType, timeScale, category, productSubType, restrOnlScribInd, currency, prodStatCde, new HashMap<>()));
    }

    @Test
    void getTopPerformanceFundsTotalCount2() throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        String productType = "";
        String category = "";
        String productSubType = "";
        String restrOnlScribInd = "";
        String currency = "";
        String prodStatCde = "";
        Assert.assertNotNull(underTest.getTopPerformanceFundsTotalCount(productType, null, category, productSubType, restrOnlScribInd, currency, prodStatCde, new HashMap<>()));
    }

    @Test
    void getBottomPerformanceTotalCount() throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        String productType = "UT";
        TimeScale timeScale = TimeScale.fromString("1Y");
        String category = "";
        String productSubType = "";
        String restrOnlScribInd = "Y";
        String currency = "HKD";
        String prodStatCde = "C";
        Assert.assertNotNull(underTest.getBottomPerformanceTotalCount(productType, timeScale, category,
                productSubType, restrOnlScribInd, currency, prodStatCde, new HashMap<>()));


    }

    @Test
    void getFundOfQuarterTotalCount()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        String productType = "UT";
        TimeScale timeScale = TimeScale.fromString("1Y");
        String category = "ss";
        String productSubType = "ss";
        Assert.assertNotNull(underTest.getFundOfQuarterTotalCount(productType,timeScale, category,productSubType, new HashMap<>()));
    }

    @Test
    void getFundsByFundHouse()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String category = "cat";
        String productSubType = "prod";
        String wpcCriteriaValue = "db";
        Assert.assertNotNull(underTest.getFundsByFundHouse(category, productSubType, timeScale,wpcCriteriaValue, new HashMap<>()));
    }

    @Test
    void getFundsByFundHouseCount()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String category = "cat";
        String productSubType = "prod";
        String wpcCriteriaValue = "db";
        Assert.assertNotNull(underTest.getFundsByFundHouseCount(category,productSubType, timeScale,wpcCriteriaValue, new HashMap<>()));
    }

    @Test
    void searchFundByProductKeys() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        List<ProductKey> productKeyList = new ArrayList<>();
        Assert.assertNotNull( underTest.searchFundByProductKeys( productKeyList, timeScale, new HashMap<>()));
    }

    @Test
    void searchFundByProductKeys2() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        ProductKey productKey = new ProductKey();
        productKey.setProdAltNum("sss");
        productKey.setMarket("HKD");
        productKey.setProductType("HK");
        List<ProductKey> productKeyList = new ArrayList<>();
        productKeyList.add(productKey);
        Assert.assertNotNull( underTest.searchFundByProductKeys( productKeyList, timeScale, new HashMap<>()));
    }

    @Test
    void searchFundByProductKeysCount() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        List<ProductKey> productKeyList = new ArrayList<>();
        Assert.assertNotNull(underTest.searchFundByProductKeysCount( productKeyList,timeScale, new HashMap<>()));
    }

    @Test
    void searchFundByProductKeysCount3() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        List<ProductKey> productKeyList = new ArrayList<>();
        productKeyList.add(new ProductKey());
        productKeyList.add(new ProductKey());
        Assert.assertNotNull(underTest.searchFundByProductKeysCount( productKeyList,timeScale, new HashMap<>()));
    }

    @Test
    void searchFundByProductKeysCount2() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        ProductKey productKey = new ProductKey();
        productKey.setProdAltNum("sss");
        productKey.setMarket("HKD");
        productKey.setProductType("HK");
        List<ProductKey> productKeyList = new ArrayList<>();
        productKeyList.add(productKey);
        Assert.assertNotNull(underTest.searchFundByProductKeysCount( productKeyList,timeScale, new HashMap<>()));
    }

    @Test
    void searchFundsRemoveChanlCde()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        List<ProductKey> productKeyList = new ArrayList<>();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        QuickViewRequest request = new QuickViewRequest();
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        Assert.assertNotNull(underTest.searchFundsRemoveChanlCde( productKeyList, timeScale, request));
    }

    @Test
    void searchFundsRemoveChanlCde2()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        List<ProductKey> productKeyList = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProdAltNum("sss");
        productKey.setMarket("HKD");
        productKey.setProductType("HK");
        productKeyList.add(productKey);
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        QuickViewRequest request = new QuickViewRequest();
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        Assert.assertNotNull(underTest.searchFundsRemoveChanlCde( productKeyList, timeScale, request));
    }

    @Test
    void searchFundFromdb() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        QuickViewRequest request = new QuickViewRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String tableNameValue ="quickview.newfunds";
        String currency="HKD";
        String prodStatCde="C";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.addHeader(CommonConstants.REQUEST_HEADER_CHANNELID,"OHI");
        request.addHeader(CommonConstants.REQUEST_HEADER_GBGF,"CMB");
        Assert.assertNotNull(underTest.searchFundFromdb( timeScale, tableNameValue, request, currency, prodStatCde));
    }

    @Test
    void searchFundFromdb5() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        QuickViewRequest request = new QuickViewRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String tableNameValue ="quickview.newfunds";
        String currency="HKD";
        String prodStatCde="C";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.addHeader(CommonConstants.REQUEST_HEADER_CHANNELID,"OHB");
        request.addHeader(CommonConstants.REQUEST_HEADER_GBGF,"CMB");
        Assert.assertNotNull(underTest.searchFundFromdb( timeScale, tableNameValue, request, currency, prodStatCde));
    }

    @Test
    void searchFundFromdb6() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        QuickViewRequest request = new QuickViewRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String tableNameValue ="quickview.newfunds";
        String currency="HKD";
        String prodStatCde="C";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.addHeader(CommonConstants.REQUEST_HEADER_GBGF,"WPB");
        Assert.assertNotNull(underTest.searchFundFromdb( timeScale, tableNameValue, request, currency, prodStatCde));
    }

    @Test
    void searchFundFromdb7() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        QuickViewRequest request = new QuickViewRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String tableNameValue ="quickview.newfunds";
        String currency="HKD";
        String prodStatCde="C";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        request.addHeader(CommonConstants.REQUEST_HEADER_CHANNELID,"MOB");
        request.addHeader(CommonConstants.REQUEST_HEADER_GBGF,"CMB");
        Assert.assertNotNull(underTest.searchFundFromdb( timeScale, tableNameValue, request, currency, prodStatCde));
    }

    @Test
    void searchFundFromdb2() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        QuickViewRequest request = new QuickViewRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String tableNameValue ="quickview.bestsellers";
        String currency="HKD";
        String prodStatCde="C";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        Assert.assertNotNull(underTest.searchFundFromdb( timeScale, tableNameValue, request, currency, prodStatCde));
    }

    @Test
    void searchFundFromdb3() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1M");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        QuickViewRequest request = new QuickViewRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String tableNameValue ="quickview.newfunds";
        String currency="HKD";
        String prodStatCde="C";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        Assert.assertNotNull(underTest.searchFundFromdb( timeScale, tableNameValue, request, currency, prodStatCde));
    }

    @Test
    void searchFundFromdb4() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1D");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        QuickViewRequest request = new QuickViewRequest();
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String tableNameValue ="quickview.newfunds";
        String currency="HKD";
        String prodStatCde="C";
        request.setChannelRestrictCode(channelRestrictCode);
        request.setRestrOnlScribInd(restrOnlScribInd);
        Assert.assertNotNull(underTest.searchFundFromdb( timeScale, tableNameValue, request, currency, prodStatCde));
    }

    @Test
    void getTop5PerformersFunds() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String productType ="UT";
        String currency="HKD";
        String prodStatCde="C";
        String category="SI";
        Assert.assertNotNull(underTest.getTop5PerformersFunds(productType, timeScale,category,
        "FD",channelRestrictCode,restrOnlScribInd,prodStatCde,
        "10", currency, 10, new HashMap<>()));
    }

    @Test
    void getTop5PerformersFundsTotalCount() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String productType ="UT";
        String currency="HKD";
        String prodStatCde="C";
        String category="SI";
        Assert.assertNotNull(underTest.getTop5PerformersFundsTotalCount( productType, category, "re", channelRestrictCode,  restrOnlScribInd,  prodStatCde,  "10", currency, new HashMap<>()));
    }

    @Test
    void getTopDividendCount()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String productType ="UT";
        String currency="HKD";
        String prodStatCde="C";
        String category="SI";
        Assert.assertNotNull(underTest.getTopDividendCount(productType,timeScale,category, "re",
         channelRestrictCode, restrOnlScribInd,  prodStatCde, "10", currency, new HashMap<>()));
    }

    @Test
    void getTopDividend()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String channelRestrictCode ="SI_I";
        String restrOnlScribInd ="Y";
        String productType ="UT";
        String currency="HKD";
        String prodStatCde="C";
        String category="SI";
        Assert.assertNotNull(underTest.getTopDividend(  productType, timeScale,category,
         "re", channelRestrictCode,  restrOnlScribInd,  prodStatCde, "10", currency, 10, new HashMap<>()));
    }

    @Test
    void getEsgFundCount() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String restrOnlScribInd ="Y";
        String currency="HKD";
        String prodStatCde="C";
        Assert.assertNotNull(underTest.getEsgFundCount("Y",timeScale,restrOnlScribInd,currency, prodStatCde, new HashMap<>()));
    }

    @Test
    void getEsgFundCount2() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String restrOnlScribInd ="Y";
        String currency="HKD";
        String prodStatCde="C";
        Assert.assertNotNull(underTest.getEsgFundCount(timeScale,restrOnlScribInd,currency, prodStatCde, new HashMap<>()));
    }

    @Test
    void getGBAFundCount() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String restrOnlScribInd ="Y";
        String currency="HKD";
        String prodStatCde="C";
        Assert.assertNotNull(underTest.getGBAFundCount("Y",timeScale, restrOnlScribInd,currency,prodStatCde, new HashMap<>()));
    }

    @Test
    void getGBAFundCount2() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        String restrOnlScribInd ="Y";
        String currency="HKD";
        String prodStatCde="C";
        Assert.assertNotNull(underTest.getGBAFundCount(timeScale, restrOnlScribInd,currency,prodStatCde, new HashMap<>()));
    }

    @Test
    void getEsgFund() throws Exception{
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        Assert.assertNotNull(underTest.getEsgFund(timeScale, "Y","HKD","C", new HashMap<>(),10));
    }

    @Test
    void getGBAFund()throws Exception {
        Field period = underTest.getClass().getDeclaredField("period");
        period.setAccessible(true);
        period.set(underTest, "1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        TimeScale timeScale = TimeScale.fromString("1Y");
        Assert.assertNotNull(underTest.getGBAFund(timeScale,"Y", "HKD" ,"C", new HashMap<>(), 10));
    }

}