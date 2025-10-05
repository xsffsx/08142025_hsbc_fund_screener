/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * <p>
 * <b> Because Jackson's ObjectMapper and Gson's Gson class is thread-safe, so
 * use ObjectMapper and Gson instance as a static field in util bean. </b>
 * </p>
 */
public final class JsonUtil {

    private JsonUtil() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Gson gson = new Gson();

    private static final String EMPTY_STRING = "";

    static {
        JsonUtil.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     *
     * <p>
     * <b> BUG: Deserializing BigDecimal using JsonNode loses precision. </b>
     * </p>
     *
     * @param content
     * @return
     * @throws IOException
     */
    @Deprecated
    public static JsonNode readTree(final String content) throws IOException {
        return JsonUtil.objectMapper.readTree(content);
    }

    /**
     *
     * <p>
     * <b> Call Jackson's ObjectMapper.readValue(String content, Class
     * <T> valueType) method. </b>
     * </p>
     *
     * @param content
     * @param valueType
     * @return
     * @throws IOException
     */
    public static <T> T readValue(final String content, final Class<T> valueType) throws IOException {
        return JsonUtil.objectMapper.readValue(content, valueType);
    }

    /**
     *
     * <p>
     * <b> Call Jackson's ObjectMapper.readValue(String content, TypeReference
     * valueTypeRef) method. </b>
     * </p>
     *
     * @param content
     * @param type
     * @return
     * @throws IOException
     */
    public static <T> T readValue(final String content, final TypeReference<T> type) throws IOException {
        return JsonUtil.objectMapper.readValue(content, type);
    }

    /**
     *
     * <p>
     * <b> Call Jackson's ObjectMapper.writeValueAsString(Object value)
     * method. </b>
     * </p>
     *
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String writeValueAsString(final Object obj) throws JsonProcessingException {
        return JsonUtil.objectMapper.writeValueAsString(obj);
    }

    /**
     *
     * <p>
     * <b> Convert String to JsonObject. </b>
     * </p>
     *
     * @param content
     * @return
     */
    public static JsonObject getAsJsonObject(final String content) {
        JsonElement jsonElement = JsonUtil.gson.fromJson(content, JsonElement.class);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        if (!jsonElement.isJsonObject()) {
            return null;
        }
        return jsonElement.getAsJsonObject();
    }

    /**
     *
     * <p>
     * <b> Call Gson's Gson.fromJson(String json, Class<T> classOfT)
     * method. </b>
     * </p>
     *
     * @param content
     * @param valueType
     * @return
     */
    public static <T> T fromJson(final String content, final Class<T> valueType) {
        return JsonUtil.gson.fromJson(content, valueType);
    }

    /**
     *
     * <p>
     * <b> Call Gson's Gson.fromJson(String json, Type typeOfT) method. </b>
     * </p>
     *
     * @param content
     * @param type
     * @return
     */
    public static <T> T fromJson(final String content, final Type type) {
        return JsonUtil.gson.fromJson(content, type);
    }

    /**
     *
     * <p>
     * <b> Call Gson's Gson.toJson(Object src) method. </b>
     * </p>
     *
     * @param obj
     * @return
     */
    public static String toJson(final Object obj) {
        return JsonUtil.gson.toJson(obj);
    }

    /**
     * <p>
     * <b> Get string value use given field from JsonObject. </b>
     * </p>
     *
     * @param jsonObj
     * @param field
     * @return
     */
    public static String getAsString(final JsonObject jsonObj, final String field) {
        if (jsonObj == null) {
            return JsonUtil.EMPTY_STRING;
        }
        JsonElement jsonElement = jsonObj.get(field);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return JsonUtil.EMPTY_STRING;
        }
        return jsonElement.getAsString();
    }

    /**
     *
     * <p>
     * <b> Get JsonArray use given field from JsonObject. </b>
     * </p>
     *
     * @param jsonObj
     * @param field
     * @return
     */
    public static JsonArray getAsJsonArray(final JsonObject jsonObj, final String field) {
        if (jsonObj == null) {
            return null;
        }
        JsonElement jsonElement = jsonObj.get(field);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return null;
        }
        if (!jsonElement.isJsonArray()) {
            return null;
        }
        return jsonElement.getAsJsonArray();
    }

}
