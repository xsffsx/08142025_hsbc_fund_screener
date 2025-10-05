
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchListCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchMinMaxCriteria;


public class SearchCriteriaRespMsg {

    
    private List<SearchMinMaxCriteria> minMaxCriteria;

    
    private List<SearchListCriteria> listCriteria;

    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("minMaxCriteria", this.minMaxCriteria).append("listCriteria", this.listCriteria).toString();
    }

    
    public List<SearchMinMaxCriteria> getMinMaxCriteria() {
        return this.minMaxCriteria;
    }

    
    public void setMinMaxCriteria(final List<SearchMinMaxCriteria> minMaxCriteria) {
        this.minMaxCriteria = minMaxCriteria;
    }

    
    public List<SearchListCriteria> getListCriteria() {
        return this.listCriteria;
    }

    
    public void setListCriteria(final List<SearchListCriteria> listCriteria) {
        this.listCriteria = listCriteria;
    }

}
