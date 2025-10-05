/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.dummy.wpc.batch.extSP;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SolvingElementType.
 * 
 * @version $Revision$ $Date$
 */
public class SolvingElementType extends com.dummy.wpc.batch.extSP.RangeElementType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _multi.
     */
    private boolean _multi;

    /**
     * keeps track of state for field: _multi
     */
    private boolean _has_multi;

    /**
     * Field _precision.
     */
    private java.math.BigDecimal _precision;


      //----------------/
     //- Constructors -/
    //----------------/

    public SolvingElementType() {
        super();
        setContent("");
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteMulti(
    ) {
        this._has_multi= false;
    }

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public java.lang.String getContent(
    ) {
        return this._content;
    }

    /**
     * Returns the value of field 'multi'.
     * 
     * @return the value of field 'Multi'.
     */
    public boolean getMulti(
    ) {
        return this._multi;
    }

    /**
     * Returns the value of field 'precision'.
     * 
     * @return the value of field 'Precision'.
     */
    public java.math.BigDecimal getPrecision(
    ) {
        return this._precision;
    }

    /**
     * Method hasMulti.
     * 
     * @return true if at least one Multi has been added
     */
    public boolean hasMulti(
    ) {
        return this._has_multi;
    }

    /**
     * Returns the value of field 'multi'.
     * 
     * @return the value of field 'Multi'.
     */
    public boolean isMulti(
    ) {
        return this._multi;
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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(
            final java.lang.String content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'multi'.
     * 
     * @param multi the value of field 'multi'.
     */
    public void setMulti(
            final boolean multi) {
        this._multi = multi;
        this._has_multi = true;
    }

    /**
     * Sets the value of field 'precision'.
     * 
     * @param precision the value of field 'precision'.
     */
    public void setPrecision(
            final java.math.BigDecimal precision) {
        this._precision = precision;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.extSP.ElementType
     */
    public static com.dummy.wpc.batch.extSP.ElementType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.extSP.ElementType) Unmarshaller.unmarshal(com.dummy.wpc.batch.extSP.SolvingElementType.class, reader);
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
