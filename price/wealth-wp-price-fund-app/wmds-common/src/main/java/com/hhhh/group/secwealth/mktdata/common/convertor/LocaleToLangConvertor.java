/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class LocaleToLangConvertor.
 */
public class LocaleToLangConvertor {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.hhhh.group.secwealth.mktdata.convertor.Convertor#doConvert(java.lang
     * .Object, java.lang.Object[])
     */
    public static String doConvert(final String locale, final Object... params) {
        String out = null;
        if (StringUtil.isValid(locale)) {
            out = locale.replace(CommonConstants.SYMBOL_UNDERLINE, CommonConstants.SYMBOL_LINE_CONNECTIVE);
        }
        return out;
    }
}
