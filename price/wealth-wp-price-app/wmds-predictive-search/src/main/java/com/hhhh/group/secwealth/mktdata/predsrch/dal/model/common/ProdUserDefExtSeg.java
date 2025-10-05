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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"fieldCde", "fieldValue"})
@XmlRootElement(name = "prodAsetVoltlClassSeg")
@Searchable(root = false)
public class ProdUserDefExtSeg {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String fieldCde;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    @SearchableProperty(index = Index.ANALYZED, store = Store.YES)
    protected String fieldValue;

    /**
     * @return the fieldCde
     */
    public String getFieldCde() {
        return this.fieldCde;
    }

    /**
     * @param fieldCde
     *            the fieldCde to set
     */
    public void setFieldCde(final String fieldCde) {
        this.fieldCde = fieldCde;
    }

    /**
     * @return the fieldValue
     */
    public String getFieldValue() {
        return this.fieldValue;
    }

    /**
     * @param fieldValue
     *            the fieldValue to set
     */
    public void setFieldValue(final String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
