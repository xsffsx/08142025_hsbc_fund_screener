package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class SummaryTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondQuote().new Summary().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondQuote().new Summary().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondQuote().new Summary());
    }

    @Test
    void testSetterAndGetter() {
        BondQuote.Summary obj = new BondQuote().new Summary();

        obj.setBuyQuoteId("test");
        obj.setSellQuoteId("test");
        obj.setBuyPrice(new BigDecimal("0"));
        obj.setSellPrice(new BigDecimal("0"));
        obj.setBuyYield(new BigDecimal("0"));
        obj.setSellYield(new BigDecimal("0"));
        obj.setBuyQuantityAvailable(new BigDecimal("0"));
        obj.setSellQuantityAvailable(new BigDecimal("0"));
        obj.setBuyMarkUp(new BigDecimal("0"));
        obj.setSellMarkUp(new BigDecimal("0"));

        Assertions.assertNotNull(obj.getBuyQuoteId());
        Assertions.assertNotNull(obj.getSellQuoteId());
        Assertions.assertNotNull(obj.getBuyPrice());
        Assertions.assertNotNull(obj.getSellPrice());
        Assertions.assertNotNull(obj.getBuyYield());
        Assertions.assertNotNull(obj.getSellYield());
        Assertions.assertNotNull(obj.getBuyQuantityAvailable());
        Assertions.assertNotNull(obj.getSellQuantityAvailable());
        Assertions.assertNotNull(obj.getBuyMarkUp());
        Assertions.assertNotNull(obj.getSellMarkUp());
    }
}
