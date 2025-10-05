/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

/*
 */

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.bmc.DurationException;

/**
 * <p>
 * <b> Define a duration exception util. </b>
 * </p>
 */
public final class DurationExceptionUtil {

    private DurationExceptionUtil() {}

    public static void updateException(final List<DurationException> list, final Throwable e, final int duration) {
        if (null != list) {
            GregorianCalendar currentTime = DateUtil.getMachineCalendar(null, null);
            if (duration > 0) {
                currentTime.add(Calendar.SECOND, (-1) * duration);
                for (int i = list.size() - 1; i >= 0; i--) {
                    GregorianCalendar oldTime = list.get(i).getTime();
                    if (currentTime.after(oldTime)) {
                        list.remove(i);
                    } else {
                        break;
                    }
                }
            }
            list.add(0, new DurationException(e));
        }
    }
}
