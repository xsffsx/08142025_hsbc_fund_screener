/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
 *         &lt;element ref=&quot;{}fundHouseCde&quot;/&gt;
 *         &lt;element ref=&quot;{}closeEndFundInd&quot;/&gt;
 *         &lt;element ref=&quot;{}invstIncrmMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}utRdmMinNum&quot;/&gt;
 *         &lt;element ref=&quot;{}rdmMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}utRtainMinNum&quot;/&gt;
 *         &lt;element ref=&quot;{}fundRtainMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}suptMipInd&quot;/&gt;
 *         &lt;element ref=&quot;{}invstMipMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}invstMipIncrmMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}utMipRdmMinNum&quot;/&gt;
 *         &lt;element ref=&quot;{}rdmMipMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}utMipRtainMinNum&quot;/&gt;
 *         &lt;element ref=&quot;{}fundMipRtainMinAmt&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}schemChrgCde&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}chrgInitSalesPct&quot;/&gt;
 *         &lt;element ref=&quot;{}dscntMaxPct&quot;/&gt;
 *         &lt;element ref=&quot;{}offerStartDtTm&quot;/&gt;
 *         &lt;element ref=&quot;{}offerEndDtTm&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}payCashDivInd&quot;/&gt;
 *         &lt;element ref=&quot;{}hldayFundNextDt&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}scribCtoffNextDtTm&quot;/&gt;
 *         &lt;element ref=&quot;{}rdmCtoffNextDtTm&quot;/&gt;
 *         &lt;element ref=&quot;{}dealNextDt&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}fundClassCde&quot;/&gt;
 *         &lt;element ref=&quot;{}amcmInd&quot;/&gt;
 *         &lt;element ref=&quot;{}spclFundInd&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}insuLinkUtTrstInd&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}fundSwInMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}fundSwOutMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}fundSwOutRtainMinAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}utSwOutRtainMinNum&quot;/&gt;
 *         &lt;element ref=&quot;{}fundSwOutMaxAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}tranMaxAmt&quot;/&gt;
 *         &lt;element ref=&quot;{}fundUnswithSeg&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"fundHouseCde", "closeEndFundInd", "invstIncrmMinAmt", "utRdmMinNum", "rdmMinAmt",
    "utRtainMinNum", "fundRtainMinAmt", "suptMipInd", "invstMipMinAmt", "invstMipIncrmMinAmt", "utMipRdmMinNum", "rdmMipMinAmt",
    "utMipRtainMinNum", "fundMipRtainMinAmt", "schemChrgCde", "chrgInitSalesPct", "dscntMaxPct", "offerStartDtTm", "offerEndDtTm",
    "payCashDivInd", "hldayFundNextDt", "scribCtoffNextDtTm", "rdmCtoffNextDtTm", "dealNextDt", "fundClassCde", "amcmInd",
    "spclFundInd", "insuLinkUtTrstInd", "fundSwInMinAmt", "fundSwOutMinAmt", "fundSwOutRtainMinAmt", "utSwOutRtainMinNum",
    "fundSwOutMaxAmt", "tranMaxAmt", "prodShoreLocCde", "fundUnswithSeg"})
@XmlRootElement(name = "utTrstInstmSeg")
public class UtTrstInstmSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String fundHouseCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String closeEndFundInd;
    @XmlElement(required = true)
    protected BigInteger invstIncrmMinAmt;
    @XmlElement(required = true)
    protected BigInteger utRdmMinNum;
    @XmlElement(required = true)
    protected BigInteger rdmMinAmt;
    @XmlElement(required = true)
    protected BigInteger utRtainMinNum;
    @XmlElement(required = true)
    protected BigInteger fundRtainMinAmt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptMipInd;
    @XmlElement(required = true)
    protected BigInteger invstMipMinAmt;
    @XmlElement(required = true)
    protected BigInteger invstMipIncrmMinAmt;
    @XmlElement(required = true)
    protected BigInteger utMipRdmMinNum;
    @XmlElement(required = true)
    protected BigInteger rdmMipMinAmt;
    @XmlElement(required = true)
    protected BigInteger utMipRtainMinNum;
    protected BigInteger fundMipRtainMinAmt;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String schemChrgCde;
    @XmlElement(required = true)
    protected BigDecimal chrgInitSalesPct;
    @XmlElement(required = true)
    protected BigDecimal dscntMaxPct;
    @XmlElement(required = true)
    protected String offerStartDtTm;
    protected String offerEndDtTm;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String payCashDivInd;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String hldayFundNextDt;
    @XmlElement(required = true)
    protected String scribCtoffNextDtTm;
    @XmlElement(required = true)
    protected String rdmCtoffNextDtTm;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String dealNextDt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String fundClassCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String amcmInd;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String spclFundInd;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String insuLinkUtTrstInd;
    @XmlElement(required = true)
    protected BigInteger fundSwInMinAmt;
    @XmlElement(required = true)
    protected BigInteger fundSwOutMinAmt;
    @XmlElement(required = true)
    protected BigInteger fundSwOutRtainMinAmt;
    @XmlElement(required = true)
    protected BigInteger utSwOutRtainMinNum;
    @XmlElement(required = true)
    protected BigDecimal fundSwOutMaxAmt;
    @XmlElement(required = true)
    protected BigDecimal tranMaxAmt;
    @XmlElement(required = true)
    protected String prodShoreLocCde;
    protected List<FundUnswithSeg> fundUnswithSeg;

    /**
     * Gets the value of the fundHouseCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFundHouseCde() {
        return this.fundHouseCde;
    }

    /**
     * Sets the value of the fundHouseCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFundHouseCde(final String value) {
        this.fundHouseCde = value;
    }

    /**
     * Gets the value of the closeEndFundInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCloseEndFundInd() {
        return this.closeEndFundInd;
    }

    /**
     * Sets the value of the closeEndFundInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCloseEndFundInd(final String value) {
        this.closeEndFundInd = value;
    }

    /**
     * Gets the value of the invstIncrmMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getInvstIncrmMinAmt() {
        return this.invstIncrmMinAmt;
    }

    /**
     * Sets the value of the invstIncrmMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setInvstIncrmMinAmt(final BigInteger value) {
        this.invstIncrmMinAmt = value;
    }

    /**
     * Gets the value of the utRdmMinNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getUtRdmMinNum() {
        return this.utRdmMinNum;
    }

    /**
     * Sets the value of the utRdmMinNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setUtRdmMinNum(final BigInteger value) {
        this.utRdmMinNum = value;
    }

    /**
     * Gets the value of the rdmMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getRdmMinAmt() {
        return this.rdmMinAmt;
    }

    /**
     * Sets the value of the rdmMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setRdmMinAmt(final BigInteger value) {
        this.rdmMinAmt = value;
    }

    /**
     * Gets the value of the utRtainMinNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getUtRtainMinNum() {
        return this.utRtainMinNum;
    }

    /**
     * Sets the value of the utRtainMinNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setUtRtainMinNum(final BigInteger value) {
        this.utRtainMinNum = value;
    }

    /**
     * Gets the value of the fundRtainMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getFundRtainMinAmt() {
        return this.fundRtainMinAmt;
    }

    /**
     * Sets the value of the fundRtainMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setFundRtainMinAmt(final BigInteger value) {
        this.fundRtainMinAmt = value;
    }

    /**
     * Gets the value of the suptMipInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSuptMipInd() {
        return this.suptMipInd;
    }

    /**
     * Sets the value of the suptMipInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSuptMipInd(final String value) {
        this.suptMipInd = value;
    }

    /**
     * Gets the value of the invstMipMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getInvstMipMinAmt() {
        return this.invstMipMinAmt;
    }

    /**
     * Sets the value of the invstMipMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setInvstMipMinAmt(final BigInteger value) {
        this.invstMipMinAmt = value;
    }

    /**
     * Gets the value of the invstMipIncrmMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getInvstMipIncrmMinAmt() {
        return this.invstMipIncrmMinAmt;
    }

    /**
     * Sets the value of the invstMipIncrmMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setInvstMipIncrmMinAmt(final BigInteger value) {
        this.invstMipIncrmMinAmt = value;
    }

    /**
     * Gets the value of the utMipRdmMinNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getUtMipRdmMinNum() {
        return this.utMipRdmMinNum;
    }

    /**
     * Sets the value of the utMipRdmMinNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setUtMipRdmMinNum(final BigInteger value) {
        this.utMipRdmMinNum = value;
    }

    /**
     * Gets the value of the rdmMipMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getRdmMipMinAmt() {
        return this.rdmMipMinAmt;
    }

    /**
     * Sets the value of the rdmMipMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setRdmMipMinAmt(final BigInteger value) {
        this.rdmMipMinAmt = value;
    }

    /**
     * Gets the value of the utMipRtainMinNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getUtMipRtainMinNum() {
        return this.utMipRtainMinNum;
    }

    /**
     * Sets the value of the utMipRtainMinNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setUtMipRtainMinNum(final BigInteger value) {
        this.utMipRtainMinNum = value;
    }

    /**
     * Gets the value of the fundMipRtainMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getFundMipRtainMinAmt() {
        return this.fundMipRtainMinAmt;
    }

    /**
     * Sets the value of the fundMipRtainMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setFundMipRtainMinAmt(final BigInteger value) {
        this.fundMipRtainMinAmt = value;
    }

    /**
     * Gets the value of the schemChrgCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSchemChrgCde() {
        return this.schemChrgCde;
    }

    /**
     * Sets the value of the schemChrgCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSchemChrgCde(final String value) {
        this.schemChrgCde = value;
    }

    /**
     * Gets the value of the chrgInitSalesPct property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getChrgInitSalesPct() {
        return this.chrgInitSalesPct;
    }

    /**
     * Sets the value of the chrgInitSalesPct property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setChrgInitSalesPct(final BigDecimal value) {
        this.chrgInitSalesPct = value;
    }

    /**
     * Gets the value of the dscntMaxPct property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getDscntMaxPct() {
        return this.dscntMaxPct;
    }

    /**
     * Sets the value of the dscntMaxPct property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setDscntMaxPct(final BigDecimal value) {
        this.dscntMaxPct = value;
    }

    /**
     * Gets the value of the offerStartDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOfferStartDtTm() {
        return this.offerStartDtTm;
    }

    /**
     * Sets the value of the offerStartDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOfferStartDtTm(final String value) {
        this.offerStartDtTm = value;
    }

    /**
     * Gets the value of the offerEndDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOfferEndDtTm() {
        return this.offerEndDtTm;
    }

    /**
     * Sets the value of the offerEndDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOfferEndDtTm(final String value) {
        this.offerEndDtTm = value;
    }

    /**
     * Gets the value of the payCashDivInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPayCashDivInd() {
        return this.payCashDivInd;
    }

    /**
     * Sets the value of the payCashDivInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPayCashDivInd(final String value) {
        this.payCashDivInd = value;
    }

    /**
     * Gets the value of the hldayFundNextDt property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHldayFundNextDt() {
        return this.hldayFundNextDt;
    }

    /**
     * Sets the value of the hldayFundNextDt property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHldayFundNextDt(final String value) {
        this.hldayFundNextDt = value;
    }

    /**
     * Gets the value of the scribCtoffNextDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getScribCtoffNextDtTm() {
        return this.scribCtoffNextDtTm;
    }

    /**
     * Sets the value of the scribCtoffNextDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setScribCtoffNextDtTm(final String value) {
        this.scribCtoffNextDtTm = value;
    }

    /**
     * Gets the value of the rdmCtoffNextDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRdmCtoffNextDtTm() {
        return this.rdmCtoffNextDtTm;
    }

    /**
     * Sets the value of the rdmCtoffNextDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRdmCtoffNextDtTm(final String value) {
        this.rdmCtoffNextDtTm = value;
    }

    /**
     * Gets the value of the dealNextDt property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDealNextDt() {
        return this.dealNextDt;
    }

    /**
     * Sets the value of the dealNextDt property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDealNextDt(final String value) {
        this.dealNextDt = value;
    }

    /**
     * Gets the value of the fundClassCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFundClassCde() {
        return this.fundClassCde;
    }

    /**
     * Sets the value of the fundClassCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFundClassCde(final String value) {
        this.fundClassCde = value;
    }

    /**
     * Gets the value of the amcmInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAmcmInd() {
        return this.amcmInd;
    }

    /**
     * Sets the value of the amcmInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAmcmInd(final String value) {
        this.amcmInd = value;
    }

    /**
     * Gets the value of the spclFundInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSpclFundInd() {
        return this.spclFundInd;
    }

    /**
     * Sets the value of the spclFundInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSpclFundInd(final String value) {
        this.spclFundInd = value;
    }

    /**
     * Gets the value of the insuLinkUtTrstInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInsuLinkUtTrstInd() {
        return this.insuLinkUtTrstInd;
    }

    /**
     * Sets the value of the insuLinkUtTrstInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInsuLinkUtTrstInd(final String value) {
        this.insuLinkUtTrstInd = value;
    }

    /**
     * Gets the value of the fundSwInMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getFundSwInMinAmt() {
        return this.fundSwInMinAmt;
    }

    /**
     * Sets the value of the fundSwInMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setFundSwInMinAmt(final BigInteger value) {
        this.fundSwInMinAmt = value;
    }

    /**
     * Gets the value of the fundSwOutMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getFundSwOutMinAmt() {
        return this.fundSwOutMinAmt;
    }

    /**
     * Sets the value of the fundSwOutMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setFundSwOutMinAmt(final BigInteger value) {
        this.fundSwOutMinAmt = value;
    }

    /**
     * Gets the value of the fundSwOutRtainMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getFundSwOutRtainMinAmt() {
        return this.fundSwOutRtainMinAmt;
    }

    /**
     * Sets the value of the fundSwOutRtainMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setFundSwOutRtainMinAmt(final BigInteger value) {
        this.fundSwOutRtainMinAmt = value;
    }

    /**
     * Gets the value of the utSwOutRtainMinNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getUtSwOutRtainMinNum() {
        return this.utSwOutRtainMinNum;
    }

    /**
     * Sets the value of the utSwOutRtainMinNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setUtSwOutRtainMinNum(final BigInteger value) {
        this.utSwOutRtainMinNum = value;
    }

    /**
     * Gets the value of the fundSwOutMaxAmt property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getFundSwOutMaxAmt() {
        return this.fundSwOutMaxAmt;
    }

    /**
     * Sets the value of the fundSwOutMaxAmt property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setFundSwOutMaxAmt(final BigDecimal value) {
        this.fundSwOutMaxAmt = value;
    }

    /**
     * Gets the value of the tranMaxAmt property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getTranMaxAmt() {
        return this.tranMaxAmt;
    }

    /**
     * Sets the value of the tranMaxAmt property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setTranMaxAmt(final BigDecimal value) {
        this.tranMaxAmt = value;
    }

    /**
     * @return the prodShoreLocCde
     */
    public String getProdShoreLocCde() {
        return this.prodShoreLocCde;
    }

    /**
     * @param prodShoreLocCde
     *            the prodShoreLocCde to set
     */
    public void setProdShoreLocCde(final String prodShoreLocCde) {
        this.prodShoreLocCde = prodShoreLocCde;
    }

    /**
     * Gets the value of the fundUnswithSeg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the fundUnswithSeg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getFundUnswithSeg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FundUnswithSeg }
     * 
     * 
     */
    public List<FundUnswithSeg> getFundUnswithSeg() {
        if (this.fundUnswithSeg == null) {
            this.fundUnswithSeg = new ArrayList<FundUnswithSeg>();
        }
        return this.fundUnswithSeg;
    }

}
