
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * The Class DateToStringConvertor.
 */
public class StringToDateConvertor implements Convertor<String, Date> {

    /** The date format. */
    private String dateFormat = null;

    /** The time zone. */
    private String timeZone = "GMT";


    public Date doConvert(final String in, final Object... params) {

        Date out = null;
        if (StringUtil.isValid(in)) {
            String dateFormatStr = this.dateFormat.substring(0, in.length());
            if (dateFormatStr.indexOf("T") >= 0) dateFormatStr = dateFormatStr.replace("T", "'T'");
            String timeZoneStr = this.timeZone;

            String[] validParams = this.validDateParams(params);
            dateFormatStr = StringUtils.isNotBlank(validParams[0]) ? validParams[0] : dateFormatStr;
            timeZoneStr = StringUtils.isNotBlank(validParams[1]) ? validParams[1] : timeZoneStr;
            try {
                SimpleDateFormat df = new SimpleDateFormat(dateFormatStr);
                df.setTimeZone(TimeZone.getTimeZone(timeZoneStr));
                out = df.parse(in);
            } catch (ParseException e) {
                LogUtil.error(StringToDateConvertor.class,
                    new StringBuffer("Date string[").append(in).append("] convert fail by using format[").append(dateFormatStr)
                        .append("] and timeZone[").append(timeZoneStr).append("].").toString());
                throw new SystemException(e);
            }
        }
        return out;
    }

    public String[] validDateParams(final Object... params) {
        String[] validParams = new String[2];
        if (null != params && params.length > 0) {
            String dateFormatParam = (String) params[0];
            if (StringUtil.isValid(dateFormatParam)) validParams[0] = dateFormatParam;
            if (params.length > 1) {
                String timeZoneParam = (String) params[1];
                if (StringUtil.isValid(timeZoneParam)) validParams[1] = timeZoneParam;
            }
        }
        return validParams;
    }


    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }


    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }

}
