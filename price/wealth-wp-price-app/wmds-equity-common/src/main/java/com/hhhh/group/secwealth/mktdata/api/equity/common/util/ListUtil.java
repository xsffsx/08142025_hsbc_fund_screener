/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;

/**
 * <p>
 * <b> A util for list handler. </b>
 * </p>
 */
public final class ListUtil {

    public static boolean isValid(final List<?> list) {
        return null != list && list.size() > 0;
    }

    public static boolean isInvalid(final List<?> list) {
        return !ListUtil.isValid(list);
    }

    public static String toString(final List<?> list) {
        return toString(list, SymbolConstant.SYMBOL_COLON);
    }

    public static String toString(final List<?> list, final String connnector) {
        String str = null;
        if (null != list) {
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    sb.append(list.get(i));
                } else {
                    sb.append(connnector).append(list.get(i));
                }
            }
            str = sb.toString();
        }
        return str;
    }
}
