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
 * Class Name		EmailConstants
 *
 * Creation Date	Mar 7, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	
 *    CMM/PPCR No.		
 *    Programmer		
 *    Description
 * 
 */
package com.dummy.wmd.wpc.graphql.constant;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class EmailConstants {
    public static final String FDDFLTRCVR = "#WPC_SUPPORT";
    public static final String HFIM = "HFIM";
    public static final String HFIK = "HFIK";

    public static final String TYPE1COD = "1001";
    public static final String TYPE2COD = "2001";
    public static final String HEAD1 = "HEADER                        ";
    public static final String HEAD2 = "   HFI ENS ";
    public static final String TRAILER1 = "TRAILER                       ";
    public static final String TRAILER2 = "   HFI ENS ";
    public static final String MESSAGEFILENAME = "HFEMM";
    public static final String KEYFILENAME = "HFEMK";

}