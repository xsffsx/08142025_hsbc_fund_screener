
package com.hhhh.group.secwealth.mktdata.fund.criteria;


public final class Constants {
    private Constants() {}

    public static final String BLANK = "";
    public static final String CRITERIA_SEPARATOR = ",";
    public static final String CRITERIA_ITEM_SEPARATOR = "\\|";
    public static final String APOSTROPHE = "'";
    public static final String MINMAXFIELD_ERROR_MESSAGE = "minMaxCriteriaKeys should not be empty";
    public static final String LISTFIELD_ERROR_MESSAGE = "listField's should not be empty";
    public static final int ZERO = 0;
    public static final String MARKETCODE_ERROR_MESSAGE = "The requested market code should not be empty";
    public static final String TIMESCALE_ERROR_MESSAGE = "TimeScale should not be null and empty";
    public static final String CRITERIA_ERROR_MESSAGE = "Criteria parsing error.";
    public static final String ALL = "ALL";
    public static final String TABLE_ALIAS_BOND = "tblBond";
    public static final String TABLE_ALIAS_FUND = "tblFund";
    public static final String TABLE_ALIAS_PROD_ID = "tblProdId";
    public static final String TABLE_ALIAS_PROD = "tblProd";

    public static final String TABLE_ALIAS_ETF = "tblETF";

    public static final String TABLE_ALIAS_UT_PROD = "utProd";
    public static final String TABLE_ALIAS_UT_RTN = "utRtn";

    public static final Long MAX_RESULT_SEARCH_FUND = Long.valueOf(5000);
    public static final Long MAX_RESULT_SEARCH_BOND = Long.valueOf(100);
    public static final String ONLINE_UPDATION = "ONLINE";
    public static final String PRICE_QUOTE_DATE_FORMAT = "yyyy-MM-ddHH:mm:ss.SSS'Z'";
    public static final String CHAINKEY_SEPARATOR = "|";
    public static final String REAL_TIME = "real-time";
    public static final String DELAYED = "delayed";
    public static final String EQUITY_REQUEST_TYPE_CONTAINS = "01";
    public static final String REAL_TIME_QUOTE_INDICATOR = "N";
    public static final String DEALYED_QUOTE_INDICATOR = "Y";
    public static final String SUBSCRIBERTYPE_PROF = "PROF";
    public static final String REQUEST_VALIDATION_WARNING_CD = "1";
    public static final String AGREEMENT_EXPRY_DATE_FORMAT = "yyyyMMdd";

    public static final String SERVICE_SUCCESS_CD = "0";
    public static final String SERVICE_FAILURE_CD = "2";
    public static final String SERVICE_WARNING_CD = "1";
    public static final String SERVICE_SMARTTRADE_CONNECTIVITY_ISSUE = "3";
    public static final String SERVICE_SMARTTRADE_NODATA = "4";
    public static final String WPC_MQ_BUSINESS_PROCESS_CODE = "MDSBE";


    public static final String PRODUCT_KEY_SEPARATOR = ":";
    public static final String CRITERION_REQUEST_SWITCH_OUT_FUND = "switchOutFund";
    public static final String CRITERION_REQUEST_MONTHLY_INVESTMENT_PLAN = "allowSellMipProdInd";
    public static final String CRITERION_WPC_SWITCHABLE_FUND_GROUP = "switchFundGrpList";
    public static final String CRITERION_WPC_SUPPORT_MONTHLY_INVESTMENT_PLAN = "ALLOW_SELL_MIP_PROD_IND";
    public static final String CRITERION_REQUEST_hhhh_RISK_LEVEL_RATING = "HRR";
    public static final String CRITERION_KEYWORD = "keyword";
    public static final String hhhh_BEST_SELLER = "hhhh_BEST_SELLER";
    public static final String hhhh_NEW_FUND = "hhhh_NEW_FUND";
    public static final String hhhh_RETIREMENT_FUND = "hhhh_RETIREMENT_FUND";
    public static final String hhhh_TOP5_PERFORMERS = "hhhh_TOP5_PERFORMERS";
    public static final String NEXT_DEAL_DATE = "NDD";
    public static final String PRODUCT_SUB_TYPE = "PST";
    public static final String CATEGORY_CODE = "CAT";
    public static final String RESTR_ONL_SCRIB = "ROS";
    public static final String PROD_STAT_CDE = "PSC";

    public static final String BOND_CLOSING_PRICE_JOB_TIMEZONE_REGION_PARAM_KEY = "BOND_CLOSING_PRICE_JOB_TIMEZONE_REGION_PARAM_KEY";
    public static final String BOND_CLOSING_PRICE_JOB_AS_OF_TIME_KEY = "BOND_CLOSING_PRICE_JOB_AS_OF_TIME_KEY";
    public static final String BOND_CLOSING_PRICE_JOB_IS_DELTA_PARAM_KEY = "BOND_CLOSING_PRICE_JOB_IS_DELTA_PARAM_KEY";
    public static final String BOND_CLOSING_PRICE_JOB_IS_DELTA_PARAM_VALUE_TRUE = "Y";
    public static final String BOND_CLOSING_PRICE_JOB_IS_DELTA_PARAM_VALUE_FALSE = "N";
    public static final String WPC_API_CLIENT_FORMULA = "{0} AND {1} AND {2} AND {3}";


    // credit rating agencies
    public static final String CREDIT_RATING_AGENCY_STANDARD_AND_POORS = "SP";
    public static final String CREDIT_RATING_AGENCY_MOODYS = "MOODY";

    public static final String PRODUCT_TYPE_OPTION = "OPTS";
    public static final String PRODUCT_TYPE_EQUITY = "SEC";
    public static final String PRODUCT_TYPE_FUND = "UT";
    public static final String PRODUCT_TYPE_ETF = "ETF";

    public static final String ASSET_TYPE = "ASSET";
    public static final String MARKET = "MARKET";
    public static final String ASSET_TYPE_EQUITY = "EQUITY";
    public static final String ASSET_TYPE_INDEX = "INDEX";

    public static final String COUNTRY_ID = "COUNTRY_ID";
    public static final String GROUP_ID = "GROUP_ID";
}
