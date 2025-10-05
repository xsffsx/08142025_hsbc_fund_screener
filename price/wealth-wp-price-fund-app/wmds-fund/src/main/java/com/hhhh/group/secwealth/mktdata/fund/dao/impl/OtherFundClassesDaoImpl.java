
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.OtherFundClassesRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.OtherFundClassesDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;


@Repository("otherFundClassesDao")
public class OtherFundClassesDaoImpl extends FundCommonDao implements OtherFundClassesDao {

    public List<UtProdInstm> getUtProdInstmList(final OtherFundClassesRequest request) throws Exception {
        StringBuilder hql = new StringBuilder();

        hql.append(" select t1 from UtProdInstm t1, UtProdInstm t2");
        hql.append(" where t1.fundId = t2.fundId");
        hql.append(" and t1.utProdInstmId.productId <> t2.utProdInstmId.productId");
        hql.append(" and t2.market = :market");
        hql.append(" and t2.productType = :productType");
        hql.append(" and t2.symbol = :prodAltNum");

        QueryUtil.buildCmbSearchHql(hql, request.getHeaders(), "t2");

        LogUtil.debug(OtherFundClassesDaoImpl.class, "Hql query for getUtProdInstmList:" + hql.toString());
        Query query = createQueryForHql(hql.toString());
        query.setParameter("market", request.getMarket());
        query.setParameter("productType", request.getProductType());
        query.setParameter("prodAltNum", request.getProdAltNum());
        List<UtProdInstm> utProdInstmList = query.getResultList();
        return utProdInstmList;
    }

}
