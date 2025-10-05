
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;



public class PriceQuote {

    private String ric;

    private String symbol;

    private String market;

    private String productType;

    private String underlyingProductType;

    private BigDecimal priceQuote;

    /**
     * point out it's delay quote or realtime qoute,the value should be Y or N.
     */
    private Boolean delay;

    /** The currency of the qoute. */
    private String currency;

    private String companyName;

    private BigDecimal changeAmount;

    private BigDecimal changePercent;

    private String unsignedAgreementId;

    private BigDecimal quoteVMA50D;

    private String prodAltNum;

    private String prodCdeAltClassCde;

    private String productStatus;

    private BigDecimal dayRangeLow;

    private BigDecimal dayRangeHigh;

    private BigDecimal yearLowPrice;

    private BigDecimal yearHighPrice;

    private String exchange;

    private String underlyingSymbol;

    private String underlyingMarket;

    private String rootSymbol;

    private String optionType;

    private String expiryDate;

    private BigDecimal strike;

    private String exchangeUpdatedTime;

    private String inDaylightTime;

    private String exchangeTimezone;

    private String tradeUnits;

    private BigDecimal previousClosePrice;

    private BigDecimal openPrice;

    private BigDecimal turnoverAmount;

    private BigDecimal bidPrice;

    private String bidPriceDate;

    private BigDecimal askPrice;

    private BigDecimal dividendYield;

    private String distributionFrequency;

    private String topPerformersIndicator;

    private String topSellProdIndex; // hhhh_BEST_SELLER Filtering Indicator

    /** The Level 1 Fund Category Code. */
    private String categoryLevel1Code;
    /** The Level 1 Fund Category Name. */
    private String categoryLevel1Name;

    private CalendarReturns calendarReturns;

    private CumulativeReturns cumulativeReturns;

    private String riskLvlCde;

    private String launchDate;

    private List<ProdAltNumSeg> prodAltNumSegs;




    public BigDecimal getBidPrice() {
        return this.bidPrice;
    }

    public void setBidPrice(final BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }


    public String getBidPriceDate() {
        return this.bidPriceDate;
    }


    public void setBidPriceDate(final String bidPriceDate) {
        this.bidPriceDate = bidPriceDate;
    }

    public BigDecimal getAskPrice() {
        return this.askPrice;
    }


    public void setAskPrice(final BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public String getExchangeUpdatedTime() {
        return this.exchangeUpdatedTime;
    }

    public void setExchangeUpdatedTime(final String exchangeUpdatedTime) {
        this.exchangeUpdatedTime = exchangeUpdatedTime;
    }

    public String getInDaylightTime() {
        return this.inDaylightTime;
    }

    public void setInDaylightTime(final String inDaylightTime) {
        this.inDaylightTime = inDaylightTime;
    }

    public String getExchangeTimezone() {
        return this.exchangeTimezone;
    }

    public void setExchangeTimezone(final String exchangeTimezone) {
        this.exchangeTimezone = exchangeTimezone;
    }

    public String getTradeUnits() {
        return this.tradeUnits;
    }

    public void setTradeUnits(final String tradeUnits) {
        this.tradeUnits = tradeUnits;
    }


    public BigDecimal getPreviousClosePrice() {
        return this.previousClosePrice;
    }


    public void setPreviousClosePrice(final BigDecimal previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }


    public BigDecimal getOpenPrice() {
        return this.openPrice;
    }


    public void setOpenPrice(final BigDecimal openPrice) {
        this.openPrice = openPrice;
    }


    public BigDecimal getTurnoverAmount() {
        return this.turnoverAmount;
    }


    public void setTurnoverAmount(final BigDecimal turnoverAmount) {
        this.turnoverAmount = turnoverAmount;
    }


    public String getUnderlyingProductType() {
        return this.underlyingProductType;
    }


    public void setUnderlyingProductType(final String underlyingProductType) {
        this.underlyingProductType = underlyingProductType;
    }


    public String getUnsignedAgreementId() {
        return this.unsignedAgreementId;
    }

    public void setUnsignedAgreementId(final String unsignedAgreementId) {
        this.unsignedAgreementId = unsignedAgreementId;
    }


    public String getSymbol() {
        return this.symbol;
    }


    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }


    public String getMarket() {
        return this.market;
    }


    public void setMarket(final String market) {
        this.market = market;
    }


    public String getProductType() {
        return this.productType;
    }


    public void setProductType(final String productType) {
        this.productType = productType;
    }


    public BigDecimal getPriceQuote() {
        return this.priceQuote;
    }


    public void setPriceQuote(final BigDecimal priceQuote) {
        this.priceQuote = priceQuote;
    }


    public Boolean getDelay() {
        return this.delay;
    }


    public void setDelay(final Boolean delay) {
        this.delay = delay;
    }


    public String getCurrency() {
        return this.currency;
    }


    public void setCurrency(final String currency) {
        this.currency = currency;
    }


    public String getCompanyName() {
        return this.companyName;
    }


    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }



    public String getRic() {
        return this.ric;
    }


    public void setRic(final String ric) {
        this.ric = ric;
    }


    public String getProdAltNum() {
        return this.prodAltNum;
    }


    public void setProdAltNum(final String prodAltNum) {
        this.prodAltNum = prodAltNum;
    }


    public String getProdCdeAltClassCde() {
        return this.prodCdeAltClassCde;
    }


    public void setProdCdeAltClassCde(final String prodCdeAltClassCde) {
        this.prodCdeAltClassCde = prodCdeAltClassCde;
    }


    public String getProductStatus() {
        return this.productStatus;
    }


    public void setProductStatus(final String productStatus) {
        this.productStatus = productStatus;
    }


    public BigDecimal getQuoteVMA50D() {
        return this.quoteVMA50D;
    }


    public void setQuoteVMA50D(final BigDecimal quoteVMA50D) {
        this.quoteVMA50D = quoteVMA50D;
    }


    public BigDecimal getChangeAmount() {
        return this.changeAmount;
    }


    public void setChangeAmount(final BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }


    public BigDecimal getChangePercent() {
        return this.changePercent;
    }


    public void setChangePercent(final BigDecimal changePercent) {
        this.changePercent = changePercent;
    }


    public BigDecimal getDayRangeLow() {
        return this.dayRangeLow;
    }


    public void setDayRangeLow(final BigDecimal dayRangeLow) {
        this.dayRangeLow = dayRangeLow;
    }


    public BigDecimal getDayRangeHigh() {
        return this.dayRangeHigh;
    }


    public void setDayRangeHigh(final BigDecimal dayRangeHigh) {
        this.dayRangeHigh = dayRangeHigh;
    }


    public BigDecimal getYearLowPrice() {
        return this.yearLowPrice;
    }


    public void setYearLowPrice(final BigDecimal yearLowPrice) {
        this.yearLowPrice = yearLowPrice;
    }


    public BigDecimal getYearHighPrice() {
        return this.yearHighPrice;
    }


    public void setYearHighPrice(final BigDecimal yearHighPrice) {
        this.yearHighPrice = yearHighPrice;
    }


    public String getExchange() {
        return this.exchange;
    }


    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    public String getUnderlyingSymbol() {
        return this.underlyingSymbol;
    }


    public void setUnderlyingSymbol(final String underlyingSymbol) {
        this.underlyingSymbol = underlyingSymbol;
    }


    public String getUnderlyingMarket() {
        return this.underlyingMarket;
    }

    public void setUnderlyingMarket(final String underlyingMarket) {
        this.underlyingMarket = underlyingMarket;
    }


    public String getRootSymbol() {
        return this.rootSymbol;
    }


    public void setRootSymbol(final String rootSymbol) {
        this.rootSymbol = rootSymbol;
    }


    public String getOptionType() {
        return this.optionType;
    }


    public void setOptionType(final String optionType) {
        this.optionType = optionType;
    }


    public String getExpiryDate() {
        return this.expiryDate;
    }


    public void setExpiryDate(final String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public BigDecimal getStrike() {
        return this.strike;
    }


    public void setStrike(final BigDecimal strike) {
        this.strike = strike;
    }


    public BigDecimal getDividendYield() {
        return this.dividendYield;
    }


    public void setDividendYield(final BigDecimal dividendYield) {
        this.dividendYield = dividendYield;
    }


    public String getDistributionFrequency() {
        return this.distributionFrequency;
    }


    public void setDistributionFrequency(final String distributionFrequency) {
        this.distributionFrequency = distributionFrequency;
    }


    public String getTopPerformersIndicator() {
        return this.topPerformersIndicator;
    }


    public void setTopPerformersIndicator(final String topPerformersIndicator) {
        this.topPerformersIndicator = topPerformersIndicator;
    }


    public String getTopSellProdIndex() {
        return this.topSellProdIndex;
    }


    public void setTopSellProdIndex(final String topSellProdIndex) {
        this.topSellProdIndex = topSellProdIndex;
    }

    public String getCategoryLevel1Code() {
        return this.categoryLevel1Code;
    }

    public void setCategoryLevel1Code(final String categoryLevel1Code) {
        this.categoryLevel1Code = categoryLevel1Code;
    }

    public String getCategoryLevel1Name() {
        return this.categoryLevel1Name;
    }

    public void setCategoryLevel1Name(final String categoryLevel1Name) {
        this.categoryLevel1Name = categoryLevel1Name;
    }


    public CalendarReturns getCalendarReturns() {
        return this.calendarReturns;
    }


    public void setCalendarReturns(final CalendarReturns calendarReturns) {
        this.calendarReturns = calendarReturns;
    }


    public CumulativeReturns getCumulativeReturns() {
        return this.cumulativeReturns;
    }


    public void setCumulativeReturns(final CumulativeReturns cumulativeReturns) {
        this.cumulativeReturns = cumulativeReturns;
    }


    public String getRiskLvlCde() {
        return this.riskLvlCde;
    }

    public void setRiskLvlCde(final String riskLvlCde) {
        this.riskLvlCde = riskLvlCde;
    }


    public String getLaunchDate() {
        return this.launchDate;
    }


    public void setLaunchDate(final String launchDate) {
        this.launchDate = launchDate;
    }

    public List<ProdAltNumSeg> getProdAltNumSegs() {
        return prodAltNumSegs;
    }

    public void setProdAltNumSegs(List<ProdAltNumSeg> prodAltNumSegs) {
        this.prodAltNumSegs = prodAltNumSegs;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PriceQuote [ric=");
        builder.append(this.ric);
        builder.append(", symbol=");
        builder.append(this.symbol);
        builder.append(", market=");
        builder.append(this.market);
        builder.append(", productType=");
        builder.append(this.productType);
        builder.append(", underlyingProductType=");
        builder.append(this.underlyingProductType);
        builder.append(", underlyingSymbol=");
        builder.append(this.underlyingSymbol);
        builder.append(", underlyingMarket=");
        builder.append(this.underlyingMarket);
        builder.append(", rootSymbol=");
        builder.append(this.rootSymbol);
        builder.append(", optionType=");
        builder.append(this.optionType);
        builder.append(", expiryDate=");
        builder.append(this.expiryDate);
        builder.append(", strike=");
        builder.append(this.strike);
        builder.append(", priceQuote=");
        builder.append(this.priceQuote);
        builder.append(", delay=");
        builder.append(this.delay);
        builder.append(", currency=");
        builder.append(this.currency);
        builder.append(", companyName=");
        builder.append(this.companyName);
        builder.append(", changeAmount=");
        builder.append(this.changeAmount);
        builder.append(", changePercent=");
        builder.append(this.changePercent);
        builder.append(", unsignedAgreementId=");
        builder.append(this.unsignedAgreementId);
        builder.append(", quoteVMA50D=");
        builder.append(this.quoteVMA50D);
        builder.append(", prodAltNum=");
        builder.append(this.prodAltNum);
        builder.append(", prodCdeAltClassCde=");
        builder.append(this.prodCdeAltClassCde);
        builder.append(", errorCode=");
        builder.append(", productStatus=");
        builder.append(this.productStatus);
        builder.append(", dayRangeLow=");
        builder.append(this.dayRangeLow);
        builder.append(", dayRangeHigh=");
        builder.append(this.dayRangeHigh);
        builder.append(", yearLowPrice=");
        builder.append(this.yearLowPrice);
        builder.append(", yearHighPrice=");
        builder.append(this.yearHighPrice);
        builder.append(", prodAltNumSegs=");
        builder.append(this.prodAltNumSegs);
        builder.append(this.exchange);
        builder.append("]");
        return builder.toString();
    }

}