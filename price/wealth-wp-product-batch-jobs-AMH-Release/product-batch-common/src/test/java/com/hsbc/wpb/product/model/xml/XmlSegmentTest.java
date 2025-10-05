package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

public class XmlSegmentTest {

    @Test
    public void prodKeySegTest() throws Exception {
        ProductKeySegment expected = new ProductKeySegment();
        expected.setCtryRecCde("HK");
        expected.setGrpMembrRecCde("HBAP");
        expected.setProdTypeCde("UT");
        expected.setProdCde("123456");
        expected.setProdCdeAltClassCde("P");
        expected.setCtryProdTradeCde("US");
        expected.setCcyProdCde("HKD");

        ProductKeySegment actual = JsonUtil.convertJson2Object(CommonUtils.readResource("/prodKeySeg.json"), ProductKeySegment.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void prodAltNumSegTest() throws Exception {
        ProductAlternativeNumberSegment expected = new ProductAlternativeNumberSegment();
        expected.setProdAltNum("SG7U32949426");
        expected.setProdCdeAltClassCde("M");

        ProductAlternativeNumberSegment actual = JsonUtil.convertJson2Object(CommonUtils.readResource("/prodAltNumSeg.json"), ProductAlternativeNumberSegment.class);
        Assert.assertEquals(expected, actual);
    }
}
