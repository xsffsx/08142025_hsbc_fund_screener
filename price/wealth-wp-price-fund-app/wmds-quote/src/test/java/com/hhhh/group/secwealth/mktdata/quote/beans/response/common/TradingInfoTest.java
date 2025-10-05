package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TradingInfoTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new BondQuote().new TradingInfo().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new BondQuote().new TradingInfo().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new BondQuote().new TradingInfo());
    }

    @Test
    void testSetterAndGetter() {
        BondQuote.TradingInfo obj = new BondQuote().new TradingInfo();
        obj.setMinimumTradeIncrementalSell(0.0D);
        obj.setMinimumTradeAmountSell(0.0D);
        obj.setMinimumTradeIncrementalBuy(0.0D);
        obj.setMinimumTradeAmountBuy(0.0D);
        obj.setSettlementCalendar("test");
        obj.setStandardSettlement("test");

        Assertions.assertNotNull(obj.getMinimumTradeIncrementalSell());
        Assertions.assertNotNull(obj.getMinimumTradeAmountSell());
        Assertions.assertNotNull(obj.getMinimumTradeIncrementalBuy());
        Assertions.assertNotNull(obj.getMinimumTradeAmountBuy());
        Assertions.assertNotNull(obj.getSettlementCalendar());
        Assertions.assertNotNull(obj.getStandardSettlement());
    }
}
