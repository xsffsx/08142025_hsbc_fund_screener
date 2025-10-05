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
 * Class Name		FinDocComConstants
 *
 * Creation Date	Apr 3, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Apr 3, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.constant;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class FinDocComConstants {

    //Language
    public static final String BL="BL";
    public static final String EN="EN";
    public static final String TW="TW";
    public static final String JP="JP";
    public static final String IN="IN";
    //STATUS
    public static final String APPROVAL = "A";
    public static final String PENDING = "P";
    public static final String REJECT = "R";
    public static final String CONFIRM = "C";
    public static final String PROCESSING = "I";
    public static final String PROC_APPROVAL = "V";
    
    public static final String FDSN="FDSN";
    public static final String WPC="WPC";
}
