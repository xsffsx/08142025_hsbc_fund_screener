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
 * Class Name		Rate
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

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.common.exception.WPCBaseException;

/**
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java.internal.UML2JavaTransform)"
 */
public class Rate {

    /**
     * Minor Unit
     */
    private int minorUnit;
    /**
     * Amount 
     */
    private long amount;

    public Rate() {
    }

    /**
     * Transform an integer to Rate based on RateFieldInfo
     * @param amt
     * @param i
     * @return
     * @throws IFWException
     */
    public static Rate newInstance(long amt, RateFieldInfo i) throws WPCBaseException {
        final String m = "newInstance";
        if (i == null) {
            return null;
        }
        Rate r = new Rate();
        // append zero
        // multiple by no. of decimal place defined in RateFieldInfo
        r.setAmount(new Double(amt * Math.pow(10, i.getMinorUnit())).longValue());
        r.setMinorUnit(i.getMinorUnit());
        r = i.validate(r);
        return r;
    }

    /**
     * Transform BigDecimal to Rate
     * @param amt
     * @return
     * @throws WPCBaseException
     */
    public static Rate newInstance(BigDecimal amt) {
        final String m = "newInstance";
        if (amt == null) {
            return null;
        }
        Rate r = new Rate();
        r.setMinorUnit(amt.scale());
        // append zero
        // multiple by no. of decimal place defined in RateFieldInfo
        amt = amt.movePointRight(amt.scale());
        r.setAmount(amt.longValue());
        return r;
    }
    
    public static BigDecimal newBigDecimal(String string) throws WPCBaseException {
    	if(string != null && !"".equals(string)){
    		return new BigDecimal(string);
    	}
    	return null;
    }
    

    public static Rate newInstance(BigDecimal amt, RateFieldInfo i) throws WPCBaseException {
        final String m = "newInstance";
        if (amt == null || i == null) {
            return null;
        }
        Rate r = new Rate();
        // append zero
        // multiple by no. of decimal place defined in RateFieldInfo
        if (amt.scale() == i.getMinorUnit()) {
            amt = amt.multiply(new BigDecimal(Math.pow(10,
                i.getMinorUnit())));
            amt.setScale(0);
        } else {
            throw new WPCBaseException(Rate.class,
                m,
                "Incompatible decima place : " + i.getFieldName() + " : " + amt.toString() + " [" + i.getLength() +"," + i.getMinorUnit() + "]");
        }
        r.setAmount(amt.longValue());
        r.setMinorUnit(i.getMinorUnit());
        r = i.validate(r);
        return r;
    }

    public static Rate newInstance(GSMRate grate, RateFieldInfo i) throws WPCBaseException {
        final String m = "newInstance";
        if(grate == null || i == null || StringUtils.isBlank(grate.getAmt())){
        	return null;
        }
        BigDecimal amt = new BigDecimal(grate.getSignInd()
            + grate.getAmt());
        amt = amt.movePointLeft(Integer.parseInt(grate.getPrecsnCnt()));
//        amt = amt.setScale(Integer.parseInt(grate.getPrecsnCnt()));
        return newInstance(amt, i);
    }
    
    public static String rateCheck(Rate rat){
    	if(rat != null){
    		return String.valueOf(rat.getAmount());
    	}
		return null;
    }
    
    public static BigDecimal DecimalGet(Rate rate){
		if(rate != null){
			return rate.toBigDecimal();
		}
    	return null;
    }
    
    public static GSMRate toGSMRate(GSMRate grate, Rate rate) {

        if (rate != null && grate != null) {
            grate.setAmt(String.valueOf(Math.abs(rate.getAmount())));
            grate.setPrecsnCnt(String.valueOf(rate.getMinorUnit()));
            if (rate.getAmount() < 0) {
                grate.setSignInd("-");
            } else if (rate.getAmount() >= 0) {
                grate.setSignInd("+");
            }
        } else {
            grate = null;
        }
        return grate;
    }

    public String toString() {
	        return "Amount : " + getAmount() + ", Minor Unit : "
	            + getMinorUnit();   	
    }

    public BigDecimal toBigDecimal() {
        String s = String.valueOf(getAmount());
        // BigDecimal amt = new BigDecimal(getAmount());
        BigDecimal amt = new BigDecimal(s);
        amt = amt.movePointLeft(getMinorUnit());
//        amt = amt.setScale(getMinorUnit());  
        return amt;
    }

    /**
     * @return Returns the amount.
     */
    public long getAmount() {
        return amount;
    }

    /**
     * @param amount The amount to set.
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }

    /**
     * @return Returns the minorUnit.
     */
    public int getMinorUnit() {
        return minorUnit;
    }

    /**
     * @param minorUnit The minorUnit to set.
     */
    public void setMinorUnit(int minorUnit) {
        this.minorUnit = minorUnit;
    }
}