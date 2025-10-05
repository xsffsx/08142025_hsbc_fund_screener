package com.dummy.wpb.product.jpo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "equity_recommendations")
public class EquityRecommendationsPo {
    @Id
    private String id = UUID.randomUUID().toString();
    private String reuters;
    private String isin;
    private String rating;
    private Double targetPrice;
    private Double upside;
    private String keyHighlight;  //From Active List
    private String rationale;     //From Active List
    private String risk;          //From Active List
    private String url;
    private String sector;
    private String industryGroup;
    private Double forwardPe;
    private Double forwardPb;
    private Double forwardDividendYield;
    private Double historicDividendYield;
    private String recommended;
    private String errMsg;
    private LocalDateTime recCreatDtTm;
    private LocalDateTime recUpdtDtTm;
}
