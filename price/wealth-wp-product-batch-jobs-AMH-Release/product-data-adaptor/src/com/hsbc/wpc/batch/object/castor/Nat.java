/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/object/castor/Nat.java 1.1 2011/06/15 16:20:02CST Perry Guo (WMDHKG0007) Development  $
 */

package com.dummy.wpc.batch.object.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Nat.
 * 
 * @version $Revision: 1.1 $ $Date: 2011/06/15 16:20:02CST $
 */
public class Nat implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _cde
     */
    private java.lang.String _cde;

    /**
     * Field _ind
     */
    private java.lang.String _ind;


      //----------------/
     //- Constructors -/
    //----------------/

    public Nat() 
     {
        super();
    } //-- com.dummy.hfi.batch.object.castor.Nat()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cde'.
     * 
     * @return String
     * @return the value of field 'cde'.
     */
    public java.lang.String getCde()
    {
        return this._cde;
    } //-- java.lang.String getCde() 

    /**
     * Returns the value of field 'ind'.
     * 
     * @return String
     * @return the value of field 'ind'.
     */
    public java.lang.String getInd()
    {
        return this._ind;
    } //-- java.lang.String getInd() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'cde'.
     * 
     * @param cde the value of field 'cde'.
     */
    public void setCde(java.lang.String cde)
    {
        this._cde = cde;
    } //-- void setCde(java.lang.String) 

    /**
     * Sets the value of field 'ind'.
     * 
     * @param ind the value of field 'ind'.
     */
    public void setInd(java.lang.String ind)
    {
        this._ind = ind;
    } //-- void setInd(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Nat
     */
    public static com.dummy.wpc.batch.object.castor.Nat unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.dummy.wpc.batch.object.castor.Nat) Unmarshaller.unmarshal(com.dummy.wpc.batch.object.castor.Nat.class, reader);
    } //-- com.dummy.hfi.batch.object.castor.Nat unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
