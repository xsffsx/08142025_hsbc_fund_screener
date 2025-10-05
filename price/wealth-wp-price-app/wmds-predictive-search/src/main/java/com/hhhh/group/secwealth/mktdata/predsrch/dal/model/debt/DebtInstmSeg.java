/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.debt;

import java.math.BigDecimal;
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
@XmlType(name = "", propOrder = {"isrBndNme", "pdcyCoupnPymtCd", "coupnAnnlRt", "flexMatOptInd", "intIndAccrAmt", "invstIncMinAmt",
    "subDebtInd"})
@XmlRootElement(name = "debtInstmSeg")
public class DebtInstmSeg {

    protected String isrBndNme;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String pdcyCoupnPymtCd;
    protected BigDecimal coupnAnnlRt;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String flexMatOptInd;
    @XmlElement(name = "IntIndAccrAmt")
    protected BigInteger intIndAccrAmt;
    @XmlElement(name = "InvstIncMinAmt")
    protected BigInteger invstIncMinAmt;
    @XmlElement(name = "SubDebtInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String subDebtInd;

    /**
     * Gets the value of the isrBndNme property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIsrBndNme() {
        return this.isrBndNme;
    }

    /**
     * Sets the value of the isrBndNme property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIsrBndNme(final String value) {
        this.isrBndNme = value;
    }

    /**
     * Gets the value of the pdcyCoupnPymtCd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPdcyCoupnPymtCd() {
        return this.pdcyCoupnPymtCd;
    }

    /**
     * Sets the value of the pdcyCoupnPymtCd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPdcyCoupnPymtCd(final String value) {
        this.pdcyCoupnPymtCd = value;
    }

    /**
     * Gets the value of the coupnAnnlRt property.
     * 
     * @return possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getCoupnAnnlRt() {
        return this.coupnAnnlRt;
    }

    /**
     * Sets the value of the coupnAnnlRt property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setCoupnAnnlRt(final BigDecimal value) {
        this.coupnAnnlRt = value;
    }

    /**
     * Gets the value of the flexMatOptInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFlexMatOptInd() {
        return this.flexMatOptInd;
    }

    /**
     * Sets the value of the flexMatOptInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFlexMatOptInd(final String value) {
        this.flexMatOptInd = value;
    }

    /**
     * Gets the value of the intIndAccrAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getIntIndAccrAmt() {
        return this.intIndAccrAmt;
    }

    /**
     * Sets the value of the intIndAccrAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setIntIndAccrAmt(final BigInteger value) {
        this.intIndAccrAmt = value;
    }

    /**
     * Gets the value of the invstIncMinAmt property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getInvstIncMinAmt() {
        return this.invstIncMinAmt;
    }

    /**
     * Sets the value of the invstIncMinAmt property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setInvstIncMinAmt(final BigInteger value) {
        this.invstIncMinAmt = value;
    }

    /**
     * Gets the value of the subDebtInd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSubDebtInd() {
        return this.subDebtInd;
    }

    /**
     * Sets the value of the subDebtInd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSubDebtInd(final String value) {
        this.subDebtInd = value;
    }

}
