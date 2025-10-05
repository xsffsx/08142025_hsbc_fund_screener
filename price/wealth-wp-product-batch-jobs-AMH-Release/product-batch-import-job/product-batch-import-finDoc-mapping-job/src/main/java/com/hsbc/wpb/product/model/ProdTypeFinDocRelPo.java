package com.dummy.wpb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "prod_type_fin_doc_type_rel")
public class ProdTypeFinDocRelPo {

    @Id
    private String _id;
    private String ctryRecCde;
    private String grpMembrRecCde;
    private String prodTypeCde;
    private String docFinTypeCde;
    private Long docDispSeqNum;
    private String dispMisngLinkInd;
    private String chanlComnCde;

    private LocalDateTime recCreatDtTm;

    private LocalDateTime recUpdtDtTm;
}