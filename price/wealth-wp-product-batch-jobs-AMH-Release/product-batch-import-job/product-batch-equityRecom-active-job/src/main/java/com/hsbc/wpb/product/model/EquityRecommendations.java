package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class EquityRecommendations {
    private String rating;
    private Double targetPrice;
    private Double upside;
    private String keyHighlight;
    private String rationale;
    private String risk;
    private String url;
    private String sector;
    private String industryGroup;
    private Double forwardPe;
    private Double forwardPb;
    private Double forwardDividendYield;
    private Double historicDividendYield;
    private String recommended;
}
