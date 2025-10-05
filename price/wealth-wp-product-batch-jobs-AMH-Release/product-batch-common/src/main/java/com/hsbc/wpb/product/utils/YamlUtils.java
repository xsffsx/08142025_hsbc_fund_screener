package com.dummy.wpb.product.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wpb.product.exception.productBatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URL;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class YamlUtils {

    private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());


    public static <T> T readResource(String classpath, Class<T> clazz) {
        URL yamlUrl = YamlUtils.class.getResource(classpath);
        if (null == yamlUrl) {
            throw new IllegalArgumentException("Resource not found: " + classpath);
        }
        try {
            return yamlMapper.readValue(yamlUrl, clazz);
        } catch (IOException e) {
            throw new productBatchException("Error reading yaml resource: " + classpath, e);
        }
    }

    public static <T> T readResource(String classpath, TypeReference<T> typeReference) {
        URL yamlUrl = YamlUtils.class.getResource(classpath);
        if (null == yamlUrl) {
            throw new IllegalArgumentException("Resource not found: " + classpath);
        }
        try {
            return yamlMapper.readValue(yamlUrl, typeReference);
        } catch (IOException e) {
            throw new productBatchException("Error reading yaml resource: " + classpath, e);
        }
    }
}
