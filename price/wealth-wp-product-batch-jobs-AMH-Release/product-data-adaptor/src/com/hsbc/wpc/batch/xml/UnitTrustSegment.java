/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/UnitTrustSegment.java 1.4.1.2 2013/01/15 10:33:33CST CHRIS CUI (43601081) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class UnitTrustSegment.
 * 
 * @version $Revision: 1.4.1.2 $ $Date: 2013/01/15 10:33:33CST $
 */
public class UnitTrustSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fundHouseCde.
     */
    private java.lang.String _fundHouseCde;

    /**
     * Field _closeEndFundInd.
     */
    private java.lang.String _closeEndFundInd;

    /**
     * Field _utPrcProdLnchAmt.
     */
    private java.lang.String _utPrcProdLnchAmt;

    /**
     * Field _fundAmt.
     */
    private java.lang.String _fundAmt;

    /**
     * Field _invstIncrmMinAmt.
     */
    private java.lang.String _invstIncrmMinAmt;

    /**
     * Field _utRdmMinNum.
     */
    private java.lang.String _utRdmMinNum;

    /**
     * Field _rdmMinAmt.
     */
    private java.lang.String _rdmMinAmt;

    /**
     * Field _utRtainMinNum.
     */
    private java.lang.String _utRtainMinNum;

    /**
     * Field _fundRtainMinAmt.
     */
    private java.lang.String _fundRtainMinAmt;

    /**
     * Field _suptMipInd.
     */
    private java.lang.String _suptMipInd;

    /**
     * Field _invstMipMinAmt.
     */
    private java.lang.String _invstMipMinAmt;

    /**
     * Field _invstMipIncrmMinAmt.
     */
    private java.lang.String _invstMipIncrmMinAmt;

    /**
     * Field _utMipRdmMinNum.
     */
    private java.lang.String _utMipRdmMinNum;

    /**
     * Field _rdmMipMinAmt.
     */
    private java.lang.String _rdmMipMinAmt;

    /**
     * Field _utMipRtainMinNum.
     */
    private java.lang.String _utMipRtainMinNum;

    /**
     * Field _fundMipRtainMinAmt.
     */
    private java.lang.String _fundMipRtainMinAmt;

    /**
     * Field _rtrnGurntPct.
     */
    private java.lang.String _rtrnGurntPct;

    /**
     * Field _schemChrgCde.
     */
    private java.lang.String _schemChrgCde;

    /**
     * Field _chrgInitSalesPct.
     */
    private java.lang.String _chrgInitSalesPct;

    /**
     * Field _chrgFundSwPct.
     */
    private java.lang.String _chrgFundSwPct;

    /**
     * Field _dscntMaxPct.
     */
    private java.lang.String _dscntMaxPct;

    /**
     * Field _offerStartDtTm.
     */
    private java.lang.String _offerStartDtTm;

    /**
     * Field _offerEndDtTm.
     */
    private java.lang.String _offerEndDtTm;

    /**
     * Field _payCashDivInd.
     */
    private java.lang.String _payCashDivInd;

    /**
     * Field _hldayFundNextDt.
     */
    private java.lang.String _hldayFundNextDt;

    /**
     * Field _scribCtoffNextDtTm.
     */
    private java.lang.String _scribCtoffNextDtTm;

    /**
     * Field _rdmCtoffNextDtTm.
     */
    private java.lang.String _rdmCtoffNextDtTm;

    /**
     * Field _dealNextDt.
     */
    private java.lang.String _dealNextDt;

    /**
     * Field _fundClassCde.
     */
    private java.lang.String _fundClassCde;

    /**
     * Field _amcmInd.
     */
    private java.lang.String _amcmInd;

    /**
     * Field _divDclrDt.
     */
    private java.lang.String _divDclrDt;

    /**
     * Field _divPymtDt.
     */
    private java.lang.String _divPymtDt;

    /**
     * Field _autoSweepFundInd.
     */
    private java.lang.String _autoSweepFundInd;

    /**
     * Field _spclFundInd.
     */
    private java.lang.String _spclFundInd;

    /**
     * Field _insuLinkUtTrstInd.
     */
    private java.lang.String _insuLinkUtTrstInd;

    /**
     * Field _prodTrmtDt.
     */
    private java.lang.String _prodTrmtDt;

    /**
     * Field _fundSwInMinAmt.
     */
    private java.lang.String _fundSwInMinAmt;

    /**
     * Field _fundSwOutMinAmt.
     */
    private java.lang.String _fundSwOutMinAmt;

    /**
     * Field _fundSwOutRtainMinAmt.
     */
    private java.lang.String _fundSwOutRtainMinAmt;

    /**
     * Field _utSwOutRtainMinNum.
     */
    private java.lang.String _utSwOutRtainMinNum;

    /**
     * Field _fundSwOutMaxAmt.
     */
    private java.lang.String _fundSwOutMaxAmt;

    /**
     * Field _tranMaxAmt.
     */
    private java.lang.String _tranMaxAmt;

    /**
     * Field _incmHandlOptCde.
     */
    private java.lang.String _incmHandlOptCde;

    /**
     * Field _autoRenewFundInd.
     */
    private java.lang.String _autoRenewFundInd;

    /**
     * Field _fundValnTm.
     */
    private java.lang.String _fundValnTm;

    /**
     * Field _prodShoreLocCde.
     */
    private java.lang.String _prodShoreLocCde;

    /**
     * Field _divYieldPct.
     */
    private java.lang.String _divYieldPct;

    /**
     * Field _fundUnswithSegList.
     */
    private java.util.Vector _fundUnswithSegList;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnitTrustSegment() {
        super();
        this._fundUnswithSegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vFundUnswithSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addFundUnswithSeg(
            final com.dummy.wpc.batch.xml.FundUnswithSeg vFundUnswithSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._fundUnswithSegList.addElement(vFundUnswithSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vFundUnswithSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addFundUnswithSeg(
            final int index,
            final com.dummy.wpc.batch.xml.FundUnswithSeg vFundUnswithSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._fundUnswithSegList.add(index, vFundUnswithSeg);
    }

    /**
     * Method enumerateFundUnswithSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.FundUnswithSeg elements
     */
    public java.util.Enumeration enumerateFundUnswithSeg(
    ) {
        return this._fundUnswithSegList.elements();
    }

    /**
     * Returns the value of field 'amcmInd'.
     * 
     * @return the value of field 'AmcmInd'.
     */
    public java.lang.String getAmcmInd(
    ) {
        return this._amcmInd;
    }

    /**
     * Returns the value of field 'autoRenewFundInd'.
     * 
     * @return the value of field 'AutoRenewFundInd'.
     */
    public java.lang.String getAutoRenewFundInd(
    ) {
        return this._autoRenewFundInd;
    }

    /**
     * Returns the value of field 'autoSweepFundInd'.
     * 
     * @return the value of field 'AutoSweepFundInd'.
     */
    public java.lang.String getAutoSweepFundInd(
    ) {
        return this._autoSweepFundInd;
    }

    /**
     * Returns the value of field 'chrgFundSwPct'.
     * 
     * @return the value of field 'ChrgFundSwPct'.
     */
    public java.lang.String getChrgFundSwPct(
    ) {
        return this._chrgFundSwPct;
    }

    /**
     * Returns the value of field 'chrgInitSalesPct'.
     * 
     * @return the value of field 'ChrgInitSalesPct'.
     */
    public java.lang.String getChrgInitSalesPct(
    ) {
        return this._chrgInitSalesPct;
    }

    /**
     * Returns the value of field 'closeEndFundInd'.
     * 
     * @return the value of field 'CloseEndFundInd'.
     */
    public java.lang.String getCloseEndFundInd(
    ) {
        return this._closeEndFundInd;
    }

    /**
     * Returns the value of field 'dealNextDt'.
     * 
     * @return the value of field 'DealNextDt'.
     */
    public java.lang.String getDealNextDt(
    ) {
        return this._dealNextDt;
    }

    /**
     * Returns the value of field 'divDclrDt'.
     * 
     * @return the value of field 'DivDclrDt'.
     */
    public java.lang.String getDivDclrDt(
    ) {
        return this._divDclrDt;
    }

    /**
     * Returns the value of field 'divPymtDt'.
     * 
     * @return the value of field 'DivPymtDt'.
     */
    public java.lang.String getDivPymtDt(
    ) {
        return this._divPymtDt;
    }

    /**
     * Returns the value of field 'divYieldPct'.
     * 
     * @return the value of field 'DivYieldPct'.
     */
    public java.lang.String getDivYieldPct(
    ) {
        return this._divYieldPct;
    }

    /**
     * Returns the value of field 'dscntMaxPct'.
     * 
     * @return the value of field 'DscntMaxPct'.
     */
    public java.lang.String getDscntMaxPct(
    ) {
        return this._dscntMaxPct;
    }

    /**
     * Returns the value of field 'fundAmt'.
     * 
     * @return the value of field 'FundAmt'.
     */
    public java.lang.String getFundAmt(
    ) {
        return this._fundAmt;
    }

    /**
     * Returns the value of field 'fundClassCde'.
     * 
     * @return the value of field 'FundClassCde'.
     */
    public java.lang.String getFundClassCde(
    ) {
        return this._fundClassCde;
    }

    /**
     * Returns the value of field 'fundHouseCde'.
     * 
     * @return the value of field 'FundHouseCde'.
     */
    public java.lang.String getFundHouseCde(
    ) {
        return this._fundHouseCde;
    }

    /**
     * Returns the value of field 'fundMipRtainMinAmt'.
     * 
     * @return the value of field 'FundMipRtainMinAmt'.
     */
    public java.lang.String getFundMipRtainMinAmt(
    ) {
        return this._fundMipRtainMinAmt;
    }

    /**
     * Returns the value of field 'fundRtainMinAmt'.
     * 
     * @return the value of field 'FundRtainMinAmt'.
     */
    public java.lang.String getFundRtainMinAmt(
    ) {
        return this._fundRtainMinAmt;
    }

    /**
     * Returns the value of field 'fundSwInMinAmt'.
     * 
     * @return the value of field 'FundSwInMinAmt'.
     */
    public java.lang.String getFundSwInMinAmt(
    ) {
        return this._fundSwInMinAmt;
    }

    /**
     * Returns the value of field 'fundSwOutMaxAmt'.
     * 
     * @return the value of field 'FundSwOutMaxAmt'.
     */
    public java.lang.String getFundSwOutMaxAmt(
    ) {
        return this._fundSwOutMaxAmt;
    }

    /**
     * Returns the value of field 'fundSwOutMinAmt'.
     * 
     * @return the value of field 'FundSwOutMinAmt'.
     */
    public java.lang.String getFundSwOutMinAmt(
    ) {
        return this._fundSwOutMinAmt;
    }

    /**
     * Returns the value of field 'fundSwOutRtainMinAmt'.
     * 
     * @return the value of field 'FundSwOutRtainMinAmt'.
     */
    public java.lang.String getFundSwOutRtainMinAmt(
    ) {
        return this._fundSwOutRtainMinAmt;
    }

    /**
     * Method getFundUnswithSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.FundUnswithSeg at the given index
     */
    public com.dummy.wpc.batch.xml.FundUnswithSeg getFundUnswithSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._fundUnswithSegList.size()) {
            throw new IndexOutOfBoundsException("getFundUnswithSeg: Index value '" + index + "' not in range [0.." + (this._fundUnswithSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.FundUnswithSeg) _fundUnswithSegList.get(index);
    }

    /**
     * Method getFundUnswithSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.FundUnswithSeg[] getFundUnswithSeg(
    ) {
        com.dummy.wpc.batch.xml.FundUnswithSeg[] array = new com.dummy.wpc.batch.xml.FundUnswithSeg[0];
        return (com.dummy.wpc.batch.xml.FundUnswithSeg[]) this._fundUnswithSegList.toArray(array);
    }

    /**
     * Method getFundUnswithSegCount.
     * 
     * @return the size of this collection
     */
    public int getFundUnswithSegCount(
    ) {
        return this._fundUnswithSegList.size();
    }

    /**
     * Returns the value of field 'fundValnTm'.
     * 
     * @return the value of field 'FundValnTm'.
     */
    public java.lang.String getFundValnTm(
    ) {
        return this._fundValnTm;
    }

    /**
     * Returns the value of field 'hldayFundNextDt'.
     * 
     * @return the value of field 'HldayFundNextDt'.
     */
    public java.lang.String getHldayFundNextDt(
    ) {
        return this._hldayFundNextDt;
    }

    /**
     * Returns the value of field 'incmHandlOptCde'.
     * 
     * @return the value of field 'IncmHandlOptCde'.
     */
    public java.lang.String getIncmHandlOptCde(
    ) {
        return this._incmHandlOptCde;
    }

    /**
     * Returns the value of field 'insuLinkUtTrstInd'.
     * 
     * @return the value of field 'InsuLinkUtTrstInd'.
     */
    public java.lang.String getInsuLinkUtTrstInd(
    ) {
        return this._insuLinkUtTrstInd;
    }

    /**
     * Returns the value of field 'invstIncrmMinAmt'.
     * 
     * @return the value of field 'InvstIncrmMinAmt'.
     */
    public java.lang.String getInvstIncrmMinAmt(
    ) {
        return this._invstIncrmMinAmt;
    }

    /**
     * Returns the value of field 'invstMipIncrmMinAmt'.
     * 
     * @return the value of field 'InvstMipIncrmMinAmt'.
     */
    public java.lang.String getInvstMipIncrmMinAmt(
    ) {
        return this._invstMipIncrmMinAmt;
    }

    /**
     * Returns the value of field 'invstMipMinAmt'.
     * 
     * @return the value of field 'InvstMipMinAmt'.
     */
    public java.lang.String getInvstMipMinAmt(
    ) {
        return this._invstMipMinAmt;
    }

    /**
     * Returns the value of field 'offerEndDtTm'.
     * 
     * @return the value of field 'OfferEndDtTm'.
     */
    public java.lang.String getOfferEndDtTm(
    ) {
        return this._offerEndDtTm;
    }

    /**
     * Returns the value of field 'offerStartDtTm'.
     * 
     * @return the value of field 'OfferStartDtTm'.
     */
    public java.lang.String getOfferStartDtTm(
    ) {
        return this._offerStartDtTm;
    }

    /**
     * Returns the value of field 'payCashDivInd'.
     * 
     * @return the value of field 'PayCashDivInd'.
     */
    public java.lang.String getPayCashDivInd(
    ) {
        return this._payCashDivInd;
    }

    /**
     * Returns the value of field 'prodShoreLocCde'.
     * 
     * @return the value of field 'ProdShoreLocCde'.
     */
    public java.lang.String getProdShoreLocCde(
    ) {
        return this._prodShoreLocCde;
    }

    /**
     * Returns the value of field 'prodTrmtDt'.
     * 
     * @return the value of field 'ProdTrmtDt'.
     */
    public java.lang.String getProdTrmtDt(
    ) {
        return this._prodTrmtDt;
    }

    /**
     * Returns the value of field 'rdmCtoffNextDtTm'.
     * 
     * @return the value of field 'RdmCtoffNextDtTm'.
     */
    public java.lang.String getRdmCtoffNextDtTm(
    ) {
        return this._rdmCtoffNextDtTm;
    }

    /**
     * Returns the value of field 'rdmMinAmt'.
     * 
     * @return the value of field 'RdmMinAmt'.
     */
    public java.lang.String getRdmMinAmt(
    ) {
        return this._rdmMinAmt;
    }

    /**
     * Returns the value of field 'rdmMipMinAmt'.
     * 
     * @return the value of field 'RdmMipMinAmt'.
     */
    public java.lang.String getRdmMipMinAmt(
    ) {
        return this._rdmMipMinAmt;
    }

    /**
     * Returns the value of field 'rtrnGurntPct'.
     * 
     * @return the value of field 'RtrnGurntPct'.
     */
    public java.lang.String getRtrnGurntPct(
    ) {
        return this._rtrnGurntPct;
    }

    /**
     * Returns the value of field 'schemChrgCde'.
     * 
     * @return the value of field 'SchemChrgCde'.
     */
    public java.lang.String getSchemChrgCde(
    ) {
        return this._schemChrgCde;
    }

    /**
     * Returns the value of field 'scribCtoffNextDtTm'.
     * 
     * @return the value of field 'ScribCtoffNextDtTm'.
     */
    public java.lang.String getScribCtoffNextDtTm(
    ) {
        return this._scribCtoffNextDtTm;
    }

    /**
     * Returns the value of field 'spclFundInd'.
     * 
     * @return the value of field 'SpclFundInd'.
     */
    public java.lang.String getSpclFundInd(
    ) {
        return this._spclFundInd;
    }

    /**
     * Returns the value of field 'suptMipInd'.
     * 
     * @return the value of field 'SuptMipInd'.
     */
    public java.lang.String getSuptMipInd(
    ) {
        return this._suptMipInd;
    }

    /**
     * Returns the value of field 'tranMaxAmt'.
     * 
     * @return the value of field 'TranMaxAmt'.
     */
    public java.lang.String getTranMaxAmt(
    ) {
        return this._tranMaxAmt;
    }

    /**
     * Returns the value of field 'utMipRdmMinNum'.
     * 
     * @return the value of field 'UtMipRdmMinNum'.
     */
    public java.lang.String getUtMipRdmMinNum(
    ) {
        return this._utMipRdmMinNum;
    }

    /**
     * Returns the value of field 'utMipRtainMinNum'.
     * 
     * @return the value of field 'UtMipRtainMinNum'.
     */
    public java.lang.String getUtMipRtainMinNum(
    ) {
        return this._utMipRtainMinNum;
    }

    /**
     * Returns the value of field 'utPrcProdLnchAmt'.
     * 
     * @return the value of field 'UtPrcProdLnchAmt'.
     */
    public java.lang.String getUtPrcProdLnchAmt(
    ) {
        return this._utPrcProdLnchAmt;
    }

    /**
     * Returns the value of field 'utRdmMinNum'.
     * 
     * @return the value of field 'UtRdmMinNum'.
     */
    public java.lang.String getUtRdmMinNum(
    ) {
        return this._utRdmMinNum;
    }

    /**
     * Returns the value of field 'utRtainMinNum'.
     * 
     * @return the value of field 'UtRtainMinNum'.
     */
    public java.lang.String getUtRtainMinNum(
    ) {
        return this._utRtainMinNum;
    }

    /**
     * Returns the value of field 'utSwOutRtainMinNum'.
     * 
     * @return the value of field 'UtSwOutRtainMinNum'.
     */
    public java.lang.String getUtSwOutRtainMinNum(
    ) {
        return this._utSwOutRtainMinNum;
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
    public void removeAllFundUnswithSeg(
    ) {
        this._fundUnswithSegList.clear();
    }

    /**
     * Method removeFundUnswithSeg.
     * 
     * @param vFundUnswithSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeFundUnswithSeg(
            final com.dummy.wpc.batch.xml.FundUnswithSeg vFundUnswithSeg) {
        boolean removed = _fundUnswithSegList.remove(vFundUnswithSeg);
        return removed;
    }

    /**
     * Method removeFundUnswithSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.FundUnswithSeg removeFundUnswithSegAt(
            final int index) {
        java.lang.Object obj = this._fundUnswithSegList.remove(index);
        return (com.dummy.wpc.batch.xml.FundUnswithSeg) obj;
    }

    /**
     * Sets the value of field 'amcmInd'.
     * 
     * @param amcmInd the value of field 'amcmInd'.
     */
    public void setAmcmInd(
            final java.lang.String amcmInd) {
        this._amcmInd = amcmInd;
    }

    /**
     * Sets the value of field 'autoRenewFundInd'.
     * 
     * @param autoRenewFundInd the value of field 'autoRenewFundInd'
     */
    public void setAutoRenewFundInd(
            final java.lang.String autoRenewFundInd) {
        this._autoRenewFundInd = autoRenewFundInd;
    }

    /**
     * Sets the value of field 'autoSweepFundInd'.
     * 
     * @param autoSweepFundInd the value of field 'autoSweepFundInd'
     */
    public void setAutoSweepFundInd(
            final java.lang.String autoSweepFundInd) {
        this._autoSweepFundInd = autoSweepFundInd;
    }

    /**
     * Sets the value of field 'chrgFundSwPct'.
     * 
     * @param chrgFundSwPct the value of field 'chrgFundSwPct'.
     */
    public void setChrgFundSwPct(
            final java.lang.String chrgFundSwPct) {
        this._chrgFundSwPct = chrgFundSwPct;
    }

    /**
     * Sets the value of field 'chrgInitSalesPct'.
     * 
     * @param chrgInitSalesPct the value of field 'chrgInitSalesPct'
     */
    public void setChrgInitSalesPct(
            final java.lang.String chrgInitSalesPct) {
        this._chrgInitSalesPct = chrgInitSalesPct;
    }

    /**
     * Sets the value of field 'closeEndFundInd'.
     * 
     * @param closeEndFundInd the value of field 'closeEndFundInd'.
     */
    public void setCloseEndFundInd(
            final java.lang.String closeEndFundInd) {
        this._closeEndFundInd = closeEndFundInd;
    }

    /**
     * Sets the value of field 'dealNextDt'.
     * 
     * @param dealNextDt the value of field 'dealNextDt'.
     */
    public void setDealNextDt(
            final java.lang.String dealNextDt) {
        this._dealNextDt = dealNextDt;
    }

    /**
     * Sets the value of field 'divDclrDt'.
     * 
     * @param divDclrDt the value of field 'divDclrDt'.
     */
    public void setDivDclrDt(
            final java.lang.String divDclrDt) {
        this._divDclrDt = divDclrDt;
    }

    /**
     * Sets the value of field 'divPymtDt'.
     * 
     * @param divPymtDt the value of field 'divPymtDt'.
     */
    public void setDivPymtDt(
            final java.lang.String divPymtDt) {
        this._divPymtDt = divPymtDt;
    }

    /**
     * Sets the value of field 'divYieldPct'.
     * 
     * @param divYieldPct the value of field 'divYieldPct'.
     */
    public void setDivYieldPct(
            final java.lang.String divYieldPct) {
        this._divYieldPct = divYieldPct;
    }

    /**
     * Sets the value of field 'dscntMaxPct'.
     * 
     * @param dscntMaxPct the value of field 'dscntMaxPct'.
     */
    public void setDscntMaxPct(
            final java.lang.String dscntMaxPct) {
        this._dscntMaxPct = dscntMaxPct;
    }

    /**
     * Sets the value of field 'fundAmt'.
     * 
     * @param fundAmt the value of field 'fundAmt'.
     */
    public void setFundAmt(
            final java.lang.String fundAmt) {
        this._fundAmt = fundAmt;
    }

    /**
     * Sets the value of field 'fundClassCde'.
     * 
     * @param fundClassCde the value of field 'fundClassCde'.
     */
    public void setFundClassCde(
            final java.lang.String fundClassCde) {
        this._fundClassCde = fundClassCde;
    }

    /**
     * Sets the value of field 'fundHouseCde'.
     * 
     * @param fundHouseCde the value of field 'fundHouseCde'.
     */
    public void setFundHouseCde(
            final java.lang.String fundHouseCde) {
        this._fundHouseCde = fundHouseCde;
    }

    /**
     * Sets the value of field 'fundMipRtainMinAmt'.
     * 
     * @param fundMipRtainMinAmt the value of field
     * 'fundMipRtainMinAmt'.
     */
    public void setFundMipRtainMinAmt(
            final java.lang.String fundMipRtainMinAmt) {
        this._fundMipRtainMinAmt = fundMipRtainMinAmt;
    }

    /**
     * Sets the value of field 'fundRtainMinAmt'.
     * 
     * @param fundRtainMinAmt the value of field 'fundRtainMinAmt'.
     */
    public void setFundRtainMinAmt(
            final java.lang.String fundRtainMinAmt) {
        this._fundRtainMinAmt = fundRtainMinAmt;
    }

    /**
     * Sets the value of field 'fundSwInMinAmt'.
     * 
     * @param fundSwInMinAmt the value of field 'fundSwInMinAmt'.
     */
    public void setFundSwInMinAmt(
            final java.lang.String fundSwInMinAmt) {
        this._fundSwInMinAmt = fundSwInMinAmt;
    }

    /**
     * Sets the value of field 'fundSwOutMaxAmt'.
     * 
     * @param fundSwOutMaxAmt the value of field 'fundSwOutMaxAmt'.
     */
    public void setFundSwOutMaxAmt(
            final java.lang.String fundSwOutMaxAmt) {
        this._fundSwOutMaxAmt = fundSwOutMaxAmt;
    }

    /**
     * Sets the value of field 'fundSwOutMinAmt'.
     * 
     * @param fundSwOutMinAmt the value of field 'fundSwOutMinAmt'.
     */
    public void setFundSwOutMinAmt(
            final java.lang.String fundSwOutMinAmt) {
        this._fundSwOutMinAmt = fundSwOutMinAmt;
    }

    /**
     * Sets the value of field 'fundSwOutRtainMinAmt'.
     * 
     * @param fundSwOutRtainMinAmt the value of field
     * 'fundSwOutRtainMinAmt'.
     */
    public void setFundSwOutRtainMinAmt(
            final java.lang.String fundSwOutRtainMinAmt) {
        this._fundSwOutRtainMinAmt = fundSwOutRtainMinAmt;
    }

    /**
     * 
     * 
     * @param index
     * @param vFundUnswithSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setFundUnswithSeg(
            final int index,
            final com.dummy.wpc.batch.xml.FundUnswithSeg vFundUnswithSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._fundUnswithSegList.size()) {
            throw new IndexOutOfBoundsException("setFundUnswithSeg: Index value '" + index + "' not in range [0.." + (this._fundUnswithSegList.size() - 1) + "]");
        }
        
        this._fundUnswithSegList.set(index, vFundUnswithSeg);
    }

    /**
     * 
     * 
     * @param vFundUnswithSegArray
     */
    public void setFundUnswithSeg(
            final com.dummy.wpc.batch.xml.FundUnswithSeg[] vFundUnswithSegArray) {
        //-- copy array
        _fundUnswithSegList.clear();
        
        for (int i = 0; i < vFundUnswithSegArray.length; i++) {
                this._fundUnswithSegList.add(vFundUnswithSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'fundValnTm'.
     * 
     * @param fundValnTm the value of field 'fundValnTm'.
     */
    public void setFundValnTm(
            final java.lang.String fundValnTm) {
        this._fundValnTm = fundValnTm;
    }

    /**
     * Sets the value of field 'hldayFundNextDt'.
     * 
     * @param hldayFundNextDt the value of field 'hldayFundNextDt'.
     */
    public void setHldayFundNextDt(
            final java.lang.String hldayFundNextDt) {
        this._hldayFundNextDt = hldayFundNextDt;
    }

    /**
     * Sets the value of field 'incmHandlOptCde'.
     * 
     * @param incmHandlOptCde the value of field 'incmHandlOptCde'.
     */
    public void setIncmHandlOptCde(
            final java.lang.String incmHandlOptCde) {
        this._incmHandlOptCde = incmHandlOptCde;
    }

    /**
     * Sets the value of field 'insuLinkUtTrstInd'.
     * 
     * @param insuLinkUtTrstInd the value of field
     * 'insuLinkUtTrstInd'.
     */
    public void setInsuLinkUtTrstInd(
            final java.lang.String insuLinkUtTrstInd) {
        this._insuLinkUtTrstInd = insuLinkUtTrstInd;
    }

    /**
     * Sets the value of field 'invstIncrmMinAmt'.
     * 
     * @param invstIncrmMinAmt the value of field 'invstIncrmMinAmt'
     */
    public void setInvstIncrmMinAmt(
            final java.lang.String invstIncrmMinAmt) {
        this._invstIncrmMinAmt = invstIncrmMinAmt;
    }

    /**
     * Sets the value of field 'invstMipIncrmMinAmt'.
     * 
     * @param invstMipIncrmMinAmt the value of field
     * 'invstMipIncrmMinAmt'.
     */
    public void setInvstMipIncrmMinAmt(
            final java.lang.String invstMipIncrmMinAmt) {
        this._invstMipIncrmMinAmt = invstMipIncrmMinAmt;
    }

    /**
     * Sets the value of field 'invstMipMinAmt'.
     * 
     * @param invstMipMinAmt the value of field 'invstMipMinAmt'.
     */
    public void setInvstMipMinAmt(
            final java.lang.String invstMipMinAmt) {
        this._invstMipMinAmt = invstMipMinAmt;
    }

    /**
     * Sets the value of field 'offerEndDtTm'.
     * 
     * @param offerEndDtTm the value of field 'offerEndDtTm'.
     */
    public void setOfferEndDtTm(
            final java.lang.String offerEndDtTm) {
        this._offerEndDtTm = offerEndDtTm;
    }

    /**
     * Sets the value of field 'offerStartDtTm'.
     * 
     * @param offerStartDtTm the value of field 'offerStartDtTm'.
     */
    public void setOfferStartDtTm(
            final java.lang.String offerStartDtTm) {
        this._offerStartDtTm = offerStartDtTm;
    }

    /**
     * Sets the value of field 'payCashDivInd'.
     * 
     * @param payCashDivInd the value of field 'payCashDivInd'.
     */
    public void setPayCashDivInd(
            final java.lang.String payCashDivInd) {
        this._payCashDivInd = payCashDivInd;
    }

    /**
     * Sets the value of field 'prodShoreLocCde'.
     * 
     * @param prodShoreLocCde the value of field 'prodShoreLocCde'.
     */
    public void setProdShoreLocCde(
            final java.lang.String prodShoreLocCde) {
        this._prodShoreLocCde = prodShoreLocCde;
    }

    /**
     * Sets the value of field 'prodTrmtDt'.
     * 
     * @param prodTrmtDt the value of field 'prodTrmtDt'.
     */
    public void setProdTrmtDt(
            final java.lang.String prodTrmtDt) {
        this._prodTrmtDt = prodTrmtDt;
    }

    /**
     * Sets the value of field 'rdmCtoffNextDtTm'.
     * 
     * @param rdmCtoffNextDtTm the value of field 'rdmCtoffNextDtTm'
     */
    public void setRdmCtoffNextDtTm(
            final java.lang.String rdmCtoffNextDtTm) {
        this._rdmCtoffNextDtTm = rdmCtoffNextDtTm;
    }

    /**
     * Sets the value of field 'rdmMinAmt'.
     * 
     * @param rdmMinAmt the value of field 'rdmMinAmt'.
     */
    public void setRdmMinAmt(
            final java.lang.String rdmMinAmt) {
        this._rdmMinAmt = rdmMinAmt;
    }

    /**
     * Sets the value of field 'rdmMipMinAmt'.
     * 
     * @param rdmMipMinAmt the value of field 'rdmMipMinAmt'.
     */
    public void setRdmMipMinAmt(
            final java.lang.String rdmMipMinAmt) {
        this._rdmMipMinAmt = rdmMipMinAmt;
    }

    /**
     * Sets the value of field 'rtrnGurntPct'.
     * 
     * @param rtrnGurntPct the value of field 'rtrnGurntPct'.
     */
    public void setRtrnGurntPct(
            final java.lang.String rtrnGurntPct) {
        this._rtrnGurntPct = rtrnGurntPct;
    }

    /**
     * Sets the value of field 'schemChrgCde'.
     * 
     * @param schemChrgCde the value of field 'schemChrgCde'.
     */
    public void setSchemChrgCde(
            final java.lang.String schemChrgCde) {
        this._schemChrgCde = schemChrgCde;
    }

    /**
     * Sets the value of field 'scribCtoffNextDtTm'.
     * 
     * @param scribCtoffNextDtTm the value of field
     * 'scribCtoffNextDtTm'.
     */
    public void setScribCtoffNextDtTm(
            final java.lang.String scribCtoffNextDtTm) {
        this._scribCtoffNextDtTm = scribCtoffNextDtTm;
    }

    /**
     * Sets the value of field 'spclFundInd'.
     * 
     * @param spclFundInd the value of field 'spclFundInd'.
     */
    public void setSpclFundInd(
            final java.lang.String spclFundInd) {
        this._spclFundInd = spclFundInd;
    }

    /**
     * Sets the value of field 'suptMipInd'.
     * 
     * @param suptMipInd the value of field 'suptMipInd'.
     */
    public void setSuptMipInd(
            final java.lang.String suptMipInd) {
        this._suptMipInd = suptMipInd;
    }

    /**
     * Sets the value of field 'tranMaxAmt'.
     * 
     * @param tranMaxAmt the value of field 'tranMaxAmt'.
     */
    public void setTranMaxAmt(
            final java.lang.String tranMaxAmt) {
        this._tranMaxAmt = tranMaxAmt;
    }

    /**
     * Sets the value of field 'utMipRdmMinNum'.
     * 
     * @param utMipRdmMinNum the value of field 'utMipRdmMinNum'.
     */
    public void setUtMipRdmMinNum(
            final java.lang.String utMipRdmMinNum) {
        this._utMipRdmMinNum = utMipRdmMinNum;
    }

    /**
     * Sets the value of field 'utMipRtainMinNum'.
     * 
     * @param utMipRtainMinNum the value of field 'utMipRtainMinNum'
     */
    public void setUtMipRtainMinNum(
            final java.lang.String utMipRtainMinNum) {
        this._utMipRtainMinNum = utMipRtainMinNum;
    }

    /**
     * Sets the value of field 'utPrcProdLnchAmt'.
     * 
     * @param utPrcProdLnchAmt the value of field 'utPrcProdLnchAmt'
     */
    public void setUtPrcProdLnchAmt(
            final java.lang.String utPrcProdLnchAmt) {
        this._utPrcProdLnchAmt = utPrcProdLnchAmt;
    }

    /**
     * Sets the value of field 'utRdmMinNum'.
     * 
     * @param utRdmMinNum the value of field 'utRdmMinNum'.
     */
    public void setUtRdmMinNum(
            final java.lang.String utRdmMinNum) {
        this._utRdmMinNum = utRdmMinNum;
    }

    /**
     * Sets the value of field 'utRtainMinNum'.
     * 
     * @param utRtainMinNum the value of field 'utRtainMinNum'.
     */
    public void setUtRtainMinNum(
            final java.lang.String utRtainMinNum) {
        this._utRtainMinNum = utRtainMinNum;
    }

    /**
     * Sets the value of field 'utSwOutRtainMinNum'.
     * 
     * @param utSwOutRtainMinNum the value of field
     * 'utSwOutRtainMinNum'.
     */
    public void setUtSwOutRtainMinNum(
            final java.lang.String utSwOutRtainMinNum) {
        this._utSwOutRtainMinNum = utSwOutRtainMinNum;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * com.dummy.wpc.batch.xml.UnitTrustSegment
     */
    public static com.dummy.wpc.batch.xml.UnitTrustSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.UnitTrustSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.UnitTrustSegment.class, reader);
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
