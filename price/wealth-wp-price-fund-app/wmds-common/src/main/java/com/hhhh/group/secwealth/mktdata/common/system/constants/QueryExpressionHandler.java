/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2014. ALL RIGHTS RESERVED.
 * 
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.system.constants;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryParser.QueryParser;

import com.hhhh.group.secwealth.mktdata.common.criteria.CriteriaOperator;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

/**
 * <p>
 * <b> Build query expression . </b>
 * </p>
 */
public class QueryExpressionHandler {

    /** The Constant PRODUCT_TYPE_ANALYZED. */
    public final static String PRODUCT_TYPE_ANALYZED = "productType_analyzed:";

    /** The Constant PRODUCT_SUBTYPE_ANALYZED. */
    public final static String PRODUCT_SUBTYPE_ANALYZED = "productSubType_analyzed:";

    /** The Constant SYMBOL_ANALYZED. */
    public final static String SYMBOL_ANALYZED = "symbol_analyzed:";

    /** The Constant countryTradableCode. */
    public final static String COUNTRYTRADABLECODE_ANALYZED = "countryTradableCode_analyzed:";

    /** The Constant KEY_ANALYZED. */
    public final static String KEY_ANALYZED = "key_analyzed:";

    /** The Constant EQUAL_SEARCH. */
    public final static String EQUAL_SEARCH = "_analyzed:";

    /** The Constant PRODUCT_KEY. */
    public final static String PRODUCT_KEY = "productKey";

    /** The Constant SWITCHABLE_GROUP. */
    public final static String SWITCHABLE_GROUP = "switchableGroup";

    /** The Constant switchOutFund. */
    public final static String SWITCHOUT_FUND = "switchOutFund";

    /** The Constant PST(productSubType). */
    public final static String PRODSUBTYPE_FUND = "PST";

    /** The Constant allowSwInProdInd. */
    public final static String ALLOWSWIN_FUND = "allowSwInProdInd";

    /** The Constant allowSwOutProdInd. */
    public final static String ALLOWSWOUT_FUND = "allowSwOutProdInd";

    /** The Constant allowTradeProdInd. */
    public final static String ALLOWTRADEPROD_FUND = "allowTradeProdInd";

    /** The Constant PRODUCTCODE_ANALYZED. */
    public final static String PRODUCTCODE_ANALYZED = "productCode_analyzed:";

    /** The Constant SWITCHABLE_GROUP. */
    public final static String RESCHANNELCDE = "resChannelCde";

    /** The Constant RESTRICT ONLINE SUBSCRIPTION INDICATOR. */
    public final static String RESTRONLSCRIBIND = "restrOnlScribInd";

    public final static String QUERY_TYPE_EXACT = "exact";

    public final static String QUERY_TYPE_START_WITH = "startWith";

    public final static String QUERY_TYPE_CONTAINS = "contains";

    public final static String WHITE_SPACE_PATTERN = "\\u0020";

    public final static String PRODUCT_TYPE_FUND = "UT";

    public static String[] getSortingFieldsBySteps(final int step) {
        String[] sortingFields = null;
        switch (step) {
        case 1:
            sortingFields = new String[] {"productTypeWeight", "countryTradableCodeWeight", "productName"};
            break;
        case 2:
            sortingFields = new String[] {"productTypeWeight", "countryTradableCodeWeight", "symbol"};
            break;
        case 3:
            sortingFields = new String[] {"productTypeWeight", "countryTradableCodeWeight", "symbol", "productName"};
            break;
        case 4:
            sortingFields = new String[] {"productTypeWeight", "countryTradableCodeWeight", "productName", "symbol"};
            break;
        case 5:
            sortingFields = new String[] {"productTypeWeight", "countryTradableCodeWeight", "symbol", "productName"};
            break;
        case 6:
            sortingFields = new String[] {"productTypeWeight", "countryTradableCodeWeight", "productName", "symbol"};
            break;
        }
        return sortingFields;
    }

    public static String getSearchFieldsBySteps(final int step) {
        String searchFields = "symbol";
        switch (step) {
        case 1:
            searchFields = "symbol";
            break;
        case 2:
            searchFields = "productName";
            break;
        case 3:
            searchFields = "symbol";
            break;
        case 4:
            searchFields = "productName";
            break;
        case 5:
            searchFields = "symbol";
            break;
        case 6:
            searchFields = "productName";
            break;
        }
        return searchFields;
    }

    public static String getQueryTypeBySteps(final int step) {
        if (step == 1 || step == 2) {
            return QueryExpressionHandler.QUERY_TYPE_EXACT;
        } else if (step == 3 || step == 4) {
            return QueryExpressionHandler.QUERY_TYPE_START_WITH;
        } else {
            return QueryExpressionHandler.QUERY_TYPE_CONTAINS;
        }
    }

    public static String toQueryString(final String keyWord) {
        if (keyWord != null) {
            return StringUtils.replace(QueryParser.escape(keyWord), CommonConstants.SPACE,
                QueryExpressionHandler.WHITE_SPACE_PATTERN).toLowerCase();
        }
        return null;
    }

    public static void constructContainsQueryExpression(final String searchField, final String keyWord,
        final StringBuffer searchClauses) {
        searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET).append(searchField).append(QueryExpressionHandler.EQUAL_SEARCH)
            .append(CommonConstants.SYMBOL_ASTERISK).append(toQueryString(keyWord)).append(CommonConstants.SYMBOL_ASTERISK)
            .append(CommonConstants.SYMBOL_RIGHT_BRACKET);
    }

    public static void constructStartWithQueryExpression(final String searchField, final String keyWord,
        final StringBuffer searchClauses) {
        searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET).append(searchField).append(QueryExpressionHandler.EQUAL_SEARCH)
            .append(toQueryString(keyWord)).append(CommonConstants.SYMBOL_ASTERISK).append(CommonConstants.SYMBOL_RIGHT_BRACKET);
    }

    public static void constructExactQueryExpression(final String searchField, final String keyWord,
        final StringBuffer searchClauses) {
        searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET).append(searchField).append(QueryExpressionHandler.EQUAL_SEARCH)
            .append(toQueryString(keyWord)).append(CommonConstants.SYMBOL_RIGHT_BRACKET);
    }

    public static String buildCommonQueryExpression(final String queryType, final String[] assetClasses, final String searchField,
        final String keyWord, final List<SearchCriteria> filters) throws Exception {
        StringBuffer searchClauses = new StringBuffer();
        int flag = 0;
        if (null != keyWord) {
            flag = 1;
            if (QueryExpressionHandler.QUERY_TYPE_EXACT.equals(queryType)) {
                constructExactQueryExpression(searchField, keyWord, searchClauses);
            } else if (QueryExpressionHandler.QUERY_TYPE_START_WITH.equals(queryType)) {
                constructStartWithQueryExpression(searchField, keyWord, searchClauses);
            } else if (QueryExpressionHandler.QUERY_TYPE_CONTAINS.equals(queryType)) {
                constructContainsQueryExpression(searchField, keyWord, searchClauses);
            }
        }
        // The record will be filtered by OR expression statement
        if (null != filters && filters.size() > 0) {
            // flag!=0 means have keyword expression,should add "AND".
            if (flag != 0) {
                searchClauses.append(CriteriaOperator.AND);
            } else {
                flag = 1;
            }
            // predSearch filter "channelRestrictCode", condition is:
            // "(symbol_analyzed:*u622*) AND (NOT resChannelCde_analyzed:srbpi) AND (productType_analyzed:UT)"
            // can't get expect data, so update condition:
            // (symbol_analyzed:*u622*) AND NOT resChannelCde_analyzed:srbpi
            // AND (productType_analyzed:UT)
            if (filters.size() != 1 && !(filters.get(0).getOperator().equalsIgnoreCase(CriteriaOperator.NE_OPERATOR))) {
                searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET);
            }
            int size = filters.size();
            searchClauses.append(getFilterQuery(filters.get(0).getCriteriaKey(), filters.get(0).getCriteriaValue(), filters.get(0)
                .getOperator()));
            for (int j = 1; j < size; j++) {
                searchClauses.append(CriteriaOperator.AND)
                    .append(
                        getFilterQuery(filters.get(j).getCriteriaKey(), filters.get(j).getCriteriaValue(), filters.get(j)
                            .getOperator()));
            }
            if (filters.size() != 1 && !(filters.get(0).getOperator().equalsIgnoreCase(CriteriaOperator.NE_OPERATOR))) {
                searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
            }
        }
        if (!CommonConstants.ALL.equals(assetClasses[0])) {
            if (flag != 0) {
                searchClauses.append(CriteriaOperator.AND);
            }
            searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET).append(addProductType(assetClasses[0]));
            for (int i = 1; i < assetClasses.length; i++) {
                searchClauses.append(CriteriaOperator.OR).append(addProductType(assetClasses[i]));
            }
            searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
        }
        return searchClauses.toString();
    }
    
    public static String buildQueriesExpression(final String[] assetClasses, final String searchField,
            final String[] keyWord, final List<SearchCriteria> filters) throws Exception {
            StringBuffer searchClauses = new StringBuffer();
            int flag = 0;
            if (null != keyWord) {
                flag = 1;
                searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET);
                constructExactQueryExpression(searchField, keyWord[0], searchClauses);
                for (int i = 1; i < keyWord.length; i++) {
                    searchClauses.append(CriteriaOperator.OR);
                    constructExactQueryExpression(searchField, keyWord[i], searchClauses);
                }
                searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
            }
            // The record will be filtered by OR expression statement
            if (null != filters && filters.size() > 0) {
                // flag!=0 means have keyword expression,should add "AND".
                if (flag != 0) {
                    searchClauses.append(CriteriaOperator.AND);
                } else {
                    flag = 1;
                }
                // predSearch filter "channelRestrictCode", condition is:
                // "(symbol_analyzed:*u622*) AND (NOT resChannelCde_analyzed:srbpi) AND (productType_analyzed:UT)"
                // can't get expect data, so update condition:
                // (symbol_analyzed:*u622*) AND NOT resChannelCde_analyzed:srbpi
                // AND (productType_analyzed:UT)
                if (filters.size() != 1 && !(filters.get(0).getOperator().equalsIgnoreCase(CriteriaOperator.NE_OPERATOR))) {
                    searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET);
                }
                int size = filters.size();
                searchClauses.append(getFilterQuery(filters.get(0).getCriteriaKey(), filters.get(0).getCriteriaValue(), filters.get(0)
                    .getOperator()));
                for (int j = 1; j < size; j++) {
                    searchClauses.append(CriteriaOperator.AND)
                        .append(
                            getFilterQuery(filters.get(j).getCriteriaKey(), filters.get(j).getCriteriaValue(), filters.get(j)
                                .getOperator()));
                }
                if (filters.size() != 1 && !(filters.get(0).getOperator().equalsIgnoreCase(CriteriaOperator.NE_OPERATOR))) {
                    searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
                }
            }
            if (!CommonConstants.ALL.equals(assetClasses[0])) {
                if (flag != 0) {
                    searchClauses.append(CriteriaOperator.AND);
                }
                searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET).append(addProductType(assetClasses[0]));
                for (int i = 1; i < assetClasses.length; i++) {
                    searchClauses.append(CriteriaOperator.OR).append(addProductType(assetClasses[i]));
                }
                searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
            }
            return searchClauses.toString();
        }

    /**
     * Builds the search clauses.
     * 
     * @param keyWord
     *            the key word
     * @param assetClass
     *            the asset class
     * @param searchFields
     *            the search fields
     * @param filters
     *            the filters
     * @param searchTypeByField
     *            the search type by field
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String buildQueryExpression(final String keyWord, final String[] assetClass, final String[] searchFields,
        final List<SearchCriteria> filters, final Map<String, String> searchTypeByField) throws Exception {

        StringBuffer searchClauses = new StringBuffer();
        // Indicator for expression builder
        int flag = 0;
        if (null != keyWord) {
            flag = 1;
            searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET);
            StringBuffer key = new StringBuffer().append(toQueryString(keyWord)).append(CommonConstants.SYMBOL_ASTERISK);
            searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET).append(searchTypeByField.get(searchFields[0])).append(key)
                .append(CommonConstants.SYMBOL_RIGHT_BRACKET);
            for (int i = 1; i < searchFields.length; i++) {
                searchClauses.append(CriteriaOperator.OR).append(CommonConstants.SYMBOL_LEFT_BRACKET)
                    .append(searchTypeByField.get(searchFields[i])).append(key).append(CommonConstants.SYMBOL_RIGHT_BRACKET);
            }
            searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
        }

        // The record will be filtered by OR expression statement
        if (null != filters && filters.size() > 0) {
            // flag!=0 means have keyword expression,should add "AND".
            if (flag != 0) {
                searchClauses.append(CriteriaOperator.AND);
            } else {
                flag = 1;
            }
            searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET);
            int size = filters.size();
            searchClauses.append(getFilterQuery(filters.get(0).getCriteriaKey(), filters.get(0).getCriteriaValue(), filters.get(0)
                .getOperator()));
            for (int j = 1; j < size; j++) {
                searchClauses.append(CriteriaOperator.AND)
                    .append(
                        getFilterQuery(filters.get(j).getCriteriaKey(), filters.get(j).getCriteriaValue(), filters.get(j)
                            .getOperator()));
            }
            searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
        }

        if (!CommonConstants.ALL.equals(assetClass[0])) {
            if (flag != 0) {
                searchClauses.append(CriteriaOperator.AND);
            }
            searchClauses.append(CommonConstants.SYMBOL_LEFT_BRACKET).append(addProductType(assetClass[0]));
            for (int k = 1; k < assetClass.length; k++) {
                searchClauses.append(CriteriaOperator.OR).append(addProductType(assetClass[k]));
            }
            searchClauses.append(CommonConstants.SYMBOL_RIGHT_BRACKET);
        }
        return searchClauses.toString();
    }

    /**
     * Gets the filter query.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @param operator
     *            the operator
     * @return the filter query
     * @throws Exception
     *             the exception
     */
    public static String getFilterQuery(final String key, final String value, final String operator) throws Exception {
        if (operator.equalsIgnoreCase(CommonConstants.SYMBOL_EQUAL) || operator.equalsIgnoreCase(CriteriaOperator.EQ_OPERATOR)) {
            return new StringBuilder(key).append(QueryExpressionHandler.EQUAL_SEARCH).append(toQueryString(value)).toString();
        } else if (operator.equalsIgnoreCase(CriteriaOperator.NE_OPERATOR)) {
            if (key.equalsIgnoreCase(QueryExpressionHandler.PRODUCT_KEY)) {
                // prodCdeAltClassCde:prodAltNum:Market:productType
                String[] fields = value.split(CommonConstants.SYMBOL_COLON);
                return new StringBuilder(CriteriaOperator.NOT).append(CommonConstants.SYMBOL_LEFT_BRACKET)
                    .append(getProductByKey(fields[1], fields[2], fields[3])).append(CommonConstants.SYMBOL_RIGHT_BRACKET)
                    .toString();
            } else {
                return new StringBuilder(CriteriaOperator.NOT).append(key).append(QueryExpressionHandler.EQUAL_SEARCH)
                    .append(toQueryString(value)).toString();
            }
        } else if (operator.equalsIgnoreCase(CriteriaOperator.IN_OPERATOR)) {
            String[] strs = value.split(CommonConstants.SYMBOL_COLON);
            String keyAppender = new StringBuilder(key).append(QueryExpressionHandler.EQUAL_SEARCH).toString();
            StringBuilder sb = new StringBuilder(CommonConstants.SYMBOL_LEFT_BRACKET).append(keyAppender).append(
                toQueryString(strs[0]));
            for (int i = 1; i < strs.length; i++) {
                sb.append(CriteriaOperator.OR).append(keyAppender).append(toQueryString(strs[i]));
            }
            return sb.append(CommonConstants.SYMBOL_RIGHT_BRACKET).toString();
        } else {
            LogUtil.error(QueryExpressionHandler.class, "Unrecognized operator");
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }

    }

    /**
     * <p>
     * <b> Return a expression by symbol,countryTradeableCode and productType
     * </b>
     * </p>
     * .
     * 
     * @param symbol
     *            the symbol
     * @param countryTradableCode
     *            the country tradable code
     * @param productType
     *            the product type
     * @return the product by key
     */
    public static String getProductByKey(final String symbol, final String countryTradableCode, final String productType) {
        return new StringBuilder(QueryExpressionHandler.SYMBOL_ANALYZED).append(toQueryString(symbol)).append(CriteriaOperator.AND)
            .append(QueryExpressionHandler.COUNTRYTRADABLECODE_ANALYZED).append(countryTradableCode).append(CriteriaOperator.AND)
            .append(addProductType(productType)).toString();

    }

    /**
     * Adds the product type.
     * 
     * @param productType
     *            the product type
     * @return the string
     */
    public static String addProductType(final String productType) {
        int find = productType.indexOf(CommonConstants.SYMBOL_UNDERLINE);
        if (find != -1) {
            // The productSubType in index is productType+"_"+productSubType
            return new StringBuffer(QueryExpressionHandler.PRODUCT_SUBTYPE_ANALYZED).append(productType).toString();
        } else {
            return new StringBuffer(QueryExpressionHandler.PRODUCT_TYPE_ANALYZED).append(productType).toString();
        }
    }

}
