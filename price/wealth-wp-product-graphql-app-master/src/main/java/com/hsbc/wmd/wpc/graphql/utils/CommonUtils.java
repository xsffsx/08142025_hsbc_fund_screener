package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class CommonUtils {
    private CommonUtils() {}

    public static Document readResourceAsDocument(String classpath) {
        String text = CommonUtils.readResource(classpath);
        return Document.parse(text);
    }

    public static String readResource(String classpath) {
        try {
            return StreamUtils.copyToString(new ClassPathResource(classpath).getInputStream(), Charset.defaultCharset());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Resource not found: " + classpath);
        } catch (Exception e) {
            throw new productErrorException(productErrors.RuntimeException, "Error reading resource: " + classpath);
        }
    }

    public static String canonicalPath(String path){
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            throw new productErrorException(productErrors.RuntimeException, "Failed to convert a path to canonical path: " + path);
        }
    }

    public static Path toCanonicalPath(Path path){
        try {
            String canonicalPath = path.toFile().getCanonicalPath();
            return Paths.get(canonicalPath);
        } catch (IOException e) {
            throw new productErrorException(productErrors.RuntimeException, "Failed to convert a path to canonical path: " + path);
        }
    }

    public static String extractStringValue(Map<String, Object> dataMap, String key) {
        Object value = dataMap.get(key);
        return Objects.isNull(value) ? null : String.valueOf(value);
    }
}
