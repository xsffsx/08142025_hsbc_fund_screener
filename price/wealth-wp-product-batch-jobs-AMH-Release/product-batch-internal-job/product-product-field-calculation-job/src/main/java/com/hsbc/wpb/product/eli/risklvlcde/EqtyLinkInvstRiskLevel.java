package com.dummy.wpb.product.eli.risklvlcde;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EqtyLinkInvstRiskLevel {

    private Long prodId;
    private String riskLvlCde;
    private Long tenor;
    private BigDecimal cptlProtcPct;
    private String undlQtyInd;
    private String undlRiskLvlCde;
}
