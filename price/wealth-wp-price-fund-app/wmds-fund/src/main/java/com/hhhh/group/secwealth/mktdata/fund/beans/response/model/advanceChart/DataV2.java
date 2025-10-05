
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.advanceChart;

import java.math.BigDecimal;


public class DataV2 {

    private String date;

    private BigDecimal cumulativeReturn;

    private String navPriceText;


    public String getNavPriceText() {
        return navPriceText;
    }

    public void setNavPriceText(String navPriceText) {
        this.navPriceText = navPriceText;
    }

    
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



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cumulativeReturn == null) ? 0 : cumulativeReturn.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((navPriceText == null) ? 0 : navPriceText.hashCode());
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
        DataV2 other = (DataV2) obj;
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
        if (navPriceText == null) {
            if (other.navPriceText != null)
                return false;
        } else if (!navPriceText.equals(other.navPriceText))
            return false;
        return true;
    }


}
