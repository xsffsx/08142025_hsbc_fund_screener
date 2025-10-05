
package com.hhhh.group.secwealth.mktdata.fund.beans.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.dao.pojo.UtProdCat;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;


public class TopAndBottomCategoryResponse {
    String country;
    String assetClassCode;
    String assetClassName;
    String timeScale;
    Date asOfDate;

    List<TopBottomCategory> topCategories = new ArrayList<TopBottomCategory>();
    List<TopBottomCategory> bottomCategories = new ArrayList<TopBottomCategory>();


    public String getCountry() {
        return this.country;
    }


    public void setCountry(final String country) {
        this.country = country;
    }


    public String getAssetClassCode() {
        return this.assetClassCode;
    }


    public void setAssetClassCode(final String assetClassCode) {
        this.assetClassCode = assetClassCode;
    }


    public String getAssetClassName() {
        return this.assetClassName;
    }


    public void setAssetClassName(final String assetClassName) {
        this.assetClassName = assetClassName;
    }


    public String getTimeScale() {
        return this.timeScale;
    }


    public void setTimeScale(final String timeScale) {
        this.timeScale = timeScale;
    }


    public Date getAsOfDate() {
        return this.asOfDate;
    }


    public void setAsOfDate(final Date asOfDate) {
        this.asOfDate = asOfDate;
    }


    public List<TopBottomCategory> getTopCategories() {
        return this.topCategories;
    }


    public void setTopCategories(final List<TopBottomCategory> topCategories) {
        this.topCategories = topCategories;
    }


    public List<TopBottomCategory> getBottomCategories() {
        return this.bottomCategories;
    }



    public void setBottomCategories(final List<TopBottomCategory> bottomCategories) {
        this.bottomCategories = bottomCategories;
    }


    public void addBottomCategories(final int index, final List<UtProdCat> categories, final TimeScale timeScale) {
        addTPCategories(index, this.bottomCategories, categories, timeScale);
    }

    public void addTopCategories(final int index, final List<UtProdCat> categories, final TimeScale timeScale) {
        addTPCategories(index, this.topCategories, categories, timeScale);
    }

    private void addTPCategories(final int index, final List<TopBottomCategory> tpCategories, final List<UtProdCat> categories,
        final TimeScale timeScale) {
        for (UtProdCat category : categories) {
            TopBottomCategory tpCategory = new TopBottomCategory();
            tpCategory.setCategoryCode(category.getUtProdCatId().gethhhhCategoryCode());
            if (0 == index) {
                tpCategory.setCategoryName(category.getProductNlsName1());
            } else if (1 == index) {
                tpCategory.setCategoryName(category.getProductNlsName2());
            } else if (2 == index) {
                tpCategory.setCategoryName(category.getProductNlsName3());
            } else {
                tpCategory.setCategoryName(category.getProductNlsName1());
            }
            tpCategory.setNumberOfFunds(category.getNumberOfProducts());
            tpCategory.setTrailingTotalReturn(category.getUtProdCatPerfmRtrn().get(0).getTrailingTotalReturn());
            tpCategory.setAverageRisk(category.getStddev());
            tpCategories.add(tpCategory);
        }
    }

}
