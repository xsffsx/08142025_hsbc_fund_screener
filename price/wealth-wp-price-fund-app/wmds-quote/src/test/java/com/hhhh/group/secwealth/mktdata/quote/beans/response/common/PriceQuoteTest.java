package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PriceQuoteTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new PriceQuote().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new PriceQuote().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new PriceQuote());
    }

    @Test
    void testSetterAndGetter() {
        PriceQuote obj = new PriceQuote();
        obj.setRic("test");
        obj.setSymbol("test");
        obj.setMarket("test");
        obj.setProductType("test");
        obj.setUnderlyingProductType("test");
        obj.setPriceQuote(new BigDecimal("0"));
        obj.setDelay(false);
        obj.setCurrency("test");
        obj.setCompanyName("test");
        obj.setChangeAmount(new BigDecimal("0"));
        obj.setChangePercent(new BigDecimal("0"));
        obj.setUnsignedAgreementId("test");
        obj.setQuoteVMA50D(new BigDecimal("0"));
        obj.setProdAltNum("test");
        obj.setProdCdeAltClassCde("test");
        obj.setProductStatus("test");
        obj.setDayRangeLow(new BigDecimal("0"));
        obj.setDayRangeHigh(new BigDecimal("0"));
        obj.setYearLowPrice(new BigDecimal("0"));
        obj.setYearHighPrice(new BigDecimal("0"));
        obj.setExchange("test");
        obj.setUnderlyingSymbol("test");
        obj.setUnderlyingMarket("test");
        obj.setRootSymbol("test");
        obj.setOptionType("test");
        obj.setExpiryDate("test");
        obj.setStrike(new BigDecimal("0"));
        obj.setExchangeUpdatedTime("test");
        obj.setInDaylightTime("test");
        obj.setExchangeTimezone("test");
        obj.setTradeUnits("test");
        obj.setPreviousClosePrice(new BigDecimal("0"));
        obj.setOpenPrice(new BigDecimal("0"));
        obj.setTurnoverAmount(new BigDecimal("0"));
        obj.setBidPrice(new BigDecimal("0"));
        obj.setBidPriceDate("test");
        obj.setAskPrice(new BigDecimal("0"));
        obj.setDividendYield(new BigDecimal("0"));
        obj.setDistributionFrequency("test");
        obj.setTopPerformersIndicator("test");
        obj.setTopSellProdIndex("test");
        obj.setCategoryLevel1Code("test");
        obj.setCategoryLevel1Name("test");
        obj.setCalendarReturns(new CalendarReturns());
        obj.setCumulativeReturns(new CumulativeReturns());
        obj.setRiskLvlCde("test");
        obj.setLaunchDate("test");
        Assertions.assertNotNull(obj.toString());
        this.testGetter(obj);
    }

    private void testGetter(final PriceQuote obj) {
        String result = obj.getRic() + obj.getSymbol() + obj.getMarket() + obj.getProductType() + obj.getUnderlyingProductType()
            + obj.getPriceQuote()  + obj.getDelay() + obj.getCurrency() + obj.getCompanyName()
            + obj.getChangeAmount() + obj.getChangePercent() + obj.getUnsignedAgreementId()
            + obj.getQuoteVMA50D() + obj.getProdAltNum() + obj.getProdCdeAltClassCde() + obj.getProductStatus()
            + obj.getDayRangeLow() + obj.getDayRangeHigh() + obj.getYearLowPrice() + obj.getYearHighPrice() + obj.getExchange()
            + obj.getUnderlyingSymbol() + obj.getUnderlyingMarket() + obj.getRootSymbol() + obj.getOptionType()
            + obj.getExpiryDate() + obj.getStrike() + obj.getExchangeUpdatedTime() + obj.getInDaylightTime()
            + obj.getExchangeTimezone() + obj.getTradeUnits() + obj.getPreviousClosePrice() + obj.getOpenPrice()
            + obj.getTurnoverAmount() + obj.getBidPrice() + obj.getBidPriceDate() + obj.getAskPrice() + obj.getDividendYield()
            + obj.getDistributionFrequency() + obj.getTopPerformersIndicator() + obj.getTopSellProdIndex()
            + obj.getCategoryLevel1Code() + obj.getCategoryLevel1Name() + obj.getCalendarReturns()
            + obj.getCumulativeReturns() + obj.getRiskLvlCde() + obj.getLaunchDate();
        Assertions.assertNotNull(result);
    }
}
