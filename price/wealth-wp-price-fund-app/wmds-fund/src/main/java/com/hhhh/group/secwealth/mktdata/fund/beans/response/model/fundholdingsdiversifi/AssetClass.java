package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi;

import java.math.BigDecimal;

public class AssetClass {

    private String assetClassName;
    private BigDecimal weighting;
    private BigDecimal allocationCategoryAverage;
    private Boolean weightingIndicator;
    private Boolean allocationCategoryAverageIndicator;

    public String getAssetClassName() {
        return this.assetClassName;
    }

    public void setAssetClassName(final String assetClassName) {
        this.assetClassName = assetClassName;
    }


    public BigDecimal getWeighting() {
        return this.weighting;
    }


    public void setWeighting(final BigDecimal weighting) {
        this.weighting = weighting;
    }


    public BigDecimal getAllocationCategoryAverage() {
        return this.allocationCategoryAverage;
    }


    public void setAllocationCategoryAverage(final BigDecimal allocationCategoryAverage) {
        this.allocationCategoryAverage = allocationCategoryAverage;
    }


    public Boolean getWeightingIndicator() {
        return this.weightingIndicator;
    }


    public void setWeightingIndicator(final Boolean weightingIndicator) {
        this.weightingIndicator = weightingIndicator;
    }


    public Boolean getAllocationCategoryAverageIndicator() {
        return this.allocationCategoryAverageIndicator;
    }


    public void setAllocationCategoryAverageIndicator(final Boolean allocationCategoryAverageIndicator) {
        this.allocationCategoryAverageIndicator = allocationCategoryAverageIndicator;
    }

}
