package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SortCriteriaValueTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new SortCriteriaValue().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SortCriteriaValue().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SortCriteriaValue());
    }

    @Test
    void testSetterAndGetter() {
        SortCriteriaValue obj = new SortCriteriaValue();
        obj.setSortKey("");
        obj.setSortOrder("");

        Assertions.assertNotNull(obj.getSortKey());
        Assertions.assertNotNull(obj.getSortOrder());
    }
}
