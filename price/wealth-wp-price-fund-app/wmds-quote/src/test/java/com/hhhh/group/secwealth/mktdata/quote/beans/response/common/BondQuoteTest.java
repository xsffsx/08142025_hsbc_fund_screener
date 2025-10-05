package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BondQuoteTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondQuote().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondQuote().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondQuote());
    }

    @Test
    void testSetterAndGetter() {
        BondQuote obj = new BondQuote();

        obj.setSummary(obj.getSummary());
        obj.setBondDetails(obj.bondDetails);
        obj.setCouponAccuredInterest(obj.getCouponAccuredInterest());
        obj.setMaturityFeatures(obj.getMaturityFeatures());
        obj.setRiskMeasures(obj.getRiskMeasures());
        obj.setTradingInfo(obj.getTradingInfo());

        Assertions.assertNull(obj.getSummary());
        Assertions.assertNull(obj.getBondDetails());
        Assertions.assertNull(obj.getCouponAccuredInterest());
        Assertions.assertNull(obj.getMaturityFeatures());
        Assertions.assertNull(obj.getRiskMeasures());
        Assertions.assertNull(obj.getTradingInfo());
    }
}
