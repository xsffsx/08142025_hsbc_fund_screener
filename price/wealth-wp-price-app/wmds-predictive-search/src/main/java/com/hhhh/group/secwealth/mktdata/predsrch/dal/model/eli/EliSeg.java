/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.eli;

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
 *         &lt;sequence minOccurs=&quot;0&quot;&gt;
 *           &lt;element ref=&quot;{}isrBndNme&quot;/&gt;
 *           &lt;element ref=&quot;{}pdcyCoupnPymtCd&quot;/&gt;
 *           &lt;element ref=&quot;{}coupnAnnlRt&quot;/&gt;
 *           &lt;element ref=&quot;{}flexMatOptInd&quot;/&gt;
 *           &lt;element ref=&quot;{}IntIndAccrAmt&quot;/&gt;
 *         &lt;/sequence&gt;
 *         &lt;element ref=&quot;{}InvstIncMinAmt&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}SubDebtInd&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"prodExtnlCde", "prodExtnlTypeCde", "eqtyLinkInvstTypeCde", "trdDt", "dscntBuyPct",
    "dscntSellPct", "yieldToMturPct", "denAmt", "trdMinAmt", "suptAonInd", "pymtDt", "valnDt", "offerTypeCde", "custSellQtaCnt",
    "ruleQtaAltmtCde", "setlDt", "lnchProdInd", "rtrvProdExtnlInd", "prodExtnlCatCde", "pdcyCallCde", "pdcyKnockInCde"})
@XmlRootElement(name = "eliSeg")
public class EliSeg {

    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodExtnlCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodExtnlTypeCde;

    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String eqtyLinkInvstTypeCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String trdDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String dscntBuyPct;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String dscntSellPct;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String yieldToMturPct;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String denAmt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String trdMinAmt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String suptAonInd;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pymtDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String valnDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String offerTypeCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String custSellQtaCnt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ruleQtaAltmtCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String setlDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String lnchProdInd;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rtrvProdExtnlInd;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodExtnlCatCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pdcyCallCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pdcyKnockInCde;

    public String getProdExtnlCde() {
        return this.prodExtnlCde;
    }

    public void setProdExtnlCde(final String prodExtnlCde) {
        this.prodExtnlCde = prodExtnlCde;
    }

    public String getProdExtnlTypeCde() {
        return this.prodExtnlTypeCde;
    }

    public void setProdExtnlTypeCde(final String prodExtnlTypeCde) {
        this.prodExtnlTypeCde = prodExtnlTypeCde;
    }

    public String getEqtyLinkInvstTypeCde() {
        return this.eqtyLinkInvstTypeCde;
    }

    public void setEqtyLinkInvstTypeCde(final String eqtyLinkInvstTypeCde) {
        this.eqtyLinkInvstTypeCde = eqtyLinkInvstTypeCde;
    }

    public String getTrdDt() {
        return this.trdDt;
    }

    public void setTrdDt(final String trdDt) {
        this.trdDt = trdDt;
    }

    public String getDscntBuyPct() {
        return this.dscntBuyPct;
    }

    public void setDscntBuyPct(final String dscntBuyPct) {
        this.dscntBuyPct = dscntBuyPct;
    }

    public String getDscntSellPct() {
        return this.dscntSellPct;
    }

    public void setDscntSellPct(final String dscntSellPct) {
        this.dscntSellPct = dscntSellPct;
    }

    public String getYieldToMturPct() {
        return this.yieldToMturPct;
    }

    public void setYieldToMturPct(final String yieldToMturPct) {
        this.yieldToMturPct = yieldToMturPct;
    }

    public String getDenAmt() {
        return this.denAmt;
    }

    public void setDenAmt(final String denAmt) {
        this.denAmt = denAmt;
    }

    public String getTrdMinAmt() {
        return this.trdMinAmt;
    }

    public void setTrdMinAmt(final String trdMinAmt) {
        this.trdMinAmt = trdMinAmt;
    }

    public String getSuptAonInd() {
        return this.suptAonInd;
    }

    public void setSuptAonInd(final String suptAonInd) {
        this.suptAonInd = suptAonInd;
    }

    public String getPymtDt() {
        return this.pymtDt;
    }

    public void setPymtDt(final String pymtDt) {
        this.pymtDt = pymtDt;
    }

    public String getValnDt() {
        return this.valnDt;
    }

    public void setValnDt(final String valnDt) {
        this.valnDt = valnDt;
    }

    public String getOfferTypeCde() {
        return this.offerTypeCde;
    }

    public void setOfferTypeCde(final String offerTypeCde) {
        this.offerTypeCde = offerTypeCde;
    }

    public String getCustSellQtaCnt() {
        return this.custSellQtaCnt;
    }

    public void setCustSellQtaCnt(final String custSellQtaCnt) {
        this.custSellQtaCnt = custSellQtaCnt;
    }

    public String getRuleQtaAltmtCde() {
        return this.ruleQtaAltmtCde;
    }

    public void setRuleQtaAltmtCde(final String ruleQtaAltmtCde) {
        this.ruleQtaAltmtCde = ruleQtaAltmtCde;
    }

    public String getSetlDt() {
        return this.setlDt;
    }

    public void setSetlDt(final String setlDt) {
        this.setlDt = setlDt;
    }

    public String getLnchProdInd() {
        return this.lnchProdInd;
    }

    public void setLnchProdInd(final String lnchProdInd) {
        this.lnchProdInd = lnchProdInd;
    }

    public String getRtrvProdExtnlInd() {
        return this.rtrvProdExtnlInd;
    }

    public void setRtrvProdExtnlInd(final String rtrvProdExtnlInd) {
        this.rtrvProdExtnlInd = rtrvProdExtnlInd;
    }

    public String getProdExtnlCatCde() {
        return this.prodExtnlCatCde;
    }

    public void setProdExtnlCatCde(final String prodExtnlCatCde) {
        this.prodExtnlCatCde = prodExtnlCatCde;
    }

    public String getPdcyCallCde() {
        return this.pdcyCallCde;
    }

    public void setPdcyCallCde(final String pdcyCallCde) {
        this.pdcyCallCde = pdcyCallCde;
    }

    public String getPdcyKnockInCde() {
        return this.pdcyKnockInCde;
    }

    public void setPdcyKnockInCde(final String pdcyKnockInCde) {
        this.pdcyKnockInCde = pdcyKnockInCde;
    }

}
