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
 * Class Name		AmountFieldInfo
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


public class AmountFieldInfo extends RateFieldInfo {

    public final static int THROW_ERROR = 0;
    public final static int ROUND_UP_IF_EXCEED_DP = 1;
    public final static int CCY_LENGTH = 3;
    
    private int length = 0;
    private int minorUnit = 0; // i.e. decimal place
    private int ccdLength = 0;
    
	/**
	 * @return Returns the ccdLength.
	 */
	public int getCcdLength() {
		return ccdLength;
	}
	
    private boolean overrideCurrencyDP = true;
    private int errorHandling;   
    
    public AmountFieldInfo(int in_errorHandling,
        String in_fieldName,
        boolean in_overrideCurrencyDP,
        int in_length,
        int in_minorUnit) {
        super(in_errorHandling, in_fieldName, in_length, in_minorUnit);
        overrideCurrencyDP = in_overrideCurrencyDP;
    }

    /**
     * @return Returns the overrideCurrencyDP.
     */
    public boolean isOverrideCurrencyDP() {
        return overrideCurrencyDP;
    }

    public Amount validate(Amount a) throws WPCBaseException {
    	// check overrideCurrencyDP
//        if (overrideCurrencyDP) {
//            
//        } else {
//        }

    	// check length
    	// negative index
    	int IndLen = 0;
    	if(a.getAmount() < 0 ){
			IndLen = getLength() + 1;
    	}
    	else{
    		IndLen = getLength();
    	}
    	
        if (String.valueOf(a.getAmount()).length() > IndLen) {
            throw new WPCBaseException(Rate.class, "validate", "Exceed Length : " + getFieldName() + ":" + a.getAmount() + ":" + a.getMinorUnit() + "[" + getLength()+"," + getMinorUnit()+"]");
        }
        
        if (a.getMinorUnit() > getMinorUnit()) {
            if (errorHandling == ROUND_UP_IF_EXCEED_DP) {
                BigDecimal b = new BigDecimal(a.getAmount()).movePointLeft(a.getMinorUnit());
                b = b.setScale(minorUnit, BigDecimal.ROUND_HALF_UP);
                a.setAmount(b.movePointRight(minorUnit).intValue());
                a.setMinorUnit(minorUnit);
            } else {
            	throw new WPCBaseException(Rate.class, "validate", ": " + getFieldName() + ":" + a.getAmount() + ":" + a.getMinorUnit() + "[" + getLength()+"," + getMinorUnit()+"]");
            }
        }
        
        // check currency lenght
        if(a.getCurrencyCode().length() > CCY_LENGTH){
        	throw new WPCBaseException(Rate.class, "validate", ": " + getFieldName() + ":" + a.getAmount() + ":" + a.getMinorUnit() + 
        			":" + a.getCurrencyCode() + "[" + CCY_LENGTH +"]");
        }
        
        return a;
    }
}