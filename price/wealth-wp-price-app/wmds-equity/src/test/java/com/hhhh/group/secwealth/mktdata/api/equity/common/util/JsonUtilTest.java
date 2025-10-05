/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
public class JsonUtilTest {

    @Test
    @SuppressWarnings("deprecation")
    public void testReadTreeLosesPrecision() throws IOException {
        String key = "demo";
        String value = "0.10";
        String content = "{\"" + key + "\":" + value + "}";
        String result = JsonUtil.readTree(content).get(key).asText();
        assertThat(result, not(value));
    }

    @Test
    public void testGetAsJsonObjectWithInvalidContentParameter() {
        String invalidContent = null;
        JsonObject result = JsonUtil.getAsJsonObject(invalidContent);
        assertThat(result, nullValue());

        invalidContent = "";
        result = JsonUtil.getAsJsonObject(invalidContent);
        assertThat(result, nullValue());
    }

    @Test
    public void testGetAsStringWithInvalidJsonObjectParameter() {
        JsonObject invalidJsonObject = null;
        String field = "symbol";

        String result = JsonUtil.getAsString(invalidJsonObject, field);
        assertThat(result, isEmptyString());
    }

    @Test
    public void testGetAsStringWithInvalidFieldParameter() {
        JsonObject jsonObj = new JsonObject();

        String invalidField = null;
        String result = JsonUtil.getAsString(jsonObj, invalidField);
        assertThat(result, isEmptyString());

        invalidField = "";
        result = JsonUtil.getAsString(jsonObj, invalidField);
        assertThat(result, isEmptyString());

        invalidField = "non-exist-field";
        result = JsonUtil.getAsString(jsonObj, invalidField);
        assertThat(result, isEmptyString());
    }

    @Test
    public void testGetAsJsonArrayCorrectResult() {
        String field = "result";
        String content = "{'" + field + "':['1','2','3']}";
        JsonObject jsonObj = JsonUtil.getAsJsonObject(content);
        JsonArray result = JsonUtil.getAsJsonArray(jsonObj, field);
        assertThat(result.size(), equalTo(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetAsJsonArrayWithEmptyArray() {
        String field = "result";
        String content = "{'" + field + "':[]}";
        JsonObject jsonObj = JsonUtil.getAsJsonObject(content);
        JsonArray jsonArray = JsonUtil.getAsJsonArray(jsonObj, field);
        jsonArray.get(0).getAsJsonObject();
    }

    @Test
    public void testGetAsJsonArrayWithInvalidJsonObjectParameter() {
        JsonObject invalidJsonObject = null;
        String field = "result";

        JsonArray result = JsonUtil.getAsJsonArray(invalidJsonObject, field);
        assertThat(result, nullValue());
    }

    @Test
    public void testGetAsJsonArrayWithInvalidFieldParameter() {
        JsonObject jsonObj = new JsonObject();

        String invalidField = null;
        JsonArray result = JsonUtil.getAsJsonArray(jsonObj, invalidField);
        assertThat(result, nullValue());

        invalidField = "";
        result = JsonUtil.getAsJsonArray(jsonObj, invalidField);
        assertThat(result, nullValue());

        invalidField = "non-exist-field";
        result = JsonUtil.getAsJsonArray(jsonObj, invalidField);
        assertThat(result, nullValue());
    }

    @Test
    public void testGetAsJsonArrayWithInvalidFormatContent() {
        JsonObject jsonObj = new JsonObject();
        String field = "symbol";
        jsonObj.addProperty(field, "00005");

        JsonArray result = JsonUtil.getAsJsonArray(jsonObj, field);
        assertThat(result, nullValue());
    }

}
