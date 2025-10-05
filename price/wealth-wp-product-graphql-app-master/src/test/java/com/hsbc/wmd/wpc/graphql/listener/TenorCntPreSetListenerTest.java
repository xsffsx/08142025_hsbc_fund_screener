package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.ProdTypeCde;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;


public class TenorCntPreSetListenerTest {

    private final TenorCntPreSetListener tenorCntPreSetListener = new TenorCntPreSetListener();

    @Test
    public void testBeforeInsert() {
        Document document = new Document();
        document.put(Field.prodTypeCde, ProdTypeCde.BOND_CD);
        // case 1: prodMturDt is null
        tenorCntPreSetListener.beforeInsert(document);
        Assert.assertNull(document.get(Field.termRemainDayCnt));
        document.put(Field.lastUpdatedBy, "wps");
        Assert.assertNull(document.get(Field.termRemainDayCnt));
        // case 1: prodMturDt < today
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        document.put(Field.prodMturDt, calendar.getTime());
        tenorCntPreSetListener.beforeInsert(document);
        Assert.assertEquals(0L, document.get(Field.termRemainDayCnt));
        // case 2: prodLnchDt is null, prodLnchDt <= today
        calendar.add(Calendar.DAY_OF_YEAR, 50);
        document.put(Field.prodMturDt, calendar.getTime());
        tenorCntPreSetListener.beforeInsert(document);
        Assert.assertEquals(50L, document.get(Field.termRemainDayCnt));

        calendar.add(Calendar.DAY_OF_YEAR, -60);
        document.put(Field.prodLnchDt, calendar.getTime());
        tenorCntPreSetListener.beforeInsert(document);
        Assert.assertEquals(50L, document.get(Field.termRemainDayCnt));
        // case 3: prodMturDt >= prodLnchDt > today
        document.put(Field.prodMturDt, LocalDate.now().plusDays(40));
        document.put(Field.prodLnchDt, LocalDate.now().plusDays(10));
        tenorCntPreSetListener.beforeInsert(document);
        Assert.assertEquals(31L, document.get(Field.termRemainDayCnt));

        // test for exception case
        document.put(Field.prodLnchDt, new Object());
        tenorCntPreSetListener.beforeInsert(document);
        Assert.assertEquals(41L, document.get(Field.termRemainDayCnt));
    }

    @Test
    public void testBeforeUpdate() {
        Document document = new Document();
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertTrue(document.isEmpty());
        // prdInvstTnorNum
        document.put(Field.prodTypeCde, ProdTypeCde.BOND_CD);
        document.put(Field.lastUpdatedBy, "wps");
        document.put(Field.prdProdCde, "Y");
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertNull(document.getLong(Field.prdInvstTnorNum));
        // 2 years
        document.put(Field.prdProdNum, 2L);
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertEquals(730L, document.getLong(Field.prdInvstTnorNum).longValue());
        // 2 Months
        document.put(Field.prdProdCde, "M");
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertEquals(60L, document.getLong(Field.prdInvstTnorNum).longValue());
        // 2 Weeks
        document.put(Field.prdProdCde, "W");
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertEquals(14L, document.getLong(Field.prdInvstTnorNum).longValue());
        // 2 Days
        document.put(Field.prdProdCde, "D");
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertEquals(2L, document.getLong(Field.prdInvstTnorNum).longValue());
        // OTHER code - default as D
        document.put(Field.prdProdCde, "S");
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertEquals(2L, document.getLong(Field.prdInvstTnorNum).longValue());
        document.remove(Field.prdProdCde);
        tenorCntPreSetListener.beforeUpdate(document, Collections.emptyList());
        Assert.assertEquals(2L, document.getLong(Field.prdInvstTnorNum).longValue());
    }

    @Test
    public void testInterestJsonPaths() {
        Assert.assertFalse(tenorCntPreSetListener.interestJsonPaths().isEmpty());
    }
}