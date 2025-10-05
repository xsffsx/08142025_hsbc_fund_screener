package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CommodityTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new Commodity().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new Commodity().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new Commodity());
    }

    @Test
    void testSetterAndGetter() {
        Commodity obj = new Commodity();

        obj.setDayBidLow(new BigDecimal("0"));
        obj.setDayBidHigh(new BigDecimal("0"));
        obj.setDayAskLow(new BigDecimal("0"));
        obj.setDayAskHigh(new BigDecimal("0"));
        obj.setPreviousCloseBidPrice(new BigDecimal("0"));
        obj.setPreviousCloseAskPrice(new BigDecimal("0"));

        Assertions.assertNotNull(obj.getDayBidLow());
        Assertions.assertNotNull(obj.getDayBidHigh());
        Assertions.assertNotNull(obj.getDayAskLow());
        Assertions.assertNotNull(obj.getDayAskHigh());
        Assertions.assertNotNull(obj.getPreviousCloseBidPrice());
        Assertions.assertNotNull(obj.getPreviousCloseAskPrice());
    }
}
