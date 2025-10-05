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
@XmlType(name = "", propOrder = {"geoLocCde", "asetAllocWghtPct"})
@XmlRootElement(name = "asetGeoLocAllocSeg")
@Searchable(root = false)
public class AsetGeoLocAllocSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String geoLocCde;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String asetAllocWghtPct;

    public String getGeoLocCde() {
        return this.geoLocCde;
    }

    public void setGeoLocCde(final String geoLocCde) {
        this.geoLocCde = geoLocCde;
    }

    public String getAsetAllocWghtPct() {
        return this.asetAllocWghtPct;
    }

    public void setAsetAllocWghtPct(final String asetAllocWghtPct) {
        this.asetAllocWghtPct = asetAllocWghtPct;
    }

}
