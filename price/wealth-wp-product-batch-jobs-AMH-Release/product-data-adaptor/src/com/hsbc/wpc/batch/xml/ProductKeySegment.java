/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProductKeySegment.java 1.3 2011/12/06 19:08:17CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

//---------------------------------/
//- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductKeySegment.
 *
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:17CST $
 */
public class ProductKeySegment implements java.io.Serializable {


    //--------------------------/
    //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ctryRecCde.
     */
    private java.lang.String _ctryRecCde;

    /**
     * Field _grpMembrRecCde.
     */
    private java.lang.String _grpMembrRecCde;

    /**
     * Field _prodTypeCde.
     */
    private java.lang.String _prodTypeCde;

    /**
     * Field _prodCde.
     */
    private java.lang.String _prodCde;

    /**
     * Field __prodCdeAltClassCde.
     */
    private java.lang.String _prodCdeAltClassCde;


    //----------------/
    //- Constructors -/
    //----------------/

    public ProductKeySegment() {
        super();
    }


    //-----------/
    //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ctryRecCde'.
     *
     * @return the value of field 'CtryRecCde'.
     */
    public java.lang.String getCtryRecCde(
    ) {
        return this._ctryRecCde;
    }

    /**
     * Returns the value of field 'grpMembrRecCde'.
     *
     * @return the value of field 'GrpMembrRecCde'.
     */
    public java.lang.String getGrpMembrRecCde(
    ) {
        return this._grpMembrRecCde;
    }

    /**
     * Returns the value of field 'prodCde'.
     *
     * @return the value of field 'ProdCde'.
     */
    public java.lang.String getProdCde(
    ) {
        return this._prodCde;
    }

    /**
     * Returns the value of field 'prodTypeCde'.
     *
     * @return the value of field 'ProdTypeCde'.
     */
    public java.lang.String getProdTypeCde(
    ) {
        return this._prodTypeCde;
    }

    /**
     * Returns the value of field 'prodCdeAltClassCde'.
     *
     * @return the value of field 'ProdCdeAltClassCde'.
     */
    public java.lang.String getProdCdeAltClassCde(
    ) {
        return this._prodCdeAltClassCde;
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
     * Sets the value of field 'ctryRecCde'.
     *
     * @param ctryRecCde the value of field 'ctryRecCde'.
     */
    public void setCtryRecCde(
            final java.lang.String ctryRecCde) {
        this._ctryRecCde = ctryRecCde;
    }

    /**
     * Sets the value of field 'grpMembrRecCde'.
     *
     * @param grpMembrRecCde the value of field 'grpMembrRecCde'.
     */
    public void setGrpMembrRecCde(
            final java.lang.String grpMembrRecCde) {
        this._grpMembrRecCde = grpMembrRecCde;
    }

    /**
     * Sets the value of field 'prodCde'.
     *
     * @param prodCde the value of field 'prodCde'.
     */
    public void setProdCde(
            final java.lang.String prodCde) {
        this._prodCde = prodCde;
    }

    /**
     * Sets the value of field 'prodTypeCde'.
     *
     * @param prodTypeCde the value of field 'prodTypeCde'.
     */
    public void setProdTypeCde(
            final java.lang.String prodTypeCde) {
        this._prodTypeCde = prodTypeCde;
    }

    /**
     * Sets the value of field 'prodCdeAltClassCde'.
     *
     * @param prodCdeAltClassCde the value of field 'prodCdeAltClassCde'.
     */
    public void setProdCdeAltClassCde(
            final java.lang.String prodCdeAltClassCde) {
        this._prodCdeAltClassCde = prodCdeAltClassCde;
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
     * com.dummy.wpc.batch.xml.ProductKeySegment
     */
    public static com.dummy.wpc.batch.xml.ProductKeySegment unmarshal(
            final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProductKeySegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProductKeySegment.class, reader);
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
