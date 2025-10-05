package com.hhhh.group.secwealth.mktdata.common.predsrch.request.helper;

/**
 * 
 * @author 43967870
 * For get searchObject by use InternalProductKeyUtil
 * Internal Search info Request
 */

public class InternalSearchRequest {
    
    private String altClassCde;
    
    private String countryCode;
    
    private String groupMember;
    
    private String prodAltNum;
    
    private String countryTradableCode;
    
    private String productType;
    
    private String locale;
    
	public InternalSearchRequest(String altClassCde, String countryCode, String groupMember, String prodAltNum,
			String countryTradableCode, String productType, String locale) {
		super();
		this.altClassCde = altClassCde;
		this.countryCode = countryCode;
		this.groupMember = groupMember;
		this.prodAltNum = prodAltNum;
		this.countryTradableCode = countryTradableCode;
		this.productType = productType;
		this.locale = locale;
	}

	public String getAltClassCde() {
		return altClassCde;
	}

	public void setAltClassCde(String altClassCde) {
		this.altClassCde = altClassCde;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getGroupMember() {
		return groupMember;
	}

	public void setGroupMember(String groupMember) {
		this.groupMember = groupMember;
	}

	public String getProdAltNum() {
		return prodAltNum;
	}

	public void setProdAltNum(String prodAltNum) {
		this.prodAltNum = prodAltNum;
	}

	public String getCountryTradableCode() {
		return countryTradableCode;
	}

	public void setCountryTradableCode(String countryTradableCode) {
		this.countryTradableCode = countryTradableCode;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
    
}
