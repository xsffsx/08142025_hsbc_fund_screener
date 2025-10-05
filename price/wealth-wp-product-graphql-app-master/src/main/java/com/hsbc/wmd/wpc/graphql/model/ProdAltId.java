package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

@Data
public class ProdAltId {

    String prodTypeCde;
    String prodAltNum;
    String prodCdeAltClassCde;

    public ProdAltId(String prodTypeCde, String prodAltNum, String prodCdeAltClassCde) {
        this.prodTypeCde = prodTypeCde;
        this.prodAltNum = prodAltNum;
        this.prodCdeAltClassCde = prodCdeAltClassCde;
    }
}
