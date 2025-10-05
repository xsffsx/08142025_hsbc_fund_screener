package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtSvceIdTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new UtSvceId().toString());
    }

    @Test
    void testHashCode() {
        UtSvceId obj = new UtSvceId();
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setBatchId(0L);
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setPerformanceId("");
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setFundSvcCde("");
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
    }

    @Test
    void testNotEquals() {
        UtSvceId obj = new UtSvceId();
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setBatchId(0L);
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setPerformanceId("");
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setFundSvcCde("");
        Assertions.assertNotEquals(new Object(), obj);
    }

    @Test
    void testSetterAndGetter() {
        UtSvceId obj = new UtSvceId();
        obj.setBatchId(0L);
        obj.setPerformanceId("");
        obj.setFundSvcCde("");

        Assertions.assertEquals(0, obj.getBatchId());
        Assertions.assertNotNull(obj.getPerformanceId());
        Assertions.assertNotNull(obj.getFundSvcCde());
    }
}
