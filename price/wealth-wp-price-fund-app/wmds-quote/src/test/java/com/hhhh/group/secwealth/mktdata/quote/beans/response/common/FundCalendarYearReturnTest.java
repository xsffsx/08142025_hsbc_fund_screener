package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class FundCalendarYearReturnTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new FundCalendarYearReturn().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new FundCalendarYearReturn().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new FundCalendarYearReturn());
    }

    @Test
    void testSetterAndGetter() {
        FundCalendarYearReturn obj = new FundCalendarYearReturn();

        obj.setYear(new BigDecimal("0"));
        obj.setAnnualCalendarYearReturn(new BigDecimal("0"));

        Assertions.assertNotNull(obj.getYear());
        Assertions.assertNotNull(obj.getAnnualCalendarYearReturn());
    }
}
