package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchCriteriaTest {
    @Test
    void testToString() {
        Assertions.assertNotNull(new SearchCriteria().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SearchCriteria().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SearchCriteria());
    }

    @Test
    void testSetterAndGetter() {
        SearchCriteria obj = new SearchCriteria();
        obj.setCriteriaKey("");
        obj.setCriteriaValue("");
        obj.setOperator("");

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertNotNull(obj.getCriteriaValue());
        Assertions.assertNotNull(obj.getOperator());
    }
}
