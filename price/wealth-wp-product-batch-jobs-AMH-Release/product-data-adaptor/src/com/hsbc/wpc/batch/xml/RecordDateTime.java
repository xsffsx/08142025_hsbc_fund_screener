/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/RecordDateTime.java 1.3 2011/12/06 19:08:21CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class RecordDateTime.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:21CST $
 */
public class RecordDateTime implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _recCreatDtTm.
     */
    private java.lang.String _recCreatDtTm;

    /**
     * Field _recUpdtDtTm.
     */
    private java.lang.String _recUpdtDtTm;

    /**
     * Field _prodPrcUpdtDtTm.
     */
    private java.lang.String _prodPrcUpdtDtTm;

    /**
     * Field _recOnlnUpdtDtTm.
     */
    private java.lang.String _recOnlnUpdtDtTm;

    /**
     * Field _prodStatUpdtDtTm.
     */
    private java.lang.String _prodStatUpdtDtTm;

    /**
     * Field _timeZone.
     */
    private java.lang.String _timeZone;


      //----------------/
     //- Constructors -/
    //----------------/

    public RecordDateTime() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'prodPrcUpdtDtTm'.
     * 
     * @return the value of field 'ProdPrcUpdtDtTm'.
     */
    public java.lang.String getProdPrcUpdtDtTm(
    ) {
        return this._prodPrcUpdtDtTm;
    }

    /**
     * Returns the value of field 'prodStatUpdtDtTm'.
     * 
     * @return the value of field 'ProdStatUpdtDtTm'.
     */
    public java.lang.String getProdStatUpdtDtTm(
    ) {
        return this._prodStatUpdtDtTm;
    }

    /**
     * Returns the value of field 'recCreatDtTm'.
     * 
     * @return the value of field 'RecCreatDtTm'.
     */
    public java.lang.String getRecCreatDtTm(
    ) {
        return this._recCreatDtTm;
    }

    /**
     * Returns the value of field 'recOnlnUpdtDtTm'.
     * 
     * @return the value of field 'RecOnlnUpdtDtTm'.
     */
    public java.lang.String getRecOnlnUpdtDtTm(
    ) {
        return this._recOnlnUpdtDtTm;
    }

    /**
     * Returns the value of field 'recUpdtDtTm'.
     * 
     * @return the value of field 'RecUpdtDtTm'.
     */
    public java.lang.String getRecUpdtDtTm(
    ) {
        return this._recUpdtDtTm;
    }

    /**
     * Returns the value of field 'timeZone'.
     * 
     * @return the value of field 'TimeZone'.
     */
    public java.lang.String getTimeZone(
    ) {
        return this._timeZone;
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
     * Sets the value of field 'prodPrcUpdtDtTm'.
     * 
     * @param prodPrcUpdtDtTm the value of field 'prodPrcUpdtDtTm'.
     */
    public void setProdPrcUpdtDtTm(
            final java.lang.String prodPrcUpdtDtTm) {
        this._prodPrcUpdtDtTm = prodPrcUpdtDtTm;
    }

    /**
     * Sets the value of field 'prodStatUpdtDtTm'.
     * 
     * @param prodStatUpdtDtTm the value of field 'prodStatUpdtDtTm'
     */
    public void setProdStatUpdtDtTm(
            final java.lang.String prodStatUpdtDtTm) {
        this._prodStatUpdtDtTm = prodStatUpdtDtTm;
    }

    /**
     * Sets the value of field 'recCreatDtTm'.
     * 
     * @param recCreatDtTm the value of field 'recCreatDtTm'.
     */
    public void setRecCreatDtTm(
            final java.lang.String recCreatDtTm) {
        this._recCreatDtTm = recCreatDtTm;
    }

    /**
     * Sets the value of field 'recOnlnUpdtDtTm'.
     * 
     * @param recOnlnUpdtDtTm the value of field 'recOnlnUpdtDtTm'.
     */
    public void setRecOnlnUpdtDtTm(
            final java.lang.String recOnlnUpdtDtTm) {
        this._recOnlnUpdtDtTm = recOnlnUpdtDtTm;
    }

    /**
     * Sets the value of field 'recUpdtDtTm'.
     * 
     * @param recUpdtDtTm the value of field 'recUpdtDtTm'.
     */
    public void setRecUpdtDtTm(
            final java.lang.String recUpdtDtTm) {
        this._recUpdtDtTm = recUpdtDtTm;
    }

    /**
     * Sets the value of field 'timeZone'.
     * 
     * @param timeZone the value of field 'timeZone'.
     */
    public void setTimeZone(
            final java.lang.String timeZone) {
        this._timeZone = timeZone;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.RecordDateTime
     */
    public static com.dummy.wpc.batch.xml.RecordDateTime unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.RecordDateTime) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.RecordDateTime.class, reader);
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
