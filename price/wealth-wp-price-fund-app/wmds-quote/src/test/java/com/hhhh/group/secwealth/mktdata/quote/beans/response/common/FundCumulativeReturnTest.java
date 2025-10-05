package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class FundCumulativeReturnTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new FundCumulativeReturn().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new FundCumulativeReturn().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new FundCumulativeReturn());
    }

    @Test
    void testSetterAndGetter() {
        FundCumulativeReturn obj = new FundCumulativeReturn();
        obj.setPeriod("test");
        obj.setTrailingTotalReturn(new BigDecimal("0"));
        obj.setDailyPerformanceNAV(new BigDecimal("0"));

        Assertions.assertNotNull(obj.getPeriod());
        Assertions.assertNotNull(obj.getTrailingTotalReturn());
        Assertions.assertNotNull(obj.getDailyPerformanceNAV());
    }
}
