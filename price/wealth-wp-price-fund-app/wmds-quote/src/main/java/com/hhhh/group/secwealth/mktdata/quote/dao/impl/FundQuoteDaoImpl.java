/*
 */
package com.hhhh.group.secwealth.mktdata.quote.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.quote.dao.FundQuoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("fundQuoteDao")
public class FundQuoteDaoImpl implements FundQuoteDao {

    @Autowired
    @Qualifier("baseDao")
    private BaseDao baseDao;


    public List<UtProdInstm> search(final List<InternalProductKey> ipkList, final Map<String, String> headers) throws Exception {
        if (ListUtil.isValid(ipkList)) {
            StringBuilder hql = new StringBuilder();
            hql.append(" from UtProdInstm u");
            for (int i = 0; i < ipkList.size(); i++) {
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

            LogUtil.debug(FundQuoteDaoImpl.class, "Hql query for fundQuoteDetail:" + hql.toString());

            EntityManager entityManager = this.baseDao.getEntityManager();
            Query query = entityManager.createQuery(hql.toString());
            for (int i = 0; i < ipkList.size(); i++) {
                query.setParameter("market" + i, ipkList.get(i).getCountryTradableCode());
                query.setParameter("productType" + i, ipkList.get(i).getProductType());
                query.setParameter("prodAltNum" + i, ipkList.get(i).getProdAltNum());
            }
            List<UtProdInstm> utProdInstmList = query.getResultList();
            return utProdInstmList;
        }
        return null;
    }

    @Override
    public Map<Integer, List<String>> searchChanlFund(final String chanlRestCde) throws Exception {
        LogUtil.debug(FundQuoteDaoImpl.class, "Enter into the search channl fund");

        StringBuilder hql = new StringBuilder(
            "select distinct chanl.utProdChanlId.prodId, chanl.allowBuyProdInd, chanl.allowSellProdInd from UtProdChanl chanl where chanl.channelCde = :channelCde");
        EntityManager entityManager = this.baseDao.getEntityManager();
        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("channelCde", chanlRestCde);
        List<Object[]> chanlProductList = query.getResultList();

        Map<Integer, List<String>> chanlMap = new HashMap<Integer, List<String>>();
        for (Object[] chanl : chanlProductList) {
            List<String> indicate = new ArrayList<String>();
            indicate.add(chanl[1].toString());
            indicate.add(chanl[2].toString());
            chanlMap.put(Integer.parseInt(chanl[0].toString()), indicate);
        }
        return chanlMap;
    }

}
