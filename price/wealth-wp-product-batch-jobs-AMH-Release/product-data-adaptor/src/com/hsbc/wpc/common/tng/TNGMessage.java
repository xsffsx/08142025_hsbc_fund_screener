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
 * Class Name TNGMessage
 * 
 * Creation Date Jul 11, 2006
 * 
 * Abstract The class provides TNG message functions.
 * 
 * Amendment History (In chronological sequence):
 * 
 * Amendment Date CMM/PPCR No. Programmer Description
 */
package com.dummy.wpc.common.tng;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;


/**
 * The class provides TNG message functions.
 * 
 * @author Andy W H Man
 * @version 1.0 13Aug2006
 * @since 1.0
 */
public class TNGMessage {

    /**
     * This method logs TNG messages to cosole.
     * 
     * @param msgNum
     *            the message number
     * @param msgType
     *            the message type
     * @param service
     *            the service code
     * @param exInfo
     *            the error message
     * @param contactPnt
     *            the contact point for the error
     */
    public static void logTNGMsgExInfo(final String msgNum, final String msgType, final String service, final String exInfo,
        final String contactPnt) {
        try {
            Runtime.getRuntime().exec("logger -t root " + getTNGMsg(msgNum, msgType, service, exInfo, contactPnt));
        } catch (IOException e) {
            System.err.println("Send TNG Message Failed:" + e.getMessage() + "\n");
        }
    }

    public static void logTNGMsg(final String msgNum, final String msgType, final String service, final String contactPnt) {
        logTNGMsgExInfo(msgNum, msgType, service, "", contactPnt);
    }

    public static void logTNGMsg(final String msgNum, final String msgType, final String service) {
        logTNGMsg(msgNum, msgType, service, TNGMsgConstants.DEFAULT_CONTACT_POINT);
    }

    public static void logTNGMsgExInfo(final String msgNum, final String msgType, final String service, final String exInfo) {
        logTNGMsgExInfo(msgNum, msgType, service, exInfo, TNGMsgConstants.DEFAULT_CONTACT_POINT);
    }

    private static String getTNGMsg(String msgNum, final String msgType, final String service, final String exInfo,
        final String contactPnt) {
        msgNum = chkMsgNumLength(msgNum);
        if (StringUtils.isNotBlank(exInfo)) {
            return "WPC" + msgNum + msgType + " " + service + ": " + TNGMapping(msgNum) + " (" + exInfo + ") " + contactPnt;
        } else {
            return "WPC" + msgNum + msgType + " " + service + ": " + TNGMapping(msgNum) + " " + contactPnt;
        }

    }

    private static String chkMsgNumLength(final String msgNum) {
        StringBuffer msgNumBuffer = new StringBuffer();
        String rtrnMsgNum;
        if (msgNum.length() == 3) {
            msgNumBuffer.append("0");
            msgNumBuffer.append(msgNum);
        } else {
            msgNumBuffer.append(msgNum);
        }
        rtrnMsgNum = msgNumBuffer.toString();
        return rtrnMsgNum;
    }

    private static String TNGMapping(final String msgNum) {
        String rtrnMsg = "Unkown Reason";
        if (msgNum.equals("0001")) {
            rtrnMsg = TNGMsgConstants.DA001;
        } else if (msgNum.equals("0002")) {
            rtrnMsg = TNGMsgConstants.DA002;
        } else if (msgNum.equals("0003")) {
            rtrnMsg = TNGMsgConstants.DA003;
        } else if (msgNum.equals("0004")) {
            rtrnMsg = TNGMsgConstants.DA004;
        } else if (msgNum.equals("0005")) {
            rtrnMsg = TNGMsgConstants.DA005;
        } else if (msgNum.equals("0006")) {
            rtrnMsg = TNGMsgConstants.DA006;
        } else if (msgNum.equals("0007")) {
            rtrnMsg = TNGMsgConstants.DA007;
        } else if (msgNum.equals("0008")) {
            rtrnMsg = TNGMsgConstants.DA008;
        } else if (msgNum.equals("0009")) {
            rtrnMsg = TNGMsgConstants.DA009;
        } else if (msgNum.equals("0010")) {
            rtrnMsg = TNGMsgConstants.DA010;
        }

        return rtrnMsg;
    }
}