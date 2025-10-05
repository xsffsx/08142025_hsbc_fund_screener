
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.otherfundclass.AssetClass;



public class OtherFundClassesResponse {

    private List<AssetClass> assetClasses;

    
    public List<AssetClass> getAssetClasses() {
        return this.assetClasses;
    }

    
    public void setAssetClasses(final List<AssetClass> assetClasses) {
        this.assetClasses = assetClasses;
    }

}
