/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.RecDtTmSeg;

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
 *         &lt;element ref=&quot;{}prodKeySeg&quot;/&gt;
 *         &lt;element ref=&quot;{}prodAltNumSeg&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *         &lt;element ref=&quot;{}prodInfoSeg&quot;/&gt;
 *         &lt;element ref=&quot;{}stockInstmSeg&quot;/&gt;
 *         &lt;element ref=&quot;{}recDtTmSeg&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"ctryRecCde", "grpMembrRecCde", "cdvTypeCde", "cdvCde", "cdvDesc", "cdvPllDesc", "cdvSllDesc",
    "cdvDispSeqNum", "cdvParntTypeCde", "cdvParntCde", "recDtTmSeg"})
@XmlRootElement(name = "refData")
public class RefData {

    @XmlElement(required = true)
    protected String ctryRecCde;

    @XmlElement(required = true)
    protected String grpMembrRecCde;

    @XmlElement(required = true)
    protected String cdvTypeCde;

    @XmlElement(required = true)
    protected String cdvCde;

    @XmlElement(required = true)
    protected String cdvDesc;

    @XmlElement(required = true)
    protected String cdvPllDesc;

    @XmlElement(required = true)
    protected String cdvSllDesc;

    @XmlElement(required = true)
    protected String cdvDispSeqNum;

    @XmlElement(required = true)
    protected String cdvParntTypeCde;

    @XmlElement(required = true)
    protected String cdvParntCde;

    @XmlElement(required = true)
    protected RecDtTmSeg recDtTmSeg;

    public String getCtryRecCde() {
        return this.ctryRecCde;
    }

    public void setCtryRecCde(final String ctryRecCde) {
        this.ctryRecCde = ctryRecCde;
    }

    public String getGrpMembrRecCde() {
        return this.grpMembrRecCde;
    }

    public void setGrpMembrRecCde(final String grpMembrRecCde) {
        this.grpMembrRecCde = grpMembrRecCde;
    }

    public String getCdvTypeCde() {
        return this.cdvTypeCde;
    }

    public void setCdvTypeCde(final String cdvTypeCde) {
        this.cdvTypeCde = cdvTypeCde;
    }

    public String getCdvCde() {
        return this.cdvCde;
    }

    public void setCdvCde(final String cdvCde) {
        this.cdvCde = cdvCde;
    }

    public String getCdvDesc() {
        return this.cdvDesc;
    }

    public void setCdvDesc(final String cdvDesc) {
        this.cdvDesc = cdvDesc;
    }

    public String getCdvPllDesc() {
        return this.cdvPllDesc;
    }

    public void setCdvPllDesc(final String cdvPllDesc) {
        this.cdvPllDesc = cdvPllDesc;
    }

    public String getCdvSllDesc() {
        return this.cdvSllDesc;
    }

    public void setCdvSllDesc(final String cdvSllDesc) {
        this.cdvSllDesc = cdvSllDesc;
    }

    public String getCdvDispSeqNum() {
        return this.cdvDispSeqNum;
    }

    public void setCdvDispSeqNum(final String cdvDispSeqNum) {
        this.cdvDispSeqNum = cdvDispSeqNum;
    }

    public String getCdvParntTypeCde() {
        return this.cdvParntTypeCde;
    }

    public void setCdvParntTypeCde(final String cdvParntTypeCde) {
        this.cdvParntTypeCde = cdvParntTypeCde;
    }

    public String getCdvParntCde() {
        return this.cdvParntCde;
    }

    public void setCdvParntCde(final String cdvParntCde) {
        this.cdvParntCde = cdvParntCde;
    }

    public RecDtTmSeg getRecDtTmSeg() {
        return this.recDtTmSeg;
    }

    public void setRecDtTmSeg(final RecDtTmSeg recDtTmSeg) {
        this.recDtTmSeg = recDtTmSeg;
    }

}
