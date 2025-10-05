/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.utlis;
public class StringUtil {

    private static final String NULL_STRING = "null";

    private static final String EMPTY_STRING = "";

    private StringUtil() {

    }

    /**
     *
     * <p>
     * <b> If string is not null and empty, return true. </b>
     * </p>
     *
     * @param str
     * @return
     */
    public static boolean isValid(final String str) {
        return !isInValid(str);
    }

    /**
     *
     * <p>
     * <b> If string is null or empty, return true. </b>
     * </p>
     *
     * @param str
     * @return
     */
    public static boolean isInValid(final String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     *
     * <p>
     * <b> Usually used to determine whether the value in the response returned
     * by Vendor is valid or not. </b>
     * </p>
     *
     * @param str
     * @return
     */
    public static boolean isValidResp(final String str) {
        return !isInValidResp(str);
    }

    /**
     *
     * <p>
     * <b> Usually used to determine whether the value in the response returned
     * by Vendor is invalid or not. </b>
     * </p>
     *
     * @param str
     * @return
     */
    public static boolean isInValidResp(final String str) {

        return str == null || str.trim().isEmpty() || StringUtil.NULL_STRING.equalsIgnoreCase(str);
    }

    /**
     *
     * <p>
     * <b> Usually used to format the string value in the response returned by
     * Vendor. If str parameter is invalid, return empty string. </b>
     * </p>
     *
     * @param str
     * @return
     */
    public static String fromRespStr(final String str) {
        if (isValidResp(str)) {
            return str;
        }
        return StringUtil.EMPTY_STRING;
    }

}
