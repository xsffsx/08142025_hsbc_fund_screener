package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HoldingsTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new Holdings().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new Holdings().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new Holdings());
    }

    @Test
    void testSetterAndGetter() {
        Holdings obj = new Holdings();
        obj.setCriteriaKey("");
        obj.setCriteriaValue(false);
        obj.setTop(0);
        obj.setOthers(false);

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertFalse(obj.getCriteriaValue());
        Assertions.assertEquals(0, obj.getTop());
        Assertions.assertFalse(obj.getOthers());
    }
}
