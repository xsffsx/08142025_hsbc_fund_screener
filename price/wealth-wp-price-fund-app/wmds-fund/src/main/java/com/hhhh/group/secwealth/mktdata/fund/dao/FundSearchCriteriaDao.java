
package com.hhhh.group.secwealth.mktdata.fund.dao;

import com.hhhh.group.secwealth.mktdata.common.criteria.ListValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.MinMaxValue;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchCriteriaRequest;

import java.util.List;



public interface FundSearchCriteriaDao {

    public List<MinMaxValue> searchMinMaxCriteria(FundSearchCriteriaRequest request,
        String minMaxCriteria, String countryCode, String groupMember)
        throws Exception;

    public List<ListValue> searchListCriteria(FundSearchCriteriaRequest request, String listField,
        String locale, String countryCode, String groupMember)
        throws Exception;

}