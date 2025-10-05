/*
 */
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;

import com.hhhh.group.secwealth.mktdata.common.common.ProductStatus;



public class QuoteData {

    private ProductStatus productStatus;

    private String ric;

    private String symbol;

    private String market;

    private String productType;

    private String productSubTypeCode;

    private String prodCdeAltClassCde;

    private Boolean quoteIndicator;

    private BigDecimal priceQuote;

    private String currency;

    private String companyName;

    private String securityDescription;

    private BigDecimal changeAmount;

    private BigDecimal changePercent;

    private BigDecimal bidPrice;

    private String bidPriceDate;

    private BigDecimal bidSize;

    private BigDecimal askPrice;

    private BigDecimal askSize;

    private BigDecimal dayRangeLow;

    private BigDecimal dayRangeHigh;

    private BigDecimal tradingVolume;

    private BigDecimal openPrice;

    private BigDecimal yearLowPrice;

    private BigDecimal yearHighPrice;

    private BigDecimal averageVolume;

    private BigDecimal peRatio;

    private BigDecimal marketCap;

    private BigDecimal sharesOutstanding;

    private BigDecimal beta;

    private BigDecimal previousClosePrice;

    private BigDecimal dividend;

    private BigDecimal dividendYield;

    private String exDividendDate;

    private BigDecimal boardLot;

    private String casEligibleFlag;

    private String status;

    private String historyCloseDate;

    private BigDecimal turnOver;

    private String marketClosed;

    private String nominalPriceType;

    private BigDecimal spreadBid;

    private BigDecimal spreadAsk;

    private BigDecimal eps;

    private BigDecimal iep;

    private BigDecimal iev;

    private BigDecimal turnoverAmount;

    private BigDecimal turnoverIncludeAmount;

    private String quoteSector;

    private String quoteIndustry;

    private String quoteExchange;

    private BigDecimal quote1MPercChange;

    private BigDecimal quote3MPercChange;

    private BigDecimal quote6MPercChange;

    private BigDecimal quote12MPercChange;

    private String exchangeTimezone;

    private String exchangeUpdatedTime;

    private String unsignedAgreementId;

    private BigDecimal quoteVMA50D;

    private String prodShoreLocCde;

    private String allowSellMipProdInd;

    private String upperTradingLimit;

    private String lowerTradingLimit;

    private String riskAlert;

    private String riskRating;

    private String riskLvlCde;

    private Option option;


    private BondQuote bond;


    private InterestRate interestRate;

    private Commodity commodity;

    private String performanceId;

    private String tradableCurrency;

    private String agreementSignStatus;

    private String agreementId;

    private String inDaylightTime;

    private String tradeUnits;

    private String distributionFrequency;

    private String topPerformersIndicator;

    private String topSellProdIndex; // hhhh_BEST_SELLER Filtering Indicator
    /** The Level 1 Fund Category Code. */
    private String categoryLevel1Code;
    /** The Level 1 Fund Category Name. */
    private String categoryLevel1Name;

    private String allowBuy;

    private String allowSell;

    private String piFundInd;

    private String deAuthFundInd;

    private CalendarReturns calendarReturns;

    private CumulativeReturns cumulativeReturns;

    private String launchDate;


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


    public String getAllowBuy() {
        return this.allowBuy;
    }


    public void setAllowBuy(final String allowBuy) {
        this.allowBuy = allowBuy;
    }

    public String getAllowSell() {
        return this.allowSell;
    }


    public void setAllowSell(final String allowSell) {
        this.allowSell = allowSell;
    }


    public String getPiFundInd() {
        return this.piFundInd;
    }


    public void setPiFundInd(final String piFundInd) {
        this.piFundInd = piFundInd;
    }


    public String getDeAuthFundInd() {
        return this.deAuthFundInd;
    }


    public void setDeAuthFundInd(final String deAuthFundInd) {
        this.deAuthFundInd = deAuthFundInd;
    }

    public String getTradeUnits() {
        return this.tradeUnits;
    }

    public void setTradeUnits(final String tradeUnits) {
        this.tradeUnits = tradeUnits;
    }


    public String getTradableCurrency() {
        return this.tradableCurrency;
    }


    public void setTradableCurrency(final String tradableCurrency) {
        this.tradableCurrency = tradableCurrency;
    }


    public InterestRate getInterestRate() {
        return this.interestRate;
    }


    public void setInterestRate(final InterestRate interestRate) {
        this.interestRate = interestRate;
    }


    public Commodity getCommodity() {
        return this.commodity;
    }


    public void setCommodity(final Commodity commodity) {
        this.commodity = commodity;
    }


    public String getPerformanceId() {
        return this.performanceId;
    }


    public void setPerformanceId(final String performanceId) {
        this.performanceId = performanceId;
    }


    public String getRic() {
        return this.ric;
    }


    public void setRic(final String ric) {
        this.ric = ric;
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


    public String getProductSubTypeCode() {
        return this.productSubTypeCode;
    }


    public void setProductSubTypeCode(final String productSubTypeCode) {
        this.productSubTypeCode = productSubTypeCode;
    }


    public String getProdCdeAltClassCde() {
        return this.prodCdeAltClassCde;
    }


    public void setProdCdeAltClassCde(final String prodCdeAltClassCde) {
        this.prodCdeAltClassCde = prodCdeAltClassCde;
    }


    public Boolean getQuoteIndicator() {
        return this.quoteIndicator;
    }

    public void setQuoteIndicator(final Boolean quoteIndicator) {
        this.quoteIndicator = quoteIndicator;
    }


    public BigDecimal getPriceQuote() {
        return this.priceQuote;
    }


    public void setPriceQuote(final BigDecimal priceQuote) {
        this.priceQuote = priceQuote;
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


    public String getSecurityDescription() {
        return this.securityDescription;
    }


    public void setSecurityDescription(final String securityDescription) {
        this.securityDescription = securityDescription;
    }



    public String getUpperTradingLimit() {
        return this.upperTradingLimit;
    }


    public void setUpperTradingLimit(final String upperTradingLimit) {
        this.upperTradingLimit = upperTradingLimit;
    }


    public String getLowerTradingLimit() {
        return this.lowerTradingLimit;
    }


    public void setLowerTradingLimit(final String lowerTradingLimit) {
        this.lowerTradingLimit = lowerTradingLimit;
    }


    public String getRiskAlert() {
        return this.riskAlert;
    }


    public void setRiskAlert(final String riskAlert) {
        this.riskAlert = riskAlert;
    }

    public String getRiskRating() {
        return this.riskRating;
    }


    public void setRiskRating(final String riskRating) {
        this.riskRating = riskRating;
    }


    public String getRiskLvlCde() {
        return this.riskLvlCde;
    }


    public void setRiskLvlCde(final String riskLvlCde) {
        this.riskLvlCde = riskLvlCde;
    }



    public String getExDividendDate() {
        return this.exDividendDate;
    }


    public void setExDividendDate(final String exDividendDate) {
        this.exDividendDate = exDividendDate;
    }


    public BigDecimal getBoardLot() {
        return this.boardLot;
    }


    public void setBoardLot(final BigDecimal boardLot) {
        this.boardLot = boardLot;
    }


    public String getCasEligibleFlag() {
        return this.casEligibleFlag;
    }


    public void setCasEligibleFlag(final String casEligibleFlag) {
        this.casEligibleFlag = casEligibleFlag;
    }


    public String getStatus() {
        return this.status;
    }


    public void setStatus(final String status) {
        this.status = status;
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


    public BigDecimal getBidSize() {
        return this.bidSize;
    }


    public void setBidSize(final BigDecimal bidSize) {
        this.bidSize = bidSize;
    }


    public BigDecimal getAskPrice() {
        return this.askPrice;
    }


    public void setAskPrice(final BigDecimal askPrice) {
        this.askPrice = askPrice;
    }


    public BigDecimal getAskSize() {
        return this.askSize;
    }


    public void setAskSize(final BigDecimal askSize) {
        this.askSize = askSize;
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


    public BigDecimal getTradingVolume() {
        return this.tradingVolume;
    }


    public void setTradingVolume(final BigDecimal tradingVolume) {
        this.tradingVolume = tradingVolume;
    }


    public BigDecimal getOpenPrice() {
        return this.openPrice;
    }


    public void setOpenPrice(final BigDecimal openPrice) {
        this.openPrice = openPrice;
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


    public BigDecimal getAverageVolume() {
        return this.averageVolume;
    }


    public void setAverageVolume(final BigDecimal averageVolume) {
        this.averageVolume = averageVolume;
    }

    public BigDecimal getPeRatio() {
        return this.peRatio;
    }


    public void setPeRatio(final BigDecimal peRatio) {
        this.peRatio = peRatio;
    }


    public BigDecimal getMarketCap() {
        return this.marketCap;
    }


    public void setMarketCap(final BigDecimal marketCap) {
        this.marketCap = marketCap;
    }


    public BigDecimal getSharesOutstanding() {
        return this.sharesOutstanding;
    }


    public void setSharesOutstanding(final BigDecimal sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }


    public BigDecimal getBeta() {
        return this.beta;
    }

    public void setBeta(final BigDecimal beta) {
        this.beta = beta;
    }


    public BigDecimal getPreviousClosePrice() {
        return this.previousClosePrice;
    }


    public void setPreviousClosePrice(final BigDecimal previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }


    public BigDecimal getDividend() {
        return this.dividend;
    }


    public void setDividend(final BigDecimal dividend) {
        this.dividend = dividend;
    }


    public BigDecimal getDividendYield() {
        return this.dividendYield;
    }


    public void setDividendYield(final BigDecimal dividendYield) {
        this.dividendYield = dividendYield;
    }


    public String getHistoryCloseDate() {
        return this.historyCloseDate;
    }


    public void setHistoryCloseDate(final String historyCloseDate) {
        this.historyCloseDate = historyCloseDate;
    }


    public BigDecimal getTurnOver() {
        return this.turnOver;
    }


    public void setTurnOver(final BigDecimal turnOver) {
        this.turnOver = turnOver;
    }


    public String getMarketClosed() {
        return this.marketClosed;
    }


    public void setMarketClosed(final String marketClosed) {
        this.marketClosed = marketClosed;
    }


    public String getNominalPriceType() {
        return this.nominalPriceType;
    }


    public void setNominalPriceType(final String nominalPriceType) {
        this.nominalPriceType = nominalPriceType;
    }

    public BigDecimal getSpreadBid() {
        return this.spreadBid;
    }


    public void setSpreadBid(final BigDecimal spreadBid) {
        this.spreadBid = spreadBid;
    }


    public BigDecimal getSpreadAsk() {
        return this.spreadAsk;
    }


    public void setSpreadAsk(final BigDecimal spreadAsk) {
        this.spreadAsk = spreadAsk;
    }


    public BigDecimal getEps() {
        return this.eps;
    }


    public void setEps(final BigDecimal eps) {
        this.eps = eps;
    }

    public BigDecimal getIep() {
        return this.iep;
    }


    public void setIep(final BigDecimal iep) {
        this.iep = iep;
    }

    public BigDecimal getIev() {
        return this.iev;
    }


    public void setIev(final BigDecimal iev) {
        this.iev = iev;
    }


    public BigDecimal getTurnoverAmount() {
        return this.turnoverAmount;
    }


    public void setTurnoverAmount(final BigDecimal turnoverAmount) {
        this.turnoverAmount = turnoverAmount;
    }


    public BigDecimal getTurnoverIncludeAmount() {
        return this.turnoverIncludeAmount;
    }


    public void setTurnoverIncludeAmount(final BigDecimal turnoverIncludeAmount) {
        this.turnoverIncludeAmount = turnoverIncludeAmount;
    }


    public String getQuoteSector() {
        return this.quoteSector;
    }


    public void setQuoteSector(final String quoteSector) {
        this.quoteSector = quoteSector;
    }

    public String getQuoteIndustry() {
        return this.quoteIndustry;
    }


    public void setQuoteIndustry(final String quoteIndustry) {
        this.quoteIndustry = quoteIndustry;
    }


    public String getQuoteExchange() {
        return this.quoteExchange;
    }


    public void setQuoteExchange(final String quoteExchange) {
        this.quoteExchange = quoteExchange;
    }

    public BigDecimal getQuote1MPercChange() {
        return this.quote1MPercChange;
    }


    public void setQuote1MPercChange(final BigDecimal quote1mPercChange) {
        this.quote1MPercChange = quote1mPercChange;
    }


    public BigDecimal getQuote3MPercChange() {
        return this.quote3MPercChange;
    }


    public void setQuote3MPercChange(final BigDecimal quote3mPercChange) {
        this.quote3MPercChange = quote3mPercChange;
    }

    public BigDecimal getQuote6MPercChange() {
        return this.quote6MPercChange;
    }


    public void setQuote6MPercChange(final BigDecimal quote6mPercChange) {
        this.quote6MPercChange = quote6mPercChange;
    }


    public BigDecimal getQuote12MPercChange() {
        return this.quote12MPercChange;
    }


    public void setQuote12MPercChange(final BigDecimal quote12mPercChange) {
        this.quote12MPercChange = quote12mPercChange;
    }


    public String getExchangeTimezone() {
        return this.exchangeTimezone;
    }


    public void setExchangeTimezone(final String exchangeTimezone) {
        this.exchangeTimezone = exchangeTimezone;
    }


    public String getExchangeUpdatedTime() {
        return this.exchangeUpdatedTime;
    }


    public void setExchangeUpdatedTime(final String exchangeUpdatedTime) {
        this.exchangeUpdatedTime = exchangeUpdatedTime;
    }


    public String getUnsignedAgreementId() {
        return this.unsignedAgreementId;
    }


    public void setUnsignedAgreementId(final String unsignedAgreementId) {
        this.unsignedAgreementId = unsignedAgreementId;
    }


    public String getProdShoreLocCde() {
        return this.prodShoreLocCde;
    }


    public void setProdShoreLocCde(final String prodShoreLocCde) {
        this.prodShoreLocCde = prodShoreLocCde;
    }


    public Option getOption() {
        return this.option;
    }


    public void setOption(final Option option) {
        this.option = option;
    }

    public BondQuote getBond() {
        return this.bond;
    }


    public void setBond(final BondQuote bond) {
        this.bond = bond;
    }


    public BigDecimal getQuoteVMA50D() {
        return this.quoteVMA50D;
    }


    public void setQuoteVMA50D(final BigDecimal quoteVMA50D) {
        this.quoteVMA50D = quoteVMA50D;
    }


    public String getAllowSellMipProdInd() {
        return this.allowSellMipProdInd;
    }


    public void setAllowSellMipProdInd(final String allowSellMipProdInd) {
        this.allowSellMipProdInd = allowSellMipProdInd;
    }


    public ProductStatus getProductStatus() {
        return this.productStatus;
    }


    public void setProductStatus(final ProductStatus productStatus) {
        this.productStatus = productStatus;
    }


    public String getAgreementSignStatus() {
        return this.agreementSignStatus;
    }


    public void setAgreementSignStatus(final String agreementSignStatus) {
        this.agreementSignStatus = agreementSignStatus;
    }


    public String getAgreementId() {
        return this.agreementId;
    }


    public void setAgreementId(final String agreementId) {
        this.agreementId = agreementId;
    }


    public String getInDaylightTime() {
        return this.inDaylightTime;
    }


    public void setInDaylightTime(final String inDaylightTime) {
        this.inDaylightTime = inDaylightTime;
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


    public String getLaunchDate() {
        return this.launchDate;
    }


    public void setLaunchDate(final String launchDate) {
        this.launchDate = launchDate;
    }

    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("QuoteData [productStatus=");
        builder.append(this.productStatus);
        builder.append(", ric=");
        builder.append(this.ric);
        builder.append(", symbol=");
        builder.append(this.symbol);
        builder.append(", market=");
        builder.append(this.market);
        builder.append(", productType=");
        builder.append(this.productType);
        builder.append(", productSubTypeCode=");
        builder.append(this.productSubTypeCode);
        builder.append(", quoteIndicator=");
        builder.append(this.quoteIndicator);
        builder.append(", priceQuote=");
        builder.append(this.priceQuote);
        builder.append(", currency=");
        builder.append(this.currency);
        builder.append(", companyName=");
        builder.append(this.companyName);
        builder.append(", securityDescription=");
        builder.append(this.securityDescription);
        builder.append(", changeAmount=");
        builder.append(this.changeAmount);
        builder.append(", changePercent=");
        builder.append(this.changePercent);
        builder.append(", bidPrice=");
        builder.append(this.bidPrice);
        builder.append(", bidSize=");
        builder.append(this.bidSize);
        builder.append(", askPrice=");
        builder.append(this.askPrice);
        builder.append(", askSize=");
        builder.append(this.askSize);
        builder.append(", dayRangeLow=");
        builder.append(this.dayRangeLow);
        builder.append(", dayRangeHigh=");
        builder.append(this.dayRangeHigh);
        builder.append(", tradingVolume=");
        builder.append(this.tradingVolume);
        builder.append(", openPrice=");
        builder.append(this.openPrice);
        builder.append(", yearLowPrice=");
        builder.append(this.yearLowPrice);
        builder.append(", yearHighPrice=");
        builder.append(this.yearHighPrice);
        builder.append(", averageVolume=");
        builder.append(this.averageVolume);
        builder.append(", peRatio=");
        builder.append(this.peRatio);
        builder.append(", marketCap=");
        builder.append(this.marketCap);
        builder.append(", sharesOutstanding=");
        builder.append(this.sharesOutstanding);
        builder.append(", beta=");
        builder.append(this.beta);
        builder.append(", previousClosePrice=");
        builder.append(this.previousClosePrice);
        builder.append(", dividend=");
        builder.append(this.dividend);
        builder.append(", dividendYield=");
        builder.append(this.dividendYield);
        builder.append(", exDividendDate=");
        builder.append(this.exDividendDate);
        builder.append(", boardLot=");
        builder.append(this.boardLot);
        builder.append(", status=");
        builder.append(this.status);
        builder.append(", historyCloseDate=");
        builder.append(this.historyCloseDate);
        builder.append(", turnOver=");
        builder.append(this.turnOver);
        builder.append(", marketClosed=");
        builder.append(this.marketClosed);
        builder.append(", nominalPriceType=");
        builder.append(this.nominalPriceType);
        builder.append(", spreadBid=");
        builder.append(this.spreadBid);
        builder.append(", spreadAsk=");
        builder.append(this.spreadAsk);
        builder.append(", eps=");
        builder.append(this.eps);
        builder.append(", iep=");
        builder.append(this.iep);
        builder.append(", iev=");
        builder.append(this.iev);
        builder.append(", turnoverAmount=");
        builder.append(this.turnoverAmount);
        builder.append(", turnoverIncludeAmount=");
        builder.append(this.turnoverIncludeAmount);
        builder.append(", quoteSector=");
        builder.append(this.quoteSector);
        builder.append(", quoteIndustry=");
        builder.append(this.quoteIndustry);
        builder.append(", quoteExchange=");
        builder.append(this.quoteExchange);
        builder.append(", quote1MPercChange=");
        builder.append(this.quote1MPercChange);
        builder.append(", quote3MPercChange=");
        builder.append(this.quote3MPercChange);
        builder.append(", quote6MPercChange=");
        builder.append(this.quote6MPercChange);
        builder.append(", quote12MPercChange=");
        builder.append(this.quote12MPercChange);
        builder.append(", exchangeTimezone=");
        builder.append(this.exchangeTimezone);
        builder.append(", exchangeUpdatedTime=");
        builder.append(this.exchangeUpdatedTime);
        builder.append(", unsignedAgreementId=");
        builder.append(this.unsignedAgreementId);
        builder.append(", quoteVMA50D=");
        builder.append(this.quoteVMA50D);
        builder.append(", prodShoreLocCde=");
        builder.append(this.prodShoreLocCde);
        builder.append(", allowSellMipProdInd=");
        builder.append(this.allowSellMipProdInd);
        builder.append(", upperTradingLimit=");
        builder.append(this.upperTradingLimit);
        builder.append(", lowerTradingLimit=");
        builder.append(this.lowerTradingLimit);
        builder.append(", riskAlert=");
        builder.append(this.riskAlert);
        builder.append(", riskRating=");
        builder.append(this.riskRating);
        builder.append(", option=");
        builder.append(this.option);
        builder.append(", bond=");
        builder.append(this.bond);
        builder.append(", performanceId=");
        builder.append(this.performanceId);
        builder.append(", tradableCurrency=");
        builder.append(this.tradableCurrency);
        builder.append(", agreementSignStatus=");
        builder.append(this.agreementSignStatus);
        builder.append(", agreementId=");
        builder.append(this.agreementId);
        builder.append(", inDaylightTime=");
        builder.append(this.inDaylightTime);
        builder.append("]");
        return builder.toString();
    }

}