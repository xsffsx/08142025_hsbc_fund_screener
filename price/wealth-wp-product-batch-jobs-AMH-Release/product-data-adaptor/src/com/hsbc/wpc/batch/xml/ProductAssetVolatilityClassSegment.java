/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProductAssetVolatilityClassSegment.java 1.1 2012/04/13 16:23:30CST 43588220 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductAssetVolatilityClassSegment.
 * 
 * @version $Revision: 1.1 $ $Date: 2012/04/13 16:23:30CST $
 */
public class ProductAssetVolatilityClassSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _asetVoltlClassCde.
     */
    private java.lang.String _asetVoltlClassCde;

    /**
     * Field _asetVoltlClassWghtPct.
     */
    private java.lang.String _asetVoltlClassWghtPct;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductAssetVolatilityClassSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'asetVoltlClassCde'.
     * 
     * @return the value of field 'AsetVoltlClassCde'.
     */
    public java.lang.String getAsetVoltlClassCde(
    ) {
        return this._asetVoltlClassCde;
    }

    /**
     * Returns the value of field 'asetVoltlClassWghtPct'.
     * 
     * @return the value of field 'AsetVoltlClassWghtPct'.
     */
    public java.lang.String getAsetVoltlClassWghtPct(
    ) {
        return this._asetVoltlClassWghtPct;
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
     * Sets the value of field 'asetVoltlClassCde'.
     * 
     * @param asetVoltlClassCde the value of field
     * 'asetVoltlClassCde'.
     */
    public void setAsetVoltlClassCde(
            final java.lang.String asetVoltlClassCde) {
        this._asetVoltlClassCde = asetVoltlClassCde;
    }

    /**
     * Sets the value of field 'asetVoltlClassWghtPct'.
     * 
     * @param asetVoltlClassWghtPct the value of field
     * 'asetVoltlClassWghtPct'.
     */
    public void setAsetVoltlClassWghtPct(
            final java.lang.String asetVoltlClassWghtPct) {
        this._asetVoltlClassWghtPct = asetVoltlClassWghtPct;
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
     * com.dummy.wpc.batch.xml.ProductAssetVolatilityClassSegment
     */
    public static com.dummy.wpc.batch.xml.ProductAssetVolatilityClassSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProductAssetVolatilityClassSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProductAssetVolatilityClassSegment.class, reader);
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
