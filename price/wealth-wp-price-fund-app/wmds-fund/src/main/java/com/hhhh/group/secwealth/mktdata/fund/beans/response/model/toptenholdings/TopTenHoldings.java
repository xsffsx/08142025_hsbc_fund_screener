
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.toptenholdings;

import java.util.List;


public class TopTenHoldings {

    
    List<TopTenHoldingsItem> items;

    
    private String lastUpdatedDate;

    
    public List<TopTenHoldingsItem> getItems() {
        return this.items;
    }

    
    public void setItems(final List<TopTenHoldingsItem> items) {
        this.items = items;
    }

    
    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    
    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
