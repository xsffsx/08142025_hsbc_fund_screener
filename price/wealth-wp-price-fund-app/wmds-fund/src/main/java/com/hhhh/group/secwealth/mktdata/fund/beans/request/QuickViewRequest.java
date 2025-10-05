
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.Operator;


public class QuickViewRequest extends Request {

    private String productType;
    private List<SearchCriteria> criterias;
    private Boolean returnOnlyNumberOfMatches;
    private String channelRestrictCode;
    private String restrOnlScribInd;
    private String prodStatCde;



    
    public String getProductType() {
        return this.productType;
    }

    
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    
    public List<SearchCriteria> getCriterias() {
        return this.criterias;
    }

    
    public void setCriterias(final List<SearchCriteria> criterias) {
        this.criterias = criterias;
    }

    
    public Boolean getReturnOnlyNumberOfMatches() {
        return this.returnOnlyNumberOfMatches;
    }

    
    public void setReturnOnlyNumberOfMatches(final Boolean returnOnlyNumberOfMatches) {
        this.returnOnlyNumberOfMatches = returnOnlyNumberOfMatches;
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

    
    public String getProdStatCde() {
        return this.prodStatCde;
    }

    
    public void setProdStatCde(final String prodStatCde) {
        this.prodStatCde = prodStatCde;
    }

    public String getCriteriasString() {
        StringBuffer bf = new StringBuffer();
        if (ListUtil.isValid(this.criterias)) {
            for (int i = 0; i < this.criterias.size(); i++) {
                if (i != 0) {
                    bf.append(Constants.CRITERIA_SEPARATOR);
                }
                bf.append(this.criterias.get(i).getCriteriaKey()).append(Constants.CHAINKEY_SEPARATOR)
                    .append(this.criterias.get(i).getCriteriaValue()).append(Constants.CHAINKEY_SEPARATOR)
                    .append(Operator.valueOf(this.criterias.get(i).getOperator().toUpperCase()).getText());
            }
        }
        return bf.toString();
    }
}
