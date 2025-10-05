package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryOverviewDaoImplTest {

    @InjectMocks
    private CategoryOverviewDaoImpl underTest;

    @Mock
    private Query query;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BaseDao baseDao;
    @Mock
    private Map<String, String> categoryOverviewOrderMap;
    @Test
    void getCategoryOverviewList() throws Exception{
        when(baseDao.getEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList());
        when(categoryOverviewOrderMap.get(any())).thenReturn("cat");
        Assert.assertNotNull(underTest.getCategoryOverviewList("UT", "en_US", "HK",
        "HBAP", "SI_I", "Y"));

    }
}