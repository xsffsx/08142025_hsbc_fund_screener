package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchRangeCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchRangeCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.PropertyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchResultRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.DetailedCriteriaUtil;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.RangeCriteriaUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.omg.PortableInterceptor.INACTIVE;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FundSearchResultDaoImplTest {
    @InjectMocks
    private FundSearchResultDaoImpl underTest;

    @Mock
    private SiteFeature siteFeature;
    @Mock
    private Query query;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BaseDao baseDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    private DetailedCriteriaUtil detailedCriteriaUtil;
    @Mock
    private Map<String, String> holdingAllocationMap;
    @Mock
    private RangeCriteriaUtil rangeCriteriaUtil;
    @Mock
    private PropertyUtil prop;
    @Test
    void searchTotalCount() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        Long a  =5L;
        when(query.getSingleResult()).thenReturn(a);
        FundSearchResultRequest request = new FundSearchResultRequest();
        Map<String,List<Integer>> map = new HashMap<>();
        List<Integer> li = new ArrayList<>();
        li.add(4);
        map.put("switchOutFund",li);
        List<String> list = new ArrayList<>();
        list.add("li");
        Map<String,Boolean> mapBoolean = new HashMap<>();
        List<ProductKey>productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setMarket("HK");
        productKey.setProdAltNum("U50001");
        productKey.setProdCdeAltClassCde("M");
        productKeys.add(productKey);
        Assert.assertNotNull(underTest.searchTotalCount(request,map,list,productKeys,"HK","HBAP",mapBoolean,true,1));

    }

    @Test
    void searchFund() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        Long a  =5L;
        when(query.getSingleResult()).thenReturn(a);
        FundSearchResultRequest request = new FundSearchResultRequest();
        Map<String,List<Integer>> map = new HashMap<>();
        List<Integer> li = new ArrayList<>();
        li.add(4);
        map.put("switchOutFund",li);
        List<String> list = new ArrayList<>();
        list.add("li");
        Map<String,Boolean> mapBoolean = new HashMap<>();
        List<ProductKey>productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setMarket("HK");
        productKey.setProdAltNum("U50001");
        productKey.setProdCdeAltClassCde("M");
        productKeys.add(productKey);
        Assert.assertNotNull(underTest.searchFund(request,map,list,productKeys,"HK","HBAP",mapBoolean,true,1));
    }

    @Test
    void appendDetailedCriteria()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("allowSellMipProdInd");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        SearchCriteria searchCriteria2 = new SearchCriteria();
        searchCriteria2.setCriteriaKey("keyword");
        searchCriteria2.setCriteriaValue("hhhhA");
        searchCriteria2.setOperator("eq");
        li.add(searchCriteria);
        li.add(searchCriteria2);
        Assert.assertNotNull(underTest.appendDetailedCriteria(new StringBuilder("select"),  li,
        1, "HK", "HBAP", new HashMap<>()));
    }

    @Test
    void getProdIdByAllowSellMipProdInd()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("allowSellMipProdInd");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        Assert.assertNotNull(underTest.getProdIdByAllowSellMipProdInd(searchCriteria, new HashMap<>()));
    }


    @Test
    void appendCriteriaByProdIds()throws Exception {
        List<SearchRangeCriteria> li = new ArrayList<>();
        SearchRangeCriteria searchCriteria = new SearchRangeCriteria();
        searchCriteria.setMax(new SearchCriteria());
        searchCriteria.setMin(new SearchCriteria());
        li.add(searchCriteria);
        Assert.assertNotNull(underTest.appendCriteriaByProdIds(new StringBuilder("select"),new ArrayList<Integer>(),new ArrayList<Integer>(),new ArrayList<Integer>()));
    }

    @Test
    void appendCriteriaByRiskCode()throws Exception {
        List<String> li= new ArrayList<>();
        li.add("li");
        Assert.assertNotNull(underTest.appendCriteriaByRiskCode(new StringBuilder("select"),li));
    }

    @Test
    void validateSortOrder() throws Exception{
        Assert.assertNotNull(underTest.validateSortOrder("desc"));

    }

    @Test
    void searchHldgsMap() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        List<Integer> li = new ArrayList<>();
        li.add(1);
        Assert.assertNotNull(underTest.searchHldgsMap(li,"UT"));
    }

    @Test
    void searchTop5HldgMap()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        List<String> li = new ArrayList<>();
        li.add("Assest");
        Map<String, Boolean> map = new HashMap<>();
        map.put("seller",true);
        Assert.assertNotNull(underTest.searchTop5HldgMap(li,map));

    }

    @Test
    void getUtSvce() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        List<String> li = new ArrayList<>();
        li.add("Assest");
        Assert.assertNotNull(underTest.getUtSvce(li));
    }

    @Test
    void searchHoldingAllocation() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        List<String> li = new ArrayList<>();
        li.add("Assest");
        Map<String, Boolean> map = new HashMap<>();
        map.put("seller",true);
        Assert.assertNotNull(underTest.searchHoldingAllocation(li,map));
    }


    @Test
    void appendSqlForCoreSwitchOut() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        String [] arr = new String []{"1","2","4","5"};
        Assert.assertNotNull(underTest.appendSqlForCoreSwitchOut(arr,"SI_I","Y", new HashMap<>()));
    }

    @Test
    void getProdAltNumFromDB()throws Exception {
        Field navUrl = underTest.getClass().getDeclaredField("period");
        navUrl.setAccessible(true);
        navUrl.set(underTest,"1Y");
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(query);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(siteFeature.getStringDefaultFeature(any(String.class),any())).thenReturn("fd");

        FundSearchResultRequest request = new FundSearchResultRequest();
        List<SearchCriteria> li = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setCriteriaKey("allowSellMipProdInd");
        searchCriteria.setCriteriaValue("hhhhA");
        searchCriteria.setOperator("eq");
        li.add(searchCriteria);
        request.setDetailedCriterias(li);
        request.setChannelRestrictCode("SI_I");
        request.setCurrencyCode("HKD");
        Assert.assertNotNull(underTest.getProdAltNumFromDB(request,"quickview.bestsellers"));
        Assert.assertNotNull(underTest.getProdAltNumFromDB(request,"quickview.newfunds"));
        Assert.assertNotNull(underTest.getProdAltNumFromDB(request,"quickview.top5performance"));
        Assert.assertNotNull(underTest.getProdAltNumFromDB(request,"quickview.bestsellers"));
    }

}