package com.hhhh.group.secwealth.mktdata.fund.dao.common;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;

import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FundCommonDaoTest {

    private static final String OR_MAP_IDLIST = "IdListMap";
    private static final String OR_MAP_SUBHQL = "SubHQL";


    @Mock
    private LocaleMappingUtil localeMappingUtil;

    @Mock
    private FundCommonDao fundCommonDao;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;
    @Mock
    private BaseDao baseDao;

    @Test
    void createQueryForHql() throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        Assert.assertNull(fundCommonDao.createQueryForHql("select"));

    }

    @Test
    void createQueryForNativeSql() throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        Assert.assertNull(fundCommonDao.createQueryForNativeSql("select"));
    }

    @Test
    void getProdIdByProductKeys()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
       final List<ProductKey> li = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("UT");
        productKey.setProdAltNum("U50001");
        productKey.setMarket("HK");
        li.add(productKey);
        Assert.assertNotNull(fundCommonDao.getProdIdByProductKeys(li, new HashMap<>()));

    }

    @Test
    void getPerformanceIdByProductKeys()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List<ProductKey> li = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("UT");
        productKey.setProdAltNum("U50001");
        productKey.setMarket("HK");
        li.add(productKey);
        fundCommonDao.getPerformanceIdByProductKeys(li, new HashMap<>());
        Assert.assertNotNull(fundCommonDao.getPerformanceIdByProductKeys(li, new HashMap<>()));
    }

    @Test
    void generateHqlOR() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List<Object> subIdList = new ArrayList<Object>();
        subIdList.add(31);
        Assert.assertNotNull(fundCommonDao.generateHqlOR(subIdList,"re","3"));
    }

    @Test
    void appendSubHql() throws Exception{
        Map<String,Object> map = new HashMap<>();
        fundCommonDao.appendSubHql(new StringBuilder("select"),"or",map);
        Assert.assertNotNull(map);
    }

    @Test
    void setQueryParamKey() throws Exception{
        Map<String,Object> map = new HashMap<>();
        fundCommonDao.setQueryParamKey(query,map);
        Assert.assertNotNull(map);
    }

    @Test
    void getRestrOnlScribIds() throws Exception{
        Assert.assertNotNull(fundCommonDao.getRestrOnlScribIds("Y"));
    }

    @Test
    void searchChanlFunds() throws Exception{
        Assert.assertNotNull(fundCommonDao.searchChanlFunds("SI_I"));
    }

    @Test
    void getReturnFieldName1() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("1M")));
    }

    @Test
    void getReturnFieldName2() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("3M")));
    }

    @Test
    void getReturnFieldName3() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("6M")));
    }

    @Test
    void getReturnFieldName4() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("1Y")));
    }

    @Test
    void getReturnFieldName5() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("3Y")));
    }

    @Test
    void getReturnFieldName6() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("5Y")));
    }

    @Test
    void getReturnFieldName7() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("10Y")));
    }

    @Test
    void getReturnFieldName8() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("ytd")));
    }

    @Test
    void getReturnFieldName9() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("qtd")));
    }

    @Test
    void getReturnFieldName10() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("mtd")));
    }

    @Test
    void getReturnFieldName11() throws Exception{
        Assert.assertNull(fundCommonDao.getReturnFieldName(TimeScale.fromString("ALL")));
    }
}