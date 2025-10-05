package com.hhhh.group.secwealth.mktdata.common.criteria;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchRangeCriteriaValueTest {

    private SearchRangeCriteriaValue searchRangeCriteriaValueUnderTest;

    @Test
    void testToString() {
        Assertions.assertNotNull(new SearchRangeCriteriaValue().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SearchRangeCriteriaValue().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SearchRangeCriteriaValue());
    }

    @Test
    void testSetterAndGetter() {
        SearchRangeCriteriaValue obj = new SearchRangeCriteriaValue();
        obj.setCriteriaKey("");
        obj.setCriteriaValues(Lists.newArrayList());

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertNotNull(obj.getCriteriaValues());
    }
}
