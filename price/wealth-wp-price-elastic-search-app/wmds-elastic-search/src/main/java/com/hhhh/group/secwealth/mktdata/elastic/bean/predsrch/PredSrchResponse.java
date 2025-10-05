package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;

import java.util.List;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
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

    private String islmProdInd;

    private String exchange;

    private String fundHouseCde;

    private String bondIssuer;//

    private String allowSellMipProdInd;

    private String invstMipCutOffDayofMth;

    private String invstMipMinAmt;

    private String invstMipIncrmMinAmt;

    private String allowTradeProdInd;

    private String prodTaxFreeWrapActStaCde;

    private String allowSwInProdInd;

    private String allowSwOutProdInd;

    private List<String> fundUnSwitchCode;//

    private List<String> swithableGroups;

    private List<String> assetCountries;

    private List<String> assetSectors;

    private List<String> parentAssetClasses;

    private List<String> channelRestrictList;//

    private List<String> chanlCdeList;

    private String symbol;

    private String ric;

    private String productCode;

    private String riskLvlCde;

    private String prodStatCde;

    private String restrOnlScribInd;

    private String piFundInd;

    private String deAuthFundInd;

    // Add premRecomInd, for UK ut Upgrade new requirement
    // like best seller
    private String premRecomInd;
    
    private String gbaAcctTrdb;
    
    private String gnrAcctTrdb;
    
    private String esgInd;

    private String wpbProductInd;

    private String cmbProductInd;

    private String restrCmbOnlSrchInd;

    // for P code
    private String prodCode;

    private String vaEtfIndicator;

    private String houseViewIndicator;
    private String houseViewRecentUpdate;
    private String houseViewRating;
}
