package com.hhhh.group.secwealth.mktdata.predsrch.pres.beans;

import java.util.Arrays;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;

public class PredSrchResponse {

    /** The prod alt num seg. */
    private List<ProdAltNumSeg> prodAltNumSegs;

    /** The product type. */
    private String productType;

    /** The product sub type. */
    private String productSubType;

    /** The country tradable code. */
    private String countryTradableCode;

    /** The allow buy. */
    private String allowBuy;

    /** The allow sell. */
    private String allowSell;

    /** The product name. */
    private String productName;

    /** The product short name. */
    private String productShortName;

    private String productSecondName;

    /** The product ccy. */
    private String productCcy;

    /** The market. */
    private String market;

    /** The exchange. */
    private String exchange;

    /** The fund house cde. */
    private String fundHouseCde;

    /** Bond Issuer. */
    private String bondIssuer;

    /** The allow sell mip prod ind. */
    private String allowSellMipProdInd;

    /** The allow Trade prod ind. */
    private String allowTradeProdInd;

    /** The prod tax free wrap act status code. */
    private String prodTaxFreeWrapActStaCde;

    /** The allow SwitchIn prod ind. */
    private String allowSwInProdInd;

    /** The allow SwitchOut prod ind. */
    private String allowSwOutProdInd;

    private String[] fundUnSwitchCode;

    /** The swithable group. */
    private String[] swithableGroups;

    /** The asset country. */
    private String[] assetCountries;

    /** The asset sector. */
    private String[] assetSectors;

    /** The parent asset class. */
    private String[] parentAssetClasses;

    private String[] channelRestrictList;

    private String[] chanlCdeList;

    /** The symbol. */
    private String symbol;

    /** The product code. */
    private String productCode;

    /** The riskLvlCde. */
    private String riskLvlCde;

    private String prodStatCde;

    /** The restrict online subscription indicator. */
    private String restrOnlScribInd;

    private String piFundInd;

    private String deAuthFundInd;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PredictiveSearchResponse [prodAltNumSegs=");
        builder.append(this.prodAltNumSegs);
        builder.append(", productType=");
        builder.append(this.productType);
        builder.append(", productSubType=");
        builder.append(this.productSubType);
        builder.append(", countryTradableCode=");
        builder.append(this.countryTradableCode);
        builder.append(", allowBuy=");
        builder.append(this.allowBuy);
        builder.append(", allowSell=");
        builder.append(this.allowSell);
        builder.append(", productName=");
        builder.append(this.productName);
        builder.append(", productShortName=");
        builder.append(this.productShortName);
        builder.append(", exchange=");
        builder.append(this.exchange);
        builder.append(", productCcy=");
        builder.append(this.productCcy);
        builder.append(", fundUnswCdes=");
        builder.append(Arrays.toString(this.fundUnSwitchCode));
        builder.append(", assetCountrys=");
        builder.append(Arrays.toString(this.assetCountries));
        builder.append(", assetSectors=");
        builder.append(Arrays.toString(this.assetSectors));
        builder.append(", parentAssetClasses=");
        builder.append(Arrays.toString(this.parentAssetClasses));
        builder.append(", channelRestrictList=");
        builder.append(Arrays.toString(this.channelRestrictList));
        builder.append(", chanlCdeList=");
        builder.append(Arrays.toString(this.chanlCdeList));
        builder.append(", market=");
        builder.append(this.market);
        builder.append(", fundHouseCde=");
        builder.append(this.fundHouseCde);
        builder.append(", bondIssuer=");
        builder.append(this.bondIssuer);
        builder.append(", allowSellMipProdInd=");
        builder.append(this.allowSellMipProdInd);
        builder.append(", allowTradeProdInd=");
        builder.append(this.allowTradeProdInd);
        builder.append(", prodTaxFreeWrapActStaCde=");
        builder.append(this.prodTaxFreeWrapActStaCde);
        builder.append(", allowSwInProdInd=");
        builder.append(this.allowSwInProdInd);
        builder.append(", allowSwOutProdInd=");
        builder.append(this.allowSwOutProdInd);
        builder.append(", swithableGroups=");
        builder.append(Arrays.toString(this.swithableGroups));
        builder.append(", symbol=");
        builder.append(this.symbol);
        builder.append(", productCode=");
        builder.append(this.productCode);
        builder.append(", riskLvlCde=");
        builder.append(this.riskLvlCde);
        builder.append(", prodStatCde=");
        builder.append(this.prodStatCde);
        builder.append(", restrOnlScribInd=");
        builder.append(this.restrOnlScribInd);
        builder.append(", piFundInd=");
        builder.append(this.piFundInd);
        builder.append(", deAuthFundInd=");
        builder.append(this.deAuthFundInd);
        builder.append("]");
        return builder.toString();
    }


    /**
     * @return the prodAltNumSegs
     */
    public List<ProdAltNumSeg> getProdAltNumSegs() {
        return this.prodAltNumSegs;
    }

    /**
     * @param prodAltNumSegs
     *            the prodAltNumSegs to set
     */
    public void setProdAltNumSegs(final List<ProdAltNumSeg> prodAltNumSegs) {
        this.prodAltNumSegs = prodAltNumSegs;
    }

    /**
     * @return the productType
     */
    public String getProductType() {
        return this.productType;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    /**
     * @return the productSubType
     */
    public String getProductSubType() {
        return this.productSubType;
    }

    /**
     * @param productSubType
     *            the productSubType to set
     */
    public void setProductSubType(final String productSubType) {
        this.productSubType = productSubType;
    }

    /**
     * @return the countryTradableCode
     */
    public String getCountryTradableCode() {
        return this.countryTradableCode;
    }

    /**
     * @param countryTradableCode
     *            the countryTradableCode to set
     */
    public void setCountryTradableCode(final String countryTradableCode) {
        this.countryTradableCode = countryTradableCode;
    }

    /**
     * @return the allowBuy
     */
    public String getAllowBuy() {
        return this.allowBuy;
    }

    /**
     * @param allowBuy
     *            the allowBuy to set
     */
    public void setAllowBuy(final String allowBuy) {
        this.allowBuy = allowBuy;
    }

    /**
     * @return the allowSell
     */
    public String getAllowSell() {
        return this.allowSell;
    }

    /**
     * @param allowSell
     *            the allowSell to set
     */
    public void setAllowSell(final String allowSell) {
        this.allowSell = allowSell;
    }

    /**
     * @return the fundUnSwitchCode
     */
    public String[] getFundUnSwitchCode() {
        return this.fundUnSwitchCode;
    }


    /**
     * @param fundUnSwitchCode
     *            the fundUnSwitchCode to set
     */
    public void setFundUnSwitchCode(final String[] fundUnSwitchCode) {
        this.fundUnSwitchCode = fundUnSwitchCode;
    }


    /**
     * @return the productName
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(final String productName) {
        this.productName = productName;
    }

    /**
     * @return the productShortName
     */
    public String getProductShortName() {
        return this.productShortName;
    }

    /**
     * @param productShortName
     *            the productShortName to set
     */
    public void setProductShortName(final String productShortName) {
        this.productShortName = productShortName;
    }

    /**
     * @return the productCcy
     */
    public String getProductCcy() {
        return this.productCcy;
    }

    /**
     * @param productCcy
     *            the productCcy to set
     */
    public void setProductCcy(final String productCcy) {
        this.productCcy = productCcy;
    }

    /**
     * @return the market
     */
    public String getMarket() {
        return this.market;
    }

    /**
     * @param market
     *            the market to set
     */
    public void setMarket(final String market) {
        this.market = market;
    }

    /**
     * @return the exchange
     */
    public String getExchange() {
        return this.exchange;
    }

    /**
     * @param exchange
     *            the exchange to set
     */
    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    /**
     * @return the fundHouseCde
     */
    public String getFundHouseCde() {
        return this.fundHouseCde;
    }

    /**
     * @param fundHouseCde
     *            the fundHouseCde to set
     */
    public void setFundHouseCde(final String fundHouseCde) {
        this.fundHouseCde = fundHouseCde;
    }

    /**
     * @return the bondIssuer
     */
    public String getBondIssuer() {
        return this.bondIssuer;
    }

    /**
     * @param bondIssuer
     *            the bondIssuer to set
     */
    public void setBondIssuer(final String bondIssuer) {
        this.bondIssuer = bondIssuer;
    }

    /**
     * @return the allowSellMipProdInd
     */
    public String getAllowSellMipProdInd() {
        return this.allowSellMipProdInd;
    }

    /**
     * @param allowSellMipProdInd
     *            the allowSellMipProdInd to set
     */
    public void setAllowSellMipProdInd(final String allowSellMipProdInd) {
        this.allowSellMipProdInd = allowSellMipProdInd;
    }

    /**
     * @return the allowTradeProdInd
     */
    public String getAllowTradeProdInd() {
        return this.allowTradeProdInd;
    }


    /**
     * @param allowTradeProdInd
     *            the allowTradeProdInd to set
     */
    public void setAllowTradeProdInd(final String allowTradeProdInd) {
        this.allowTradeProdInd = allowTradeProdInd;
    }

    /**
     * @return the prodTaxFreeWrapActStaCde
     */
    public String getProdTaxFreeWrapActStaCde() {
        return this.prodTaxFreeWrapActStaCde;
    }


    /**
     * @param prodTaxFreeWrapActStaCde
     *            the prodTaxFreeWrapActStaCde to set
     */
    public void setProdTaxFreeWrapActStaCde(final String prodTaxFreeWrapActStaCde) {
        this.prodTaxFreeWrapActStaCde = prodTaxFreeWrapActStaCde;
    }


    /**
     * @return the allowSwInProdInd
     */
    public String getAllowSwInProdInd() {
        return this.allowSwInProdInd;
    }


    /**
     * @param allowSwInProdInd
     *            the allowSwInProdInd to set
     */
    public void setAllowSwInProdInd(final String allowSwInProdInd) {
        this.allowSwInProdInd = allowSwInProdInd;
    }


    /**
     * @return the allowSwOutProdInd
     */
    public String getAllowSwOutProdInd() {
        return this.allowSwOutProdInd;
    }


    /**
     * @param allowSwOutProdInd
     *            the allowSwOutProdInd to set
     */
    public void setAllowSwOutProdInd(final String allowSwOutProdInd) {
        this.allowSwOutProdInd = allowSwOutProdInd;
    }


    /**
     * @return the swithableGroups
     */
    public String[] getSwithableGroups() {
        return this.swithableGroups;
    }

    /**
     * @param swithableGroups
     *            the swithableGroups to set
     */
    public void setSwithableGroups(final String[] swithableGroups) {
        this.swithableGroups = swithableGroups;
    }

    /**
     * @return the assetCountries
     */
    public String[] getAssetCountries() {
        return this.assetCountries;
    }

    /**
     * @param assetCountries
     *            the assetCountries to set
     */
    public void setAssetCountries(final String[] assetCountries) {
        this.assetCountries = assetCountries;
    }

    /**
     * @return the assetSectors
     */
    public String[] getAssetSectors() {
        return this.assetSectors;
    }

    /**
     * @param assetSectors
     *            the assetSectors to set
     */
    public void setAssetSectors(final String[] assetSectors) {
        this.assetSectors = assetSectors;
    }

    /**
     * @return the parentAssetClasses
     */
    public String[] getParentAssetClasses() {
        return this.parentAssetClasses;
    }

    /**
     * @param parentAssetClasses
     *            the parentAssetClasses to set
     */
    public void setParentAssetClasses(final String[] parentAssetClasses) {
        this.parentAssetClasses = parentAssetClasses;
    }

    /**
     * @return the channelRestrictList
     */
    public String[] getChannelRestrictList() {
        return this.channelRestrictList;
    }

    /**
     * @param channelRestrictList
     *            the channelRestrictList to set
     */
    public void setChannelRestrictList(final String[] channelRestrictList) {
        this.channelRestrictList = channelRestrictList;
    }

    /**
     * @return the chanlCdeList
     */
    public String[] getChanlCdeList() {
        return this.chanlCdeList;
    }


    /**
     * @param chanlCdeList
     *            the chanlCdeList to set
     */
    public void setChanlCdeList(final String[] chanlCdeList) {
        this.chanlCdeList = chanlCdeList;
    }


    /**
     * @return the symbol
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * @param symbol
     *            the symbol to set
     */
    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return this.productCode;
    }

    /**
     * @param productCode
     *            the productCode to set
     */
    public void setProductCode(final String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the riskLvlCde
     */
    public String getRiskLvlCde() {
        return this.riskLvlCde;
    }

    /**
     * @param riskLvlCde
     *            the riskLvlCde to set
     */
    public void setRiskLvlCde(final String riskLvlCde) {
        this.riskLvlCde = riskLvlCde;
    }


    public String getProdStatCde() {
        return this.prodStatCde;
    }


    public void setProdStatCde(final String prodStatCde) {
        this.prodStatCde = prodStatCde;
    }


    /**
     * @return the restrOnlScribInd
     */
    public String getRestrOnlScribInd() {
        return this.restrOnlScribInd;
    }


    /**
     * @param restrOnlScribInd
     *            the restrOnlScribInd to set
     */
    public void setRestrOnlScribInd(final String restrOnlScribInd) {
        this.restrOnlScribInd = restrOnlScribInd;
    }


    /**
     * @return the piFundInd
     */
    public String getPiFundInd() {
        return this.piFundInd;
    }


    /**
     * @param piFundInd
     *            the piFundInd to set
     */
    public void setPiFundInd(final String piFundInd) {
        this.piFundInd = piFundInd;
    }


    /**
     * @return the deAuthFundInd
     */
    public String getDeAuthFundInd() {
        return this.deAuthFundInd;
    }


    /**
     * @param deAuthFundInd
     *            the deAuthFundInd to set
     */
    public void setDeAuthFundInd(final String deAuthFundInd) {
        this.deAuthFundInd = deAuthFundInd;
    }

    public String getProductSecondName() {
        return productSecondName;
    }

    public void setProductSecondName(String productSecondName) {
        this.productSecondName = productSecondName;
    }
}
