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
 * Class Name		FinDocULReq
 *
 * Creation Date	Mar 29, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 29, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "fin_doc_upld")
public class FinDocULReqPo {

    @Id
    private String _id;
    private String ctryRecCde = null;
    private String grpMembrRecCde = null;
    private String fileRqstName = null;
    private Long docUpldSeqNum = 0L;
    private String docFinTypeCde = null;
    private String docFinCatCde = null;
    private String docFinCde = null;
    private String langFinDocCde = null;
    private String docStatCde = null;
    private String reasonRejText = null;
    private Long docSerNum = 0L;
    private String formtDocCde = null;
    private Long docSzNum = 0L;
    private String docRecvName = null;
    private String docDesc = null;
    private String reqUrgntProcInd = null;
    private String emailAddrRplyText = null;
    private String prodTypeCde = null;
    private String prodSubtpCde = null;
    private String prodCde = null;
    private String reqAproveInd = null;
    private String docServrStatCde = null;
    private String urlFileServrText = null;
    private String fileDocName = null;
    private String reqArchInd = null;
    private String docArchStatCde = null;
    private String fileArchRqstName = null;
    private String urlArchSysText = null;
    private String docArchSerNum = null;
    private String urlLclSysText = null;
    private Date recCreatDtTm = null;
    private Date recUpdtDtTm = null;
    private Date docAproveDtTm = null;
    private Date docRleasStartDtTm = null;
    private Date docRleasEndDtTm = null;
    private Date docArchStartDtTm = null;
    private Date docArchEndDtTm = null;
    private Date docEffDtTm = null;
    private Date docExpirDt = null;

}
