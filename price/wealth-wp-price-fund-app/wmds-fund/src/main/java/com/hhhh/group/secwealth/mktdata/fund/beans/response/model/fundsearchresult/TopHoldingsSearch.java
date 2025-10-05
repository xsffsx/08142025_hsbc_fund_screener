package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult;

import java.math.BigDecimal;

public class TopHoldingsSearch {

    private String holdingName;
    private String holdingCompany;
    private BigDecimal holdingPercent;

    public String getHoldingName() {
        return this.holdingName;
    }

    public void setHoldingName(final String holdingName) {
        this.holdingName = holdingName;
    }

    public String getHoldingCompany() {
        return this.holdingCompany;
    }

    public void setHoldingCompany(final String holdingCompany) {
        this.holdingCompany = holdingCompany;
    }


    public BigDecimal getHoldingPercent() {
        return this.holdingPercent;
    }


    public void setHoldingPercent(final BigDecimal holdingPercent) {
        this.holdingPercent = holdingPercent;
    }

}
