
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LocaleMappingUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.dao.CategoryOverviewDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;


@Repository("categoryOverviewDao")
public class CategoryOverviewDaoImpl extends FundCommonDao implements CategoryOverviewDao {

    @Resource(name = "categoryOverviewOrderMap")
    private Map<String, String> categoryOverviewOrderMap;

    @Autowired
    @Qualifier("localeMappingUtil")
    private LocaleMappingUtil localeMappingUtil;

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getCategoryOverviewList(final String productType, final String locale, final String countryCode,
        final String groupMember, final String channelRestrictCode, final String restrOnlScribInd) throws Exception {
        LogUtil.debug(CategoryOverviewDaoImpl.class, "getCategoryOverview, productType: {}", productType);

        StringBuilder hql = new StringBuilder(
            "select t.categoryLevel1Code, t.categoryLevel1Name1, t.categoryLevel1Name2, t.categoryLevel1Name3, t.categoryCode, t.categoryName1, t.categoryName2, t.categoryName3, ");
        hql.append("round(avg(t.return1yr), 5), round(avg(t.return3yr), 5), round(avg(t.return5yr), 5), round(avg(t.return10yr), 5), round(avg(t.stdDev3Yr), 5) ");
        hql.append("from UTProdCatOverview t where t.productType = :productType ");

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = t.utProdCatOverviewId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            if (null != restrictGroupHql) {
                hql.append("and not exists (").append(restrictGroupHql).append(") ");
            }
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            hql.append("and nvl(t.restrOnlScribInd,' ') != :restrOnlScribInd ");
        }
        // GROUP BY
        hql.append("group by t.categoryCode, t.categoryName1, t.categoryName2, t.categoryName3, t.categorySequencenNum, ");
        hql.append("t.categoryLevel1Code, t.categoryLevel1Name1, t.categoryLevel1Name2, t.categoryLevel1Name3, t.categoryLevel1SequencenNum ");
        // ORDER BY
        hql.append(this.getOrderString(locale, countryCode, groupMember));
        LogUtil.debug(CategoryOverviewDaoImpl.class, "Created Query for getCategoryOverview: {}", hql);
        Query query = createQueryForHql(hql.toString());
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", channelRestrictCode);
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter("restrOnlScribInd", restrOnlScribInd);
        }
        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(CategoryOverviewDaoImpl.class, "getCategoryOverview from DB: {}", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(CategoryOverviewDaoImpl.class, "getCategoryOverview from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    private String getOrderString(final String locale, final String countryCode, final String groupMember) throws Exception {
        StringBuffer order = new StringBuffer(" ORDER BY");
        String orderKey = this.categoryOverviewOrderMap.get(countryCode + CommonConstants.SYMBOL_UNDERLINE + groupMember);
        if (StringUtil.isValid(orderKey)) {
            order.append(" t.").append(orderKey);
        } else {
            int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
            order.append(" t.categoryLevel1SequencenNum");
            if (CommonConstants.SIMPLIFIED_CHINESE == index) {
                order.append(", NLSSORT(t.categoryLevel1Name" + (index + 1) + ",'NLS_SORT=SCHINESE_PINYIN_M')");
            } else if (CommonConstants.TRADITIONAL_CHINESE == index) {
                order.append(", NLSSORT(t.categoryLevel1Name" + (index + 1) + ",'NLS_SORT=TCHINESE_STROKE_M')");
            } else {
                order.append(", t.categoryLevel1Name" + (index + 1));
            }
            order.append(", t.categorySequencenNum");
            if (CommonConstants.SIMPLIFIED_CHINESE == index) {
                order.append(", NLSSORT(t.categoryName" + (index + 1) + ",'NLS_SORT=SCHINESE_PINYIN_M')");
            } else if (CommonConstants.TRADITIONAL_CHINESE == index) {
                order.append(", NLSSORT(t.categoryName" + (index + 1) + ",'NLS_SORT=TCHINESE_STROKE_M')");
            } else {
                order.append(", t.categoryName" + (index + 1));
            }
            order.append(", t.categoryCode");
        }
        LogUtil.debug(CategoryOverviewDaoImpl.class, "Created Query for getCategoryOverview order key: {} ,order by: {}", orderKey,
            order.toString());
        return order.toString();
    }
}
