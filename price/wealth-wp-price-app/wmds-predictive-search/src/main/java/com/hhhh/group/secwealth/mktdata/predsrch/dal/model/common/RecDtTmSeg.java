/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element ref=&quot;{}recCreatDtTm&quot;/&gt;
 *         &lt;element ref=&quot;{}recUpdtDtTm&quot;/&gt;
 *         &lt;element ref=&quot;{}prodPrcUpdtDtTm&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}recOnlnUpdtDtTm&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{}prodStatUpdtDtTm&quot;/&gt;
 *         &lt;element ref=&quot;{}timeZone&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"recCreatDtTm", "recUpdtDtTm", "prodPrcUpdtDtTm", "recOnlnUpdtDtTm", "prodStatUpdtDtTm",
    "timeZone"})
@XmlRootElement(name = "recDtTmSeg")
public class RecDtTmSeg {

    @XmlElement(required = true)
    protected String recCreatDtTm;
    @XmlElement(required = true)
    protected String recUpdtDtTm;
    protected String prodPrcUpdtDtTm;
    protected String recOnlnUpdtDtTm;
    @XmlElement(required = true)
    protected String prodStatUpdtDtTm;
    @XmlElement(required = true)
    protected String timeZone;

    /**
     * Gets the value of the recCreatDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRecCreatDtTm() {
        return this.recCreatDtTm;
    }

    /**
     * Sets the value of the recCreatDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRecCreatDtTm(final String value) {
        this.recCreatDtTm = value;
    }

    /**
     * Gets the value of the recUpdtDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRecUpdtDtTm() {
        return this.recUpdtDtTm;
    }

    /**
     * Sets the value of the recUpdtDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRecUpdtDtTm(final String value) {
        this.recUpdtDtTm = value;
    }

    /**
     * Gets the value of the prodPrcUpdtDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdPrcUpdtDtTm() {
        return this.prodPrcUpdtDtTm;
    }

    /**
     * Sets the value of the prodPrcUpdtDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdPrcUpdtDtTm(final String value) {
        this.prodPrcUpdtDtTm = value;
    }

    /**
     * Gets the value of the recOnlnUpdtDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRecOnlnUpdtDtTm() {
        return this.recOnlnUpdtDtTm;
    }

    /**
     * Sets the value of the recOnlnUpdtDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRecOnlnUpdtDtTm(final String value) {
        this.recOnlnUpdtDtTm = value;
    }

    /**
     * Gets the value of the prodStatUpdtDtTm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdStatUpdtDtTm() {
        return this.prodStatUpdtDtTm;
    }

    /**
     * Sets the value of the prodStatUpdtDtTm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdStatUpdtDtTm(final String value) {
        this.prodStatUpdtDtTm = value;
    }

    /**
     * Gets the value of the timeZone property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTimeZone(final String value) {
        this.timeZone = value;
    }

}
