/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/EliLst.java 1.3 2011/12/06 19:07:53CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class EliLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:53CST $
 */
public class EliLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _eliList.
     */
    private java.util.Vector _eliList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EliLst() {
        super();
        this._eliList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vEli
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEli(
            final com.dummy.wpc.batch.xml.Eli vEli)
    throws java.lang.IndexOutOfBoundsException {
        this._eliList.addElement(vEli);
    }

    /**
     * 
     * 
     * @param index
     * @param vEli
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEli(
            final int index,
            final com.dummy.wpc.batch.xml.Eli vEli)
    throws java.lang.IndexOutOfBoundsException {
        this._eliList.add(index, vEli);
    }

    /**
     * Method enumerateEli.
     * 
     * @return an Enumeration over all com.dummy.wpc.batch.xml.Eli
     * elements
     */
    public java.util.Enumeration enumerateEli(
    ) {
        return this._eliList.elements();
    }

    /**
     * Method getEli.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.Eli at the
     * given index
     */
    public com.dummy.wpc.batch.xml.Eli getEli(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._eliList.size()) {
            throw new IndexOutOfBoundsException("getEli: Index value '" + index + "' not in range [0.." + (this._eliList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.Eli) _eliList.get(index);
    }

    /**
     * Method getEli.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.Eli[] getEli(
    ) {
        com.dummy.wpc.batch.xml.Eli[] array = new com.dummy.wpc.batch.xml.Eli[0];
        return (com.dummy.wpc.batch.xml.Eli[]) this._eliList.toArray(array);
    }

    /**
     * Method getEliCount.
     * 
     * @return the size of this collection
     */
    public int getEliCount(
    ) {
        return this._eliList.size();
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
    public void removeAllEli(
    ) {
        this._eliList.clear();
    }

    /**
     * Method removeEli.
     * 
     * @param vEli
     * @return true if the object was removed from the collection.
     */
    public boolean removeEli(
            final com.dummy.wpc.batch.xml.Eli vEli) {
        boolean removed = _eliList.remove(vEli);
        return removed;
    }

    /**
     * Method removeEliAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.Eli removeEliAt(
            final int index) {
        java.lang.Object obj = this._eliList.remove(index);
        return (com.dummy.wpc.batch.xml.Eli) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vEli
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setEli(
            final int index,
            final com.dummy.wpc.batch.xml.Eli vEli)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._eliList.size()) {
            throw new IndexOutOfBoundsException("setEli: Index value '" + index + "' not in range [0.." + (this._eliList.size() - 1) + "]");
        }
        
        this._eliList.set(index, vEli);
    }

    /**
     * 
     * 
     * @param vEliArray
     */
    public void setEli(
            final com.dummy.wpc.batch.xml.Eli[] vEliArray) {
        //-- copy array
        _eliList.clear();
        
        for (int i = 0; i < vEliArray.length; i++) {
                this._eliList.add(vEliArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.EliLst
     */
    public static com.dummy.wpc.batch.xml.EliLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.EliLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.EliLst.class, reader);
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
