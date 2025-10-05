package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MaturityFeatureTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondQuote().new MaturityFeature().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondQuote().new MaturityFeature().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondQuote().new MaturityFeature());
    }

    @Test
    void testSetterAndGetter() {
        BondQuote.MaturityFeature obj = new BondQuote().new MaturityFeature();

        obj.setActualMaturity("");
        obj.setCallable("");
        obj.setEffectiveMaturity("");
        obj.setComments("");

        Assertions.assertNotNull(obj.getActualMaturity());
        Assertions.assertNotNull(obj.getCallable());
        Assertions.assertNotNull(obj.getEffectiveMaturity());
        Assertions.assertNotNull(obj.getComments());
    }
}
