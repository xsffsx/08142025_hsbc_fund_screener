package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class EsgDataItem {
    private String identifierType;
    private String identifierValue;
    private String validIdentifier;
    private String isin;
    private String smartId;
    private String msciIssuerName;
    private String overrideSecurityName;
    private String siClass;
    private String etiReported;
    private String esgScore;
    private String esgRating;
    private String carbonIntens;
    private String sdgThmRkBscNeeds;
    private String sdgThmRkEmp;
    private String sdgThmRkClimChg;
    private String sdgThmRkNatuCapt;
    private String sdgThmRkGov;
    private String esgInd;
    private String errorMessage;
    private String updateDate;
}
