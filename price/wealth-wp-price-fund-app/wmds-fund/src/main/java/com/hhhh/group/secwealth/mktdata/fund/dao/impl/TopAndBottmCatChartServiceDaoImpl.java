
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCat;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatId;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatTtlRtrnIndex;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryChartResponse;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.dao.TopAndBottmCatChartServiceDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;


@Repository("topAndBottmCatChartServiceDao")
public class TopAndBottmCatChartServiceDaoImpl extends FundCommonDao implements TopAndBottmCatChartServiceDao {

    @Override
    public TopAndBottomCategoryChartResponse searchUtProdCatList(final int index, final List<String> productTypes,
        final List<String> productSubTypes, final List<String> countryCriterias, final List<String> categoryCodeList,
        final TimeScale timeScale, final Integer limit) throws Exception {

        TopAndBottomCategoryChartResponse topAndBottomCategoryChartResponse = new TopAndBottomCategoryChartResponse();
        List<UtProdCat> topCategories =
            getPerformanceCategories(productTypes, productSubTypes, countryCriterias, categoryCodeList, timeScale, limit, "desc");
        setCategoryReturnIndex(timeScale, topCategories);

        topAndBottomCategoryChartResponse.addDates(topCategories);
        topAndBottomCategoryChartResponse.addCategoryLines(index, topCategories);

        LogUtil.debugBeanToJson(FundHoldingsDiversifiDaoImpl.class, "Result for categoryChartAddDates",
            topAndBottomCategoryChartResponse);
        return topAndBottomCategoryChartResponse;
    }

    @SuppressWarnings("unchecked")
    public List<UtProdCat> getPerformanceCategories(final List<String> productTypes, final List<String> productSubTypes,
        final List<String> countries, final List<String> categoryCodeList, final TimeScale timeScale, final Integer limit,
        final String order) throws Exception {
        // generate hql
        StringBuilder hql = new StringBuilder();
        hql.append(
            "select distinct c.utProdCatId.hhhhCategoryCode, c.productNlsName1, c.productNlsName2, c.productNlsName3, p.trailingTotalReturn from UtProdCat c, UtProdCatPerfmRtrn p"
                + " where c.utProdCatId.hhhhCategoryCode = p.utProdCatPerfmRtrnId.hhhhCategoryCode"
                + " and p.utProdCatPerfmRtrnId.rtrnAmtTtpeCde = :timeScale and p.trailingTotalReturn is not null");

        if (ListUtil.isValid(productTypes)) {
            hql.append(" and c.productType in (:productType)");
        }
        if (ListUtil.isValid(productSubTypes)) {
            hql.append(" and c.prodSubtypeCode in (:prodSubtypeCode)");
        }
        if (ListUtil.isValid(countries)) {
            hql.append(" and c.country in (:countries)");
        }
        if (ListUtil.isValid(categoryCodeList) && !categoryCodeList.contains(Constants.ALL)) {
            hql.append(" and c.utProdCatId.hhhhCategoryCode in (:categories)");
        }
        hql.append(" order by p.trailingTotalReturn " + order + "");

        LogUtil.debug(TopAndBottmCatChartServiceDaoImpl.class, "Hql query for getPerformanceCategories:" + hql.toString());

        Query query = createQueryForHql(hql.toString());

        if (ListUtil.isValid(productTypes)) {
            query.setParameter("productType", productTypes);
        }
        if (ListUtil.isValid(productSubTypes)) {
            query.setParameter("prodSubtypeCode", productSubTypes);
        }
        if (ListUtil.isValid(countries)) {
            query.setParameter("countries", countries);
        }
        if (ListUtil.isValid(categoryCodeList) && !categoryCodeList.contains(Constants.ALL)) {
            query.setParameter("categories", categoryCodeList);
        }
        if (StringUtil.isValid(timeScale.getTimeScale()) && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            String returnFieldName = getReturnFieldName(timeScale);
            query.setParameter("timeScale", returnFieldName);
        }
        // query.setMaxResults(limit);

        try {
            List<Object[]> resultList = query.getResultList();
            List<UtProdCat> utProdCatList = new ArrayList<UtProdCat>();
            if (ListUtil.isValid(resultList)) {
                for (Object[] resultRow : resultList) {
                    if (null != resultRow && resultRow.length > 0) {
                        String hhhhCategoryCode = resultRow[0] == null ? null : resultRow[0].toString();
                        String productNlsName1 = resultRow[1] == null ? null : resultRow[1].toString();
                        String productNlsName2 = resultRow[2] == null ? null : resultRow[2].toString();
                        String productNlsName3 = resultRow[3] == null ? null : resultRow[3].toString();

                        UtProdCatId utProdCatId = new UtProdCatId();
                        UtProdCat utProdCat = new UtProdCat();
                        utProdCatId.sethhhhCategoryCode(hhhhCategoryCode);
                        utProdCat.setProductNlsName1(productNlsName1);
                        utProdCat.setProductNlsName2(productNlsName2);
                        utProdCat.setProductNlsName3(productNlsName3);
                        utProdCat.setUtProdCatId(utProdCatId);
                        utProdCatList.add(utProdCat);
                    }
                }
            }
            LogUtil.debugBeanToJson(FundHoldingsDiversifiDaoImpl.class, "Result for getPerformanceCategories from DB",
                utProdCatList);
            return utProdCatList;
        } catch (Exception e) {
            LogUtil.error(TopAndBottmCatChartServiceDaoImpl.class, "getPerformanceCategories from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    private void setCategoryReturnIndex(final TimeScale timeScale, final List<UtProdCat> categories) throws Exception {
        for (UtProdCat utProdCat : categories) {
            List<UtProdCatTtlRtrnIndex> returnIndice =
                getCategoryReturnIndex(utProdCat.getUtProdCatId().gethhhhCategoryCode(), timeScale);
            utProdCat.setCategoryTotalReturnIndex(returnIndice);
        }
        LogUtil.debugBeanToJson(FundHoldingsDiversifiDaoImpl.class, "Result for setCategoryReturnIndex from DB", categories);
    }

    @SuppressWarnings("unchecked")
    public List<UtProdCatTtlRtrnIndex> getCategoryReturnIndex(final String categoryCode, final TimeScale timeScale)
        throws Exception {
        StringBuilder hql = new StringBuilder("from ").append("UtProdCatTtlRtrnIndex").append(
            " where utProdCatTtlRtrnIndexId.frequency=:frequency and utProdCatTtlRtrnIndexId.hhhhCategoryCode=:categoryCode order by utProdCatTtlRtrnIndexId.endDate asc");
        LogUtil.debug(TopAndBottmCatChartServiceDaoImpl.class, "Hql query for getCategoryReturnIndex:" + hql.toString());
        try {
            Query query = createQueryForHql(hql.toString());
            query.setParameter("categoryCode", categoryCode);
            query.setParameter("frequency", timeScale.getTimeScale());
            List<UtProdCatTtlRtrnIndex> utProdCatTtlRtrnIndexList = query.getResultList();

            LogUtil.debugBeanToJson(FundHoldingsDiversifiDaoImpl.class, "Result for getCategoryReturnIndex from DB",
                utProdCatTtlRtrnIndexList);
            return utProdCatTtlRtrnIndexList;
        } catch (Exception e) {
            LogUtil.error(TopAndBottmCatChartServiceDaoImpl.class, "getCategoryReturnIndex from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

}
