package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CouponAccruedInterestTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondQuote().new CouponAccruedInterest().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondQuote().new CouponAccruedInterest().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondQuote().new CouponAccruedInterest());
    }

    @Test
    void testSetterAndGetter() {
        BondQuote.CouponAccruedInterest obj = new BondQuote().new CouponAccruedInterest();
        obj.setAccuredInterest(0.0D);
        obj.setCouponPaymentFrequency("test");
        obj.setCouponPaidIn("test");
        obj.setCouponRate(0.0D);
        obj.setNextCouponPayment(new BigDecimal("0"));

        Assertions.assertNotNull(obj.getAccuredInterest());
        Assertions.assertNotNull(obj.getCouponPaymentFrequency());
        Assertions.assertNotNull(obj.getCouponPaidIn());
        Assertions.assertNotNull(obj.getCouponRate());
        Assertions.assertNotNull(obj.getNextCouponPayment());
    }
}
