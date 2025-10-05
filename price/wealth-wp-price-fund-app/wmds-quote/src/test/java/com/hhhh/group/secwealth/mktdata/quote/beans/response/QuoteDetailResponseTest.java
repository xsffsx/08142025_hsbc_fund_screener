package com.hhhh.group.secwealth.mktdata.quote.beans.response;

import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.BidAskQuote;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.IndexQuote;
import com.hhhh.group.secwealth.mktdata.quote.beans.response.common.QuoteData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class QuoteDetailResponseTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new QuoteDetailResponse().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new QuoteDetailResponse().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new QuoteDetailResponse());
    }

    @Test
    void testSetterAndGetter() {
        QuoteDetailResponse obj = new QuoteDetailResponse();

        obj.setRequestType("test");
        obj.setRemainingQuote(new BigDecimal("0"));
        obj.setTotalQuote(new BigDecimal("0"));
        obj.setIndexQuotes(new IndexQuote[]{new IndexQuote()});
        obj.setBidAskQuotes(new BidAskQuote[]{new BidAskQuote()});
        obj.setPriceQuote(new QuoteData());
        obj.setEntityUpdatedTime("test");
        obj.setProdAltNumSegs(Lists.newArrayList());

        Assertions.assertNotNull(obj.getRequestType());
        Assertions.assertNotNull(obj.getRemainingQuote());
        Assertions.assertNotNull(obj.getTotalQuote());
        Assertions.assertNotNull(obj.getIndexQuotes());
        Assertions.assertNotNull(obj.getBidAskQuotes());
        Assertions.assertNotNull(obj.getPriceQuote());
        Assertions.assertNotNull(obj.getEntityUpdatedTime());
        Assertions.assertNotNull(obj.getProdAltNumSegs());
    }
}
