
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchListCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchMinMaxCriteria;


public class FundSearchCriteriaResponse {


    private List<SearchMinMaxCriteria> minMaxCriterias;


    private List<SearchListCriteria> listCriterias;


    public List<SearchMinMaxCriteria> getMinMaxCriterias() {
        return this.minMaxCriterias;
    }


    public void setMinMaxCriterias(final List<SearchMinMaxCriteria> minMaxCriterias) {
        this.minMaxCriterias = minMaxCriterias;
    }


    public List<SearchListCriteria> getListCriterias() {
        return this.listCriterias;
    }


    public void setListCriterias(final List<SearchListCriteria> listCriterias) {
        this.listCriterias = listCriterias;
    }

}
