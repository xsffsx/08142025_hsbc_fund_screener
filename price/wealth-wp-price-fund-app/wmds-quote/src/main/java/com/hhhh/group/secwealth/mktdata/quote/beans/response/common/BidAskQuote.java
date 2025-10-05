
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;


public class BidAskQuote {

    /** The bid order. */
    private BigDecimal bidOrder;

    /** The bid broker. */
    private BigDecimal bidBroker;

    /** The bid price. */
    private BigDecimal bidPrice;

    /** The bid size. */
    private BigDecimal bidSize;

    /** The ask order. */
    private BigDecimal askOrder;

    /** The ask broker. */
    private BigDecimal askBroker;

    /** The ask price. */
    private BigDecimal askPrice;

    /** The ask size. */
    private BigDecimal askSize;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BidAskQuote [bidOrder=");
        builder.append(this.bidOrder);
        builder.append(", bidBroker=");
        builder.append(this.bidBroker);
        builder.append(", bidPrice=");
        builder.append(this.bidPrice);
        builder.append(", bidSize=");
        builder.append(this.bidSize);
        builder.append(", askOrder=");
        builder.append(this.askOrder);
        builder.append(", askBroker=");
        builder.append(this.askBroker);
        builder.append(", askPrice=");
        builder.append(this.askPrice);
        builder.append(", askSize=");
        builder.append(this.askSize);
        builder.append("]");
        return builder.toString();
    }


    public BigDecimal getBidOrder() {
        return this.bidOrder;
    }


    public void setBidOrder(final BigDecimal bidOrder) {
        this.bidOrder = bidOrder;
    }


    public BigDecimal getBidBroker() {
        return this.bidBroker;
    }


    public void setBidBroker(final BigDecimal bidBroker) {
        this.bidBroker = bidBroker;
    }

    public BigDecimal getBidPrice() {
        return this.bidPrice;
    }


    public void setBidPrice(final BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }


    public BigDecimal getBidSize() {
        return this.bidSize;
    }


    public void setBidSize(final BigDecimal bidSize) {
        this.bidSize = bidSize;
    }


    public BigDecimal getAskOrder() {
        return this.askOrder;
    }


    public void setAskOrder(final BigDecimal askOrder) {
        this.askOrder = askOrder;
    }


    public BigDecimal getAskBroker() {
        return this.askBroker;
    }


    public void setAskBroker(final BigDecimal askBroker) {
        this.askBroker = askBroker;
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

}
