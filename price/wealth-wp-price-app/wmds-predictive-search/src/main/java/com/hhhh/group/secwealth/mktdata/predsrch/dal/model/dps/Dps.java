/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.dps;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableIdComponent;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdInfoSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdKeySeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.RecDtTmSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.debt.DebtInstmSeg;

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
 *         &lt;element ref=&quot;{}debtInstmSeg&quot;/&gt;
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
@XmlType(name = "", propOrder = {"prodKeySeg", "prodAltNumSeg", "prodInfoSeg", "dpsSeg", "recDtTmSeg"})
@XmlRootElement(name = "dps")
@Searchable
public class Dps {

    @XmlElement(required = true)
    @SearchableIdComponent
    protected ProdKeySeg prodKeySeg;

    @XmlElement(required = true)
    @SearchableComponent
    protected List<ProdAltNumSeg> prodAltNumSeg;

    @XmlElement(required = true)
    @SearchableComponent
    protected ProdInfoSeg prodInfoSeg;

    @XmlElement(required = true)
    protected DpsSeg dpsSeg;

    @XmlElement(required = true)
    protected RecDtTmSeg recDtTmSeg;

    /**
     * Gets the value of the prodKeySeg property.
     * 
     * @return possible object is {@link ProdKeySeg }
     * 
     */
    public ProdKeySeg getProdKeySeg() {
        return this.prodKeySeg;
    }

    /**
     * Sets the value of the prodKeySeg property.
     * 
     * @param value
     *            allowed object is {@link ProdKeySeg }
     * 
     */
    public void setProdKeySeg(final ProdKeySeg value) {
        this.prodKeySeg = value;
    }

    /**
     * Gets the value of the prodAltNumSeg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the prodAltNumSeg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getProdAltNumSeg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProdAltNumSeg }
     * 
     * 
     */
    public List<ProdAltNumSeg> getProdAltNumSeg() {
        if (this.prodAltNumSeg == null) {
            this.prodAltNumSeg = new ArrayList<ProdAltNumSeg>();
        }
        return this.prodAltNumSeg;
    }

    /**
     * Gets the value of the prodInfoSeg property.
     * 
     * @return possible object is {@link ProdInfoSeg }
     * 
     */
    public ProdInfoSeg getProdInfoSeg() {
        return this.prodInfoSeg;
    }

    /**
     * Sets the value of the prodInfoSeg property.
     * 
     * @param value
     *            allowed object is {@link ProdInfoSeg }
     * 
     */
    public void setProdInfoSeg(final ProdInfoSeg value) {
        this.prodInfoSeg = value;
    }

    /**
     * Gets the value of the debtInstmSeg property.
     * 
     * @return possible object is {@link DebtInstmSeg }
     * 
     */
    public DpsSeg getDpsSeg() {
        return this.dpsSeg;
    }

    /**
     * Sets the value of the debtInstmSeg property.
     * 
     * @param value
     *            allowed object is {@link DebtInstmSeg }
     * 
     */
    public void setDpsSeg(final DpsSeg value) {
        this.dpsSeg = value;
    }

    /**
     * Gets the value of the recDtTmSeg property.
     * 
     * @return possible object is {@link RecDtTmSeg }
     * 
     */
    public RecDtTmSeg getRecDtTmSeg() {
        return this.recDtTmSeg;
    }

    /**
     * Sets the value of the recDtTmSeg property.
     * 
     * @param value
     *            allowed object is {@link RecDtTmSeg }
     * 
     */
    public void setRecDtTmSeg(final RecDtTmSeg value) {
        this.recDtTmSeg = value;
    }

    public void setProdAltNumSeg(final List<ProdAltNumSeg> prodAltNumSeg) {
        this.prodAltNumSeg = prodAltNumSeg;
    }

}
