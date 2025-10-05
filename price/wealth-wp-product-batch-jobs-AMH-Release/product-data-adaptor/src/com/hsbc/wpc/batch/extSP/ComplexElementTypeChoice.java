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
 * Class ComplexElementTypeChoice.
 * 
 * @version $Revision$ $Date$
 */
public class ComplexElementTypeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items.
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComplexElementTypeChoice() {
        super();
        this._items = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vComplexElementTypeChoiceItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComplexElementTypeChoiceItem(
            final com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem vComplexElementTypeChoiceItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.addElement(vComplexElementTypeChoiceItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vComplexElementTypeChoiceItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComplexElementTypeChoiceItem(
            final int index,
            final com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem vComplexElementTypeChoiceItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vComplexElementTypeChoiceItem);
    }

    /**
     * Method enumerateComplexElementTypeChoiceItem.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem element
     */
    public java.util.Enumeration enumerateComplexElementTypeChoiceItem(
    ) {
        return this._items.elements();
    }

    /**
     * Method getComplexElementTypeChoiceItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem at the
     * given index
     */
    public com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem getComplexElementTypeChoiceItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getComplexElementTypeChoiceItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem) _items.get(index);
    }

    /**
     * Method getComplexElementTypeChoiceItem.Returns the contents
     * of the collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem[] getComplexElementTypeChoiceItem(
    ) {
        com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem[] array = new com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem[0];
        return (com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem[]) this._items.toArray(array);
    }

    /**
     * Method getComplexElementTypeChoiceItemCount.
     * 
     * @return the size of this collection
     */
    public int getComplexElementTypeChoiceItemCount(
    ) {
        return this._items.size();
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
    public void removeAllComplexElementTypeChoiceItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeComplexElementTypeChoiceItem.
     * 
     * @param vComplexElementTypeChoiceItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeComplexElementTypeChoiceItem(
            final com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem vComplexElementTypeChoiceItem) {
        boolean removed = _items.remove(vComplexElementTypeChoiceItem);
        return removed;
    }

    /**
     * Method removeComplexElementTypeChoiceItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem removeComplexElementTypeChoiceItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vComplexElementTypeChoiceItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setComplexElementTypeChoiceItem(
            final int index,
            final com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem vComplexElementTypeChoiceItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setComplexElementTypeChoiceItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        this._items.set(index, vComplexElementTypeChoiceItem);
    }

    /**
     * 
     * 
     * @param vComplexElementTypeChoiceItemArray
     */
    public void setComplexElementTypeChoiceItem(
            final com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem[] vComplexElementTypeChoiceItemArray) {
        //-- copy array
        _items.clear();
        
        for (int i = 0; i < vComplexElementTypeChoiceItemArray.length; i++) {
                this._items.add(vComplexElementTypeChoiceItemArray[i]);
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
     * @return the unmarshaled
     * com.dummy.wpc.batch.extSP.ComplexElementTypeChoice
     */
    public static com.dummy.wpc.batch.extSP.ComplexElementTypeChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.extSP.ComplexElementTypeChoice) Unmarshaller.unmarshal(com.dummy.wpc.batch.extSP.ComplexElementTypeChoice.class, reader);
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
