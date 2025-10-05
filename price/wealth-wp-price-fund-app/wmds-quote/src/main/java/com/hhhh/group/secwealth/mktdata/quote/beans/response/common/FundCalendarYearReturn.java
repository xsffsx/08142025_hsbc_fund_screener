
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;


public class FundCalendarYearReturn {

    private BigDecimal year;
    private BigDecimal annualCalendarYearReturn;

    public BigDecimal getYear() {
        return this.year;
    }


    public void setYear(final BigDecimal year) {
        this.year = year;
    }


    public BigDecimal getAnnualCalendarYearReturn() {
        return this.annualCalendarYearReturn;
    }


    public void setAnnualCalendarYearReturn(final BigDecimal annualCalendarYearReturn) {
        this.annualCalendarYearReturn = annualCalendarYearReturn;
    }
}
