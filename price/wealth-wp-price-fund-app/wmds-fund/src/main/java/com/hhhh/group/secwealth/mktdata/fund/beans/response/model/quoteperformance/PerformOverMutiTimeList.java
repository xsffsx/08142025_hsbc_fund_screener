package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteperformance;

import java.math.BigDecimal;

public class PerformOverMutiTimeList {

    private String itemName;
    private BigDecimal return1Mth;
    private BigDecimal return3Mth;
    private BigDecimal return6Mth;
    private BigDecimal return1Yr;
    private BigDecimal return3Yr;
    private BigDecimal return5Yr;

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }


    public BigDecimal getReturn1Mth() {
        return this.return1Mth;
    }


    public void setReturn1Mth(final BigDecimal return1Mth) {
        this.return1Mth = return1Mth;
    }


    public BigDecimal getReturn3Mth() {
        return this.return3Mth;
    }


    public void setReturn3Mth(final BigDecimal return3Mth) {
        this.return3Mth = return3Mth;
    }


    public BigDecimal getReturn6Mth() {
        return this.return6Mth;
    }


    public void setReturn6Mth(final BigDecimal return6Mth) {
        this.return6Mth = return6Mth;
    }


    public BigDecimal getReturn1Yr() {
        return this.return1Yr;
    }


    public void setReturn1Yr(final BigDecimal return1Yr) {
        this.return1Yr = return1Yr;
    }


    public BigDecimal getReturn3Yr() {
        return this.return3Yr;
    }


    public void setReturn3Yr(final BigDecimal return3Yr) {
        this.return3Yr = return3Yr;
    }


    public BigDecimal getReturn5Yr() {
        return this.return5Yr;
    }


    public void setReturn5Yr(final BigDecimal return5Yr) {
        this.return5Yr = return5Yr;
    }

}