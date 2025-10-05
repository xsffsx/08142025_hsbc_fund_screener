/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2018. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.util;

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
public final class JsonUtil2 {

    private JsonUtil2() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Gson gson = new Gson();

    private static final String EMPTY_STRING = "";

    static {
        JsonUtil2.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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

    public static JsonNode readTree(final String content) throws IOException {
        return JsonUtil2.objectMapper.readTree(content);
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
        return JsonUtil2.objectMapper.readValue(content, valueType);
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
        return JsonUtil2.objectMapper.readValue(content, type);
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
        return JsonUtil2.objectMapper.writeValueAsString(obj);
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
        JsonElement jsonElement = JsonUtil2.gson.fromJson(content, JsonElement.class);
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
        return JsonUtil2.gson.fromJson(content, valueType);
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
        return JsonUtil2.gson.fromJson(content, type);
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
        return JsonUtil2.gson.toJson(obj);
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
            return JsonUtil2.EMPTY_STRING;
        }
        JsonElement jsonElement = jsonObj.get(field);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return JsonUtil2.EMPTY_STRING;
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
