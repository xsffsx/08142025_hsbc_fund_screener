/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/CustomerCateEligListSegment.java 1.3 2011/12/06 19:07:51CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class CustomerCateEligListSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:51CST $
 */
public class CustomerCateEligListSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _cuspCatCde.
     */
    private java.lang.String _cuspCatCde;

    /**
     * Field _eligCuspCatInd.
     */
    private java.lang.String _eligCuspCatInd;


      //----------------/
     //- Constructors -/
    //----------------/

    public CustomerCateEligListSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cuspCatCde'.
     * 
     * @return the value of field 'CuspCatCde'.
     */
    public java.lang.String getCuspCatCde(
    ) {
        return this._cuspCatCde;
    }

    /**
     * Returns the value of field 'eligCuspCatInd'.
     * 
     * @return the value of field 'EligCuspCatInd'.
     */
    public java.lang.String getEligCuspCatInd(
    ) {
        return this._eligCuspCatInd;
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
     * Sets the value of field 'cuspCatCde'.
     * 
     * @param cuspCatCde the value of field 'cuspCatCde'.
     */
    public void setCuspCatCde(
            final java.lang.String cuspCatCde) {
        this._cuspCatCde = cuspCatCde;
    }

    /**
     * Sets the value of field 'eligCuspCatInd'.
     * 
     * @param eligCuspCatInd the value of field 'eligCuspCatInd'.
     */
    public void setEligCuspCatInd(
            final java.lang.String eligCuspCatInd) {
        this._eligCuspCatInd = eligCuspCatInd;
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
     * com.dummy.wpc.batch.xml.CustomerCateEligListSegment
     */
    public static com.dummy.wpc.batch.xml.CustomerCateEligListSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.CustomerCateEligListSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.CustomerCateEligListSegment.class, reader);
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
