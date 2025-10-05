package com.dummy.wmd.wpc.graphql.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapperUtils {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String writeValueAsString(Object value) {
        return objectMapper.writeValueAsString(value);
    }

    @SneakyThrows(IOException.class)
    public static <T> T readValue(String json, Class<T> toValueType) {
        return objectMapper.readValue(json, toValueType);
    }

    @SneakyThrows(IOException.class)
    public static <T> T readValue(String json,TypeReference<T> typeReference) {
        return objectMapper.readValue(json, typeReference);
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
        return objectMapper.convertValue(fromValue, typeReference);
    }
}
