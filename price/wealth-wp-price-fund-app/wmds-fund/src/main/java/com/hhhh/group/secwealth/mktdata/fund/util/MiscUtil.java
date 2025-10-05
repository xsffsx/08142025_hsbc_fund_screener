
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;


public final class MiscUtil {

    public static BigDecimal safeBigDecimalValue(final Object object) {
        try {
            return object == null ? null : new BigDecimal(object.toString());
        } catch (Exception e) {
            LogUtil.warn(MiscUtil.class, "Error occured while converting object {} to BigDecimal number", object);
            return null;
        }
    }

    public static Integer safeIntValue(final Object object) {
        try {
            return object == null ? null : Integer.valueOf(object.toString());
        } catch (Exception e) {
            LogUtil.warn(MiscUtil.class, "Error occured while converting object {} to Int number", object);
            return null;
        }
    }

    public static String safeString(final Object object) {
        return object == null ? null : object.toString();
    }

    public static Boolean safeBoolean(final Object object) {
        return object == null ? null : (Boolean) object;
    }

    public static <T> List<List<T>> partitionList(final List<T> list, final int partitionSize) {

        List<List<T>> resultList = new ArrayList<List<T>>();
        for (int firstIndex = 0; firstIndex < list.size(); firstIndex = firstIndex + partitionSize) {
            int lastIndex = firstIndex + partitionSize;
            resultList.add(list.subList(firstIndex, lastIndex < list.size() ? lastIndex : list.size()));
        }
        return resultList;
    }
}
