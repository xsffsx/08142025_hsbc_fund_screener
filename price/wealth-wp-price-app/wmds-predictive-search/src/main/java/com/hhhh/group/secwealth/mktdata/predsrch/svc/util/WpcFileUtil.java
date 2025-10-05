/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.regex.Pattern;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl.VolumeServiceImpl;

public final class WpcFileUtil {

    public static final String ALL_SITE_OUT_FILE_PATTERN = "([^\\s]+(\\/${SITE}(.+)\\.(?i)(out))$)";
    public static final String XML_FILE_PATTERN = "([^\\s]+(\\/${SITE}(.+)${FILE_TIME}\\.(?i)(xml))$)";
    public static final String XML_FILE_NAME_PATTERN = "${SITE}(.+)${FILE_TIME}\\.(?i)(xml)$";
    public static final String SITE = "${SITE}";
    public static final String FILE_TIME = "${FILE_TIME}";
    public static final String MD5_FILE_EXTENSION = ".MD5";

    // private static DateFormat df = new
    // SimpleDateFormat(DateConstants.DateFormat_yyyyMMddHHmm);
    private static final int OUT_FILE_LENGTH = 5;
    private static final String PATTERN = "\\d{2}";

    public static long converOutFileNameToTimestamp(final String fileName) {
        long timestamp = 0L;
        String name = fileName.substring(0, fileName.lastIndexOf(CommonConstants.SYMBOL_DOT));
        String[] array = name.split(CommonConstants.SYMBOL_UNDERLINE);
        int length = array.length;
        if (null != array && length >= WpcFileUtil.OUT_FILE_LENGTH) {
            try {
                timestamp = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMddHHmm).parse(array[length - 2]).getTime();
                timestamp += Long.parseLong(array[length - 1]);
            } catch (Exception e) {
                LogUtil.error(VolumeServiceImpl.class, "converterFileName parse date error: DateStr: " + array[length - 2]
                    + " ,Date Format pattern: " + DateConstants.DateFormat_yyyyMMddHHmm, e);
                timestamp = 0L;
            }
        }
        return timestamp;
    }

    public static class OutFileFilter implements FilenameFilter {
        private static final String OUT_FILE_EXTENSION = ".out";
        private String site;

        public OutFileFilter() {
            this.site = null;
        }

        public OutFileFilter(final String site) {
            this.site = site;
        }

        public boolean accept(final File dir, final String name) {
            if (name.endsWith(OutFileFilter.OUT_FILE_EXTENSION) && name.startsWith(this.site)) {
                return true;
            }
            return false;
        }
    }

    public static class WpcOutFileNameComprator implements Comparator<String> {

        public int compare(final String o1, final String o2) {
            long converterFileName1 = WpcFileUtil.converOutFileNameToTimestamp(o1);
            long converterFileName2 = WpcFileUtil.converOutFileNameToTimestamp(o2);
            if (converterFileName1 > converterFileName2) {
                return -1;
            } else if (converterFileName1 < converterFileName2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static class WpcOutFileComprator implements Comparator<File> {

        public int compare(final File o1, final File o2) {
            long converterFileName1 = WpcFileUtil.converOutFileNameToTimestamp(o1.getName());
            long converterFileName2 = WpcFileUtil.converOutFileNameToTimestamp(o2.getName());
            if (converterFileName1 > converterFileName2) {
                return -1;
            } else if (converterFileName1 < converterFileName2) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    public static String getTime(final String fileName) {
        String time = "";
        String name = fileName.substring(0, fileName.lastIndexOf(CommonConstants.SYMBOL_DOT));
        String[] array = name.split(CommonConstants.SYMBOL_UNDERLINE);
        int length = array.length;
        if (null != array && length >= WpcFileUtil.OUT_FILE_LENGTH) {
            time = array[length - 2] + WpcFileUtil.PATTERN + CommonConstants.SYMBOL_UNDERLINE + array[length - 1];
        }
        return time;
    }

    public static class WpcFilenameFilter implements FilenameFilter {
        private Pattern pattern;

        public WpcFilenameFilter(final String regPattern) {
            this.pattern = Pattern.compile(regPattern);
        }

        public boolean accept(final File dir, final String name) {
            if (this.pattern.matcher(name).matches()) {
                return true;
            }
            return false;
        }
    }
}
