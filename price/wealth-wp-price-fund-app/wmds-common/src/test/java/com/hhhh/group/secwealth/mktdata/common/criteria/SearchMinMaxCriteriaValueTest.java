package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class SearchMinMaxCriteriaValueTest {

    private SearchMinMaxCriteriaValue searchMinMaxCriteriaValueUnderTest;

    @Test
    void testToString() {
        Assertions.assertNotNull(new SearchMinMaxCriteriaValue().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SearchMinMaxCriteriaValue().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SearchMinMaxCriteriaValue());
    }

    @Test
    void testSetterAndGetter() {
        SearchMinMaxCriteriaValue obj = new SearchMinMaxCriteriaValue();
        obj.setMinOperator("");
        obj.setMinCriteriaLimit(BigDecimal.ONE);
        obj.setMaxOperator("");
        obj.setMaxCriteriaLimit(BigDecimal.ONE);

        Assertions.assertNotNull(obj.getMinOperator());
        Assertions.assertNotNull(obj.getMinCriteriaLimit());
        Assertions.assertNotNull(obj.getMaxOperator());
        Assertions.assertNotNull(obj.getMaxCriteriaLimit());
    }
}
