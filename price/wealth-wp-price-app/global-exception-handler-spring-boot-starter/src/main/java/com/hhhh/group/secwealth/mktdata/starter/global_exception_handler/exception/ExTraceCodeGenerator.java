/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception;

import java.math.BigInteger;
import java.util.Random;

public final class ExTraceCodeGenerator {

    private ExTraceCodeGenerator() {}

    public static String generate() {
        final Random random = new Random();
        final byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        return new BigInteger(1, bytes).toString();
    }

}
