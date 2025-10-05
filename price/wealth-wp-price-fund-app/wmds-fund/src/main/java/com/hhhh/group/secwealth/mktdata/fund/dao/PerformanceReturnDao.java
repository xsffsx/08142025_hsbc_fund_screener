
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;



public interface PerformanceReturnDao {

    public List<Object[]> searchProductList(PerformanceReturnRequest request) throws Exception;

}