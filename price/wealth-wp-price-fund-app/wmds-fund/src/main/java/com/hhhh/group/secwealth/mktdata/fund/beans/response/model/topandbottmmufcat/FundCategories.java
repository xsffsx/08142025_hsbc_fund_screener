package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmmufcat;

import java.util.List;

public class FundCategories {

    List<FundCategory> categories;
    private String lastUpdatedDate;


    public List<FundCategory> getCategories() {
        return this.categories;
    }


    public void setCategories(final List<FundCategory> categories) {
        this.categories = categories;
    }


    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }


    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

}
