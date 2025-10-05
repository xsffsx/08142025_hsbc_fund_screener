
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart;

import java.math.BigDecimal;


public class Data {

    private String date;

    private BigDecimal cumulativeReturn;

    private BigDecimal navPrice;


    public String getDate() {
        return this.date;
    }


    public void setDate(final String date) {
        this.date = date;
    }


    public BigDecimal getCumulativeReturn() {
        return this.cumulativeReturn;
    }


    public void setCumulativeReturn(final BigDecimal cumulativeReturn) {
        this.cumulativeReturn = cumulativeReturn;
    }


    public BigDecimal getNavPrice() {
        return this.navPrice;
    }


    public void setNavPrice(final BigDecimal navPrice) {
        this.navPrice = navPrice;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cumulativeReturn == null) ? 0 : cumulativeReturn.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((navPrice == null) ? 0 : navPrice.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Data other = (Data) obj;
        if (cumulativeReturn == null) {
            if (other.cumulativeReturn != null)
                return false;
        } else if (!cumulativeReturn.equals(other.cumulativeReturn))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (navPrice == null) {
            if (other.navPrice != null)
                return false;
        } else if (!navPrice.equals(other.navPrice))
            return false;
        return true;
    }


}
