/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/WarrLst.java 1.3 2011/12/06 19:07:53CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class WarrLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:53CST $
 */
public class WarrLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _warrList.
     */
    private java.util.Vector _warrList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WarrLst() {
        super();
        this._warrList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vWarr
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addWarr(
            final com.dummy.wpc.batch.xml.Warr vWarr)
    throws java.lang.IndexOutOfBoundsException {
        this._warrList.addElement(vWarr);
    }

    /**
     * 
     * 
     * @param index
     * @param vWarr
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addWarr(
            final int index,
            final com.dummy.wpc.batch.xml.Warr vWarr)
    throws java.lang.IndexOutOfBoundsException {
        this._warrList.add(index, vWarr);
    }

    /**
     * Method enumerateWarr.
     * 
     * @return an Enumeration over all com.dummy.wpc.batch.xml.Warr
     * elements
     */
    public java.util.Enumeration enumerateWarr(
    ) {
        return this._warrList.elements();
    }

    /**
     * Method getWarr.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.Warr at the
     * given index
     */
    public com.dummy.wpc.batch.xml.Warr getWarr(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._warrList.size()) {
            throw new IndexOutOfBoundsException("getWarr: Index value '" + index + "' not in range [0.." + (this._warrList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.Warr) _warrList.get(index);
    }

    /**
     * Method getWarr.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.Warr[] getWarr(
    ) {
        com.dummy.wpc.batch.xml.Warr[] array = new com.dummy.wpc.batch.xml.Warr[0];
        return (com.dummy.wpc.batch.xml.Warr[]) this._warrList.toArray(array);
    }

    /**
     * Method getWarrCount.
     * 
     * @return the size of this collection
     */
    public int getWarrCount(
    ) {
        return this._warrList.size();
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
    public void removeAllWarr(
    ) {
        this._warrList.clear();
    }

    /**
     * Method removeWarr.
     * 
     * @param vWarr
     * @return true if the object was removed from the collection.
     */
    public boolean removeWarr(
            final com.dummy.wpc.batch.xml.Warr vWarr) {
        boolean removed = _warrList.remove(vWarr);
        return removed;
    }

    /**
     * Method removeWarrAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.Warr removeWarrAt(
            final int index) {
        java.lang.Object obj = this._warrList.remove(index);
        return (com.dummy.wpc.batch.xml.Warr) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vWarr
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setWarr(
            final int index,
            final com.dummy.wpc.batch.xml.Warr vWarr)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._warrList.size()) {
            throw new IndexOutOfBoundsException("setWarr: Index value '" + index + "' not in range [0.." + (this._warrList.size() - 1) + "]");
        }
        
        this._warrList.set(index, vWarr);
    }

    /**
     * 
     * 
     * @param vWarrArray
     */
    public void setWarr(
            final com.dummy.wpc.batch.xml.Warr[] vWarrArray) {
        //-- copy array
        _warrList.clear();
        
        for (int i = 0; i < vWarrArray.length; i++) {
                this._warrList.add(vWarrArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.WarrLst
     */
    public static com.dummy.wpc.batch.xml.WarrLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.WarrLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.WarrLst.class, reader);
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
