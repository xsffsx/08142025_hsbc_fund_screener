
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.dao.AdvanceChartDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Repository("advanceChartDao")
public class AdvanceChartDaoImpl extends FundCommonDao implements AdvanceChartDao {



    public List<UtProdInstm> getUtProdInstmList(final List<ProductKey> productKeys, final Map<String, String> headers) throws Exception {
        StringBuilder hql = new StringBuilder();
        hql.append("from UtProdInstm u");

        for (int i = 0; i < productKeys.size(); i++) {
            if (i == 0) {
                hql.append(" where (u.market= :market" + i);
            } else {
                hql.append(" or (u.market= :market" + i);
            }
            hql.append(" and u.productType = :productType" + i);
            hql.append(" and u.symbol = :prodAltNum" + i);
            hql.append(" )");
        }

        QueryUtil.buildCmbSearchHql(hql, headers, "u");

        LogUtil.debug(FundSearchResultDaoImpl.class, "create query for getProdIdByProductKeys is : {}", hql.toString());
        Query query = createQueryForHql(hql.toString());
        for (int i = 0; i < productKeys.size(); i++) {
            query.setParameter("market" + i, productKeys.get(i).getMarket());
            query.setParameter("productType" + i, productKeys.get(i).getProductType());
            query.setParameter("prodAltNum" + i, productKeys.get(i).getProdAltNum());
        }
        List<UtProdInstm> UtProdInstmList = query.getResultList();
        LogUtil.debugBeanToJson(AdvanceChartDaoImpl.class, "result for UtProdInstm from DB is ", UtProdInstmList);
        return UtProdInstmList;
    }

    @Override
    public List<UtProdInstm> getEnableCacheProdInstmList() throws Exception {
        StringBuilder hql = new StringBuilder();
        hql.append(
            "select symbol, mStarID,undlIndexCde,prodName,market,productType from UtProdInstm where undlIndexCde is not null");
        Query query = createQueryForHql(hql.toString());
        List<Object[]> UtProdInstmList = query.getResultList();
        List<UtProdInstm> utProdInstmList = new ArrayList<UtProdInstm>();
        if (ListUtil.isValid(UtProdInstmList)) {
            for (Object[] resultRow : UtProdInstmList) {
                if (null != resultRow && resultRow.length > 0) {
                    String symbol = MiscUtil.safeString(resultRow[0]);
                    String mStarID = MiscUtil.safeString(resultRow[1]);
                    String undlIndexCde = MiscUtil.safeString(resultRow[2]);
                    String prodName = MiscUtil.safeString(resultRow[3]);
                    String market = MiscUtil.safeString(resultRow[4]);
                    String productType = MiscUtil.safeString(resultRow[5]);
                    UtProdInstm utProdInstm = new UtProdInstm();
                    utProdInstm.setmStarID(mStarID);
                    utProdInstm.setSymbol(symbol);
                    utProdInstm.setUndlIndexCde(undlIndexCde);
                    utProdInstm.setProdName(prodName);
                    utProdInstm.setMarket(market);
                    utProdInstm.setProductType(productType);
                    utProdInstmList.add(utProdInstm);
                }
            }

        }
        LogUtil.debugBeanToJson(AdvanceChartDaoImpl.class, "result for UtProdInstm from DB is ", UtProdInstmList);
        return utProdInstmList;
    }
}
