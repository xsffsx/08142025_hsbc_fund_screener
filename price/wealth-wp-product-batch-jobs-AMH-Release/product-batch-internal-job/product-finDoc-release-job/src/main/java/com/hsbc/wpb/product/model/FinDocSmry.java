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
 * Class Name		FinDocSmry
 *
 * Creation Date	Jan 24, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Jan 24, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.model;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class FinDocSmry {
    
    private String ctryCde;
    private String orgnCde;
    private Long docSerNum = 0L;
    private String docTypeCde;
    private String docSubtpCde;

    private String docId;  
    private String langCatCde;
    private String statCde;
    private String formtTypeCde;
    private Long docSizeNum = 0L;
    
    private String docIncmName;
    private String docExplnText;
    private String urgInd;
    private String emailAdrRpyText;
    private String aprvReqInd;
    
    private String statFsCde;    
    private String urlFsText;    
    private String docFsName;
    private String archReqInd;
    private String statArchCde;

    private String fileArchName;
    private String urlArchText;
    private String docSerArchNum;
    private String urlSysText;
    private Date creatDt;

    private Time creatTm;   
    private Date updtLastDt;
    private Time updtLastTm;
    private Date aprvDt;
    private Time aprvTm;

    private Date relStartDt;
    private Time relStartTm;
    private Date relEndDt;
    private Time relEndTm;
    private Date archStartDt;

    private Time archStartTm;
    private Date archEndDt;
    private Time archEndTm;
    private Date effDt;
    private Time effTm;

    private Date expirDt;
    
    private String prodTypCde;
    private String prodSubtpCde;
    private String prodCde;
}
