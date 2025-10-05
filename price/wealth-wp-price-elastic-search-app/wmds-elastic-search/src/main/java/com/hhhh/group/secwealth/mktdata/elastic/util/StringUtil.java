/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.InitialContext;
import org.apache.commons.lang3.StringUtils;


/**
 * The Class StringUtil.
 */
public final class StringUtil {

    private StringUtil() {}

    private static String serverName = null;
    private static String serverPath = null;

    /**
     * 
     * <p>
     * <b> Append suffix for the parameter name which is a array. </b>
     * </p>
     * 
     * @param name
     * @return
     */
    public static String getOriginalName(final String name) {
        return new StringBuilder(name).append(CommonConstants.ORIGINAL_FIELD_SUFFIX).toString();
    }

    public static String getTokenName(final String name) {
        return new StringBuilder(name).append(CommonConstants.TOKEN_FIELD_SUFFIX).toString();
    }

    /**
     * <p>
     * <b> Check the String array if contains the field. </b>
     * </p>
     * 
     * @param array
     * @param field
     * @return
     */
    public static boolean ifContainField(final String[] array, final String field) {
        if (null != array) {
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(field)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * combine String fields with a symbol
     * 
     * @return String
     */
    public static String appendParamsWithSymbol(final List<List<String>> params, final String symbol) {
        String result = null;

        if (params.isEmpty() || StringUtils.isBlank(symbol)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (List<String> param : params) {
            sb.append(",");
            int size = param.size();
            for (int i = 0; i < size; i++) {
                String temp = param.get(i);
                sb.append(temp);

                if (i < size - 1) {
                    sb.append(symbol);
                }
            }
        }
        if (sb.length() > 0) {
            result = sb.substring(1);
        }

        return result;
    }

    /**
     * combine String fields with vertical
     * 
     * @return String
     */
    public static String combineWithVertical(final String... params) {
        if (null != params) {
            int length = params.length;
            if (length == 1) {
                return params[0];
            } else {
                StringBuilder sb = new StringBuilder(params[0]);
                for (int i = 1; i < length; i++) {
                    sb.append(CommonConstants.SYMBOL_VERTICAL).append(params[i]);
                }
                return sb.toString();
            }
        } else {
            return null;
        }
    }

    /**
     * combine two String fields with underline
     */
    public static String combineWithUnderline(final String... params) {
        if (null != params) {
            int length = params.length;
            if (length == 1) {
                return params[0];
            } else {
                StringBuilder sb = new StringBuilder(params[0]);
                for (int i = 1; i < length; i++) {
                    if (StringUtil.isValid(params[i])) {
                        sb.append(CommonConstants.SYMBOL_UNDERLINE).append(params[i]);
                    }
                }
                return sb.toString();
            }
        } else {
            return null;
        }
    }

    /**
     * combine two String fields with dot
     */
    public static String combineWithDot(final String... params) {
        if (null != params) {
            int length = params.length;
            if (length == 1) {
                return params[0];
            } else {
                StringBuilder sb = new StringBuilder(params[0]);
                for (int i = 1; i < length; i++) {
                    if (null != params[i] && !CommonConstants.EMPTY_STRING.equals(params[i])) {
                        sb.append(CommonConstants.SYMBOL_DOT).append(params[i]);
                    }
                }
                return sb.toString();
            }
        } else {
            return null;
        }
    }

    /**
     * Checks if is invalid.
     * 
     * @param str
     *            the str
     * 
     * @return true, if is invalid
     */
    public static boolean isInvalid(final String str) {
        return (str == null || str.trim().length() == 0 || "null".equals(str) || "\"null\"".equals(str));
    }

    public static boolean isInvalidforToken(final String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * Checks if is valid.
     * 
     * @param str
     *            the str
     * @return true, if is valid
     */
    public static boolean isValid(final String str) {
        return !isInvalid(str);
    }

    public static boolean isValidforToken(final String str) {
        return !isInvalidforToken(str);
    }

    /**
     * String tokenize to list.
     * 
     * @param str
     *            the str
     * 
     * @return the list< string>
     */
    public static List<String> stringTokenizeToList(final String str) {
        return stringTokenizeToList(str, ",");
    }

    /**
     * String tokenize to list.
     * 
     * @param str
     *            the str
     * @param delimiter
     *            the delimiter
     * 
     * @return the list< string>
     */
    public static List<String> stringTokenizeToList(final String str, final String delimiter) {
        List<String> strList = null;
        if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank(delimiter)) {
            strList = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(str, delimiter);
            while (st.hasMoreTokens()) {
                strList.add(st.nextToken());
            }
        }
        return strList;
    }

    /**
     * String tokenize to set.
     * 
     * @param str
     *            the str
     * 
     * @return the set< string>
     */
    public static Set<String> stringTokenizeToSet(final String str) {
        return stringTokenizeToSet(str, ",");
    }

    /**
     * String tokenize to set.
     * 
     * @param str
     *            the str
     * @param delimiter
     *            the delimiter
     * 
     * @return the set< string>
     */
    public static Set<String> stringTokenizeToSet(final String str, final String delimiter) {
        Set<String> strSet = null;
        if (StringUtils.isNotBlank(str) && StringUtils.isNotBlank(delimiter)) {
            strSet = new HashSet<>();
            StringTokenizer st = new StringTokenizer(str, delimiter);
            while (st.hasMoreTokens()) {
                strSet.add(st.nextToken());
            }
        }
        return strSet;
    }

    public static String stringMatcher(final String str, final String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public static String replaceSpace(final String str) {
        return str.replace(" ", "%20");
    }

    public static String streamToStringConvert(final InputStream stream) throws IOException {
        StringBuilder str = new StringBuilder();
        Reader iReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(iReader);
        String line;
        while ((line = reader.readLine()) != null) {
            str.append(line);
        }
        iReader.close();
        reader.close();
        return str.toString();
    }

    public static boolean isNumeric(final String str) {
        if (isValid(str)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (isNum.matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUSSymbol(final String str) {
        if (isValid(str)) {
            //"AAIC PR C" "APN.U"
            Pattern pattern = Pattern.compile("[a-z\\s\\.+A-Z]*");
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChineseChar(final String str) {
        String expression;
        expression = "[\\u4E00-\\u9FA5]+";
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean isASCIIChar(final char c) {
        return (c >= 0 && c <= 127);
    }

    public static String toStringAndCheckNull(final Object b) {
        String result = null;
        if (null != b) {
            result = String.valueOf(b);
        }
        return result;
    }

    public static String toStringWithoutSpace(final Object b) {
        String result = null;
        if (null != b) {
            result = b.toString().trim();
        }
        return result;
    }

    public static String toUpperCase(final String str) {
        String result = null;
        if (str != null) {
            result = str.toUpperCase();
        }
        return result;
    }


    public static boolean stringToBoolean(final String str) {
        if ("".equals(str) || str == null) {
            return false;
        }
        return str.equalsIgnoreCase("true");
    }

    public static Boolean transformYNIntoBoolean(final String str) {
        Boolean result = null;
        if (StringUtil.isValid(str)) {
            if ("N".equalsIgnoreCase(str)) {
                result = false;
            } else if ("Y".equalsIgnoreCase(str)) {
                result = true;
            }
        }
        return result;
    }

    // midc delay field return null, set default value false
    public static Boolean transformStringtoBoolean(final String str) {
        Boolean result = null;
        if (StringUtil.isValid(str)) {
            if ("N".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                result = false;
            } else if ("Y".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str)) {
                result = true;
            }
        } else {
            result = false;
        }
        return result;
    }

    public static String formatHKStockSymbol(final String symbol) {
        if (StringUtil.isInvalid(symbol) && symbol.length() < 5) {
            String empty = "00000";
            empty = empty.substring(0, empty.length() - symbol.length()) + symbol;
            return empty;
        }
        return symbol;
    }

    /**
     * Filter special string.
     * 
     * @param str
     *            the str
     * @param specialChar
     *            the special char
     * @param splitSeparator
     *            the split separator
     * @return the string[]
     */
    public static String[] filterSpecialString(final String str, final char specialChar, final char splitSeparator) {
        List<String> list = new ArrayList<>();
        char[] array = str.toCharArray();
        char lastChar = array[0];
        StringBuilder sb = new StringBuilder();
        for (char c : array) {
            if (specialChar == c) {
                if (splitSeparator == lastChar) {
                    sb.append(c);
                } else {
                    list.add(sb.toString());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(c);
            }
            lastChar = c;
        }
        list.add(sb.toString());

        return list.toArray(new String[list.size()]);
    }

    public static String getServerName() {
        if (StringUtil.isInvalid(StringUtil.serverName)) {
            try {
                StringUtil.serverName = new InitialContext().lookup("serverName").toString();
            } catch (Exception e) {
                StringUtil.serverName = "";
            }
        }
        return StringUtil.serverName;
    }

    public static String getServerPath() {
        if (StringUtil.isInvalid(StringUtil.serverPath)) {
            try {
                StringUtil.serverPath = new InitialContext().lookup("serverPath").toString();
            } catch (Exception e) {
                StringUtil.serverPath = "";
            }
        }
        return StringUtil.serverPath;
    }

}
