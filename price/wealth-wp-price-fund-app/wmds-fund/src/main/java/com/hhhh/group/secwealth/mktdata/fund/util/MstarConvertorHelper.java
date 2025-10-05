
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.util.Date;

import com.hhhh.group.secwealth.mktdata.common.common.ConvertorKey;
import com.hhhh.group.secwealth.mktdata.common.common.FiledTypeConstant;
import com.hhhh.group.secwealth.mktdata.common.convertor.ConvertorUtil;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;


public class MstarConvertorHelper {

    public static String covertDateString(final String in) {
        String out = null;
        if (StringUtil.isValid(in)) {
            Date date = (Date) ConvertorUtil.doConvert(ConvertorKey.STRING_TO_DATE_CONVERTOR, VendorType.MSTAR, in);
            out = (String) ConvertorUtil.doConvert(ConvertorKey.DATE_TO_STRING_CONVERTOR, FiledTypeConstant.DATE, date);
        }
        return out;
    }

    public static String covertLanguageCode(final String locale) {
        String lang = "";
        if (StringUtil.isValid(locale)) {
            String[] temp = locale.split(CommonConstants.SYMBOL_UNDERLINE);
            lang = temp[0];
        }
        return lang;
    }
}
