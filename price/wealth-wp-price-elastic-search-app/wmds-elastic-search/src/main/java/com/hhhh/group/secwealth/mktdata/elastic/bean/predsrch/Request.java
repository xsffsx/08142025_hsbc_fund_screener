/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;

import javax.validation.constraints.NotEmpty;


/**
 * <p>
 * <b> Base Request </b>
 * </p>
 */
public abstract class Request {

    @NotEmpty(message = "{validator.notEmpty.predSrchRequest.assetClasses.message}")
    private String[] assetClasses;

    private String[] searchFields;

    private String[] sortingFields;

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

    private String market;

    private List<SearchCriteria> filterCriterias;

    private String topNum;

    private String channelRestrictCode;

    private String restrOnlScribInd;

    private Map<String, String> headers = new HashMap<>();

    public void addHeader(final String name, final String value) {
        this.headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void putAllHeaders(final Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            this.headers.putAll(headers);
        }
    }

    public String getSiteKey() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_COUNTRYCODE) + CommonConstants.SYMBOL_UNDERLINE
            + this.headers.get(CommonConstants.REQUEST_HEADER_GROUPMEMBER);
    }

    public String getLocale() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_LOCALE);
    }


    public String getCountryCode() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_COUNTRYCODE);
    }

    public String getGroupMember() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_GROUPMEMBER);
    }

    public String getChannelId() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_CHANNELID);
    }
    public String getGbgf() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_GBGF);
    }

    public String getAppCode() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_APPCODE);
    }

    public String getLineOfBusiness() {
        return this.headers.get(CommonConstants.REQUEST_HEADER_LINEOFBUSINESS);
    }

    public String getE2ETrustWealthSaml() {
        return this.headers.get(CommonConstants.E2ETRUST_HEADER_WEALTHSAML);
    }

    public String getE2ETrustSaml() {
        return this.headers.get(CommonConstants.E2ETRUST_HEADER_SAML);
    }
}
