package com.dummy.wpb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "fin_doc")
public class FinDocPo {
    @Id
    private String _id;
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
    private Date docAproveDtTm;
    private Date docRleasStartDtTm;
    private Date docRleasEndDtTm;
    private Date docArchStartDtTm;
    private Date docArchEndDtTm;
    private Date docEffDtTm;
    private Date docExpirDt;
    private Date recCreatDtTm;
    private Date recUpdtDtTm;
}
