
package com.hhhh.group.secwealth.mktdata.fund.beans.helper;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCat;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatTtlRtrnIndex;
import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCatTtlRtrnIndexId;


public class TopAndBottomCategoryChartResponse {

    List<CategoryLine> categoryLines = new ArrayList<CategoryLine>();

    Set<Date> dates = new TreeSet<Date>();


    public List<CategoryLine> getCategoryLines() {
        return this.categoryLines;
    }


    public void setCategoryLines(final List<CategoryLine> categoryLines) {
        this.categoryLines = categoryLines;
    }


    public Set<Date> getDates() {
        return this.dates;
    }


    public void setDates(final Set<Date> dates) {
        this.dates = dates;
    }

    public void addCategoryLines(final int index, final List<UtProdCat> categories) {

        CategoryLine line = null;

        for (UtProdCat category : categories) {
            line = new CategoryLine();
            line.setCategoryCode(category.getUtProdCatId().gethhhhCategoryCode());
            if (0 == index) {
                line.setCategoryName(category.getProductNlsName1());
            } else if (1 == index) {
                line.setCategoryName(category.getProductNlsName2());
            } else if (2 == index) {
                line.setCategoryName(category.getProductNlsName3());
            } else {
                line.setCategoryName(category.getProductNlsName1());
            }
            int i = 0;
            int j = 0;

            calculateCategoryTotalReturnIndexPercentChange(category.getCategoryTotalReturnIndex());

            while (i < category.getCategoryTotalReturnIndex().size() && j < this.dates.size()) {
                UtProdCatTtlRtrnIndex returnIndex = category.getCategoryTotalReturnIndex().get(i);
                Date date = (Date) this.dates.toArray()[j];
                UtProdCatTtlRtrnIndexId utProdCatTtlRtrnIndexId = returnIndex.getUtProdCatTtlRtrnIndexId();
                Date endDate = null;
                if (null != utProdCatTtlRtrnIndexId) {
                    endDate = utProdCatTtlRtrnIndexId.getEndDate();
                }
                if (endDate == null) {
                    i++;
                } else if (endDate.equals(date)) {
                    // line.addDataPoint(returnIndex.getValue());
                    line.addDataPoint(returnIndex.getChange());
                    i++;
                    j++;
                } else if (endDate.compareTo(date) > 0) {
                    line.addDataPoint(null);
                    j++;
                } else {
                    i++;
                }
            }
            this.categoryLines.add(line);
        }
    }


    private void calculateCategoryTotalReturnIndexPercentChange(final List<UtProdCatTtlRtrnIndex> list) {

        if (list == null) {
            return;
        }

        int size = list.size();

        for (int i = 0; i < size - 1; i++) {
            UtProdCatTtlRtrnIndex current = list.get(i);
            UtProdCatTtlRtrnIndex next = list.get(i + 1);

            BigDecimal changeVal = next.getValue().subtract(current.getValue());
            BigDecimal change = changeVal.divide(current.getValue(), 6, RoundingMode.HALF_UP);
            BigDecimal changePct = change.multiply(new BigDecimal("100"));

            next.setChange(changePct);
        }

        if (list.get(0) != null) {
            list.get(0).setChange(new BigDecimal("0"));
        }

    }

    public void addDates(final List<UtProdCat> categories) {
        for (UtProdCat category : categories) {
            for (UtProdCatTtlRtrnIndex returnIndex : category.getCategoryTotalReturnIndex()) {
                UtProdCatTtlRtrnIndexId utProdCatTtlRtrnIndexId = returnIndex.getUtProdCatTtlRtrnIndexId();
                if (null != utProdCatTtlRtrnIndexId) {
                    if (utProdCatTtlRtrnIndexId.getEndDate() == null) {
                        continue;
                    }
                    this.dates.add(utProdCatTtlRtrnIndexId.getEndDate());
                }
            }
        }
    }
}
