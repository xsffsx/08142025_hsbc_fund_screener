package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class RiskMeasureTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondQuote().new RiskMeasure().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondQuote().new RiskMeasure().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondQuote().new RiskMeasure());
    }

    @Test
    void testSetterAndGetter() {
        BondQuote.RiskMeasure obj = new BondQuote().new RiskMeasure();
        obj.setEffectiveDuration(new BigDecimal("0"));
        obj.setRiskLvlCde("test");
        obj.setRatings(Lists.newArrayList());

        Assertions.assertNotNull(obj.getEffectiveDuration());
        Assertions.assertNotNull(obj.getRiskLvlCde());
        Assertions.assertNotNull(obj.getRatings());
    }
}
