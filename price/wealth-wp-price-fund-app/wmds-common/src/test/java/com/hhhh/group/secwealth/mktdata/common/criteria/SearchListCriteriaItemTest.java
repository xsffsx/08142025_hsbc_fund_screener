package com.hhhh.group.secwealth.mktdata.common.criteria;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchListCriteriaItemTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new SearchListCriteriaItem().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new SearchListCriteriaItem().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new SearchListCriteriaItem());
    }

    @Test
    void testSetterAndGetter() {
        SearchListCriteriaItem obj = new SearchListCriteriaItem();

        obj.setItemKey("");
        obj.setItemValue(false);

        Assertions.assertNotNull(obj.getItemKey());
        Assertions.assertNotNull(obj.getItemValue());
    }

    @Test
    void testSetItemValue() {
        SearchListCriteriaItem obj = new SearchListCriteriaItem();
        obj.setItemValue("itemValue");
        Assertions.assertNotNull(obj);
    }

    @Test
    void testSetParent() {
        SearchListCriteriaItem obj = new SearchListCriteriaItem();
        obj.setParent("parent");
        Assertions.assertNotNull(obj);
    }
}
