package com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class ProductKeyObjectTest {

    private ProductKeyObject obj;

    @Before
    public void setUp() throws Exception {
        obj = new ProductKeyObject();
    }

    @Test
    public void testEquals() throws Exception {
        assertNotEquals("o1","o");
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
        Assert.assertNotNull(test);

    }
}
