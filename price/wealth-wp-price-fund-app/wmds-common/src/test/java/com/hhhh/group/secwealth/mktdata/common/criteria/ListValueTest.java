package com.hhhh.group.secwealth.mktdata.common.criteria;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ListValueTest {

    private ListValue listValueUnderTest;

    @Test
    void testToString() {
        Assertions.assertNotNull(new ListValue().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new ListValue().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new ListValue());
    }

    @Test
    void testSetterAndGetter() {
        ListValue obj = new ListValue();
        
        obj.setCriteriaKey("");
        obj.setMapItems(Maps.newHashMap());
        obj.addMapItem("", "");

        Assertions.assertNotNull(obj.getCriteriaKey());
        Assertions.assertNotNull(obj.getMapItems());
    }
}
