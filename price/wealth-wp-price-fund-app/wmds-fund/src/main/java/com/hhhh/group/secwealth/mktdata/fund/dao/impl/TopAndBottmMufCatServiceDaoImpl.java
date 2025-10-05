
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCat;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatId;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatPerfmRtrn;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.TopAndBottomCategoryResponse;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.dao.TopAndBottmMufCatServiceDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;


@Repository("topAndBottmMufCatServiceDao")
public class TopAndBottmMufCatServiceDaoImpl extends FundCommonDao implements TopAndBottmMufCatServiceDao {

    @Override
    public TopAndBottomCategoryResponse searchTopAndBottomList(final int index, final List<String> productTypes,
        final List<String> productSubTypes, final List<String> countryCriterias, final String assetClassCode,
        final TimeScale timeScale, final Integer limit, final String channelRestrictCode, final String restrOnlScribInd)
        throws Exception {

        TopAndBottomCategoryResponse topAndBottomCategoryResponse = new TopAndBottomCategoryResponse();
        List<UtProdCat> topItemLists = searchTopPerformanceCategoriesByAssetClassCode(productTypes, productSubTypes,
            countryCriterias, assetClassCode, timeScale, limit, channelRestrictCode, restrOnlScribInd);
        setRiskMeasure(topItemLists, timeScale);
        topAndBottomCategoryResponse.addTopCategories(index, topItemLists, timeScale);
        List<UtProdCat> bottomItemLists = searchBottomPerformanceCategoriesByAssetClassCode(productTypes, productSubTypes,
            countryCriterias, assetClassCode, timeScale, limit, channelRestrictCode, restrOnlScribInd);
        setRiskMeasure(bottomItemLists, timeScale);
        topAndBottomCategoryResponse.addBottomCategories(index, bottomItemLists, timeScale);
        return topAndBottomCategoryResponse;
    }

    public List<UtProdCat> searchTopPerformanceCategoriesByAssetClassCode(final List<String> productTypes,
        final List<String> productSubTypes, final List<String> countryCriteria, final String assetClassCode,
        final TimeScale timeScale, final Integer limit, final String channelRestrictCode, final String restrOnlScribInd)
        throws Exception {

        return this.searchPerformanceCategories(productTypes, productSubTypes, countryCriteria, assetClassCode, timeScale, limit,
            "desc", channelRestrictCode, restrOnlScribInd);
    }

    public List<UtProdCat> searchBottomPerformanceCategoriesByAssetClassCode(final List<String> productTypes,
        final List<String> productSubTypes, final List<String> countryCriteria, final String assetClassCode,
        final TimeScale timeScale, final Integer limit, final String channelRestrictCode, final String restrOnlScribInd)
        throws Exception {

        return this.searchPerformanceCategories(productTypes, productSubTypes, countryCriteria, assetClassCode, timeScale, limit,
            "asc", channelRestrictCode, restrOnlScribInd);
    }

    @SuppressWarnings("unchecked")
    private List<UtProdCat> searchPerformanceCategories(final List<String> productTypes, final List<String> productSubTypes,
        final List<String> countries, final String assetClassCode, final TimeScale timeScale, final Integer limit,
        final String order, final String channelRestrictCode, final String restrOnlScribInd) throws Exception {
        StringBuilder hql = new StringBuilder(
            "select distinct c.utProdCatId.hhhhCategoryCode, c.productNlsName1, c.productNlsName2, c.productNlsName3, c.stddev, p.trailingTotalReturn ");
        hql.append(
            " from UtProdCat c, UtProdCatPerfmRtrn p where c.utProdCatId.hhhhCategoryCode = p.utProdCatPerfmRtrnId.hhhhCategoryCode "
                + " and p.utProdCatPerfmRtrnId.rtrnAmtTtpeCde = :timeScale and p.trailingTotalReturn is not null ");
        StringBuilder hql1 = new StringBuilder(
            "select distinct c.utProdCatId.hhhhCategoryCode, count(c.utProdCatId.hhhhCategoryCode) as numberOfFunds from UtProdCat c, UtProdInstm u "
                + "where c.utProdCatId.productId = u.utProdInstmId.productId ");
        if (ListUtil.isValid(productTypes)) {
            hql.append(" and c.productType in (:productType)");
            hql1.append(" and c.productType in (:productType)");
        }
        if (ListUtil.isValid(productSubTypes)) {
            hql.append(" and c.prodSubtypeCode in (:prodSubtypeCode)");
            hql1.append(" and c.prodSubtypeCode in (:prodSubtypeCode)");
        }
        if (ListUtil.isValid(countries)) {
            hql.append(" and c.country in (:countries)");
            hql1.append(" and c.country in (:countries)");
        }
        if (StringUtil.isValid(assetClassCode) && !assetClassCode.equalsIgnoreCase(Constants.ALL)) {
            hql.append(" and c.assetClassCode in (:assetClassCode)");
            hql1.append(" and c.assetClassCode in (:assetClassCode)");
        }

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = c.utProdCatId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            if (null != restrictGroupHql) {
                hql.append(" and not exists (").append(restrictGroupHql).append(") ");
                hql1.append(" and not exists (").append(restrictGroupHql).append(") ");
            }
        }

        List<Integer> restrOnlScribIds = null;
        if (StringUtil.isValid(restrOnlScribInd)) {
            restrOnlScribIds = getRestrOnlScribIds(restrOnlScribInd);
            if (ListUtil.isValid(restrOnlScribIds)) {
                hql.append(" and c.utProdCatId.productId not in (:restrOnlScribIds) ");
                hql1.append(" and c.utProdCatId.productId not in (:restrOnlScribIds) ");
            }
        }

        hql.append(" order by p.trailingTotalReturn " + order + "");
        hql1.append(" group by c.utProdCatId.hhhhCategoryCode");

        LogUtil.debug(TopAndBottmCatChartServiceDaoImpl.class, "Hql query for searchPerformanceCategories:" + hql.toString());
        LogUtil.debug(TopAndBottmCatChartServiceDaoImpl.class, "Hql query for numberOfFunds:" + hql1.toString());

        Query query = createQueryForHql(hql.toString());
        Query query1 = createQueryForHql(hql1.toString());
        if (ListUtil.isValid(productTypes)) {
            query.setParameter("productType", productTypes);
            query1.setParameter("productType", productTypes);
        }
        if (ListUtil.isValid(productSubTypes)) {
            query.setParameter("prodSubtypeCode", productSubTypes);
            query1.setParameter("prodSubtypeCode", productSubTypes);
        }
        if (ListUtil.isValid(countries)) {
            query.setParameter("countries", countries);
            query1.setParameter("countries", countries);
        }
        if (StringUtil.isValid(timeScale.getTimeScale()) && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            String returnFieldName = getReturnFieldName(timeScale);
            query.setParameter("timeScale", returnFieldName);
        }
        if (StringUtil.isValid(assetClassCode) && !assetClassCode.equalsIgnoreCase(Constants.ALL)) {
            query.setParameter("assetClassCode", assetClassCode);
            query1.setParameter("assetClassCode", assetClassCode);
        }
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", channelRestrictCode);
            query1.setParameter("channelComnCde", channelRestrictCode);
        }
        if (ListUtil.isValid(restrOnlScribIds)) {
            query.setParameter("restrOnlScribIds", restrOnlScribIds);
            query1.setParameter("restrOnlScribIds", restrOnlScribIds);
        }
        query.setMaxResults(limit);

        try {
            List<Object[]> resultList = query.getResultList();
            List<Object[]> results = query1.getResultList();
            List<UtProdCat> utProdCatList = new ArrayList<UtProdCat>();
            if (ListUtil.isValid(resultList)) {
                for (Object[] resultRow : resultList) {
                    if (null != resultRow && resultRow.length > 0) {
                        String hhhhCategoryCode = MiscUtil.safeString(resultRow[0]);
                        String productNlsName1 = MiscUtil.safeString(resultRow[1]);
                        String productNlsName2 = MiscUtil.safeString(resultRow[2]);
                        String productNlsName3 = MiscUtil.safeString(resultRow[3]);
                        BigDecimal stddev = MiscUtil.safeBigDecimalValue(resultRow[4]);

                        UtProdCat utProdCat = new UtProdCat();
                        UtProdCatId utProdCatId = new UtProdCatId();
                        utProdCatId.sethhhhCategoryCode(hhhhCategoryCode);
                        utProdCat.setProductNlsName1(productNlsName1);
                        utProdCat.setProductNlsName2(productNlsName2);
                        utProdCat.setProductNlsName3(productNlsName3);
                        utProdCat.setStddev(stddev);
                        utProdCat.setUtProdCatId(utProdCatId);

                        for (Object[] result : results) {
                            String categoryCode = MiscUtil.safeString(result[0]);
                            if (categoryCode != null && categoryCode.equals(hhhhCategoryCode)) {
                                Integer numberOfProducts = MiscUtil.safeIntValue(result[1]);
                                utProdCat.setNumberOfProducts(numberOfProducts);
                                break;
                            }
                        }
                        utProdCatList.add(utProdCat);
                    }
                }
            }
            LogUtil.debugBeanToJson(TopAndBottmMufCatServiceDaoImpl.class, "Result for searchPerformanceCategories from DB",
                utProdCatList);
            return utProdCatList;
        } catch (Exception e) {
            LogUtil.error(TopAndBottmMufCatServiceDaoImpl.class, "searchPerformanceCategories from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public Date searchPerformanceTableLastUpdateDate(final String channelRestrictCode, final String restrOnlScribInd)
        throws Exception {
        StringBuilder hql = new StringBuilder(
            "select max(p.endDate) from UtProdCat c, UtProdCatPerfmRtrn p where c.utProdCatId.hhhhCategoryCode = p.utProdCatPerfmRtrnId.hhhhCategoryCode ");

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = c.utProdCatId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            if (null != restrictGroupHql) {
                hql.append(" and not exists (").append(restrictGroupHql).append(") ");
            }
        }

        List<Integer> restrOnlScribIds = null;
        if (StringUtil.isValid(restrOnlScribInd)) {
            restrOnlScribIds = getRestrOnlScribIds(restrOnlScribInd);
            if (ListUtil.isValid(restrOnlScribIds)) {
                hql.append(" and c.utProdCatId.productId not in (:restrOnlScribIds) ");
            }
        }

        Query query = createQueryForHql(hql.toString());
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", channelRestrictCode);
        }
        if (ListUtil.isValid(restrOnlScribIds)) {
            query.setParameter("restrOnlScribIds", restrOnlScribIds);
        }

        Object date = query.getSingleResult();
        return date == null ? null : (Date) date;
    }

    private void setRiskMeasure(final List<UtProdCat> categories, final TimeScale timeScale) throws Exception {
        for (UtProdCat utProdCat : categories) {
            List<UtProdCatPerfmRtrn> returnPerfmRtrn =
                getCategoryPerfmRtrn(utProdCat.getUtProdCatId().gethhhhCategoryCode(), timeScale);
            utProdCat.setUtProdCatPerfmRtrn(returnPerfmRtrn);
        }
    }

    @SuppressWarnings("unchecked")
    public List<UtProdCatPerfmRtrn> getCategoryPerfmRtrn(final String categoryCode, final TimeScale timeScale) throws Exception {
        StringBuilder hql = new StringBuilder("from UtProdCatPerfmRtrn r").append(
            " where r.utProdCatPerfmRtrnId.rtrnAmtTtpeCde=:rtrnAmtTtpeCde and  r.utProdCatPerfmRtrnId.hhhhCategoryCode=:categoryCode order by endDate asc");
        LogUtil.debug(TopAndBottmCatChartServiceDaoImpl.class, "create query for getCategoryPerfmRtrn : " + hql.toString());
        Query query = createQueryForHql(hql.toString());
        String rtrnAmtTtpeCde = getReturnFieldName(timeScale);
        query.setParameter("categoryCode", categoryCode);
        query.setParameter("rtrnAmtTtpeCde", rtrnAmtTtpeCde);

        try {
            List<UtProdCatPerfmRtrn> utProdCatPerfmRtrnList = query.getResultList();
            LogUtil.debugBeanToJson(TopAndBottmMufCatServiceDaoImpl.class, "Result for getCategoryPerfmRtrn from DB",
                utProdCatPerfmRtrnList);
            return utProdCatPerfmRtrnList;
        } catch (Exception e) {
            LogUtil.error(TopAndBottmMufCatServiceDaoImpl.class, "getCategoryPerfmRtrn from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

}
