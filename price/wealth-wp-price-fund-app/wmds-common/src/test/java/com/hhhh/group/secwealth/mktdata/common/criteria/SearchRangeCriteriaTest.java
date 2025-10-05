package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SearchRangeCriteriaTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new SearchRangeCriteria().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SearchRangeCriteria().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SearchRangeCriteria());
    }

    @Test
    void testSetterAndGetter() {
        SearchRangeCriteria obj = new SearchRangeCriteria();

        obj.setMin(new SearchCriteria());
        obj.setMax(new SearchCriteria());

        Assertions.assertNotNull(obj.getMin());
        Assertions.assertNotNull(obj.getMax());
    }

    @Test
    void testGetFromContext() {
        Map<String, Object> map = new HashMap<>();
        List<List<SearchRangeCriteria>> result = SearchRangeCriteria.getFromContext(map, "requestParam",
                "convertorKey", false);
        Assertions.assertNull(result);
    }
}
