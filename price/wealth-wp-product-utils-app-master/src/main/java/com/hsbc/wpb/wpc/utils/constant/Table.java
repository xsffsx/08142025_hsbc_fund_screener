package com.dummy.wpb.wpc.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Table {
    public static Table getInstance(){
        return new Table();
    }
    public static final String TB_PROD = "TB_PROD";
    public static final String PROD_PRC_HIST = "PROD_PRC_HIST";
    public static final String CDE_DESC_VALUE = "CDE_DESC_VALUE";
    public static final String CDE_DESC_VALUE_CHANL_REL = "CDE_DESC_VALUE_CHANL_REL";
    public static final String ASET_VOLTL_CLASS_CHAR = "ASET_VOLTL_CLASS_CHAR";
    public static final String ASET_VOLTL_CLASS_CORL = "ASET_VOLTL_CLASS_CORL";
    public static final String PROD_TYPE_FIN_DOC = "PROD_TYPE_FIN_DOC";
    public static final String PROD_SUBTP_FIN_DOC = "PROD_SUBTP_FIN_DOC";
    public static final String PROD_TYPE_STAF_LIC_CHECK = "PROD_TYPE_STAF_LIC_CHECK";
    public static final String PROD_SUBTP_STAF_LIC_CHECK = "PROD_SUBTP_STAF_LIC_CHECK";
    public static final String PROD_ATRIB_MAP = "PROD_ATRIB_MAP";
    public static final String CHANL_COMN_CDE = "CHANL_COMN_CDE";
    public static final String TB_PROD_USER_DEFIN_EXT_FIELD = "TB_PROD_USER_DEFIN_EXT_FIELD";
    public static final String TB_PROD_USER_DEFIN_EG_EXT_FIEL = "TB_PROD_USER_DEFIN_EG_EXT_FIEL";
    public static final String TB_PROD_USER_DEFIN_OP_EXT_FIEL = "TB_PROD_USER_DEFIN_OP_EXT_FIEL";
    public static final String TB_PROD_USER_DEFIN_FIELD = "TB_PROD_USER_DEFIN_FIELD";
    public static final String PROD_PERFM = "PROD_PERFM";
    public static final String FIN_DOC = "FIN_DOC";
    public static final String TB_PROD_TYPE_CHANL_ATTR ="TB_PROD_TYPE_CHANL_ATTR";
    public static final String PROD_TYPE_FIN_DOC_TYPE_REL ="PROD_TYPE_FIN_DOC_TYPE_REL";
    public static final String FIN_DOC_HIST = "FIN_DOC_HIST";
    public static final String PEND_APROVE_TRAN = "PEND_APROVE_TRAN";
    public static final String SYS_PARM = "SYS_PARM";
    public static final String FIN_DOC_PARM = "FIN_DOC_PARM";
    public static final String SYS_ACTV_LOG  = "SYS_ACTV_LOG";
    public static final String LOG_EQTY_LINK_INVST  = "LOG_EQTY_LINK_INVST";
    public static final String PROD_NAME_SEQS  = "PROD_NAME_SEQS";
    public static final String FIN_DOC_UPLD  = "FIN_DOC_UPLD";
}
