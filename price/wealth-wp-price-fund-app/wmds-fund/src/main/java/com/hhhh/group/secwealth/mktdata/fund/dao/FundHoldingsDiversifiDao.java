
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundHoldingsDiversifiRequest;



public interface FundHoldingsDiversifiDao {

    public List<Integer> searchProductId(FundHoldingsDiversifiRequest request) throws Exception;

    public List<UtProdInstm> searchHoldingDiversification(List<Integer> prodIds_DB) throws Exception;

    public List<UtCatAsetAlloc> searchAllocation(final List<Integer> prodIds_DB, final String classTypeCode) throws Exception;

}