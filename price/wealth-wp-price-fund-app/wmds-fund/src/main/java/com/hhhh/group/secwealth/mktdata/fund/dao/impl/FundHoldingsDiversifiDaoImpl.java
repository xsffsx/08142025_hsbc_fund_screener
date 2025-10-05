
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundHoldingsDiversifiRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundHoldingsDiversifiDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundSearchResultDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository("fundHoldingsDiversifiDao")
public class FundHoldingsDiversifiDaoImpl extends FundCommonDao implements FundHoldingsDiversifiDao {

    @Override
    public List<Integer> searchProductId(final FundHoldingsDiversifiRequest request) throws Exception {
        List<ProductKey> productKeys = request.getProductKeys();
        return getProdIdByProductKeys(productKeys, request.getHeaders());
    }

    @SuppressWarnings("unchecked")
    public List<UtProdInstm> searchHoldingDiversification(final List<Integer> productIds) throws Exception {
        LogUtil.debug(FundHoldingsDiversifiDaoImpl.class, "control entering into the searchHoldingDiversification.");
        if (productIds.isEmpty()) {
            return new ArrayList<UtProdInstm>();
        }
        LogUtil.debug(FundHoldingsDiversifiDaoImpl.class, "Got productIds {}", productIds);
        StringBuilder hql = new StringBuilder("from UtProdInstm utProd where utProd.utProdInstmId.productId in (:productIds) ");
        LogUtil.debug(FundHoldingsDiversifiDaoImpl.class, "Hql query for searchHoldingDiversification: {}", hql.toString());

        Query query = createQueryForHql(hql.toString());
        query.setParameter("productIds", productIds);
        LogUtil.debug(FundHoldingsDiversifiDaoImpl.class, "Hql query for searchHoldingDiversification Parameter: {}", productIds);
        try {
            List<UtProdInstm> fundList = query.getResultList();
            LogUtil.debugBeanToJson(FundCompareDaoImpl.class, "Result for searchHoldingDiversification from DB", fundList);

            return fundList;
        } catch (Exception e) {
            LogUtil.error(FundHoldingsDiversifiDaoImpl.class, "searchHoldingDiversification from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }


    @SuppressWarnings("unchecked")
    public List<UtCatAsetAlloc> searchAllocation(final List<Integer> prodIds_DB, final String classTypeCode) throws Exception {
        if (prodIds_DB.isEmpty()) {
            return new ArrayList<UtCatAsetAlloc>();
        }
        LogUtil.debug(FundSearchResultDao.class, "Enter into the searchAllocationHodg");
        StringBuilder hql = new StringBuilder();
        hql.append("from UtCatAsetAlloc");
        hql.append(" where utCatAsetAllocId.classTypeCode = '" + classTypeCode + "'");
        Map<String, Object> orMap = generateHqlOR(prodIds_DB, "utCatAsetAllocId.productId", "allocationProdIds");
        appendSubHql(hql, "and", orMap);
        LogUtil.debug(FundSearchResultDao.class, "Hql query for " + classTypeCode + " allocation:" + hql.toString());
        Query query = createQueryForHql(hql.toString());
        setQueryParamKey(query, orMap);
        try {
            List<UtCatAsetAlloc> resultList = query.getResultList();
            LogUtil.debugBeanToJson(FundCompareDaoImpl.class, "Result for " + classTypeCode + " allocation from DB", resultList);
            return resultList;
        } catch (Exception e) {
            LogUtil.error(FundHoldingsDiversifiDaoImpl.class, "searchAllocation from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

}
