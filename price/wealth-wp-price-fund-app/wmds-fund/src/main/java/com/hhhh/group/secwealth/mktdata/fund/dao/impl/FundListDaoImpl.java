
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.criteria.Criterias;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtHoldings;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundListRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundListDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("fundListDao")
public class FundListDaoImpl extends FundCommonDao implements FundListDao {

    @Resource(name = "holdingAllocationMap")
    private Map<String, String> holdingAllocationMap;

    @SuppressWarnings("unchecked")
    @Override
    public List<UtProdInstm> searchProductList(final FundListRequest request) throws Exception {
        List<Integer> prodIds = getProdIdByProductKeys(request.getProductKeys(), request.getHeaders());
        if (ListUtil.isValid(prodIds)) {
            StringBuilder hql = new StringBuilder();
            hql.append("select utProd from UtProdInstm utProd");
            Map<String, Object> orMap = generateHqlOR(prodIds, "utProd.utProdInstmId.productId", "prodIds");
            appendSubHql(hql, "where", orMap);

            QueryUtil.buildCmbSearchHql(hql, request.getHeaders(), "utProd");

            LogUtil.debug(FundListDaoImpl.class, "create query for searchProductList is :  " + hql.toString());

            Query query = createQueryForHql(hql.toString());
            setQueryParamKey(query, orMap);
            try {
                List<UtProdInstm> utProdInstmList = query.getResultList();
                LogUtil.debugBeanToJson(FundListDaoImpl.class, "Result for searchProductList from DB", utProdInstmList);
                return utProdInstmList;
            } catch (Exception e) {
                LogUtil.error(FundListDaoImpl.class, "searchProductList from DB error", e);
                throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
            }
        }
        return ListUtils.EMPTY_LIST;
    }

    public Map<String, List<UTHoldingAlloc>> searchHoldingAllocation(final FundListRequest request) throws Exception {

        // validation if return
        // "assetAlloc,stockSectors,equityRegional,bondSectors,bondRegional",
        // default: true
        List<Criterias> criteriasList = request.getCriterias();
        Map<String, Boolean> criteriaFalseMap = new HashMap<>();
        if (ListUtil.isValid(criteriasList)) {
            for (Criterias cs : criteriasList) {
                String criteriaKey = this.holdingAllocationMap.get(cs.getCriteriaKey().toUpperCase());
                if (StringUtil.isValid(criteriaKey) && !cs.getCriteriaValue()) {
                    criteriaFalseMap.put(criteriaKey, cs.getCriteriaValue());
                }
            }
            if (criteriaFalseMap.size() == 5) {
                return null;
            }
        }

        List<String> performanceIds = getPerformanceIdByProductKeys(request.getProductKeys(), request.getHeaders());
        Map<String, List<UTHoldingAlloc>> map = new HashMap<String, List<UTHoldingAlloc>>();
        if (ListUtil.isInvalid(performanceIds)) {
            return null;
        }
        LogUtil.debug(FundListDaoImpl.class, "Enter into the searchHoldingAllocation");
        StringBuilder hql = new StringBuilder(" from UTHoldingAlloc utAlloc");
        Map<String, Object> orMap = generateHqlOR(performanceIds, "utAlloc.utHoldingAllocId.performanceId", "assetAllocProds");
        appendSubHql(hql, "where", orMap);
        List<String> holdingAllocClassType = new ArrayList<String>();
        if (!criteriaFalseMap.isEmpty() && criteriaFalseMap.size() > 0) {
            for (String key : criteriaFalseMap.keySet()) {
                holdingAllocClassType.add(key);
            }
            hql.append(" and utAlloc.utHoldingAllocId.holdingAllocClassType not in (:holdingAllocClassType)");
        }
        Query query = createQueryForHql(hql.toString());
        setQueryParamKey(query, orMap);
        if (!criteriaFalseMap.isEmpty() && criteriaFalseMap.size() > 0) {
            query.setParameter("holdingAllocClassType", holdingAllocClassType);
        }
        try {
            List<UTHoldingAlloc> resultList = query.getResultList();
            for (UTHoldingAlloc utHoldingAlloc : resultList) {
                String performanceId = utHoldingAlloc.getUtHoldingAllocId().getPerformanceId();
                List<UTHoldingAlloc> utHoldingAllocList = map.get(performanceId);
                if (utHoldingAllocList == null) {
                    utHoldingAllocList = new ArrayList<UTHoldingAlloc>();
                    map.put(performanceId, utHoldingAllocList);
                }
                utHoldingAllocList.add(utHoldingAlloc);
            }
            LogUtil.debugBeanToJson(FundListDaoImpl.class, "Result for search searchHoldingAllocation from DB", resultList);
            return map;
        } catch (Exception e) {
            LogUtil.error(FundListDaoImpl.class, "searchHoldingAllocation from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<UtHoldings>> searchTopTenHldgMap(final FundListRequest request, final String topTenHoldings)
        throws Exception {

        // validation if show "topTenHoldings"
        List<Criterias> criteriasList = request.getCriterias();
        if (null != criteriasList) {
            for (Criterias cs : criteriasList) {
                if (null != cs.getCriteriaKey() && topTenHoldings.equals(cs.getCriteriaKey()) && !cs.getCriteriaValue()) {
                    return null;
                }
            }
        }

        List<String> performanceIds = getPerformanceIdByProductKeys(request.getProductKeys(), request.getHeaders());
        LogUtil.debug(FundListDaoImpl.class, "Enter into the searchTopTenHldg");
        Map<String, List<UtHoldings>> map = new HashMap<String, List<UtHoldings>>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from UtHoldings");
        Map<String, Object> orMap = generateHqlOR(performanceIds, "utHoldingsId.performanceId", "topTenHldgProds");
        appendSubHql(hql, "where", orMap);
        hql.append(" order by utHoldingsId.holdingId");
        LogUtil.debug(FundListDaoImpl.class, "Hql query for searchTopTenHldg: {}", hql.toString());
        Query query = createQueryForHql(hql.toString());
        setQueryParamKey(query, orMap);
        try {
            List<UtHoldings> resultList = query.getResultList();
            for (UtHoldings fundHolding : resultList) {
                String performanceId = fundHolding.getUtHoldingsId().getPerformanceId();
                List<UtHoldings> holdingList = map.get(performanceId);
                if (holdingList == null) {
                    holdingList = new ArrayList<UtHoldings>();
                    map.put(performanceId, holdingList);
                }
                holdingList.add(fundHolding);
            }
            LogUtil.debugBeanToJson(FundListDaoImpl.class, "Result for searchTopTenHldg from DB", resultList);
            return map;
        } catch (Exception e) {
            LogUtil.error(FundListDaoImpl.class, "searchTopTenHldg from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public Map<Integer, List<String>> searchChanlFund(final String chanlRestCde) throws Exception {
        Map<Integer, List<String>> chanlMap = searchChanlFunds(chanlRestCde);
        return chanlMap;
    }

}