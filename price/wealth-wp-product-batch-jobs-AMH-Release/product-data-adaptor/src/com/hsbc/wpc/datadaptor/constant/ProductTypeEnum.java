package com.dummy.wpc.datadaptor.constant;

/**
 * Product Type Enum
 *
 */
public enum ProductTypeEnum {

	BOND("BOND"), DPS("DPS"), ELI("ELI"), FCYDEP("FCYDEP"), INS("INS"), LCYDEP("LCYDEP"), SID("SID"), UT("UT"), 
	SEC("SEC"), SN("SN"), WRTS("WRTS"), DFLT("");

	private String productType;

	private ProductTypeEnum(String productType) {
		this.productType = productType;
	}
	
	/**
	 * retrieve the Enum by product type string code
	 * @param prodType product type string code
	 * @return  the corresponding enum object
	 */
	public static ProductTypeEnum retrieveProdType(String prodType) {
		ProductTypeEnum prodTypeEnum = DFLT;
		for (ProductTypeEnum typeEnum : ProductTypeEnum.values()) {
			if (typeEnum.productType.equalsIgnoreCase(prodType)) {
				prodTypeEnum = typeEnum;
				break;
			}
		}

		return prodTypeEnum;
	}

	public String getProductType() {
		return productType;
	}
	
}
