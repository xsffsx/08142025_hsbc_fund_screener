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
 * Class Name		FinDocConstants
 *
 * Creation Date	Feb 4, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Feb 4, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.constant;



import java.util.HashMap;


public class FinDocConstants {
    
    public static String VAL_ERROR   = "Invalid Value";
    public static String MAN_ERROR   = "Missing";
    public static String LEN_ERROR   = "Invalid Length";
    public static String FLD_ERROR   = "Invalid Values";
    public static String PRO_ERROR   = "Invalid Product";
    public static String DUP_ERROR   = "Duplicate Records";
    public static String PEN_ERROR   = "Pending Approval Record Exist";
    public static String DT_TM_ERROR = "Invalid Format";
    public static String FILE_ERROR  = "Document File Not Exist";
    public static String MDOC_ERROR  = "Mapping To Document Not Exist";

    public static String CHNL = "WPC";

    public static String MAP_VSC_1ST_HEAD = "Action";
    public static String CSV_1ST_HEAD = "Country Code";
    public static String VER_1_1 = "ver 1.1";
    public static String VER_1_1_SUB_HEAD = "Document Information";

    //DB Path
    public final static String ARCHURL = "ARCHURL";
    public final static String FSURL = "FSURL";    
    public final static String SYS_APRV = "ARPVREQ";
    public final static String SYS_ARCH = "ARCHREQ";

    public static String MSG_NAME = "FINDOCMT";

   //Default Code
    public final static String DFLT_APRV_REQ = "";
    public final static String DFLT_ARCH_REQ = "";
    /*
    public final static String DFLT_SYS_URL = WPCProperties.getProperty("FinDoc.Dflt.SysUrl");
    public final static String DFLT_FS_URL = WPCProperties.getProperty("FinDoc.Dflt.FsUrl");
    public final static String DFLT_ARCH_URL = WPCProperties.getProperty("FinDoc.Dflt.ArchUrl");
    
    //DB Record
    public static final String FDARCDATADIR = WPCProperties.getProperty("FinDoc.ArchDataDir");
    public static final String FDARCREQDIR = WPCProperties.getProperty("FinDoc.ArchReqDir");
    public static final String FDFSDATADIR = WPCProperties.getProperty("FinDoc.FsDataDir");
    public static final String FDFSACKDIR = WPCProperties.getProperty("FinDoc.FsAckDir");
     */
    public static final String FDDFLTSNDR = "#WPC_SUPPORT";
/*
    public static final String ARC_CSV = WPCProperties.getProperty("Findoc.arc.csv");
    public static final String ACK_FILE_PATH = WPCProperties.getProperty("FinDoc.ackFile.path");
    public static final String SPOM_ACK= WPCProperties.getProperty("FinDoc.spoms.ack");
    public static final String MAX_TO_PWS = WPCProperties.getProperty("FinDoc.MaxNo.PWS");
 */

    public static final String SUBTYPCDE_GN = "GN";
 /*
    //Production
    public static String[] INPUT_FILE_PREFIX = StringUtils.split(WPCProperties.getProperty("FinDoc.InFile.Prefix"), ",");    
    public static String [] MAP_INPUT_FILE_PREFIX=StringUtils.split(WPCProperties.getProperty("FinDoc.Map.InFile.Prefix"), ",");
 */

    //input excel titles
    public static final String ACTION_CDE = "Action";
    public static final String CTRY_CDE = "Country Code";
    public static final String ORGN_CDE = "Institution Code";
    public static final String DOC_TYP = "Document Type";
    public static final String DOC_STP = "Document Subtype";
    public static final String DOC_ID="Document Code";
    public static final String SYS_PARM_CDE = "System Parameter Code";
    public static final String SYS_PARM_VAL = "System Parameter Value";
    public static final String RMK = "Remarks";
    public static final String PROD_TYP = "Product Type";
    public static final String PROD_STP = "Product Subtype";
    public static final String PROD_ID = "Product Code";
    public static final String LANG = "Language";
    public static final String DOC_SRC = "Document Source";
    public static final String DOC_TO_TYP = "Document Type";
    public static final String DOC_TO_STP = "Document Subtype";
    public static final String DOC_TO_ID = "Document Code";
    public static final String LANG_TO = "Language";
    public static final String URL = "URL";
    public static final String RPY_EMAIL = "Reply-to Email";
    public static final String CUST_CLASS_CDE = "Customer Classification";
    public static final String RESULT = "Result";
    public static final String REJ_REASON = "Reject Reasn";
    public static final String FORMAT="Format";
    public static final String EXPIRY_DT="Expiry Date";
    public static final String EFF_DT="Effective Date";
    public static final String EFF_TM="Effective Time";
    public static final String EFF_TM_OLD = "Effectve Time";
    public static final String DOC_DESC="Document Description";
    public static final String URG_IND= "Urgent Flag";

    public static final String DOC_NAME = "Document Name";
    public static final String FMT_TYP = "Format";   
    public static final String DOC_TYPE_CDE = "Document Type";
    public static final String DOC_SUBTYPE_CDE = "Document Subtype";
    public static final String LANG_CAT_CDE = "Language";
    public static final String FORMT_TYPE_CDE = "Format";
    public static final String DOC_INCM_NAME = "Document Name";
    public static final String EXPIR_DT = "Expiry Date Ccyymmdd";
    public static final String DOC_EXPLN_TEXT = "Document Description";
    public static final String PROD_TYPE_CDE = "Product Type";
    //public static final String PROD_SUBTYPE_CDE = "Product Subtype"; //in WPC, product subtype will not be a product key 
    public static final String EMAIL_ADR_RPY_TEXT = "Reply-to Email";

    public static final String DOC_TYP_TS = "TERMSHEET";
    public static final String DOC_TYP_PS = "PRCSUPPL";
    public static final String DOC_TYP_AP = "APPENDIX";
    public static final String DOC_TYP_FS = "FACTSHEET";

    // CUST_CLASS_CDE value
    public static final String CUST_CLASS_CDE_RBB = "RBB";
    public static final String CUST_CLASS_CDE_PFS = "PFS";
    public static final String CUST_CLASS_CDE_DEFAULT = "ALL";
    
    //Reject Reason
    public static final String DUPLICATE_REC_INSERT = "Duplicate Record for Insert ";
    public static final String NO_REC_DELETE = "No Record for Delete";
    public static final String NO_REC_CHANGE = "No Record for Change";
    public static final String REC_PROBLEM = "Record Problem";

    //Action Code Value
    public static final String ADD_ACTION = "A";
    public static final String DELETE_ACTION = "D";
    public static final String CHANGE_ACTION = "C";
    public static final String REJECT_ACTION = "R";
    public static final String MODIFY_ACTION = "M";

    //Language Code
    public static final String LANG_CDE_TW = "TW";
    public static final String LANG_CDE_EN = "EN";
    public static final String LANG_CDE_BL = "BL";
    public static final String LANG_CDE_ZH = "ZH";
    public static final String LANG_CDE_JP = "JP";
    public static final String LANG_CDE_IN = "IN";
    public static final String LANG_CDE_PT = "PT";
    public static final String LANG_CDE_ES = "ES";
    
    //Document source type
    public static final String DOC_SRC_TYP_DOC = "DOC";
    public static final String DOC_SRC_TYP_URL = "URL";
    
    //Format
    public static final String FMT_PDF="PDF";
    public static final String FMT_DOC="DOC";
    public static final String FMT_XLS="XLS";
    public static final String FMT_TIF="TIF";
    public static final String FMT_TIFF="TIFF";
    public static final String FMT_GIF="GIF";
    public static final String FMT_JPEG="JPEG";
    public static final String FMT_JPG="JPG";
    public static final String FMT_PPT="PPT";
    public static final String FMT_MP3="MP3";
    public static final String FMT_MP4="MP4";
    public static final String FMT_ZIP="ZIP";
    public static final String FMT_TXT="TXT";
    public static final String FMT_VSD="VSD";
    public static final String FMT_HTML="HTML";
    public static final String FMT_XML="XML";
    
    //Reference Data Type Code
    public static final String FINDOCTYP="FINDOCTYP";
    public static final String FINDOCSTP="FINDOCSTP";
    public static final String PRODTYP="PRODTYP";
    public static final String PRODSUBTYP ="PRODSUBTP";
    
    //STATUS
    public static final String PROC_APPROVAL = "V";
    public static final String PROC_REJECT = "J";
    public static final String APPROVAL = "A";
    public static final String PENDING = "P";
    public static final String REJECT = "R";
    public static final String CONFIRM = "C";
    public static final String PROCESSING = "I";
    
    //YES/NO
    public static final String YES="Y";
    public static final String NO="N";
    
    //FinDocTyp
    public static final String DOC="DOC";
    public static final String FinDocFACTSHEET = "fs";

    //PROD
    public static final String ELI="ELI";
    public static final String SID="SID";
    public static final String SN="SN";

    public static final String dummy_HFI="dummy_HFI";
    public static final String dummy_HFIDOC="dummy_HFIDOC";
/*
    //Script
    public static final String ARC_FTP_REQ_SH = WPCProperties.getProperty("ARC_FTP_REQ.Script");
    public static final String ARC_FTP_SH = WPCProperties.getProperty("ARC_FTP.Script");
    public static final String PWS_DEL_ACK_SH = WPCProperties.getProperty("PWS_DEL_ACK.Script");
    public static final String PWS_FTP_SH = WPCProperties.getProperty("PWS_FTP.Script");*/

    //suffix
    public static final String SUFFIX_CSV =".csv";
    public static final String SUFFIX_ACK =".ack";
    public static final String SUFFIX_XLS =".xls";
    public static final String SUFFIX_XLSX =".xlsx";
    public static final String SUFFIX_PDF =".pdf";
    public static final String SUFFIX_DOC =".doc";
    public static final String SUFFIX_TIF =".tif";
    public static final String SUFFIX_TIFF =".tiff";
    public static final String SUFFIX_GIF =".gif";
    public static final String SUFFIX_JPEG =".jpeg";
    public static final String SUFFIX_JPG =".jpg";
    public static final String SUFFIX_PPT =".ppt";
    public static final String SUFFIX_MP3 =".mp3";
    public static final String SUFFIX_MP4 =".mp4";
    public static final String SUFFIX_ZIP =".zip";
    public static final String SUFFIX_TXT =".txt";
    public static final String SUFFIX_VSD =".vsd";
    public static final String SUFFIX_HTML =".html";
    public static final String SUFFIX_XML =".xml";
    
    //file
    public static final String ARCHDATA ="FinDocArchData.txt";
    public static final String ARCH_FILE_LIST ="FinDocArchFiles.txt";
    public static final String ARCH_ZIP_FILE_LIST ="FinDocArchZipFiles.txt";
    public static final String DELACK ="FinDocDelPwsAck.txt";
    public static final String FTP="FinDocFtp.txt";

    public static final String RECORD_NOT_FOUND = "Record not found: ";
        
    public static final String PEND_APRV_ID="21";
    
    public static final double MAX_ARCHIVE_ZIP_FILE_SIZE = 20;

    public static final String DOC_CLASS_NAME_PREFIX = "dummy_";
    public static final String DOC_CLASS_NAME_SUFFIX = "_7";
    public static final String SUBTYPE_NAME_PREFIX = "dummy_";
    public static final String SUBTYPE_NAME_SUFFIX = "_7";
    
    public static final String ARCH_PRTY_NUM = "001";
    public static final String BANK_NUM = "04";
    
    public static final HashMap CTRY_ORGN_MAP = new HashMap();
    static {
        CTRY_ORGN_MAP.put("ID", "HFIINM");
        CTRY_ORGN_MAP.put("IM", "HFIIMO");
        CTRY_ORGN_MAP.put("MY", "HFIMYH");
        CTRY_ORGN_MAP.put("SE", "HFISEL");
        CTRY_ORGN_MAP.put("SG", "HFISGH");
        CTRY_ORGN_MAP.put("JP", "HFITKY");
        CTRY_ORGN_MAP.put("TW", "HFITWM");
	}

    public static final String UNDERLINE = "_";

    public static final String SUFFIX_BAK =".bak";

}