/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.predsrch.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;

@JsonInclude(Include.NON_NULL)
public class PredSrchRequest extends Request implements Serializable {

	private static final long serialVersionUID = 1L;

	private String keyword;

	private String[] assetClasses;

	private String[] searchFields;

	private String[] sortingFields;

	private String market;

	private List<SearchCriteria> filterCriterias;

	private String topNum;

	private String channelRestrictCode;

	private String restrOnlScribInd;

	// private boolean isPreciseSrch;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String[] getAssetClasses() {
		return assetClasses;
	}

	public void setAssetClasses(String[] assetClasses) {
		this.assetClasses = assetClasses;
	}

	public String[] getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String[] searchFields) {
		this.searchFields = searchFields;
	}

	public String[] getSortingFields() {
		return sortingFields;
	}

	public void setSortingFields(String[] sortingFields) {
		this.sortingFields = sortingFields;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public List<SearchCriteria> getFilterCriterias() {
		return filterCriterias;
	}

	public void setFilterCriterias(List<SearchCriteria> filterCriterias) {
		this.filterCriterias = filterCriterias;
	}

	public String getTopNum() {
		return topNum;
	}

	public void setTopNum(String topNum) {
		this.topNum = topNum;
	}

	public String getChannelRestrictCode() {
		return channelRestrictCode;
	}

	public void setChannelRestrictCode(String channelRestrictCode) {
		this.channelRestrictCode = channelRestrictCode;
	}

	public String getRestrOnlScribInd() {
		return restrOnlScribInd;
	}

	public void setRestrOnlScribInd(String restrOnlScribInd) {
		this.restrOnlScribInd = restrOnlScribInd;
	}

}
