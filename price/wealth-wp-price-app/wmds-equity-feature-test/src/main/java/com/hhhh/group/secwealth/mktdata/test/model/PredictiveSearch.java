package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PredictiveSearch {

    private List<ProdAltNumSeg> prodAltNumSegs;

    @JsonAlias("Product type")
    private String productType;

    private String productSubType;

    @JsonAlias("Country tradable code")
    private String countryTradableCode;

    private String allowBuy;

    private String allowSell;

    private String productName;

    private String productShortName;

    private String productCcy;

    private String market;

    @JsonAlias("Exchange")
    private String exchange;

    private String fundHouseCde;

    private String bondIssuer;

    private String allowSellMipProdInd;

    private String allowTradeProdInd;

    private String prodTaxFreeWrapActStaCde;

    private String allowSwInProdInd;

    private String allowSwOutProdInd;

    private String[] fundUnSwitchCode;

    private String[] swithableGroups;

    private String[] assetCountries;

    private String[] assetSectors;

    private String[] parentAssetClasses;

    private String[] channelRestrictList;

    private String[] chanlCdeList;

    private String symbol;

    private String productCode;

    private String riskLvlCde;

    private String prodStatCde;

    private String restrOnlScribInd;

    private String piFundInd;

    private String deAuthFundInd;

}
