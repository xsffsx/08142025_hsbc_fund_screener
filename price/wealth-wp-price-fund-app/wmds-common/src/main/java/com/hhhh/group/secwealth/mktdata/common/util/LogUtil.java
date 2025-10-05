/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.common.dao.impl.CustomerContextHolder;
import com.hhhh.group.secwealth.mktdata.common.exception.BaseException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;


public final class LogUtil {

    private static final String LOG_CONFIG = "system/logback.xml";
    private static final String CONFIG_FILE = "system/common/config.properties";
    private static final String LOG_SEPARATOR = " -> ";
    private static final String LOG_STACKTRACEELEMENTINDEX = "app.logger.stackTraceElementIndex";
    private static final int LOG_length_limit = 900;
    private static final String LOG_NEWLINES = "\r\n";

    public static final String INSTANCE_NAME = "instanceName";
    public static final String LOG_VERSION = "logging.version";
    public static final String LOG_APP = "logging.app";
    public static final String LOG_CF_ORG = "logging.cf.org";
    public static final String LOG_CF_SPACE = "logging.cf.space";


    /** The folder exist. */
    private static boolean folderExist = false;

    /**
     * log config for get log line number, set the StackTraceElement begin
     * Index. if set wrong number, the line number will be 0
     */
    private static int stackTraceElementIndex = 0;

    private static LoggerContext loggerContext;

    private LogUtil() {}

    static {
        InputStream inConfig = null;
        InputStream inAppConfig = null;
        try {
            // log file instance path
            System.setProperty(LogUtil.INSTANCE_NAME, StringUtil.getServerName());

            Properties prop = new Properties();
            // set the StackTraceElement begin Index form config.properties
            inConfig = LogUtil.class.getClassLoader().getResourceAsStream(LogUtil.CONFIG_FILE);
            prop.load(inConfig);
            LogUtil.stackTraceElementIndex = Integer.parseInt(prop.getProperty(LogUtil.LOG_STACKTRACEELEMENTINDEX, "0"));

            // load logback.xml, init the LoggerContext object
            LogUtil.loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            ContextInitializer initializer = new ContextInitializer(LogUtil.loggerContext);
            if (initializer.findURLOfDefaultConfigurationFile(false) == null) {
                LogUtil.loggerContext.reset();
                URL defaultConfig = LogUtil.loggerContext.getClass().getClassLoader().getResource(LogUtil.LOG_CONFIG);
                initializer.configureByResource(defaultConfig);
            }
        } catch (Exception e) {
            throw new SystemException("Failed to load default logging configuration", e);
        } finally {
            if (inConfig != null) {
                try {
                    inConfig.close();
                } catch (IOException e) {
                    throw new SystemException("Failed to close the InputStream", e);
                }
            }
            if (inAppConfig != null) {
                try {
                    inAppConfig.close();
                } catch (IOException e) {
                    throw new SystemException("Failed to close the InputStream", e);
                }
            }
        }
    }

    public static Logger getLogger(final Class<?> clazz) {
        return LogUtil.loggerContext.getLogger(clazz);
    }

    public static void debug(final Class<?> clazz, final String msg) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            log.debug(getLogInfo(clazz) + msg);
        }
    }

    public static void debug(final Class<?> clazz, final String format, final Object arg) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            log.debug(getLogInfo(clazz) + format, arg);
        }
    }

    public static void debug(final Class<?> clazz, final String format, final Object arg1, final Object arg2) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            log.debug(getLogInfo(clazz) + format, arg1, arg2);
        }
    }

    public static void debug(final Class<?> clazz, final String format, final Object... argArray) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            log.debug(getLogInfo(clazz) + format, argArray);
        }
    }

    public static void debug(final Class<?> clazz, final String msg, final BaseException ex) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            log.debug(getErrorLogInfo(clazz, ex) + msg, ex);
        }
    }

    public static void debug(final Class<?> clazz, final String msg, final Throwable t) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            log.debug(getLogInfo(clazz) + msg, t);
        }
    }

    public static void debugBeanToJson(final Class<?> clazz, final String msg, final Object obj) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            if (obj != null) {
                try {
                    LogUtil.debug(clazz, msg + ": {}", JacksonUtil.beanToJson(obj));
                } catch (Exception e) {
                    LogUtil.error(LogUtil.class, "LogUtil debugBeanToJson error", e);
                }
            } else {
                LogUtil.debug(clazz, msg);
            }
        }
    }

    public static void debugOutboundMsg(final Class<?> clazz, final String msg, final String reqUrl, final String reqParams) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            String reqMsg = new StringBuilder(CommonConstants.OUTBOUND_MSG_PREFIX).append(reqUrl)
                .append(CommonConstants.SYMBOL_INTERROGATION).append(reqParams).toString();
            LogUtil.debug(clazz, substringLog(msg + CommonConstants.SYMBOL_VERTICAL + reqMsg));
        }
    }

    public static void debugInboundMsg(final Class<?> clazz, final String msg, final String resp) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isDebugEnabled()) {
            String respMsg = new StringBuilder(CommonConstants.INBOUND_MSG_PREFIX).append(resp).toString();
            LogUtil.debug(clazz, substringLog(msg + CommonConstants.SYMBOL_VERTICAL + respMsg));
        }
    }

    public static void info(final Class<?> clazz, final String msg) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            log.info(getLogInfo(clazz) + msg);
        }
    }

    public static void info(final Class<?> clazz, final String format, final Object arg) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            log.info(getLogInfo(clazz) + format, arg);
        }
    }

    public static void info(final Class<?> clazz, final String format, final Object arg1, final Object arg2) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            log.info(getLogInfo(clazz) + format, arg1, arg2);
        }
    }

    public static void info(final Class<?> clazz, final String format, final Object... argArray) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            log.info(getLogInfo(clazz) + format, argArray);
        }
    }

    public static void info(final Class<?> clazz, final String msg, final BaseException ex) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            log.info(getErrorLogInfo(clazz, ex) + msg, ex);
        }
    }

    public static void info(final Class<?> clazz, final String msg, final Throwable t) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            log.info(getLogInfo(clazz) + msg, t);
        }
    }

    public static void infoBeanToJson(final Class<?> clazz, final String msg, final Object obj) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            if (obj != null) {
                try {
                    LogUtil.info(clazz, msg + ": {}", JacksonUtil.beanToJson(obj));
                } catch (Exception e) {
                    LogUtil.error(LogUtil.class, "LogUtil infoBeanToJson error", e);
                }
            } else {
                LogUtil.info(clazz, msg);
            }
        }
    }

    public static void infoOutboundMsg(final Class<?> clazz, final String msg, final String reqUrl, final String reqParams) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            String reqMsg = new StringBuilder(CommonConstants.OUTBOUND_MSG_PREFIX).append(reqUrl)
                .append(CommonConstants.SYMBOL_INTERROGATION).append(reqParams).toString();
            LogUtil.info(clazz, substringLog(msg + CommonConstants.SYMBOL_VERTICAL + reqMsg));
        }
    }

    public static void infoInboundMsg(final Class<?> clazz, final String msg, final String resp) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isInfoEnabled()) {
            String respMsg = new StringBuilder(CommonConstants.INBOUND_MSG_PREFIX).append(resp).toString();
            LogUtil.info(clazz, substringLog(msg + CommonConstants.SYMBOL_VERTICAL + respMsg));
        }
    }

    public static void warn(final Class<?> clazz, final String msg) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isWarnEnabled()) {
            log.warn(getLogInfo(clazz) + msg);
        }
    }

    public static void warn(final Class<?> clazz, final String msg, final BaseException ex) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isWarnEnabled()) {
            log.warn(getErrorLogInfo(clazz, ex) + msg, ex);
        }
    }

    public static void warn(final Class<?> clazz, final String msg, final Throwable t) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isWarnEnabled()) {
            log.warn(getLogInfo(clazz) + msg, t);
        }
    }

    public static void warn(final Class<?> clazz, final String format, final Object arg) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isWarnEnabled()) {
            log.warn(getLogInfo(clazz) + format, arg);
        }
    }

    public static void warn(final Class<?> clazz, final String format, final Object arg1, final Object arg2) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isWarnEnabled()) {
            log.warn(getLogInfo(clazz) + format, arg1, arg2);
        }
    }

    public static void warn(final Class<?> clazz, final String format, final Object... argArray) {
        Logger log = LogUtil.getLogger(clazz);
        if (log.isWarnEnabled()) {
            log.warn(getLogInfo(clazz) + format, argArray);
        }
    }

    public static void error(final Class<?> clazz, final String msg) {
        LogUtil.getLogger(clazz).error(getLogInfo(clazz) + msg);
    }

    public static void error(final Class<?> clazz, final String format, final Object arg) {
        LogUtil.getLogger(clazz).error(getLogInfo(clazz) + format, arg);
    }

    public static void error(final Class<?> clazz, final String format, final Object arg1, final Object arg2) {
        LogUtil.getLogger(clazz).error(getLogInfo(clazz) + format, arg1, arg2);
    }

    public static void error(final Class<?> clazz, final String format, final Object... argArray) {
        LogUtil.getLogger(clazz).error(getLogInfo(clazz) + format, argArray);
    }

    public static void error(final Class<?> clazz, final String msg, final BaseException ex) {
        LogUtil.getLogger(clazz).error(getErrorLogInfo(clazz, ex) + msg, ex);
    }

    public static void error(final Class<?> clazz, final String msg, final Throwable t) {
        LogUtil.getLogger(clazz).error(getLogInfo(clazz) + msg, t);
    }

    public static void errorBeanToJson(final Class<?> clazz, final String msg, final Object obj) {
        if (obj != null) {
            try {
                LogUtil.error(clazz, msg + ": " + JacksonUtil.beanToJson(obj));
            } catch (Exception e) {
                LogUtil.error(LogUtil.class, "LogUtil debugBeanToJson error", e);
            }
        } else {
            LogUtil.error(clazz, msg);
        }
    }

    public static void errorBeanToJson(final Class<?> clazz, final String msg, final Object obj, final Throwable t) {
        if (obj != null) {
            try {
                LogUtil.error(clazz, msg + ": " + JacksonUtil.beanToJson(obj), t);
            } catch (Exception e) {
                LogUtil.error(LogUtil.class, "LogUtil debugBeanToJson error", e);
            }
        } else {
            LogUtil.error(clazz, msg);
        }
    }

    private static String getLogInfo(final Class<?> clazz) {
        String lineNumber = CommonConstants.SYMBOL_VERTICAL + getLogLineNumber(clazz) + LogUtil.LOG_SEPARATOR;
        String siteKey = CustomerContextHolder.getSiteKeyFromHeaderMap();
        String appCode = CustomerContextHolder.getAppCodeFromHeaderMap();
        if (StringUtil.isValid(siteKey) && StringUtil.isValid(appCode)) {
            return lineNumber + "SiteKey: " + siteKey + ", Application code: " + appCode + ", Message: ";
        }
        return lineNumber;
    }

    private static String getErrorLogInfo(final Class<?> clazz, final BaseException ex) {
        String lineNumber = CommonConstants.SYMBOL_VERTICAL + getLogLineNumber(clazz) + LogUtil.LOG_SEPARATOR;
        String siteKey = CustomerContextHolder.getSiteKeyFromHeaderMap();
        String appCode = CustomerContextHolder.getAppCodeFromHeaderMap();
        if (StringUtil.isValid(siteKey) && StringUtil.isValid(appCode)) {
            return lineNumber + "SiteKey: " + siteKey + ", Application code: " + appCode + ", TraceCode: " + ex.getTraceCode()
                + ", Message: ";
        }
        return lineNumber + "TraceCode: " + ex.getTraceCode() + ", Message: ";
    }

    private static int getLogLineNumber(final Class<?> clazz) {
        try {
            String clzName = clazz.getName();
            StackTraceElement[] stes = Thread.currentThread().getStackTrace();
            for (int i = LogUtil.stackTraceElementIndex; i < stes.length; i++) {
                if (clzName.equals(stes[i].getClassName())) {
                    return stes[i].getLineNumber();
                }
            }
            String clzSimpleName = clazz.getSimpleName();
            for (int i = LogUtil.stackTraceElementIndex; i < stes.length; i++) {
                String fileName = stes[i].getFileName();
                if (fileName.startsWith(clzSimpleName)) {
                    return stes[i].getLineNumber();
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * Gen command file.
     *
     * @param filePath
     *            the file path
     * @param cmd
     *            the cmd
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String genLogFile(final String filePath, final String cmd) throws Exception {
        String format = "yyyyMMddHHmmssSSS";
        FastDateFormat formatter = FastDateFormat.getInstance(format);
        String formattedDate = formatter.format(new Date());
        StringBuffer fileNameBuf = new StringBuffer();
        createFolderIfNotExist(filePath);
        fileNameBuf.append(filePath);
        fileNameBuf.append("MKDLogger");
        fileNameBuf.append("_");
        fileNameBuf.append(formattedDate);
        String tempFileName = fileNameBuf.toString() + ".tmp";
        String fullFileName = fileNameBuf.toString() + ".RDY";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFileName);
            out.write(cmd.getBytes());
        } catch (FileNotFoundException e) {
            LogUtil.error(LogUtil.class, "genLogFile error", e);
            fullFileName = "";
            throw new SystemException(e.getMessage(), e);
        } catch (IOException e) {
            LogUtil.error(LogUtil.class, "genLogFile error", e);
            fullFileName = "";
            throw new SystemException(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        File tempFile = new File(tempFileName);
        File readyFile = new File(fullFileName);
        if (tempFile.isFile()) {
            tempFile.renameTo(readyFile);
        }
        return fullFileName;
    }

    /**
     * Creates the folder if not exist.
     *
     * @param cmdFilePath
     *            the cmd file path
     */
    private static void createFolderIfNotExist(final String cmdFilePath) {
        if (!LogUtil.folderExist) {
            File file = new File(cmdFilePath);
            boolean exists = file.exists();
            // Create one directory
            if (!exists) {
                boolean success = file.mkdirs();
                if (success) {
                    LogUtil.folderExist = true;
                }
            } else {
                LogUtil.folderExist = true;
            }
        }
    }

    private static String substringLog(String log) {
        String str = log;
        try {
            if (StringUtil.isValid(log) && log.length() > LogUtil.LOG_length_limit) {
                StringBuffer bfLog = new StringBuffer();
                log = substringLog(log, bfLog);
                return log;
            }
            return log;
        } catch (Exception e) {
            LogUtil.error(LogUtil.class, "substringLog error", e);
            return str;
        }
    }

    private static String substringLog(String log, final StringBuffer bfLog) {
        if (StringUtil.isValid(log) && log.length() > LogUtil.LOG_length_limit) {
            bfLog.append(log.substring(0, LogUtil.LOG_length_limit)).append(LogUtil.LOG_NEWLINES);
            log = log.substring(LogUtil.LOG_length_limit);
            if (log.length() > LogUtil.LOG_length_limit) {
                substringLog(log, bfLog);
            } else {
                bfLog.append(log);
            }
        }
        return bfLog.toString();
    }
}