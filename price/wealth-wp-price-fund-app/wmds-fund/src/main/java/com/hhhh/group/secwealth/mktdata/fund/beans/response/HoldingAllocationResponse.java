
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.math.BigDecimal;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.holdingallocation.HoldingAllocation;



public class HoldingAllocationResponse {

    List<HoldingAllocation> holdingAllocation;;
    private BigDecimal numberOfStockHoldings;
    private BigDecimal numberOfBondHoldings;
    private String lastUpdatedDate;


    public List<HoldingAllocation> getHoldingAllocation() {
        return this.holdingAllocation;
    }


    public void setHoldingAllocation(final List<HoldingAllocation> holdingAllocation) {
        this.holdingAllocation = holdingAllocation;
    }


    public BigDecimal getNumberOfStockHoldings() {
        return this.numberOfStockHoldings;
    }


    public void setNumberOfStockHoldings(final BigDecimal numberOfStockHoldings) {
        this.numberOfStockHoldings = numberOfStockHoldings;
    }


    public BigDecimal getNumberOfBondHoldings() {
        return this.numberOfBondHoldings;
    }


    public void setNumberOfBondHoldings(final BigDecimal numberOfBondHoldings) {
        this.numberOfBondHoldings = numberOfBondHoldings;
    }


    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }


    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

}
