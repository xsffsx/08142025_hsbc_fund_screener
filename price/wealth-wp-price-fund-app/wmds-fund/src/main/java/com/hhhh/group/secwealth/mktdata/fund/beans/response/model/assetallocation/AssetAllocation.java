
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.assetallocation;

import java.math.BigDecimal;


public class AssetAllocation {
    private String assetClass;
    private BigDecimal assetWeight;


    public String getAssetClass() {
        return this.assetClass;
    }


    public void setAssetClass(final String assetClass) {
        this.assetClass = assetClass;
    }


    public BigDecimal getAssetWeight() {
        return this.assetWeight;
    }


    public void setAssetWeight(final BigDecimal assetWeight) {
        this.assetWeight = assetWeight;
    }
}
