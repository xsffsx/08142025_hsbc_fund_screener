
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;

/**
 * the class Commodity
 */
public class Commodity {

    private BigDecimal dayBidLow;

    private BigDecimal dayBidHigh;

    private BigDecimal dayAskLow;

    private BigDecimal dayAskHigh;

    private BigDecimal previousCloseBidPrice;

    private BigDecimal previousCloseAskPrice;


    public BigDecimal getDayBidLow() {
        return this.dayBidLow;
    }


    public void setDayBidLow(final BigDecimal dayBidLow) {
        this.dayBidLow = dayBidLow;
    }


    public BigDecimal getDayBidHigh() {
        return this.dayBidHigh;
    }


    public void setDayBidHigh(final BigDecimal dayBidHigh) {
        this.dayBidHigh = dayBidHigh;
    }


    public BigDecimal getDayAskLow() {
        return this.dayAskLow;
    }


    public void setDayAskLow(final BigDecimal dayAskLow) {
        this.dayAskLow = dayAskLow;
    }


    public BigDecimal getDayAskHigh() {
        return this.dayAskHigh;
    }


    public void setDayAskHigh(final BigDecimal dayAskHigh) {
        this.dayAskHigh = dayAskHigh;
    }


    public BigDecimal getPreviousCloseBidPrice() {
        return this.previousCloseBidPrice;
    }


    public void setPreviousCloseBidPrice(final BigDecimal previousCloseBidPrice) {
        this.previousCloseBidPrice = previousCloseBidPrice;
    }


    public BigDecimal getPreviousCloseAskPrice() {
        return this.previousCloseAskPrice;
    }


    public void setPreviousCloseAskPrice(final BigDecimal previousCloseAskPrice) {
        this.previousCloseAskPrice = previousCloseAskPrice;
    }


}
