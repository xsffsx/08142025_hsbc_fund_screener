package com.dummy.wpb.wpc.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("java:S115")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Crypto {
    public static Crypto getInstance(){
        return new Crypto();
    }
    public static final String key = "ENCRYPT_KEY";
}
