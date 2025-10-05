/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/SidSegment.java 1.4.2.2 2012/12/28 10:21:27CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SidSegment.
 * 
 * @version $Revision: 1.4.2.2 $ $Date: 2012/12/28 10:21:27CST $
 */
public class SidSegment implements java.io.Serializable {


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
     * Field _prodConvCde.
     */
    private java.lang.String _prodConvCde;

    /**
     * Field _rtrnIntrmPrevPct.
     */
    private java.lang.String _rtrnIntrmPrevPct;

    /**
     * Field _rtrnIntrmPaidPrevDt.
     */
    private java.lang.String _rtrnIntrmPaidPrevDt;

    /**
     * Field _rtrnIntrmPaidNextDt.
     */
    private java.lang.String _rtrnIntrmPaidNextDt;

    /**
     * Field _ccyLinkDepstCde.
     */
    private java.lang.String _ccyLinkDepstCde;

    /**
     * Field _mktStartDt.
     */
    private java.lang.String _mktStartDt;

    /**
     * Field _mktEndDt.
     */
    private java.lang.String _mktEndDt;

    /**
     * Field _yieldAnnlMinPct.
     */
    private java.lang.String _yieldAnnlMinPct;

    /**
     * Field _yieldAnnlPotenPct.
     */
    private java.lang.String _yieldAnnlPotenPct;

    /**
     * Field _allowEarlyRdmInd.
     */
    private java.lang.String _allowEarlyRdmInd;

    /**
     * Field _rdmEarlyDalwText.
     */
    private java.lang.String _rdmEarlyDalwText;

    /**
     * Field _rdmEarlyIndAmt.
     */
    private java.lang.String _rdmEarlyIndAmt;

    /**
     * Field _offerTypeCde.
     */
    private java.lang.String _offerTypeCde;

    /**
     * Field _custSellQtaNum.
     */
    private java.lang.String _custSellQtaNum;

    /**
     * Field _ruleQtaAltmtCde.
     */
    private java.lang.String _ruleQtaAltmtCde;

    /**
     * Field _bonusIntCalcTypeCde.
     */
    private java.lang.String _bonusIntCalcTypeCde;

    /**
     * Field _bonusIntDtTypeCde.
     */
    private java.lang.String _bonusIntDtTypeCde;

    /**
     * Field _cptlProtcPct.
     */
    private java.lang.String _cptlProtcPct;

    /**
     * Field _lnchProdInd.
     */
    private java.lang.String _lnchProdInd;

    /**
     * Field _rtrvProdExtnlInd.
     */
    private java.lang.String _rtrvProdExtnlInd;

    /**
     * Field _sidRtrnSegList.
     */
    private java.util.Vector _sidRtrnSegList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SidSegment() {
        super();
        this._sidRtrnSegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSidRtrnSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSidRtrnSeg(
            final com.dummy.wpc.batch.xml.SidRtrnSeg vSidRtrnSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._sidRtrnSegList.addElement(vSidRtrnSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vSidRtrnSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSidRtrnSeg(
            final int index,
            final com.dummy.wpc.batch.xml.SidRtrnSeg vSidRtrnSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._sidRtrnSegList.add(index, vSidRtrnSeg);
    }

    /**
     * Method enumerateSidRtrnSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.SidRtrnSeg elements
     */
    public java.util.Enumeration enumerateSidRtrnSeg(
    ) {
        return this._sidRtrnSegList.elements();
    }

    /**
     * Returns the value of field 'allowEarlyRdmInd'.
     * 
     * @return the value of field 'AllowEarlyRdmInd'.
     */
    public java.lang.String getAllowEarlyRdmInd(
    ) {
        return this._allowEarlyRdmInd;
    }

    /**
     * Returns the value of field 'bonusIntCalcTypeCde'.
     * 
     * @return the value of field 'BonusIntCalcTypeCde'.
     */
    public java.lang.String getBonusIntCalcTypeCde(
    ) {
        return this._bonusIntCalcTypeCde;
    }

    /**
     * Returns the value of field 'bonusIntDtTypeCde'.
     * 
     * @return the value of field 'BonusIntDtTypeCde'.
     */
    public java.lang.String getBonusIntDtTypeCde(
    ) {
        return this._bonusIntDtTypeCde;
    }

    /**
     * Returns the value of field 'ccyLinkDepstCde'.
     * 
     * @return the value of field 'CcyLinkDepstCde'.
     */
    public java.lang.String getCcyLinkDepstCde(
    ) {
        return this._ccyLinkDepstCde;
    }

    /**
     * Returns the value of field 'cptlProtcPct'.
     * 
     * @return the value of field 'CptlProtcPct'.
     */
    public java.lang.String getCptlProtcPct(
    ) {
        return this._cptlProtcPct;
    }

    /**
     * Returns the value of field 'custSellQtaNum'.
     * 
     * @return the value of field 'CustSellQtaNum'.
     */
    public java.lang.String getCustSellQtaNum(
    ) {
        return this._custSellQtaNum;
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
     * Returns the value of field 'mktEndDt'.
     * 
     * @return the value of field 'MktEndDt'.
     */
    public java.lang.String getMktEndDt(
    ) {
        return this._mktEndDt;
    }

    /**
     * Returns the value of field 'mktStartDt'.
     * 
     * @return the value of field 'MktStartDt'.
     */
    public java.lang.String getMktStartDt(
    ) {
        return this._mktStartDt;
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
     * Returns the value of field 'prodConvCde'.
     * 
     * @return the value of field 'ProdConvCde'.
     */
    public java.lang.String getProdConvCde(
    ) {
        return this._prodConvCde;
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
     * Returns the value of field 'rdmEarlyDalwText'.
     * 
     * @return the value of field 'RdmEarlyDalwText'.
     */
    public java.lang.String getRdmEarlyDalwText(
    ) {
        return this._rdmEarlyDalwText;
    }

    /**
     * Returns the value of field 'rdmEarlyIndAmt'.
     * 
     * @return the value of field 'RdmEarlyIndAmt'.
     */
    public java.lang.String getRdmEarlyIndAmt(
    ) {
        return this._rdmEarlyIndAmt;
    }

    /**
     * Returns the value of field 'rtrnIntrmPaidNextDt'.
     * 
     * @return the value of field 'RtrnIntrmPaidNextDt'.
     */
    public java.lang.String getRtrnIntrmPaidNextDt(
    ) {
        return this._rtrnIntrmPaidNextDt;
    }

    /**
     * Returns the value of field 'rtrnIntrmPaidPrevDt'.
     * 
     * @return the value of field 'RtrnIntrmPaidPrevDt'.
     */
    public java.lang.String getRtrnIntrmPaidPrevDt(
    ) {
        return this._rtrnIntrmPaidPrevDt;
    }

    /**
     * Returns the value of field 'rtrnIntrmPrevPct'.
     * 
     * @return the value of field 'RtrnIntrmPrevPct'.
     */
    public java.lang.String getRtrnIntrmPrevPct(
    ) {
        return this._rtrnIntrmPrevPct;
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
     * Method getSidRtrnSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.SidRtrnSeg
     * at the given index
     */
    public com.dummy.wpc.batch.xml.SidRtrnSeg getSidRtrnSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._sidRtrnSegList.size()) {
            throw new IndexOutOfBoundsException("getSidRtrnSeg: Index value '" + index + "' not in range [0.." + (this._sidRtrnSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.SidRtrnSeg) _sidRtrnSegList.get(index);
    }

    /**
     * Method getSidRtrnSeg.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.SidRtrnSeg[] getSidRtrnSeg(
    ) {
        com.dummy.wpc.batch.xml.SidRtrnSeg[] array = new com.dummy.wpc.batch.xml.SidRtrnSeg[0];
        return (com.dummy.wpc.batch.xml.SidRtrnSeg[]) this._sidRtrnSegList.toArray(array);
    }

    /**
     * Method getSidRtrnSegCount.
     * 
     * @return the size of this collection
     */
    public int getSidRtrnSegCount(
    ) {
        return this._sidRtrnSegList.size();
    }

    /**
     * Returns the value of field 'yieldAnnlMinPct'.
     * 
     * @return the value of field 'YieldAnnlMinPct'.
     */
    public java.lang.String getYieldAnnlMinPct(
    ) {
        return this._yieldAnnlMinPct;
    }

    /**
     * Returns the value of field 'yieldAnnlPotenPct'.
     * 
     * @return the value of field 'YieldAnnlPotenPct'.
     */
    public java.lang.String getYieldAnnlPotenPct(
    ) {
        return this._yieldAnnlPotenPct;
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
    public void removeAllSidRtrnSeg(
    ) {
        this._sidRtrnSegList.clear();
    }

    /**
     * Method removeSidRtrnSeg.
     * 
     * @param vSidRtrnSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeSidRtrnSeg(
            final com.dummy.wpc.batch.xml.SidRtrnSeg vSidRtrnSeg) {
        boolean removed = _sidRtrnSegList.remove(vSidRtrnSeg);
        return removed;
    }

    /**
     * Method removeSidRtrnSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.SidRtrnSeg removeSidRtrnSegAt(
            final int index) {
        java.lang.Object obj = this._sidRtrnSegList.remove(index);
        return (com.dummy.wpc.batch.xml.SidRtrnSeg) obj;
    }

    /**
     * Sets the value of field 'allowEarlyRdmInd'.
     * 
     * @param allowEarlyRdmInd the value of field 'allowEarlyRdmInd'
     */
    public void setAllowEarlyRdmInd(
            final java.lang.String allowEarlyRdmInd) {
        this._allowEarlyRdmInd = allowEarlyRdmInd;
    }

    /**
     * Sets the value of field 'bonusIntCalcTypeCde'.
     * 
     * @param bonusIntCalcTypeCde the value of field
     * 'bonusIntCalcTypeCde'.
     */
    public void setBonusIntCalcTypeCde(
            final java.lang.String bonusIntCalcTypeCde) {
        this._bonusIntCalcTypeCde = bonusIntCalcTypeCde;
    }

    /**
     * Sets the value of field 'bonusIntDtTypeCde'.
     * 
     * @param bonusIntDtTypeCde the value of field
     * 'bonusIntDtTypeCde'.
     */
    public void setBonusIntDtTypeCde(
            final java.lang.String bonusIntDtTypeCde) {
        this._bonusIntDtTypeCde = bonusIntDtTypeCde;
    }

    /**
     * Sets the value of field 'ccyLinkDepstCde'.
     * 
     * @param ccyLinkDepstCde the value of field 'ccyLinkDepstCde'.
     */
    public void setCcyLinkDepstCde(
            final java.lang.String ccyLinkDepstCde) {
        this._ccyLinkDepstCde = ccyLinkDepstCde;
    }

    /**
     * Sets the value of field 'cptlProtcPct'.
     * 
     * @param cptlProtcPct the value of field 'cptlProtcPct'.
     */
    public void setCptlProtcPct(
            final java.lang.String cptlProtcPct) {
        this._cptlProtcPct = cptlProtcPct;
    }

    /**
     * Sets the value of field 'custSellQtaNum'.
     * 
     * @param custSellQtaNum the value of field 'custSellQtaNum'.
     */
    public void setCustSellQtaNum(
            final java.lang.String custSellQtaNum) {
        this._custSellQtaNum = custSellQtaNum;
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
     * Sets the value of field 'mktEndDt'.
     * 
     * @param mktEndDt the value of field 'mktEndDt'.
     */
    public void setMktEndDt(
            final java.lang.String mktEndDt) {
        this._mktEndDt = mktEndDt;
    }

    /**
     * Sets the value of field 'mktStartDt'.
     * 
     * @param mktStartDt the value of field 'mktStartDt'.
     */
    public void setMktStartDt(
            final java.lang.String mktStartDt) {
        this._mktStartDt = mktStartDt;
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
     * Sets the value of field 'prodConvCde'.
     * 
     * @param prodConvCde the value of field 'prodConvCde'.
     */
    public void setProdConvCde(
            final java.lang.String prodConvCde) {
        this._prodConvCde = prodConvCde;
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
     * Sets the value of field 'rdmEarlyDalwText'.
     * 
     * @param rdmEarlyDalwText the value of field 'rdmEarlyDalwText'
     */
    public void setRdmEarlyDalwText(
            final java.lang.String rdmEarlyDalwText) {
        this._rdmEarlyDalwText = rdmEarlyDalwText;
    }

    /**
     * Sets the value of field 'rdmEarlyIndAmt'.
     * 
     * @param rdmEarlyIndAmt the value of field 'rdmEarlyIndAmt'.
     */
    public void setRdmEarlyIndAmt(
            final java.lang.String rdmEarlyIndAmt) {
        this._rdmEarlyIndAmt = rdmEarlyIndAmt;
    }

    /**
     * Sets the value of field 'rtrnIntrmPaidNextDt'.
     * 
     * @param rtrnIntrmPaidNextDt the value of field
     * 'rtrnIntrmPaidNextDt'.
     */
    public void setRtrnIntrmPaidNextDt(
            final java.lang.String rtrnIntrmPaidNextDt) {
        this._rtrnIntrmPaidNextDt = rtrnIntrmPaidNextDt;
    }

    /**
     * Sets the value of field 'rtrnIntrmPaidPrevDt'.
     * 
     * @param rtrnIntrmPaidPrevDt the value of field
     * 'rtrnIntrmPaidPrevDt'.
     */
    public void setRtrnIntrmPaidPrevDt(
            final java.lang.String rtrnIntrmPaidPrevDt) {
        this._rtrnIntrmPaidPrevDt = rtrnIntrmPaidPrevDt;
    }

    /**
     * Sets the value of field 'rtrnIntrmPrevPct'.
     * 
     * @param rtrnIntrmPrevPct the value of field 'rtrnIntrmPrevPct'
     */
    public void setRtrnIntrmPrevPct(
            final java.lang.String rtrnIntrmPrevPct) {
        this._rtrnIntrmPrevPct = rtrnIntrmPrevPct;
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
     * 
     * 
     * @param index
     * @param vSidRtrnSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSidRtrnSeg(
            final int index,
            final com.dummy.wpc.batch.xml.SidRtrnSeg vSidRtrnSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._sidRtrnSegList.size()) {
            throw new IndexOutOfBoundsException("setSidRtrnSeg: Index value '" + index + "' not in range [0.." + (this._sidRtrnSegList.size() - 1) + "]");
        }
        
        this._sidRtrnSegList.set(index, vSidRtrnSeg);
    }

    /**
     * 
     * 
     * @param vSidRtrnSegArray
     */
    public void setSidRtrnSeg(
            final com.dummy.wpc.batch.xml.SidRtrnSeg[] vSidRtrnSegArray) {
        //-- copy array
        _sidRtrnSegList.clear();
        
        for (int i = 0; i < vSidRtrnSegArray.length; i++) {
                this._sidRtrnSegList.add(vSidRtrnSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'yieldAnnlMinPct'.
     * 
     * @param yieldAnnlMinPct the value of field 'yieldAnnlMinPct'.
     */
    public void setYieldAnnlMinPct(
            final java.lang.String yieldAnnlMinPct) {
        this._yieldAnnlMinPct = yieldAnnlMinPct;
    }

    /**
     * Sets the value of field 'yieldAnnlPotenPct'.
     * 
     * @param yieldAnnlPotenPct the value of field
     * 'yieldAnnlPotenPct'.
     */
    public void setYieldAnnlPotenPct(
            final java.lang.String yieldAnnlPotenPct) {
        this._yieldAnnlPotenPct = yieldAnnlPotenPct;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.SidSegment
     */
    public static com.dummy.wpc.batch.xml.SidSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.SidSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.SidSegment.class, reader);
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
