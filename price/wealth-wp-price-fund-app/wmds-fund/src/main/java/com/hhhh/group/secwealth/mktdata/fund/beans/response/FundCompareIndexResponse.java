
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundcompareindex.FundCompareIndex;


public class FundCompareIndexResponse {

    private List<FundCompareIndex> compareIndex;

    
    public List<FundCompareIndex> getCompareIndex() {
        return this.compareIndex;
    }

    
    public void setCompareIndex(final List<FundCompareIndex> compareIndex) {
        this.compareIndex = compareIndex;
    }

}
