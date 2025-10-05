/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DateUtilTest {

    @Test
    public void testConvertISO8601FormatIsCorrectFormat() throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timezoneId = "America/New_York";
        String iso8601TimezoneId = "GMT+8";
        String date = DateUtil.current(pattern, TimeZone.getTimeZone(timezoneId));
        String iso8601Date = DateUtil.convertToISO8601Format(date, pattern, TimeZone.getTimeZone(timezoneId),
            TimeZone.getTimeZone(iso8601TimezoneId));

        String regex =
            "^([\\+-]?\\d{4}(?!\\d{2}\\b))((-?)((0[1-9]|1[0-2])(\\3([12]\\d|0[1-9]|3[01]))?|W([0-4]\\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\\d|[12]\\d{2}|3([0-5]\\d|6[1-6])))([T\\s]((([01]\\d|2[0-3])((:?)[0-5]\\d)?|24\\:?00)([\\.,]\\d+(?!:))?)?(\\17[0-5]\\d([\\.,]\\d+)?)?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)?)?)?$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(iso8601Date);

        assertThat(m.matches(), equalTo(true));
    }

    @Test
    public void testConvertISO8601FormatIsSameResult() throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timezoneId = "America/New_York";
        final String iso8601Pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        String iso8601TimezoneId = "GMT+8";
        String date = DateUtil.current(pattern, TimeZone.getTimeZone(timezoneId));
        String iso8601Date = DateUtil.convertToISO8601Format(date, pattern, TimeZone.getTimeZone(timezoneId),
            TimeZone.getTimeZone(iso8601TimezoneId));

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone(timezoneId));
        SimpleDateFormat iso8601Formatter = new SimpleDateFormat(iso8601Pattern);
        iso8601Formatter.setTimeZone(TimeZone.getTimeZone(iso8601TimezoneId));

        assertThat(iso8601Formatter.parse(iso8601Date).getTime(), equalTo(formatter.parse(date).getTime()));
    }

    @Test
    public void testConvertISO8601FormatWithInvalidDateParameter() throws ParseException {
        String invalidDate = "";
        String defaultResponseAsDateInvalid = "";
        String result = DateUtil.convertToISO8601Format(invalidDate, "yyyy-MM-dd", null, null);
        assertThat(result, equalTo(defaultResponseAsDateInvalid));
    }

}
