package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TopAndBottmMufCatServiceDaoImplTest {
    @InjectMocks
    private TopAndBottmMufCatServiceDaoImpl underTest;
    @Mock
    private Query query;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BaseDao baseDao;
    @Test
    void searchTopAndBottomList()throws Exception {
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        List<String> productTypes = new ArrayList<>();
        productTypes.add("UT");
        List<String> productSubTypes = new ArrayList<>();
        productSubTypes.add("HFI");
        List<String> countryCriterias = new ArrayList<>();
        productSubTypes.add("HK");
        Assert.assertNotNull(underTest.searchTopAndBottomList(2,productTypes,productSubTypes,countryCriterias,"FD",TimeScale.fromString("1Y"),5,"SI_I","Y"));

    }

    @Test
    void searchPerformanceTableLastUpdateDate() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        Assert.assertNull(underTest.searchPerformanceTableLastUpdateDate("SI_I","Y"));
    }

}