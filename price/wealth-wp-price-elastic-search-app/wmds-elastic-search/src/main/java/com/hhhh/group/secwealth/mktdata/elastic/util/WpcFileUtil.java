/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class WpcFileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(WpcFileUtil.class);

    public static final String ALL_SITE_OUT_FILE_PATTERN = "((${SITE}(.+)\\.(?i)(out))$)";
    //old ([^\\s]+(\\/${SITE}(.+)${FILE_TIME}\\.(?i)(xml))$)
    public static final String XML_FILE_PATTERN = "((${SITE}(.+)${FILE_TIME}\\.(?i)(xml))$)";
    public static final String XML_FILE_NAME_PATTERN = "${SITE}(.+)${FILE_TIME}\\.(?i)(xml)$";
    public static final String SITE = "${SITE}";
    public static final String FILE_TIME = "${FILE_TIME}";
    public static final String MD5_FILE_EXTENSION = ".MD5";
    private static final int OUT_FILE_LENGTH = 5;
    private static final String PATTERN = "\\d{2}";

    public static long converOutFileNameToTimestamp(final String fileName) {
        long timestamp = 0L;
        String name = fileName.substring(0, fileName.lastIndexOf(CommonConstants.SYMBOL_DOT));
        String[] array = name.split(CommonConstants.SYMBOL_UNDERLINE);
        int length = array.length;
        DateFormat df = new SimpleDateFormat(DateConstants.DATEFORMAT_YYYY_MM_DD_HH_MM);
        if (length >= WpcFileUtil.OUT_FILE_LENGTH) {
            try {
                timestamp = df.parse(array[length - 2]).getTime();
                timestamp += Long.parseLong(array[length - 1]);
            } catch (Exception e) {
                logger.error("converterFileName parse date error: DateStr: " + array[length - 2]
                    + " ,Date Format pattern: " + DateConstants.DATEFORMAT_YYYY_MM_DD_HH_MM, e);
                timestamp = 0L;
            }
        }
        return timestamp;
    }

    public static String getTime(final String fileName) {
        String time = "";
        String name = fileName.substring(0, fileName.lastIndexOf(CommonConstants.SYMBOL_DOT));
        String[] array = name.split(CommonConstants.SYMBOL_UNDERLINE);
        int length = array.length;
        if (length >= WpcFileUtil.OUT_FILE_LENGTH) {
            time = array[length - 2] + WpcFileUtil.PATTERN + CommonConstants.SYMBOL_UNDERLINE + array[length - 1];
        }
        return time;
    }

    public class WpcFilenameFilter implements FilenameFilter {
        private Pattern pattern;

        public WpcFilenameFilter(final String regPattern) {
            this.pattern = Pattern.compile(regPattern);
        }

        public boolean accept(final File dir, final String name) {
            return this.pattern.matcher(name).matches();
        }
    }
}
