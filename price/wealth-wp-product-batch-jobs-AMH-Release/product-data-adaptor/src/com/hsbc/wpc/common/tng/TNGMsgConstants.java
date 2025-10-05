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
 * Class Name TNGMsgConstants
 * 
 * Creation Date Jul 6, 2006
 * 
 * Abstract The class stores constants for TMG message functions.
 * 
 * Amendment History (In chronological sequence):
 * 
 * Amendment Date CMM/PPCR No. Programmer Description
 */
package com.dummy.wpc.common.tng;

/**
 * The class stores constants for TMG message functions.
 * 
 * @author Andy W H Man
 * @version 1.0 13Aug2006
 * @since 1.0
 */
public interface TNGMsgConstants {
    public static final String DEFAULT_CONTACT_POINT = "Please contact WMD Support.";
    public static final String SERVICE_NAME = "Data Adaptor";
    public static final String DA001 = "Config path is necessary.";
    public static final String DA002 = "Parameters format is wrong.";
    public static final String DA003 = "The format of job code is wrong.";
    public static final String DA004 = "Cannot load the config file.";
    public static final String DA005 = "Cannot initialize the log file.";
    public static final String DA006 = "Run job failed.";
    public static final String DA007 = "Cannot find any available data file.";
    public static final String DA008 = "Missing ACK file(s).";
    public static final String DA009 = "Exist error records.";
    public static final String DA010 = "Missing mandatory parameter.";
}
