package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExchangeAgreementTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new ExchangeAgreement().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new ExchangeAgreement().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new ExchangeAgreement());
    }

    @Test
    void testSetterAndGetter() {
        ExchangeAgreement obj = new ExchangeAgreement();
        obj.setExchange("test");
        obj.setAgreementId("test");

        Assertions.assertNotNull(obj.getExchange());
        Assertions.assertNotNull(obj.getAgreementId());
    }
}
