package com.dummy.wpb.product.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class ReferenceData {

    private String _id;
    private String ctryRecCde;
    private String grpMembrRecCde;
    private String cdvTypeCde;
    private String cdvCde;
    private String cdvDesc;
    private String cdvPllDesc;
    private String cdvSllDesc;
    private Long cdvDispSeqNum;
    private String cdvParntTypeCde;
    private String cdvParntCde;
    private String recCmntText;
    List<String> chanlComnCde;
    private String recCreatDtTm;
    private String recUpdtDtTm;

    @Override
    public String toString() {
        return "ReferenceData{" +
                "ctryRecCde='" + ctryRecCde + '\'' +
                ", grpMembrRecCde='" + grpMembrRecCde + '\'' +
                ", cdvTypeCde='" + cdvTypeCde + '\'' +
                ", cdvCde='" + cdvCde + '\'' +
                '}';
    }
}
