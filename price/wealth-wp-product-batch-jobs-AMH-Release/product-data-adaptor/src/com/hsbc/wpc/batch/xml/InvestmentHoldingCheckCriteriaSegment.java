/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/InvestmentHoldingCheckCriteriaSegment.java 1.3 2011/12/06 19:07:52CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class InvestmentHoldingCheckCriteriaSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:52CST $
 */
public class InvestmentHoldingCheckCriteriaSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fieldTypeCde.
     */
    private java.lang.String _fieldTypeCde;

    /**
     * Field _fieldCde.
     */
    private java.lang.String _fieldCde;

    /**
     * Field _fieldValueText.
     */
    private java.lang.String _fieldValueText;


      //----------------/
     //- Constructors -/
    //----------------/

    public InvestmentHoldingCheckCriteriaSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'fieldTypeCde'.
     * 
     * @return the value of field 'FieldTypeCde'.
     */
    public java.lang.String getFieldTypeCde(
    ) {
        return this._fieldTypeCde;
    }

    /**
     * Returns the value of field 'fieldValueText'.
     * 
     * @return the value of field 'FieldValueText'.
     */
    public java.lang.String getFieldValueText(
    ) {
        return this._fieldValueText;
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
     * Sets the value of field 'fieldCde'.
     * 
     * @param fieldCde the value of field 'fieldCde'.
     */
    public void setFieldCde(
            final java.lang.String fieldCde) {
        this._fieldCde = fieldCde;
    }

    /**
     * Sets the value of field 'fieldTypeCde'.
     * 
     * @param fieldTypeCde the value of field 'fieldTypeCde'.
     */
    public void setFieldTypeCde(
            final java.lang.String fieldTypeCde) {
        this._fieldTypeCde = fieldTypeCde;
    }

    /**
     * Sets the value of field 'fieldValueText'.
     * 
     * @param fieldValueText the value of field 'fieldValueText'.
     */
    public void setFieldValueText(
            final java.lang.String fieldValueText) {
        this._fieldValueText = fieldValueText;
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
     * com.dummy.wpc.batch.xml.InvestmentHoldingCheckCriteriaSegment
     */
    public static com.dummy.wpc.batch.xml.InvestmentHoldingCheckCriteriaSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.InvestmentHoldingCheckCriteriaSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.InvestmentHoldingCheckCriteriaSegment.class, reader);
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
