
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompareindex;

import java.util.List;


public class FundCompareIndex {

    private String categoryCode;
    private String categoryName;
    private List<CompareIndex> items;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public List<CompareIndex> getItems() {
        return this.items;
    }


    public void setItems(final List<CompareIndex> items) {
        this.items = items;
    }

}
