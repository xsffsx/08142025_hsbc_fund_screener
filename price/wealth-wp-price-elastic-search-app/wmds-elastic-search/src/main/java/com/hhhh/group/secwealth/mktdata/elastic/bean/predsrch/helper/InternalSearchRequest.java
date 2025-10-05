package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.helper;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author 43967870
 * For get searchObject by use InternalProductKeyUtil
 * Internal Search info Request
 */

@Setter
@Getter
public class InternalSearchRequest {
    
    private String altClassCde;
    
    private String countryCode;
    
    private String groupMember;
    
    private String prodAltNum;
    
    private String countryTradableCode;
    
    private String productType;
    
    private String locale;
}
