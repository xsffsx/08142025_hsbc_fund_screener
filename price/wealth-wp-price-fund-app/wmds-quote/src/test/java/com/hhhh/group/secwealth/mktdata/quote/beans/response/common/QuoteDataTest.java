package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.hhhh.group.secwealth.mktdata.common.common.ProductStatus;

class QuoteDataTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new QuoteData().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new QuoteData().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new QuoteData());
    }

    @Test
    void testSetterAndGetter() {
        QuoteData obj = new QuoteData();
        obj.setProductStatus(ProductStatus.NORMAL);
        obj.setRic("test");
        obj.setSymbol("test");
        obj.setMarket("test");
        obj.setProductType("test");
        obj.setProductSubTypeCode("test");
        obj.setProdCdeAltClassCde("test");
        obj.setQuoteIndicator(false);
        obj.setPriceQuote(new BigDecimal("0"));
        obj.setCurrency("test");
        obj.setCompanyName("test");
        obj.setSecurityDescription("test");
        obj.setChangeAmount(new BigDecimal("0"));
        obj.setChangePercent(new BigDecimal("0"));
        obj.setBidPrice(new BigDecimal("0"));
        obj.setBidPriceDate("test");
        obj.setBidSize(new BigDecimal("0"));
        obj.setAskPrice(new BigDecimal("0"));
        obj.setAskSize(new BigDecimal("0"));
        obj.setDayRangeLow(new BigDecimal("0"));
        obj.setDayRangeHigh(new BigDecimal("0"));
        obj.setTradingVolume(new BigDecimal("0"));
        obj.setOpenPrice(new BigDecimal("0"));
        obj.setYearLowPrice(new BigDecimal("0"));
        obj.setYearHighPrice(new BigDecimal("0"));
        obj.setAverageVolume(new BigDecimal("0"));
        obj.setPeRatio(new BigDecimal("0"));
        obj.setMarketCap(new BigDecimal("0"));
        obj.setSharesOutstanding(new BigDecimal("0"));
        obj.setBeta(new BigDecimal("0"));
        obj.setPreviousClosePrice(new BigDecimal("0"));
        obj.setDividend(new BigDecimal("0"));
        obj.setDividendYield(new BigDecimal("0"));
        obj.setExDividendDate("test");
        obj.setBoardLot(new BigDecimal("0"));
        obj.setCasEligibleFlag("test");
        obj.setStatus("test");
        obj.setHistoryCloseDate("test");
        obj.setTurnOver(new BigDecimal("0"));
        obj.setMarketClosed("test");
        obj.setNominalPriceType("test");
        obj.setSpreadBid(new BigDecimal("0"));
        obj.setSpreadAsk(new BigDecimal("0"));
        obj.setEps(new BigDecimal("0"));
        obj.setIep(new BigDecimal("0"));
        obj.setIev(new BigDecimal("0"));
        obj.setTurnoverAmount(new BigDecimal("0"));
        obj.setTurnoverIncludeAmount(new BigDecimal("0"));
        obj.setQuoteSector("test");
        obj.setQuoteIndustry("test");
        obj.setQuoteExchange("test");
        obj.setQuote1MPercChange(new BigDecimal("0"));
        obj.setQuote3MPercChange(new BigDecimal("0"));
        obj.setQuote6MPercChange(new BigDecimal("0"));
        obj.setQuote12MPercChange(new BigDecimal("0"));
        obj.setExchangeTimezone("test");
        obj.setExchangeUpdatedTime("test");
        obj.setUnsignedAgreementId("test");
        obj.setQuoteVMA50D(new BigDecimal("0"));
        obj.setProdShoreLocCde("test");
        obj.setAllowSellMipProdInd("test");
        obj.setUpperTradingLimit("test");
        obj.setLowerTradingLimit("test");
        obj.setRiskAlert("test");
        obj.setRiskRating("test");
        obj.setRiskLvlCde("test");
        obj.setOption(new Option());
        obj.setBond(new BondQuote());
        obj.setInterestRate(new InterestRate());
        obj.setCommodity(new Commodity());
        obj.setPerformanceId("test");
        obj.setTradableCurrency("test");
        obj.setAgreementSignStatus("test");
        obj.setAgreementId("test");
        obj.setInDaylightTime("test");
        obj.setTradeUnits("test");
        obj.setDistributionFrequency("test");
        obj.setTopPerformersIndicator("test");
        obj.setTopSellProdIndex("test");
        obj.setCategoryLevel1Code("test");
        obj.setCategoryLevel1Name("test");
        obj.setAllowBuy("test");
        obj.setAllowSell("test");
        obj.setPiFundInd("test");
        obj.setDeAuthFundInd("test");
        obj.setCalendarReturns(new CalendarReturns());
        obj.setCumulativeReturns(new CumulativeReturns());
        obj.setLaunchDate("test");
        Assertions.assertNotNull(obj.toString());
        this.testGetter(obj);


    }

    private void testGetter(final QuoteData obj) {

        String result = obj.getProductStatus() + obj.getRic() + obj.getSymbol() + obj.getMarket() + obj.getProductType()
            + obj.getProductSubTypeCode() + obj.getProdCdeAltClassCde() + obj.getQuoteIndicator() + obj.getPriceQuote()
       + obj.getCurrency() + obj.getCompanyName() + obj.getSecurityDescription()
            + obj.getChangeAmount() + obj.getChangePercent()  + obj.getBidPrice()
            + obj.getBidPriceDate() + obj.getBidSize() + obj.getAskPrice() + obj.getAskSize() + obj.getDayRangeLow()
            + obj.getDayRangeHigh() + obj.getTradingVolume() + obj.getOpenPrice() + obj.getYearLowPrice() + obj.getYearHighPrice()
            + obj.getAverageVolume() + obj.getPeRatio() + obj.getMarketCap() + obj.getSharesOutstanding() + obj.getBeta()
            + obj.getPreviousClosePrice() + obj.getDividend() + obj.getDividendYield() + obj.getExDividendDate() + obj.getBoardLot()
            + obj.getCasEligibleFlag() + obj.getStatus() + obj.getHistoryCloseDate() + obj.getTurnOver() + obj.getMarketClosed()
            + obj.getNominalPriceType() + obj.getSpreadBid() + obj.getSpreadAsk() + obj.getEps() + obj.getIep() + obj.getIev()
            + obj.getTurnoverAmount() + obj.getTurnoverIncludeAmount() + obj.getQuoteSector() + obj.getQuoteIndustry()
            + obj.getQuoteExchange() + obj.getQuote1MPercChange() + obj.getQuote3MPercChange() + obj.getQuote6MPercChange()
            + obj.getQuote12MPercChange() + obj.getExchangeTimezone() + obj.getExchangeUpdatedTime() + obj.getUnsignedAgreementId()
            + obj.getQuoteVMA50D() + obj.getProdShoreLocCde() + obj.getAllowSellMipProdInd() + obj.getUpperTradingLimit()
            + obj.getLowerTradingLimit() + obj.getRiskAlert() + obj.getRiskRating() + obj.getRiskLvlCde() + obj.getOption()
            + obj.getBond() + obj.getInterestRate() + obj.getCommodity() + obj.getPerformanceId() + obj.getTradableCurrency()
            + obj.getAgreementSignStatus() + obj.getAgreementId() + obj.getInDaylightTime() + obj.getTradeUnits()
            + obj.getDistributionFrequency() + obj.getTopPerformersIndicator() + obj.getTopSellProdIndex() + obj.getCategoryLevel1Name() + obj.getAllowBuy()
            + obj.getAllowSell() + obj.getPiFundInd() + obj.getDeAuthFundInd() + obj.getCalendarReturns()
            + obj.getCumulativeReturns() + obj.getLaunchDate() ;
        Assertions.assertNotNull(result);
    }
}
