package com.dummy.wpb.wpc.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeUtils {
    private static Map<String, String> cache = new ConcurrentHashMap<>();

    public static Map<String, Object> toCamelCase(Map<String, Object> map) {
        Map<String, Object> resultMap = new LinkedHashMap<>(map.size());
        map.forEach((k, v) -> resultMap.put(toCamelCase(k), v));
        return resultMap;
    }

    public static String toCamelCase(String text) {
        if (null == text) return null;
        return textToCamelCase(text);
    }

    private static String textToCamelCase(String text) {
        String result = cache.get(text);// introduce cache to speed up mess call
        if (null == result) {
            boolean lowerCase = true;
            StringBuilder sb = new StringBuilder();
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                lowerCase = isLowerCase(lowerCase, sb, chars, i);
            }
            result = sb.toString();
            cache.put(text, result);
        }
        return result;
    }

    private static boolean isLowerCase(boolean lowerCase, StringBuilder sb, char[] chars, int i) {
        char c = chars[i];
        if (i > 0 && ('a' <= chars[i - 1] && chars[i - 1] <= 'z') && ('A' <= c && c <= 'Z')) {     // eg. prodId, keep the I as uppercase
            sb.append(c);
        } else if (c == '-' || c == '_') {
            lowerCase = false;
        } else if ('0' <= c && c <= '9') {
            sb.append(c);
            lowerCase = false;
        } else {
            formatAToZ(lowerCase, sb, c);
            lowerCase = true;

        }
        return lowerCase;
    }

    private static void formatAToZ(boolean lowerCase, StringBuilder sb, char c) {
        if (lowerCase) {
            if ('A' <= c && c <= 'Z') {   // A-Z to a-z
                c = (char) ('a' + c - 'A');     // to lower case
            }
        } else {
            if ('a' <= c && c <= 'z') {   // a-z to A-Z
                c = (char) ('A' + c - 'a');     // to upper case
            }
        }
        sb.append(c);
    }
}
