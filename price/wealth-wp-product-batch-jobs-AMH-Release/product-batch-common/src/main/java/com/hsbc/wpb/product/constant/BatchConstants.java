package com.dummy.wpb.product.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchConstants {
    public static final String INDICATOR_YES = "Y";
    public static final String INDICATOR_NO = "N";
    public static final String ACTIVE = "A";
    public static final String CLOSED_FROM_SUBSCRIPTION = "C";
    public static final String DELISTED = "D";
    public static final String EXPIRED = "E";
    public static final String NORMAL = "N";
    public static final String PENDING = "P";
    public static final String SUSPENDED = "S";
    public static final String TERMINATED = "T";
    public static final String COMMA_CHAR = ",";
    public static final String UNDERLINE = "_";
    public static final String FILE_SUFFIX_ACK = "ack";
    public static final String FILE_SUFFIX_BAK = "bak";
    public static final String FILE_SUFFIX_PDF = "pdf";

    public static final String PRODUCT_STATUS_ACTIVE = "A";
    public static final String PRODUCT_STATUS_DELISTED = "D";
    public static final String PRODUCT_STATUS_PENDING = "P";

    /* Reference Data Type Code */
    public static final String FUND_HSE_CDE_REF_TYPE_CDE = "FUNDHSEMAP";
    public static final String ESG_SI_CLASS_CDE_REF_TYPE_CDE = "ESGSICLASS";
    public static final String SANCTION_RULE_REF_TYPE_CDE = "SANCTIONRULE";

    /* Underlying Asset Class Code */
    public static final String EQUITY = "EQ";
    public static final String FIXED_INCOME = "FI";
    public static final String FOREIGN_CURRENCIES = "FX";
    public static final String HYBRID = "HY";
    public static final String SECURITY = "SEC";
    public static final String WARRANT = "WRTS";
    public static final String UNIT_TRUST = "UT";
    public static final String BOND_CD = "BOND";
    public static final String DEPOSIT_PLUS = "DPS";
    public static final String STRUCTURE_INVESTMENT_DEPOSIT = "SID";
    public static final String EQUITY_LINKED_INVESTMENT = "ELI";
    public static final String STRUCTURE_NOTE = "SN";
    public static final String MANDATORY_PROVIDENT_FUND = "MPF";
    public static final String CREATE = "CREATE";
    public static final String SEPARATOR_OF_PROD_TYPE_AND_MARKET = "~";
    public static final String BATCH_GFIX_CONFIG_FILE = "/jobs/wpc_batch_gfix.xml";

    public static final String BATCH_DEBUGGER = "DEBUGGER.com.dummy.esf.batch.logging";
    public static final String BATCH_ELI_ERROR_RECORDER = "ERROR_ELI_RECORDER";
    public static final String HK = "HK";
    public static final String HBAP = "HBAP";
    public static final String dummy = "dummy";
    public static final String GFIX_EXPECTED_MIN_SUBSCRIBE_BOND = "GFIX.SUB.BOND.MIN.NUM";
    public static final String GFIX_COMMAND_SCRIPT = "GFIX.CMD.Script";
    public static final String GFIX_COMMAND_SUBSCRIBE = "GFIX.CMD.PARM.SUB";
    public static final String GFIX_COMMAND_UNSUBSCRIBE = "GFIX.CMD.PARM.UNSUB";
    public static final String GFIX_MESSAGE_SESSION_ID = "GFIX.MSG.SESSION.ID";
    public static final String GFIX_MESSAGE_REQUEST_TYPE = "GFIX.MSG.REQ.TYPE";
    public static final String GFIX_MESSAGE_SENDER = "GFIX.MSG.SENDER";
    public static final String GFIX_MESSAGE_TARGET = "GFIX.MSG.TARGET";
    public static final String GFIX_MESSAGE_ON_BEHALF_OF = "GFIX.MSG.ON.BEHALF.OF";
    public static final String GFIX_MESSAGE_ON_BEHALF_OF_CMB = "GFIX.MSG.ON.BEHALF.OF.CMB";
    public static final String BATCH_ELI_HOSTNAME = "ELIHOSTNAME";
    public static final String PRODUCT_TYPE_CATEGORY = "PPN";
    public static final String DOT = ".";

    public static final String PROPKEY_IBONDREFDATA = "refdata.ibondlist";

    public static final String PROPKEY_IBONDREFDATA_CMB = "refdata.ibondlist.cmb";
    public static final String DATA_REF_TYPE_CDE_IBONDLIST = "NEGYIELDBOND";
    public static final String PROD_TYPE_CDE_REF_TYPE_CDE = "PRODTYP";
    public static final String PROD_SUBTP_CDE_REF_TYPE_CDE = "PRODSUBTP";
    public static final String DATA_REF_TYPE_CDE_CALLFREQUENCY = "PDCYCAL";
    public static final String DATA_REF_TYPE_CDE_KNOCKINFREQUENCY = "PDCYKNOCKIN";
    public static final String DATA_REF_TYPE_CDE_COUPONFREQUENCY = "SPFREQCDE";
    public static final String MKT_INVST_CDE_REF_TYPE_CDE = "IVSTMKT";
    public static final String BASE_CCY_CDE_REF_TYPE_CDE = "BASECCY";
    public static final String MORNING_STAR = "MS";
    public static final String RBT = "RBT";
    public static final String UNDL_ASET_CDE_REF_TYPE_CDE = "AC";
    public static final String SYS_CDE_GHFISDW = "GHFISDW";
    public static final String SYS_CDE_GHFIBONDCSV = "GHFIBONDCSV";
    public static final String SYS_CDE_GHFIGSOPS_CE = "GHFIGSOPS.CE";
    public static final String SYS_CDE_AMHGSOPS_CE = "AMHGSOPS.CE";
    public static final String SYS_CDE_AMHGSOPS_PD = "AMHGSOPS.PD";
    public static final String SYS_CDE_AMHGSOPS_AS = "AMHGSOPS.AS";
    public static final String SI_I = "SI_I";
    public static final String ALLOW_BUY_PROD_IND = "ALLOW_BUY_PROD_IND";
    public static final String ALLOW_SELL_MIP_PROD_IND = "ALLOW_SELL_MIP_PROD_IND";
    public static final String ALLOW_SELL_PROD_IND = "ALLOW_SELL_PROD_IND";
    public static final String ALLOW_SW_IN_PROD_IND = "ALLOW_SW_IN_PROD_IND";
    public static final String ALLOW_SW_OUT_PROD_IND = "ALLOW_SW_OUT_PROD_IND";

    public static final String CHANNEL_CDE_SRBPI = "SRBPI";
    public static final String RESTRICTED_NATIONALITY = "N";
    public static final String RESTRICTED = "R";

    public static final String FILE_TYPE_FULLSET = "FULLSET";
    public static final String FILE_TYPE_DELTA = "DELTA";

    public static final String FILENAME_DATETIME_PATTERN = "yyyyMMddHHmmss";
    public static final String BOND_ISSUER_REF_TYPE_CDE = "BONDISSUER";
    public static final String BOND_ISSUER_GUNTR_REF_TYPE_CDE = "ISSUERGRNTR";
    public static final String BOND_GUNTR_REF_TYPE_CDE = "BONDGRNTR";
    public static final String THOMSON_REUTERS_COUPON_FREQ = "TRCOUPONFREQ";
    public static final String THOMSON_REUTERS_COUPON_TYPE = "TRCOUPONTYPE";
    public static final String EGRESS_GSOPSD_SEQ = "EGRESS.GSOPSD.SEQ";
    public static final String EGRESS_GSOPSD_LAST_SUCCESSFUL_DT_TM = "EGRESS.GSOPSD.DAC.PRODUCT.LAST.SUCCESSFUL.DT.TM";
    public static final String Q_CODE_LAST_REGISTER_DT_TM = "Q.CODE.LAST.REGISTER.DT.TM";
    // Reuters TRIS
    public static final String REUTERS_PRICE_STATUS_POPULATED = "P";
    public static final String REUTERS_PRICE_STATUS_READY = "R";

    public static final String PROCESSED_FILE_NAMES = "processedFileNames";
    public static final String EXTEND_FIELDS = "ext";
    public static final String EXTEND_EG_FIELDS = "extEg";
    public static final String EXTEND_OP_FIELDS = "extOp";
    public static final String USER_DEFINED_FIELDS = "udf";
    public static final String XML = "xml";
    public static final String CSV = "csv";
    public static final String EXCEPTION_TITLE = "Exception: ";
}
