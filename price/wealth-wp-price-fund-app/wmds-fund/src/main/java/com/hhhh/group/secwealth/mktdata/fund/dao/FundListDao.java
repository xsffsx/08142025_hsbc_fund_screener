
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtHoldings;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;



public interface FundListDao {

    public List<UtProdInstm> searchProductList(FundListRequest request) throws Exception;

    public Map<String, List<UTHoldingAlloc>> searchHoldingAllocation(FundListRequest request) throws Exception;

    public Map<String, List<UtHoldings>> searchTopTenHldgMap(FundListRequest request, String assetAlloc) throws Exception;

    public Map<Integer, List<String>> searchChanlFund(String chanlRestCde) throws Exception;

}