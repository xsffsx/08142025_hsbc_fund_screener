package com.dummy.wpb.product.model.graphql;

import lombok.Data;
import org.bson.Document;

import java.time.LocalDateTime;

@Data
public class Amendment {
    private Long _id;
    private String ctryRecCde;
    private String grpMembrRecCde;
    private String emplyNum;
    private String recStatCde;
    private String actionCde;
    private String docType;
    private Long docId;
    private String summary;
    private Document doc;
    private Document docBase;
    private Document docLatest;
    private Document diffFromAmendment;
    private Document diffFromLatest;
    private Document diffConflict;
    private String requestComment;
    private String approvalComment;
    private String approvedBy;
    private LocalDateTime recCreatDtTm;
    private LocalDateTime recUpdtDtTm;
}
