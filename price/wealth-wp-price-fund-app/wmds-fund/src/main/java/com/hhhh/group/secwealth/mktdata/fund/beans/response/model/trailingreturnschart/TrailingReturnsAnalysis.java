package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.trailingreturnschart;

import java.util.List;

public class TrailingReturnsAnalysis {

    List<TrailingReturnsAnalysisItem> items;

    private String lastUpdatedDate;

    public List<TrailingReturnsAnalysisItem> getItems() {
        return this.items;
    }

    public void setItems(final List<TrailingReturnsAnalysisItem> items) {
        this.items = items;
    }

    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

}
