
package com.hhhh.group.secwealth.mktdata.fund.dao.impl;

import com.hhhh.group.secwealth.mktdata.common.criteria.ListValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.MinMaxValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.QueryUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.FundSearchCriteriaRequest;
import com.hhhh.group.secwealth.mktdata.fund.constants.FundSearchListCriteriaKeys;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;
import com.hhhh.group.secwealth.mktdata.fund.criteria.FundCriteriaKeyMapper;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.DetailedCriteriaUtil;
import com.hhhh.group.secwealth.mktdata.fund.dao.FundSearchCriteriaDao;
import com.hhhh.group.secwealth.mktdata.fund.dao.common.FundCommonDao;
import com.hhhh.group.secwealth.mktdata.fund.service.FundSearchCriteriaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Repository("fundSearchCriteriaDao")
public class FundSearchCriteriaDaoImpl extends FundCommonDao implements FundSearchCriteriaDao {

    private final String NULLS_LAST = "NULLS LAST";

    @Resource(name = "fundCriteriaKeyMapper")
    protected FundCriteriaKeyMapper criteriaKeyMapper;

    @Autowired
    @Qualifier("detailedCriteriaUtil")
    private DetailedCriteriaUtil detailedCriteriaUtil;

    protected static final List<String> EXCLUDE_LIST = Arrays.asList(new String[] {Constants.CRITERION_REQUEST_SWITCH_OUT_FUND,
        Constants.CRITERION_REQUEST_MONTHLY_INVESTMENT_PLAN, Constants.CRITERION_REQUEST_hhhh_RISK_LEVEL_RATING});

    @SuppressWarnings("unchecked")
    @Override
    public List<MinMaxValue> searchMinMaxCriteria(final FundSearchCriteriaRequest request,
        final String minMaxCriteria, final String countryCode, final String groupMember) throws Exception {
        LogUtil.debug(FundSearchCriteriaDaoImpl.class, "Control enter into the searchMinMaxCriteria for productType is: {}.",
                request.getProductType());
        if (minMaxCriteria == null) {
            return new ArrayList<>();
        }

        List<String> fieldNames = new ArrayList<String>();
        validatedCriteriaString(minMaxCriteria);
        String[] minMaxCriterias = minMaxCriteria.split(Constants.CRITERIA_SEPARATOR);
        StringBuilder hql = new StringBuilder("select ");
        for (String Criteria : minMaxCriterias) {
            validateCriteriaKey(Criteria.trim(), countryCode, groupMember);
            fieldNames.add(Criteria.trim());
        }
        hql = format(hql, Constants.TABLE_ALIAS_UT_PROD, fieldNames, countryCode, groupMember).append(" from UtProdInstm utProd");
        if (StringUtil.isValid(request.getProductType())) {
            hql.append(" where utProd.productType = :productType ");
        }

        // remove restrict products
        StringBuilder restrictGroupHql = removeRestrictHql(hql,request.getChannelRestrictCode());
        if (StringUtil.isValid(request.getRestrOnlScribInd())) {
            hql.append(" and nvl(utProd.restrOnlScribInd,' ') != :restrOnlScribInd ");
        }
        if (ListUtil.isValid(request.getPredefinedCriterias())) {
            appendPredefinedCriterias(hql, request.getPredefinedCriterias(), countryCode, groupMember);
        }
        LogUtil.debug(FundSearchCriteriaDaoImpl.class, "Hql query for MinMaxCriteriaSearchResult  " + hql.toString());

        hql = QueryUtil.buildCmbSearchHql(hql, request.getHeaders(), "utProd");

        Query query = createQueryForHql(hql.toString());
        setQueryParam(request.getProductType(),query,restrictGroupHql,request.getChannelRestrictCode(),request.getRestrOnlScribInd());

        try {
            List<Object[]> result = query.getResultList();
            List<MinMaxValue> minMaxValueList = new ArrayList<MinMaxValue>();
            for (int i = 0; i < fieldNames.size(); i++) {
                MinMaxValue minMaxValue = new MinMaxValue();
                for (Object[] dbResultLine : result) {
                    minMaxValue.setCriteriaKey(fieldNames.get(i));
                    minMaxValue.setMaxCriteria(dbResultLine[i * 2]);
                    minMaxValue.setMinCriteria(dbResultLine[i * 2 + 1]);
                }
                minMaxValueList.add(minMaxValue);
            }
            LogUtil.debug(FundSearchCriteriaDaoImpl.class, "Exit from the searchMinMaxCriteria.");
            LogUtil.debugBeanToJson(FundSearchCriteriaDaoImpl.class, "Result for searchMinMaxCriteria from DB", minMaxValueList);
            return minMaxValueList;
        } catch (Exception e) {
            LogUtil.error(FundSearchCriteriaDaoImpl.class, "searchMinMaxCriteria from DB error", e);
            throw new SystemException(ErrTypeConstants.DB_DATA_UNAVAILABLE);
        }
    }


    @SuppressWarnings("unchecked")
    public List<ListValue> searchListCriteria(final FundSearchCriteriaRequest request,
        final String listField, final String locale, final String countryCode, final String groupMember) throws Exception {
        LogUtil.debug(FundSearchCriteriaDaoImpl.class, "Control enter into the searchListCriteria for productType is:{}.",
                request.getProductType());
        if (listField == null) {
            return new ArrayList<>();
        }

        List<String> fieldNames = new ArrayList<String>();
        validatedCriteriaString(listField);
        String[] listFields = listField.split(Constants.CRITERIA_SEPARATOR);
        for (String field : listFields) {
            validateCriteriaKey(field.trim(), countryCode, groupMember);
            fieldNames.add(field.trim());
        }
        List<ListValue> listCriterias = new ArrayList<ListValue>();
        long startTime = System.currentTimeMillis();
        int index = this.localeMappingUtil.getNameByIndex(countryCode + CommonConstants.SYMBOL_DOT + locale);
        for (String fieldName : fieldNames) {
            ListValue listCriteria = new ListValue();
            listCriteria.setCriteriaKey(fieldName);

            StringBuilder hql = new StringBuilder();
            switch(fieldName){
                case "FAM":
                    hql = new StringBuilder(
                            "select distinct utProd.familyCode, utProd.familyName1, utProd.familyName2, utProd.familyName3, utProd.familySequencenNum, ''");
                    break;
                case "CAT":
                    hql = new StringBuilder(
                            "select distinct utProd.categoryCode, utProd.categoryName1, utProd.categoryName2, utProd.categoryName3, utProd.categorySequencenNum, utProd.categoryLevel1Name"
                                    + (index + 1));
                    break;
                case "RISK":
                    hql = new StringBuilder("select distinct utProd.riskLvlCde, '', '', '', '', ''");
                    break;
                case "CCY":
                    hql = new StringBuilder("select distinct utProd.currency, '', '', '', '', ''");
                    break;
                case "DF":
                    hql = new StringBuilder("select distinct utProd.distributionFrequency, '', '', '', '', ''");
                    break;
                case "Y1QTL":
                    hql = new StringBuilder("select distinct utProd.rank1Yr, '', '', '', '', ''");
                    break;
                case "Y3QTL":
                    hql = new StringBuilder("select distinct utProd.rank3Yr, '', '', '', '', ''");
                    break;
                case "Y5QTL":
                    hql = new StringBuilder("select distinct utProd.rank5Yr, '', '', '', '', ''");
                    break;
                case "Y10QTL":
                    hql = new StringBuilder("select distinct utProd.rank10Yr, '', '', '', '', ''");
                    break;
                case "ACQN":
                    hql = new StringBuilder(
                            "select distinct utProd.averageCreditQualityName, utProd.averageCreditQualityNum, '', '', '', ''");
                    break;
                case "CATLV1":
                    hql = new StringBuilder(
                            "select distinct utProd.categoryLevel1Code, utProd.categoryLevel1Name1, utProd.categoryLevel1Name2, utProd.categoryLevel1Name3, utProd.categoryLevel1SequencenNum, ''");
                    break;
                case "INVSTRG":
                    hql = new StringBuilder(
                            "select distinct utProd.investmentRegionCode, utProd.investmentRegionName1, utProd.investmentRegionName2, utProd.investmentRegionName3, utProd.investmentRegionSequencenNum, ''");
                    break;
                case "AMCM":
                    hql = new StringBuilder(
                            "select distinct utProd.amcmAuthorizeIndicator, utProd.amcmAuthorizeIndicator, '', '', '', ''");
                    break;
                case "PSC":
                    hql = new StringBuilder("select distinct utProd.prodStatCde, utProd.prodStatCde, '', '', '', ''");
                    break;
                case "PCDI":
                    hql = new StringBuilder("select distinct utProd.payCashDivInd, utProd.payCashDivInd, '', '', '', ''");
                    break;
                case "GBAA":
                    hql = new StringBuilder("select distinct utProd.gbaAcctTrdb, utProd.gbaAcctTrdb, '', '', '', ''");
                    break;
                case "GNRA":
                    hql = new StringBuilder("select distinct utProd.gnrAcctTrdb, utProd.gnrAcctTrdb, '', '', '', ''");
                    break;
                case "SIFC":
                    hql = new StringBuilder("select distinct utProd.siFundCategoryCode, utProd.siFundCategoryCode, '', '', '', ''");
                    break;
                case "SRLN":
                    hql = new StringBuilder("select distinct utProd.shortLstRPQLvlNum, utProd.shortLstRPQLvlNum, '', '', '', ''");
                    break;
                case "ESG":
                    hql = new StringBuilder("select distinct utProd.esgInd, utProd.esgInd, '', '', '', ''");
                    break;
                default:
            }
            hql.append(" from UtProdInstm utProd ");
            if (request.getProductType() != null) {
                hql.append(" where utProd.productType = :productType ");
            }

            hql = QueryUtil.buildCmbSearchHql(hql, request.getHeaders(), "utProd");

            // remove restrict products
            StringBuilder restrictGroupHql = removeRestrictHql(hql,request.getChannelRestrictCode());
            if (StringUtil.isValid(request.getRestrOnlScribInd())) {
                hql.append(" and nvl(utProd.restrOnlScribInd,' ') != :restrOnlScribInd ");
            }
            if (ListUtil.isValid(request.getPredefinedCriterias())) {
                appendPredefinedCriterias(hql, request.getPredefinedCriterias(), countryCode, groupMember);
            }

            // Sort HQL
            switch(fieldName){
                case "FAM":
                    hql.append(" order by utProd.familySequencenNum");
                    break;
                case "CAT":
                    hql.append(" order by utProd.categorySequencenNum");
                    hql =  concatSortSql(hql,index,"categoryName");
                    break;
                case "RISK":
                    hql.append(" order by utProd.riskLvlCde");
                    break;
                case "CCY":
                    hql.append(" order by utProd.currency");
                    break;
                case "DF":
                    hql.append(" order by utProd.distributionFrequency asc ").append(this.NULLS_LAST);
                    break;
                case "Y1QTL":
                    hql.append(" order by utProd.rank1Yr asc ").append(this.NULLS_LAST);
                    break;
                case "Y3QTL":
                    hql.append(" order by utProd.rank3Yr asc ").append(this.NULLS_LAST);
                    break;
                case "Y5QTL":
                    hql.append(" order by utProd.rank5Yr asc ").append(this.NULLS_LAST);
                    break;
                case "Y10QTL":
                    hql.append(" order by utProd.rank10Yr asc ").append(this.NULLS_LAST);
                    break;
                case "ACQN":
                    hql.append(" order by utProd.averageCreditQualityNum desc ").append(this.NULLS_LAST);
                    break;
                case "CATLV1":
                    hql.append(" order by utProd.categoryLevel1SequencenNum");
                    hql =  concatSortSql(hql,index,"categoryLevel1Name");
                    break;
                case "INVSTRG":
                    hql.append(" order by utProd.investmentRegionSequencenNum");
                    hql =  concatSortSql(hql,index,"investmentRegionName");
                    break;
                case "AMCM":
                    hql.append(" order by utProd.amcmAuthorizeIndicator desc ").append(this.NULLS_LAST);
                    break;
                case "PSC":
                    hql.append(" order by utProd.prodStatCde desc ").append(this.NULLS_LAST);
                    break;
                case "PCDI":
                    hql.append(" order by utProd.payCashDivInd desc ").append(this.NULLS_LAST);
                    break;
                case "GBAA":
                    hql.append(" order by utProd.gbaAcctTrdb desc ").append(this.NULLS_LAST);
                    break;
                case "GNRA":
                    hql.append(" order by utProd.gnrAcctTrdb desc ").append(this.NULLS_LAST);
                    break;
                case "SIFC":
                    hql.append(" order by utProd.siFundCategoryCode desc ").append(this.NULLS_LAST);
                    break;
                case "SRLN":
                    hql.append(" order by utProd.shortLstRPQLvlNum desc ").append(this.NULLS_LAST);
                    break;
                case "ESG":
                    hql.append(" order by utProd.esgInd desc ").append(this.NULLS_LAST);
                    break;
                default:
            }
            // query
            LogUtil.debug(FundSearchCriteriaDaoImpl.class, "searchListCriteria HQL: " + hql.toString());
            Query query = createQueryForHql(hql.toString());
            setQueryParam(request.getProductType(),query,restrictGroupHql,request.getChannelRestrictCode(),request.getRestrOnlScribInd());
            long startTime1 = System.currentTimeMillis();
            List<Object[]> resultLists = query.getResultList();
            LogUtil.debug(FundSearchCriteriaDaoImpl.class,
                "[SearchCriteria] sql resultLists cost Time :" + Long.toString(System.currentTimeMillis() - startTime1));

            collectListCriteria(index,listCriteria,fieldName,resultLists);
            listCriterias.add(listCriteria);
        }

        LogUtil.debug(FundSearchCriteriaDaoImpl.class, "Exit from the searchListCriteria.");
        long endTime = System.currentTimeMillis();
        LogUtil.debug(FundSearchCriteriaServiceImpl.class,
            "[SearchCriteria] deal ListCriteria cost Time : " + Long.toString(endTime - startTime));
        return listCriterias;
    }

    public void setQueryParam(String productType,Query query,StringBuilder restrictGroupHql,String channelRestrictCode,String restrOnlScribInd){
        if (productType != null) {
            query.setParameter("productType", productType);
        }
        if (null != restrictGroupHql) {
            query.setParameter("channelComnCde", channelRestrictCode);
        }
        if (StringUtil.isValid(restrOnlScribInd)) {
            query.setParameter("restrOnlScribInd", restrOnlScribInd);
        }

    }

    public StringBuilder removeRestrictHql(StringBuilder hql,String channelRestrictCode){
        // remove restrict products
        StringBuilder restrictGroupHql = null;
        if (StringUtil.isValid(channelRestrictCode)) {
            restrictGroupHql = new StringBuilder("select distinct(chanl.utProdChanlId.prodId) from UtProdChanl chanl ");
            restrictGroupHql.append("where chanl.utProdChanlId.prodId = utProd.utProdInstmId.productId ");
            restrictGroupHql.append("and chanl.channelComnCde = :channelComnCde ");
            hql.append("and not exists (").append(restrictGroupHql).append(") ");
        }
        return restrictGroupHql;
    }


    public void setListCriteriaByIndex(int index,ListValue listCriteria,Object[] dbResultLine){
        if (1 == index) {
            listCriteria.addMapItem(dbResultLine[0], dbResultLine[2]);
        } else if (2 == index) {
            listCriteria.addMapItem(dbResultLine[0], dbResultLine[3]);
        } else {
            listCriteria.addMapItem(dbResultLine[0], dbResultLine[1]);
        }
        if (dbResultLine[5] != null) {
            listCriteria.addMapItem(dbResultLine[0] + FundSearchCriteriaServiceImpl.PARENT_KEY, dbResultLine[5]);
        }

    }
    public void collectListCriteria(int index,ListValue listCriteria,String fieldName,List<Object[]> resultLists){
        if (ListUtil.isValid(resultLists)) {
            if (FundSearchListCriteriaKeys.isLocale(fieldName)) {
                for (Object[] dbResultLine : resultLists) {
                    setListCriteriaByIndex(index,listCriteria,dbResultLine);
                }
            } else {
                for (Object[] dbResultLine : resultLists) {
                    listCriteria.addMapItem(dbResultLine[0], dbResultLine[0]);
                    if (dbResultLine[5] != null) {
                        listCriteria.addMapItem(dbResultLine[0] + FundSearchCriteriaServiceImpl.PARENT_KEY, dbResultLine[5]);
                    }
                }
            }
        }
    }

    public StringBuilder concatSortSql(StringBuilder hql,int index,String filed){

        if (CommonConstants.SIMPLIFIED_CHINESE == index) {
            // NLS_SORT=SCHINESE_PINYIN_M is oracle sql feature
            hql.append(",nlssort(utProd."+ filed + (index + 1) + ",'NLS_SORT=SCHINESE_PINYIN_M')");
        } else if (CommonConstants.TRADITIONAL_CHINESE == index) {
            // NLS_SORT=TCHINESE_STROKE_M is oracle sql feature
            hql.append(",nlssort(utProd."+ filed + (index + 1) + ",'NLS_SORT=TCHINESE_STROKE_M')");
        } else {
            hql.append(",utProd."+ filed + (index + 1));
        }
        return hql;
    }

    public List<SearchCriteria> appendPredefinedCriterias(final StringBuilder hql, final List<SearchCriteria> detailedCriterias,
        final String countryCode, final String groupMember)  {
        String str = "";
        str = this.detailedCriteriaUtil.toString(detailedCriterias, Constants.TABLE_ALIAS_UT_PROD, null, null, countryCode,
            groupMember);
        if (StringUtil.isValid(str)) {
            hql.append(" and ").append(str);
        }
        return detailedCriterias;
    }

    public String validatedCriteriaString(final String FieldString){
        if (null != FieldString && FieldString.trim().length() == Constants.ZERO) {
            LogUtil.error(FundSearchCriteriaDaoImpl.class, "The criteria keys should not be empty.");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        return FieldString;
    }

    public void validateCriteriaKey(final String criteriaKey, final String countryCode, final String groupMember){
        if (this.criteriaKeyMapper != null && this.criteriaKeyMapper.getDbFieldName(criteriaKey, countryCode, groupMember) == null) {
            // check exclude list
            if (FundSearchCriteriaDaoImpl.EXCLUDE_LIST.contains(criteriaKey)) {
                LogUtil.debug(FundSearchCriteriaDaoImpl.class, "Criteria key {} is found", criteriaKey);
            }else{
                LogUtil.error(FundSearchCriteriaDaoImpl.class, "Criteria key {} is not found", criteriaKey);
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }
        }
    }

    public StringBuilder format(final StringBuilder hql, final String tableName, final List<String> fieldNames,
        final String countryCode, final String groupMember) {
        for (String fieldName : fieldNames) {
            hql.append(",max(")
                .append(this.prefixTableAlias(tableName, this.getMappedCriteriaKey(fieldName, countryCode, groupMember)))
                .append(")");
            hql.append(",min(")
                .append(this.prefixTableAlias(tableName, this.getMappedCriteriaKey(fieldName, countryCode, groupMember)))
                .append(")");
        }
        return new StringBuilder(hql.toString().replaceFirst(",", Constants.BLANK));
    }

    public String getMappedCriteriaKey(final String criteriaKey, final String countryCode, final String groupMember) {
        if (this.criteriaKeyMapper == null) {
            return criteriaKey;
        } else {
            return this.criteriaKeyMapper.getDbFieldName(criteriaKey, countryCode, groupMember);
        }
    }

    public String prefixTableAlias(final String tableName, final String fieldName) {
        if (tableName == null || fieldName == null || fieldName.contains(".")) {
            return fieldName;
        } else {
            return new StringBuilder(tableName).append(".").append(fieldName).toString();
        }
    }

}
