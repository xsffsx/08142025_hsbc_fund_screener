/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ChanlAttrSegment.
 * 
 * @version $Revision$ $Date$
 */
public class ChanlAttrSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _chanlCde.
     */
    private java.lang.String _chanlCde;

    /**
     * Field _fieldCde.
     */
    private java.lang.String _fieldCde;

    /**
     * Field _fieldValue.
     */
    private java.lang.String _fieldValue;


      //----------------/
     //- Constructors -/
    //----------------/

    public ChanlAttrSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'chanlCde'.
     * 
     * @return the value of field 'ChanlCde'.
     */
    public java.lang.String getChanlCde(
    ) {
        return this._chanlCde;
    }

    /**
     * Returns the value of field 'fieldCde'.
     * 
     * @return the value of field 'FieldCde'.
     */
    public java.lang.String getFieldCde(
    ) {
        return this._fieldCde;
    }

    /**
     * Returns the value of field 'fieldValue'.
     * 
     * @return the value of field 'FieldValue'.
     */
    public java.lang.String getFieldValue(
    ) {
        return this._fieldValue;
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
     * Sets the value of field 'chanlCde'.
     * 
     * @param chanlCde the value of field 'chanlCde'.
     */
    public void setChanlCde(
            final java.lang.String chanlCde) {
        this._chanlCde = chanlCde;
    }

    /**
     * Sets the value of field 'fieldCde'.
     * 
     * @param fieldCde the value of field 'fieldCde'.
     */
    public void setFieldCde(
            final java.lang.String fieldCde) {
        this._fieldCde = fieldCde;
    }

    /**
     * Sets the value of field 'fieldValue'.
     * 
     * @param fieldValue the value of field 'fieldValue'.
     */
    public void setFieldValue(
            final java.lang.String fieldValue) {
        this._fieldValue = fieldValue;
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
     * com.dummy.wpc.batch.xml.ChanlAttrSegment
     */
    public static com.dummy.wpc.batch.xml.ChanlAttrSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ChanlAttrSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ChanlAttrSegment.class, reader);
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
