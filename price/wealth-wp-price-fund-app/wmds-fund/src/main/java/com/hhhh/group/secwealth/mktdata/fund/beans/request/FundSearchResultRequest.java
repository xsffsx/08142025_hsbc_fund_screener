
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.criteria.Holdings;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.criteria.SearchRangeCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.criteria.SortCriteriaValue;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;


public class FundSearchResultRequest extends Request {

    private String productType;
    // optional
    private List<SearchCriteria> detailedCriterias;
    private List<SearchRangeCriteriaValue> rangeCriterias;
    private List<ProductKey> productKeys;
    private List<Holdings> holdings;
    private String sortBy;
    private String sortOrder;
    private Integer numberOfRecords;
    private Integer startDetail;
    private Integer endDetail;
    private String currencyCode;
    private String entityTimezone;
    private Boolean returnOnlyNumberOfMatches;
    private String channelRestrictCode;
    private String restrOnlScribInd;
    private List<SortCriteriaValue> sortCriterias;

    
    public String getProductType() {
        return this.productType;
    }

    
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    
    public List<SearchCriteria> getDetailedCriterias() {
        return this.detailedCriterias;
    }

    
    public void setDetailedCriterias(final List<SearchCriteria> detailedCriterias) {
        this.detailedCriterias = detailedCriterias;
    }

    
    public List<SearchRangeCriteriaValue> getRangeCriterias() {
        return this.rangeCriterias;
    }

    
    public void setRangeCriterias(final List<SearchRangeCriteriaValue> rangeCriterias) {
        this.rangeCriterias = rangeCriterias;
    }

    
    public List<ProductKey> getProductKeys() {
        return this.productKeys;
    }

    
    public void setProductKeys(final List<ProductKey> productKeys) {
        this.productKeys = productKeys;
    }

    
    public List<Holdings> getHoldings() {
        return this.holdings;
    }

    
    public void setHoldings(final List<Holdings> holdings) {
        this.holdings = holdings;
    }

    
    public String getSortBy() {
        return this.sortBy;
    }

    
    public void setSortBy(final String sortBy) {
        this.sortBy = sortBy;
    }

    
    public String getSortOrder() {
        return this.sortOrder;
    }

    
    public void setSortOrder(final String sortOrder) {
        this.sortOrder = sortOrder;
    }

    
    public Integer getNumberOfRecords() {
        return this.numberOfRecords;
    }

    
    public void setNumberOfRecords(final Integer numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    
    public Integer getStartDetail() {
        return this.startDetail;
    }

    
    public void setStartDetail(final Integer startDetail) {
        this.startDetail = startDetail;
    }

    
    public Integer getEndDetail() {
        return this.endDetail;
    }

    
    public void setEndDetail(final Integer endDetail) {
        this.endDetail = endDetail;
    }

    
    public String getCurrencyCode() {
        return this.currencyCode;
    }

    
    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    
    public String getEntityTimezone() {
        return this.entityTimezone;
    }

    
    public void setEntityTimezone(final String entityTimezone) {
        this.entityTimezone = entityTimezone;
    }

    
    public Boolean getReturnOnlyNumberOfMatches() {
        return this.returnOnlyNumberOfMatches;
    }

    
    public void setReturnOnlyNumberOfMatches(final Boolean returnOnlyNumberOfMatches) {
        this.returnOnlyNumberOfMatches = returnOnlyNumberOfMatches;
    }

    
    public String getChannelRestrictCode() {
        return this.channelRestrictCode;
    }

    
    public void setChannelRestrictCode(final String channelRestrictCode) {
        this.channelRestrictCode = channelRestrictCode;
    }

    
    public String getRestrOnlScribInd() {
        return this.restrOnlScribInd;
    }

    
    public void setRestrOnlScribInd(final String restrOnlScribInd) {
        this.restrOnlScribInd = restrOnlScribInd;
    }

    
    public List<SortCriteriaValue> getSortCriterias() {
        return this.sortCriterias;
    }

    
    public void setSortCriterias(final List<SortCriteriaValue> sortCriterias) {
        this.sortCriterias = sortCriterias;
    }

}
