/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		CRNCY_DP_CONST
 *
 * Creation Date	Apr 12, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Apr 12, 2006
 *    CMM/PPCR No.		
 *    Programmer		cbdhkg0010
 *    Description
 * 
 */
package com.dummy.wpc.datadaptor.constant;

import java.util.Hashtable;

public class CRNCY_DP_CONST {

    public final static String[][] ccyPairs = { 
        { "HKD", "2" },
        { "JPY", "0" }
    };

    public static Hashtable CCY_DP = null;

    {
        if (ccyPairs != null) {
            for (int i = 0; i < ccyPairs.length; i++) {
                CCY_DP.put(ccyPairs[i][0], new Integer(ccyPairs[i][1]));
            }
        }
    }
    public final static int getDP(String ccy) {

        if (CCY_DP == null) {
        	CCY_DP = new Hashtable();
            for (int i = 0; i < ccyPairs.length; i++) {
                CCY_DP.put(ccyPairs[i][0], new Integer(ccyPairs[i][1]));
            }
        }

        if(CCY_DP.get(ccy) != null){
            return ((Integer) CCY_DP.get(ccy)).intValue();    
        }
        return -1;
    }
}