/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProductPriceSegment.java 1.3 2011/12/06 19:07:52CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductPriceSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:52CST $
 */
public class ProductPriceSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prcEffDt.
     */
    private java.lang.String _prcEffDt;

    /**
     * Field _pdcyPrcCde.
     */
    private java.lang.String _pdcyPrcCde;

    /**
     * Field _prcInpDt.
     */
    private java.lang.String _prcInpDt;

    /**
     * Field _ccyProdMktPrcCde.
     */
    private java.lang.String _ccyProdMktPrcCde;

    /**
     * Field _prodBidPrcAmt.
     */
    private java.lang.String _prodBidPrcAmt;

    /**
     * Field _prodOffrPrcAmt.
     */
    private java.lang.String _prodOffrPrcAmt;

    /**
     * Field _prodNavPrcAmt.
     */
    private java.lang.String _prodNavPrcAmt;

    /**
     * Field _prodMktPrcAmt.
     */
    private java.lang.String _prodMktPrcAmt;

    /**
     * Field _prodMktPricePrevAmt.
     */
    private java.lang.String _prodMktPricePrevAmt;

    /**
     * Field _stockPrcSeg.
     */
    private com.dummy.wpc.batch.xml.StockPrcSeg _stockPrcSeg;

    /**
     * Field _recDtTmSeg.
     */
    private com.dummy.wpc.batch.xml.RecDtTmSeg _recDtTmSeg;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductPriceSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ccyProdMktPrcCde'.
     * 
     * @return the value of field 'CcyProdMktPrcCde'.
     */
    public java.lang.String getCcyProdMktPrcCde(
    ) {
        return this._ccyProdMktPrcCde;
    }

    /**
     * Returns the value of field 'pdcyPrcCde'.
     * 
     * @return the value of field 'PdcyPrcCde'.
     */
    public java.lang.String getPdcyPrcCde(
    ) {
        return this._pdcyPrcCde;
    }

    /**
     * Returns the value of field 'prcEffDt'.
     * 
     * @return the value of field 'PrcEffDt'.
     */
    public java.lang.String getPrcEffDt(
    ) {
        return this._prcEffDt;
    }

    /**
     * Returns the value of field 'prcInpDt'.
     * 
     * @return the value of field 'PrcInpDt'.
     */
    public java.lang.String getPrcInpDt(
    ) {
        return this._prcInpDt;
    }

    /**
     * Returns the value of field 'prodBidPrcAmt'.
     * 
     * @return the value of field 'ProdBidPrcAmt'.
     */
    public java.lang.String getProdBidPrcAmt(
    ) {
        return this._prodBidPrcAmt;
    }

    /**
     * Returns the value of field 'prodMktPrcAmt'.
     * 
     * @return the value of field 'ProdMktPrcAmt'.
     */
    public java.lang.String getProdMktPrcAmt(
    ) {
        return this._prodMktPrcAmt;
    }

    /**
     * Returns the value of field 'prodMktPricePrevAmt'.
     * 
     * @return the value of field 'ProdMktPricePrevAmt'.
     */
    public java.lang.String getProdMktPricePrevAmt(
    ) {
        return this._prodMktPricePrevAmt;
    }

    /**
     * Returns the value of field 'prodNavPrcAmt'.
     * 
     * @return the value of field 'ProdNavPrcAmt'.
     */
    public java.lang.String getProdNavPrcAmt(
    ) {
        return this._prodNavPrcAmt;
    }

    /**
     * Returns the value of field 'prodOffrPrcAmt'.
     * 
     * @return the value of field 'ProdOffrPrcAmt'.
     */
    public java.lang.String getProdOffrPrcAmt(
    ) {
        return this._prodOffrPrcAmt;
    }

    /**
     * Returns the value of field 'recDtTmSeg'.
     * 
     * @return the value of field 'RecDtTmSeg'.
     */
    public com.dummy.wpc.batch.xml.RecDtTmSeg getRecDtTmSeg(
    ) {
        return this._recDtTmSeg;
    }

    /**
     * Returns the value of field 'stockPrcSeg'.
     * 
     * @return the value of field 'StockPrcSeg'.
     */
    public com.dummy.wpc.batch.xml.StockPrcSeg getStockPrcSeg(
    ) {
        return this._stockPrcSeg;
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
     * Sets the value of field 'ccyProdMktPrcCde'.
     * 
     * @param ccyProdMktPrcCde the value of field 'ccyProdMktPrcCde'
     */
    public void setCcyProdMktPrcCde(
            final java.lang.String ccyProdMktPrcCde) {
        this._ccyProdMktPrcCde = ccyProdMktPrcCde;
    }

    /**
     * Sets the value of field 'pdcyPrcCde'.
     * 
     * @param pdcyPrcCde the value of field 'pdcyPrcCde'.
     */
    public void setPdcyPrcCde(
            final java.lang.String pdcyPrcCde) {
        this._pdcyPrcCde = pdcyPrcCde;
    }

    /**
     * Sets the value of field 'prcEffDt'.
     * 
     * @param prcEffDt the value of field 'prcEffDt'.
     */
    public void setPrcEffDt(
            final java.lang.String prcEffDt) {
        this._prcEffDt = prcEffDt;
    }

    /**
     * Sets the value of field 'prcInpDt'.
     * 
     * @param prcInpDt the value of field 'prcInpDt'.
     */
    public void setPrcInpDt(
            final java.lang.String prcInpDt) {
        this._prcInpDt = prcInpDt;
    }

    /**
     * Sets the value of field 'prodBidPrcAmt'.
     * 
     * @param prodBidPrcAmt the value of field 'prodBidPrcAmt'.
     */
    public void setProdBidPrcAmt(
            final java.lang.String prodBidPrcAmt) {
        this._prodBidPrcAmt = prodBidPrcAmt;
    }

    /**
     * Sets the value of field 'prodMktPrcAmt'.
     * 
     * @param prodMktPrcAmt the value of field 'prodMktPrcAmt'.
     */
    public void setProdMktPrcAmt(
            final java.lang.String prodMktPrcAmt) {
        this._prodMktPrcAmt = prodMktPrcAmt;
    }

    /**
     * Sets the value of field 'prodMktPricePrevAmt'.
     * 
     * @param prodMktPricePrevAmt the value of field
     * 'prodMktPricePrevAmt'.
     */
    public void setProdMktPricePrevAmt(
            final java.lang.String prodMktPricePrevAmt) {
        this._prodMktPricePrevAmt = prodMktPricePrevAmt;
    }

    /**
     * Sets the value of field 'prodNavPrcAmt'.
     * 
     * @param prodNavPrcAmt the value of field 'prodNavPrcAmt'.
     */
    public void setProdNavPrcAmt(
            final java.lang.String prodNavPrcAmt) {
        this._prodNavPrcAmt = prodNavPrcAmt;
    }

    /**
     * Sets the value of field 'prodOffrPrcAmt'.
     * 
     * @param prodOffrPrcAmt the value of field 'prodOffrPrcAmt'.
     */
    public void setProdOffrPrcAmt(
            final java.lang.String prodOffrPrcAmt) {
        this._prodOffrPrcAmt = prodOffrPrcAmt;
    }

    /**
     * Sets the value of field 'recDtTmSeg'.
     * 
     * @param recDtTmSeg the value of field 'recDtTmSeg'.
     */
    public void setRecDtTmSeg(
            final com.dummy.wpc.batch.xml.RecDtTmSeg recDtTmSeg) {
        this._recDtTmSeg = recDtTmSeg;
    }

    /**
     * Sets the value of field 'stockPrcSeg'.
     * 
     * @param stockPrcSeg the value of field 'stockPrcSeg'.
     */
    public void setStockPrcSeg(
            final com.dummy.wpc.batch.xml.StockPrcSeg stockPrcSeg) {
        this._stockPrcSeg = stockPrcSeg;
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
     * com.dummy.wpc.batch.xml.ProductPriceSegment
     */
    public static com.dummy.wpc.batch.xml.ProductPriceSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProductPriceSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProductPriceSegment.class, reader);
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
