/*
 */

package com.hhhh.group.secwealth.mktdata.elastic.bean;

public class ProdKeySeg {
	private String id;
	private String ctryRecCde;
	private String grpMembrRecCde;
	private String prodTypeCde;
	private String prodCde;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCtryRecCde() {
		return this.ctryRecCde;
	}

	public void setCtryRecCde(final String ctryRecCde) {
		this.ctryRecCde = ctryRecCde;
	}

	public String getGrpMembrRecCde() {
		return this.grpMembrRecCde;
	}

	public void setGrpMembrRecCde(final String grpMembrRecCde) {
		this.grpMembrRecCde = grpMembrRecCde;
	}

	public String getProdTypeCde() {
		return this.prodTypeCde;
	}

	public void setProdTypeCde(final String prodTypeCde) {
		this.prodTypeCde = prodTypeCde;
	}

	public String getProdCde() {
		return this.prodCde;
	}

	public void setProdCde(final String prodCde) {
		this.prodCde = prodCde;
	}

}
