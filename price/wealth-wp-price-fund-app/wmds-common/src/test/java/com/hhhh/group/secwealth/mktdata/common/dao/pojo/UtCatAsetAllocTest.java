package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

class UtCatAsetAllocTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new UtCatAsetAlloc().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new UtCatAsetAlloc().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new UtCatAsetAlloc());
    }

    @Test
    void testSetterAndGetter() {
        UtCatAsetAlloc obj = new UtCatAsetAlloc();
        obj.setUtCatAsetAllocId(new UtCatAsetAllocId());
        obj.setFundAllocation(new BigDecimal("0"));
        obj.setProdnName1("");
        obj.setProdnName2("");
        obj.setProdnName3("");
        obj.setIsFundShortPosition("");
        obj.setCategoryAllocation(new BigDecimal("0"));
        obj.setIsCategoryShortPosition("");
        obj.setUpdatedOn(new Date());
        obj.setUpdatedBy("");

        Assertions.assertNotNull(obj.getUtCatAsetAllocId());
        Assertions.assertNotNull(obj.getFundAllocation());
        Assertions.assertNotNull(obj.getProdnName1());
        Assertions.assertNotNull(obj.getProdnName2());
        Assertions.assertNotNull(obj.getProdnName3());
        Assertions.assertNotNull(obj.getIsFundShortPosition());
        Assertions.assertNotNull(obj.getCategoryAllocation());
        Assertions.assertNotNull(obj.getIsCategoryShortPosition());
        Assertions.assertNotNull(obj.getUpdatedOn());
        Assertions.assertNotNull(obj.getUpdatedBy());
    }
}
