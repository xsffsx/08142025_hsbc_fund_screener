
package com.hhhh.group.secwealth.mktdata.fund.beans.request;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.common.ProductKey;


@SuppressWarnings("serial")
public class AdvanceChartRequest extends Request implements Serializable {

    private static final long serialVersionUID = -1609298223156989996L;

    private List<ProductKey> ProductKeys;

    private String startDate;

    private String endDate;

    private String period;

    private String currency;

    private String[] dataType;

    private String frequency;

    private boolean navForwardFill;

    private String timeZone;



    public List<ProductKey> getProductKeys() {
        return this.ProductKeys;
    }


    public void setProductKeys(final List<ProductKey> productKeys) {
        this.ProductKeys = productKeys;
    }


    public String getStartDate() {
        return this.startDate;
    }


    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return this.endDate;
    }


    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }


    public String getPeriod() {
        return this.period;
    }


    public void setPeriod(final String period) {
        this.period = period;
    }


    public String getCurrency() {
        return this.currency;
    }


    public void setCurrency(final String currency) {
        this.currency = currency;
    }


    public String[] getDataType() {
        return this.dataType;
    }


    public void setDataType(final String[] dataType) {
        this.dataType = dataType;
    }


    public String getFrequency() {
        return this.frequency;
    }


    public void setFrequency(final String frequency) {
        this.frequency = frequency;
    }


    public boolean getNavForwardFill() {
        return this.navForwardFill;
    }


    public void setNavForwardFill(final boolean navForwardFill) {
        this.navForwardFill = navForwardFill;
    }





    public String getTimeZone() {
        return this.timeZone;
    }


    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ProductKeys == null) ? 0 : ProductKeys.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + Arrays.hashCode(dataType);
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
        result = prime * result + (navForwardFill ? 1231 : 1237);
        result = prime * result + ((period == null) ? 0 : period.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((timeZone == null) ? 0 : timeZone.hashCode());
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
        AdvanceChartRequest other = (AdvanceChartRequest) obj;
        if (ProductKeys == null) {
            if (other.ProductKeys != null)
                return false;
        } else if (!ProductKeys.equals(other.ProductKeys))
            return false;
        if (currency == null) {
            if (other.currency != null)
                return false;
        } else if (!currency.equals(other.currency))
            return false;
        if (!Arrays.equals(dataType, other.dataType))
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (frequency == null) {
            if (other.frequency != null)
                return false;
        } else if (!frequency.equals(other.frequency))
            return false;
        if (navForwardFill != other.navForwardFill)
            return false;
        if (period == null) {
            if (other.period != null)
                return false;
        } else if (!period.equals(other.period))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (timeZone == null) {
            if (other.timeZone != null)
                return false;
        } else if (!timeZone.equals(other.timeZone))
            return false;
        return true;
    }


}
