
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundCompareDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;


@Repository("fundCompareDao")
public class FundCompareDaoImpl extends FundCommonDao implements FundCompareDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<UtProdInstm> searchForCompare(final FundCompareRequest request) throws Exception {
        List<Integer> prodIds = getProdIdByProductKeys(request.getProductKeys(), request.getHeaders());
        if (ListUtil.isValid(prodIds)) {
            StringBuilder hql = new StringBuilder();
            hql.append("select utProd from UtProdInstm utProd");
            Map<String, Object> orMap = generateHqlOR(prodIds, "utProd.utProdInstmId.productId", "prodIds");
            appendSubHql(hql, "where", orMap);

            QueryUtil.buildCmbSearchHql(hql,request.getHeaders(),"utProd");

            LogUtil.debug(FundCompareDaoImpl.class, "create query for searchForCompare is :  " + hql.toString());

            Query query = createQueryForHql(hql.toString());
            setQueryParamKey(query, orMap);
            try {
                List<UtProdInstm> utProdInstmList = query.getResultList();
                LogUtil.debugBeanToJson(FundCompareDaoImpl.class, "Result for searchForCompare from DB", utProdInstmList);
                return utProdInstmList;
            } catch (Exception e) {
                LogUtil.error(FundCompareDaoImpl.class, "searchForCompare from DB error", e);
                throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
            }
        }
        return null;
    }
}
