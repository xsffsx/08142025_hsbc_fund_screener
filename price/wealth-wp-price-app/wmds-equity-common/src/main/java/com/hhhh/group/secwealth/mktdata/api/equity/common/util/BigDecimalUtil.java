/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;

import java.math.BigDecimal;
import java.util.StringTokenizer;

public class BigDecimalUtil {

    private BigDecimalUtil() {

    }

    /**
     *
     * <p>
     * <b> Avoid convert String to BigDecimal for invalid string throws
     * exception. If str parameter is invalid, return null. </b>
     * </p>
     *
     * @param str
     * @return
     */
    public static BigDecimal fromString(final String str) {
        BigDecimal result;
        if (StringUtil.isValidResp(str)) {
            result = new BigDecimal(str);
        } else {
            result = null;
        }
        return result;
    }

    private static BigDecimal parseBigDecimalReutersProc(String reuter_number, int nScale) {
        String sIntegerPart = "";
        String sFractionalPart = "";
        BigDecimal dDecimal = BigDecimal.valueOf(-1);
        // A fractional value with integer part and fractional part
        if (reuter_number.trim().indexOf(" ") == -1) { // String may be an
            // integer
            if (reuter_number.trim().indexOf("/") != -1) { // String is a
                // fractional value
                sIntegerPart = "0";
                sFractionalPart = reuter_number.trim();
            } else { // String is an integer
                sIntegerPart = reuter_number.trim();
                // dDecimal = new BigDecimal("0");
                dDecimal = BigDecimal.valueOf(0);
            }
        } else { // A fractional value with integer part and fractional part
            sIntegerPart = reuter_number.substring(0, reuter_number.indexOf(" "));
            sFractionalPart = reuter_number.substring(reuter_number.indexOf(" ") + 1, reuter_number.length());
        }

        // Calculate value of fractional part
        if (sFractionalPart.length() > 0) {
            StringTokenizer token = new StringTokenizer(sFractionalPart, "/");
            int i = 0;

            // A fraction number must have two parts separate by a "/" e.g 1/32
            if (token.countTokens() != 2) {
                throw new ApplicationException(ExCodeConstant.EX_CODE_UNDEFINED);
            }

            while (token.hasMoreElements()) {
                if (i == 0) {
                    dDecimal = new BigDecimal(token.nextToken().trim());
                    i++;
                } else {
                    String sTemp;
                    sTemp = token.nextToken().trim();
                    // Check divided by zero
                    if (Double.parseDouble(sTemp) > -1e-64 && Double.parseDouble(sTemp) < 1e-64) {
                        throw new ApplicationException(ExCodeConstant.EX_CODE_UNDEFINED);
                    }
                    dDecimal = dDecimal.divide(new BigDecimal(sTemp), nScale, BigDecimal.ROUND_HALF_UP);
                }
            }
        }

        BigDecimal dDecimalNumber = new BigDecimal(sIntegerPart);
        dDecimalNumber = dDecimalNumber.add(dDecimal);
        return dDecimalNumber;
    }

    public static BigDecimal fromStringAndCheckNull(String s) {
        BigDecimal result = null;
        if (StringUtil.isValid(s)) {
            try {
                if(s.contains(",")){
                    s = s.replace(",", "");
                }
                result = new BigDecimal(s);
                if(0==result.compareTo(BigDecimal.ZERO)){
                    result = new BigDecimal(0);
                }
            } catch (Exception e) {
                result = null;
            }
        }
        return result;
    }

}
