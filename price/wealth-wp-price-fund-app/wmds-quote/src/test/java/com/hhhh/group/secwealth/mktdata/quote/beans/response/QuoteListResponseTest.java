package com.hhhh.group.secwealth.mktdata.quote.beans.response;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.ExchangeAgreement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class QuoteListResponseTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new QuoteListResponse().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new QuoteListResponse().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new QuoteListResponse());
    }

    @Test
    void testSetterAndGetter() {
        QuoteListResponse obj = new QuoteListResponse();

        obj.setRequestType("");
        obj.setRemainingQuote(new BigDecimal("0"));
        obj.setTotalQuote(new BigDecimal("0"));
        obj.setPriceQuotes(Lists.newArrayList());
        obj.setEntityUpdatedTime("");
        obj.setEntityTimezone("");
        obj.setInDaylightTime("");
        obj.setSignedExchanges(new ExchangeAgreement[]{new ExchangeAgreement()});
        obj.setUnSignedExchanges(new ExchangeAgreement[]{new ExchangeAgreement()});
        obj.setProdAltNumSegs(new ProdAltNumSeg[]{new ProdAltNumSeg()});

        Assertions.assertNotNull(obj.getRequestType());
        Assertions.assertNotNull(obj.getRemainingQuote());
        Assertions.assertNotNull(obj.getTotalQuote());
        Assertions.assertNotNull(obj.getPriceQuotes());
        Assertions.assertNotNull(obj.getEntityUpdatedTime());
        Assertions.assertNotNull(obj.getEntityTimezone());
        Assertions.assertNotNull(obj.getInDaylightTime());
        Assertions.assertNotNull(obj.getSignedExchanges());
        Assertions.assertNotNull(obj.getUnSignedExchanges());
        Assertions.assertNotNull(obj.getProdAltNumSegs());
    }
}
