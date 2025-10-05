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
 * Class SectionsType.
 * 
 * @version $Revision$ $Date$
 */
public class SectionsType implements java.io.Serializable {


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

    public SectionsType() {
        super();
        this._items = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSectionsTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSectionsTypeItem(
            final com.dummy.wpc.batch.extSP.SectionsTypeItem vSectionsTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.addElement(vSectionsTypeItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vSectionsTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSectionsTypeItem(
            final int index,
            final com.dummy.wpc.batch.extSP.SectionsTypeItem vSectionsTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vSectionsTypeItem);
    }

    /**
     * Method enumerateSectionsTypeItem.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.extSP.SectionsTypeItem elements
     */
    public java.util.Enumeration enumerateSectionsTypeItem(
    ) {
        return this._items.elements();
    }

    /**
     * Method getSectionsTypeItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.extSP.SectionsTypeItem at the given index
     */
    public com.dummy.wpc.batch.extSP.SectionsTypeItem getSectionsTypeItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getSectionsTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.extSP.SectionsTypeItem) _items.get(index);
    }

    /**
     * Method getSectionsTypeItem.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.extSP.SectionsTypeItem[] getSectionsTypeItem(
    ) {
        com.dummy.wpc.batch.extSP.SectionsTypeItem[] array = new com.dummy.wpc.batch.extSP.SectionsTypeItem[0];
        return (com.dummy.wpc.batch.extSP.SectionsTypeItem[]) this._items.toArray(array);
    }

    /**
     * Method getSectionsTypeItemCount.
     * 
     * @return the size of this collection
     */
    public int getSectionsTypeItemCount(
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
    public void removeAllSectionsTypeItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeSectionsTypeItem.
     * 
     * @param vSectionsTypeItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeSectionsTypeItem(
            final com.dummy.wpc.batch.extSP.SectionsTypeItem vSectionsTypeItem) {
        boolean removed = _items.remove(vSectionsTypeItem);
        return removed;
    }

    /**
     * Method removeSectionsTypeItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.extSP.SectionsTypeItem removeSectionsTypeItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (com.dummy.wpc.batch.extSP.SectionsTypeItem) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vSectionsTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSectionsTypeItem(
            final int index,
            final com.dummy.wpc.batch.extSP.SectionsTypeItem vSectionsTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setSectionsTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        this._items.set(index, vSectionsTypeItem);
    }

    /**
     * 
     * 
     * @param vSectionsTypeItemArray
     */
    public void setSectionsTypeItem(
            final com.dummy.wpc.batch.extSP.SectionsTypeItem[] vSectionsTypeItemArray) {
        //-- copy array
        _items.clear();
        
        for (int i = 0; i < vSectionsTypeItemArray.length; i++) {
                this._items.add(vSectionsTypeItemArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.extSP.SectionsType
     */
    public static com.dummy.wpc.batch.extSP.SectionsType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.extSP.SectionsType) Unmarshaller.unmarshal(com.dummy.wpc.batch.extSP.SectionsType.class, reader);
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
