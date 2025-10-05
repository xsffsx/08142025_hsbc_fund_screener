package com.dummy.wpb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "sys_parm")
public class SystemParameter {

    @Id
    private String id = UUID.randomUUID().toString();

    private String ctryRecCde;

    private String grpMembrRecCde;

    private String parmCde;

    private String parmValueText;

    private LocalDateTime recCreatDtTm;

    private LocalDateTime recUpdtDtTm;
}
