/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2013. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.beans;

/**
 * The Class SearchProduct.
 */
public class SearchProduct {


    /** The External key. */
    private String externalKey;

    /** The prodCdeAltClassCde. */
    private String prodCdeAltClassCde;

    /** The SearchableObject. */
    private SearchableObject searchObject;

	public String getExternalKey() {
		return externalKey;
	}

	public void setExternalKey(String externalKey) {
		this.externalKey = externalKey;
	}

	public String getProdCdeAltClassCde() {
		return prodCdeAltClassCde;
	}

	public void setProdCdeAltClassCde(String prodCdeAltClassCde) {
		this.prodCdeAltClassCde = prodCdeAltClassCde;
	}

	public SearchableObject getSearchObject() {
		return searchObject;
	}

	public void setSearchObject(SearchableObject searchObject) {
		this.searchObject = searchObject;
	}

}