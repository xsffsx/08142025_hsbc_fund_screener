package com.dummy.wpb.product.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MarketRate implements Serializable {

    private String curveType;

    private String currencyPair;

    private Double midRate;

    private String xpeTime;
}
