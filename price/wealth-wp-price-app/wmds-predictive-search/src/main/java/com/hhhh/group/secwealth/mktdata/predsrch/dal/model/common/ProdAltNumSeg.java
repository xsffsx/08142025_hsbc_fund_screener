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

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

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
 *         &lt;element ref=&quot;{}prodCdeAltClassCde&quot;/&gt;
 *         &lt;element ref=&quot;{}prodAltNum&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"prodCdeAltClassCde", "prodAltNum"})
@XmlRootElement(name = "prodAltNumSeg")
@Searchable(root = false)
public class ProdAltNumSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String prodCdeAltClassCde;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    @SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES)
    protected String prodAltNum;

    /**
     * Gets the value of the prodCdeAltClassCde property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdCdeAltClassCde() {
        return this.prodCdeAltClassCde;
    }

    /**
     * Sets the value of the prodCdeAltClassCde property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdCdeAltClassCde(final String value) {
        this.prodCdeAltClassCde = value;
    }

    /**
     * Gets the value of the prodAltNum property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProdAltNum() {
        return this.prodAltNum;
    }

    /**
     * Sets the value of the prodAltNum property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProdAltNum(final String value) {
        this.prodAltNum = value;
    }

}
