package com.dummy.wpb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "sys_parm")
public class SystemParmPo {

    @Id
    private String _id;

    private String ctryRecCde;

    private String grpMembrRecCde;

    private String parmCde;

    private String docFinTypeCde;

    private String docFinCatCde;

    private String parmValueText;

    private Date recCreatDtTm;

    private Date recUpdtDtTm;

}