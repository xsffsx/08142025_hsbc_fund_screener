
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareIndexRequest;


public interface FundCompareIndexDao {


    public List<Object[]> getCompareIndexList(FundCompareIndexRequest request) throws Exception;

}
