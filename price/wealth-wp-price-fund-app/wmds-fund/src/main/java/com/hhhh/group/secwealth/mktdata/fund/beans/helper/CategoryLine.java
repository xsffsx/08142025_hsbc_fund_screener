
package com.hhhh.group.secwealth.mktdata.fund.beans.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CategoryLine {

    String categoryCode;
    String categoryName;

    List<BigDecimal> dataPoints = new ArrayList<BigDecimal>();

    @Override
    public String toString() {
        return "[categoryCode:" + this.categoryCode + ",categoryName:" + this.categoryName + "]";
    }


    public String getCategoryCode() {
        return this.categoryCode;
    }


    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }


    public String getCategoryName() {
        return this.categoryName;
    }


    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }


    public List<BigDecimal> getDataPoints() {
        return this.dataPoints;
    }

  
    public void setDataPoints(final List<BigDecimal> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void addDataPoint(final BigDecimal dataPoint) {
        this.dataPoints.add(dataPoint);
    }

}
