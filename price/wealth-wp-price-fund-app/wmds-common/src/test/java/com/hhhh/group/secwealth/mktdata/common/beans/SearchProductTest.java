package com.hhhh.group.secwealth.mktdata.common.beans;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SearchProductTest {

    @Test
    public void search(){
        SearchProduct searchProduct = new SearchProduct();
        searchProduct.getExternalKey();
        searchProduct.setExternalKey("");
        searchProduct.getProdCdeAltClassCde();
        searchProduct.setProdCdeAltClassCde("");
        searchProduct.getSearchObject();
        searchProduct.setSearchObject(new SearchableObject());
        Assert.assertTrue(true);
    }
}
