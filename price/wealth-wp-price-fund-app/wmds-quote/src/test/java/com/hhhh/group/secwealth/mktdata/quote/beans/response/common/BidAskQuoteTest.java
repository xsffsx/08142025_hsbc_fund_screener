package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BidAskQuoteTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BidAskQuote().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BidAskQuote().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BidAskQuote());
    }

    @Test
    void testSetterAndGetter() {
        BidAskQuote obj = new BidAskQuote();

        obj.setBidOrder(new BigDecimal("0"));
        obj.setBidBroker(new BigDecimal("0"));
        obj.setBidPrice(new BigDecimal("0"));
        obj.setBidSize(new BigDecimal("0"));
        obj.setAskOrder(new BigDecimal("0"));
        obj.setAskBroker(new BigDecimal("0"));
        obj.setAskPrice(new BigDecimal("0"));
        obj.setAskSize(new BigDecimal("0"));

        Assertions.assertNotNull(obj.getBidOrder());
        Assertions.assertNotNull(obj.getBidBroker());
        Assertions.assertNotNull(obj.getBidPrice());
        Assertions.assertNotNull(obj.getBidSize());
        Assertions.assertNotNull(obj.getAskOrder());
        Assertions.assertNotNull(obj.getAskBroker());
        Assertions.assertNotNull(obj.getAskPrice());
        Assertions.assertNotNull(obj.getAskSize());
    }
}
