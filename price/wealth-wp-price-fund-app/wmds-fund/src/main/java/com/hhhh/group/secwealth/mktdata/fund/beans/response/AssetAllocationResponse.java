package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.assetallocation.AssetAllocation;

public class AssetAllocationResponse {


    private List<AssetAllocation> assetAllocations;
    private String portfolioDate;


    public List<AssetAllocation> getAssetAllocations() {
        return this.assetAllocations;
    }



    public void setAssetAllocations(final List<AssetAllocation> assetAllocations) {
        this.assetAllocations = assetAllocations;
    }



    public String getPortfolioDate() {
        return this.portfolioDate;
    }



    public void setPortfolioDate(final String portfolioDate) {
        this.portfolioDate = portfolioDate;
    }
}
