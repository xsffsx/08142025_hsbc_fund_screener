/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/EliSegment.java 1.4.1.1 2012/12/28 10:21:25CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class EliSegment.
 * 
 * @version $Revision: 1.4.1.1 $ $Date: 2012/12/28 10:21:25CST $
 */
public class EliSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodExtnlCde.
     */
    private java.lang.String _prodExtnlCde;

    /**
     * Field _prodExtnlTypeCde.
     */
    private java.lang.String _prodExtnlTypeCde;

    /**
     * Field _eqtyLinkInvstTypeCde.
     */
    private java.lang.String _eqtyLinkInvstTypeCde;

    /**
     * Field _trdDt.
     */
    private java.lang.String _trdDt;

    /**
     * Field _dscntBuyPct.
     */
    private java.lang.String _dscntBuyPct;

    /**
     * Field _dscntSellPct.
     */
    private java.lang.String _dscntSellPct;

    /**
     * Field _yieldToMturPct.
     */
    private java.lang.String _yieldToMturPct;

    /**
     * Field _denAmt.
     */
    private java.lang.String _denAmt;

    /**
     * Field _trdMinAmt.
     */
    private java.lang.String _trdMinAmt;

    /**
     * Field _suptAonInd.
     */
    private java.lang.String _suptAonInd;

    /**
     * Field _pymtDt.
     */
    private java.lang.String _pymtDt;

    /**
     * Field _valnDt.
     */
    private java.lang.String _valnDt;

    /**
     * Field _offerTypeCde.
     */
    private java.lang.String _offerTypeCde;

    /**
     * Field _custSellQtaCnt.
     */
    private java.lang.String _custSellQtaCnt;

    /**
     * Field _ruleQtaAltmtCde.
     */
    private java.lang.String _ruleQtaAltmtCde;

    /**
     * Field _setlDt.
     */
    private java.lang.String _setlDt;

    /**
     * Field _lnchProdInd.
     */
    private java.lang.String _lnchProdInd;

    /**
     * Field _rtrvProdExtnlInd.
     */
    private java.lang.String _rtrvProdExtnlInd;

    /**
     * Field _prodExtnlCatCde.
     */
    private java.lang.String _prodExtnlCatCde;

    /**
     * Field _pdcyCallCde.
     */
    private java.lang.String _pdcyCallCde;

    /**
     * Field _pdcyKnockInCde.
     */
    private java.lang.String _pdcyKnockInCde;

    /**
     * Field _eliUndlStockSegList.
     */
    private java.util.Vector _eliUndlStockSegList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EliSegment() {
        super();
        this._eliUndlStockSegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vEliUndlStockSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEliUndlStockSeg(
            final com.dummy.wpc.batch.xml.EliUndlStockSeg vEliUndlStockSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._eliUndlStockSegList.addElement(vEliUndlStockSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vEliUndlStockSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEliUndlStockSeg(
            final int index,
            final com.dummy.wpc.batch.xml.EliUndlStockSeg vEliUndlStockSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._eliUndlStockSegList.add(index, vEliUndlStockSeg);
    }

    /**
     * Method enumerateEliUndlStockSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.EliUndlStockSeg elements
     */
    public java.util.Enumeration enumerateEliUndlStockSeg(
    ) {
        return this._eliUndlStockSegList.elements();
    }

    /**
     * Returns the value of field 'custSellQtaCnt'.
     * 
     * @return the value of field 'CustSellQtaCnt'.
     */
    public java.lang.String getCustSellQtaCnt(
    ) {
        return this._custSellQtaCnt;
    }

    /**
     * Returns the value of field 'denAmt'.
     * 
     * @return the value of field 'DenAmt'.
     */
    public java.lang.String getDenAmt(
    ) {
        return this._denAmt;
    }

    /**
     * Returns the value of field 'dscntBuyPct'.
     * 
     * @return the value of field 'DscntBuyPct'.
     */
    public java.lang.String getDscntBuyPct(
    ) {
        return this._dscntBuyPct;
    }

    /**
     * Returns the value of field 'dscntSellPct'.
     * 
     * @return the value of field 'DscntSellPct'.
     */
    public java.lang.String getDscntSellPct(
    ) {
        return this._dscntSellPct;
    }

    /**
     * Method getEliUndlStockSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.EliUndlStockSeg at the given index
     */
    public com.dummy.wpc.batch.xml.EliUndlStockSeg getEliUndlStockSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._eliUndlStockSegList.size()) {
            throw new IndexOutOfBoundsException("getEliUndlStockSeg: Index value '" + index + "' not in range [0.." + (this._eliUndlStockSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.EliUndlStockSeg) _eliUndlStockSegList.get(index);
    }

    /**
     * Method getEliUndlStockSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.EliUndlStockSeg[] getEliUndlStockSeg(
    ) {
        com.dummy.wpc.batch.xml.EliUndlStockSeg[] array = new com.dummy.wpc.batch.xml.EliUndlStockSeg[0];
        return (com.dummy.wpc.batch.xml.EliUndlStockSeg[]) this._eliUndlStockSegList.toArray(array);
    }

    /**
     * Method getEliUndlStockSegCount.
     * 
     * @return the size of this collection
     */
    public int getEliUndlStockSegCount(
    ) {
        return this._eliUndlStockSegList.size();
    }

    /**
     * Returns the value of field 'eqtyLinkInvstTypeCde'.
     * 
     * @return the value of field 'EqtyLinkInvstTypeCde'.
     */
    public java.lang.String getEqtyLinkInvstTypeCde(
    ) {
        return this._eqtyLinkInvstTypeCde;
    }

    /**
     * Returns the value of field 'lnchProdInd'.
     * 
     * @return the value of field 'LnchProdInd'.
     */
    public java.lang.String getLnchProdInd(
    ) {
        return this._lnchProdInd;
    }

    /**
     * Returns the value of field 'offerTypeCde'.
     * 
     * @return the value of field 'OfferTypeCde'.
     */
    public java.lang.String getOfferTypeCde(
    ) {
        return this._offerTypeCde;
    }

    /**
     * Returns the value of field 'pdcyCallCde'.
     * 
     * @return the value of field 'PdcyCallCde'.
     */
    public java.lang.String getPdcyCallCde(
    ) {
        return this._pdcyCallCde;
    }

    /**
     * Returns the value of field 'pdcyKnockInCde'.
     * 
     * @return the value of field 'PdcyKnockInCde'.
     */
    public java.lang.String getPdcyKnockInCde(
    ) {
        return this._pdcyKnockInCde;
    }

    /**
     * Returns the value of field 'prodExtnlCatCde'.
     * 
     * @return the value of field 'ProdExtnlCatCde'.
     */
    public java.lang.String getProdExtnlCatCde(
    ) {
        return this._prodExtnlCatCde;
    }

    /**
     * Returns the value of field 'prodExtnlCde'.
     * 
     * @return the value of field 'ProdExtnlCde'.
     */
    public java.lang.String getProdExtnlCde(
    ) {
        return this._prodExtnlCde;
    }

    /**
     * Returns the value of field 'prodExtnlTypeCde'.
     * 
     * @return the value of field 'ProdExtnlTypeCde'.
     */
    public java.lang.String getProdExtnlTypeCde(
    ) {
        return this._prodExtnlTypeCde;
    }

    /**
     * Returns the value of field 'pymtDt'.
     * 
     * @return the value of field 'PymtDt'.
     */
    public java.lang.String getPymtDt(
    ) {
        return this._pymtDt;
    }

    /**
     * Returns the value of field 'rtrvProdExtnlInd'.
     * 
     * @return the value of field 'RtrvProdExtnlInd'.
     */
    public java.lang.String getRtrvProdExtnlInd(
    ) {
        return this._rtrvProdExtnlInd;
    }

    /**
     * Returns the value of field 'ruleQtaAltmtCde'.
     * 
     * @return the value of field 'RuleQtaAltmtCde'.
     */
    public java.lang.String getRuleQtaAltmtCde(
    ) {
        return this._ruleQtaAltmtCde;
    }

    /**
     * Returns the value of field 'setlDt'.
     * 
     * @return the value of field 'SetlDt'.
     */
    public java.lang.String getSetlDt(
    ) {
        return this._setlDt;
    }

    /**
     * Returns the value of field 'suptAonInd'.
     * 
     * @return the value of field 'SuptAonInd'.
     */
    public java.lang.String getSuptAonInd(
    ) {
        return this._suptAonInd;
    }

    /**
     * Returns the value of field 'trdDt'.
     * 
     * @return the value of field 'TrdDt'.
     */
    public java.lang.String getTrdDt(
    ) {
        return this._trdDt;
    }

    /**
     * Returns the value of field 'trdMinAmt'.
     * 
     * @return the value of field 'TrdMinAmt'.
     */
    public java.lang.String getTrdMinAmt(
    ) {
        return this._trdMinAmt;
    }

    /**
     * Returns the value of field 'valnDt'.
     * 
     * @return the value of field 'ValnDt'.
     */
    public java.lang.String getValnDt(
    ) {
        return this._valnDt;
    }

    /**
     * Returns the value of field 'yieldToMturPct'.
     * 
     * @return the value of field 'YieldToMturPct'.
     */
    public java.lang.String getYieldToMturPct(
    ) {
        return this._yieldToMturPct;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllEliUndlStockSeg(
    ) {
        this._eliUndlStockSegList.clear();
    }

    /**
     * Method removeEliUndlStockSeg.
     * 
     * @param vEliUndlStockSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeEliUndlStockSeg(
            final com.dummy.wpc.batch.xml.EliUndlStockSeg vEliUndlStockSeg) {
        boolean removed = _eliUndlStockSegList.remove(vEliUndlStockSeg);
        return removed;
    }

    /**
     * Method removeEliUndlStockSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.EliUndlStockSeg removeEliUndlStockSegAt(
            final int index) {
        java.lang.Object obj = this._eliUndlStockSegList.remove(index);
        return (com.dummy.wpc.batch.xml.EliUndlStockSeg) obj;
    }

    /**
     * Sets the value of field 'custSellQtaCnt'.
     * 
     * @param custSellQtaCnt the value of field 'custSellQtaCnt'.
     */
    public void setCustSellQtaCnt(
            final java.lang.String custSellQtaCnt) {
        this._custSellQtaCnt = custSellQtaCnt;
    }

    /**
     * Sets the value of field 'denAmt'.
     * 
     * @param denAmt the value of field 'denAmt'.
     */
    public void setDenAmt(
            final java.lang.String denAmt) {
        this._denAmt = denAmt;
    }

    /**
     * Sets the value of field 'dscntBuyPct'.
     * 
     * @param dscntBuyPct the value of field 'dscntBuyPct'.
     */
    public void setDscntBuyPct(
            final java.lang.String dscntBuyPct) {
        this._dscntBuyPct = dscntBuyPct;
    }

    /**
     * Sets the value of field 'dscntSellPct'.
     * 
     * @param dscntSellPct the value of field 'dscntSellPct'.
     */
    public void setDscntSellPct(
            final java.lang.String dscntSellPct) {
        this._dscntSellPct = dscntSellPct;
    }

    /**
     * 
     * 
     * @param index
     * @param vEliUndlStockSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setEliUndlStockSeg(
            final int index,
            final com.dummy.wpc.batch.xml.EliUndlStockSeg vEliUndlStockSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._eliUndlStockSegList.size()) {
            throw new IndexOutOfBoundsException("setEliUndlStockSeg: Index value '" + index + "' not in range [0.." + (this._eliUndlStockSegList.size() - 1) + "]");
        }
        
        this._eliUndlStockSegList.set(index, vEliUndlStockSeg);
    }

    /**
     * 
     * 
     * @param vEliUndlStockSegArray
     */
    public void setEliUndlStockSeg(
            final com.dummy.wpc.batch.xml.EliUndlStockSeg[] vEliUndlStockSegArray) {
        //-- copy array
        _eliUndlStockSegList.clear();
        
        for (int i = 0; i < vEliUndlStockSegArray.length; i++) {
                this._eliUndlStockSegList.add(vEliUndlStockSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'eqtyLinkInvstTypeCde'.
     * 
     * @param eqtyLinkInvstTypeCde the value of field
     * 'eqtyLinkInvstTypeCde'.
     */
    public void setEqtyLinkInvstTypeCde(
            final java.lang.String eqtyLinkInvstTypeCde) {
        this._eqtyLinkInvstTypeCde = eqtyLinkInvstTypeCde;
    }

    /**
     * Sets the value of field 'lnchProdInd'.
     * 
     * @param lnchProdInd the value of field 'lnchProdInd'.
     */
    public void setLnchProdInd(
            final java.lang.String lnchProdInd) {
        this._lnchProdInd = lnchProdInd;
    }

    /**
     * Sets the value of field 'offerTypeCde'.
     * 
     * @param offerTypeCde the value of field 'offerTypeCde'.
     */
    public void setOfferTypeCde(
            final java.lang.String offerTypeCde) {
        this._offerTypeCde = offerTypeCde;
    }

    /**
     * Sets the value of field 'pdcyCallCde'.
     * 
     * @param pdcyCallCde the value of field 'pdcyCallCde'.
     */
    public void setPdcyCallCde(
            final java.lang.String pdcyCallCde) {
        this._pdcyCallCde = pdcyCallCde;
    }

    /**
     * Sets the value of field 'pdcyKnockInCde'.
     * 
     * @param pdcyKnockInCde the value of field 'pdcyKnockInCde'.
     */
    public void setPdcyKnockInCde(
            final java.lang.String pdcyKnockInCde) {
        this._pdcyKnockInCde = pdcyKnockInCde;
    }

    /**
     * Sets the value of field 'prodExtnlCatCde'.
     * 
     * @param prodExtnlCatCde the value of field 'prodExtnlCatCde'.
     */
    public void setProdExtnlCatCde(
            final java.lang.String prodExtnlCatCde) {
        this._prodExtnlCatCde = prodExtnlCatCde;
    }

    /**
     * Sets the value of field 'prodExtnlCde'.
     * 
     * @param prodExtnlCde the value of field 'prodExtnlCde'.
     */
    public void setProdExtnlCde(
            final java.lang.String prodExtnlCde) {
        this._prodExtnlCde = prodExtnlCde;
    }

    /**
     * Sets the value of field 'prodExtnlTypeCde'.
     * 
     * @param prodExtnlTypeCde the value of field 'prodExtnlTypeCde'
     */
    public void setProdExtnlTypeCde(
            final java.lang.String prodExtnlTypeCde) {
        this._prodExtnlTypeCde = prodExtnlTypeCde;
    }

    /**
     * Sets the value of field 'pymtDt'.
     * 
     * @param pymtDt the value of field 'pymtDt'.
     */
    public void setPymtDt(
            final java.lang.String pymtDt) {
        this._pymtDt = pymtDt;
    }

    /**
     * Sets the value of field 'rtrvProdExtnlInd'.
     * 
     * @param rtrvProdExtnlInd the value of field 'rtrvProdExtnlInd'
     */
    public void setRtrvProdExtnlInd(
            final java.lang.String rtrvProdExtnlInd) {
        this._rtrvProdExtnlInd = rtrvProdExtnlInd;
    }

    /**
     * Sets the value of field 'ruleQtaAltmtCde'.
     * 
     * @param ruleQtaAltmtCde the value of field 'ruleQtaAltmtCde'.
     */
    public void setRuleQtaAltmtCde(
            final java.lang.String ruleQtaAltmtCde) {
        this._ruleQtaAltmtCde = ruleQtaAltmtCde;
    }

    /**
     * Sets the value of field 'setlDt'.
     * 
     * @param setlDt the value of field 'setlDt'.
     */
    public void setSetlDt(
            final java.lang.String setlDt) {
        this._setlDt = setlDt;
    }

    /**
     * Sets the value of field 'suptAonInd'.
     * 
     * @param suptAonInd the value of field 'suptAonInd'.
     */
    public void setSuptAonInd(
            final java.lang.String suptAonInd) {
        this._suptAonInd = suptAonInd;
    }

    /**
     * Sets the value of field 'trdDt'.
     * 
     * @param trdDt the value of field 'trdDt'.
     */
    public void setTrdDt(
            final java.lang.String trdDt) {
        this._trdDt = trdDt;
    }

    /**
     * Sets the value of field 'trdMinAmt'.
     * 
     * @param trdMinAmt the value of field 'trdMinAmt'.
     */
    public void setTrdMinAmt(
            final java.lang.String trdMinAmt) {
        this._trdMinAmt = trdMinAmt;
    }

    /**
     * Sets the value of field 'valnDt'.
     * 
     * @param valnDt the value of field 'valnDt'.
     */
    public void setValnDt(
            final java.lang.String valnDt) {
        this._valnDt = valnDt;
    }

    /**
     * Sets the value of field 'yieldToMturPct'.
     * 
     * @param yieldToMturPct the value of field 'yieldToMturPct'.
     */
    public void setYieldToMturPct(
            final java.lang.String yieldToMturPct) {
        this._yieldToMturPct = yieldToMturPct;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.EliSegment
     */
    public static com.dummy.wpc.batch.xml.EliSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.EliSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.EliSegment.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
