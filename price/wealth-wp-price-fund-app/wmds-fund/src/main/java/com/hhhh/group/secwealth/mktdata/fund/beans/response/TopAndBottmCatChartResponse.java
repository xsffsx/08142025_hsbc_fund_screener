package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmcatchart.CategoriesChartData;
import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.topandbottmcatchart.CategoriesList;

public class TopAndBottmCatChartResponse {

    private String marketPeriod;

    private List<CategoriesList> categories;

    private List<CategoriesChartData> topBottomChartData;

    public String getMarketPeriod() {
        return this.marketPeriod;
    }

    public void setMarketPeriod(final String marketPeriod) {
        this.marketPeriod = marketPeriod;
    }

    public List<CategoriesList> getCategories() {
        return this.categories;
    }

    public void setCategories(final List<CategoriesList> categories) {
        this.categories = categories;
    }

    public List<CategoriesChartData> getTopBottomChartData() {
        return this.topBottomChartData;
    }

    public void setTopBottomChartData(final List<CategoriesChartData> topBottomChartData) {
        this.topBottomChartData = topBottomChartData;
    }

}
