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
 * Class ProdProdRelSegment.
 * 
 * @version $Revision$ $Date$
 */
public class ProdProdRelSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodAltNumRel.
     */
    private java.lang.String _prodAltNumRel;

    /**
     * Field _prodProdRelCde.
     */
    private java.lang.String _prodProdRelCde;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProdProdRelSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'prodAltNumRel'.
     * 
     * @return the value of field 'ProdAltNumRel'.
     */
    public java.lang.String getProdAltNumRel(
    ) {
        return this._prodAltNumRel;
    }

    /**
     * Returns the value of field 'prodProdRelCde'.
     * 
     * @return the value of field 'ProdProdRelCde'.
     */
    public java.lang.String getProdProdRelCde(
    ) {
        return this._prodProdRelCde;
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
     * Sets the value of field 'prodAltNumRel'.
     * 
     * @param prodAltNumRel the value of field 'prodAltNumRel'.
     */
    public void setProdAltNumRel(
            final java.lang.String prodAltNumRel) {
        this._prodAltNumRel = prodAltNumRel;
    }

    /**
     * Sets the value of field 'prodProdRelCde'.
     * 
     * @param prodProdRelCde the value of field 'prodProdRelCde'.
     */
    public void setProdProdRelCde(
            final java.lang.String prodProdRelCde) {
        this._prodProdRelCde = prodProdRelCde;
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
     * com.dummy.wpc.batch.xml.ProdProdRelSegment
     */
    public static com.dummy.wpc.batch.xml.ProdProdRelSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdProdRelSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdProdRelSegment.class, reader);
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
