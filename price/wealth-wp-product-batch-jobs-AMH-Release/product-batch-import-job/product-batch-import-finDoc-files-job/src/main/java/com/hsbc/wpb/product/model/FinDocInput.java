package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class FinDocInput {
    private String ctryCde;
    private String orgnCde;
    private String fileReqName;
    private Long seqNum = 0L;
    private String docTypeCde;    //fin_doc_upld.docFinTypeCde
    private String docSubtypeCde; //fin_doc_upld.docFinCatCde
    private String docId;         //fin_doc_upld.docFinCde
    private String langCatCde = null;   //fin_doc_upld.langFinDocCde
    private String formtTypeCde;  //fin_doc_upld.formtDocCde
    private String docIncmName;   //pdf fileName  fin_doc_upld.fileDocName fin_doc_upld.docRecvName
    private String docExplnText;  //fin_doc_upld.docDesc
    private String prodTypeCde;
    private String prodSubtypeCde;
    private String prodCde;
    private String urgInd;
    private String emailAdrRpyText;
    private String effDt = null;
    private String effTm = null;
    private String expirDt = null;       //fin_doc_upld.docExpirDt CCYYMMDD
    private String statCde = null;

    private Long prodId = 0L; //corresponding prod_id in PROD table
    private String aprvReqInd;
    private String archReqInd;
    private Long docSzNum;
}