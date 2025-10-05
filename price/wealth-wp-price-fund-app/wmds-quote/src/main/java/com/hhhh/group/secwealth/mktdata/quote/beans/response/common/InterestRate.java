
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;


public class InterestRate {

    private BigDecimal interestRateAmount;

    private BigDecimal interestRateChangePercent;

    private BigDecimal weekagoRateAmount;

    private String tenor;


    public BigDecimal getInterestRateAmount() {
        return this.interestRateAmount;
    }


    public void setInterestRateAmount(final BigDecimal interestRateAmount) {
        this.interestRateAmount = interestRateAmount;
    }

    public BigDecimal getInterestRateChangePercent() {
        return this.interestRateChangePercent;
    }


    public void setInterestRateChangePercent(final BigDecimal interestRateChangePercent) {
        this.interestRateChangePercent = interestRateChangePercent;
    }


    public BigDecimal getWeekagoRateAmount() {
        return this.weekagoRateAmount;
    }


    public void setWeekagoRateAmount(final BigDecimal weekagoRateAmount) {
        this.weekagoRateAmount = weekagoRateAmount;
    }

    public String getTenor() {
        return this.tenor;
    }

    public void setTenor(final String tenor) {
        this.tenor = tenor;
    }

}
