package com.dummy.wpb.product.util;

import com.dummy.wpb.product.model.xml.ProductKeySegment;
import com.dummy.wpb.product.model.xml.ProductUserDefineFieldExtendSegment;
import com.dummy.wpb.product.model.xml.UnitTrustInstrument;
import org.bson.Document;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
public class DocumentUtilTest {

    @Test
    public void testParseObject() {
        UnitTrustInstrument unitTrustInstrument = new UnitTrustInstrument();
        ProductKeySegment productKeySegment = new ProductKeySegment();
        productKeySegment.setCtryRecCde("HK");
        unitTrustInstrument.setProdKeySeg(productKeySegment);
        ProductUserDefineFieldExtendSegment userDefSeg = new ProductUserDefineFieldExtendSegment();
        userDefSeg.setFieldCde("lastDealDt");
        userDefSeg.setFieldValue("20230123");
        Document document = DocumentUtil.parseObject(unitTrustInstrument);
        Assert.assertEquals("HK", document.get("ctryRecCde"));
    }
}