
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.categoryoverview;

import java.math.BigDecimal;


public class CategorySummary {

    private String categoryCode;
    private String categoryName;
    private BigDecimal return1Y;
    private BigDecimal return3Y;
    private BigDecimal return5Y;
    private BigDecimal return10Y;
    private BigDecimal stdDev3Y;


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


    public BigDecimal getReturn1Y() {
        return this.return1Y;
    }


    public void setReturn1Y(final BigDecimal return1y) {
        this.return1Y = return1y;
    }


    public BigDecimal getReturn3Y() {
        return this.return3Y;
    }


    public void setReturn3Y(final BigDecimal return3y) {
        this.return3Y = return3y;
    }


    public BigDecimal getReturn5Y() {
        return this.return5Y;
    }


    public void setReturn5Y(final BigDecimal return5y) {
        this.return5Y = return5y;
    }


    public BigDecimal getReturn10Y() {
        return this.return10Y;
    }


    public void setReturn10Y(final BigDecimal return10y) {
        this.return10Y = return10y;
    }


    public BigDecimal getStdDev3Y() {
        return this.stdDev3Y;
    }


    public void setStdDev3Y(final BigDecimal stdDev3Y) {
        this.stdDev3Y = stdDev3Y;
    }
}