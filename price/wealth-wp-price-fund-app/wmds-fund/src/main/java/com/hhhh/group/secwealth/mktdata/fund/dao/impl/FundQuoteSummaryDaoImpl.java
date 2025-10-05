
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundQuoteSummaryRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundQuoteSummaryDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;


@Repository("fundQuoteSummaryDao")
public class FundQuoteSummaryDaoImpl extends FundCommonDao implements FundQuoteSummaryDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<UtProdInstm> searchProfile(final FundQuoteSummaryRequest request) throws Exception {

        String market = request.getMarket();
        String prodAltNum = request.getProdAltNum();
        String productType = request.getProductType();
        Map<String, String> headers = request.getHeaders();
        StringBuilder hql = new StringBuilder();

        hql.append("from UtProdInstm where market= :market and productType = :productType and symbol = :prodAltNum");

        QueryUtil.buildCmbSearchHql2(hql,headers);

        LogUtil.debug(FundQuoteSummaryDaoImpl.class, "Hql query for getFundQuoteSummary: {}", hql.toString());

        Query query = createQueryForHql(hql.toString());
        query.setParameter("market", market);
        query.setParameter("productType", productType);
        query.setParameter("prodAltNum", prodAltNum);
        LogUtil.debug(FundQuoteSummaryDaoImpl.class,
            "Hql query for getFundQuoteSummary Parameter: market: {}, productType: {}, prodAltNum: {}", market, productType,
            prodAltNum);
        try {
            List<UtProdInstm> resultList = query.getResultList();
            LogUtil.debugBeanToJson(FundQuoteSummaryDaoImpl.class, "Result for getFundQuoteSummary from DB", resultList);
            return resultList;
        } catch (Exception e) {
            LogUtil.error(FundQuoteSummaryDaoImpl.class, "searchProfile from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public Map<Integer, List<String>> searchChanlFund(final String chanlRestCde) throws Exception {
        Map<Integer, List<String>> chanlMap = searchChanlFunds(chanlRestCde);
        return chanlMap;
    }

}