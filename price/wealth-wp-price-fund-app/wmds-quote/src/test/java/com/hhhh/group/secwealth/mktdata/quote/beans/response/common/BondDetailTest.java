package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BondDetailTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondQuote().new BondDetail().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondQuote().new BondDetail().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondQuote().new BondDetail());
    }

    @Test
    void testSetterAndGetter() {
        BondQuote.BondDetail obj = new BondQuote().new BondDetail();

        obj.setBondType("test");
        obj.setIssuer("test");
        obj.setBondCurrency("test");
        obj.setCusip("test");
        obj.setInitialIssueDate("test");
        obj.setIsin("test");
        obj.setMktStatus("test");
        obj.setMktBidDiscountMargin(new BigDecimal("0"));
        obj.setMktOfferDiscountMargin(new BigDecimal("0"));
        obj.setTotalOutstanding(new BigDecimal("0"));
        obj.setInstrumentStatus("test");

        Assertions.assertNotNull(obj.getBondType());
        Assertions.assertNotNull(obj.getIssuer());
        Assertions.assertNotNull(obj.getBondCurrency());
        Assertions.assertNotNull(obj.getCusip());
        Assertions.assertNotNull(obj.getInitialIssueDate());
        Assertions.assertNotNull(obj.getIsin());
        Assertions.assertNotNull(obj.getMktStatus());
        Assertions.assertNotNull(obj.getMktBidDiscountMargin());
        Assertions.assertNotNull(obj.getMktOfferDiscountMargin());
        Assertions.assertNotNull(obj.getTotalOutstanding());
        Assertions.assertNotNull(obj.getInstrumentStatus());
    }
}
