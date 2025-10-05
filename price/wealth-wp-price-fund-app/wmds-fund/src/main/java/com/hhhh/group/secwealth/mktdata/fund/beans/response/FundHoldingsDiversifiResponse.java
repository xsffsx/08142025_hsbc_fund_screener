package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundholdingsdiversifi.HoldingsDiversificationItem;

public class FundHoldingsDiversifiResponse {

    List<HoldingsDiversificationItem> holdingsDiversifications;


    public List<HoldingsDiversificationItem> getHoldingsDiversifications() {
        return this.holdingsDiversifications;
    }


    public void setHoldingsDiversifications(final List<HoldingsDiversificationItem> holdingsDiversifications) {
        this.holdingsDiversifications = holdingsDiversifications;
    }

}
