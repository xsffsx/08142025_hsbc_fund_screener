package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult;

import java.math.BigDecimal;

public class FundSearchResultYear {

    private Integer yearName;
    private BigDecimal yearValue;


    public Integer getYearName() {
        return this.yearName;
    }


    public void setYearName(final Integer yearName) {
        this.yearName = yearName;
    }


    public BigDecimal getYearValue() {
        return this.yearValue;
    }


    public void setYearValue(final BigDecimal yearValue) {
        this.yearValue = yearValue;
    }

}
