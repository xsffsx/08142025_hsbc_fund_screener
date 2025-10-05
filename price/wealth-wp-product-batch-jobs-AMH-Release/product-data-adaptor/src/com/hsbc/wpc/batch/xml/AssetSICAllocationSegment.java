/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/AssetSICAllocationSegment.java 1.1 2012/12/27 18:14:11CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AssetSICAllocationSegment.
 * 
 * @version $Revision: 1.1 $ $Date: 2012/12/27 18:14:11CST $
 */
public class AssetSICAllocationSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sicCde.
     */
    private java.lang.String _sicCde;

    /**
     * Field _asetAllocWghtPct.
     */
    private java.lang.String _asetAllocWghtPct;


      //----------------/
     //- Constructors -/
    //----------------/

    public AssetSICAllocationSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'asetAllocWghtPct'.
     * 
     * @return the value of field 'AsetAllocWghtPct'.
     */
    public java.lang.String getAsetAllocWghtPct(
    ) {
        return this._asetAllocWghtPct;
    }

    /**
     * Returns the value of field 'sicCde'.
     * 
     * @return the value of field 'SicCde'.
     */
    public java.lang.String getSicCde(
    ) {
        return this._sicCde;
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
     * Sets the value of field 'asetAllocWghtPct'.
     * 
     * @param asetAllocWghtPct the value of field 'asetAllocWghtPct'
     */
    public void setAsetAllocWghtPct(
            final java.lang.String asetAllocWghtPct) {
        this._asetAllocWghtPct = asetAllocWghtPct;
    }

    /**
     * Sets the value of field 'sicCde'.
     * 
     * @param sicCde the value of field 'sicCde'.
     */
    public void setSicCde(
            final java.lang.String sicCde) {
        this._sicCde = sicCde;
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
     * com.dummy.wpc.batch.xml.AssetSICAllocationSegment
     */
    public static com.dummy.wpc.batch.xml.AssetSICAllocationSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.AssetSICAllocationSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.AssetSICAllocationSegment.class, reader);
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
