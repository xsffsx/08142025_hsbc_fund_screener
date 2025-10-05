/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProductRestrictCustCountrySegment.java 1.3 2011/12/06 19:08:04CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductRestrictCustCountrySegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:04CST $
 */
public class ProductRestrictCustCountrySegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _restrCtryTypeCde.
     */
    private java.lang.String _restrCtryTypeCde;

    /**
     * Field _ctryIsoCde.
     */
    private java.lang.String _ctryIsoCde;

    /**
     * Field _restrCde.
     */
    private java.lang.String _restrCde;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductRestrictCustCountrySegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ctryIsoCde'.
     * 
     * @return the value of field 'CtryIsoCde'.
     */
    public java.lang.String getCtryIsoCde(
    ) {
        return this._ctryIsoCde;
    }

    /**
     * Returns the value of field 'restrCde'.
     * 
     * @return the value of field 'RestrCde'.
     */
    public java.lang.String getRestrCde(
    ) {
        return this._restrCde;
    }

    /**
     * Returns the value of field 'restrCtryTypeCde'.
     * 
     * @return the value of field 'RestrCtryTypeCde'.
     */
    public java.lang.String getRestrCtryTypeCde(
    ) {
        return this._restrCtryTypeCde;
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
     * Sets the value of field 'ctryIsoCde'.
     * 
     * @param ctryIsoCde the value of field 'ctryIsoCde'.
     */
    public void setCtryIsoCde(
            final java.lang.String ctryIsoCde) {
        this._ctryIsoCde = ctryIsoCde;
    }

    /**
     * Sets the value of field 'restrCde'.
     * 
     * @param restrCde the value of field 'restrCde'.
     */
    public void setRestrCde(
            final java.lang.String restrCde) {
        this._restrCde = restrCde;
    }

    /**
     * Sets the value of field 'restrCtryTypeCde'.
     * 
     * @param restrCtryTypeCde the value of field 'restrCtryTypeCde'
     */
    public void setRestrCtryTypeCde(
            final java.lang.String restrCtryTypeCde) {
        this._restrCtryTypeCde = restrCtryTypeCde;
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
     * com.dummy.wpc.batch.xml.ProductRestrictCustCountrySegment
     */
    public static com.dummy.wpc.batch.xml.ProductRestrictCustCountrySegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProductRestrictCustCountrySegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProductRestrictCustCountrySegment.class, reader);
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
