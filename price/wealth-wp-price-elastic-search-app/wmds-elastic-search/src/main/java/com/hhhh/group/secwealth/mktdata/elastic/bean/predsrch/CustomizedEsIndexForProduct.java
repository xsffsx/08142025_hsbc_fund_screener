/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "testutb", createIndex = false, shards = 1, replicas = 0)
@Getter
@Setter
public class CustomizedEsIndexForProduct {
    private String id;
    @Field(type = FieldType.Keyword)
    private String ctryRecCde;
    @Field(type = FieldType.Keyword)
    private String grpMembrRecCde;
    private List<ProdAltNumSeg> prodAltNumSegs;
    private String productType;
    private String productSubType;
    private String countryTradableCode;
    @Field(type = FieldType.Keyword)
    private String allowBuy;
    @Field(type = FieldType.Keyword)
    private String allowSell;
    private String productName;
    private List<String> productNameAnalyzed;
    private List<String> productShrtNameAnalyzed;
    private String productShortName;
    @Field(type = FieldType.Keyword)
    private String productCcy;
    @Field(type = FieldType.Keyword)
    private String market;
    @Field(type = FieldType.Keyword)
    private String islmProdInd;
    @Field(type = FieldType.Keyword)
    private String exchange;
    @Field(type = FieldType.Keyword)
    private String prodShoreLocCde;
    @Field(type = FieldType.Keyword)
    private String fundHouseCde;
    @Field(type = FieldType.Keyword)
    private String bondIssuer;
    @Field(type = FieldType.Keyword)
    private String allowSellMipProdInd;
    @Field(type = FieldType.Keyword)
    private String invstMipCutOffDayofMth;
    @Field(type = FieldType.Keyword)
    private String invstMipMinAmt;
    @Field(type = FieldType.Keyword)
    private String invstMipIncrmMinAmt;
    @Field(type = FieldType.Keyword)
    private String allowTradeProdInd;
    @Field(type = FieldType.Keyword)
    private String prodTaxFreeWrapActStaCde;
    // add
    @Field(type = FieldType.Keyword)
    private String prodStatCde;
    @Field(type = FieldType.Keyword)
    private List<String> resChannelCde;
    @Field(type = FieldType.Keyword)
    private List<String> chanlCde;
    @Field(type = FieldType.Keyword)
    private String restrOnlScribInd;
    @Field(type = FieldType.Keyword)
    private String piFundInd;
    @Field(type = FieldType.Keyword)
    private String deAuthFundInd;
    @Field(type = FieldType.Keyword)
    private String gbaAcctTrdb;
    @Field(type = FieldType.Keyword)
    private String gnrAcctTrdb;
    // add 0613
    @Field(type = FieldType.Keyword)
    private List<String> chanlAllowBuy;
    @Field(type = FieldType.Keyword)
    private List<String> chanlAllowSell;

    @Field(type = FieldType.Keyword)
    private List<String> chanlAllowSwitchOut;
    @Field(type = FieldType.Keyword)
    private List<String> chanlAllowSwitchIn;
    @Field(type = FieldType.Keyword)
    private String isrBndNme;

    @Field(type = FieldType.Keyword)
    private String allowSwInProdInd;
    @Field(type = FieldType.Keyword)
    private String allowSwOutProdInd;
    private List<String> switchableGroups;
    @Field(type = FieldType.Keyword)
    private List<String> assetCountries;
    @Field(type = FieldType.Keyword)
    private List<String> assetSectors;
    @Field(type = FieldType.Keyword)
    private List<String> parentAssetClasses;
    private String symbol;
    private String symbolLowercase;

    private String sedol;

    private String isin;
    @Field(type = FieldType.Keyword)
    private String key;
    @Field(type = FieldType.Keyword)
    private String productCode;
    @Field(type = FieldType.Keyword)
    private String riskLvlCde;
    @Field(type = FieldType.Keyword)
    private String productTypeWeight;
    private String countryTradableCodeWeight;
    private String marketWeight;
    @Field(type = FieldType.Keyword)
    private String popularity;
    @Field(type = FieldType.Keyword)
    private List<String> fundUnswithSeg;
    @Field(type = FieldType.Keyword)
    private String sequence;    
    @Field(type = FieldType.Keyword)
    private String esgInd;

    // Add premRecomInd, for UK ut Upgrade new requirement
    // like best seller
    @Field(type = FieldType.Keyword)
    private String premRecomInd;

    private String prodCode;

    private String vaEtfIndicator;

    private String houseViewIndicator;
    private String houseViewRecentUpdate;
    private String houseViewRating;


    // cmb ut
    @Field(type = FieldType.Keyword)
    private String wpbProductInd;
    @Field(type = FieldType.Keyword)
    private String cmbProductInd;
    @Field(type = FieldType.Keyword)
    private String restrCmbOnlSrchInd;

}
