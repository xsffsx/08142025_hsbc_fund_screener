
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompare.FundCompareProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class FundCompareResponse {

    private List<FundCompareProduct> products;

    
    @JsonIgnore
    private List<SearchProduct> searchProductList;


    
    public List<FundCompareProduct> getProducts() {
        return this.products;
    }

    
    public void setProducts(final List<FundCompareProduct> products) {
        this.products = products;
    }

    
    public List<SearchProduct> getSearchProductList() {
        return this.searchProductList;
    }

    
    public void setSearchProductList(final List<SearchProduct> searchProductList) {
        this.searchProductList = searchProductList;
    }

}
