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
 * Class Name		Amount
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
public class Amount {

    /**
     * Comment for <code>currencyCode</code>
     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java.internal.UML2JavaTransform)"
     */
    private String currencyCode;
    private long amount;
    private int minorUnit;

    /**
     * Transform the amount value from DB2 ResultSet to complex type which consists of 
     * 1/ amount value in integer, &
     * 2/ minor units of the value
     * E.g. 14000.000 -> amount value : 14000000 & minor units : 3
     * Throw error when
     * 1/ defined minor units is smaller than original scale
     * @param ccyCode
     * @param amt
     * @param minorUnit
     * @return
     * @throws WPCBaseException
     */
    public static Amount newInstance(String ccyCode,
        BigDecimal amt,
        int minorUnit) throws WPCBaseException {
        final String m = "newInstance";
        if (ccyCode == null || amt == null) {
            return null;
        }
        Amount amount = new Amount();
        amount.setCurrencyCode(ccyCode);
        // the BigDecimal should be in zero scale
        if (amt.scale() >= 1) {
            throw new WPCBaseException(Amount.class,m,
                "Incompatible decimal place : " + amt.toString());
        }
        amount.setAmount(Long.parseLong(amt.toString()));
        amount.setMinorUnit(minorUnit);
        return amount;
    }

    public static Amount newInstance(GSMAmount gamt, AmountFieldInfo i, boolean checkMad) throws WPCBaseException {
        final String m = "newInstance";

        if (gamt == null || i == null
            || StringUtils.isBlank(gamt.getAmt())) {
            return null;
        }
        
        //check currency code null
        String ccyCode = gamt.getCcod();
        if(checkMad){
	        if (StringUtils.isBlank(ccyCode)) {
	        	throw new WPCBaseException (Rate.class, "validate", "Currency value invalid when Amount value not null: " + i.getFieldName() + ":" + gamt.getAmt() + ":" + gamt.getCcod());
	        }
        }
        
        String signInd = "";
        if (gamt.getSignInd() != null) {
            signInd = gamt.getSignInd();
        }
        BigDecimal amt = new BigDecimal(signInd + gamt.getAmt());
        if(amt.scale() >= 1){
            // input from GSMAmount should have no decimal place
            throw new WPCBaseException(Amount.class,
                m,
                "Incompatible decimal place : "
                    + i.getFieldName() + " : " + amt.toString()
                    + "[" + i.getLength() + ","
                    + i.getMinorUnit() + "]");            
        }
        // check minor unit null
        if(StringUtils.isBlank(gamt.getPrecsnCnt())){
        	throw new WPCBaseException(Rate.class, "validate", "MinorUnit value invalid when Amount value not null: " + i.getFieldName() + ":" + gamt.getAmt() + ":" + gamt.getPrecsnCnt());
        }
        int minorUnit = Integer.parseInt(gamt.getPrecsnCnt());
        //
        Amount amount = new Amount();
        amount.setCurrencyCode(ccyCode);
//        amount.setAmount(amt.intValue());
        amount.setAmount(amt.longValue());
        amount.setMinorUnit(minorUnit);

        // get original value
        if (i.isOverrideCurrencyDP()) {
            amt = amt.setScale(minorUnit);
            // check if overrided by currency
            int mu = i.getMinorUnit();
            mu = CRNCY_DP_CONST.getDP(ccyCode);
            if (amt.scale() <= mu) {
                amt = amt.multiply(new BigDecimal(Math.pow(10, mu)));
                amt.setScale(0);
            } else {
                throw new WPCBaseException(Amount.class,m,
                    "Incompatible decimal place : "
                        + i.getFieldName() + " : " + amt.toString()
                        + "[" + i.getLength() + ","
                        + i.getMinorUnit() + "]");
            }
        }
        amount = i.validate(amount);
        return amount;

    }

    public static GSMAmount toGSMAmount(GSMAmount gamt, Amount amt) {

        if (amt != null && gamt != null) {
            gamt.setCcod(amt.getCurrencyCode());
            gamt.setPrecsnCnt(String.valueOf(amt.getMinorUnit()));
            if (amt.getAmount() < 0) {
                gamt.setSignInd("-");
            } else if (amt.getAmount() >= 0) {
                gamt.setSignInd("+");
            }
            // +/- sign from amount
            long tmp = Math.abs(amt.getAmount());
            gamt.setAmt(String.valueOf(tmp));
        } else {
            gamt = null;
        }
        return gamt;
    }

    public static BigDecimal AmountGet(Amount amt) {
        if (amt != null) {
            long value = amt.getAmount();
            return BigDecimal.valueOf(value);
        }
        return null;
    }

    public static String CurrencyGet(Amount amt) {
        if (amt != null) {
            return amt.getCurrencyCode();
        }
        return null;
    }

    public static Integer MinorUnitGet(Amount amt) {

        if (amt != null) {
            return new Integer(amt.getMinorUnit());
        }
        return null;
    }

    public String toString() {
        return "Currency : " + getCurrencyCode() + ", Amount : "
            + getAmount() + ", MinorUnit : " + getMinorUnit();
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
     * @return Returns the currencyCode.
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode The currencyCode to set.
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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