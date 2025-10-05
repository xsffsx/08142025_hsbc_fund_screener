
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.hhhh.group.secwealth.mktdata.common.common.ConvertorKey;
import com.hhhh.group.secwealth.mktdata.common.common.FiledTypeConstant;
import com.hhhh.group.secwealth.mktdata.common.convertor.ConvertorUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;


public final class FeConvertorHelper {

    public static Boolean stringToBoolean(final String in) {
        Boolean out = null;
        if (StringUtil.isValid(in)) {
            out = (Boolean) ConvertorUtil.doConvert(ConvertorKey.STRING_TO_BOOLEAN_CONVERTOR, in);
        }
        return out;
    }

    public static String booleanToString(final Boolean in) {
        String out = null;
        if (null != in) {
            out = (String) ConvertorUtil.doConvert(ConvertorKey.BOOLEAN_TO_STRING_CONVERTOR, in);
        }
        return out;
    }

    public static Date stringToDate(final String in) {
        Date out = null;
        if (StringUtil.isValid(in)) {
            out = (Date) ConvertorUtil.doConvert(ConvertorKey.STRING_TO_DATE_CONVERTOR, in);
        }
        return out;
    }

    public static String dateToString(final XMLGregorianCalendar in) {
        String out = null;
        if (null != in) {
            out = dateToString(in.toGregorianCalendar().getTime());
        }
        return out;
    }

    public static String dateToString(final Date in) {
        String out = null;
        if (null != in) {
            out = (String) ConvertorUtil.doConvert(ConvertorKey.DATE_TO_STRING_CONVERTOR, FiledTypeConstant.DATE, in);
        }
        return out;
    }

    public static String dateTimeToString(final XMLGregorianCalendar in) {
        String out = null;
        if (null != in) {
            out = dateTimeToString(in.toGregorianCalendar().getTime());
        }
        return out;
    }

    public static String dateTimeToString(final Date in) {
        String out = null;
        if (null != in) {
            out = (String) ConvertorUtil.doConvert(ConvertorKey.DATE_TO_STRING_CONVERTOR, FiledTypeConstant.DATETIME, in);
        }
        return out;
    }

    public static String entityDateToString(final Date in, final String timeZone) {
        String out = null;
        if (null != in) {
            out = (String) ConvertorUtil.doConvert(ConvertorKey.DATE_TO_STRING_CONVERTOR, FiledTypeConstant.DATE, in, "", timeZone);
        }
        return out;
    }

    public static String dateToString(final Date in, final String dateFormat, final String timeZone) {
        String out = null;
        if (null != in) {
            out = (String) ConvertorUtil.doConvert(ConvertorKey.DATE_TO_STRING_CONVERTOR, FiledTypeConstant.DATE, in, dateFormat,
                timeZone);
        }
        return out;
    }

    public static String entityDateTimeToString(final Date in, final String timeZone) {
        String out = null;
        if (null != in) {
            out = (String) ConvertorUtil.doConvert(ConvertorKey.DATE_TO_STRING_CONVERTOR, FiledTypeConstant.DATETIME, in, "",
                timeZone);
        }
        return out;
    }
}
