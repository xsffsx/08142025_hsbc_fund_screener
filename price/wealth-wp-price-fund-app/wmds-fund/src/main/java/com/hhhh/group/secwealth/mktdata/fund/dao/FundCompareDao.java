
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareRequest;



public interface FundCompareDao {

    public List<UtProdInstm> searchForCompare(FundCompareRequest request) throws Exception;

}