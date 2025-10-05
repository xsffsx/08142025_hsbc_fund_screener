package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;



public class TopAndBottmMufCatRequest extends Request {

    private String market;
    private String assetAllocation;
    private String timeScale;
    private String productType;
    private String productSubType;
    private String channelRestrictCode;
    private String restrOnlScribInd;

    
    public String getMarket() {
        return this.market;
    }

    
    public void setMarket(final String market) {
        this.market = market;
    }

    
    public String getAssetAllocation() {
        return this.assetAllocation;
    }

    
    public void setAssetAllocation(final String assetAllocation) {
        this.assetAllocation = assetAllocation;
    }

    
    public String getTimeScale() {
        return this.timeScale;
    }

    
    public void setTimeScale(final String timeScale) {
        this.timeScale = timeScale;
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

    
    public String getChannelRestrictCode() {
        return this.channelRestrictCode;
    }

    
    public void setChannelRestrictCode(final String channelRestrictCode) {
        this.channelRestrictCode = channelRestrictCode;
    }

    
    public String getRestrOnlScribInd() {
        return this.restrOnlScribInd;
    }

    
    public void setRestrOnlScribInd(final String restrOnlScribInd) {
        this.restrOnlScribInd = restrOnlScribInd;
    }

}
