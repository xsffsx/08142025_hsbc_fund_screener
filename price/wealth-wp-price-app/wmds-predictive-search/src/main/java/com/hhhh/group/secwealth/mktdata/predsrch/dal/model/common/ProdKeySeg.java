/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;

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
 *         &lt;element ref=&quot;{}ctryRecCde&quot;/&gt;
 *         &lt;element ref=&quot;{}grpMembrRecCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prodTypeCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prodCde&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "ctryRecCde", "grpMembrRecCde", "prodTypeCde", "prodCde"})
@XmlRootElement(name = "prodKeySeg")
@Searchable(root = false)
public class ProdKeySeg {

    @XmlElement(required = false)
    @SearchableId
    private int id;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String ctryRecCde;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String grpMembrRecCde;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodTypeCde;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String prodCde;

    /**
     * Gets the value of the ctryRecCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCtryRecCde() {
        return this.ctryRecCde;
    }

    /**
     * Sets the value of the ctryRecCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCtryRecCde(final String value) {
        this.ctryRecCde = value;
    }

    /**
     * Gets the value of the grpMembrRecCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGrpMembrRecCde() {
        return this.grpMembrRecCde;
    }

    /**
     * Sets the value of the grpMembrRecCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGrpMembrRecCde(final String value) {
        this.grpMembrRecCde = value;
    }

    /**
     * Gets the value of the prodTypeCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdTypeCde() {
        return this.prodTypeCde;
    }

    /**
     * Sets the value of the prodTypeCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdTypeCde(final String value) {
        this.prodTypeCde = value;
    }

    public String getProdCde() {
        return this.prodCde;
    }

    public void setProdCde(final String prodCde) {
        this.prodCde = prodCde;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

}
