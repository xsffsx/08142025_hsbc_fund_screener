
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.performancereturn.PerformanceReturn;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class PerformanceReturnResponse {

    private List<PerformanceReturn> products;

    
    @JsonIgnore
    private List<SearchProduct> searchProductList;

    
    public List<PerformanceReturn> getProducts() {
        return this.products;
    }

    
    public void setProducts(final List<PerformanceReturn> products) {
        this.products = products;
    }

    
    public List<SearchProduct> getSearchProductList() {
        return this.searchProductList;
    }

    
    public void setSearchProductList(final List<SearchProduct> searchProductList) {
        this.searchProductList = searchProductList;
    }

}
