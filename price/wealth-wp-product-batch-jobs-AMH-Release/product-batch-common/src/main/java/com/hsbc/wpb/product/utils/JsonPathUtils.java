package com.dummy.wpb.product.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonPathUtils {

    public static <T> T readValue(Map<?, ?> map, String jsonPath) {

        try {
            return JsonPath.read(map, jsonPath);
        } catch (JsonPathException|IllegalArgumentException e) {
            // just return null
            return null;
        }
    }

    public static <T> T readValue(Map<?, ?> map, String jsonPath, T defaultValue) {
        T value = readValue(map, jsonPath);
        return Objects.nonNull(value) ? value : defaultValue;
    }

    public static void deleteValue(Map<?, ?> obj, String jsonPath) {
        try {
            JsonPath.parse(obj).delete(jsonPath);
        } catch (PathNotFoundException e) {
            // ignore that
        }

    }

    public static void setValue(Map<?, ?> obj, String jsonPath, Object value) {
        DocumentContext context = JsonPath.parse(obj);
        Matcher arrayMatcher = Pattern.compile("^(\\$\\.)?[a-zA-Z0-9]+\\[(?<index>\\d)](|(\\.[a-zA-Z0-9]+))$").matcher(jsonPath);
        if (arrayMatcher.find()) {
            String[] arraySplits = jsonPath.split("\\[(\\d)](.?)");
            //eg: ctryProdTradeCde[0]
            String parent = arraySplits[0];
            int index = Integer.parseInt(arrayMatcher.group("index"));

            Object actualValue = readValue(obj, parent);

            if (!(actualValue instanceof List)) {
                actualValue = new ArrayList<>();
            }

            List<Object> parentValue = (List<Object>) actualValue;
            while (parentValue.size() - 1 < index) {
                parentValue.add(null);
            }

            if (arraySplits.length > 1) {
                // like: tradeCcy[0].ccyProdTradeCde
                String fieldName = arraySplits[1];
                Map<String, Object> map = (Map<String, Object>) Optional.ofNullable(parentValue.get(index)).orElse(new LinkedHashMap<>());
                map.put(fieldName, value);
                parentValue.set(index, map);
            } else {
                // like: ctryProdTradeCde[0]
                parentValue.set(index, value);
            }
            setValue(obj, parent, parentValue);
        } else if (jsonPath.contains(".")) {
            //eg: performance.perfm1YrAmt
            String parent = jsonPath.substring(0, jsonPath.lastIndexOf("."));
            if (null == readValue(obj, parent)) {
                setValue(obj, parent, new Document()); // make sure parent exist
            }
            String attr = jsonPath.substring(jsonPath.lastIndexOf(".") + 1);
            context.put(parent, attr, value);
        } else {
            context.put("$", jsonPath, value);
        }
    }

    public static void setValueIfAbsent(Map<?, ?> obj, String jsonPath, Object value) {
        if (null == readValue(obj, jsonPath)) {
            setValue(obj, jsonPath, value);
        }
    }

    public static void copyValue(Map<?, ?> sourceObj, Map<?, ?> targetObj, String jsonPath) {
        copyValue(sourceObj, targetObj, jsonPath, true);
    }

    public static void copyValue(Map<?, ?> sourceObj, Map<?, ?> targetObj, String jsonPath, boolean nullCovering) {
        Object sourceValue = JsonPathUtils.readValue(sourceObj, jsonPath);
        if (null != sourceValue || nullCovering) {
            JsonPathUtils.setValue(targetObj, jsonPath, sourceValue);
        }
    }
}
