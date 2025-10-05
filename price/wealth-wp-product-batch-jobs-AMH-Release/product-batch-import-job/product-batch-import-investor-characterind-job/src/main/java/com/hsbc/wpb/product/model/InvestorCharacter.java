package com.dummy.wpb.product.model;

import lombok.Data;

import java.util.Date;


@Data
public class InvestorCharacter {
    private String ctryRecCde;

    private String grpMembrRecCde;

    private Long prodId;

    private String maintCde;

    private String prodAltPrimNum;

    private String prodTypeCde;

    private String prodMktCde;

    private String prodDervtCde;

    private String prodDervtRvsCde;

    private String prodDervRvsEffDt;

    private String internalProdTypeCde;

    private String internalProdSubTypeCde;

    private Date recUpdtDtTm;

}
