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
 * Class ComplexElementType.
 * 
 * @version $Revision$ $Date$
 */
public class ComplexElementType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _key.
     */
    private java.lang.String _key;

    /**
     * Field _type.
     */
    private com.dummy.wpc.batch.extSP.types.EltTypeEnumType _type;

    /**
     * Field _items.
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComplexElementType() {
        super();
        this._items = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vComplexElementTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComplexElementTypeItem(
            final com.dummy.wpc.batch.extSP.ComplexElementTypeItem vComplexElementTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.addElement(vComplexElementTypeItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vComplexElementTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComplexElementTypeItem(
            final int index,
            final com.dummy.wpc.batch.extSP.ComplexElementTypeItem vComplexElementTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vComplexElementTypeItem);
    }

    /**
     * Method enumerateComplexElementTypeItem.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.extSP.ComplexElementTypeItem elements
     */
    public java.util.Enumeration enumerateComplexElementTypeItem(
    ) {
        return this._items.elements();
    }

    /**
     * Method getComplexElementTypeItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.extSP.ComplexElementTypeItem at the given
     * index
     */
    public com.dummy.wpc.batch.extSP.ComplexElementTypeItem getComplexElementTypeItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getComplexElementTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.extSP.ComplexElementTypeItem) _items.get(index);
    }

    /**
     * Method getComplexElementTypeItem.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.extSP.ComplexElementTypeItem[] getComplexElementTypeItem(
    ) {
        com.dummy.wpc.batch.extSP.ComplexElementTypeItem[] array = new com.dummy.wpc.batch.extSP.ComplexElementTypeItem[0];
        return (com.dummy.wpc.batch.extSP.ComplexElementTypeItem[]) this._items.toArray(array);
    }

    /**
     * Method getComplexElementTypeItemCount.
     * 
     * @return the size of this collection
     */
    public int getComplexElementTypeItemCount(
    ) {
        return this._items.size();
    }

    /**
     * Returns the value of field 'key'.
     * 
     * @return the value of field 'Key'.
     */
    public java.lang.String getKey(
    ) {
        return this._key;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public com.dummy.wpc.batch.extSP.types.EltTypeEnumType getType(
    ) {
        return this._type;
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
    public void removeAllComplexElementTypeItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeComplexElementTypeItem.
     * 
     * @param vComplexElementTypeItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeComplexElementTypeItem(
            final com.dummy.wpc.batch.extSP.ComplexElementTypeItem vComplexElementTypeItem) {
        boolean removed = _items.remove(vComplexElementTypeItem);
        return removed;
    }

    /**
     * Method removeComplexElementTypeItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.extSP.ComplexElementTypeItem removeComplexElementTypeItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (com.dummy.wpc.batch.extSP.ComplexElementTypeItem) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vComplexElementTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setComplexElementTypeItem(
            final int index,
            final com.dummy.wpc.batch.extSP.ComplexElementTypeItem vComplexElementTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setComplexElementTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        this._items.set(index, vComplexElementTypeItem);
    }

    /**
     * 
     * 
     * @param vComplexElementTypeItemArray
     */
    public void setComplexElementTypeItem(
            final com.dummy.wpc.batch.extSP.ComplexElementTypeItem[] vComplexElementTypeItemArray) {
        //-- copy array
        _items.clear();
        
        for (int i = 0; i < vComplexElementTypeItemArray.length; i++) {
                this._items.add(vComplexElementTypeItemArray[i]);
        }
    }

    /**
     * Sets the value of field 'key'.
     * 
     * @param key the value of field 'key'.
     */
    public void setKey(
            final java.lang.String key) {
        this._key = key;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final com.dummy.wpc.batch.extSP.types.EltTypeEnumType type) {
        this._type = type;
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
     * com.dummy.wpc.batch.extSP.ComplexElementType
     */
    public static com.dummy.wpc.batch.extSP.ComplexElementType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.extSP.ComplexElementType) Unmarshaller.unmarshal(com.dummy.wpc.batch.extSP.ComplexElementType.class, reader);
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
