package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;


import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MultiPredSrchRequest extends Request {

	@NotEmpty(message = "{validator.notEmpty.multiPredSrchRequest.keyword.message}")
	private String[] keyword;

	private int needRecords;
	
	private boolean isPreciseSrch;

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("keyword", this.keyword).append("assetClass", super.getAssetClasses())
				.append("searchFields", super.getSearchFields()).append("sortingFields", super.getSortingFields())
				.append("market", super.getMarket()).append("filters", super.getFilterCriterias()).append("topNum", super.getTopNum())
				.append("channelRestrictCode", super.getChannelRestrictCode())
				.append("restrOnlScribInd", super.getRestrOnlScribInd()).toString();
	}

	public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}

	public boolean getIsPreciseSrch() {
		return isPreciseSrch;
	}

	public void setIsPreciseSrch(boolean isPreciseSrch) {
		this.isPreciseSrch = isPreciseSrch;
	}

	public int getNeedRecords() { return  this.needRecords; }

	public void setNeedRecords(final int needRecords) { this.needRecords = needRecords; }


}
