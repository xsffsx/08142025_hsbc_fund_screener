package com.hhhh.group.secwealth.mktdata.quote.dao.impl;
import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundQuoteDaoImplTest {
    @Mock
    private BaseDao mockBaseDao;

    @InjectMocks
    private FundQuoteDaoImpl fundQuoteDaoImplUnderTest;
    @Test
    void search() throws Exception {
        EntityManager mockEntityManager = mock(EntityManager.class);
        Query mockQuery = mock(Query.class);
        List<UtProdInstm> utProdInstmList = new ArrayList<>();
        UtProdInstm instm = new UtProdInstm();
        utProdInstmList.add(instm);
        final List<InternalProductKey> ipkList = Collections.singletonList(new InternalProductKey("countryCode", "groupMember",
                "countryTradableCde", "productType", "prodCdeAltClassCde", "prodAltNum"));

        when(this.mockBaseDao.getEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.createQuery(any(String.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(utProdInstmList);

        List<UtProdInstm> result = this.fundQuoteDaoImplUnderTest.search(ipkList, new HashMap<>());
        Assertions.assertNotNull(result);
    }

    @Test
    void testSearch_2() throws Exception {
        List<UtProdInstm> result = this.fundQuoteDaoImplUnderTest.search(null, new HashMap<>());
        Assertions.assertNull(result);
    }

    @Test
    void testSearch_3() throws Exception {
        EntityManager mockEntityManager = mock(EntityManager.class);
        Query mockQuery = mock(Query.class);
        List<UtProdInstm> utProdInstmList = new ArrayList<>();
        UtProdInstm instm = new UtProdInstm();
        utProdInstmList.add(instm);
        final List<InternalProductKey> ipkList = Arrays.asList(
                new InternalProductKey("countryCode", "groupMember", "countryTradableCde", "productType", "prodCdeAltClassCde",
                        "prodAltNum"),
                new InternalProductKey("countryCode", "groupMember", "countryTradableCde", "productType", "prodCdeAltClassCde",
                        "prodAltNum"),
                new InternalProductKey("countryCode", "groupMember", "countryTradableCde", "productType", "prodCdeAltClassCde",
                        "prodAltNum"));

        when(this.mockBaseDao.getEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.createQuery(any(String.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(utProdInstmList);

        List<UtProdInstm> result = this.fundQuoteDaoImplUnderTest.search(ipkList, new HashMap<>());
        Assertions.assertNotNull(result);
    }

    @Test
    void testSearchChanlFund() throws Exception {
        List<Object[]> list = new ArrayList<>();
        Object[] objArray = new Object[] {"1", "test1", "test2"};
        list.add(objArray);
        EntityManager mockEntityManager = mock(EntityManager.class);
        Query mockQuery = mock(Query.class);
        when(this.mockBaseDao.getEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.createQuery(any(String.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(list);
        Map<Integer, List<String>> result = this.fundQuoteDaoImplUnderTest.searchChanlFund("chanlRestCde");
        Assertions.assertNotNull(result);
    }

}