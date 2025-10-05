package com.dummy.wpb.product.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenizedGoldProduct implements Serializable {

    private Long prodId;

    private String ctryRecCde;

    private String grpMembrRecCde;

    private String prodTypeCde;

    private String prodSubtpCde;

    private String currencyPairs;
}
