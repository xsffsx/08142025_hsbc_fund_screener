package com.hhhh.group.secwealth.mktdata.common.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public final class JacksonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // JacksonUtil.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JacksonUtil.mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JacksonUtil.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String beanToJson(final T obj) throws Exception {
        try {
            return JacksonUtil.mapper.writeValueAsString(obj);
        } catch (Exception e) {
            String msg = "CommonException: JacksonUtil, javaBean2Json, Param: " + obj.toString();
            LogUtil.error(JacksonUtil.class, msg, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, msg);
        }
    }

    public static <T> T jsonToBean(final String json, final Class<T> clazz) throws Exception {
        try {
            return JacksonUtil.mapper.readValue(json, clazz);
        } catch (Exception e) {
            String msg = "CommonException: JacksonUtil, json2javaBean, Param: " + json + ", " + clazz.toString();
            LogUtil.error(JacksonUtil.class, msg, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, msg);
        }
    }

    public static Map<String, Object> jsonToMap(final String json) throws Exception {
        try {
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
            HashMap<String, Object> map = JacksonUtil.mapper.readValue(json, typeRef);
            return map;
        } catch (Exception e) {
            String msg = "CommonException: JacksonUtil, json2Map, Param: " + json;
            LogUtil.error(JacksonUtil.class, msg, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, msg);
        }
    }

    public static <T> List<T> jsonToList(final String json, final Class<T> clazz) throws Exception {
        try {
            CollectionType listType = JacksonUtil.mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> list = JacksonUtil.mapper.readValue(json, listType);
            return list;
        } catch (Exception e) {
            String msg = "CommonException: JacksonUtil, jsonToList, Param: " + json;
            LogUtil.error(JacksonUtil.class, msg, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, msg);
        }
    }
}
