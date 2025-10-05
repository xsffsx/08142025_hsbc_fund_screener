package com.hhhh.group.secwealth.mktdata.common.util;


import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JacksonUtilTest {

    @Test
    public void testBeanToJson() {
        InternalProductKey param = new InternalProductKey();
        param.setCountryCode("2543");

        try {
            String ret = JacksonUtil.beanToJson(param);
            assertEquals("{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}", ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonToBean() {
        try {
            InternalProductKey internalProductKey = JacksonUtil.jsonToBean("{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}", InternalProductKey.class);
            assertEquals("2543",internalProductKey.getCountryCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testJsonToMap() {
        try {
            Map<String, Object> ret = JacksonUtil.jsonToMap("{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}");
            assertEquals("2543",ret.get("countryCode"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testJsonToList() {
        try {
            List<InternalProductKey> rets = JacksonUtil.jsonToList("[{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}]", InternalProductKey.class);
            assertEquals( "2543",rets.get(0).getCountryCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Test
    public void testJsonToBeanEx() {
        Boolean flage = false;
        try {
            JacksonUtil.jsonToBean("ex{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}", InternalProductKey.class);
        } catch (Exception e) {
            flage = true;
        }
        org.junit.Assert.assertTrue(flage);
    }

    @Test
    public void testJsonToMapEx() {
        Boolean flage = false;

        try {
            JacksonUtil.jsonToMap("ex{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}");
        } catch (Exception e) {
            flage = true;
        }
        org.junit.Assert.assertTrue(flage);
    }

    @Test
    public void testJsonToListEx() {
        Boolean flage = false;
        try {
            JacksonUtil.jsonToList("1[{\"countryCode\":\"2543\",\"groupMember\":null,\"countryTradableCode\":null,\"productType\":null,\"prodCdeAltClassCde\":null,\"prodAltNum\":null}]", InternalProductKey.class);
        } catch (Exception e) {
            flage = true;
        }
        org.junit.Assert.assertTrue(flage);
    }

}
