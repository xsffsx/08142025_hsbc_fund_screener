
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.util.List;


public class CumulativeReturns {

    private List<FundCumulativeReturn> items;
    private String monthEndDate;
    private String dayEndDate;


    public List<FundCumulativeReturn> getItems() {
        return this.items;
    }


    public void setItems(final List<FundCumulativeReturn> items) {
        this.items = items;
    }


    public String getMonthEndDate() {
        return this.monthEndDate;
    }


    public void setMonthEndDate(final String monthEndDate) {
        this.monthEndDate = monthEndDate;
    }


    public String getDayEndDate() {
        return this.dayEndDate;
    }


    public void setDayEndDate(final String dayEndDate) {
        this.dayEndDate = dayEndDate;
    }

}
