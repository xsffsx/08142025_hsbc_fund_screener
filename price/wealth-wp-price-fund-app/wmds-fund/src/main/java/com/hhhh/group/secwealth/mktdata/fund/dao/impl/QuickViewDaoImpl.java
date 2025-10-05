
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuickViewRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.criteria.SqlConstants;
import com.hhhh.group.secwealth.mktdata.fund.dao.QuickViewDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Repository("quickViewDao")
@SuppressWarnings("java:S3776, java:S3749") //ignore these warnings
public class QuickViewDaoImpl extends FundCommonDao implements QuickViewDao {

    private static final String CHANNEL_COMN_CDE = "channelcomncde";
    private static final String NVL_RESTR_ONLN_SCRIB_IND_EXP = " and nvl(restr_onln_scrib_ind,' ') != :";
    private static final String RESTR_ONL_SCRIB_IND = "restrOnlScribInd";
    private final String hhhh_BEST_SELLER = "quickview.bestsellers";
    private final String hhhh_RETIREMENT_FUND = "quickview.retirementfunds";
    private final String hhhh_NEW_FUND = "quickview.newfunds";

    @Value("#{systemConfig['newfunds.return.time']}")
    private String period;

    @Override
    public List<Object[]> getTopPerformanceFunds(final String productType, final TimeScale timeScale, final String category,
                                                 final String productSubType, final Integer quickviewResultLimit,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception {
        return this.getPerformanceFunds(productType, timeScale, category, productSubType, "desc", quickviewResultLimit,restrOnlScribInd,currency,prodStatCde, headers);
    }

    @Override
    public List<Object[]> getBottomPerformanceFunds(final String productType, final TimeScale timeScale, final String category,
                                                    final String productSubType, final Integer quickviewResultLimit,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception {

        return this.getPerformanceFunds(productType, timeScale, category, productSubType, "asc", quickviewResultLimit,restrOnlScribInd,currency,prodStatCde, headers);
    }

    @Override
    public List<Object[]> getFundOfQuarter(final String productType, final TimeScale timeScale, final String category,
                                           final String productSubType, final Integer quickviewResultLimit, final Map<String, String> headers) throws Exception {
        return this.getFundsOfQuarter(productType, timeScale, category, productSubType, "asc", quickviewResultLimit, headers);
    }

    @Override
    public Integer getTopPerformanceFundsTotalCount(final String productType, final TimeScale timeScale, final String category,
                                                    final String productSubType,final String restrOnlScribInd, final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception {
        return this.getPerformanceFundsTotalCount(productType, timeScale, category, productSubType, "desc", restrOnlScribInd,currency , prodStatCde, headers);
    }

    @Override
    public Integer getBottomPerformanceTotalCount(final String productType, final TimeScale timeScale, final String category,
                                                  final String productSubType,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception {
        return this.getPerformanceFundsTotalCount(productType, timeScale, category, productSubType, "asc", restrOnlScribInd,currency , prodStatCde, headers);
    }

    @Override
    public Integer getFundOfQuarterTotalCount(final String productType, final TimeScale timeScale, final String category,
                                              final String productSubType, final Map<String, String> headers) throws Exception {
        return this.getFundsOfQuarterTotalCount(productType, timeScale, category, productSubType, "desc", headers);
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> getPerformanceFunds(final String productType, final TimeScale timeScale, final String category,
                                               final String productSubType, final String order, final Integer quickviewResultLimit,final String restrOnlScribInd,final String currency,final String prodStatCde, final Map<String, String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class,
                "getPerformanceFunds, productType: {}, timeScale: {}, order: {}, quickviewResultLimit {}", productType, timeScale,
                order, quickviewResultLimit);
        StringBuilder hql = new StringBuilder(
                "select distinct symbol, prod_type_cde, p.rec_updt_dt_tm, prod_name,prod_pll_name, ctry_prod_trade_cde, fund_cat_cde, rtrn_1yr_dpn, risk_lvl_cde,prod_sll_name,ccy_prod_cde,esg_ind,gba_acct_trdb,yield_1yr_pct,prod_stat_cde");
        hql.append(" from  v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id ");
        hql.append(SqlConstants.FUND_CAT_LVL);

        if (StringUtil.isValid(restrOnlScribInd)) {
            hql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(productType)) {
            hql.append(" and p.prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            hql.append(" and p.fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            hql.append(" and p.prod_subtp_cde = :productSubType ");
        }

        if (StringUtil.isValid(currency)) {
            hql.append(" and p.ccy_prod_cde = :currency ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            hql.append(" and p.prod_stat_cde != :prodStatCde ");
        }

        QueryUtil.buildCmbSearchSql(hql, headers, "p");

        hql.append(" order by p.rtrn_1yr_dpn ").append(order);
        Query query = createQueryForNativeSql(hql.toString());

        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        if (quickviewResultLimit != null) {
            query.setMaxResults(quickviewResultLimit);
        }

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getPerformanceFunds: {}", hql);
        LogUtil.debug(QuickViewDaoImpl.class,
                "Created Query for getPerformanceFunds Parameter: timeScale: {}, productType: {}, category: {}, productSubType: {}",
                timeScale, productType, category, productSubType);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getPerformanceFunds in QuickViewDaoImpl");

        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getPerformanceFunds from DB:", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getPerformanceFunds from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    private Integer getPerformanceFundsTotalCount(final String productType, final TimeScale timeScale, final String category,
                                                  final String productSubType, final String order, final String restrOnlScribInd,final String currency , final String prodStatCde, final Map<String, String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class, "getPerformanceFundsTotalCount, productType: {}, timeScale: {}, order: {}",
                productType, timeScale, order);
        StringBuilder sql = new StringBuilder("select count(1) from (");
        sql.append("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, r.rtrn_amt, p.risk_lvl_cde, p.prod_sll_name");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");
        sql.append(SqlConstants.FUND_CAT_LVL);
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            sql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }

        if (StringUtil.isValid(productType)) {
            sql.append(" and p.prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            sql.append(" and p.fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            sql.append(" and p.prod_subtp_cde = :productSubType ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            sql.append(" and p.prod_stat_cde != :prodStatCde ");
        }
        if (StringUtil.isValid(currency)) {
            sql.append(" and p.ccy_prod_cde = :currency ");
        }

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        sql.append(" order by r.rtrn_amt ").append(order);
        sql.append(")");

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getPerformanceFundsTotalCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        LogUtil
                .debug(
                        QuickViewDaoImpl.class,
                        "Created Query for getPerformanceFundsTotalCount Parameter: timeScale: {}, productType: {}, category: {}, productSubType: {}",
                        timeScale, productType, category, productSubType);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getPerformanceFundsTotalCount in QuickViewDaoImpl");

        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getPerformanceFundsTotalCount from DB:", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getPerformanceFundsTotalCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> getFundsOfQuarter(final String productType, final TimeScale timeScale, final String category,
                                             final String productSubType, final String order, final Integer quickviewResultLimit, final Map<String,String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class,
                "getPerformanceFunds, productType: {}, timeScale: {}, order: {}, quickviewResultLimit {}", productType, timeScale,
                order, quickviewResultLimit);
        StringBuilder hql = new StringBuilder(
                "select distinct p.symbol, p.productType, p.updatedOn, p.prodName, p.prodPllName, p.market, p.categoryCode, r.rtrnAmt, p.riskLvlCde, p.prodSllName,p.currency , p.esgInd,p.gbaAcctTrdb,p.yield1Yr,p.prodStatCde");
        hql.append(" from UtProdInstm p, UtReturns r where p.performanceId = r.utReturnsId.performanceId");
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append(" and r.utReturnsId.fundReturnTypeCode = :timeScale ");
        }
        if (StringUtil.isValid(productType)) {
            hql.append(" and p.productType = :productType ");
        }
        if (StringUtil.isValid(category)) {
            hql.append(" and p.categoryCode = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            hql.append(" and p.productSubTypeCode = :productSubType ");
        }

        QueryUtil.buildCmbSearchHql(hql, headers, "p");

        hql.append(" and p.listProdCode = 'SLT_1' and p.listProdType = 'SLT'");
        hql.append(" order by r.rtrnAmt ").append(order);
        Query query = createQueryForHql(hql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getPerformanceFunds: {}", hql);
        LogUtil.debug(QuickViewDaoImpl.class,
                "Created Query for getPerformanceFunds Parameter: timeScale: {}, productType: {}, category: {}, productSubType: {}",
                timeScale, productType, category, productSubType);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getPerformanceFunds in QuickViewDaoImpl");

        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getPerformanceFunds from DB:", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getPerformanceFunds from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    private Integer getFundsOfQuarterTotalCount(final String productType, final TimeScale timeScale, final String category,
                                                final String productSubType, final String order, final Map<String, String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class, "getPerformanceFundsTotalCount, productType: {}, timeScale: {}, order: {}",
                productType, timeScale, order);
        StringBuilder sql = new StringBuilder("select count(1) from (");
        sql.append("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, r.rtrn_amt, p.risk_lvl_cde, p.prod_sll_name");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(productType)) {
            sql.append(" and p.prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            sql.append(" and p.fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            sql.append(" and p.prod_subtp_cde = :productSubType ");
        }

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        sql.append("and p.list_prod_cde = 'SLT_1' and list_prod_type = 'SLT'");
        sql.append(" order by r.rtrn_amt ").append(order);
        sql.append(")");

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getPerformanceFundsTotalCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }

        LogUtil
                .debug(
                        QuickViewDaoImpl.class,
                        "Created Query for getPerformanceFundsTotalCount Parameter: timeScale: {}, productType: {}, category: {}, productSubType: {}",
                        timeScale, productType, category, productSubType);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getPerformanceFundsTotalCount in QuickViewDaoImpl");

        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getPerformanceFundsTotalCount from DB:", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getPerformanceFundsTotalCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getFundsByFundHouse(final String category, final String productSubType, final TimeScale timeScale,
                                              final String wpcCriteriaValue, final Map<String, String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class, "control enter into the getFundsByFundHouse category is: {}, wpcCriteriaValue is:{}",
                category, wpcCriteriaValue);
        StringBuilder hql = new StringBuilder(
                "select distinct p.symbol, p.productType, p.updatedOn, p.prodName, p.prodPllName, p.market, p.categoryCode, r.rtrnAmt, p.riskLvlCde, p.prodSllName,p.currency , p.esgInd,p.gbaAcctTrdb,p.yield1Yr,p.prodStatCde");
        hql.append(" from UtProdInstm p, UtReturns r where p.performanceId = r.utReturnsId.performanceId");
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append(" and r.utReturnsId.fundReturnTypeCode = :timeScale ");
        }
        if (StringUtil.isValid(category)) {
            hql.append(" and p.categoryCode = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            hql.append(" and p.productSubTypeCode = :productSubType ");
        }
        if (StringUtil.isValid(wpcCriteriaValue)) {
            hql.append(" and p.familyCode = :wpcCriteriaValue ");
        }

        QueryUtil.buildCmbSearchHql(hql, headers, "p");

        Query query = createQueryForHql(hql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        query.setParameter("wpcCriteriaValue", wpcCriteriaValue);
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(wpcCriteriaValue)) {
            query.setParameter("wpcCriteriaValue", wpcCriteriaValue);
        }
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getFundsByFundHouse: {}", hql);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getAllhhhhFunds in QuickViewDaoImpl");
        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "getFundsByFundHouse from DB: {}", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getFundsByFundHouse from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public Integer getFundsByFundHouseCount(final String category, final String productSubType, final TimeScale timeScale,
                                            final String wpcCriteriaValue, final Map<String, String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class,
                "control enter into the getFundsByFundHouseCount category is: {}, wpcCriteriaValue is:{}", category, wpcCriteriaValue);

        StringBuilder sql = new StringBuilder("select count(1) from (");
        sql.append("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, r.rtrn_amt, p.risk_lvl_cde, p.prod_sll_name");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(category)) {
            sql.append(" and p.fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            sql.append(" and p.prod_subtp_cde = :productSubType ");
        }
        if (StringUtil.isValid(wpcCriteriaValue)) {
            sql.append(" and p.fund_fm_cde = :wpcCriteriaValue ");
        }

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        sql.append(")");
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getFundsByFundHouseCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        query.setParameter("wpcCriteriaValue", wpcCriteriaValue);
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(wpcCriteriaValue)) {
            query.setParameter("wpcCriteriaValue", wpcCriteriaValue);
        }

        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getAllhhhhFunds in QuickViewDaoImpl");
        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "getFundsByFundHouseCount from DB: {}", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getFundsByFundHouseCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> searchFundByProductKeys(final List<ProductKey> productKeyList, final TimeScale timeScale, final Map<String,String> headers)
            throws Exception {
        LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "control enter into the getProductByProductInfo productInfo is: {}",
                productKeyList);
        if (ListUtil.isInvalid(productKeyList)) {
            return ListUtils.EMPTY_LIST;
        }

        StringBuilder hql = new StringBuilder(
                "select distinct p.symbol, p.productType, p.updatedOn, p.prodName, p.prodPllName, p.market, p.categoryCode, r.rtrnAmt, p.riskLvlCde, p.prodSllName ,p.currency , p.esgInd,p.gbaAcctTrdb,p.yield1Yr,p.prodStatCde");
        hql.append(" from UtProdInstm p, UtReturns r where p.performanceId = r.utReturnsId.performanceId");

        QueryUtil.buildCmbSearchHql(hql, headers, "p");

        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append(" and r.utReturnsId.fundReturnTypeCode = :timeScale and (");
        }

        for (int i = 0; i < productKeyList.size(); i++) {
            if (i == 0) {
                hql.append(" ( ");
            } else {
                hql.append(" or ( ");
            }
            hql.append(" p.market = '" + productKeyList.get(i).getMarket() + "'");
            hql.append(" and p.symbol = '" + productKeyList.get(i).getProdAltNum() + "'");
            hql.append(" and p.productType = '" + productKeyList.get(i).getProductType() + "'");
            hql.append(" ) ");
        }
        hql.append(" )");
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getProductByProductInfo: {}", hql);

        Query query = createQueryForHql(hql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }

        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getProductByProductInfo in QuickViewDaoImpl");
        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "getProductByProductInfo from DB: {}", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getProductByProductInfo from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public Integer searchFundByProductKeysCount(final List<ProductKey> productKeyList, final TimeScale timeScale, final Map<String,String> headers) throws Exception {
        LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "control enter into the searchFundByProductKeysCount productInfo is: {}",
                productKeyList);
        if (ListUtil.isInvalid(productKeyList)) {
            return 0;
        }

        StringBuilder sql = new StringBuilder("select count(1) from (");
        sql.append("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, r.rtrn_amt, p.risk_lvl_cde, p.prod_sll_name");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale and (");
        }

        for (int i = 0; i < productKeyList.size(); i++) {
            if (i == 0) {
                sql.append(" ( ");
            } else {
                sql.append(" or ( ");
            }
            sql.append(" p.ctry_prod_trade_cde = '" + productKeyList.get(i).getMarket() + "'");
            sql.append(" and p.symbol = '" + productKeyList.get(i).getProdAltNum() + "'");
            sql.append(" and p.prod_type_cde = '" + productKeyList.get(i).getProductType() + "'");
            sql.append(" ) ");
        }
        sql.append(" ))");
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for searchFundByProductKeysCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from searchFundByProductKeysCount in QuickViewDaoImpl");
        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "searchFundByProductKeysCount from DB:", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "searchFundByProductKeysCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> searchFundsRemoveChanlCde(final List<ProductKey> productKeyList, final TimeScale timeScale,
                                                    final QuickViewRequest request) throws Exception {
        LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "control enter into the getProductByProductInfo productInfo is: {}",
                productKeyList);
        if (ListUtil.isInvalid(productKeyList)) {
            return ListUtils.EMPTY_LIST;
        }

        StringBuilder hql = new StringBuilder(
                "select distinct p.symbol, p.productType, p.updatedOn, p.prodName, p.prodPllName, p.market, p.categoryCode, r.rtrnAmt, p.riskLvlCde, p.prodSllName");
        hql.append(" from UtProdInstm p, UtReturns r where p.performanceId = r.utReturnsId.performanceId");

        QueryUtil.buildCmbSearchHql(hql, request.getHeaders(), "p");

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(request.getChannelRestrictCode())) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = p.utProdInstmId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            hql.append(" and not exists (").append(restrictGroupHql).append(") ");
        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            hql.append(" and nvl(p." + RESTR_ONL_SCRIB_IND + ",' ') != :restrOnlScribInd ");
        }

        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append(" and r.utReturnsId.fundReturnTypeCode = :timeScale and (");
        }

        for (int i = 0; i < productKeyList.size(); i++) {
            if (i == 0) {
                hql.append(" ( ");
            } else {
                hql.append(" or ( ");
            }
            hql.append(" p.market = '" + productKeyList.get(i).getMarket() + "'");
            hql.append(" and p.symbol = '" + productKeyList.get(i).getProdAltNum() + "'");
            hql.append(" and p.productType = '" + productKeyList.get(i).getProductType() + "'");
            hql.append(" ) ");
        }
        hql.append(" )");
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getProductByProductInfo: {}", hql);

        Query query = createQueryForHql(hql.toString());
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", request.getChannelRestrictCode());
        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            query.setParameter(RESTR_ONL_SCRIB_IND, request.getRestrOnlScribInd());
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }

        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getProductByProductInfo in QuickViewDaoImpl");
        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "getProductByProductInfo from DB: {}", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getProductByProductInfo from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public List<Object[]> searchFundFromdb(final TimeScale timeScale, final String tableNameValue,
                                           final QuickViewRequest request, final String currency, final String prodStatCde) throws Exception {

        StringBuilder hql = new StringBuilder(
                "select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, p.rtrn_1yr_dpn, p.risk_lvl_cde, p.prod_sll_name,p.ccy_prod_cde, p.esg_ind, p.gba_acct_trdb,p.yield_1yr_pct,p.prod_stat_cde,p.prod_top_sell_rank_num ");
        hql.append("from v_ut_prod_instm p  ");

        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append("left join v_ut_returns r on p.performance_id = r.performance_id and r.fund_return_type_code = :timeScale ");
        }

        if (tableNameValue.equals(this.hhhh_BEST_SELLER)) {
            hql.append("where p.top_sell_prod_ind = 'Y' ");
        }
        if (tableNameValue.equals(this.hhhh_RETIREMENT_FUND)) {
            hql.append("where p.retire_invst_ind = 'Y' ");
        }       
        if (tableNameValue.equals(this.hhhh_NEW_FUND)) {
            String date = getNewfundsRreturnTime();
            hql.append("where p.prod_lnch_dt >= " + "to_date('" + date + "', 'yyyy-MM-dd') ");
        }

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(request.getChannelRestrictCode())) {
            restrictGroupHql = new StringBuilder("select distinct prod_id from v_ut_prod_chanl ");
            restrictGroupHql.append("where v_ut_prod_chanl.prod_id = p.prod_id and chanl_comn_cde = :channelcomncde ");
            hql.append(" and not exists (").append(restrictGroupHql).append(") ");

        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            hql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }
        if (StringUtil.isValid(currency)) {
            hql.append(" and CCY_PROD_CDE = :currency ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            hql.append("and prod_stat_cde != :prodStatCde ");
        }

        QueryUtil.buildCmbSearchSql(hql, request.getHeaders(), "p");

        if (tableNameValue.equals(this.hhhh_BEST_SELLER)) {
            hql.append("order by p.rtrn_1yr_dpn desc ");
        }

        Query query = createQueryForNativeSql(hql.toString());
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (null != restrictGroupHql) {
            query.setParameter(CHANNEL_COMN_CDE, request.getChannelRestrictCode());
        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            query.setParameter(RESTR_ONL_SCRIB_IND, request.getRestrOnlScribInd());
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }

        try {
            List<Object[]> resultList = query.getResultList();
            LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "getProductByProductInfo from DB: {}", resultList);
            return resultList;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getProductByProductInfo from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }

    }




    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getTop5PerformersFunds(final String productType, final TimeScale timeScale, final String category,
                                                 final String productSubType, final String channelRestrictCode, final String restrOnlScribInd, final String prodStatCde,
                                                 final String topPerfmList,final String currency,final Integer quickviewResultLimit, final Map<String,String> header) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class,
                "getTop5PerformersFunds, productType: {}, timeScale: {}, category: {}, productSubType: {}", productType, timeScale,
                category, productSubType);

        StringBuilder hql = new StringBuilder("select * from ( ");
        hql.append("select distinct symbol, prod_type_cde, p.rec_updt_dt_tm, prod_name, prod_pll_name, ctry_prod_trade_cde, fund_cat_cde, rtrn_amt, risk_lvl_cde, prod_sll_name,ccy_prod_cde,esg_ind,gba_acct_trdb,yield_1yr_pct,fund_cat_lvl1_cde,rtrn_1yr_amt, prod_stat_cde, ");
        hql.append("rank() over(partition by fund_cat_lvl1_cde order by rtrn_1yr_dpn desc nulls last) num ");
        hql.append("from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id and pri_share_class_ind = :priShareClassInd and fund_cat_lvl1_cde is not null ");

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct prod_id from v_ut_prod_chanl ");
            restrictGroupHql.append("where v_ut_prod_chanl.prod_id = p.prod_id and chanl_comn_cde = :channelcomncde ");
            if (null != restrictGroupHql) {
                hql.append(" and not exists (").append(restrictGroupHql).append(") ");
            }
        }

        if (StringUtil.isValid(restrOnlScribInd)) {
            hql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }

        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append("and fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(productType)) {
            hql.append("and prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            hql.append("and fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            hql.append("and prod_subtp_cde = :productSubType ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            hql.append("and prod_stat_cde != :prodStatCde ");
        }
        if (StringUtil.isValid(currency)) {
            hql.append("and ccy_prod_cde = :currency ");
        }

        QueryUtil.buildCmbSearchSql(hql, header, "p");

        hql.append(") t where t.num <= " + topPerfmList);
        hql.append(" order by fund_cat_lvl1_cde asc, rtrn_1yr_amt desc ");
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getTop5PerformersFunds: {}", hql);

        Query query = createQueryForNativeSql(hql.toString());

        query.setParameter("priShareClassInd", CommonConstants.YES);
        if (null != restrictGroupHql) {
            query.setParameter(CHANNEL_COMN_CDE, channelRestrictCode);
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }

        if (quickviewResultLimit != null && quickviewResultLimit != 0) {
            query.setMaxResults(quickviewResultLimit);
        }
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getTop5PerformersFunds in QuickViewDaoImpl");

        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getTop5PerformersFunds from DB:", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getTop5PerformersFunds from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public Integer getTop5PerformersFundsTotalCount(final String productType, final String category, final String productSubType,
                                                    final String channelRestrictCode, final String restrOnlScribInd, final String prodStatCde, final String topPerfmList,final  String currency, final Map<String,String> header)
            throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class,
                "getTop5PerformersFundsTotalCount, productType: {}, category: {}, productSubType: {}", productType, category,
                productSubType);

        StringBuilder hql = new StringBuilder("select count(1) from ( ");
        hql.append("select rank() over(partition BY fund_cat_lvl1_cde order by rtrn_1yr_dpn DESC nulls last) num from v_ut_prod_instm ");
        hql.append("where pri_share_class_ind = :priShareClassInd and fund_cat_lvl1_cde is not null ");

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct prod_id from v_ut_prod_chanl ");
            restrictGroupHql
                    .append("where v_ut_prod_chanl.prod_id = v_ut_prod_instm.prod_id and chanl_comn_cde = :channelcomncde ");
            hql.append(" and not exists (").append(restrictGroupHql).append(") ");

        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            hql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }

        if (StringUtil.isValid(productType)) {
            hql.append("and prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            hql.append("and fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            hql.append("and prod_subtp_cde = :productSubType ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            hql.append("and prod_stat_cde != :prodStatCde ");
        }
        if (StringUtil.isValid(currency)) {
            hql.append("and ccy_prod_cde = :currency ");
        }

        QueryUtil.buildCmbSearchSql2(hql, header);

        hql.append(") t where t.num <= " + topPerfmList);
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getTop5PerformersFundsTotalCount: {}", hql);

        Query query = createQueryForNativeSql(hql.toString());

        query.setParameter("priShareClassInd", CommonConstants.YES);
        if (null != restrictGroupHql) {
            query.setParameter(CHANNEL_COMN_CDE, channelRestrictCode);
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }

        LogUtil.debug(QuickViewDaoImpl.class,
                "Created Query for getTop5PerformersFundsTotalCount Parameter: productType: {}, category: {}, productSubType: {}",
                productType, category, productSubType);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getTop5PerformersFundsTotalCount in QuickViewDaoImpl");

        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getTop5PerformersFundsTotalCount from DB:", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getTop5PerformersFundsTotalCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public Integer getTopDividendCount(final String productType,final TimeScale timeScale, final String category, final String productSubType,
                                       final String channelRestrictCode, final String restrOnlScribInd, final String prodStatCde, final String topPerfmList,final  String currency, final Map<String,String> headers)
            throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class,
                "getTopDividendCount, productType: {}, category: {}, productSubType: {}", productType, category,
                productSubType);

        StringBuilder hql = new StringBuilder("select count(1) from ( ");
        hql.append("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, r.rtrn_amt, p.risk_lvl_cde, p.prod_sll_name , p.esg_ind ");
        hql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");

        hql.append(SqlConstants.FUND_CAT_LVL);
        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct prod_id from v_ut_prod_chanl ");
            restrictGroupHql
                    .append("where v_ut_prod_chanl.prod_id = v_ut_prod_instm.prod_id and chanl_comn_cde = :channelcomncde ");
            if (null != restrictGroupHql) {
                hql.append(" and not exists (").append(restrictGroupHql).append(") ");
            }
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            hql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(productType)) {
            hql.append("and prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            hql.append("and fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            hql.append("and prod_subtp_cde = :productSubType ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            hql.append("and prod_stat_cde  != :prodStatCde ");
        }
        if (StringUtil.isValid(currency)) {
            hql.append("and ccy_prod_cde  = :currency ");
        }

        QueryUtil.buildCmbSearchSql(hql, headers, "p");

        hql.append(")");
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getTopDividendCount: {}", hql);

        Query query = createQueryForNativeSql(hql.toString());
        if (null != restrictGroupHql) {
            query.setParameter(CHANNEL_COMN_CDE, channelRestrictCode);
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }

        LogUtil.debug(QuickViewDaoImpl.class,
                "Created Query for getTopDividendCount Parameter: productType: {}, category: {}, productSubType: {}",
                productType, category, productSubType);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getTopDividendCount in QuickViewDaoImpl");

        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getTopDividendCount from DB:", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getTopDividendCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @Override
    public List<Object[]> getTopDividend(final String productType, final TimeScale timeScale, final String category,
                                         final String productSubType, final String channelRestrictCode, final String restrOnlScribInd, final String prodStatCde,
                                         final String topDivdList,final String currency,final Integer numberOfRecords, final Map<String,String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class,
                "getTopDividend, productType: {}, timeScale: {}, order: {}, quickviewResultLimit {}", productType, timeScale);
        StringBuilder hql = new StringBuilder(
                "select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, r.rtrn_amt, p.risk_lvl_cde, p.prod_sll_name ,p.ccy_prod_cde , p.esg_ind,p.gba_acct_trdb,p.yield_1yr_pct,p.prod_stat_cde  ");
        hql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");

        hql.append(SqlConstants.FUND_CAT_LVL);
        if (StringUtil.isValid(restrOnlScribInd)) {
            hql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            hql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(productType)) {
            hql.append("and prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            hql.append("and fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            hql.append("and prod_subtp_cde = :productSubType ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            hql.append("and prod_stat_cde  != :prodStatCde ");
        }
        if (StringUtil.isValid(currency)) {
            hql.append("and ccy_prod_cde  = :currency ");
        }

        QueryUtil.buildCmbSearchSql(hql, headers, "p");

        hql.append(" order by p.YIELD_1YR_PCT desc ");
        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getTopDividend: {}", hql);

        Query query = createQueryForNativeSql(hql.toString());
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(productType)) {
            query.setParameter("productType", productType);
        }
        if (StringUtil.isValid(category)) {
            query.setParameter("category", category);
        }
        if (StringUtil.isValid(productSubType)) {
            query.setParameter("productSubType", productSubType);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }

        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getTopDividend in QuickViewDaoImpl");

        if (numberOfRecords != null && numberOfRecords != 0) {
            query.setMaxResults(numberOfRecords);
        }
        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getTopDividend from DB:", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getTopDividend from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }
    private String getNewfundsRreturnTime() {
        String dateType = this.period.substring(this.period.length() - 1).toUpperCase();
        int number = Integer.parseInt(this.period.substring(0, this.period.length() - 1));
        Calendar cal = Calendar.getInstance();

        if (CommonConstants.YEAR_PERIOD.equals(dateType)) {
            cal.add(Calendar.YEAR, -number);
            cal.add(Calendar.MONTH, -1);
        } else if (CommonConstants.MONTH_PERIOD.equals(dateType)) {
            cal.add(Calendar.MONTH, -number - 1);
        } else if (CommonConstants.DAY_PERIOD.equals(dateType)) {
            cal.add(Calendar.DAY_OF_YEAR, -number);
        } else {
            LogUtil.error(FundSearchResultDaoImpl.class, "period is invalid, period: " + this.period);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }

        String date = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd_withHyphen).format(cal.getTime());
        return date;
    }

    @Override
    public Integer getEsgFundCount(final TimeScale timeScale,String restrOnlScribInd,String currency , String prodStatCde, Map<String,String> headers) throws Exception {
        return this.getEsgFundCount("Y", timeScale,restrOnlScribInd,currency,prodStatCde, headers);
    }

    @Override
    public Integer getGBAFundCount(final TimeScale timeScale,String restrOnlScribInd,String currency , String prodStatCde, Map<String,String> headers) throws Exception {
        return this.getGBAFundCount("Y", timeScale,restrOnlScribInd,currency,prodStatCde,headers);
    }
    @Override
    public List<Object[]> getEsgFund(final TimeScale timeScale ,String restrOnlScribInd,String currency, String prodStatCde , Map<String,String> headers, Integer... quickviewResultLimit) throws Exception {
        return this.getEsgFund("Y", timeScale, "desc" ,restrOnlScribInd,currency, prodStatCde, headers, quickviewResultLimit);
    }
    @Override
    public List<Object[]> getGBAFund(final TimeScale timeScale ,String restrOnlScribInd,String currency, String prodStatCde , Map<String,String> headers, Integer... quickviewResultLimit) throws Exception {
        return this.getGBAFund("Y", timeScale, "desc" ,restrOnlScribInd,currency, prodStatCde, headers, quickviewResultLimit);
    }
    public Integer getEsgFundCount(final String esgFlag, final TimeScale timeScale ,String restrOnlScribInd,String currency , String prodStatCde, Map<String,String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class, "getEsgFundCount, esgFlag: {}, timeScale: {} , currency: {} , prodStatCde:{} ",
                esgFlag, timeScale , currency,prodStatCde);
        StringBuilder sql = new StringBuilder("select count(1) from (");
        sql.append("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, p.rtrn_1yr_dpn, p.risk_lvl_cde, p.prod_sll_name , p.esg_ind ");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");

        if (StringUtil.isValid(restrOnlScribInd)) {
            sql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(esgFlag)) {
            sql.append(" and p.esg_ind = :esgFlag ");
        }
        if (StringUtil.isValid(currency)) {
            sql.append(" and p.CCY_PROD_CDE = :currency ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            sql.append(" and p.PROD_STAT_CDE != :prodStatCde ");
        }

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        sql.append(")");

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getEsgFundCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(esgFlag)) {
            query.setParameter("esgFlag", esgFlag);
        }

        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        LogUtil.debug(
                QuickViewDaoImpl.class,
                "Created Query for getEsgFundCount Parameter: esgFlag: {}, timeScale: {} , prodStatCde:{}",
                esgFlag, timeScale,prodStatCde);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getEsgFundCount in QuickViewDaoImpl");

        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getEsgFundCount from DB:", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getEsgFundCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }


    private List<Object[]> getEsgFund(String esgFlag, TimeScale timeScale, String order ,String restrOnlScribInd,String currency,String prodStatCde, Map<String,String> headers, Integer... quickviewResultLimit) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class, "control enter into the getEsgFund esgFlag is: {}, timeScale is:{} , order is:{} , currency is {}: ",
                esgFlag, timeScale,order,currency);
        StringBuilder sql = new StringBuilder("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, p.rtrn_1yr_dpn, p.risk_lvl_cde, p.prod_sll_name ,p.ccy_prod_cde , p.esg_ind,p.gba_acct_trdb,p.yield_1yr_pct, p.prod_stat_cde ");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");

        if (StringUtil.isValid(restrOnlScribInd)) {
            sql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(esgFlag)) {
            sql.append(" and p.esg_ind = :esgFlag ");
        }

        if (StringUtil.isValid(currency)) {
            sql.append(" and p.CCY_PROD_CDE = :currency ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            sql.append(" and p.PROD_STAT_CDE != :prodStatCde ");
        }

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        if (StringUtil.isValid(order)) {
            sql.append(" order by p.rtrn_1yr_dpn ").append(order);
        }

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getEsgFundCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(esgFlag)) {
            query.setParameter("esgFlag", esgFlag);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        if(quickviewResultLimit !=null && quickviewResultLimit[0] > 0){
            query.setMaxResults(quickviewResultLimit[0]);

        }
        LogUtil.debug(
                QuickViewDaoImpl.class,
                "Created Query for getEsgFundCount Parameter: esgFlag: {}, timeScale: {} , order: {} ",
                esgFlag, timeScale,order);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getEsgFundCount in QuickViewDaoImpl");

        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getEsgFund from DB:", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getEsgFund from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }


    private List<Object[]> getGBAFund(String gbaFlag, TimeScale timeScale, String order ,String restrOnlScribInd,String currency,String prodStatCde, Map<String,String> headers, Integer... quickviewResultLimit) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class, "control enter into the getgbaFund gbaFlag is: {}, timeScale is:{} , order is:{} , currency is {}: ",
                gbaFlag, timeScale,order,currency);
        StringBuilder sql = new StringBuilder("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, p.rtrn_1yr_dpn, p.risk_lvl_cde, p.prod_sll_name ,p.ccy_prod_cde , p.esg_ind,p.gba_acct_trdb,p.yield_1yr_pct ,p.prod_stat_cde ");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");

        if (StringUtil.isValid(restrOnlScribInd)) {
            sql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(gbaFlag)) {
            sql.append(" and p.gba_acct_trdb = :gbaFlag ");
        }

        if (StringUtil.isValid(currency)) {
            sql.append(" and p.CCY_PROD_CDE = :currency ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            sql.append(" and p.PROD_STAT_CDE != :prodStatCde ");
        }
        if (StringUtil.isValid(order)) {
            sql.append(" order by p.rtrn_1yr_dpn ").append(order);
        }

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getGbaFundCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(gbaFlag)) {
            query.setParameter("gbaFlag", gbaFlag);
        }
        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        if(quickviewResultLimit !=null && quickviewResultLimit[0] > 0){
            query.setMaxResults(quickviewResultLimit[0]);

        }
        LogUtil.debug(
                QuickViewDaoImpl.class,
                "Created Query for getGbaFundCount Parameter: gbaFlag: {}, timeScale: {} , order: {} ",
                gbaFlag, timeScale,order);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getGbaFundCount in QuickViewDaoImpl");

        try {
            List<Object[]> list = query.getResultList();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getGbaFund from DB:", list);
            return list;
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getGbaFund from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    public Integer getGBAFundCount(final String gbaFlag, final TimeScale timeScale ,String restrOnlScribInd,String currency , String prodStatCde, Map<String,String> headers) throws Exception {
        LogUtil.debug(QuickViewDaoImpl.class, "getGBAFundCount, gbaFlag: {}, timeScale: {} , currency: {} , prodStatCde:{} ",
                gbaFlag, timeScale , currency,prodStatCde);
        StringBuilder sql = new StringBuilder("select count(1) from (");
        sql.append("select distinct p.symbol, p.prod_type_cde, p.rec_updt_dt_tm, p.prod_name, p.prod_pll_name, p.ctry_prod_trade_cde, p.fund_cat_cde, r.rtrn_amt, p.risk_lvl_cde, p.prod_sll_name , p.esg_ind ");
        sql.append(" from v_ut_prod_instm p, v_ut_returns r where p.performance_id = r.performance_id");

        if (StringUtil.isValid(restrOnlScribInd)) {
            sql.append(NVL_RESTR_ONLN_SCRIB_IND_EXP + RESTR_ONL_SCRIB_IND + " ");
        }

        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            sql.append(" and r.fund_return_type_code = :timeScale ");
        }
        if (StringUtil.isValid(gbaFlag)) {
            sql.append(" and p.gba_acct_trdb = :gbaFlag ");
        }
        if (StringUtil.isValid(currency)) {
            sql.append(" and p.CCY_PROD_CDE = :currency ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            sql.append(" and p.PROD_STAT_CDE != :prodStatCde ");
        }

        QueryUtil.buildCmbSearchSql(sql, headers, "p");

        sql.append(")");

        LogUtil.debug(QuickViewDaoImpl.class, "Created Query for getGbaFundCount: {}", sql);

        Query query = createQueryForNativeSql(sql.toString());
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter(RESTR_ONL_SCRIB_IND, restrOnlScribInd);
        }
        if (timeScale != null && !CommonConstants.ALL.equals(timeScale.getTimeScale())) {
            query.setParameter("timeScale", timeScale.getTimeScale());
        }
        if (StringUtil.isValid(gbaFlag)) {
            query.setParameter("gbaFlag", gbaFlag);
        }

        if (StringUtil.isValid(currency)) {
            query.setParameter("currency", currency);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
        }
        LogUtil.debug(
                QuickViewDaoImpl.class,
                "Created Query for getgbaFundCount Parameter: gbaFlag: {}, timeScale: {} , prodStatCde:{}",
                gbaFlag, timeScale,prodStatCde);
        LogUtil.debug(QuickViewDaoImpl.class, "Exit from getGBAFundCount in QuickViewDaoImpl");

        try {
            BigDecimal count = (BigDecimal) query.getSingleResult();
            LogUtil.debugBeanToJson(QuickViewDaoImpl.class, "getGbaFundCount from DB:", count);
            return count == null ? 0 : count.intValue();
        } catch (Exception e) {
            LogUtil.error(QuickViewDaoImpl.class, "getGbaFundCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

}
