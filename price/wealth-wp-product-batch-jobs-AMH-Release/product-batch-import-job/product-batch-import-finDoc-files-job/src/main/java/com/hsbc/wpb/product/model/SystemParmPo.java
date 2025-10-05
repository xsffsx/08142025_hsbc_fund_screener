package com.dummy.wpb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "sys_parm")
public class SystemParmPo {

    @Id
    private String _id;

    private String ctryRecCde;

    private String grpMembrRecCde;

    private String parmCde;

    private String parmValueText;

    private LocalDateTime recCreatDtTm;

    private LocalDateTime recUpdtDtTm;

}