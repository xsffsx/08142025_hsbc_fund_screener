
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.util.List;



public class CalendarReturns {

    private List<FundCalendarYearReturn> items;
    private String monthEndDate;


    public List<FundCalendarYearReturn> getItems() {
        return this.items;
    }


    public void setItems(final List<FundCalendarYearReturn> items) {
        this.items = items;
    }


    public String getMonthEndDate() {
        return this.monthEndDate;
    }


    public void setMonthEndDate(final String monthEndDate) {
        this.monthEndDate = monthEndDate;
    }

}
