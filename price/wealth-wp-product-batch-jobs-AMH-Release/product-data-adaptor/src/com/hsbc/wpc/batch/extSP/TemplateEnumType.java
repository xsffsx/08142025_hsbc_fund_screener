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
 * Class TemplateEnumType.
 * 
 * @version $Revision$ $Date$
 */
public class TemplateEnumType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _items.
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public TemplateEnumType() {
        super();
        this._items = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vTemplateEnumTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTemplateEnumTypeItem(
            final com.dummy.wpc.batch.extSP.TemplateEnumTypeItem vTemplateEnumTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.addElement(vTemplateEnumTypeItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vTemplateEnumTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTemplateEnumTypeItem(
            final int index,
            final com.dummy.wpc.batch.extSP.TemplateEnumTypeItem vTemplateEnumTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vTemplateEnumTypeItem);
    }

    /**
     * Method enumerateTemplateEnumTypeItem.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.extSP.TemplateEnumTypeItem elements
     */
    public java.util.Enumeration enumerateTemplateEnumTypeItem(
    ) {
        return this._items.elements();
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Method getTemplateEnumTypeItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.extSP.TemplateEnumTypeItem at the given
     * index
     */
    public com.dummy.wpc.batch.extSP.TemplateEnumTypeItem getTemplateEnumTypeItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getTemplateEnumTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.extSP.TemplateEnumTypeItem) _items.get(index);
    }

    /**
     * Method getTemplateEnumTypeItem.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.extSP.TemplateEnumTypeItem[] getTemplateEnumTypeItem(
    ) {
        com.dummy.wpc.batch.extSP.TemplateEnumTypeItem[] array = new com.dummy.wpc.batch.extSP.TemplateEnumTypeItem[0];
        return (com.dummy.wpc.batch.extSP.TemplateEnumTypeItem[]) this._items.toArray(array);
    }

    /**
     * Method getTemplateEnumTypeItemCount.
     * 
     * @return the size of this collection
     */
    public int getTemplateEnumTypeItemCount(
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
    public void removeAllTemplateEnumTypeItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeTemplateEnumTypeItem.
     * 
     * @param vTemplateEnumTypeItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeTemplateEnumTypeItem(
            final com.dummy.wpc.batch.extSP.TemplateEnumTypeItem vTemplateEnumTypeItem) {
        boolean removed = _items.remove(vTemplateEnumTypeItem);
        return removed;
    }

    /**
     * Method removeTemplateEnumTypeItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.extSP.TemplateEnumTypeItem removeTemplateEnumTypeItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (com.dummy.wpc.batch.extSP.TemplateEnumTypeItem) obj;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * 
     * 
     * @param index
     * @param vTemplateEnumTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setTemplateEnumTypeItem(
            final int index,
            final com.dummy.wpc.batch.extSP.TemplateEnumTypeItem vTemplateEnumTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setTemplateEnumTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        this._items.set(index, vTemplateEnumTypeItem);
    }

    /**
     * 
     * 
     * @param vTemplateEnumTypeItemArray
     */
    public void setTemplateEnumTypeItem(
            final com.dummy.wpc.batch.extSP.TemplateEnumTypeItem[] vTemplateEnumTypeItemArray) {
        //-- copy array
        _items.clear();
        
        for (int i = 0; i < vTemplateEnumTypeItemArray.length; i++) {
                this._items.add(vTemplateEnumTypeItemArray[i]);
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
     * com.dummy.wpc.batch.extSP.TemplateEnumType
     */
    public static com.dummy.wpc.batch.extSP.TemplateEnumType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.extSP.TemplateEnumType) Unmarshaller.unmarshal(com.dummy.wpc.batch.extSP.TemplateEnumType.class, reader);
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
