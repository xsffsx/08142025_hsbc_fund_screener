/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import java.math.BigDecimal;

public final class BigDecimalUtil {

    private BigDecimalUtil() {}

    private static int DEFAULT_SCALE = 11;

    /** 7 decimal places. */
    public static final int BIGDECIMAL_SCALE_8 = 8;

    /** 4 decimal places. */
    public static final int BIGDECIMAL_SCALE_5 = 5;

    /** 3 decimal places. */
    public static final int BIGDECIMAL_SCALE_4 = 4;

    public static BigDecimal divide(final BigDecimal dividend, final BigDecimal divisor, final Integer scale) {
        BigDecimal response = null;
        if (null != dividend && null != divisor && !BigDecimal.ZERO.equals(divisor)) {
            try {
                response = dividend.divide(divisor);
            } catch (Exception e) {
                if (null != scale) {
                    response = dividend.divide(divisor, scale, BigDecimal.ROUND_HALF_UP);
                } else {
                    response = dividend.divide(divisor, BigDecimalUtil.DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        return response;
    }

    public static BigDecimal multiply(final BigDecimal multiplier, final BigDecimal multiplicand) {
        BigDecimal response = null;
        if (null != multiplier && null != multiplicand) {
            response = multiplier.multiply(multiplicand);
        }
        return response;
    }

    public static BigDecimal invert(final BigDecimal divisor, final int scale) {
        return divide(BigDecimal.ONE, divisor, scale);
    }

    public static String toStringAndCheckNull(final BigDecimal b) {
        String result = null;
        if (null != b) {
            result = b.toPlainString();
        }
        return result;
    }

    public static BigDecimal fromStringAndCheckNull(String s) {
        BigDecimal result = null;
        if (StringUtil.isValid(s)) {
            try {
                if (s.contains(",")) {
                    s = s.replace(",", "");
                }
                result = new BigDecimal(s);
                if (0 == result.compareTo(BigDecimal.ZERO)) {
                    result = new BigDecimal(0);
                }
            } catch (Exception e) {
                LogUtil.error(BigDecimalUtil.class, "fromStringAndCheckNull error:", e);
                result = null;
            }
        }
        return result;
    }

    public static String divideByHundred(final BigDecimal b) {
        return BigDecimalUtil.toStringAndCheckNull(BigDecimalUtil.divide(b, BigDecimal.valueOf(100L), null));
    }

    public static BigDecimal multiplyByHundred(final BigDecimal b) {
        return BigDecimalUtil.multiply(b, BigDecimal.valueOf(100L));
    }

}
