
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;


public class CategoryOverviewRequest extends Request {

    private String productType;
    private String productSubTypeCode;
    private String includeNull;
    private String channelRestrictCode;
    private String restrOnlScribInd;


    public String getProductType() {
        return this.productType;
    }


    public void setProductType(final String productType) {
        this.productType = productType;
    }


    public String getProductSubTypeCode() {
        return this.productSubTypeCode;
    }


    public void setProductSubTypeCode(final String productSubTypeCode) {
        this.productSubTypeCode = productSubTypeCode;
    }


    public String getIncludeNull() {
        return this.includeNull;
    }


    public void setIncludeNull(final String includeNull) {
        this.includeNull = includeNull;
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
