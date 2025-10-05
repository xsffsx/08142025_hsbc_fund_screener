
package com.hhhh.group.secwealth.mktdata.fund.beans.response.model.categoryoverview;

import java.util.List;


public class CategoryLevel1Summary {

    private String categoryLevel1Code;
    private String categoryLevel1Name;
    private List<CategorySummary> items;

    
    public String getCategoryLevel1Code() {
        return this.categoryLevel1Code;
    }

    
    public void setCategoryLevel1Code(final String categoryLevel1Code) {
        this.categoryLevel1Code = categoryLevel1Code;
    }

    
    public String getCategoryLevel1Name() {
        return this.categoryLevel1Name;
    }

    
    public void setCategoryLevel1Name(final String categoryLevel1Name) {
        this.categoryLevel1Name = categoryLevel1Name;
    }

    
    public List<CategorySummary> getItems() {
        return this.items;
    }

    
    public void setItems(final List<CategorySummary> items) {
        this.items = items;
    }

}
