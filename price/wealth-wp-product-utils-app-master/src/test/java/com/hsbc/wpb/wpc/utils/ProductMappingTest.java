package com.dummy.wpb.wpc.utils;


import org.junit.Assert;
import org.junit.Test;

public class ProductMappingTest{

    @Test
    public void testProductMapping() {
        ProductMapping.getTableInfoList();
        String prodId = ProductMapping.getProductIdField("PROD_TRADE_ELIG");
        Assert.assertEquals("PROD_ID",prodId);
    }


}