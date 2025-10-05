
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundCompareIndexRequest;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundCompareIndexDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;


@Repository("fundCompareIndexDao")
public class FundCompareIndexDaoImpl extends FundCommonDao implements FundCompareIndexDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getCompareIndexList(final FundCompareIndexRequest request) throws Exception {
        String productType = request.getProductType();
        String countryCode = request.getCountryCode();
        String groupMember = request.getGroupMember();
        String locale = request.getLocale();
        LogUtil.debug(FundCompareIndexDaoImpl.class, "getCompareIndexList, productType: {}", productType);

        StringBuilder hql = new StringBuilder(
            "select distinct p.indexId, p.indexName, p.categoryCode, p.categoryName1, p.categoryName2, p.categoryName3, p.categorySequencenNum ");
        hql.append("from UtProdInstm P where p.categoryCode is not null and p.indexId is not null ");
        if (StringUtil.isValid(productType)) {
            hql.append("and p.productType = :productType");
        }

        QueryUtil.buildCmbSearchHql(hql,request.getHeaders(),"p");

        // ORDER BY
        hql.append(this.getOrderString(locale, countryCode, groupMember));
        LogUtil.debug(FundCompareIndexDaoImpl.class, "Created Query for getCompareIndexList: {}", hql);
        Query query = createQueryForHql(hql.toString());
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        try {
            List<Object[]> compareIndexList = query.getResultList();
            LogUtil.debugBeanToJson(FundCompareIndexDaoImpl.class, "getCompareIndexList from DB: {}", compareIndexList);
            return compareIndexList;
        } catch (Exception e) {
            LogUtil.error(FundCompareIndexDaoImpl.class, "getCompareIndexList from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    private String getOrderString(final String locale, final String countryCode, final String groupMember) throws Exception {
        StringBuffer order = new StringBuffer(" ORDER BY");
        int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
        order.append(" p.categorySequencenNum");
        if (CommonConstants.SIMPLIFIED_CHINESE == index) {
            order.append(", NLSSORT(p.categoryName" + (index + 1) + ",'NLS_SORT=SCHINESE_PINYIN_M')");
        } else if (CommonConstants.TRADITIONAL_CHINESE == index) {
            order.append(", NLSSORT(p.categoryName" + (index + 1) + ",'NLS_SORT=TCHINESE_STROKE_M')");
        } else {
            order.append(", p.categoryName" + (index + 1));
        }
        order.append(", p.indexName");
        LogUtil.debug(FundCompareIndexDaoImpl.class, "Created Query for FundCompareIndexDaoImpl order key: {} ,order by: {}", order,
            order.toString());
        return order.toString();
    }
}
