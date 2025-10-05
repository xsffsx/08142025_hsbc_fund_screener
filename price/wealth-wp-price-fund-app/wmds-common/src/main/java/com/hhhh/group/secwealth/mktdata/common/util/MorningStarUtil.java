/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * <b> Morning Star utilities </b>
 * </p>
 */
public final class MorningStarUtil {

    public static String trimCurrency(final String inputCcy) {
        if (StringUtils.isBlank(inputCcy)) {
            return null;
        }
        return inputCcy.substring(inputCcy.length() - 3);
    }

}
