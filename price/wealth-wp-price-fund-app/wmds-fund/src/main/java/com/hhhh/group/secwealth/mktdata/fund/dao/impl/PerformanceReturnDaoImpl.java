
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.PerformanceReturnRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.PerformanceReturnDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;


@Repository("performanceReturnDao")
public class PerformanceReturnDaoImpl extends FundCommonDao implements PerformanceReturnDao {

    @Autowired
    @Qualifier("internalProductKeyUtil")
    private InternalProductKeyUtil internalProductKeyUtil;

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> searchProductList(final PerformanceReturnRequest request) throws Exception {
        List<Integer> prodIds = getProdIdByProductKeys(request.getProductKeys(), request.getHeaders());

        if (ListUtil.isInvalid(prodIds)) {
            return ListUtils.EMPTY_LIST;
        }

        StringBuilder hql = new StringBuilder();
        hql.append("select utProd.prodName,utProd.prodPllName,utProd.prodSllName,utProd.symbol from UtProdInstm utProd where utProd.utProdInstmId.productId in (:prodIds)");
        Query query = createQueryForHql(hql.toString());
        LogUtil.debug(PerformanceReturnDaoImpl.class, "create query for searchProductList is :  " + hql.toString());
        query.setParameter("prodIds", prodIds);
        List<Object[]> resultList = query.getResultList();
        LogUtil.debugBeanToJson(PerformanceReturnDaoImpl.class, "Result for searchProductList from DB", resultList);
        return resultList;
    }

}