package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

class UTProdCatOverviewTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new UTProdCatOverview().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new UTProdCatOverview().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new UTProdCatOverview());
    }

    @Test
    void testSetterAndGetter() {
        UTProdCatOverview obj = new UTProdCatOverview();
        obj.setUtProdCatOverviewId(new UTProdCatOverviewId());
        obj.setProductType("");
        obj.setCategoryCode("");
        obj.setCategorySequencenNum(0);
        obj.setCategoryName1("");
        obj.setCategoryName2("");
        obj.setCategoryName3("");
        obj.setCategoryLevel1Code("");
        obj.setCategoryLevel1SequencenNum(0);
        obj.setCategoryLevel1Name1("");
        obj.setCategoryLevel1Name2("");
        obj.setCategoryLevel1Name3("");
        obj.setReturn1yr(new BigDecimal("0"));
        obj.setReturn3yr(new BigDecimal("0"));
        obj.setReturn5yr(new BigDecimal("0"));
        obj.setReturn10yr(new BigDecimal("0"));
        obj.setStdDev3Yr(new BigDecimal("0"));
        obj.setRestrOnlScribInd("");
        obj.setUpdatedBy("");
        obj.setUpdaetdOn(new Date());

        Assertions.assertNotNull(obj.getUtProdCatOverviewId());
        Assertions.assertNotNull(obj.getProductType());
        Assertions.assertNotNull(obj.getCategoryCode());
        Assertions.assertEquals(0, obj.getCategorySequencenNum());
        Assertions.assertNotNull(obj.getCategoryName1());
        Assertions.assertNotNull(obj.getCategoryName2());
        Assertions.assertNotNull(obj.getCategoryName3());
        Assertions.assertNotNull(obj.getCategoryLevel1Code());
        Assertions.assertEquals(0, obj.getCategoryLevel1SequencenNum());
        Assertions.assertNotNull(obj.getCategoryLevel1Name1());
        Assertions.assertNotNull(obj.getCategoryLevel1Name2());
        Assertions.assertNotNull(obj.getCategoryLevel1Name3());
        Assertions.assertNotNull(obj.getReturn1yr());
        Assertions.assertNotNull(obj.getReturn3yr());
        Assertions.assertNotNull(obj.getReturn5yr());
        Assertions.assertNotNull(obj.getReturn10yr());
        Assertions.assertNotNull(obj.getStdDev3Yr());
        Assertions.assertNotNull(obj.getRestrOnlScribInd());
        Assertions.assertNotNull(obj.getUpdatedBy());
        Assertions.assertNotNull(obj.getUpdaetdOn());
    }
}
