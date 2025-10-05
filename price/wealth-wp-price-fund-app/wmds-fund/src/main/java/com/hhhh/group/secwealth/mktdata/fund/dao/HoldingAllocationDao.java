
package com.hhhh.group.secwealth.mktdata.fund.dao;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.HoldingAllocationRequest;



public interface HoldingAllocationDao {

    public List<Object[]> searchHoldingAllocList(HoldingAllocationRequest request) throws Exception;

    public List<UTHoldingAlloc> searchHoldingAllocation(String performanceId) throws Exception;

}