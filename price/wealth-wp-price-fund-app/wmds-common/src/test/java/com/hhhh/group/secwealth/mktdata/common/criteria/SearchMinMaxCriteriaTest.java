package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SearchMinMaxCriteriaTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new SearchMinMaxCriteria().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SearchMinMaxCriteria().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SearchMinMaxCriteria());
    }

    @Test
    void testSetterAndGetter() {
        SearchMinMaxCriteria obj = new SearchMinMaxCriteria();

        obj.setCriteriaKey("");
        obj.setMinimum(new Object());
        obj.setMaximum(new Object());

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertNotNull(obj.getMinimum());
        Assertions.assertNotNull(obj.getMaximum());
    }

    @Test
    void testGetFromContext() {
        Map<String, Object> map = new HashMap<>();
        List<SearchMinMaxCriteria> result = SearchMinMaxCriteria.getFromContext(map, "requestParam",
                "convertorKey", false);
        Assertions.assertNull(result);
    }
}
