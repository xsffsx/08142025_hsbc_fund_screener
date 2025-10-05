package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdvanceChartDaoImplTest {

    @InjectMocks
    private AdvanceChartDaoImpl underTest;

    @Mock
    private Query query;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BaseDao baseDao;
    @Test
    void getUtProdInstmList()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List<ProductKey> li = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("UT");
        productKey.setProdAltNum("U50001");
        productKey.setMarket("HK");
        li.add(productKey);
        Assert.assertNotNull(underTest.getUtProdInstmList(li, new HashMap<>()));

    }

    @Test
    void getEnableCacheProdInstmList()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        List<ProductKey> li = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setProductType("UT");
        productKey.setProdAltNum("U50001");
        productKey.setMarket("HK");
        li.add(productKey);
        Assert.assertNotNull(underTest.getEnableCacheProdInstmList());

    }
}