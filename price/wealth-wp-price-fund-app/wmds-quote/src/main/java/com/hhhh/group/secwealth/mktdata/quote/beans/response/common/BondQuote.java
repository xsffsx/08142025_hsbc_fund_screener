/*
 */
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



public class BondQuote {



    private Summary summary;


    protected BondDetail bondDetails;


    protected CouponAccruedInterest couponAccuredInterest;


    protected MaturityFeature maturityFeatures;


    protected RiskMeasure riskMeasures;


    protected TradingInfo tradingInfo;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("couponAccuredInterest", this.couponAccuredInterest).append("maturityFeatures", this.maturityFeatures)
            .append("tradingInfo", this.tradingInfo).toString();
    }


    public BondDetail getBondDetails() {
        return this.bondDetails;
    }


    public void setBondDetails(final BondDetail bondDetails) {
        this.bondDetails = bondDetails;
    }


    public CouponAccruedInterest getCouponAccuredInterest() {
        return this.couponAccuredInterest;
    }


    public void setCouponAccuredInterest(final CouponAccruedInterest couponAccuredInterest) {
        this.couponAccuredInterest = couponAccuredInterest;
    }


    public MaturityFeature getMaturityFeatures() {
        return this.maturityFeatures;
    }


    public void setMaturityFeatures(final MaturityFeature maturityFeatures) {
        this.maturityFeatures = maturityFeatures;
    }


    public RiskMeasure getRiskMeasures() {
        return this.riskMeasures;
    }


    public void setRiskMeasures(final RiskMeasure riskMeasures) {
        this.riskMeasures = riskMeasures;
    }

    /**
     * +++++++++++++++++++++++++ Internal Class
     * ++++++++++++++++++++++++++++++++
     **/


    public Summary getSummary() {
        return this.summary;
    }


    public void setSummary(final Summary summary) {
        this.summary = summary;
    }


    public TradingInfo getTradingInfo() {
        return this.tradingInfo;
    }


    public void setTradingInfo(final TradingInfo tradingInfo) {
        this.tradingInfo = tradingInfo;
    }

    /** Class Summary from BondQuote. */
    public class Summary {

        private String buyQuoteId;


        private String sellQuoteId;


        private BigDecimal buyPrice;


        private BigDecimal sellPrice;


        private BigDecimal buyYield;


        private BigDecimal sellYield;


        private BigDecimal buyQuantityAvailable;


        private BigDecimal sellQuantityAvailable;


        private BigDecimal buyMarkUp;


        private BigDecimal sellMarkUp;


        public String getBuyQuoteId() {
            return this.buyQuoteId;
        }


        public void setBuyQuoteId(final String buyQuoteId) {
            this.buyQuoteId = buyQuoteId;
        }


        public String getSellQuoteId() {
            return this.sellQuoteId;
        }


        public void setSellQuoteId(final String sellQuoteId) {
            this.sellQuoteId = sellQuoteId;
        }


        public BigDecimal getBuyPrice() {
            return this.buyPrice;
        }


        public void setBuyPrice(final BigDecimal buyPrice) {
            this.buyPrice = buyPrice;
        }


        public BigDecimal getSellPrice() {
            return this.sellPrice;
        }


        public void setSellPrice(final BigDecimal sellPrice) {
            this.sellPrice = sellPrice;
        }


        public BigDecimal getBuyYield() {
            return this.buyYield;
        }


        public void setBuyYield(final BigDecimal buyYield) {
            this.buyYield = buyYield;
        }

        public BigDecimal getSellYield() {
            return this.sellYield;
        }


        public void setSellYield(final BigDecimal sellYield) {
            this.sellYield = sellYield;
        }


        public BigDecimal getBuyQuantityAvailable() {
            return this.buyQuantityAvailable;
        }


        public void setBuyQuantityAvailable(final BigDecimal buyQuantityAvailable) {
            this.buyQuantityAvailable = buyQuantityAvailable;
        }


        public BigDecimal getSellQuantityAvailable() {
            return this.sellQuantityAvailable;
        }


        public void setSellQuantityAvailable(final BigDecimal sellQuantityAvailable) {
            this.sellQuantityAvailable = sellQuantityAvailable;
        }


        public BigDecimal getBuyMarkUp() {
            return this.buyMarkUp;
        }


        public void setBuyMarkUp(final BigDecimal buyMarkUp) {
            this.buyMarkUp = buyMarkUp;
        }


        public BigDecimal getSellMarkUp() {
            return this.sellMarkUp;
        }


        public void setSellMarkUp(final BigDecimal sellMarkUp) {
            this.sellMarkUp = sellMarkUp;
        }

    }

    /** Class BondDetail from BondQuote. */
    public class BondDetail {
        protected String bondType;
        protected String issuer;
        protected String bondCurrency;
        protected String cusip;
        protected String initialIssueDate;// datatime string
        protected String isin;
        protected String mktStatus;
        protected BigDecimal mktBidDiscountMargin;
        protected BigDecimal mktOfferDiscountMargin;
        protected BigDecimal totalOutstanding;
        protected String instrumentStatus;


        public BigDecimal getMktBidDiscountMargin() {
            return this.mktBidDiscountMargin;
        }


        public void setMktBidDiscountMargin(final BigDecimal mktBidDiscountMargin) {
            this.mktBidDiscountMargin = mktBidDiscountMargin;
        }


        public BigDecimal getMktOfferDiscountMargin() {
            return this.mktOfferDiscountMargin;
        }


        public void setMktOfferDiscountMargin(final BigDecimal mktOfferDiscountMargin) {
            this.mktOfferDiscountMargin = mktOfferDiscountMargin;
        }


        public String getInstrumentStatus() {
            return this.instrumentStatus;
        }


        public void setInstrumentStatus(final String instrumentStatus) {
            this.instrumentStatus = instrumentStatus;
        }


        public String getBondType() {
            return this.bondType;
        }


        public void setBondType(final String bondType) {
            this.bondType = bondType;
        }


        public String getIssuer() {
            return this.issuer;
        }


        public void setIssuer(final String issuer) {
            this.issuer = issuer;
        }


        public String getBondCurrency() {
            return this.bondCurrency;
        }


        public void setBondCurrency(final String bondCurrency) {
            this.bondCurrency = bondCurrency;
        }


        public String getCusip() {
            return this.cusip;
        }


        public void setCusip(final String cusip) {
            this.cusip = cusip;
        }


        public String getInitialIssueDate() {
            return this.initialIssueDate;
        }


        public void setInitialIssueDate(final String initialIssueDate) {
            this.initialIssueDate = initialIssueDate;
        }


        public String getIsin() {
            return this.isin;
        }

        public void setIsin(final String isin) {
            this.isin = isin;
        }

        public String getMktStatus() {
            return this.mktStatus;
        }

        public void setMktStatus(final String mktStatus) {
            this.mktStatus = mktStatus;
        }

        public BigDecimal getTotalOutstanding() {
            return this.totalOutstanding;
        }

        public void setTotalOutstanding(final BigDecimal totalOutstanding) {
            this.totalOutstanding = totalOutstanding;
        }
    }

    public class CouponAccruedInterest {
        protected Double accuredInterest;

        // protected String couponCurrency; //use couponPaidIn
        protected String couponPaymentFrequency;
        protected String couponPaidIn;
        protected Double couponRate;
        protected BigDecimal nextCouponPayment;


        public Double getAccuredInterest() {
            return this.accuredInterest;
        }


        public void setAccuredInterest(final Double accuredInterest) {
            this.accuredInterest = accuredInterest;
        }


        public String getCouponPaidIn() {
            return this.couponPaidIn;
        }


        public void setCouponPaidIn(final String couponPaidIn) {
            this.couponPaidIn = couponPaidIn;
        }


        public String getCouponPaymentFrequency() {
            return this.couponPaymentFrequency;
        }


        public void setCouponPaymentFrequency(final String couponPaymentFrequency) {
            this.couponPaymentFrequency = couponPaymentFrequency;
        }


        public Double getCouponRate() {
            return this.couponRate;
        }


        public void setCouponRate(final Double couponRate) {
            this.couponRate = couponRate;
        }


        public BigDecimal getNextCouponPayment() {
            return this.nextCouponPayment;
        }


        public void setNextCouponPayment(final BigDecimal nextCouponPayment) {
            this.nextCouponPayment = nextCouponPayment;
        }

    }

    /** Class MaturityFeature for BondQuote. */
    public class MaturityFeature {

        protected String actualMaturity;

        protected String callable;

        protected String effectiveMaturity;

        protected String comments;


        public String getActualMaturity() {
            return this.actualMaturity;
        }


        public void setActualMaturity(final String actualMaturity) {
            this.actualMaturity = actualMaturity;
        }

        public String getCallable() {
            return this.callable;
        }

        public void setCallable(final String callable) {
            this.callable = callable;
        }

        public String getEffectiveMaturity() {
            return this.effectiveMaturity;
        }


        public void setEffectiveMaturity(final String effectiveMaturity) {
            this.effectiveMaturity = effectiveMaturity;
        }

        public String getComments() {
            return this.comments;
        }


        public void setComments(final String comments) {
            this.comments = comments;
        }
    }

    /** Class TradingInfo for BondQuote. */
    public class TradingInfo {
        protected Double minimumTradeIncrementalSell;

        protected Double minimumTradeAmountSell;

        protected Double minimumTradeIncrementalBuy;

        protected Double minimumTradeAmountBuy;

        protected String settlementCalendar;

        protected String standardSettlement;


        public Double getMinimumTradeIncrementalSell() {
            return this.minimumTradeIncrementalSell;
        }


        public void setMinimumTradeIncrementalSell(final Double minimumTradeIncrementalSell) {
            this.minimumTradeIncrementalSell = minimumTradeIncrementalSell;
        }


        public Double getMinimumTradeAmountSell() {
            return this.minimumTradeAmountSell;
        }


        public void setMinimumTradeAmountSell(final Double minimumTradeAmountSell) {
            this.minimumTradeAmountSell = minimumTradeAmountSell;
        }


        public Double getMinimumTradeIncrementalBuy() {
            return this.minimumTradeIncrementalBuy;
        }


        public void setMinimumTradeIncrementalBuy(final Double minimumTradeIncrementalBuy) {
            this.minimumTradeIncrementalBuy = minimumTradeIncrementalBuy;
        }


        public Double getMinimumTradeAmountBuy() {
            return this.minimumTradeAmountBuy;
        }


        public void setMinimumTradeAmountBuy(final Double minimumTradeAmountBuy) {
            this.minimumTradeAmountBuy = minimumTradeAmountBuy;
        }

        public String getSettlementCalendar() {
            return this.settlementCalendar;
        }


        public void setSettlementCalendar(final String settlementCalendar) {
            this.settlementCalendar = settlementCalendar;
        }

        public String getStandardSettlement() {
            return this.standardSettlement;
        }


        public void setStandardSettlement(final String standardSettlement) {
            this.standardSettlement = standardSettlement;
        }
    }

    /** Class RiskMeasure for BondQuote. */
    public class RiskMeasure {
        protected BigDecimal effectiveDuration;
        protected String riskLvlCde;

        protected List<BondRating> ratings = new ArrayList<BondRating>();


        public BigDecimal getEffectiveDuration() {
            return this.effectiveDuration;
        }


        public void setEffectiveDuration(final BigDecimal effectiveDuration) {
            this.effectiveDuration = effectiveDuration;
        }

        public String getRiskLvlCde() {
            return this.riskLvlCde;
        }


        public void setRiskLvlCde(final String riskLvlCde) {
            this.riskLvlCde = riskLvlCde;
        }


        public List<BondRating> getRatings() {
            return this.ratings;
        }


        public void setRatings(final List<BondRating> ratings) {
            this.ratings = ratings;
        }
    }
}
