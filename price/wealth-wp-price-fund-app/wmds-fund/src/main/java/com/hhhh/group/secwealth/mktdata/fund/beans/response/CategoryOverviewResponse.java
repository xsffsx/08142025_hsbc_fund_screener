
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.beans.response.model.categoryoverview.CategoryLevel1Summary;


public class CategoryOverviewResponse {

    private List<CategoryLevel1Summary> categoryOver;
    private String lastUpdatedDate;

    
    public List<CategoryLevel1Summary> getCategoryOver() {
        return this.categoryOver;
    }

    
    public void setCategoryOver(final List<CategoryLevel1Summary> categoryOver) {
        this.categoryOver = categoryOver;
    }

    
    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    
    public void setLastUpdatedDate(final String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

}
