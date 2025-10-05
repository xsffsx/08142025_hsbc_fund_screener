package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmmufcat;

import java.math.BigDecimal;

public class FundCategoriesItem {

    public String categoriesCode;
    public String categoriesName;
    public Integer numberOfProducts;
    public BigDecimal trailingTotalReturn;
    public BigDecimal averageRisk;

    
    public String getCategoriesCode() {
        return this.categoriesCode;
    }

    
    public void setCategoriesCode(final String categoriesCode) {
        this.categoriesCode = categoriesCode;
    }

    
    public String getCategoriesName() {
        return this.categoriesName;
    }

    
    public void setCategoriesName(final String categoriesName) {
        this.categoriesName = categoriesName;
    }

    
    public Integer getNumberOfProducts() {
        return this.numberOfProducts;
    }

    
    public void setNumberOfProducts(final Integer numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
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
