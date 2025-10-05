/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

public final class PredictiveSearchConstant {
	private  PredictiveSearchConstant() {
	}

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

	public static final String SYMBOL = "symbol";

	// support T code search
	public static final String KEY = "key";

	// support P code search
	public static final String PROD_CODE = "prodCode";

	public static final String PRODUCT_NAME = "productName_analyzed";

	public static final String PRIORITY = "priority";

	public static final String POPULARITY = "popularity";

	public static final String PRODUCTTYPE_WEIGHT = "productTypeWeight";

	public static final String COUNTRYTRADABLECOD_EWEIGHT = "countryTradableCodeWeight";

	public static final String COUNTRY_TRADEABLE_CODE = "countryTradableCode";

	public static final String HOUSE_VIEW_INDICATOR = "houseViewIndicator";

	public static final String HOUSE_VIEW_RECENT_UPDATE = "houseViewRecentUpdate";

	public static final String HOUSE_VIEW_RATING = "houseViewRating";

	public static final String PRODUCT_TYPE_ANALYZED = "productType";

	public static final String PRODUCT_SUBTYPE_ANALYZED = "productSubType";

	public static final String SWITCHOUTFUND_SWITCHABLEGROUP = "switchableGroups";

	public static final String SWITCHOUTFUND_ALLOWSWOUTPRODIND = "allowSwOutProdInd";

	public static final String SWITCHOUTFUND_UNSWITCHABLELIST = "unSwitchableList";

	public static final String UNSWITCHOUTFUND = "unSwitchOutFund";

	public static final String SWITCHOUTFUND = "switchOutFund";

	public static final String ALLOWSWIN_FUND = "allowSwInProdInd";

	public static  final String PRODUCT_KEY = "productKey";

	public static final String PRODUCT_TYPE_FUND = "UT";

	public static final String RESCHANNELCDE = "resChannelCde";

	public static final String RESTRONLSCRIBIND = "restrOnlScribInd";

	public static final String RESTRCMBONLSRCHIND = "restrCMBOnlSrchbInd";

	public static final String CMBPRODIND = "cmbProductInd";

	public static final String WPBPRODIND = "wpbProductInd";


	public static final String PROD_ALT_CLASS_CDE_I = "I";

	public static final String PROD_ALT_CLASS_CDE_J = "J";

	public static final String PROD_ALT_CLASS_CDE_F = "F";
	// Tr code
	public static final String PROD_ALT_CLASS_CDE_T = "T";

	// Primary code
	public static final String PROD_ALT_CLASS_CDE_P = "P";

	// Ric Code
	public static final String PROD_ALT_CLASS_CDE_R = "R";

	// Sedol Code
	public static final String PROD_ALT_CLASS_CDE_S = "S";

	// Market code
	public static final String PROD_ALT_CLASS_CDE_M = "M";

	// WPC code
	public static final String PROD_ALT_CLASS_CDE_W = "W";

	// Performance ID
	public static final String PROD_ALT_CLASS_CDE_O = "O";

	private static final String[] SEQUENCE = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"};

	public static final String SORTING_FIELD = "field";

	public static final String SORTING_OPERATOR = "operator";

	public static final String SORTING_SEQUENCE = "sequence";

	public static final String SORTING_OPERATOR_EXACT = "exact";

	public static final String SORTING_OPERATOR_CONTAINS = "contains";

	public static final String SORTING_DEFAULT = "DEFAULT";

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

	public static final String ERRORMSG_LOADWPCFILEERR = "LoadWpcFileErr";

	public static final String ERRORMSG_VOLUMESERVICECONFIGERR = "VolumeServiceConfigErr";

	public static final String INPUT_PARAMETER_INVALID = "InputParameterInvalid";

	public static final String SYSTEM_INIT_ERROR = "SystemInitError";

	// The id value for health check object
	public static final String SYMBOL_HEALTHCHECK = "HealthCheck";

	public static final String EX_CODE_ACCESS_ILAB_ERROR = "AccessIlabError";

	// The digit of version time stamp
	public static final int VERSION_TIMESTAMP_DIGIT = 12;

	// The length of out file
	public static final int OUT_FILE_LENGTH = 5;

	// The length of constant file
	public static final int CONSTANT_FILE_LENGTH = 6;

	public static String[] getSequence() {
		return PredictiveSearchConstant.SEQUENCE;
	}
}
