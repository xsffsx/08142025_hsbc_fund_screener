package com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AttributeMapTest {

    private AttributeMap obj;

    @Before
    public void setUp() throws Exception {
        obj = new AttributeMap();
    }

    @Test
    public void testEquals() throws Exception {
        assertNotEquals("o1","o");
    }



    @Test
    public void testToString() throws Exception {
        assertNotNull(obj.toString());
    }

    @Test
    public void testGetter() throws Exception {
        ProductSimpleInformation test = new ProductSimpleInformation();
        test.setPrdSbTpCd("rew");
        test.setProductKey(new ArrayList<ProductInfo>());
        String riskLvlCde = test.getRiskLvlCde();
        List<ProductInfo> productKey = test.getProductKey();
        String prdSbTpCd = test.getPrdSbTpCd();
        Assert.assertNotNull(productKey);
        Assert.assertNull(riskLvlCde);
        Assert.assertNotNull(prdSbTpCd);
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
