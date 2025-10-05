package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class DateTimeUtilTest {

    @Test
    public void testConvertDate() throws ParseException {
        String ddMmmYyyy = DateUtil.convertDate("14 JUN 2021", "dd MMM yyyy", DateUtil.DATE_DAY_PATTERN);
        System.out.println(ddMmmYyyy);
        Assert.assertNotNull(ddMmmYyyy);
    }

    @Test
    public void testConvertLong2Timestamp() {
        //72003638
        String durationBreakdown = getDurationBreakdown(72003638);
        System.out.println(durationBreakdown);
        Assert.assertNotNull(durationBreakdown);
    }

    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= TimeUnit.SECONDS.toMillis(seconds);

        StringBuilder sb = new StringBuilder(64);
        sb.append(hours);
        sb.append(":");
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
        sb.append(".");
        sb.append(millis);

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}
