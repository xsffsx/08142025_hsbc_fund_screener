/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/AvcCharLst.java 1.1 2012/12/27 18:14:11CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AvcCharLst.
 * 
 * @version $Revision: 1.1 $ $Date: 2012/12/27 18:14:11CST $
 */
public class AvcCharLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _avcCharList.
     */
    private java.util.Vector _avcCharList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AvcCharLst() {
        super();
        this._avcCharList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAvcChar
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAvcChar(
            final com.dummy.wpc.batch.xml.AvcChar vAvcChar)
    throws java.lang.IndexOutOfBoundsException {
        this._avcCharList.addElement(vAvcChar);
    }

    /**
     * 
     * 
     * @param index
     * @param vAvcChar
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAvcChar(
            final int index,
            final com.dummy.wpc.batch.xml.AvcChar vAvcChar)
    throws java.lang.IndexOutOfBoundsException {
        this._avcCharList.add(index, vAvcChar);
    }

    /**
     * Method enumerateAvcChar.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.AvcChar elements
     */
    public java.util.Enumeration enumerateAvcChar(
    ) {
        return this._avcCharList.elements();
    }

    /**
     * Method getAvcChar.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.AvcChar at
     * the given index
     */
    public com.dummy.wpc.batch.xml.AvcChar getAvcChar(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._avcCharList.size()) {
            throw new IndexOutOfBoundsException("getAvcChar: Index value '" + index + "' not in range [0.." + (this._avcCharList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.AvcChar) _avcCharList.get(index);
    }

    /**
     * Method getAvcChar.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.AvcChar[] getAvcChar(
    ) {
        com.dummy.wpc.batch.xml.AvcChar[] array = new com.dummy.wpc.batch.xml.AvcChar[0];
        return (com.dummy.wpc.batch.xml.AvcChar[]) this._avcCharList.toArray(array);
    }

    /**
     * Method getAvcCharCount.
     * 
     * @return the size of this collection
     */
    public int getAvcCharCount(
    ) {
        return this._avcCharList.size();
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
     */
    public void removeAllAvcChar(
    ) {
        this._avcCharList.clear();
    }

    /**
     * Method removeAvcChar.
     * 
     * @param vAvcChar
     * @return true if the object was removed from the collection.
     */
    public boolean removeAvcChar(
            final com.dummy.wpc.batch.xml.AvcChar vAvcChar) {
        boolean removed = _avcCharList.remove(vAvcChar);
        return removed;
    }

    /**
     * Method removeAvcCharAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.AvcChar removeAvcCharAt(
            final int index) {
        java.lang.Object obj = this._avcCharList.remove(index);
        return (com.dummy.wpc.batch.xml.AvcChar) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vAvcChar
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAvcChar(
            final int index,
            final com.dummy.wpc.batch.xml.AvcChar vAvcChar)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._avcCharList.size()) {
            throw new IndexOutOfBoundsException("setAvcChar: Index value '" + index + "' not in range [0.." + (this._avcCharList.size() - 1) + "]");
        }
        
        this._avcCharList.set(index, vAvcChar);
    }

    /**
     * 
     * 
     * @param vAvcCharArray
     */
    public void setAvcChar(
            final com.dummy.wpc.batch.xml.AvcChar[] vAvcCharArray) {
        //-- copy array
        _avcCharList.clear();
        
        for (int i = 0; i < vAvcCharArray.length; i++) {
                this._avcCharList.add(vAvcCharArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.AvcCharLst
     */
    public static com.dummy.wpc.batch.xml.AvcCharLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.AvcCharLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.AvcCharLst.class, reader);
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
