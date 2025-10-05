package com.hhhh.group.secwealth.mktdata.predsrch.pres.beans;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;

public class MultiPredSrchRequest extends Request {
    /** The keyword. */
    private String[] keyword;

    /** The asset class. */
    private String[] assetClasses;

    /** The search fields. */
    private String[] searchFields;

    /** The sorting fields. */
    private String[] sortingFields;

    /** The market. */
    private String market;

    /** The criteria. */
    private List<SearchCriteria> filterCriterias;

    /** The top num. */
    private String topNum;

    private String channelRestrictCode;

    private String restrOnlScribInd;
    
    private boolean multiPreciseSrch;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("keyword", this.keyword).append("assetClass", this.assetClasses)
            .append("searchFields", this.searchFields).append("sortingFields", this.sortingFields).append("market", this.market)
            .append("filters", this.filterCriterias).append("topNum", this.topNum)
            .append("channelRestrictCode", this.channelRestrictCode).append("restrOnlScribInd", this.restrOnlScribInd).toString();
    }
    
    public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}

	/**
     * @return the assetClasses
     */
    public String[] getAssetClasses() {
        return this.assetClasses;
    }

    /**
     * @param assetClasses
     *            the assetClasses to set
     */
    public void setAssetClasses(final String[] assetClasses) {
        this.assetClasses = assetClasses;
    }

    /**
     * @return the searchFields
     */
    public String[] getSearchFields() {
        return this.searchFields;
    }

    /**
     * @param searchFields
     *            the searchFields to set
     */
    public void setSearchFields(final String[] searchFields) {
        this.searchFields = searchFields;
    }

    /**
     * @return the sortingFields
     */
    public String[] getSortingFields() {
        return this.sortingFields;
    }

    /**
     * @param sortingFields
     *            the sortingFields to set
     */
    public void setSortingFields(final String[] sortingFields) {
        this.sortingFields = sortingFields;
    }

    /**
     * @return the market
     */
    public String getMarket() {
        return this.market;
    }

    /**
     * @param market
     *            the market to set
     */
    public void setMarket(final String market) {
        this.market = market;
    }

    /**
     * @return the filterCriterias
     */
    public List<SearchCriteria> getFilterCriterias() {
        return this.filterCriterias;
    }

    /**
     * @param filterCriterias
     *            the filterCriterias to set
     */
    public void setFilterCriterias(final List<SearchCriteria> filterCriterias) {
        this.filterCriterias = filterCriterias;
    }

    /**
     * @return the topNum
     */
    public String getTopNum() {
        return this.topNum;
    }

    /**
     * @param topNum
     *            the topNum to set
     */
    public void setTopNum(final String topNum) {
        this.topNum = topNum;
    }

    /**
     * @return the channelRestrictCode
     */
    public String getChannelRestrictCode() {
        return this.channelRestrictCode;
    }

    /**
     * @param channelRestrictCode
     *            the channelRestrictCode to set
     */
    public void setChannelRestrictCode(final String channelRestrictCode) {
        this.channelRestrictCode = channelRestrictCode;
    }

    /**
     * @return the restrOnlScribInd
     */
    public String getRestrOnlScribInd() {
        return this.restrOnlScribInd;
    }

    /**
     * @param restrOnlScribInd
     *            the restrOnlScribInd to set
     */
    public void setRestrOnlScribInd(final String restrOnlScribInd) {
        this.restrOnlScribInd = restrOnlScribInd;
    }

	public boolean isMultiPreciseSrch() {
		return multiPreciseSrch;
	}

	public void setMultiPreciseSrch(boolean multiPreciseSrch) {
		this.multiPreciseSrch = multiPreciseSrch;
	}

}
