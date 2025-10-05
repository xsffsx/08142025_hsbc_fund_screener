/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProductAltNumberSegment.java 1.3 2011/12/06 19:08:06CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductAltNumberSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:06CST $
 */
public class ProductAltNumberSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodCdeAltClassCde.
     */
    private java.lang.String _prodCdeAltClassCde;

    /**
     * Field _prodAltNum.
     */
    private java.lang.String _prodAltNum;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductAltNumberSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'prodAltNum'.
     * 
     * @return the value of field 'ProdAltNum'.
     */
    public java.lang.String getProdAltNum(
    ) {
        return this._prodAltNum;
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
     * Sets the value of field 'prodAltNum'.
     * 
     * @param prodAltNum the value of field 'prodAltNum'.
     */
    public void setProdAltNum(
            final java.lang.String prodAltNum) {
        this._prodAltNum = prodAltNum;
    }

    /**
     * Sets the value of field 'prodCdeAltClassCde'.
     * 
     * @param prodCdeAltClassCde the value of field
     * 'prodCdeAltClassCde'.
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
     * com.dummy.wpc.batch.xml.ProductAltNumberSegment
     */
    public static com.dummy.wpc.batch.xml.ProductAltNumberSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProductAltNumberSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProductAltNumberSegment.class, reader);
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
