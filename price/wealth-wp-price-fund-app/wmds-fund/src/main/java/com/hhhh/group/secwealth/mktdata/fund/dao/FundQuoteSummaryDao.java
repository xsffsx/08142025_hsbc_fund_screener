
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;



public interface FundQuoteSummaryDao {

    public abstract List<UtProdInstm> searchProfile(FundQuoteSummaryRequest request) throws Exception;

    public Map<Integer, List<String>> searchChanlFund(String chanlRestCde) throws Exception;

}