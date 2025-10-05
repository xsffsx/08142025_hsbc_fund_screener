/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.warrant;

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
 *         &lt;element ref=&quot;{}prodListTypeCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prodListCde&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"prodListTypeCde", "prodListCde"})
@XmlRootElement(name = "prodListSeg")
public class ProdListSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodListTypeCde;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodListCde;

    /**
     * Gets the value of the prodListTypeCde property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getProdListTypeCde() {
        return this.prodListTypeCde;
    }

    /**
     * Sets the value of the prodListTypeCde property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setProdListTypeCde(final String value) {
        this.prodListTypeCde = value;
    }

    /**
     * Gets the value of the prodListCde property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getProdListCde() {
        return this.prodListCde;
    }

    /**
     * Sets the value of the prodListCde property.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setProdListCde(final String value) {
        this.prodListCde = value;
    }

}
