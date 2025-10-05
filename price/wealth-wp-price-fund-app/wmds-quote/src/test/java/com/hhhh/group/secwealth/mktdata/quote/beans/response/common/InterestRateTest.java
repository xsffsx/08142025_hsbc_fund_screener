package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class InterestRateTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new InterestRate().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new InterestRate().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new InterestRate());
    }

    @Test
    void testSetterAndGetter() {
        InterestRate obj = new InterestRate();
        obj.setInterestRateAmount(new BigDecimal("0"));
        obj.setInterestRateChangePercent(new BigDecimal("0"));
        obj.setWeekagoRateAmount(new BigDecimal("0"));
        obj.setTenor("test");

        Assertions.assertNotNull(obj.getInterestRateAmount());
        Assertions.assertNotNull(obj.getInterestRateChangePercent());
        Assertions.assertNotNull(obj.getWeekagoRateAmount());
        Assertions.assertNotNull(obj.getTenor());
    }
}
