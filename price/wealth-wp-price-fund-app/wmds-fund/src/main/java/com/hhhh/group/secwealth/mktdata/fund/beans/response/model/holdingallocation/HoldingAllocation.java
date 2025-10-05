
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.holdingallocation;

import java.util.List;


public class HoldingAllocation {

    private String methods;
    private List<Breakdowns> breakdowns;
    private String portfolioDate;

    
    public String getMethods() {
        return this.methods;
    }

    
    public void setMethods(final String methods) {
        this.methods = methods;
    }

    
    public List<Breakdowns> getBreakdowns() {
        return this.breakdowns;
    }

    
    public void setBreakdowns(final List<Breakdowns> breakdowns) {
        this.breakdowns = breakdowns;
    }

    
    public String getPortfolioDate() {
        return this.portfolioDate;
    }

    
    public void setPortfolioDate(final String portfolioDate) {
        this.portfolioDate = portfolioDate;
    }

}
