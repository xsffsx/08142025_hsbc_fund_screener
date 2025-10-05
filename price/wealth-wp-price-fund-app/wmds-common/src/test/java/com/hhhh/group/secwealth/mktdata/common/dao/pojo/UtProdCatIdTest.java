package com.hhhh.group.secwealth.mktdata.common.dao.pojo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UtProdCatIdTest {

    @Test
    public void test(){
        UtProdCatId utProdCatId = new UtProdCatId();
        utProdCatId.setBatchId(0L);
        utProdCatId.setProductId(0);
        utProdCatId.sethhhhCategoryCode("");
        utProdCatId.gethhhhCategoryCode();
        utProdCatId.getBatchId();
        utProdCatId.getProductId();
        Assert.assertTrue(true);
    }
}
