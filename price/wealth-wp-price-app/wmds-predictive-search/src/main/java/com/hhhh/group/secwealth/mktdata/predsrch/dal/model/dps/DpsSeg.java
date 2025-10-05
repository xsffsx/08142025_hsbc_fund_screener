/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.dps;

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
@XmlType(name = "", propOrder = {"prodExtnlCde", "depstPlusTypeCde", "prodExtnlSuffNum", "ccyLinkDepstCde", "tailrMadeDepstInd",
    "knockTypeCde", "exchgRateFixDt", "factrConvRate", "intRate", "exchgSpotRate", "exchgInitRate", "exchgBreakEvenRate",
    "obsrvStartDt", "obsrvEndDt", "exchgTrigRate", "rtnOptCde", "ccyFromCde", "ccyToCde"})
@XmlRootElement(name = "dpsSeg")
public class DpsSeg {

    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodExtnlCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String depstPlusTypeCde;

    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodExtnlSuffNum;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ccyLinkDepstCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String tailrMadeDepstInd;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String knockTypeCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String exchgRateFixDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String factrConvRate;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String intRate;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String exchgSpotRate;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String exchgInitRate;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String exchgBreakEvenRate;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String obsrvStartDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String obsrvEndDt;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String exchgTrigRate;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String rtnOptCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ccyFromCde;
    @XmlElement(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ccyToCde;

    public String getProdExtnlCde() {
        return this.prodExtnlCde;
    }

    public void setProdExtnlCde(final String prodExtnlCde) {
        this.prodExtnlCde = prodExtnlCde;
    }

    public String getDepstPlusTypeCde() {
        return this.depstPlusTypeCde;
    }

    public void setDepstPlusTypeCde(final String depstPlusTypeCde) {
        this.depstPlusTypeCde = depstPlusTypeCde;
    }

    public String getProdExtnlSuffNum() {
        return this.prodExtnlSuffNum;
    }

    public void setProdExtnlSuffNum(final String prodExtnlSuffNum) {
        this.prodExtnlSuffNum = prodExtnlSuffNum;
    }

    public String getCcyLinkDepstCde() {
        return this.ccyLinkDepstCde;
    }

    public void setCcyLinkDepstCde(final String ccyLinkDepstCde) {
        this.ccyLinkDepstCde = ccyLinkDepstCde;
    }

    public String getTailrMadeDepstInd() {
        return this.tailrMadeDepstInd;
    }

    public void setTailrMadeDepstInd(final String tailrMadeDepstInd) {
        this.tailrMadeDepstInd = tailrMadeDepstInd;
    }

    public String getKnockTypeCde() {
        return this.knockTypeCde;
    }

    public void setKnockTypeCde(final String knockTypeCde) {
        this.knockTypeCde = knockTypeCde;
    }

    public String getExchgRateFixDt() {
        return this.exchgRateFixDt;
    }

    public void setExchgRateFixDt(final String exchgRateFixDt) {
        this.exchgRateFixDt = exchgRateFixDt;
    }

    public String getFactrConvRate() {
        return this.factrConvRate;
    }

    public void setFactrConvRate(final String factrConvRate) {
        this.factrConvRate = factrConvRate;
    }

    public String getIntRate() {
        return this.intRate;
    }

    public void setIntRate(final String intRate) {
        this.intRate = intRate;
    }

    public String getExchgSpotRate() {
        return this.exchgSpotRate;
    }

    public void setExchgSpotRate(final String exchgSpotRate) {
        this.exchgSpotRate = exchgSpotRate;
    }

    public String getExchgInitRate() {
        return this.exchgInitRate;
    }

    public void setExchgInitRate(final String exchgInitRate) {
        this.exchgInitRate = exchgInitRate;
    }

    public String getExchgBreakEvenRate() {
        return this.exchgBreakEvenRate;
    }

    public void setExchgBreakEvenRate(final String exchgBreakEvenRate) {
        this.exchgBreakEvenRate = exchgBreakEvenRate;
    }

    public String getObsrvStartDt() {
        return this.obsrvStartDt;
    }

    public void setObsrvStartDt(final String obsrvStartDt) {
        this.obsrvStartDt = obsrvStartDt;
    }

    public String getObsrvEndDt() {
        return this.obsrvEndDt;
    }

    public void setObsrvEndDt(final String obsrvEndDt) {
        this.obsrvEndDt = obsrvEndDt;
    }

    public String getExchgTrigRate() {
        return this.exchgTrigRate;
    }

    public void setExchgTrigRate(final String exchgTrigRate) {
        this.exchgTrigRate = exchgTrigRate;
    }

    public String getRtnOptCde() {
        return this.rtnOptCde;
    }

    public void setRtnOptCde(final String rtnOptCde) {
        this.rtnOptCde = rtnOptCde;
    }

    public String getCcyFromCde() {
        return this.ccyFromCde;
    }

    public void setCcyFromCde(final String ccyFromCde) {
        this.ccyFromCde = ccyFromCde;
    }

    public String getCcyToCde() {
        return this.ccyToCde;
    }

    public void setCcyToCde(final String ccyToCde) {
        this.ccyToCde = ccyToCde;
    }

}
