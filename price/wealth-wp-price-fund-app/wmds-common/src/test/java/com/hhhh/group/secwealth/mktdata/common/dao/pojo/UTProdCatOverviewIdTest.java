package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UTProdCatOverviewIdTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new UTProdCatOverviewId().toString());
    }

    @Test
    void testHashCode() {
        UTProdCatOverviewId obj = new UTProdCatOverviewId();
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setBatchId(0L);
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
        obj.setProductId(0);
        Assertions.assertNotEquals(new Object().hashCode(), obj.hashCode());
    }

    @Test
    void testNotEquals() {
        UTProdCatOverviewId obj = new UTProdCatOverviewId();
        Assertions.assertNotEquals(new Object(), obj);
        obj.setBatchId(0L);
        Assertions.assertNotEquals(new Object(), obj);
        obj.setProductId(0);
        Assertions.assertNotEquals(new Object(), obj);
    }

    @Test
    void testSetterAndGetter() {
        UTProdCatOverviewId obj = new UTProdCatOverviewId();
        obj.setBatchId(0L);
        obj.setProductId(0);

        Assertions.assertEquals(0, obj.getBatchId());
        Assertions.assertEquals(0, obj.getProductId());
    }
}
