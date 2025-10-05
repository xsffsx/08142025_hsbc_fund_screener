package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class EsgDocument {
    private String siClass;
    private Double esgScore;
    private String esgRating;
    private Double carbonIntens;
    private Double sdgThmRkBscNeeds;
    private Double sdgThmRkEmp;
    private Double sdgThmRkClimChg;
    private Double sdgThmRkNatuCapt;
    private Double sdgThmRkGov;
}
