package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.hhhh.group.secwealth.mktdata.elastic.validator.XssSecurity;

@XssSecurity
public class PredSrchRequest extends Request {

    @Size(min = 0, max = 100)
    @NotEmpty(message = "{validator.notEmpty.predSrchRequest.keyword.message}")
    private String keyword;

    private boolean houseViewFlag = false;

    private String houseViewFilter;

    public String getHouseViewFilter() {
        return houseViewFilter;
    }

    public void setHouseViewFilter(String houseViewFilter) {
        this.houseViewFilter = houseViewFilter;
    }

    public boolean isHouseViewFlag() {
        return houseViewFlag;
    }

    public void setHouseViewFlag(boolean houseViewFlag) {
        this.houseViewFlag = houseViewFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("keyword", this.keyword).append("assetClass", super.getAssetClasses())
                .append("searchFields", super.getSearchFields()).append("sortingFields", super.getSortingFields()).append("market", super.getMarket())
                .append("filters", super.getFilterCriterias()).append("topNum", super.getTopNum())
                .append("channelRestrictCode", super.getChannelRestrictCode()).append("restrOnlScribInd", super.getRestrOnlScribInd()).toString();
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return this.keyword;
    }

    /**
     * @param keyword
     *            the keyword to set
     */
    public void setKeyword(final String keyword) {
        this.keyword = keyword;
    }

}
