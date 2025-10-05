/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		HFIBatchConstant
 *
 * Creation Date	Mar 2, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 2, 2006
 *    CMM/PPCR No.		
 *    Programmer		35021438
 *    Description		Batch Constants
 * 
 */
package com.dummy.wpc.datadaptor.constant;

import java.math.BigDecimal;

public final class Const {

	public static class Config {
		/* application constant */
		public static final String APP_CTRY_CDE = "APP.country.code";
		public static final String APP_ORGN_CDE = "APP.organization.code";

		/* interface file */
		public static final String GSOPSINTERFACE = "INTERFACE.gsops";
		public static final String ELIGINTERFACE = "INTERFACE.elig";
		public static final String UTINTERFACE = "INTERFACE.ut";
		public static final String BONDINTERFACE1 = "INTERFACE.bond.1";
		public static final String BONDINTERFACE2 = "INTERFACE.bond.2";
		public static final String BONDINTERFACE3 = "INTERFACE.bond.3";
		public static final String REUTERSBONDINTERFACE = "INTERFACE.bond.reuters";// added
																					// by
																					// Olivia
																					// for
																					// Reuters
																					// interface.
		public static final String DPSINTERFACE = "INTERFACE.dps";
		public static final String SIDINTERFACE = "INTERFACE.sid";
		public static final String SMIPINTERFACE = "INTERFACE.smip";
		public static final String PROTYESTINTERFACE = "INTERFACE.protyest";
		public static final String MPFFUNDUTPRCINTERFACE = "INTERFACE.mpffundutprc";
		public static final String MPFCUMPERFMNCEINTERFACE = "INTERFACE.mpfcumperfmnce";
		public static final String FRGNCCYHLDYINTERFACE = "INTERFACE.frgnccyhldy";

		/* interface constant */
		public static final String INTERFACE_DPS_RISK_LV = "INTERFACE.dps.risklevel";

		public static final String SPOMSELISNINTERFACE_PATH = "INTERFACE.spomselisn.path";
		public static final String SPOMSELISNINTERFACE_PREFIX = "INTERFACE.spomselisn.prefix";

		/* fixed length file configuration */
		public static final String DPSFILECONFIG = "FILE_CONFIG.dps";
		public static final String SIDFILECONFIG = "FILE_CONFIG.sid";
		public static final String UTFILECONFIG = "FILE_CONFIG.ut";
		public static final String SMIPFILECONFIG = "FILE_CONFIG.smip";
		public static final String PROTYESTFILECONFIG = "FILE_CONFIG.protyest";
		public static final String MPFFUNDUTPRCFILECONFIG = "FILE_CONFIG.mpffundutprc";
		public static final String MPFCUMPERFMNCEFILECONFIG = "FILE_CONFIG.mpfcumperfmnce";
		public static final String FRGNCCYHLDYFILECONFIG = "FILE_CONFIG.frgnccyhldy";

		/* query configuration */
		public static final String PMQUERYCONFIG = "QUERY_CONFIG.pm";
		public static final String SECQUERYCONFIG = "QUERY_CONFIG.sec";
		public static final String WRTSQUERYCONFIG = "QUERY_CONFIG.wrts";
		public static final String ELIQUERYCONFIG = "QUERY_CONFIG.eli";
		public static final String ELIGQUERYCONFIG = "QUERY_CONFIG.elig";
		public static final String UTQUERYCONFIG = "QUERY_CONFIG.ut";
		public static final String BONDQUERYCONFIG = "QUERY_CONFIG.bond";
		public static final String BONDQUERYCONFIG2 = "QUERY_CONFIG.bond2";// added
																			// by
																			// Olivia
																			// for
																			// Reuters
																			// interface
		public static final String BONDQUERYCONFIG3 = "QUERY_CONFIG.bond3";// added
																			// by
																			// Olivia
																			// for
																			// Reuters
																			// interface
		public static final String DPSQUERYCONFIG = "QUERY_CONFIG.dps";
		public static final String SIDQUERYCONFIG = "QUERY_CONFIG.sid";
		public static final String SPOMSELIQUERYCONFIG = "QUERY_CONFIG.spoms.eli";
		public static final String SPOMSSIDQUERYCONFIG = "QUERY_CONFIG.spoms.sid";
		public static final String DEFAULTREFQUERYCONFIG = "QUERY_CONFIG.ref.1";
		public static final String REFQUERYCONFIG = "QUERY_CONFIG.ref.2";
		public static final String PROTYESTQUERYCONFIG = "QUERY_CONFIG.protyest";
		public static final String MPFFUNDUTPRCQUERYCONFIG = "QUERY_CONFIG.mpffundutprc";
		public static final String MPFCUMPERFMNCEQUERYCONFIG = "QUERY_CONFIG.mpfcumperfmnce";
		public static final String FRGNCCYHLDYQUERYCONFIG = "QUERY_CONFIG.frgnccyhldy";
		public static final String SPOMSELISNQUERYCONFIG = "QUERY_CONFIG.spomselisn";
		public static final String GENERICQUERYCONFIG = "QUERY_CONFIG.generic";
		public static final String SEARCHQUERYCONFIG = "QUERY_CONFIG.search";

		/* SPOMS web services configuration */
		public static final String WSUSERNAME = "WS.username";
		public static final String WSPASSWORD = "WS.password";
		public static final String WSPASSWORDFILE = "WS.pswdfile.path";
		public static final String WSKEYFILE = "WS.keyfile.path";
		public static final String WSENDADDRESS = "WS.endaddress";

		/* Market Data service configuration */
		public static final String MDUSERNAME = "MD.username";
		public static final String MDPASSWORD = "MD.password";
		public static final String MDPASSWORDFILE = "MD.pswdfile.path";
		public static final String MDKEYFILE = "MD.keyfile.path";

		/* Logfile path */
		public static final String LOGFILEPATH = "HFIBATCH.logfile.path";
		/* HFI - WISDOM Bond update number */
		public static final String WISDOM_BOND_NUM = "WISDOM.BOND.NUM";
		public static final String BOND_HIGHEST_YIELD_NUM = "BOND.HIGHEST.YIELD.NUM";
		public static final String BOND_TOP_PRFM_NUM = "BOND.TOP.PRFM.NUM";
		public static final String BOND_NEWLY_INTRODUCED_NUM = "BOND.NEWLY.INTRODUCED.NUM";
		public static final String BOND_PRICE_DATE_TYPE = "BOND.PRICE.DATE.TYPE";
		public static final String BOND_PRICE_DATE_COUNT = "BOND.PRICE.DATE.COUNT";
		public static final String BOND_LAUNCH_DATE_TYPE = "BOND.LAUNCH.DATE.TYPE";
		public static final String BOND_LAUNCH_DATE_COUNT = "BOND.LAUNCH.DATE.COUNT";
		public static final String BOND_CLOSING_DATE_TYPE = "BOND.CLOSING.DATE.TYPE";
		public static final String BOND_CLOSING_DATE_COUNT = "BOND.CLOSING.DATE.COUNT";
		public static final String BOND_END_DATE_TYPE = "BOND.END.DATE.TYPE";
		public static final String BOND_END_DATE_COUNT = "BOND.END.DATE.COUNT";
	}

	/* common strings */
	public static final String APP_NAME = "HFI";
	public static final String NORM = "NORM";
	public static final String LOWER = "LOWER";
	public static final String UPPER = "UPPER";
	public static final String PUT = "PUT";
	public static final String CALL = "CALL";
	public static final String DPS_FILE = "TDRI1";
	public static final String SID_FILE = "TDRI2";
	public static final String UT_FILE = "HFIUP";
	public static final String SMIP_FILE = "SMIPSTIN";
	public static final String MPF_FUND_PRC_FILE = "MPFPC";
	public static final String MPF_CUM_PRFM_FILE = "MPCPF";
	public static final String FUND_HSE_CDE_REF_TYPE_CDE = "FUNDHSEMAP";
	public static final String PROD_TYPE_CDE_REF_TYPE_CDE = "PRODTYP";
	public static final String PROD_SUBTP_CDE_REF_TYPE_CDE = "PRODSUBTP";
	public static final String SEC_ALOW_CHNL_SUBTP_CDE_REF_TYPE_CDE = "SECDISPRDSTP";
	public static final String WRTS_ALOW_CHNL_SUBTP_CDE_REF_TYPE_CDE = "WRTSDISPRDSTP";
	public static final String ASET_CLASS_CDE_REF_TYPE_CDE = "AC";
	public static final String EXPIRED = "E";
	public static final String UNUSED_DAY = "U";
	public static final String BOND_ISSUER_REF_TYPE_CDE = "BONDISSUER";// new
																		// added
																		// for
																		// online
																		// bond
	public static final String BOND_ISSUER_GUNTR_REF_TYPE_CDE = "ISSUERGRNTR";// new
																				// added
																				// for
																				// online
																				// bond
	public static final String BOND_TOPSELLER_FILE = "BOND_TOPSELLER";
	public static final String THOMSON_REUTERS_COUPON_FREQ = "TRCOUPONFREQ";// Added
																			// by
																			// Olivia
																			// for
																			// Reuters
																			// Interface,mapping
																			// between
																			// TR
																			// Coupon
																			// Freq
																			// and
																			// SPOMS
																			// Coupon
																			// Freq
	public static final String THOMSON_REUTERS_COUPON_TYPE = "TRCOUPONTYPE";// Added
																			// by
																			// Olivia
																			// for
																			// Reuters
																			// Interface,mapping
																			// between
																			// TR
																			// coupon
																			// type
																			// and
																			// SPOMS
																			// coupon
																			// type

	public static final String UNDERLINE = "_";
	public static final String PERIOD = ".";

	/* numbers and algebraic signs */
	public static final String POS_SGN = "+";
	public static final String NEG_SGN = "-";
	public static final int PROTY_EST_PRC_HIST_VALID_PERIOD = -7;
	public static final int HLDY_REC_TOTAL_LEN = 453;
	public static final int HLDY_REC_BEGIN_LEN = 13;
	public static final int HLDY_REC_MIDDLE_LEN = 4;
	public static final int HLDY_REC_END_LEN = 31;
	public static final int HLDY_STUS_LEN = 31;

	/* Error code */
	public static final String ERROR_CDE = "0001";
	public static final String ERROR = "E";

	/* table strings */
	public static final String PM_TBL = "TBHFPROD";
	public static final String SEC_TBL = "TBHFPDEQ";
	public static final String WRTS_TBL = "TBHFPDWR";
	public static final String CUST_CTRY_ELIG_TBL = "TBHFCECT";
	public static final String CUST_ELIG_TBL = "TBHFCUEG";
	public static final String UT_TBL = "TBHFPDUT";
	public static final String BOND_TBL = "TBHFPDBN";
	public static final String DPS_TBL = "TBHFPDPS";
	public static final String SID_TBL = "TBHFPDSD";
	public static final String ELI_TBL = "TBHFPDEN";
	public static final String CHNL_TBL = "TBHFPDCH";
	public static final String REF_TBL = "TBHFREFE";
	public static final String PROTY_EST_TBL = "TBHFPYDS";
	public static final String PROTY_EST_PRC_HIST_TBL = "TBHFPYPR";
	public static final String MPF_TBL = "TBHFPDMF";
	public static final String PROD_PRC_HIST_TBL = "TBHFPDPH";
	public static final String FRGN_CRNCY_HLDY_TBL = "TBHFFXHO";
	public static final String PROD_USR_DEF_FLD_TBL = "TBHFPDUF";
	public static final String PDRL_TBL = "TBHFPDRL";// Added by Olivia for
														// Reuters interface,
														// check termsheet

	/* xml tags */
	public static final String QUERY = "query";
	public static final String SCHEMA = "schema";
	public static final String KEYWORD = "keyword";
	public static final String TABLE = "table";
	public static final String COLUMN = "column";
	public static final String PRIMARY_KEY = "primaryKey";

	/* SQL */
	public static final String SELECT_FOR_UPDATE_QUERY = "SELECT INT_SYS_PARM_VAL FROM TBHFIPRM WHERE CTRY_CDE =? AND ORGN_CDE =? AND INT_SYS_PARM_CDE=? FOR UPDATE OF INT_SYS_PARM_VAL, UPDT_LAST_DT, UPDT_LAST_TM";
	public static final String UPDATE_QUERY = "UPDATE TBHFIPRM SET INT_SYS_PARM_VAL=?, UPDT_LAST_DT=?, UPDT_LAST_TM=? WHERE CTRY_CDE =? AND ORGN_CDE =? AND INT_SYS_PARM_CDE=?";

	/* SQL common strings */
	public static final String SQL_SELECT = "SELECT";
	public static final String SQL_UPDATE = "UPDATE";
	public static final String SQL_DELETE = "DELETE";
	public static final String SQL_INSERT = "INSERT";
	public static final String SQL_SET = " SET ";
	public static final String SQL_FROM = " FROM ";
	public static final String SQL_WHERE = " WHERE ";

	/* Product Type Code */
	public static final String EQUITY = "SEC";
	public static final String WARRANT = "WRTS";
	public static final String UNIT_TRUST = "UT";
	public static final String BOND_CD = "BOND";
	public static final String DEPOSIT_PLUS = "DPS";
	public static final String STRUCTURE_INVESTMENT_DEPOSIT = "SID";
	public static final String EQUITY_LINKED_INVESTMENT = "ELI";
	public static final String STRUCTURE_NOTE = "SN";
	public static final String MANDATORY_PROVIDENT_FUND = "MPF";

	/* Customer Eligibility */
	public static final BigDecimal AGE_TRD_MIN_NUM = BigDecimal.valueOf(18);
	public static final BigDecimal AGE_TRD_MAX_NUM = BigDecimal.valueOf(65);
	public static final String FUNC_RSR_TYPE_CDE = "T";

	/* Product Master */
	public static final String CTRY_CDE = "CTRY_CDE";
	public static final String ORGN_CDE = "ORGN_CDE";
	public static final String PROD_TYPE_CDE = "PROD_TYPE_CDE";
	public static final String PROD_SUBTP_CDE = "PROD_SUBTP_CDE";
	public static final String PROD_CDE = "PROD_CDE";
	public static final String PROD_NAME = "PROD_NAME";
	public static final String SHRT_NAME = "SHRT_NAME";

	public static final String CRNCY_CDE = "CRNCY_CDE";
	public static final String RISK_LVL_CDE = "RISK_LVL_CDE";
	public static final String PRD_PROD_CDE = "PRD_PROD_CDE";
	public static final String PRD_PROD_NUM = "PRD_PROD_NUM";
	public static final String LAUNCH_DT = "LAUNCH_DT";
	public static final String MAT_DT = "MAT_DT";
	public static final String REGN_INVST_CDE = "REGN_INVST_CDE";
	public static final String SECT_CDE = "SECT_CDE";
	public static final String SLST_FUND_1_IND = "SLST_FUND_1_IND";
	public static final String SLST_FUND_EFF_1_DT = "SLST_FUND_EFF_1_DT";

	public static final String CTRY_REC_CDE = "CTRY_REC_CDE";
	public static final String GRP_MEMBR_REC_CDE = "GRP_MEMBR_REC_CDE";
	public static final String PROD_ALT_PRIM_NUM = "PROD_ALT_PRIM_NUM";
	public static final String PROD_CDE_ALT_CLASS_CDE = "PROD_CDE_ALT_CLASS_CDE";
	public static final String PROD_ALT_NUM = "PROD_ALT_NUM";
	public static final String PROD_TYPE = "PROD_TYPE";
	public static final String PROD_PLL_NAME = "PROD_PLL_NAME";
	public static final String PROD_SLL_NAME = "PROD_SLL_NAME";
	public static final String PROD_SHRT_NAME = "PROD_SHRT_NAME";
	public static final String PROD_SHRT_PLL_NAME = "PROD_SHRT_PLL_NAME";
	public static final String PROD_SHRT_SLL_NAME = "PROD_SHRT_SLL_NAME";
	public static final String PROD_DESC = "PROD_DESC";
	public static final String PROD_PLL_DESC = "PROD_PLL_DESC";
	public static final String PROD_SLL_DESC = "PROD_SLL_DESC";
	public static final String ASET_CLASS_CDE = "ASET_CLASS_CDE";
	public static final String ASET_UNDL_CDE = "ASET_UNDL_CDE";
	public static final String PROD_STAT_CDE = "PROD_STAT_CDE";
	public static final String CCY_PROD_CDE = "CCY_PROD_CDE";
	public static final String CCY_PROD_TRADE_CDE = "CCY_PROD_TRADE_CDE";
	public static final String TERM_REMAIN_DAY_CNT = "TERM_REMAIN_DAY_CNT";
	public static final String PROD_LNCH_DT = "PROD_LNCH_DT";
	public static final String PROD_MTUR_DT = "PROD_MTUR_DT";
	public static final String MKT_INVST_CDE = "MKT_INVST_CDE";
	public static final String SECT_INVST_CDE = "SECT_INVST_CDE";
	public static final String ALLOW_BUY_PROD_IND = "ALLOW_BUY_PROD_IND";
	public static final String ALLOW_SELL_PROD_IND = "ALLOW_SELL_PROD_IND";
	public static final String ALLOW_BUY_UT_PROD_IND = "ALLOW_BUY_UT_PROD_IND";
	public static final String ALLOW_BUY_AMT_PROD_IND = "ALLOW_BUY_AMT_PROD_IND";
	public static final String ALLOW_SELL_UT_PROD_IND = "ALLOW_SELL_UT_PROD_IND";
	public static final String ALLOW_SELL_AMT_PROD_IND = "ALLOW_SELL_AMT_PROD_IND";
	public static final String ALLOW_SELL_MIP_PROD_IND = "ALLOW_SELL_MIP_PROD_IND";
	public static final String ALLOW_SELL_MIP_UT_PROD_IND = "ALLOW_SELL_MIP_UT_PROD_IND";
	public static final String ALLOW_SELL_MIP_AMT_PROD_IND = "ALLOW_SELL_MIP_AMT_PROD_IND";
	public static final String ALLOW_SW_IN_PROD_IND = "ALLOW_SW_IN_PROD_IND";
	public static final String ALLOW_SW_IN_UT_PROD_IND = "ALLOW_SW_IN_UT_PROD_IND";
	public static final String ALLOW_SW_IN_AMT_PROD_IND = "ALLOW_SW_IN_AMT_PROD_IND";
	public static final String ALLOW_SW_OUT_PROD_IND = "ALLOW_SW_OUT_PROD_IND";
	public static final String ALLOW_SW_OUT_UT_PROD_IND = "ALLOW_SW_OUT_UT_PROD_IND";
	public static final String ALLOW_SW_OUT_AMT_PROD_IND = "ALLOW_SW_OUT_AMT_PROD_IND";
	public static final String INCM_CHAR_PROD_IND = "INCM_CHAR_PROD_IND";
	public static final String CPTL_GURNT_PROD_IND = "CPTL_GURNT_PROD_IND";
	public static final String YIELD_ENHN_PROD_IND = "YIELD_ENHN_PROD_IND";
	public static final String GRWTH_CHAR_PROD_IND = "GRWTH_CHAR_PROD_IND";
	public static final String PRTY_PROD_SRCH_RSULT_NUM = "PRTY_PROD_SRCH_RSULT_NUM";
	public static final String PROD_SLST_IND = "PROD_SLST_IND";
	public static final String PROD_EFF_SLST_DT = "PROD_EFF_SLST_DT";
	public static final String AVAIL_MKT_INFO_IND = "AVAIL_MKT_INFO_IND";
	public static final String PRD_RTRN_AVG_NUM = "PRD_RTRN_AVG_NUM";
	public static final String RTRN_VOLTL_AVG_PCT = "RTRN_VOLTL_AVG_PCT";
	public static final String DMY_PROD_SUBTP_REC_IND = "DMY_PROD_SUBTP_REC_IND";
	public static final String DISP_COM_PROD_SRCH_IND = "DISP_COM_PROD_SRCH_IND";
	public static final String DIVR_NUM = "DIVR_NUM";
	public static final String MRK_TO_MKT_IND = "MRK_TO_MKT_IND";
	public static final String PROD_NARR_TEXT = "PROD_NARR_TEXT";
	public static final String PROD_NARR_PLL_TEXT = "PROD_NARR_PLL_TEXT";
	public static final String PROD_NARR_SLL_TEXT = "PROD_NARR_SLL_TEXT";
	public static final String CTRY_PROD_TRADE_1_CDE = "CTRY_PROD_TRADE_1_CDE";
	public static final String CTRY_PROD_TRADE_2_CDE = "CTRY_PROD_TRADE_2_CDE";
	public static final String CTRY_PROD_TRADE_3_CDE = "CTRY_PROD_TRADE_3_CDE";
	public static final String CTRY_PROD_TRADE_4_CDE = "CTRY_PROD_TRADE_4_CDE";
	public static final String CTRY_PROD_TRADE_5_CDE = "CTRY_PROD_TRADE_5_CDE";
	public static final String BUS_START_TM = "BUS_START_TM";
	public static final String BUS_END_TM = "BUS_END_TM";
	public static final String INTRO_PROD_CURR_PRD_IND = "INTRO_PROD_CURR_PRD_IND";
	public static final String PROD_TOP_SELL_RANK_NUM = "PROD_TOP_SELL_RANK_NUM";
	public static final String PROD_TOP_PERFM_RANK_NUM = "PROD_TOP_PERFM_RANK_NUM";
	public static final String PROD_TOP_YIELD_RANK_NUM = "PROD_TOP_YIELD_RANK_NUM";
	public static final String PROD_ISSUE_CROS_REFER_DT = "PROD_ISSUE_CROS_REFER_DT";
	public static final String BCHMK_PLL_NAME = "BCHMK_PLL_NAME";
	public static final String BCHMK_SLL_NAME = "BCHMK_SLL_NAME";
	public static final String TRD_FIRST_DT = "TRD_FIRST_DT";
	public static final String LOAN_PROD_OD_MRGN_PCT = "LOAN_PROD_OD_MRGN_PCT";
	public static final String QTY_UNIT_PROD_CDE = "QTY_UNIT_PROD_CDE";
	public static final String PROD_LOC_CDE = "PROD_LOC_CDE";
	public static final String PROD_TAX_FREE_WRAP_ACT_STA_CDE = "PROD_TAX_FREE_WRAP_ACT_STA_CDE";
	public static final String DCML_PLACE_TRADE_UNIT_NUM = "DCML_PLACE_TRADE_UNIT_NUM";
	public static final String CCY_INVST_CDE = "CCY_INVST_CDE";
	public static final String PRC_INP_DT = "PRC_INP_DT";
	public static final String CCY_PROD_MKT_PRC_CDE = "CCY_PROD_MKT_PRC_CDE";
	public static final String PROD_BID_PRC_AMT = "PROD_BID_PRC_AMT";
	public static final String PROD_OFFR_PRC_AMT = "PROD_OFFR_PRC_AMT";
	public static final String PROD_MKT_PRC_AMT = "PROD_MKT_PRC_AMT";
	public static final String PROD_NAV_PRC_AMT = "PROD_NAV_PRC_AMT";
	public static final String PERFM_CALC_DT = "PERFM_CALC_DT";
	public static final String PERFM_YR_TO_DT_PCT = "PERFM_YR_TO_DT_PCT";
	public static final String PERFM_1MO_PCT = "PERFM_1MO_PCT";
	public static final String PERFM_3MO_PCT = "PERFM_3MO_PCT";
	public static final String PERFM_6MO_PCT = "PERFM_6MO_PCT";
	public static final String PERFM_1YR_PCT = "PERFM_1YR_PCT";
	public static final String PERFM_3YR_PCT = "PERFM_3YR_PCT";
	public static final String PERFM_5YR_PCT = "PERFM_5YR_PCT";
	public static final String PERFM_EXT_1YR_PCT = "PERFM_EXT_1YR_PCT";
	public static final String PERFM_SINCE_LNCH_PCT = "PERFM_SINCE_LNCH_PCT";
	public static final String RTRN_VOLTL_1YR_PCT = "RTRN_VOLTL_1YR_PCT";
	public static final String RTRN_VOLTL_EXT_1YR_PCT = "RTRN_VOLTL_EXT_1YR_PCT";
	public static final String RTRN_VOLTL_3YR_PCT = "RTRN_VOLTL_3YR_PCT";
	public static final String RTRN_VOLTL_EXT_3YR_PCT = "RTRN_VOLTL_EXT_3YR_PCT";
	public static final String PERFM_6MO_AMT = "PERFM_6MO_AMT";
	public static final String PERFM_YR_TO_DT_AMT = "PERFM_YR_TO_DT_AMT";
	public static final String PERFM_1YR_AMT = "PERFM_1YR_AMT";
	public static final String PERFM_3YR_AMT = "PERFM_3YR_AMT";
	public static final String BCHMK_PERFM_CALC_DT = "BCHMK_PERFM_CALC_DT";
	public static final String BCHMK_PERFM_YR_TO_DT_PCT = "BCHMK_PERFM_YR_TO_DT_PCT";
	public static final String BCHMK_PERFM_1MO_PCT = "BCHMK_PERFM_1MO_PCT";
	public static final String BCHMK_PERFM_3MO_PCT = "BCHMK_PERFM_3MO_PCT";
	public static final String BCHMK_PERFM_6MO_PCT = "BCHMK_PERFM_6MO_PCT";
	public static final String BCHMK_PERFM_1YR_PCT = "BCHMK_PERFM_1YR_PCT";
	public static final String BCHMK_PERFM_3YR_PCT = "BCHMK_PERFM_3YR_PCT";
	public static final String BCHMK_PERFM_5YR_PCT = "BCHMK_PERFM_5YR_PCT";
	public static final String BCHMK_PERFM_EXT_1YR_PCT = "BCHMK_PERFM_EXT_1YR_PCT";
	public static final String BCHMK_PERFM_SINCE_LNCH_PCT = "BCHMK_PERFM_SINCE_LNCH_PCT";
	public static final String BCHMK_RTRN_VOLTL_1YR_PCT = "BCHMK_RTRN_VOLTL_1YR_PCT";
	public static final String BCHMK_RTRN_VOLTL_EXT_1YR_PCT = "BCHMK_RTRN_VOLTL_EXT_1YR_PCT";
	public static final String BCHMK_RTRN_VOLTL_3YR_PCT = "BCHMK_RTRN_VOLTL_3YR_PCT";
	public static final String BCHMK_RTRN_VOLTL_EXT_3YR_PCT = "BCHMK_RTRN_VOLTL_EXT_3YR_PCT";
	public static final String BCHMK_PERFM_6MO_AMT = "BCHMK_PERFM_6MO_AMT";
	public static final String BCHMK_PERFM_YR_TO_DT_AMT = "BCHMK_PERFM_YR_TO_DT_AMT";
	public static final String BCHMK_PERFM_1YR_AMT = "BCHMK_PERFM_1YR_AMT";
	public static final String BCHMK_PERFM_3YR_AMT = "BCHMK_PERFM_3YR_AMT";
	public static final String PLDG_LIMIT_ASSOC_ACCT_IND = "PLDG_LIMIT_ASSOC_ACCT_IND";
	public static final String PROD_OWN_CDE = "PROD_OWN_CDE";
	public static final String FORGN_PROD_IND = "FORGN_PROD_IND";
	public static final String ASET_CAT_EXTNL_CDE = "ASET_CAT_EXTNL_CDE";
	public static final String PROD_MKT_PRICE_PREV_AMT = "PROD_MKT_PRICE_PREV_AMT";
	public static final String CHRG_CAT_CDE = "CHRG_CAT_CDE";
	public static final String SUPT_RCBL_CASH_PROD_IND = "SUPT_RCBL_CASH_PROD_IND";
	public static final String SUPT_RCBL_SCRIP_PROD_IND = "SUPT_RCBL_SCRIP_PROD_IND";
	public static final String ASET_UNDER_MGMT_CHRG_PROD_IND = "ASET_UNDER_MGMT_CHRG_PROD_IND";
	public static final String INVST_IMIG_PROD_IND = "INVST_IMIG_PROD_IND";
	public static final String RESTR_INVSTR_PROD_IND = "RESTR_INVSTR_PROD_IND";
	public static final String PROD_INVST_OBJ_TEXT = "PROD_INVST_OBJ_TEXT";
	public static final String PROD_INVST_OBJ_PLL_TEXT = "PROD_INVST_OBJ_PLL_TEXT";
	public static final String PROD_INVST_OBJ_SLL_TEXT = "PROD_INVST_OBJ_SLL_TEXT";
	public static final String NO_SCRIB_FEE_PROD_IND = "NO_SCRIB_FEE_PROD_IND";
	public static final String TOP_SELL_PROD_IND = "TOP_SELL_PROD_IND";
	public static final String TOP_PERFM_PROD_IND = "TOP_PERFM_PROD_IND";
	public static final String INVST_INIT_MAX_AMT = "INVST_INIT_MAX_AMT";
	public static final String RCMND_PROD_DECSN_CDE = "RCMND_PROD_DECSN_CDE";
	public static final String ASET_TEXT = "ASET_TEXT";
	public static final String PROD_DERVT_CDE = "PROD_DERVT_CDE";
	public static final String PROD_DERVT_RVS_CDE = "PROD_DERVT_RVS_CDE";
	public static final String PRD_DERV_RVS_EFF_DT = "PRD_DERV_RVS_EFF_DT";
	public static final String EQTY_UNDL_IND = "EQTY_UNDL_IND";
	public static final String WLTH_ACCUM_GOAL_IND = "WLTH_ACCUM_GOAL_IND";
	public static final String PRTY_WLTH_ACCUM_GOAL_CDE = "PRTY_WLTH_ACCUM_GOAL_CDE";
	public static final String PLAN_FOR_RTIRE_GOAL_IND = "PLAN_FOR_RTIRE_GOAL_IND";
	public static final String PRTY_PLN_FOR_RTIRE_CDE = "PRTY_PLN_FOR_RTIRE_CDE";
	public static final String EDUC_GOAL_IND = "EDUC_GOAL_IND";
	public static final String PRTY_EDUC_CDE = "PRTY_EDUC_CDE";
	public static final String LIVE_IN_RTIRE_GOAL_IND = "LIVE_IN_RTIRE_GOAL_IND";
	public static final String PRTY_LIVE_IN_RTIRE_CDE = "PRTY_LIVE_IN_RTIRE_CDE";
	public static final String PROTC_GOAL_IND = "PROTC_GOAL_IND";
	public static final String PRTY_PROTC_CDE = "PRTY_PROTC_CDE";
	public static final String MNG_SOLN_IND = "MNG_SOLN_IND";
	public static final String PRD_INVST_TNOR_NUM = "PRD_INVST_TNOR_NUM";
	public static final String GEO_RISK_IND = "GEO_RISK_IND";
	public static final String PROD_LQDY_IND = "PROD_LQDY_IND";
	public static final String RVRSE_ENQ_PROD_IND = "RVRSE_ENQ_PROD_IND";
	public static final String PROD_INVST_TYPE_CDE = "PROD_INVST_TYPE_CDE";
	public static final String ISLM_PROD_IND = "ISLM_PROD_IND";
	public static final String CNTL_ADVC_IND = "CNTL_ADVC_IND";
	public static final String USER_DEFIN_FIELD = "USER_DEFIN_FIELD";
	public static final String PROD_CMNT_TEXT = "PROD_CMNT_TEXT";
	public static final String ASET_VOLTL_CLASS_CDE = "ASET_VOLTL_CLASS_CDE";
	public static final String ASET_VOLTL_CLASS_WGHT_PCT = "ASET_VOLTL_CLASS_WGHT_PCT";
	public static final String SIC_CDE = "SIC_CDE";
	public static final String SIC_ASET_ALLOC_WGHT_PCT = "SIC_ASET_ALLOC_WGHT_PCT";
	public static final String GEO_LOC_CDE = "GEO_LOC_CDE";
	public static final String GEO_ASET_ALLOC_WGHT_PCT = "GEO_ASET_ALLOC_WGHT_PCT";

	/* common fields */
	public static final String INT_PCT = "INT_PCT";
	public static final String CRNCY_LINK_CDE = "CRNCY_LINK_CDE";
	public static final String INVST_INIT_MIN_AMT = "INVST_INIT_MIN_AMT";
	public static final String TERMSHEET = "TERMSHEET";// Added by Olivia for
														// updating state code

	/* Deposit Plus */
	public static final String DPS_TYPE_CDE = "DPS_TYPE_CDE";
	public static final String TMD_PROD_CDE = "TMD_PROD_CDE";
	public static final String DEPST_MIN_AMT = "DEPST_MIN_AMT";
	public static final String TLR_MADE_DPS_IND = "TLR_MADE_DPS_IND";
	public static final String KNOCK_TYPE_CDE = "KNOCK_TYPE_CDE";
	public static final String EXCHG_RATE_FIX_DT = "EXCHG_RATE_FIX_DT";
	public static final String CONV_FACTR_RATE = "CONV_FACTR_RATE";
	public static final String EXCHG_SPOT_RATE = "EXCHG_SPOT_RATE";
	public static final String EXCHG_INIT_RATE = "EXCHG_INIT_RATE";
	public static final String EXCHG_BKVN_RATE = "EXCHG_BKVN_RATE";
	public static final String PRD_OBSR_START_DT = "PRD_OBSR_START_DT";
	public static final String PRD_OBSR_END_DT = "PRD_OBSR_END_DT";
	public static final String RTRN_OPTN = "RTRN_OPTN";
	public static final String PROD_SFX = "PROD_SFX";
	public static final String SRC_CRNCY = "SRC_CRNCY";
	public static final String TRG_CRNCY = "TRG_CRNCY";

	/* Structured Investment Deposit */
	public static final String PROD_STUS = "PROD_STUS";
	public static final String APX_TYPE_CDE = "APX_TYPE_CDE";
	public static final String CONV_CDE = "CONV_CDE";
	public static final String RTRN_INTRM_STR = "RTRN_INTRM_STR";
	// public static final String RTRN_INTRM_PCT = "RTRN_INTRM_PCT";
	public static final String PYMT_INTRM_RTRN_DT = "PYMT_INTRM_RTRN_DT";
	public static final String PRD_MKT_START_DT = "PRD_MKT_START_DT";
	public static final String PRD_MKT_END_DT = "PRD_MKT_END_DT";
	public static final String YLD_ANNL_MIN_PCT = "YLD_ANNL_MIN_PCT";
	public static final String ALLOW_ERDM_IND = "ALLOW_ERDM_IND";
	public static final String PRD_ERDM_DALW_TEXT = "PRD_ERDM_DALW_TEXT";
	public static final String ERDM_PCT = "ERDM_PCT";
	// public static final String OFFER_TYPE_CDE = "OFFER_TYPE_CDE";
	public static final String OVDFT_SEC_PCT = "OVDFT_SEC_PCT";
	public static final String BONUS_RATE_TYPE = "BONUS_RATE_TYPE";
	public static final String BONUS_DATE_TYPE = "BONUS_DATE_TYPE";
	public static final String LAUNCH_STUS = "LAUNCH_STUS";

	/* Unit Trust */
	public static final String FUND_CDE = "FUND_CDE";
	public static final String CRNCY_PRC_CDE = "CRNCY_PRC_CDE";
	public static final String BID_PRC_AMT = "BID_PRC_AMT";
	public static final String OFFER_PRC_AMT = "OFFER_PRC_AMT";
	public static final String NAV_PRC_AMT = "NAV_PRC_AMT";
	public static final String PRC_EFF_DT = "PRC_EFF_DT";
	public static final String BCHMK_DT = "BCHMK_DT";
	public static final String PRFM_1YR_SGN = "PRFM_1YR_SGN";
	public static final String PRFM_1YR_PCT = "PRFM_1YR_PCT";
	public static final String PRFM_3YR_SGN = "PRFM_3YR_SGN";
	public static final String PRFM_3YR_PCT = "PRFM_3YR_PCT";
	public static final String PRFM_5YR_SGN = "PRFM_5YR_SGN";
	public static final String PRFM_5YR_PCT = "PRFM_5YR_PCT";
	public static final String BCHMK_1YR_SGN = "BCHMK_1YR_SGN";
	public static final String BCHMK_1YR_PCT = "BCHMK_1YR_PCT";
	public static final String BCHMK_3YR_SGN = "BCHMK_3YR_SGN";
	public static final String BCHMK_3YR_PCT = "BCHMK_3YR_PCT";
	public static final String BCHMK_5YR_SGN = "BCHMK_5YR_SGN";
	public static final String BCHMK_5YR_PCT = "BCHMK_5YR_PCT";
	public static final String PRFM_6MO_SGN = "PRFM_6MO_SGN";
	public static final String PRFM_6MO_PCT = "PRFM_6MO_PCT";
	public static final String PRFM_SNC_LNCH_SGN = "PRFM_SNC_LNCH_SGN";
	public static final String PRFM_SNC_LNCH_PCT = "PRFM_SNC_LNCH_PCT";
	public static final String PRFM_YTD_SGN = "PRFM_YTD_SGN";
	public static final String PRFM_YTD_PCT = "PRFM_YTD_PCT";
	public static final String BCHMK_6MO_SGN = "BCHMK_6MO_SGN";
	public static final String BCHMK_6MO_PCT = "BCHMK_6MO_PCT";
	public static final String BCHMK_SNC_LNCH_SGN = "BCHMK_SNC_LNCH_SGN";
	public static final String BCHMK_SNC_LNCH_PCT = "BCHMK_SNC_LNCH_PCT";
	public static final String BCHMK_YTD_SGN = "BCHMK_YTD_SGN";
	public static final String BCHMK_YTD_PCT = "BCHMK_YTD_PCT";
	public static final String BCHMK_NAME = "BCHMK_NAME";
	public static final String FUND_TYPE_CDE = "FUND_TYPE_CDE";
	public static final String FUND_CAT_CDE = "FUND_CAT_CDE";
	public static final String NXT_SCRIB_DEAL_DT = "NXT_SCRIB_DEAL_DT";
	public static final String NXT_SCRIB_CTOFF_DT = "NXT_SCRIB_CTOFF_DT";
	public static final String AMCM_IND = "AMCM_IND";
	public static final String OFFER_START_DT = "OFFER_START_DT";
	public static final String OFFER_START_TM = "OFFER_START_TM";
	public static final String OFFER_END_DT = "OFFER_END_DT";
	public static final String OFFER_END_TM = "OFFER_END_TM";
	public static final String MIP_IND = "MIP_IND";
	public static final String FUND_CLASS_CDE = "FUND_CLASS_CDE";
	public static final String SW_GRP = "SW_GRP";
	public static final String FUND_STUS_CDE = "FUND_STUS_CDE";
	public static final String DIV_FLAG = "DIV_FLAG";
	public static final String DIV_MTHD = "DIV_MTHD";
	public static final String CHNL_ALOW_CAS_IND = "CHNL_ALOW_CAS_IND";
	public static final String CHNL_ALOW_PFSFP_IND = "CHNL_ALOW_PFSFP_IND";
	public static final String CHNL_ALOW_PFSPD_IND = "CHNL_ALOW_PFSPD_IND";
	public static final String CHNL_ALOW_PWSPB_IND = "CHNL_ALOW_PWSPB_IND";
	public static final String CHNL_ALOW_PWSCB_IND = "CHNL_ALOW_PWSCB_IND";
	public static final String CHNL_ALOW_PIB_IND = "CHNL_ALOW_PIB_IND";
	public static final String CHNL_ALOW_WEX_IND = "CHNL_ALOW_WEX_IND";
	public static final String CHNL_ALOW_IVRS_IND = "CHNL_ALOW_IVRS_IND";
	public static final String dummy_FUND_CDE = "dummy_FUND_CDE";
	public static final String FUND_HOUSE_CDE = "FUND_HOUSE_CDE";
	public static final String INVST_INIT_MIN_CCY = "INVST_INIT_MIN_CCY";
	public static final String INVST_ICT_MIN_AMT = "INVST_ICT_MIN_AMT";
	public static final String RDEM_MIN_AMT = "RDEM_MIN_AMT";
	public static final String UNIT_ARDM_MIN_NUM = "UNIT_ARDM_MIN_NUM";
	public static final String FUND_ARDM_MIN_AMT = "FUND_ARDM_MIN_AMT";
	public static final String INVST_MIP_MIN_AMT = "INVST_MIP_MIN_AMT";
	public static final String INVST_INCR_MIP_AMT = "INVST_INCR_MIP_AMT";
	public static final String FN_MIP_RDM_MIN_AMT = "FN_MIP_RDM_MIN_AMT";
	public static final String UT_MIP_ARD_MIN_NUM = "UT_MIP_ARD_MIN_NUM";
	public static final String UT_MIP_ARD_MIN_AMT = "UT_MIP_ARD_MIN_AMT";
	public static final String CHRG_INIT_PCT = "CHRG_INIT_PCT";
	public static final String DSCNT_MAX_PCT = "DSCNT_MAX_PCT";
	public static final String NXT_HLDAY_DT = "NXT_HLDAY_DT";
	public static final String NXT_RDEM_CTOFF_DT = "NXT_RDEM_CTOFF_DT";
	// new column
	public static final String SCHEM_CHRG_CDE = "SCHEM_CHRG_CDE";

	public static final String CLOSE_END_FUND_IND = "CLOSE_END_FUND_IND";
	public static final String UT_PRC_PROD_LNCH_AMT = "UT_PRC_PROD_LNCH_AMT";
	public static final String FUND_AMT = "FUND_AMT";
	public static final String INVST_INCRM_MIN_AMT = "INVST_INCRM_MIN_AMT";
	public static final String UT_RDM_MIN_NUM = "UT_RDM_MIN_NUM";
	public static final String RDM_MIN_AMT = "RDM_MIN_AMT";
	public static final String UT_RTAIN_MIN_NUM = "UT_RTAIN_MIN_NUM";
	public static final String FUND_RTAIN_MIN_AMT = "FUND_RTAIN_MIN_AMT";
	public static final String SUPT_MIP_IND = "SUPT_MIP_IND";
	public static final String INVST_MIP_INCRM_MIN_AMT = "INVST_MIP_INCRM_MIN_AMT";
	public static final String UT_MIP_RDM_MIN_NUM = "UT_MIP_RDM_MIN_NUM";
	public static final String RDM_MIP_MIN_AMT = "RDM_MIP_MIN_AMT";
	public static final String UT_MIP_RTAIN_MIN_NUM = "UT_MIP_RTAIN_MIN_NUM";
	public static final String FUND_MIP_RTAIN_MIN_AMT = "FUND_MIP_RTAIN_MIN_AMT";
	public static final String RTRN_GURNT_PCT = "RTRN_GURNT_PCT";
	public static final String CHRG_INIT_SALES_PCT = "CHRG_INIT_SALES_PCT";
	public static final String CHRG_FUND_SW_PCT = "CHRG_FUND_SW_PCT";
	public static final String OFFER_START_DT_TM = "OFFER_START_DT_TM";
	public static final String OFFER_END_DT_TM = "OFFER_END_DT_TM";
	public static final String PAY_CASH_DIV_IND = "PAY_CASH_DIV_IND";
	public static final String HLDAY_FUND_NEXT_DT = "HLDAY_FUND_NEXT_DT";
	public static final String SCRIB_CTOFF_NEXT_DT_TM = "SCRIB_CTOFF_NEXT_DT_TM";
	public static final String RDM_CTOFF_NEXT_DT_TM = "RDM_CTOFF_NEXT_DT_TM";
	public static final String DEAL_NEXT_DT = "DEAL_NEXT_DT";
	public static final String DIV_DCLR_DT = "DIV_DCLR_DT";
	public static final String DIV_PYMT_DT = "DIV_PYMT_DT";
	public static final String AUTO_SWEEP_FUND_IND = "AUTO_SWEEP_FUND_IND";
	public static final String SPCL_FUND_IND = "SPCL_FUND_IND";
	public static final String INSU_LINK_UT_TRST_IND = "INSU_LINK_UT_TRST_IND";
	public static final String PROD_TRMT_DT = "PROD_TRMT_DT";
	public static final String FUND_SW_IN_MIN_AMT = "FUND_SW_IN_MIN_AMT";
	public static final String FUND_SW_OUT_MIN_AMT = "FUND_SW_OUT_MIN_AMT";
	public static final String FUND_SW_OUT_RTAIN_MIN_AMT = "FUND_SW_OUT_RTAIN_MIN_AMT";
	public static final String UT_SW_OUT_RTAIN_MIN_NUM = "UT_SW_OUT_RTAIN_MIN_NUM";
	public static final String FUND_SW_OUT_MAX_AMT = "FUND_SW_OUT_MAX_AMT";
	public static final String TRAN_MAX_AMT = "TRAN_MAX_AMT";
	public static final String INCM_HANDL_OPT_CDE = "INCM_HANDL_OPT_CDE";
	public static final String AUTO_RENEW_FUND_IND = "AUTO_RENEW_FUND_IND";
	public static final String FUND_VALN_TM = "FUND_VALN_TM";
	public static final String PROD_SHORE_LOC_CDE = "PROD_SHORE_LOC_CDE";
	public static final String DIV_YIELD_PCT = "DIV_YIELD_PCT";

	/* BOND */
	public static final String ISR_BOND_NAME = "ISR_BOND_NAME";
	public static final String ISSUE_NUM = "ISSUE_NUM";
	public static final String PROD_ISS_DT = "PROD_ISS_DT";
	public static final String PDCY_COUPN_PYMT_CDE = "PDCY_COUPN_PYMT_CDE";
	public static final String COUPN_ANNL_RATE = "COUPN_ANNL_RATE";
	public static final String COUPN_EXT_INSTM_RATE = "COUPN_EXT_INSTM_RATE";
	public static final String PYMT_COUPN_NEXT_DT = "PYMT_COUPN_NEXT_DT";
	public static final String FLEX_MAT_OPT_IND = "FLEX_MAT_OPT_IND";
	public static final String INT_IND_ACCR_AMT = "INT_IND_ACCR_AMT";
	public static final String PROD_BOD_LOT_QTY_CNT = "PROD_BOD_LOT_QTY_CNT";
	public static final String MTUR_EXT_DT = "MTUR_EXT_DT";
	public static final String SUB_DEBT_IND = "SUB_DEBT_IND";
	public static final String BOND_STAT_CDE = "BOND_STAT_CDE";
	public static final String CTRY_BOND_ISSUE_CDE = "CTRY_BOND_ISSUE_CDE";
	public static final String GRNTR_NAME = "GRNTR_NAME";
	public static final String CPTL_TIER_TEXT = "CPTL_TIER_TEXT";
	public static final String COUPN_TYPE = "COUPN_TYPE";
	public static final String COUPN_PREV_RATE = "COUPN_PREV_RATE";
	public static final String INDEX_FLT_RATE_NAME = "INDEX_FLT_RATE_NAME";
	public static final String BOND_FLT_SPRD_RATE = "BOND_FLT_SPRD_RATE";
	public static final String COUPN_CURR_START_DT = "COUPN_CURR_START_DT";
	public static final String COUPN_PREV_START_DT = "COUPN_PREV_START_DT";
	public static final String BOND_CALL_NEXT_DT = "BOND_CALL_NEXT_DT";
	public static final String INT_BASIS_CALC_TEXT = "INT_BASIS_CALC_TEXT";
	public static final String INT_ACCR_DAY_CNT = "INT_ACCR_DAY_CNT";
	public static final String INVST_SOLD_LEST_AMT = "INVST_SOLD_LEST_AMT";
	public static final String INVST_INCRM_SOLD_AMT = "INVST_INCRM_SOLD_AMT";
	public static final String SHR_BID_CNT = "SHR_BID_CNT";
	public static final String SHR_OFFR_CNT = "SHR_OFFR_CNT";
	public static final String PROD_CLOSE_BID_PRC_AMT = "PROD_CLOSE_BID_PRC_AMT";
	public static final String PROD_CLOSE_OFFR_PRC_AMT = "PROD_CLOSE_OFFR_PRC_AMT";
	public static final String BOND_CLOSE_DT = "BOND_CLOSE_DT";
	public static final String BOND_SETL_DT = "BOND_SETL_DT";
	public static final String DSCNT_MRGN_BSEL_PCT = "DSCNT_MRGN_BSEL_PCT";
	public static final String DSCNT_MRGN_BBUY_PCT = "DSCNT_MRGN_BBUY_PCT";
	public static final String PRC_BOND_RECV_DT_TM = "PRC_BOND_RECV_DT_TM";
	public static final String YIELD_OFFER_TEXT = "YIELD_OFFER_TEXT";
	public static final String COUPN_ANNL_TEXT = "COUPN_ANNL_TEXT";
	public static final String YIELD_DT = "YIELD_DT";
	public static final String YIELD_EFF_DT = "YIELD_EFF_DT";
	public static final String YIELD_BID_PCT = "YIELD_BID_PCT";
	public static final String YIELD_TO_CALL_BID_PCT = "YIELD_TO_CALL_BID_PCT";
	public static final String YIELD_TO_MTUR_BID_PCT = "YIELD_TO_MTUR_BID_PCT";
	public static final String YIELD_BID_CLOSE_PCT = "YIELD_BID_CLOSE_PCT";
	public static final String YIELD_OFFER_PCT = "YIELD_OFFER_PCT";
	public static final String YIELD_TO_CALL_OFFER_PCT = "YIELD_TO_CALL_OFFER_PCT";
	public static final String YIELD_TO_MTUR_OFFER_TEXT = "YIELD_TO_MTUR_OFFER_TEXT";
	public static final String YIELD_OFFER_CLOSE_PCT = "YIELD_OFFER_CLOSE_PCT";
	public static final String CREDIT_RTNG_AGCY_CDE = "CREDIT_RTNG_AGCY_CDE";
	public static final String CREDIT_RTNG_CDE = "CREDIT_RTNG_CDE";
	public static final String ISR_DESC = "ISR_DESC";
	public static final String SR_TYPE_CDE = "SR_TYPE_CDE";
	public static final String ISR_PLL_DESC = "ISR_PLL_DESC";
	public static final String ISR_SLL_DESC = "ISR_SLL_DESC";
	public static final String PRODUCT_IND_SIMPLE = "S";
	public static final String PRODUCT_IND_COMPLEX = "C";

	/* Property Estate */
	public static final String PRC_START_DT = "PRC_START_DT";
	public static final String PRC_END_DT = "PRC_END_DT";
	public static final String PROP_ESTAT_CDE = "PROP_ESTAT_CDE";
	public static final String PHS_BLK_INFO = "PHS_BLK_INFO";
	public static final String LVL_INFO = "LVL_INFO";
	public static final String PRC_HIGH_AMT = "PRC_HIGH_AMT";
	public static final String PRC_AVG_AMT = "PRC_AVG_AMT";
	public static final String PRC_LOW_AMT = "PRC_LOW_AMT";
	public static final String TRAN_CNT = "TRAN_CNT";

	/* Mandatory Provident Fund */
	public static final String FUND_NAME = "FUND_NAME";
	public static final String PRFM_CUM_SGN = "PRFM_CUM_SGN";
	public static final String PRFM_CUM_PCT = "PRFM_CUM_PCT";
	public static final String PRFM_UPDT_DT = "PRFM_UPDT_DT";
	public static final String PRFM_1MO_SGN = "PRFM_1MO_SGN";
	public static final String PRFM_1MO_PCT = "PRFM_1MO_PCT";

	/* Foreign Currency Holiday */
	public static final String REC_YEAR = "REC_YEAR";
	public static final String REC_MO = "REC_MO";
	public static final String DAY_MO_CNT = "DAY_MO_CNT";
	public static final String DAY_STAT_CDE = "DAY_STAT_CDE";

	public static final String YIELD_TYPE_DAILY = "D";

	/* SID Manual Upload Form */
	public static final String PROD_EXTNL_CDE = "PROD_EXTNL_CDE";
	public static final String PROD_EXTNL_TYPE_CDE = "PROD_EXTNL_TYPE_CDE";
	public static final String PROD_CONV_CDE = "PROD_CONV_CDE";
	public static final String RTRN_INTRM_PREV_PCT = "RTRN_INTRM_PREV_PCT";
	public static final String RTRN_INTRM_PAID_PREV_DT = "RTRN_INTRM_PAID_PREV_DT";
	public static final String RTRN_INTRM_PAID_NEXT_DT = "RTRN_INTRM_PAID_NEXT_DT";
	public static final String CCY_LINK_DEPST_CDE = "CCY_LINK_DEPST_CDE";
	public static final String MKT_START_DT = "MKT_START_DT";
	public static final String MKT_END_DT = "MKT_END_DT";
	public static final String YIELD_ANNL_MIN_PCT = "YIELD_ANNL_MIN_PCT";
	public static final String YIELD_ANNL_POTEN_PCT = "YIELD_ANNL_POTEN_PCT";
	public static final String ALLOW_EARLY_RDM_IND = "ALLOW_EARLY_RDM_IND";
	public static final String RDM_EARLY_DALW_TEXT = "RDM_EARLY_DALW_TEXT";
	// public static final String PRC_EARLY_RDM_PCT = "PRC_EARLY_RDM_PCT";
	public static final String RDM_EARLY_IND_AMT = "RDM_EARLY_IND_AMT";
	public static final String OFFER_TYPE_CDE = "OFFER_TYPE_CDE";
	public static final String CUST_SELL_QTA_NUM = "CUST_SELL_QTA_NUM";
	public static final String RULE_QTA_ALTMT_CDE = "RULE_QTA_ALTMT_CDE";
	public static final String BONUS_INT_CALC_TYPE_CDE = "BONUS_INT_CALC_TYPE_CDE";
	public static final String BONUS_INT_DT_TYPE_CDE = "BONUS_INT_DT_TYPE_CDE";
	public static final String CPTL_PROTC_PCT = "CPTL_PROTC_PCT";
	public static final String LNCH_PROD_IND = "LNCH_PROD_IND";
	public static final String RTRV_PROD_EXTNL_IND = "RTRV_PROD_EXTNL_IND";
	public static final String RTRN_INTRM_PAID_DT = "RTRN_INTRM_PAID_DT";
	public static final String RTRN_INTRM_PCT = "RTRN_INTRM_PCT";

	/**
	 * Prevent object construction outside of this class.
	 */
	private Const() {
		// null constructor
	}
}