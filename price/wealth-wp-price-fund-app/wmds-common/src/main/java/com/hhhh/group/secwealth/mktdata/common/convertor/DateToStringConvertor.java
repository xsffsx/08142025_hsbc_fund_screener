/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

/**
 * The Class DateToStringConvertor.
 */
public class DateToStringConvertor implements Convertor<Date, String> {

    /** The date format. */
    private String dateFormat = null;

    /** The time zone. */
    private String timeZone = "GMT";

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.hhhh.group.secwealth.mktdata.convertor.Convertor#doConvert(java.lang
     * .Object)
     */
    public String doConvert(final Date in, final Object... params) {

        String out = null;
        if (null != in) {
            String dateFormatStr = this.dateFormat;
            String timeZoneStr = this.timeZone;
            if (null != params && params.length > 0) {
                String param_1 = (String) params[0];
                if (StringUtil.isValid(param_1)) {
                    dateFormatStr = param_1;
                }
                if (params.length > 1) {
                    String param_2 = (String) params[1];
                    if (StringUtil.isValid(param_2)) {
                        timeZoneStr = param_2;
                    }
                }
            }
            final FastDateFormat df = FastDateFormat.getInstance(dateFormatStr, TimeZone.getTimeZone(timeZoneStr));
            out = df.format(in);
        }
        return out;
    }

    /**
     * Sets the date format.
     * 
     * @param dateFormat
     *            the dateFormat to set
     */
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Sets the time zone.
     * 
     * @param timeZone
     *            the timeZone to set
     */
    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }

}
