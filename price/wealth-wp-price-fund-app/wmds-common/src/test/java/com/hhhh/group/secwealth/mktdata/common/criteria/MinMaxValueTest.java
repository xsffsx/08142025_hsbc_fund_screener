package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MinMaxValueTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new MinMaxValue().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new MinMaxValue().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new MinMaxValue());
    }

    @Test
    void testSetterAndGetter() {
        MinMaxValue obj = new MinMaxValue();

        obj.setCriteriaKey("");
        obj.setMinCriteria(new Object());
        obj.setMaxCriteria(new Object());

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertNotNull(obj.getMinCriteria());
        Assertions.assertNotNull(obj.getMaxCriteria());
    }
}
