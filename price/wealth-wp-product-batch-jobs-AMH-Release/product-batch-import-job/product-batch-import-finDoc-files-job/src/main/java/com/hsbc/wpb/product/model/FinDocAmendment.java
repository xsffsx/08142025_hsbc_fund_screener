package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class FinDocAmendment {
    private String ctryRecCde;
    private String grpMembrRecCde;
    private Long rsrcItemIdFinDoc;
    private String docFinTypeCde;
    private String docFinCatCde;
    private String docFinCde;
    private String langFinDocCde;
    private String docStatCde;
    private String formtDocCde;
    private Long docSzNum;
    private String docRecvName;
    private String docDesc;
    private String reqUrgntProcInd;
    private String emailAddrRplyText;
    private String reqAproveInd;
    private String docServrStatCde;
    private String urlFileServrText;
    private String fileDocName;
    private String reqArchInd;
    private String docArchStatCde;
    private String fileArchRqstName;
    private String urlArchSysText;
    private String docArchSerNum;
    private String urlLclSysText;
    private String docAproveDtTm;
    private String docRleasStartDtTm;
    private String docRleasEndDtTm;
    private String docArchStartDtTm;
    private String docArchEndDtTm;
    private String docEffDtTm;
    private String docExpirDt;
    private String recCreatDtTm;
    private String recUpdtDtTm;
}
