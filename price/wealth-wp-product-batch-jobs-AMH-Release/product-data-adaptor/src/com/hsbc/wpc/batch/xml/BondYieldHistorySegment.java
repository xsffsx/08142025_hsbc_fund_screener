/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/BondYieldHistorySegment.java 1.3 2011/12/06 19:07:52CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class BondYieldHistorySegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:52CST $
 */
public class BondYieldHistorySegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _yieldTypeCde.
     */
    private java.lang.String _yieldTypeCde;

    /**
     * Field _yieldDt.
     */
    private java.lang.String _yieldDt;

    /**
     * Field _yieldEffDt.
     */
    private java.lang.String _yieldEffDt;

    /**
     * Field _yieldBidPct.
     */
    private java.lang.String _yieldBidPct;

    /**
     * Field _yieldToCallBidPct.
     */
    private java.lang.String _yieldToCallBidPct;

    /**
     * Field _yieldToMturBidPct.
     */
    private java.lang.String _yieldToMturBidPct;

    /**
     * Field _yieldBidClosePct.
     */
    private java.lang.String _yieldBidClosePct;

    /**
     * Field _yieldOfferPct.
     */
    private java.lang.String _yieldOfferPct;

    /**
     * Field _yieldToCallOfferPct.
     */
    private java.lang.String _yieldToCallOfferPct;

    /**
     * Field _yieldToMturOfferText.
     */
    private java.lang.String _yieldToMturOfferText;

    /**
     * Field _yieldOfferClosePct.
     */
    private java.lang.String _yieldOfferClosePct;


      //----------------/
     //- Constructors -/
    //----------------/

    public BondYieldHistorySegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'yieldBidClosePct'.
     * 
     * @return the value of field 'YieldBidClosePct'.
     */
    public java.lang.String getYieldBidClosePct(
    ) {
        return this._yieldBidClosePct;
    }

    /**
     * Returns the value of field 'yieldBidPct'.
     * 
     * @return the value of field 'YieldBidPct'.
     */
    public java.lang.String getYieldBidPct(
    ) {
        return this._yieldBidPct;
    }

    /**
     * Returns the value of field 'yieldDt'.
     * 
     * @return the value of field 'YieldDt'.
     */
    public java.lang.String getYieldDt(
    ) {
        return this._yieldDt;
    }

    /**
     * Returns the value of field 'yieldEffDt'.
     * 
     * @return the value of field 'YieldEffDt'.
     */
    public java.lang.String getYieldEffDt(
    ) {
        return this._yieldEffDt;
    }

    /**
     * Returns the value of field 'yieldOfferClosePct'.
     * 
     * @return the value of field 'YieldOfferClosePct'.
     */
    public java.lang.String getYieldOfferClosePct(
    ) {
        return this._yieldOfferClosePct;
    }

    /**
     * Returns the value of field 'yieldOfferPct'.
     * 
     * @return the value of field 'YieldOfferPct'.
     */
    public java.lang.String getYieldOfferPct(
    ) {
        return this._yieldOfferPct;
    }

    /**
     * Returns the value of field 'yieldToCallBidPct'.
     * 
     * @return the value of field 'YieldToCallBidPct'.
     */
    public java.lang.String getYieldToCallBidPct(
    ) {
        return this._yieldToCallBidPct;
    }

    /**
     * Returns the value of field 'yieldToCallOfferPct'.
     * 
     * @return the value of field 'YieldToCallOfferPct'.
     */
    public java.lang.String getYieldToCallOfferPct(
    ) {
        return this._yieldToCallOfferPct;
    }

    /**
     * Returns the value of field 'yieldToMturBidPct'.
     * 
     * @return the value of field 'YieldToMturBidPct'.
     */
    public java.lang.String getYieldToMturBidPct(
    ) {
        return this._yieldToMturBidPct;
    }

    /**
     * Returns the value of field 'yieldToMturOfferText'.
     * 
     * @return the value of field 'YieldToMturOfferText'.
     */
    public java.lang.String getYieldToMturOfferText(
    ) {
        return this._yieldToMturOfferText;
    }

    /**
     * Returns the value of field 'yieldTypeCde'.
     * 
     * @return the value of field 'YieldTypeCde'.
     */
    public java.lang.String getYieldTypeCde(
    ) {
        return this._yieldTypeCde;
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
     * Sets the value of field 'yieldBidClosePct'.
     * 
     * @param yieldBidClosePct the value of field 'yieldBidClosePct'
     */
    public void setYieldBidClosePct(
            final java.lang.String yieldBidClosePct) {
        this._yieldBidClosePct = yieldBidClosePct;
    }

    /**
     * Sets the value of field 'yieldBidPct'.
     * 
     * @param yieldBidPct the value of field 'yieldBidPct'.
     */
    public void setYieldBidPct(
            final java.lang.String yieldBidPct) {
        this._yieldBidPct = yieldBidPct;
    }

    /**
     * Sets the value of field 'yieldDt'.
     * 
     * @param yieldDt the value of field 'yieldDt'.
     */
    public void setYieldDt(
            final java.lang.String yieldDt) {
        this._yieldDt = yieldDt;
    }

    /**
     * Sets the value of field 'yieldEffDt'.
     * 
     * @param yieldEffDt the value of field 'yieldEffDt'.
     */
    public void setYieldEffDt(
            final java.lang.String yieldEffDt) {
        this._yieldEffDt = yieldEffDt;
    }

    /**
     * Sets the value of field 'yieldOfferClosePct'.
     * 
     * @param yieldOfferClosePct the value of field
     * 'yieldOfferClosePct'.
     */
    public void setYieldOfferClosePct(
            final java.lang.String yieldOfferClosePct) {
        this._yieldOfferClosePct = yieldOfferClosePct;
    }

    /**
     * Sets the value of field 'yieldOfferPct'.
     * 
     * @param yieldOfferPct the value of field 'yieldOfferPct'.
     */
    public void setYieldOfferPct(
            final java.lang.String yieldOfferPct) {
        this._yieldOfferPct = yieldOfferPct;
    }

    /**
     * Sets the value of field 'yieldToCallBidPct'.
     * 
     * @param yieldToCallBidPct the value of field
     * 'yieldToCallBidPct'.
     */
    public void setYieldToCallBidPct(
            final java.lang.String yieldToCallBidPct) {
        this._yieldToCallBidPct = yieldToCallBidPct;
    }

    /**
     * Sets the value of field 'yieldToCallOfferPct'.
     * 
     * @param yieldToCallOfferPct the value of field
     * 'yieldToCallOfferPct'.
     */
    public void setYieldToCallOfferPct(
            final java.lang.String yieldToCallOfferPct) {
        this._yieldToCallOfferPct = yieldToCallOfferPct;
    }

    /**
     * Sets the value of field 'yieldToMturBidPct'.
     * 
     * @param yieldToMturBidPct the value of field
     * 'yieldToMturBidPct'.
     */
    public void setYieldToMturBidPct(
            final java.lang.String yieldToMturBidPct) {
        this._yieldToMturBidPct = yieldToMturBidPct;
    }

    /**
     * Sets the value of field 'yieldToMturOfferText'.
     * 
     * @param yieldToMturOfferText the value of field
     * 'yieldToMturOfferText'.
     */
    public void setYieldToMturOfferText(
            final java.lang.String yieldToMturOfferText) {
        this._yieldToMturOfferText = yieldToMturOfferText;
    }

    /**
     * Sets the value of field 'yieldTypeCde'.
     * 
     * @param yieldTypeCde the value of field 'yieldTypeCde'.
     */
    public void setYieldTypeCde(
            final java.lang.String yieldTypeCde) {
        this._yieldTypeCde = yieldTypeCde;
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
     * com.dummy.wpc.batch.xml.BondYieldHistorySegment
     */
    public static com.dummy.wpc.batch.xml.BondYieldHistorySegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.BondYieldHistorySegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.BondYieldHistorySegment.class, reader);
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
