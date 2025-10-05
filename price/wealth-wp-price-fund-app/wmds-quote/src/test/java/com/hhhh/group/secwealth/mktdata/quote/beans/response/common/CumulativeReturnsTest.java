package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CumulativeReturnsTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new CumulativeReturns().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new CumulativeReturns().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new CumulativeReturns());
    }

    @Test
    void testSetterAndGetter() {
        CumulativeReturns obj = new CumulativeReturns();
        
        obj.setItems(Lists.newArrayList());
        obj.setMonthEndDate("test");
        obj.setDayEndDate("test");

        Assertions.assertNotNull(obj.getItems());
        Assertions.assertNotNull(obj.getMonthEndDate());
        Assertions.assertNotNull(obj.getDayEndDate());
    }
}
