
package com.hhhh.group.secwealth.mktdata.fund.dao;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtHoldings;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.UtSvceHelper;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchResultRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;

import java.util.List;
import java.util.Map;



public interface FundSearchResultDao {

    public List<UtProdInstm> searchFund(FundSearchResultRequest request, Map<String, List<Integer>> switchOutFundMap,
        List<String> hhhhRiskLevlList, List<ProductKey> prodIds_wpcWebService, final String countryCode, final String groupMember,
        Map<String, Boolean> holdingsValueMap, boolean flag, int catLevel) throws Exception;

    public Integer searchTotalCount(FundSearchResultRequest request, Map<String, List<Integer>> switchOutFundMap,
        List<String> hhhhRiskLevlList, List<ProductKey> prodIds_wpcWebService, final String countryCode, final String groupMember,
        Map<String, Boolean> holdingsValueMap, boolean flag, int catLevel) throws Exception;

    public Map<Integer, List<UtCatAsetAlloc>> searchHldgsMap(List<Integer> prodIds_DB, final String classTypeCode) throws Exception;

    public Map<String, List<UtHoldings>> searchTop5HldgMap(List<String> performanceIds_DB, Map<String, Boolean> holdingsValueMap)
        throws Exception;

    public List<UtSvceHelper> getUtSvce(List<String> performanceIds_DB) throws Exception;

    public Map<String, List<UTHoldingAlloc>> searchHoldingAllocation(List<String> performanceIds_DB,
        Map<String, Boolean> holdingsValueMap) throws Exception;

    public List<Integer> appendSqlForCoreSwitchOut(String[] fields, String channelRestrictCode, String restrOnlScribInd, Map<String,String> headers)
        throws Exception;

    public List<Integer> appendSqlForUKSwitchOut(String[] fields, Map<String,String> headers) throws Exception;

    public List<ProductKey> getProdAltNumFromDB(FundSearchResultRequest request, String field) throws Exception;

    public Map<Integer, List<String>> searchChanlFund(String chanlRestCde) throws Exception;

}