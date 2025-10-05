/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common;

import java.util.Arrays;
import java.util.List;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

/**
 * The Class SearchableObject.
 */
@Searchable
public class SearchableObject {

    /** The id. */
    @SearchableId(name = "id")
    private String id;

    /**
     * *************** The following fields can be searchable ****************.
     */

    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String[] productName_analyzed;

    /** The product name. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String productName;

    /** The product short name_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String[] productShortName_analyzed;

    /** The product short name. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String productShortName;

    /** The sswitchableGroup_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String[] switchableGroup_analyzed;

    /** The symbol_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String symbol_analyzed;

    /** The symbol. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String symbol;

    // Ric or TR code as a key
    /** The key_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String key_analyzed;

    /** The key. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String key;

    /** The product type_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String productType_analyzed;

    /** The product type. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String productType;

    /** The country tradable code. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String countryTradableCode;

    /** The market_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String countryTradableCode_analyzed;

    /** The product sub type. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String productSubType;

    /** The product sub type_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String productSubType_analyzed;

    /** The Fund House Code. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String fundHouseCde;

    /** The Fund House Code_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String fundHouseCde_analyzed;

    /** The Issuer bond name. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String isrBndNme;

    /** The Bond issuer analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String isrBndNme_analyzed;

    /** The allowSellMipProdInd indicate. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String allowSellMipProdInd;

    /** The allowSellMipProdInd analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String allowSellMipProdInd_analyzed;

    /** The restrOnlScribInd indicate. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String restrOnlScribInd;

    /** The restrOnlScribInd analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String restrOnlScribInd_analyzed;

    /** The allowTradeProdInd indicate. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String allowTradeProdInd;

    /** The allowTradeProdInd analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String allowTradeProdInd_analyzed;

    /** The allowSwInProdInd indicate. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String allowSwInProdInd;

    /** The allowSwInProdInd analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String allowSwInProdInd_analyzed;

    /** The allowSwOutProdInd indicate. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String allowSwOutProdInd;

    /** The allowSwOutProdInd analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String allowSwOutProdInd_analyzed;

    /** The product code. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String productCode;

    /** The product code_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String productCode_analyzed;

    /** The product tax free wrapper account status code. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String prodTaxFreeWrapActStaCde;

    /** The The product tax free wrapper account status code_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodTaxFreeWrapActStaCde_analyzed;

    /**
     * ******************* Above fields can be searchable *******************.
     */

    /** The country tradable code weight. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected Integer countryTradableCodeWeight;

    /** The product type weight. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected Double productTypeWeight;

    /** The market. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String market;

    @SearchableComponent
    protected List<ProdAltNumSeg> prodAltNumSeg;

    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String[] fundUnswithSeg;

    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String[] resChannelCde;

    /** The resChannelCde_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String[] resChannelCde_analyzed;

    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String[] chanlCde;

    /** The chanlCde_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String[] chanlCde_analyzed;

    /** The allow buy. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String allowBuy;

    /** The allow buy analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String allowBuy_analyzed;

    /** The allow sell. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String allowSell;

    /** The allow buy. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected List<String> chanlAllowBuy;

    /** The allow sell. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected List<String> chanlAllowSell;

    /** The exchange. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String exchange;

    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String exchange_analyzed;

    /** The product ccy. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String productCcy;

    /** The asset country. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String[] assetCountry;

    /** The asset sector. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String[] assetSector;

    /** The parent asset class. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String[] parentAssetClass;

    /** The Product Shore Location Code. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String prodShoreLocCde;

    /** The Risk Rating. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String riskLvlCde;

    /** The product type_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodStatCde_analyzed;

    /** The product type. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String prodStatCde;

    /** The product type_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String piFundInd_analyzed;

    /** The product type. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String piFundInd;

    /** The product type_analyzed. */
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String deAuthFundInd_analyzed;

    /** The product type. */
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String deAuthFundInd;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SearchableObject [id=");
        builder.append(this.id);
        builder.append(", productName_analyzed=");
        builder.append(Arrays.toString(this.productName_analyzed));
        builder.append(", productName=");
        builder.append(this.productName);
        builder.append(", productShortName_analyzed=");
        builder.append(Arrays.toString(this.productShortName_analyzed));
        builder.append(", productShortName=");
        builder.append(this.productShortName);
        builder.append(", switchableGroup_analyzed=");
        builder.append(Arrays.toString(this.switchableGroup_analyzed));
        builder.append(", symbol_analyzed=");
        builder.append(this.symbol_analyzed);
        builder.append(", symbol=");
        builder.append(this.symbol);
        builder.append(", key_analyzed=");
        builder.append(this.key_analyzed);
        builder.append(", key=");
        builder.append(this.key);
        builder.append(", productType_analyzed=");
        builder.append(this.productType_analyzed);
        builder.append(", productType=");
        builder.append(this.productType);
        builder.append(", countryTradableCode=");
        builder.append(this.countryTradableCode);
        builder.append(", countryTradableCode_analyzed=");
        builder.append(this.countryTradableCode_analyzed);
        builder.append(", productSubType=");
        builder.append(this.productSubType);
        builder.append(", productSubType_analyzed=");
        builder.append(this.productSubType_analyzed);
        builder.append(", fundHouseCde=");
        builder.append(this.fundHouseCde);
        builder.append(", fundHouseCde_analyzed=");
        builder.append(this.fundHouseCde_analyzed);
        builder.append(", isrBndNme=");
        builder.append(this.isrBndNme);
        builder.append(", isrBndNme_analyzed=");
        builder.append(this.isrBndNme_analyzed);
        builder.append(", allowSellMipProdInd=");
        builder.append(this.allowSellMipProdInd);
        builder.append(", allowSellMipProdInd_analyzed=");
        builder.append(this.allowSellMipProdInd_analyzed);
        builder.append(", restrOnlScribInd=");
        builder.append(this.restrOnlScribInd);
        builder.append(", restrOnlScribInd_analyzed=");
        builder.append(this.restrOnlScribInd_analyzed);
        builder.append(", allowTradeProdInd=");
        builder.append(this.allowTradeProdInd);
        builder.append(", allowTradeProdInd_analyzed=");
        builder.append(this.allowTradeProdInd_analyzed);
        builder.append(", allowSwInProdInd=");
        builder.append(this.allowSwInProdInd);
        builder.append(", allowSwInProdInd_analyzed=");
        builder.append(this.allowSwInProdInd_analyzed);
        builder.append(", allowSwOutProdInd=");
        builder.append(this.allowSwOutProdInd);
        builder.append(", allowSwOutProdInd_analyzed=");
        builder.append(this.allowSwOutProdInd_analyzed);
        builder.append(", productCode=");
        builder.append(this.productCode);
        builder.append(", productCode_analyzed=");
        builder.append(this.productCode_analyzed);
        builder.append(", market=");
        builder.append(this.market);
        builder.append(", prodAltNumSeg=");
        builder.append(this.prodAltNumSeg);
        builder.append(", fundUnswithSeg=");
        builder.append(Arrays.toString(this.fundUnswithSeg));
        builder.append(", resChannelCde=");
        builder.append(Arrays.toString(this.resChannelCde));
        builder.append(", resChannelCde_analyzed=");
        builder.append(Arrays.toString(this.resChannelCde_analyzed));
        builder.append(", chanlCde=");
        builder.append(Arrays.toString(this.chanlCde));
        builder.append(", chanlCde_analyzed=");
        builder.append(Arrays.toString(this.chanlCde_analyzed));
        builder.append(", allowBuy=");
        builder.append(this.allowBuy);
        builder.append(", allowBuy_analyzed=");
        builder.append(this.allowBuy_analyzed);
        builder.append(", allowSell=");
        builder.append(this.allowSell);
        builder.append(", chanlAllowBuy=");
        builder.append(this.chanlAllowBuy);
        builder.append(", chanlAllowSell=");
        builder.append(this.chanlAllowSell);
        builder.append(", exchange=");
        builder.append(this.exchange);
        builder.append(", exchange_analyzed=");
        builder.append(this.exchange_analyzed);
        builder.append(", productCcy=");
        builder.append(this.productCcy);
        builder.append(", assetCountry=");
        builder.append(Arrays.toString(this.assetCountry));
        builder.append(", assetSector=");
        builder.append(Arrays.toString(this.assetSector));
        builder.append(", parentAssetClass=");
        builder.append(Arrays.toString(this.parentAssetClass));
        builder.append(", prodShoreLocCde=");
        builder.append(this.prodShoreLocCde);
        builder.append(", riskLvlCde=");
        builder.append(this.riskLvlCde);
        builder.append(", prodTaxFreeWrapActStaCde=");
        builder.append(this.prodTaxFreeWrapActStaCde);
        builder.append(", prodTaxFreeWrapActStaCde_analyzed=");
        builder.append(this.prodTaxFreeWrapActStaCde_analyzed);
        builder.append(", countryTradableCodeWeight=");
        builder.append(this.countryTradableCodeWeight);
        builder.append(", productTypeWeight=");
        builder.append(this.productTypeWeight);
        builder.append(", prodStatCde=");
        builder.append(this.prodStatCde);
        builder.append(", prodStatCde_analyzed=");
        builder.append(this.prodStatCde_analyzed);
        builder.append(", piFundInd=");
        builder.append(this.piFundInd);
        builder.append(", piFundInd_analyzed=");
        builder.append(this.piFundInd_analyzed);
        builder.append(", deAuthFundInd=");
        builder.append(this.deAuthFundInd);
        builder.append(", deAuthFundInd_analyzed=");
        builder.append(this.deAuthFundInd_analyzed);
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
     * Gets the allow buy.
     *
     * @return the allow buy
     */
    public String getAllowBuy_analyzed() {
        return this.allowBuy_analyzed;
    }

    /**
     * Sets the allow buy.
     *
     * @param allowBuy
     *            the new allow buy
     */
    public void setAllowBuy_analyzed(final String allowBuy_analyzed) {
        this.allowBuy_analyzed = allowBuy_analyzed;
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
     * @return the chanlAllowBuy
     */
    public List<String> getChanlAllowBuy() {
        return this.chanlAllowBuy;
    }

    /**
     * @param chanlAllowBuy
     *            the chanlAllowBuy to set
     */
    public void setChanlAllowBuy(final List<String> chanlAllowBuy) {
        this.chanlAllowBuy = chanlAllowBuy;
    }

    /**
     * @return the chanlAllowSell
     */
    public List<String> getChanlAllowSell() {
        return this.chanlAllowSell;
    }

    /**
     * @param chanlAllowSell
     *            the chanlAllowSell to set
     */
    public void setChanlAllowSell(final List<String> chanlAllowSell) {
        this.chanlAllowSell = chanlAllowSell;
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
     * @return the exchange_analyzed
     */
    public String getExchange_analyzed() {
        return this.exchange_analyzed;
    }

    /**
     * @param exchange_analyzed
     *            the exchange_analyzed to set
     */
    public void setExchange_analyzed(final String exchange_analyzed) {
        this.exchange_analyzed = exchange_analyzed;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Gets the product type_analyzed.
     *
     * @return the product type_analyzed
     */
    public String getProductType_analyzed() {
        return this.productType_analyzed;
    }

    /**
     * Sets the product type_analyzed.
     *
     * @param productType_analyzed
     *            the new product type_analyzed
     */
    public void setProductType_analyzed(final String productType_analyzed) {
        this.productType_analyzed = productType_analyzed;
    }

    /**
     * Gets the asset country.
     *
     * @return the asset country
     */
    public String[] getAssetCountry() {
        return this.assetCountry;
    }

    /**
     * Sets the asset country.
     *
     * @param assetCountry
     *            the new asset country
     */
    public void setAssetCountry(final String[] assetCountry) {
        this.assetCountry = assetCountry;
    }

    /**
     * Gets the asset sector.
     *
     * @return the asset sector
     */
    public String[] getAssetSector() {
        return this.assetSector;
    }

    /**
     * Sets the asset sector.
     *
     * @param assetSector
     *            the new asset sector
     */
    public void setAssetSector(final String[] assetSector) {
        this.assetSector = assetSector;
    }

    /**
     * Gets the prod alt num seg.
     *
     * @return the prod alt num seg
     */
    public List<ProdAltNumSeg> getProdAltNumSeg() {
        return this.prodAltNumSeg;
    }

    /**
     * Sets the prod alt num seg.
     *
     * @param prodAltNumSeg
     *            the new prod alt num seg
     */
    public void setProdAltNumSeg(final List<ProdAltNumSeg> prodAltNumSeg) {
        this.prodAltNumSeg = prodAltNumSeg;
    }

    /**
     * @return the fundUnswithSeg
     */
    public String[] getFundUnswithSeg() {
        return this.fundUnswithSeg;
    }

    /**
     * @param fundUnswithSeg
     *            the fundUnswithSeg to set
     */
    public void setFundUnswithSeg(final String[] fundUnswithSeg) {
        this.fundUnswithSeg = fundUnswithSeg;
    }

    /**
     * Gets the product name_analyzed.
     *
     * @return the product name_analyzed
     */
    public String[] getProductName_analyzed() {
        return this.productName_analyzed;
    }

    /**
     * Sets the product name_analyzed.
     *
     * @param productName_analyzed
     *            the new product name_analyzed
     */
    public void setProductName_analyzed(final String[] productName_analyzed) {
        this.productName_analyzed = productName_analyzed;
    }

    /**
     * Gets the product short name_analyzed.
     *
     * @return the product short name_analyzed
     */
    public String[] getProductShortName_analyzed() {
        return this.productShortName_analyzed;
    }

    /**
     * Sets the product short name_analyzed.
     *
     * @param productShortName_analyzed
     *            the new product short name_analyzed
     */
    public void setProductShortName_analyzed(final String[] productShortName_analyzed) {
        this.productShortName_analyzed = productShortName_analyzed;
    }

    /**
     * Gets the symbol_analyzed.
     *
     * @return the symbol_analyzed
     */
    public String getSymbol_analyzed() {
        return this.symbol_analyzed;
    }

    /**
     * Sets the symbol_analyzed.
     *
     * @param symbol_analyzed
     *            the new symbol_analyzed
     */
    public void setSymbol_analyzed(final String symbol_analyzed) {
        this.symbol_analyzed = symbol_analyzed;
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
    public String[] getParentAssetClass() {
        return this.parentAssetClass;
    }

    /**
     * Sets the parent asset class.
     *
     * @param parentAssetClass
     *            the new parent asset class
     */
    public void setParentAssetClass(final String[] parentAssetClass) {
        this.parentAssetClass = parentAssetClass;
    }

    /**
     * Gets the key_analyzed.
     *
     * @return the key_analyzed
     */
    public String getKey_analyzed() {
        return this.key_analyzed;
    }

    /**
     * Sets the key_analyzed.
     *
     * @param key_analyzed
     *            the new key_analyzed
     */
    public void setKey_analyzed(final String key_analyzed) {
        this.key_analyzed = key_analyzed;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the new key
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * Gets the product sub type_analyzed.
     *
     * @return productSubType_analyzed
     */
    public String getProductSubType_analyzed() {
        return this.productSubType_analyzed;
    }

    /**
     * Sets the product sub type_analyzed.
     *
     * @param productSubType_analyzed
     *            The name to be set productSubType_analyzed
     */
    public void setProductSubType_analyzed(final String productSubType_analyzed) {
        this.productSubType_analyzed = productSubType_analyzed;
    }

    /**
     * Gets the prod shore loc cde.
     *
     * @return the prodShoreLocCde
     */
    public String getProdShoreLocCde() {
        return this.prodShoreLocCde;
    }

    /**
     * Sets the prod shore loc cde.
     *
     * @param prodShoreLocCde
     *            the prodShoreLocCde to set
     */
    public void setProdShoreLocCde(final String prodShoreLocCde) {
        this.prodShoreLocCde = prodShoreLocCde;
    }

    /**
     * @return the countryTradableCode_analyzed
     */
    public String getCountryTradableCode_analyzed() {
        return this.countryTradableCode_analyzed;
    }

    /**
     * @param countryTradableCode_analyzed
     *            the countryTradableCode_analyzed to set
     */
    public void setCountryTradableCode_analyzed(final String countryTradableCode_analyzed) {
        this.countryTradableCode_analyzed = countryTradableCode_analyzed;
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
     * Gets the fund house cde_analyzed.
     *
     * @return the fundHouseCde_analyzed
     */
    public String getFundHouseCde_analyzed() {
        return this.fundHouseCde_analyzed;
    }

    /**
     * Sets the fund house cde_analyzed.
     *
     * @param fundHouseCde_analyzed
     *            the fundHouseCde_analyzed to set
     */
    public void setFundHouseCde_analyzed(final String fundHouseCde_analyzed) {
        this.fundHouseCde_analyzed = fundHouseCde_analyzed;
    }

    /**
     * @return the isrBndNme
     */
    public String getIsrBndNme() {
        return this.isrBndNme;
    }

    /**
     * @param isrBndNme
     *            the isrBndNme to set
     */
    public void setIsrBndNme(final String isrBndNme) {
        this.isrBndNme = isrBndNme;
    }

    /**
     * @return the isrBndNme_analyzed
     */
    public String getIsrBndNme_analyzed() {
        return this.isrBndNme_analyzed;
    }

    /**
     * @param isrBndNme_analyzed
     *            the isrBndNme_analyzed to set
     */
    public void setIsrBndNme_analyzed(final String isrBndNme_analyzed) {
        this.isrBndNme_analyzed = isrBndNme_analyzed;
    }

    /**
     * @return the switchableGroup_analyzed
     */
    public String[] getSwitchableGroup_analyzed() {
        return this.switchableGroup_analyzed;
    }

    /**
     * @param switchableGroup_analyzed
     *            the switchableGroup_analyzed to set
     */
    public void setSwitchableGroup_analyzed(final String[] switchableGroup_analyzed) {
        this.switchableGroup_analyzed = switchableGroup_analyzed;
    }

    /**
     * @return the resChannelCde
     */
    public String[] getResChannelCde() {
        return this.resChannelCde;
    }

    /**
     * @param resChannelCde
     *            the resChannelCde to set
     */
    public void setResChannelCde(final String[] resChannelCde) {
        this.resChannelCde = resChannelCde;
    }

    /**
     * @return the resChannelCde_analyzed
     */
    public String[] getResChannelCde_analyzed() {
        return this.resChannelCde_analyzed;
    }

    /**
     * @param resChannelCde_analyzed
     *            the resChannelCde_analyzed to set
     */
    public void setResChannelCde_analyzed(final String[] resChannelCde_analyzed) {
        this.resChannelCde_analyzed = resChannelCde_analyzed;
    }

    /**
     * @return the chanlCde
     */
    public String[] getChanlCde() {
        return this.chanlCde;
    }

    /**
     * @param chanlCde
     *            the chanlCde to set
     */
    public void setChanlCde(final String[] chanlCde) {
        this.chanlCde = chanlCde;
    }

    /**
     * @return the chanlCde_analyzed
     */
    public String[] getChanlCde_analyzed() {
        return this.chanlCde_analyzed;
    }

    /**
     * @param chanlCde_analyzed
     *            the chanlCde_analyzed to set
     */
    public void setChanlCde_analyzed(final String[] chanlCde_analyzed) {
        this.chanlCde_analyzed = chanlCde_analyzed;
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
     * Gets the allow sell mip prod ind_analyzed.
     *
     * @return the allow sell mip prod ind_analyzed
     */
    public String getAllowSellMipProdInd_analyzed() {
        return this.allowSellMipProdInd_analyzed;
    }

    /**
     * Sets the allow sell mip prod ind_analyzed.
     *
     * @param allowSellMipProdInd_analyzed
     *            the new allow sell mip prod ind_analyzed
     */
    public void setAllowSellMipProdInd_analyzed(final String allowSellMipProdInd_analyzed) {
        this.allowSellMipProdInd_analyzed = allowSellMipProdInd_analyzed;
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
     * @return the restrOnlScribInd_analyzed
     */
    public String getRestrOnlScribInd_analyzed() {
        return this.restrOnlScribInd_analyzed;
    }

    /**
     * @param restrOnlScribInd_analyzed
     *            the restrOnlScribInd_analyzed to set
     */
    public void setRestrOnlScribInd_analyzed(final String restrOnlScribInd_analyzed) {
        this.restrOnlScribInd_analyzed = restrOnlScribInd_analyzed;
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
     * Gets the allow Trade prod ind_analyzed.
     *
     * @return the allow trade prod ind_analyzed
     */
    public String getAllowTradeProdInd_analyzed() {
        return this.allowTradeProdInd_analyzed;
    }

    /**
     * Sets the allow Trade prod ind_analyzed.
     *
     * @param allowTradeProdInd_analyzed
     *            the new allow Trade prod ind_analyzed
     */
    public void setAllowTradeProdInd_analyzed(final String allowTradeProdInd_analyzed) {
        this.allowTradeProdInd_analyzed = allowTradeProdInd_analyzed;
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
     * Gets the allow switchin prod ind_analyzed.
     *
     * @return the allow switchin prod ind_analyzed
     */
    public String getAllowSwInProdInd_analyzed() {
        return this.allowSwInProdInd_analyzed;
    }

    /**
     * Sets the allow Trade prod ind_analyzed.
     *
     * @param allowSwInProdInd_analyzed
     *            the new allow switchin prod ind_analyzed
     */
    public void setAllowSwInProdInd_analyzed(final String allowSwInProdInd_analyzed) {
        this.allowSwInProdInd_analyzed = allowSwInProdInd_analyzed;
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
     * Gets the allow switchout prod ind_analyzed.
     *
     * @return the allow switchout prod ind_analyzed
     */
    public String getAllowSwOutProdInd_analyzed() {
        return this.allowSwOutProdInd_analyzed;
    }

    /**
     * Sets the allow Trade prod ind_analyzed.
     *
     * @param allowSwOutProdInd_analyzed
     *            the new allow switchout prod ind_analyzed
     */
    public void setAllowSwOutProdInd_analyzed(final String allowSwOutProdInd_analyzed) {
        this.allowSwOutProdInd_analyzed = allowSwOutProdInd_analyzed;
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

    /**
     * Gets the product code_analyzed.
     *
     * @return the product code_analyzed
     */
    public String getProductCode_analyzed() {
        return this.productCode_analyzed;
    }

    /**
     * Sets the product code_analyzed.
     *
     * @param productCode_analyzed
     *            the new product code_analyzed
     */
    public void setProductCode_analyzed(final String productCode_analyzed) {
        this.productCode_analyzed = productCode_analyzed;
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
     * @return the prodTaxFreeWrapActStaCde_analyzed
     */
    public String getProdTaxFreeWrapActStaCde_analyzed() {
        return this.prodTaxFreeWrapActStaCde_analyzed;
    }

    /**
     * @param prodTaxFreeWrapActStaCde_analyzed
     *            the prodTaxFreeWrapActStaCde_analyzed to set
     */
    public void setProdTaxFreeWrapActStaCde_analyzed(final String prodTaxFreeWrapActStaCde_analyzed) {
        this.prodTaxFreeWrapActStaCde_analyzed = prodTaxFreeWrapActStaCde_analyzed;
    }

    /**
     * @return the countryTradableCodeWeight
     */
    public Integer getCountryTradableCodeWeight() {
        return this.countryTradableCodeWeight;
    }

    /**
     * @param countryTradableCodeWeight
     *            the countryTradableCodeWeight to set
     */
    public void setCountryTradableCodeWeight(final Integer countryTradableCodeWeight) {
        this.countryTradableCodeWeight = countryTradableCodeWeight;
    }

    /**
     * @return the productTypeWeight
     */
    public Double getProductTypeWeight() {
        return this.productTypeWeight;
    }

    /**
     * @param productTypeWeight
     *            the productTypeWeight to set
     */
    public void setProductTypeWeight(final Double productTypeWeight) {
        this.productTypeWeight = productTypeWeight;
    }

    public String getProdStatCde_analyzed() {
        return this.prodStatCde_analyzed;
    }

    public void setProdStatCde_analyzed(final String prodStatCde_analyzed) {
        this.prodStatCde_analyzed = prodStatCde_analyzed;
    }

    public String getProdStatCde() {
        return this.prodStatCde;
    }

    public void setProdStatCde(final String prodStatCde) {
        this.prodStatCde = prodStatCde;
    }

    /**
     * @return the piFundInd_analyzed
     */
    public String getPiFundInd_analyzed() {
        return this.piFundInd_analyzed;
    }

    /**
     * @param piFundInd_analyzed
     *            the piFundInd_analyzed to set
     */
    public void setPiFundInd_analyzed(final String piFundInd_analyzed) {
        this.piFundInd_analyzed = piFundInd_analyzed;
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
     * @return the deAuthFundInd_analyzed
     */
    public String getDeAuthFundInd_analyzed() {
        return this.deAuthFundInd_analyzed;
    }

    /**
     * @param deAuthFundInd_analyzed
     *            the deAuthFundInd_analyzed to set
     */
    public void setDeAuthFundInd_analyzed(final String deAuthFundInd_analyzed) {
        this.deAuthFundInd_analyzed = deAuthFundInd_analyzed;
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

}