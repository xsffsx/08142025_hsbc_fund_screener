/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common;

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
 *         &lt;element ref=&quot;{}asetUndlCdeSeqNum&quot;/&gt;
 *         &lt;element ref=&quot;{}asetUndlCde&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"asetUndlCdeSeqNum", "asetUndlCde"})
@XmlRootElement(name = "prodAsetUndlSeg")
public class ProdAsetUndlSeg {

    @XmlElement(required = true)
    protected BigInteger asetUndlCdeSeqNum;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String asetUndlCde;

    /**
     * Gets the value of the asetUndlCdeSeqNum property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getAsetUndlCdeSeqNum() {
        return this.asetUndlCdeSeqNum;
    }

    /**
     * Sets the value of the asetUndlCdeSeqNum property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setAsetUndlCdeSeqNum(final BigInteger value) {
        this.asetUndlCdeSeqNum = value;
    }

    /**
     * Gets the value of the asetUndlCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAsetUndlCde() {
        return this.asetUndlCde;
    }

    /**
     * Sets the value of the asetUndlCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAsetUndlCde(final String value) {
        this.asetUndlCde = value;
    }

}
