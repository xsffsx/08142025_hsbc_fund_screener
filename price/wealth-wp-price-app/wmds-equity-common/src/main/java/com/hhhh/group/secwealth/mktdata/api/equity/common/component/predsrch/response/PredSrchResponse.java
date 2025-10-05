/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredSrchResponse {

    private List<ProdAltNumSeg> prodAltNumSegs;

    private String productType;

    private String productSubType;

    private String countryTradableCode;

    private String allowBuy;

    private String allowSell;

    private String productName;

    private String productShortName;

    private String productSecondName;

    private String productCcy;

    private String market;

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

    private String symbol;

    private String productCode;

    private String riskLvlCde;

    private String prodStatCde;

    private String restrOnlScribInd;

}
