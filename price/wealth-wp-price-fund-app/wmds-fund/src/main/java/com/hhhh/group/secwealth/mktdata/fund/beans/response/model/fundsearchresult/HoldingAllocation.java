
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.fundsearchresult;

import java.math.BigDecimal;


public class HoldingAllocation {

    private String name;
    private BigDecimal weighting;


    public String getName() {
        return this.name;
    }


    public void setName(final String name) {
        this.name = name;
    }


    public BigDecimal getWeighting() {
        return this.weighting;
    }


    public void setWeighting(final BigDecimal weighting) {
        this.weighting = weighting;
    }

}
