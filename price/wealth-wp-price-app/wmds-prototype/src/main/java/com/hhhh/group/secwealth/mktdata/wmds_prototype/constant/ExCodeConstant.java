/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_prototype.constant;

public final class ExCodeConstant {

    /**
     * Illegal Configuration
     */
    public static final String EX_CODE_ILLEGAL_CONFIGURATION = "IllegalConfiguration";

    public static final String EX_CODE_APP_ILLEGAL_CONFIGURATION = "AppIllegalConfiguration";

    public static final String EX_CODE_TRIS_ILLEGAL_CONFIGURATION = "TrisIllegalConfiguration";
    
    public static final String EX_CODE_ETNET_ILLEGAL_CONFIGURATION = "EtnetIllegalConfiguration";
    
    public static final String EX_CODE_LABCI_ILLEGAL_CONFIGURATION = "LabciIllegalConfiguration";
    
    public static final String EX_CODE_MIDFS_ILLEGAL_CONFIGURATION = "MidfsIllegalConfiguration";

    public static final String EX_CODE_PREDSRCH_ILLEGAL_CONFIGURATION = "PredSrchIllegalConfiguration";

    /**
     * Illegal Initialization
     */
    public static final String EX_CODE_ILLEGAL_INITIALIZATION = "IllegalInitialization";

    public static final String EX_CODE_AUTHENTICATION_ILLEGAL_INITIALIZATION = "AuthenticationIllegalInitialization";

    /**
     * Authentication
     */
    public static final String EX_CODE_AUTHENTICATION_ENCRYPTION_FAILED = "AuthenticationEncryptionFailed";

    public static final String EX_CODE_AUTHENTICATION_DECRYPTION_FAIELD = "AuthenticationDecryptionFailed";

    public static final String EX_CODE_NO_AUTHORIZED_ACCESS = "NoAuthorizedAccess";

    /**
     * Undefined
     */
    public static final String EX_CODE_UNDEFINED = "Undefined";

    public static final String EX_CODE_DB_UNKNOWN_ERROR = "DBUnknownError";

    public static final String EX_CODE_DB_PERSIST_FAILED = "DBPersistFailed";

    public static final String EX_CODE_NO_DOCUMENT_IS_AVAILABLE = "NoDocumentIsAvailable";

    /**
     * Vendor
     */
    public static final String EX_CODE_ACCESS_VENDOR_ERROR = "AccessVendorError";

    public static final String EX_CODE_ACCESS_PREDSRCH_ERROR = "AccessPredSrchError";


	public static final String EX_CODE_QUOTE_DETAIL_ONLY_SUPPORT_ONE_PRODALTNUM = "QuoteDetailOnlySupportOneProdAltNum";

    public static final String EX_CODE_ACCESS_TRIS_ERROR = "AccessTrisError";

    public static final String EX_CODE_ACCESS_LABCI_ERROR = "AccessLabciError";

    public static final String EX_CODE_ACCESS_ETNET_ERROR = "AccessEtnetError";
	
    public static final String EX_CODE_ACCESS_Midfs_ERROR = "AccessMidfsError";

    public static final String EX_CODE_ACCESS_ENCRYPTOR_ERROR = "AccessEncryptorError";

    public static final String EX_CODE_INVALID_RESPONSE = "InvalidResponse";

    public static final String EX_CODE_INVALID_REQUEST = "InvalidRequest";

    public static final String EX_CODE_PREDSRCH_INVALID_RESPONSE = "PredSrchInvalidResponse";

    public static final String EX_CODE_PREDSRCH_EMPTY_RESPONSE = "PredSrchEmptyResponse";

	public static final String EX_CODE_SITE_NOT_SUPPORT = "SiteNotSupport";

    public static final String EX_CODE_TRIS_INVALID_RESPONSE = "TrisInvalidResponse";
    
    public static final String EX_CODE_ETNET_INVALID_RESPONSE = "EtnetInvalidResponse";

    public static final String EX_CODE_ETNET_AUTHENTICATION_FAIL = "EtnetAuthenticationFail";

    public static final String EX_CODE_ETNET_INVALID_TOKEN = "EtnetInvalidToken";

    public static final String EX_CODE_ETNET_SERVER_ERROR = "EtnetServerError";

    public static final String EX_CODE_ETNET_INVALID_PARAM = "EtnetInvalidParam";

    public static final String EX_CODE_ETNET_UNDERFINED_ERROR= "EtnetUndefinedError";

    public static final String EX_CODE_LABCI_INVALID_RESPONSE = "LabciInvalidResponse";

    public static final String EX_CODE_LABCI_PORTAL_INVALID_TOKEN = "LabciPortalInvalidToken";

    public static final String EX_CODE_LABCI_PORTAL_INVALID_REQUEST = "LabciPortalInvalidRequest";

    public static final String EX_CODE_TRIS_INVALID_TOKEN = "TrisInvaildToken";

    public static final String EX_CODE_TRIS_PERMISS_DENIED = "TrisPermissDenied";

    public static final String EX_CODE_TRIS_INVAILD_REQUEST = "TrisInvaildRequest";

    public static final String EX_CODE_TRIS_UNDEFINED_ERROR = "TrisUndefinedError";

    public static final String EX_CODE_MIDFS_INVALID_RESPONSE = "MidfsInvalidResponse";
    
    public static final String EX_CODE_MIDFS_REQUEST_PRODALTNUM_MORE_THAN_20 = "MidfsRequestProdAltNumMoreThan20";

    public static final String EX_CODE_REQUEST_PARAMETER_ERROR = "RequestParameterError";

    public static final String EX_CODE_REQUEST_NOTMATCH_ERROR = "RegEx";

    public static final String EX_CODE_REQUEST_PARAMETER_NOT_SUPPORT = "RequestParameterNotSupport";

    public static final String EX_CODE_NO_AVAILABLE_SERVICE_MATCHED_ERROR = "NoAvailableServiceMatched";

    /**
     * Common Exception
     */
    public static final String EX_CODE_INPUT_PARAMETER_INVALID = "InputParameterInvalid";

    public static final String EX_CODE_XSS_SECURITY_ERR = "XssSecurityErr";

    public static final String EX_CODE_SERVICE_NO_AVAILABLE = "ServiceNoAvailable";

    public static final String EX_CODE_DB_DATA_UNAVAILABLE = "DBDataUnavailable";

    public static final String EX_CODE_NORECORDFOUND = "NoRecordFound";
    
    public static final String EX_CODE_HEADER_FUNCTIONID_INVALID = "HeaderFunctionIdInvalid";
    
    public static final String EX_CODE_DB_NO_RECORD = "DBNoRecord";

    /**
     * Predictive Search Exception
     */
    
    public static final String EX_CODE_SFTPCONNECTERR = "SftpConnectErr";
    
    public static final String EX_CODE_VOLUMESERVICEERR = "VolumeServiceErr";
    
    public static final String EX_CODE_FILEVERIFYERR = "FileVerifyErr";
    
    public static final String EX_CODE_DATAPARSINGERR = "DataParsingErr";

    public static final String EX_CODE_DATAUNAVAILABLE = "DataUnavailable";

    public static final String EX_CODE_QUERYSTRINGINVALID = "QueryStringInvalid";

    public static final String INPUT_PARAMETER_INVALID = "InputParameterInvalid";
    
    public static final String EX_CODE_COPYTOLOCAL = "CopyToLocalErr";
    
    public static final String ERRORMSG_VOLUMESERVICECONFIGERR = "VolumeServiceConfigErr";
    
	public static final String EX_CODE_SFTP_CONFIG_ERROR = "SFTPConfigError";
	
	public static final String INDEX_DATA_NOT_EXIST = "IndexDataNotExists";

    /**
     * Mstar Exception
     */
    public static final String EX_CODE_Mstar_TIMEOUT = "MstarTimeout";

    public static final String EX_CODE_Mstar_NO_DATA = "MstarNoData";

    public static final String EX_CODE_Mstar_UNDEFINED = "MstarUndefinedError";

    public static final String EX_CODE_Mstar_INVALID_RESPONSE = "MstarInvalidResponse";

    public static final String EX_CODE_Mstar_UNMARSHAL_FAIL = "MstarUnmarshalFail";

    /**
     * WPC WebService Exception
     */
    public static final String EX_CODE_WPC_INVALID_REQUEST = "WpcWSInvalidRequest";

    public static final String EX_CODE_WPC_INVALID_RESPONSE = "WpcWSInvalidResponse";

    public static final String EX_CODE_WPC_UNDEFINED = "WpcWSUndefinedError";

    /**
     * System Exception
     */
    public static final String EX_CODE_SYSTEM_ERROR = "SystemError";

    public static final String EX_CODE_SYSTEM_INIT_ERROR = "SystemInitError";
    
    /** The Constant DB_DATA_UNAVAILABLE. */
    public static final String DB_DATA_UNAVAILABLE = "DBDataUnavailable";
    /**
     * Cache for eid
     */
    public static final String EX_CODE_CACHE_NO_EID_FOUND = "CacheNoEIDFound";
    
    public static final String EX_CODE_CACHE_BAD_REQUEST = "CacheBadRequest";
    
    public static final String EX_CODE_CACHE_UNCACHED_RECORD = "CacheUncachedRecord";
    
    public static final String EX_CODE_CACHE_INVALID_HTTP_STATUS = "CacheInvalidHttpStatus";

    private ExCodeConstant() {
    }

    public static final String EX_CODE_RBP_TRADE_HOUR_ERROR = "AccessRbpServerError";
    
    /**
	 * labci Exception
	 */
	public static final String EX_CODE_LABCI_SERVER_ERROR = "LabciServerError";
	
	public static final String EX_CODE_LABCI_STOCK_NOT_FOUND = "LabciStockNotFound";
	
	public static final String EX_CODE_LABCI_INVALID_REQUEST = "LabciInvalidRequest";
	
	public static final String EX_CODE_LABCI_UNDEFINED_ERROR = "LabciUndefinedError";
	
	/**
	 * midfs Exception
	 */
	public static final String EX_CODE_MIDFS_INVALID_REQUEST = "MidfsInvalidRequest";
	
	public static final String EX_CODE_MIDFS_SERVER_ERROR = "MidfsServerError";
	
	public static final String EX_CODE_MIDFS_STOCK_NOT_FOUND = "MidfsStockNotFound";
	
	public static final String EX_CODE_MIDFS_UNDEFINED_ERROR = "MidfsUndefinedError";
	
	public static final String EX_CODE_DB_ACTION_FAIL = "DBActionFail";

	public static final String EX_CODE_CACHE_BAD_RESPONSE = "CacheBadResponse";
	
	public static final String EX_CODE_QUOTES_PRODCDEALTCLASSCDE_ONLY_SUPPORT_M_CODE =
        "QuotesRequestProdCdeAltClassCdeOnlySupportMCode";
}
