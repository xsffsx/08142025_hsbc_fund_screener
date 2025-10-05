
package com.hhhh.group.secwealth.mktdata.fund.beans.helper;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class UtHoldingsHelper {

    private String performanceId;
    private String holdingName;
    private String holdingCompanyName;
    private BigDecimal weight;
    private String productId;
    private String holdingId;
    // GEO
    private BigDecimal categoryAllocation;
    private Boolean isCategoryShortPosition;
    // SIC
    private BigDecimal fundAllocation;
    private Boolean isFundShortPosition;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("performanceId", this.performanceId).append("holdingName", this.holdingName)
            .append("holdingCompanyName", this.holdingCompanyName).append("weight", this.weight)
            .append("productId", this.productId).append("holdingId", this.holdingId)
            .append("categoryAllocation", this.categoryAllocation).append("isCategoryShortPosition", this.isCategoryShortPosition)
            .append("fundAllocation", this.fundAllocation).append("isFundShortPosition", this.isFundShortPosition).toString();
    }



    public String getPerformanceId() {
        return this.performanceId;
    }


    public void setPerformanceId(final String performanceId) {
        this.performanceId = performanceId;
    }


    public String getHoldingName() {
        return this.holdingName;
    }


    public void setHoldingName(final String holdingName) {
        this.holdingName = holdingName;
    }


    public String getHoldingCompanyName() {
        return this.holdingCompanyName;
    }


    public void setHoldingCompanyName(final String holdingCompanyName) {
        this.holdingCompanyName = holdingCompanyName;
    }


    public BigDecimal getWeight() {
        return this.weight;
    }


    public void setWeight(final BigDecimal weight) {
        this.weight = weight;
    }


    public String getHoldingId() {
        return this.holdingId;
    }


    public void setHoldingId(final String holdingId) {
        this.holdingId = holdingId;
    }


    public String getProductId() {
        return this.productId;
    }


    public void setProductId(final String productId) {
        this.productId = productId;
    }


    public BigDecimal getCategoryAllocation() {
        return this.categoryAllocation;
    }


    public void setCategoryAllocation(final BigDecimal categoryAllocation) {
        this.categoryAllocation = categoryAllocation;
    }


    public Boolean getIsCategoryShortPosition() {
        return this.isCategoryShortPosition;
    }


    public void setIsCategoryShortPosition(final Boolean isCategoryShortPosition) {
        this.isCategoryShortPosition = isCategoryShortPosition;
    }


    public BigDecimal getFundAllocation() {
        return this.fundAllocation;
    }


    public void setFundAllocation(final BigDecimal fundAllocation) {
        this.fundAllocation = fundAllocation;
    }


    public Boolean getIsFundShortPosition() {
        return this.isFundShortPosition;
    }


    public void setIsFundShortPosition(final Boolean isFundShortPosition) {
        this.isFundShortPosition = isFundShortPosition;
    }
}
