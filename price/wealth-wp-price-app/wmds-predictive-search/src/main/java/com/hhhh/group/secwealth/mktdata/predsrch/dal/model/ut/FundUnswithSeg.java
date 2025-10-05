/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut;

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
 *         &lt;element ref=&quot;{}fundUnswCde&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"fundUnswCde"})
@XmlRootElement(name = "fundUnswithSeg")
public class FundUnswithSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String fundUnswCde;

    /**
     * Gets the value of the fundUnswCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFundUnswCde() {
        return this.fundUnswCde;
    }

    /**
     * Sets the value of the fundUnswCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFundUnswCde(final String value) {
        this.fundUnswCde = value;
    }

}
