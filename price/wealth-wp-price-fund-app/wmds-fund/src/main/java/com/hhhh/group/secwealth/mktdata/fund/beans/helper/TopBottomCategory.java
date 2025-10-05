
package com.hhhh.group.secwealth.mktdata.fund.beans.helper;

import java.math.BigDecimal;


public class TopBottomCategory {
    String categoryCode;
    String categoryName;
    Integer numberOfFunds;
    Integer numberOfhhhhFunds;
    BigDecimal trailingTotalReturn;
    BigDecimal averageRisk;


    public String getCategoryCode() {
        return this.categoryCode;
    }


    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }


    public String getCategoryName() {
        return this.categoryName;
    }


    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }


    public Integer getNumberOfFunds() {
        return this.numberOfFunds;
    }


    public void setNumberOfFunds(final Integer numberOfFunds) {
        this.numberOfFunds = numberOfFunds;
    }


    public Integer getNumberOfhhhhFunds() {
        return this.numberOfhhhhFunds;
    }


    public void setNumberOfhhhhFunds(final Integer numberOfhhhhFunds) {
        this.numberOfhhhhFunds = numberOfhhhhFunds;
    }


    public BigDecimal getTrailingTotalReturn() {
        return this.trailingTotalReturn;
    }


    public void setTrailingTotalReturn(final BigDecimal trailingTotalReturn) {
        this.trailingTotalReturn = trailingTotalReturn;
    }


    public BigDecimal getAverageRisk() {
        return this.averageRisk;
    }


    public void setAverageRisk(final BigDecimal averageRisk) {
        this.averageRisk = averageRisk;
    }


}
