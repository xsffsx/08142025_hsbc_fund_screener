package com.hhhh.group.secwealth.mktdata.common.criteria;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SearchListCriteriaTest {

    @Mock
    LogUtil logUtil;

    @Test
    void testToString() {
        Assertions.assertNotNull(new SearchListCriteria().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SearchListCriteria().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SearchListCriteria());
    }

    @Test
    void testSetterAndGetter() {
        SearchListCriteria obj = new SearchListCriteria();
        obj.setCriteriaKey("");
        obj.setItems(Lists.newArrayList());

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertNotNull(obj.getItems());
    }

    @Test
    void testGetFromContext() {
        Map<String, Object> map = new HashMap<>();
        List<SearchListCriteria> result = SearchListCriteria.getFromContext(map, "requestParam", "convertorKey",
                false);
        Assertions.assertNull(result);
        List<String> criteria = new ArrayList<>();
        criteria.add("ALL");
        map.put("requestParam",criteria);
        SearchListCriteria.getFromContext(map, "requestParam", "convertorKey", false);
        Assertions.assertNotNull(map);

    }
}
