package com.hhhh.group.secwealth.mktdata.fund.beans.response;


import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.toptenholdings.TopTenHoldings;

public class TopTenHoldingsResponse {

    private TopTenHoldings top10Holdings;

    public TopTenHoldings getTop10Holdings() {
        return this.top10Holdings;
    }

    public void setTop10Holdings(final TopTenHoldings top10Holdings) {
        this.top10Holdings = top10Holdings;
    }

}
