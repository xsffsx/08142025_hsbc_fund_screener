package com.dummy.wpb.wpc.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminControl {
     public static boolean validateSecretKey(String key) {
        String expectedKey = System.getProperty("admin.secret.key");
        return key.equals(expectedKey);
    }
}
