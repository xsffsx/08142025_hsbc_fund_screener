/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2007 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		ParseException
 *
 * Creation Date	Jun 27, 2007
 *
 * Abstract			(The program's main functions)
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date
 *    CMM/PPCR No.		
 *    Programmer		35021438
 *    Description		This class extends EHouseException and is thrown if 
 * 						any exception occurs when parsing interface file.
 */

package com.dummy.wpc.common.exception;

//import com.ibm.jvm.Trace;



/**
 * @author (Developer name in Notes format, e.g. Peter T M Chan)
 * @version 1.0 (ddMMMyyyy, e.g. 13Aug2006)
 * @since 1.0
 */
public class ParseException extends Exception {

//    public ParseException(Trace arg0, String arg1) {
//    	super(arg0.toString() + "  ParseException :" + arg1);
//    }
//
//    public ParseException(Trace arg0, String arg1, Throwable arg2) {
//    	super(arg0.toString() + "  ParseException :" + arg1 , arg2);
//    }

    public ParseException(Class in_class, String method, String message) {
    	super("Class Name: " + in_class.getName() + 
    			" Method: " + method  + 
    			" Message: " + message);
    }
}
