/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/StockPriceSegment.java 1.3 2011/12/06 19:07:51CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class StockPriceSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:51CST $
 */
public class StockPriceSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodPrcChngPct.
     */
    private java.lang.String _prodPrcChngPct;

    /**
     * Field _prodPrcChngAmt.
     */
    private java.lang.String _prodPrcChngAmt;

    /**
     * Field _prodOpenPrcAmt.
     */
    private java.lang.String _prodOpenPrcAmt;

    /**
     * Field _prodTdyHighPrcAmt.
     */
    private java.lang.String _prodTdyHighPrcAmt;

    /**
     * Field _prodTdyLowPrcAmt.
     */
    private java.lang.String _prodTdyLowPrcAmt;

    /**
     * Field _shareTrdCnt.
     */
    private java.lang.String _shareTrdCnt;

    /**
     * Field _prodTrnvrAmt.
     */
    private java.lang.String _prodTrnvrAmt;

    /**
     * Field _prodPrc52WeekMaxAmt.
     */
    private java.lang.String _prodPrc52WeekMaxAmt;

    /**
     * Field _prodPrc52WeekMinAmt.
     */
    private java.lang.String _prodPrc52WeekMinAmt;

    /**
     * Field _prcEarnRate.
     */
    private java.lang.String _prcEarnRate;

    /**
     * Field _divPct.
     */
    private java.lang.String _divPct;


      //----------------/
     //- Constructors -/
    //----------------/

    public StockPriceSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'divPct'.
     * 
     * @return the value of field 'DivPct'.
     */
    public java.lang.String getDivPct(
    ) {
        return this._divPct;
    }

    /**
     * Returns the value of field 'prcEarnRate'.
     * 
     * @return the value of field 'PrcEarnRate'.
     */
    public java.lang.String getPrcEarnRate(
    ) {
        return this._prcEarnRate;
    }

    /**
     * Returns the value of field 'prodOpenPrcAmt'.
     * 
     * @return the value of field 'ProdOpenPrcAmt'.
     */
    public java.lang.String getProdOpenPrcAmt(
    ) {
        return this._prodOpenPrcAmt;
    }

    /**
     * Returns the value of field 'prodPrc52WeekMaxAmt'.
     * 
     * @return the value of field 'ProdPrc52WeekMaxAmt'.
     */
    public java.lang.String getProdPrc52WeekMaxAmt(
    ) {
        return this._prodPrc52WeekMaxAmt;
    }

    /**
     * Returns the value of field 'prodPrc52WeekMinAmt'.
     * 
     * @return the value of field 'ProdPrc52WeekMinAmt'.
     */
    public java.lang.String getProdPrc52WeekMinAmt(
    ) {
        return this._prodPrc52WeekMinAmt;
    }

    /**
     * Returns the value of field 'prodPrcChngAmt'.
     * 
     * @return the value of field 'ProdPrcChngAmt'.
     */
    public java.lang.String getProdPrcChngAmt(
    ) {
        return this._prodPrcChngAmt;
    }

    /**
     * Returns the value of field 'prodPrcChngPct'.
     * 
     * @return the value of field 'ProdPrcChngPct'.
     */
    public java.lang.String getProdPrcChngPct(
    ) {
        return this._prodPrcChngPct;
    }

    /**
     * Returns the value of field 'prodTdyHighPrcAmt'.
     * 
     * @return the value of field 'ProdTdyHighPrcAmt'.
     */
    public java.lang.String getProdTdyHighPrcAmt(
    ) {
        return this._prodTdyHighPrcAmt;
    }

    /**
     * Returns the value of field 'prodTdyLowPrcAmt'.
     * 
     * @return the value of field 'ProdTdyLowPrcAmt'.
     */
    public java.lang.String getProdTdyLowPrcAmt(
    ) {
        return this._prodTdyLowPrcAmt;
    }

    /**
     * Returns the value of field 'prodTrnvrAmt'.
     * 
     * @return the value of field 'ProdTrnvrAmt'.
     */
    public java.lang.String getProdTrnvrAmt(
    ) {
        return this._prodTrnvrAmt;
    }

    /**
     * Returns the value of field 'shareTrdCnt'.
     * 
     * @return the value of field 'ShareTrdCnt'.
     */
    public java.lang.String getShareTrdCnt(
    ) {
        return this._shareTrdCnt;
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
     * Sets the value of field 'divPct'.
     * 
     * @param divPct the value of field 'divPct'.
     */
    public void setDivPct(
            final java.lang.String divPct) {
        this._divPct = divPct;
    }

    /**
     * Sets the value of field 'prcEarnRate'.
     * 
     * @param prcEarnRate the value of field 'prcEarnRate'.
     */
    public void setPrcEarnRate(
            final java.lang.String prcEarnRate) {
        this._prcEarnRate = prcEarnRate;
    }

    /**
     * Sets the value of field 'prodOpenPrcAmt'.
     * 
     * @param prodOpenPrcAmt the value of field 'prodOpenPrcAmt'.
     */
    public void setProdOpenPrcAmt(
            final java.lang.String prodOpenPrcAmt) {
        this._prodOpenPrcAmt = prodOpenPrcAmt;
    }

    /**
     * Sets the value of field 'prodPrc52WeekMaxAmt'.
     * 
     * @param prodPrc52WeekMaxAmt the value of field
     * 'prodPrc52WeekMaxAmt'.
     */
    public void setProdPrc52WeekMaxAmt(
            final java.lang.String prodPrc52WeekMaxAmt) {
        this._prodPrc52WeekMaxAmt = prodPrc52WeekMaxAmt;
    }

    /**
     * Sets the value of field 'prodPrc52WeekMinAmt'.
     * 
     * @param prodPrc52WeekMinAmt the value of field
     * 'prodPrc52WeekMinAmt'.
     */
    public void setProdPrc52WeekMinAmt(
            final java.lang.String prodPrc52WeekMinAmt) {
        this._prodPrc52WeekMinAmt = prodPrc52WeekMinAmt;
    }

    /**
     * Sets the value of field 'prodPrcChngAmt'.
     * 
     * @param prodPrcChngAmt the value of field 'prodPrcChngAmt'.
     */
    public void setProdPrcChngAmt(
            final java.lang.String prodPrcChngAmt) {
        this._prodPrcChngAmt = prodPrcChngAmt;
    }

    /**
     * Sets the value of field 'prodPrcChngPct'.
     * 
     * @param prodPrcChngPct the value of field 'prodPrcChngPct'.
     */
    public void setProdPrcChngPct(
            final java.lang.String prodPrcChngPct) {
        this._prodPrcChngPct = prodPrcChngPct;
    }

    /**
     * Sets the value of field 'prodTdyHighPrcAmt'.
     * 
     * @param prodTdyHighPrcAmt the value of field
     * 'prodTdyHighPrcAmt'.
     */
    public void setProdTdyHighPrcAmt(
            final java.lang.String prodTdyHighPrcAmt) {
        this._prodTdyHighPrcAmt = prodTdyHighPrcAmt;
    }

    /**
     * Sets the value of field 'prodTdyLowPrcAmt'.
     * 
     * @param prodTdyLowPrcAmt the value of field 'prodTdyLowPrcAmt'
     */
    public void setProdTdyLowPrcAmt(
            final java.lang.String prodTdyLowPrcAmt) {
        this._prodTdyLowPrcAmt = prodTdyLowPrcAmt;
    }

    /**
     * Sets the value of field 'prodTrnvrAmt'.
     * 
     * @param prodTrnvrAmt the value of field 'prodTrnvrAmt'.
     */
    public void setProdTrnvrAmt(
            final java.lang.String prodTrnvrAmt) {
        this._prodTrnvrAmt = prodTrnvrAmt;
    }

    /**
     * Sets the value of field 'shareTrdCnt'.
     * 
     * @param shareTrdCnt the value of field 'shareTrdCnt'.
     */
    public void setShareTrdCnt(
            final java.lang.String shareTrdCnt) {
        this._shareTrdCnt = shareTrdCnt;
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
     * com.dummy.wpc.batch.xml.StockPriceSegment
     */
    public static com.dummy.wpc.batch.xml.StockPriceSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.StockPriceSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.StockPriceSegment.class, reader);
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
