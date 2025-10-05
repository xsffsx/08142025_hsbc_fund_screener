/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/BondSegment.java 1.4 2012/04/13 10:10:30CST 43588220 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class BondSegment.
 * 
 * @version $Revision: 1.4 $ $Date: 2012/04/13 10:10:30CST $
 */
public class BondSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _isrBndNme.
     */
    private java.lang.String _isrBndNme;

    /**
     * Field _issueNum.
     */
    private java.lang.String _issueNum;

    /**
     * Field _prodIssDt.
     */
    private java.lang.String _prodIssDt;

    /**
     * Field _pdcyCoupnPymtCd.
     */
    private java.lang.String _pdcyCoupnPymtCd;

    /**
     * Field _coupnAnnlRt.
     */
    private java.lang.String _coupnAnnlRt;

    /**
     * Field _coupnExtInstmRt.
     */
    private java.lang.String _coupnExtInstmRt;

    /**
     * Field _pymtCoupnNextDt.
     */
    private java.lang.String _pymtCoupnNextDt;

    /**
     * Field _flexMatOptInd.
     */
    private java.lang.String _flexMatOptInd;

    /**
     * Field _intIndAccrAmt.
     */
    private java.lang.String _intIndAccrAmt;

    /**
     * Field _invstIncMinAmt.
     */
    private java.lang.String _invstIncMinAmt;

    /**
     * Field _prodBodLotQtyCnt.
     */
    private java.lang.String _prodBodLotQtyCnt;

    /**
     * Field _mturExtDt.
     */
    private java.lang.String _mturExtDt;

    /**
     * Field _subDebtInd.
     */
    private java.lang.String _subDebtInd;

    /**
     * Field _bondStatCde.
     */
    private java.lang.String _bondStatCde;

    /**
     * Field _ctryBondIssueCde.
     */
    private java.lang.String _ctryBondIssueCde;

    /**
     * Field _grntrName.
     */
    private java.lang.String _grntrName;

    /**
     * Field _cptlTierText.
     */
    private java.lang.String _cptlTierText;

    /**
     * Field _coupnType.
     */
    private java.lang.String _coupnType;

    /**
     * Field _coupnPrevRt.
     */
    private java.lang.String _coupnPrevRt;

    /**
     * Field _indexFltRtNme.
     */
    private java.lang.String _indexFltRtNme;

    /**
     * Field _bondFltSprdRt.
     */
    private java.lang.String _bondFltSprdRt;

    /**
     * Field _coupnCurrStartDt.
     */
    private java.lang.String _coupnCurrStartDt;

    /**
     * Field _coupnPrevStartDt.
     */
    private java.lang.String _coupnPrevStartDt;

    /**
     * Field _bondCallNextDt.
     */
    private java.lang.String _bondCallNextDt;

    /**
     * Field _intBassiCalcText.
     */
    private java.lang.String _intBassiCalcText;

    /**
     * Field _intAccrDayCnt.
     */
    private java.lang.String _intAccrDayCnt;

    /**
     * Field _invstSoldLestAmt.
     */
    private java.lang.String _invstSoldLestAmt;

    /**
     * Field _invstIncrmSoldAmt.
     */
    private java.lang.String _invstIncrmSoldAmt;

    /**
     * Field _shrBidCnt.
     */
    private java.lang.String _shrBidCnt;

    /**
     * Field _shrOffrCnt.
     */
    private java.lang.String _shrOffrCnt;

    /**
     * Field _prodClsBidPrcAmt.
     */
    private java.lang.String _prodClsBidPrcAmt;

    /**
     * Field _prodClsOffrPrcAmt.
     */
    private java.lang.String _prodClsOffrPrcAmt;

    /**
     * Field _bondCloseDt.
     */
    private java.lang.String _bondCloseDt;

    /**
     * Field _bondSetlDt.
     */
    private java.lang.String _bondSetlDt;

    /**
     * Field _dscntMrgnBselPct.
     */
    private java.lang.String _dscntMrgnBselPct;

    /**
     * Field _dscntMrgnBbuyPct.
     */
    private java.lang.String _dscntMrgnBbuyPct;

    /**
     * Field _prcBondRecvDtTm.
     */
    private java.lang.String _prcBondRecvDtTm;

    /**
     * Field _yieldOfferText.
     */
    private java.lang.String _yieldOfferText;

    /**
     * Field _coupnAnnlText.
     */
    private java.lang.String _coupnAnnlText;

    /**
     * Field _isrDesc.
     */
    private java.lang.String _isrDesc;

    /**
     * Field _isrPllDesc.
     */
    private java.lang.String _isrPllDesc;

    /**
     * Field _isrSllDesc.
     */
    private java.lang.String _isrSllDesc;

    /**
     * Field _srTypeCde.
     */
    private java.lang.String _srTypeCde;

    /**
     * Field _creditRtingSegList.
     */
    private java.util.Vector _creditRtingSegList;

    /**
     * Field _yieldHistSegList.
     */
    private java.util.Vector _yieldHistSegList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BondSegment() {
        super();
        this._creditRtingSegList = new java.util.Vector();
        this._yieldHistSegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCreditRtingSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCreditRtingSeg(
            final com.dummy.wpc.batch.xml.CreditRtingSeg vCreditRtingSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._creditRtingSegList.addElement(vCreditRtingSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vCreditRtingSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCreditRtingSeg(
            final int index,
            final com.dummy.wpc.batch.xml.CreditRtingSeg vCreditRtingSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._creditRtingSegList.add(index, vCreditRtingSeg);
    }

    /**
     * 
     * 
     * @param vYieldHistSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addYieldHistSeg(
            final com.dummy.wpc.batch.xml.YieldHistSeg vYieldHistSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._yieldHistSegList.addElement(vYieldHistSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vYieldHistSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addYieldHistSeg(
            final int index,
            final com.dummy.wpc.batch.xml.YieldHistSeg vYieldHistSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._yieldHistSegList.add(index, vYieldHistSeg);
    }

    /**
     * Method enumerateCreditRtingSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.CreditRtingSeg elements
     */
    public java.util.Enumeration enumerateCreditRtingSeg(
    ) {
        return this._creditRtingSegList.elements();
    }

    /**
     * Method enumerateYieldHistSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.YieldHistSeg elements
     */
    public java.util.Enumeration enumerateYieldHistSeg(
    ) {
        return this._yieldHistSegList.elements();
    }

    /**
     * Returns the value of field 'bondCallNextDt'.
     * 
     * @return the value of field 'BondCallNextDt'.
     */
    public java.lang.String getBondCallNextDt(
    ) {
        return this._bondCallNextDt;
    }

    /**
     * Returns the value of field 'bondCloseDt'.
     * 
     * @return the value of field 'BondCloseDt'.
     */
    public java.lang.String getBondCloseDt(
    ) {
        return this._bondCloseDt;
    }

    /**
     * Returns the value of field 'bondFltSprdRt'.
     * 
     * @return the value of field 'BondFltSprdRt'.
     */
    public java.lang.String getBondFltSprdRt(
    ) {
        return this._bondFltSprdRt;
    }

    /**
     * Returns the value of field 'bondSetlDt'.
     * 
     * @return the value of field 'BondSetlDt'.
     */
    public java.lang.String getBondSetlDt(
    ) {
        return this._bondSetlDt;
    }

    /**
     * Returns the value of field 'bondStatCde'.
     * 
     * @return the value of field 'BondStatCde'.
     */
    public java.lang.String getBondStatCde(
    ) {
        return this._bondStatCde;
    }

    /**
     * Returns the value of field 'coupnAnnlRt'.
     * 
     * @return the value of field 'CoupnAnnlRt'.
     */
    public java.lang.String getCoupnAnnlRt(
    ) {
        return this._coupnAnnlRt;
    }

    /**
     * Returns the value of field 'coupnAnnlText'.
     * 
     * @return the value of field 'CoupnAnnlText'.
     */
    public java.lang.String getCoupnAnnlText(
    ) {
        return this._coupnAnnlText;
    }

    /**
     * Returns the value of field 'coupnCurrStartDt'.
     * 
     * @return the value of field 'CoupnCurrStartDt'.
     */
    public java.lang.String getCoupnCurrStartDt(
    ) {
        return this._coupnCurrStartDt;
    }

    /**
     * Returns the value of field 'coupnExtInstmRt'.
     * 
     * @return the value of field 'CoupnExtInstmRt'.
     */
    public java.lang.String getCoupnExtInstmRt(
    ) {
        return this._coupnExtInstmRt;
    }

    /**
     * Returns the value of field 'coupnPrevRt'.
     * 
     * @return the value of field 'CoupnPrevRt'.
     */
    public java.lang.String getCoupnPrevRt(
    ) {
        return this._coupnPrevRt;
    }

    /**
     * Returns the value of field 'coupnPrevStartDt'.
     * 
     * @return the value of field 'CoupnPrevStartDt'.
     */
    public java.lang.String getCoupnPrevStartDt(
    ) {
        return this._coupnPrevStartDt;
    }

    /**
     * Returns the value of field 'coupnType'.
     * 
     * @return the value of field 'CoupnType'.
     */
    public java.lang.String getCoupnType(
    ) {
        return this._coupnType;
    }

    /**
     * Returns the value of field 'cptlTierText'.
     * 
     * @return the value of field 'CptlTierText'.
     */
    public java.lang.String getCptlTierText(
    ) {
        return this._cptlTierText;
    }

    /**
     * Method getCreditRtingSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.CreditRtingSeg at the given index
     */
    public com.dummy.wpc.batch.xml.CreditRtingSeg getCreditRtingSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._creditRtingSegList.size()) {
            throw new IndexOutOfBoundsException("getCreditRtingSeg: Index value '" + index + "' not in range [0.." + (this._creditRtingSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.CreditRtingSeg) _creditRtingSegList.get(index);
    }

    /**
     * Method getCreditRtingSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.CreditRtingSeg[] getCreditRtingSeg(
    ) {
        com.dummy.wpc.batch.xml.CreditRtingSeg[] array = new com.dummy.wpc.batch.xml.CreditRtingSeg[0];
        return (com.dummy.wpc.batch.xml.CreditRtingSeg[]) this._creditRtingSegList.toArray(array);
    }

    /**
     * Method getCreditRtingSegCount.
     * 
     * @return the size of this collection
     */
    public int getCreditRtingSegCount(
    ) {
        return this._creditRtingSegList.size();
    }

    /**
     * Returns the value of field 'ctryBondIssueCde'.
     * 
     * @return the value of field 'CtryBondIssueCde'.
     */
    public java.lang.String getCtryBondIssueCde(
    ) {
        return this._ctryBondIssueCde;
    }

    /**
     * Returns the value of field 'dscntMrgnBbuyPct'.
     * 
     * @return the value of field 'DscntMrgnBbuyPct'.
     */
    public java.lang.String getDscntMrgnBbuyPct(
    ) {
        return this._dscntMrgnBbuyPct;
    }

    /**
     * Returns the value of field 'dscntMrgnBselPct'.
     * 
     * @return the value of field 'DscntMrgnBselPct'.
     */
    public java.lang.String getDscntMrgnBselPct(
    ) {
        return this._dscntMrgnBselPct;
    }

    /**
     * Returns the value of field 'flexMatOptInd'.
     * 
     * @return the value of field 'FlexMatOptInd'.
     */
    public java.lang.String getFlexMatOptInd(
    ) {
        return this._flexMatOptInd;
    }

    /**
     * Returns the value of field 'grntrName'.
     * 
     * @return the value of field 'GrntrName'.
     */
    public java.lang.String getGrntrName(
    ) {
        return this._grntrName;
    }

    /**
     * Returns the value of field 'indexFltRtNme'.
     * 
     * @return the value of field 'IndexFltRtNme'.
     */
    public java.lang.String getIndexFltRtNme(
    ) {
        return this._indexFltRtNme;
    }

    /**
     * Returns the value of field 'intAccrDayCnt'.
     * 
     * @return the value of field 'IntAccrDayCnt'.
     */
    public java.lang.String getIntAccrDayCnt(
    ) {
        return this._intAccrDayCnt;
    }

    /**
     * Returns the value of field 'intBassiCalcText'.
     * 
     * @return the value of field 'IntBassiCalcText'.
     */
    public java.lang.String getIntBassiCalcText(
    ) {
        return this._intBassiCalcText;
    }

    /**
     * Returns the value of field 'intIndAccrAmt'.
     * 
     * @return the value of field 'IntIndAccrAmt'.
     */
    public java.lang.String getIntIndAccrAmt(
    ) {
        return this._intIndAccrAmt;
    }

    /**
     * Returns the value of field 'invstIncMinAmt'.
     * 
     * @return the value of field 'InvstIncMinAmt'.
     */
    public java.lang.String getInvstIncMinAmt(
    ) {
        return this._invstIncMinAmt;
    }

    /**
     * Returns the value of field 'invstIncrmSoldAmt'.
     * 
     * @return the value of field 'InvstIncrmSoldAmt'.
     */
    public java.lang.String getInvstIncrmSoldAmt(
    ) {
        return this._invstIncrmSoldAmt;
    }

    /**
     * Returns the value of field 'invstSoldLestAmt'.
     * 
     * @return the value of field 'InvstSoldLestAmt'.
     */
    public java.lang.String getInvstSoldLestAmt(
    ) {
        return this._invstSoldLestAmt;
    }

    /**
     * Returns the value of field 'isrBndNme'.
     * 
     * @return the value of field 'IsrBndNme'.
     */
    public java.lang.String getIsrBndNme(
    ) {
        return this._isrBndNme;
    }

    /**
     * Returns the value of field 'isrDesc'.
     * 
     * @return the value of field 'IsrDesc'.
     */
    public java.lang.String getIsrDesc(
    ) {
        return this._isrDesc;
    }

    /**
     * Returns the value of field 'isrPllDesc'.
     * 
     * @return the value of field 'IsrPllDesc'.
     */
    public java.lang.String getIsrPllDesc(
    ) {
        return this._isrPllDesc;
    }

    /**
     * Returns the value of field 'isrSllDesc'.
     * 
     * @return the value of field 'IsrSllDesc'.
     */
    public java.lang.String getIsrSllDesc(
    ) {
        return this._isrSllDesc;
    }

    /**
     * Returns the value of field 'issueNum'.
     * 
     * @return the value of field 'IssueNum'.
     */
    public java.lang.String getIssueNum(
    ) {
        return this._issueNum;
    }

    /**
     * Returns the value of field 'mturExtDt'.
     * 
     * @return the value of field 'MturExtDt'.
     */
    public java.lang.String getMturExtDt(
    ) {
        return this._mturExtDt;
    }

    /**
     * Returns the value of field 'pdcyCoupnPymtCd'.
     * 
     * @return the value of field 'PdcyCoupnPymtCd'.
     */
    public java.lang.String getPdcyCoupnPymtCd(
    ) {
        return this._pdcyCoupnPymtCd;
    }

    /**
     * Returns the value of field 'prcBondRecvDtTm'.
     * 
     * @return the value of field 'PrcBondRecvDtTm'.
     */
    public java.lang.String getPrcBondRecvDtTm(
    ) {
        return this._prcBondRecvDtTm;
    }

    /**
     * Returns the value of field 'prodBodLotQtyCnt'.
     * 
     * @return the value of field 'ProdBodLotQtyCnt'.
     */
    public java.lang.String getProdBodLotQtyCnt(
    ) {
        return this._prodBodLotQtyCnt;
    }

    /**
     * Returns the value of field 'prodClsBidPrcAmt'.
     * 
     * @return the value of field 'ProdClsBidPrcAmt'.
     */
    public java.lang.String getProdClsBidPrcAmt(
    ) {
        return this._prodClsBidPrcAmt;
    }

    /**
     * Returns the value of field 'prodClsOffrPrcAmt'.
     * 
     * @return the value of field 'ProdClsOffrPrcAmt'.
     */
    public java.lang.String getProdClsOffrPrcAmt(
    ) {
        return this._prodClsOffrPrcAmt;
    }

    /**
     * Returns the value of field 'prodIssDt'.
     * 
     * @return the value of field 'ProdIssDt'.
     */
    public java.lang.String getProdIssDt(
    ) {
        return this._prodIssDt;
    }

    /**
     * Returns the value of field 'pymtCoupnNextDt'.
     * 
     * @return the value of field 'PymtCoupnNextDt'.
     */
    public java.lang.String getPymtCoupnNextDt(
    ) {
        return this._pymtCoupnNextDt;
    }

    /**
     * Returns the value of field 'shrBidCnt'.
     * 
     * @return the value of field 'ShrBidCnt'.
     */
    public java.lang.String getShrBidCnt(
    ) {
        return this._shrBidCnt;
    }

    /**
     * Returns the value of field 'shrOffrCnt'.
     * 
     * @return the value of field 'ShrOffrCnt'.
     */
    public java.lang.String getShrOffrCnt(
    ) {
        return this._shrOffrCnt;
    }

    /**
     * Returns the value of field 'srTypeCde'.
     * 
     * @return the value of field 'SrTypeCde'.
     */
    public java.lang.String getSrTypeCde(
    ) {
        return this._srTypeCde;
    }

    /**
     * Returns the value of field 'subDebtInd'.
     * 
     * @return the value of field 'SubDebtInd'.
     */
    public java.lang.String getSubDebtInd(
    ) {
        return this._subDebtInd;
    }

    /**
     * Method getYieldHistSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.YieldHistSeg
     * at the given index
     */
    public com.dummy.wpc.batch.xml.YieldHistSeg getYieldHistSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._yieldHistSegList.size()) {
            throw new IndexOutOfBoundsException("getYieldHistSeg: Index value '" + index + "' not in range [0.." + (this._yieldHistSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.YieldHistSeg) _yieldHistSegList.get(index);
    }

    /**
     * Method getYieldHistSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.YieldHistSeg[] getYieldHistSeg(
    ) {
        com.dummy.wpc.batch.xml.YieldHistSeg[] array = new com.dummy.wpc.batch.xml.YieldHistSeg[0];
        return (com.dummy.wpc.batch.xml.YieldHistSeg[]) this._yieldHistSegList.toArray(array);
    }

    /**
     * Method getYieldHistSegCount.
     * 
     * @return the size of this collection
     */
    public int getYieldHistSegCount(
    ) {
        return this._yieldHistSegList.size();
    }

    /**
     * Returns the value of field 'yieldOfferText'.
     * 
     * @return the value of field 'YieldOfferText'.
     */
    public java.lang.String getYieldOfferText(
    ) {
        return this._yieldOfferText;
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
    public void removeAllCreditRtingSeg(
    ) {
        this._creditRtingSegList.clear();
    }

    /**
     */
    public void removeAllYieldHistSeg(
    ) {
        this._yieldHistSegList.clear();
    }

    /**
     * Method removeCreditRtingSeg.
     * 
     * @param vCreditRtingSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeCreditRtingSeg(
            final com.dummy.wpc.batch.xml.CreditRtingSeg vCreditRtingSeg) {
        boolean removed = _creditRtingSegList.remove(vCreditRtingSeg);
        return removed;
    }

    /**
     * Method removeCreditRtingSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.CreditRtingSeg removeCreditRtingSegAt(
            final int index) {
        java.lang.Object obj = this._creditRtingSegList.remove(index);
        return (com.dummy.wpc.batch.xml.CreditRtingSeg) obj;
    }

    /**
     * Method removeYieldHistSeg.
     * 
     * @param vYieldHistSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeYieldHistSeg(
            final com.dummy.wpc.batch.xml.YieldHistSeg vYieldHistSeg) {
        boolean removed = _yieldHistSegList.remove(vYieldHistSeg);
        return removed;
    }

    /**
     * Method removeYieldHistSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.YieldHistSeg removeYieldHistSegAt(
            final int index) {
        java.lang.Object obj = this._yieldHistSegList.remove(index);
        return (com.dummy.wpc.batch.xml.YieldHistSeg) obj;
    }

    /**
     * Sets the value of field 'bondCallNextDt'.
     * 
     * @param bondCallNextDt the value of field 'bondCallNextDt'.
     */
    public void setBondCallNextDt(
            final java.lang.String bondCallNextDt) {
        this._bondCallNextDt = bondCallNextDt;
    }

    /**
     * Sets the value of field 'bondCloseDt'.
     * 
     * @param bondCloseDt the value of field 'bondCloseDt'.
     */
    public void setBondCloseDt(
            final java.lang.String bondCloseDt) {
        this._bondCloseDt = bondCloseDt;
    }

    /**
     * Sets the value of field 'bondFltSprdRt'.
     * 
     * @param bondFltSprdRt the value of field 'bondFltSprdRt'.
     */
    public void setBondFltSprdRt(
            final java.lang.String bondFltSprdRt) {
        this._bondFltSprdRt = bondFltSprdRt;
    }

    /**
     * Sets the value of field 'bondSetlDt'.
     * 
     * @param bondSetlDt the value of field 'bondSetlDt'.
     */
    public void setBondSetlDt(
            final java.lang.String bondSetlDt) {
        this._bondSetlDt = bondSetlDt;
    }

    /**
     * Sets the value of field 'bondStatCde'.
     * 
     * @param bondStatCde the value of field 'bondStatCde'.
     */
    public void setBondStatCde(
            final java.lang.String bondStatCde) {
        this._bondStatCde = bondStatCde;
    }

    /**
     * Sets the value of field 'coupnAnnlRt'.
     * 
     * @param coupnAnnlRt the value of field 'coupnAnnlRt'.
     */
    public void setCoupnAnnlRt(
            final java.lang.String coupnAnnlRt) {
        this._coupnAnnlRt = coupnAnnlRt;
    }

    /**
     * Sets the value of field 'coupnAnnlText'.
     * 
     * @param coupnAnnlText the value of field 'coupnAnnlText'.
     */
    public void setCoupnAnnlText(
            final java.lang.String coupnAnnlText) {
        this._coupnAnnlText = coupnAnnlText;
    }

    /**
     * Sets the value of field 'coupnCurrStartDt'.
     * 
     * @param coupnCurrStartDt the value of field 'coupnCurrStartDt'
     */
    public void setCoupnCurrStartDt(
            final java.lang.String coupnCurrStartDt) {
        this._coupnCurrStartDt = coupnCurrStartDt;
    }

    /**
     * Sets the value of field 'coupnExtInstmRt'.
     * 
     * @param coupnExtInstmRt the value of field 'coupnExtInstmRt'.
     */
    public void setCoupnExtInstmRt(
            final java.lang.String coupnExtInstmRt) {
        this._coupnExtInstmRt = coupnExtInstmRt;
    }

    /**
     * Sets the value of field 'coupnPrevRt'.
     * 
     * @param coupnPrevRt the value of field 'coupnPrevRt'.
     */
    public void setCoupnPrevRt(
            final java.lang.String coupnPrevRt) {
        this._coupnPrevRt = coupnPrevRt;
    }

    /**
     * Sets the value of field 'coupnPrevStartDt'.
     * 
     * @param coupnPrevStartDt the value of field 'coupnPrevStartDt'
     */
    public void setCoupnPrevStartDt(
            final java.lang.String coupnPrevStartDt) {
        this._coupnPrevStartDt = coupnPrevStartDt;
    }

    /**
     * Sets the value of field 'coupnType'.
     * 
     * @param coupnType the value of field 'coupnType'.
     */
    public void setCoupnType(
            final java.lang.String coupnType) {
        this._coupnType = coupnType;
    }

    /**
     * Sets the value of field 'cptlTierText'.
     * 
     * @param cptlTierText the value of field 'cptlTierText'.
     */
    public void setCptlTierText(
            final java.lang.String cptlTierText) {
        this._cptlTierText = cptlTierText;
    }

    /**
     * 
     * 
     * @param index
     * @param vCreditRtingSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCreditRtingSeg(
            final int index,
            final com.dummy.wpc.batch.xml.CreditRtingSeg vCreditRtingSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._creditRtingSegList.size()) {
            throw new IndexOutOfBoundsException("setCreditRtingSeg: Index value '" + index + "' not in range [0.." + (this._creditRtingSegList.size() - 1) + "]");
        }
        
        this._creditRtingSegList.set(index, vCreditRtingSeg);
    }

    /**
     * 
     * 
     * @param vCreditRtingSegArray
     */
    public void setCreditRtingSeg(
            final com.dummy.wpc.batch.xml.CreditRtingSeg[] vCreditRtingSegArray) {
        //-- copy array
        _creditRtingSegList.clear();
        
        for (int i = 0; i < vCreditRtingSegArray.length; i++) {
                this._creditRtingSegList.add(vCreditRtingSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'ctryBondIssueCde'.
     * 
     * @param ctryBondIssueCde the value of field 'ctryBondIssueCde'
     */
    public void setCtryBondIssueCde(
            final java.lang.String ctryBondIssueCde) {
        this._ctryBondIssueCde = ctryBondIssueCde;
    }

    /**
     * Sets the value of field 'dscntMrgnBbuyPct'.
     * 
     * @param dscntMrgnBbuyPct the value of field 'dscntMrgnBbuyPct'
     */
    public void setDscntMrgnBbuyPct(
            final java.lang.String dscntMrgnBbuyPct) {
        this._dscntMrgnBbuyPct = dscntMrgnBbuyPct;
    }

    /**
     * Sets the value of field 'dscntMrgnBselPct'.
     * 
     * @param dscntMrgnBselPct the value of field 'dscntMrgnBselPct'
     */
    public void setDscntMrgnBselPct(
            final java.lang.String dscntMrgnBselPct) {
        this._dscntMrgnBselPct = dscntMrgnBselPct;
    }

    /**
     * Sets the value of field 'flexMatOptInd'.
     * 
     * @param flexMatOptInd the value of field 'flexMatOptInd'.
     */
    public void setFlexMatOptInd(
            final java.lang.String flexMatOptInd) {
        this._flexMatOptInd = flexMatOptInd;
    }

    /**
     * Sets the value of field 'grntrName'.
     * 
     * @param grntrName the value of field 'grntrName'.
     */
    public void setGrntrName(
            final java.lang.String grntrName) {
        this._grntrName = grntrName;
    }

    /**
     * Sets the value of field 'indexFltRtNme'.
     * 
     * @param indexFltRtNme the value of field 'indexFltRtNme'.
     */
    public void setIndexFltRtNme(
            final java.lang.String indexFltRtNme) {
        this._indexFltRtNme = indexFltRtNme;
    }

    /**
     * Sets the value of field 'intAccrDayCnt'.
     * 
     * @param intAccrDayCnt the value of field 'intAccrDayCnt'.
     */
    public void setIntAccrDayCnt(
            final java.lang.String intAccrDayCnt) {
        this._intAccrDayCnt = intAccrDayCnt;
    }

    /**
     * Sets the value of field 'intBassiCalcText'.
     * 
     * @param intBassiCalcText the value of field 'intBassiCalcText'
     */
    public void setIntBassiCalcText(
            final java.lang.String intBassiCalcText) {
        this._intBassiCalcText = intBassiCalcText;
    }

    /**
     * Sets the value of field 'intIndAccrAmt'.
     * 
     * @param intIndAccrAmt the value of field 'intIndAccrAmt'.
     */
    public void setIntIndAccrAmt(
            final java.lang.String intIndAccrAmt) {
        this._intIndAccrAmt = intIndAccrAmt;
    }

    /**
     * Sets the value of field 'invstIncMinAmt'.
     * 
     * @param invstIncMinAmt the value of field 'invstIncMinAmt'.
     */
    public void setInvstIncMinAmt(
            final java.lang.String invstIncMinAmt) {
        this._invstIncMinAmt = invstIncMinAmt;
    }

    /**
     * Sets the value of field 'invstIncrmSoldAmt'.
     * 
     * @param invstIncrmSoldAmt the value of field
     * 'invstIncrmSoldAmt'.
     */
    public void setInvstIncrmSoldAmt(
            final java.lang.String invstIncrmSoldAmt) {
        this._invstIncrmSoldAmt = invstIncrmSoldAmt;
    }

    /**
     * Sets the value of field 'invstSoldLestAmt'.
     * 
     * @param invstSoldLestAmt the value of field 'invstSoldLestAmt'
     */
    public void setInvstSoldLestAmt(
            final java.lang.String invstSoldLestAmt) {
        this._invstSoldLestAmt = invstSoldLestAmt;
    }

    /**
     * Sets the value of field 'isrBndNme'.
     * 
     * @param isrBndNme the value of field 'isrBndNme'.
     */
    public void setIsrBndNme(
            final java.lang.String isrBndNme) {
        this._isrBndNme = isrBndNme;
    }

    /**
     * Sets the value of field 'isrDesc'.
     * 
     * @param isrDesc the value of field 'isrDesc'.
     */
    public void setIsrDesc(
            final java.lang.String isrDesc) {
        this._isrDesc = isrDesc;
    }

    /**
     * Sets the value of field 'isrPllDesc'.
     * 
     * @param isrPllDesc the value of field 'isrPllDesc'.
     */
    public void setIsrPllDesc(
            final java.lang.String isrPllDesc) {
        this._isrPllDesc = isrPllDesc;
    }

    /**
     * Sets the value of field 'isrSllDesc'.
     * 
     * @param isrSllDesc the value of field 'isrSllDesc'.
     */
    public void setIsrSllDesc(
            final java.lang.String isrSllDesc) {
        this._isrSllDesc = isrSllDesc;
    }

    /**
     * Sets the value of field 'issueNum'.
     * 
     * @param issueNum the value of field 'issueNum'.
     */
    public void setIssueNum(
            final java.lang.String issueNum) {
        this._issueNum = issueNum;
    }

    /**
     * Sets the value of field 'mturExtDt'.
     * 
     * @param mturExtDt the value of field 'mturExtDt'.
     */
    public void setMturExtDt(
            final java.lang.String mturExtDt) {
        this._mturExtDt = mturExtDt;
    }

    /**
     * Sets the value of field 'pdcyCoupnPymtCd'.
     * 
     * @param pdcyCoupnPymtCd the value of field 'pdcyCoupnPymtCd'.
     */
    public void setPdcyCoupnPymtCd(
            final java.lang.String pdcyCoupnPymtCd) {
        this._pdcyCoupnPymtCd = pdcyCoupnPymtCd;
    }

    /**
     * Sets the value of field 'prcBondRecvDtTm'.
     * 
     * @param prcBondRecvDtTm the value of field 'prcBondRecvDtTm'.
     */
    public void setPrcBondRecvDtTm(
            final java.lang.String prcBondRecvDtTm) {
        this._prcBondRecvDtTm = prcBondRecvDtTm;
    }

    /**
     * Sets the value of field 'prodBodLotQtyCnt'.
     * 
     * @param prodBodLotQtyCnt the value of field 'prodBodLotQtyCnt'
     */
    public void setProdBodLotQtyCnt(
            final java.lang.String prodBodLotQtyCnt) {
        this._prodBodLotQtyCnt = prodBodLotQtyCnt;
    }

    /**
     * Sets the value of field 'prodClsBidPrcAmt'.
     * 
     * @param prodClsBidPrcAmt the value of field 'prodClsBidPrcAmt'
     */
    public void setProdClsBidPrcAmt(
            final java.lang.String prodClsBidPrcAmt) {
        this._prodClsBidPrcAmt = prodClsBidPrcAmt;
    }

    /**
     * Sets the value of field 'prodClsOffrPrcAmt'.
     * 
     * @param prodClsOffrPrcAmt the value of field
     * 'prodClsOffrPrcAmt'.
     */
    public void setProdClsOffrPrcAmt(
            final java.lang.String prodClsOffrPrcAmt) {
        this._prodClsOffrPrcAmt = prodClsOffrPrcAmt;
    }

    /**
     * Sets the value of field 'prodIssDt'.
     * 
     * @param prodIssDt the value of field 'prodIssDt'.
     */
    public void setProdIssDt(
            final java.lang.String prodIssDt) {
        this._prodIssDt = prodIssDt;
    }

    /**
     * Sets the value of field 'pymtCoupnNextDt'.
     * 
     * @param pymtCoupnNextDt the value of field 'pymtCoupnNextDt'.
     */
    public void setPymtCoupnNextDt(
            final java.lang.String pymtCoupnNextDt) {
        this._pymtCoupnNextDt = pymtCoupnNextDt;
    }

    /**
     * Sets the value of field 'shrBidCnt'.
     * 
     * @param shrBidCnt the value of field 'shrBidCnt'.
     */
    public void setShrBidCnt(
            final java.lang.String shrBidCnt) {
        this._shrBidCnt = shrBidCnt;
    }

    /**
     * Sets the value of field 'shrOffrCnt'.
     * 
     * @param shrOffrCnt the value of field 'shrOffrCnt'.
     */
    public void setShrOffrCnt(
            final java.lang.String shrOffrCnt) {
        this._shrOffrCnt = shrOffrCnt;
    }

    /**
     * Sets the value of field 'srTypeCde'.
     * 
     * @param srTypeCde the value of field 'srTypeCde'.
     */
    public void setSrTypeCde(
            final java.lang.String srTypeCde) {
        this._srTypeCde = srTypeCde;
    }

    /**
     * Sets the value of field 'subDebtInd'.
     * 
     * @param subDebtInd the value of field 'subDebtInd'.
     */
    public void setSubDebtInd(
            final java.lang.String subDebtInd) {
        this._subDebtInd = subDebtInd;
    }

    /**
     * 
     * 
     * @param index
     * @param vYieldHistSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setYieldHistSeg(
            final int index,
            final com.dummy.wpc.batch.xml.YieldHistSeg vYieldHistSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._yieldHistSegList.size()) {
            throw new IndexOutOfBoundsException("setYieldHistSeg: Index value '" + index + "' not in range [0.." + (this._yieldHistSegList.size() - 1) + "]");
        }
        
        this._yieldHistSegList.set(index, vYieldHistSeg);
    }

    /**
     * 
     * 
     * @param vYieldHistSegArray
     */
    public void setYieldHistSeg(
            final com.dummy.wpc.batch.xml.YieldHistSeg[] vYieldHistSegArray) {
        //-- copy array
        _yieldHistSegList.clear();
        
        for (int i = 0; i < vYieldHistSegArray.length; i++) {
                this._yieldHistSegList.add(vYieldHistSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'yieldOfferText'.
     * 
     * @param yieldOfferText the value of field 'yieldOfferText'.
     */
    public void setYieldOfferText(
            final java.lang.String yieldOfferText) {
        this._yieldOfferText = yieldOfferText;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.BondSegment
     */
    public static com.dummy.wpc.batch.xml.BondSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.BondSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.BondSegment.class, reader);
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
