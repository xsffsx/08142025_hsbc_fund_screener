/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.beans;

import java.util.Arrays;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;

/**
 * The Class ResponseInfo.
 */
public class ResponseInfo {

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

    /** The exchange. */
    private String exchange;

    /** The asset country. */
    private String[] assetCountrys;

    /** The asset sector. */
    private String[] assetSectors;

    /** The parent asset class. */
    private String[] parentAssetClasses;

    /** The prod decriptor. */
    private String prodDecriptor;

    /** The text. */
    private String text;

    /** The symbol. */
    private String symbol;

    /** The market. */
    private String market;

    /** The product code. */
    private String productCode;

    /** The fund house cde. */
    private String fundHouseCde;

    /** Bond Issuer. */
    private String isrBndNme;

    /** The swithable group. */
    private String[] swithableGroups;

    private String[] fundUnswCdes;

    private String[] channelRestrictGroup;

    private String[] chanlCdeList;

    /** The allow sell mip prod ind. */
    private String allowSellMipProdInd;

    /** The allow Trade prod ind. */
    private String allowTradeProdInd;

    /** The prod tax free wrap act status code. */
    private String prodTaxFreeWrapActStaCde;

    /** The allow SwitchIn prod ind. */
    private String allowSwInProdInd;

    /** The allow SwitvhOut prod ind. */
    private String allowSwOutProdInd;

    /** The riskLvlCde. */
    private String riskLvlCde;

    private String prodStatCde;

    /** The restrict online subscription indicator. */
    private String restrOnlScribInd;

    private String piFundInd;

    private String deAuthFundInd;


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResponseInfo [prodAltNumSegs=");
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
        builder.append(", productCcy=");
        builder.append(this.productCcy);
        builder.append(", fundUnswCdes=");
        builder.append(Arrays.toString(this.fundUnswCdes));
        builder.append(", channelRestrictGroup=");
        builder.append(Arrays.toString(this.channelRestrictGroup));
        builder.append(", chanlCdeList=");
        builder.append(Arrays.toString(this.chanlCdeList));
        builder.append(", exchange=");
        builder.append(this.exchange);
        builder.append(", assetCountrys=");
        builder.append(Arrays.toString(this.assetCountrys));
        builder.append(", assetSectors=");
        builder.append(Arrays.toString(this.assetSectors));
        builder.append(", parentAssetClasses=");
        builder.append(Arrays.toString(this.parentAssetClasses));
        builder.append(", prodDecriptor=");
        builder.append(this.prodDecriptor);
        builder.append(", text=");
        builder.append(this.text);
        builder.append(", symbol=");
        builder.append(this.symbol);
        builder.append(", market=");
        builder.append(this.market);
        builder.append(", productCode=");
        builder.append(this.productCode);
        builder.append(", fundHouseCde=");
        builder.append(this.fundHouseCde);
        builder.append(", isrBndNme=");
        builder.append(this.isrBndNme);
        builder.append(", swithableGroups=");
        builder.append(Arrays.toString(this.swithableGroups));
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
     * Gets the product type.
     * 
     * @return the product type
     */
    public String getProductType() {
        return this.productType;
    }

    /**
     * Sets the product type.
     * 
     * @param productType
     *            the new product type
     */
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    /**
     * Gets the country tradable code.
     * 
     * @return the country tradable code
     */
    public String getCountryTradableCode() {
        return this.countryTradableCode;
    }

    /**
     * @return the fundUnswCdes
     */
    public String[] getFundUnswCdes() {
        return this.fundUnswCdes;
    }

    /**
     * @param fundUnswCdes
     *            the fundUnswCdes to set
     */
    public void setFundUnswCdes(final String[] fundUnswCdes) {
        this.fundUnswCdes = fundUnswCdes;
    }

    /**
     * Sets the country tradable code.
     * 
     * @param countryTradableCode
     *            the new country tradable code
     */
    public void setCountryTradableCode(final String countryTradableCode) {
        this.countryTradableCode = countryTradableCode;
    }

    /**
     * Gets the allow buy.
     * 
     * @return the allow buy
     */
    public String getAllowBuy() {
        return this.allowBuy;
    }

    /**
     * Sets the allow buy.
     * 
     * @param allowBuy
     *            the new allow buy
     */
    public void setAllowBuy(final String allowBuy) {
        this.allowBuy = allowBuy;
    }

    /**
     * Gets the allow sell.
     * 
     * @return the allow sell
     */
    public String getAllowSell() {
        return this.allowSell;
    }

    /**
     * Sets the allow sell.
     * 
     * @param allowSell
     *            the new allow sell
     */
    public void setAllowSell(final String allowSell) {
        this.allowSell = allowSell;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * Sets the product name.
     * 
     * @param productName
     *            the new product name
     */
    public void setProductName(final String productName) {
        this.productName = productName;
    }

    /**
     * Gets the product short name.
     * 
     * @return the product short name
     */
    public String getProductShortName() {
        return this.productShortName;
    }

    /**
     * Sets the product short name.
     * 
     * @param productShortName
     *            the new product short name
     */
    public void setProductShortName(final String productShortName) {
        this.productShortName = productShortName;
    }

    /**
     * Gets the exchange.
     * 
     * @return the exchange
     */
    public String getExchange() {
        return this.exchange;
    }

    /**
     * Sets the exchange.
     * 
     * @param exchange
     *            the new exchange
     */
    public void setExchange(final String exchange) {
        this.exchange = exchange;
    }

    /**
     * Gets the asset country.
     * 
     * @return the asset country
     */
    public String[] getAssetCountrys() {
        return this.assetCountrys;
    }

    /**
     * Sets the asset country.
     * 
     * @param assetCountry
     *            the new asset country
     */
    public void setAssetCountrys(final String[] assetCountrys) {
        this.assetCountrys = assetCountrys;
    }

    /**
     * Gets the asset sector.
     * 
     * @return the asset sector
     */
    public String[] getAssetSectors() {
        return this.assetSectors;
    }

    /**
     * Sets the asset sector.
     * 
     * @param assetSector
     *            the new asset sector
     */
    public void setAssetSectors(final String[] assetSectors) {
        this.assetSectors = assetSectors;
    }

    /**
     * Gets the prod alt num seg.
     * 
     * @return the prod alt num seg
     */
    public List<ProdAltNumSeg> getProdAltNumSegs() {
        return this.prodAltNumSegs;
    }

    /**
     * Sets the prod alt num seg.
     * 
     * @param prodAltNumSeg
     *            the new prod alt num seg
     */
    public void setProdAltNumSegs(final List<ProdAltNumSeg> prodAltNumSegs) {
        this.prodAltNumSegs = prodAltNumSegs;
    }

    /**
     * Gets the prod decriptor.
     * 
     * @return the prod decriptor
     */
    public String getProdDecriptor() {
        return this.prodDecriptor;
    }

    /**
     * Sets the prod decriptor.
     * 
     * @param prodDecriptor
     *            the new prod decriptor
     */
    public void setProdDecriptor(final String prodDecriptor) {
        this.prodDecriptor = prodDecriptor;
    }

    /**
     * Gets the text.
     * 
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text.
     * 
     * @param text
     *            the new text
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Gets the symbol.
     * 
     * @return the symbol
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Sets the symbol.
     * 
     * @param symbol
     *            the new symbol
     */
    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the product sub type.
     * 
     * @return the product sub type
     */
    public String getProductSubType() {
        return this.productSubType;
    }

    /**
     * Sets the product sub type.
     * 
     * @param productSubType
     *            the new product sub type
     */
    public void setProductSubType(final String productSubType) {
        this.productSubType = productSubType;
    }

    /**
     * Gets the product ccy.
     * 
     * @return the product ccy
     */
    public String getProductCcy() {
        return this.productCcy;
    }

    /**
     * Sets the product ccy.
     * 
     * @param productCcy
     *            the new product ccy
     */
    public void setProductCcy(final String productCcy) {
        this.productCcy = productCcy;
    }

    /**
     * Gets the market.
     * 
     * @return the market
     */
    public String getMarket() {
        return this.market;
    }

    /**
     * Sets the market.
     * 
     * @param market
     *            the new market
     */
    public void setMarket(final String market) {
        this.market = market;
    }

    /**
     * Gets the product code.
     * 
     * @return the product code
     */
    public String getProductCode() {
        return this.productCode;
    }

    /**
     * Sets the product code.
     * 
     * @param productCode
     *            the new product code
     */
    public void setProductCode(final String productCode) {
        this.productCode = productCode;
    }

    /**
     * Gets the parent asset class.
     * 
     * @return the parent asset class
     */
    public String[] getParentAssetClasses() {
        return this.parentAssetClasses;
    }

    /**
     * Sets the parent asset class.
     * 
     * @param parentAssetClass
     *            the new parent asset class
     */
    public void setParentAssetClasses(final String[] parentAssetClasses) {
        this.parentAssetClasses = parentAssetClasses;
    }

    /**
     * Gets the fund house cde.
     * 
     * @return the fundHouseCde
     */
    public String getFundHouseCde() {
        return this.fundHouseCde;
    }

    /**
     * Sets the fund house cde.
     * 
     * @param fundHouseCde
     *            the fundHouseCde to set
     */
    public void setFundHouseCde(final String fundHouseCde) {
        this.fundHouseCde = fundHouseCde;
    }

    /**
     * Gets the isr bnd nme.
     * 
     * @return the isrBndNme
     */
    public String getIsrBndNme() {
        return this.isrBndNme;
    }

    /**
     * Sets the isr bnd nme.
     * 
     * @param isrBndNme
     *            the isrBndNme to set
     */
    public void setIsrBndNme(final String isrBndNme) {
        this.isrBndNme = isrBndNme;
    }

    /**
     * @return the swithableGroup
     */
    public String[] getSwithableGroups() {
        return this.swithableGroups;
    }

    /**
     * @param swithableGroup
     *            the swithableGroup to set
     */
    public void setSwithableGroups(final String[] swithableGroups) {
        this.swithableGroups = swithableGroups;
    }

    /**
     * Gets the allow sell mip prod ind.
     * 
     * @return the allow sell mip prod ind
     */
    public String getAllowSellMipProdInd() {
        return this.allowSellMipProdInd;
    }

    /**
     * Sets the allow sell mip prod ind.
     * 
     * @param allowSellMipProdInd
     *            the new allow sell mip prod ind
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
     * @return the channelRestrictGroup
     */
    public String[] getChannelRestrictGroup() {
        return this.channelRestrictGroup;
    }

    /**
     * @param channelRestrictGroup
     *            the channelRestrictGroup to set
     */
    public void setChannelRestrictGroup(final String[] channelRestrictGroup) {
        this.channelRestrictGroup = channelRestrictGroup;
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