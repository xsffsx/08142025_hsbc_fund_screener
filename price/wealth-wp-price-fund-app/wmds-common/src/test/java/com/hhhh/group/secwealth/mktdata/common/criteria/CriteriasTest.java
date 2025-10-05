package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CriteriasTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new Criterias().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new Criterias().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new Criterias());
    }

    @Test
    void testSetterAndGetter() {
        Criterias obj = new Criterias();

        obj.setCriteriaKey("");
        obj.setCriteriaValue(false);

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertFalse(obj.getCriteriaValue());
    }
}
