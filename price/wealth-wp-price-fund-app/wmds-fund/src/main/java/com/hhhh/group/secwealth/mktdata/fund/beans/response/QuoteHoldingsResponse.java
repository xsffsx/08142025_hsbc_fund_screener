package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.quoteholdings.Holdings;

public class QuoteHoldingsResponse {

    private Holdings holdings;

    public Holdings getHoldings() {
        return this.holdings;
    }

    public void setHoldings(final Holdings holdings) {
        this.holdings = holdings;
    }
}
