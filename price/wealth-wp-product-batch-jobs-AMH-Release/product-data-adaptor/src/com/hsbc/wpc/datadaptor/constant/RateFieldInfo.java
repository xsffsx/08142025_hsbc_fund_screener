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
 * Class Name		RateFieldInfo
 *
 * Creation Date	Jan 3, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Jan 3, 2006
 *    CMM/PPCR No.		
 *    Programmer		cbdhkg0010
 *    Description
 * 
 */
package com.dummy.wpc.datadaptor.constant;

import java.math.BigDecimal;

import com.dummy.wpc.common.exception.WPCBaseException;

public class RateFieldInfo extends FieldInfo {

    public final static int THROW_ERROR = 0;
    public final static int ROUND_UP_IF_EXCEED_DP = 1;

    private int length = 0;
    private int minorUnit = 0; // i.e. decimal place
    private int errorHandling;

    public RateFieldInfo(int in_errorHandling,
        String in_fieldName,
        int in_length,
        int in_minorUnit) {
        super(in_fieldName);
        errorHandling = in_errorHandling;
        length = in_length;
        minorUnit = in_minorUnit;
    }

    /**
     * @return Returns the length.
     */
    public int getLength() {
        return length;
    }

    /**
     * @return Returns the minorUnit.
     */
    public int getMinorUnit() {
        return minorUnit;
    }

    public Rate validate(Rate r) throws WPCBaseException {

        // check length
    	// negative index
    	int IndLen = 0;
    	if(r.getAmount() < 0 ){
			IndLen = getLength() + 1;
    	}
    	else{
    		IndLen = getLength();
    	}
    	
        if (String.valueOf(r.getAmount()).length() > IndLen) {
            throw new WPCBaseException(Rate.class, "validate", "Exceed Length : " + getFieldName() + ":" + r.getAmount() + ":" + r.getMinorUnit() + "[" + getLength()+"," + getMinorUnit()+"]");
        }

        if (r.getMinorUnit() > minorUnit) {
            if (errorHandling == ROUND_UP_IF_EXCEED_DP) {
                BigDecimal b = new BigDecimal(r.getAmount()).movePointLeft(r.getMinorUnit());
                b = b.setScale(minorUnit, BigDecimal.ROUND_HALF_UP);
                r.setAmount(b.movePointRight(minorUnit).intValue());
                r.setMinorUnit(minorUnit);
            } else {
            	throw new WPCBaseException(Rate.class, "validate", ": " + getFieldName() + ":" + r.getAmount() + ":" + r.getMinorUnit() + "[" + getLength()+"," + getMinorUnit()+"]");
            }
        }
        return r;
    }
}