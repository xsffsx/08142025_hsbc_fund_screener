
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundList.FundListProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class FundListResponse {

    
    private int totalNumberOfRecords;

    
    private List<FundListProduct> products;

    
    @JsonIgnore
    private List<SearchProduct> searchProductList;

    
    // private String entityUpdatedTime;

    
    public int getTotalNumberOfRecords() {
        return this.totalNumberOfRecords;
    }

    
    public void setTotalNumberOfRecords(final int totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }

    
    public List<FundListProduct> getProducts() {
        return this.products;
    }

    
    public void setProducts(final List<FundListProduct> products) {
        this.products = products;
    }

    
    public List<SearchProduct> getSearchProductList() {
        return this.searchProductList;
    }

    
    public void setSearchProductList(final List<SearchProduct> searchProductList) {
        this.searchProductList = searchProductList;
    }

}
