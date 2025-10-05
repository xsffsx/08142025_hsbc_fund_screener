/*
 * ***************************************************************
 * Copyright. dummy Holdings plc 2007 ALL RIGHTS RESERVED.
 * 
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system or translated in any human or computer language
 * in any way or for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 * 
 * Class Name ConfigConstants
 * 
 * Creation Date Jul 5, 2007
 * 
 * Abstract (The program's main functions)
 * 
 * Amendment History (In chronological sequence):
 * 
 * Amendment Date CMM/PPCR No. Programmer 35021438 Description Definition of
 * all configuration constants specific for the batch process.
 */

package com.dummy.wpb.product.constant;


/**
 * @author (Developer name in Notes format, e.g. Peter T M Chan)
 * @version 1.0 (ddMMMyyyy, e.g. 13Aug2006)
 * @since 1.0
 */
public class ConfigConstants {

    /* Application Constant */
    public static final String APP_CTRY_CDE = "app.country.code";
    public static final String APP_ORGN_CDE = "app.organization.code";

    /* Source ctry/orgn remap Constant */
    public static final String SOURCE_CTRY_CDE_REMAP = "source.ctry.code.remap";

    public static final String SOURCE_ORGN_CDE_REMAP = "source.orgn.code.remap";

    /* Country/Organization Specific Properties Path */
    public static final String PROPERTIES_PATH = "PATH.batch.properties";
    public static final String PROPERTIES_PATH_ONLINE = "PATH.online.properties";

    /* Logfile Path */
    public static final String LOG_PATH = "PATH.log";

    /* Custom Query Configuration */
    public static final String QUERY_CONFIG_PATH = "QUERY_CONFIG.path";
    public static final String QUERY_CONFIG_FILENAME_SUFFIX = "QUERY_CONFIG.suffix";

    /* global ctry/group member code */
    public static final String GLOBAL_CTRY_CDE = "global.ctry.cde";

    public static final String GLOBAL_ORGN_CDE = "global.orgn.cde";

    /* Extend Field Configuration */
    public static final String EXTEND_FIELD_CONFIG_PATH = "EXTEND_FIELD_CONFIG.path";

    public static final String EXTEND_FIELD_CONFIG_FILENAME_SUFFIX = "EXTEND_FIELD_CONFIG.suffix";
    public static final String IDS_CONFIG_PATH = "IDS_CONFIG.PATH";
    public static final String IDS_CONFIG_FILENAME_SUFFIX = "IDS_CONFIG.SUFFIX";

    public static final String IDW_CONFIG_PATH = "IDW_CONFIG.PATH";
    public static final String IDW_CONFIG_FILENAME_SUFFIX = "IDW_CONFIG.SUFFIX";

    public static final String DATASYNC_INTERFACE_PATH = "DATASYNC.INTERFACE.PATH";
    public static final String DATASYNC_FIELDLIST_CONFIG_PATH = "DATASYNC.FIELDLIST.CONFIG.PATH";
    public static final String DATASYNC_FIELDLIST_CONFIG_SUFFIX = "DATASYNC.FIELDLIST.CONFIG.SUFFIX";

    /* Tags Mapping - Query */
    public static final String CONFIG = "config";
    public static final String CONFIG_ATTRIB = "sysCode";
    public static final String QUERY = "query";
    public static final String VERSION = "version";
    public static final String SCHEMA = "schema";
    public static final String TABLE = "table";
    public static final String KEYWORD = "keyword";
    public static final String COLUMN = "column";
    public static final String PRIMARY_KEY = "primaryKey";
    public static final String CONDITION = "condition";
    public static final String COLUMN_TRUST_ATTR = "trust";

    /* Fields Mapping - Excel */
    public static final String STARTING_ROW = "STRT_ROW";
    public static final String ACTION_CODE = "ACTN_CDE";

    /* Fields Mapping - Header and Trailer */
    public static final String HEADER_TRAILER = "HEADER_TRAILER";
    public static final String FILE_NAME = "FILE_NAME";
    public static final String SEND_SYS_NAME = "SEND_SYS_NAME";
    public static final String TOTAL_REC_CNT = "TOTAL_REC_CNT";

    /* Fields Mapping - Product Master */
    public static final String COUNTRY_RECORD_CODE = "CTRY_REC_CDE";
    public static final String GROUP_MEMBER_RECORD_CODE = "GRP_MEMBR_REC_CDE";
    public static final String PRODUCT_TYPE_CODE = "PROD_TYPE_CDE";
    public static final String PRODUCT_ALTERNATIVE_PRIMARY_NUMBER = "PROD_ALT_PRIM_NUM";
    public static final String TARGET_PRODUCT_ALTERNATIVE_PRIMARY_NUMBER = "TRG_PROD_ALT_PRIM_NUM";
    public static final String PRODUCT_SUBTYPE_CODE = "PROD_SUBTP_CDE";
    public static final String PRODUCT_CODE = "PROD_CDE";
    public static final String PRODUCT_ID = "PROD_ID";
    public static final String PRODUCT_TYPE = "PROD_TYPE";
    public static final String PRODUCT_NAME = "PROD_NAME";
    public static final String PRODUCT_NAME_IN_PRIMARY_LANGUAGE = "PROD_PLL_NAME";
    public static final String PRODUCT_NAME_IN_SECONDARY_LANGUAGE = "PROD_SLL_NAME";
    public static final String PRODUCT_SHORT_NAME = "PROD_SHRT_NAME";
    public static final String PRODUCT_SHORT_NAME_IN_PRIMARY_LANGUAGE = "PROD_SHRT_PLL_NAME";
    public static final String PRODUCT_SHORT_NAME_IN_SECONDARY_LANGUAGE = "PROD_SHRT_SLL_NAME";
    public static final String PRODUCT_DESCRIPTION = "PROD_DESC";
    public static final String PRODUCT_DESCRIPTION_IN_PRIMARY_LANGUAGE = "PROD_PLL_DESC";
    public static final String PRODUCT_DESCRIPTION_IN_SECONDARY_LANGUAGE = "PROD_SLL_DESC";
    public static final String ASSET_CLASS_CODE = "ASET_CLASS_CDE";
    public static final String ASSET_UNDERLYING_CODE = "ASET_UNDL_CDE";
    public static final String PRODUCT_STATUS_CODE = "PROD_STAT_CDE";
    public static final String CURRENCY_PRODUCT_CODE = "CCY_PROD_CDE";
    public static final String CURRENCY_PRODUCT_TRADABLE_CODE = "CCY_PROD_TRADE_CDE";
    public static final String RISK_LEVEL_CODE = "RISK_LVL_CDE";
    public static final String PERIOD_PRODUCT_CODE = "PRD_PROD_CDE";
    public static final String PERIOD_PRODUCT_NUMBER = "PRD_PROD_NUM";
    public static final String TERM_REMAINING_DAY_COUNT = "TERM_REMAIN_DAY_CNT";
    public static final String PRODUCT_LAUNCH_DATE = "PROD_LNCH_DT";
    public static final String PRODUCT_MATURITY_DATE = "PROD_MTUR_DT";
    public static final String MARKET_INVESTMENT_CODE = "MKT_INVST_CDE";
    public static final String SECTOR_INVESTMENT_CODE = "SECT_INVST_CDE";
    public static final String ALLOW_BUY_PRODUCT_INDICATOR = "ALLOW_BUY_PROD_IND";
    public static final String ALLOW_SELL_PRODUCT_INDICATOR = "ALLOW_SELL_PROD_IND";
    public static final String ALLOW_BUY_UNIT_PRODUCT_INDICATOR = "ALLOW_BUY_UT_PROD_IND";
    public static final String ALLOW_BUY_AMOUNT_PRODUCT_INDICATOR = "ALLOW_BUY_AMT_PROD_IND";
    public static final String ALLOW_SELL_UNIT_PRODUCT_INDICATOR = "ALLOW_SELL_UT_PROD_IND";
    public static final String ALLOW_SELL_AMOUNT_PRODUCT_INDICATOR = "ALLOW_SELL_AMT_PROD_IND";
    public static final String ALLOW_SELL_MONTHLY_INVESTMENT_PROGRAM_PRODUCT_INDICATOR = "ALLOW_SELL_MIP_PROD_IND";
    public static final String ALLOW_SELL_MONTHLY_INVESTMENT_PROGRAM_UNIT_PRODUCT_INDICATOR = "ALLOW_SELL_MIP_UT_PROD_IND";
    public static final String ALLOW_SELL_MONTHLY_INVESTMENT_PROGRAM_AMOUNT_PRODUCT_INDICATOR = "ALLOW_SELL_MIP_AMT_PROD_IND";
    public static final String ALLOW_SWITCH_IN_PRODUCT_INDICATOR = "ALLOW_SW_IN_PROD_IND";
    public static final String ALLOW_SWITCH_IN_UNIT_PRODUCT_INDICATOR = "ALLOW_SW_IN_UT_PROD_IND";
    public static final String ALLOW_SWITCH_IN_AMOUNT_PRODUCT_INDICATOR = "ALLOW_SW_IN_AMT_PROD_IND";
    public static final String ALLOW_SWITCH_OUT_PRODUCT_INDICATOR = "ALLOW_SW_OUT_PROD_IND";
    public static final String ALLOW_SWITCH_OUT_UNIT_PRODUCT_INDICATOR = "ALLOW_SW_OUT_UT_PROD_IND";
    public static final String ALLOW_SWITCH_OUT_AMOUNT_PRODUCT_INDICATOR = "ALLOW_SW_OUT_AMT_PROD_IND";
    public static final String INCOME_CHARACTERISTIC_PRODUCT_INDICATOR = "INCM_CHAR_PROD_IND";
    public static final String CAPITAL_GUARANTEED_PRODUCT_INDICATOR = "CPTL_GURNT_PROD_IND";
    public static final String YIELD_ENHANCEMENT_PRODUCT_INDICATOR = "YIELD_ENHN_PROD_IND";
    public static final String GROWTH_CHARACTERISTIC_PRODUCT_INDICATOR = "GRWTH_CHAR_PROD_IND";
    public static final String PRIORITY_PRODUCT_SEARCH_RESULT_NUMBER = "PRTY_PROD_SRCH_RSULT_NUM";
    public static final String SHORTLIST_PRODUCT_LIST_INDICATOR = "PROD_SLST_IND";
    public static final String PRODUCT_EFFECTIVE_SHORTLIST_DATE = "PROD_EFF_SLST_DT";
    public static final String LIST_PRODUCT_TYPE = "SLT";
    public static final String PRODUCT_ALTERNATED_NUMBER = "PROD_ALT_NUM";
    public static final String PRODUCT_CODE_ALTERNATED_CLASS_CODE = "PROD_CDE_ALT_CLASS_CDE";
    public static final String PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY = "P";
    public static final String PRODUCT_CODE_ALTERNATED_CLASS_CODE_MARKET = "M";

    public static final String PRODUCT_CODE_ALTERNATED_CLASS_CODE_ISIN = "I";

    public static final String PRODUCT_CODE_ALTERNATED_CLASS_CODE_RIC = "R";
    public static final String PRODUCT_CODE_ALTERNATED_CLASS_CODE_TR = "T";

    public static final String PRODUCT_CODE_ALTERNATED_CLASS_CODE_SEDOL = "S";
    public static final String AVAILABLE_MARKET_INFORMATION_INDICATOR = "AVAIL_MKT_INFO_IND";
    public static final String PERIOD_RETURN_AVERAGE_NUMBER = "PRD_RTRN_AVG_NUM";
    public static final String RETURN_VOLATILITY_AVERAGE_PERCENT = "RTRN_VOLTL_AVG_PCT";
    public static final String DUMMY_PRODUCT_SUBTYPE_RECORD_INDICATOR = "DMY_PROD_SUBTP_REC_IND";
    public static final String DISPLAY_COMMON_PRODUCT_SEARCH_INDICATOR = "DISP_COM_PROD_SRCH_IND";
    public static final String DIVISOR_NUMBER = "DIVR_NUM";
    public static final String MARK_TO_MARKET_INDICATOR = "MRK_TO_MKT_IND";
    public static final String PRODUCT_NARRATIVE_TEXT = "PROD_NARR_TEXT";
    public static final String PRODUCT_NARRATIVE_TEXT_IN_PRIMARY_LANGUAGE = "PROD_NARR_PLL_TEXT";
    public static final String PRODUCT_NARRATIVE_TEXT_IN_SECONDARY_LANGUAGE = "PROD_NARR_SLL_TEXT";
    public static final String COUNTRY_PRODUCT_TRADABLE_CODE_1 = "CTRY_PROD_TRADE_CDE_1";
    public static final String COUNTRY_PRODUCT_TRADABLE_CODE_2 = "CTRY_PROD_TRADE_CDE_2";
    public static final String COUNTRY_PRODUCT_TRADABLE_CODE_3 = "CTRY_PROD_TRADE_CDE_3";
    public static final String COUNTRY_PRODUCT_TRADABLE_CODE_4 = "CTRY_PROD_TRADE_CDE_4";
    public static final String COUNTRY_PRODUCT_TRADABLE_CODE_5 = "CTRY_PROD_TRADE_CDE_5";
    public static final String COUNTRY_PRODUCT_TRADABLE_1_CODE = "CTRY_PROD_TRADE_1_CDE";
    public static final String BUSINESS_START_TIME = "BUS_START_TM";
    public static final String BUSINESS_END_TIME = "BUS_END_TM";
    public static final String INTRODUCE_PRODUCT_CURRENT_PERIOD_INDICATOR = "INTRO_PROD_CURR_PRD_IND";
    public static final String PRODUCT_TOP_SELL_RANK_NUMBER = "PROD_TOP_SELL_RANK_NUM";
    public static final String PRODUCT_TOP_PERFORMANCE_RANK_NUMBER = "PROD_TOP_PERFM_RANK_NUM";
    public static final String PRODUCT_TOP_YIELD_RANK_NUMBER = "PROD_TOP_YIELD_RANK_NUM";
    public static final String RECORD_ONLINE_UPDATE_DATE = "REC_ONLN_UPDT_DT";
    public static final String RECORD_ONLINE_UPDATE_TIME = "REC_ONLN_UPDT_TM";
    public static final String ALLOWED_CHANNEL_RELATIONSHIP_LAST_UPDATE_DATE = "REL_UPDT_LAST_DT";
    public static final String ALLOWED_CHANNEL_RELATIONSHIP_LAST_UPDATE_TIME = "REL_UPDT_LAST_TM";
    public static final String PRODUCT_ISSUE_CROSS_REFERENCE_DATE = "PROD_ISSUE_CROS_REFER_DT";
    public static final String BENCHMARK_NAME = "BCHMK_NAME";
    public static final String BENCHMARK_NAME_IN_PRIMARY_LANGUAGE = "BCHMK_PLL_NAME";
    public static final String BENCHMARK_NAME_IN_SECONDARY_LANGUAGE = "BCHMK_SLL_NAME";
    public static final String TRADE_FIRST_DATE = "TRD_FIRST_DT";
    public static final String LOAN_PRODUCT_OVERDRAFT_MARGIN_PERCENT = "LOAN_PROD_OD_MRGN_PCT";
    public static final String QUANTITY_UNIT_PRODUCT_CODE = "QTY_UNIT_PROD_CDE";
    public static final String PRODUCT_LOCATION_CODE = "PROD_LOC_CDE";
    public static final String PRODUCT_TAX_FREE_WRAPPER_ACCOUNT_STATUS_CODE = "PROD_TAX_FREE_WRAP_ACT_STA_CDE";
    public static final String PRODUCT_COMMENT_TEXT = "PROD_CMNT_TEXT";
    public static final String PLEDGE_LIMIT_ASSOCIATE_ACCOUNT_INDICATOR = "PLDG_LIMIT_ASSOC_ACCT_IND";
    public static final String PRODUCT_OWNERSHIP_CODE = "PROD_OWN_CDE";
    public static final String FOREIGN_PRODUCT_INDICATOR = "FORGN_PROD_IND";
    public static final String ASSET_CATEGORY_EXTERNAL_CODE = "ASET_CAT_EXTNL_CDE";
    public static final String PRODUCT_MARKET_PRICE_PREVIOUS_AMOUNT = "PROD_MKT_PRICE_PREV_AMT";
    public static final String CHARGE_CATEGORY_CODE = "CHRG_CAT_CDE";
    public static final String SUPPORT_RECEIVABLE_CASH_PRODUCT_INDICATOR = "SUPT_RCBL_CASH_PROD_IND";
    public static final String SUPPORT_RECEIVABLE_SCRIP_PRODUCT_INDICATOR = "SUPT_RCBL_SCRIP_PROD_IND";
    public static final String ASSET_UNDER_MANAGEMENT_CHARGEABLE_PRODUCT_INDICATOR = "ASET_UNDER_MGMT_CHRG_PROD_IND";
    // public static final String ISLAMIC_PRODUCT_INDICATOR = "ISLM_PROD_IND";
    public static final String INVESTMENT_IMMIGRATION_PRODUCT_INDICATOR = "INVST_IMIG_PROD_IND";
    public static final String RESTRICTED_INVESTOR_PRODUCT_INDICATOR = "RESTR_INVSTR_PROD_IND";
    public static final String PRODUCT_INVESTMENT_OBJECTIVE_TEXT = "PROD_INVST_OBJ_TEXT";
    public static final String PRODUCT_INVESTMENT_OBJECTIVE_PRIMARY_LOCAL_LANGUAGE_TEXT = "PROD_INVST_OBJ_PLL_TEXT";
    public static final String PRODUCT_INVESTMENT_OBJECTIVE_SECONDARY_LOCAL_LANGUAGE_TEXT = "PROD_INVST_OBJ_SLL_TEXT";
    public static final String INVESTMENT_INITIAL_MINIMUM_AMOUNT = "INVST_INIT_MIN_AMT";
    public static final String NO_SUBSCRIPTION_FEE_PRODUCT_INDICATOR = "NO_SCRIB_FEE_PROD_IND";
    public static final String TOP_SELL_PRODUCT_INDICATOR = "TOP_SELL_PROD_IND";
    public static final String TOP_PERFORMANCE_PRODUCT_INDICATOR = "TOP_PERFM_PROD_IND";
    public static final String INVESTMENT_INITIAL_MAXIMUM_AMOUNT = "INVST_INIT_MAX_AMT";
    // Data modle v3.21 begin
    public static final String RECOMMENDED_PRODUCT_DECISION_CODE = "RCMND_PROD_DECSN_CDE";
    public static final String ASSET_TEXT = "ASET_TEXT";
    public static final String PRODUCT_DERIVATIVE_CODE = "PROD_DERVT_CDE";
    public static final String PRODUCT_DERIVATIVE_REVISED_CODE = "PROD_DERVT_RVS_CDE";
    public static final String PRODUCT_DERIVATIVE_REVISED_EFFECTIVE_DATE = "PRD_DERV_RVS_EFF_DT";
    public static final String EQUITY_UNDERLYING_INDICATOR = "EQTY_UNDL_IND";
    public static final String WEALTH_ACCUMULATION_GOAL_INDICATOR = "WLTH_ACCUM_GOAL_IND";
    public static final String PROD_INVST_TYPE_CDE = "PROD_INVST_TYPE_CDE";
    public static final String PRIORITY_WEALTH_ACCUMULATION_GOAL_CODE = "PRTY_WLTH_ACCUM_GOAL_CDE";
    public static final String PLAN_FOR_RETIREMENT_GOAL_INDICATOR = "PLAN_FOR_RTIRE_GOAL_IND";
    public static final String PRIORITY_PLAN_FOR_RETIREMENT_CODE = "PRTY_PLN_FOR_RTIRE_CDE";
    public static final String EDUCATION_GOAL_INDICATOR = "EDUC_GOAL_IND";
    public static final String PRIORITY_EDUCATION_CODE = "PRTY_EDUC_CDE";
    public static final String LIVE_IN_RETIREMENT_GOAL_INDICATOR = "LIVE_IN_RTIRE_GOAL_IND";
    public static final String PRIORITY_LIVE_IN_RETIREMENT_CODE = "PRTY_LIVE_IN_RTIRE_CDE";
    public static final String PROTECTION_GOAL_INDICATOR = "PROTC_GOAL_IND";
    public static final String PRIORITY_PROTECTION_CODE = "PRTY_PROTC_CDE";
    public static final String MANAGE_SOLUTION_INDICATOR = "MNG_SOLN_IND";
    public static final String PERIOD_INVESTMENT_TENOR_NUMBER = "PRD_INVST_TNOR_NUM";
    public static final String GEOGRAPHICAL_RISK_INDICATOR = "GEO_RISK_IND";
    public static final String PRODUCT_LIQUIDITY_INDICATOR = "PROD_LQDY_IND";
    public static final String REVERSE_ENQUIRY_PRODUCT_INDICATOR = "RVRSE_ENQ_PROD_IND";
    public static final String PRODUCT_INVESTMENT_TYPE_CODE = "PROD_INVST_TYPE_CDE";
    public static final String ISLAMIC_PRODUCT_INDICATOR = "ISLM_PROD_IND";
    public static final String CONTROL_ADVICE_INDICATOR = "CNTL_ADVC_IND";
    // Data modle v3.21 end

    /* Fields Mapping - Security */
    public static final String IS_SEC_DATA_DICT_EXIST = "IS_SEC_DATA_DICT_EXIST";
    public static final String PRODUCT_PRICE_CHANGE_PERCENT = "PROD_PRC_CHNG_PCT";
    public static final String PRODUCT_PRICE_CHANGE_AMOUNT = "PROD_PRC_CHNG_AMT";
    public static final String PRODUCT_OPENING_PRICE_AMOUNT = "PROD_OPEN_PRC_AMT";
    public static final String PRODUCT_TODAY_HIGHEST_PRICE_AMOUNT = "PROD_TDY_HIGH_PRC_AMT";
    public static final String PRODUCT_TODAY_LOWEST_PRICE_AMOUNT = "PROD_TDY_LOW_PRC_AMT";
    public static final String SHARE_TRADE_COUNT = "SHARE_TRD_CNT";
    public static final String PRODUCT_TURNOVER_AMOUNT = "PROD_TRNVR_AMT";
    public static final String PRODUCT_CLOSING_PRICE_AMOUNT = "PROD_CLOSE_PRC_AMT";
    public static final String PRODUCT_PRICE_52_WEEK_MAXIMUM_AMOUNT = "PROD_PRC_52_WEEK_MAX_AMT";
    public static final String PRODUCT_PRICE_52_WEEK_MINIMUM_AMOUNT = "PROD_PRC_52_WEEK_MIN_AMT";
    public static final String PRICE_EARNINGS_RATE = "PRC_EARN_RATE";
    public static final String EARNINGS_PER_SHARE_ANNUAL_AMOUNT = "EARN_PER_SHARE_ANNL_AMT";
    public static final String DIVIDEND_PERCENT = "DIV_PCT";
    public static final String MARGIN_TRADE_INDICATOR = "MRGN_TRD_IND";
    public static final String MARGIN_SECURED_OVERDRAFT_PERCENT = "MRGN_SEC_OD_PCT";
    public static final String SUPPORT_AUCTION_TRADING_INDICATOR = "SUPT_AUCTN_TRD_IND";
    public static final String STOP_LOSS_MINIMUM_PERCENT = "STOP_LOSS_MIN_PCT";
    public static final String STOP_LOSS_MAXIMUM_PERCENT = "STOP_LOSS_MAX_PCT";
    public static final String SPREAD_STOP_LOSS_MINIMUM_COUNT = "SPRD_STOP_LOSS_MIN_CNT";
    public static final String SPREAD_STOP_LOSS_MAXIMUM_COUNT = "SPRD_STOP_LOSS_MAX_CNT";
    public static final String PRODUCT_BOARD_LOT_QUANTITY_COUNT = "PROD_BOD_LOT_QTY_CNT";
    // public static final String PRODUCT_PRICE_SPREAD_AMOUNT =
    // "PROD_PRC_SPRD_AMT";
    public static final String MARKET_PRODUCT_TRADING_CODE = "MKT_PROD_TRD_CDE";
    public static final String SUPPORT_MONTHLY_INVESTMENT_PROGRAM_INDICATOR = "SUPT_MIP_IND";
    public static final String INVESTMENT_MONTHLY_INVESTMENT_PROGRAM_MINIMUM_AMOUNT = "INVST_MIP_MIN_AMT";
    public static final String INVESTMENT_MONTHLY_INVESTMENT_PROGRAM_INCREMENT_MINIMUM_AMOUNT = "INVST_MIP_INCRM_MIN_AMT";
    public static final String PRODUCT_ISSUE_QUANTITY_TOTAL_COUNT = "PROD_ISS_QTY_TTL_CNT";
    public static final String BOARD_LOT_PRODUCT_INDICATOR = "BOD_LOT_PROD_IND";
    public static final String TRADING_LIMIT_INDICATOR = "TRD_LIMIT_IND";
    public static final String PRODUCT_MAXIMUM_INDIVIDUAL_OWNER_PERCENT = "PROD_MAX_INDV_OWNR_PCT";
    public static final String PRODUCT_MAXIMUM_INDIVIDUAL_FOREIGN_INVESTOR_PERCENT = "PROD_MAX_INDV_FORGN_INVSTR_PCT";
    public static final String PRODUCT_MAXIMUM_FOREIGN_INVESTOR_TOTAL_PERCENT = "PROD_MAX_FORGN_INVSTR_TTL_PCT";
    public static final String PRODUCT_EXERCISE_PRICE_AMOUNT = "PROD_EXER_PRC_AMT";
    public static final String POSSIBLE_PRODUCT_BORROW_INDICATOR = "PSBL_PROD_BORW_IND";
    public static final String ALLOW_PRODUCT_LENDING_INDICATOR = "ALLW_PROD_LEND_IND";
    public static final String POOL_PRODUCT_INDICATOR = "POOL_PROD_IND";
    public static final String SCRIP_ONLY_PRODUCT_INDICATOR = "SCRIP_ONLY_PROD_IND";
    public static final String SUPPORT_AUTOMATIC_TRADE_INDICATOR = "SUPT_ATMC_TRD_IND";
    public static final String SUPPORT_NET_SETTLEMENT_INDICATOR = "SUPT_NET_SETL_IND";
    public static final String SUPPORT_STOP_LOSS_ORDER_INDICATOR = "SUPT_STOP_LOSS_ORD_IND";
    public static final String SUPPORT_WIN_WIN_ORDER_INDICATOR = "SUPT_WIN_WIN_ORD_IND";
    public static final String SUPPORT_ALL_IN_ONE_ORDER_INDICATOR = "SUPT_ALL_IN_1_ORD_IND";
    public static final String SUPPORT_PRODUCT_SPLIT_INDICATOR = "SUPT_PROD_SPLT_IND";
    public static final String MARGIN_PRICE_AUCTION_TRADING_PERCENT = "MRGN_PRC_AUCTN_TRD_PCT";
    public static final String PRODUCT_AUCTION_TRADING_EXPIRY_DATE = "PROD_AUCTN_TRD_EXPIR_DT";
    public static final String LOAN_PRODUCT_MARGIN_TRADING_PERCENT = "LOAN_PROD_MRGN_TRD_PCT";
    public static final String LOAN_BUDGET_PRODUCT_INITIAL_PUBLIC_OFFER_AMOUNT = "LOAN_BDGT_PROD_IPO_AMT";
    public static final String LOAN_PRODUCT_INITIAL_PUBLIC_OFFER_TOTAL_AMOUNT = "LOAN_PROD_IPO_TTL_AMT";
    public static final String EXCLUDE_LIMIT_CALCULATION_INDICATOR = "EXCL_LIMIT_CALC_IND";
    public static final String PRODUCT_MARKET_STATUS_CHANGE_LAST_DATE = "PROD_MKT_STAT_CHNG_LA_DT";
    public static final String PRICE_VARIANCE_CODE = "PRC_VAR_CDE";
    public static final String METHOD_CALCULATION_SCRIP_CHARGE_CODE = "METH_CALC_SCRIP_CHRG_CDE";
    public static final String PRODUCT_SELL_MAXIMUM_QUANTITY_COUNT = "PROD_SELL_MAX_QTY_CNT";
    public static final String PRODUCT_BUY_MAXIMUM_QUANTITY_COUNT = "PROD_BUY_MAX_QTY_CNT";
    public static final String PRICE_VARIANCE_MINIMUM_AMOUNT = "PRC_VAR_MIN_AMT";
    public static final String PRICE_VARIANCE_MINIMUM_EFFECTIVE_DATE = "PRC_VAR_MIN_EFF_DT";
    public static final String MARKET_SEGMENT_EXCHANGE_CODE = "MKT_SEG_EXCHG_CDE";
    public static final String COUNTRY_PRODUCT_REGISTERED_CODE = "CTRY_PROD_REGIS_CDE";
    public static final String PROD_STAT_CDE = "PROD_STAT_CDE";

    public static final String MRK_TO_MKT_IND = "MRK_TO_MKT_IND";
    public static final String INCM_CHAR_PROD_IND = "INCM_CHAR_PROD_IND";
    public static final String CPTL_GURNT_PROD_IND = "CPTL_GURNT_PROD_IND";

    /* Fields Mapping - Warrant */
    public static final String PRODUCT_INTERNATIONAL_SECURITIES_IDENTIFICATION_NUMBER = "PROD_INTL_SEC_ID_NUM";
    public static final String ISSUER_BOND_NAME = "ISR_BOND_NAME";
    public static final String WARRANT_TYPE_CODE = "WARR_TYPE_CDE";
    public static final String PRODUCT_STRIKE_PRICE_AMOUNT = "PROD_STRK_PRC_AMT";
    public static final String ENTITLEMENT_RATE = "ENTL_RATE";
    public static final String INSTRUMENT_UNDERLYING_CODE = "INSTM_UNDL_CDE";
    public static final String CURRENCY_UNDERLYING_INSTRUMENT_CODE = "CCY_UNDL_INSTM_CDE";
    public static final String INSTRUMENT_UNDERLYING_MARKET_PRICE_AMOUNT = "INSTM_UNDL_MKT_PRC_AMT";
    public static final String VOLATILITY_IMPLIED_PERCENT = "VOLTL_IMPLY_PCT";
    public static final String MONEYNESS_PERCENT = "MONEY_PCT";
    public static final String MONEYNESS_TYPE_CODE = "MONEY_TYPE_CDE";
    public static final String PREMIUM_PERCENT = "PREM_PCT";
    public static final String GEARING_EFFECTIVE_RATE = "GEAR_EFF_RATE";
    public static final String DELTA_PERCENT = "DELTA_PCT";
    public static final String IS_WRTS_DATA_DICT_EXIST = "IS_WRTS_DATA_DICT_EXIST";
    public static final String ISSUER_CODE = "ISR_CDE";

    /* Fields Mapping - Unit Trust */
    public static final String FUND_HOUSE_CODE = "FUND_HOUSE_CDE";
    public static final String CLOSE_END_FLAG = "CLOSE_END_FUND_IND";
    public static final String UNIT_PRICE_PRODUCT_LAUNCH_AMOUNT = "UT_PRC_PROD_LNCH_AMT";
    public static final String FUND_AMOUNT = "FUND_AMT";
    public static final String INVESTMENT_INCREMENT_MINIMUM_AMOUNT = "INVST_INCRM_MIN_AMT";
    public static final String UNIT_REDEMPTION_MINIMUM_NUMBER = "UT_RDM_MIN_NUM";
    public static final String REDEMPTION_MINIMUM_AMOUNT = "RDM_MIN_AMT";
    public static final String UNIT_RETAIN_MINIMUM_NUMBER = "UT_RTAIN_MIN_NUM";
    public static final String FUND_RETAIN_MINIMUM_AMOUNT = "FUND_RTAIN_MIN_AMT";
    public static final String UNIT_MONTHLY_INVESTMENT_PROGRAM_REDEMPTION_MINIMUM_NUMBER = "UT_MIP_RDM_MIN_NUM";
    public static final String REDEMPTION_MONTHLY_INVESTMENT_PROGRAM_MINIMUM_AMOUNT = "RDM_MIP_MIN_AMT";
    public static final String UNIT_MONTHLY_INVESTMENT_PROGRAM_RETAIN_MINIMUM_NUMBER = "UT_MIP_RTAIN_MIN_NUM";
    public static final String FUND_MONTHLY_INVESTMENT_PROGRAM_RETAIN_MINIMUM_AMOUNT = "FUND_MIP_RTAIN_MIN_AMT";
    public static final String RETURN_GUARANTEE_PERCENT = "RTRN_GURNT_PCT";
    public static final String SCHEME_CHARGE_CODE = "SCHEM_CHRG_CDE";
    public static final String CHARGE_INITIAL_SALES_PERCENT = "CHRG_INIT_SALES_PCT";
    public static final String CHARGE_FUND_SWITCH_PERCENT = "CHRG_FUND_SW_PCT";
    public static final String DISCOUNT_MAXIMUM_PERCENT = "DSCNT_MAX_PCT";
    public static final String OFFER_START_DATE_TIME = "OFFER_START_DT_TM";
    public static final String OFFER_END_DATE_TIME = "OFFER_END_DT_TM";
    public static final String PAY_CASH_DIVIDEND_INDICATOR = "PAY_CASH_DIV_IND";
    public static final String HOLIDAY_FUND_NEXT_DATE = "HLDAY_FUND_NEXT_DT";
    public static final String SUBSCRIPTION_CUTOFF_NEXT_DATE_TIME = "SCRIB_CTOFF_NEXT_DT_TM";
    public static final String REDEMPTION_CUTOFF_NEXT_DATE_TIME = "RDM_CTOFF_NEXT_DT_TM";

    // public static final String FUND_EXTERNAL_NUMBER = "FUND_EXTNL_NUM";
    public static final String DEAL_NEXT_DATE = "DEAL_NEXT_DT";
    public static final String FUND_CLASS_CODE = "FUND_CLASS_CDE";
    public static final String AMCM_INDICATOR = "AMCM_IND";
    public static final String DIVIDEND_DECLARATION_DATE = "DIV_DCLR_DT";
    public static final String DIVIDEND_PAYMENT_DATE = "DIV_PYMT_DT";
    public static final String AUTO_SWEEP_FUND_INDICATOR = "AUTO_SWEEP_FUND_IND";
    public static final String SPECIAL_FUND_INDICATOR = "SPCL_FUND_IND";
    public static final String FUND_UNSWITCHABLE_CODE = "FUND_UNSW_CDE";
    public static final String INSURANCE_LINKED_UNIT_TRUST_INDICATOR = "INSU_LINK_UT_TRST_IND";
    public static final String PRODUCT_TERMINATE_DATE = "PROD_TRMT_DT";
    public static final String FUND_SWITCH_IN_MINIMUM_AMOUNT = "FUND_SW_IN_MIN_AMT";
    public static final String FUND_SWITCH_OUT_MINIMUM_AMOUNT = "FUND_SW_OUT_MIN_AMT";
    public static final String FUND_SWITCH_OUT_RETAIN_MINIMUM_AMOUNT = "FUND_SW_OUT_RTAIN_MIN_AMT";
    public static final String UNIT_SWITCH_OUT_RETAIN_MINIMUM_NUMBER = "UT_SW_OUT_RTAIN_MIN_NUM";
    public static final String FUND_SWITCH_OUT_MAXIMUM_AMOUNT = "FUND_SW_OUT_MAX_AMT";
    public static final String TRANSACTION_MAXIMUM_AMOUNT = "TRAN_MAX_AMT";
    public static final String INCOME_HANDLE_OPTION_CODE = "INCM_HANDL_OPT_CDE";
    public static final String DECIMAL_PLACE_TRADE_UNIT_NUMBER = "DCML_PLACE_TRADE_UNIT_NUM";
    public static final String AUTO_RENEWABLE_FUND_INDICATOR = "AUTO_RENEW_FUND_IND";
    public static final String SWITCH_GROUP = "SWTCH _GP";
    public static final String DIVIDEND_FLAG = "DVD_FLG";
    public static final String DIVIDEND_METHOD = "DVD_MTHD";
    public static final String ALLOWED_CUSTOMER_APPLICATION_SYSTEM_INDICATOR = "ALOW_CAS_CHNL_IND";
    public static final String ALLOWED_PFSNET_DAILY_FUND_PRICE_INDICATOR = "ALOW_PFSFP_CHNL_IND";
    public static final String ALLOWED_PFSNET_PRODUCT_DATABASE_INDICATOR = "ALOW_PFSPD_CHNL_IND";
    public static final String ALLOWED_PUBLIC_WEBSITE_PERSONAL_BANKING_INDICATOR = "ALOW_PWSPB_CHNL_IND";
    public static final String ALLOWED_PUBLIC_WEBSITE_COMMERCIAL_BANKING_INDICATOR = "ALOW_PWSCB_CHNL_IND";
    public static final String ALLOWED_PERSONAL_INTERNET_BANKING_INDICATOR = "ALOW_PIB_CHNL_IND";
    public static final String ALLOWED_WEALTH_EXPLORER_INDICATOR = "ALOW_WEX_CHNL_IND";
    public static final String ALLOWED_INTERACTIVE_VOICE_RESPONSE_SYSTEM_INDICATOR = "ALOW_IVRS_CHNL_IND";
    public static final String PREVIOUS_BUSINESS_DAY_NET_ASSET_VALUE_PRICE = "PREV_BUS_DAY_NAV_PRC_AMT";
    public static final String FUND_VALUATION_TIME = "FUND_VALN_TM";
    public static final String PRODUCT_SHORE_LOCATION_CODE = "PROD_SHORE_LOC_CDE";
    public static final String DIVIDEND_YIELD_PERCENT = "DIV_YIELD_PCT";

    /* Fields Mapping - Deposit Plus */
    public static final String DPS_TYPE_CODE = "DEPST_PLUS_TYPE_CDE";
    public static final String TMD_INTERNAL_PRODUCT_CODE = "PROD_EXTNL_CDE";
    public static final String TMD_PRODUCT_SUFFIX = "PROD_EXTNL_SUFF_NUM";
    public static final String LINKED_CURRENCY_CODE = "CCY_LINK_DEPST_CDE";
    public static final String CURRENCY_INVESTMENT_CODE = "CCY_INVST_CDE";
    public static final String TAILORED_MADE_INDICATOR = "TAILR_MADE_DEPST_IND";
    public static final String KNOCK_TYPE_CODE = "KNOCK_TYPE_CDE";
    public static final String FIXING_DATE = "EXCHG_RATE_FIX_DT";
    public static final String CONVERSION_FACTOR = "FACTR_CONV_RATE";
    public static final String ALL_IN_RATE = "INT_RATE";
    public static final String SPOT_RATE = "EXCHG_SPOT_RATE";
    public static final String INITIAL_EXCHANGE_RATE = "EXCHG_INIT_RATE";
    public static final String BREAKEVEN_RATE = "EXCHG_BREAK_EVEN_RATE";
    public static final String OBSERVATION_START_DATE = "OBSRV_START_DT";
    public static final String OBSERVATION_END_DATE = "OBSRV_END_DT";
    public static final String TRIGGER_RATE = "EXCHG_TRIG_RATE";
    public static final String RETURN_MECHANISM_CODE = "RTN_OPT_CDE";
    public static final String SOURCE_CURRENCY_CODE = "CCY_FROM_CDE";
    public static final String TARGET_CURRENCY_CODE = "CCY_TO_CDE";

    /* Fields Mapping - Structure Investment Deposit */
    // public static final String PROD_EXTNL_CDE = "PROD_EXTNL_CDE";
    // public static final String PROD_EXTNL_TYPE_CDE = "PROD_EXTNL_TYPE_CDE";
    public static final String PROD_CONV_CDE = "PROD_CONV_CDE";
    public static final String RTRN_INTRM_PREV_PCT = "RTRN_INTRM_PREV_PCT";
    public static final String RTRN_INTRM_PAID_PREV_DT = "RTRN_INTRM_PAID_PREV_DT";
    public static final String RTRN_INTRM_PAID_NEXT_DT = "RTRN_INTRM_PAID_NEXT_DT";
    public static final String CCY_LINK_DEPST_CDE = "CCY_LINK_DEPST_CDE";
    public static final String MKT_START_DT = "MKT_START_DT";
    public static final String MKT_END_DT = "MKT_END_DT";
    // public static final String INVST_INIT_MIN_AMT = "INVST_INIT_MIN_AMT";
    public static final String YIELD_ANNL_MIN_PCT = "YIELD_ANNL_MIN_PCT";
    public static final String YIELD_ANNL_POTEN_PCT = "YIELD_ANNL_POTEN_PCT";
    public static final String ALLOW_EARLY_RDM_IND = "ALLOW_EARLY_RDM_IND";
    public static final String RDM_EARLY_DALW_TEXT = "RDM_EARLY_DALW_TEXT";
    public static final String PRC_EARLY_RDM_PCT = "PRC_EARLY_RDM_PCT";
    public static final String RDM_EARLY_IND_AMT = "RDM_EARLY_IND_AMT";
    // public static final String OFFER_TYPE_CDE = "OFFER_TYPE_CDE";
    public static final String CUST_SELL_QTA_NUM = "CUST_SELL_QTA_NUM";
    // public static final String RULE_QTA_ALTMT_CDE = "RULE_QTA_ALTMT_CDE";
    public static final String BONUS_INT_CALC_TYPE_CDE = "BONUS_INT_CALC_TYPE_CDE";
    public static final String BONUS_INT_DT_TYPE_CDE = "BONUS_INT_DT_TYPE_CDE";
    public static final String CPTL_PROTC_PCT = "CPTL_PROTC_PCT";
    // public static final String LNCH_PROD_IND = "LNCH_PROD_IND";
    // public static final String RTRV_PROD_EXTNL_IND = "RTRV_PROD_EXTNL_IND";
    public static final String RTRN_INTRM_PAID_DT = "RTRN_INTRM_PAID_DT";
    public static final String RTRN_INTRM_PCT = "RTRN_INTRM_PCT";

    /* Fields Mapping - Equity Linked Investment */
    public static final String PROD_EXTNL_CDE = "PROD_EXTNL_CDE";
    public static final String PROD_EXTNL_TYPE_CDE = "PROD_EXTNL_TYPE_CDE";
    public static final String INSTM_UNDL_CDE = "INSTM_UNDL_CDE";
    public static final String INSTM_UNDL_TEXT = "INSTM_UNDL_TEXT";
    public static final String PROD_ID_UNDL_INSTM = "PROD_ID_UNDL_INSTM";
    public static final String EQTY_LINK_INVST_TYPE_CDE = "EQTY_LINK_INVST_TYPE_CDE";
    public static final String TRD_DT = "TRD_DT";
    public static final String PROD_STRK_PRC_AMT = "PROD_STRK_PRC_AMT";
    public static final String PROD_STRK_CALL_PRC_AMT = "PROD_STRK_CALL_PRC_AMT";
    public static final String PROD_STRK_PUT_PRC_AMT = "PROD_STRK_PUT_PRC_AMT";
    public static final String PROD_CLOSE_PRC_AMT = "PROD_CLOSE_PRC_AMT";
    public static final String PROD_CLOSE_LOW_PRC_AMT = "PROD_CLOSE_LOW_PRC_AMT";
    public static final String PROD_CLOSE_UPPR_PRC_AMT = "PROD_CLOSE_UPPR_PRC_AMT";
    public static final String PROD_CLOSE_PUT_PRC_AMT = "PROD_CLOSE_PUT_PRC_AMT";
    public static final String PROD_CLOSE_CALL_PRC_AMT = "PROD_CLOSE_CALL_PRC_AMT";
    public static final String PROD_EXER_PRC_AMT = "PROD_EXER_PRC_AMT";
    public static final String DSCNT_BUY_PCT = "DSCNT_BUY_PCT";
    public static final String DSCNT_SELL_PCT = "DSCNT_SELL_PCT";
    public static final String PROD_BREAK_EVEN_PRC_AMT = "PROD_BREAK_EVEN_PRC_AMT";
    public static final String PROD_BREAK_EVEN_LOW_PRC_AMT = "PROD_BREAK_EVEN_LOW_PRC_AMT";
    public static final String PROD_BREAK_EVEN_UPPR_PRC_AMT = "PROD_BREAK_EVEN_UPPR_PRC_AMT";
    public static final String PROD_BREAK_EVEN_PUT_PRC_AMT = "PROD_BREAK_EVEN_PUT_PRC_AMT";
    public static final String PROD_BREAK_EVEN_CALL_PRC_AMT = "PROD_BREAK_EVEN_CALL_PRC_AMT";
    public static final String PROD_FLR_PRC_AMT = "PROD_FLR_PRC_AMT";
    public static final String PROD_BAR_PRC_AMT = "PROD_BAR_PRC_AMT";
    public static final String PROD_KNOCK_IN_TRIG_PRC_AMT = "PROD_KNOCK_IN_TRIG_PRC_AMT";
    public static final String PROD_EARL_CALL_TRIG_PRC_AMT = "PROD_EARL_CALL_TRIG_PRC_AMT";
    public static final String PROD_EXPI_CLOSE_PRC_AMT = "PROD_EXPI_CLOSE_PRC_AMT";
    public static final String PROD_DOWN_OUT_BAR_PRC_AMT = "PROD_DOWN_OUT_BAR_PRC_AMT";
    public static final String YIELD_TO_MTUR_PCT = "YIELD_TO_MTUR_PCT";
    public static final String DEN_AMT = "DEN_AMT";
    public static final String INVST_INIT_MIN_AMT = "INVST_INIT_MIN_AMT";
    public static final String TRD_MIN_AMT = "TRD_MIN_AMT";
    public static final String SPRD_CNT = "SPRD_CNT";
    public static final String SUPT_AON_IND = "SUPT_AON_IND";
    public static final String PYMT_DT = "PYMT_DT";
    public static final String VALN_DT = "VALN_DT";
    public static final String INSTM_ENTL_CNT = "INSTM_ENTL_CNT";
    public static final String OFFER_TYPE_CDE = "OFFER_TYPE_CDE";
    public static final String CUST_SELL_QTA_CNT = "CUST_SELL_QTA_CNT";
    public static final String RULE_QTA_ALTMT_CDE = "RULE_QTA_ALTMT_CDE";
    public static final String SETL_DT = "SETL_DT";
    public static final String LNCH_PROD_IND = "LNCH_PROD_IND";
    public static final String RTRV_PROD_EXTNL_IND = "RTRV_PROD_EXTNL_IND";
    public static final String PROD_EXTNL_CAT_CDE = "PROD_EXTNL_CAT_CDE";
    public static final String PDCY_CALL_CDE = "PDCY_CALL_CDE";
    public static final String PDCY_KNOCK_IN_CDE = "PDCY_KNOCK_IN_CDE";
    public static final String COMPLEX_PRODUCT_IND = "COMPLEX_PRODUCT_IND";


    /* Fields Mapping - Equity Linked Investment Underlying Stock */
    public static final String CRNCY_INSTM_UNDL_PRICE_CDE = "CRNCY_INSTM_UNDL_PRICE_CDE";
    public static final String PROD_KNOCK_IN_PRICE_AMT = "PROD_KNOCK_IN_PRICE_AMT";
    public static final String PROD_STRK_PRICE_INIT_PCT = "PROD_STRK_PRICE_INIT_PCT";
    public static final String PROD_STRK_CALL_PRICE_INIT_PCT = "PROD_STRK_CALL_PRICE_INIT_PCT";
    public static final String PROD_KNOCK_IN_PRICE_INIT_PCT = "PROD_KNOCK_IN_PRICE_INIT_PCT";
    public static final String PROD_INIT_SPOT_PRICE_AMT = "PROD_INIT_SPOT_PRICE_AMT";
    
    /* Fields Mapping - Bond and CD */
    public static final String ISSUER_NUMBER = "ISSUE_NUM";
    public static final String PRODUCT_ISSUE_DATE = "PROD_ISS_DT";
    public static final String PERIODICITY_COUPON_PAYMENT_CODE = "PDCY_COUPN_PYMT_CDE";
    public static final String COUPON_ANNUAL_RATE = "COUPN_ANNL_RATE";
    public static final String COUPON_ANNUAL_TEXT = "COUPN_ANNL_TEXT";
    public static final String COUPON_EXTENDABLE_INSTRUMENT_RATE = "COUPN_EXT_INSTM_RATE";
    public static final String PAYMENT_COUPON_NEXT_DATE = "PYMT_COUPN_NEXT_DT";
    public static final String FLEXIBLE_MATURITY_OPTION_INDICATOR = "FLEX_MAT_OPT_IND";
    public static final String CREDIT_RATING_AGENCY_CODE = "CREDIT_RTNG_AGCY_CDE";
    public static final String CREDIT_RATING_CODE = "CREDIT_RTNG_CDE";
    public static final String INTEREST_INDICATIVE_ACCRUE_AMOUNT = "INT_IND_ACCR_AMT";
    public static final String MATURITY_EXTENDED_DATE = "MTUR_EXT_DT";
    public static final String YIELD_BID_PERCENT = "YIELD_BID_PCT";
    public static final String YIELD_TO_CALL_BID_PERCENT = "YIELD_TO_CALL_BID_PCT";
    public static final String YIELD_TO_MATURITY_BID_PERCENT = "YIELD_TO_MTUR_BID_PCT";
    public static final String YIELD_OFFER_TEXT = "YIELD_OFFER_TEXT";
    public static final String YIELD_OFFER_PERCENT = "YIELD_OFFER_PCT";
    public static final String YIELD_TO_CALL_OFFER_PERCENT = "YIELD_TO_CALL_OFFER_PCT";
    public static final String YIELD_TO_MATURITY_OFFER_TEXT = "YIELD_TO_MTUR_OFFER_TEXT";
    public static final String SUBORDINATED_DEBT_INDICATOR = "SUB_DEBT_IND";
    public static final String BOND_STATUS_CODE = "BOND_STAT_CDE";
    public static final String COUNTRY_BOND_ISSUE_CODE = "CTRY_BOND_ISSUE_CDE";
    public static final String GUARANTOR_NAME = "GRNTR_NAME";
    public static final String CAPITAL_TIER_TEXT = "CPTL_TIER_TEXT";
    public static final String COUPON_TYPE = "COUPN_TYPE";
    public static final String COUPON_PREVIOUS_RATE = "COUPN_PREV_RATE";
    public static final String INDEX_FLOAT_RATE_NAME = "INDEX_FLT_RATE_NAME";
    public static final String BOND_FLOAT_SPREAD_RATE = "BOND_FLT_SPRD_RATE";
    public static final String COUPON_CURRENT_START_DATE = "COUPN_CURR_START_DT";
    public static final String COUPON_PREVIOUS_START_DATE = "COUPN_PREV_START_DT";
    public static final String BOND_CALLABLE_NEXT_DATE = "BOND_CALL_NEXT_DT";
    public static final String INTEREST_BASIS_CALCULATION_TEXT = "INT_BASIS_CALC_TEXT";
    public static final String INTEREST_ACCRUE_DAY_COUNT = "INT_ACCR_DAY_CNT";
    public static final String INVESTMENT_SOLD_LEAST_AMOUNT = "INVST_SOLD_LEST_AMT";
    public static final String INVESTMENT_INCREMENT_SOLD_AMOUNT = "INVST_INCRM_SOLD_AMT";
    public static final String SHARE_BID_COUNT = "SHR_BID_CNT";
    public static final String SHARE_OFFER_COUNT = "SHR_OFFR_CNT";
    public static final String PRODUCT_CLOSING_BID_PRICE_AMOUNT = "PROD_CLOSE_BID_PRC_AMT";
    public static final String PRODUCT_CLOSING_OFFER_PRICE_AMOUNT = "PROD_CLOSE_OFFR_PRC_AMT";
    public static final String YIELD_TYPE_CODE = "YIELD_TYPE_CDE";
    public static final String YIELD_TYPE_CODE_DAILY = "D";
    public static final String YIELD_DATE = "YIELD_DT";
    public static final String YIELD_EFFECTIVE_DATE = "YIELD_EFF_DT";
    public static final String YIELD_BID_CLOSING_PERCENT = "YIELD_BID_CLOSE_PCT";
    public static final String YIELD_OFFER_CLOSING_PERCENT = "YIELD_OFFER_CLOSE_PCT";
    public static final String BOND_CLOSING_DATE = "BOND_CLOSE_DT";
    public static final String BOND_SETTLEMENT_DATE = "BOND_SETL_DT";
    public static final String DISCOUNT_MARGIN_BANK_SELL_PERCENT = "DSCNT_MRGN_BSEL_PCT";
    public static final String DISCOUNT_MARGIN_BANK_BUY_PERCENT = "DSCNT_MRGN_BBUY_PCT";
    public static final String PRICE_BOND_RECEIVE_DATE_TIME = "PRC_BOND_RECV_DT_TM";
    public static final String ISSUER_DESCRIPTION = "ISR_DESC";
    public static final String ISSUER_PRIMARY_LOCAL_LANGUAGE_DESCRIPTION = "ISR_PLL_DESC";
    public static final String ISSUER_SECONDARY_LOCAL_LANGUAGE_DESCRIPTION = "ISR_SLL_DESC";
    public static final String SENIORITY_TYPE_CODE = "SR_TYPE_CDE";

    /* Fields Mapping - Mandatory Provident Fund */
    public static final String MPF_PRODUCT_CODE = "PROD_MPF_CDE";
    public static final String MPF_FUND_CODE = "FUND_MPF_CDE";
    public static final String CUMULATIVE_PERFORMANCE_PERCENT = "PERFM_CUM_PCT";
    public static final String CUMULATIVE_PERFORMANCE_IN_1_MONTH = "PERFM_1MO_PCT";
    public static final String CUMULATIVE_PERFORMANCE_IN_6_MONTH = "PERFM_6MO_PCT";
    public static final String CUMULATIVE_PERFORMANCE_IN_1_YEAR = "PERFM_1YR_PCT";
    public static final String CUMULATIVE_PERFORMANCE_IN_3_YEAR = "PERFM_3YR_PCT";
    public static final String CUMULATIVE_PERFORMANCE_IN_5_YEAR = "PERFM_5YR_PCT";
    public static final String PERFORMANCE_SINCE_LAUNCH_DATE = "PRFM_EFF_DT";
    public static final String PERFORMANCE_EFFECTIVE_DATE = "PFRM_EFF_DT";
    public static final String PERFORMANCE_SINCE_LAUNCH_PERCENT = "PERFM_SINCE_LNCH_PCT";
    public static final String PERFORMANCE_DATE = "PRFM_UPDT_DT";
    public static final String FUND_CODE = "FUND_CDE";
    public static final String FUND_NAME = "FUND_NAME";
    /* Fields Mapping - Foreign Currency Holiday */
    public static final String RECORD_YEAR = "REC_YEAR";
    public static final String RECORD_MONTH = "REC_MO";
    public static final String NUMBER_OF_DAYS_IN_MONTH = "DAY_MO_CNT";
    public static final String DAY_STATUS = "DAY_STAT_CDE";
    public static final String TOTAL_RECORD_LENGTH = "TOT_REC_LGTH";
    public static final String RECORD_LENGTH = "REC_LGTH";
    public static final String MONTH_LENGTH = "MO_LGTH";
    public static final String DAY_STATUS_LENGTH = "DAY_STAT_LGTH";

    /* Property Estate Information */
    public static final String PROPERTY_ESTATE_CODE = "PROP_ESTAT_CDE";
    public static final String PROPERTY_ESTATE_NAME = "PROP_ESTAT_NAME";
    public static final String PROPERTY_ESTATE_PLL_NAME = "PROP_ESTAT_PLL_NAME";
    public static final String PROPERTY_ESTATE_SLL_NAME = "PROP_ESTAT_SLL_NAME";
    public static final String DISTRICT_CODE = "DIST_CDE";

    /* Property Estate Price History Information */
    public static final String PROPERTY_ESTATE_PRICE_EFFECTIVE_START_DATE = "PRC_START_DT";
    public static final String PROPERTY_ESTATE_PRICE_EFFECTIVE_END_DATE = "PRC_END_DT";
    public static final String PROPERTY_ESTATE_HIGHEST_PRICE_CURRENCY_CODE = "PRC_HIGH_CCY";
    public static final String PROPERTY_ESTATE_HIGHEST_PRICE = "PRC_HIGH_AMT";
    public static final String PROPERTY_ESTATE_HIGHEST_PRICE_DECIMAL_PLACE = "PRC_HIGH_DP";
    public static final String PROPERTY_ESTATE_AVERAGE_PRICE_CURRENCY_CODE = "PRC_AVG_CCY";
    public static final String PROPERTY_ESTATE_AVERAGE_PRICE = "PRC_AVG_AMT";
    public static final String PROPERTY_ESTATE_AVERAGE_PRICE_DECIMAL_PLACE = "PRC_AVG_DP";
    public static final String PROPERTY_ESTATE_LOWEST_PRICE_CURRENCY_CODE = "PRC_LOW_CCY";
    public static final String PROPERTY_ESTATE_LOWEST_PRICE = "PRC_LOW_AMT";
    public static final String PROPERTY_ESTATE_LOWEST_PRICE_DECIMAL_PLACE = "PRC_LOW_DP";
    public static final String TRANSACTION_COUNT = "TRAN_CNT";
    public static final String PRICE_HISTORY_RETAIN_PERIOD = "PRC_HIST_RET_PRD";

    /* Field Mapping - Customer Eligibility */
    public static final String RESTRICTION_COUNTRY_TYPE_CODE_RESTRICTED_NATIONALITY = "N";
    public static final String RESTRICTION_COUNTRY_TYPE_CODE_RESTRICTED_COUNTRY_OF_RESIDENCE = "R";
    public static final String RESTRICTION_COUNTRY_TYPE_CODE_RESTRICTED_COUNTRY_OF_CORRESPONDENCE = "C";
    public static final String RESTRICTION_COUNTRY_TYPE_CODE_RESTRICTED_COUNTRY_OF_FISCAL = "F";
    public static final String CUSTOMER_PRODUCT_CATEGORY_CODE = "CUST_PROD_CAT_CDE";
    public static final String ELIGIBLE_CUSTOMER_PRODUCT_CATEGORY_INDICATOR = "ELIG_CUSP_CAT_IND";
    public static final String OVERRIDE_CUSTOMER_PRODUCT_CATEGORY_ELIGIBILITY_INDICATOR = "OVRID_CUSP_CAT_ELIG_IND";
    public static final String REQUIRE_COMPLETE_RISK_PROFILING_QUESTIONNAIRE_INDICATOR = "REQ_CMPL_RPQ_IND";
    public static final String OVERRIDE_REQUIRE_COMPLETE_RISK_PROFILING_QUESTIONNAIRE_INDICATOR = "OVRID_REQ_CMPL_RPQ_IND";
    public static final String PERIOD_VALID_RISK_PROFILING_QUESTIONNAIRE_CODE = "PRD_VLID_RPQ_CDE";
    public static final String PERIOD_VALID_RISK_PROFILING_QUESTIONNAIRE_NUMBER = "PRD_VLID_RPQ_NUM";
    public static final String OVERRIDE_RISK_PROFILING_QUESTIONNAIRE_VALID_PERIOD_INDICATOR = "OVRID_RPQ_VLID_PRD_IND";
    public static final String ACTION_LOW_RISK_PROFILING_QUESTIONNAIRE_SCORE_TEXT = "ACTION_LOW_RPQ_SCR_TEXT";
    public static final String FORM_REQUIRED_CODE = "FORM_REQ_CDE";
    public static final String OVERRIDE_REQUIRE_FORM_INDICATOR = "OVRID_REQ_FORM_IND";
    public static final String AGE_ALLOW_TRADE_MINIMUM_NUMBER = "AGE_ALLOW_TRD_MIN_NUM";
    public static final String AGE_ALLOW_TRADE_MAXIMUM_NUMBER = "AGE_ALLOW_TRD_MAX_NUM";
    public static final String OVERRIDE_MINIMUM_AGE_RESTRICTION_INDICATOR = "OVRID_MIN_AGE_RESTR_IND";
    public static final String OVERRIDE_MAXIMUM_AGE_RESTRICTION_INDICATOR = "OVRID_MAX_AGE_RESTR_IND";
    public static final String OPERATION_TYPE_CODE = "OPER_TYPE_CDE";
    public static final String OVERRIDE_CORRESPONDENCE_COUNTRY_CHECK_INDICATOR = "OVRID_CORS_CTRY_CHK_IND";
    public static final String OVERRIDE_RESIDENCE_COUNTRY_CHECK_INDICATOR = "OVRID_RES_CTRY_CHK_IND";
    public static final String OVERRIDE_NATIONALITY_CHECK_INDICATOR = "OVRID_NATL_CHK_IND";

    public static final String OVERRIDE_FISCAL_RESIDENCE_COUNTRY_CHECK_INDICATOR = "OVRID_RESTR_FISCL_CTRY_CHK_IND";
    public static final String REQUIRE_INVESTMENT_HOLDING_CHECK_INDICATOR = "REQ_INVST_HLDG_CHK_IND";
    public static final String OVERRIDE_INVESTMENT_HOLDING_CHECK_INDICATOR = "OVRID_INVST_HLDG_CHK_IND";

    /* Field Mapping - Customer Country Eligibility */
    public static final String RESTRICTION_COUNTRY_TYPE_CODE = "RESTR_CTRY_TYPE_CDE";
    public static final String COUNTRY_ROLE_CODE = "CTRY_ROLE_CDE";
    public static final String COUNTRY_ISO_CODE = "CTRY_ISO_CDE";
    public static final String RELATED_COUNTRY_CODE = "REL_CTRY_CDE";
    public static final String RESTRICTION_INDICATOR = "RSR_IND";
    public static final String COUNTRY_OF_CORRESPONDENCE_RELATED_COUNTRY_ISO_CODE = "CORS_CTRY_ISO_CDE";
    public static final String COUNTRY_OF_CORRESPONDENCE_RESTRICTION_CODE = "CORS_RESTR_CDE";
    public static final String COUNTRY_OF_RESIDENCE_RELATED_COUNTRY_ISO_CODE = "RES_CTRY_ISO_CDE";
    public static final String COUNTRY_OF_RESIDENCE_RESTRICTION_CODE = "RES_RESTR_CDE";
    public static final String NATIONALITY_RELATED_COUNTRY_ISO_CODE = "NATL_CTRY_ISO_CDE";
    public static final String NATIONALITY_RESTRICTION_CODE = "NATL_RESTR_CDE";
    public static final String FISCAL_RESIDENCE_COUNTRY_ISO_CODE = "FISCL_RES_CTRY_ISO_CDE";
    public static final String FISCAL_RESIDENCE_RESTRICTION_CODE = "FISCL_RES_RESTR_CDE";

    /* Fields Mapping - Reference Data */
    public static final String CODE_DESCRIPTION_VALUE_TYPE_CODE = "CDV_TYPE_CDE";
    public static final String CODE_DESCRIPTION_VALUE_CODE = "CDV_CDE";
    public static final String CODE_DESCRIPTION_VALUE_DESCRIPTION = "CDV_DESC";
    public static final String CODE_DESCRIPTION_VALUE_DESCRIPTION_IN_PRIMARY_LANGUAGE = "CDV_PLL_DESC";
    public static final String CODE_DESCRIPTION_VALUE_DESCRIPTION_IN_SECONDARY_LANGUAGE = "CDV_SLL_DESC";
    public static final String CODE_DESCRIPTION_VALUE_DISPLAY_SEQUENCE_NUMBER = "CDV_DISP_SEQ_NUM";
    public static final String CODE_DESCRIPTION_VALUE_PARENT_TYPE = "CDV_PARNT_TYPE_CDE";
    public static final String CODE_DESCRIPTION_VALUE_PARENT_CODE = "CDV_PARNT_CDE";
    public static final String RECORD_COMMENT_TEXT = "REC_CMNT_TEXT";

    /* Field Mapping - Product to Financial Document Relationship */
    public static final String DOCUMENT_TYPE_CODE = "DOC_TYPE_CDE";
    public static final String LANGUAGE_CATEGORY_CODE = "LANG_CAT_CDE";
    public static final String URL_DOC_TEXT = "URL_DOC_TEXT";
    public static final String SOURCE_TYPE_TO_CODE = "SRC_TYPE_TO_CDE";
    public static final String DOCUMENT_TYPE_TO_CODE = "DOC_TYPE_TO_CDE";
    public static final String DOCUMENT_SUBTYPE_TO_CODE = "DOC_SUBTP_TO_CDE";
    public static final String DOCUMENT_TO_ID = "DOC_TO_ID";
    public static final String LANGUAGE_CATEGORY_TO_CODE = "LANG_CAT_TO_CDE";
    public static final String UNIFORM_RESOURCE_LOCATOR_TO_TEXT = "URL_TO_TEXT";

    /* Field Mapping - Product User Defined Field */
    public static final String FIELD_TYPE_CODE_USER_DEFINED_FIELD = "U";
    public static final String FIELD_CODE_USER_DEFINED_FIELD = "UDF";
    public static final String FIELD_TYPE_CODE_CUSTOMER_ELIGIBILITY_CRITERIA_FIELD = "C";
    public static final String FIELD_CODE_CUSTOMER_ELIGIBILITY_CRITERIA_FIELD = "CUEG_CRI";
    public static final String USER_DEFINED_FIELD = "USER_DEFIN_FIELD";
    public static final String BONDCSV_BOND_USR_DEF_FLD = "bondcsv.bond.usr_def_fld";

    /* Field Mapping - Product User Defined Field Extend */
    public static final String FIELD_TYPE_CODE_USER_DEFINED_FIELD_EXTEND = "A";
    /* Field Mapping - Product User Defined OP Field Extend */
    public static final String FIELD_TYPE_CODE_USER_DEFINED_OP_FIELD_EXTEND = "A";

    /* Field Mapping - Product User Defined EG Field Extend */
    public static final String FIELD_TYPE_CODE_USER_DEFINED_EG_FIELD_EXTEND = "A";

    /* Field Mapping - Product Performance */
    public static final String VARIANCE_PRICE_TYPE_CODE = "P";
    public static final String VARIANCE_PRICE_YEAR_TO_DATE_PERCENT = "PERFM_YR_TO_DT_PCT";
    public static final String VARIANCE_PRICE_1_MONTH_PERCENT = "PERFM_1MO_PCT";
    public static final String VARIANCE_PRICE_3_MONTH_PERCENT = "PERFM_3MO_PCT";
    public static final String VARIANCE_PRICE_6_MONTH_PERCENT = "PERFM_6MO_PCT";
    public static final String VARIANCE_PRICE_1_YEAR_PERCENT = "PERFM_1YR_PCT";
    public static final String VARIANCE_PRICE_EXTENDED_1_YEAR_PERCENT = "PERFM_EXT_1YR_PCT";
    public static final String VARIANCE_PRICE_3_YEAR_PERCENT = "PERFM_3YR_PCT";
    public static final String VARIANCE_PRICE_5_YEAR_PERCENT = "PERFM_5YR_PCT";
    public static final String VARIANCE_PRICE_SINCE_LAUNCH_PERCENT = "PERFM_SINCE_LNCH_PCT";
    public static final String VARIANCE_PRICE_RETURN_VOLATILITY_1_YEAR_PERCENT = "RTRN_VOLTL_1YR_PCT";
    public static final String VARIANCE_PRICE_RETURN_VOLATILITY_EXTENDED_1_YEAR_PERCENT = "RTRN_VOLTL_EXT_1YR_PCT";
    public static final String VARIANCE_PRICE_RETURN_VOLATILITY_3_YEAR_PERCENT = "RTRN_VOLTL_3YR_PCT";
    public static final String VARIANCE_PRICE_RETURN_VOLATILITY_EXTENDED_3_YEAR_PERCENT = "RTRN_VOLTL_EXT_3YR_PCT";
    public static final String VARIANCE_PRICE_CALCULATE_DATE = "PERFM_CALC_DT";
    public static final String VARIANCE_PRICE_6_MONTH_AMOUNT = "PERFM_6MO_AMT";
    public static final String VARIANCE_PRICE_YEAR_TO_DATA_AMOUNT = "PERFM_YR_TO_DT_AMT";
    public static final String VARIANCE_PRICE_1_YEAR_AMOUNT = "PERFM_1YR_AMT";
    public static final String VARIANCE_PRICE_3_YEAR_AMOUNT = "PERFM_3YR_AMT";

    public static final String VARIANCE_BENCHMARK_TYPE_CODE = "B";
    public static final String VARIANCE_BENCHMARK_YEAR_TO_DATE_PERCENT = "BCHMK_PERFM_YR_TO_DT_PCT";
    public static final String VARIANCE_BENCHMARK_1_MONTH_PERCENT = "BCHMK_PERFM_1MO_PCT";
    public static final String VARIANCE_BENCHMARK_3_MONTH_PERCENT = "BCHMK_PERFM_3MO_PCT";
    public static final String VARIANCE_BENCHMARK_6_MONTH_PERCENT = "BCHMK_PERFM_6MO_PCT";
    public static final String VARIANCE_BENCHMARK_1_YEAR_PERCENT = "BCHMK_PERFM_1YR_PCT";
    public static final String VARIANCE_BENCHMARK_EXTENDED_1_YEAR_PERCENT = "BCHMK_PERFM_EXT_1YR_PCT";
    public static final String VARIANCE_BENCHMARK_3_YEAR_PERCENT = "BCHMK_PERFM_3YR_PCT";
    public static final String VARIANCE_BENCHMARK_5_YEAR_PERCENT = "BCHMK_PERFM_5YR_PCT";
    public static final String VARIANCE_BENCHMARK_SINCE_LAUNCH_PERCENT = "BCHMK_PERFM_SINCE_LNCH_PCT";
    public static final String VARIANCE_BENCHMARK_RETURN_VOLATILITY_1_YEAR_PERCENT = "BCHMK_RTRN_VOLTL_1YR_PCT";
    public static final String VARIANCE_BENCHMARK_RETURN_VOLATILITY_EXTENDED_1_YEAR_PERCENT = "BCHMK_RTRN_VOLTL_EXT_1YR_PCT";
    public static final String VARIANCE_BENCHMARK_RETURN_VOLATILITY_3_YEAR_PERCENT = "BCHMK_RTRN_VOLTL_3YR_PCT";
    public static final String VARIANCE_BENCHMARK_RETURN_VOLATILITY_EXTENDED_3_YEAR_PERCENT = "BCHMK_RTRN_VOLTL_EXT_3YR_PCT";
    public static final String VARIANCE_BENCHMARK_CALCULATE_DATE = "BCHMK_PERFM_CALC_DT";
    public static final String VARIANCE_BENCHMARK_6_MONTH_AMOUNT = "BCHMK_PERFM_6MO_AMT";
    public static final String VARIANCE_BENCHMARK_YEAR_TO_DATA_AMOUNT = "BCHMK_PERFM_YR_TO_DT_AMT";
    public static final String VARIANCE_BENCHMARK_1_YEAR_AMOUNT = "BCHMK_PERFM_1YR_AMT";
    public static final String VARIANCE_BENCHMARK_3_YEAR_AMOUNT = "BCHMK_PERFM_3YR_AMT";

    /* Field Mapping - Price History */
    public static final String PERIODICITY_PRICE_CODE = "PDCY_PRC_CDE";
    public static final String PERIODICITY_PRICE_CODE_DAILY = "D";
    public static final String PRICE_EFFECTIVE_DATE = "PRC_EFF_DT";
    // public static final String PRICE_TYPE_CODE = "PRC_TYPE_CDE";
    public static final String PRICE_DATE = "PRC_DT";
    public static final String PRICE_INPUT_DATE = "PRC_INP_DT";
    public static final String CURRENCY_PRODUCT_MARKET_PRICE_CODE = "CCY_PROD_MKT_PRC_CDE";
    public static final String PRODUCT_BID_PRICE_AMOUNT = "PROD_BID_PRC_AMT";
    public static final String PRODUCT_OFFER_PRICE_AMOUNT = "PROD_OFFR_PRC_AMT";
    public static final String PRODUCT_MARKET_PRICE_AMOUNT = "PROD_MKT_PRC_AMT";
    public static final String PRODUCT_NET_ASSET_VALUE_PRICE_AMOUNT = "PROD_NAV_PRC_AMT";
    public static final String PRICE_TYPE_BID = "BID";
    public static final String PRICE_TYPE_OFFR = "OFFR";
    public static final String PRICE_TYPE_MKT = "MKT";
    public static final String PRICE_TYPE_NAV = "NAV";
    // public static final String PRICE_CURRENCY_CODE = "CRNCY_PRC_CDE";
    // public static final String NET_ASSET_VALUE_PRICE_CURRENCY_CODE =
    // "NAV_PRC_CCY";
    // public static final String NET_ASSET_VALUE_PRICE_AMOUNT = "NAV_PRC_AMT";
    // public static final String NET_ASSET_VALUE_PRICE_DECIMAL_PLACE =
    // "NAV_PRC_DP";

    /* Field Mapping - Staff Eligibility */
    public static final String RELATION_TYPE_CODE = "REL_TYPE_CDE";
    public static final String EMPLOYMENT_POSITION_CODE = "EMPLOY_POSN_CDE";
    public static final String FORMULA_EMPLOYEE_ELIGIBILITY_TEXT = "FRMLA_EMPLY_ELIG_TEXT";
    public static final String OVERRIDE_EMPLOYEE_ELIGIBILITY_CHECK_INDICATOR = "OVRID_EMPLY_ELIG_CHK_IND";
    public static final String CUTOFF_EMPLOYEE_ELIGIBILITY_CHECK_INDICATOR = "CTOFF_EMPLY_ELIG_CHK_IND";
    public static final String EMPLOYEE_ELIGIBILITY_CHECK_CUTOFF_DATE = "EMPLY_ELIG_CTOFF_DT";
    public static final String FORMULA_EMPLOYEE_ELIGIBILITY_BEFORE_CUTOFF_TEXT = "FRMLA_EMP_ELIG_BFR_CTOFF_TEXT";
    public static final String DEFAULT_STAFF_CATEGORY_INDICATOR = "DFLT_STAFF_CAT_IND";

    /* Field Mapping - Reference Data Channel Relationship */
    public static final String CHANNEL_COMMUNICATION_CODE = "CHANL_COMN_CDE";

    /* Field Mapping - Product to Product Relation */
    public static final String PRODUCT_TO_PRODUCT_RELATION_CODE = "PROD_PROD_RELN_CDE";
    public static final String RELATED_COUNTRY_RECORD_CODE = "REL_CTRY_REC_CDE";
    public static final String RELATED_GROUP_MEMBER_RECORD_CODE = "REL_GRP_MEMBR_REC_CDE";
    public static final String RELATED_PRODUCT_TYPE_CODE = "REL_PROD_TYPE_CDE";
    public static final String RELATED_PRODUCT_ALTERNATIVE_PRIMARY_NUMBER = "REL_PROD_ALT_PRIM_NUM";
    public static final String PRODUCT_TO_PRODUCT_RELATION_CODE_OLD = "PROD_PROD_RELN_CDE_OLD";

    /* Market Data */
    public static final String MKT_DATA_SOURCE_FILE_PATH = "INTERFACE.mktdata.path";
    public static final String MKT_DATA_SOURCE_FILE_NAME = "INTERFACE.mktdata.name";
    public static final String MKT_DATA_SOURCE_FILE_SUFFIX = "INTERFACE.mktdata.suffix";
    public static final String MKT_DATA_CONFIG_FILE = "FILE_CONFIG.mktdata";


    public static final String FORMAT_ALLOW = "FORMAT_ALLOW";
    public static final String FORMAT = "FORMAT";
    public static final String CREATE_CUSTELIG_ALLOW = "CREATE_CUSTELIG_ALLOW";
    public static final String CREATE_PRODFINDOC_ALLOW = "CREATE_PRODFINDOC_ALLOW";
    public static final String CREATE_REFDATA_ALLOW = "CREATE_REFDATA_ALLOW";

    public static final String CREATE_ACTION = "CREATE";
    public static final String UPDATE_ACTION = "UPDATE";

    /*
     * to configurate the supported country for every legacy system (so that
     * WPC to backward compatible with legacy system with ISM1.0 enquire
     * message)
     */
    public static final String GWM_SUPPORTED_SITES = "GWM.ISM10.SUPPORTED.SITES";
    public static final String OBT_SUPPORTED_SITES = "OBT.ISM10.SUPPORTED.SITES";
    public static final String GHFI_SUPPORTED_SITES = "GHFI.ISM10.SUPPORTED.SITES";
    public static final String WPC_SUPPORTED_SITES = "WPC.ISM10.SUPPORTED.SITES";

    /* PROD_ASET_VOLTL_CLASS */
    public static final String ASET_VOLTL_CLASS_CDE = "ASET_VOLTL_CLASS_CDE";
    public static final String ASET_VOLTL_CLASS_WGHT_PCT = "ASET_VOLTL_CLASS_WGHT_PCT";

    /* ASET_VOLTL_CLASS_CORL */
    public static final String ASET_VOLTL_CLASS_RELATED_CDE = "ASET_VOLTL_CLASS_REL_CDE";
    public static final String ASET_VOLTL_CLASS_CORRELATION_RATE = "ASET_VOLTL_CLASS_CORL_RATE";

    /* ASET_VOLTL_CLASS_CHAR */
    public static final String RETURN_VOLTL_PERCENT = "RTRN_VOLTL_PCT";

    /* ASET_SIC_ALLOC */
    public static final String SIC_CDE = "SIC_CDE";
    public static final String ASET_ALLOC_WGHT_PCT = "ASET_ALLOC_WGHT_PCT";
    public static final String GEO_LOC_CDE = "GEO_LOC_CDE";

    /* External Configuration */
    public static final String WISDOM_TOPSELLER_NUM = "TOPSELLERNUM";


    /* For Product Key Handling */
    public static final String PRODKEY_UNIQUE = "BATCH.PRODKEY.UNIQUE";
    public static final String PRODKEY_PRIMARY = "BATCH.PRODKEY.PRIMARY";
    public static final String PRODKEY_ALL = "BATCH.PRODKEY.ALL";
    public static final String PRODKEY_OTHER = "BATCH.PRODKEY.OTHER";

    public static final String PRODKEY_PRODCDE = "BATCH.PRODKEY.PRODCDE";

    public static final String PRODKEY_FILTER_PROD_STATUS_CODE = "PRODKEY.FILTER_PROD_STATUS_CODE";
    public static final String PRODKEY_FILTER_PROD_STATUS_CODE_IGNORE_TYPE = "PRODKEY.FILTER_PROD_STATUS_CODE.IGNORE_TYPE";
    public static final String PRODKEY_FILTER_PROD_STATUS_CODE_DEFAULT = "PRODKEY.FILTER_PROD_STATUS_CODE.DEFAULT";

    /* For Performance Attributes */
    public static final String PROD_PERFM_PERFORMANCE_TYPE_CODES = "PROD_PERFM.PERFORMANCE_TYPE_CODES";
    public static final String PROD_PERFM_PERFORMANCE_TYPE_CODE_PREFIX = "PROD_PERFM.PERFORMANCE_TYPE_CODE.";

    public static final String PROPERTIES_DELIMITER = ".";

    /* Online Language Code Support */
    public static final String DEFAULT_LANGUAGE_SUPPORT_CODE = "ENG";
    public static final String PRIMARY_LANGUAGE_SUPPORT_CODE = "PL";
    public static final String SECONDARY_LANGUAGE_SUPPORT_CODE = "SL";

    /* For Data Sync */
    public static final String REPALCE_REFERENCE_CODE = "REPLACE_CDV_TYPE_CDE";

    public static final String TR_CODE_SUPPORTED_PRODTYPE = "TR.CODE.PRODTYPE.SUPPORT";

    /* For MS CATEGORY TO ASSET CLASS MAPPING */
    public static final String FUNDCAT_DERIVE_ASETCLASS = "FUNDCAT.DERIVE.ASETCLASS";
    /* RBT BOND EXT FIELD*/
    public static final String GNR_ACCT_TRDB = "gnr_acct_trdb";

    public static final String GBA_ACCT_TRDB = "gba_acct_trdb";
    public static final String NEWLY_ISSUE_BOND_IND = "newly_issue_bond_ind";
    public static final String RBP_MIGR_IND = "rbp_migr_ind";
    public static final String PROD_SUBTP_CDE_CAT = "prod_subtp_cde_cat";
    public static final String MKT_ORDER_IND = "mkt_order_ind";
    public static final String JADE_BOND_PROD_IND = "jade_bond_prod_ind";
    public static final String BOND_INDUS_GRP = "bond_indus_grp";
    public static final String ESG_IND = "esg_ind";
    public static final String PRC_EFF_TM_STR = "prc_eff_tm_str";
    public static final String CMB_ONLY_IND = "cmb_only_ind";
    public static final String CMB_PI_BOND_IND = "cmb_pi_bond_ind";
    public static final String BUY_RESTRICT_CHANNEL = "buy_restrict_channel";
    public static final String CMPlX_PROD_IND = "cmplx_prod_ind";
    /* for CMB copy only */
    public static final String dummy_BUY_RESTRICT_CHANNEL = "dummy_buy_restrict_channel";
    public static final String dummy_CMPlX_PROD_IND = "dummy_cmplx_prod_ind";

    /* Web Service Health Check */
    public static final String HEALTH_CHECK_RETRY_CNT = "healthcheck.retry";
    public static final String HEALTH_CHECK_WEB_SERVICE_REQUEST = "healthcheck.webservice.request";
    public static final String HEALTH_CHECK_WEB_SERVICE_URL = "healthcheck.webservice.url";
    public static final String SAML_TKEN_NAME = "tokenName";
    public static final String SAMLHEADER = "X-dummy-Saml";
    public static final String INPUTXSL = "converted_";
    
    public static final String XSL_Extension = ".xsl";
    
    public static final String MDSFE="MKD";
    
    public static final String NEW="NEW";
    
    public static final String COMMISSURE="_";
    
    public static final String FILE_FORMAT="xml";
	
    public static final String FILTERFILEPATH="filterFilePath";
    
    public static final String SCHEMAFILEPATH="schemaFilePath";	
    
    public static final String BAK=".bak";
    
    public static final String SCHEMA_CONFIG_FILE_PATH="schema_config_file_path";
    
    public static final String SOURCE_FILE_PATH="source_file_path";
    
    public static final String PROD_USER_DEFIN_EXT_FIELD = "prod_user_defin_ext_field";
    
    public static final String PROD_USER_DEFIN_OP_EXT_FIELD = "prod_user_defin_op_ext_field";
    
    public static final String PROD_USER_DEFIN_EG_EXT_FIELD = "prod_user_defin_eg_ext_field";
    
    /* RBT primary code */
    public static final String RBT_PRIMARY_CODE = "rbt.primary.code";
    
    /* Sweep Invest Fund Indicator excel form */
    public static final String siFundInd = "siFundInd";
    
    public static final String SUPPORT_MULTI_THREAD = "support_multi_thread";
    
	public static class Symbol {
		public static String AND = "&";
		public static String OR = "|";
		public static String QUESTION = "?";
		public static String EQUALITY = "=";
	}
    
	public static final String CMB_BOND_FINDOC_URL = "cmb_bond_findoc_url";
}
    
 





