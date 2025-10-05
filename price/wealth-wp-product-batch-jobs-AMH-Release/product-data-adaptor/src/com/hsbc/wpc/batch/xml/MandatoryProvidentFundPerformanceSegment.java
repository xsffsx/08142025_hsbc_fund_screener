/*
 */
package com.dummy.wpc.batch.xml;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
public class MandatoryProvidentFundPerformanceSegment implements java.io.Serializable {


    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/


    /**
     * Field _perfmEffDt
     */
    private java.lang.String _perfmCumPct;

    /**
     * Field _perfm1moPct.
     */
    private java.lang.String _perfm1moPct;

    /**
     * Field _perfm6moPct.
     */
    private java.lang.String _perfm6moPct;

    /**
     * Field _perfm1yrPct.
     */
    private java.lang.String _perfm1yrPct;


    /**
     * Field _perfm3yrPct.
     */
    private java.lang.String _perfm3yrPct;

    /**
     * Field _perfm5yrPct.
     */
    private java.lang.String _perfm5yrPct;

    /**
     * Field _perfmSinceLnchPct.
     */
    private java.lang.String _perfmSinceLnchPct;

    /**
     * Field _perfmEffDt
     */
    private java.lang.String _perfmEffDt;

    /**
     * Field _disIndc
     */
    private java.lang.String _disIndc;


    /**
     * Field _recDtTmSeg.
     */
    private com.dummy.wpc.batch.xml.RecDtTmSeg _recDtTmSeg;


    // ----------------/
    // - Constructors -/
    // ----------------/

    public MandatoryProvidentFundPerformanceSegment() {
        super();
    }


    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Returns the value of field 'perfm1moPct'.
     * 
     * @return the value of field 'Perfm1moPct'.
     */
    public java.lang.String getPerfm1moPct() {
        return this._perfm1moPct;
    }


    /**
     * Returns the value of field 'perfm1yrPct'.
     * 
     * @return the value of field 'Perfm1yrPct'.
     */
    public java.lang.String getPerfm1yrPct() {
        return this._perfm1yrPct;
    }


    /**
     * Returns the value of field 'perfm3yrPct'.
     * 
     * @return the value of field 'Perfm3yrPct'.
     */
    public java.lang.String getPerfm3yrPct() {
        return this._perfm3yrPct;
    }

    /**
     * Returns the value of field 'perfm5yrPct'.
     * 
     * @return the value of field 'Perfm5yrPct'.
     */
    public java.lang.String getPerfm5yrPct() {
        return this._perfm5yrPct;
    }


    /**
     * Returns the value of field 'perfm6moPct'.
     * 
     * @return the value of field 'Perfm6moPct'.
     */
    public java.lang.String getPerfm6moPct() {
        return this._perfm6moPct;
    }


    /**
     * Returns the value of field 'perfmSinceLnchPct'.
     * 
     * @return the value of field 'PerfmSinceLnchPct'.
     */
    public java.lang.String getPerfmSinceLnchPct() {
        return this._perfmSinceLnchPct;
    }


    /**
     * Returns the value of field 'recDtTmSeg'.
     * 
     * @return the value of field 'RecDtTmSeg'.
     */
    public com.dummy.wpc.batch.xml.RecDtTmSeg getRecDtTmSeg() {
        return this._recDtTmSeg;
    }


    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
     * @throws org.exolab.castor.xml.MarshalException
     *             if object is null or if any SAXException is thrown during
     *             marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException
     *             if an IOException occurs during marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     * @throws org.exolab.castor.xml.MarshalException
     *             if object is null or if any SAXException is thrown during
     *             marshaling
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException,
        org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'perfm1moPct'.
     * 
     * @param perfm1moPct
     *            the value of field 'perfm1moPct'.
     */
    public void setPerfm1moPct(final java.lang.String perfm1moPct) {
        this._perfm1moPct = perfm1moPct;
    }


    /**
     * Sets the value of field 'perfm1yrPct'.
     * 
     * @param perfm1yrPct
     *            the value of field 'perfm1yrPct'.
     */
    public void setPerfm1yrPct(final java.lang.String perfm1yrPct) {
        this._perfm1yrPct = perfm1yrPct;
    }


    /**
     * Sets the value of field 'perfm3yrPct'.
     * 
     * @param perfm3yrPct
     *            the value of field 'perfm3yrPct'.
     */
    public void setPerfm3yrPct(final java.lang.String perfm3yrPct) {
        this._perfm3yrPct = perfm3yrPct;
    }

    /**
     * Sets the value of field 'perfm5yrPct'.
     * 
     * @param perfm5yrPct
     *            the value of field 'perfm5yrPct'.
     */
    public void setPerfm5yrPct(final java.lang.String perfm5yrPct) {
        this._perfm5yrPct = perfm5yrPct;
    }


    /**
     * Sets the value of field 'perfm6moPct'.
     * 
     * @param perfm6moPct
     *            the value of field 'perfm6moPct'.
     */
    public void setPerfm6moPct(final java.lang.String perfm6moPct) {
        this._perfm6moPct = perfm6moPct;
    }


    /**
     * Sets the value of field 'perfmSinceLnchPct'.
     * 
     * @param perfmSinceLnchPct
     *            the value of field 'perfmSinceLnchPct'.
     */
    public void setPerfmSinceLnchPct(final java.lang.String perfmSinceLnchPct) {
        this._perfmSinceLnchPct = perfmSinceLnchPct;
    }


    /**
     * Sets the value of field 'recDtTmSeg'.
     * 
     * @param recDtTmSeg
     *            the value of field 'recDtTmSeg'.
     */
    public void setRecDtTmSeg(final com.dummy.wpc.batch.xml.RecDtTmSeg recDtTmSeg) {
        this._recDtTmSeg = recDtTmSeg;
    }

    /**
     * @return the _perfmEffDt
     */
    public java.lang.String getPerfmEffDt() {
        return this._perfmEffDt;
    }


    /**
     * @param _perfmEffDt
     *            the _perfmEffDt to set
     */
    public void setPerfmEffDt(final java.lang.String _perfmEffDt) {
        this._perfmEffDt = _perfmEffDt;
    }

    /**
     * @return the _perfmCumPct
     */
    public java.lang.String getPerfmCumPct() {
        return this._perfmCumPct;
    }


    /**
     * @param _perfmCumPct
     *            the _perfmCumPct to set
     */
    public void setPerfmCumPct(final java.lang.String _perfmCumPct) {
        this._perfmCumPct = _perfmCumPct;
    }


    /**
     * @return the _disIndc
     */
    public java.lang.String getDisIndc() {
        return this._disIndc;
    }


    /**
     * @param _disIndc
     *            the _disIndc to set
     */
    public void setDisIndc(final java.lang.String _disIndc) {
        this._disIndc = _disIndc;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException
     *             if object is null or if any SAXException is thrown during
     *             marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     * @return the unmarshaled
     *         com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment
     */
    public static com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment unmarshal(final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment) Unmarshaller.unmarshal(
            com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }


}
