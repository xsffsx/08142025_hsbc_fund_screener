
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;



public class TopAndBottmCatChartRequest extends Request {

    private String market;
    private String categoryCode;
    private String marketPeriod;
    private String productType;
    private String productSubType;


    public String getMarket() {
        return this.market;
    }


    public void setMarket(final String market) {
        this.market = market;
    }


    public String getCategoryCode() {
        return this.categoryCode;
    }


    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }


    public String getMarketPeriod() {
        return this.marketPeriod;
    }


    public void setMarketPeriod(final String marketPeriod) {
        this.marketPeriod = marketPeriod;
    }


    public String getProductType() {
        return this.productType;
    }


    public void setProductType(final String productType) {
        this.productType = productType;
    }


    public String getProductSubType() {
        return this.productSubType;
    }


    public void setProductSubType(final String productSubType) {
        this.productSubType = productSubType;
    }

}
