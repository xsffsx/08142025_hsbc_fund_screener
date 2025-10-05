/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.util.Map;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

public class ConvertorUtil {

    private static Map<String, Convertor<Object, Object>> convertors;

    public static Object doConvert(final String key, final String type, final Object in, final Object... params) {
        if (null == key) {
            LogUtil.error(ConvertorUtil.class, "Key is null for " + ConvertorUtil.class.getSimpleName() + " to get convertor.");
            throw new SystemException("Key is null for " + ConvertorUtil.class.getSimpleName() + " to get convertor.");
        }
        if (null == type) {
            LogUtil.error(ConvertorUtil.class, "Type is null for " + ConvertorUtil.class.getSimpleName() + " to get convertor.");
            throw new SystemException("Type is null for " + ConvertorUtil.class.getSimpleName() + " to get convertor.");
        }
        return doConvert(key + CommonConstants.SYMBOL_UNDERLINE + type, in, params);
    }

    public static Object doConvert(final String key, final Object in, final Object... params) {
        if (null == key) {
            LogUtil.error(ConvertorUtil.class, "Key is null for " + ConvertorUtil.class.getSimpleName() + " to get convertor.");
            throw new SystemException("Key is null for " + ConvertorUtil.class.getSimpleName() + " to get convertor.");
        }
        Convertor<Object, Object> convertor = ConvertorUtil.convertors.get(key);
        if (null == convertor) {
            LogUtil.error(ConvertorUtil.class, "Convert is not found for " + ConvertorUtil.class.getSimpleName() + " by key[" + key
                + "].");
            throw new SystemException("Convert is not found for " + ConvertorUtil.class.getSimpleName() + " by key[" + key + "].");
        }
        return convertor.doConvert(in, params);
    }

    /**
     * @param convertors
     *            the convertors to set
     */
    public void setConvertors(final Map<String, Convertor<Object, Object>> convertors) {
        ConvertorUtil.convertors = convertors;
    }
}
