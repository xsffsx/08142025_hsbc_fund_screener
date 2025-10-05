/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.sid;

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
@XmlType(name = "", propOrder = {"prodExtnlCde", "prodExtnlTypeCde", "prodConvCde", "rtrnIntrmPrevPct", "rtrnIntrmPaidPrevDt",
    "rtrnIntrmPaidNextDt", "ccyLinkDepstCde", "mktStartDt", "mktEndDt", "yieldAnnlMinPct", "yieldAnnlPotenPct", "allowEarlyRdmInd",
    "rdmEarlyDalwText", "rdmEarlyIndAmt", "offerTypeCde", "custSellQtaNum", "ruleQtaAltmtCde", "bonusIntCalcTypeCde",
    "bonusIntDtTypeCde", "cptlProtcPct", "lnchProdInd", "rtrvProdExtnlInd"})
@XmlRootElement(name = "sidSeg")
public class SidSeg {

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
    protected String prodConvCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rtrnIntrmPrevPct;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rtrnIntrmPaidPrevDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rtrnIntrmPaidNextDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ccyLinkDepstCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mktStartDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mktEndDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String yieldAnnlMinPct;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String yieldAnnlPotenPct;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String allowEarlyRdmInd;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rdmEarlyDalwText;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rdmEarlyIndAmt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String offerTypeCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String custSellQtaNum;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ruleQtaAltmtCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String bonusIntCalcTypeCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String bonusIntDtTypeCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String cptlProtcPct;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String lnchProdInd;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rtrvProdExtnlInd;

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

    public String getProdConvCde() {
        return this.prodConvCde;
    }

    public void setProdConvCde(final String prodConvCde) {
        this.prodConvCde = prodConvCde;
    }

    public String getRtrnIntrmPrevPct() {
        return this.rtrnIntrmPrevPct;
    }

    public void setRtrnIntrmPrevPct(final String rtrnIntrmPrevPct) {
        this.rtrnIntrmPrevPct = rtrnIntrmPrevPct;
    }

    public String getRtrnIntrmPaidPrevDt() {
        return this.rtrnIntrmPaidPrevDt;
    }

    public void setRtrnIntrmPaidPrevDt(final String rtrnIntrmPaidPrevDt) {
        this.rtrnIntrmPaidPrevDt = rtrnIntrmPaidPrevDt;
    }

    public String getRtrnIntrmPaidNextDt() {
        return this.rtrnIntrmPaidNextDt;
    }

    public void setRtrnIntrmPaidNextDt(final String rtrnIntrmPaidNextDt) {
        this.rtrnIntrmPaidNextDt = rtrnIntrmPaidNextDt;
    }

    public String getCcyLinkDepstCde() {
        return this.ccyLinkDepstCde;
    }

    public void setCcyLinkDepstCde(final String ccyLinkDepstCde) {
        this.ccyLinkDepstCde = ccyLinkDepstCde;
    }

    public String getMktStartDt() {
        return this.mktStartDt;
    }

    public void setMktStartDt(final String mktStartDt) {
        this.mktStartDt = mktStartDt;
    }

    public String getMktEndDt() {
        return this.mktEndDt;
    }

    public void setMktEndDt(final String mktEndDt) {
        this.mktEndDt = mktEndDt;
    }

    public String getYieldAnnlMinPct() {
        return this.yieldAnnlMinPct;
    }

    public void setYieldAnnlMinPct(final String yieldAnnlMinPct) {
        this.yieldAnnlMinPct = yieldAnnlMinPct;
    }

    public String getYieldAnnlPotenPct() {
        return this.yieldAnnlPotenPct;
    }

    public void setYieldAnnlPotenPct(final String yieldAnnlPotenPct) {
        this.yieldAnnlPotenPct = yieldAnnlPotenPct;
    }

    public String getAllowEarlyRdmInd() {
        return this.allowEarlyRdmInd;
    }

    public void setAllowEarlyRdmInd(final String allowEarlyRdmInd) {
        this.allowEarlyRdmInd = allowEarlyRdmInd;
    }

    public String getRdmEarlyDalwText() {
        return this.rdmEarlyDalwText;
    }

    public void setRdmEarlyDalwText(final String rdmEarlyDalwText) {
        this.rdmEarlyDalwText = rdmEarlyDalwText;
    }

    public String getRdmEarlyIndAmt() {
        return this.rdmEarlyIndAmt;
    }

    public void setRdmEarlyIndAmt(final String rdmEarlyIndAmt) {
        this.rdmEarlyIndAmt = rdmEarlyIndAmt;
    }

    public String getOfferTypeCde() {
        return this.offerTypeCde;
    }

    public void setOfferTypeCde(final String offerTypeCde) {
        this.offerTypeCde = offerTypeCde;
    }

    public String getCustSellQtaNum() {
        return this.custSellQtaNum;
    }

    public void setCustSellQtaNum(final String custSellQtaNum) {
        this.custSellQtaNum = custSellQtaNum;
    }

    public String getRuleQtaAltmtCde() {
        return this.ruleQtaAltmtCde;
    }

    public void setRuleQtaAltmtCde(final String ruleQtaAltmtCde) {
        this.ruleQtaAltmtCde = ruleQtaAltmtCde;
    }

    public String getBonusIntCalcTypeCde() {
        return this.bonusIntCalcTypeCde;
    }

    public void setBonusIntCalcTypeCde(final String bonusIntCalcTypeCde) {
        this.bonusIntCalcTypeCde = bonusIntCalcTypeCde;
    }

    public String getBonusIntDtTypeCde() {
        return this.bonusIntDtTypeCde;
    }

    public void setBonusIntDtTypeCde(final String bonusIntDtTypeCde) {
        this.bonusIntDtTypeCde = bonusIntDtTypeCde;
    }

    public String getCptlProtcPct() {
        return this.cptlProtcPct;
    }

    public void setCptlProtcPct(final String cptlProtcPct) {
        this.cptlProtcPct = cptlProtcPct;
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

}
