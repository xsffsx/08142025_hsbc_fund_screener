package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.DetailedCriteriaUtil;
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
class PerformanceReturnDaoImplTest {

    @InjectMocks
    private PerformanceReturnDaoImpl underTest;

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


    @Test
    void searchProductList() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        PerformanceReturnRequest request = new PerformanceReturnRequest();
        List<ProductKey> productKeys = new ArrayList<>();
        ProductKey productKey = new ProductKey();
        productKey.setMarket("HK");
        productKey.setProdAltNum("U50001");
        productKey.setProdCdeAltClassCde("M");
        productKeys.add(productKey);
        request.setProductKeys(productKeys);
        Assert.assertNotNull(underTest.searchProductList(request));

    }
}