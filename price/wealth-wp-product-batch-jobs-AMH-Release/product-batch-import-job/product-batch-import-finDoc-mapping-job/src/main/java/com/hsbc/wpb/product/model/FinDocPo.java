package com.dummy.wpb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime docAproveDtTm;
    private LocalDateTime docRleasStartDtTm;
    private LocalDateTime docRleasEndDtTm;
    private LocalDateTime docArchStartDtTm;
    private LocalDateTime docArchEndDtTm;
    private LocalDateTime docEffDtTm;
    private LocalDate docExpirDt;
    private LocalDateTime recCreatDtTm;
    private LocalDateTime recUpdtDtTm;
    
}