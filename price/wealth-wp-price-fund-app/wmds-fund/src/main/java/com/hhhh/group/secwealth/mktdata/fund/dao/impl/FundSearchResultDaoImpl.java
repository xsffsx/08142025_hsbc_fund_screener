
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchRangeCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.SortCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UTHoldingAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtCatAsetAlloc;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtHoldings;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdInstm;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.*;
import com.hhhh.group.secwealth.mktdata.fund.beans.helper.UtSvceHelper;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchResultRequest;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.DetailedCriteriaUtil;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.RangeCriteriaUtil;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.SortOrder;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundSearchResultDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import com.hhhh.group.secwealth.mktdata.fund.service.FundSearchResultServiceImpl;
import com.hhhh.group.secwealth.mktdata.fund.util.MiscUtil;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@Repository("fundSearchResultDao")
@SuppressWarnings("java:S1192, java:S3749") //ignore these warnings
public class FundSearchResultDaoImpl extends FundCommonDao implements FundSearchResultDao {

    private final String DEFAULT_ORDERBY = "DEFAULT";
    private final String ORDER_BY_CAT = "categoryName";
    private final String ORDER_BY_FAM = "familyName";
    private final String ORDER_BY_CATLV1 = "categoryLevel1Name";
    private final String ORDER_BY_CATLV0 = "categoryLevel0Name";
    private final String ORDER_BY_INVSTRG = "investmentRegionName";
    private final String IS_SORT_BY_LOCALE = "TRUE";

    private final String NULLS_LAST = "NULLS LAST";
    // oracle sql feature
    private final String ORDERBY_SCHINESE_PINYIN = "'NLS_SORT=SCHINESE_PINYIN_M'";
    // oracle sql feature
    private final String ORDERBY_TCHINESE_STROKE = "'NLS_SORT=TCHINESE_STROKE_M'";
    private final String NLSSORT = "nlssort";
    private final String hhhh_BEST_SELLER = "quickview.bestsellers";
    private final String hhhh_NEW_FUND = "quickview.newfunds";
    private final String hhhh_RETIREMENT_FUND = "quickview.retirementfunds";
    private final String hhhh_TOP5_PERFORMERS = "quickview.top5performance";
    private final String hhhh_FUND_OF_QUARTER = "quickview.fundofquarter";
    private final String SITE_FEATURE_TOPPERFMLIST = ".topPerfmList";


    @Autowired
    @Qualifier("detailedCriteriaUtil")
    private DetailedCriteriaUtil detailedCriteriaUtil;

    @Autowired
    @Qualifier("rangeCriteriaUtil")
    private RangeCriteriaUtil rangeCriteriaUtil;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

    @Resource(name = "orderByFieldsMap")
    private Map<String, Map<String, String>> orderByFieldsMap;

    @Resource(name = "holdingAllocationMap")
    private Map<String, String> holdingAllocationMap;

    @Value("#{systemConfig['assetAlloc.values']}")
    private String assetAlloc;

    @Value("#{systemConfig['newfunds.return.time']}")
    private String period;

//    @Value("#{systemConfig['fundSearch.limit.record']}")
//    private String limitRecord;

    private Query generateQueryForFundSearch(final StringBuilder hql, final FundSearchResultRequest request,
        final List<Integer> prodIds_switchOutFund, final List<String> hhhhRiskLevlList, final String countryCode,
        final String groupMember, final boolean flag, final int catLevel) throws Exception {

        List<SearchCriteria> detailedCriterias = request.getDetailedCriterias();
        List<Integer> prodIds_allowSellMipProdInd = new ArrayList<Integer>();
        if (ListUtil.isValid(detailedCriterias)) {
            int index =
                this.localeMappingUtil.getNameByIndex(request.getCountryCode() + CommonConstants.SYMBOL_DOT + request.getLocale());
            prodIds_allowSellMipProdInd = appendDetailedCriteria(hql, detailedCriterias, index, countryCode, groupMember, request.getHeaders());
        }

        List<SearchRangeCriteriaValue> rangeCriterias = request.getRangeCriterias();
        if (ListUtil.isValid(rangeCriterias)) {
            appendRangeCriteria(hql, rangeCriterias, countryCode, groupMember);
        }

        List<Integer> prodIds = getProdIdByProductKeys(request.getProductKeys(), request.getHeaders());
        Map<String, Object> orMap_prodId = new HashMap<String, Object>();
        if (ListUtil.isValid(prodIds_switchOutFund) || ListUtil.isValid(prodIds_allowSellMipProdInd)
            || ListUtil.isValid(prodIds)) {
            orMap_prodId = appendCriteriaByProdIds(hql, prodIds_switchOutFund, prodIds_allowSellMipProdInd, prodIds);
        }

        // hhhh Risk Level
        Map<String, Object> orMap_riskCode = new HashMap<String, Object>();
        if (ListUtil.isValid(hhhhRiskLevlList)) {
            orMap_riskCode = appendCriteriaByRiskCode(hql, hhhhRiskLevlList);
        }

        List<String[]> sortList = this.getSortCriterias(request, countryCode, groupMember, request.getLocale());
        if (ListUtil.isValid(sortList)) {
            if (flag) {
                appendSortByForTPAndBestSeller(hql, catLevel, request.getLocale(), countryCode);
            }
            appendSortBy(hql, sortList, request.getLocale(), request.getCountryCode(), request.getGroupMember());
        }

        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for generateQueryForFundSearch: {}", hql.toString());

        Query query = createQueryForHql(hql.toString());
        if (ListUtil.isValid(detailedCriterias)) {
            for (SearchCriteria searchCriteria : detailedCriterias) {
                if (Constants.CRITERION_KEYWORD.equals(searchCriteria.getCriteriaKey())) {
                    String value = StringUtils.isNotBlank(searchCriteria.getCriteriaValue())
                        ? searchCriteria.getCriteriaValue().toLowerCase() : searchCriteria.getCriteriaValue();
                    query.setParameter(Constants.CRITERION_KEYWORD, "%" + value.replace("*", "%") + "%");
                    break;
                }
            }
        }
        query.setParameter("productType", request.getProductType());
        if (!orMap_prodId.isEmpty() || !orMap_riskCode.isEmpty()) {
            setQueryParamKey(query, orMap_prodId);
            setQueryParamKey(query, orMap_riskCode);
        } else if (ListUtil.isValid(request.getProductKeys())) {
            String msg = "No record found from DB|ProdAltNum=" + request.getProductKeys().get(0).getProdAltNum();
            LogUtil.error(FundSearchResultDaoImpl.class, msg);
            throw new CommonException(ErrTypeConstants.WARNMSG_NORECORDFOUND, msg);
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    private List<String[]> getSortCriterias(final FundSearchResultRequest request, final String countryCode,
        final String groupMember, final String locale) throws Exception {
        List<SortCriteriaValue> sortCriterias = request.getSortCriterias();
        String sortBy = request.getSortBy();
        String sortOrderStr = request.getSortOrder();

        if (ListUtil.isValid(sortCriterias)) {
            List<String[]> sortList = new LinkedList<String[]>();
            for (SortCriteriaValue sort : sortCriterias) {
                String sortKey = sort.getSortKey();
                String key = this.getSortDbFieldName(sortKey, countryCode, groupMember);
                if (StringUtil.isInvalid(key)) {
                    key = this.getDefaultSortDbFieldName(sortKey, countryCode, locale);
                }
                String order = this.validateSortOrder(sort.getSortOrder()).name();
                sortList.add(new String[] {key, order, this.isSortByLocale(sortKey)});
            }
            return sortList;

        } else if (StringUtil.isValid(sortBy) && StringUtil.isValid(sortOrderStr)) {
            String key = this.getSortDbFieldName(sortBy, countryCode, groupMember);
            if (StringUtil.isInvalid(key)) {
                key = this.getDefaultSortDbFieldName(sortBy, countryCode, locale);
            }
            String order = this.validateSortOrder(sortOrderStr).name();

            List<String[]> sortList = new LinkedList<String[]>();
            sortList.add(new String[] {key, order, this.isSortByLocale(sortBy)});
            return sortList;
        }
        return ListUtils.EMPTY_LIST;
    }

    private String isSortByLocale(final String sortbyStr) {
        String result = null;
        if (this.ORDER_BY_CAT.equalsIgnoreCase(sortbyStr) || this.ORDER_BY_FAM.equalsIgnoreCase(sortbyStr)
            || this.ORDER_BY_CATLV1.equalsIgnoreCase(sortbyStr) || this.ORDER_BY_INVSTRG.equalsIgnoreCase(sortbyStr)
            || this.ORDER_BY_CATLV0.equalsIgnoreCase(sortbyStr)) {
            result = this.IS_SORT_BY_LOCALE;
        }
        return result;
    }

    @Override
    public Integer searchTotalCount(final FundSearchResultRequest request, final Map<String, List<Integer>> switchOutFundMap,
        final List<String> hhhhRiskLevlList, final List<ProductKey> prodIds_wpcWebService, final String countryCode,
        final String groupMember, final Map<String, Boolean> holdingsValueMap, final boolean flag, final int catLevel)
        throws Exception {
        if (prodIds_wpcWebService != null && prodIds_wpcWebService.size() == 0) {
            return 0;
        }
        List<Integer> prodIds_switchOutFund = null;
        if (!switchOutFundMap.isEmpty()) {
            if (ListUtil.isValid(switchOutFundMap.get("switchOutFund"))) {
                prodIds_switchOutFund = switchOutFundMap.get("switchOutFund");
            } else {
                return 0;
            }
        }

        List<ProductKey> productKeys = request.getProductKeys();
        if (ListUtil.isValid(prodIds_wpcWebService)) {
            if (ListUtil.isInvalid(productKeys)) {
                productKeys = new ArrayList<ProductKey>();
            }
            productKeys.addAll(prodIds_wpcWebService);
            request.setProductKeys(productKeys);
        }

        LogUtil.debug(FundSearchResultDaoImpl.class, "Enter into the searchTotalCount");
        StringBuilder hql = new StringBuilder();

        Map<String, Boolean> holdingsFalseMap = ifSearchDB(holdingsValueMap);
        String sortBy = sortAssetAlloc(request);
        if (holdingsFalseMap.size() != 5 && StringUtil.isValid(sortBy)) {
            hql.append(
                "select count(*) from UtProdInstm utProd, UTHoldingAlloc utAlloc where utProd.performanceId=utAlloc.utHoldingAllocId.performanceId ");
            hql.append(
                "and utProd.productType= :productType and utAlloc.utHoldingAllocId.holdingAllocClassName= :holdingAllocClassName ");
        } else {
            hql.append("select count(*) from UtProdInstm utProd where utProd.productType= :productType ");
        }

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(request.getChannelRestrictCode())) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = utProd.utProdInstmId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            if (null != restrictGroupHql) {
                hql.append("and not exists (").append(restrictGroupHql).append(") ");
            }
        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            hql.append("and nvl(utProd.restrOnlScribInd,' ') != :restrOnlScribInd ");
        }

        hql = QueryUtil.buildCmbSearchHql(hql, request.getHeaders(),"utProd");
        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for searchTotalCount: {}", hql.toString());

        Query query = generateQueryForFundSearch(hql, request, prodIds_switchOutFund, hhhhRiskLevlList, countryCode, groupMember,
            flag, catLevel);
        if (StringUtil.isValid(sortBy) && this.assetAlloc.contains(sortBy)) {
            query.setParameter("holdingAllocClassName", sortBy);
        }
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", request.getChannelRestrictCode());
        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            query.setParameter("restrOnlScribInd", request.getRestrOnlScribInd());
        }

        if (null == query) {
            return 0;
        }
        try {
            Long totalCount = (Long) query.getSingleResult();
            LogUtil.debug(FundSearchResultDaoImpl.class, "TotalCount for fundSearch from DB: {}", totalCount);
            return totalCount.intValue();
        } catch (Exception e) {
            LogUtil.error(FundSearchResultDaoImpl.class, "searchTotalCount from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UtProdInstm> searchFund(final FundSearchResultRequest request, final Map<String, List<Integer>> switchOutFundMap,
        final List<String> hhhhRiskLevlList, final List<ProductKey> prodIds_wpcWebService, final String countryCode,
        final String groupMember, final Map<String, Boolean> holdingsValueMap, final boolean flag, final int catLevel)
        throws Exception {
        LogUtil.debug(FundSearchResultDaoImpl.class, "Enter into the search utProdInstm");
        if (prodIds_wpcWebService != null && prodIds_wpcWebService.size() == 0) {
            return null;
        }
        List<Integer> prodIds_switchOutFund = null;
        if (!switchOutFundMap.isEmpty()) {
            if (ListUtil.isValid(switchOutFundMap.get("switchOutFund"))) {
                prodIds_switchOutFund = switchOutFundMap.get("switchOutFund");
            } else {
                return null;
            }
        }

        List<ProductKey> productKeys = request.getProductKeys();
        if (ListUtil.isValid(prodIds_wpcWebService)) {
            if (ListUtil.isInvalid(productKeys)) {
                productKeys = new ArrayList<ProductKey>();
            }
            productKeys.addAll(prodIds_wpcWebService);
            request.setProductKeys(productKeys);
        }

        Map<String, Boolean> holdingsFalseMap = ifSearchDB(holdingsValueMap);
        StringBuilder hql = new StringBuilder();

        String sortBy = sortAssetAlloc(request);
        if (holdingsFalseMap.size() != 5 && StringUtil.isValid(sortBy)) {
            hql.append(
                "select utProd from UtProdInstm utProd, UTHoldingAlloc utAlloc where utProd.performanceId=utAlloc.utHoldingAllocId.performanceId ");
            hql.append(
                "and utProd.productType= :productType and utAlloc.utHoldingAllocId.holdingAllocClassName= :holdingAllocClassName ");
        } else {
            hql.append("select utProd from UtProdInstm utProd where utProd.productType= :productType ");
        }

        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(request.getChannelRestrictCode())) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = utProd.utProdInstmId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            if (null != restrictGroupHql) {
                hql.append("and not exists (").append(restrictGroupHql).append(") ");
            }
        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            hql.append("and nvl(utProd.restrOnlScribInd,' ') != :restrOnlScribInd ");
        }

        hql = QueryUtil.buildCmbSearchHql(hql, request.getHeaders(),"utProd");

        LogUtil.debug(FundSearchResultDaoImpl.class, "create query for searchFund is: {}", hql.toString());

        Query query = generateQueryForFundSearch(hql, request, prodIds_switchOutFund, hhhhRiskLevlList, countryCode, groupMember,
            flag, catLevel);
        if (StringUtil.isValid(sortBy) && this.assetAlloc.contains(sortBy)) {
            query.setParameter("holdingAllocClassName", sortBy);
        }
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", request.getChannelRestrictCode());
        }
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            query.setParameter("restrOnlScribInd", request.getRestrOnlScribInd());
        }

        // set number of records
        Integer startDetail = request.getStartDetail();
        Integer endDetail = request.getEndDetail();
        Integer numberOfRecords = request.getNumberOfRecords();
//        Integer limitRecordCount =  Integer.valueOf(limitRecord);
//        if( (numberOfRecords != null && limitRecordCount != null && numberOfRecords > limitRecordCount)
//            || (null != request.getProductKeys() && request.getProductKeys().size() > limitRecordCount)
//        ) {
//            LogUtil.error(FundSearchResultDaoImpl.class,"numberOfRecords = " +numberOfRecords +" limitRecordCount = "+ limitRecordCount);
//            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
//        }

        if (startDetail != null && startDetail > 0) {
            if (numberOfRecords != null && numberOfRecords != 0) {
                query.setFirstResult(startDetail - 1);
                query.setMaxResults(numberOfRecords);
            } else if (endDetail != null && endDetail != 0 && numberOfRecords == null) {
                query.setFirstResult(startDetail - 1);
                query.setMaxResults(endDetail);
            }
        }
        try {
            List<UtProdInstm> resultList = query.getResultList();
           // LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "Result for fundSearch from DB", resultList);
            return resultList;
        } catch (Exception e) {
            LogUtil.error(FundSearchResultDaoImpl.class, "searchFund from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    private String sortAssetAlloc(final FundSearchResultRequest request) {
        String sortStr = null;
        List<SortCriteriaValue> sortCriterias = request.getSortCriterias();
        String sortBy = request.getSortBy();
        if (ListUtil.isValid(sortCriterias)) {
            for (SortCriteriaValue sort : sortCriterias) {
                String sortKey = sort.getSortKey();
                if (sortKey != null && this.assetAlloc.contains(sortKey)) {
                    sortStr = sortKey;
                    break;
                }
            }

        } else if (StringUtil.isValid(sortBy)) {
            if (sortBy != null && this.assetAlloc.contains(sortBy)) {
                sortStr = sortBy;
            }
        }
        return sortStr;
    }

    public List<Integer> appendDetailedCriteria(final StringBuilder hql, final List<SearchCriteria> detailedCriterias,
        final int index, final String countryCode, final String groupMember, final Map<String,String> headers) throws Exception {
        List<Integer> prodIds_allowSellMipProdInd = new ArrayList<Integer>();
        List<SearchCriteria> newDetailedCriterias = new ArrayList<SearchCriteria>();
        for (SearchCriteria searchCriteria : detailedCriterias) {
            if (Constants.CRITERION_REQUEST_MONTHLY_INVESTMENT_PLAN.equals(searchCriteria.getCriteriaKey())) {
                prodIds_allowSellMipProdInd = getProdIdByAllowSellMipProdInd(searchCriteria, headers);
            } else if (Constants.CRITERION_KEYWORD.equals(searchCriteria.getCriteriaKey())) {
                getKeywordSql(hql, index);
            } else {
                newDetailedCriterias.add(searchCriteria);
            }
        }
        String str = "";
        if (this.detailedCriteriaUtil.existsKey(newDetailedCriterias, DetailedCriteriaUtil.LOACTYPE_CRITERIA_MAPKEY)) {
            String ltSql = this.getLTSql(Constants.TABLE_ALIAS_UT_PROD);
            str = this.detailedCriteriaUtil.toString(newDetailedCriterias, Constants.TABLE_ALIAS_UT_PROD, ltSql, "fundSvcClzTypeCd",
                countryCode, groupMember);
        } else {
            str = this.detailedCriteriaUtil.toString(newDetailedCriterias, Constants.TABLE_ALIAS_UT_PROD, null, null, countryCode,
                groupMember);
        }
        if (StringUtil.isValid(str)) {
            hql.append(" and ").append(str);
        }
        return prodIds_allowSellMipProdInd;
    }

    private void getKeywordSql(final StringBuilder sqlBuilder, final int index) {
        sqlBuilder.append(" and (lower(utProd.symbol) like :");
        sqlBuilder.append(Constants.CRITERION_KEYWORD);
        if (index == 0) {
            sqlBuilder.append(" or lower(utProd.prodName) like :");
            sqlBuilder.append(Constants.CRITERION_KEYWORD);

            sqlBuilder.append(" or lower(utProd.productShortName) like :");
            sqlBuilder.append(Constants.CRITERION_KEYWORD);
        } else if (index == 1) {
            sqlBuilder.append(" or lower(utProd.prodPllName) like :");
            sqlBuilder.append(Constants.CRITERION_KEYWORD);

            sqlBuilder.append(" or lower(utProd.productShortPrimaryLanguageName) like :");
            sqlBuilder.append(Constants.CRITERION_KEYWORD);
        } else if (index == 2) {
            sqlBuilder.append(" or lower(utProd.prodSllName) like :");
            sqlBuilder.append(Constants.CRITERION_KEYWORD);

            sqlBuilder.append(" or lower(utProd.productShortSecondLanguageName) like :");
            sqlBuilder.append(Constants.CRITERION_KEYWORD);
        }
        sqlBuilder.append(" ) ");
    }

    private String getLTSql(final String fundAlias) {
        String sql = ("EXISTS (FROM UtSvce WHERE utSvceId.performanceId = " + fundAlias
            + ".performanceId AND utSvceId.fundSvcCde = " + fundAlias + ".symbol )");
        return sql;
    }

    // get prodcut id list with AllowSellMipProdInd
    @SuppressWarnings("unchecked")
    public List<Integer> getProdIdByAllowSellMipProdInd(final SearchCriteria searchCriteria, final Map<String,String> headers) throws Exception {
        StringBuilder hql = new StringBuilder();
        hql.append("select utProd.utProdInstmId.productId from UtProdInstm utProd ");
        hql.append(" where utProd.allowSellMipProdInd= :allowSellMipProdInd ");

        hql = QueryUtil.buildCmbSearchHql(hql, headers,"utProd");

        LogUtil.debug(FundSearchResultDaoImpl.class, "create query for getProdIdByAllowSellMipProdInd is: {}", hql.toString());
        List<Integer> idList = new ArrayList<Integer>();
        try {
            if (null != searchCriteria) {
                Query query = createQueryForHql(hql.toString());
                query.setParameter("allowSellMipProdInd", searchCriteria.getCriteriaValue());
                idList = query.getResultList();
            }
          //  LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "Result for getProdIdByAllowSellMipProdInd from DB", idList);
            return idList;
        } catch (Exception e) {
            LogUtil.error(FundSearchResultDaoImpl.class, "getProdIdByAllowSellMipProdInd from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    public void appendRangeCriteria(final StringBuilder hql, final List<SearchRangeCriteriaValue> rangeCriterias,
        final String countryCode, final String groupMember) throws Exception {
        String str = this.rangeCriteriaUtil.toString(rangeCriterias, Constants.TABLE_ALIAS_UT_PROD, countryCode, groupMember);
        if (StringUtil.isValid(str)) {
            hql.append(" and ").append(str);
        }
    }

    public Map<String, Object> appendCriteriaByProdIds(final StringBuilder hql, final List<Integer> prodIds_switchOutFund,
        final List<Integer> prodIds_allowSellMipProdInd, final List<Integer> prodIds_productKey) {
        Map<String, Object> orMap = new HashMap<String, Object>();
        List<Integer> allProdIds = new ArrayList<Integer>();
        if (ListUtil.isValid(prodIds_productKey)) {
            allProdIds.addAll(prodIds_productKey);
        }
        if (ListUtil.isValid(prodIds_switchOutFund)) {
            allProdIds.addAll(prodIds_switchOutFund);
        }
        if (ListUtil.isValid(prodIds_allowSellMipProdInd)) {
            allProdIds.addAll(prodIds_allowSellMipProdInd);
        }
        if (ListUtil.isValid(allProdIds)) {
            HashSet<Integer> set = new HashSet<Integer>(allProdIds);
            allProdIds.clear();
            allProdIds.addAll(set);
            orMap = generateHqlOR(allProdIds, "utProd.utProdInstmId.productId", "prodIds");
            appendSubHql(hql, "and", orMap);
        }
        return orMap;
    }

    public Map<String, Object> appendCriteriaByRiskCode(final StringBuilder hql, final List<String> hhhhRiskLevlList) {
        Map<String, Object> orMap = new HashMap<String, Object>();
        if (ListUtil.isValid(hhhhRiskLevlList)) {
            orMap = generateHqlOR(hhhhRiskLevlList, "utProd.riskLvlCde", "riskCodes");
            appendSubHql(hql, "and", orMap);
        }
        return orMap;
    }

    private void appendSortBy(final StringBuilder hql, final List<String[]> sortList, final String locale, final String countryCode,
        final String groupMember) throws Exception {
        if (hql.toString().indexOf("count(") == -1) {
            int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
            if (ListUtil.isValid(sortList)) {
                if (hql.toString().contains("order by")) {
                    hql.append(",");
                } else {
                    hql.append(" order by ");
                }
                for (String[] array : sortList) {
                    String sortStr = "";
                    if (array[0].indexOf(".") > -1) {
                        if (array[2] != null && this.IS_SORT_BY_LOCALE.equals(array[2])) {
                            if (CommonConstants.SIMPLIFIED_CHINESE == index) {
                                String[] sortMapping = array[0].split(CommonConstants.SYMBOL_COMMA);
                                sortStr = sortMapping[0] + CommonConstants.SYMBOL_COMMA + this.NLSSORT
                                    + CommonConstants.SYMBOL_LEFT_BRACKET + sortMapping[1] + (index + 1)
                                    + CommonConstants.SYMBOL_COMMA + this.ORDERBY_SCHINESE_PINYIN
                                    + CommonConstants.SYMBOL_RIGHT_BRACKET;
                            } else if (CommonConstants.TRADITIONAL_CHINESE == index) {
                                String[] sortMapping = array[0].split(CommonConstants.SYMBOL_COMMA);
                                sortStr = sortMapping[0] + CommonConstants.SYMBOL_COMMA + this.NLSSORT
                                    + CommonConstants.SYMBOL_LEFT_BRACKET + sortMapping[1] + (index + 1)
                                    + CommonConstants.SYMBOL_COMMA + this.ORDERBY_TCHINESE_STROKE
                                    + CommonConstants.SYMBOL_RIGHT_BRACKET;
                            } else {
                                sortStr = array[0] + (index + 1);
                            }
                        } else {
                            sortStr = array[0];
                        }
                    } else {
                        sortStr = Constants.TABLE_ALIAS_UT_PROD + CommonConstants.SYMBOL_DOT + array[0];
                    }

                    if (sortStr.indexOf(CommonConstants.SYMBOL_COMMA) == -1) {
                        sortStr = sortStr + CommonConstants.SPACE + array[1] + CommonConstants.SPACE + this.NULLS_LAST;
                    } else {
                        sortStr = sortStr.substring(0, sortStr.indexOf(CommonConstants.SYMBOL_COMMA)) + CommonConstants.SPACE
                            + array[1] + CommonConstants.SPACE + this.NULLS_LAST
                            + sortStr.substring(sortStr.indexOf(CommonConstants.SYMBOL_COMMA)) + CommonConstants.SPACE + array[1]
                            + CommonConstants.SPACE + this.NULLS_LAST;
                    }

                    hql.append(sortStr + CommonConstants.SPACE + CommonConstants.SYMBOL_COMMA);
                }
                // for Oracle ORDER BY and ROWNUM not correctly
                hql.append(" symbol");
            }
        }
        LogUtil.debug(FundSearchResultDaoImpl.class, "appendSortBy HQL: " + hql.toString());
    }

    private void appendSortByForTPAndBestSeller(final StringBuilder hql, final int catLevel, final String locale,
        final String countryCode) throws Exception {
        if (hql.toString().indexOf("count(") == -1) {
            int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
            hql.append(" order by utProd.categoryLevel" + catLevel + "SequencenNum asc ");
            if (CommonConstants.SIMPLIFIED_CHINESE == index) {
                // NLS_SORT=SCHINESE_PINYIN_M is oracle sql feature
                hql.append(",nlssort(utProd.categoryLevel" + catLevel + "Name" + (index + 1) + ",'NLS_SORT=SCHINESE_PINYIN_M')");
            } else if (CommonConstants.TRADITIONAL_CHINESE == index) {
                // NLS_SORT=TCHINESE_STROKE_M is oracle sql feature
                hql.append(",nlssort(utProd.categoryLevel" + catLevel + "Name" + (index + 1) + ",'NLS_SORT=TCHINESE_STROKE_M')");
            } else {
                hql.append(",utProd.categoryLevel" + catLevel + "Name" + (index + 1));
            }
        }
    }

    public SortOrder validateSortOrder(final String sortOrderStr) throws Exception {
        SortOrder sortOrder = SortOrder.fromString(sortOrderStr);
        if (null != sortOrder) {
            return sortOrder;
        } else {
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<Integer, List<UtCatAsetAlloc>> searchHldgsMap(final List<Integer> prodIds_DB, final String classTypeCode)
        throws Exception {
        if (ListUtil.isInvalid(prodIds_DB)) {
            return null;
        }
        LogUtil.debug(FundSearchResultDaoImpl.class, "Enter into the searchHldgs");
        Map<Integer, List<UtCatAsetAlloc>> map = new HashMap<Integer, List<UtCatAsetAlloc>>();
        StringBuilder hql = new StringBuilder();
        hql.append("from UtCatAsetAlloc ");
        hql.append("where utCatAsetAllocId.classTypeCode = '" + classTypeCode + "'");
        Map<String, Object> orMap = generateHqlOR(prodIds_DB, "utCatAsetAllocId.productId", "sicHldgProds");
        appendSubHql(hql, "and", orMap);
        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for searchSicHldg: {}", hql.toString());
        Query query = createQueryForHql(hql.toString());
        setQueryParamKey(query, orMap);
        try {
            LogUtil.debug(FundSearchResultDaoImpl.class, " query getResultList HldgsMap cost time.");
            List<UtCatAsetAlloc> resultList = query.getResultList();
            LogUtil.debug(FundSearchResultDaoImpl.class, "Result for start HldgsMap cost time.");
            for (UtCatAsetAlloc alloc : resultList) {
                Integer productId = alloc.getUtCatAsetAllocId().getProductId();
                List<UtCatAsetAlloc> allocList = map.get(productId);
                if (allocList == null) {
                    allocList = new ArrayList<UtCatAsetAlloc>();
                    map.put(productId, allocList);
                }
                allocList.add(alloc);
            }
            LogUtil.debug(FundSearchResultDaoImpl.class, "Result for end HldgsMap cost time.");
           // LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "Result for " + classTypeCode + " Hldgs from DB", resultList);
            return map;
        } catch (Exception e) {
            LogUtil.error(FundSearchResultDaoImpl.class, "searchHldgsMap from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<UtHoldings>> searchTop5HldgMap(final List<String> performanceIds_DB,
        final Map<String, Boolean> holdingsValueMap) throws Exception {
        if (ListUtil.isInvalid(performanceIds_DB)) {
            return null;
        }

        // validation if return "topHoldingsList"
        // default: true = return
        if (!holdingsValueMap.isEmpty()) {
            for (String key : holdingsValueMap.keySet()) {
                if (key.equalsIgnoreCase("topHoldingsList")) {
                    Boolean criteriaValue = holdingsValueMap.get(key);
                    if (null != criteriaValue && !criteriaValue) {
                        return null;
                    }
                }
            }
        }

        LogUtil.debug(FundSearchResultDaoImpl.class, "Enter into the searchTop5Hldg");
        Map<String, List<UtHoldings>> map = new HashMap<String, List<UtHoldings>>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from UtHoldings");
        Map<String, Object> orMap = generateHqlOR(performanceIds_DB, "utHoldingsId.performanceId", "top5HldgProds");
        appendSubHql(hql, "where", orMap);
        hql.append(" order by utHoldingsId.holdingId");
        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for searchTop5Hldg: {}", hql.toString());
        Query query = createQueryForHql(hql.toString());
        setQueryParamKey(query, orMap);
        try {
            LogUtil.debug(FundSearchResultDaoImpl.class, " query getResultList Top5Hldg cost time.");
            List<UtHoldings> resultList = query.getResultList();
            LogUtil.debug(FundSearchResultDaoImpl.class, "Result for start Top5Hldg: cost time.");
            for (UtHoldings fundHolding : resultList) {
                String performanceId = fundHolding.getUtHoldingsId().getPerformanceId();
                List<UtHoldings> holdingList = map.get(performanceId);
                if (holdingList == null) {
                    holdingList = new ArrayList<UtHoldings>();
                    map.put(performanceId, holdingList);
                }
                holdingList.add(fundHolding);
            }
            LogUtil.debug(FundSearchResultDaoImpl.class, "Result for end Top5Hldg cost time.");
           // LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "Result for searchTop5Hldg from DB", resultList);
            return map;
        } catch (Exception e) {
            LogUtil.error(FundSearchResultDaoImpl.class, "searchTop5HldgMap from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UtSvceHelper> getUtSvce(final List<String> performanceIds_DB) throws Exception {
        if (ListUtil.isInvalid(performanceIds_DB)) {
            return null;
        }
        LogUtil.debug(FundSearchResultDaoImpl.class, "Enter into the getUtSvce");
        StringBuilder hql = new StringBuilder();
        hql.append("select distinct s.utSvceId.performanceId, s.fundSvcClzTypeCd from UtSvce s ");
        Map<String, Object> orMap = generateHqlOR(performanceIds_DB, "s.utSvceId.performanceId", "utSvceCodes");
        appendSubHql(hql, "where", orMap);
        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for getUtSvce: {}", hql.toString());
        Query query = createQueryForHql(hql.toString());
        setQueryParamKey(query, orMap);
        try {
            List<Object[]> resultList = query.getResultList();
            List<UtSvceHelper> utSvceHelperList = new ArrayList<UtSvceHelper>();
            if (ListUtil.isValid(resultList)) {
                for (Object[] resultRow : resultList) {
                    if (null != resultRow && resultRow.length > 0) {
                        UtSvceHelper utSvceHelper = new UtSvceHelper();
                        utSvceHelper.setPerformanceId(MiscUtil.safeString(resultRow[0]));
                        utSvceHelper.setFundSvcClzTypeCd(MiscUtil.safeString(resultRow[1]));
                        utSvceHelperList.add(utSvceHelper);
                    }
                }
            }
           // LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "Result for getUtSvce from DB", utSvceHelperList);
            return utSvceHelperList;
        } catch (Exception e) {
            LogUtil.error(FundSearchResultDaoImpl.class, "getUtSvce from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, List<UTHoldingAlloc>> searchHoldingAllocation(final List<String> performanceIds_DB,
        final Map<String, Boolean> holdingsValueMap) throws Exception {
        if (ListUtil.isInvalid(performanceIds_DB)) {
            return null;
        }

        // validation if return
        // "assetAlloc,stockSectors,equityRegional,bondSectors,bondRegional",
        // default: true == return
        Map<String, Boolean> holdingsFalseMap = ifSearchDB(holdingsValueMap);
        if (holdingsFalseMap.size() == 5) {
            return null;
        }

        LogUtil.debug(FundSearchResultDaoImpl.class, "Enter into the searchHoldingAllocation");
        Map<String, List<UTHoldingAlloc>> map = new HashMap<String, List<UTHoldingAlloc>>();
        StringBuilder hql = new StringBuilder(" from UTHoldingAlloc utAlloc");
        Map<String, Object> orMap = generateHqlOR(performanceIds_DB, "utAlloc.utHoldingAllocId.performanceId", "holdingAllocProds");
        appendSubHql(hql, "where", orMap);
        List<String> holdingAllocClassType = new ArrayList<String>();
        if (!holdingsFalseMap.isEmpty() && holdingsFalseMap.size() > 0) {
            for (String key : holdingsFalseMap.keySet()) {
                holdingAllocClassType.add(key);
            }
            hql.append(" and utAlloc.utHoldingAllocId.holdingAllocClassType not in (:holdingAllocClassType) ");
        }
        Query query = createQueryForHql(hql.toString());
        setQueryParamKey(query, orMap);
        if (!holdingAllocClassType.isEmpty() && holdingAllocClassType.size() > 0) {
            query.setParameter("holdingAllocClassType", holdingAllocClassType);
        }

        try {
            LogUtil.debug(FundSearchResultDaoImpl.class, " query getResultList searchHoldingAllocation cost time.");
            List<UTHoldingAlloc> resultList = query.getResultList();
            LogUtil.debug(FundSearchResultDaoImpl.class, "Result for start searchHoldingAllocation: cost time.");
            for (UTHoldingAlloc assetAlloc : resultList) {
                String performanceId = assetAlloc.getUtHoldingAllocId().getPerformanceId();
                List<UTHoldingAlloc> assetAllocList = map.get(performanceId);
                if (assetAllocList == null) {
                    assetAllocList = new ArrayList<UTHoldingAlloc>();
                    map.put(performanceId, assetAllocList);
                }
                assetAllocList.add(assetAlloc);
            }
            LogUtil.debug(FundSearchResultDaoImpl.class, "Result for end searchHoldingAllocation cost time.");
           // LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "Result for searchHoldingAllocation from DB", resultList);
            return map;
        } catch (Exception e) {
            LogUtil.error(FundSearchResultDaoImpl.class, "searchHoldingAllocation from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Integer> appendSqlForUKSwitchOut(final String[] fields, final Map<String,String> headers) throws Exception {
        StringBuilder hql = new StringBuilder(
            " select utProd.utProdInstmId.productId, utProd.allowSwOutProdInd from UtProdInstm utProd where utProd.productType= :productType and utProd.symbol= :symbol and utProd.market= :market ");

        hql = QueryUtil.buildCmbSearchHql(hql, headers,"utProd");

        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for appendSqlForUKSwitchOut1: {}", hql.toString());

        Query query = createQueryForHql(hql.toString());
        if (StringUtil.isValid(fields[3])) {
            query.setParameter("productType", fields[3]);
        }
        if (StringUtil.isValid(fields[1])) {
            query.setParameter("symbol", fields[1]);
        }
        if (StringUtil.isValid(fields[2])) {
            query.setParameter("market", fields[2]);
        }
        List<Object[]> results = query.getResultList();
        List<Integer> prodId = new ArrayList<Integer>();
        for (Object[] result : results) {
            if (CommonConstants.YES.equalsIgnoreCase((String) result[1])) {
                prodId.add((Integer) result[0]);
            } else {
                LogUtil.debug(FundSearchResultServiceImpl.class, "The product: " + fields[1] + " can't to switch out.");
            }
        }
        if (ListUtil.isInvalid(prodId)) {
            return ListUtils.EMPTY_LIST;
        }
        StringBuilder prodIdHql = new StringBuilder("select utProd.utProdInstmId.productId from UtProdInstm utProd ");
        prodIdHql.append(" where utProd.allowSwInProdInd=:allowSwInProdInd and utProd.utProdInstmId.productId not in (:prodId) ");

        prodIdHql = QueryUtil.buildCmbSearchHql(prodIdHql, headers,"utProd");

        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for appendSqlForUKSwitchOut2: {}", prodIdHql.toString());

        Query prodIdQuery = createQueryForHql(prodIdHql.toString());
        prodIdQuery.setParameter("allowSwInProdInd", CommonConstants.YES);
        prodIdQuery.setParameter("prodId", prodId);
        List<Integer> prodId_switchOut = prodIdQuery.getResultList();
        return prodId_switchOut;
    }

    @SuppressWarnings("unchecked")
    public List<Integer> appendSqlForCoreSwitchOut(final String[] fields, final String channelRestrictCode,
        final String restrOnlScribInd, final Map<String,String> headers) throws Exception {
        // get switchOut product id and switchOut group
        StringBuilder hql = new StringBuilder(
            " select utProd.utProdInstmId.productId, swGroup.utProdSwitchoutGroupId.switchTableGroup from UtProdInstm utProd, UTProdSwitchoutGroup swGroup ");
        hql.append("where utProd.utProdInstmId.productId = swGroup.utProdSwitchoutGroupId.productId ");
        hql.append(" and utProd.productType= :productType and utProd.symbol= :symbol and utProd.market= :market ");

        hql = QueryUtil.buildCmbSearchHql(hql, headers, "utProd");

        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for appendSqlForCoreSwitchOut1: {}", hql.toString());

        Query query = createQueryForHql(hql.toString());
        if (StringUtil.isValid(fields[3])) {
            query.setParameter("productType", fields[3]);
        }
        if (StringUtil.isValid(fields[1])) {
            query.setParameter("symbol", fields[1]);
        }
        if (StringUtil.isValid(fields[2])) {
            query.setParameter("market", fields[2]);
        }
        List<Object[]> resultList = query.getResultList();
        Set<Integer> prodId = new HashSet<Integer>();
        Set<String> swithoutGroupList = new HashSet<String>();

        if (ListUtil.isValid(resultList)) {
            for (Object[] result : resultList) {
                prodId.add(Integer.parseInt(String.valueOf(result[0])));
                swithoutGroupList.add(String.valueOf(result[1]));
            }
            if (null == prodId || prodId.size() < 1 || null == swithoutGroupList || swithoutGroupList.size() < 1) {
                LogUtil.error(FundSearchResultServiceImpl.class, "The product: " + fields[1] + " can't to switch out.");
                return ListUtils.EMPTY_LIST;
            }
        } else {
            return ListUtils.EMPTY_LIST;
        }

        // get blackList for switchOut product
        StringBuilder blackListHql = new StringBuilder(
            "select distinct(swBlack.utProdSwitchoutId.fundUnSwitchoutCode) from UTProdSwitchout swBlack where swBlack.utProdSwitchoutId.productId in (:prodId) ");
        Query blackListQuery = createQueryForHql(blackListHql.toString());
        blackListQuery.setParameter("prodId", prodId);
        List<String> swithoutBlackList = blackListQuery.getResultList();

        // get the product_id of other products that this product "fields[1]"
        // can switchOut
        // filter conditions: remove the product itself and move the product
        // "blackList" product
        StringBuilder prodIdHql = new StringBuilder();
        prodIdHql.append(
            "select distinct(utProd.utProdInstmId.productId) from UtProdInstm utProd, UTProdSwitchoutGroup swGroup where utProd.utProdInstmId.productId = swGroup.utProdSwitchoutGroupId.productId ");
        prodIdHql.append(
            "and utProd.productType= :productType and utProd.utProdInstmId.productId not in (:prodId) and swGroup.utProdSwitchoutGroupId.switchTableGroup in (:swithoutGroupList) ");
        prodIdHql.append("and utProd.allowSwInProdInd = 'Y' ");
        if (ListUtil.isValid(swithoutBlackList)) {
            prodIdHql.append("and utProd.symbol not in (:swithoutBlackList)");
        }
        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = utProd.utProdInstmId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            if (null != restrictGroupHql) {
                prodIdHql.append("and not exists (").append(restrictGroupHql).append(") ");
            }
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            prodIdHql.append("and nvl(utProd.restrOnlScribInd,' ') != :restrOnlScribInd ");
        }

        prodIdHql = QueryUtil.buildCmbSearchHql(prodIdHql, headers, "utProd");

        LogUtil.debug(FundSearchResultDaoImpl.class, "Hql query for appendSqlForCoreSwitchOut2: {}", prodIdHql.toString());

        Query prodIdQuery = createQueryForHql(prodIdHql.toString());
        prodIdQuery.setParameter("productType", fields[3]);
        prodIdQuery.setParameter("prodId", prodId);
        prodIdQuery.setParameter("swithoutGroupList", swithoutGroupList);
        if (ListUtil.isValid(swithoutBlackList)) {
            prodIdQuery.setParameter("swithoutBlackList", swithoutBlackList);
        }
        if (null != restrictGroupHql) {
            prodIdQuery.setParameter("channelComnCde", channelRestrictCode);
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            prodIdQuery.setParameter("restrOnlScribInd", restrOnlScribInd);
        }
        List<Integer> prodId_switchOut = prodIdQuery.getResultList();
        return prodId_switchOut;
    }

    public List<ProductKey> getProdAltNumFromDB(final FundSearchResultRequest request, final String field) throws Exception {
        List<ProductKey> ProductKeyList = new ArrayList<ProductKey>();

        List<Integer> tpProdIds = new ArrayList<Integer>();
        StringBuilder hql = new StringBuilder("select distinct symbol, productType, market from UtProdInstm where ");
        if (StringUtil.isValid(field)) {
            if (field.equals(this.hhhh_BEST_SELLER)) {
                hql.append("topSellProdIndex = :topSellProdIndex");
            }
            if (field.equals(this.hhhh_RETIREMENT_FUND)) {
            	hql.append("retireInvstInd = :retireInvstInd");
            }
            if (field.equals(this.hhhh_NEW_FUND)) {
                String date = getNewfundsRreturnTime();
                hql.append("prodLaunchDate >= " + "to_date( '" + date + "', 'yyyy-MM-dd')");
            }
            if (field.equals(this.hhhh_TOP5_PERFORMERS)) {
                tpProdIds = getTopPerfmSql(request);
                hql.append("utProdInstmId.productId in (:tpProdIds)");
            }
            if (field.equals(this.hhhh_FUND_OF_QUARTER)) {
                hql.append("listProdCode = :listProdCode and listProdType = :listProdType");
            }

            hql = QueryUtil.buildCmbSearchHql2(hql, request.getHeaders());

            Query query = createQueryForHql(hql.toString());
            if (field.equals(this.hhhh_BEST_SELLER)) {
                query.setParameter("topSellProdIndex", CommonConstants.YES);
            }
            if (field.equals(this.hhhh_RETIREMENT_FUND)) {
                query.setParameter("retireInvstInd", CommonConstants.YES);
            }
            if (field.equals(this.hhhh_TOP5_PERFORMERS)) {
                query.setParameter("tpProdIds", tpProdIds);
            }
            if (field.equals(this.hhhh_FUND_OF_QUARTER)) {
                query.setParameter("listProdCode", "SLT_1");
                query.setParameter("listProdType", "SLT");
            }

            List<Object[]> resultList = query.getResultList();
            if (ListUtil.isValid(resultList)) {
                for (Object[] result : resultList) {
                    if (null != result && result.length > 0) {
                        ProductKey productKey = new ProductKey();
                        productKey.setProdAltNum(MiscUtil.safeString(result[0]));
                        productKey.setProductType(MiscUtil.safeString(result[1]));
                        productKey.setMarket(MiscUtil.safeString(result[2]));
                        ProductKeyList.add(productKey);
                    }
                }
            }
        }
        return ProductKeyList;
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

    private String getSortDbFieldName(final String orderKey, final String countryCode, final String groupMember) {
        // Get value by CountryCode_GroupMember
        String exchange = countryCode + CommonConstants.SYMBOL_UNDERLINE + groupMember;
        Map<String, String> itemCodeMap = this.orderByFieldsMap.get(exchange);
        Object itemCode = itemCodeMap == null ? null : itemCodeMap.get(orderKey);

        // Get value by CountryCode
        if (itemCode == null && !exchange.equals(this.DEFAULT_ORDERBY)) {
            exchange = countryCode;
            itemCodeMap = this.orderByFieldsMap.get(exchange);
            itemCode = itemCodeMap == null ? null : itemCodeMap.get(orderKey);
        }
        // Get value by DEFAULT
        if (itemCode == null && !exchange.equals(this.DEFAULT_ORDERBY)) {
            itemCodeMap = this.orderByFieldsMap.get(this.DEFAULT_ORDERBY);
            itemCode = itemCodeMap.get(orderKey);
        }
        return itemCode == null ? null : itemCode.toString();
    }

    private String getDefaultSortDbFieldName(final String orderKey, final String countryCode, final String locale)
        throws Exception {
        int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
        String sortBy = null;
        if ("shortName".equalsIgnoreCase(orderKey)) {
            if (0 == index) {
                sortBy = "utProd.productShortName";
            } else if (1 == index) {
                sortBy = "utProd.productShortPrimaryLanguageName";
            } else if (2 == index) {
                sortBy = "utProd.productShortSecondLanguageName";
            } else {
                sortBy = "utProd.productShortName";
            }
        } else {
            if (0 == index) {
                sortBy = "utProd.prodName";
            } else if (1 == index) {
                sortBy = "utProd.prodPllName";
            } else if (2 == index) {
                sortBy = "utProd.prodSllName";
            } else {
                sortBy = "utProd.prodName";
            }
        }
        return sortBy;
    }


    private Map<String, Boolean> ifSearchDB(final Map<String, Boolean> holdingsValueMap) {
        Map<String, Boolean> holdingsFalseMap = new HashMap<>();
        if (!holdingsValueMap.isEmpty()) {
            Iterator iterator = this.holdingAllocationMap.entrySet().iterator();
            while (iterator.hasNext()) {
                String mapValue = (String) ((Map.Entry) iterator.next()).getValue();
                for (String criteriaKey : holdingsValueMap.keySet()) {
                    if (criteriaKey.equalsIgnoreCase(mapValue) && !holdingsValueMap.get(criteriaKey)) {
                        holdingsFalseMap.put(criteriaKey, holdingsValueMap.get(criteriaKey));
                    }
                }
            }
        }
        return holdingsFalseMap;
    }

    private List<Integer> getTopPerfmSql(final FundSearchResultRequest request) throws Exception {

        String productType = request.getProductType();
        List<SearchCriteria> detailedCriterias = request.getDetailedCriterias();
        String topPerfmList = this.siteFeature.getStringDefaultFeature(request.getSiteKey(), this.SITE_FEATURE_TOPPERFMLIST);
        String productSubType = null;
        String category = null;
        String prodStatCde = null;

        if (ListUtil.isValid(detailedCriterias)) {
            for (SearchCriteria searchCriteria : detailedCriterias) {
                if (Constants.PRODUCT_SUB_TYPE.equals(searchCriteria.getCriteriaKey())) {
                    productSubType = searchCriteria.getCriteriaValue();
                }
                if (Constants.CATEGORY_CODE.equals(searchCriteria.getCriteriaKey())) {
                    category = searchCriteria.getCriteriaValue();
                }
                if (Constants.PROD_STAT_CDE.equals(searchCriteria.getCriteriaKey())) {
                    prodStatCde = searchCriteria.getCriteriaValue();
                }
            }
        }

        StringBuilder topPerfmHql = new StringBuilder(
            "select prod_id from (select prod_id, fund_cat_lvl1_cde, rtrn_1yr_amt, rank() over(partition by fund_cat_lvl1_cde order by rtrn_1yr_dpn desc nulls last) num ");
        topPerfmHql.append("from v_ut_prod_instm where pri_share_class_ind = :priShareClassInd and fund_cat_lvl1_cde is not null ");

        // remove restrict products
        String channelRestrictCode = request.getChannelRestrictCode();
        String restrOnlScribInd = request.getRestrOnlScribInd();
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct prod_id from v_ut_prod_chanl ");
            restrictGroupHql
                .append("where v_ut_prod_chanl.prod_id = v_ut_prod_instm.prod_id and chanl_comn_cde = :channelComnCde ");
            if (null != restrictGroupHql) {
                topPerfmHql.append(" and not exists (").append(restrictGroupHql).append(") ");
            }
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            topPerfmHql.append(" and nvl(restr_onln_scrib_ind,' ') != :restrOnlScribInd ");
        }
        if (StringUtil.isValid(prodStatCde)) {
            topPerfmHql.append(" and nvl(prod_stat_cde,' ') != :prodStatCde ");
        }
        if (StringUtil.isValid(productType)) {
            topPerfmHql.append(" and prod_type_cde = :productType ");
        }
        if (StringUtil.isValid(category)) {
            topPerfmHql.append(" and fund_cat_cde = :category ");
        }
        if (StringUtil.isValid(productSubType)) {
            topPerfmHql.append(" and prod_subtp_cde = :productSubType ");
        }

        topPerfmHql = QueryUtil.buildCmbSearchSql2(topPerfmHql, request.getHeaders());

        topPerfmHql.append(") t where t.num <= " + topPerfmList + " ");
        topPerfmHql.append("order by fund_cat_lvl1_cde asc, rtrn_1yr_amt desc ");

        Query query = createQueryForNativeSql(topPerfmHql.toString());

        query.setParameter("priShareClassInd", CommonConstants.YES);
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", request.getChannelRestrictCode());
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter("restrOnlScribInd", restrOnlScribInd);
        }
        if (StringUtil.isValid(prodStatCde)) {
            query.setParameter("prodStatCde", prodStatCde);
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

        List<BigDecimal> tpProdIdsList = query.getResultList();
       // LogUtil.debugBeanToJson(FundSearchResultDaoImpl.class, "Result for fundSearch top performance from DB", tpProdIdsList);

        if (ListUtil.isValid(tpProdIdsList)) {
            List<Integer> tpProdIds = new ArrayList<Integer>();
            for (BigDecimal prodId : tpProdIdsList) {
                tpProdIds.add(prodId.intValue());
            }
            return tpProdIds;
        } else {
            return null;
        }
    }

    @Override
    public Map<Integer, List<String>> searchChanlFund(final String chanlRestCde) throws Exception {
        Map<Integer, List<String>> chanlMap = searchChanlFunds(chanlRestCde);
        return chanlMap;
    }

}