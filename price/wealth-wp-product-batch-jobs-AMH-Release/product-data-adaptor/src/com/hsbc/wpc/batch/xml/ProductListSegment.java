/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProductListSegment.java 1.3 2011/12/06 19:07:56CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductListSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:56CST $
 */
public class ProductListSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodListTypeCde.
     */
    private java.lang.String _prodListTypeCde;

    /**
     * Field _prodListCde.
     */
    private java.lang.String _prodListCde;

    /**
     * Field _prodEffSlstDt.
     */
    private java.lang.String _prodEffSlstDt;

    /**
     * Field _recCmntText.
     */
    private java.lang.String _recCmntText;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductListSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'prodEffSlstDt'.
     * 
     * @return the value of field 'ProdEffSlstDt'.
     */
    public java.lang.String getProdEffSlstDt(
    ) {
        return this._prodEffSlstDt;
    }

    /**
     * Returns the value of field 'prodListCde'.
     * 
     * @return the value of field 'ProdListCde'.
     */
    public java.lang.String getProdListCde(
    ) {
        return this._prodListCde;
    }

    /**
     * Returns the value of field 'prodListTypeCde'.
     * 
     * @return the value of field 'ProdListTypeCde'.
     */
    public java.lang.String getProdListTypeCde(
    ) {
        return this._prodListTypeCde;
    }

    /**
     * Returns the value of field 'recCmntText'.
     * 
     * @return the value of field 'RecCmntText'.
     */
    public java.lang.String getRecCmntText(
    ) {
        return this._recCmntText;
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
     * Sets the value of field 'prodEffSlstDt'.
     * 
     * @param prodEffSlstDt the value of field 'prodEffSlstDt'.
     */
    public void setProdEffSlstDt(
            final java.lang.String prodEffSlstDt) {
        this._prodEffSlstDt = prodEffSlstDt;
    }

    /**
     * Sets the value of field 'prodListCde'.
     * 
     * @param prodListCde the value of field 'prodListCde'.
     */
    public void setProdListCde(
            final java.lang.String prodListCde) {
        this._prodListCde = prodListCde;
    }

    /**
     * Sets the value of field 'prodListTypeCde'.
     * 
     * @param prodListTypeCde the value of field 'prodListTypeCde'.
     */
    public void setProdListTypeCde(
            final java.lang.String prodListTypeCde) {
        this._prodListTypeCde = prodListTypeCde;
    }

    /**
     * Sets the value of field 'recCmntText'.
     * 
     * @param recCmntText the value of field 'recCmntText'.
     */
    public void setRecCmntText(
            final java.lang.String recCmntText) {
        this._recCmntText = recCmntText;
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
     * com.dummy.wpc.batch.xml.ProductListSegment
     */
    public static com.dummy.wpc.batch.xml.ProductListSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProductListSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProductListSegment.class, reader);
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
