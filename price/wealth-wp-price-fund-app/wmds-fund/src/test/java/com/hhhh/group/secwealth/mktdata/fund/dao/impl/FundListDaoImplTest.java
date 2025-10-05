package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.criteria.Criterias;
import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FundListDaoImplTest {
    @InjectMocks
    private FundListDaoImpl underTest;

    @Mock
    private Query query;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BaseDao baseDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;
    @Mock
    Map<String, String> holdingAllocationMap;

    @Test
    void searchProductList() throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        FundListRequest request = new FundListRequest();
        List<ProductKey> li = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("UT");
        productKey.setProdAltNum("U50001");
        productKey.setMarket("HK");
        li.add(productKey);
        request.setProductKeys(li);
        Assert.assertNotNull(underTest.searchProductList(request));
    }

    @Test
    void searchHoldingAllocation() throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        when(holdingAllocationMap.get(any())).thenReturn("fd");
        FundListRequest request = new FundListRequest();
        List<Criterias> li = new ArrayList<>();
        Criterias cr = new Criterias();
        cr.setCriteriaKey("tp");
        cr.setCriteriaValue(true);
        li.add(cr);
        request.setCriterias(li);
        Assert.assertNull(underTest.searchHoldingAllocation(request));
    }

    @Test
    void searchTopTenHldgMap() throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        when(holdingAllocationMap.get(any())).thenReturn("fd");
        FundListRequest request = new FundListRequest();
        List<Criterias> li = new ArrayList<>();
        Criterias cr = new Criterias();
        cr.setCriteriaKey("tp");
        cr.setCriteriaValue(true);
        li.add(cr);
        request.setCriterias(li);
        Assert.assertNotNull(underTest.searchTopTenHldgMap(request, "fd"));

    }

    @Test
    void searchChanlFund() throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        when(holdingAllocationMap.get(any())).thenReturn("fd");
        Assert.assertNotNull(underTest.searchChanlFund("SI_I"));
    }
}