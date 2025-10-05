
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.HoldingAllocationRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.HoldingAllocationDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;


@Repository("holdingAllocationDao")
public class HoldingAllocationDaoImpl extends FundCommonDao implements HoldingAllocationDao {

    @Override
    public List<Object[]> searchHoldingAllocList(final HoldingAllocationRequest request) throws Exception {
        String market = request.getMarket();
        String prodAltNum = request.getProdAltNum();
        String productType = request.getProductType();
        Map<String, String> headers = request.getHeaders();
        StringBuilder hql = new StringBuilder(
            "select distinct u.performanceId, u.numberOfBondHoldings, u.numberOfStockHoldings, u.holdingAllocationPortfolioDate from UtProdInstm u");
        hql.append(" where u.market= :market and u.productType = :productType and u.symbol = :prodAltNum");

        QueryUtil.buildCmbSearchHql(hql, headers, "u");

        Query query = createQueryForHql(hql.toString());

        query.setParameter("market", market);
        query.setParameter("prodAltNum", prodAltNum);
        query.setParameter("productType", productType);
        List<Object[]> holdingAllocList = query.getResultList();
        LogUtil.debugBeanToJson(HoldingAllocationDaoImpl.class, "Result for searchHoldingAllocation from DB", holdingAllocList);

        return holdingAllocList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UTHoldingAlloc> searchHoldingAllocation(final String performanceId) throws Exception {

        if (StringUtil.isInvalid(performanceId)) {
            return null;
        }

        StringBuilder hql = new StringBuilder(
            "from UTHoldingAlloc holdAlloc where holdAlloc.utHoldingAllocId.performanceId in (:performanceId)");
        LogUtil.debug(HoldingAllocationDaoImpl.class, "Hql query for searchHoldingAllocation performanceId: {}", performanceId);
        Query query = createQueryForHql(hql.toString());
        LogUtil.debug(HoldingAllocationDaoImpl.class, "Hql query for searchHoldingAllocation: {}", hql.toString());
        query.setParameter("performanceId", performanceId);

        List<UTHoldingAlloc> holdingAllocList = query.getResultList();
        LogUtil.debugBeanToJson(HoldingAllocationDaoImpl.class, "Result for searchHoldingAllocation from DB", holdingAllocList);

        return holdingAllocList;
    }


}