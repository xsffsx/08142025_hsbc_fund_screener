/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesPriceQuote;
import com.google.gson.JsonObject;

public class BeanUtilTest {

    @Test
    public void testGetFieldNameAndFieldTypeCorrectResult() {
        String key = "symbol(java.lang.String)";
        String fieldName = "symbol";
        String fieldType = "java.lang.String";
        String result;
        try {
            result = Whitebox.invokeMethod(BeanUtil.class, "getFieldName", key);
            assertThat(result, equalTo(fieldName));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
        try {
            result = Whitebox.invokeMethod(BeanUtil.class, "getFieldType", key);
            assertThat(result, equalTo(fieldType));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetSetMethodNameCorrectResult() {
        String field = "symbol";
        String setMehtodName = "setSymbol";
        try {
            String result = Whitebox.invokeMethod(BeanUtil.class, "getSetMethodName", field);
            assertThat(result, equalTo(setMehtodName));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

    }

    @Test
    public void testGetValueCorrectResult() {
        String value = "America/New_York";
        String fieldType = "java.lang.String";
        Object result;
        try {
            result = Whitebox.invokeMethod(BeanUtil.class, "getValue", fieldType, value);
            assertThat(result, instanceOf(String.class));
            assertThat(result, equalTo(value));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        value = "0.100";
        fieldType = "java.math.BigDecimal";
        try {
            result = Whitebox.invokeMethod(BeanUtil.class, "getValue", fieldType, value);
            assertThat(result, instanceOf(BigDecimal.class));
            assertThat(result, equalTo(new BigDecimal(value)));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testGetValueWithInvalidValueParameter() {
        String invalidValue = "";
        validate(invalidValue);

        invalidValue = " ";
        validate(invalidValue);

        invalidValue = null;
        validate(invalidValue);

        invalidValue = "null";
        validate(invalidValue);
    }

    private void validate(final String invalidValue) {
        String fieldType = "java.lang.String";
        Object result;
        try {
            result = Whitebox.invokeMethod(BeanUtil.class, "getValue", fieldType, invalidValue);
            assertThat(result.toString(), isEmptyString());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }

        fieldType = "java.math.BigDecimal";
        try {
            result = Whitebox.invokeMethod(BeanUtil.class, "getValue", fieldType, invalidValue);
            assertThat(result, nullValue());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

    @Test
    public void testJsonToBeanWithoutType() {
        QuotesPriceQuote priceQuote = new QuotesPriceQuote();
        Map<String, String[]> fieldMapper = new HashMap<>();
        fieldMapper.put("nominalPrice", new String[] {"OFF_CLOSE", "TRDPRC_1", "ADJUST_CLS"});
        fieldMapper.put("currency", new String[] {"CURRENCY"});
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("OFF_CLOSE", "");
        jsonObj.addProperty("TRDPRC_1", "14.500");
        jsonObj.addProperty("ADJUST_CLS", "15.400");
        jsonObj.addProperty("CURRENCY", "HKD");
        try {
            Whitebox.invokeMethod(BeanUtil.class, "jsonToBeanWithoutType", priceQuote, fieldMapper, jsonObj);
            assertThat(priceQuote.getCurrency(), equalTo("HKD"));
            assertThat(priceQuote.getNominalPrice(), equalTo(new BigDecimal("14.500")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception");
        }
    }

}
