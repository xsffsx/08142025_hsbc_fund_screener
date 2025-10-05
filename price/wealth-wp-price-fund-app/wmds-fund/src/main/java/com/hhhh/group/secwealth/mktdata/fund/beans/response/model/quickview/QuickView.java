
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quickview;

import java.util.List;

public class QuickView {

    private int totalNumberOfRecords;
    private List<QuickViewRespItem> items;
    private String lastUpdatedDate;


    public int getTotalNumberOfRecords() {
        return this.totalNumberOfRecords;
    }


    public void setTotalNumberOfRecords(final int totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }


    public List<QuickViewRespItem> getItems() {
        return this.items;
    }


    public void setItems(final List<QuickViewRespItem> items) {
        this.items = items;
    }


    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }


    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
