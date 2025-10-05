package com.hhhh.group.secwealth.mktdata.test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JSONUtil {

    private static ObjectMapper mapper;

    public static void setObjectMapper(ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    public static <T> T readValue(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            log.error("ObjectMapper read json with error", e);
        }
        return null;
    }

    public static String toString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("ObjectMapper to json with error", e);
        }
        return null;
    }

}
