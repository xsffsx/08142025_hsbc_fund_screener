/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.stock.ProdListSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.stock.ProdTradeCcySeg;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained
 * within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{}prodSubtpCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prodName&quot;/&gt;
 *         &lt;element ref=&quot;{}prodPllName&quot;/&gt;
 *         &lt;element ref=&quot;{}prodShrtName&quot;/&gt;
 *         &lt;element ref=&quot;{}prodShrtSllName&quot;/&gt;
 *         &lt;element ref=&quot;{}prodShrtPllName&quot;/&gt;
 *         &lt;element ref=&quot;{}prodDesc&quot;/&gt;
 *         &lt;element ref=&quot;{}asetClassCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prodStatCde&quot;/&gt;
 *         &lt;element ref=&quot;{}ccyProdCde&quot;/&gt;
 *         &lt;element ref=&quot;{}riskLvlCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prdProdCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prdProdNum&quot;/&gt;
 *         &lt;element ref=&quot;{}termRemainDayCnt&quot;/&gt;
 *         &lt;element ref=&quot;{}prodLnchDt&quot;/&gt;
 *         &lt;element ref=&quot;{}prodMturDt&quot;/&gt;
 *         &lt;element ref=&quot;{}mktInvstCde&quot;/&gt;
 *         &lt;element ref=&quot;{}allowBuyProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSellProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowBuyUtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowBuyAmtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSellUtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSellAmtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSellMipProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSellMipUtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSellMipAmtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSwInProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSwInUtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSwInAmtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}prodTaxFreeWrapActStaCde&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSwOutProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSwOutUtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowSwOutAmtProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}incmCharProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}cptlGurntProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}yieldEnhnProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}grwthCharProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}prtyProdSrchRsultNum&quot;/&gt;
 *         &lt;sequence minOccurs=&quot;0&quot;&gt;
 *           &lt;element ref=&quot;{}prdRtrnAvgNum&quot;/&gt;
 *           &lt;element ref=&quot;{}rtrnVoltlAvgPct&quot;/&gt;
 *         &lt;/sequence&gt;
 *         &lt;element ref=&quot;{}dmyProdSubtpRecInd&quot;/&gt;
 *         &lt;element ref=&quot;{}dispComProdSrchInd&quot;/&gt;
 *         &lt;element ref=&quot;{}divrNum&quot;/&gt;
 *         &lt;element ref=&quot;{}mrkToMktInd&quot;/&gt;
 *         &lt;element ref=&quot;{}ctryProdTrade1Cde&quot;/&gt;
 *         &lt;element ref=&quot;{}busStartTm&quot;/&gt;
 *         &lt;element ref=&quot;{}busEndTm&quot;/&gt;
 *         &lt;element ref=&quot;{}prodTopSellRankNum&quot;/&gt;
 *         &lt;element ref=&quot;{}prodTopPerfmRankNum&quot;/&gt;
 *         &lt;element ref=&quot;{}prodTopYieldRankNum&quot;/&gt;
 *         &lt;element ref=&quot;{}ccyInvstCde&quot;/&gt;
 *         &lt;element ref=&quot;{}qtyTypeCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prodLocCde&quot;/&gt;
 *         &lt;element ref=&quot;{}trdFirstDt&quot;/&gt;
 *         &lt;element ref=&quot;{}suptRcblCashProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptRcblScripProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}dcmlPlaceTradeUnitNum&quot;/&gt;
 *         &lt;element ref=&quot;{}pldgLimitAssocAcctInd&quot;/&gt;
 *         &lt;element ref=&quot;{}aumChrgProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}invstInitMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}prodTradeCcySeg&quot;/&gt;
 *         &lt;element ref=&quot;{}prodAsetUndlSeg&quot;/&gt;
 *         &lt;element ref=&quot;{}prodListSeg&quot;/&gt;
 *         &lt;element ref=&quot;{}resChanSeg&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"prodSubtpCde", "prodName", "prodPllName", "prodSllName", "prodShrtName", "prodShrtSllName",
    "switchableGroups", "prodShrtPllName", "prodDesc", "asetClassCde", "prodStatCde", "ccyProdCde", "riskLvlCde", "prdProdCde",
    "prdProdNum", "termRemainDayCnt", "prodLnchDt", "prodMturDt", "mktInvstCde", "allowBuyProdInd", "allowSellProdInd",
    "allowBuyUtProdInd", "allowBuyAmtProdInd", "allowSellUtProdInd", "allowSellAmtProdInd", "allowSellMipProdInd",
    "allowSellMipUtProdInd", "allowSellMipAmtProdInd", "allowSwInProdInd", "allowSwInUtProdInd", "allowSwInAmtProdInd",
    "prodTaxFreeWrapActStaCde", "allowSwOutProdInd", "allowSwOutUtProdInd", "allowSwOutAmtProdInd", "incmCharProdInd",
    "cptlGurntProdInd", "yieldEnhnProdInd", "grwthCharProdInd", "prtyProdSrchRsultNum", "prdRtrnAvgNum", "rtrnVoltlAvgPct",
    "dmyProdSubtpRecInd", "dispComProdSrchInd", "divrNum", "mrkToMktInd", "ctryProdTrade1Cde", "busStartTm", "busEndTm",
    "prodTopSellRankNum", "prodTopPerfmRankNum", "prodTopYieldRankNum", "ccyInvstCde", "qtyTypeCde", "prodLocCde", "trdFirstDt",
    "suptRcblCashProdInd", "suptRcblScripProdInd", "dcmlPlaceTradeUnitNum", "pldgLimitAssocAcctInd", "aumChrgProdInd",
    "invstInitMinAmt", "prodTradeCcySeg", "prodAsetUndlSeg", "prodListSeg", "prodUserDefExtSeg", "chanlAttrSeg", "resChanSeg",
    "asetSicAllocSeg", "asetGeoLocAllocSeg", "prodAsetVoltlClassSeg"})
@XmlRootElement(name = "prodInfoSeg")
@Searchable(root = false)
public class ProdInfoSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodSubtpCde;

    @XmlElement(required = true)
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodName;

    @XmlElement(required = false)
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodPllName;

    @XmlElement(required = false)
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodSllName;

    @XmlElement(required = true)
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodShrtName;

    @XmlElement(required = false)
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodShrtSllName;

    @XmlElement(required = false)
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String prodShrtPllName;

    @XmlElementWrapper(name = "switchableGroups")
    @XmlElement(name = "switchable", required = false)
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String[] switchableGroups;

    @XmlElement(required = true)
    protected String prodDesc;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String asetClassCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodStatCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ccyProdCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String riskLvlCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prdProdCde;
    @XmlElement(required = true)
    protected BigInteger prdProdNum;
    @XmlElement(required = true)
    protected BigInteger termRemainDayCnt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String prodLnchDt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String prodMturDt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mktInvstCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowBuyProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSellProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowBuyUtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowBuyAmtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSellUtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSellAmtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSellMipProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSellMipUtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSellMipAmtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSwInProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSwInUtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSwInAmtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodTaxFreeWrapActStaCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSwOutProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSwOutUtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowSwOutAmtProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String incmCharProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String cptlGurntProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String yieldEnhnProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String grwthCharProdInd;
    @XmlElement(required = true)
    protected BigInteger prtyProdSrchRsultNum;
    protected BigDecimal prdRtrnAvgNum;
    protected BigDecimal rtrnVoltlAvgPct;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String dmyProdSubtpRecInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String dispComProdSrchInd;
    @XmlElement(required = true)
    protected BigInteger divrNum;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mrkToMktInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ctryProdTrade1Cde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String busStartTm;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String busEndTm;
    @XmlElement(required = true)
    protected BigInteger prodTopSellRankNum;
    @XmlElement(required = true)
    protected BigInteger prodTopPerfmRankNum;
    @XmlElement(required = true)
    protected BigInteger prodTopYieldRankNum;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ccyInvstCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String qtyTypeCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodLocCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String trdFirstDt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptRcblCashProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptRcblScripProdInd;
    @XmlElement(required = true)
    protected BigInteger dcmlPlaceTradeUnitNum;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pldgLimitAssocAcctInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String aumChrgProdInd;
    @XmlElement(required = true)
    protected BigInteger invstInitMinAmt;
    @XmlElement(required = false)
    protected ProdTradeCcySeg prodTradeCcySeg;
    @XmlElement(required = true)
    protected ProdAsetUndlSeg prodAsetUndlSeg;
    @XmlElement(required = false)
    protected ProdListSeg prodListSeg;
    @XmlElement(required = true)
    protected List<ProdUserDefExtSeg> prodUserDefExtSeg;
    @XmlElement(required = true)
    protected List<ChanlAttrSeg> chanlAttrSeg;
    @XmlElement(required = false)
    protected ResChanSeg resChanSeg;
    @XmlElement(required = true)
    protected List<AsetSicAllocSeg> asetSicAllocSeg;
    @XmlElement(required = true)
    protected List<AsetGeoLocAllocSeg> asetGeoLocAllocSeg;
    @XmlElement(required = true)
    protected List<ProdAsetVoltlClassSeg> prodAsetVoltlClassSeg;

    /**
     * Gets the value of the prodSubtpCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdSubtpCde() {
        return this.prodSubtpCde;
    }

    /**
     * Sets the value of the prodSubtpCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdSubtpCde(final String value) {
        this.prodSubtpCde = value;
    }

    /**
     * Gets the value of the prodName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdName() {
        return this.prodName;
    }

    /**
     * Sets the value of the prodName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdName(final String value) {
        this.prodName = value;
    }

    /**
     * Gets the value of the prodShrtName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdShrtName() {
        return this.prodShrtName;
    }

    /**
     * Sets the value of the prodShrtName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdShrtName(final String value) {
        this.prodShrtName = value;
    }

    /**
     * Gets the value of the prodShrtSllName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdShrtSllName() {
        return this.prodShrtSllName;
    }

    /**
     * Sets the value of the prodShrtSllName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdShrtSllName(final String value) {
        this.prodShrtSllName = value;
    }

    /**
     * Gets the value of the prodDesc property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdDesc() {
        return this.prodDesc;
    }

    /**
     * Sets the value of the prodDesc property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdDesc(final String value) {
        this.prodDesc = value;
    }

    /**
     * Gets the value of the asetClassCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAsetClassCde() {
        return this.asetClassCde;
    }

    /**
     * Sets the value of the asetClassCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAsetClassCde(final String value) {
        this.asetClassCde = value;
    }

    /**
     * Gets the value of the prodStatCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdStatCde() {
        return this.prodStatCde;
    }

    /**
     * Sets the value of the prodStatCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdStatCde(final String value) {
        this.prodStatCde = value;
    }

    /**
     * Gets the value of the ccyProdCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCcyProdCde() {
        return this.ccyProdCde;
    }

    /**
     * Sets the value of the ccyProdCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCcyProdCde(final String value) {
        this.ccyProdCde = value;
    }

    /**
     * Gets the value of the riskLvlCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRiskLvlCde() {
        return this.riskLvlCde;
    }

    /**
     * Sets the value of the riskLvlCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRiskLvlCde(final String riskLvlCde) {
        this.riskLvlCde = riskLvlCde;
    }

    /**
     * Gets the value of the prdProdCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPrdProdCde() {
        return this.prdProdCde;
    }

    /**
     * Sets the value of the prdProdCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPrdProdCde(final String value) {
        this.prdProdCde = value;
    }

    /**
     * Gets the value of the prdProdNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getPrdProdNum() {
        return this.prdProdNum;
    }

    /**
     * Sets the value of the prdProdNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setPrdProdNum(final BigInteger value) {
        this.prdProdNum = value;
    }

    /**
     * Gets the value of the termRemainDayCnt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getTermRemainDayCnt() {
        return this.termRemainDayCnt;
    }

    /**
     * Sets the value of the termRemainDayCnt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setTermRemainDayCnt(final BigInteger value) {
        this.termRemainDayCnt = value;
    }

    /**
     * Gets the value of the prodLnchDt property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdLnchDt() {
        return this.prodLnchDt;
    }

    /**
     * Sets the value of the prodLnchDt property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdLnchDt(final String value) {
        this.prodLnchDt = value;
    }

    /**
     * Gets the value of the prodMturDt property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdMturDt() {
        return this.prodMturDt;
    }

    /**
     * Sets the value of the prodMturDt property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdMturDt(final String value) {
        this.prodMturDt = value;
    }

    /**
     * Gets the value of the mktInvstCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMktInvstCde() {
        return this.mktInvstCde;
    }

    /**
     * Sets the value of the mktInvstCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMktInvstCde(final String value) {
        this.mktInvstCde = value;
    }

    /**
     * Gets the value of the allowBuyProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowBuyProdInd() {
        return this.allowBuyProdInd;
    }

    /**
     * Sets the value of the allowBuyProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowBuyProdInd(final String value) {
        this.allowBuyProdInd = value;
    }

    /**
     * Gets the value of the allowSellProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSellProdInd() {
        return this.allowSellProdInd;
    }

    /**
     * Sets the value of the allowSellProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSellProdInd(final String value) {
        this.allowSellProdInd = value;
    }

    /**
     * Gets the value of the allowBuyUtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowBuyUtProdInd() {
        return this.allowBuyUtProdInd;
    }

    /**
     * Sets the value of the allowBuyUtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowBuyUtProdInd(final String value) {
        this.allowBuyUtProdInd = value;
    }

    /**
     * Gets the value of the allowBuyAmtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowBuyAmtProdInd() {
        return this.allowBuyAmtProdInd;
    }

    /**
     * Sets the value of the allowBuyAmtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowBuyAmtProdInd(final String value) {
        this.allowBuyAmtProdInd = value;
    }

    /**
     * Gets the value of the allowSellUtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSellUtProdInd() {
        return this.allowSellUtProdInd;
    }

    /**
     * Sets the value of the allowSellUtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSellUtProdInd(final String value) {
        this.allowSellUtProdInd = value;
    }

    /**
     * Gets the value of the allowSellAmtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSellAmtProdInd() {
        return this.allowSellAmtProdInd;
    }

    /**
     * Sets the value of the allowSellAmtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSellAmtProdInd(final String value) {
        this.allowSellAmtProdInd = value;
    }

    /**
     * Gets the value of the allowSellMipProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSellMipProdInd() {
        return this.allowSellMipProdInd;
    }

    /**
     * Sets the value of the allowSellMipProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSellMipProdInd(final String value) {
        this.allowSellMipProdInd = value;
    }

    /**
     * Gets the value of the allowSellMipUtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSellMipUtProdInd() {
        return this.allowSellMipUtProdInd;
    }

    /**
     * Sets the value of the allowSellMipUtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSellMipUtProdInd(final String value) {
        this.allowSellMipUtProdInd = value;
    }

    /**
     * Gets the value of the allowSellMipAmtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSellMipAmtProdInd() {
        return this.allowSellMipAmtProdInd;
    }

    /**
     * Sets the value of the allowSellMipAmtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSellMipAmtProdInd(final String value) {
        this.allowSellMipAmtProdInd = value;
    }

    /**
     * Gets the value of the allowSwInProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSwInProdInd() {
        return this.allowSwInProdInd;
    }

    /**
     * Sets the value of the allowSwInProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSwInProdInd(final String value) {
        this.allowSwInProdInd = value;
    }

    /**
     * Gets the value of the allowSwInUtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSwInUtProdInd() {
        return this.allowSwInUtProdInd;
    }

    /**
     * Sets the value of the allowSwInUtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSwInUtProdInd(final String value) {
        this.allowSwInUtProdInd = value;
    }

    /**
     * Gets the value of the allowSwInAmtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSwInAmtProdInd() {
        return this.allowSwInAmtProdInd;
    }

    /**
     * Sets the value of the allowSwInAmtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSwInAmtProdInd(final String value) {
        this.allowSwInAmtProdInd = value;
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
     * Gets the value of the allowSwOutProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSwOutProdInd() {
        return this.allowSwOutProdInd;
    }

    /**
     * Sets the value of the allowSwOutProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSwOutProdInd(final String value) {
        this.allowSwOutProdInd = value;
    }

    /**
     * Gets the value of the allowSwOutUtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSwOutUtProdInd() {
        return this.allowSwOutUtProdInd;
    }

    /**
     * Sets the value of the allowSwOutUtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSwOutUtProdInd(final String value) {
        this.allowSwOutUtProdInd = value;
    }

    /**
     * Gets the value of the allowSwOutAmtProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAllowSwOutAmtProdInd() {
        return this.allowSwOutAmtProdInd;
    }

    /**
     * Sets the value of the allowSwOutAmtProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAllowSwOutAmtProdInd(final String value) {
        this.allowSwOutAmtProdInd = value;
    }

    /**
     * Gets the value of the incmCharProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIncmCharProdInd() {
        return this.incmCharProdInd;
    }

    /**
     * Sets the value of the incmCharProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIncmCharProdInd(final String value) {
        this.incmCharProdInd = value;
    }

    /**
     * Gets the value of the cptlGurntProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCptlGurntProdInd() {
        return this.cptlGurntProdInd;
    }

    /**
     * Sets the value of the cptlGurntProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCptlGurntProdInd(final String value) {
        this.cptlGurntProdInd = value;
    }

    /**
     * Gets the value of the yieldEnhnProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getYieldEnhnProdInd() {
        return this.yieldEnhnProdInd;
    }

    /**
     * Sets the value of the yieldEnhnProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setYieldEnhnProdInd(final String value) {
        this.yieldEnhnProdInd = value;
    }

    /**
     * Gets the value of the grwthCharProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGrwthCharProdInd() {
        return this.grwthCharProdInd;
    }

    /**
     * Sets the value of the grwthCharProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGrwthCharProdInd(final String value) {
        this.grwthCharProdInd = value;
    }

    /**
     * Gets the value of the prtyProdSrchRsultNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getPrtyProdSrchRsultNum() {
        return this.prtyProdSrchRsultNum;
    }

    /**
     * Sets the value of the prtyProdSrchRsultNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setPrtyProdSrchRsultNum(final BigInteger value) {
        this.prtyProdSrchRsultNum = value;
    }

    /**
     * Gets the value of the prdRtrnAvgNum property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getPrdRtrnAvgNum() {
        return this.prdRtrnAvgNum;
    }

    /**
     * Sets the value of the prdRtrnAvgNum property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setPrdRtrnAvgNum(final BigDecimal value) {
        this.prdRtrnAvgNum = value;
    }

    /**
     * Gets the value of the rtrnVoltlAvgPct property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getRtrnVoltlAvgPct() {
        return this.rtrnVoltlAvgPct;
    }

    /**
     * Sets the value of the rtrnVoltlAvgPct property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setRtrnVoltlAvgPct(final BigDecimal value) {
        this.rtrnVoltlAvgPct = value;
    }

    /**
     * Gets the value of the dmyProdSubtpRecInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDmyProdSubtpRecInd() {
        return this.dmyProdSubtpRecInd;
    }

    /**
     * Sets the value of the dmyProdSubtpRecInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDmyProdSubtpRecInd(final String value) {
        this.dmyProdSubtpRecInd = value;
    }

    /**
     * Gets the value of the dispComProdSrchInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDispComProdSrchInd() {
        return this.dispComProdSrchInd;
    }

    /**
     * Sets the value of the dispComProdSrchInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDispComProdSrchInd(final String value) {
        this.dispComProdSrchInd = value;
    }

    /**
     * Gets the value of the divrNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getDivrNum() {
        return this.divrNum;
    }

    /**
     * Sets the value of the divrNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setDivrNum(final BigInteger value) {
        this.divrNum = value;
    }

    /**
     * Gets the value of the mrkToMktInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMrkToMktInd() {
        return this.mrkToMktInd;
    }

    /**
     * Sets the value of the mrkToMktInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMrkToMktInd(final String value) {
        this.mrkToMktInd = value;
    }

    /**
     * Gets the value of the ctryProdTrade1Cde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCtryProdTrade1Cde() {
        return this.ctryProdTrade1Cde;
    }

    /**
     * Sets the value of the ctryProdTrade1Cde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCtryProdTrade1Cde(final String value) {
        this.ctryProdTrade1Cde = value;
    }

    /**
     * Gets the value of the busStartTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBusStartTm() {
        return this.busStartTm;
    }

    /**
     * Sets the value of the busStartTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBusStartTm(final String value) {
        this.busStartTm = value;
    }

    /**
     * Gets the value of the busEndTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBusEndTm() {
        return this.busEndTm;
    }

    /**
     * Sets the value of the busEndTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBusEndTm(final String value) {
        this.busEndTm = value;
    }

    /**
     * Gets the value of the prodTopSellRankNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getProdTopSellRankNum() {
        return this.prodTopSellRankNum;
    }

    /**
     * Sets the value of the prodTopSellRankNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setProdTopSellRankNum(final BigInteger value) {
        this.prodTopSellRankNum = value;
    }

    /**
     * Gets the value of the prodTopPerfmRankNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getProdTopPerfmRankNum() {
        return this.prodTopPerfmRankNum;
    }

    /**
     * Sets the value of the prodTopPerfmRankNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setProdTopPerfmRankNum(final BigInteger value) {
        this.prodTopPerfmRankNum = value;
    }

    /**
     * Gets the value of the prodTopYieldRankNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getProdTopYieldRankNum() {
        return this.prodTopYieldRankNum;
    }

    /**
     * Sets the value of the prodTopYieldRankNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setProdTopYieldRankNum(final BigInteger value) {
        this.prodTopYieldRankNum = value;
    }

    /**
     * Gets the value of the ccyInvstCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCcyInvstCde() {
        return this.ccyInvstCde;
    }

    /**
     * Sets the value of the ccyInvstCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCcyInvstCde(final String value) {
        this.ccyInvstCde = value;
    }

    /**
     * Gets the value of the qtyTypeCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getQtyTypeCde() {
        return this.qtyTypeCde;
    }

    /**
     * Sets the value of the qtyTypeCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setQtyTypeCde(final String value) {
        this.qtyTypeCde = value;
    }

    /**
     * Gets the value of the prodLocCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdLocCde() {
        return this.prodLocCde;
    }

    /**
     * Sets the value of the prodLocCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdLocCde(final String value) {
        this.prodLocCde = value;
    }

    /**
     * Gets the value of the trdFirstDt property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTrdFirstDt() {
        return this.trdFirstDt;
    }

    /**
     * Sets the value of the trdFirstDt property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTrdFirstDt(final String value) {
        this.trdFirstDt = value;
    }

    /**
     * Gets the value of the suptRcblCashProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSuptRcblCashProdInd() {
        return this.suptRcblCashProdInd;
    }

    /**
     * Sets the value of the suptRcblCashProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSuptRcblCashProdInd(final String value) {
        this.suptRcblCashProdInd = value;
    }

    /**
     * Gets the value of the suptRcblScripProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSuptRcblScripProdInd() {
        return this.suptRcblScripProdInd;
    }

    /**
     * Sets the value of the suptRcblScripProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSuptRcblScripProdInd(final String value) {
        this.suptRcblScripProdInd = value;
    }

    /**
     * Gets the value of the dcmlPlaceTradeUnitNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getDcmlPlaceTradeUnitNum() {
        return this.dcmlPlaceTradeUnitNum;
    }

    /**
     * Sets the value of the dcmlPlaceTradeUnitNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setDcmlPlaceTradeUnitNum(final BigInteger value) {
        this.dcmlPlaceTradeUnitNum = value;
    }

    /**
     * Gets the value of the pldgLimitAssocAcctInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPldgLimitAssocAcctInd() {
        return this.pldgLimitAssocAcctInd;
    }

    /**
     * Sets the value of the pldgLimitAssocAcctInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPldgLimitAssocAcctInd(final String value) {
        this.pldgLimitAssocAcctInd = value;
    }

    /**
     * Gets the value of the aumChrgProdInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAumChrgProdInd() {
        return this.aumChrgProdInd;
    }

    /**
     * Sets the value of the aumChrgProdInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAumChrgProdInd(final String value) {
        this.aumChrgProdInd = value;
    }

    /**
     * Gets the value of the invstInitMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getInvstInitMinAmt() {
        return this.invstInitMinAmt;
    }

    /**
     * Sets the value of the invstInitMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setInvstInitMinAmt(final BigInteger value) {
        this.invstInitMinAmt = value;
    }

    /**
     * Gets the value of the prodTradeCcySeg property.
     * 
     * @return possible object is {@link ProdTradeCcySeg }
     * 
     */
    public ProdTradeCcySeg getProdTradeCcySeg() {
        return this.prodTradeCcySeg;
    }

    /**
     * Sets the value of the prodTradeCcySeg property.
     * 
     * @param value
     *            allowed object is {@link ProdTradeCcySeg }
     * 
     */
    public void setProdTradeCcySeg(final ProdTradeCcySeg value) {
        this.prodTradeCcySeg = value;
    }

    /**
     * Gets the value of the prodAsetUndlSeg property.
     * 
     * @return possible object is {@link ProdAsetUndlSeg }
     * 
     */
    public ProdAsetUndlSeg getProdAsetUndlSeg() {
        return this.prodAsetUndlSeg;
    }

    /**
     * Sets the value of the prodAsetUndlSeg property.
     * 
     * @param value
     *            allowed object is {@link ProdAsetUndlSeg }
     * 
     */
    public void setProdAsetUndlSeg(final ProdAsetUndlSeg value) {
        this.prodAsetUndlSeg = value;
    }

    /**
     * Gets the value of the prodListSeg property.
     * 
     * @return possible object is {@link ProdListSeg }
     * 
     */
    public ProdListSeg getProdListSeg() {
        return this.prodListSeg;
    }

    /**
     * Sets the value of the prodListSeg property.
     * 
     * @param value
     *            allowed object is {@link ProdListSeg }
     * 
     */
    public void setProdListSeg(final ProdListSeg value) {
        this.prodListSeg = value;
    }

    /**
     * @return the prodUserDefExtSeg
     */
    public List<ProdUserDefExtSeg> getProdUserDefExtSeg() {
        return this.prodUserDefExtSeg;
    }

    /**
     * @param prodUserDefExtSeg
     *            the prodUserDefExtSeg to set
     */
    public void setProdUserDefExtSeg(final List<ProdUserDefExtSeg> prodUserDefExtSeg) {
        this.prodUserDefExtSeg = prodUserDefExtSeg;
    }

    /**
     * @return the chanlAttrSeg
     */
    public List<ChanlAttrSeg> getChanlAttrSeg() {
        return this.chanlAttrSeg;
    }

    /**
     * @param chanlAttrSeg
     *            the chanlAttrSeg to set
     */
    public void setChanlAttrSeg(final List<ChanlAttrSeg> chanlAttrSeg) {
        this.chanlAttrSeg = chanlAttrSeg;
    }

    /**
     * Gets the value of the resChanSeg property.
     * 
     * @return possible object is {@link ResChanSeg }
     * 
     */
    public ResChanSeg getResChanSeg() {
        return this.resChanSeg;
    }

    /**
     * Sets the value of the resChanSeg property.
     * 
     * @param value
     *            allowed object is {@link ResChanSeg }
     * 
     */
    public void setResChanSeg(final ResChanSeg value) {
        this.resChanSeg = value;
    }

    public String getProdPllName() {
        return this.prodPllName;
    }

    public void setProdPllName(final String prodPllName) {
        this.prodPllName = prodPllName;
    }

    public String getProdShrtPllName() {
        return this.prodShrtPllName;
    }

    public void setProdShrtPllName(final String prodShrtPllName) {
        this.prodShrtPllName = prodShrtPllName;
    }

    public String getProdSllName() {
        return this.prodSllName;
    }

    public void setProdSllName(final String prodSllName) {
        this.prodSllName = prodSllName;
    }

    public List<AsetSicAllocSeg> getAsetSicAllocSeg() {
        return this.asetSicAllocSeg;
    }

    public void setAsetSicAllocSeg(final List<AsetSicAllocSeg> asetSicAllocSeg) {
        this.asetSicAllocSeg = asetSicAllocSeg;
    }

    public List<AsetGeoLocAllocSeg> getAsetGeoLocAllocSeg() {
        return this.asetGeoLocAllocSeg;
    }

    public void setAsetGeoLocAllocSeg(final List<AsetGeoLocAllocSeg> asetGeoLocAllocSeg) {
        this.asetGeoLocAllocSeg = asetGeoLocAllocSeg;
    }

    public List<ProdAsetVoltlClassSeg> getProdAsetVoltlClassSeg() {
        return this.prodAsetVoltlClassSeg;
    }

    public void setProdAsetVoltlClassSeg(final List<ProdAsetVoltlClassSeg> prodAsetVoltlClassSeg) {
        this.prodAsetVoltlClassSeg = prodAsetVoltlClassSeg;
    }

    /**
     * @return the switchableGroups
     */
    public String[] getSwitchableGroups() {
        return this.switchableGroups;
    }

    /**
     * @param switchableGroups
     *            the switchableGroups to set
     */
    public void setSwitchableGroups(final String[] switchableGroups) {
        this.switchableGroups = switchableGroups;
    }

}
