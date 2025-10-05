package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class IndexQuoteTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new IndexQuote().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new IndexQuote().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new IndexQuote());
    }

    @Test
    void testSetterAndGetter() {
        IndexQuote obj = new IndexQuote();
        obj.setSymbol("test");
        obj.setQuote(new BigDecimal("0"));
        obj.setName("test");
        obj.setChange(new BigDecimal("0"));
        obj.setChangePercentage(new BigDecimal("0"));

        Assertions.assertNotNull(obj.getSymbol());
        Assertions.assertNotNull(obj.getQuote());
        Assertions.assertNotNull(obj.getName());
        Assertions.assertNotNull(obj.getChange());
        Assertions.assertNotNull(obj.getChangePercentage());
    }
}
