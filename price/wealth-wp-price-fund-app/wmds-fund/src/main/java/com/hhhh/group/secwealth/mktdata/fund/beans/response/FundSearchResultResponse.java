
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.common.Pagination;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult.FundSearchResultProduct;



public class FundSearchResultResponse {



    private Pagination pagination;


    private List<FundSearchResultProduct> products;


    private String entityUpdatedTime;


    public Pagination getPagination() {
        return this.pagination;
    }


    public void setPagination(final Pagination pagination) {
        this.pagination = pagination;
    }


    public List<FundSearchResultProduct> getProducts() {
        return this.products;
    }


    public void setProducts(final List<FundSearchResultProduct> products) {
        this.products = products;
    }


    public String getEntityUpdatedTime() {
        return this.entityUpdatedTime;
    }


    public void setEntityUpdatedTime(final String entityUpdatedTime) {
        this.entityUpdatedTime = entityUpdatedTime;
    }

}
