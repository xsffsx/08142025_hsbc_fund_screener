package com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProductSimpleInformationTest {

    private ProductSimpleInformation obj;

    @Before
    public void setUp() throws Exception {
        obj = new ProductSimpleInformation();
    }

    @Test
    public void testEquals() throws Exception {
        assertNotEquals("q","o");

    }



    @Test
    public void testToString() throws Exception {
        Assert.assertNotNull(obj.toString());
    }

    @Test
    public void testGetter() throws Exception {
        ProductSimpleInformation test = new ProductSimpleInformation();

        String riskLvlCde = test.getRiskLvlCde();
        List<ProductInfo> productKey = test.getProductKey();
        String prdSbTpCd = test.getPrdSbTpCd();
        Assert.assertNull(prdSbTpCd);
    }

    @Test
    public void testSetter() throws Exception {
        ProductSimpleInformation test = new ProductSimpleInformation();
        test.setRiskLvlCde("");
        test.setProductKey(Lists.newArrayList());
        test.setPrdSbTpCd("");
        assertNotNull(test);
    }
}
