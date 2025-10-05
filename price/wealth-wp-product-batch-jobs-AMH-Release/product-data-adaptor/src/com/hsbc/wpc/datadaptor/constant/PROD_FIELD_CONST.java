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
 * Class Name		PROD_FIELD_CONST
 *
 * Creation Date	Apr 10, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Apr 10, 2006
 *    CMM/PPCR No.		
 *    Programmer		cbdhkg0010
 *    Description
 * 
 */
package com.dummy.wpc.datadaptor.constant;


public class PROD_FIELD_CONST{

    public class FIELD_NAME_CONST{
        public final static String productTypeCode = "Product Type Code";
        public final static String productCodeAlternativeClassificationCode = "Product Code Alternative Classification Code";
        public final static String productAlternativePrimaryNumber = "Product Alternative Primary Number";
        
        
        public final static String productSubtypeCode = "Product Subtype Code"; 
        public final static String productCode = "Product Code";
        public final static String ProductId = "Product ID";
        
        public final static String countryRecordCode = "Country Record Code";
        public final static String groupMemberRecordCode = "Group Member Record Code";
        
        public final static String productName = "Product Name";
        public final static String productPrimaryLocalLanguageName = "Product Primary Local Language Name";
        public final static String productSecondaryLocalLanguageName = "Product Secondary Local Language Name";
        public final static String prodSubtpCde = "Product Subtype Code";
        
        public final static String productDescription = "Product Description";
        public final static String productPrimaryLocalLanguageDescription = "Product Primary Local Language Description";
        public final static String productSecondaryLocalLanguageDescription = "Product Secondary Local Language Description";
        
        public final static String productShortName = "Product Short Name";
        public final static String productPrimaryLocalLanguageShortName = "Product Primary Local Language Short Name";
        public final static String productSecondaryLocalLanguageShortName = "Product Secondary Local Language Short Name";
        
        public final static String lifeCycleExternalStatusCode = "Product Status";
        
        public final static String searchResultHighPriorityIndicator = "Search Result High Priority Indicator";
        
        public final static String shortlist1Indicator = "Fund of the Quarter Indicator";
        public final static String shortlist2Indicator = "Shortlisted Product Indicator 2";
        public final static String shortlist3Indicator = "Shortlisted Product Indicator 3";
        
        public final static String shortlistEffective1Date = "Fund of the Quarter Effective Date";
        public final static String shortlistEffective2Date = "Shortlisted Product Indicator 2 Effective Date";
        public final static String shortlistEffective3Date = "Shortlisted Product Indicator 3 Effective Date";
        
        public final static String userDefinedValueText = "User Defined Value Text";
        public final static String userDefinedSearchableValueText = "User Defined Searchable Value Text";
        public final static String keyWordText = "Keyword Text";

        public final static String relatedMarketInformationIndicator = "Related Market Information Indicator";

        public final static String dummyProductSubtypeIndicator = "Dummy Product Subtype Indicator";
        public final static String commonSearchResultDisplayIndicator = "Common Search Result Display Indicator";
        
        public final static String mrkToMktInd = "Market To Market Indicator";
        public final static String prcDivNum = "Price Divisor Number";
        
        public final static String newIntroInd = "Newly Introduce Indicator"; 
        
        public final static String topSellRankNum = "Top Seller Rank Number"; 

        public final static String topPerfmRankNum = "Top Performance Rank Number";

        public final static String topIvstYldRankNum = "Top Investment Yield Rank Number";
        
        public final static String prdBusStartRge = "Product Business Start Time Range";

        public final static String prdBusEndRge = "Product Business End Time Range";

        public final static String rmk = "Remarks";
    }
    
    public final static StringFieldInfo prodSubtpCde = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.prodSubtpCde, 50, null);//TODO : 30

    public final static StringFieldInfo productCodeAlternativeClassificationCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productCodeAlternativeClassificationCode, 1, null);
    public final static StringFieldInfo productAlternativePrimaryNumber = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productAlternativePrimaryNumber, 30, null);
    
    public final static StringFieldInfo productTypeCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productTypeCode, 15, null);
    public final static StringFieldInfo productSubtypeCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productSubtypeCode, 30, null);
    public final static StringFieldInfo productCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productCode, 30, null);
    public final static StringFieldInfo productId = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.ProductId,30,null);

    public final static StringFieldInfo countryRecordCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.countryRecordCode, 2, null);
    public final static StringFieldInfo groupMemberRecordCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.groupMemberRecordCode, 4, null);
    
    
    public final static StringFieldInfo productName = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productName, 100, null);
    public final static StringFieldInfo productPrimaryLocalLanguageName = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productPrimaryLocalLanguageName, 300, null);
    public final static StringFieldInfo productSecondaryLocalLanguageName = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productSecondaryLocalLanguageName, 300, null);
    
    public final static StringFieldInfo productDescription = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productDescription, 300, null);
    public final static StringFieldInfo productPrimaryLocalLanguageDescription = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productPrimaryLocalLanguageDescription, 900, null);
    public final static StringFieldInfo productSecondaryLocalLanguageDescription = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productSecondaryLocalLanguageDescription, 900, null);
    
    public final static StringFieldInfo productShortName = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productShortName, 30, null);
    public final static StringFieldInfo productPrimaryLocalLanguageShortName = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productPrimaryLocalLanguageShortName, 90, null);
    public final static StringFieldInfo productSecondaryLocalLanguageShortName = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.productSecondaryLocalLanguageShortName, 90, null);
    
    public final static StringFieldInfo lifeCycleExternalStatusCode = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.lifeCycleExternalStatusCode, 1, null);
    
    public final static RateFieldInfo searchResultHighPriorityIndicator = new RateFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.searchResultHighPriorityIndicator, 1, 0);
    
    public final static StringFieldInfo shortlist1Indicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.shortlist1Indicator, 1, null);
    public final static StringFieldInfo shortlist2Indicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.shortlist2Indicator, 1, null);
    public final static StringFieldInfo shortlist3Indicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.shortlist3Indicator, 1, null);
    
    public final static FieldInfo shortlistEffective1Date = new FieldInfo(FIELD_NAME_CONST.shortlistEffective1Date);
    public final static FieldInfo shortlistEffective2Date = new FieldInfo(FIELD_NAME_CONST.shortlistEffective2Date);
    public final static FieldInfo shortlistEffective3Date = new FieldInfo(FIELD_NAME_CONST.shortlistEffective3Date);
    
    public final static StringFieldInfo userDefinedValueText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.userDefinedValueText, 50, null);
    public final static StringFieldInfo userDefinedSearchableValueText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.userDefinedSearchableValueText, 50, null);
    public final static StringFieldInfo keyWordText = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.keyWordText, 20, null);

    public final static StringFieldInfo relatedMarketInformationIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.relatedMarketInformationIndicator, 1, null);

    public final static StringFieldInfo dummyProductSubtypeIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.dummyProductSubtypeIndicator, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo commonSearchResultDisplayIndicator = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.commonSearchResultDisplayIndicator, 1, new String[]{"Y", "N"});
    
    public final static StringFieldInfo mrkToMktInd = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.mrkToMktInd, 1, new String[]{"Y", "N"});
    public final static StringFieldInfo prcDivNum = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.prcDivNum, 3, null);
    
    public final static StringFieldInfo newIntroInd =  new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.newIntroInd, 1, null); 
    
    public final static StringFieldInfo topSellRankNum = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.topSellRankNum, 3, null); 

    public final static StringFieldInfo topPerfmRankNum = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.topPerfmRankNum, 3, null);

    public final static StringFieldInfo topIvstYldRankNum = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.topIvstYldRankNum, 3, null);
    
    public final static StringFieldInfo prdBusStartRge = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.prdBusStartRge, 20, null);

    public final static StringFieldInfo prdBusEndRge = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.prdBusEndRge, 20, null);

    public final static StringFieldInfo rmk = new StringFieldInfo(StringFieldInfo.THROW_ERROR, FIELD_NAME_CONST.rmk, 500, null);

}