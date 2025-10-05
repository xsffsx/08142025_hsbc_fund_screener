package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareIndexRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundHoldingsDiversifiRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FundHoldingsDiversifiDaoImplTest {
    @InjectMocks
    private FundHoldingsDiversifiDaoImpl underTest;

    @Mock
    private Query query;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BaseDao baseDao;
    @Mock
    private LocaleMappingUtil localeMappingUtil;

    @Mock
    private FundCompareIndexRequest request;
    @Test
    void searchProductId()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List<ProductKey> li = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("UT");
        productKey.setProdAltNum("U50001");
        productKey.setMarket("HK");
        li.add(productKey);
        FundHoldingsDiversifiRequest request = new FundHoldingsDiversifiRequest();
        request.setProductKeys(li);
        Assert.assertNotNull(underTest.searchProductId(request));

    }

    @Test
    void searchHoldingDiversification()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List<Integer> li = new ArrayList<>();
        li.add(4);
        Assert.assertNotNull(underTest.searchHoldingDiversification(li));
    }

    @Test
    void searchAllocation()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List<Integer> li = new ArrayList<>();
        li.add(4);
        Assert.assertNotNull(underTest.searchAllocation(li,"assest"));

    }

}