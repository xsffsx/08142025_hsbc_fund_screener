/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.constant;

public final class Constant {

    /**
     * THREAD_INVISIBLE
     */
    public static final String THREAD_INVISIBLE_SITE = "SITE";

    public static final String THREAD_INVISIBLE_RAW_NAME_ID = "NAME_ID";

    public static final String THREAD_INVISIBLE_ENCRYPTED_CUSTOMER_ID = "ENCRYPTED_CUSTOMER_ID";

    public static final String THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER = "RESPONSE_TRIS_FIELD_MAPPER";

    public static final String THREAD_INVISIBLE_RESPONSE_LABCI_FIELD_MAPPER = "RESPONSE_LABCI_FIELD_MAPPER";

    public static final String THREAD_INVISIBLE_PREDSRCH_RESPONSE = "PREDSRCH_RESPONSE";

    public static final String THREAD_INVISIBLE_MARKET = "MARKET";

    public static final String THREAD_INVISIBLE_DELAY = "DELAY";

    public static final String THREAD_INVISIBLE_SYMBOL = "SYMBOL";

    public static final String THREAD_INVISIBLE_PRODUCTTYPE = "PRODUCTTYPE";

    public static final String THREAD_INVISIBLE_M_CODE_LIST = "MCODELIST";

    public static final String THREAD_INVISIBLE_LOCALE_INDEX = "LOCALE_INDEX";

    public static final String THREAD_INVISIBLE_SAME_SECTOR_NUM = "SAME_SECTOR_NUM";

    public static final String THREAD_INVISIBLE_QUOTES_MESSAGES = "QUOTES_MESSAGES";

    public static final String THREAD_INVISIBLE_EXCHANGE_CODE = "EXCHANGE_CODE";

    public static final String THREAD_INVISIBLE_PRODUCT_KEY = "PRODUCT_KEY";

    public static final String THREAD_INVISIBLE_COMMON_HEADER = "COMMON_HEADER";

    public static final String THREAD_INVISIBLE_QUOTES_REQUEST = "QUOTES_REQUEST";

    public static final String THREAD_INVISIBLE_QUOTES_LABCI_REQUEST = "LABCI_REQUEST";

    public static final String THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST = "TOP_TEN_MOVERS_REQUEST";

    public static final String THREAD_INVISIBLE_IS_SKIP = "IS_SKIP";

    public static final String THREAD_INVISIBLE_PERFORM_OVER_MUTI_TIME_CONVERTOR_MSTAR = "performOverMutiTimeConvertor_MSTAR";

    public static final String THREAD_INVISIBLE_ALTNUM_EXCHNAGE = "AltnumExchange";

    public static final String THREAD_INVISIBLE_NEED_AGREEMENT_CHECKING = "NEED_AGREEMENT_CHECK";

    public static final String THREAD_INVISIBLE_AGREEMENT_CHECK_RESULT = "AGREEMENT_CHECK_RESULT";

    public static final String THREAD_INVISIBLE_MARKET_HOUR_CHECK_RESULT = "MARKET_HOUR_CHECK_RESULT";

    public static final String ONLINE_UPDATION = "ONLINE";

    public static final String SUBSCRIBERTYPE_PROF = "PROF";

    public static final String SUBSCRIBERTYPE = "subscriberType";

    public static final String TRADE_DAY_COUNT = "tradeDayCount";

    public static final String SERVICE_PROD_KEYS = "serviceProductKeys";

    public static final String MISSING_PROD_KEYS = "missingProductKeys";

    public static final String CONTAINED_PROD_KEYS = "containedProductKeys";

    /**
     * DATA_TYPE
     */
    public static final String DATE_FORMAT_MIDFS = "dd-MMM-yyyyHH:mm:ss";
    public static final String DATA_TYPE_STRING = "java.lang.String";

    public static final String DATA_TYPE_BIGDECIMAL = "java.math.BigDecimal";

    public static final int TRIS_TOKEN_DURATION_MINUTES = 15; // 15 minutes

    /**
     * DATE_FORMAT
     */
    public static final String DATEFORMAT_DDMMMYYYYHHMMSSSS = "dd-MMM-yyyy HH:mm:ss.SS";

    public static final String DATEFORMAT_DDMMYYYHHMM = "dd MMM yyyy'T'HH:mm";

    public static final String DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String DATE_FORMAT_TRIS_TOKEN = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_FOT_LABCI = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String DATE_FORMAT_TRIS_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";

    public static final String DATE_FORMAT_TRIS_ISO8601_V2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String DATE_FORMAT_YYY_MM_DD_T_HH_MM_SS_SSS_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * TIMEZONE
     */
    public static final String TIMEZONE_TRIS_ISO8601 = "UTC";

    public static final String TIMEZONE = "GMT";

    public static final String YEAR_PERIOD = "Y";

    public static final String MONTH_PERIOD = "M";

    public static final String DAY_PERIOD = "D";


    /**
     * PRODUCT CODE ALT CLASS
     */
    public static final String PROD_CDE_ALT_CLASS_CODE_M = "M";

    public static final String PROD_CDE_ALT_CLASS_CODE_P = "P";

    public static final String PROD_CDE_ALT_CLASS_CODE_R = "R";

    public static final String PROD_CDE_ALT_CLASS_CODE_T = "T";

    public static final String PROD_CDE_ALT_CLASS_CODE_O = "O";


    /**
     * APP
     */
    public static final String DEFAULT_OPTION = "DEFAULT";

    public static final String TRIS_MARKET = "tris-market";

    public static final String MIDFS_MARKET = "midfs-market";

    public static final String REAL_TIME = "real-time";

    public static final String DELAYED = "delayed";

    public static final String REALTIME = "realtime";

    public static final String UNLIMITED = "unlimited";

    public static final String QUOTE = "quote";

    public static final String ZERO = "0";

    public static final String CASE_0 = "0";

    public static final String CASE_1 = "1";

    public static final String CASE_10 = "10";

    public static final String CASE_20 = "20";

    public static final String CASE_22 = "22";

    public static final String CASE_31 = "31";

    public static final String CASE_33 = "33";

    public static final String APP_CODE_SRBPBE = "SRBPBE";

    public static final String APP_CODE_SRBP = "SRBP";

    public static final String APP_CODE_WD = "WD";

    public static final String APP_SUPPORT_ALL_EXCHANGES = "ALL";

    public static final String CODING_UTF8 = "UTF-8";

    public static final String HTTP_PARAM_TOKEN = "token";

    public static final String HTTP_PARAM_SYMBOL = "symbol";

    // BELOW ARE CONSTANTS FOR MIDFS

    /**
     * REQUEST_PARAMETER
     */
    public static final String REQUEST_PARAMETER_COMMAND_ID = "COMMAND_ID";

    public static final String REQUEST_PARAMETER_QUOTE_STOCK = "{stock}";

    public static final String REQUEST_PARAMETER_EID = "{eid}";

    public static final String REQUEST_PARAMETER_IBUSERID = "{ibuserid}";

    public static final String REQUEST_PARAMETER_OBO = "{obo}";

    public static final String REQUEST_PARAMETER_PKG_ID = "{packageID}";

    public static final String REQUEST_PARAMETER_PKG_STATUS = "{packageStatus}";

    public static final String REQUEST_PARAMETER_CHARGE_AC_STATUS = "{chargeAcStatus}";

    public static final String REQUEST_PARAMETER_CUS_SEGMENT = "{cusSegment}";

    public static final String REQUEST_PARAMETER_BONUS = "{bonus}";

    public static final String REQUEST_PARAMETER_LAST_LOGON = "{lastLogonTime}";

    /**
     * VENDOR_MIDFS
     */
    public static final String MIDFS_RESPONSE_PREFIX = "stock_";

    /**
     * ChainMapping
     *
     */
    public static final String CHAIN_TYPE = "chainType";
    public static final String RIC = "ric";
    public static final String CHAIN_CODE = "chainCode";

    /**
     * Service name mapping
     */
    public static final String DISPATCHER_ENDPOINT_BY_DEFAULT = "DEFAULT";

    public static final String DB_RECORD_UPDATE_BY_ONLINE = "ONLINE";

    /**
     * Common Request Header Key
     */
    public static final String REQUEST_HEADER_KEY_COUNTRYCODE = "X-hhhh-Chnl-CountryCode";

    public static final String REQUEST_HEADER_KEY_GROUP_MEMBER = "X-hhhh-Chnl-Group-Member";

    public static final String REQUEST_HEADER_KEY_CHANNEL_ID = "X-hhhh-Channel-Id";

    public static final String REQUEST_HEADER_KEY_LOCALE = "X-hhhh-Locale";

    public static final String REQUEST_HEADER_KEY_APP_CODE = "X-hhhh-App-Code";

    public static final String REQUEST_HEADER_KEY_SAML = "X-hhhh-Saml";

    public static final String REQUEST_HEADER_KEY_SAML3 = "X-hhhh-Saml3";

    public static final String REQUEST_HEADER_KEY_JWT = "X-hhhh-E2E-TRUST-TOKEN";

    public static final String REQUEST_HEADER_KEY_STAFF_ID = "X-hhhh-Staff-Id";

    public static final String REQUEST_HEADER_KEY_CUSTOMER_ID = "X-hhhh-Customer-Id";

    public static final String REQUEST_HEADER_KEY_FUNCTION_ID = "X-hhhh-Function-Id";

    public static final String REQUEST_HEADER_KEY_USER_ID = "X-hhhh-User-Id";

    public static final String REQUEST_HEADER_KEY_SOURCE_SYSTEM_ID ="X-hhhh-Source-System-Id";

    //App code
    public static final String APP_CODE_STB ="STB";
    public static final String APP_CODE_CMB ="CMB";


    //Channel-Id
    public static final String CHANNEL_ID_OHB ="OHB";
    public static final String CHANNEL_ID_OHI ="OHI";

    //exchange

    public static final String EXCHANGE_SEHK ="SEHK";

    private Constant() {}

}
