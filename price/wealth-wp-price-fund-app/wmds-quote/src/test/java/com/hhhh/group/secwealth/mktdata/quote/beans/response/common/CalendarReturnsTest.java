package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalendarReturnsTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new CalendarReturns().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new CalendarReturns().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new CalendarReturns());
    }

    @Test
    void testSetterAndGetter() {
        CalendarReturns obj = new CalendarReturns();
        
        obj.setItems(Lists.newArrayList());
        obj.setMonthEndDate("test");

        Assertions.assertNotNull(obj.getItems());
        Assertions.assertNotNull(obj.getMonthEndDate());
    }
}
