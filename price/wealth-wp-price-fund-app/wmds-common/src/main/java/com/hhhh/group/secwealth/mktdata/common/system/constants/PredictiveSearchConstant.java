/*
 */
package com.hhhh.group.secwealth.mktdata.common.system.constants;

public interface PredictiveSearchConstant {

    // The package for JAXB paring
    public static final String REFDATAPACKAGE = "com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData";

    // The keyword for reference data file name
    public static final String REFERENCEDATA = "ReferenceData";

    // It's update type to load data
    public static final String OPERATION_TYPE_UDPATE = "update";

    // It's create type to load data
    public static final String OPERATION_TYPE_CREATE = "create";

    // The suffix of out file
    public static final String OUT_FILE = ".out";

    // The suffix of search field
    public static final String ANALYZED_FIELD = "_analyzed";

    // Tr code
    public static final String PROD_ALT_CLASS_CDE_T = "T";

    // Primary code
    public static final String PROD_ALT_CLASS_CDE_P = "P";

    // Ric Code
    public static final String PROD_ALT_CLASS_CDE_R = "R";

    // Market code
    public static final String PROD_ALT_CLASS_CDE_M = "M";

    // WPC code
    public static final String PROD_ALT_CLASS_CDE_W = "W";

    // Performance ID
    public static final String PROD_ALT_CLASS_CDE_O = "O";

    // Product descriptor
    public static final String PRODUCTDESCRIPTOR = "{-1}";

    // The symbol which will be replaced by market Code
    public static final String MARKETCODE = "{0}";

    // The symbol which will be replaced by product name
    public static final String PRODUCTNAME = "{1}";

    // The symbol which will be replaced by exchange
    public static final String EXCHANGE = "{2}";

    // The error key for data parsing error
    public static final String ERRORMSG_DATAPARSINGERR = "DataParsingErr";

    // The error key for data unavailable error
    public static final String ERRORMSG_DATAUNAVAILABLE = "DataUnavailable";

    // The error key for no record found error
    public static final String ERRORMSG_NORECORDFOUND = "NoRecordFound";

    // The error key for Query String Invalid error
    public static final String ERRORMSG_QUERYSTRINGINVALID = "QueryStringInvalid";

    // The error key for wpc data verify error
    public static final String ERRORMSG_VOLUMESERVICEERR = "VolumeServiceErr";

    // The error key for wpc data verify error
    public static final String ERRORMSG_DATAVERIFYERR = "DataVerifyErr";

    // The id value for health check object
    public static final String SYMBOL_HEALTHCHECK = "HealthCheck";

    // The digit of version time stamp
    public final static int VERSION_TIMESTAMP_DIGIT = 12;

    // The length of out file
    public final static int OUT_FILE_LENGTH = 5;

    // The length of constant file
    public final static int CONSTANT_FILE_LENGTH = 6;
}
