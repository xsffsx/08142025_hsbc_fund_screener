/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.warrant;

import java.math.BigInteger;

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
 *         &lt;element ref=&quot;{}mrgnTrdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptAuctnTrdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}sprdStopLossMinCnt&quot;/&gt;
 *         &lt;element ref=&quot;{}sprdStopLossMaxCnt&quot;/&gt;
 *         &lt;element ref=&quot;{}prodBodLotQtyCnt&quot;/&gt;
 *         &lt;element ref=&quot;{}mktProdTrdCde&quot;/&gt;
 *         &lt;element ref=&quot;{}suptMipInd&quot;/&gt;
 *         &lt;element ref=&quot;{}bodLotProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}trdLimitInd&quot;/&gt;
 *         &lt;element ref=&quot;{}psblProdBorwInd&quot;/&gt;
 *         &lt;element ref=&quot;{}allowProdLendInd&quot;/&gt;
 *         &lt;element ref=&quot;{}poolProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}scripOnlyProdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptAtmcTrdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptNetSetlInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptStopLossOrdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptWinWinOrdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptAllIn1OrdInd&quot;/&gt;
 *         &lt;element ref=&quot;{}suptProdSpltInd&quot;/&gt;
 *         &lt;element ref=&quot;{}mrgnPrcAuctnTrdPct&quot;/&gt;
 *         &lt;element ref=&quot;{}exclLimitCalcInd&quot;/&gt;
 *         &lt;element ref=&quot;{}prodMktStatChngLaDt&quot;/&gt;
 *         &lt;element ref=&quot;{}prcVarCde&quot;/&gt;
 *         &lt;element ref=&quot;{}methCalcScripChrgCde&quot;/&gt;
 *         &lt;element ref=&quot;{}mktSegExchgCde&quot;/&gt;
 *         &lt;element ref=&quot;{}ctryProdRegisCde&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"mrgnTrdInd", "suptAuctnTrdInd", "sprdStopLossMinCnt", "sprdStopLossMaxCnt", "prodBodLotQtyCnt",
    "mktProdTrdCde", "suptMipInd", "bodLotProdInd", "trdLimitInd", "psblProdBorwInd", "allowProdLendInd", "poolProdInd",
    "scripOnlyProdInd", "suptAtmcTrdInd", "suptNetSetlInd", "suptStopLossOrdInd", "suptWinWinOrdInd", "suptAllIn1OrdInd",
    "suptProdSpltInd", "mrgnPrcAuctnTrdPct", "exclLimitCalcInd", "prodMktStatChngLaDt", "prcVarCde", "methCalcScripChrgCde",
    "mktSegExchgCde", "ctryProdRegisCde"})
@XmlRootElement(name = "stockInstmSeg")
public class StockInstmSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mrgnTrdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptAuctnTrdInd;
    @XmlElement(required = true)
    protected BigInteger sprdStopLossMinCnt;
    @XmlElement(required = true)
    protected BigInteger sprdStopLossMaxCnt;
    @XmlElement(required = true)
    protected BigInteger prodBodLotQtyCnt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mktProdTrdCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptMipInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String bodLotProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String trdLimitInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String psblProdBorwInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowProdLendInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String poolProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String scripOnlyProdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptAtmcTrdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptNetSetlInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptStopLossOrdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptWinWinOrdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptAllIn1OrdInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptProdSpltInd;
    @XmlElement(required = true)
    protected BigInteger mrgnPrcAuctnTrdPct;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String exclLimitCalcInd;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String prodMktStatChngLaDt;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prcVarCde;
    @XmlElement(required = true)
    protected MethCalcScripChrgCde methCalcScripChrgCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mktSegExchgCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ctryProdRegisCde;

    /**
     * Gets the value of the mrgnTrdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMrgnTrdInd() {
        return this.mrgnTrdInd;
    }

    /**
     * Sets the value of the mrgnTrdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setMrgnTrdInd(final String value) {
        this.mrgnTrdInd = value;
    }

    /**
     * Gets the value of the suptAuctnTrdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuptAuctnTrdInd() {
        return this.suptAuctnTrdInd;
    }

    /**
     * Sets the value of the suptAuctnTrdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSuptAuctnTrdInd(final String value) {
        this.suptAuctnTrdInd = value;
    }

    /**
     * Gets the value of the sprdStopLossMinCnt property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getSprdStopLossMinCnt() {
        return this.sprdStopLossMinCnt;
    }

    /**
     * Sets the value of the sprdStopLossMinCnt property.
     *
     * @param value
     *            allowed object is {@link BigInteger }
     *
     */
    public void setSprdStopLossMinCnt(final BigInteger value) {
        this.sprdStopLossMinCnt = value;
    }

    /**
     * Gets the value of the sprdStopLossMaxCnt property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getSprdStopLossMaxCnt() {
        return this.sprdStopLossMaxCnt;
    }

    /**
     * Sets the value of the sprdStopLossMaxCnt property.
     *
     * @param value
     *            allowed object is {@link BigInteger }
     *
     */
    public void setSprdStopLossMaxCnt(final BigInteger value) {
        this.sprdStopLossMaxCnt = value;
    }

    /**
     * Gets the value of the prodBodLotQtyCnt property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getProdBodLotQtyCnt() {
        return this.prodBodLotQtyCnt;
    }

    /**
     * Sets the value of the prodBodLotQtyCnt property.
     *
     * @param value
     *            allowed object is {@link BigInteger }
     *
     */
    public void setProdBodLotQtyCnt(final BigInteger value) {
        this.prodBodLotQtyCnt = value;
    }

    /**
     * Gets the value of the mktProdTrdCde property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMktProdTrdCde() {
        return this.mktProdTrdCde;
    }

    /**
     * Sets the value of the mktProdTrdCde property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setMktProdTrdCde(final String value) {
        this.mktProdTrdCde = value;
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
     * Gets the value of the bodLotProdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getBodLotProdInd() {
        return this.bodLotProdInd;
    }

    /**
     * Sets the value of the bodLotProdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setBodLotProdInd(final String value) {
        this.bodLotProdInd = value;
    }

    /**
     * Gets the value of the trdLimitInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTrdLimitInd() {
        return this.trdLimitInd;
    }

    /**
     * Sets the value of the trdLimitInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setTrdLimitInd(final String value) {
        this.trdLimitInd = value;
    }

    /**
     * Gets the value of the psblProdBorwInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPsblProdBorwInd() {
        return this.psblProdBorwInd;
    }

    /**
     * Sets the value of the psblProdBorwInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setPsblProdBorwInd(final String value) {
        this.psblProdBorwInd = value;
    }

    /**
     * Gets the value of the allowProdLendInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getAllowProdLendInd() {
        return this.allowProdLendInd;
    }

    /**
     * Sets the value of the allowProdLendInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setAllowProdLendInd(final String value) {
        this.allowProdLendInd = value;
    }

    /**
     * Gets the value of the poolProdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPoolProdInd() {
        return this.poolProdInd;
    }

    /**
     * Sets the value of the poolProdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setPoolProdInd(final String value) {
        this.poolProdInd = value;
    }

    /**
     * Gets the value of the scripOnlyProdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getScripOnlyProdInd() {
        return this.scripOnlyProdInd;
    }

    /**
     * Sets the value of the scripOnlyProdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setScripOnlyProdInd(final String value) {
        this.scripOnlyProdInd = value;
    }

    /**
     * Gets the value of the suptAtmcTrdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuptAtmcTrdInd() {
        return this.suptAtmcTrdInd;
    }

    /**
     * Sets the value of the suptAtmcTrdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSuptAtmcTrdInd(final String value) {
        this.suptAtmcTrdInd = value;
    }

    /**
     * Gets the value of the suptNetSetlInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuptNetSetlInd() {
        return this.suptNetSetlInd;
    }

    /**
     * Sets the value of the suptNetSetlInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSuptNetSetlInd(final String value) {
        this.suptNetSetlInd = value;
    }

    /**
     * Gets the value of the suptStopLossOrdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuptStopLossOrdInd() {
        return this.suptStopLossOrdInd;
    }

    /**
     * Sets the value of the suptStopLossOrdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSuptStopLossOrdInd(final String value) {
        this.suptStopLossOrdInd = value;
    }

    /**
     * Gets the value of the suptWinWinOrdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuptWinWinOrdInd() {
        return this.suptWinWinOrdInd;
    }

    /**
     * Sets the value of the suptWinWinOrdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSuptWinWinOrdInd(final String value) {
        this.suptWinWinOrdInd = value;
    }

    /**
     * Gets the value of the suptAllIn1OrdInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuptAllIn1OrdInd() {
        return this.suptAllIn1OrdInd;
    }

    /**
     * Sets the value of the suptAllIn1OrdInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSuptAllIn1OrdInd(final String value) {
        this.suptAllIn1OrdInd = value;
    }

    /**
     * Gets the value of the suptProdSpltInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSuptProdSpltInd() {
        return this.suptProdSpltInd;
    }

    /**
     * Sets the value of the suptProdSpltInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setSuptProdSpltInd(final String value) {
        this.suptProdSpltInd = value;
    }

    /**
     * Gets the value of the mrgnPrcAuctnTrdPct property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getMrgnPrcAuctnTrdPct() {
        return this.mrgnPrcAuctnTrdPct;
    }

    /**
     * Sets the value of the mrgnPrcAuctnTrdPct property.
     *
     * @param value
     *            allowed object is {@link BigInteger }
     *
     */
    public void setMrgnPrcAuctnTrdPct(final BigInteger value) {
        this.mrgnPrcAuctnTrdPct = value;
    }

    /**
     * Gets the value of the exclLimitCalcInd property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getExclLimitCalcInd() {
        return this.exclLimitCalcInd;
    }

    /**
     * Sets the value of the exclLimitCalcInd property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setExclLimitCalcInd(final String value) {
        this.exclLimitCalcInd = value;
    }

    /**
     * Gets the value of the prodMktStatChngLaDt property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getProdMktStatChngLaDt() {
        return this.prodMktStatChngLaDt;
    }

    /**
     * Sets the value of the prodMktStatChngLaDt property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setProdMktStatChngLaDt(final String value) {
        this.prodMktStatChngLaDt = value;
    }

    /**
     * Gets the value of the prcVarCde property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPrcVarCde() {
        return this.prcVarCde;
    }

    /**
     * Sets the value of the prcVarCde property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setPrcVarCde(final String value) {
        this.prcVarCde = value;
    }

    /**
     * Gets the value of the methCalcScripChrgCde property.
     *
     * @return possible object is {@link MethCalcScripChrgCde }
     *
     */
    public MethCalcScripChrgCde getMethCalcScripChrgCde() {
        return this.methCalcScripChrgCde;
    }

    /**
     * Sets the value of the methCalcScripChrgCde property.
     *
     * @param value
     *            allowed object is {@link MethCalcScripChrgCde }
     *
     */
    public void setMethCalcScripChrgCde(final MethCalcScripChrgCde value) {
        this.methCalcScripChrgCde = value;
    }

    /**
     * Gets the value of the mktSegExchgCde property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMktSegExchgCde() {
        return this.mktSegExchgCde;
    }

    /**
     * Sets the value of the mktSegExchgCde property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setMktSegExchgCde(final String value) {
        this.mktSegExchgCde = value;
    }

    /**
     * Gets the value of the ctryProdRegisCde property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCtryProdRegisCde() {
        return this.ctryProdRegisCde;
    }

    /**
     * Sets the value of the ctryProdRegisCde property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setCtryProdRegisCde(final String value) {
        this.ctryProdRegisCde = value;
    }

}
