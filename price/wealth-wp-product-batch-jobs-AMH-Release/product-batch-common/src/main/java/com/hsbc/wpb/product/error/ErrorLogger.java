/*
 * *************************************************************** Copyright.
 * dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system or translated in any human or computer language
 * in any way or for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name ErrorLogger
 *
 * Creation Date Jul 11, 2006
 *
 * Abstract The class provides TNG message functions.
 *
 * Amendment History (In chronological sequence):
 *
 * Amendment Date CMM/PPCR No. Programmer Description
 */
package com.dummy.wpb.product.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The class provides TNG message functions.
 *
 * @author Andy W H Man
 * @version 1.0 13Aug2006
 * @since 1.0
 */
public class ErrorLogger {

    private ErrorLogger(){}

    private static final Log cloudLogger = LogFactory.getLog("COULD_LOG_RECORDER");

    /**
     * This method logs TNG messages to cosole.
     *
     * @param errorCode
     * product errorCode
     * @param jobName
     * Job name
     * @param errorDescription
     * Error description
     */
    public static void logErrorMsg(ErrorCode errorCode, String jobName, String errorDescription) {
        logErrorMsg(String.format("%s %s: %s (%s)", errorCode.name(), jobName, errorCode.desc(), errorDescription));
    }

    public static void logErrorMsg(ErrorCode errorCode, String jobName) {
        logErrorMsg(String.format("%s %s: %s", errorCode.name(), jobName, errorCode.desc()));
    }
    public static void logErrorMsg(String message) {
        cloudLogger.info(message);
    }
}