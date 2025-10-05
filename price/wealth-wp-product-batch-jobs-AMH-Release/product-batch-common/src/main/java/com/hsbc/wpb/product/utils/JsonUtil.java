package com.dummy.wpb.product.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.dummy.wpb.product.exception.productBatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class JsonUtil {

    private static ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();



    /**
     * convert Java object to Json String.
     *
     * @param obj java object
     * @return json string
     * @throws JsonProcessingException
     */
    public static String convertObject2Json(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * convert Json string to Java object
     *
     * @param json       json string
     * @param targetType corresponding java type
     * @return java object
     * @throws IOException exception when failed to convert
     */

    public static <T> T convertJson2Object(String json,
                                           Class<T> targetType) throws IOException {
        return objectMapper.readValue(json, targetType);
    }

    public static <T> T convertJson2Object(String json, TypeReference<T> toValueTypeRef) {
        try {
            return objectMapper.readValue(json, toValueTypeRef);
        } catch (JsonProcessingException e) {
            throw new productBatchException("An error occurred when parsing JSON: " + e.getMessage(), e);
        }
    }

    public static <T> T deepCopy(T object) {
        if (null == object) {
            return null;
        }
        try {
            return (T) convertType(object, object.getClass());
        } catch (IOException e) {
            throw new productBatchException(e);
        }
    }

    public static <T> T deepCopy(T object, TypeReference<T> toValueTypeRef) {
        try {
            return convertJson2Object(convertObject2Json(object), toValueTypeRef);
        } catch (JsonProcessingException e) {
            throw new productBatchException(e);
        }
    }


    /**
     * convert json string to java object by using JavaType, this is for generic
     * collection which elements is defined in your own framework.
     *
     * @param json json string
     * @param type target type
     * @return java object
     * @throws IOException exception when failed to convert
     */
    public static Object convertJson2Object(String json,
                                            JavaType type) throws IOException {
        return objectMapper.readValue(json, type);
    }

    public static <T> T convertType(Object srcObj, Class<T> targetClass) throws IOException {
        return convertJson2Object(convertObject2Json(srcObj), targetClass);
    }

    public static <T> T convertType(Object srcObj, TypeReference<T> typeReference) throws IOException {
        return convertJson2Object(convertObject2Json(srcObj), typeReference);
    }
}
