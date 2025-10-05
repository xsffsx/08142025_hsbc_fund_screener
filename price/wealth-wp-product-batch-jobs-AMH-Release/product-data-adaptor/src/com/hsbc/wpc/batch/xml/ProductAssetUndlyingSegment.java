/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProductAssetUndlyingSegment.java 1.3 2011/12/06 19:08:13CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductAssetUndlyingSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:13CST $
 */
public class ProductAssetUndlyingSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _asetUndlCdeSeqNum.
     */
    private java.lang.String _asetUndlCdeSeqNum;

    /**
     * Field _asetUndlCde.
     */
    private java.lang.String _asetUndlCde;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductAssetUndlyingSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'asetUndlCde'.
     * 
     * @return the value of field 'AsetUndlCde'.
     */
    public java.lang.String getAsetUndlCde(
    ) {
        return this._asetUndlCde;
    }

    /**
     * Returns the value of field 'asetUndlCdeSeqNum'.
     * 
     * @return the value of field 'AsetUndlCdeSeqNum'.
     */
    public java.lang.String getAsetUndlCdeSeqNum(
    ) {
        return this._asetUndlCdeSeqNum;
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
     * Sets the value of field 'asetUndlCde'.
     * 
     * @param asetUndlCde the value of field 'asetUndlCde'.
     */
    public void setAsetUndlCde(
            final java.lang.String asetUndlCde) {
        this._asetUndlCde = asetUndlCde;
    }

    /**
     * Sets the value of field 'asetUndlCdeSeqNum'.
     * 
     * @param asetUndlCdeSeqNum the value of field
     * 'asetUndlCdeSeqNum'.
     */
    public void setAsetUndlCdeSeqNum(
            final java.lang.String asetUndlCdeSeqNum) {
        this._asetUndlCdeSeqNum = asetUndlCdeSeqNum;
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
     * com.dummy.wpc.batch.xml.ProductAssetUndlyingSegment
     */
    public static com.dummy.wpc.batch.xml.ProductAssetUndlyingSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProductAssetUndlyingSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProductAssetUndlyingSegment.class, reader);
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
