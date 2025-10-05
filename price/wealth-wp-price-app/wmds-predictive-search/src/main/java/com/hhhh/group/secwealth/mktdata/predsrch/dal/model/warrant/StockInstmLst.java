/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.warrant;

import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element ref=&quot;{}stockInstm&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"stockInstm"})
@XmlRootElement(name = "stockInstmLst")
public class StockInstmLst {

    @XmlElement(required = true)
    protected List<StockInstm> stockInstm;

    /**
     * Gets the value of the stockInstm property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the stockInstm property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getStockInstm().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StockInstm }
     *
     *
     */
    public List<StockInstm> getStockInstm() {
        if (this.stockInstm == null) {
            this.stockInstm = new ArrayList<StockInstm>();
        }
        return this.stockInstm;
    }

}
