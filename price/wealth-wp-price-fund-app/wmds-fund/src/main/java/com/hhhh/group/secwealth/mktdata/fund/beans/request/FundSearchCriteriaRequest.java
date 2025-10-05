
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;


public class FundSearchCriteriaRequest extends Request {

    private String productType;
    private List<SearchCriteria> predefinedCriterias;
    private String[] minMaxCriteriaKeys;
    private String[] listCriteriaKeys;
    private String channelRestrictCode;
    private String restrOnlScribInd;

    
    public String getProductType() {
        return this.productType;
    }

    
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    
    public List<SearchCriteria> getPredefinedCriterias() {
        return this.predefinedCriterias;
    }

    
    public void setPredefinedCriterias(final List<SearchCriteria> predefinedCriterias) {
        this.predefinedCriterias = predefinedCriterias;
    }

    
    public String[] getMinMaxCriteriaKeys() {
        return this.minMaxCriteriaKeys;
    }

    
    public void setMinMaxCriteriaKeys(final String[] minMaxCriteriaKeys) {
        this.minMaxCriteriaKeys = minMaxCriteriaKeys;
    }

    
    public String[] getListCriteriaKeys() {
        return this.listCriteriaKeys;
    }

    
    public void setListCriteriaKeys(final String[] listCriteriaKeys) {
        this.listCriteriaKeys = listCriteriaKeys;
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
