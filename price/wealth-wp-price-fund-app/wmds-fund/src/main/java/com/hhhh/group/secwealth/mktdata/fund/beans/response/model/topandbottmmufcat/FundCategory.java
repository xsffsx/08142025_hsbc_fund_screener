package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmmufcat;

import java.util.List;

public class FundCategory {

    private String itemName;
    private List<FundCategoriesItem> items;


    public String getItemName() {
        return this.itemName;
    }


    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }


    public List<FundCategoriesItem> getItems() {
        return this.items;
    }


    public void setItems(final List<FundCategoriesItem> items) {
        this.items = items;
    }

}
