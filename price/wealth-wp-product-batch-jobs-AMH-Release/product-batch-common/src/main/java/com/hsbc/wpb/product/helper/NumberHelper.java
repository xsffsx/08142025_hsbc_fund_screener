package com.dummy.wpb.product.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class NumberHelper {

    public static Double toDouble(String s) {
        Double d = null;
        if (StringUtils.isNotBlank(s)) {
            try {
                d = Double.valueOf(s);
            } catch (NumberFormatException e) {
                log.warn("Failed to parse String into Double. {}", e.getMessage());
            }
        }
        return d;
    }
}
