
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;


public class IndexQuote {

    private String symbol;

    private BigDecimal quote;

    private String name;

    private BigDecimal change;

    private BigDecimal changePercentage;


    public String getSymbol() {
        return this.symbol;
    }


    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getQuote() {
        return this.quote;
    }


    public void setQuote(final BigDecimal quote) {
        this.quote = quote;
    }


    public String getName() {
        return this.name;
    }


    public void setName(final String name) {
        this.name = name;
    }


    public BigDecimal getChange() {
        return this.change;
    }


    public void setChange(final BigDecimal change) {
        this.change = change;
    }


    public BigDecimal getChangePercentage() {
        return this.changePercentage;
    }


    public void setChangePercentage(final BigDecimal changePercentage) {
        this.changePercentage = changePercentage;
    }

}
