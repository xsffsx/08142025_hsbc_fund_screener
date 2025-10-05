/*
 * COPYRIGHT. hhhh HOLDINGS PLC 2011. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the prior
 * written consent of hhhh Holdings plc.
 */
package com.hhhh.group.secwealth.mktdata.common.beans;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;

/**
 * The Class SearchableObject.
 */
public class SearchableObject {

    /** The id. */
    private String id;

    /**
     * *************** The following fields can be searchable ****************.
     */

    protected String[] productName_analyzed;

    /** The product name. */
    protected String productName;

    /** The product short name_analyzed. */
    protected String[] productShortName_analyzed;

    /** The product short name. */
    protected String productShortName;

    /** The sswitchableGroup_analyzed. */
    protected String[] switchableGroup_analyzed;

    /** The symbol_analyzed. */
    protected String symbol_analyzed;

    /** The symbol. */
    protected String symbol;

    // Ric or TR code as a key
    /** The key_analyzed. */
    protected String key_analyzed;

    /** The key. */
    protected String key;

    /** The product type_analyzed. */
    protected String productType_analyzed;

    /** The product type. */
    protected String productType;

    /** The country tradable code. */
    protected String countryTradableCode;

    /** The market_analyzed. */
    protected String countryTradableCode_analyzed;

    /** The product sub type. */
    protected String productSubType;

    /** The product sub type_analyzed. */
    protected String productSubType_analyzed;

    /** The Fund House Code. */
    protected String fundHouseCde;

    /** The Fund House Code_analyzed. */
    protected String fundHouseCde_analyzed;

    /** The Issuer bond name. */
    protected String isrBndNme;

    /** The Bond issuer analyzed. */
    protected String isrBndNme_analyzed;

    /** The allowSellMipProdInd indicate. */
    protected String allowSellMipProdInd;

    /** The allowSellMipProdInd analyzed. */
    protected String allowSellMipProdInd_analyzed;

    /** The restrOnlScribInd indicate. */
    protected String restrOnlScribInd;

    /** The restrOnlScribInd analyzed. */
    protected String restrOnlScribInd_analyzed;

    /** The allowTradeProdInd indicate. */
    protected String allowTradeProdInd;

    /** The allowTradeProdInd analyzed. */
    protected String allowTradeProdInd_analyzed;

    /** The allowSwInProdInd indicate. */
    protected String allowSwInProdInd;

    /** The allowSwInProdInd analyzed. */
    protected String allowSwInProdInd_analyzed;

    /** The allowSwOutProdInd indicate. */
    protected String allowSwOutProdInd;

    /** The allowSwOutProdInd analyzed. */
    protected String allowSwOutProdInd_analyzed;

    /** The product code. */
    protected String productCode;

    /** The product code_analyzed. */
    protected String productCode_analyzed;

    /** The product tax free wrapper account status code. */
    protected String prodTaxFreeWrapActStaCde;

    /** The The product tax free wrapper account status code_analyzed. */
    protected String prodTaxFreeWrapActStaCde_analyzed;

    /**
     * ******************* Above fields can be searchable *******************.
     */

    /** The country tradable code weight. */
    protected Integer countryTradableCodeWeight;

    /** The product type weight. */
    protected Double productTypeWeight;

    /** The market. */
    protected String market;

    protected List<ProdAltNumSeg> prodAltNumSeg;

    // Just for Function Test start
    protected List<ProdAltNumSeg> prodAltNumSegs;

    public List<ProdAltNumSeg> getProdAltNumSeg() {
        if (null != this.prodAltNumSeg) {
            return this.prodAltNumSeg;
        }
        return this.prodAltNumSegs;
    }
    // Just for Function Test end

    protected String[] fundUnswithSeg;

    protected String[] resChannelCde;

    /** The resChannelCde_analyzed. */
    protected String[] resChannelCde_analyzed;

    protected String[] chanlCde;

    /** The chanlCde_analyzed. */
    protected String[] chanlCde_analyzed;

    /** The allow buy. */
    protected String allowBuy;

    /** The allow buy analyzed. */
    protected String allowBuy_analyzed;

    /** The allow sell. */
    protected String allowSell;

    /** The allow buy. */
    protected List<String> chanlAllowBuy;

    /** The allow sell. */
    protected List<String> chanlAllowSell;

    /** The exchange. */
    protected String exchange;

    /** The product ccy. */
    protected String productCcy;

    /** The asset country. */
    protected String[] assetCountry;

    /** The asset sector. */
    protected String[] assetSector;

    /** The parent asset class. */
    protected String[] parentAssetClass;

    /** The Product Shore Location Code. */
    protected String prodShoreLocCde;

    /** The Risk Rating. */
    protected String riskLvlCde;

    /** The product type_analyzed. */
    protected String prodStatCde_analyzed;

    /** The product type. */
    protected String prodStatCde;

    /** The product type_analyzed. */
    protected String piFundInd_analyzed;

    /** The product type. */
    protected String piFundInd;

    /** The product type_analyzed. */
    protected String deAuthFundInd_analyzed;

    /** The product type. */
    protected String deAuthFundInd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getProductName_analyzed() {
		return productName_analyzed;
	}

	public void setProductName_analyzed(String[] productName_analyzed) {
		this.productName_analyzed = productName_analyzed;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String[] getProductShortName_analyzed() {
		return productShortName_analyzed;
	}

	public void setProductShortName_analyzed(String[] productShortName_analyzed) {
		this.productShortName_analyzed = productShortName_analyzed;
	}

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	public String[] getSwitchableGroup_analyzed() {
		return switchableGroup_analyzed;
	}

	public void setSwitchableGroup_analyzed(String[] switchableGroup_analyzed) {
		this.switchableGroup_analyzed = switchableGroup_analyzed;
	}

	public String getSymbol_analyzed() {
		return symbol_analyzed;
	}

	public void setSymbol_analyzed(String symbol_analyzed) {
		this.symbol_analyzed = symbol_analyzed;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getKey_analyzed() {
		return key_analyzed;
	}

	public void setKey_analyzed(String key_analyzed) {
		this.key_analyzed = key_analyzed;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getProductType_analyzed() {
		return productType_analyzed;
	}

	public void setProductType_analyzed(String productType_analyzed) {
		this.productType_analyzed = productType_analyzed;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCountryTradableCode() {
		return countryTradableCode;
	}

	public void setCountryTradableCode(String countryTradableCode) {
		this.countryTradableCode = countryTradableCode;
	}

	public String getCountryTradableCode_analyzed() {
		return countryTradableCode_analyzed;
	}

	public void setCountryTradableCode_analyzed(String countryTradableCode_analyzed) {
		this.countryTradableCode_analyzed = countryTradableCode_analyzed;
	}

	public String getProductSubType() {
		return productSubType;
	}

	public void setProductSubType(String productSubType) {
		this.productSubType = productSubType;
	}

	public String getProductSubType_analyzed() {
		return productSubType_analyzed;
	}

	public void setProductSubType_analyzed(String productSubType_analyzed) {
		this.productSubType_analyzed = productSubType_analyzed;
	}

	public String getFundHouseCde() {
		return fundHouseCde;
	}

	public void setFundHouseCde(String fundHouseCde) {
		this.fundHouseCde = fundHouseCde;
	}

	public String getFundHouseCde_analyzed() {
		return fundHouseCde_analyzed;
	}

	public void setFundHouseCde_analyzed(String fundHouseCde_analyzed) {
		this.fundHouseCde_analyzed = fundHouseCde_analyzed;
	}

	public String getIsrBndNme() {
		return isrBndNme;
	}

	public void setIsrBndNme(String isrBndNme) {
		this.isrBndNme = isrBndNme;
	}

	public String getIsrBndNme_analyzed() {
		return isrBndNme_analyzed;
	}

	public void setIsrBndNme_analyzed(String isrBndNme_analyzed) {
		this.isrBndNme_analyzed = isrBndNme_analyzed;
	}

	public String getAllowSellMipProdInd() {
		return allowSellMipProdInd;
	}

	public void setAllowSellMipProdInd(String allowSellMipProdInd) {
		this.allowSellMipProdInd = allowSellMipProdInd;
	}

	public String getAllowSellMipProdInd_analyzed() {
		return allowSellMipProdInd_analyzed;
	}

	public void setAllowSellMipProdInd_analyzed(String allowSellMipProdInd_analyzed) {
		this.allowSellMipProdInd_analyzed = allowSellMipProdInd_analyzed;
	}

	public String getRestrOnlScribInd() {
		return restrOnlScribInd;
	}

	public void setRestrOnlScribInd(String restrOnlScribInd) {
		this.restrOnlScribInd = restrOnlScribInd;
	}

	public String getRestrOnlScribInd_analyzed() {
		return restrOnlScribInd_analyzed;
	}

	public void setRestrOnlScribInd_analyzed(String restrOnlScribInd_analyzed) {
		this.restrOnlScribInd_analyzed = restrOnlScribInd_analyzed;
	}

	public String getAllowTradeProdInd() {
		return allowTradeProdInd;
	}

	public void setAllowTradeProdInd(String allowTradeProdInd) {
		this.allowTradeProdInd = allowTradeProdInd;
	}

	public String getAllowTradeProdInd_analyzed() {
		return allowTradeProdInd_analyzed;
	}

	public void setAllowTradeProdInd_analyzed(String allowTradeProdInd_analyzed) {
		this.allowTradeProdInd_analyzed = allowTradeProdInd_analyzed;
	}

	public String getAllowSwInProdInd() {
		return allowSwInProdInd;
	}

	public void setAllowSwInProdInd(String allowSwInProdInd) {
		this.allowSwInProdInd = allowSwInProdInd;
	}

	public String getAllowSwInProdInd_analyzed() {
		return allowSwInProdInd_analyzed;
	}

	public void setAllowSwInProdInd_analyzed(String allowSwInProdInd_analyzed) {
		this.allowSwInProdInd_analyzed = allowSwInProdInd_analyzed;
	}

	public String getAllowSwOutProdInd() {
		return allowSwOutProdInd;
	}

	public void setAllowSwOutProdInd(String allowSwOutProdInd) {
		this.allowSwOutProdInd = allowSwOutProdInd;
	}

	public String getAllowSwOutProdInd_analyzed() {
		return allowSwOutProdInd_analyzed;
	}

	public void setAllowSwOutProdInd_analyzed(String allowSwOutProdInd_analyzed) {
		this.allowSwOutProdInd_analyzed = allowSwOutProdInd_analyzed;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCode_analyzed() {
		return productCode_analyzed;
	}

	public void setProductCode_analyzed(String productCode_analyzed) {
		this.productCode_analyzed = productCode_analyzed;
	}

	public String getProdTaxFreeWrapActStaCde() {
		return prodTaxFreeWrapActStaCde;
	}

	public void setProdTaxFreeWrapActStaCde(String prodTaxFreeWrapActStaCde) {
		this.prodTaxFreeWrapActStaCde = prodTaxFreeWrapActStaCde;
	}

	public String getProdTaxFreeWrapActStaCde_analyzed() {
		return prodTaxFreeWrapActStaCde_analyzed;
	}

	public void setProdTaxFreeWrapActStaCde_analyzed(String prodTaxFreeWrapActStaCde_analyzed) {
		this.prodTaxFreeWrapActStaCde_analyzed = prodTaxFreeWrapActStaCde_analyzed;
	}

	public Integer getCountryTradableCodeWeight() {
		return countryTradableCodeWeight;
	}

	public void setCountryTradableCodeWeight(Integer countryTradableCodeWeight) {
		this.countryTradableCodeWeight = countryTradableCodeWeight;
	}

	public Double getProductTypeWeight() {
		return productTypeWeight;
	}

	public void setProductTypeWeight(Double productTypeWeight) {
		this.productTypeWeight = productTypeWeight;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public List<ProdAltNumSeg> getProdAltNumSegs() {
		return prodAltNumSegs;
	}

	public void setProdAltNumSegs(List<ProdAltNumSeg> prodAltNumSegs) {
		this.prodAltNumSegs = prodAltNumSegs;
	}

	public String[] getFundUnswithSeg() {
		return fundUnswithSeg;
	}

	public void setFundUnswithSeg(String[] fundUnswithSeg) {
		this.fundUnswithSeg = fundUnswithSeg;
	}

	public String[] getResChannelCde() {
		return resChannelCde;
	}

	public void setResChannelCde(String[] resChannelCde) {
		this.resChannelCde = resChannelCde;
	}

	public String[] getResChannelCde_analyzed() {
		return resChannelCde_analyzed;
	}

	public void setResChannelCde_analyzed(String[] resChannelCde_analyzed) {
		this.resChannelCde_analyzed = resChannelCde_analyzed;
	}

	public String[] getChanlCde() {
		return chanlCde;
	}

	public void setChanlCde(String[] chanlCde) {
		this.chanlCde = chanlCde;
	}

	public String[] getChanlCde_analyzed() {
		return chanlCde_analyzed;
	}

	public void setChanlCde_analyzed(String[] chanlCde_analyzed) {
		this.chanlCde_analyzed = chanlCde_analyzed;
	}

	public String getAllowBuy() {
		return allowBuy;
	}

	public void setAllowBuy(String allowBuy) {
		this.allowBuy = allowBuy;
	}

	public String getAllowBuy_analyzed() {
		return allowBuy_analyzed;
	}

	public void setAllowBuy_analyzed(String allowBuy_analyzed) {
		this.allowBuy_analyzed = allowBuy_analyzed;
	}

	public String getAllowSell() {
		return allowSell;
	}

	public void setAllowSell(String allowSell) {
		this.allowSell = allowSell;
	}

	public List<String> getChanlAllowBuy() {
		return chanlAllowBuy;
	}

	public void setChanlAllowBuy(List<String> chanlAllowBuy) {
		this.chanlAllowBuy = chanlAllowBuy;
	}

	public List<String> getChanlAllowSell() {
		return chanlAllowSell;
	}

	public void setChanlAllowSell(List<String> chanlAllowSell) {
		this.chanlAllowSell = chanlAllowSell;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getProductCcy() {
		return productCcy;
	}

	public void setProductCcy(String productCcy) {
		this.productCcy = productCcy;
	}

	public String[] getAssetCountry() {
		return assetCountry;
	}

	public void setAssetCountry(String[] assetCountry) {
		this.assetCountry = assetCountry;
	}

	public String[] getAssetSector() {
		return assetSector;
	}

	public void setAssetSector(String[] assetSector) {
		this.assetSector = assetSector;
	}

	public String[] getParentAssetClass() {
		return parentAssetClass;
	}

	public void setParentAssetClass(String[] parentAssetClass) {
		this.parentAssetClass = parentAssetClass;
	}

	public String getProdShoreLocCde() {
		return prodShoreLocCde;
	}

	public void setProdShoreLocCde(String prodShoreLocCde) {
		this.prodShoreLocCde = prodShoreLocCde;
	}

	public String getRiskLvlCde() {
		return riskLvlCde;
	}

	public void setRiskLvlCde(String riskLvlCde) {
		this.riskLvlCde = riskLvlCde;
	}

	public String getProdStatCde_analyzed() {
		return prodStatCde_analyzed;
	}

	public void setProdStatCde_analyzed(String prodStatCde_analyzed) {
		this.prodStatCde_analyzed = prodStatCde_analyzed;
	}

	public String getProdStatCde() {
		return prodStatCde;
	}

	public void setProdStatCde(String prodStatCde) {
		this.prodStatCde = prodStatCde;
	}

	public String getPiFundInd_analyzed() {
		return piFundInd_analyzed;
	}

	public void setPiFundInd_analyzed(String piFundInd_analyzed) {
		this.piFundInd_analyzed = piFundInd_analyzed;
	}

	public String getPiFundInd() {
		return piFundInd;
	}

	public void setPiFundInd(String piFundInd) {
		this.piFundInd = piFundInd;
	}

	public String getDeAuthFundInd_analyzed() {
		return deAuthFundInd_analyzed;
	}

	public void setDeAuthFundInd_analyzed(String deAuthFundInd_analyzed) {
		this.deAuthFundInd_analyzed = deAuthFundInd_analyzed;
	}

	public String getDeAuthFundInd() {
		return deAuthFundInd;
	}

	public void setDeAuthFundInd(String deAuthFundInd) {
		this.deAuthFundInd = deAuthFundInd;
	}

	public void setProdAltNumSeg(List<ProdAltNumSeg> prodAltNumSeg) {
		this.prodAltNumSeg = prodAltNumSeg;
	}

}