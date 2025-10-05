
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.util.Date;
import java.util.TimeZone;


public class TimeZoneUtil {

    public static String isInDaylightTime(final TimeZone timeZone, final Date date) {

        String inDaylightTime = null;

        if (null != timeZone && null != date) {

            if (timeZone.useDaylightTime()) {

                if (timeZone.inDaylightTime(date)) {
                    inDaylightTime = "Y";
                } else {
                    inDaylightTime = "N";
                }

            }
        }

        return inDaylightTime;
    }

}
