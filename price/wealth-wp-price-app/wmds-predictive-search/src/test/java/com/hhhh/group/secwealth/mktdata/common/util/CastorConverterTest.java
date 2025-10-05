package com.hhhh.group.secwealth.mktdata.common.util;

import org.junit.Test;


public class CastorConverterTest {

    @Test
    public void testCastorBeanToXMLConvert() {
        Boolean flag = false;
        try {
            CastorConverter.castorBeanToXMLConvert(new Object(), "");
        } catch (Exception e) {
            flag = true;
        }
        org.junit.Assert.assertTrue(flag);
    }

    @Test
    public void testCastorXMLToBeanConvert() {
        Boolean flag = false;
        try {
            CastorConverter.castorXMLToBeanConvert("sourceStream",
                    "responseMapContent", CastorConverterTest.class, true);
        } catch (Exception e) {
            flag = true;
        }
        org.junit.Assert.assertTrue(flag);

    }
}
